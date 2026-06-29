package view.president;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import view.format;

public class request extends ScrollPane {

    public request() {
        VBox mainContent = new VBox(24);
        mainContent.setPadding(new Insets(32));
        mainContent.setStyle("-fx-background-color: transparent;");

        HBox kpiRow = new HBox(16);
        VBox card1 = createStatCard("Tổng số", "128", "#475569", "#1e293b", "#a5b4fc", "📝");
        VBox card2 = createStatCard("Chờ duyệt", "18", "#475569", "#1e293b", "#fde68a", "⏳");
        VBox card3 = createStatCard("Đạt", "84", "#475569", "#1e293b", "#6ee7b7", "✔");
        VBox card4 = createStatCard("Từ chối", "26", "#475569", "#1e293b", "#fca5a5", "✖");

        HBox.setHgrow(card1, Priority.ALWAYS); HBox.setHgrow(card2, Priority.ALWAYS);
        HBox.setHgrow(card3, Priority.ALWAYS); HBox.setHgrow(card4, Priority.ALWAYS);
        kpiRow.getChildren().addAll(card1, card2, card3, card4);

        HBox filterBar = new HBox(16);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(12, 24, 12, 24));
        format.formatGlass(filterBar, 40, 0.4);

        HBox searchBox = new HBox(8);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = format.formatTextField("Tìm kiếm...");
        Button btnSearch = format.formatFindBtn();
        searchBox.getChildren().addAll(searchField, btnSearch);

        ComboBox<String> cbStatus = format.formatSortBtn("Trạng thái", "Tất cả", "Chờ duyệt", "Đã duyệt", "Từ chối");
        ComboBox<String> cbDept = format.formatSortBtn("Bộ phận", "Tất cả", "Nội dung", "Kỹ thuật", "Truền thông", "Hậu cần");
        ComboBox<String> cbTime = format.formatSortBtn("Thời gian", "Tất cả", "Tuần này", "Tháng này", "Năm nay");

        fixHover(cbStatus); fixHover(cbDept); fixHover(cbTime);

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        filterBar.getChildren().addAll(searchBox, cbStatus, cbDept, cbTime, spacer);

        VBox tableContainer = format.formatTableContainer();
        tableContainer.getChildren().add(createTableHeader());

        VBox rows = new VBox(4);
        rows.getChildren().addAll(
                createAppRow("temp.png", "Nguyễn Văn A", "vanya@gmail.com", "0901234567", "Kỹ thuật", "26/06/2026", "Chờ duyệt", "Sinh viên ngành công nghệ, mong muốn rèn luyện kỹ năng lập trình.", "Có kinh nghiệm lập trình cơ bản qua một số dự án môn học.", "Muốn học hỏi quy trình làm việc thực tế và đóng góp cho nhóm."),
                createAppRow("temp.png", "Trần Thu Hà", "ha.tt@gmail.com", "0988776655", "Nội dung", "25/06/2026", "Đã duyệt", "Yêu thích viết lách, xây dựng kịch bản và sáng tạo nội dung.", "Từng quản lý nội dung tuyến bài đăng cho fanpage trường.", "Muốn tìm kiếm môi trường năng động để thử thách bản thân."),
                createAppRow("temp.png", "Lê Đức Anh", "anh.ld@gmail.com", "0123456789", "Truyền thông", "20/06/2026", "Từ chối", "Năng nổ, nhiệt tình và thích các hoạt động cộng đồng.", "Chưa có kinh nghiệm thực tế về mảng truyền thông, báo chí.", "Muốn kết nối thêm nhiều bạn bè và học hỏi kỹ năng mới.")
        );

        ScrollPane scrollList = new ScrollPane(rows);
        scrollList.setPrefHeight(450);
        format.formatScrollbar(scrollList, rows, 8);
        applySmoothScroll(scrollList, rows);
        tableContainer.getChildren().add(scrollList);

        mainContent.getChildren().addAll(kpiRow, filterBar, tableContainer);
        format.formatScrollbar(this, mainContent, 12);
        this.setContent(mainContent);
    }

    private void applySmoothScroll(ScrollPane scroll, VBox content) {
        scroll.addEventFilter(javafx.scene.input.ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() != 0) {
                event.consume();
                double vvalue = scroll.getVvalue();
                double delta = event.getDeltaY() * 2.5;
                double contentHeight = content.getBoundsInLocal().getHeight();
                double viewportHeight = scroll.getViewportBounds().getHeight();
                if (contentHeight > viewportHeight) scroll.setVvalue(vvalue - delta / (contentHeight - viewportHeight));
            }
        });
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

    private VBox createStatCard(String title, String value, String titleColor, String valColor, String iconBg, String iconEmoji) {
        VBox box = format.formatBoxCard();
        box.setPadding(new Insets(20));
        box.setMinHeight(Region.USE_PREF_SIZE);
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        VBox textCol = new VBox(4);
        textCol.getChildren().addAll(format.formatLabel(title, FontWeight.BOLD, 12, titleColor), format.formatLabel(value, FontWeight.BLACK, 28, valColor));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        StackPane iconContainer = new StackPane();
        iconContainer.getChildren().addAll(new Circle(24, Color.web(iconBg)), format.formatLabel(iconEmoji, FontWeight.NORMAL, 20, "#000000"));
        content.getChildren().addAll(textCol, spacer, iconContainer);
        box.getChildren().add(content);
        return box;
    }

    private HBox createTableHeader() {
        HBox header = new HBox(16);
        header.setPadding(new Insets(12, 16, 12, 16));
        header.setStyle("-fx-border-color: transparent transparent rgba(255,255,255,0.4) transparent; -fx-border-width: 1px;");
        Label l1 = format.formatLabel("", FontWeight.BLACK, 10, "#94a3b8"); l1.setPrefWidth(60);
        Label l2 = format.formatLabel("ỨNG VIÊN", FontWeight.BLACK, 10, "#94a3b8"); l2.setPrefWidth(200);
        Label l3 = format.formatLabel("EMAIL", FontWeight.BLACK, 10, "#94a3b8"); l3.setPrefWidth(180);
        Label l4 = format.formatLabel("BỘ PHẬN ĐĂNG KÝ", FontWeight.BLACK, 10, "#94a3b8"); l4.setPrefWidth(120);
        Label l5 = format.formatLabel("NGÀY NỘP", FontWeight.BLACK, 10, "#94a3b8"); l5.setPrefWidth(100);
        Label l6 = format.formatLabel("TRẠNG THÁI", FontWeight.BLACK, 10, "#94a3b8"); l6.setPrefWidth(120);
        Label l7 = format.formatLabel("", FontWeight.BLACK, 10, "#94a3b8"); l7.setPrefWidth(60);
        header.getChildren().addAll(l1, l2, l3, l4, l5, l6, l7);
        return header;
    }

    private HBox createAppRow(String avatarUrl, String name, String email, String phone, String targetDept, String date, String status, String intro, String exp, String reason) {
        HBox row = new HBox(16);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand;");

        row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand; -fx-background-radius: 16px;"));
        row.setOnMouseExited(e -> row.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand;"));

        ImageView avatar = new ImageView(new Image(avatarUrl));
        avatar.setFitWidth(40); avatar.setFitHeight(40); avatar.setClip(new Circle(20, 20, 20));
        HBox avatarBox = new HBox(avatar); avatarBox.setPrefWidth(60);

        Label lblName = format.formatLabel(name, FontWeight.BOLD, 14, "#1e293b"); lblName.setPrefWidth(200);
        Label lblEmail = format.formatLabel(email, FontWeight.MEDIUM, 13, "#475569"); lblEmail.setPrefWidth(180);
        Label lblDept = format.formatLabel(targetDept, FontWeight.BOLD, 13, "#7c4dff"); lblDept.setPrefWidth(120);
        Label lblDate = format.formatLabel(date, FontWeight.MEDIUM, 13, "#94a3b8"); lblDate.setPrefWidth(100);

        String stBg = status.equals("Đã duyệt") ? "rgba(16,185,129,0.15)" : status.equals("Chờ duyệt") ? "rgba(245,158,11,0.15)" : "rgba(239,68,68,0.15)";
        String stText = status.equals("Đã duyệt") ? "#10b981" : status.equals("Chờ duyệt") ? "#f59e0b" : "#ef4444";
        HBox statusBox = new HBox(format.formatBadge(status, stBg, stText)); statusBox.setAlignment(Pos.CENTER_LEFT); statusBox.setPrefWidth(120);

        Button btnView = format.formatCircleBtn("👁️‍🗨️", "#448aff", "#7c4dff");
        btnView.setOnAction(e -> {
            StackPane detailModal = createAppDetailModal(avatarUrl, name, email, phone, targetDept, date, status, intro, exp, reason);
            frame.getInstance().showCustomModal(detailModal);
        });

        HBox actionBox = new HBox(btnView); actionBox.setAlignment(Pos.CENTER_LEFT); actionBox.setPrefWidth(60);

        row.getChildren().addAll(avatarBox, lblName, lblEmail, lblDept, lblDate, statusBox, actionBox);
        return row;
    }

    private StackPane createAppDetailModal(String avatarUrl, String name, String email, String phone, String targetDept, String date, String status, String intro, String exp, String reason) {
        StackPane rootModalPane = new StackPane();

        VBox box = new VBox(20);
        box.setPrefWidth(740);
        box.setMaxSize(740, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        Label lblTitle = format.formatLabel("Đơn ứng tuyển", FontWeight.BLACK, 24, "#1e293b");
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        String stBg = status.equals("Đã duyệt") ? "rgba(16,185,129,0.15)" : status.equals("Chờ duyệt") ? "rgba(245,158,11,0.15)" : "rgba(239,68,68,0.15)";
        String stText = status.equals("Đã duyệt") ? "#10b981" : status.equals("Chờ duyệt") ? "#f59e0b" : "#ef4444";
        Label statusBadge = format.formatBadge(status, stBg, stText);
        statusBadge.setStyle(statusBadge.getStyle() + "-fx-font-size: 14px; -fx-padding: 6 12;");
        header.getChildren().addAll(lblTitle, spacer, statusBadge);

        VBox contentBody = new VBox(24);

        HBox personalInfo = new HBox(20);
        personalInfo.setAlignment(Pos.CENTER_LEFT);
        ImageView avt = new ImageView(new Image(avatarUrl));
        avt.setFitWidth(70); avt.setFitHeight(70); avt.setClip(new Circle(35, 35, 35));

        VBox pDetails = new VBox(8);
        pDetails.getChildren().addAll(
                format.formatLabel(name, FontWeight.BLACK, 18, "#1e293b"),
                new HBox(24,
                        new VBox(2, format.formatLabel("Email", FontWeight.BOLD, 10, "#94a3b8"), format.formatLabel(email, FontWeight.BOLD, 14, "#475569")),
                        new VBox(2, format.formatLabel("Số điện thoại", FontWeight.BOLD, 10, "#94a3b8"), format.formatLabel(phone, FontWeight.BOLD, 14, "#475569"))
                )
        );
        personalInfo.getChildren().addAll(avt, pDetails);

        VBox appInfo = format.formatBoxCard();
        appInfo.setPadding(new Insets(24));
        appInfo.getChildren().add(format.formatLabel("NỘI DUNG ĐƠN", FontWeight.BLACK, 12, "#94a3b8"));

        GridPane appGrid = new GridPane();
        appGrid.setHgap(40); appGrid.setVgap(16);
        appGrid.add(new VBox(4, format.formatLabel("Bộ phận đăng ký", FontWeight.BOLD, 11, "#94a3b8"), format.formatLabel(targetDept, FontWeight.BOLD, 16, "#7c4dff")), 0, 0);
        appGrid.add(new VBox(4, format.formatLabel("Ngày nộp", FontWeight.BOLD, 11, "#94a3b8"), format.formatLabel(date, FontWeight.BOLD, 16, "#475569")), 1, 0);
        appInfo.getChildren().add(appGrid);

        appInfo.getChildren().addAll(
                createSection("Giới thiệu", intro),
                createSection("Kinh nghiệm", exp),
                createSection("Lý do tham gia", reason)
        );

        VBox noteBox = new VBox(8);
        noteBox.getChildren().add(format.formatLabel("GHI CHÚ", FontWeight.BOLD, 11, "#94a3b8"));
        TextField fNote = format.formatTextField("Nhập đánh giá...");
        if (!status.equals("Chờ duyệt")) {
            fNote.setText("Đã xử lý: " + date + ".");
            fNote.setDisable(true);
        }
        noteBox.getChildren().add(fNote);

        contentBody.getChildren().addAll(personalInfo, appInfo, noteBox);
        ScrollPane scrollContent = new ScrollPane(contentBody);
        scrollContent.setPrefHeight(380);
        format.formatScrollbar(scrollContent, contentBody, 8);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(16, 0, 0, 0));

        Button btnClose = getModalActionBtn("Đóng", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnClose.setOnAction(e -> frame.getInstance().closeOverlayModal());

        if (status.equals("Chờ duyệt")) {
            Button btnReject = getModalActionBtn("Từ chối", "rgba(239,68,68,0.1)", "#ef4444", "rgba(0,0,0,0.1)");
            btnReject.setOnAction(e -> {
                VBox rejectModal = createRejectModal(name, rootModalPane, box);
                rootModalPane.getChildren().setAll(rejectModal);
            });

            Button btnApprove = getModalActionBtn("Duyệt", "#5020d8", "white", "rgba(80,32,216,0.4)");
            btnApprove.setOnAction(e -> {
                VBox approveModal = createCampFormModal(name, targetDept, rootModalPane, box);
                rootModalPane.getChildren().setAll(approveModal);
            });
            actions.getChildren().addAll(btnClose, btnReject, btnApprove);
        } else {
            actions.getChildren().add(btnClose);
        }

        box.getChildren().addAll(header, scrollContent, actions);
        rootModalPane.getChildren().add(box);
        return rootModalPane;
    }

    private VBox createSection(String title, String content) {
        VBox box = new VBox(4);
        box.setPadding(new Insets(8, 0, 0, 0));
        Label lblTitle = format.formatLabel(title, FontWeight.BOLD, 12, "#94a3b8");
        Label lblContent = format.formatLabel(content, FontWeight.MEDIUM, 14, "#1e293b");
        lblContent.setWrapText(true);
        box.getChildren().addAll(lblTitle, lblContent);
        return box;
    }

    private VBox createCampFormModal(String name, String targetDept, StackPane rootModalPane, VBox previousView) {
        VBox box = new VBox(20);
        box.setPrefWidth(420);
        box.setMaxSize(420, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel("Duyệt đơn ứng tuyển", FontWeight.BLACK, 20, "#5020d8");
        Label sub = format.formatLabel("Thiết lập thông tin tài khoản thành viên mới để hoàn tất.", FontWeight.MEDIUM, 12, "#64748b");
        sub.setWrapText(true);

        VBox fields = new VBox(16);
        fields.getChildren().add(new VBox(4, format.formatLabel("Họ tên", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(name, FontWeight.BLACK, 16, "#1e293b")));

        ComboBox<String> cbDept = format.formatSortBtn("Chọn bộ phận", "Nội dung", "Kỹ thuật", "Truyền thông", "Hậu cần", "Chưa phân ban");
        cbDept.setValue(targetDept);
        cbDept.setMaxWidth(Double.MAX_VALUE);
        fixHover(cbDept);
        fields.getChildren().add(new VBox(4, format.formatLabel("Bộ phận trực thuộc", FontWeight.BOLD, 12, "#94a3b8"), cbDept));

        ComboBox<String> cbRole = format.formatSortBtn("Chọn chức vụ", "Hội trưởng", "Trưởng ban", "Thành viên");
        cbRole.setValue("Thành viên");
        cbRole.setMaxWidth(Double.MAX_VALUE);
        fixHover(cbRole);
        fields.getChildren().add(new VBox(4, format.formatLabel("Chức vụ cấp quyền", FontWeight.BOLD, 12, "#94a3b8"), cbRole));

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        TextField fDate = format.formatTextField(today); fDate.setDisable(true);
        fields.getChildren().add(new VBox(4, format.formatLabel("Ngày gia nhập", FontWeight.BOLD, 12, "#94a3b8"), fDate));

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(16, 0, 0, 0));

        Button btnCancel = getModalActionBtn("Quay lại", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> rootModalPane.getChildren().setAll(previousView));

        Button btnConfirm = getModalActionBtn("Lưu hồ sơ", "#5020d8", "white", "rgba(80,32,216,0.4)");
        btnConfirm.setOnAction(e -> {
            frame.getInstance().closeOverlayModal();
            frame.getInstance().triggerToast("Đã duyệt đơn và tạo tài khoản thành viên thành công");
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().addAll(title, sub, fields, actions);
        return box;
    }

    private VBox createRejectModal(String name, StackPane rootModalPane, VBox previousView) {
        VBox box = new VBox(20);
        box.setPrefWidth(420);
        box.setMaxSize(420, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel("Từ chối hồ sơ", FontWeight.BLACK, 20, "#ef4444");
        Label sub = format.formatLabel("Xác nhận không tiếp nhận ứng viên " + name + ".", FontWeight.MEDIUM, 12, "#64748b");

        VBox fields = new VBox(8);
        fields.getChildren().add(format.formatLabel("Lý do từ chối", FontWeight.BOLD, 12, "#94a3b8"));
        TextField fReason = format.formatTextField("Nhập lý do...");
        fields.getChildren().add(fReason);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(16, 0, 0, 0));

        Button btnCancel = getModalActionBtn("Quay lại", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> rootModalPane.getChildren().setAll(previousView));

        Button btnConfirm = getModalActionBtn("Xác nhận", "#ef4444", "white", "rgba(239,68,68,0.4)");
        btnConfirm.setOnAction(e -> {
            frame.getInstance().closeOverlayModal();
            frame.getInstance().triggerToast("Đã từ chối đơn ứng tuyển");
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().addAll(title, sub, fields, actions);
        return box;
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