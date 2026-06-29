package dao;

import model.Event;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EventDAO {

    public boolean insert(Event event) {
        String sql = "INSERT INTO events(club_id, dept_id, name, description, event_date, event_time, location, status, created_by) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, event.getClubId());
            if (event.getDeptId() != null) {
                pstmt.setInt(2, event.getDeptId());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            pstmt.setString(3, event.getName());
            pstmt.setString(4, event.getDescription());
            pstmt.setString(5, event.getEventDate());
            pstmt.setString(6, event.getEventTime());
            pstmt.setString(7, event.getLocation());
            pstmt.setString(8, event.getStatus());
            pstmt.setString(9, event.getCreatedBy());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Event event) {
        String sql = "UPDATE events SET dept_id = ?, name = ?, description = ?, event_date = ?, event_time = ?, location = ?, status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (event.getDeptId() != null) {
                pstmt.setInt(1, event.getDeptId());
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }
            pstmt.setString(2, event.getName());
            pstmt.setString(3, event.getDescription());
            pstmt.setString(4, event.getEventDate());
            pstmt.setString(5, event.getEventTime());
            pstmt.setString(6, event.getLocation());
            pstmt.setString(7, event.getStatus());
            pstmt.setInt(8, event.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM events WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Event getById(int id) {
        String sql = "SELECT * FROM events WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractEventFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Event> getAll() {
        List<Event> list = new ArrayList<>();
        String sql = "SELECT * FROM events";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extractEventFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Map<Event, Integer> getEventsWithParticipantCount(int clubId) {
        Map<Event, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT e.*, COUNT(a.username) AS participant_count FROM events e LEFT JOIN attendances a ON e.id = a.event_id WHERE e.club_id = ? GROUP BY e.id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clubId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Event event = extractEventFromResultSet(rs);
                    int count = rs.getInt("participant_count");
                    map.put(event, count);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    public boolean createEventWithMassAttendance(Event event, boolean isAllClub) {
        String insertEvent = "INSERT INTO events(club_id, dept_id, name, description, event_date, event_time, location, status, created_by) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String getMembers = "SELECT username FROM club_members WHERE club_id = ? AND status = 'ACTIVE'";
        String insertAttendance = "INSERT INTO attendances(event_id, username, status) VALUES(?, ?, 'PENDING')";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            int newEventId = -1;

            try (PreparedStatement pstmt1 = conn.prepareStatement(insertEvent, Statement.RETURN_GENERATED_KEYS)) {
                pstmt1.setInt(1, event.getClubId());
                if (event.getDeptId() != null) {
                    pstmt1.setInt(2, event.getDeptId());
                } else {
                    pstmt1.setNull(2, Types.INTEGER);
                }
                pstmt1.setString(3, event.getName());
                pstmt1.setString(4, event.getDescription());
                pstmt1.setString(5, event.getEventDate());
                pstmt1.setString(6, event.getEventTime());
                pstmt1.setString(7, event.getLocation());
                pstmt1.setString(8, event.getStatus());
                pstmt1.setString(9, event.getCreatedBy());
                pstmt1.executeUpdate();

                try (ResultSet rs = pstmt1.getGeneratedKeys()) {
                    if (rs.next()) {
                        newEventId = rs.getInt(1);
                    }
                }
            }

            if (newEventId != -1 && isAllClub) {
                List<String> usernames = new ArrayList<>();
                try (PreparedStatement pstmt2 = conn.prepareStatement(getMembers)) {
                    pstmt2.setInt(1, event.getClubId());
                    try (ResultSet rs = pstmt2.executeQuery()) {
                        while (rs.next()) {
                            usernames.add(rs.getString("username"));
                        }
                    }
                }

                if (!usernames.isEmpty()) {
                    try (PreparedStatement pstmt3 = conn.prepareStatement(insertAttendance)) {
                        for (String uname : usernames) {
                            pstmt3.setInt(1, newEventId);
                            pstmt3.setString(2, uname);
                            pstmt3.addBatch();
                        }
                        pstmt3.executeBatch();
                    }
                }
            }

            conn.commit();
            return true;

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

    private Event extractEventFromResultSet(ResultSet rs) throws SQLException {
        int deptIdVal = rs.getInt("dept_id");
        Integer deptId = rs.wasNull() ? null : deptIdVal;

        return new Event(
                rs.getInt("id"),
                rs.getInt("club_id"),
                deptId,
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("event_date"),
                rs.getString("event_time"),
                rs.getString("location"),
                rs.getString("status"),
                rs.getString("created_at"),
                rs.getString("updated_at"),
                rs.getString("created_by")
        );
    }
}