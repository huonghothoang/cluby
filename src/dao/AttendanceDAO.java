package dao;

import model.Attendance;
import model.User;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AttendanceDAO {

    public boolean insert(Attendance att) {
        String sql = "INSERT INTO attendances(event_id, username, status, updated_by) VALUES(?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, att.getEventId());
            pstmt.setString(2, att.getUsername());
            pstmt.setString(3, att.getStatus());
            pstmt.setString(4, att.getUpdatedBy());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Attendance att) {
        String sql = "UPDATE attendances SET status = ?, updated_by = ?, updated_at = CURRENT_TIMESTAMP WHERE event_id = ? AND username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, att.getStatus());
            pstmt.setString(2, att.getUpdatedBy());
            pstmt.setInt(3, att.getEventId());
            pstmt.setString(4, att.getUsername());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int eventId, String username) {
        String sql = "DELETE FROM attendances WHERE event_id = ? AND username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            pstmt.setString(2, username);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Attendance getById(int eventId, String username) {
        String sql = "SELECT * FROM attendances WHERE event_id = ? AND username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            pstmt.setString(2, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractAttendanceFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Attendance> getAll() {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT * FROM attendances";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extractAttendanceFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Map<Attendance, User> getAttendanceWithUserInfo(int eventId, Integer deptId) {
        Map<Attendance, User> map = new LinkedHashMap<>();
        String sql;

        if (deptId == null) {
            sql = "SELECT a.*, u.full_name, u.email, u.phone, u.avatar_url FROM attendances a JOIN users u ON a.username = u.username WHERE a.event_id = ?";
        } else {
            sql = "SELECT a.*, u.full_name, u.email, u.phone, u.avatar_url FROM attendances a JOIN users u ON a.username = u.username JOIN club_members m ON a.username = m.username WHERE a.event_id = ? AND m.dept_id = ?";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, eventId);
            if (deptId != null) {
                pstmt.setInt(2, deptId);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Attendance att = extractAttendanceFromResultSet(rs);
                    User user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    user.setAvatarUrl(rs.getString("avatar_url"));
                    map.put(att, user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    public Map<String, int[]> getOverallAttendanceStatsPerMember(int clubId) {
        Map<String, int[]> map = new LinkedHashMap<>();
        String sql = "SELECT a.username, " +
                "SUM(CASE WHEN a.status = 'PRESENT' THEN 1 ELSE 0 END) AS p_count, " +
                "SUM(CASE WHEN a.status = 'EXCUSED' THEN 1 ELSE 0 END) AS e_count, " +
                "SUM(CASE WHEN a.status = 'ABSENT' THEN 1 ELSE 0 END) AS a_count " +
                "FROM attendances a JOIN events e ON a.event_id = e.id " +
                "WHERE e.club_id = ? GROUP BY a.username";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clubId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    map.put(rs.getString("username"), new int[]{
                            rs.getInt("p_count"),
                            rs.getInt("e_count"),
                            rs.getInt("a_count")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    public boolean lockAttendanceSession(int eventId, String executorUsername) {
        String sql = "UPDATE attendances SET status = 'ABSENT', updated_by = ?, updated_at = CURRENT_TIMESTAMP WHERE event_id = ? AND status = 'PENDING'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, executorUsername);
            pstmt.setInt(2, eventId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Attendance extractAttendanceFromResultSet(ResultSet rs) throws SQLException {
        return new Attendance(
                rs.getInt("event_id"),
                rs.getString("username"),
                rs.getString("status"),
                rs.getString("created_at"),
                rs.getString("updated_at"),
                rs.getString("updated_by")
        );
    }

    public boolean markAllAsPresent(int eventId, String updaterUsername) {
        String sql = "UPDATE attendances SET status = 'PRESENT', updated_by = ?, updated_at = CURRENT_TIMESTAMP WHERE event_id = ? AND status = 'PENDING'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, updaterUsername);
            pstmt.setInt(2, eventId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}