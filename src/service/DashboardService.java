package service;

import dao.*;
import model.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DashboardService {

    private final ClubMemberDAO clubMemberDAO;
    private final EventDAO eventDAO;
    private final TransactionDAO transactionDAO;
    private final ApplicationDAO applicationDAO;
    private final WorkDAO workDAO;
    private final TaskDAO taskDAO;
    private final AttendanceDAO attendanceDAO;
    private final UserDAO userDAO;
    private final DepartmentDAO departmentDAO;

    public DashboardService() {
        this.clubMemberDAO = new ClubMemberDAO();
        this.eventDAO = new EventDAO();
        this.transactionDAO = new TransactionDAO();
        this.applicationDAO = new ApplicationDAO();
        this.workDAO = new WorkDAO();
        this.taskDAO = new TaskDAO();
        this.attendanceDAO = new AttendanceDAO();
        this.userDAO = new UserDAO();
        this.departmentDAO = new DepartmentDAO();
    }

    public String[] getPresidentKPIs(int clubId) {
        int members = 0;
        for (ClubMember m : clubMemberDAO.getAll()) {
            if (m.getClubId() == clubId && m.getStatus().equals("ACTIVE")) members++;
        }
        int events = 0;
        for (Event e : eventDAO.getAll()) {
            if (e.getClubId() == clubId) events++;
        }
        long fund = 0;
        for (Transaction t : transactionDAO.getByClubId(clubId)) {
            if (t.getStatus().equals("VALID")) {
                if (t.getTransType().equals("INCOME")) fund += t.getAmount();
                else fund -= t.getAmount();
            }
        }
        int apps = 0;
        for (Application a : applicationDAO.getAll()) {
            if (a.getClubId() == clubId && a.getStatus().equals("PENDING")) apps++;
        }
        DecimalFormat df = new DecimalFormat("#,###đ");
        return new String[]{
                String.valueOf(members),
                String.valueOf(events),
                df.format(fund).replace(",", "."),
                String.valueOf(apps)
        };
    }

    public String[] getHeadKPIs(int clubId, int deptId) {
        int members = 0;
        for (ClubMember m : clubMemberDAO.getAll()) {
            if (m.getClubId() == clubId && m.getDeptId() != null && m.getDeptId() == deptId && m.getStatus().equals("ACTIVE")) members++;
        }
        int doing = 0, overdue = 0;
        for (Work w : workDAO.getWorksByDept(deptId)) {
            if (w.getStatus().equals("IN_PROGRESS")) doing++;
            if (w.getStatus().equals("OVERDUE")) overdue++;
        }
        int upcomingEvents = 0;
        for (Event e : eventDAO.getAll()) {
            if (e.getClubId() == clubId && e.getStatus().equals("UPCOMING")) upcomingEvents++;
        }
        return new String[]{
                String.valueOf(members),
                String.valueOf(doing),
                String.valueOf(upcomingEvents),
                String.valueOf(overdue)
        };
    }

    public String[] getMemKPIs(String username) {
        int doingTasks = 0, doneTasks = 0;
        for (Task t : taskDAO.getTasksByUsername(username)) {
            if (t.getStatus().equals("NOT_STARTED") || t.getStatus().equals("IN_PROGRESS")) doingTasks++;
            if (t.getStatus().equals("COMPLETED")) doneTasks++;
        }
        int joinedEvents = 0, present = 0, total = 0;
        for (Attendance a : attendanceDAO.getAll()) {
            if (a.getUsername().equals(username)) {
                joinedEvents++;
                if (!a.getStatus().equals("PENDING")) total++;
                if (a.getStatus().equals("PRESENT")) present++;
            }
        }
        String attRate = total == 0 ? "0%" : (int)((present * 100.0) / total) + "%";
        return new String[]{
                String.valueOf(doingTasks),
                String.valueOf(doneTasks),
                String.valueOf(joinedEvents),
                attRate
        };
    }

    public int[] getAppKPIs(int clubId) {
        int total = 0, pending = 0, approved = 0, rejected = 0;
        for (Application a : applicationDAO.getAll()) {
            if (a.getClubId() == clubId) {
                total++;
                if (a.getStatus().equals("PENDING")) pending++;
                else if (a.getStatus().equals("APPROVED")) approved++;
                else if (a.getStatus().equals("REJECTED")) rejected++;
            }
        }
        return new int[]{total, pending, approved, rejected};
    }

    public int[] getEventKPIs(int clubId) {
        int total = 0, upcoming = 0, ongoing = 0, completed = 0;
        for (Event e : eventDAO.getAll()) {
            if (e.getClubId() == clubId && !e.getStatus().equals("CANCELED")) {
                total++;
                if (e.getStatus().equals("UPCOMING")) upcoming++;
                else if (e.getStatus().equals("ONGOING")) ongoing++;
                else if (e.getStatus().equals("COMPLETED")) completed++;
            }
        }
        return new int[]{total, upcoming, ongoing, completed};
    }

    public int[] getWorkKPIs(int clubId, Integer deptId) {
        int total = 0, doing = 0, done = 0, overdue = 0;
        List<Work> works = deptId != null ? workDAO.getWorksByDept(deptId) : workDAO.getAll();
        for (Work w : works) {
            boolean belongs = deptId != null;
            if (!belongs && w.getEventId() != null) {
                Event e = eventDAO.getById(w.getEventId());
                if (e != null && e.getClubId() == clubId) belongs = true;
            }
            if (!belongs && w.getDeptId() != null) {
                Department d = departmentDAO.getById(w.getDeptId());
                if (d != null && d.getClubId() == clubId) belongs = true;
            }
            if (belongs) {
                total++;
                if (w.getStatus().equals("IN_PROGRESS")) doing++;
                else if (w.getStatus().equals("COMPLETED")) done++;
                else if (w.getStatus().equals("OVERDUE")) overdue++;
            }
        }
        return new int[]{total, doing, done, overdue};
    }

    public int[] getTaskKPIs(String username) {
        int total = 0, doing = 0, done = 0, overdue = 0;
        for (Task t : taskDAO.getTasksByUsername(username)) {
            total++;
            if (t.getStatus().equals("NOT_STARTED") || t.getStatus().equals("IN_PROGRESS")) doing++;
            else if (t.getStatus().equals("COMPLETED")) done++;
            else if (t.getStatus().equals("OVERDUE")) overdue++;
        }
        return new int[]{total, doing, done, overdue};
    }

    public Map<String, Integer> getDeptWorkStatusStats(int deptId) {
        Map<String, Integer> stats = new LinkedHashMap<>();
        int done = 0, doing = 0, notStarted = 0, overdue = 0;
        for (Work w : workDAO.getWorksByDept(deptId)) {
            String st = w.getStatus();
            if (st.equals("COMPLETED")) done++;
            else if (st.equals("IN_PROGRESS")) doing++;
            else if (st.equals("OVERDUE")) overdue++;
            else notStarted++;
        }
        stats.put("Hoàn thành", done);
        stats.put("Đang làm", doing);
        stats.put("Chưa làm", notStarted);
        stats.put("Quá hạn", overdue);
        return stats;
    }

    public Map<ClubMember, Object[]> getTopMembers(int clubId, int deptId) {
        Map<ClubMember, Object[]> result = new LinkedHashMap<>();
        List<ClubMember> members = new ArrayList<>();
        for (ClubMember m : clubMemberDAO.getAll()) {
            if (m.getClubId() == clubId && m.getDeptId() != null && m.getDeptId() == deptId && m.getStatus().equals("ACTIVE")) {
                members.add(m);
            }
        }
        for (ClubMember m : members) {
            int doing = 0, done = 0;
            for (Task t : taskDAO.getTasksByUsername(m.getUsername())) {
                if (t.getStatus().equals("COMPLETED")) done++;
                else if (!t.getStatus().equals("CANCELED")) doing++;
            }
            User u = userDAO.getById(m.getUsername());
            if (u != null) {
                result.put(m, new Object[]{u, doing, done});
            }
        }
        return result;
    }

    public Map<User, String[]> getTopMembersForClub(int clubId, int limit) {
        List<ClubMember> activeMembers = new ArrayList<>();
        for (ClubMember m : clubMemberDAO.getAll()) {
            if (m.getClubId() == clubId && m.getStatus().equals("ACTIVE")) {
                activeMembers.add(m);
            }
        }

        List<Object[]> statsList = new ArrayList<>();
        for (ClubMember m : activeMembers) {
            int doingTasks = 0;
            for (Task t : taskDAO.getTasksByUsername(m.getUsername())) {
                if (t.getStatus().equals("NOT_STARTED") || t.getStatus().equals("IN_PROGRESS")) {
                    doingTasks++;
                }
            }
            int joinedEvents = 0, present = 0, total = 0;
            for (Attendance a : attendanceDAO.getAll()) {
                if (a.getUsername().equals(m.getUsername())) {
                    joinedEvents++;
                    if (!a.getStatus().equals("PENDING")) total++;
                    if (a.getStatus().equals("PRESENT")) present++;
                }
            }
            String attRate = total == 0 ? "0%" : (int)((present * 100.0) / total) + "%";
            double rateDouble = total == 0 ? 0.0 : (double) present / total;

            double score = doingTasks + joinedEvents * 2 + rateDouble * 10;
            statsList.add(new Object[]{m, score, doingTasks, joinedEvents, attRate});
        }

        statsList.sort((a, b) -> Double.compare((double)b[ 1 ], (double)a[ 1 ]));

        Map<User, String[]> result = new LinkedHashMap<>();
        int count = 0;
        for (Object[] obj : statsList) {
            if (count >= limit) break;
            ClubMember m = (ClubMember) obj[ 0 ];
            User u = userDAO.getById(m.getUsername());
            if (u != null) {
                result.put(u, new String[]{
                        String.valueOf(obj[ 2 ]),
                        String.valueOf(obj[ 3 ]),
                        (String) obj[ 4 ]
                });
                count++;
            }
        }
        return result;
    }
}