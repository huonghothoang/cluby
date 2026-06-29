package model;

public class Event {
    private int id;
    private int clubId;
    private Integer deptId;
    private String name;
    private String description;
    private String eventDate;
    private String eventTime;
    private String location;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String createdBy;

    public Event() {}

    public Event(int id, int clubId, Integer deptId, String name, String description, String eventDate, String eventTime, String location, String status, String createdAt, String updatedAt, String createdBy) {
        this.id = id; this.clubId = clubId; this.deptId = deptId; this.name = name; this.description = description;
        this.eventDate = eventDate; this.eventTime = eventTime; this.location = location; this.status = status;
        this.createdAt = createdAt; this.updatedAt = updatedAt; this.createdBy = createdBy;
    }

    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public int getClubId() { return clubId; } public void setClubId(int clubId) { this.clubId = clubId; }
    public Integer getDeptId() { return deptId; } public void setDeptId(Integer deptId) { this.deptId = deptId; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
    public String getEventDate() { return eventDate; } public void setEventDate(String eventDate) { this.eventDate = eventDate; }
    public String getEventTime() { return eventTime; } public void setEventTime(String eventTime) { this.eventTime = eventTime; }
    public String getLocation() { return location; } public void setLocation(String location) { this.location = location; }
    public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
    public String getCreatedAt() { return createdAt; } public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; } public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    public String getCreatedBy() { return createdBy; } public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}