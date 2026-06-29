package dao;

import model.Transaction;
import model.User;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TransactionDAO {

    public boolean insert(Transaction trans) {
        String sql = "INSERT INTO transactions(club_id, event_id, trans_type, amount, content, trans_date, note, status, cancel_reason, executor_username) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trans.getClubId());
            if (trans.getEventId() != null) {
                pstmt.setInt(2, trans.getEventId());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            pstmt.setString(3, trans.getTransType());
            pstmt.setLong(4, trans.getAmount());
            pstmt.setString(5, trans.getContent());
            pstmt.setString(6, trans.getTransDate());
            pstmt.setString(7, trans.getNote());
            pstmt.setString(8, trans.getStatus());
            pstmt.setString(9, trans.getCancelReason());
            pstmt.setString(10, trans.getExecutorUsername());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Transaction trans) {
        String sql = "UPDATE transactions SET club_id = ?, event_id = ?, trans_type = ?, amount = ?, content = ?, trans_date = ?, note = ?, status = ?, cancel_reason = ?, executor_username = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trans.getClubId());
            if (trans.getEventId() != null) {
                pstmt.setInt(2, trans.getEventId());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            pstmt.setString(3, trans.getTransType());
            pstmt.setLong(4, trans.getAmount());
            pstmt.setString(5, trans.getContent());
            pstmt.setString(6, trans.getTransDate());
            pstmt.setString(7, trans.getNote());
            pstmt.setString(8, trans.getStatus());
            pstmt.setString(9, trans.getCancelReason());
            pstmt.setString(10, trans.getExecutorUsername());
            pstmt.setInt(11, trans.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Transaction getById(int id) {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractTransactionFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Transaction> getAll() {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extractTransactionFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Transaction> getByClubId(int clubId) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE club_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clubId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractTransactionFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Map<Transaction, User> getTransactionsWithUserInfo(int clubId) {
        Map<Transaction, User> map = new LinkedHashMap<>();
        String sql = "SELECT t.*, u.full_name, u.email, u.phone, u.avatar_url FROM transactions t JOIN users u ON t.executor_username = u.username WHERE t.club_id = ? ORDER BY t.id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clubId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Transaction trans = extractTransactionFromResultSet(rs);
                    User user = new User();
                    user.setUsername(rs.getString("executor_username"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    user.setAvatarUrl(rs.getString("avatar_url"));
                    map.put(trans, user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    public long[] getFinanceStats(int clubId) {
        long[] stats = new long[]{0, 0, 0};
        String sql = "SELECT " +
                "SUM(CASE WHEN trans_type = 'INCOME' AND status = 'VALID' THEN amount ELSE 0 END) AS total_income, " +
                "SUM(CASE WHEN trans_type = 'EXPENSE' AND status = 'VALID' THEN amount ELSE 0 END) AS total_expense, " +
                "SUM(CASE WHEN status = 'PENDING' THEN amount ELSE 0 END) AS total_pending " +
                "FROM transactions WHERE club_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clubId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats = new long[]{
                            rs.getLong("total_income"),
                            rs.getLong("total_expense"),
                            rs.getLong("total_pending")
                    };
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    public boolean cancelTransaction(int id, String cancelReason) {
        String sql = "UPDATE transactions SET status = 'CANCELED', cancel_reason = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cancelReason);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Transaction extractTransactionFromResultSet(ResultSet rs) throws SQLException {
        int eventIdVal = rs.getInt("event_id");
        Integer eventId = rs.wasNull() ? null : eventIdVal;

        return new Transaction(
                rs.getInt("id"),
                rs.getInt("club_id"),
                eventId,
                rs.getString("trans_type"),
                rs.getLong("amount"),
                rs.getString("content"),
                rs.getString("trans_date"),
                rs.getString("note"),
                rs.getString("status"),
                rs.getString("cancel_reason"),
                rs.getString("created_at"),
                rs.getString("updated_at"),
                rs.getString("executor_username")
        );
    }
}