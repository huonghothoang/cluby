package view;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class format {

    // Tạo Label: Hỗ trợ cấu hình nhanh Font chữ Google Sans, độ đậm và màu sắc.
    public static Label formatLabel(String text, FontWeight weight, int size, String hexColor) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Google Sans", weight, size));
        lbl.setTextFill(Color.web(hexColor));
        return lbl;
    }

    // Hiệu ứng Glassmorphism: Phủ lớp kính mờ bo góc lên bất kỳ Layout (Region) nào được truyền vào.
    public static void formatGlass(Region region, int radius, double opacity) {
        region.setStyle(
                "-fx-background-color: rgba(255,255,255," + opacity + ");" +
                        "-fx-background-radius: " + radius + "px;" +
                        "-fx-border-color: white;" +
                        "-fx-border-radius: " + radius + "px;"
        );
    }

    // Tạo Badge (Nhãn dán): Thường dùng để phân loại trạng thái, danh mục với màu nền và chữ linh hoạt.
    public static Label formatBadge(String text, String bgColor, String textColor) {
        Label badge = new Label(text);
        badge.setStyle(
                "-fx-background-color: " + bgColor + ";" +
                        "-fx-text-fill: " + textColor + ";" +
                        "-fx-font-size: 10px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 4 10 4 10;" +
                        "-fx-background-radius: 12px;"
        );
        return badge;
    }

    // Tạo Box Card (Thẻ chất lỏng): Trả về một VBox được bo góc sâu, đổ bóng nhẹ nhàng dùng làm khung chứa chính (Liquid Card).
    public static VBox formatBoxCard() {
        VBox box = new VBox(20);
        box.setPadding(new Insets(28));
        box.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.42);" +
                        "-fx-background-radius: 40px;" +
                        "-fx-border-color: rgba(255, 255, 255, 0.65);" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 40px;"
        );
        box.setEffect(new DropShadow(30, 0, 15, Color.web("#7c4dff", 0.08)));
        return box;
    }

    // Định dạng Thanh cuộn: Tàng hình viền, tùy biến thanh nắm kéo (Thumb) mỏng, ngắn gọn và bo tròn theo phong cách Gemini.
    public static void formatScrollbar(ScrollPane scrollPane, VBox contentBox, int paddingRight) {
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-padding: 0;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        if (contentBox != null) contentBox.setPadding(new Insets(0, paddingRight, 0, 0));

        String css = ".scroll-pane > .viewport { -fx-background-color: transparent; } " +
                ".scroll-bar:vertical { -fx-background-color: transparent; -fx-pref-width: 12px; } " +
                ".scroll-bar:vertical .track { -fx-background-color: transparent; } " +
                ".scroll-bar:vertical .thumb { -fx-background-color: rgba(124,77,255,0.4); -fx-background-radius: 10px; -fx-max-height: 20px; } " +
                ".scroll-bar:vertical .thumb:hover { -fx-background-color: rgba(124,77,255,0.8); } " +
                ".scroll-bar:vertical .increment-button, .scroll-bar:vertical .decrement-button { -fx-padding: 0; }";
        scrollPane.getStylesheets().add("data:text/css," + css.replaceAll(" ", "%20"));
    }

    // Tạo TextField tiêu chuẩn: Trường nhập liệu bo góc, viền trong suốt dùng cho các Form điền thông tin.
    public static TextField formatTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle(
                "-fx-background-color: rgb(208 197 244 / 0.5);" +
                        "-fx-background-radius: 40px;" +
                        "-fx-border-color: transparent;" +
                        "-fx-border-width: 0px;" +
                        "-fx-padding: 10 16 10 16;" +
                        "-fx-font-family: 'Google Sans';"
        );
        return tf;
    }

    // Tạo Nút bấm tiêu chuẩn: Được tái chế từ nút "Yêu cầu Footage", tích hợp sẵn icon, gradient nền và hiệu ứng nổi (hover).
    public static Button formatBtn(String text, String iconStr) {
        Button btn = new Button();
        HBox content = new HBox(8);
        content.setAlignment(Pos.CENTER);

        Label lblText = new Label(text);
        lblText.setTextFill(Color.WHITE);
        lblText.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));

        Label lblIcon = new Label(iconStr);
        lblIcon.setTextFill(Color.WHITE);

        content.getChildren().addAll(lblText, lblIcon);
        btn.setGraphic(content);
        btn.setPadding(new Insets(10, 20, 10, 20));

        btn.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #7c4dff, #448aff);" +
                        "-fx-background-radius: 20px;" +
                        "-fx-border-color: transparent;" +
                        "-fx-border-width: 0px;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.15), 15, 0.35, 0, 6);" +
                        "-fx-cursor: hand;"
        );

        btn.setOnMouseEntered(e -> { btn.setScaleX(1.05); btn.setScaleY(1.05); });
        btn.setOnMouseExited(e -> { btn.setScaleX(1.0); btn.setScaleY(1.0); });
        return btn;
    }

    // Tạo Nút tìm kiếm (Kính lúp): Dùng cho các thanh tìm kiếm nhỏ gọn.
    public static Button formatFindBtn() {
        Button btn = new Button("🔍");
        btn.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));
        btn.setTextFill(Color.WHITE);
        btn.setPrefHeight(40);
        btn.setStyle(
                "-fx-background-color: #5020d8;" +
                        "-fx-background-radius: 40px;" +
                        "-fx-padding: 0 16 0 16;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.15), 10, 0.3, 0, 4);"
        );
        btn.setOnMouseEntered(e -> { btn.setScaleX(1.05); btn.setScaleY(1.05); });
        btn.setOnMouseExited(e -> { btn.setScaleX(1.0); btn.setScaleY(1.0); });
        return btn;
    }

    // Tạo nút Menu Điều hướng (Sidebar): Nạp cấu trúc Text và Icon nhưng chưa đổ màu nền (Xử lý nền ở trạng thái Active/Inactive).
    public static Button formatNavBtn(String emoji, String title, boolean isSubItem) {
        Button btn = new Button();
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(isSubItem ? 32 : 36);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(isSubItem ? new Insets(6, 12, 6, 36) : new Insets(8, 12, 8, 12));

        HBox graphicBox = new HBox(8);
        graphicBox.setAlignment(Pos.CENTER_LEFT);

        if (!emoji.isEmpty()) {
            Label iconLbl = new Label(emoji);
            iconLbl.setStyle("-fx-font-size: " + (isSubItem ? "14px;" : "16px;"));
            Label titleLbl = new Label(title);
            titleLbl.setFont(Font.font("Google Sans", isSubItem ? FontWeight.MEDIUM : FontWeight.BOLD, 12));
            graphicBox.getChildren().addAll(iconLbl, titleLbl);
        } else {
            Label titleLbl = new Label(title);
            titleLbl.setFont(Font.font("Google Sans", isSubItem ? FontWeight.MEDIUM : FontWeight.BOLD, 12));
            graphicBox.getChildren().add(titleLbl);
        }
        btn.setGraphic(graphicBox);
        return btn;
    }

    // Ép kiểu Active cho Nút Menu: Kích hoạt nền gradient sáng và đổi màu chữ thành tím.
    public static void formatNavBtnActive(Button btn) {
        btn.setStyle(
                "-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, rgba(255,255,255,0.92) 0%, rgba(255,255,255,0.65) 100%);" +
                        "-fx-background-radius: 12px; -fx-border-color: transparent; -fx-border-width: 0px;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.12), 10, 0.4, 0, 4);"
        );
        if (btn.getGraphic() instanceof HBox) {
            for (Node n : ((HBox) btn.getGraphic()).getChildren()) {
                if (n instanceof Label) ((Label)n).setTextFill(Color.web("#7c4dff"));
            }
        }
    }

    // Ép kiểu Inactive cho Nút Menu: Xóa nền (trong suốt) và trả màu chữ về xám sẫm.
    public static void formatNavBtnInactive(Button btn) {
        btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 12px; -fx-border-color: transparent; -fx-border-width: 0px;");
        if (btn.getGraphic() instanceof HBox) {
            for (Node n : ((HBox) btn.getGraphic()).getChildren()) {
                if (n instanceof Label) ((Label)n).setTextFill(Color.web("#475569"));
            }
        }
    }

    // Quản lý Toast Notification: Bắn thông báo nổi góc phải kèm hiệu ứng trượt/nổi dần (Fade In/Slide) tự động tắt sau 3.5s.
    public static void formatToast(VBox toastContainer, String msg) {
        HBox toast = new HBox(12);
        toast.setAlignment(Pos.CENTER_LEFT);
        toast.setPadding(new Insets(12, 20, 12, 20));
        toast.setStyle(
                "-fx-background-color: rgba(255,255,255,0.92); -fx-background-radius: 40px;" +
                        "-fx-border-color: transparent; -fx-border-width: 0px; -fx-font-family: 'Google Sans';" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.12), 20, 0.4, 0, 8);"
        );

        Label icon = new Label("✨");
        icon.setStyle("-fx-text-fill: #000000; -fx-font-size: 14px;");
        Label text = new Label(msg);
        text.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));
        text.setTextFill(Color.web("#1e293b"));
        toast.getChildren().addAll(icon, text);
        toastContainer.getChildren().add(toast);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(350), toast);
        fadeIn.setFromValue(0); fadeIn.setToValue(1); fadeIn.play();

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(350), toast);
        slideIn.setFromY(15); slideIn.setToY(0); slideIn.play();

        FadeTransition fadeOut = new FadeTransition(Duration.millis(400), toast);
        fadeOut.setFromValue(1); fadeOut.setToValue(0);
        fadeOut.setDelay(Duration.seconds(3.5));

        TranslateTransition slideOut = new TranslateTransition(Duration.millis(400), toast);
        slideOut.setFromY(0); slideOut.setToY(15);
        slideOut.setDelay(Duration.seconds(3.5));

        fadeOut.setOnFinished(e -> toastContainer.getChildren().remove(toast));
        fadeOut.play(); slideOut.play();
    }

    // Modal cơ bản (Đăng xuất/Xác nhận): Gồm 1 Tiêu đề, 1 Phụ đề và 2 nút bấm với action tùy biến.
    public static VBox formatSimpleModal(String titleText, String subText, String cancelText, String confirmText, EventHandler<ActionEvent> cancelAction, EventHandler<ActionEvent> confirmAction) {
        VBox box = new VBox(16);
        box.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(28));
        box.setStyle("-fx-background-color: rgba(255,255,255); -fx-background-radius: 40px; -fx-border-color: transparent; -fx-border-width: 0px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = formatLabel(titleText, FontWeight.BLACK, 22, "#1e293b");
        Label sub = formatLabel(subText, FontWeight.MEDIUM, 11, "#94a3b8");

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);
        actions.setPadding(new Insets(10, 0, 0, 0));

        Button btnCancel = new Button(cancelText);
        btnCancel.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));
        btnCancel.setTextFill(Color.web("#64748b"));
        btnCancel.setStyle("-fx-background-color: rgb(178, 162, 228,0.2); -fx-background-radius: 40px; -fx-padding: 8 16 8 16; -fx-cursor: hand;");
        btnCancel.setOnMouseEntered(e -> { btnCancel.setScaleX(1.05); btnCancel.setScaleY(1.05); });
        btnCancel.setOnMouseExited(e -> { btnCancel.setScaleX(1.0); btnCancel.setScaleY(1.0); });
        if(cancelAction != null) btnCancel.setOnAction(cancelAction);

        Button btnConfirm = new Button(confirmText);
        btnConfirm.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));
        btnConfirm.setTextFill(Color.WHITE);
        btnConfirm.setStyle("-fx-background-color: #ef4444; -fx-background-radius: 40px; -fx-padding: 8 16 8 16; -fx-effect: dropshadow(three-pass-box, rgba(239,68,68,0.3), 10, 0.3, 0, 6); -fx-cursor: hand;");
        btnConfirm.setOnMouseEntered(e -> { btnConfirm.setScaleX(1.05); btnConfirm.setScaleY(1.05); });
        btnConfirm.setOnMouseExited(e -> { btnConfirm.setScaleX(1.0); btnConfirm.setScaleY(1.0); });
        if(confirmAction != null) btnConfirm.setOnAction(confirmAction);

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().addAll(title, sub, actions);
        return box;
    }

    // Modal Phức tạp (Nhập form như Request Footage): Chứa nhiều khung nhập liệu (TextField) và 2 nút hành động.
    public static VBox formatComplexModal(String titleText, String subText, EventHandler<ActionEvent> cancelAction, EventHandler<ActionEvent> confirmAction) {
        VBox box = new VBox(16);
        box.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(28));
        box.setStyle("-fx-background-color: rgba(255,255,255); -fx-background-radius: 40px; -fx-border-color: transparent; -fx-border-width: 0px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = formatLabel(titleText, FontWeight.BLACK, 22, "#1e293b");
        Label sub = formatLabel(subText, FontWeight.MEDIUM, 11, "#94a3b8");

        VBox fields = new VBox(12);
        TextField fName = formatTextField("Tên Sự Kiện / Đề Tài (Ví dụ: Đại nhạc hội Glimpz 2026)");
        TextField fLoc = formatTextField("Địa Điểm (Ví dụ: Đà Nẵng, Việt Nam)");

        HBox row = new HBox(12);
        TextField fBudget = formatTextField("Ngân Sách Dịch Vụ ($)");
        TextField fDeadline = formatTextField("Hạn Chót (YYYY-MM-DD)");
        HBox.setHgrow(fBudget, Priority.ALWAYS);
        HBox.setHgrow(fDeadline, Priority.ALWAYS);
        row.getChildren().addAll(fBudget, fDeadline);

        fields.getChildren().addAll(fName, fLoc, row);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnCancel = new Button("Huỷ");
        btnCancel.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));
        btnCancel.setTextFill(Color.web("#64748b"));
        btnCancel.setStyle("-fx-background-color: rgb(178, 162, 228,0.2); -fx-background-radius: 40px; -fx-padding: 8 16 8 16; -fx-cursor: hand;");
        btnCancel.setOnMouseEntered(e -> { btnCancel.setScaleX(1.05); btnCancel.setScaleY(1.05); });
        btnCancel.setOnMouseExited(e -> { btnCancel.setScaleX(1.0); btnCancel.setScaleY(1.0); });
        if(cancelAction != null) btnCancel.setOnAction(cancelAction);

        Button btnConfirm = new Button("Gửi Yêu Cầu");
        btnConfirm.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));
        btnConfirm.setTextFill(Color.WHITE);
        btnConfirm.setStyle("-fx-background-color: #5020d8; -fx-background-radius: 40px; -fx-padding: 8 16 8 16; -fx-effect: dropshadow(three-pass-box, rgb(178, 162, 228,0.2), 10, 0.3, 0, 6); -fx-cursor: hand;");
        btnConfirm.setOnMouseEntered(e -> { btnConfirm.setScaleX(1.05); btnConfirm.setScaleY(1.05); });
        btnConfirm.setOnMouseExited(e -> { btnConfirm.setScaleX(1.0); btnConfirm.setScaleY(1.0); });
        if(confirmAction != null) btnConfirm.setOnAction(confirmAction);

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().addAll(title, sub, fields, actions);
        return box;
    }
}