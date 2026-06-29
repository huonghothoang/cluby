package dao;

import model.Work;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WorkDAO {

    public boolean insert(Work work) {
        String sql = "INSERT INTO works(event_id, dept_id, name, description, start_date, deadline, priority, status, created_by) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (work.getEventId() != null) {
                pstmt.setInt(1, work.getEventId());
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }
            if (work.getDeptId() != null) {
                pstmt.setInt(2, work.getDeptId());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            pstmt.setString(3, work.getName());
            pstmt.setString(4, work.getDescription());
            pstmt.setString(5, work.getStartDate());
            pstmt.setString(6, work.getDeadline());
            pstmt.setString(7, work.getPriority());
            pstmt.setString(8, work.getStatus());
            pstmt.setString(9, work.getCreatedBy());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Work work) {
        String sql = "UPDATE works SET event_id = ?, dept_id = ?, name = ?, description = ?, start_date = ?, deadline = ?, priority = ?, status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (work.getEventId() != null) {
                pstmt.setInt(1, work.getEventId());
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }
            if (work.getDeptId() != null) {
                pstmt.setInt(2, work.getDeptId());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            pstmt.setString(3, work.getName());
            pstmt.setString(4, work.getDescription());
            pstmt.setString(5, work.getStartDate());
            pstmt.setString(6, work.getDeadline());
            pstmt.setString(7, work.getPriority());
            pstmt.setString(8, work.getStatus());
            pstmt.setInt(9, work.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM works WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Work getById(int id) {
        String sql = "SELECT * FROM works WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractWorkFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Work> getAll() {
        List<Work> list = new ArrayList<>();
        String sql = "SELECT * FROM works";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extractWorkFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Work> getWorksByDept(int deptId) {
        List<Work> list = new ArrayList<>();
        String sql = "SELECT * FROM works WHERE dept_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, deptId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractWorkFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Map<Work, int[]> getWorksWithProgressStats(int deptId) {
        Map<Work, int[]> map = new LinkedHashMap<>();
        String sql = "SELECT w.*, " +
                "SUM(CASE WHEN t.priority = 'HIGH' THEN 3 WHEN t.priority = 'MEDIUM' THEN 2 WHEN t.priority = 'LOW' THEN 1 ELSE 0 END) AS total_points, " +
                "SUM(CASE WHEN t.status = 'COMPLETED' THEN (CASE WHEN t.priority = 'HIGH' THEN 3 WHEN t.priority = 'MEDIUM' THEN 2 WHEN t.priority = 'LOW' THEN 1 ELSE 0 END) ELSE 0 END) AS achieved_points " +
                "FROM works w LEFT JOIN tasks t ON w.id = t.work_id " +
                "WHERE w.dept_id = ? GROUP BY w.id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, deptId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    map.put(extractWorkFromResultSet(rs), new int[]{
                            rs.getInt("achieved_points"),
                            rs.getInt("total_points")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    private Work extractWorkFromResultSet(ResultSet rs) throws SQLException {
        int eventIdVal = rs.getInt("event_id");
        Integer eventId = rs.wasNull() ? null : eventIdVal;

        int deptIdVal = rs.getInt("dept_id");
        Integer deptId = rs.wasNull() ? null : deptIdVal;

        return new Work(
                rs.getInt("id"),
                eventId,
                deptId,
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("start_date"),
                rs.getString("deadline"),
                rs.getString("priority"),
                rs.getString("status"),
                rs.getString("created_at"),
                rs.getString("updated_at"),
                rs.getString("created_by")
        );
    }
}