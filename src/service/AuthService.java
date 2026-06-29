package service;

import dao.UserDAO;
import model.User;
import org.apache.commons.codec.digest.DigestUtils;

public class AuthService {

    private final UserDAO userDAO;
    private final EmailService emailService;

    public AuthService() {
        this.userDAO = new UserDAO();
        this.emailService = new EmailService();
    }

    public User login(String identifier, String rawPassword) {
        if (identifier == null || identifier.trim().isEmpty() || rawPassword == null || rawPassword.trim().isEmpty()) {
            return null;
        }

        User user = userDAO.getUserByIdentifier(identifier.trim());
        if (user != null) {
            String hashedInput = DigestUtils.sha256Hex(rawPassword);
            if (user.getPasswordHash().equals(hashedInput)) {
                userDAO.updateLastLogin(user.getUsername());
                return user;
            }
        }
        return null;
    }

    public String register(String fullName, String email, String rawPassword) {
        if (fullName == null || fullName.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                rawPassword == null || rawPassword.trim().isEmpty()) {
            return null;
        }

        if (userDAO.checkEmailExists(email.trim())) {
            return null;
        }

        String generatedId = generateStudentId();
        while (userDAO.checkUsernameExists(generatedId)) {
            generatedId = generateStudentId();
        }

        String hashedPassword = DigestUtils.sha256Hex(rawPassword);

        User newUser = new User();
        newUser.setUsername(generatedId);
        newUser.setFullName(fullName.trim());
        newUser.setEmail(email.trim());
        newUser.setPhone("");
        newUser.setPasswordHash(hashedPassword);
        newUser.setAvatarUrl("gallery/default_avatar.png");
        newUser.setLastLoginAt(null);

        if (userDAO.insert(newUser)) {
            emailService.sendWelcomeEmail(newUser.getEmail(), newUser.getFullName(), generatedId);
            return generatedId;
        }
        return null;
    }

    private String generateStudentId() {
        int randomNum = (int) (Math.random() * 1000);
        return "26MB" + String.format("%03d", randomNum);
    }
}