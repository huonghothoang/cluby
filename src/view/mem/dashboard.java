package view.mem;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import view.format;

public class dashboard extends ScrollPane {

    private String userDept = "Ban Truyền thông";

    public dashboard() {
        VBox mainContent = new VBox(24);
        mainContent.setPadding(new Insets(32));
        mainContent.setStyle("-fx-background-color: transparent;");

        setFixedWidth(mainContent, 1000);

        HBox kpiRow = new HBox(16);
        setFixedWidth(kpiRow, 1000);

        VBox k1 = createStatCard("Nhiệm vụ đang làm", "3", "#475569", "#1e293b", "#bfdbfe", "📝");
        VBox k2 = createStatCard("Việc đã hoàn thành", "12", "#475569", "#10b981", "#a7f3d0", "✅");
        VBox k3 = createStatCard("Sự kiện tham gia", "8", "#475569", "#f59e0b", "#fde68a", "📅");
        VBox k4 = createStatCard("Tỷ lệ điểm danh", "95%", "#475569", "#7c4dff", "#e9d5ff", "🎯");

        kpiRow.getChildren().addAll(k1, k2, k3, k4);

        HBox middleRow = new HBox(24);
        setFixedWidth(middleRow, 1000);

        VBox tasksBox = buildTasksBox();
        VBox eventsBox = buildEventsBox();

        if (!userDept.equals("Chưa phân công")) {
            middleRow.getChildren().addAll(tasksBox, eventsBox);
        } else {
            middleRow.getChildren().add(eventsBox);
        }

        VBox notifsBox = buildNotifsBox();

        mainContent.getChildren().addAll(kpiRow, middleRow, notifsBox);
        format.formatScrollbar(this, mainContent, 12);
        this.setContent(mainContent);
    }

    private void setFixedWidth(Region r, double w) {
        r.setPrefWidth(w);
        r.setMinWidth(w);
        r.setMaxWidth(w);
        r.setMinHeight(Region.USE_PREF_SIZE);
        if (r instanceof Label) {
            ((Label) r).setWrapText(true);
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

    private VBox createStatCard(String title, String value, String titleColor, String valColor, String iconBg, String iconEmoji) {
        VBox box = format.formatBoxCard();
        setFixedWidth(box, 238);
        box.setPadding(new Insets(20));

        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);

        VBox textCol = new VBox(4);
        textCol.getChildren().addAll(
                format.formatLabel(title, FontWeight.BOLD, 11, titleColor),
                format.formatLabel(value, FontWeight.BLACK, 24, valColor)
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        StackPane iconContainer = new StackPane();
        iconContainer.getChildren().addAll(
                new Circle(20, Color.web(iconBg)),
                format.formatLabel(iconEmoji, FontWeight.NORMAL, 18, "#000000")
        );

        content.getChildren().addAll(textCol, spacer, iconContainer);
        box.getChildren().add(content);
        return box;
    }

    private VBox buildTasksBox() {
        VBox box = format.formatBoxCard();
        setFixedWidth(box, 600);

        box.getChildren().add(format.formatLabel("🔥 VIỆC CẦN LÀM GẤP", FontWeight.BLACK, 16, "#ef4444"));

        VBox tasksList = new VBox(12);
        boolean hasTasks = true;

        if (hasTasks) {
            tasksList.getChildren().addAll(
                    createTaskCard("Vẽ Background", "Thiết kế Poster Workshop", "28/06/2026", "Cao", "Đang hoàn thành", true),
                    createTaskCard("Lên Typography", "Thiết kế Poster Workshop", "28/06/2026", "Trung bình", "Chưa bắt đầu", false),
                    createTaskCard("Chỉnh sửa Video Teaser", "Team Building ĐN", "30/06/2026", "Cao", "Đã hoàn thành", false),
                    createTaskCard("Nộp bài Đồ án Cơ sở 1", "Việc chung CLB", "02/07/2026", "Cao", "Không hoàn thành", false)
            );
        } else {
            HBox emptyState = new HBox(format.formatLabel("Hiện không có nhiệm vụ nào cần làm.", FontWeight.MEDIUM, 14, "#94a3b8"));
            emptyState.setAlignment(Pos.CENTER);
            emptyState.setPadding(new Insets(32, 0, 32, 0));
            tasksList.getChildren().add(emptyState);
        }

        ScrollPane tasksScroll = new ScrollPane(tasksList);
        tasksScroll.setPrefHeight(320);
        format.formatScrollbar(tasksScroll, tasksList, 16);
        applySmoothScroll(tasksScroll, tasksList);

        box.getChildren().add(tasksScroll);
        return box;
    }

    private VBox createTaskCard(String taskName, String parentWork, String deadline, String priority, String status, boolean isOverdue) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        setFixedWidth(card, 530);
        card.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 24px; -fx-border-color: rgba(255,255,255,0.9); -fx-border-width: 2px; -fx-border-radius: 24px; -fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.08), 10, 0, 0, 4);");

        HBox topRow = new HBox(12);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label lblTitle = format.formatLabel(taskName, FontWeight.BLACK, 16, "#1e293b");
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox badgeBox = new HBox(8);
        badgeBox.setAlignment(Pos.CENTER_RIGHT);

        if (isOverdue && !status.equalsIgnoreCase("Đã hoàn thành") && !status.equalsIgnoreCase("Không hoàn thành")) {
            Label overdueBadge = format.formatBadge("⚠ Quá hạn", "rgba(239,68,68,0.15)", "#ef4444");
            overdueBadge.setStyle(overdueBadge.getStyle() + "-fx-font-size: 11px; -fx-padding: 4 10;");
            badgeBox.getChildren().add(overdueBadge);
        }

        String stBg = status.equalsIgnoreCase("Chưa bắt đầu") ? "rgba(100,116,139,0.15)" :
                status.equalsIgnoreCase("Đang hoàn thành") ? "rgba(59,130,246,0.15)" :
                status.equalsIgnoreCase("Đã hoàn thành") ? "rgba(16,185,129,0.15)" :
                "rgba(239,68,68,0.15)";

        String stText = status.equalsIgnoreCase("Chưa bắt đầu") ? "#64748b" :
                status.equalsIgnoreCase("Đang hoàn thành") ? "#3b82f6" :
                status.equalsIgnoreCase("Đã hoàn thành") ? "#10b981" :
                "#ef4444";

        Label statusBadge = format.formatBadge(status, stBg, stText);
        statusBadge.setStyle(statusBadge.getStyle() + "-fx-font-size: 11px; -fx-padding: 4 10;");
        badgeBox.getChildren().add(statusBadge);

        topRow.getChildren().addAll(lblTitle, spacer, badgeBox);

        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(20); infoGrid.setVgap(8);

        infoGrid.add(format.formatLabel("📁 Thuộc: " + parentWork, FontWeight.BOLD, 12, "#7c4dff"), 0, 0, 2, 1);
        infoGrid.add(format.formatLabel("⏳ Hạn chót: " + deadline, FontWeight.BOLD, 12, isOverdue ? "#ef4444" : "#475569"), 0, 1);

        String prioBg = priority.equals("Cao") ? "rgba(239,68,68,0.15)" : priority.equals("Trung bình") ? "rgba(245,158,11,0.15)" : "rgba(16,185,129,0.15)";
        String prioText = priority.equals("Cao") ? "#ef4444" : priority.equals("Trung bình") ? "#f59e0b" : "#10b981";
        Label prioBadge = format.formatBadge("Ưu tiên: " + priority, prioBg, prioText);
        prioBadge.setStyle(prioBadge.getStyle() + "-fx-font-size: 11px; -fx-padding: 2 8;");
        infoGrid.add(prioBadge, 1, 1);

        HBox actionsRow = new HBox();
        actionsRow.setAlignment(Pos.CENTER_RIGHT);

        if (!status.equalsIgnoreCase("Đã hoàn thành") && !status.equalsIgnoreCase("Không hoàn thành")) {
            Button btnSubmit = new Button("Nộp sản phẩm");
            btnSubmit.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));
            btnSubmit.setTextFill(Color.WHITE);
            btnSubmit.setStyle("-fx-background-color: #5020d8; -fx-background-radius: 20px; -fx-padding: 8 20; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(80,32,216,0.4), 10, 0, 0, 4);");
            btnSubmit.setOnMouseEntered(e -> { btnSubmit.setScaleX(1.05); btnSubmit.setScaleY(1.05); });
            btnSubmit.setOnMouseExited(e -> { btnSubmit.setScaleX(1.0); btnSubmit.setScaleY(1.0); });

            btnSubmit.setOnAction(e -> {
                StackPane submitModal = createSubmitTaskModal(taskName, parentWork, deadline);
                frame.getInstance().showCustomModal(submitModal);
            });
            actionsRow.getChildren().add(btnSubmit);
        } else {
            Label lblLocked = format.formatLabel(status.equalsIgnoreCase("Đã hoàn thành") ? "🎉 Đã nộp sản phẩm" : "🔒 Đã khóa nghiệm thu", FontWeight.BOLD, 12, status.equalsIgnoreCase("Đã hoàn thành") ? "#10b981" : "#ef4444");
            actionsRow.getChildren().add(lblLocked);
        }

        card.getChildren().addAll(topRow, infoGrid, actionsRow);
        return card;
    }

    private VBox buildEventsBox() {
        VBox box = format.formatBoxCard();
        setFixedWidth(box, 376);

        box.getChildren().add(format.formatLabel("⏰ SỰ KIỆN SẮP TỚI", FontWeight.BLACK, 16, "#1e293b"));

        VBox eventsList = new VBox(16);
        eventsList.getChildren().addAll(
                createSimpleEventCard("WORKSHOP GITHUB & GITFLOW", "28/06/2026", "08:00", "Hội trường A"),
                createSimpleEventCard("TEAM BUILDING ĐÀ NẴNG", "10/07/2026", "07:00", "Biển Mỹ Khê"),
                createSimpleEventCard("TALKSHOW ĐỊNH HƯỚNG", "20/07/2026", "18:00", "Phòng V701")
        );

        ScrollPane eventsScroll = new ScrollPane(eventsList);
        eventsScroll.setPrefHeight(320);
        format.formatScrollbar(eventsScroll, eventsList, 16);
        applySmoothScroll(eventsScroll, eventsList);

        box.getChildren().add(eventsScroll);
        return box;
    }

    private VBox createSimpleEventCard(String title, String date, String time, String loc) {
        VBox box = new VBox(12);
        box.setPadding(new Insets(16));
        format.formatGlass(box, 24, 0.6);
        setFixedWidth(box, 305);

        Label lblTitle = format.formatLabel(title, FontWeight.BOLD, 16, "#1e293b");
        lblTitle.setWrapText(true);

        HBox statsRow = new HBox(20);
        statsRow.getChildren().addAll(
                createMiniStat("Ngày", date, "#334155"),
                createMiniStat("Giờ", time, "#334155"),
                createMiniStat("Địa điểm", loc, "#7c4dff")
        );

        box.getChildren().addAll(lblTitle, statsRow);
        return box;
    }

    private VBox createMiniStat(String title, String val, String valColor) {
        VBox box = new VBox(2);
        box.setMinHeight(Region.USE_PREF_SIZE);
        Label lblV = format.formatLabel(val, FontWeight.BOLD, 11, valColor);
        lblV.setWrapText(true);
        box.getChildren().addAll(format.formatLabel(title.toUpperCase(), FontWeight.BOLD, 9, "#94a3b8"), lblV);
        return box;
    }

    private VBox buildNotifsBox() {
        VBox box = format.formatBoxCard();
        setFixedWidth(box, 1000);

        Label title = format.formatLabel("📢 Thông báo hệ thống", FontWeight.BLACK, 18, "#1e293b");

        VBox notifList = new VBox(12);
        notifList.getChildren().addAll(
                createNotifRow("Ban Truyền thông vừa tạo sự kiện mới trên cổng quản lý.", "#6366f1"),
                createNotifRow("Tuyển thành viên K28 đã chính thức mở đơn ứng tuyển trực tuyến.", "#ec4899"),
                createNotifRow("Biên bản cuộc họp toàn thể tháng 6 đã được ban thư ký tải lên.", "#f59e0b"),
                createNotifRow("Lịch tổng duyệt sự kiện Workshop được dời sang chiều thứ Bảy.", "#ef4444"),
                createNotifRow("Hệ thống chuẩn bị bảo trì vào 00:00 đêm nay.", "#3b82f6")
        );

        ScrollPane notifScroll = new ScrollPane(notifList);
        notifScroll.setPrefHeight(250);
        format.formatScrollbar(notifScroll, notifList, 16);
        applySmoothScroll(notifScroll, notifList);

        box.getChildren().addAll(title, notifScroll);
        return box;
    }

    private HBox createNotifRow(String text, String dotColor) {
        HBox box = new HBox(12);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(16));
        format.formatGlass(box, 16, 0.6);
        setFixedWidth(box, 930);
        box.setMinHeight(Region.USE_PREF_SIZE);

        Label content = format.formatLabel(text, FontWeight.NORMAL, 13, "#334155");
        content.setWrapText(true);
        HBox.setHgrow(content, Priority.ALWAYS);

        box.getChildren().addAll(new Circle(4, Color.web(dotColor)), content);
        return box;
    }

    private StackPane createSubmitTaskModal(String taskName, String parentWork, String deadline) {
        StackPane rootModalPane = new StackPane();
        VBox box = new VBox(24);

        box.setPrefWidth(500);
        box.setMaxSize(500, Region.USE_PREF_SIZE);
        box.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 32px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.25)));

        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);

        StackPane iconBg = new StackPane();
        iconBg.setPrefSize(48, 48);
        iconBg.setStyle("-fx-background-color: rgba(124,77,255,0.15); -fx-background-radius: 16px;");
        Label headerIcon = format.formatLabel("🚀", FontWeight.NORMAL, 24, "#000000");
        iconBg.getChildren().add(headerIcon);

        VBox titleBox = new VBox(4);
        titleBox.getChildren().addAll(
                format.formatLabel("Nộp Sản Phẩm", FontWeight.BLACK, 22, "#1e293b"),
                format.formatLabel("Gửi kết quả nghiệm thu cho Trưởng ban", FontWeight.MEDIUM, 13, "#64748b")
        );
        header.getChildren().addAll(iconBg, titleBox);

        VBox infoBox = new VBox(16);
        infoBox.setPadding(new Insets(20));
        infoBox.setStyle("-fx-background-color: linear-gradient(to bottom right, #f8fafc, #f1f5f9); -fx-background-radius: 20px; -fx-border-color: #e2e8f0; -fx-border-width: 1px; -fx-border-radius: 20px;");

        GridPane grid = new GridPane();
        grid.setHgap(24); grid.setVgap(16);

        Label lblTask = format.formatLabel(taskName, FontWeight.BLACK, 15, "#5020d8");
        lblTask.setWrapText(true);
        grid.add(new VBox(6, format.formatLabel("NHIỆM VỤ ĐƯỢC GIAO", FontWeight.BLACK, 11, "#94a3b8"), lblTask), 0, 0, 2, 1);

        grid.add(new VBox(6, format.formatLabel("THUỘC CÔNG VIỆC", FontWeight.BLACK, 11, "#94a3b8"), format.formatLabel(parentWork, FontWeight.BOLD, 13, "#475569")), 0, 1);
        grid.add(new VBox(6, format.formatLabel("HẠN CHÓT", FontWeight.BLACK, 11, "#94a3b8"), format.formatLabel(deadline, FontWeight.BOLD, 13, "#ef4444")), 1, 1);

        infoBox.getChildren().add(grid);

        VBox fields = new VBox(8);
        fields.getChildren().add(format.formatLabel("Đường dẫn đính kèm (Drive, Docs, Figma...) *", FontWeight.BOLD, 13, "#1e293b"));

        TextField fLink = format.formatTextField("Dán link sản phẩm của bạn vào đây...");
        fLink.setStyle(fLink.getStyle() + "-fx-background-color: white; -fx-border-color: #cbd5e1; -fx-border-width: 2px; -fx-border-radius: 16px; -fx-background-radius: 16px; -fx-padding: 12 16;");
        fLink.setOnMouseEntered(e -> fLink.setStyle(fLink.getStyle().replace("-fx-border-color: #cbd5e1;", "-fx-border-color: #7c4dff;")));
        fLink.setOnMouseExited(e -> {
            if (!fLink.isFocused()) fLink.setStyle(fLink.getStyle().replace("-fx-border-color: #7c4dff;", "-fx-border-color: #cbd5e1;"));
        });

        fields.getChildren().add(fLink);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);
        actions.setPadding(new Insets(8, 0, 0, 0));

        Button btnCancel = getShadowBtn("Quay lại", "", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> frame.getInstance().closeOverlayModal());

        Button btnConfirm = getShadowBtn("Gửi Nghiệm Thu", "✨", "#5020d8", "white", "rgba(80,32,216,0.4)");
        btnConfirm.setOnAction(e -> {
            if (fLink.getText().trim().isEmpty()) {
                frame.getInstance().triggerToast("❌ Vui lòng dán link sản phẩm trước khi nộp!");
                return;
            }
            frame.getInstance().closeOverlayModal();
            frame.getInstance().triggerToast("Đã nộp sản phẩm thành công! Chờ Trưởng ban nghiệm thu.");
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);

        box.getChildren().addAll(header, infoBox, fields, actions);
        rootModalPane.getChildren().add(box);
        return rootModalPane;
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
}