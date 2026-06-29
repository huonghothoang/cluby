package model;

public class Notification {
    private int id;
    private int clubId;
    private String content;
    private String dotColor;
    private String createdAt;

    public Notification() {}

    public Notification(int id, int clubId, String content, String dotColor, String createdAt) {
        this.id = id; this.clubId = clubId; this.content = content; this.dotColor = dotColor; this.createdAt = createdAt;
    }

    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public int getClubId() { return clubId; } public void setClubId(int clubId) { this.clubId = clubId; }
    public String getContent() { return content; } public void setContent(String content) { this.content = content; }
    public String getDotColor() { return dotColor; } public void setDotColor(String dotColor) { this.dotColor = dotColor; }
    public String getCreatedAt() { return createdAt; } public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}