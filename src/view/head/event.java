package view.head;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import view.format;

public class event extends ScrollPane {

    private static final String CURRENT_HEAD_DEPT = "Truyền thông";
    private static final VBox studentJoinRows = new VBox(6);
    private static final VBox joinLogRows = new VBox(4);

    public event() {
        setPickOnBounds(false);

        VBox dashboardContent = new VBox(32);
        dashboardContent.setPadding(new Insets(32));
        dashboardContent.setStyle("-fx-background-color: transparent;");

        HBox kpiRow = new HBox(16);
        kpiRow.getChildren().addAll(
                format.formatKPICard("Tổng sự kiện", "24", "#64748b", "#1e293b"),
                format.formatKPICard("🔵 Sắp diễn ra", "3", "#3b82f6", "#1e293b"),
                format.formatKPICard("🟡 Đang diễn ra", "1", "#f59e0b", "#1e293b"),
                format.formatKPICard("🟢 Đã kết thúc", "18", "#10b981", "#1e293b")
        );
        for (Node node : kpiRow.getChildren()) { HBox.setHgrow(node, Priority.ALWAYS); }

        HBox filterBar = new HBox(16);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(12, 24, 12, 24));
        format.formatGlass(filterBar, 40, 0.4);

        HBox searchBox = new HBox(8);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = format.formatTextField("Tìm kiếm sự kiện...");
        Button btnSearch = format.formatFindBtn();
        searchBox.getChildren().addAll(searchField, btnSearch);

        ComboBox<String> cbStatus = format.formatSortBtn("Lọc trạng thái", "Tất cả", "Sắp diễn ra", "Đang diễn ra", "Đã kết thúc", "Đã hủy");
        ComboBox<String> cbTime = format.formatSortBtn("Lọc thời gian", "Tất cả", "Tháng này", "Quý này", "Năm nay");
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        filterBar.getChildren().addAll(searchBox, cbStatus, cbTime, spacer);

        VBox eventTableContainer = format.formatTableContainer();
        Label tableTitle = format.formatLabel("📅 DANH SÁCH SỰ KIỆN CLB", FontWeight.BLACK, 14, "#1e293b");
        tableTitle.setPadding(new Insets(12, 16, 0, 16));
        eventTableContainer.getChildren().addAll(tableTitle, createEventTableHeader());

        VBox eventRows = new VBox(4);
        eventRows.getChildren().addAll(
                createEventRow("Workshop GitHub", "28/06/2026", "08:00 - 11:30", "Hội trường A", "Truyền thông", "Sắp diễn ra", "Chưa tham gia"),
                createEventRow("Họp toàn CLB", "26/06/2026", "19:00 - 21:00", "Google Meet (Online)", "Sự kiện", "Đang diễn ra", "Đã tham gia"),
                createEventRow("Team Building ĐN", "12/06/2026", "07:00 - 17:00", "Biển Mỹ Khê", "Nhân sự", "Đã kết thúc", "Đã tham gia")
        );

        ScrollPane eventScroll = new ScrollPane(eventRows);
        eventScroll.setPrefHeight(220);
        format.formatScrollbar(eventScroll, eventRows, 8);
        applySmoothScroll(eventScroll, eventRows);
        eventTableContainer.getChildren().add(eventScroll);

        VBox logTableContainer = format.formatTableContainer();
        Label logTitle = format.formatLabel("📜 NHẬT KÝ HOẠT ĐỘNG THAM GIA SỰ KIỆN", FontWeight.BLACK, 14, "#1e293b");
        logTitle.setPadding(new Insets(12, 16, 0, 16));
        logTableContainer.getChildren().addAll(logTitle, createLogTableHeader());

        if (joinLogRows.getChildren().isEmpty()) {
            addLogEntry("26/06/2026 19:15:30", "Đã tham gia sự kiện hệ thống: Họp toàn CLB thành công");
        }

        ScrollPane logScroll = new ScrollPane(joinLogRows);
        logScroll.setPrefHeight(180);
        format.formatScrollbar(logScroll, joinLogRows, 8);
        applySmoothScroll(logScroll, joinLogRows);
        logTableContainer.getChildren().add(logScroll);

        HBox attendanceRow = buildAttendanceRow();

        dashboardContent.getChildren().addAll(kpiRow, filterBar, eventTableContainer, logTableContainer, attendanceRow);
        format.formatScrollbar(this, dashboardContent, 12);
        this.setContent(dashboardContent);
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

    private void invokeShowModal(VBox modal) {
        if (view.head.frame.getInstance() != null) {
            view.head.frame.getInstance().showCustomModal(modal);
        } else if (view.mem.frame.getInstance() != null) {
            view.mem.frame.getInstance().showCustomModal(modal);
        }
    }

    private void invokeCloseModal() {
        if (view.head.frame.getInstance() != null) {
            view.head.frame.getInstance().closeOverlayModal();
        } else if (view.mem.frame.getInstance() != null) {
            view.mem.frame.getInstance().closeOverlayModal();
        }
    }

    private void invokeToast(String msg) {
        if (view.head.frame.getInstance() != null) {
            view.head.frame.getInstance().triggerToast(msg);
        } else if (view.mem.frame.getInstance() != null) {
            view.mem.frame.getInstance().triggerToast(msg);
        }
    }

    private HBox createEventTableHeader() {
        HBox header = new HBox(16);
        header.setPadding(new Insets(12, 16, 12, 16));
        header.setStyle("-fx-border-color: transparent transparent rgba(255,255,255,0.4) transparent; -fx-border-width: 1px;");

        Label l1 = format.formatLabel("TÊN SỰ KIỆN", FontWeight.BLACK, 10, "#94a3b8"); l1.setPrefWidth(240);
        Label l2 = format.formatLabel("THỜI GIAN", FontWeight.BLACK, 10, "#94a3b8"); l2.setPrefWidth(140);
        Label l3 = format.formatLabel("ĐỊA ĐIỂM", FontWeight.BLACK, 10, "#94a3b8"); l3.setPrefWidth(160);
        Label l4 = format.formatLabel("BAN TỔ CHỨC", FontWeight.BLACK, 10, "#94a3b8"); l4.setPrefWidth(110);
        Label l5 = format.formatLabel("TRẠNG THÁI", FontWeight.BLACK, 10, "#94a3b8"); l5.setPrefWidth(110);
        Label l6 = format.formatLabel("CHI TIẾT", FontWeight.BLACK, 10, "#94a3b8"); l6.setPrefWidth(80);

        header.getChildren().addAll(l1, l2, l3, l4, l5, l6);
        return header;
    }

    private HBox createEventRow(String title, String date, String time, String loc, String leader, String status, String userJoinState) {
        HBox row = new HBox(16);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand;");
        row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand; -fx-background-radius: 12px;"));
        row.setOnMouseExited(e -> row.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand;"));

        Label lblTitle = format.formatLabel(title, FontWeight.BOLD, 14, "#1e293b"); lblTitle.setPrefWidth(240); lblTitle.setWrapText(true);
        Label lblTime = format.formatLabel("📅 " + date + " | ⏰ " + time, FontWeight.MEDIUM, 12, "#475569"); lblTime.setPrefWidth(140); lblTime.setWrapText(true);
        Label lblLoc = format.formatLabel(loc, FontWeight.MEDIUM, 13, "#475569"); lblLoc.setPrefWidth(160); lblLoc.setWrapText(true);
        Label lblLeader = format.formatLabel(leader, FontWeight.BOLD, 12, "#7c4dff"); lblLeader.setPrefWidth(110);

        String stBg = status.equals("Sắp diễn ra") ? "rgba(59,130,246,0.15)" : status.equals("Đang diễn ra") ? "rgba(245,158,11,0.15)" : "rgba(16,185,129,0.15)";
        String stText = status.equals("Sắp diễn ra") ? "#3b82f6" : status.equals("Đang diễn ra") ? "#f59e0b" : "#10b981";
        HBox statusBox = new HBox(format.formatBadge(status, stBg, stText)); statusBox.setAlignment(Pos.CENTER_LEFT); statusBox.setPrefWidth(110);

        HBox actionBox = new HBox(8); actionBox.setAlignment(Pos.CENTER_LEFT); actionBox.setPrefWidth(80);
        Button btnView = format.formatCircleBtn("👁️‍🗨️", "#448aff", "#7c4dff");
        btnView.setOnAction(e -> {
            VBox modal = createEventDetailModal(title, date, time, loc, leader, status, 120, "Mô tả chi tiết hoạt động sự kiện nội bộ.", userJoinState);
            invokeShowModal(modal);
        });
        actionBox.getChildren().add(btnView);

        row.getChildren().addAll(lblTitle, lblTime, lblLoc, lblLeader, statusBox, actionBox);
        return row;
    }

    private HBox createLogTableHeader() {
        HBox header = new HBox(24);
        header.setPadding(new Insets(12, 16, 12, 16));
        header.setStyle("-fx-border-color: transparent transparent rgba(255,255,255,0.4) transparent; -fx-border-width: 1px;");

        Label l1 = format.formatLabel("THỜI GIAN GHI NHẬN", FontWeight.BLACK, 10, "#94a3b8"); l1.setPrefWidth(220);
        Label l2 = format.formatLabel("NỘI DUNG HOẠT ĐỘNG THAM GIA SỰ KIỆN", FontWeight.BLACK, 10, "#94a3b8"); l2.setPrefWidth(650);

        header.getChildren().addAll(l1, l2);
        return header;
    }

    private static void addLogEntry(String timeStamp, String content) {
        HBox row = new HBox(24);
        row.setPadding(new Insets(10, 16, 10, 16));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-border-color: transparent transparent rgba(255,255,255,0.2) transparent; -fx-border-width: 1px;");

        Label lblTime = format.formatLabel(timeStamp, FontWeight.MEDIUM, 13, "#64748b"); lblTime.setPrefWidth(220);
        Label lblLog = format.formatLabel(content, FontWeight.BOLD, 13, "#1e293b"); lblLog.setPrefWidth(650); lblLog.setWrapText(true);

        row.getChildren().addAll(lblTime, lblLog);
        joinLogRows.getChildren().add(0, row);
    }

    private HBox buildAttendanceRow() {
        HBox row = new HBox(24);

        VBox chartBox = format.formatBoxCard();
        HBox.setHgrow(chartBox, Priority.ALWAYS);
        chartBox.setPrefWidth(400);
        chartBox.getChildren().add(format.formatLabel("TỶ LỆ ĐIỂM DANH THEO THÀNH VIÊN", FontWeight.BLACK, 14, "#1e293b"));

        VBox attList = new VBox(8);
        attList.getChildren().addAll(
                createAttendanceProgressCard("trish.jpeg", "Nguyễn Văn A", 0.8, 0.1, 0.1),
                createAttendanceProgressCard("trish.jpeg", "Trần Văn B", 0.7, 0.2, 0.1),
                createAttendanceProgressCard("trish.jpeg", "Lê Văn C", 0.9, 0.05, 0.05),
                createAttendanceProgressCard("trish.jpeg", "Phạm Văn D", 0.6, 0.1, 0.3),
                createAttendanceProgressCard("trish.jpeg", "Hoàng Thị E", 1.0, 0.0, 0.0),
                createAttendanceProgressCard("trish.jpeg", "Ngô Văn F", 0.5, 0.5, 0.0)
        );

        ScrollPane attScroll = new ScrollPane(attList);
        attScroll.setPrefHeight(260);
        format.formatScrollbar(attScroll, attList, 8);
        applySmoothScroll(attScroll, attList);
        chartBox.getChildren().add(attScroll);

        VBox logBox = format.formatBoxCard();
        HBox.setHgrow(logBox, Priority.ALWAYS);
        logBox.setPrefWidth(550);
        logBox.getChildren().add(format.formatLabel("NHẬT KÝ ĐIỂM DANH", FontWeight.BLACK, 14, "#1e293b"));

        VBox tableContainer = new VBox(0);
        HBox header = new HBox(12);
        header.setPadding(new Insets(12, 8, 12, 8));
        header.setStyle("-fx-border-color: transparent transparent rgba(0,0,0,0.1) transparent; -fx-border-width: 1px;");
        Label l1 = format.formatLabel("AVATAR", FontWeight.BLACK, 10, "#94a3b8"); l1.setPrefWidth(40);
        Label l2 = format.formatLabel("THÀNH VIÊN", FontWeight.BLACK, 10, "#94a3b8"); l2.setPrefWidth(120);
        Label l3 = format.formatLabel("SỰ KIỆN", FontWeight.BLACK, 10, "#94a3b8"); HBox.setHgrow(l3, Priority.ALWAYS); l3.setMaxWidth(Double.MAX_VALUE);
        Label l4 = format.formatLabel("NGÀY", FontWeight.BLACK, 10, "#94a3b8"); l4.setPrefWidth(60);
        Label l5 = format.formatLabel("TÌNH TRẠNG", FontWeight.BLACK, 10, "#94a3b8"); l5.setPrefWidth(90);
        header.getChildren().addAll(l1, l2, l3, l4, l5);
        tableContainer.getChildren().add(header);

        VBox rows = new VBox(0);
        rows.getChildren().addAll(
                createLogAttendanceRow("trish.jpeg", "Nguyễn Văn A", "Workshop GitHub", "28/06", "Có mặt"),
                createLogAttendanceRow("trish.jpeg", "Trần Văn B", "Workshop GitHub", "28/06", "Có mặt"),
                createLogAttendanceRow("trish.jpeg", "Lê Văn C", "Họp toàn CLB", "26/06", "Có phép"),
                createLogAttendanceRow("trish.jpeg", "Phạm Văn D", "Team Building", "12/06", "Vắng"),
                createLogAttendanceRow("trish.jpeg", "Hoàng Văn E", "Họp toàn CLB", "26/06", "Có mặt"),
                createLogAttendanceRow("trish.jpeg", "Phạm Thị F", "Workshop GitHub", "28/06", "Có phép"),
                createLogAttendanceRow("trish.jpeg", "Ngô Văn G", "Team Building", "12/06", "Có mặt")
        );

        ScrollPane scroll = new ScrollPane(rows);
        scroll.setPrefHeight(230);
        format.formatScrollbar(scroll, rows, 8);
        applySmoothScroll(scroll, rows);

        tableContainer.getChildren().add(scroll);
        logBox.getChildren().add(tableContainer);

        row.getChildren().addAll(chartBox, logBox);
        return row;
    }

    private HBox createAttendanceProgressCard(String avatarUrl, String name, double present, double excused, double absent) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12));
        row.setStyle("-fx-border-color: transparent transparent rgba(0,0,0,0.05) transparent; -fx-border-width: 1px;");

        ImageView avt = new ImageView(new Image(avatarUrl));
        avt.setFitWidth(36); avt.setFitHeight(36); avt.setClip(new Circle(18, 18, 18));
        HBox avatarBox = new HBox(avt); avatarBox.setPrefWidth(40);

        VBox info = new VBox(6); HBox.setHgrow(info, Priority.ALWAYS);
        HBox nameRow = new HBox(); nameRow.setAlignment(Pos.CENTER_LEFT);
        Label lblName = format.formatLabel(name, FontWeight.BOLD, 13, "#1e293b");
        Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
        nameRow.getChildren().addAll(lblName, sp);

        HBox bar = new HBox(); bar.setPrefHeight(8); bar.setMaxWidth(Double.MAX_VALUE);
        StackPane pBar = new StackPane(); pBar.setPrefHeight(8);
        StackPane eBar = new StackPane(); eBar.setPrefHeight(8);
        StackPane aBar = new StackPane(); aBar.setPrefHeight(8);

        pBar.prefWidthProperty().bind(bar.widthProperty().multiply(present));
        eBar.prefWidthProperty().bind(bar.widthProperty().multiply(excused));
        aBar.prefWidthProperty().bind(bar.widthProperty().multiply(absent));

        String pRad = (absent == 0 && excused == 0) ? "8px" : "8px 0 0 8px";
        pBar.setStyle("-fx-background-color: #10b981; -fx-background-radius: " + pRad + ";");
        String eRad = absent == 0 ? "0 8px 8px 0" : (present == 0 ? "8px 0 0 8px" : "0");
        if (present == 0 && absent == 0) eRad = "8px";
        eBar.setStyle("-fx-background-color: #f59e0b; -fx-background-radius: " + eRad + ";");
        String aRad = (present == 0 && excused == 0) ? "8px" : "0 8px 8px 0";
        aBar.setStyle("-fx-background-color: #ef4444; -fx-background-radius: " + aRad + ";");

        if (present > 0) bar.getChildren().add(pBar);
        if (excused > 0) bar.getChildren().add(eBar);
        if (absent > 0) bar.getChildren().add(aBar);

        HBox labels = new HBox(12);
        if (present > 0) labels.getChildren().add(format.formatLabel((int)(present*100) + "% Có mặt", FontWeight.BOLD, 10, "#10b981"));
        if (excused > 0) labels.getChildren().add(format.formatLabel((int)(excused*100) + "% Phép", FontWeight.BOLD, 10, "#f59e0b"));
        if (absent > 0) labels.getChildren().add(format.formatLabel((int)(absent*100) + "% Vắng", FontWeight.BOLD, 10, "#ef4444"));

        info.getChildren().addAll(nameRow, bar, labels);
        row.getChildren().addAll(avatarBox, info);
        return row;
    }

    private HBox createLogAttendanceRow(String avatarUrl, String name, String event, String date, String status) {
        HBox row = new HBox(12);
        row.setPadding(new Insets(12, 8, 12, 8));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-border-color: transparent transparent rgba(0,0,0,0.05) transparent; -fx-border-width: 1px;");

        ImageView avt = new ImageView(new Image(avatarUrl));
        avt.setFitWidth(30); avt.setFitHeight(30); avt.setClip(new Circle(15, 15, 15));
        HBox avatarBox = new HBox(avt); avatarBox.setPrefWidth(40); avatarBox.setAlignment(Pos.CENTER_LEFT);

        Label lblName = format.formatLabel(name, FontWeight.BOLD, 12, "#1e293b"); lblName.setPrefWidth(120);
        Label lblEvent = format.formatLabel(event, FontWeight.BOLD, 12, "#475569"); HBox.setHgrow(lblEvent, Priority.ALWAYS); lblEvent.setMaxWidth(Double.MAX_VALUE);
        Label lblDate = format.formatLabel(date, FontWeight.BOLD, 11, "#94a3b8"); lblDate.setPrefWidth(60);

        String stBg = status.equals("Có mặt") ? "rgba(16,185,129,0.15)" : status.equals("Có phép") ? "rgba(245,158,11,0.15)" : "rgba(239,68,68,0.15)";
        String stText = status.equals("Có mặt") ? "#10b981" : status.equals("Có phép") ? "#f59e0b" : "#ef4444";
        Label badge = format.formatBadge(status, stBg, stText);
        badge.setStyle(badge.getStyle() + "-fx-font-size: 11px; -fx-padding: 4 8;");
        HBox statusBox = new HBox(badge); statusBox.setPrefWidth(90); statusBox.setAlignment(Pos.CENTER_LEFT);

        row.getChildren().addAll(avatarBox, lblName, lblEvent, lblDate, statusBox);
        return row;
    }

    private VBox createEventDetailModal(String title, String date, String time, String loc, String leader, String status, int participants, String desc, String initialJoinState) {
        VBox rootModal = new VBox();
        rootModal.setAlignment(Pos.CENTER);

        StackPane modalContentStack = new StackPane();

        VBox viewMode = new VBox(20);
        viewMode.setPrefWidth(880);
        viewMode.setMaxWidth(880);
        viewMode.setPadding(new Insets(32));
        viewMode.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        viewMode.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        HBox splitRow = new HBox(28);

        VBox infoBox = new VBox(16);
        infoBox.setPrefWidth(480); infoBox.setMaxWidth(480);

        Label lblTitle = format.formatLabel(title, FontWeight.BLACK, 22, "#1e293b"); lblTitle.setWrapText(true);
        String stBg = status.equals("Sắp diễn ra") ? "rgba(59,130,246,0.15)" : status.equals("Đang diễn ra") ? "rgba(245,158,11,0.15)" : "rgba(16,185,129,0.15)";
        String stText = status.equals("Sắp diễn ra") ? "#3b82f6" : status.equals("Đang diễn ra") ? "#f59e0b" : "#10b981";
        HBox badgeLine = new HBox(8, format.formatBadge(status, stBg, stText), format.formatBadge(leader, "rgba(124,77,255,0.1)", "#7c4dff"));

        VBox cardDetails = format.formatBoxCard();
        cardDetails.setPadding(new Insets(20));
        cardDetails.setSpacing(14);

        Label textDesc = format.formatLabel(desc, FontWeight.MEDIUM, 13, "#475569");
        textDesc.setWrapText(true);

        cardDetails.getChildren().addAll(
                new VBox(4, format.formatLabel("Thời gian tổ chức", FontWeight.BOLD, 11, "#94a3b8"), format.formatLabel(date + " | " + time, FontWeight.BOLD, 14, "#1e293b")),
                new VBox(4, format.formatLabel("Địa điểm diễn ra", FontWeight.BOLD, 11, "#94a3b8"), format.formatLabel(loc, FontWeight.BOLD, 14, "#1e293b")),
                new VBox(4, format.formatLabel("Nội dung mô tả chi tiết", FontWeight.BOLD, 11, "#94a3b8"), textDesc)
        );
        infoBox.getChildren().addAll(lblTitle, badgeLine, cardDetails);

        VBox memberListCard = format.formatBoxCard();
        memberListCard.setPrefWidth(320); memberListCard.setMaxWidth(320);
        memberListCard.setPadding(new Insets(20));
        Label listTitle = format.formatLabel("👥 SINH VIÊN ĐĂNG KÝ THAM GIA", FontWeight.BLACK, 12, "#94a3b8");

        studentJoinRows.getChildren().clear();
        addStudentToMockRow("23HT011", "Cao Văn Tiến");
        addStudentToMockRow("24SK019", "Lê Phương Thảo");

        HBox userRealtimeRow = new HBox(12);
        userRealtimeRow.setAlignment(Pos.CENTER_LEFT);
        userRealtimeRow.setPadding(new Insets(6, 12, 6, 12));
        userRealtimeRow.setStyle("-fx-background-color: rgba(16,185,129,0.1); -fx-background-radius: 10px;");
        userRealtimeRow.getChildren().addAll(
                format.formatLabel("CURRENT_HEAD_ID", FontWeight.BOLD, 11, "#10b981"),
                format.formatLabel("Bạn (Trưởng ban)", FontWeight.BOLD, 13, "#1e293b")
        );

        if (initialJoinState.equals("Đã tham gia")) {
            studentJoinRows.getChildren().add(0, userRealtimeRow);
        }

        ScrollPane scrollStudent = new ScrollPane(studentJoinRows);
        scrollStudent.setPrefHeight(180);
        format.formatScrollbar(scrollStudent, studentJoinRows, 6);
        memberListCard.getChildren().addAll(listTitle, scrollStudent);

        splitRow.getChildren().addAll(infoBox, memberListCard);

        HBox actionRow = new HBox(16);
        actionRow.setAlignment(Pos.CENTER);

        Button btnClose = getFormBtn("Đóng", "rgba(148, 163, 184, 0.2)", "#64748b");
        btnClose.setOnAction(e -> invokeCloseModal());
        actionRow.getChildren().add(btnClose);

        Button btnJoin = getFormBtn("Tham gia", "#10b981", "white");
        Button btnCancelJoin = getFormBtn("Hủy tham gia", "#ef4444", "white");

        if (leader.equals(CURRENT_HEAD_DEPT)) {
            btnJoin.setVisible(false); btnJoin.setManaged(false);
            btnCancelJoin.setVisible(false); btnCancelJoin.setManaged(false);
        } else {
            btnJoin.setVisible(true); btnJoin.setManaged(true);
            btnCancelJoin.setVisible(true); btnCancelJoin.setManaged(true);

            if (initialJoinState.equals("Đã tham gia")) {
                btnJoin.setVisible(false); btnJoin.setManaged(false);
            } else {
                btnCancelJoin.setVisible(false); btnCancelJoin.setManaged(false);
            }
        }

        btnJoin.setOnAction(e -> {
            btnJoin.setVisible(false); btnJoin.setManaged(false);
            btnCancelJoin.setVisible(true); btnCancelJoin.setManaged(true);

            if (!studentJoinRows.getChildren().contains(userRealtimeRow)) {
                studentJoinRows.getChildren().add(0, userRealtimeRow);
            }

            invokeToast("Hãy tham gia đúng giờ nhé!");
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            addLogEntry(timestamp, "Đã đăng ký tham gia sự kiện ban khác: " + title);
        });

        btnCancelJoin.setOnAction(e -> {
            VBox confirmModal = format.formatSimpleModal(
                    "Xác nhận hủy", "Bạn có chắc chắn muốn hủy đăng ký tham gia sự kiện ban khác không?", "Quay lại", "Xác nhận",
                    ev -> invokeCloseModal(),
                    ev -> {
                        invokeCloseModal();

                        btnCancelJoin.setVisible(false); btnCancelJoin.setManaged(false);
                        btnJoin.setVisible(true); btnJoin.setManaged(true);

                        studentJoinRows.getChildren().remove(userRealtimeRow);

                        invokeToast("Đã hủy đăng ký tham gia.");
                        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                        addLogEntry(timestamp, "Đã hủy đăng ký tham gia sự kiện: " + title);
                    }
            );
            confirmModal.setMaxSize(400, Region.USE_PREF_SIZE);
            invokeShowModal(confirmModal);
        });

        if (!leader.equals(CURRENT_HEAD_DEPT)) {
            actionRow.getChildren().addAll(btnJoin, btnCancelJoin);
        }

        Region blankSpacer = new Region();
        VBox.setVgrow(blankSpacer, Priority.ALWAYS);
        blankSpacer.setPrefHeight(16);

        viewMode.getChildren().addAll(splitRow, blankSpacer, actionRow);
        modalContentStack.getChildren().add(viewMode);
        rootModal.getChildren().add(modalContentStack);

        return rootModal;
    }

    private void addStudentToMockRow(String mssv, String name) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(4, 8, 4, 8));
        row.setStyle("-fx-border-color: transparent transparent #f1f5f9 transparent; -fx-border-width: 1px;");
        row.getChildren().addAll(format.formatLabel(mssv, FontWeight.BOLD, 11, "#94a3b8"), format.formatLabel(name, FontWeight.MEDIUM, 13, "#334155"));
        studentJoinRows.getChildren().add(row);
    }

    private static Button getFormBtn(String text, String bgColor, String textColor) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));
        btn.setTextFill(Color.web(textColor));
        btn.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 40px; -fx-padding: 8 16 8 16; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> { btn.setScaleX(1.05); btn.setScaleY(1.05); });
        btn.setOnMouseExited(e -> { btn.setScaleX(1.0); btn.setScaleY(1.0); });
        return btn;
    }
}