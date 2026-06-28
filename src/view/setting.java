package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import view.format;
import view.president.frame;
import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class setting extends ScrollPane {

    private String userName = "Hương";
    private String userRole = "Hội trưởng";
    private String userDept = "Chưa phân công";

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

    private VBox buildProfileCard() {
        VBox card = format.formatBoxCard();
        card.getChildren().add(format.formatLabel("THÔNG TIN CÁ NHÂN", FontWeight.BLACK, 14, "#1e293b"));

        HBox content = new HBox(24);

        VBox leftBox = new VBox(12);
        leftBox.setAlignment(Pos.CENTER);
        ImageView avt = new ImageView(new Image("temp.png"));
        avt.setFitWidth(100); avt.setFitHeight(100);
        avt.setClip(new Circle(50, 50, 50));
        Button btnChangeAvt = getShadowBtn("Đổi ảnh", "", "rgba(100,116,139,0.1)", "#475569", "rgba(0,0,0,0.05)");
        btnChangeAvt.setOnAction(e -> frame.getInstance().triggerToast("Đã mở chọn ảnh"));
        leftBox.getChildren().addAll(avt, btnChangeAvt);

        VBox rightBox = new VBox(12);
        HBox.setHgrow(rightBox, Priority.ALWAYS);

        TextField fId = format.formatTextField("25GT020"); fId.setDisable(true);
        rightBox.getChildren().add(new VBox(4, format.formatLabel("Mã định danh", FontWeight.BOLD, 12, "#94a3b8"), fId));

        TextField fName = format.formatTextField(userName); fName.setDisable(true);
        rightBox.getChildren().add(new VBox(4, format.formatLabel("Họ và tên", FontWeight.BOLD, 12, "#94a3b8"), fName));

        TextField fEmail = format.formatTextField("huong@gmail.com");
        rightBox.getChildren().add(new VBox(4, format.formatLabel("Email", FontWeight.BOLD, 12, "#94a3b8"), fEmail));

        TextField fPhone = format.formatTextField("0901234567");
        rightBox.getChildren().add(new VBox(4, format.formatLabel("Số điện thoại", FontWeight.BOLD, 12, "#94a3b8"), fPhone));

        HBox actions = new HBox();
        actions.setAlignment(Pos.CENTER_RIGHT);
        Button btnSave = getShadowBtn("Lưu", "", "#5020d8", "white", "rgba(80,32,216,0.4)");
        btnSave.setOnAction(e -> frame.getInstance().triggerToast("Đã lưu thông tin"));
        actions.getChildren().add(btnSave);
        rightBox.getChildren().add(actions);

        content.getChildren().addAll(leftBox, rightBox);
        card.getChildren().add(content);
        return card;
    }

    private VBox buildActivityCard() {
        VBox card = format.formatBoxCard();
        card.getChildren().add(format.formatLabel("HOẠT ĐỘNG", FontWeight.BLACK, 14, "#1e293b"));

        GridPane grid = new GridPane();
        grid.setHgap(32); grid.setVgap(16);

        grid.add(new VBox(4, format.formatLabel("Ngày tham gia", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel("01/01/2023", FontWeight.BLACK, 16, "#1e293b")), 0, 0);
        grid.add(new VBox(4, format.formatLabel("Trạng thái", FontWeight.BOLD, 12, "#94a3b8"), format.formatBadge("Hoạt động", "rgba(16,185,129,0.15)", "#10b981")), 1, 0);

        grid.add(new VBox(4, format.formatLabel("Bộ phận", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(userDept, FontWeight.BLACK, 16, "#7c4dff")), 0, 1);
        grid.add(new VBox(4, format.formatLabel("Chức vụ", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(userRole, FontWeight.BLACK, 16, "#f59e0b")), 1, 1);

        card.getChildren().add(grid);
        return card;
    }

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
        btnSave.setOnAction(e -> frame.getInstance().triggerToast("Đã đổi mật khẩu"));
        actions.getChildren().add(btnSave);
        form.getChildren().add(actions);

        card.getChildren().add(form);
        return card;
    }

    private VBox buildAdvancedCard() {
        VBox card = format.formatBoxCard();
        card.getChildren().add(format.formatLabel("TÙY CHỌN KHÁC", FontWeight.BLACK, 14, "#1e293b"));

        VBox content = new VBox(16);

        if (!userRole.equals("Thành viên")) {
            content.getChildren().add(createActionRow(
                    "Chuyển giao quyền", "Bàn giao lại vị trí quản lý hiện tại.", "Chuyển giao", "rgba(59,130,246,0.15)", "#3b82f6", e -> showTransferModal()
            ));

            if (!userRole.equals("Hội trưởng")) {
                content.getChildren().add(createActionRow(
                        "Từ chức", "Thôi giữ vị trí quản lý hiện tại.", "Từ chức", "rgba(245,158,11,0.15)", "#f59e0b", e -> showResignModal()
                ));
            }
        }

        content.getChildren().add(createActionRow(
                "Rời CLB", "Thoát khỏi hệ thống câu lạc bộ.", "Rời CLB", "rgba(239,68,68,0.15)", "#ef4444", e -> showLeaveModal()
        ));

        card.getChildren().add(content);
        return card;
    }

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

    private void showTransferModal() {
        StackPane rootModal = new StackPane();
        VBox box = new VBox(20);
        box.setPrefWidth(400); box.setMaxSize(400, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        box.getChildren().addAll(
                format.formatLabel("Chuyển giao", FontWeight.BLACK, 20, "#1e293b"),
                format.formatLabel("Bàn giao cho nhân sự đang hoạt động.", FontWeight.MEDIUM, 12, "#64748b")
        );

        ComboBox<String> cbSuccessor = new ComboBox<>();
        cbSuccessor.setPromptText("Chọn người tiếp nhận");
        cbSuccessor.setPrefWidth(Double.MAX_VALUE);
        cbSuccessor.setStyle("-fx-background-color: rgba(59,130,246,0.15); -fx-background-radius: 20px; -fx-padding: 8 16; -fx-font-family: 'Google Sans'; -fx-font-weight: bold; -fx-text-fill: #3b82f6; -fx-cursor: hand; -fx-border-color: rgba(59,130,246,0.3); -fx-border-radius: 20px;");

        cbSuccessor.getItems().addAll(
                "Hoàng Kim Yến",
                "Nguyễn Văn A",
                "Lê Thị B"
        );

        box.getChildren().add(new VBox(6, format.formatLabel("Người tiếp nhận", FontWeight.BOLD, 12, "#94a3b8"), cbSuccessor));

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnCancel = getShadowBtn("Hủy", "", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> frame.getInstance().closeOverlayModal());

        Button btnConfirm = getShadowBtn("Xác nhận", "", "#3b82f6", "white", "rgba(59,130,246,0.4)");
        btnConfirm.setOnAction(e -> {
            if (cbSuccessor.getValue() == null) {
                frame.getInstance().triggerToast("Vui lòng chọn người tiếp nhận.");
                return;
            }
            frame.getInstance().closeOverlayModal();
            frame.getInstance().triggerToast("Đã chuyển giao. Đang đăng xuất...");

            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> System.exit(0));
            pause.play();
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().add(actions);

        rootModal.getChildren().add(box);
        frame.getInstance().showCustomModal(rootModal);
    }

    private void showLeaveModal() {
        VBox modal = format.formatSimpleModal(
                "Xác nhận rời CLB",
                "Bạn muốn rời khỏi câu lạc bộ?",
                "Hủy", "Xác nhận",
                e -> frame.getInstance().closeOverlayModal(),
                e -> {
                    frame.getInstance().closeOverlayModal();
                    frame.getInstance().triggerToast("Tạm biệt!");
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(event -> System.exit(0));
                    pause.play();
                }
        );
        frame.getInstance().showCustomModal(modal);
    }

    private void showResignModal() {
        VBox modal = format.formatSimpleModal(
                "Xác nhận từ chức",
                "Bạn sẽ trở thành thành viên thường.",
                "Hủy", "Từ chức",
                e -> frame.getInstance().closeOverlayModal(),
                e -> {
                    frame.getInstance().closeOverlayModal();
                    frame.getInstance().triggerToast("Đã cập nhật chức vụ.");
                }
        );
        frame.getInstance().showCustomModal(modal);
    }

    private void formatField(PasswordField tf, String prompt) {
        tf.setPromptText(prompt);
        tf.setStyle("-fx-background-color: rgb(208 197 244 / 0.5); -fx-background-radius: 40px; -fx-padding: 10 16; -fx-font-family: 'Google Sans'; -fx-text-inner-color: #000000;");
    }

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