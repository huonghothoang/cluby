package model;

public class Club {
    private int id;
    private String name;
    private String description;
    private String logoUrl;
    private String createdAt;
    private String updatedAt;
    private String createdBy;

    public Club() {}

    public Club(int id, String name, String description, String logoUrl, String createdAt, String updatedAt, String createdBy) {
        this.id = id; this.name = name; this.description = description; this.logoUrl = logoUrl;
        this.createdAt = createdAt; this.updatedAt = updatedAt; this.createdBy = createdBy;
    }

    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
    public String getLogoUrl() { return logoUrl; } public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public String getCreatedAt() { return createdAt; } public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; } public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    public String getCreatedBy() { return createdBy; } public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}