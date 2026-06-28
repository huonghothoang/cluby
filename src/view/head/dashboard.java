package view.head;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
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
                buildMemberAndNotifRow()
        );

        this.setContent(mainContent);
    }

    private HBox buildHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Label mainTitle = format.formatLabel("Tổng quan bộ phận " + deptName, FontWeight.BLACK, 28, "#1e293b");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnNotif = new Button("🔔");
        btnNotif.setStyle("-fx-background-color: linear-gradient(to bottom right, #7c4dff, #448aff); -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 50%; -fx-min-width: 44px; -fx-min-height: 44px; -fx-effect: dropshadow(three-pass-box, rgba(124,77,255,0.4), 10, 0.3, 0, 4); -fx-cursor: hand;");

        btnNotif.setOnMouseEntered(e -> { btnNotif.setScaleX(1.05); btnNotif.setScaleY(1.05); });
        btnNotif.setOnMouseExited(e -> { btnNotif.setScaleX(1.0); btnNotif.setScaleY(1.0); });

        btnNotif.setOnAction(e -> {
            frame.getInstance().triggerToast("Chưa có thông báo mới.");
        });

        header.getChildren().addAll(mainTitle, spacer, btnNotif);
        return header;
    }

    private HBox buildStatsRow() {
        HBox row = new HBox(16);
        row.getChildren().addAll(
                createClickableKPICard("Nhân sự", "24 thành viên", "#64748b", "#1e293b", "Danh sách nhân sự"),
                createClickableKPICard("Đang làm", "8 công việc", "#3b82f6", "#1e293b", "Công việc hiện tại"),
                createClickableKPICard("Sắp tới hạn", "3 sự kiện", "#f59e0b", "#f59e0b", "Sự kiện sắp tới"),
                createClickableKPICard("Quá hạn", "2 công việc", "#ef4444", "#ef4444", "Công việc quá hạn")
        );
        for (javafx.scene.Node node : row.getChildren()) { HBox.setHgrow(node, Priority.ALWAYS); }
        return row;
    }

    private HBox buildTaskAndChartRow() {
        HBox row = new HBox(24);

        VBox leftCol = format.formatBoxCard();
        HBox.setHgrow(leftCol, Priority.ALWAYS);
        leftCol.setPrefWidth(850);

        Label leftTitle = format.formatLabel("📋 Công việc ưu tiên", FontWeight.BLACK, 18, "#1e293b");

        VBox tasksList = new VBox(20);
        tasksList.getChildren().addAll(
                createWorkCard("Thiết kế Poster", "Workshop GitHub", "26/06/2026", "Cao", "Đang làm", true, false, 0.6),
                createWorkCard("Viết bài nội bộ", "Việc chung", "29/06/2026", "Trung bình", "Chưa làm", false, true, 0.0),
                createWorkCard("Chụp ảnh", "Team Building", "10/07/2026", "Cao", "Đang làm", false, false, 0.8),
                createWorkCard("Duyệt kịch bản", "Họp CLB", "15/07/2026", "Thấp", "Đang làm", false, false, 0.3),
                createWorkCard("Tổng hợp kinh phí", "Workshop GitHub", "25/06/2026", "Cao", "Chưa làm", true, false, 0.0)
        );

        ScrollPane tasksScroll = new ScrollPane(tasksList);
        tasksScroll.setPrefHeight(450);
        format.formatScrollbar(tasksScroll, tasksList, 16);

        leftCol.getChildren().addAll(leftTitle, tasksScroll);

        VBox rightCol = format.formatBoxCard();
        rightCol.setPrefWidth(350);

        Label rightTitle = format.formatLabel("📊 Tiến độ", FontWeight.BLACK, 18, "#1e293b");

        PieChart pieChart = new PieChart();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Đang làm", 8),
                new PieChart.Data("Chưa làm", 5),
                new PieChart.Data("Xong", 15),
                new PieChart.Data("Hủy", 1)
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
                createLegendItem("#10b981", "Xong: 15"),
                createLegendItem("#3b82f6", "Đang làm: 8"),
                createLegendItem("#94a3b8", "Chưa làm: 5"),
                createLegendItem("#ef4444", "Hủy: 1")
        );

        rightCol.getChildren().addAll(rightTitle, pieChart, legendBox);

        row.getChildren().addAll(leftCol, rightCol);
        return row;
    }

    private HBox buildMemberAndNotifRow() {
        HBox row = new HBox(24);

        VBox leftCol = format.formatBoxCard();
        HBox.setHgrow(leftCol, Priority.ALWAYS);
        leftCol.setPrefWidth(600);

        Label leftTitle = format.formatLabel("⭐ Nhân sự nổi bật", FontWeight.BLACK, 18, "#1e293b");

        VBox memberList = new VBox(12);
        memberList.getChildren().addAll(
                createTopMemberRow("trish.jpeg", "Nguyễn Văn A", "Trưởng nhóm", 6, 5),
                createTopMemberRow("trish.jpeg", "Lê Thị B", "Thành viên cốt cán", 4, 4),
                createTopMemberRow("trish.jpeg", "Trần Văn C", "Thành viên", 3, 3),
                createTopMemberRow("trish.jpeg", "Phạm Thị D", "Thành viên mới", 2, 2),
                createTopMemberRow("trish.jpeg", "Hoàng Văn E", "Thành viên", 2, 1)
        );

        ScrollPane memberScroll = new ScrollPane(memberList);
        memberScroll.setPrefHeight(260);
        format.formatScrollbar(memberScroll, memberList, 16);

        leftCol.getChildren().addAll(leftTitle, memberScroll);

        VBox rightCol = format.formatBoxCard();
        HBox.setHgrow(rightCol, Priority.ALWAYS);
        rightCol.setPrefWidth(600);

        Label rightTitle = format.formatLabel("📢 Thông báo", FontWeight.BLACK, 18, "#1e293b");

        VBox notifList = new VBox(12);
        notifList.getChildren().addAll(
                createNotifRow("Đã khởi tạo sự kiện mới.", "#6366f1"),
                createNotifRow("Mở đơn ứng tuyển thế hệ mới.", "#ec4899"),
                createNotifRow("Cập nhật biên bản họp tháng 6.", "#f59e0b"),
                createNotifRow("Yêu cầu duyệt chi thiết bị.", "#ef4444"),
                createNotifRow("Hệ thống bảo trì lúc 00:00.", "#3b82f6")
        );

        ScrollPane notifScroll = new ScrollPane(notifList);
        notifScroll.setPrefHeight(260);
        format.formatScrollbar(notifScroll, notifList, 16);

        rightCol.getChildren().addAll(rightTitle, notifScroll);

        row.getChildren().addAll(leftCol, rightCol);
        return row;
    }

    private VBox createClickableKPICard(String title, String value, String titleColor, String valColor, String filterTag) {
        VBox box = format.formatKPICard(title, value, titleColor, valColor);
        box.setStyle("-fx-background-color: rgba(255,255,255,0.5); -fx-background-radius: 32px; -fx-border-width: 0px; -fx-border-color: transparent; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.05), 10, 0, 0, 4);");

        box.setOnMouseEntered(e -> {
            box.setScaleX(1.03);
            box.setScaleY(1.03);
            box.setStyle("-fx-background-color: rgba(255,255,255,0.85); -fx-background-radius: 32px; -fx-border-width: 0px; -fx-border-color: transparent; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.25), 25, 0.3, 0, 8);");
        });
        box.setOnMouseExited(e -> {
            box.setScaleX(1.0);
            box.setScaleY(1.0);
            box.setStyle("-fx-background-color: rgba(255,255,255,0.5); -fx-background-radius: 32px; -fx-border-width: 0px; -fx-border-color: transparent; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.05), 10, 0, 0, 4);");
        });
        box.setOnMouseClicked(e -> frame.getInstance().triggerToast("Lọc theo: " + filterTag));
        return box;
    }

    private VBox createWorkCard(String title, String event, String deadline, String priority, String status, boolean isOverdue, boolean isNearDeadline, double progress) {
        VBox box = new VBox(16);
        box.setMaxWidth(Double.MAX_VALUE);
        box.setMinHeight(Region.USE_PREF_SIZE);
        box.setPadding(new Insets(24));
        box.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 32px; -fx-border-color: rgba(255,255,255,0.9); -fx-border-width: 2px; -fx-border-radius: 32px; -fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.1), 15, 0, 0, 6);");

        HBox topRow = new HBox(12);
        topRow.setAlignment(Pos.CENTER_LEFT);
        Label lblTitle = format.formatLabel(title, FontWeight.BLACK, 18, "#1e293b");
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        String stBg = status.equals("Chưa làm") ? "rgba(100,116,139,0.15)" : status.equals("Đang làm") ? "rgba(59,130,246,0.15)" : status.equals("Xong") ? "rgba(16,185,129,0.15)" : "rgba(239,68,68,0.15)";
        String stText = status.equals("Chưa làm") ? "#64748b" : status.equals("Đang làm") ? "#3b82f6" : status.equals("Xong") ? "#10b981" : "#ef4444";

        HBox badgeBox = new HBox(8);
        badgeBox.setAlignment(Pos.CENTER_RIGHT);
        Label statusBadge = format.formatBadge(status, stBg, stText);
        statusBadge.setStyle(statusBadge.getStyle() + "-fx-font-size: 11px; -fx-padding: 4 10;");
        badgeBox.getChildren().add(statusBadge);

        if (isOverdue && !status.equals("Xong") && !status.equals("Hủy")) {
            Label overdueBadge = format.formatBadge("⚠ Quá hạn", "rgba(239,68,68,0.15)", "#ef4444");
            overdueBadge.setStyle(overdueBadge.getStyle() + "-fx-font-size: 11px; -fx-padding: 4 10;");
            badgeBox.getChildren().add(0, overdueBadge);
        } else if (isNearDeadline && !status.equals("Xong") && !status.equals("Hủy")) {
            Label nearBadge = format.formatBadge("⏰ Gần hạn", "rgba(245,158,11,0.15)", "#f59e0b");
            nearBadge.setStyle(nearBadge.getStyle() + "-fx-font-size: 11px; -fx-padding: 4 10;");
            badgeBox.getChildren().add(0, nearBadge);
        }

        topRow.getChildren().addAll(lblTitle, spacer, badgeBox);

        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(24); infoGrid.setVgap(8);
        String eventLabel = event.equals("Việc chung") ? "🏢 Việc chung" : "📅 " + event;
        infoGrid.add(format.formatLabel(eventLabel, FontWeight.BOLD, 13, event.equals("Việc chung") ? "#94a3b8" : "#7c4dff"), 0, 0, 2, 1);
        infoGrid.add(format.formatLabel("⏳ Hạn: " + deadline, FontWeight.BOLD, 13, isOverdue ? "#ef4444" : "#475569"), 0, 1);

        String prioColor = priority.equals("Cao") ? "#ef4444" : priority.equals("Trung bình") ? "#f59e0b" : "#10b981";
        infoGrid.add(format.formatLabel("🔥 Ưu tiên: " + priority, FontWeight.BOLD, 13, prioColor), 1, 1);

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

        HBox actionsRow = new HBox(12);
        actionsRow.setAlignment(Pos.CENTER_RIGHT);

        Button btnView = new Button("Chi tiết");
        btnView.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));
        btnView.setTextFill(Color.WHITE);
        btnView.setStyle("-fx-background-color: #5020d8; -fx-background-radius: 20px; -fx-padding: 8 20; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(80,32,216,0.4), 10, 0, 0, 4);");
        btnView.setOnMouseEntered(e -> { btnView.setScaleX(1.05); btnView.setScaleY(1.05); });
        btnView.setOnMouseExited(e -> { btnView.setScaleX(1.0); btnView.setScaleY(1.0); });

        btnView.setOnAction(e -> {
            frame.getInstance().triggerToast("Xem chi tiết: " + title);
        });

        actionsRow.getChildren().add(btnView);

        box.getChildren().addAll(topRow, infoGrid, progressRow, actionsRow);
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

    private HBox createTopMemberRow(String avatarUrl, String name, String role, int doing, int done) {
        HBox row = new HBox(16);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setAlignment(Pos.CENTER_LEFT);
        format.formatGlass(row, 16, 0.4);
        row.setMaxWidth(Double.MAX_VALUE);

        ImageView avatar = new ImageView(new Image(avatarUrl));
        avatar.setFitWidth(44); avatar.setFitHeight(44);
        avatar.setClip(new Circle(22, 22, 22));

        VBox info = new VBox(4);
        info.getChildren().addAll(
                format.formatLabel(name, FontWeight.BOLD, 14, "#1e293b"),
                format.formatLabel(role, FontWeight.BOLD, 11, "#7c4dff")
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox stats = new VBox(4);
        stats.setAlignment(Pos.CENTER_RIGHT);
        stats.getChildren().addAll(
                format.formatLabel(doing + " việc", FontWeight.BOLD, 12, "#3b82f6"),
                format.formatLabel(done + " xong", FontWeight.BOLD, 12, "#10b981")
        );

        row.getChildren().addAll(avatar, info, spacer, stats);
        return row;
    }

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