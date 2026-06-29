package view;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import view.format;
import view.president.frame;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class finance extends ScrollPane {

    private final long CURRENT_BALANCE = 15800000;

    public finance() {
        setPickOnBounds(false);

        VBox mainContent = new VBox(24);
        mainContent.setPadding(new Insets(32));
        mainContent.setStyle("-fx-background-color: transparent;");

        HBox kpiRow = new HBox(16);
        kpiRow.getChildren().addAll(
                createStatCard("Quỹ hiện tại", "15.800.000đ", "#10b981", "#10b981", "rgba(16,185,129,0.15)", "💳"),
                createStatCard("Thu tháng này", "+3.500.000đ", "#475569", "#3b82f6", "rgba(59,130,246,0.15)", "📥"),
                createStatCard("Chi tháng này", "-1.200.000đ", "#475569", "#ef4444", "rgba(239,68,68,0.15)", "📤"),
                createStatCard("Đã hủy", "2", "#475569", "#64748b", "rgba(100,116,139,0.15)", "✖")
        );

        for (Node node : kpiRow.getChildren()) { HBox.setHgrow(node, Priority.ALWAYS); }

        HBox dashRow = new HBox(24);

        VBox chartBox = format.formatBoxCard();
        chartBox.setPrefWidth(400);
        chartBox.getChildren().add(format.formatLabel("CƠ CẤU THU / CHI", FontWeight.BLACK, 12, "#94a3b8"));

        PieChart pieChart = new PieChart();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Thu", 74.5),
                new PieChart.Data("Chi", 25.5)
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
        logTitleBox.getChildren().add(
                format.formatLabel("LỊCH SỬ GIAO DỊCH", FontWeight.BLACK, 12, "#94a3b8")
        );
        logBox.getChildren().add(logTitleBox);

        VBox logTable = new VBox(0);
        logTable.getChildren().add(createLogHeader());

        VBox logRows = new VBox(0);
        logRows.getChildren().addAll(
                createLogRow("28/06 14:35", "Hội trưởng Quân", "Thu: Hội phí tháng 6 (+2.500.000đ)"),
                createLogRow("27/06 09:12", "Trưởng ban Yến", "Chi: In ấn banner (-350.000đ)"),
                createLogRow("25/06 18:40", "Hội trưởng Quân", "Hủy: Sai số tiền (-1.000.000đ)"),
                createLogRow("20/06 10:00", "Hội trưởng Quân", "Thu: Tài trợ ABC (+5.000.000đ)"),
                createLogRow("15/06 08:30", "Trưởng ban Yến", "Chi: Mua sắm (-1.000.000đ)")
        );

        ScrollPane logScroll = new ScrollPane(logRows);
        logScroll.setPrefHeight(210);
        format.formatScrollbar(logScroll, logRows, 8);
        applySmoothScroll(logScroll, logRows);

        logTable.getChildren().add(logScroll);
        logBox.getChildren().add(logTable);

        dashRow.getChildren().addAll(chartBox, logBox);

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

        fixHover(cbType); fixHover(cbStatus); fixHover(cbEvent);

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnAdd = getShadowBtn("Thêm giao dịch", "", "#5020d8", "white", "rgba(80,32,216,0.4)");
        btnAdd.setOnAction(e -> {
            StackPane addModalRoot = new StackPane();
            VBox addForm = createTransactionFormModal(addModalRoot);
            addModalRoot.getChildren().add(addForm);
            frame.getInstance().showCustomModal(addModalRoot);
        });

        filterBar.getChildren().addAll(searchBox, cbType, cbStatus, cbEvent, spacer, btnAdd);

        VBox tableContainer = format.formatTableContainer();
        tableContainer.getChildren().add(createTableHeader());

        tableContainer.getChildren().addAll(
                createTransRow("28/06/2026", "Thu", "Hội phí tháng 6", "+2.500.000đ", "Hợp lệ", "Không", "Hội trưởng Nguyễn Minh Quân"),
                createTransRow("27/06/2026", "Chi", "In ấn banner", "-350.000đ", "Hợp lệ", "Workshop GitHub", "Trưởng ban Hoàng Kim Yến"),
                createTransRow("20/06/2026", "Thu", "Tài trợ ABC", "+5.000.000đ", "Hợp lệ", "Workshop GitHub", "Hội trưởng Nguyễn Minh Quân"),
                createTransRow("15/06/2026", "Chi", "Mua sắm vật tư", "-1.000.000đ", "Đã hủy", "Team Building", "Trưởng ban Hoàng Kim Yến")
        );

        mainContent.getChildren().addAll(kpiRow, dashRow, filterBar, tableContainer);
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
                if (contentHeight > viewportHeight) {
                    scroll.setVvalue(vvalue - delta / (contentHeight - viewportHeight));
                }
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

    private HBox createTableHeader() {
        HBox header = new HBox(16);
        header.setPadding(new Insets(12, 16, 12, 16));
        header.setStyle("-fx-border-color: transparent transparent rgba(255,255,255,0.4) transparent; -fx-border-width: 1px;");

        Label l1 = format.formatLabel("NGÀY", FontWeight.BLACK, 10, "#94a3b8"); l1.setPrefWidth(100);
        Label l2 = format.formatLabel("LOẠI", FontWeight.BLACK, 10, "#94a3b8"); l2.setPrefWidth(80);
        Label l3 = format.formatLabel("NỘI DUNG", FontWeight.BLACK, 10, "#94a3b8"); l3.setPrefWidth(260);
        Label l4 = format.formatLabel("SỐ TIỀN", FontWeight.BLACK, 10, "#94a3b8"); l4.setPrefWidth(140);
        Label l5 = format.formatLabel("TRẠNG THÁI", FontWeight.BLACK, 10, "#94a3b8"); l5.setPrefWidth(120);
        Label l6 = format.formatLabel("", FontWeight.BLACK, 10, "#94a3b8"); l6.setPrefWidth(60);

        header.getChildren().addAll(l1, l2, l3, l4, l5, l6);
        return header;
    }

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
        Label lblTitle = format.formatLabel("Thông tin giao dịch", FontWeight.BLACK, 24, "#1e293b");
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
        infoBox.getChildren().add(format.formatLabel("CHI TIẾT", FontWeight.BLACK, 12, "#94a3b8"));

        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(40); infoGrid.setVgap(16);

        infoGrid.add(new VBox(4, format.formatLabel("Nội dung", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(content, FontWeight.BOLD, 15, "#1e293b")), 0, 0, 2, 1);
        infoGrid.add(new VBox(4, format.formatLabel("Ngày thực hiện", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(date, FontWeight.BLACK, 14, "#475569")), 0, 1);
        infoGrid.add(new VBox(4, format.formatLabel("Người thực hiện", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(executor, FontWeight.BLACK, 14, "#475569")), 1, 1);

        String eventLabel = event.equals("Không") ? "Không có" : event;
        infoGrid.add(new VBox(4, format.formatLabel("Sự kiện liên kết", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(eventLabel, FontWeight.BOLD, 14, event.equals("Không") ? "#94a3b8" : "#7c4dff")), 0, 2, 2, 1);

        if (!note.isEmpty()) {
            Label lblDesc = format.formatLabel(note, FontWeight.MEDIUM, 14, "#475569");
            lblDesc.setWrapText(true);
            infoGrid.add(new VBox(4, format.formatLabel("Ghi chú thêm", FontWeight.BOLD, 12, "#94a3b8"), lblDesc), 0, 3, 2, 1);
        }

        infoBox.getChildren().add(infoGrid);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);

        Button btnClose = getModalActionBtn("Đóng", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnClose.setOnAction(e -> frame.getInstance().closeOverlayModal());
        actions.getChildren().add(btnClose);

        if (status.equals("Hợp lệ")) {
            Button btnCancelTrans = getModalActionBtn("Hủy giao dịch", "rgba(239,68,68,0.1)", "#ef4444", "rgba(0,0,0,0.1)");
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

    private VBox createTransactionFormModal(StackPane rootModalPane) {
        VBox box = new VBox(20);
        box.setPrefWidth(500);
        box.setMaxSize(500, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel("Thêm giao dịch", FontWeight.BLACK, 24, "#1e293b");

        VBox fields = new VBox(16);

        HBox typeSelector = new HBox(12);
        Button btnThu = new Button("Thu");
        Button btnChi = new Button("Chi");
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
        VBox typeGroup = new VBox(6, format.formatLabel("Loại giao dịch", FontWeight.BOLD, 12, "#94a3b8"), typeSelector);

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        TextField fDate = format.formatTextField(today); fDate.setDisable(true);
        VBox dateGroup = new VBox(6, format.formatLabel("Ngày tạo", FontWeight.BOLD, 12, "#94a3b8"), fDate);
        HBox.setHgrow(dateGroup, Priority.ALWAYS);

        HBox typeDateRow = new HBox(24, typeGroup, dateGroup);
        typeDateRow.setAlignment(Pos.CENTER_LEFT);

        TextField fContent = format.formatTextField("Ví dụ: Hội phí tháng 6");
        VBox contentGroup = new VBox(6, format.formatLabel("Nội dung", FontWeight.BOLD, 12, "#94a3b8"), fContent);

        TextField fAmount = format.formatTextField("Ví dụ: 500.000");
        VBox amountGroup = new VBox(6, format.formatLabel("Số tiền", FontWeight.BOLD, 12, "#94a3b8"), fAmount);

        ComboBox<String> cbEvent = format.formatSortBtn("Sự kiện", "Không", "Workshop GitHub", "Team Building");
        cbEvent.setValue("Không");
        cbEvent.setPrefWidth(Double.MAX_VALUE);
        cbEvent.setStyle("-fx-background-color: rgba(59,130,246,0.15); -fx-background-radius: 20px; -fx-padding: 4 12; -fx-font-family: 'Google Sans'; -fx-font-weight: bold; -fx-text-fill: #3b82f6; -fx-cursor: hand;");
        VBox eventGroup = new VBox(6, format.formatLabel("Liên kết Sự kiện", FontWeight.BOLD, 12, "#94a3b8"), cbEvent);

        TextField fNote = format.formatTextField("Ghi chú...");
        VBox noteGroup = new VBox(6, format.formatLabel("Ghi chú", FontWeight.BOLD, 12, "#94a3b8"), fNote);

        fields.getChildren().addAll(typeDateRow, contentGroup, amountGroup, eventGroup, noteGroup);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(16, 0, 0, 0));

        Button btnCancel = getModalActionBtn("Huỷ", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> frame.getInstance().closeOverlayModal());

        Button btnConfirm = getModalActionBtn("Lưu giao dịch", "#10b981", "white", "rgba(16,185,129,0.4)");
        btnConfirm.setOnAction(e -> {
            if (fContent.getText().trim().isEmpty()) {
                frame.getInstance().triggerToast("Vui lòng nhập nội dung giao dịch");
                return;
            }
            if (fAmount.getText().trim().isEmpty()) {
                frame.getInstance().triggerToast("Vui lòng nhập số tiền");
                return;
            }
            frame.getInstance().closeOverlayModal();
            frame.getInstance().triggerToast("Đã thêm giao dịch thành công");
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().addAll(title, fields, actions);

        return box;
    }

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

        Label wDesc = format.formatLabel("Hệ thống sẽ hủy giao dịch và hoàn lại số dư tài chính.", FontWeight.MEDIUM, 12, "#dc2626");
        wDesc.setWrapText(true);
        warningBox.getChildren().add(wDesc);

        VBox fields = new VBox(8);
        fields.getChildren().add(format.formatLabel("Lý do hủy", FontWeight.BOLD, 12, "#94a3b8"));
        TextField fReason = format.formatTextField("Nhập lý do...");
        fields.getChildren().add(fReason);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(16, 0, 0, 0));

        Button btnCancel = getModalActionBtn("Quay lại", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> rootModalPane.getChildren().setAll(previousView));

        Button btnConfirm = getModalActionBtn("Xác nhận hủy", "#ef4444", "white", "rgba(239,68,68,0.4)");
        btnConfirm.setOnAction(e -> {
            if (fReason.getText().trim().isEmpty()) {
                frame.getInstance().triggerToast("Vui lòng nhập lý do hủy");
                return;
            }
            frame.getInstance().closeOverlayModal();
            frame.getInstance().triggerToast("Giao dịch đã được hủy");
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().addAll(title, warningBox, fields, actions);

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