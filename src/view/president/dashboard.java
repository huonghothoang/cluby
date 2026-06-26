package view.president;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.FontWeight;

import view.format;

public class dashboard extends ScrollPane {

    private String clubName = "CLB Tuổi trẻ";
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

    // Khởi tạo giao diện chính của bảng điều khiển tổng quan
    public dashboard() {
        VBox mainContent = new VBox(32);
        mainContent.setPadding(new Insets(32));
        mainContent.setStyle("-fx-background-color: transparent;");

        format.formatScrollbar(this, mainContent, 12);

        mainContent.getChildren().addAll(
                buildHeader(), buildStatsRow(), buildMiddleRow(), buildPersonnelRow(), buildFinanceRow()
        );
        this.setContent(mainContent);
    }

    // Tạo thanh tiêu đề chứa tên câu lạc bộ và nút thông báo
    private HBox buildHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Label mainTitle = format.formatLabel(headerTitlePrefix + clubName, FontWeight.BLACK, 28, "#1e293b");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnNotif = new Button("🔔");
        btnNotif.setStyle("-fx-background-color: linear-gradient(to bottom right, #7c4dff, #448aff); -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 50%; -fx-min-width: 44px; -fx-min-height: 44px; -fx-effect: dropshadow(three-pass-box, rgba(124,77,255,0.4), 10, 0.3, 0, 4); -fx-cursor: hand;");
        btnNotif.setOnMouseEntered(e -> { btnNotif.setScaleX(1.05); btnNotif.setScaleY(1.05); });
        btnNotif.setOnMouseExited(e -> { btnNotif.setScaleX(1.0); btnNotif.setScaleY(1.0); });

        header.getChildren().addAll(mainTitle, spacer, btnNotif);
        return header;
    }

    // Tạo hàng thẻ thống kê các số liệu tổng quan cơ bản
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

    // Tạo khối hiển thị hoạt động sắp tới và thông báo hệ thống
    private HBox buildMiddleRow() {
        HBox row = new HBox(24);

        VBox leftCol = format.formatBoxCard();
        HBox.setHgrow(leftCol, Priority.ALWAYS);
        leftCol.setPrefWidth(700);

        Label leftTitle = format.formatLabel(eventSectionTitle, FontWeight.BLACK, 18, "#1e293b");

        VBox eventsList = new VBox(16);
        eventsList.getChildren().addAll(
                createEventCard(event1Cat, event1Name, event1Date, event1Loc, event1Reg),
                createEventCard(event2Cat, event2Name, event2Date, event2Loc, event2Reg),
                createEventCard(event3Cat, event3Name, event3Date, event3Loc, event3Reg)
        );

        ScrollPane eventsScroll = new ScrollPane(eventsList);
        eventsScroll.setPrefHeight(280);
        format.formatScrollbar(eventsScroll, eventsList, 16);

        leftCol.getChildren().addAll(leftTitle, eventsScroll);

        VBox rightCol = format.formatBoxCard();
        rightCol.setPrefWidth(550);

        Label rightTitle = format.formatLabel(notifSectionTitle, FontWeight.BLACK, 18, "#1e293b");
        VBox notifList = new VBox(12);
        notifList.getChildren().addAll(
                createNotifRow(notif1, "#6366f1"), createNotifRow(notif2, "#ec4899"),
                createNotifRow(notif3, "#f59e0b"), createNotifRow(notif4, "#ef4444"), createNotifRow(notif5, "#3b82f6")
        );

        ScrollPane notifScroll = new ScrollPane(notifList);
        notifScroll.setPrefHeight(280);
        format.formatScrollbar(notifScroll, notifList, 16);

        rightCol.getChildren().addAll(rightTitle, notifScroll);
        row.getChildren().addAll(leftCol, rightCol);
        return row;
    }

    // Tạo khối biểu đồ tròn biểu diễn tình hình nhân sự theo ban
    private VBox buildPersonnelRow() {
        VBox rowCard = format.formatBoxCard();
        HBox.setHgrow(rowCard, Priority.ALWAYS);

        Label title = format.formatLabel(personnelSectionTitle, FontWeight.BLACK, 18, "#1e293b");

        HBox content = new HBox(60);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(10, 0, 10, 0));

        PieChart chart = new PieChart();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Truyền thông", 35), new PieChart.Data("Kỹ thuật", 28),
                new PieChart.Data("Sự kiện", 30), new PieChart.Data("Nhân sự", 20)
        );
        chart.setData(pieChartData);
        chart.setLabelsVisible(true); chart.setLegendVisible(true);
        chart.setPrefSize(400, 300);
        chart.getStylesheets().add("data:text/css," + ".chart-legend { -fx-background-color: transparent; } .chart-legend-item { -fx-text-fill: #1e293b; }".replaceAll(" ", "%20"));

        VBox deptsList = new VBox(12);
        deptsList.getChildren().addAll(
                createDeptStat("📢 Truyền thông", deptMedia), createDeptStat("💻 Kỹ thuật", deptTech),
                createDeptStat("🎪 Sự kiện", deptEvent), createDeptStat("👥 Nhân sự", deptHR)
        );

        ScrollPane deptsScroll = new ScrollPane(deptsList);
        deptsScroll.setPrefHeight(300);
        format.formatScrollbar(deptsScroll, deptsList, 16);
        HBox.setHgrow(deptsScroll, Priority.ALWAYS);

        content.getChildren().addAll(chart, deptsScroll);
        rowCard.getChildren().addAll(title, content);
        return rowCard;
    }

    // Tạo khối báo cáo tài chính gồm ô chỉ số và danh sách giao dịch
    private VBox buildFinanceRow() {
        VBox finCol = format.formatBoxCard();
        HBox.setHgrow(finCol, Priority.ALWAYS);
        finCol.setMaxWidth(Double.MAX_VALUE);

        HBox finHeader = new HBox(12);
        finHeader.setAlignment(Pos.CENTER_LEFT);
        Label finTitle = format.formatLabel(financeSectionTitle, FontWeight.BLACK, 18, "#1e293b");

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

        Label transTitle = format.formatLabel(transSectionTitle, FontWeight.BOLD, 12, "#64748b");

        VBox transList = new VBox(8);
        transList.getChildren().addAll(
                createTransRow(trans1Date, trans1Name, trans1Amount, trans1IsIncome),
                createTransRow(trans2Date, trans2Name, trans2Amount, trans2IsIncome),
                createTransRow(trans3Date, trans3Name, trans3Amount, trans3IsIncome)
        );
        ScrollPane transScroll = new ScrollPane(transList);
        transScroll.setPrefHeight(180);
        format.formatScrollbar(transScroll, transList, 16);

        finCol.getChildren().addAll(finHeader, finStatsGrid, transTitle, transScroll);
        return finCol;
    }

    // Thiết lập cấu trúc giao diện cho từng thẻ số liệu tổng quan có chứa icon
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

    // Thiết lập bố cục thẻ hiển thị thông tin tóm tắt của một hoạt động sự kiện
    private VBox createEventCard(String category, String title, String date, String loc, String reg) {
        VBox box = new VBox(12);
        box.setPadding(new Insets(20));
        format.formatGlass(box, 24, 0.6);
        box.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(box, Priority.ALWAYS);

        Label lblTitle = format.formatLabel(title, FontWeight.BOLD, 16, "#1e293b");
        lblTitle.setWrapText(true);

        HBox statsRow = new HBox(32);
        statsRow.getChildren().addAll(
                createMiniStat("Ngày / Deadline", date, "#334155"),
                createMiniStat("Địa điểm", loc, "#334155"),
                createMiniStat("Đăng ký", reg, "#7c4dff")
        );

        box.getChildren().addAll(format.formatBadge(category, "rgba(124,77,255,0.15)", "#7c4dff"), lblTitle, statsRow);
        return box;
    }

    // Tạo khối văn bản nhỏ hiển thị tiêu đề và nội dung thuộc tính của sự kiện
    private VBox createMiniStat(String title, String val, String valColor) {
        VBox box = new VBox(2);
        box.setMinHeight(Region.USE_PREF_SIZE);
        Label lblV = format.formatLabel(val, FontWeight.BOLD, 12, valColor);
        lblV.setWrapText(true);
        box.getChildren().addAll(format.formatLabel(title.toUpperCase(), FontWeight.BOLD, 9, "#94a3b8"), lblV);
        return box;
    }

    // Tạo hộp thông tin số lượng nhân sự dạng ô kính mờ cho từng phân ban
    private VBox createDeptStat(String name, String count) {
        VBox box = new VBox(4);
        box.setPadding(new Insets(16));
        format.formatGlass(box, 16, 0.4);
        box.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        box.setMaxWidth(Double.MAX_VALUE);

        box.getChildren().addAll(format.formatLabel(name, FontWeight.BOLD, 12, "#64748b"), format.formatLabel(count, FontWeight.BLACK, 16, "#1e293b"));
        return box;
    }

    // Tạo ô thẻ hiển thị số tiền hoặc chỉ số thu chi tài chính thu nhỏ
    private VBox createMiniFinCard(String title, String val, String valColor) {
        VBox box = new VBox(4);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(16));
        format.formatGlass(box, 16, 0.6);
        box.setMaxWidth(Double.MAX_VALUE);
        box.setMinHeight(Region.USE_PREF_SIZE);
        GridPane.setHgrow(box, Priority.ALWAYS);

        Label lblV = format.formatLabel(val, FontWeight.BLACK, 16, valColor);
        lblV.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        box.getChildren().addAll(format.formatLabel(title.toUpperCase(), FontWeight.BOLD, 10, "#94a3b8"), lblV);
        return box;
    }

    // Tạo hàng hiển thị lịch sử một mục giao dịch tài chính thu hoặc chi
    private HBox createTransRow(String date, String name, String amount, boolean isIncome) {
        HBox box = new HBox(12);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(12, 16, 12, 16));
        format.formatGlass(box, 16, 0.4);
        box.setMaxWidth(Double.MAX_VALUE);
        box.setMinHeight(Region.USE_PREF_SIZE);

        Label lblD = format.formatLabel(date, FontWeight.BOLD, 11, "#94a3b8");
        lblD.setMinWidth(40);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblA = format.formatLabel(amount, FontWeight.BLACK, 13, isIncome ? "#10b981" : "#f43f5e");
        lblA.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        box.getChildren().addAll(lblD, format.formatLabel(name, FontWeight.BOLD, 12, "#1e293b"), spacer, lblA);
        return box;
    }

    // Tạo một hàng thông báo hệ thống đi kèm chấm tròn màu đánh dấu nhận diện
    private HBox createNotifRow(String text, String dotColor) {
        HBox box = new HBox(12);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(16));
        format.formatGlass(box, 16, 0.6);
        box.setMaxWidth(Double.MAX_VALUE);
        box.setMinHeight(Region.USE_PREF_SIZE);

        Label content = format.formatLabel(text, FontWeight.NORMAL, 13, "#334155");
        content.setWrapText(true);
        HBox.setHgrow(content, Priority.ALWAYS);

        box.getChildren().addAll(new Circle(4, Color.web(dotColor)), content);
        return box;
    }
}