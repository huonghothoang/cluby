package model;

public class Application {
    private int id;
    private int clubId;
    private String username;
    private int targetDeptId;
    private String intro;
    private String experience;
    private String reason;
    private String status;
    private String reviewerNote;
    private String createdAt;
    private String updatedAt;
    private String reviewedBy;

    public Application() {}

    public Application(int id, int clubId, String username, int targetDeptId, String intro, String experience, String reason, String status, String reviewerNote, String createdAt, String updatedAt, String reviewedBy) {
        this.id = id; this.clubId = clubId; this.username = username; this.targetDeptId = targetDeptId;
        this.intro = intro; this.experience = experience; this.reason = reason; this.status = status;
        this.reviewerNote = reviewerNote; this.createdAt = createdAt; this.updatedAt = updatedAt; this.reviewedBy = reviewedBy;
    }

    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public int getClubId() { return clubId; } public void setClubId(int clubId) { this.clubId = clubId; }
    public String getUsername() { return username; } public void setUsername(String username) { this.username = username; }
    public int getTargetDeptId() { return targetDeptId; } public void setTargetDeptId(int targetDeptId) { this.targetDeptId = targetDeptId; }
    public String getIntro() { return intro; } public void setIntro(String intro) { this.intro = intro; }
    public String getExperience() { return experience; } public void setExperience(String experience) { this.experience = experience; }
    public String getReason() { return reason; } public void setReason(String reason) { this.reason = reason; }
    public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
    public String getReviewerNote() { return reviewerNote; } public void setReviewerNote(String reviewerNote) { this.reviewerNote = reviewerNote; }
    public String getCreatedAt() { return createdAt; } public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; } public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    public String getReviewedBy() { return reviewedBy; } public void setReviewedBy(String reviewedBy) { this.reviewedBy = reviewedBy; }
}