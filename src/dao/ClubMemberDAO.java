package dao;

import model.ClubMember;
import model.User;
import database.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClubMemberDAO {

    public boolean insert(ClubMember member) {
        String sql = "INSERT INTO club_members(club_id, username, dept_id, role, status, note, joined_date, updated_by) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, member.getClubId());
            pstmt.setString(2, member.getUsername());
            if (member.getDeptId() != null && member.getDeptId() > 0) {
                pstmt.setInt(3, member.getDeptId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setString(4, member.getRole());
            pstmt.setString(5, member.getStatus());
            pstmt.setString(6, member.getNote());
            pstmt.setString(7, member.getJoinedDate());
            pstmt.setString(8, member.getUpdatedBy());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(ClubMember member) {
        String sql = "UPDATE club_members SET dept_id = ?, role = ?, status = ?, note = ?, joined_date = ?, updated_by = ?, updated_at = CURRENT_TIMESTAMP WHERE club_id = ? AND username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (member.getDeptId() != null && member.getDeptId() > 0) {
                pstmt.setInt(1, member.getDeptId());
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }
            pstmt.setString(2, member.getRole());
            pstmt.setString(3, member.getStatus());
            pstmt.setString(4, member.getNote());
            pstmt.setString(5, member.getJoinedDate());
            pstmt.setString(6, member.getUpdatedBy());
            pstmt.setInt(7, member.getClubId());
            pstmt.setString(8, member.getUsername());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy thành viên để cập nhật.");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int clubId, String username) {
        String sql = "DELETE FROM club_members WHERE club_id = ? AND username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clubId);
            pstmt.setString(2, username);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ClubMember getById(int clubId, String username) {
        String sql = "SELECT * FROM club_members WHERE club_id = ? AND username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clubId);
            pstmt.setString(2, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractClubMemberFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ClubMember> getAll() {
        List<ClubMember> list = new ArrayList<>();
        String sql = "SELECT * FROM club_members";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extractClubMemberFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Map<ClubMember, User> getMembersWithUserInfo(int clubId) {
        Map<ClubMember, User> map = new LinkedHashMap<>();
        String sql = "SELECT m.*, u.full_name, u.email, u.phone, u.avatar_url, u.username AS u_name FROM club_members m JOIN users u ON m.username = u.username WHERE m.club_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clubId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ClubMember member = extractClubMemberFromResultSet(rs);
                    User user = new User();
                    user.setUsername(rs.getString("u_name"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    user.setAvatarUrl(rs.getString("avatar_url"));
                    map.put(member, user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    public List<ClubMember> getEligibleSuccessors(int clubId, Integer deptId, String currentUsername) {
        List<ClubMember> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM club_members WHERE club_id = ? AND username != ? AND status = 'ACTIVE'");

        if (deptId != null && deptId > 0) {
            sql.append(" AND dept_id = ?");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setInt(1, clubId);
            pstmt.setString(2, currentUsername);
            if (deptId != null && deptId > 0) {
                pstmt.setInt(3, deptId);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractClubMemberFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ClubMember getDeptLeader(int clubId, int deptId) {
        String sql = "SELECT * FROM club_members WHERE club_id = ? AND dept_id = ? AND role = 'LEADER' AND status = 'ACTIVE'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clubId);
            pstmt.setInt(2, deptId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractClubMemberFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean transferDeptLeadership(int clubId, int deptId, String oldLeader, String newLeader) {
        String demote = "UPDATE club_members SET role = 'MEMBER', updated_at = CURRENT_TIMESTAMP WHERE club_id = ? AND username = ?";
        String promote = "UPDATE club_members SET role = 'LEADER', dept_id = ?, updated_at = CURRENT_TIMESTAMP WHERE club_id = ? AND username = ?";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt1 = conn.prepareStatement(demote);
                 PreparedStatement pstmt2 = conn.prepareStatement(promote)) {
                pstmt1.setInt(1, clubId);
                pstmt1.setString(2, oldLeader);
                pstmt1.executeUpdate();

                pstmt2.setInt(1, deptId);
                pstmt2.setInt(2, clubId);
                pstmt2.setString(3, newLeader);
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
                if (conn != null && !conn.isClosed()) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private ClubMember extractClubMemberFromResultSet(ResultSet rs) throws SQLException {
        int deptIdVal = rs.getInt("dept_id");
        Integer deptId = rs.wasNull() ? null : deptIdVal;
        return new ClubMember(
                rs.getInt("club_id"),
                rs.getString("username"),
                deptId,
                rs.getString("role"),
                rs.getString("status"),
                rs.getString("note"),
                rs.getString("joined_date"),
                rs.getString("created_at"),
                rs.getString("updated_at"),
                rs.getString("updated_by")
        );
    }
}