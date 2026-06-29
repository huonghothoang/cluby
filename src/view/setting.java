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

    private VBox topCardsCol1;
    private VBox topCardsCol2;

    public setting() {
        VBox mainContent = new VBox(24);
        mainContent.setPadding(new Insets(32));
        mainContent.setStyle("-fx-background-color: transparent;");

        HBox topCards = new HBox(24);
        topCardsCol1 = new VBox(24); HBox.setHgrow(topCardsCol1, Priority.ALWAYS); topCardsCol1.setPrefWidth(600);
        topCardsCol2 = new VBox(24); HBox.setHgrow(topCardsCol2, Priority.ALWAYS); topCardsCol2.setPrefWidth(600);

        refreshUI();

        topCards.getChildren().addAll(topCardsCol1, topCardsCol2);
        mainContent.getChildren().add(topCards);

        format.formatScrollbar(this, mainContent, 12);
        this.setContent(mainContent);
    }

    private void refreshUI() {
        topCardsCol1.getChildren().clear();
        topCardsCol2.getChildren().clear();

        topCardsCol1.getChildren().addAll(buildProfileCard(), buildActivityCard());
        topCardsCol2.getChildren().addAll(buildSecurityCard(), buildAdvancedCard());
    }

    private void fixHover(Node node) {
        node.setOnMouseEntered(e -> node.setOpacity(0.7));
        node.setOnMouseExited(e -> {
            node.setOpacity(1.0);
            node.setScaleX(1.0);
            node.setScaleY(1.0);
        });
        if (node instanceof ComboBox) {
            ((ComboBox<?>) node).setOnShowing(e -> {
                node.setOpacity(1.0);
                node.setScaleX(1.0);
                node.setScaleY(1.0);
            });
            ((ComboBox<?>) node).setOnHidden(e -> {
                node.setOpacity(1.0);
            });
        }
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

        Button btnChangeAvt = new Button("Đổi ảnh");
        btnChangeAvt.setPrefWidth(100);
        btnChangeAvt.setPrefHeight(35);
        btnChangeAvt.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));
        btnChangeAvt.setTextFill(Color.web("#475569"));
        btnChangeAvt.setStyle("-fx-background-color: rgba(100,116,139,0.1); -fx-background-radius: 20px; -fx-cursor: hand;");
        btnChangeAvt.setOnMouseEntered(e -> btnChangeAvt.setStyle("-fx-background-color: rgba(100,116,139,0.2); -fx-background-radius: 20px; -fx-cursor: hand;"));
        btnChangeAvt.setOnMouseExited(e -> btnChangeAvt.setStyle("-fx-background-color: rgba(100,116,139,0.1); -fx-background-radius: 20px; -fx-cursor: hand;"));
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

        Button btnSave = getModalActionBtn("Lưu", "#5020d8", "white", "rgba(80,32,216,0.4)");
        btnSave.setOnAction(e -> frame.getInstance().triggerToast("Đã lưu thông tin"));

        rightBox.getChildren().add(btnSave);
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

        String roleColor = userRole.equals("Hội trưởng") ? "#f59e0b" : userRole.equals("Trưởng ban") ? "#10b981" : "#475569";
        grid.add(new VBox(4, format.formatLabel("Chức vụ", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(userRole, FontWeight.BLACK, 16, roleColor)), 1, 1);

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

        Button btnSave = getModalActionBtn("Đổi mật khẩu", "#10b981", "white", "rgba(16,185,129,0.4)");
        btnSave.setOnAction(e -> frame.getInstance().triggerToast("Đã đổi mật khẩu"));

        form.getChildren().add(btnSave);
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

        Button btn = new Button(btnText);
        btn.setPrefWidth(140);
        btn.setPrefHeight(40);
        btn.setFont(Font.font("Google Sans", FontWeight.BOLD, 13));
        btn.setTextFill(Color.web(btnColor));
        btn.setStyle("-fx-background-color: " + btnBg + "; -fx-background-radius: 20px; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 4);");
        btn.setOnMouseEntered(e -> { btn.setScaleX(1.03); btn.setScaleY(1.03); });
        btn.setOnMouseExited(e -> { btn.setScaleX(1.0); btn.setScaleY(1.0); });
        btn.setOnAction(action);

        row.getChildren().addAll(info, spacer, btn);
        return row;
    }

    private void showTransferModal() {
        StackPane rootModal = new StackPane();
        VBox box = new VBox(20);
        box.setPrefWidth(450); box.setMaxSize(450, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel("Chuyển giao quyền", FontWeight.BLACK, 20, "#1e293b");
        Label desc = format.formatLabel("Chỉ có thể chuyển giao cho thành viên thường, để tránh chồng chất nhiệm vụ. Nếu muốn chọn một trưởng ban, cần cho trưởng ban đó được ra khỏi ban.", FontWeight.MEDIUM, 13, "#64748b");
        desc.setWrapText(true);

        ComboBox<String> cbSuccessor = format.formatSortBtn("Chọn người tiếp nhận", "Hoàng Kim Yến", "Nguyễn Văn A", "Lê Thị B");
        cbSuccessor.setMaxWidth(Double.MAX_VALUE);
        fixHover(cbSuccessor);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(16, 0, 0, 0));

        Button btnCancel = getModalActionBtn("Hủy", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> frame.getInstance().closeOverlayModal());

        Button btnConfirm = getModalActionBtn("Xác nhận", "#3b82f6", "white", "rgba(59,130,246,0.4)");
        btnConfirm.setOnAction(e -> {
            if (cbSuccessor.getValue() == null || cbSuccessor.getValue().equals("Chọn người tiếp nhận")) {
                frame.getInstance().triggerToast("Vui lòng chọn người tiếp nhận.");
                return;
            }
            frame.getInstance().closeOverlayModal();
            frame.getInstance().triggerToast("Đã chuyển giao. Bạn đã trở thành thành viên thường.");
            userRole = "Thành viên";
            refreshUI();
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().addAll(title, desc, new VBox(6, format.formatLabel("Người tiếp nhận", FontWeight.BOLD, 12, "#94a3b8"), cbSuccessor), actions);
        rootModal.getChildren().add(box);
        frame.getInstance().showCustomModal(rootModal);
    }

    private void showLeaveModal() {
        if (userRole.equals("Hội trưởng")) {
            StackPane rootModal = new StackPane();
            VBox box = new VBox(20);
            box.setPrefWidth(420); box.setMaxSize(420, Region.USE_PREF_SIZE);
            box.setPadding(new Insets(32));
            box.setStyle("-fx-background-color: white; -fx-background-radius: 40px;");
            box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

            Label title = format.formatLabel("Không thể rời CLB", FontWeight.BLACK, 20, "#ef4444");
            Label desc = format.formatLabel("Bạn chưa chuyển giao vai trò Hội trưởng. Vui lòng chuyển giao quyền lực và trở thành thành viên thường trước khi rời câu lạc bộ.", FontWeight.MEDIUM, 13, "#64748b");
            desc.setWrapText(true);

            HBox actions = new HBox(12);
            actions.setAlignment(Pos.CENTER);
            actions.setPadding(new Insets(16, 0, 0, 0));

            Button btnClose = getModalActionBtn("Đã hiểu", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
            btnClose.setOnAction(e -> frame.getInstance().closeOverlayModal());
            actions.getChildren().add(btnClose);

            box.getChildren().addAll(title, desc, actions);
            rootModal.getChildren().add(box);
            frame.getInstance().showCustomModal(rootModal);
        } else {
            StackPane rootModal = new StackPane();
            VBox box = new VBox(20);
            box.setPrefWidth(400); box.setMaxSize(400, Region.USE_PREF_SIZE);
            box.setPadding(new Insets(32));
            box.setStyle("-fx-background-color: white; -fx-background-radius: 40px;");
            box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

            Label title = format.formatLabel("Xác nhận rời CLB", FontWeight.BLACK, 20, "#ef4444");
            Label desc = format.formatLabel("Bạn có chắc chắn muốn rời khỏi câu lạc bộ không? Bạn sẽ không thể hoàn tác hành động này.", FontWeight.MEDIUM, 13, "#64748b");
            desc.setWrapText(true);

            HBox actions = new HBox(12);
            actions.setAlignment(Pos.CENTER);
            actions.setPadding(new Insets(16, 0, 0, 0));

            Button btnCancel = getModalActionBtn("Hủy", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
            btnCancel.setOnAction(e -> frame.getInstance().closeOverlayModal());
            Button btnConfirm = getModalActionBtn("Xác nhận", "#ef4444", "white", "rgba(239,68,68,0.4)");
            btnConfirm.setOnAction(e -> {
                frame.getInstance().closeOverlayModal();
                frame.getInstance().triggerToast("Tạm biệt!");
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(event -> System.exit(0));
                pause.play();
            });
            actions.getChildren().addAll(btnCancel, btnConfirm);

            box.getChildren().addAll(title, desc, actions);
            rootModal.getChildren().add(box);
            frame.getInstance().showCustomModal(rootModal);
        }
    }

    private void showResignModal() {
        StackPane rootModal = new StackPane();
        VBox box = new VBox(20);
        box.setPrefWidth(420); box.setMaxSize(420, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel("Xác nhận từ chức", FontWeight.BLACK, 20, "#f59e0b");
        Label desc = format.formatLabel("Bạn sẽ thôi giữ vị trí quản lý hiện tại và trở thành thành viên thường.", FontWeight.MEDIUM, 13, "#64748b");
        desc.setWrapText(true);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(16, 0, 0, 0));

        Button btnCancel = getModalActionBtn("Hủy", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> frame.getInstance().closeOverlayModal());

        Button btnConfirm = getModalActionBtn("Từ chức", "#f59e0b", "white", "rgba(245,158,11,0.4)");
        btnConfirm.setOnAction(e -> {
            frame.getInstance().closeOverlayModal();
            frame.getInstance().triggerToast("Đã cập nhật chức vụ.");
            userRole = "Thành viên";
            refreshUI();
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().addAll(title, desc, actions);
        rootModal.getChildren().add(box);
        frame.getInstance().showCustomModal(rootModal);
    }

    private void formatField(PasswordField tf, String prompt) {
        tf.setPromptText(prompt);
        tf.setStyle("-fx-background-color: rgb(208 197 244 / 0.5); -fx-background-radius: 40px; -fx-padding: 12 16; -fx-font-family: 'Google Sans'; -fx-text-inner-color: #000000; -fx-prompt-text-fill: #64748b; -fx-border-width: 0px;");
    }

    private Button getModalActionBtn(String text, String bgColor, String textColor, String shadowColor) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(45);
        HBox.setHgrow(btn, Priority.ALWAYS);
        btn.setFont(Font.font("Google Sans", FontWeight.BOLD, 13));
        btn.setTextFill(Color.web(textColor));
        btn.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 20px; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, " + shadowColor + ", 10, 0, 0, 4);");
        btn.setOnMouseEntered(e -> { btn.setScaleX(1.02); btn.setScaleY(1.02); });
        btn.setOnMouseExited(e -> { btn.setScaleX(1.0); btn.setScaleY(1.0); });
        return btn;
    }
}