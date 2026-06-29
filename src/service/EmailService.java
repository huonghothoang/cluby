package service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class EmailService {

    private String username;
    private String password;

    public EmailService() {
        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream(".env")) {
            prop.load(input);
            this.username = prop.getProperty("EMAIL_USER");
            this.password = prop.getProperty("EMAIL_PASS");
        } catch (IOException ex) {
            System.err.println("Không tìm thấy tệp .env! Đảm bảo nó nằm ở thư mục gốc.");
            ex.printStackTrace();
        }
    }

    private Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public void sendWelcomeEmail(String email, String fullName, String generatedId) {
        try {
            Message message = new MimeMessage(getSession());
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Chào mừng bạn gia nhập hệ thống Cluby!");
            String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 10px; max-width: 500px;'>"
                    + "<h2 style='color: #5020d8;'>Xin chào " + fullName + ",</h2>"
                    + "<p>Chào mừng bạn đã đăng ký tham gia hệ thống quản lý <b>Cluby</b>.</p>"
                    + "<div style='background-color: #f3f0ff; padding: 15px; border-left: 5px solid #5020d8; margin: 15px 0;'>"
                    + "Mã định danh của bạn là:<br>"
                    + "<b style='color: #5020d8; font-size: 22px;'>" + generatedId + "</b>"
                    + "</div>"
                    + "<p>Vui lòng chờ ban quản trị phê duyệt đơn đăng nhập của bạn.</p>"
                    + "</div>";
            message.setContent(htmlContent, "text/html; charset=utf-8");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendApplicationResult(String email, boolean isApproved) {
        try {
            Message message = new MimeMessage(getSession());
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Kết quả xét duyệt đơn ứng tuyển Câu lạc bộ");
            String htmlContent;
            if (isApproved) {
                htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 10px; max-width: 500px;'>"
                        + "<h2 style='color: #28a745;'>Chúc mừng!</h2>"
                        + "<p>Đơn ứng tuyển của bạn đã được <b>chấp thuận</b>.</p>"
                        + "<p>Bạn có thể đăng nhập vào hệ thống ngay bây giờ.</p>"
                        + "</div>";
            } else {
                htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 10px; max-width: 500px;'>"
                        + "<h2 style='color: #dc3545;'>Thông báo</h2>"
                        + "<p>Rất tiếc đơn ứng tuyển của bạn chưa phù hợp ở thời điểm hiện tại.</p>"
                        + "<p>Chúc bạn may mắn ở đợt tuyển quân lần sau!</p>"
                        + "</div>";
            }
            message.setContent(htmlContent, "text/html; charset=utf-8");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendEventReminder(String email, String eventName, String time) {
        try {
            Message message = new MimeMessage(getSession());
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Nhắc nhở sự kiện sắp diễn ra: " + eventName);
            String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 10px; max-width: 500px;'>"
                    + "<h2 style='color: #5020d8;'>Nhắc nhở sự kiện</h2>"
                    + "<p>Sự kiện <b>" + eventName + "</b> sẽ diễn ra vào lúc:</p>"
                    + "<div style='text-align: center; font-size: 18px; font-weight: bold; color: #ef4444; padding: 10px; border: 1px dashed #ef4444;'>"
                    + time
                    + "</div>"
                    + "<p style='margin-top: 15px;'>Đừng quên tham gia đúng giờ nhé!</p>"
                    + "</div>";
            message.setContent(htmlContent, "text/html; charset=utf-8");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}