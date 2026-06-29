/* package service;

import dao.AttendanceDAO;
import dao.ClubMemberDAO;
import dao.DepartmentDAO;
import dao.EventDAO;
import dao.TaskDAO;
import dao.UserDAO;
import dao.WorkDAO;
import model.Attendance;
import model.ClubMember;
import model.Department;
import model.Event;
import model.Task;
import model.User;
import model.Work;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WorkEventService {

    private final EventDAO eventDAO;
    private final WorkDAO workDAO;
    private final TaskDAO taskDAO;
    private final AttendanceDAO attendanceDAO;
    private final DepartmentDAO departmentDAO;
    private final UserDAO userDAO;
    private final ClubMemberDAO clubMemberDAO;

    public WorkEventService() {
        this.eventDAO = new EventDAO();
        this.workDAO = new WorkDAO();
        this.taskDAO = new TaskDAO();
        this.attendanceDAO = new AttendanceDAO();
        this.departmentDAO = new DepartmentDAO();
        this.userDAO = new UserDAO();
        this.clubMemberDAO = new ClubMemberDAO();
    }

    public boolean createEvent(int clubId, String name, String desc, String date, String time, String loc, String deptName, String creatorUsername, boolean isAllClub) {
        Integer deptId = getDeptIdByName(clubId, deptName);
        Event event = new Event(0, clubId, deptId, name.trim(), desc != null ? desc.trim() : "", parseToISO(date.trim()), time.trim(), loc.trim(), "UPCOMING", null, null, creatorUsername);
        return eventDAO.createEventWithMassAttendance(event, isAllClub);
    }

    public boolean updateEvent(int eventId, String name, String desc, String date, String time, String loc, String deptName, int clubId) {
        Event event = eventDAO.getById(eventId);
        if (event == null) return false;
        event.setName(name.trim());
        event.setDescription(desc != null ? desc.trim() : "");
        event.setEventDate(parseToISO(date.trim()));
        event.setEventTime(time.trim());
        event.setLocation(loc.trim());
        event.setDeptId(getDeptIdByName(clubId, deptName));
        return eventDAO.update(event);
    }

    public boolean cancelEvent(int eventId) {
        Event event = eventDAO.getById(eventId);
        if (event == null) return false;
        event.setStatus("CANCELED");
        return eventDAO.update(event);
    }

    public boolean joinEvent(int eventId, String username) {
        Attendance att = new Attendance(eventId, username, "PENDING", null, null, username);
        return attendanceDAO.insert(att);
    }

    public boolean cancelJoinEvent(int eventId, String username) {
        return attendanceDAO.delete(eventId, username);
    }

    public boolean updateAttendanceStatus(int eventId, String username, String uiStatus, String updaterUsername) {
        Attendance att = new Attendance(eventId, username, mapAttendanceStatusToDB(uiStatus), null, null, updaterUsername);
        return attendanceDAO.update(att);
    }

    public boolean lockAttendanceSession(int eventId, String executorUsername) {
        return attendanceDAO.lockAttendanceSession(eventId, executorUsername);
    }

    public Map<String, Double> getOverallAttendanceStats(int clubId) {
        Map<String, int[]> rawStats = attendanceDAO.getOverallAttendanceStatsPerMember(clubId);
        int totalP = 0, totalE = 0, totalA = 0;
        for (int[] arr : rawStats.values()) {
            totalP += arr[ 0 ];
            totalE += arr[ 1 ];
            totalA += arr[ 2 ];
        }
        int total = totalP + totalE + totalA;
        Map<String, Double> result = new LinkedHashMap<>();
        if (total == 0) {
            result.put("PRESENT", 0.0);
            result.put("EXCUSED", 0.0);
            result.put("ABSENT", 0.0);
        } else {
            result.put("PRESENT", (double) totalP / total * 100);
            result.put("EXCUSED", (double) totalE / total * 100);
            result.put("ABSENT", (double) totalA / total * 100);
        }
        return result;
    }

    public Map<Event, Integer> getEventsWithParticipantCount(int clubId) {
        Map<Event, Integer> raw = eventDAO.getEventsWithParticipantCount(clubId);
        Map<Event, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<Event, Integer> entry : raw.entrySet()) {
            Event e = entry.getKey();
            e.setEventDate(formatToVN(e.getEventDate()));
            result.put(e, entry.getValue());
        }
        return result;
    }

    public Map<Event, Integer> filterEvents(int clubId, String keyword, String statusFilter, String timeFilter) {
        Map<Event, Integer> raw = eventDAO.getEventsWithParticipantCount(clubId);
        Map<Event, Integer> result = new LinkedHashMap<>();
        String kw = keyword == null ? "" : keyword.trim().toLowerCase();
        String sF = statusFilter == null ? "Tất cả" : statusFilter.trim();
        String tF = timeFilter == null ? "Tất cả" : timeFilter.trim();
        LocalDate now = LocalDate.now();

        for (Map.Entry<Event, Integer> entry : raw.entrySet()) {
            Event e = entry.getKey();
            boolean matchKw = kw.isEmpty() || e.getName().toLowerCase().contains(kw);
            boolean matchStatus = sF.equals("Tất cả") || mapEventStatusToUI(e.getStatus()).equalsIgnoreCase(sF);
            boolean matchTime = true;

            if (!tF.equals("Tất cả") && e.getEventDate() != null) {
                try {
                    LocalDate date = LocalDate.parse(e.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    if (tF.equals("Tháng này") && date.getMonthValue() != now.getMonthValue()) matchTime = false;
                    else if (tF.equals("Năm nay") && date.getYear() != now.getYear()) matchTime = false;
                } catch (Exception ex) {}
            }

            if (matchKw && matchStatus && matchTime) {
                e.setEventDate(formatToVN(e.getEventDate()));
                result.put(e, entry.getValue());
            }
        }
        return result;
    }

    public Map<Integer, Integer> getMonthlyEventStats(int clubId, int year) {
        Map<Integer, Integer> stats = new LinkedHashMap<>();
        for (int i = 1; i <= 12; i++) {
            stats.put(i, 0);
        }
        List<Event> events = eventDAO.getAll();
        String yearPrefix = String.valueOf(year) + "-";
        for (Event e : events) {
            if (e.getClubId() == clubId && e.getEventDate() != null && e.getEventDate().startsWith(yearPrefix)) {
                try {
                    int month = Integer.parseInt(e.getEventDate().substring(5, 7));
                    stats.put(month, stats.get(month) + 1);
                } catch (Exception ex) {}
            }
        }
        return stats;
    }

    public boolean createWork(int clubId, String name, String desc, String eventName, String deptName, String startDate, String deadline, String uiPriority, String creatorUsername) {
        Integer eventId = getEventIdByName(clubId, eventName);
        Integer deptId = getDeptIdByName(clubId, deptName);
        Work work = new Work(0, eventId, deptId, name.trim(), desc != null ? desc.trim() : "", parseToISO(startDate.trim()), parseToISO(deadline.trim()), mapPriorityToDB(uiPriority), "NOT_STARTED", null, null, creatorUsername);
        return workDAO.insert(work);
    }

    public boolean updateWork(int workId, int clubId, String name, String desc, String eventName, String deptName, String startDate, String deadline, String uiPriority, String uiStatus) {
        Work work = workDAO.getById(workId);
        if (work == null) return false;
        work.setName(name.trim());
        work.setDescription(desc != null ? desc.trim() : "");
        work.setEventId(getEventIdByName(clubId, eventName));
        work.setDeptId(getDeptIdByName(clubId, deptName));
        work.setStartDate(parseToISO(startDate.trim()));
        work.setDeadline(parseToISO(deadline.trim()));
        work.setPriority(mapPriorityToDB(uiPriority));
        work.setStatus(mapWorkStatusToDB(uiStatus));
        return workDAO.update(work);
    }

    public boolean cancelWork(int workId) {
        Work work = workDAO.getById(workId);
        if (work == null) return false;
        work.setStatus("CANCELED");
        return workDAO.update(work);
    }

    public Map<Work, String[]> getWorksWithDetails(int deptId) {
        Map<Work, String[]> result = new LinkedHashMap<>();
        List<Work> works = workDAO.getWorksByDept(deptId);
        for (Work w : works) {
            String eventName = "Việc chung";
            if (w.getEventId() != null) {
                Event e = eventDAO.getById(w.getEventId());
                if (e != null) eventName = e.getName();
            }
            String departmentName = "Chưa phân công";
            if (w.getDeptId() != null) {
                Department d = departmentDAO.getById(w.getDeptId());
                if (d != null) departmentName = d.getName();
            }
            w.setStartDate(formatToVN(w.getStartDate()));
            w.setDeadline(formatToVN(w.getDeadline()));
            result.put(w, new String[]{eventName, departmentName});
        }
        return result;
    }

    public Map<Work, String[]> filterWorks(int clubId, String keyword, String statusFilter, String deptFilter, String timeSort) {
        List<Work> allWorks = workDAO.getAll();
        Map<Work, String[]> result = new LinkedHashMap<>();
        String kw = keyword == null ? "" : keyword.trim().toLowerCase();
        String sF = statusFilter == null ? "Tất cả" : statusFilter.trim();
        String dF = deptFilter == null ? "Tất cả" : deptFilter.trim();

        for (Work w : allWorks) {
            int wClubId = -1;
            String eventName = "Việc chung";
            if (w.getEventId() != null) {
                Event e = eventDAO.getById(w.getEventId());
                if (e != null) {
                    wClubId = e.getClubId();
                    eventName = e.getName();
                }
            }
            String deptName = "Chưa phân công";
            if (w.getDeptId() != null) {
                Department d = departmentDAO.getById(w.getDeptId());
                if (d != null) {
                    wClubId = d.getClubId();
                    deptName = d.getName();
                }
            }
            if (wClubId != clubId && wClubId != -1) continue;

            boolean matchKw = kw.isEmpty() || w.getName().toLowerCase().contains(kw);
            boolean matchStatus = sF.equals("Tất cả") || mapWorkStatusToUI(w.getStatus()).equalsIgnoreCase(sF);
            boolean matchDept = dF.equals("Tất cả") || deptName.equalsIgnoreCase(dF);

            if (matchKw && matchStatus && matchDept) {
                w.setStartDate(formatToVN(w.getStartDate()));
                w.setDeadline(formatToVN(w.getDeadline()));
                result.put(w, new String[]{eventName, deptName});
            }
        }

        List<Map.Entry<Work, String[]>> list = new ArrayList<>(result.entrySet());
        if ("Cũ nhất".equals(timeSort)) {
            list.sort((e1, e2) -> e1.getKey().getCreatedAt().compareTo(e2.getKey().getCreatedAt()));
        } else {
            list.sort((e1, e2) -> e2.getKey().getCreatedAt().compareTo(e1.getKey().getCreatedAt()));
        }

        Map<Work, String[]> sortedResult = new LinkedHashMap<>();
        for (Map.Entry<Work, String[]> entry : list) {
            sortedResult.put(entry.getKey(), entry.getValue());
        }
        return sortedResult;
    }

    public double getWorkProgress(int deptId, int workId) {
        Map<Work, int[]> stats = workDAO.getWorksWithProgressStats(deptId);
        for (Map.Entry<Work, int[]> entry : stats.entrySet()) {
            if (entry.getKey().getId() == workId) {
                int[] val = entry.getValue();
                int achieved = val[ 0 ];
                int total = val[ 1 ];
                if (total == 0) return 0.0;
                return (double) achieved / total;
            }
        }
        return 0.0;
    }

    public Map<String, Integer> getWorkStatusStats(int clubId) {
        Map<String, Integer> stats = new LinkedHashMap<>();
        int done = 0, doing = 0, notStarted = 0, overdue = 0;
        List<Work> works = workDAO.getAll();

        for (Work w : works) {
            boolean belongs = false;
            if (w.getDeptId() != null) {
                Department d = departmentDAO.getById(w.getDeptId());
                if (d != null && d.getClubId() == clubId) belongs = true;
            }
            if (!belongs && w.getEventId() != null) {
                Event e = eventDAO.getById(w.getEventId());
                if (e != null && e.getClubId() == clubId) belongs = true;
            }
            if (belongs) {
                String st = w.getStatus();
                if (st.equals("COMPLETED")) done++;
                else if (st.equals("IN_PROGRESS")) doing++;
                else if (st.equals("OVERDUE")) overdue++;
                else notStarted++;
            }
        }
        stats.put("Hoàn thành", done);
        stats.put("Đang làm", doing);
        stats.put("Chưa làm", notStarted);
        stats.put("Quá hạn", overdue);
        return stats;
    }

    public boolean createTask(int workId, String username, String name, String uiPriority, String creatorUsername) {
        Task task = new Task(0, workId, username, name.trim(), mapPriorityToDB(uiPriority), "NOT_STARTED", "", 0, null, null, creatorUsername);
        return taskDAO.insert(task);
    }

    public boolean submitTaskProduct(int taskId, String productLink, String updaterUsername) {
        Task task = taskDAO.getById(taskId);
        if (task == null) return false;
        task.setProductLink(productLink.trim());
        task.setStatus("COMPLETED");
        task.setUpdatedBy(updaterUsername);
        return taskDAO.update(task);
    }

    public boolean evaluateTask(int taskId, boolean isAccepted, String updaterUsername) {
        Task task = taskDAO.getById(taskId);
        if (task == null) return false;
        task.setIsAccepted(isAccepted ? 1 : 0);
        task.setUpdatedBy(updaterUsername);
        return taskDAO.update(task);
    }

    public Map<Task, String> getTasksWithWorkNameByUsername(String username) {
        Map<Task, String> result = new LinkedHashMap<>();
        List<Task> tasks = taskDAO.getTasksByUsername(username);
        for (Task t : tasks) {
            t.setCreatedAt(formatToVN(t.getCreatedAt()));
            Work w = workDAO.getById(t.getWorkId());
            result.put(t, w != null ? w.getName() : "Không xác định");
        }
        return result;
    }

    public Map<Task, User> getTasksWithUserInfo(int workId) {
        Map<Task, User> raw = taskDAO.getTasksWithUserInfoByWorkId(workId);
        Map<Task, User> result = new LinkedHashMap<>();
        for (Map.Entry<Task, User> entry : raw.entrySet()) {
            Task t = entry.getKey();
            t.setCreatedAt(formatToVN(t.getCreatedAt()));
            result.put(t, entry.getValue());
        }
        return result;
    }

    public boolean markAllAsPresent(int eventId, String updaterUsername) {
        boolean success = true;
        List<Attendance> list = attendanceDAO.getAll();
        for (Attendance a : list) {
            if (a.getEventId() == eventId && a.getStatus().equals("PENDING")) {
                a.setStatus("PRESENT");
                a.setUpdatedBy(updaterUsername);
                if (!attendanceDAO.update(a)) success = false;
            }
        }
        return success;
    }

    public Map<Task, String> filterMemTasks(String username, String statusFilter, String timeSort) {
        List<Task> tasks = taskDAO.getTasksByUsername(username);
        Map<Task, String> result = new LinkedHashMap<>();

        if ("Cũ nhất".equals(timeSort)) {
            tasks.sort((t1, t2) -> t1.getCreatedAt().compareTo(t2.getCreatedAt()));
        } else {
            tasks.sort((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()));
        }

        String sF = statusFilter == null ? "Tất cả" : statusFilter.trim();
        for (Task t : tasks) {
            String mappedStatus = t.getStatus().equals("COMPLETED") ? "Hoàn thành" :
                    (t.getStatus().equals("NOT_STARTED") ? "Chưa bắt đầu" : "Đang thực hiện");

            if (sF.equals("Tất cả") || mappedStatus.equalsIgnoreCase(sF)) {
                t.setCreatedAt(formatToVN(t.getCreatedAt()));
                Work w = workDAO.getById(t.getWorkId());
                result.put(t, w != null ? w.getName() : "Không xác định");
            }
        }
        return result;
    }

    public Map<Attendance, Object[]> filterAttendanceList(int clubId, int eventId, String keyword, String deptFilter, String statusFilter) {
        Map<Attendance, User> raw = attendanceDAO.getAttendanceWithUserInfo(eventId);
        Map<Attendance, Object[]> result = new LinkedHashMap<>();

        String kw = keyword == null ? "" : keyword.trim().toLowerCase();
        String dF = deptFilter == null ? "Tất cả" : deptFilter.trim();
        String sF = statusFilter == null ? "Tất cả" : statusFilter.trim();

        for (Map.Entry<Attendance, User> entry : raw.entrySet()) {
            Attendance att = entry.getKey();
            User u = entry.getValue();

            String deptName = "Chưa phân ban";
            ClubMember cm = clubMemberDAO.getById(clubId, att.getUsername());
            if (cm != null && cm.getDeptId() != null && cm.getDeptId() > 0) {
                Department d = departmentDAO.getById(cm.getDeptId());
                if (d != null) deptName = d.getName();
            }

            String uiStatus = mapAttendanceStatusToUI(att.getStatus());

            boolean matchKw = kw.isEmpty() || (u != null && u.getFullName().toLowerCase().contains(kw)) || att.getUsername().toLowerCase().contains(kw);
            boolean matchDept = dF.equals("Tất cả") || deptName.equalsIgnoreCase(dF);
            boolean matchStatus = sF.equals("Tất cả") || uiStatus.equalsIgnoreCase(sF);

            if (matchKw && matchDept && matchStatus) {
                result.put(att, new Object[]{u, deptName, uiStatus});
            }
        }
        return result;
    }

    private Integer getDeptIdByName(int clubId, String name) {
        if (name == null || name.trim().isEmpty() || name.equalsIgnoreCase("Chưa phân công") || name.equalsIgnoreCase("Chưa phân ban") || name.equalsIgnoreCase("Tất cả")) {
            return null;
        }
        List<Department> depts = departmentDAO.getByClubId(clubId);
        for (Department d : depts) {
            if (d.getName().equalsIgnoreCase(name.trim())) return d.getId();
        }
        return null;
    }

    private Integer getEventIdByName(int clubId, String name) {
        if (name == null || name.trim().isEmpty() || name.equalsIgnoreCase("Việc chung") || name.equalsIgnoreCase("Không thuộc sự kiện")) {
            return null;
        }
        List<Event> events = eventDAO.getAll();
        for (Event e : events) {
            if (e.getClubId() == clubId && e.getName().equalsIgnoreCase(name.trim())) return e.getId();
        }
        return null;
    }

    private String parseToISO(String vnDate) {
        if (vnDate == null || !vnDate.contains("/")) return vnDate;
        try {
            LocalDate date = LocalDate.parse(vnDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return date.toString();
        } catch (Exception e) {
            return vnDate;
        }
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

    private String mapAttendanceStatusToDB(String uiStatus) {
        if (uiStatus == null) return "PENDING";
        switch (uiStatus.trim()) {
            case "Có mặt": return "PRESENT";
            case "Vắng": return "ABSENT";
            case "Có phép": return "EXCUSED";
            case "Chưa đến": return "PENDING";
            default: return "PENDING";
        }
    }

    public String mapAttendanceStatusToUI(String dbStatus) {
        if (dbStatus == null) return "Chưa đến";
        switch (dbStatus.trim()) {
            case "PRESENT": return "Có mặt";
            case "ABSENT": return "Vắng";
            case "EXCUSED": return "Có phép";
            case "PENDING": return "Chưa đến";
            default: return "Chưa đến";
        }
    }

    private String mapPriorityToDB(String uiPriority) {
        if (uiPriority == null) return "MEDIUM";
        switch (uiPriority.trim()) {
            case "Cao": return "HIGH";
            case "Trung bình": return "MEDIUM";
            case "Thấp": return "LOW";
            default: return "MEDIUM";
        }
    }

    private String mapWorkStatusToDB(String uiStatus) {
        if (uiStatus == null) return "NOT_STARTED";
        switch (uiStatus.trim()) {
            case "Chưa bắt đầu": return "NOT_STARTED";
            case "Đang tiến hành":
            case "Đang thực hiện": return "IN_PROGRESS";
            case "Hoàn thành": return "COMPLETED";
            case "Đã hủy": return "CANCELED";
            case "Quá hạn": return "OVERDUE";
            default: return "NOT_STARTED";
        }
    }

    private String mapEventStatusToUI(String dbStatus) {
        if (dbStatus == null) return "Sắp diễn ra";
        switch (dbStatus.trim()) {
            case "UPCOMING": return "Sắp diễn ra";
            case "ONGOING": return "Đang diễn ra";
            case "COMPLETED": return "Đã kết thúc";
            case "CANCELED": return "Đã hủy";
            default: return "Sắp diễn ra";
        }
    }

    private String mapWorkStatusToUI(String dbStatus) {
        if (dbStatus == null) return "Chưa bắt đầu";
        switch (dbStatus.trim()) {
            case "NOT_STARTED": return "Chưa bắt đầu";
            case "IN_PROGRESS": return "Đang tiến hành";
            case "COMPLETED": return "Hoàn thành";
            case "CANCELED": return "Đã hủy";
            case "OVERDUE": return "Quá hạn";
            default: return "Chưa bắt đầu";
        }
    }

    public Map<String, String> getMemberAttendanceLog(String username) {
        Map<String, String> log = new LinkedHashMap<>();
        List<Attendance> all = attendanceDAO.getAll();
        for (Attendance a : all) {
            if (a.getUsername().equals(username)) {
                Event e = eventDAO.getById(a.getEventId());
                if (e != null) {
                    String time = a.getCreatedAt();
                    if (time != null && time.length() >= 16) time = time.substring(0, 16).replace("-", "/");
                    String statusUI = mapAttendanceStatusToUI(a.getStatus());
                    log.put(time != null ? time : "Vừa xong", "Cập nhật trạng thái " + statusUI + " tại sự kiện " + e.getName());
                }
            }
        }
        return log;
    }
}

 */