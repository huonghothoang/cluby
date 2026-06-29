package service;

import dao.*;
import model.*;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportService {

    private final ClubMemberDAO clubMemberDAO;
    private final EventDAO eventDAO;
    private final WorkDAO workDAO;
    private final TaskDAO taskDAO;
    private final TransactionDAO transactionDAO;
    private final DepartmentDAO departmentDAO;

    public ReportService() {
        this.clubMemberDAO = new ClubMemberDAO();
        this.eventDAO = new EventDAO();
        this.workDAO = new WorkDAO();
        this.taskDAO = new TaskDAO();
        this.transactionDAO = new TransactionDAO();
        this.departmentDAO = new DepartmentDAO();
    }

    public String[] getReportOverallKPIs(int clubId) {
        int members = 0;
        for (ClubMember m : clubMemberDAO.getAll()) {
            if (m.getClubId() == clubId && m.getStatus().equals("ACTIVE")) members++;
        }
        int events = 0;
        for (Event e : eventDAO.getAll()) {
            if (e.getClubId() == clubId && !e.getStatus().equals("CANCELED")) events++;
        }
        long fund = 0;
        for (Transaction t : transactionDAO.getByClubId(clubId)) {
            if (t.getStatus().equals("VALID")) {
                if (t.getTransType().equals("INCOME")) fund += t.getAmount();
                else fund -= t.getAmount();
            }
        }
        int doneTasks = 0, totalTasks = 0;
        for (Task t : taskDAO.getAll()) {
            Work w = workDAO.getById(t.getWorkId());
            boolean belongs = false;
            if (w != null && w.getDeptId() != null) {
                Department d = departmentDAO.getById(w.getDeptId());
                if (d != null && d.getClubId() == clubId) belongs = true;
            }
            if (w != null && !belongs && w.getEventId() != null) {
                Event e = eventDAO.getById(w.getEventId());
                if (e != null && e.getClubId() == clubId) belongs = true;
            }
            if (belongs) {
                totalTasks++;
                if (t.getStatus().equals("COMPLETED")) doneTasks++;
            }
        }
        String taskRate = totalTasks == 0 ? "0%" : (int)((doneTasks * 100.0) / totalTasks) + "%";
        DecimalFormat df = new DecimalFormat("#,###đ");
        return new String[]{
                String.valueOf(members),
                String.valueOf(events),
                taskRate,
                df.format(fund).replace(",", ".")
        };
    }

    public Map<String, Double> getIncomeSourceStats(int clubId) {
        Map<String, Double> stats = new LinkedHashMap<>();
        double fee = 0, sponsor = 0, other = 0;
        for (Transaction t : transactionDAO.getByClubId(clubId)) {
            if (t.getTransType().equals("INCOME") && t.getStatus().equals("VALID")) {
                String c = t.getContent().toLowerCase();
                if (c.contains("hội phí") || c.contains("phí") || c.contains("đóng quỹ")) fee += t.getAmount();
                else if (c.contains("tài trợ") || c.contains("sponsor") || c.contains("rót vốn")) sponsor += t.getAmount();
                else other += t.getAmount();
            }
        }
        stats.put("Hội phí", fee);
        stats.put("Tài trợ", sponsor);
        stats.put("Khác", other);
        return stats;
    }

    public Map<String, Double> getExpenseCategoryStats(int clubId) {
        Map<String, Double> stats = new LinkedHashMap<>();
        double event = 0, admin = 0, items = 0;
        for (Transaction t : transactionDAO.getByClubId(clubId)) {
            if (t.getTransType().equals("EXPENSE") && t.getStatus().equals("VALID")) {
                String c = t.getContent().toLowerCase();
                if (c.contains("sự kiện") || c.contains("teambuilding") || c.contains("workshop") || t.getEventId() != null) event += t.getAmount();
                else if (c.contains("in ấn") || c.contains("vật tư") || c.contains("mua sắm") || c.contains("thiết bị")) items += t.getAmount();
                else admin += t.getAmount();
            }
        }
        stats.put("Sự kiện", event);
        stats.put("Vật tư", items);
        stats.put("Hành chính", admin);
        return stats;
    }
}