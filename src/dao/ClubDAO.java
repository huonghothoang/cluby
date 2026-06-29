package dao;

import model.Club;
import database.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClubDAO {

    public boolean insert(Club club) {
        String sql = "INSERT INTO clubs(name, description, logo_url, created_by) VALUES(?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, club.getName());
            pstmt.setString(2, club.getDescription());
            pstmt.setString(3, club.getLogoUrl());
            pstmt.setString(4, club.getCreatedBy());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Club club) {
        String sql = "UPDATE clubs SET name = ?, description = ?, logo_url = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, club.getName());
            pstmt.setString(2, club.getDescription());
            pstmt.setString(3, club.getLogoUrl());
            pstmt.setInt(4, club.getId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy CLB để cập nhật (ID không tồn tại).");
            }

            String fetchSql = "SELECT updated_at FROM clubs WHERE id = ?";
            try (PreparedStatement fetchStmt = conn.prepareStatement(fetchSql)) {
                fetchStmt.setInt(1, club.getId());
                try (ResultSet rs = fetchStmt.executeQuery()) {
                    if (rs.next()) {
                        club.setUpdatedAt(rs.getString("updated_at"));
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM clubs WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Club getById(int id) {
        String sql = "SELECT * FROM clubs WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractClubFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Club> getAll() {
        List<Club> list = new ArrayList<>();
        String sql = "SELECT * FROM clubs";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    list.add(extractClubFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Club> searchClubs(String keyword) {
        List<Club> list = new ArrayList<>();
        String sql = "SELECT * FROM clubs WHERE name LIKE ? OR description LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            pstmt.setString(1, kw);
            pstmt.setString(2, kw);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractClubFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Club> getByCreatedBy(String username) {
        List<Club> list = new ArrayList<>();
        String sql = "SELECT * FROM clubs WHERE created_by = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractClubFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean createClubTransaction(Club club, String creatorUsername) {
        String insertClub = "INSERT INTO clubs(name, description, logo_url, created_by) VALUES(?, ?, ?, ?)";
        String insertMember = "INSERT INTO club_members(club_id, username, role, status, joined_date) VALUES(?, ?, 'PRESIDENT', 'ACTIVE', CURRENT_DATE)";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            int newClubId = -1;
            try (PreparedStatement pstmt1 = conn.prepareStatement(insertClub, Statement.RETURN_GENERATED_KEYS)) {
                pstmt1.setString(1, club.getName());
                pstmt1.setString(2, club.getDescription());
                pstmt1.setString(3, club.getLogoUrl());
                pstmt1.setString(4, creatorUsername);
                pstmt1.executeUpdate();
                try (ResultSet rs = pstmt1.getGeneratedKeys()) {
                    if (rs.next()) {
                        newClubId = rs.getInt(1);
                    }
                }
            }
            if (newClubId != -1) {
                try (PreparedStatement pstmt2 = conn.prepareStatement(insertMember)) {
                    pstmt2.setInt(1, newClubId);
                    pstmt2.setString(2, creatorUsername);
                    pstmt2.executeUpdate();
                }
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
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

    private Club extractClubFromResultSet(ResultSet rs) throws SQLException {
        return new Club(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("logo_url"),
                rs.getString("created_at"),
                rs.getString("updated_at"),
                rs.getString("created_by")
        );
    }
}