package view.head;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.FontWeight;
import view.format;

public class dashboard extends ScrollPane {

    private String deptName = "Truyền thông";

    public dashboard() {
        VBox mainContent = new VBox(32);
        mainContent.setPadding(new Insets(32));
        mainContent.setStyle("-fx-background-color: transparent;");

        format.formatScrollbar(this, mainContent, 12);

        mainContent.getChildren().addAll(
                buildHeader(),
                buildStatsRow(),
                buildTaskAndChartRow(),
                buildNotifRow()
        );

        this.setContent(mainContent);
    }

    private HBox buildHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Label mainTitle = format.formatLabel("Tổng quan bộ phận " + deptName, FontWeight.BLACK, 28, "#1e293b");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(mainTitle, spacer);
        return header;
    }

    private HBox buildStatsRow() {
        HBox row = new HBox(16);

        VBox card1 = createStatCard("Nhân sự", "24 thành viên", "#475569", "#1e293b", "#a5b4fc", "👥");
        VBox card2 = createStatCard("Công việc", "8 đã nhận", "#475569", "#3b82f6", "#bfdbfe", "⏳");
        VBox card3 = createStatCard("Sự kiện", "3 đã nhận", "#475569", "#f59e0b", "#fde68a", "📅");

        HBox.setHgrow(card1, Priority.ALWAYS);
        HBox.setHgrow(card2, Priority.ALWAYS);
        HBox.setHgrow(card3, Priority.ALWAYS);

        row.getChildren().addAll(card1, card2, card3);
        return row;
    }

    private VBox createStatCard(String title, String value, String titleColor, String valColor, String iconBg, String iconEmoji) {
        VBox box = format.formatBoxCard();
        box.setPadding(new Insets(20));
        box.setMinHeight(Region.USE_PREF_SIZE);

        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);

        VBox textCol = new VBox(4);
        textCol.getChildren().addAll(format.formatLabel(title, FontWeight.BOLD, 12, titleColor), format.formatLabel(value, FontWeight.BLACK, 24, valColor));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        StackPane iconContainer = new StackPane();
        iconContainer.getChildren().addAll(new Circle(24, Color.web(iconBg)), format.formatLabel(iconEmoji, FontWeight.NORMAL, 20, "#000000"));

        content.getChildren().addAll(textCol, spacer, iconContainer);
        box.getChildren().add(content);

        box.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 32px; -fx-border-width: 0px; -fx-border-color: transparent; -fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.05), 10, 0, 0, 4);");

        return box;
    }

    private HBox buildTaskAndChartRow() {
        HBox row = new HBox(24);

        VBox leftCol = format.formatBoxCard();
        HBox.setHgrow(leftCol, Priority.ALWAYS);
        leftCol.setPrefWidth(850);

        Label leftTitle = format.formatLabel("Công việc ưu tiên", FontWeight.BLACK, 18, "#1e293b");

        VBox tasksList = new VBox(20);
        tasksList.getChildren().addAll(
                createWorkCard("Thiết kế Poster", "Workshop GitHub", "26/06/2026", "Cao", "Đang tiến hành", true, 0.6),
                createWorkCard("Viết bài nội bộ", "Việc chung", "29/06/2026", "Trung bình", "Chưa tiến hành", false, 0.0),
                createWorkCard("Chụp ảnh", "Team Building", "10/07/2026", "Cao", "Đang tiến hành", false, 0.8)
        );

        ScrollPane tasksScroll = new ScrollPane(tasksList);
        tasksScroll.setPrefHeight(450);
        format.formatScrollbar(tasksScroll, tasksList, 16);

        leftCol.getChildren().addAll(leftTitle, tasksScroll);

        VBox rightCol = format.formatBoxCard();
        rightCol.setPrefWidth(350);

        Label rightTitle = format.formatLabel("Tiến độ", FontWeight.BLACK, 18, "#1e293b");

        PieChart pieChart = new PieChart();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Đang tiến hành", 8),
                new PieChart.Data("Chưa tiến hành", 5),
                new PieChart.Data("Hoàn thành", 15),
                new PieChart.Data("Quá hạn", 1)
        );
        pieChart.setData(pieChartData);
        pieChart.setLabelsVisible(false);
        pieChart.setLegendVisible(false);
        pieChart.setPrefHeight(240);

        String chartCss = ".chart-pie { -fx-border-color: white; -fx-border-width: 2px; } " +
                ".default-color0.chart-pie { -fx-pie-color: #3b82f6; } " +
                ".default-color1.chart-pie { -fx-pie-color: #94a3b8; } " +
                ".default-color2.chart-pie { -fx-pie-color: #10b981; } " +
                ".default-color3.chart-pie { -fx-pie-color: #ef4444; }";
        pieChart.getStylesheets().add("data:text/css," + chartCss.replaceAll(" ", "%20"));

        VBox legendBox = new VBox(12);
        legendBox.setAlignment(Pos.CENTER);
        legendBox.setPadding(new Insets(16, 0, 0, 0));
        legendBox.getChildren().addAll(
                createLegendItem("#10b981", "Hoàn thành: 15"),
                createLegendItem("#3b82f6", "Đang tiến hành: 8"),
                createLegendItem("#94a3b8", "Chưa tiến hành: 5"),
                createLegendItem("#ef4444", "Quá hạn: 1")
        );

        rightCol.getChildren().addAll(rightTitle, pieChart, legendBox);

        row.getChildren().addAll(leftCol, rightCol);
        return row;
    }

    private VBox buildNotifRow() {
        VBox col = format.formatBoxCard();
        VBox.setVgrow(col, Priority.ALWAYS);

        Label title = format.formatLabel("Thông báo hệ thống", FontWeight.BLACK, 18, "#1e293b");

        VBox notifTable = new VBox(0);
        notifTable.getChildren().add(createNotifHeader());

        VBox notifRows = new VBox(0);
        notifRows.getChildren().addAll(
                createNotifRow("28/06 14:35", "Ban Truyền thông vừa tạo sự kiện mới trên cổng quản lý."),
                createNotifRow("28/06 08:00", "Tuyển thành viên K28 đã chính thức mở đơn ứng tuyển trực tuyến."),
                createNotifRow("27/06 16:20", "Biên bản cuộc họp toàn thể tháng 6 đã được ban thư ký tải lên."),
                createNotifRow("26/06 10:15", "Ban Tài chính yêu cầu duyệt khoản chi mua sắm thiết bị."),
                createNotifRow("25/06 09:00", "Hệ thống chuẩn bị bảo trì vào 00:00 đêm nay.")
        );

        ScrollPane notifScroll = new ScrollPane(notifRows);
        notifScroll.setPrefHeight(220);
        format.formatScrollbar(notifScroll, notifRows, 8);

        notifTable.getChildren().add(notifScroll);

        col.getChildren().addAll(title, notifTable);
        return col;
    }

    private VBox createWorkCard(String title, String event, String deadline, String priority, String status, boolean isOverdue, double progress) {
        VBox box = new VBox(16);
        box.setMaxWidth(Double.MAX_VALUE);
        box.setMinHeight(Region.USE_PREF_SIZE);
        box.setPadding(new Insets(24));
        box.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 32px; -fx-border-color: rgba(255,255,255,0.9); -fx-border-width: 2px; -fx-border-radius: 32px; -fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.1), 15, 0, 0, 6);");

        HBox topRow = new HBox(12);
        topRow.setAlignment(Pos.CENTER_LEFT);
        Label lblTitle = format.formatLabel(title, FontWeight.BLACK, 18, "#1e293b");

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        String stBg = status.equals("Chưa tiến hành") ? "rgba(100,116,139,0.15)" : status.equals("Đang tiến hành") ? "rgba(59,130,246,0.15)" : status.equals("Hoàn thành") ? "rgba(16,185,129,0.15)" : "rgba(239,68,68,0.15)";
        String stText = status.equals("Chưa tiến hành") ? "#64748b" : status.equals("Đang tiến hành") ? "#3b82f6" : status.equals("Hoàn thành") ? "#10b981" : "#ef4444";

        HBox badgeBox = new HBox(8);
        badgeBox.setAlignment(Pos.CENTER_RIGHT);

        Label statusBadge = format.formatBadge(status, stBg, stText);
        statusBadge.setStyle(statusBadge.getStyle() + "-fx-font-size: 11px; -fx-padding: 4 10;");
        badgeBox.getChildren().add(statusBadge);

        if (isOverdue && !status.equals("Hoàn thành")) {
            Label overdueBadge = format.formatBadge("Quá hạn", "rgba(239,68,68,0.15)", "#ef4444");
            overdueBadge.setStyle(overdueBadge.getStyle() + "-fx-font-size: 11px; -fx-padding: 4 10;");
            badgeBox.getChildren().add(0, overdueBadge);
        }

        topRow.getChildren().addAll(lblTitle, spacer, badgeBox);

        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(24); infoGrid.setVgap(8);

        String eventLabel = event.equals("Việc chung") ? "Việc chung" : event;
        infoGrid.add(format.formatLabel(eventLabel, FontWeight.BOLD, 13, event.equals("Việc chung") ? "#94a3b8" : "#7c4dff"), 0, 0, 2, 1);
        infoGrid.add(format.formatLabel("Hạn: " + deadline, FontWeight.BOLD, 13, isOverdue ? "#ef4444" : "#475569"), 0, 1);

        String prioColor = priority.equals("Cao") ? "#ef4444" : priority.equals("Trung bình") ? "#f59e0b" : "#10b981";
        infoGrid.add(format.formatLabel("Ưu tiên: " + priority, FontWeight.BOLD, 13, prioColor), 1, 1);

        HBox progressRow = new HBox(16);
        progressRow.setAlignment(Pos.CENTER_LEFT);

        Label lblProgText = format.formatLabel((int)(progress * 100) + "%", FontWeight.BLACK, 13, "#7c4dff");
        lblProgText.setPrefWidth(40);

        StackPane track = new StackPane();
        track.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(track, Priority.ALWAYS);
        track.setPrefHeight(15);
        track.setStyle("-fx-background-color: rgba(124,77,255,0.15); -fx-background-radius: 15px;");
        track.setAlignment(Pos.CENTER_LEFT);

        StackPane bar = new StackPane();
        bar.setPrefHeight(15);
        bar.setStyle("-fx-background-color: linear-gradient(to right, #448aff, #7c4dff); -fx-background-radius: 15px;");
        bar.maxWidthProperty().bind(track.widthProperty().multiply(progress));

        track.getChildren().add(bar);

        progressRow.getChildren().addAll(lblProgText, track);

        box.getChildren().addAll(topRow, infoGrid, progressRow);

        return box;
    }

    private HBox createLegendItem(String colorHex, String text) {
        HBox box = new HBox(8);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setMaxWidth(160);

        Circle dot = new Circle(6, Color.web(colorHex));
        Label lbl = format.formatLabel(text, FontWeight.BOLD, 13, "#1e293b");

        box.getChildren().addAll(dot, lbl);
        return box;
    }

    private HBox createNotifHeader() {
        HBox header = new HBox(16);
        header.setPadding(new Insets(12, 16, 12, 16));
        header.setStyle("-fx-border-color: transparent transparent rgba(0,0,0,0.1) transparent; -fx-border-width: 1px;");

        Label l1 = format.formatLabel("THỜI GIAN", FontWeight.BLACK, 10, "#94a3b8");
        l1.setPrefWidth(130);
        l1.setMinWidth(130);

        Label l2 = format.formatLabel("NỘI DUNG", FontWeight.BLACK, 10, "#94a3b8");
        HBox.setHgrow(l2, Priority.ALWAYS);
        l2.setMaxWidth(Double.MAX_VALUE);

        header.getChildren().addAll(l1, l2);
        return header;
    }

    private HBox createNotifRow(String time, String content) {
        HBox row = new HBox(16);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-border-color: transparent transparent rgba(0,0,0,0.05) transparent; -fx-border-width: 1px;");

        Label lblTime = format.formatLabel(time, FontWeight.BOLD, 12, "#64748b");
        lblTime.setPrefWidth(130);
        lblTime.setMinWidth(130);

        Label lblContent = format.formatLabel(content, FontWeight.MEDIUM, 13, "#1e293b");
        lblContent.setWrapText(true);
        HBox.setHgrow(lblContent, Priority.ALWAYS);

        row.getChildren().addAll(lblTime, lblContent);
        return row;
    }
}