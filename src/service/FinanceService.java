package service;

import dao.EventDAO;
import dao.TransactionDAO;
import model.Event;
import model.Transaction;
import model.User;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FinanceService {

    private final TransactionDAO transactionDAO;
    private final EventDAO eventDAO;

    public FinanceService() {
        this.transactionDAO = new TransactionDAO();
        this.eventDAO = new EventDAO();
    }

    public boolean createTransaction(int clubId, String uiType, String content, long amount, String eventName, String note, String creatorUsername) {
        Integer eventId = null;
        if (eventName != null && !eventName.trim().isEmpty() && !eventName.equalsIgnoreCase("Không") && !eventName.equalsIgnoreCase("Tất cả")) {
            List<Event> events = eventDAO.getAll();
            for (Event e : events) {
                if (e.getClubId() == clubId && e.getName().equalsIgnoreCase(eventName.trim())) {
                    eventId = e.getId();
                    break;
                }
            }
        }

        String dbType = uiType != null && uiType.equalsIgnoreCase("Thu") ? "INCOME" : "EXPENSE";

        String todayISO = LocalDate.now().toString();

        Transaction t = new Transaction(0, clubId, eventId, dbType, amount, content.trim(), todayISO, note != null ? note.trim() : "", "VALID", "", null, null, creatorUsername);
        return transactionDAO.insert(t);
    }

    public boolean cancelTransaction(int transId, String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            return false;
        }
        return transactionDAO.cancelTransaction(transId, reason.trim());
    }

    public Map<Transaction, String[]> getTransactionsWithDetails(int clubId) {
        Map<Transaction, String[]> result = new LinkedHashMap<>();
        Map<Transaction, User> rawMap = transactionDAO.getTransactionsWithUserInfo(clubId);

        DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter vnFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Map.Entry<Transaction, User> entry : rawMap.entrySet()) {
            Transaction t = entry.getKey();
            User u = entry.getValue();

            if (t.getTransDate() != null && t.getTransDate().contains("-")) {
                try {
                    LocalDate date = LocalDate.parse(t.getTransDate(), isoFormatter);
                    t.setTransDate(date.format(vnFormatter));
                } catch (Exception ignored) {}
            }

            String eventName = "Không";
            if (t.getEventId() != null) {
                Event e = eventDAO.getById(t.getEventId());
                if (e != null) {
                    eventName = e.getName();
                }
            }

            String creatorName = u != null ? u.getFullName() : t.getExecutorUsername();

            result.put(t, new String[]{eventName, creatorName});
        }
        return result;
    }

    public Map<Transaction, String[]> filterTransactions(int clubId, String keyword, String typeFilter, String statusFilter, String eventFilter) {
        Map<Transaction, String[]> allTrans = getTransactionsWithDetails(clubId);
        Map<Transaction, String[]> filtered = new LinkedHashMap<>();

        String kw = keyword == null ? "" : keyword.trim().toLowerCase();
        String tF = typeFilter == null ? "Tất cả" : typeFilter.trim();
        String sF = statusFilter == null ? "Tất cả" : statusFilter.trim();
        String eF = eventFilter == null ? "Tất cả" : eventFilter.trim();

        for (Map.Entry<Transaction, String[]> entry : allTrans.entrySet()) {
            Transaction t = entry.getKey();
            String[] details = entry.getValue();
            String eName = details[ 0 ];

            boolean matchKeyword = kw.isEmpty() || t.getContent().toLowerCase().contains(kw);
            boolean matchType = tF.equals("Tất cả") || mapTypeToUI(t.getTransType()).equalsIgnoreCase(tF);
            boolean matchStatus = sF.equals("Tất cả") || mapStatusToUI(t.getStatus()).equalsIgnoreCase(sF);
            boolean matchEvent = eF.equals("Tất cả") || eName.equalsIgnoreCase(eF);

            if (matchKeyword && matchType && matchStatus && matchEvent) {
                filtered.put(t, details);
            }
        }
        return filtered;
    }

    public String[] getFormattedFinanceStats(int clubId) {
        List<Transaction> list = transactionDAO.getByClubId(clubId);

        long currentFund = 0;
        long incomeThisMonth = 0;
        long expenseThisMonth = 0;
        long totalPending = 0;

        String currentMonthPrefix = LocalDate.now().toString().substring(0, 7);

        for (Transaction t : list) {
            if (t.getStatus().equals("VALID")) {
                if (t.getTransType().equals("INCOME")) {
                    currentFund += t.getAmount();
                    if (t.getTransDate() != null && t.getTransDate().startsWith(currentMonthPrefix)) {
                        incomeThisMonth += t.getAmount();
                    }
                } else if (t.getTransType().equals("EXPENSE")) {
                    currentFund -= t.getAmount();
                    if (t.getTransDate() != null && t.getTransDate().startsWith(currentMonthPrefix)) {
                        expenseThisMonth += t.getAmount();
                    }
                }
            } else if (t.getStatus().equals("PENDING")) {
                totalPending += t.getAmount();
            }
        }

        DecimalFormat formatter = new DecimalFormat("#,###đ");

        return new String[]{
                formatter.format(currentFund).replace(",", "."),
                "+" + formatter.format(incomeThisMonth).replace(",", "."),
                "-" + formatter.format(expenseThisMonth).replace(",", "."),
                formatter.format(totalPending).replace(",", ".")
        };
    }

    public String formatCurrency(long amount, boolean isIncome) {
        DecimalFormat formatter = new DecimalFormat("#,###đ");
        String formatted = formatter.format(amount).replace(",", ".");
        if (isIncome) {
            return "+" + formatted;
        } else {
            return "-" + formatted;
        }
    }

    public String mapTypeToUI(String dbType) {
        if (dbType == null) return "Chi";
        return dbType.equals("INCOME") ? "Thu" : "Chi";
    }

    public String mapStatusToUI(String dbStatus) {
        if (dbStatus == null) return "Đã hủy";
        return dbStatus.equals("VALID") ? "Hợp lệ" : "Đã hủy";
    }

    public Map<Transaction, String[]> getRecentTransactions(int clubId, int limit) {
        Map<Transaction, String[]> allTrans = getTransactionsWithDetails(clubId);
        Map<Transaction, String[]> recent = new LinkedHashMap<>();
        int count = 0;
        for (Map.Entry<Transaction, String[]> entry : allTrans.entrySet()) {
            if (count >= limit) break;
            recent.put(entry.getKey(), entry.getValue());
            count++;
        }
        return recent;
    }
}