package dao;

import model.Task;
import model.User;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TaskDAO {

    public boolean insert(Task task) {
        String sql = "INSERT INTO tasks(work_id, username, name, priority, status, product_link, is_accepted, updated_by) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, task.getWorkId());
            pstmt.setString(2, task.getUsername());
            pstmt.setString(3, task.getName());
            pstmt.setString(4, task.getPriority());
            pstmt.setString(5, task.getStatus());
            pstmt.setString(6, task.getProductLink());
            pstmt.setInt(7, task.getIsAccepted());
            pstmt.setString(8, task.getUpdatedBy());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Task task) {
        String sql = "UPDATE tasks SET work_id = ?, username = ?, name = ?, priority = ?, status = ?, product_link = ?, is_accepted = ?, updated_by = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, task.getWorkId());
            pstmt.setString(2, task.getUsername());
            pstmt.setString(3, task.getName());
            pstmt.setString(4, task.getPriority());
            pstmt.setString(5, task.getStatus());
            pstmt.setString(6, task.getProductLink());
            pstmt.setInt(7, task.getIsAccepted());
            pstmt.setString(8, task.getUpdatedBy());
            pstmt.setInt(9, task.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Task getById(int id) {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractTaskFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Task> getAll() {
        List<Task> list = new ArrayList<>();
        String sql = "SELECT * FROM tasks";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extractTaskFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Task> getTasksByWorkId(int workId) {
        List<Task> list = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE work_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, workId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractTaskFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Task> getTasksByUsername(String username) {
        List<Task> list = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractTaskFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Map<Task, User> getTasksWithUserInfoByWorkId(int workId) {
        Map<Task, User> map = new LinkedHashMap<>();
        String sql = "SELECT t.*, u.full_name, u.email, u.phone, u.avatar_url FROM tasks t JOIN users u ON t.username = u.username WHERE t.work_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, workId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Task task = extractTaskFromResultSet(rs);
                    User user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    user.setAvatarUrl(rs.getString("avatar_url"));
                    map.put(task, user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    private Task extractTaskFromResultSet(ResultSet rs) throws SQLException {
        return new Task(
                rs.getInt("id"),
                rs.getInt("work_id"),
                rs.getString("username"),
                rs.getString("name"),
                rs.getString("priority"),
                rs.getString("status"),
                rs.getString("product_link"),
                rs.getInt("is_accepted"),
                rs.getString("created_at"),
                rs.getString("updated_at"),
                rs.getString("updated_by")
        );
    }
}