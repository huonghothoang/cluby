package view.mem;

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
import view.format;

public class work extends ScrollPane {

    private VBox dashboardContent;
    private static final VBox taskRowsContainer = new VBox(4);

    public work() {
        setPickOnBounds(false);

        dashboardContent = new VBox(32);
        dashboardContent.setPadding(new Insets(32));
        dashboardContent.setStyle("-fx-background-color: transparent;");

        HBox kpiRow = new HBox(16);
        kpiRow.getChildren().addAll(
                format.formatKPICard("Công việc tham gia", "4", "#64748b", "#1e293b"),
                format.formatKPICard("🔵 Đang thực hiện", "3", "#3b82f6", "#1e293b"),
                format.formatKPICard("🟢 Đã hoàn thành", "1", "#10b981", "#1e293b"),
                format.formatKPICard("🔴 Quá hạn", "0", "#ef4444", "#ef4444")
        );
        for (Node node : kpiRow.getChildren()) { HBox.setHgrow(node, Priority.ALWAYS); }

        HBox filterBar = new HBox(16);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(12, 24, 12, 24));
        format.formatGlass(filterBar, 40, 0.4);

        HBox searchBox = new HBox(8);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = format.formatTextField("Tìm tên công việc...");
        Button btnSearch = format.formatFindBtn();
        searchBox.getChildren().addAll(searchField, btnSearch);

        ComboBox<String> cbTimeSort = format.formatSortBtn("Sắp xếp thời gian", "Mới nhất", "Cũ nhất");
        ComboBox<String> cbStatusSort = format.formatSortBtn("Lọc trạng thái", "Tất cả", "Chưa bắt đầu", "Đang thực hiện", "Hoàn thành");

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        filterBar.getChildren().addAll(searchBox, cbTimeSort, cbStatusSort, spacer);

        VBox workTableContainer = format.formatTableContainer();
        Label tableTitle = format.formatLabel("📋 DANH SÁCH HOÀN THÀNH CÔNG VIỆC ĐƯỢC GIAO", FontWeight.BLACK, 14, "#1e293b");
        tableTitle.setPadding(new Insets(12, 16, 0, 16));
        workTableContainer.getChildren().addAll(tableTitle, createWorkTableHeader());

        VBox workRows = new VBox(4);
        workRows.getChildren().addAll(
                createWorkRow("Thiết kế Poster Workshop", "Workshop GitHub", "28/06/2026", "Đang thực hiện", "Cao", false, 0.44),
                createWorkRow("Chuẩn bị bài đăng Fanpage", "Workshop GitHub", "28/06/2026", "Đang thực hiện", "Trung bình", false, 0.20),
                createWorkRow("Chỉnh sửa Video Teaser", "Team Building ĐN", "30/06/2026", "Đang thực hiện", "Cao", false, 0.50),
                createWorkRow("Chụp ảnh Profile Ban", "Không thuộc sự kiện", "20/06/2026", "Hoàn thành", "Thấp", false, 1.0)
        );

        ScrollPane workScroll = new ScrollPane(workRows);
        workScroll.setPrefHeight(350);
        format.formatScrollbar(workScroll, workRows, 8);
        applySmoothScroll(workScroll, workRows);
        workTableContainer.getChildren().add(workScroll);

        dashboardContent.getChildren().addAll(kpiRow, filterBar, workTableContainer);
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

    private HBox createWorkTableHeader() {
        HBox header = new HBox(16);
        header.setPadding(new Insets(12, 16, 12, 16));
        header.setStyle("-fx-border-color: transparent transparent rgba(255,255,255,0.4) transparent; -fx-border-width: 1px;");

        Label l1 = format.formatLabel("TÊN CÔNG VIỆC", FontWeight.BLACK, 10, "#94a3b8"); l1.setPrefWidth(220);
        Label l2 = format.formatLabel("THUỘC SỰ KIỆN", FontWeight.BLACK, 10, "#94a3b8"); l2.setPrefWidth(150);
        Label l3 = format.formatLabel("HẠN CHÓT", FontWeight.BLACK, 10, "#94a3b8"); l3.setPrefWidth(110);
        Label l4 = format.formatLabel("MỨC ƯU TIÊN", FontWeight.BLACK, 10, "#94a3b8"); l4.setPrefWidth(110);
        Label l5 = format.formatLabel("TIẾN ĐỘ THỰC TẾ", FontWeight.BLACK, 10, "#94a3b8"); l5.setPrefWidth(180);
        Label l6 = format.formatLabel("XEM CHI TIẾT", FontWeight.BLACK, 10, "#94a3b8"); l6.setPrefWidth(80);

        header.getChildren().addAll(l1, l2, l3, l4, l5, l6);
        return header;
    }

    private HBox createWorkRow(String title, String event, String deadline, String status, String priority, boolean isOverdue, double progress) {
        HBox row = new HBox(16);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand;");
        row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand; -fx-background-radius: 12px;"));
        row.setOnMouseExited(e -> row.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand;"));

        VBox titleLayout = new VBox(4);
        titleLayout.setPrefWidth(220);
        Label lblTitle = format.formatLabel(title, FontWeight.BOLD, 14, "#1e293b"); lblTitle.setWrapText(true);

        String stBg = status.equals("Chưa bắt đầu") ? "rgba(100,116,139,0.12)" : status.equals("Đang thực hiện") ? "rgba(59,130,246,0.12)" : "rgba(16,185,129,0.12)";
        String stText = status.equals("Chưa bắt đầu") ? "#64748b" : status.equals("Đang thực hiện") ? "#3b82f6" : "#10b981";
        Label statusBadge = format.formatBadge(status, stBg, stText);
        statusBadge.setStyle(statusBadge.getStyle() + "-fx-font-size: 10px; -fx-padding: 2 6;");

        HBox badges = new HBox(6, statusBadge);
        if (isOverdue && !status.equals("Hoàn thành")) {
            Label overdueBadge = format.formatBadge("⚠ Quá hạn", "rgba(239,68,68,0.12)", "#ef4444");
            overdueBadge.setStyle(overdueBadge.getStyle() + "-fx-font-size: 10px; -fx-padding: 2 6;");
            badges.getChildren().add(0, overdueBadge);
        }
        titleLayout.getChildren().addAll(lblTitle, badges);

        String eventLabel = event.equals("Không thuộc sự kiện") ? "🏢 Việc chung" : "📅 " + event;
        Label lblEvent = format.formatLabel(eventLabel, FontWeight.BOLD, 12, event.equals("Không thuộc sự kiện") ? "#94a3b8" : "#7c4dff");
        lblEvent.setPrefWidth(150); lblEvent.setWrapText(true);

        Label lblDeadline = format.formatLabel(deadline, FontWeight.BOLD, 13, isOverdue ? "#ef4444" : "#475569");
        lblDeadline.setPrefWidth(110);

        Label lblPrio = format.formatLabel(priority, FontWeight.BLACK, 13, priority.equals("Cao") ? "#ef4444" : priority.equals("Trung bình") ? "#f59e0b" : "#10b981");
        lblPrio.setPrefWidth(110);

        HBox progressRow = new HBox(8);
        progressRow.setAlignment(Pos.CENTER_LEFT);
        progressRow.setPrefWidth(180);

        Label lblProgText = format.formatLabel((int)(progress * 100) + "%", FontWeight.BLACK, 11, "#7c4dff");
        lblProgText.setMinWidth(30);

        StackPane track = new StackPane();
        HBox.setHgrow(track, Priority.ALWAYS);
        track.setPrefHeight(6);
        track.setStyle("-fx-background-color: rgba(124,77,255,0.12); -fx-background-radius: 6px;");
        track.setAlignment(Pos.CENTER_LEFT);

        StackPane bar = new StackPane();
        bar.setPrefHeight(6);
        bar.setStyle("-fx-background-color: linear-gradient(to right, #448aff, #7c4dff); -fx-background-radius: 6px;");
        bar.maxWidthProperty().bind(track.widthProperty().multiply(progress));
        track.getChildren().add(bar);
        progressRow.getChildren().addAll(lblProgText, track);

        HBox actionBox = new HBox(8); actionBox.setAlignment(Pos.CENTER_LEFT); actionBox.setPrefWidth(80);
        Button btnView = format.formatCircleBtn("👁️‍🗨️", "#448aff", "#7c4dff");
        btnView.setOnAction(e -> {
            StackPane detailModal = createWorkProcessModal(title, event, deadline, status, progress);
            view.mem.frame.getInstance().showCustomModal(detailModal);
        });
        actionBox.getChildren().add(btnView);

        row.getChildren().addAll(titleLayout, lblEvent, lblDeadline, lblPrio, progressRow, actionBox);
        return row;
    }

    private StackPane createWorkProcessModal(String workTitle, String eventName, String deadline, String status, double progress) {
        StackPane rootModalPane = new StackPane();
        VBox modalContent = new VBox(20);
        modalContent.setPrefWidth(900);
        modalContent.setMaxSize(900, Region.USE_PREF_SIZE);
        modalContent.setPadding(new Insets(32));
        modalContent.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        modalContent.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        VBox titleBox = new VBox(4);
        titleBox.getChildren().addAll(
                format.formatLabel("Nhiệm vụ: " + workTitle, FontWeight.BLACK, 24, "#1e293b"),
                format.formatLabel("📅 " + eventName + "   |   ⏳ Hạn chót: " + deadline, FontWeight.BOLD, 13, "#64748b")
        );
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnGuide = getShadowBtn("Hướng dẫn tính %", "💡", "rgba(245,158,11,0.15)", "#f59e0b", "rgba(0,0,0,0.05)");
        btnGuide.setOnAction(e -> {
            VBox guideModal = createGuideModal(rootModalPane, modalContent);
            rootModalPane.getChildren().setAll(guideModal);
        });
        header.getChildren().addAll(titleBox, spacer, btnGuide);

        HBox progressRow = new HBox(16);
        progressRow.setAlignment(Pos.CENTER_LEFT);

        Label lblProgText = format.formatLabel("Tổng tiến độ: " + (int)(progress * 100) + "%", FontWeight.BLACK, 12, "#7c4dff");
        lblProgText.setMinWidth(Region.USE_PREF_SIZE);

        StackPane track = new StackPane();
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

        VBox listContainer = format.formatBoxCard();
        listContainer.setPadding(new Insets(16, 8, 16, 16));
        listContainer.getChildren().add(format.formatLabel("DANH SÁCH NHIỆM VỤ CỦA BẠN", FontWeight.BLACK, 12, "#94a3b8"));

        HBox listHeader = new HBox(12);
        listHeader.setPadding(new Insets(12, 12, 12, 12));
        listHeader.setStyle("-fx-border-color: transparent transparent rgba(0,0,0,0.1) transparent; -fx-border-width: 1px;");

        Label l1 = format.formatLabel("AVATAR", FontWeight.BLACK, 10, "#94a3b8"); l1.setPrefWidth(50);
        Label l2 = format.formatLabel("PHỤ TRÁCH", FontWeight.BLACK, 10, "#94a3b8"); l2.setPrefWidth(120);
        Label l3 = format.formatLabel("NHIỆM VỤ CHI TIẾT", FontWeight.BLACK, 10, "#94a3b8"); l3.setPrefWidth(180);
        Label l4 = format.formatLabel("ƯU TIÊN", FontWeight.BLACK, 10, "#94a3b8"); l4.setPrefWidth(80);
        Label l5 = format.formatLabel("TRẠNG THÁI", FontWeight.BLACK, 10, "#94a3b8"); l5.setPrefWidth(120);
        Label l6 = format.formatLabel("THAO TÁC NỘP SẢN PHẨM", FontWeight.BLACK, 10, "#94a3b8"); l6.setPrefWidth(150);

        listHeader.getChildren().addAll(l1, l2, l3, l4, l5, l6);
        listContainer.getChildren().add(listHeader);

        taskRowsContainer.getChildren().clear();
        taskRowsContainer.getChildren().addAll(
                createTaskRow(rootModalPane, modalContent, "trish.jpeg", "Bạn", "Vẽ Background", "Cao", "Đang hoàn thành", "Link Drive", true),
                createTaskRow(rootModalPane, modalContent, "trish.jpeg", "Bạn", "Lên Typography", "Trung bình", "Chưa bắt đầu", "Đang cập nhật", false)
        );

        ScrollPane scrollList = new ScrollPane(taskRowsContainer);
        scrollList.setMinHeight(220); scrollList.setMaxHeight(220);
        format.formatScrollbar(scrollList, taskRowsContainer, 8);
        applySmoothScroll(scrollList, taskRowsContainer);
        listContainer.getChildren().add(scrollList);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(16, 0, 0, 0));

        Button btnClose = getShadowBtn("Đóng", "", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnClose.setOnAction(e -> view.mem.frame.getInstance().closeOverlayModal());
        actions.getChildren().add(btnClose);

        modalContent.getChildren().addAll(header, progressRow, listContainer, actions);
        rootModalPane.getChildren().add(modalContent);
        return rootModalPane;
    }

    private HBox createTaskRow(StackPane rootModalPane, VBox previousView, String avatarUrl, String memberName, String taskName, String priority, String status, String link, boolean isAccepted) {
        HBox row = new HBox(12);
        row.setPadding(new Insets(12));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-border-color: transparent transparent rgba(0,0,0,0.05) transparent; -fx-border-width: 1px;");

        ImageView avatar = new ImageView(new Image(avatarUrl));
        avatar.setFitWidth(36); avatar.setFitHeight(36); avatar.setClip(new Circle(18, 18, 18));
        HBox avatarBox = new HBox(avatar); avatarBox.setPrefWidth(50);

        Label lblName = format.formatLabel(memberName, FontWeight.BOLD, 13, "#1e293b"); lblName.setPrefWidth(120);
        Label lblTask = format.formatLabel(taskName, FontWeight.BOLD, 13, "#475569"); lblTask.setPrefWidth(180);

        String prioBg = priority.equals("Cao") ? "rgba(239,68,68,0.15)" : priority.equals("Trung bình") ? "rgba(245,158,11,0.15)" : "rgba(16,185,129,0.15)";
        String prioText = priority.equals("Cao") ? "#ef4444" : priority.equals("Trung bình") ? "#f59e0b" : "#10b981";
        HBox prioBox = new HBox(format.formatBadge(priority, prioBg, prioText)); prioBox.setAlignment(Pos.CENTER_LEFT); prioBox.setPrefWidth(80);

        String stBg = status.equals("Đã hoàn thành") ? "rgba(16,185,129,0.15)" : "rgba(59,130,246,0.15)";
        String stText = status.equals("Đã hoàn thành") ? "#10b981" : "#3b82f6";
        HBox statusBox = new HBox(format.formatBadge(status, stBg, stText)); statusBox.setAlignment(Pos.CENTER_LEFT); statusBox.setPrefWidth(120);

        HBox actionCell = new HBox();
        actionCell.setPrefWidth(150);
        actionCell.setAlignment(Pos.CENTER_LEFT);

        if (isAccepted) {
            Label lblDone = format.formatLabel("🎉 Đã duyệt xong", FontWeight.BOLD, 12, "#10b981");
            actionCell.getChildren().add(lblDone);
        } else {
            Button btnSubmit = new Button("Nộp sản phẩm");
            btnSubmit.setFont(Font.font("Google Sans", FontWeight.BOLD, 11));
            btnSubmit.setTextFill(Color.WHITE);
            btnSubmit.setStyle("-fx-background-color: #5020d8; -fx-background-radius: 20px; -fx-padding: 6 14; -fx-cursor: hand;");
            btnSubmit.setOnAction(e -> {
                VBox submitBox = createSubmitTaskModal(rootModalPane, previousView, taskName, "Dự án hiện tại", "Hạn chót");
                rootModalPane.getChildren().setAll(submitBox);
            });
            actionCell.getChildren().add(btnSubmit);
        }

        row.getChildren().addAll(avatarBox, lblName, lblTask, prioBox, statusBox, actionCell);
        return row;
    }

    private VBox createSubmitTaskModal(StackPane rootModalPane, VBox previousView, String taskName, String parentWork, String deadline) {
        VBox box = new VBox(24);
        box.setPrefWidth(500);
        box.setMaxSize(500, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 32px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.25)));

        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        StackPane iconBg = new StackPane();
        iconBg.setPrefSize(48, 48);
        iconBg.setStyle("-fx-background-color: rgba(124,77,255,0.15); -fx-background-radius: 16px;");
        iconBg.getChildren().add(format.formatLabel("🚀", FontWeight.NORMAL, 24, "#000000"));

        VBox titleBox = new VBox(4);
        titleBox.getChildren().addAll(
                format.formatLabel("Nộp Sản Phẩm", FontWeight.BLACK, 22, "#1e293b"),
                format.formatLabel("Gửi đường dẫn liên kết cho Trưởng ban nghiệm thu", FontWeight.MEDIUM, 13, "#64748b")
        );
        header.getChildren().addAll(iconBg, titleBox);

        VBox infoBox = new VBox(12);
        infoBox.setPadding(new Insets(16));
        infoBox.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 16px; -fx-border-color: #e2e8f0;");
        infoBox.getChildren().addAll(
                format.formatLabel("Nhiệm vụ: " + taskName, FontWeight.BOLD, 14, "#5020d8"),
                format.formatLabel("Vui lòng đính kèm link sản phẩm (Drive, Figma, GitHub...) báo cáo tiến độ.", FontWeight.MEDIUM, 12, "#64748b")
        );

        VBox fields = new VBox(8);
        TextField fLink = format.formatTextField("Dán liên kết sản phẩm của bạn...");
        fields.getChildren().addAll(format.formatLabel("Đường dẫn sản phẩm *", FontWeight.BOLD, 12, "#1e293b"), fLink);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnCancel = getFormBtn("Quay lại", "rgba(148,163,184,0.15)", "#64748b");
        btnCancel.setOnAction(e -> rootModalPane.getChildren().setAll(previousView));

        Button btnConfirm = getFormBtn("Gửi Nghiệm Thu", "#5020d8", "white");
        btnConfirm.setOnAction(e -> {
            if (fLink.getText().trim().isEmpty()) {
                view.mem.frame.getInstance().triggerToast("❌ Vui lòng nhập link báo cáo sản phẩm!");
                return;
            }
            view.mem.frame.getInstance().triggerToast("Đã nộp sản phẩm thành công! Đã nhận yêu cầu nghiệm thu.");
            rootModalPane.getChildren().setAll(previousView);
        });
        actions.getChildren().addAll(btnCancel, btnConfirm);

        box.getChildren().addAll(header, infoBox, fields, actions);
        return box;
    }

    private VBox createGuideModal(StackPane rootModalPane, VBox previousView) {
        VBox box = new VBox(20);
        box.setPrefWidth(450);
        box.setMaxSize(450, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px;");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel("Thuật toán tiến độ %", FontWeight.BLACK, 20, "#f59e0b");
        Label textRule = format.formatLabel("Tiến độ tổng của Công việc được tính tự động dựa trên trọng số điểm của các nhiệm vụ đã hoàn thành:\n\n• Ưu tiên Cao: 3 điểm\n• Ưu tiên Trung bình: 2 điểm\n• Ưu tiên Thấp: 1 điểm", FontWeight.MEDIUM, 13, "#475569");
        textRule.setWrapText(true);

        HBox actions = new HBox(12); actions.setAlignment(Pos.CENTER);
        Button btnClose = getFormBtn("Đã rõ", "#10b981", "white");
        btnClose.setOnAction(e -> rootModalPane.getChildren().setAll(previousView));
        actions.getChildren().add(btnClose);

        box.getChildren().addAll(title, textRule, actions);
        return box;
    }

    private Button getShadowBtn(String text, String icon, String bgColor, String textColor, String shadowColor) {
        Button btn = new Button();
        HBox content = new HBox(8);
        content.setAlignment(Pos.CENTER);
        if (!icon.isEmpty()) content.getChildren().add(new Label(icon));
        Label lblText = new Label(text); lblText.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));
        lblText.setTextFill(Color.web(textColor)); content.getChildren().add(lblText);
        btn.setGraphic(content);
        btn.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 40px; -fx-padding: 8 16; -fx-cursor: hand;");
        return btn;
    }

    private static Button getFormBtn(String text, String bgColor, String textColor) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));
        btn.setTextFill(Color.web(textColor));
        btn.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 40px; -fx-padding: 8 16; -fx-cursor: hand;");
        return btn;
    }
}