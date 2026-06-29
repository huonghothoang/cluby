package model;

public class Work {
    private int id;
    private Integer eventId;
    private Integer deptId;
    private String name;
    private String description;
    private String startDate;
    private String deadline;
    private String priority;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String createdBy;

    public Work() {}

    public Work(int id, Integer eventId, Integer deptId, String name, String description, String startDate, String deadline, String priority, String status, String createdAt, String updatedAt, String createdBy) {
        this.id = id; this.eventId = eventId; this.deptId = deptId; this.name = name; this.description = description;
        this.startDate = startDate; this.deadline = deadline; this.priority = priority; this.status = status;
        this.createdAt = createdAt; this.updatedAt = updatedAt; this.createdBy = createdBy;
    }

    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public Integer getEventId() { return eventId; } public void setEventId(Integer eventId) { this.eventId = eventId; }
    public Integer getDeptId() { return deptId; } public void setDeptId(Integer deptId) { this.deptId = deptId; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
    public String getStartDate() { return startDate; } public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getDeadline() { return deadline; } public void setDeadline(String deadline) { this.deadline = deadline; }
    public String getPriority() { return priority; } public void setPriority(String priority) { this.priority = priority; }
    public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
    public String getCreatedAt() { return createdAt; } public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; } public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    public String getCreatedBy() { return createdBy; } public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}