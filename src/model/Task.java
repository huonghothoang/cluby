package model;

public class Task {
    private int id;
    private int workId;
    private String username;
    private String name;
    private String priority;
    private String status;
    private String productLink;
    private int isAccepted;
    private String createdAt;
    private String updatedAt;
    private String updatedBy;

    public Task() {}

    public Task(int id, int workId, String username, String name, String priority, String status, String productLink, int isAccepted, String createdAt, String updatedAt, String updatedBy) {
        this.id = id; this.workId = workId; this.username = username; this.name = name;
        this.priority = priority; this.status = status; this.productLink = productLink; this.isAccepted = isAccepted;
        this.createdAt = createdAt; this.updatedAt = updatedAt; this.updatedBy = updatedBy;
    }

    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public int getWorkId() { return workId; } public void setWorkId(int workId) { this.workId = workId; }
    public String getUsername() { return username; } public void setUsername(String username) { this.username = username; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getPriority() { return priority; } public void setPriority(String priority) { this.priority = priority; }
    public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
    public String getProductLink() { return productLink; } public void setProductLink(String productLink) { this.productLink = productLink; }
    public int getIsAccepted() { return isAccepted; } public void setIsAccepted(int isAccepted) { this.isAccepted = isAccepted; }
    public String getCreatedAt() { return createdAt; } public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; } public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    public String getUpdatedBy() { return updatedBy; } public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
}