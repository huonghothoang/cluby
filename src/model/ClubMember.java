package model;

public class ClubMember {
    private int clubId;
    private String username;
    private Integer deptId;
    private String role;
    private String status;
    private String note;
    private String joinedDate;
    private String createdAt;
    private String updatedAt;
    private String updatedBy;

    public ClubMember() {}

    public ClubMember(int clubId, String username, Integer deptId, String role, String status, String note, String joinedDate, String createdAt, String updatedAt, String updatedBy) {
        this.clubId = clubId; this.username = username; this.deptId = deptId; this.role = role;
        this.status = status; this.note = note; this.joinedDate = joinedDate; this.createdAt = createdAt;
        this.updatedAt = updatedAt; this.updatedBy = updatedBy;
    }

    public int getClubId() { return clubId; } public void setClubId(int clubId) { this.clubId = clubId; }
    public String getUsername() { return username; } public void setUsername(String username) { this.username = username; }
    public Integer getDeptId() { return deptId; } public void setDeptId(Integer deptId) { this.deptId = deptId; }
    public String getRole() { return role; } public void setRole(String role) { this.role = role; }
    public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
    public String getNote() { return note; } public void setNote(String note) { this.note = note; }
    public String getJoinedDate() { return joinedDate; } public void setJoinedDate(String joinedDate) { this.joinedDate = joinedDate; }
    public String getCreatedAt() { return createdAt; } public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; } public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    public String getUpdatedBy() { return updatedBy; } public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
}