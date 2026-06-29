package service;

import dao.ApplicationDAO;
import dao.ClubDAO;
import dao.ClubMemberDAO;
import dao.DepartmentDAO;
import dao.UserDAO;
import model.Application;
import model.Club;
import model.ClubMember;
import model.Department;
import model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClubService {

    private final ClubDAO clubDAO;
    private final DepartmentDAO departmentDAO;
    private final ClubMemberDAO clubMemberDAO;
    private final ApplicationDAO applicationDAO;
    private final UserDAO userDAO;
    private final EmailService emailService;

    public ClubService() {
        this.clubDAO = new ClubDAO();
        this.departmentDAO = new DepartmentDAO();
        this.clubMemberDAO = new ClubMemberDAO();
        this.applicationDAO = new ApplicationDAO();
        this.userDAO = new UserDAO();
        this.emailService = new EmailService();
    }

    public boolean createClub(String name, String desc, File logoFile, String creatorUsername) {
        String logoUrl = "gallery/default_logo.png";
        if (logoFile != null && logoFile.exists()) {
            try {
                Path galleryPath = Path.of("gallery").toAbsolutePath();
                if (!Files.exists(galleryPath)) {
                    Files.createDirectories(galleryPath);
                }
                String fileName = logoFile.getName();
                String extension = "";
                int i = fileName.lastIndexOf('.');
                if (i > 0) {
                    extension = fileName.substring(i);
                }
                String newFileName = "club_" + System.currentTimeMillis() + extension;
                Path destinationPath = galleryPath.resolve(newFileName);
                Files.copy(logoFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                logoUrl = "gallery/" + newFileName;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Club club = new Club(0, name.trim(), desc.trim(), logoUrl, null, null, creatorUsername);
        return clubDAO.createClubTransaction(club, creatorUsername);
    }

    public Map<Club, String> filterClubs(String keyword, String timeSortStr) {
        List<Club> clubs = clubDAO.searchClubs(keyword == null ? "" : keyword.trim());

        if ("ASC".equalsIgnoreCase(timeSortStr)) {
            clubs.sort((c1, c2) -> c1.getCreatedAt().compareTo(c2.getCreatedAt()));
        } else {
            clubs.sort((c1, c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt()));
        }

        List<String> usernames = new ArrayList<>();
        for (Club c : clubs) {
            if (!usernames.contains(c.getCreatedBy())) {
                usernames.add(c.getCreatedBy());
            }
        }

        List<User> users = userDAO.getUsersByUsernames(usernames);
        Map<String, String> userMap = new HashMap<>();
        if (users != null) {
            for (User u : users) {
                userMap.put(u.getUsername(), u.getFullName());
            }
        }

        Map<Club, String> result = new LinkedHashMap<>();
        for (Club c : clubs) {
            c.setCreatedAt(formatToVN(c.getCreatedAt()));
            String fullName = userMap.get(c.getCreatedBy());
            result.put(c, fullName != null ? fullName : c.getCreatedBy());
        }
        return result;
    }

    public boolean createDepartment(int clubId, String name, String desc, String creatorUsername) {
        if (name == null || name.trim().isEmpty()) return false;
        Department dept = new Department(0, clubId, name.trim(), desc != null ? desc.trim() : "", "ACTIVE", null, null, creatorUsername);
        return departmentDAO.insert(dept);
    }

    public boolean updateDepartment(int deptId, String name, String desc, String dbStatus) {
        Department dept = departmentDAO.getById(deptId);
        if (dept != null) {
            dept.setName(name.trim());
            dept.setDescription(desc != null ? desc.trim() : "");
            dept.setStatus(dbStatus);
            return departmentDAO.update(dept);
        }
        return false;
    }

    public boolean deleteDepartment(int clubId, int deptId) {
        Map<Department, Integer> stats = departmentDAO.getDepartmentsWithMemberCount(clubId);
        for (Map.Entry<Department, Integer> entry : stats.entrySet()) {
            if (entry.getKey().getId() == deptId) {
                if (entry.getValue() > 0) {
                    return false;
                }
                break;
            }
        }
        return departmentDAO.delete(deptId);
    }

    public Map<Department, Integer> getDepartmentsWithStats(int clubId) {
        Map<Department, Integer> raw = departmentDAO.getDepartmentsWithMemberCount(clubId);
        Map<Department, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<Department, Integer> entry : raw.entrySet()) {
            Department d = entry.getKey();
            d.setCreatedAt(formatToVN(d.getCreatedAt()));
            result.put(d, entry.getValue());
        }
        return result;
    }

    public boolean submitApplication(int clubId, String username, Integer targetDeptId, String intro, String exp, String reason) {
        if (intro == null || intro.trim().isEmpty() || reason == null || reason.trim().isEmpty()) {
            return false;
        }
        Application app = new Application(0, clubId, username, targetDeptId != null ? targetDeptId : 0, intro.trim(), exp != null ? exp.trim() : "", reason.trim(), "PENDING", "", null, null, null);
        return applicationDAO.insert(app);
    }

    public boolean processApplication(int appId, boolean isApproved, String reviewerUsername, String reviewerNote, String dbRole) {
        Application app = applicationDAO.getById(appId);
        if (app == null) return false;
        User user = userDAO.getById(app.getUsername());
        String email = user != null ? user.getEmail() : "";
        boolean result;
        if (isApproved) {
            result = applicationDAO.approveApplicationTransaction(appId, app.getClubId(), app.getUsername(), app.getTargetDeptId(), dbRole, reviewerUsername);
        } else {
            result = applicationDAO.rejectApplication(appId, reviewerNote != null ? reviewerNote.trim() : "", reviewerUsername);
        }
        if (result && !email.isEmpty()) {
            new Thread(() -> {
                emailService.sendApplicationResult(email, isApproved);
            }).start();
        }
        return result;
    }

    public Map<Application, User> filterApplications(int clubId, String keyword, String dbStatusFilter, Integer deptIdFilter, String timeFilterEnum) {
        Map<Application, User> raw = applicationDAO.getApplicationsWithUserInfo(clubId);
        Map<Application, User> result = new LinkedHashMap<>();

        String kw = keyword == null ? "" : keyword.trim().toLowerCase();
        LocalDate now = LocalDate.now();

        for (Map.Entry<Application, User> entry : raw.entrySet()) {
            Application app = entry.getKey();
            User u = entry.getValue();

            boolean matchKw = kw.isEmpty() || (u != null && u.getFullName().toLowerCase().contains(kw)) || app.getUsername().toLowerCase().contains(kw);
            boolean matchStatus = dbStatusFilter == null || dbStatusFilter.trim().isEmpty() || dbStatusFilter.equalsIgnoreCase("ALL") || app.getStatus().equalsIgnoreCase(dbStatusFilter.trim());
            boolean matchDept = deptIdFilter == null || (deptIdFilter == 0 && app.getTargetDeptId() <= 0) || (deptIdFilter > 0 && app.getTargetDeptId() == deptIdFilter);

            boolean matchTime = true;
            if (timeFilterEnum != null && !timeFilterEnum.equalsIgnoreCase("ALL") && app.getCreatedAt() != null) {
                try {
                    LocalDate appDate = LocalDate.parse(app.getCreatedAt().substring(0, 10), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    if (timeFilterEnum.equalsIgnoreCase("THIS_MONTH") && appDate.getMonthValue() != now.getMonthValue()) matchTime = false;
                    else if (timeFilterEnum.equalsIgnoreCase("THIS_YEAR") && appDate.getYear() != now.getYear()) matchTime = false;
                } catch (Exception ignored) {
                }
            }
            if (matchKw && matchStatus && matchDept && matchTime) {
                app.setCreatedAt(formatToVN(app.getCreatedAt()));
                result.put(app, u);
            }
        }
        return result;
    }

    public boolean addMember(int clubId, String username, Integer deptId, String dbRole, String creatorUsername) {
        String todayISO = LocalDate.now().toString();
        ClubMember member = new ClubMember(clubId, username, deptId, dbRole, "ACTIVE", "", todayISO, null, null, creatorUsername);
        return clubMemberDAO.insert(member);
    }

    public boolean updateMemberProfile(int clubId, String username, Integer deptId, String dbRole, String dbStatus, String note, String updaterUsername) {
        ClubMember member = clubMemberDAO.getById(clubId, username);
        if (member == null) return false;
        member.setDeptId(deptId);
        member.setRole(dbRole);
        member.setStatus(dbStatus);
        member.setNote(note != null ? note.trim() : "");
        member.setUpdatedBy(updaterUsername);
        return clubMemberDAO.update(member);
    }

    public boolean transferLeadership(int clubId, Integer deptId, String currentLeaderUsername, String newLeaderUsername) {
        if (deptId == null) return false;
        return clubMemberDAO.transferDeptLeadership(clubId, deptId, currentLeaderUsername, newLeaderUsername);
    }

    public Map<ClubMember, User> filterMembers(int clubId, String keyword, Integer deptIdFilter, String dbRoleFilter, String dbStatusFilter) {
        Map<ClubMember, User> raw = clubMemberDAO.getMembersWithUserInfo(clubId);
        Map<ClubMember, User> result = new LinkedHashMap<>();
        String kw = keyword == null ? "" : keyword.trim().toLowerCase();

        for (Map.Entry<ClubMember, User> entry : raw.entrySet()) {
            ClubMember m = entry.getKey();
            User u = entry.getValue();

            boolean matchKw = kw.isEmpty() || m.getUsername().toLowerCase().contains(kw) || (u != null && u.getFullName().toLowerCase().contains(kw));
            boolean matchDept = deptIdFilter == null || (deptIdFilter == 0 && (m.getDeptId() == null || m.getDeptId() <= 0)) || (deptIdFilter > 0 && m.getDeptId() != null && m.getDeptId().equals(deptIdFilter));
            boolean matchRole = dbRoleFilter == null || dbRoleFilter.trim().isEmpty() || dbRoleFilter.equalsIgnoreCase("ALL") || m.getRole().equalsIgnoreCase(dbRoleFilter.trim());
            boolean matchStatus = dbStatusFilter == null || dbStatusFilter.trim().isEmpty() || dbStatusFilter.equalsIgnoreCase("ALL") || m.getStatus().equalsIgnoreCase(dbStatusFilter.trim());

            if (matchKw && matchDept && matchRole && matchStatus) {
                m.setJoinedDate(formatToVN(m.getJoinedDate()));
                result.put(m, u);
            }
        }
        return result;
    }

    public Map<String, Integer> getPersonnelStatsByDept(int clubId) {
        Map<Department, Integer> raw = departmentDAO.getDepartmentsWithMemberCount(clubId);
        Map<String, Integer> res = new LinkedHashMap<>();
        int unassigned = 0;
        List<ClubMember> members = clubMemberDAO.getAll();
        for (ClubMember m : members) {
            if (m.getClubId() == clubId && m.getStatus().equals("ACTIVE") && (m.getDeptId() == null || m.getDeptId() == 0)) {
                unassigned++;
            }
        }
        for (Map.Entry<Department, Integer> e : raw.entrySet()) {
            res.put(e.getKey().getName(), e.getValue());
        }
        if (unassigned > 0) {
            res.put("UNASSIGNED", unassigned);
        }
        return res;
    }

    public Map<String, Integer> getPersonnelStatsByStatus(int clubId) {
        List<ClubMember> members = clubMemberDAO.getAll();
        int active = 0, onLeave = 0, left = 0, banned = 0;
        for (ClubMember m : members) {
            if (m.getClubId() == clubId) {
                if (m.getStatus().equals("ACTIVE")) active++;
                else if (m.getStatus().equals("ON_LEAVE")) onLeave++;
                else if (m.getStatus().equals("BANNED")) banned++;
                else left++;
            }
        }
        Map<String, Integer> res = new LinkedHashMap<>();
        res.put("ACTIVE", active);
        res.put("ON_LEAVE", onLeave);
        res.put("LEFT", left);
        res.put("BANNED", banned);
        return res;
    }

    private String formatToVN(String isoDate) {
        if (isoDate == null || !isoDate.contains("-")) return isoDate;
        try {
            LocalDate date = LocalDate.parse(isoDate.substring(0, 10), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            return isoDate;
        }
    }
}