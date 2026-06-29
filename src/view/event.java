package view;

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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class event extends ScrollPane {

    private VBox dashboardContent;
    private static final VBox joinLogRows = new VBox(4);

    public event() {
        setPickOnBounds(false);

        dashboardContent = new VBox(32);
        dashboardContent.setPadding(new Insets(32));
        dashboardContent.setStyle("-fx-background-color: transparent;");

        HBox kpiRow = new HBox(16);
        VBox card1 = createStatCard("Tổng số", "24", "#475569", "#1e293b", "#a5b4fc", "📅");
        VBox card2 = createStatCard("Sắp diễn ra", "3", "#475569", "#3b82f6", "#bfdbfe", "⏳");
        VBox card3 = createStatCard("Đang diễn ra", "1", "#475569", "#f59e0b", "#fde68a", "🔥");
        VBox card4 = createStatCard("Đã kết thúc", "18", "#475569", "#10b981", "#a7f3d0", "✅");

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

        ComboBox<String> cbStatus = format.formatSortBtn("Trạng thái", "Tất cả", "Sắp diễn ra", "Đang diễn ra", "Đã kết thúc", "Đã hủy");
        ComboBox<String> cbTime = format.formatSortBtn("Thời gian", "Tất cả", "Tháng này", "Quý này", "Năm nay");

        fixHover(cbStatus); fixHover(cbTime);
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        filterBar.getChildren().addAll(searchBox, cbStatus, cbTime, spacer);

        VBox eventTableContainer = format.formatTableContainer();
        Label tableTitle = format.formatLabel("DANH SÁCH SỰ KIỆN", FontWeight.BLACK, 14, "#1e293b");
        tableTitle.setPadding(new Insets(12, 16, 0, 16));
        eventTableContainer.getChildren().addAll(tableTitle, createEventTableHeader());

        VBox eventRows = new VBox(4);
        eventRows.getChildren().addAll(
                createEventRow("Tech Share #01: Git & GitHub", "28/06/2026", "08:00 - 11:30", "Phòng 202", "Kỹ thuật", "Sắp diễn ra", 80, false, "Chưa tham gia"),
                createEventRow("Networking Day", "26/06/2026", "19:00 - 21:00", "Online", "Nội dung", "Đang diễn ra", 128, true, "Đã tham gia"),
                createEventRow("Team Building", "12/06/2026", "07:00 - 17:00", "Biển Mỹ Khê", "Hậu cần", "Đã kết thúc", 100, false, "Đã tham gia")
        );

        ScrollPane eventScroll = new ScrollPane(eventRows);
        eventScroll.setPrefHeight(250);
        format.formatScrollbar(eventScroll, eventRows, 8);
        applySmoothScroll(eventScroll, eventRows);
        eventTableContainer.getChildren().add(eventScroll);

        VBox logTableContainer = format.formatTableContainer();
        Label logTitle = format.formatLabel("NHẬT KÝ HOẠT ĐỘNG SỰ KIỆN", FontWeight.BLACK, 14, "#1e293b");
        logTitle.setPadding(new Insets(12, 16, 0, 16));
        logTableContainer.getChildren().addAll(logTitle, createLogTableHeader());

        if (joinLogRows.getChildren().isEmpty()) {
            addLogEntry("26/06/2026 19:15", "Nguyễn Văn A", "Đã điểm danh: Có mặt (Networking Day)");
            addLogEntry("25/06/2026 14:30", "Trần Thu Hà", "Đã tham gia sự kiện: Tech Share #01");
            addLogEntry("24/06/2026 09:00", "Lê Đức Anh", "Đã điểm danh: Vắng có phép (Team Building)");
        }

        ScrollPane logScroll = new ScrollPane(joinLogRows);
        logScroll.setPrefHeight(200);
        format.formatScrollbar(logScroll, joinLogRows, 8);
        applySmoothScroll(logScroll, joinLogRows);
        logTableContainer.getChildren().add(logScroll);

        dashboardContent.getChildren().addAll(kpiRow, filterBar, eventTableContainer, logTableContainer);
        format.formatScrollbar(this, dashboardContent, 12);
        this.setContent(dashboardContent);
    }

    private void invokeShowModal(Node modal) {
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

    private HBox createEventTableHeader() {
        HBox header = new HBox(16);
        header.setPadding(new Insets(12, 16, 12, 16));
        header.setStyle("-fx-border-color: transparent transparent rgba(255,255,255,0.4) transparent; -fx-border-width: 1px;");

        Label l1 = format.formatLabel("SỰ KIỆN", FontWeight.BLACK, 10, "#94a3b8"); l1.setPrefWidth(220);
        Label l2 = format.formatLabel("THỜI GIAN", FontWeight.BLACK, 10, "#94a3b8"); l2.setPrefWidth(160);
        Label l3 = format.formatLabel("ĐỊA ĐIỂM", FontWeight.BLACK, 10, "#94a3b8"); l3.setPrefWidth(160);
        Label l4 = format.formatLabel("BỘ PHẬN", FontWeight.BLACK, 10, "#94a3b8"); l4.setPrefWidth(110);
        Label l5 = format.formatLabel("THAM GIA", FontWeight.BLACK, 10, "#94a3b8"); l5.setPrefWidth(120);
        Label l6 = format.formatLabel("THAO TÁC", FontWeight.BLACK, 10, "#94a3b8"); l6.setPrefWidth(90);

        header.getChildren().addAll(l1, l2, l3, l4, l5, l6);
        return header;
    }

    private HBox createEventRow(String title, String date, String time, String loc, String leader, String status, int count, boolean isAllClub, String userJoinState) {
        HBox row = new HBox(16);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand;");

        row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand; -fx-background-radius: 12px;"));
        row.setOnMouseExited(e -> row.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand;"));

        VBox titleLayout = new VBox(4);
        titleLayout.setPrefWidth(220);

        Label lblTitle = format.formatLabel(title, FontWeight.BOLD, 14, "#1e293b"); lblTitle.setWrapText(true);
        String stBg = status.equals("Sắp diễn ra") ? "rgba(59,130,246,0.12)" : status.equals("Đang diễn ra") ? "rgba(245,158,11,0.12)" : status.equals("Đã kết thúc") ? "rgba(16,185,129,0.12)" : "rgba(239,68,68,0.12)";
        String stText = status.equals("Sắp diễn ra") ? "#3b82f6" : status.equals("Đang diễn ra") ? "#f59e0b" : status.equals("Đã kết thúc") ? "#10b981" : "#ef4444";
        Label statusBadge = format.formatBadge(status, stBg, stText);
        statusBadge.setStyle(statusBadge.getStyle() + "-fx-font-size: 10px; -fx-padding: 2 6;");

        titleLayout.getChildren().addAll(lblTitle, statusBadge);

        Label lblTime = format.formatLabel(date + " | " + time, FontWeight.MEDIUM, 13, "#475569");
        lblTime.setPrefWidth(160); lblTime.setWrapText(true);

        Label lblLoc = format.formatLabel(loc, FontWeight.MEDIUM, 13, "#475569");
        lblLoc.setPrefWidth(160); lblLoc.setWrapText(true);

        Label lblLeader = format.formatLabel(leader, FontWeight.BOLD, 12, leader.equals("Chưa phân công") ? "#f59e0b" : "#7c4dff");
        lblLeader.setPrefWidth(110);

        Label lblCount = format.formatLabel(isAllClub ? "Toàn CLB" : count + " thành viên", FontWeight.BLACK, 13, "#334155");
        lblCount.setPrefWidth(120);

        HBox actionBox = new HBox(8); actionBox.setAlignment(Pos.CENTER_LEFT); actionBox.setPrefWidth(90);

        Button btnView = format.formatCircleBtn("👁️‍🗨️", "#448aff", "#7c4dff");
        btnView.setPrefSize(36, 36);
        btnView.setOnAction(e -> {
            StackPane detailModal = createDetailModal(title, date, time, loc, leader, status, count, isAllClub, "Thông tin chi tiết về sự kiện nội bộ.", userJoinState);
            invokeShowModal(detailModal);
        });

        actionBox.getChildren().add(btnView);
        row.getChildren().addAll(titleLayout, lblTime, lblLoc, lblLeader, lblCount, actionBox);
        return row;
    }

    private HBox createLogTableHeader() {
        HBox header = new HBox(16);
        header.setPadding(new Insets(12, 16, 12, 16));
        header.setStyle("-fx-border-color: transparent transparent rgba(255,255,255,0.4) transparent; -fx-border-width: 1px;");

        Label l1 = format.formatLabel("THỜI GIAN", FontWeight.BLACK, 10, "#94a3b8"); l1.setPrefWidth(140);
        Label l2 = format.formatLabel("THÀNH VIÊN", FontWeight.BLACK, 10, "#94a3b8"); l2.setPrefWidth(160);
        Label l3 = format.formatLabel("NỘI DUNG", FontWeight.BLACK, 10, "#94a3b8"); HBox.setHgrow(l3, Priority.ALWAYS); l3.setMaxWidth(Double.MAX_VALUE);

        header.getChildren().addAll(l1, l2, l3);
        return header;
    }

    private void addLogEntry(String timeStamp, String memberName, String content) {
        HBox row = new HBox(16);
        row.setPadding(new Insets(10, 16, 10, 16));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-border-color: transparent transparent rgba(0,0,0,0.05) transparent; -fx-border-width: 1px;");

        Label lblTime = format.formatLabel(timeStamp, FontWeight.MEDIUM, 12, "#64748b"); lblTime.setPrefWidth(140);
        Label lblMember = format.formatLabel(memberName, FontWeight.BOLD, 13, "#5020d8"); lblMember.setPrefWidth(160);
        Label lblLog = format.formatLabel(content, FontWeight.BOLD, 13, "#1e293b"); lblLog.setWrapText(true);
        HBox.setHgrow(lblLog, Priority.ALWAYS);

        row.getChildren().addAll(lblTime, lblMember, lblLog);
        joinLogRows.getChildren().add(0, row);
    }

    private StackPane createDetailModal(String title, String date, String time, String loc, String leader, String status, int participants, boolean isAllClub, String desc, String initialJoinState) {
        StackPane rootModalPane = new StackPane();
        VBox modalContent = new VBox(24);
        modalContent.setPrefWidth(820);
        modalContent.setMaxSize(820, Region.USE_PREF_SIZE);
        modalContent.setPadding(new Insets(32));
        modalContent.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        modalContent.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);

        Label lblTitle = format.formatLabel(title, FontWeight.BLACK, 24, "#1e293b");
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        String stBg = status.equals("Sắp diễn ra") ? "rgba(59,130,246,0.15)" : status.equals("Đang diễn ra") ? "rgba(245,158,11,0.15)" : status.equals("Đã kết thúc") ? "rgba(16,185,129,0.15)" : "rgba(239,68,68,0.15)";
        String stText = status.equals("Sắp diễn ra") ? "#3b82f6" : status.equals("Đang diễn ra") ? "#f59e0b" : status.equals("Đã kết thúc") ? "#10b981" : "#ef4444";
        Label statusBadge = format.formatBadge(status, stBg, stText);
        statusBadge.setStyle(statusBadge.getStyle() + "-fx-font-size: 14px; -fx-padding: 6 12;");
        header.getChildren().addAll(lblTitle, spacer, statusBadge);

        HBox bodyRow = new HBox(24);

        VBox infoBox = format.formatBoxCard();
        infoBox.setPrefWidth(370);
        infoBox.getChildren().add(format.formatLabel("THÔNG TIN SỰ KIỆN", FontWeight.BLACK, 12, "#94a3b8"));

        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(24); infoGrid.setVgap(16);
        infoGrid.add(new VBox(4, format.formatLabel("Ngày tổ chức", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(date, FontWeight.BLACK, 15, "#1e293b")), 0, 0);
        infoGrid.add(new VBox(4, format.formatLabel("Thời gian", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(time, FontWeight.BLACK, 15, "#1e293b")), 1, 0);
        infoGrid.add(new VBox(4, format.formatLabel("Địa điểm", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(loc, FontWeight.BLACK, 15, "#1e293b")), 0, 1, 2, 1);

        Label lblLeader = format.formatLabel(leader, FontWeight.BLACK, 15, leader.equals("Chưa phân công") ? "#f59e0b" : "#3b82f6");
        infoGrid.add(new VBox(4, format.formatLabel("Bộ phận phụ trách", FontWeight.BOLD, 12, "#94a3b8"), lblLeader), 0, 2, 2, 1);

        Label lblDesc = format.formatLabel(desc, FontWeight.MEDIUM, 14, "#475569");
        lblDesc.setWrapText(true);
        infoGrid.add(new VBox(4, format.formatLabel("Mô tả", FontWeight.BOLD, 12, "#94a3b8"), lblDesc), 0, 3, 2, 1);
        infoBox.getChildren().add(infoGrid);

        VBox memberBox = format.formatBoxCard();
        memberBox.setPrefWidth(370);
        memberBox.getChildren().add(format.formatLabel(isAllClub ? "THÀNH VIÊN THAM GIA" : "THÀNH VIÊN THAM GIA (" + participants + ")", FontWeight.BLACK, 12, "#94a3b8"));

        VBox studentJoinRows = new VBox(4);
        HBox userRealtimeRow = createSimpleMemberRow("Bạn", "Thành viên");
        userRealtimeRow.setStyle("-fx-background-color: rgba(16,185,129,0.1); -fx-background-radius: 8px;");

        if (isAllClub) {
            VBox noticeBox = new VBox();
            noticeBox.setAlignment(Pos.CENTER);
            noticeBox.setPrefHeight(250);
            Label notice = format.formatLabel("Toàn bộ CLB đều phải tham gia.", FontWeight.MEDIUM, 14, "#64748b");
            notice.setWrapText(true);
            noticeBox.getChildren().add(notice);
            memberBox.getChildren().add(noticeBox);
        } else {
            if (participants > 0) {
                studentJoinRows.getChildren().addAll(
                        createSimpleMemberRow("Nguyễn Minh Quân", "Quản lý"),
                        createSimpleMemberRow("Trần Văn B", "Thành viên")
                );
            }
            if (initialJoinState.equals("Đã tham gia")) {
                studentJoinRows.getChildren().add(0, userRealtimeRow);
            }

            ScrollPane memberScroll = new ScrollPane(studentJoinRows);
            memberScroll.setPrefHeight(250);
            format.formatScrollbar(memberScroll, studentJoinRows, 8);
            applySmoothScroll(memberScroll, studentJoinRows);
            memberBox.getChildren().add(memberScroll);
        }

        bodyRow.getChildren().addAll(infoBox, memberBox);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);
        Button btnClose = getModalActionBtn("Đóng", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnClose.setOnAction(e -> invokeCloseModal());

        if (!status.equals("Đã kết thúc") && !status.equals("Đã hủy") && !isAllClub) {
            Button btnJoin = getModalActionBtn("Tham gia", "#10b981", "white", "rgba(16,185,129,0.4)");
            Button btnCancelJoin = getModalActionBtn("Hủy tham gia", "rgba(239,68,68,0.1)", "#ef4444", "rgba(0,0,0,0.1)");

            if (initialJoinState.equals("Đã tham gia")) {
                btnJoin.setVisible(false); btnJoin.setManaged(false);
            } else {
                btnCancelJoin.setVisible(false); btnCancelJoin.setManaged(false);
            }

            btnJoin.setOnAction(e -> {
                btnJoin.setVisible(false); btnJoin.setManaged(false);
                btnCancelJoin.setVisible(true); btnCancelJoin.setManaged(true);
                if (!studentJoinRows.getChildren().contains(userRealtimeRow)) {
                    studentJoinRows.getChildren().add(0, userRealtimeRow);
                }
                invokeToast("Đã đăng ký tham gia sự kiện!");
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                addLogEntry(timestamp, "Bạn", "Đã tham gia sự kiện: " + title);
            });

            btnCancelJoin.setOnAction(e -> {
                btnCancelJoin.setVisible(false); btnCancelJoin.setManaged(false);
                btnJoin.setVisible(true); btnJoin.setManaged(true);
                studentJoinRows.getChildren().remove(userRealtimeRow);
                invokeToast("Đã hủy tham gia sự kiện.");
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                addLogEntry(timestamp, "Bạn", "Đã hủy tham gia sự kiện: " + title);
            });

            actions.getChildren().addAll(btnClose, btnCancelJoin, btnJoin);
        } else {
            actions.getChildren().add(btnClose);
        }

        modalContent.getChildren().addAll(header, bodyRow, actions);
        rootModalPane.getChildren().add(modalContent);

        return rootModalPane;
    }

    private HBox createSimpleMemberRow(String name, String role) {
        HBox row = new HBox(12);
        row.setPadding(new Insets(8));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-border-color: transparent transparent rgba(0,0,0,0.05) transparent; -fx-border-width: 1px;");

        Label lblName = format.formatLabel("👤 " + name, FontWeight.BOLD, 13, "#1e293b");
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        Label lblRole = format.formatLabel(role, FontWeight.MEDIUM, 11, "#94a3b8");

        row.getChildren().addAll(lblName, spacer, lblRole);
        return row;
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