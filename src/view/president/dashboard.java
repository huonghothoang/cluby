package view.president;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class dashboard extends ScrollPane {

    // Kho lưu trữ dữ liệu State: Bao gồm tên CLB, các thông số trạng thái, sự kiện và tài chính. Hiện là dữ liệu giả, đóng vai trò bản lề cho việc tích hợp Database về sau.
    private String clubName = "CLB Glimpz Hub";
    private String headerTitlePrefix = "Tổng quát về ";

    private String stat1Title = "Thành viên";
    private String membersCount = "128";

    private String stat2Title = "Sự kiện";
    private String eventsCount = "14";

    private String stat3Title = "Quỹ CLB";
    private String fundCount = "15.800.000đ";

    private String stat4Title = "Đơn ứng tuyển";
    private String applicationsCount = "23 đơn";

    private String eventSectionTitle = "⏰ Hoạt động sắp tới";

    private String event1Cat = "Kỹ thuật & Công nghệ";
    private String event1Name = "WORKSHOP GITHUB & GIT FLOW";
    private String event1Date = "28/06/2026";
    private String event1Loc = "Hội trường A";
    private String event1Reg = "54 / 80 người";

    private String event2Cat = "Nhân sự";
    private String event2Name = "TUYỂN THÀNH VIÊN THẾ HỆ K28";
    private String event2Date = "05/07/2026";
    private String event2Loc = "Trực tuyến";
    private String event2Reg = "124 đơn";

    private String event3Cat = "Nội bộ";
    private String event3Name = "HỌP TOÀN CÂU LẠC BỘ";
    private String event3Date = "12/07/2026";
    private String event3Loc = "Phòng họp C";
    private String event3Reg = "120 dự kiến";

    private String notifSectionTitle = "📢 Thông báo hệ thống";
    private String notif1 = "Ban Truyền thông vừa tạo sự kiện mới trên cổng quản lý.";
    private String notif2 = "Tuyển thành viên K28 đã chính thức mở đơn ứng tuyển trực tuyến.";
    private String notif3 = "Biên bản cuộc họp toàn thể tháng 6 đã được ban thư ký tải lên.";
    private String notif4 = "Ban Tài chính yêu cầu duyệt khoản chi mua sắm thiết bị.";
    private String notif5 = "Hệ thống chuẩn bị bảo trì vào 00:00 đêm nay.";

    private String personnelSectionTitle = "📊 Tình hình nhân sự";
    private String deptMedia = "35 t.viên";
    private String deptTech = "28 t.viên";
    private String deptEvent = "30 t.viên";
    private String deptHR = "20 t.viên";

    private String financeSectionTitle = "💰 Báo cáo Tài chính";
    private String finStat1Title = "Quỹ hiện tại";
    private String financeCurrent = "15.800.000đ";

    private String finStat2Title = "Thu tháng này";
    private String financeIncome = "+3.500.000đ";

    private String finStat3Title = "Chi tháng này";
    private String financeExpense = "-1.200.000đ";

    private String finStat4Title = "Chờ duyệt";
    private String financePending = "500.000đ";

    private String transSectionTitle = "Giao dịch gần nhất:";
    private String trans1Date = "28/06";
    private String trans1Name = "Thu hội phí";
    private String trans1Amount = "+1.200.000đ";
    private boolean trans1IsIncome = true;

    private String trans2Date = "27/06";
    private String trans2Name = "Chi in banner quảng cáo";
    private String trans2Amount = "-350.000đ";
    private boolean trans2IsIncome = false;

    private String trans3Date = "25/06";
    private String trans3Name = "Nhà tài trợ ABC rót vốn";
    private String trans3Amount = "+2.000.000đ";
    private boolean trans3IsIncome = true;

    public dashboard() {
        // Setup Component cha: Khởi tạo một cuộn VBox đóng vai trò nền tảng chứa toàn bộ các khối nội dung của bảng điều khiển.
        VBox mainContent = new VBox(32);
        mainContent.setPadding(new Insets(32));
        mainContent.setStyle("-fx-background-color: transparent;");

        styleScrollbar(this, mainContent, 12);

        mainContent.getChildren().addAll(
                buildHeader(),
                buildStatsRow(),
                buildMiddleRow(),
                buildPersonnelRow(),
                buildFinanceRow()
        );

        this.setContent(mainContent);
    }

    // Các hàm lắp ráp Layout (Builders): Lắp ghép các thẻ giao diện đã được format hóa vào chung một hàng ngang (HBox) hoặc dọc (VBox).
    private HBox buildHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Label mainTitle = createStyledLabel(headerTitlePrefix + clubName, FontWeight.BLACK, 28, "#1e293b");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnNotif = new Button("🔔");
        btnNotif.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #7c4dff, #448aff); -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 50%; -fx-min-width: 44px; -fx-min-height: 44px; -fx-effect: dropshadow(three-pass-box, rgba(124,77,255,0.4), 10, 0.3, 0, 4); -fx-cursor: hand;"
        );
        btnNotif.setOnMouseEntered(e -> { btnNotif.setScaleX(1.05); btnNotif.setScaleY(1.05); });
        btnNotif.setOnMouseExited(e -> { btnNotif.setScaleX(1.0); btnNotif.setScaleY(1.0); });

        btnNotif.setOnAction(e -> {
            System.out.println("Sự kiện click: Chuyển hướng sang trang Thông báo!");
        });

        header.getChildren().addAll(mainTitle, spacer, btnNotif);
        return header;
    }

    private HBox buildStatsRow() {
        HBox row = new HBox(24);
        row.setAlignment(Pos.CENTER);

        VBox card1 = createStatCard(stat1Title, membersCount, "#475569", "#1e293b", "#a5b4fc", "👥");
        VBox card2 = createStatCard(stat2Title, eventsCount, "#475569", "#1e293b", "#f9a8d4", "📅");
        VBox card3 = createStatCard(stat3Title, fundCount, "#475569", "#059669", "#6ee7b7", "💰");
        VBox card4 = createStatCard(stat4Title, applicationsCount, "#475569", "#7c4dff", "#d8b4fe", "📝");

        HBox.setHgrow(card1, Priority.ALWAYS); HBox.setHgrow(card2, Priority.ALWAYS);
        HBox.setHgrow(card3, Priority.ALWAYS); HBox.setHgrow(card4, Priority.ALWAYS);

        row.getChildren().addAll(card1, card2, card3, card4);
        return row;
    }

    private HBox buildMiddleRow() {
        HBox row = new HBox(24);

        VBox leftCol = createLiquidCard();
        HBox.setHgrow(leftCol, Priority.ALWAYS);
        leftCol.setPrefWidth(700);

        Label leftTitle = createStyledLabel(eventSectionTitle, FontWeight.BLACK, 18, "#1e293b");

        VBox eventsList = new VBox(16);
        eventsList.getChildren().addAll(
                createEventCard(event1Cat, event1Name, event1Date, event1Loc, event1Reg),
                createEventCard(event2Cat, event2Name, event2Date, event2Loc, event2Reg),
                createEventCard(event3Cat, event3Name, event3Date, event3Loc, event3Reg)
        );

        ScrollPane eventsScroll = new ScrollPane(eventsList);
        eventsScroll.setPrefHeight(280);
        styleScrollbar(eventsScroll, eventsList, 16);

        leftCol.getChildren().addAll(leftTitle, eventsScroll);

        VBox rightCol = createLiquidCard();
        rightCol.setPrefWidth(550);

        Label rightTitle = createStyledLabel(notifSectionTitle, FontWeight.BLACK, 18, "#1e293b");

        VBox notifList = new VBox(12);
        notifList.getChildren().addAll(
                createNotifRow(notif1, "#6366f1"),
                createNotifRow(notif2, "#ec4899"),
                createNotifRow(notif3, "#f59e0b"),
                createNotifRow(notif4, "#ef4444"),
                createNotifRow(notif5, "#3b82f6")
        );

        ScrollPane notifScroll = new ScrollPane(notifList);
        notifScroll.setPrefHeight(280);
        styleScrollbar(notifScroll, notifList, 16);

        rightCol.getChildren().addAll(rightTitle, notifScroll);
        row.getChildren().addAll(leftCol, rightCol);
        return row;
    }

    private VBox buildPersonnelRow() {
        VBox rowCard = createLiquidCard();
        HBox.setHgrow(rowCard, Priority.ALWAYS);

        Label title = createStyledLabel(personnelSectionTitle, FontWeight.BLACK, 18, "#1e293b");

        HBox content = new HBox(60);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(10, 0, 10, 0));

        PieChart chart = new PieChart();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Truyền thông", 35),
                new PieChart.Data("Kỹ thuật", 28),
                new PieChart.Data("Sự kiện", 30),
                new PieChart.Data("Nhân sự", 20)
        );
        chart.setData(pieChartData);
        chart.setLabelsVisible(true);
        chart.setLegendVisible(true);
        chart.setPrefSize(400, 300);

        String pieChartCss = ".chart-legend { -fx-background-color: transparent; } .chart-legend-item { -fx-text-fill: #1e293b; }";
        chart.getStylesheets().add("data:text/css," + pieChartCss.replaceAll(" ", "%20"));

        VBox deptsList = new VBox(12);
        deptsList.getChildren().addAll(
                createDeptStat("📢 Truyền thông", deptMedia),
                createDeptStat("💻 Kỹ thuật", deptTech),
                createDeptStat("🎪 Sự kiện", deptEvent),
                createDeptStat("👥 Nhân sự", deptHR)
        );

        ScrollPane deptsScroll = new ScrollPane(deptsList);
        deptsScroll.setPrefHeight(300);
        styleScrollbar(deptsScroll, deptsList, 16);
        HBox.setHgrow(deptsScroll, Priority.ALWAYS);

        content.getChildren().addAll(chart, deptsScroll);
        rowCard.getChildren().addAll(title, content);
        return rowCard;
    }

    private VBox buildFinanceRow() {
        VBox finCol = createLiquidCard();
        HBox.setHgrow(finCol, Priority.ALWAYS);
        finCol.setMaxWidth(Double.MAX_VALUE);

        HBox finHeader = new HBox(12);
        finHeader.setAlignment(Pos.CENTER_LEFT);
        Label finTitle = createStyledLabel(financeSectionTitle, FontWeight.BLACK, 18, "#1e293b");
        Region fSpacer = new Region();
        HBox.setHgrow(fSpacer, Priority.ALWAYS);
        Button btnAddFin = new Button("Thêm giao dịch");
        btnAddFin.setStyle("-fx-background-color: #ecfdf5; -fx-text-fill: #059669; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 20px; -fx-cursor: hand;");
        finHeader.getChildren().addAll(finTitle, fSpacer, btnAddFin);

        GridPane finStatsGrid = new GridPane();
        finStatsGrid.setHgap(16); finStatsGrid.setVgap(16);
        finStatsGrid.setMaxWidth(Double.MAX_VALUE);

        finStatsGrid.add(createMiniFinCard(finStat1Title, financeCurrent, "#1e293b"), 0, 0);
        finStatsGrid.add(createMiniFinCard(finStat2Title, financeIncome, "#059669"), 1, 0);
        finStatsGrid.add(createMiniFinCard(finStat3Title, financeExpense, "#e11d48"), 2, 0);
        finStatsGrid.add(createMiniFinCard(finStat4Title, financePending, "#d97706"), 3, 0);

        ColumnConstraints finCC = new ColumnConstraints();
        finCC.setPercentWidth(25);
        finStatsGrid.getColumnConstraints().addAll(finCC, finCC, finCC, finCC);

        Label transTitle = createStyledLabel(transSectionTitle, FontWeight.BOLD, 12, "#64748b");

        VBox transList = new VBox(8);
        transList.getChildren().addAll(
                createTransRow(trans1Date, trans1Name, trans1Amount, trans1IsIncome),
                createTransRow(trans2Date, trans2Name, trans2Amount, trans2IsIncome),
                createTransRow(trans3Date, trans3Name, trans3Amount, trans3IsIncome)
        );
        ScrollPane transScroll = new ScrollPane(transList);
        transScroll.setPrefHeight(180);
        styleScrollbar(transScroll, transList, 16);

        finCol.getChildren().addAll(finHeader, finStatsGrid, transTitle, transScroll);
        return finCol;
    }

    // Các hàm tái chế Card (Card Factories): Khởi tạo từng đối tượng phần tử giao diện lẻ (VBox/HBox) kèm dữ liệu linh động.
    private VBox createStatCard(String title, String value, String titleColor, String valColor, String iconBg, String iconEmoji) {
        VBox box = createLiquidCard();
        box.setPadding(new Insets(20));
        box.setMinHeight(Region.USE_PREF_SIZE);

        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);

        VBox textCol = new VBox(4);
        textCol.getChildren().addAll(
                createStyledLabel(title, FontWeight.BOLD, 12, titleColor),
                createStyledLabel(value, FontWeight.BLACK, 28, valColor)
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        StackPane iconContainer = new StackPane();
        iconContainer.getChildren().addAll(
                new Circle(24, Color.web(iconBg)),
                createStyledLabel(iconEmoji, FontWeight.NORMAL, 20, "#000000")
        );

        content.getChildren().addAll(textCol, spacer, iconContainer);
        box.getChildren().add(content);
        return box;
    }

    private VBox createEventCard(String category, String title, String date, String loc, String reg) {
        VBox box = new VBox(12);
        box.setPadding(new Insets(20));
        applyGlassStyle(box, 24, 0.6);
        box.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(box, Priority.ALWAYS);

        Label lblTitle = createStyledLabel(title, FontWeight.BOLD, 16, "#1e293b");
        lblTitle.setWrapText(true);

        HBox statsRow = new HBox(32);
        statsRow.getChildren().addAll(
                createMiniStat("Ngày / Deadline", date, "#334155"),
                createMiniStat("Địa điểm", loc, "#334155"),
                createMiniStat("Đăng ký", reg, "#7c4dff")
        );

        box.getChildren().addAll(createBadge(category, "#1e293b", "rgba(124,77,255,0.15)", "#7c4dff"), lblTitle, statsRow);
        return box;
    }

    private VBox createMiniStat(String title, String val, String valColor) {
        VBox box = new VBox(2);
        box.setMinHeight(Region.USE_PREF_SIZE);
        Label lblV = createStyledLabel(val, FontWeight.BOLD, 12, valColor);
        lblV.setWrapText(true);
        box.getChildren().addAll(createStyledLabel(title.toUpperCase(), FontWeight.BOLD, 9, "#94a3b8"), lblV);
        return box;
    }

    private VBox createDeptStat(String name, String count) {
        VBox box = new VBox(4);
        box.setPadding(new Insets(16));
        applyGlassStyle(box, 16, 0.4);
        box.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        box.setMaxWidth(Double.MAX_VALUE);

        box.getChildren().addAll(
                createStyledLabel(name, FontWeight.BOLD, 12, "#64748b"),
                createStyledLabel(count, FontWeight.BLACK, 16, "#1e293b")
        );
        return box;
    }

    private VBox createMiniFinCard(String title, String val, String valColor) {
        VBox box = new VBox(4);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(16));
        applyGlassStyle(box, 16, 0.6);
        box.setMaxWidth(Double.MAX_VALUE);
        box.setMinHeight(Region.USE_PREF_SIZE);
        GridPane.setHgrow(box, Priority.ALWAYS);

        Label lblV = createStyledLabel(val, FontWeight.BLACK, 16, valColor);
        lblV.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        box.getChildren().addAll(createStyledLabel(title.toUpperCase(), FontWeight.BOLD, 10, "#94a3b8"), lblV);
        return box;
    }

    private HBox createTransRow(String date, String name, String amount, boolean isIncome) {
        HBox box = new HBox(12);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(12, 16, 12, 16));
        applyGlassStyle(box, 16, 0.4);
        box.setMaxWidth(Double.MAX_VALUE);
        box.setMinHeight(Region.USE_PREF_SIZE);

        Label lblD = createStyledLabel(date, FontWeight.BOLD, 11, "#94a3b8");
        lblD.setMinWidth(40);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblA = createStyledLabel(amount, FontWeight.BLACK, 13, isIncome ? "#10b981" : "#f43f5e");
        lblA.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        box.getChildren().addAll(lblD, createStyledLabel(name, FontWeight.BOLD, 12, "#1e293b"), spacer, lblA);
        return box;
    }

    private HBox createNotifRow(String text, String dotColor) {
        HBox box = new HBox(12);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(16));
        applyGlassStyle(box, 16, 0.6);
        box.setMaxWidth(Double.MAX_VALUE);
        box.setMinHeight(Region.USE_PREF_SIZE);

        Label content = createStyledLabel(text, FontWeight.NORMAL, 13, "#334155");
        content.setWrapText(true);
        HBox.setHgrow(content, Priority.ALWAYS);

        box.getChildren().addAll(new Circle(4, Color.web(dotColor)), content);
        return box;
    }

    // Các hàm tái chế Format (Format Utilities): Quản lý tập trung các thông số liên quan đến Style (màu, CSS, đổ bóng,...) để giảm thiểu việc code lặp lại.
    private Label createStyledLabel(String text, FontWeight weight, int size, String hexColor) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Google Sans", weight, size));
        lbl.setTextFill(Color.web(hexColor));
        return lbl;
    }

    private void applyGlassStyle(Region region, int radius, double opacity) {
        region.setStyle(
                "-fx-background-color: rgba(255,255,255," + opacity + ");" +
                        "-fx-background-radius: " + radius + "px;" +
                        "-fx-border-color: white;" +
                        "-fx-border-radius: " + radius + "px;"
        );
    }

    private Label createBadge(String text, String borderColor, String bgColor, String textColor) {
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

    private VBox createLiquidCard() {
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

    // Ghi đè CSS thông qua text/css nội tuyến: Thay đổi kích thước (độ dày 12px, chiều dài tối đa 20px) và ẩn đi các thành phần rãnh cuộn mặc định.
    private void styleScrollbar(ScrollPane scrollPane, VBox contentBox, int paddingRight) {
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-padding: 0;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        contentBox.setPadding(new Insets(0, paddingRight, 0, 0));

        String css = ".scroll-pane > .viewport { -fx-background-color: transparent; } " +
                ".scroll-bar:vertical { -fx-background-color: transparent; -fx-pref-width: 12px; } " +
                ".scroll-bar:vertical .track { -fx-background-color: transparent; } " +
                ".scroll-bar:vertical .thumb { -fx-background-color: rgba(124,77,255,0.4); -fx-background-radius: 10px; -fx-max-height: 20px; } " +
                ".scroll-bar:vertical .thumb:hover { -fx-background-color: rgba(124,77,255,0.8); } " +
                ".scroll-bar:vertical .increment-button, .scroll-bar:vertical .decrement-button { -fx-padding: 0; }";

        scrollPane.getStylesheets().add("data:text/css," + css.replaceAll(" ", "%20"));
    }
}