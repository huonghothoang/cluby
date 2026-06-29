package model;

public class Attendance {
    private int eventId;
    private String username;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String updatedBy;

    public Attendance() {}

    public Attendance(int eventId, String username, String status, String createdAt, String updatedAt, String updatedBy) {
        this.eventId = eventId; this.username = username; this.status = status;
        this.createdAt = createdAt; this.updatedAt = updatedAt; this.updatedBy = updatedBy;
    }

    public int getEventId() { return eventId; } public void setEventId(int eventId) { this.eventId = eventId; }
    public String getUsername() { return username; } public void setUsername(String username) { this.username = username; }
    public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
    public String getCreatedAt() { return createdAt; } public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; } public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    public String getUpdatedBy() { return updatedBy; } public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
}