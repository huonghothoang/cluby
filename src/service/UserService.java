package service;

import dao.ClubMemberDAO;
import dao.UserDAO;
import model.ClubMember;
import model.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class UserService {

    private final UserDAO userDAO;
    private final ClubMemberDAO clubMemberDAO;

    public UserService() {
        this.userDAO = new UserDAO();
        this.clubMemberDAO = new ClubMemberDAO();
    }

    public boolean updateProfile(String username, String fullName, String email, String phone) {
        if (username == null || fullName == null || fullName.trim().isEmpty() || email == null || email.trim().isEmpty()) {
            return false;
        }
        User user = userDAO.getById(username);
        if (user != null) {
            user.setFullName(fullName.trim());
            user.setEmail(email.trim());
            user.setPhone(phone != null ? phone.trim() : "");
            return userDAO.update(user);
        }
        return false;
    }

    public void changePassword(String username, String oldRawPass, String newRawPass) throws Exception {
        if (oldRawPass == null || oldRawPass.trim().isEmpty() || newRawPass == null || newRawPass.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin mật khẩu.");
        }
        String hashedOld = DigestUtils.sha256Hex(oldRawPass);
        if (!userDAO.checkOldPassword(username, hashedOld)) {
            throw new Exception("Mật khẩu cũ không chính xác.");
        }
        String hashedNew = DigestUtils.sha256Hex(newRawPass);
        try {
            if (!userDAO.updatePassword(username, hashedNew)) {
                throw new Exception("Không thể cập nhật mật khẩu mới do lỗi truy xuất dữ liệu.");
            }
        } catch (Exception e) {
            throw new Exception("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }

    public String updateAvatar(String username, File selectedImage) throws Exception {
        if (selectedImage == null || !selectedImage.exists()) {
            throw new IllegalArgumentException("File ảnh không tồn tại.");
        }

        Path galleryPath = Paths.get(System.getProperty("user.home"), ".cluby", "gallery");
        if (!Files.exists(galleryPath)) {
            Files.createDirectories(galleryPath);
        }

        String fileName = selectedImage.getName();
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i);
        }

        String newFileName = "avatar_" + username + "_" + System.currentTimeMillis() + extension;
        Path destinationPath = galleryPath.resolve(newFileName);

        Files.copy(selectedImage.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
        String absolutePath = destinationPath.toAbsolutePath().toString();

        try {
            if (userDAO.updateAvatar(username, absolutePath)) {
                return absolutePath;
            } else {
                Files.deleteIfExists(destinationPath);
                throw new Exception("Lỗi cập nhật CSDL. Đã hoàn tác lưu file.");
            }
        } catch (Exception e) {
            Files.deleteIfExists(destinationPath);
            throw new Exception("Lỗi hệ thống: " + e.getMessage());
        }
    }

    public boolean deleteAccount(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return userDAO.deleteAccountTransaction(username);
    }

    public List<User> searchUsers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return userDAO.getAll();
        }
        return userDAO.searchUsers(keyword.trim());
    }

    public User getUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        return userDAO.getById(username);
    }

    public void resignRole(int clubId, String username) throws Exception {
        ClubMember m = clubMemberDAO.getById(clubId, username);
        if (m == null) {
            throw new Exception("Không tìm thấy thông tin thành viên.");
        }
        if ("PRESIDENT".equals(m.getRole())) {
            throw new Exception("Hội trưởng không thể tự từ chức. Vui lòng thực hiện chuyển giao quyền lực trước.");
        }
        if ("LEADER".equals(m.getRole())) {
            throw new Exception("Trưởng ban không thể tự từ chức. Vui lòng chuyển giao chức vụ Trưởng ban trước.");
        }

        m.setRole("MEMBER");
        if (!clubMemberDAO.update(m)) {
            throw new Exception("Lỗi hệ thống khi cập nhật thông tin chức vụ.");
        }
    }

    public void leaveClub(int clubId, String username) throws Exception {
        ClubMember m = clubMemberDAO.getById(clubId, username);
        if (m == null) {
            throw new Exception("Không tìm thấy thông tin thành viên.");
        }
        if ("PRESIDENT".equals(m.getRole())) {
            throw new Exception("Hội trưởng không thể rời Câu lạc bộ. Vui lòng chuyển giao quyền lực trước.");
        }
        if ("LEADER".equals(m.getRole())) {
            throw new Exception("Trưởng ban không thể rời Câu lạc bộ. Vui lòng chuyển giao chức vụ Trưởng ban trước.");
        }

        m.setStatus("LEFT");
        if (!clubMemberDAO.update(m)) {
            throw new Exception("Lỗi hệ thống khi cập nhật trạng thái rời Câu lạc bộ.");
        }
    }
}