package view.president;

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
import view.format;
import view.president.frame;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class request extends ScrollPane {

    // Khởi tạo giao diện chính quản lý danh sách và các bộ lọc đơn ứng tuyển của câu lạc bộ
    public request() {
        VBox mainContent = new VBox(24);
        mainContent.setPadding(new Insets(32));
        mainContent.setStyle("-fx-background-color: transparent;");

        HBox kpiRow = new HBox(16);
        kpiRow.getChildren().addAll(
                format.formatKPICard("Tổng đơn nộp", "128", "#64748b", "#1e293b"),
                format.formatKPICard("🟡 Đang chờ duyệt", "18", "#f59e0b", "#1e293b"),
                format.formatKPICard("🟢 Đã duyệt (Đạt)", "84", "#10b981", "#1e293b"),
                format.formatKPICard("🔴 Đã từ chối", "26", "#ef4444", "#1e293b")
        );
        for (javafx.scene.Node node : kpiRow.getChildren()) { HBox.setHgrow(node, Priority.ALWAYS); }

        HBox filterBar = new HBox(16);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(12, 24, 12, 24));
        format.formatGlass(filterBar, 40, 0.4);

        HBox searchBox = new HBox(8);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = format.formatTextField("Tìm theo tên ứng viên...");
        Button btnSearch = format.formatFindBtn();
        searchBox.getChildren().addAll(searchField, btnSearch);

        ComboBox<String> cbStatus = format.formatSortBtn("Lọc trạng thái", "Tất cả", "Chờ duyệt", "Đã duyệt", "Đã từ chối");
        ComboBox<String> cbDept = format.formatSortBtn("Lọc ban đăng ký", "Tất cả", "Truyền thông", "Kỹ thuật", "Sự kiện", "Nhân sự", "Đối ngoại");
        ComboBox<String> cbTime = format.formatSortBtn("Lọc thời gian", "Tất cả", "Tuần này", "Tháng này", "Năm nay");

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        filterBar.getChildren().addAll(searchBox, cbStatus, cbDept, cbTime, spacer);

        VBox tableContainer = format.formatTableContainer();
        tableContainer.getChildren().add(createTableHeader());

        tableContainer.getChildren().addAll(
                createAppRow("trish.jpeg", "Nguyễn Văn A", "nva@gmail.com", "0901234567", "Kỹ thuật", "26/06/2026", "Chờ duyệt", "Em là sinh viên IT năm 2, đam mê lập trình.", "Có kinh nghiệm code Java 1 năm qua các project môn học.", "Mong muốn được đóng góp cho CLB và học hỏi thêm kỹ năng thực tế."),
                createAppRow("trish.jpeg", "Lê Văn B", "lvb@gmail.com", "0988776655", "Truyền thông", "25/06/2026", "Đã duyệt", "Em thích viết lách và sáng tạo nội dung đa nền tảng.", "Từng viết content cho fanpage Sự kiện của trường.", "Thích môi trường năng động, muốn kết nối bạn bè."),
                createAppRow("trish.jpeg", "Trần Thị C", "ttc@gmail.com", "0123456789", "Sự kiện", "20/06/2026", "Đã từ chối", "Em rất năng nổ tham gia các hoạt động tập thể.", "Chưa có nhiều kinh nghiệm chạy sự kiện chuyên nghiệp.", "Muốn thử sức bản thân ở một vai trò mới mẻ hơn.")
        );

        mainContent.getChildren().addAll(kpiRow, filterBar, tableContainer);
        format.formatScrollbar(this, mainContent, 12);
        this.setContent(mainContent);
    }

    // Tạo thanh tiêu đề cột cho bảng tổng hợp thông tin hồ sơ ứng tuyển
    private HBox createTableHeader() {
        HBox header = new HBox(16);
        header.setPadding(new Insets(12, 16, 12, 16));
        header.setStyle("-fx-border-color: transparent transparent rgba(255,255,255,0.4) transparent; -fx-border-width: 1px;");

        Label l1 = format.formatLabel("ẢNH", FontWeight.BLACK, 10, "#94a3b8"); l1.setPrefWidth(60);
        Label l2 = format.formatLabel("HỌ TÊN", FontWeight.BLACK, 10, "#94a3b8"); l2.setPrefWidth(200);
        Label l3 = format.formatLabel("EMAIL", FontWeight.BLACK, 10, "#94a3b8"); l3.setPrefWidth(180);
        Label l4 = format.formatLabel("BAN MONG MUỐN", FontWeight.BLACK, 10, "#94a3b8"); l4.setPrefWidth(120);
        Label l5 = format.formatLabel("NGÀY NỘP", FontWeight.BLACK, 10, "#94a3b8"); l5.setPrefWidth(100);
        Label l6 = format.formatLabel("TRẠNG THÁI", FontWeight.BLACK, 10, "#94a3b8"); l6.setPrefWidth(120);
        Label l7 = format.formatLabel("THAO TÁC", FontWeight.BLACK, 10, "#94a3b8"); l7.setPrefWidth(60);

        header.getChildren().addAll(l1, l2, l3, l4, l5, l6, l7);
        return header;
    }

    // Tạo một hàng hiển thị thông tin tóm tắt của hồ sơ ứng tuyển kèm nút kích hoạt xem chi tiết
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

    // Tạo cửa sổ chi tiết hiển thị toàn bộ nội dung lý lịch, nguyện vọng và đính kèm bộ nút phê duyệt đơn ứng tuyển
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
        Label lblTitle = format.formatLabel("Chi tiết Đơn ứng tuyển", FontWeight.BLACK, 24, "#1e293b");
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
        appInfo.getChildren().add(format.formatLabel("THÔNG TIN ỨNG TUYỂN", FontWeight.BLACK, 12, "#94a3b8"));

        GridPane appGrid = new GridPane();
        appGrid.setHgap(40); appGrid.setVgap(16);
        appGrid.add(new VBox(4, format.formatLabel("Ban mong muốn", FontWeight.BOLD, 11, "#94a3b8"), format.formatLabel(targetDept, FontWeight.BOLD, 16, "#7c4dff")), 0, 0);
        appGrid.add(new VBox(4, format.formatLabel("Ngày nộp đơn", FontWeight.BOLD, 11, "#94a3b8"), format.formatLabel(date, FontWeight.BOLD, 16, "#475569")), 1, 0);
        appInfo.getChildren().add(appGrid);

        appInfo.getChildren().addAll(
                createSection("Giới thiệu bản thân", intro),
                createSection("Kinh nghiệm", exp),
                createSection("Lý do muốn tham gia", reason)
        );

        VBox noteBox = new VBox(8);
        noteBox.getChildren().add(format.formatLabel("GHI CHÚ (DÀNH CHO NGƯỜI DUYỆT)", FontWeight.BOLD, 11, "#94a3b8"));
        TextField fNote = format.formatTextField("Nhập nhận xét nội bộ...");
        if (!status.equals("Chờ duyệt")) {
            fNote.setText("Đã xử lý lúc " + date + ".");
            fNote.setDisable(true);
        }
        noteBox.getChildren().add(fNote);

        contentBody.getChildren().addAll(personalInfo, appInfo, noteBox);

        ScrollPane scrollContent = new ScrollPane(contentBody);
        scrollContent.setPrefHeight(380);
        format.formatScrollbar(scrollContent, contentBody, 8);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnClose = getShadowBtn("Đóng", "", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnClose.setOnAction(e -> frame.getInstance().closeOverlayModal());

        if (status.equals("Chờ duyệt")) {
            Button btnReject = getShadowBtn("Từ chối", "✖", "rgba(239,68,68,0.1)", "#ef4444", "rgba(0,0,0,0.1)");
            btnReject.setOnAction(e -> {
                VBox rejectModal = createRejectModal(name, rootModalPane, box);
                rootModalPane.getChildren().setAll(rejectModal);
            });

            Button btnApprove = getShadowBtn("Duyệt đơn", "✓", "#10b981", "white", "rgba(16,185,129,0.4)");
            btnApprove.setOnAction(e -> {
                VBox approveModal = createApproveModal(name, targetDept, rootModalPane, box);
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

    // Tạo cấu trúc phân vùng hiển thị tiêu đề hạng mục và nội dung văn bản đi kèm cho hồ sơ
    private VBox createSection(String title, String content) {
        VBox box = new VBox(4);
        Label lblTitle = format.formatLabel(title, FontWeight.BOLD, 12, "#94a3b8");
        Label lblContent = format.formatLabel(content, FontWeight.MEDIUM, 14, "#1e293b");
        lblContent.setWrapText(true);
        box.getChildren().addAll(lblTitle, lblContent);
        return box;
    }

    // Tạo biểu mẫu modal thiết lập hồ sơ chức vụ, phân ban để chuyển đổi thông tin ứng viên đạt yêu cầu thành thành viên chính thức
    private VBox createApproveModal(String name, String targetDept, StackPane rootModalPane, VBox previousView) {
        VBox box = new VBox(20);
        box.setPrefWidth(420);
        box.setMaxSize(420, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel("Xác nhận thêm thành viên", FontWeight.BLACK, 20, "#10b981");
        Label sub = format.formatLabel("Đơn ứng tuyển của " + name + " đã được duyệt. Vui lòng thiết lập hồ sơ thành viên mới trước khi hoàn tất.", FontWeight.MEDIUM, 12, "#64748b");
        sub.setWrapText(true);

        VBox fields = new VBox(16);
        fields.getChildren().add(new VBox(4, format.formatLabel("Họ tên", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(name, FontWeight.BLACK, 16, "#1e293b")));

        ComboBox<String> cbDept = format.formatSortBtn("Chọn Ban", "Truyền thông", "Kỹ thuật", "Sự kiện", "Nhân sự", "Đối ngoại", "Chưa phân công");
        cbDept.setValue(targetDept); cbDept.setPrefWidth(Double.MAX_VALUE);
        fields.getChildren().add(new VBox(4, format.formatLabel("Phân bổ Ban", FontWeight.BOLD, 12, "#94a3b8"), cbDept));

        ComboBox<String> cbRole = format.formatSortBtn("Chọn Chức vụ", "Thành viên", "Hội trưởng", "Hội phó", "Trưởng ban");
        cbRole.setValue("Thành viên"); cbRole.setPrefWidth(Double.MAX_VALUE);
        fields.getChildren().add(new VBox(4, format.formatLabel("Cấp quyền Chức vụ", FontWeight.BOLD, 12, "#94a3b8"), cbRole));

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        TextField fDate = format.formatTextField(today); fDate.setDisable(true);
        fields.getChildren().add(new VBox(4, format.formatLabel("Ngày tham gia (Mặc định)", FontWeight.BOLD, 12, "#94a3b8"), fDate));

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnCancel = getShadowBtn("Quay lại", "", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> rootModalPane.getChildren().setAll(previousView));

        Button btnConfirm = getShadowBtn("Lưu hồ sơ", "", "#10b981", "white", "rgba(16,185,129,0.4)");
        btnConfirm.setOnAction(e -> {
            frame.getInstance().closeOverlayModal();
            frame.getInstance().triggerToast("Đã duyệt đơn và thêm thành viên vào hệ thống thành công!");
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().addAll(title, sub, fields, actions);
        return box;
    }

    // Tạo cửa sổ modal ghi nhận lý do và cập nhật trạng thái không phê duyệt cho đơn ứng tuyển của cá nhân
    private VBox createRejectModal(String name, StackPane rootModalPane, VBox previousView) {
        VBox box = new VBox(20);
        box.setPrefWidth(420);
        box.setMaxSize(420, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel("Từ chối Đơn ứng tuyển", FontWeight.BLACK, 20, "#ef4444");
        Label sub = format.formatLabel("Từ chối gia nhập đối với " + name + ".", FontWeight.MEDIUM, 12, "#64748b");

        VBox fields = new VBox(8);
        fields.getChildren().add(format.formatLabel("Lý do từ chối (Tùy chọn ghi lại)", FontWeight.BOLD, 12, "#94a3b8"));
        TextField fReason = format.formatTextField("VD: Không phù hợp với định hướng CLB...");
        fields.getChildren().add(fReason);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnCancel = getShadowBtn("Quay lại", "", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> rootModalPane.getChildren().setAll(previousView));

        Button btnConfirm = getShadowBtn("Xác nhận Từ chối", "", "#ef4444", "white", "rgba(239,68,68,0.4)");
        btnConfirm.setOnAction(e -> {
            frame.getInstance().closeOverlayModal();
            frame.getInstance().triggerToast("Đã chuyển đơn ứng tuyển sang trạng thái Từ chối.");
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().addAll(title, sub, fields, actions);
        return box;
    }

    // Cấu hình định dạng kiểu dáng, font chữ hiển thị và xử lý hiệu ứng co dãn chuyển động cho thành phần nút bấm
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