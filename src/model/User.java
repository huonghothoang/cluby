package model;

public class User {
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String passwordHash;
    private String avatarUrl;
    private String lastLoginAt;
    private String createdAt;
    private String updatedAt;

    public User() {}

    public User(String username, String fullName, String email, String phone, String passwordHash, String avatarUrl, String lastLoginAt, String createdAt, String updatedAt) {
        this.username = username; this.fullName = fullName; this.email = email; this.phone = phone;
        this.passwordHash = passwordHash; this.avatarUrl = avatarUrl; this.lastLoginAt = lastLoginAt;
        this.createdAt = createdAt; this.updatedAt = updatedAt;
    }

    public String getUsername() { return username; } public void setUsername(String username) { this.username = username; }
    public String getFullName() { return fullName; } public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; } public void setPhone(String phone) { this.phone = phone; }
    public String getPasswordHash() { return passwordHash; } public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getAvatarUrl() { return avatarUrl; } public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getLastLoginAt() { return lastLoginAt; } public void setLastLoginAt(String lastLoginAt) { this.lastLoginAt = lastLoginAt; }
    public String getCreatedAt() { return createdAt; } public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; } public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}