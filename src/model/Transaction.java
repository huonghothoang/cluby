package model;

public class Transaction {
    private int id;
    private int clubId;
    private Integer eventId;
    private String transType; // 'INCOME' hoặc 'EXPENSE'
    private long amount;
    private String content;
    private String transDate;
    private String note;
    private String status;
    private String cancelReason;
    private String createdAt;
    private String updatedAt;
    private String executorUsername;

    public Transaction() {}

    public Transaction(int id, int clubId, Integer eventId, String transType, long amount, String content, String transDate, String note, String status, String cancelReason, String createdAt, String updatedAt, String executorUsername) {
        this.id = id; this.clubId = clubId; this.eventId = eventId; this.transType = transType;
        this.amount = amount; this.content = content; this.transDate = transDate; this.note = note;
        this.status = status; this.cancelReason = cancelReason; this.createdAt = createdAt;
        this.updatedAt = updatedAt; this.executorUsername = executorUsername;
    }

    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public int getClubId() { return clubId; } public void setClubId(int clubId) { this.clubId = clubId; }
    public Integer getEventId() { return eventId; } public void setEventId(Integer eventId) { this.eventId = eventId; }
    public String getTransType() { return transType; } public void setTransType(String transType) { this.transType = transType; }
    public long getAmount() { return amount; } public void setAmount(long amount) { this.amount = amount; }
    public String getContent() { return content; } public void setContent(String content) { this.content = content; }
    public String getTransDate() { return transDate; } public void setTransDate(String transDate) { this.transDate = transDate; }
    public String getNote() { return note; } public void setNote(String note) { this.note = note; }
    public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
    public String getCancelReason() { return cancelReason; } public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }
    public String getCreatedAt() { return createdAt; } public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; } public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    public String getExecutorUsername() { return executorUsername; } public void setExecutorUsername(String executorUsername) { this.executorUsername = executorUsername; }
}