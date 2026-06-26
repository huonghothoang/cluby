package view.president;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import view.format;
import view.president.frame;

public class setting extends ScrollPane {

    // Thông tin người dùng đang đăng nhập
    private String userName = "Alexandra Đặng";
    private String userRole = "Hội trưởng";
    private String userDept = "Chưa phân công";

    // Khởi tạo giao diện cài đặt tài khoản bao gồm thông tin cá nhân, hoạt động, bảo mật và thao tác nâng cao
    public setting() {
        VBox mainContent = new VBox(24);
        mainContent.setPadding(new Insets(32));
        mainContent.setStyle("-fx-background-color: transparent;");

        HBox topCards = new HBox(24);
        VBox col1 = new VBox(24); HBox.setHgrow(col1, Priority.ALWAYS); col1.setPrefWidth(600);
        VBox col2 = new VBox(24); HBox.setHgrow(col2, Priority.ALWAYS); col2.setPrefWidth(600);

        col1.getChildren().addAll(buildProfileCard(), buildActivityCard());
        col2.getChildren().addAll(buildSecurityCard(), buildAdvancedCard());

        topCards.getChildren().addAll(col1, col2);

        mainContent.getChildren().add(topCards);
        format.formatScrollbar(this, mainContent, 12);
        this.setContent(mainContent);
    }

    // Xây dựng khối thẻ chứa biểu mẫu chỉnh sửa thông tin cá nhân và ảnh đại diện của người dùng
    private VBox buildProfileCard() {
        VBox card = format.formatBoxCard();
        card.getChildren().add(format.formatLabel("THÔNG TIN CÁ NHÂN", FontWeight.BLACK, 14, "#1e293b"));

        HBox content = new HBox(24);

        VBox leftBox = new VBox(12);
        leftBox.setAlignment(Pos.CENTER);
        ImageView avt = new ImageView(new Image("trish.jpeg"));
        avt.setFitWidth(100); avt.setFitHeight(100);
        avt.setClip(new Circle(50, 50, 50));
        Button btnChangeAvt = getShadowBtn("Đổi ảnh", "", "rgba(100,116,139,0.1)", "#475569", "rgba(0,0,0,0.05)");
        btnChangeAvt.setOnAction(e -> frame.getInstance().triggerToast("Đã mở hộp thoại chọn ảnh."));
        leftBox.getChildren().addAll(avt, btnChangeAvt);

        VBox rightBox = new VBox(12);
        HBox.setHgrow(rightBox, Priority.ALWAYS);

        TextField fId = format.formatTextField("23HT001"); fId.setDisable(true);
        rightBox.getChildren().add(new VBox(4, format.formatLabel("Mã định danh", FontWeight.BOLD, 12, "#94a3b8"), fId));

        TextField fName = format.formatTextField(userName); fName.setDisable(true);
        rightBox.getChildren().add(new VBox(4, format.formatLabel("Họ và tên", FontWeight.BOLD, 12, "#94a3b8"), fName));

        TextField fEmail = format.formatTextField("alex@gmail.com");
        rightBox.getChildren().add(new VBox(4, format.formatLabel("Email", FontWeight.BOLD, 12, "#94a3b8"), fEmail));

        TextField fPhone = format.formatTextField("0901234567");
        rightBox.getChildren().add(new VBox(4, format.formatLabel("Số điện thoại", FontWeight.BOLD, 12, "#94a3b8"), fPhone));

        HBox actions = new HBox();
        actions.setAlignment(Pos.CENTER_RIGHT);
        Button btnSave = getShadowBtn("Lưu thay đổi", "", "#5020d8", "white", "rgba(80,32,216,0.4)");
        btnSave.setOnAction(e -> frame.getInstance().triggerToast("Đã lưu thông tin cá nhân."));
        actions.getChildren().add(btnSave);
        rightBox.getChildren().add(actions);

        content.getChildren().addAll(leftBox, rightBox);
        card.getChildren().add(content);
        return card;
    }

    // Xây dựng khối thẻ hiển thị thông tin chức vụ, phân ban và trạng thái hoạt động trong câu lạc bộ
    private VBox buildActivityCard() {
        VBox card = format.formatBoxCard();
        card.getChildren().add(format.formatLabel("THÔNG TIN HOẠT ĐỘNG", FontWeight.BLACK, 14, "#1e293b"));

        GridPane grid = new GridPane();
        grid.setHgap(32); grid.setVgap(16);

        grid.add(new VBox(4, format.formatLabel("Ngày tham gia", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel("01/01/2023", FontWeight.BLACK, 16, "#1e293b")), 0, 0);
        grid.add(new VBox(4, format.formatLabel("Trạng thái", FontWeight.BOLD, 12, "#94a3b8"), format.formatBadge("Đang hoạt động", "rgba(16,185,129,0.15)", "#10b981")), 1, 0);

        grid.add(new VBox(4, format.formatLabel("Ban", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(userDept, FontWeight.BLACK, 16, "#7c4dff")), 0, 1);
        grid.add(new VBox(4, format.formatLabel("Chức vụ", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(userRole, FontWeight.BLACK, 16, "#f59e0b")), 1, 1);

        card.getChildren().add(grid);
        return card;
    }

    // Thiết lập biểu mẫu nhập liệu phục vụ tính năng thay đổi mật khẩu tài khoản người dùng
    private VBox buildSecurityCard() {
        VBox card = format.formatBoxCard();
        card.getChildren().add(format.formatLabel("BẢO MẬT", FontWeight.BLACK, 14, "#1e293b"));

        VBox form = new VBox(12);

        PasswordField fOld = new PasswordField(); formatField(fOld, "Mật khẩu cũ...");
        form.getChildren().add(new VBox(4, format.formatLabel("Mật khẩu cũ", FontWeight.BOLD, 12, "#94a3b8"), fOld));

        PasswordField fNew = new PasswordField(); formatField(fNew, "Mật khẩu mới...");
        form.getChildren().add(new VBox(4, format.formatLabel("Mật khẩu mới", FontWeight.BOLD, 12, "#94a3b8"), fNew));

        PasswordField fConfirm = new PasswordField(); formatField(fConfirm, "Nhập lại mật khẩu mới...");
        form.getChildren().add(new VBox(4, format.formatLabel("Xác nhận mật khẩu", FontWeight.BOLD, 12, "#94a3b8"), fConfirm));

        HBox actions = new HBox();
        actions.setAlignment(Pos.CENTER_RIGHT);
        Button btnSave = getShadowBtn("Đổi mật khẩu", "", "#10b981", "white", "rgba(16,185,129,0.4)");
        btnSave.setOnAction(e -> frame.getInstance().triggerToast("Đã đổi mật khẩu."));
        actions.getChildren().add(btnSave);
        form.getChildren().add(actions);

        card.getChildren().add(form);
        return card;
    }

    // Xây dựng khối thẻ chứa các tác vụ quản lý đặc quyền cấp cao dựa trên vai trò của tài khoản
    private VBox buildAdvancedCard() {
        VBox card = format.formatBoxCard();
        card.getChildren().add(format.formatLabel("THẠO TÁC NÂNG CAO", FontWeight.BLACK, 14, "#1e293b"));

        VBox content = new VBox(16);

        // Chặn Thành viên không được hiện các nút Quản lý
        if (!userRole.equals("Thành viên")) {
            content.getChildren().add(createActionRow(
                    "Chuyển giao chức vụ", "Bàn giao vai trò hiện tại cho người khác.", "Chuyển giao", "rgba(59,130,246,0.15)", "#3b82f6", e -> showTransferModal()
            ));

            if (!userRole.equals("Hội trưởng")) {
                content.getChildren().add(createActionRow(
                        "Từ chức", "Thôi giữ chức vụ hiện tại.", "Từ chức", "rgba(245,158,11,0.15)", "#f59e0b", e -> showResignModal()
                ));
            }
        }

        // Rời CLB mở cho tất cả mọi người
        content.getChildren().add(createActionRow(
                "Rời câu lạc bộ", "Thoát khỏi hệ thống.", "Rời CLB", "rgba(239,68,68,0.15)", "#ef4444", e -> showLeaveModal()
        ));

        card.getChildren().add(content);
        return card;
    }

    // Thiết lập cấu trúc bố cục một hàng tác vụ nâng cao gồm nhãn mô tả thông tin và nút kích hoạt tương ứng
    private HBox createActionRow(String title, String desc, String btnText, String btnBg, String btnColor, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        HBox row = new HBox(16);
        row.setAlignment(Pos.CENTER_LEFT);

        VBox info = new VBox(4,
                format.formatLabel(title, FontWeight.BOLD, 14, "#1e293b"),
                format.formatLabel(desc, FontWeight.MEDIUM, 12, "#64748b")
        );
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        Button btn = getShadowBtn(btnText, "", btnBg, btnColor, "rgba(0,0,0,0.05)");
        btn.setOnAction(action);

        row.getChildren().addAll(info, spacer, btn);
        return row;
    }

    // Tạo biểu mẫu danh sách lựa chọn dạng modal phục vụ quy trình chuyển giao quyền điều hành tối cao của tài khoản
    private void showTransferModal() {
        StackPane rootModal = new StackPane();
        VBox box = new VBox(20);
        box.setPrefWidth(400); box.setMaxSize(400, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        box.getChildren().addAll(
                format.formatLabel("Chuyển giao", FontWeight.BLACK, 20, "#1e293b"),
                format.formatLabel("Chuyển giao cho Hội phó hoặc Thành viên đang hoạt động.", FontWeight.MEDIUM, 12, "#64748b")
        );

        // Giao diện thả xuống (Dropdown) trang trí chuẩn UI xanh dương
        ComboBox<String> cbSuccessor = new ComboBox<>();
        cbSuccessor.setPromptText("Chọn người kế nhiệm");
        cbSuccessor.setPrefWidth(Double.MAX_VALUE);
        cbSuccessor.setStyle("-fx-background-color: rgba(59,130,246,0.15); -fx-background-radius: 20px; -fx-padding: 8 16; -fx-font-family: 'Google Sans'; -fx-font-weight: bold; -fx-text-fill: #3b82f6; -fx-cursor: hand; -fx-border-color: rgba(59,130,246,0.3); -fx-border-radius: 20px;");

        String css = ".combo-box-popup .list-view { -fx-background-color: rgba(255,255,255,0.95); -fx-background-radius: 16px; -fx-border-radius: 16px; -fx-padding: 6; -fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.15), 15, 0, 0, 8); } " +
                ".combo-box-popup .list-cell { -fx-padding: 10 16; -fx-font-family: 'Google Sans'; -fx-font-weight: bold; -fx-text-fill: #3b82f6; -fx-background-radius: 12px; } " +
                ".combo-box-popup .list-cell:hover { -fx-background-color: #3b82f6; -fx-text-fill: white; }";
        cbSuccessor.getStylesheets().add("data:text/css," + css.replaceAll(" ", "%20"));

        // Lọc dữ liệu theo nghiệp vụ (Tuyệt đối không hiện Trưởng ban)
        cbSuccessor.getItems().addAll(
                "Hoàng Kim Yến (Hội phó - Đang hoạt động)",
                "Nguyễn Văn A (Thành viên - Đang hoạt động)",
                "Lê Thị B (Thành viên - Đang hoạt động)"
        );

        box.getChildren().add(new VBox(6, format.formatLabel("Người tiếp nhận", FontWeight.BOLD, 12, "#94a3b8"), cbSuccessor));

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnCancel = getShadowBtn("Hủy", "", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> frame.getInstance().closeOverlayModal());

        Button btnConfirm = getShadowBtn("Xác nhận", "", "#3b82f6", "white", "rgba(59,130,246,0.4)");
        btnConfirm.setOnAction(e -> {
            if (cbSuccessor.getValue() == null) {
                frame.getInstance().triggerToast("Vui lòng chọn người kế nhiệm.");
                return;
            }
            frame.getInstance().closeOverlayModal();
            frame.getInstance().triggerToast("Chuyển giao thành công. Bạn sẽ được đăng xuất để ứng dụng hiển thị đúng giao diện mới.");

            // Bộ đếm 5 giây rồi văng ra màn hình Login (Giả lập System.exit)
            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(event -> {
                System.exit(0);
            });
            pause.play();
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().add(actions);

        rootModal.getChildren().add(box);
        frame.getInstance().showCustomModal(rootModal);
    }

    // Kích hoạt hiển thị modal cảnh báo yêu cầu xác nhận trước khi thực hiện quy trình rời khỏi tổ chức
    private void showLeaveModal() {
        VBox modal = format.formatSimpleModal(
                "Xác nhận Rời Câu lạc bộ",
                "Bạn chắc chắn chứ? Bạn có thể xin gia nhập lại bất cứ lúc nào.",
                "Hủy", "Rời CLB",
                e -> frame.getInstance().closeOverlayModal(),
                e -> {
                    frame.getInstance().closeOverlayModal();
                    frame.getInstance().triggerToast("Tạm biệt " + userName + ", hẹn gặp lại!");

                    // Đợi 2.5s để người dùng đọc được lời chào rồi kick về màn login
                    PauseTransition pause = new PauseTransition(Duration.seconds(2.5));
                    pause.setOnFinished(event -> {
                        System.exit(0);
                    });
                    pause.play();
                }
        );
        frame.getInstance().showCustomModal(modal);
    }

    // Kích hoạt hiển thị modal cảnh báo yêu cầu xác nhận trước khi tiến hành quy trình từ chức
    private void showResignModal() {
        VBox modal = format.formatSimpleModal(
                "Xác nhận Từ chức",
                "Bạn sẽ trở thành Thành viên.",
                "Hủy", "Từ chức",
                e -> frame.getInstance().closeOverlayModal(),
                e -> {
                    frame.getInstance().closeOverlayModal();
                    frame.getInstance().triggerToast("Đã từ chức. Bạn hiện là Thành viên.");
                }
        );
        frame.getInstance().showCustomModal(modal);
    }

    // Định dạng kiểu dáng đồ họa và màu sắc văn bản cho ô nhập liệu mật khẩu bảo mật
    private void formatField(PasswordField tf, String prompt) {
        tf.setPromptText(prompt);
        tf.setStyle("-fx-background-color: rgb(208 197 244 / 0.5); -fx-background-radius: 40px; -fx-padding: 10 16; -fx-font-family: 'Google Sans'; -fx-text-inner-color: #000000;");
    }

    // Định nghĩa cấu trúc đồ họa, màu sắc và các hiệu ứng thu phóng khi rê chuột dành cho thành phần nút bấm
    private Button getShadowBtn(String text, String icon, String bgColor, String textColor, String shadowColor) {
        Button btn = new Button();
        HBox content = new HBox(8);
        content.setAlignment(Pos.CENTER);
        if (!icon.isEmpty()) {
            Label lblIcon = new Label(icon); lblIcon.setTextFill(Color.web(textColor));
            content.getChildren().add(lblIcon);
        }
        Label lblText = new Label(text);
        lblText.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));
        lblText.setTextFill(Color.web(textColor));
        content.getChildren().add(lblText);

        btn.setGraphic(content);
        btn.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 40px; -fx-padding: 8 16 8 16; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, " + shadowColor + ", 10, 0, 0, 4);");

        btn.setOnMouseEntered(e -> { btn.setScaleX(1.05); btn.setScaleY(1.05); });
        btn.setOnMouseExited(e -> { btn.setScaleX(1.0); btn.setScaleY(1.0); });
        return btn;
    }
}