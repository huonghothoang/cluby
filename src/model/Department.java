package model;

public class Department {
    private int id;
    private int clubId;
    private String name;
    private String description;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String createdBy;

    public Department() {}

    public Department(int id, int clubId, String name, String description, String status, String createdAt, String updatedAt, String createdBy) {
        this.id = id; this.clubId = clubId; this.name = name; this.description = description;
        this.status = status; this.createdAt = createdAt; this.updatedAt = updatedAt; this.createdBy = createdBy;
    }

    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public int getClubId() { return clubId; } public void setClubId(int clubId) { this.clubId = clubId; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
    public String getCreatedAt() { return createdAt; } public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; } public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    public String getCreatedBy() { return createdBy; } public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}