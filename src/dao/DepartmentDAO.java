package dao;

import model.Department;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DepartmentDAO {

    public boolean insert(Department dept) {
        String sql = "INSERT INTO departments(club_id, name, description, status, created_by) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, dept.getClubId());
            pstmt.setString(2, dept.getName());
            pstmt.setString(3, dept.getDescription());
            pstmt.setString(4, dept.getStatus());
            pstmt.setString(5, dept.getCreatedBy());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Department dept) {
        String sql = "UPDATE departments SET name = ?, description = ?, status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dept.getName());
            pstmt.setString(2, dept.getDescription());
            pstmt.setString(3, dept.getStatus());
            pstmt.setInt(4, dept.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM departments WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Department getById(int id) {
        String sql = "SELECT * FROM departments WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractDepartmentFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Department> getAll() {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT * FROM departments";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extractDepartmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Department> getByClubId(int clubId) {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT * FROM departments WHERE club_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clubId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractDepartmentFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Map<Department, Integer> getDepartmentsWithMemberCount(int clubId) {
        Map<Department, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT d.*, COUNT(m.username) AS member_count FROM departments d LEFT JOIN club_members m ON d.id = m.dept_id AND m.status = 'ACTIVE' WHERE d.club_id = ? GROUP BY d.id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clubId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Department dept = extractDepartmentFromResultSet(rs);
                    int count = rs.getInt("member_count");
                    map.put(dept, count);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    private Department extractDepartmentFromResultSet(ResultSet rs) throws SQLException {
        return new Department(
                rs.getInt("id"),
                rs.getInt("club_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("status"),
                rs.getString("created_at"),
                rs.getString("updated_at"),
                rs.getString("created_by")
        );
    }
}