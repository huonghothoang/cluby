package dao;

import model.Application;
import model.User;
import database.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ApplicationDAO {

    public boolean insert(Application app) {
        String sql = "INSERT INTO applications(club_id, username, target_dept_id, intro, experience, reason, status, reviewer_note, reviewed_by) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, app.getClubId());
            pstmt.setString(2, app.getUsername());

            if (app.getTargetDeptId() > 0) {
                pstmt.setInt(3, app.getTargetDeptId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }

            pstmt.setString(4, app.getIntro());
            pstmt.setString(5, app.getExperience());
            pstmt.setString(6, app.getReason());
            pstmt.setString(7, app.getStatus());
            pstmt.setString(8, app.getReviewerNote());
            pstmt.setString(9, app.getReviewedBy());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Application app) {
        String sql = "UPDATE applications SET club_id = ?, username = ?, target_dept_id = ?, intro = ?, experience = ?, reason = ?, status = ?, reviewer_note = ?, reviewed_by = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, app.getClubId());
            pstmt.setString(2, app.getUsername());

            if (app.getTargetDeptId() > 0) {
                pstmt.setInt(3, app.getTargetDeptId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }

            pstmt.setString(4, app.getIntro());
            pstmt.setString(5, app.getExperience());
            pstmt.setString(6, app.getReason());
            pstmt.setString(7, app.getStatus());
            pstmt.setString(8, app.getReviewerNote());
            pstmt.setString(9, app.getReviewedBy());
            pstmt.setInt(10, app.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM applications WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Application getById(int id) {
        String sql = "SELECT * FROM applications WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractApplicationFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Application> getAll() {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT * FROM applications";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extractApplicationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Map<Application, User> getApplicationsWithUserInfo(int clubId) {
        Map<Application, User> map = new LinkedHashMap<>();
        String sql = "SELECT a.*, u.full_name, u.email, u.phone, u.avatar_url, u.username AS user_n FROM applications a JOIN users u ON a.username = u.username WHERE a.club_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clubId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Application app = extractApplicationFromResultSet(rs);
                    User user = new User();
                    user.setUsername(rs.getString("user_n"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    user.setAvatarUrl(rs.getString("avatar_url"));
                    map.put(app, user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    public List<Application> getFilteredApplications(int clubId, String status, Integer deptId) {
        List<Application> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM applications WHERE club_id = ?");

        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND status = ?");
        }
        if (deptId != null && deptId > 0) {
            sql.append(" AND target_dept_id = ?");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int index = 1;
            pstmt.setInt(index++, clubId);

            if (status != null && !status.trim().isEmpty()) {
                pstmt.setString(index++, status);
            }
            if (deptId != null && deptId > 0) {
                pstmt.setInt(index++, deptId);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractApplicationFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countApplicationsByStatus(int clubId, String status) {
        String sql = "SELECT COUNT(*) FROM applications WHERE club_id = ? AND status = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clubId);
            pstmt.setString(2, status);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean approveApplicationTransaction(int appId, int clubId, String username, Integer deptId, String role, String reviewerUsername) {
        String updateApp = "UPDATE applications SET status = 'APPROVED', reviewed_by = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        String insertMember = "INSERT INTO club_members(club_id, username, dept_id, role, status, joined_date, updated_by) VALUES(?, ?, ?, ?, 'ACTIVE', CURRENT_DATE, ?)";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt1 = conn.prepareStatement(updateApp);
                 PreparedStatement pstmt2 = conn.prepareStatement(insertMember)) {

                pstmt1.setString(1, reviewerUsername);
                pstmt1.setInt(2, appId);
                pstmt1.executeUpdate();

                pstmt2.setInt(1, clubId);
                pstmt2.setString(2, username);
                if (deptId != null && deptId > 0) {
                    pstmt2.setInt(3, deptId);
                } else {
                    pstmt2.setNull(3, Types.INTEGER);
                }
                pstmt2.setString(4, role);
                pstmt2.setString(5, reviewerUsername);
                pstmt2.executeUpdate();

                conn.commit();
                return true;
            }
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean rejectApplication(int appId, String reason, String reviewerUsername) {
        String sql = "UPDATE applications SET status = 'REJECTED', reviewer_note = ?, reviewed_by = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, reason);
            pstmt.setString(2, reviewerUsername);
            pstmt.setInt(3, appId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Application extractApplicationFromResultSet(ResultSet rs) throws SQLException {
        return new Application(
                rs.getInt("id"),
                rs.getInt("club_id"),
                rs.getString("username"),
                rs.getInt("target_dept_id"),
                rs.getString("intro"),
                rs.getString("experience"),
                rs.getString("reason"),
                rs.getString("status"),
                rs.getString("reviewer_note") != null ? rs.getString("reviewer_note") : "",
                rs.getString("created_at"),
                rs.getString("updated_at"),
                rs.getString("reviewed_by") != null ? rs.getString("reviewed_by") : ""
        );
    }
}