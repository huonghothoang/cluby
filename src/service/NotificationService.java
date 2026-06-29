package service;

import dao.NotificationDAO;
import model.Notification;

import java.util.List;

public class NotificationService {

    private final NotificationDAO notificationDAO;

    public NotificationService() {
        this.notificationDAO = new NotificationDAO();
    }

    public boolean pushNotification(int clubId, String content, String typeUI) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }

        String hexColor;
        if (typeUI == null) {
            hexColor = "#3b82f6";
        } else {
            switch (typeUI.trim().toLowerCase()) {
                case "lỗi":
                case "error":
                case "quá hạn":
                case "từ chối":
                case "hủy":
                    hexColor = "#ef4444";
                    break;
                case "thành công":
                case "success":
                case "hoàn thành":
                case "đã duyệt":
                    hexColor = "#10b981";
                    break;
                case "cảnh báo":
                case "warning":
                case "đang làm":
                case "chờ duyệt":
                    hexColor = "#f59e0b";
                    break;
                case "thông báo":
                case "info":
                default:
                    hexColor = "#3b82f6";
                    break;
            }
        }

        Notification n = new Notification(0, clubId, content.trim(), hexColor, null);
        return notificationDAO.insert(n);
    }

    public List<Notification> getClubNotifications(int clubId) {
        return notificationDAO.getByClubId(clubId);
    }

    public boolean deleteNotification(int notifId) {
        return notificationDAO.delete(notifId);
    }
}