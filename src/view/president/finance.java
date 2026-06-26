package view.president;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import view.format;
import view.president.frame;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class finance extends ScrollPane {

    private final long CURRENT_BALANCE = 15800000;

    // Khởi tạo giao diện chính của trang quản lý tài chính câu lạc bộ
    public finance() {
        VBox mainContent = new VBox(24);
        mainContent.setPadding(new Insets(32));
        mainContent.setStyle("-fx-background-color: transparent;");

        // === 1. THẺ KPI ===
        HBox kpiRow = new HBox(16);
        kpiRow.getChildren().addAll(
                format.formatKPICard("Quỹ hiện tại", "15.800.000đ", "#10b981", "#10b981"),
                format.formatKPICard("Thu (Tháng)", "+3.500.000đ", "#3b82f6", "#1e293b"),
                format.formatKPICard("Chi (Tháng)", "-1.200.000đ", "#ef4444", "#1e293b"),
                format.formatKPICard("Đã hủy", "2", "#64748b", "#64748b")
        );
        for (javafx.scene.Node node : kpiRow.getChildren()) { HBox.setHgrow(node, Priority.ALWAYS); }

        // === 2. BIỂU ĐỒ & LOG ===
        HBox dashRow = new HBox(24);

        VBox chartBox = format.formatBoxCard();
        chartBox.setPrefWidth(400);
        chartBox.getChildren().add(format.formatLabel("TỶ LỆ THU / CHI", FontWeight.BLACK, 12, "#94a3b8"));

        PieChart pieChart = new PieChart();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Khoản Thu", 74.5),
                new PieChart.Data("Khoản Chi", 25.5)
        );
        pieChart.setData(pieChartData);
        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(false);
        pieChart.setPrefHeight(250);

        String chartCss = ".chart-pie { -fx-border-color: white; -fx-border-width: 2px; } " +
                ".default-color0.chart-pie { -fx-pie-color: #10b981; } " +
                ".default-color1.chart-pie { -fx-pie-color: #ef4444; } " +
                ".chart-legend { -fx-background-color: transparent; } " +
                ".chart-legend-item { -fx-text-fill: #1e293b; -fx-font-family: 'Google Sans'; -fx-font-weight: bold; }";
        pieChart.getStylesheets().add("data:text/css," + chartCss.replaceAll(" ", "%20"));
        chartBox.getChildren().add(pieChart);

        VBox logBox = format.formatBoxCard();
        HBox.setHgrow(logBox, Priority.ALWAYS);
        VBox logTitleBox = new VBox(2);
        logTitleBox.getChildren().addAll(
                format.formatLabel("LỊCH SỬ HOẠT ĐỘNG", FontWeight.BLACK, 12, "#94a3b8"),
                format.formatLabel("Dữ liệu gốc, không thể chỉnh sửa.", FontWeight.MEDIUM, 11, "#ef4444")
        );
        logBox.getChildren().add(logTitleBox);

        VBox logTable = new VBox(0);
        logTable.getChildren().add(createLogHeader());
        VBox logRows = new VBox(0);
        logRows.getChildren().addAll(
                createLogRow("28/06/2026 14:35", "Alexandra Đặng", "Thu: Thu hội phí tháng 6 (+2.500.000đ)"),
                createLogRow("27/06/2026 09:12", "Hoàng Kim Yến", "Chi: In ấn banner (-350.000đ)"),
                createLogRow("25/06/2026 18:40", "Alexandra Đặng", "Hủy: Mua nhầm vật tư (-1.000.000đ) - Sai số tiền"),
                createLogRow("20/06/2026 10:00", "Alexandra Đặng", "Thu: Tài trợ ABC (+5.000.000đ)"),
                createLogRow("15/06/2026 08:30", "Hoàng Kim Yến", "Chi: Mua nhầm vật tư (-1.000.000đ)")
        );
        ScrollPane logScroll = new ScrollPane(logRows);
        logScroll.setPrefHeight(210);
        format.formatScrollbar(logScroll, logRows, 8);
        logTable.getChildren().add(logScroll);
        logBox.getChildren().add(logTable);

        dashRow.getChildren().addAll(chartBox, logBox);

        // === 3. FILTER ===
        HBox filterBar = new HBox(16);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(12, 24, 12, 24));
        format.formatGlass(filterBar, 40, 0.4);

        HBox searchBox = new HBox(8);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = format.formatTextField("Tìm giao dịch...");
        Button btnSearch = format.formatFindBtn();
        searchBox.getChildren().addAll(searchField, btnSearch);

        ComboBox<String> cbType = format.formatSortBtn("Loại", "Tất cả", "Thu", "Chi");
        ComboBox<String> cbStatus = format.formatSortBtn("Trạng thái", "Tất cả", "Hợp lệ", "Đã hủy");
        ComboBox<String> cbEvent = format.formatSortBtn("Sự kiện", "Tất cả", "Không", "Workshop GitHub", "Team Building");

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnAdd = getShadowBtn("Thêm giao dịch", "➕", "#5020d8", "white", "rgba(80,32,216,0.4)");
        btnAdd.setOnAction(e -> {
            StackPane addModalRoot = new StackPane();
            VBox addForm = createTransactionFormModal(addModalRoot);
            addModalRoot.getChildren().add(addForm);
            frame.getInstance().showCustomModal(addModalRoot);
        });

        filterBar.getChildren().addAll(searchBox, cbType, cbStatus, cbEvent, spacer, btnAdd);

        // === 4. BẢNG GIAO DỊCH ===
        VBox tableContainer = format.formatTableContainer();
        tableContainer.getChildren().add(createTableHeader());

        tableContainer.getChildren().addAll(
                createTransRow("28/06/2026", "Thu", "Thu hội phí tháng 6", "+2.500.000đ", "Hợp lệ", "Không", "Alexandra Đặng"),
                createTransRow("27/06/2026", "Chi", "In ấn banner", "-350.000đ", "Hợp lệ", "Workshop GitHub", "Hoàng Kim Yến"),
                createTransRow("20/06/2026", "Thu", "Tài trợ ABC", "+5.000.000đ", "Hợp lệ", "Workshop GitHub", "Alexandra Đặng"),
                createTransRow("15/06/2026", "Chi", "Mua nhầm vật tư", "-1.000.000đ", "Đã hủy", "Team Building", "Hoàng Kim Yến")
        );

        mainContent.getChildren().addAll(kpiRow, dashRow, filterBar, tableContainer);
        format.formatScrollbar(this, mainContent, 12);
        this.setContent(mainContent);
    }

    // Tạo thanh tiêu đề cột cho bảng lịch sử nhật ký hoạt động
    private HBox createLogHeader() {
        HBox header = new HBox(16);
        header.setPadding(new Insets(12, 16, 12, 16));
        header.setStyle("-fx-border-color: transparent transparent rgba(0,0,0,0.1) transparent; -fx-border-width: 1px;");
        Label l1 = format.formatLabel("THỜI GIAN", FontWeight.BLACK, 10, "#94a3b8"); l1.setPrefWidth(120);
        Label l2 = format.formatLabel("NGƯỜI TẠO", FontWeight.BLACK, 10, "#94a3b8"); l2.setPrefWidth(140);
        Label l3 = format.formatLabel("NỘI DUNG", FontWeight.BLACK, 10, "#94a3b8"); l3.setPrefWidth(300); HBox.setHgrow(l3, Priority.ALWAYS);
        header.getChildren().addAll(l1, l2, l3);
        return header;
    }

    // Tạo một hàng hiển thị bản ghi nhật ký hoạt động tài chính cụ thể
    private HBox createLogRow(String time, String user, String content) {
        HBox row = new HBox(16);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-border-color: transparent transparent rgba(0,0,0,0.05) transparent; -fx-border-width: 1px;");

        Label lblTime = format.formatLabel(time, FontWeight.BOLD, 12, "#64748b"); lblTime.setPrefWidth(120);
        Label lblUser = format.formatLabel(user, FontWeight.BOLD, 12, "#5020d8"); lblUser.setPrefWidth(140);

        String colorContent = content.contains("Hủy") ? "#ef4444" : (content.contains("Thu") ? "#10b981" : "#1e293b");
        Label lblContent = format.formatLabel(content, FontWeight.MEDIUM, 13, colorContent);
        lblContent.setWrapText(true);
        HBox.setHgrow(lblContent, Priority.ALWAYS);

        row.getChildren().addAll(lblTime, lblUser, lblContent);
        return row;
    }

    // Tạo thanh tiêu đề cột cho bảng tổng hợp danh sách giao dịch tài chính
    private HBox createTableHeader() {
        HBox header = new HBox(16);
        header.setPadding(new Insets(12, 16, 12, 16));
        header.setStyle("-fx-border-color: transparent transparent rgba(255,255,255,0.4) transparent; -fx-border-width: 1px;");

        Label l1 = format.formatLabel("NGÀY", FontWeight.BLACK, 10, "#94a3b8"); l1.setPrefWidth(100);
        Label l2 = format.formatLabel("LOẠI", FontWeight.BLACK, 10, "#94a3b8"); l2.setPrefWidth(80);
        Label l3 = format.formatLabel("NỘI DUNG", FontWeight.BLACK, 10, "#94a3b8"); l3.setPrefWidth(260);
        Label l4 = format.formatLabel("SỐ TIỀN", FontWeight.BLACK, 10, "#94a3b8"); l4.setPrefWidth(140);
        Label l5 = format.formatLabel("TRẠNG THÁI", FontWeight.BLACK, 10, "#94a3b8"); l5.setPrefWidth(120);
        Label l6 = format.formatLabel("THAO TÁC", FontWeight.BLACK, 10, "#94a3b8"); l6.setPrefWidth(60);

        header.getChildren().addAll(l1, l2, l3, l4, l5, l6);
        return header;
    }

    // Tạo một hàng hiển thị chi tiết thông tin và trạng thái của một mục giao dịch
    private HBox createTransRow(String date, String type, String content, String amount, String status, String event, String executor) {
        HBox row = new HBox(16);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand;");
        row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand; -fx-background-radius: 16px;"));
        row.setOnMouseExited(e -> row.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand;"));

        Label lblDate = format.formatLabel(date, FontWeight.BOLD, 13, "#475569"); lblDate.setPrefWidth(100);

        String typeBg = type.equals("Thu") ? "rgba(16,185,129,0.15)" : "rgba(239,68,68,0.15)";
        String typeText = type.equals("Thu") ? "#10b981" : "#ef4444";
        HBox typeBox = new HBox(format.formatBadge(type, typeBg, typeText)); typeBox.setAlignment(Pos.CENTER_LEFT); typeBox.setPrefWidth(80);

        Label lblContent = format.formatLabel(content, FontWeight.BOLD, 14, "#1e293b"); lblContent.setPrefWidth(260);

        String amtColor = status.equals("Đã hủy") ? "#94a3b8" : (type.equals("Thu") ? "#10b981" : "#ef4444");
        Label lblAmount = format.formatLabel(amount, FontWeight.BLACK, 15, amtColor); lblAmount.setPrefWidth(140);

        String stBg = status.equals("Hợp lệ") ? "rgba(59,130,246,0.15)" : "rgba(100,116,139,0.15)";
        String stText = status.equals("Hợp lệ") ? "#3b82f6" : "#64748b";
        HBox statusBox = new HBox(format.formatBadge(status, stBg, stText)); statusBox.setAlignment(Pos.CENTER_LEFT); statusBox.setPrefWidth(120);

        Button btnView = format.formatCircleBtn("👁️‍🗨️", "#448aff", "#7c4dff");
        btnView.setOnAction(e -> {
            StackPane detailModal = createDetailModal(date, type, content, amount, status, event, executor, "");
            frame.getInstance().showCustomModal(detailModal);
        });
        HBox actionBox = new HBox(btnView); actionBox.setAlignment(Pos.CENTER_LEFT); actionBox.setPrefWidth(60);

        row.getChildren().addAll(lblDate, typeBox, lblContent, lblAmount, statusBox, actionBox);
        return row;
    }

    // Tạo cửa sổ modal chi tiết hiển thị toàn bộ thuộc tính giao dịch và nút yêu cầu hủy bỏ
    private StackPane createDetailModal(String date, String type, String content, String amount, String status, String event, String executor, String note) {
        StackPane rootModalPane = new StackPane();

        VBox modalContent = new VBox(24);
        modalContent.setPrefWidth(680);
        modalContent.setMaxSize(680, Region.USE_PREF_SIZE);
        modalContent.setPadding(new Insets(32));
        modalContent.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        modalContent.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        Label lblTitle = format.formatLabel("Chi tiết", FontWeight.BLACK, 24, "#1e293b");
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        String stBg = status.equals("Hợp lệ") ? "rgba(59,130,246,0.15)" : "rgba(100,116,139,0.15)";
        String stText = status.equals("Hợp lệ") ? "#3b82f6" : "#64748b";
        Label statusBadge = format.formatBadge(status, stBg, stText);
        statusBadge.setStyle(statusBadge.getStyle() + "-fx-font-size: 14px; -fx-padding: 8 16;");
        header.getChildren().addAll(lblTitle, spacer, statusBadge);

        VBox amountBox = new VBox(4);
        amountBox.setAlignment(Pos.CENTER);
        amountBox.setPadding(new Insets(24));
        amountBox.setStyle("-fx-background-color: rgba(248,250,252,0.8); -fx-background-radius: 24px; -fx-border-color: rgba(226,232,240,1); -fx-border-radius: 24px;");

        String amtColor = status.equals("Đã hủy") ? "#94a3b8" : (type.equals("Thu") ? "#10b981" : "#ef4444");
        amountBox.getChildren().addAll(
                format.formatLabel(type.toUpperCase(), FontWeight.BOLD, 14, "#64748b"),
                format.formatLabel(amount, FontWeight.BLACK, 36, amtColor)
        );

        VBox infoBox = format.formatBoxCard();
        infoBox.getChildren().add(format.formatLabel("THÔNG TIN", FontWeight.BLACK, 12, "#94a3b8"));

        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(40); infoGrid.setVgap(16);

        infoGrid.add(new VBox(4, format.formatLabel("Nội dung", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(content, FontWeight.BOLD, 15, "#1e293b")), 0, 0, 2, 1);
        infoGrid.add(new VBox(4, format.formatLabel("Ngày", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(date, FontWeight.BLACK, 14, "#475569")), 0, 1);
        infoGrid.add(new VBox(4, format.formatLabel("Người tạo", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(executor, FontWeight.BLACK, 14, "#475569")), 1, 1);

        String eventLabel = event.equals("Không") ? "Không có" : "📅 " + event;
        infoGrid.add(new VBox(4, format.formatLabel("Sự kiện", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(eventLabel, FontWeight.BOLD, 14, event.equals("Không") ? "#94a3b8" : "#7c4dff")), 0, 2, 2, 1);

        if (!note.isEmpty()) {
            Label lblDesc = format.formatLabel(note, FontWeight.MEDIUM, 14, "#475569");
            lblDesc.setWrapText(true);
            infoGrid.add(new VBox(4, format.formatLabel("Ghi chú", FontWeight.BOLD, 12, "#94a3b8"), lblDesc), 0, 3, 2, 1);
        }

        infoBox.getChildren().add(infoGrid);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnClose = getShadowBtn("Đóng", "", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnClose.setOnAction(e -> frame.getInstance().closeOverlayModal());
        actions.getChildren().add(btnClose);

        if (status.equals("Hợp lệ")) {
            Button btnCancelTrans = getShadowBtn("Hủy", "✖", "rgba(239,68,68,0.1)", "#ef4444", "rgba(0,0,0,0.1)");
            btnCancelTrans.setOnAction(e -> {
                VBox cancelModal = createCancelModal(content, rootModalPane, modalContent);
                rootModalPane.getChildren().setAll(cancelModal);
            });
            actions.getChildren().add(btnCancelTrans);
        }

        modalContent.getChildren().addAll(header, amountBox, infoBox, actions);
        rootModalPane.getChildren().add(modalContent);
        return rootModalPane;
    }

    // Tạo biểu mẫu modal khai báo thêm mới giao dịch thu hoặc chi vào hệ thống kèm theo kiểm tra tính hợp lệ số dư
    private VBox createTransactionFormModal(StackPane rootModalPane) {
        VBox box = new VBox(20);
        box.setPrefWidth(500);
        box.setMaxSize(500, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel("Thêm giao dịch", FontWeight.BLACK, 24, "#1e293b");

        VBox fields = new VBox(16);

        // --- CHỌN LOẠI GIAO DỊCH (THU / CHI) BẰNG CHUẨN PROPERTY ---
        HBox typeSelector = new HBox(12);
        Button btnThu = new Button("Khoản Thu");
        Button btnChi = new Button("Khoản Chi");

        SimpleStringProperty selectedType = new SimpleStringProperty("Chi");

        Runnable updateStyle = () -> {
            String baseStyle = "-fx-background-radius: 20px; -fx-padding: 8 24; -fx-font-family: 'Google Sans'; -fx-font-weight: bold; -fx-cursor: hand; ";
            if ("Thu".equals(selectedType.get())) {
                btnThu.setStyle(baseStyle + "-fx-background-color: #10b981; -fx-text-fill: white;");
                btnChi.setStyle(baseStyle + "-fx-background-color: rgba(100,116,139,0.1); -fx-text-fill: #64748b;");
            } else {
                btnThu.setStyle(baseStyle + "-fx-background-color: rgba(100,116,139,0.1); -fx-text-fill: #64748b;");
                btnChi.setStyle(baseStyle + "-fx-background-color: #ef4444; -fx-text-fill: white;");
            }
        };

        btnThu.setOnAction(e -> { selectedType.set("Thu"); updateStyle.run(); });
        btnChi.setOnAction(e -> { selectedType.set("Chi"); updateStyle.run(); });
        updateStyle.run();

        typeSelector.getChildren().addAll(btnThu, btnChi);
        VBox typeGroup = new VBox(6, format.formatLabel("Loại", FontWeight.BOLD, 12, "#94a3b8"), typeSelector);

        // Ngày
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        TextField fDate = format.formatTextField(today); fDate.setDisable(true);
        VBox dateGroup = new VBox(6, format.formatLabel("Ngày", FontWeight.BOLD, 12, "#94a3b8"), fDate);
        HBox.setHgrow(dateGroup, Priority.ALWAYS);

        HBox typeDateRow = new HBox(24, typeGroup, dateGroup);
        typeDateRow.setAlignment(Pos.CENTER_LEFT);

        // Nội dung & Số tiền
        TextField fContent = format.formatTextField("Nội dung...");
        VBox contentGroup = new VBox(6, format.formatLabel("Nội dung", FontWeight.BOLD, 12, "#94a3b8"), fContent);

        TextField fAmount = format.formatTextField("Số tiền...");
        VBox amountGroup = new VBox(6, format.formatLabel("Số tiền", FontWeight.BOLD, 12, "#94a3b8"), fAmount);

        // Sự kiện (Tái chế từ formatSortBtn và set màu xanh dương nổi bật)
        ComboBox<String> cbEvent = format.formatSortBtn("Sự kiện", "Không", "Workshop GitHub", "Team Building");
        cbEvent.setValue("Không");
        cbEvent.setPrefWidth(Double.MAX_VALUE);
        cbEvent.setStyle("-fx-background-color: rgba(59,130,246,0.15); -fx-background-radius: 20px; -fx-padding: 4 12; -fx-font-family: 'Google Sans'; -fx-font-weight: bold; -fx-text-fill: #3b82f6; -fx-cursor: hand;");
        VBox eventGroup = new VBox(6, format.formatLabel("Sự kiện", FontWeight.BOLD, 12, "#94a3b8"), cbEvent);

        // Ghi chú
        TextField fNote = format.formatTextField("Ghi chú...");
        VBox noteGroup = new VBox(6, format.formatLabel("Ghi chú", FontWeight.BOLD, 12, "#94a3b8"), fNote);

        fields.getChildren().addAll(typeDateRow, contentGroup, amountGroup, eventGroup, noteGroup);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnCancel = getShadowBtn("Huỷ", "", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> frame.getInstance().closeOverlayModal());

        Button btnConfirm = getShadowBtn("Lưu", "", "#10b981", "white", "rgba(16,185,129,0.4)");
        btnConfirm.setOnAction(e -> {
            if (fContent.getText().trim().isEmpty()) {
                frame.getInstance().triggerToast("Vui lòng nhập nội dung.");
                return;
            }
            String amtStr = fAmount.getText().trim();
            if (amtStr.isEmpty()) {
                frame.getInstance().triggerToast("Vui lòng nhập số tiền.");
                return;
            }
            try {
                long amountValue = Long.parseLong(amtStr.replace(".", "").replace(",", ""));
                if (amountValue <= 0) {
                    frame.getInstance().triggerToast("Số tiền phải lớn hơn 0.");
                    return;
                }
                if ("Chi".equals(selectedType.get()) && amountValue > CURRENT_BALANCE) {
                    frame.getInstance().triggerToast("Quỹ không đủ.");
                    return;
                }
            } catch (NumberFormatException ex) {
                frame.getInstance().triggerToast("Số tiền không hợp lệ.");
                return;
            }
            frame.getInstance().closeOverlayModal();
            frame.getInstance().triggerToast("Thêm giao dịch thành công.");
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().addAll(title, fields, actions);

        return box;
    }

    // Tạo cửa sổ modal cảnh báo xác nhận đi kèm biểu mẫu nhập lý do để xử lý việc hủy bỏ giao dịch
    private VBox createCancelModal(String transContent, StackPane rootModalPane, VBox previousView) {
        VBox box = new VBox(20);
        box.setPrefWidth(420);
        box.setMaxSize(420, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel("Hủy giao dịch", FontWeight.BLACK, 20, "#ef4444");

        VBox warningBox = new VBox(8);
        warningBox.setPadding(new Insets(16));
        warningBox.setStyle("-fx-background-color: rgba(239, 68, 68, 0.05); -fx-background-radius: 16px;");
        Label wDesc = format.formatLabel("Giao dịch sẽ bị hủy. Số dư tự động hoàn lại.", FontWeight.MEDIUM, 12, "#dc2626");
        wDesc.setWrapText(true);
        warningBox.getChildren().add(wDesc);

        VBox fields = new VBox(8);
        fields.getChildren().add(format.formatLabel("Lý do hủy", FontWeight.BOLD, 12, "#94a3b8"));
        TextField fReason = format.formatTextField("Lý do...");
        fields.getChildren().add(fReason);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnCancel = getShadowBtn("Quay lại", "", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> rootModalPane.getChildren().setAll(previousView));

        Button btnConfirm = getShadowBtn("Xác nhận Hủy", "", "#ef4444", "white", "rgba(239,68,68,0.4)");
        btnConfirm.setOnAction(e -> {
            if (fReason.getText().trim().isEmpty()) {
                frame.getInstance().triggerToast("Vui lòng nhập lý do.");
                return;
            }
            frame.getInstance().closeOverlayModal();
            frame.getInstance().triggerToast("Giao dịch đã hủy.");
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().addAll(title, warningBox, fields, actions);

        return box;
    }

    // Thiết lập phong cách hiển thị đồ họa và quản lý các hiệu ứng phóng to co dãn khi rê chuột trên nút bấm
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