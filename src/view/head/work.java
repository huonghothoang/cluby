package view.head;

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

public class work extends ScrollPane {

    private VBox dashboardContent;

    public work() {
        setPickOnBounds(false);

        dashboardContent = new VBox(32);
        dashboardContent.setPadding(new Insets(32));
        dashboardContent.setStyle("-fx-background-color: transparent;");

        HBox kpiRow = new HBox(16);
        VBox card1 = createStatCard("Công việc được giao", "8", "#475569", "#1e293b", "#a5b4fc", "📝");
        VBox card2 = createStatCard("Đang tiến hành", "5", "#475569", "#3b82f6", "#bfdbfe", "⏳");
        VBox card3 = createStatCard("Hoàn thành", "2", "#475569", "#10b981", "#a7f3d0", "✅");
        VBox card4 = createStatCard("Quá hạn", "1", "#475569", "#ef4444", "#fca5a5", "⚠");

        HBox.setHgrow(card1, Priority.ALWAYS); HBox.setHgrow(card2, Priority.ALWAYS);
        HBox.setHgrow(card3, Priority.ALWAYS); HBox.setHgrow(card4, Priority.ALWAYS);
        kpiRow.getChildren().addAll(card1, card2, card3, card4);

        HBox filterBar = new HBox(16);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(12, 24, 12, 24));
        format.formatGlass(filterBar, 40, 0.4);

        HBox searchBox = new HBox(8);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = format.formatTextField("Tìm tên công việc...");
        Button btnSearch = format.formatFindBtn();
        searchBox.getChildren().addAll(searchField, btnSearch);

        ComboBox<String> cbTimeSort = format.formatSortBtn("Sắp xếp", "Mới nhất", "Cũ nhất");
        ComboBox<String> cbStatusSort = format.formatSortBtn("Trạng thái", "Tất cả", "Chưa tiến hành", "Đang tiến hành", "Hoàn thành", "Quá hạn");
        fixHover(cbTimeSort); fixHover(cbStatusSort);

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnGuide = getShadowBtn("Hướng dẫn tính", "rgba(245,158,11,0.15)", "#f59e0b", "rgba(0,0,0,0.05)");
        btnGuide.setOnAction(e -> {
            StackPane guideModal = createGuideModal();
            frame.getInstance().showCustomModal(guideModal);
        });

        filterBar.getChildren().addAll(searchBox, cbTimeSort, cbStatusSort, spacer, btnGuide);

        VBox workTableContainer = format.formatTableContainer();
        Label tableTitle = format.formatLabel("DANH SÁCH QUẢN LÝ CÔNG VIỆC", FontWeight.BLACK, 14, "#1e293b");
        tableTitle.setPadding(new Insets(12, 16, 0, 16));
        workTableContainer.getChildren().addAll(tableTitle, createWorkTableHeader());

        VBox workRows = new VBox(4);
        workRows.getChildren().addAll(
                createWorkRow("Thiết kế Poster Workshop", "Workshop GitHub", "28/06/2026", "Đang tiến hành", "Cao", false, 0.44),
                createWorkRow("Chuẩn bị bài đăng Fanpage", "Workshop GitHub", "25/06/2026", "Đang tiến hành", "Trung bình", true, 0.20),
                createWorkRow("Chụp ảnh Profile Ban", "Không", "20/06/2026", "Hoàn thành", "Thấp", false, 1.0),
                createWorkRow("Quay video Teaser", "Team Building", "10/07/2026", "Chưa tiến hành", "Cao", false, 0.0)
        );

        ScrollPane workScroll = new ScrollPane(workRows);
        workScroll.setPrefHeight(300);
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

    private HBox createWorkTableHeader() {
        HBox header = new HBox(16);
        header.setPadding(new Insets(12, 16, 12, 16));
        header.setStyle("-fx-border-color: transparent transparent rgba(255,255,255,0.4) transparent; -fx-border-width: 1px;");

        Label l1 = format.formatLabel("TÊN CÔNG VIỆC", FontWeight.BLACK, 10, "#94a3b8"); l1.setPrefWidth(220);
        Label l2 = format.formatLabel("THUỘC SỰ KIỆN", FontWeight.BLACK, 10, "#94a3b8"); l2.setPrefWidth(150);
        Label l3 = format.formatLabel("HẠN CHÓT", FontWeight.BLACK, 10, "#94a3b8"); l3.setPrefWidth(110);
        Label l4 = format.formatLabel("MỨC ƯU TIÊN", FontWeight.BLACK, 10, "#94a3b8"); l4.setPrefWidth(110);
        Label l5 = format.formatLabel("TIẾN ĐỘ THỰC TẾ", FontWeight.BLACK, 10, "#94a3b8"); l5.setPrefWidth(180);
        Label l6 = format.formatLabel("CHI TIẾT", FontWeight.BLACK, 10, "#94a3b8"); l6.setPrefWidth(80);

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

        String stBg = status.equals("Chưa tiến hành") ? "rgba(100,116,139,0.12)" : status.equals("Đang tiến hành") ? "rgba(59,130,246,0.12)" : "rgba(16,185,129,0.12)";
        String stText = status.equals("Chưa tiến hành") ? "#64748b" : status.equals("Đang tiến hành") ? "#3b82f6" : "#10b981";

        Label statusBadge = format.formatBadge(status, stBg, stText);
        statusBadge.setStyle(statusBadge.getStyle() + "-fx-font-size: 10px; -fx-padding: 2 6;");

        HBox badges = new HBox(6, statusBadge);

        if (isOverdue && !status.equals("Hoàn thành")) {
            Label overdueBadge = format.formatBadge("Quá hạn", "rgba(239,68,68,0.12)", "#ef4444");
            overdueBadge.setStyle(overdueBadge.getStyle() + "-fx-font-size: 10px; -fx-padding: 2 6;");
            badges.getChildren().add(0, overdueBadge);
        }

        titleLayout.getChildren().addAll(lblTitle, badges);

        Label lblEvent = format.formatLabel(event, FontWeight.BOLD, 12, event.equals("Không") ? "#94a3b8" : "#7c4dff");
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
            frame.getInstance().showCustomModal(detailModal);
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
                format.formatLabel("Tiến độ: " + workTitle, FontWeight.BLACK, 24, "#1e293b"),
                format.formatLabel("Sự kiện: " + eventName + "   |   Hạn chót: " + deadline, FontWeight.BOLD, 13, "#64748b")
        );

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnCreateTask = getShadowBtn("Tạo nhiệm vụ", "#10b981", "white", "rgba(16,185,129,0.4)");
        btnCreateTask.setOnAction(e -> {
            VBox assignModal = createAssignTaskModal(rootModalPane, modalContent);
            rootModalPane.getChildren().setAll(assignModal);
        });

        header.getChildren().addAll(titleBox, spacer, btnCreateTask);

        HBox progressRow = new HBox(16);
        progressRow.setAlignment(Pos.CENTER_LEFT);

        Label lblProgText = format.formatLabel("Tiến độ: " + (int)(progress * 100) + "%", FontWeight.BLACK, 12, "#7c4dff");
        lblProgText.setMinWidth(Region.USE_PREF_SIZE);

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

        VBox listContainer = format.formatBoxCard();
        listContainer.setPadding(new Insets(16, 8, 16, 16));
        listContainer.getChildren().add(format.formatLabel("DANH SÁCH NHIỆM VỤ ĐÃ GIAO", FontWeight.BLACK, 12, "#94a3b8"));

        HBox listHeader = new HBox(12);
        listHeader.setPadding(new Insets(12, 12, 12, 12));
        listHeader.setStyle("-fx-border-color: transparent transparent rgba(0,0,0,0.1) transparent; -fx-border-width: 1px;");

        Label l1 = format.formatLabel("THÀNH VIÊN", FontWeight.BLACK, 10, "#94a3b8"); l1.setPrefWidth(160);
        Label l2 = format.formatLabel("NHIỆM VỤ", FontWeight.BLACK, 10, "#94a3b8"); l2.setPrefWidth(200);
        Label l3 = format.formatLabel("ƯU TIÊN", FontWeight.BLACK, 10, "#94a3b8"); l3.setPrefWidth(100);
        Label l4 = format.formatLabel("TÌNH TRẠNG", FontWeight.BLACK, 10, "#94a3b8"); l4.setPrefWidth(130);
        Label l5 = format.formatLabel("SẢN PHẨM", FontWeight.BLACK, 10, "#94a3b8"); l5.setPrefWidth(120);
        Label l6 = format.formatLabel("NGHIỆM THU", FontWeight.BLACK, 10, "#94a3b8"); l6.setPrefWidth(90);

        listHeader.getChildren().addAll(l1, l2, l3, l4, l5, l6);
        listContainer.getChildren().add(listHeader);

        VBox rows = new VBox(4);
        rows.getChildren().addAll(
                createTaskRow("Trần Văn B", "Vẽ Background", "Cao", "Hoàn thành", "https://youtube.com", true),
                createTaskRow("Lê Thị C", "Vẽ Nhân vật", "Cao", "Chưa hoàn thành", "Chưa cập nhật", false),
                createTaskRow("Nguyễn Hoàng D", "Lên typography", "Trung bình", "Chưa hoàn thành", "Chưa cập nhật", false),
                createTaskRow("Phạm Văn E", "Xuất file in", "Thấp", "Hoàn thành", "https://youtube.com", true)
        );

        ScrollPane scrollList = new ScrollPane(rows);
        scrollList.setMinHeight(250); scrollList.setMaxHeight(250);
        format.formatScrollbar(scrollList, rows, 8);
        applySmoothScroll(scrollList, rows);

        listContainer.getChildren().add(scrollList);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(16, 0, 0, 0));

        Button btnClose = getModalActionBtn("Đóng", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnClose.setOnAction(e -> frame.getInstance().closeOverlayModal());

        actions.getChildren().add(btnClose);
        modalContent.getChildren().addAll(header, progressRow, listContainer, actions);

        rootModalPane.getChildren().add(modalContent);
        return rootModalPane;
    }

    private StackPane createGuideModal() {
        StackPane rootModalPane = new StackPane();
        VBox box = new VBox(20);
        box.setPrefWidth(450);
        box.setMaxSize(450, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel("Thuật toán tính % Tiến độ", FontWeight.BLACK, 20, "#f59e0b");
        Label desc = format.formatLabel("Thanh tiến độ phản ánh khối lượng công việc thực tế dựa trên Trọng số (Mức độ quan trọng) của từng nhiệm vụ:", FontWeight.MEDIUM, 13, "#475569");
        desc.setWrapText(true);

        VBox ruleBox = new VBox(8);
        ruleBox.setPadding(new Insets(16));
        ruleBox.setStyle("-fx-background-color: rgba(245,158,11,0.1); -fx-background-radius: 16px;");
        ruleBox.getChildren().addAll(
                format.formatLabel("Ưu tiên Cao: 3 Điểm", FontWeight.BOLD, 13, "#ef4444"),
                format.formatLabel("Ưu tiên Trung bình: 2 Điểm", FontWeight.BOLD, 13, "#f59e0b"),
                format.formatLabel("Ưu tiên Thấp: 1 Điểm", FontWeight.BOLD, 13, "#10b981")
        );

        Label exTitle = format.formatLabel("Ví dụ thực tế (Theo dữ liệu mẫu):", FontWeight.BOLD, 13, "#1e293b");
        VBox exBox = new VBox(4);
        exBox.getChildren().addAll(
                format.formatLabel("- Vẽ Background (Cao - Đã xong) -> Lấy 3 điểm", FontWeight.MEDIUM, 12, "#475569"),
                format.formatLabel("- Vẽ Nhân vật (Cao - Chưa xong) -> 0 điểm", FontWeight.MEDIUM, 12, "#475569"),
                format.formatLabel("- Lên typography (TB - Chưa xong) -> 0 điểm", FontWeight.MEDIUM, 12, "#475569"),
                format.formatLabel("- Xuất file in (Thấp - Đã xong) -> Lấy 1 điểm", FontWeight.MEDIUM, 12, "#475569"),
                new Region(),
                format.formatLabel("=> Tổng điểm tối đa: 3 + 3 + 2 + 1 = 9 Điểm", FontWeight.BOLD, 13, "#1e293b"),
                format.formatLabel("=> Tổng điểm đã đạt: 3 + 1 = 4 Điểm", FontWeight.BOLD, 13, "#10b981"),
                format.formatLabel("=> Tiến độ %: (4 / 9) * 100 = 44%", FontWeight.BLACK, 14, "#7c4dff")
        );

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(16, 0, 0, 0));

        Button btnClose = getModalActionBtn("Đã hiểu", "#10b981", "white", "rgba(16,185,129,0.4)");
        btnClose.setOnAction(e -> frame.getInstance().closeOverlayModal());

        actions.getChildren().add(btnClose);
        box.getChildren().addAll(title, desc, ruleBox, exTitle, exBox, actions);
        rootModalPane.getChildren().add(box);

        return rootModalPane;
    }

    private HBox createTaskRow(String memberName, String taskName, String priority, String status, String link, boolean isAccepted) {
        HBox row = new HBox(12);
        row.setPadding(new Insets(12));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-border-color: transparent transparent rgba(0,0,0,0.05) transparent; -fx-border-width: 1px;");

        Label lblName = format.formatLabel(memberName, FontWeight.BOLD, 13, "#1e293b"); lblName.setPrefWidth(160);
        Label lblTask = format.formatLabel(taskName, FontWeight.BOLD, 13, "#475569"); lblTask.setPrefWidth(200);

        String prioBg = priority.equals("Cao") ? "rgba(239,68,68,0.15)" : priority.equals("Trung bình") ? "rgba(245,158,11,0.15)" : "rgba(16,185,129,0.15)";
        String prioText = priority.equals("Cao") ? "#ef4444" : priority.equals("Trung bình") ? "#f59e0b" : "#10b981";
        HBox prioBox = new HBox(format.formatBadge(priority, prioBg, prioText)); prioBox.setAlignment(Pos.CENTER_LEFT); prioBox.setPrefWidth(100);

        String stBg = status.equals("Hoàn thành") ? "rgba(16,185,129,0.15)" : "rgba(100,116,139,0.15)";
        String stText = status.equals("Hoàn thành") ? "#10b981" : "#64748b";
        HBox statusBox = new HBox(format.formatBadge(status, stBg, stText)); statusBox.setAlignment(Pos.CENTER_LEFT); statusBox.setPrefWidth(130);

        HBox linkBox = new HBox();
        linkBox.setPrefWidth(120);
        linkBox.setAlignment(Pos.CENTER_LEFT);
        if (!link.equals("Chưa cập nhật")) {
            Hyperlink hl = new Hyperlink(link);
            hl.setStyle("-fx-text-fill: #3b82f6; -fx-font-family: 'Google Sans'; -fx-font-weight: bold; -fx-font-size: 12px; -fx-padding: 0; -fx-border-width: 0;");
            hl.setOnAction(e -> {
                try { java.awt.Desktop.getDesktop().browse(new java.net.URI(link)); } catch (Exception ex) { ex.printStackTrace(); }
            });
            linkBox.getChildren().add(hl);
        } else {
            Label lblLink = format.formatLabel(link, FontWeight.MEDIUM, 12, "#94a3b8");
            linkBox.getChildren().add(lblLink);
        }

        CheckBox chkAccept = format.formatCheckBox(""); chkAccept.setSelected(isAccepted);
        HBox chkBox = new HBox(chkAccept); chkBox.setAlignment(Pos.CENTER_LEFT); chkBox.setPrefWidth(90);

        row.getChildren().addAll(lblName, lblTask, prioBox, statusBox, linkBox, chkBox);
        return row;
    }

    private VBox createAssignTaskModal(StackPane rootModalPane, VBox previousView) {
        VBox box = new VBox(20);
        box.setPrefWidth(450);
        box.setMaxSize(450, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel("Giao nhiệm vụ", FontWeight.BLACK, 20, "#1e293b");

        VBox fields = new VBox(16);

        TextField fTaskName = format.formatTextField("Ví dụ: Vẽ phác thảo Background...");
        VBox nameGroup = new VBox(6, format.formatLabel("Tên nhiệm vụ *", FontWeight.BOLD, 12, "#94a3b8"), fTaskName);

        ComboBox<String> cbPriority = format.formatSortBtn("Mức độ ưu tiên", "Thấp", "Trung bình", "Cao");
        cbPriority.setValue("Trung bình"); cbPriority.setPrefWidth(Double.MAX_VALUE);
        fixHover(cbPriority);
        VBox prioGroup = new VBox(6, format.formatLabel("Độ quan trọng", FontWeight.BOLD, 12, "#94a3b8"), cbPriority);

        ComboBox<String> cbMember = format.formatSortBtn("Chọn thành viên phụ trách", "Trần Văn B", "Lê Thị C", "Nguyễn Hoàng D", "Phạm Văn E");
        cbMember.setPrefWidth(Double.MAX_VALUE);
        fixHover(cbMember);
        VBox memberGroup = new VBox(6, format.formatLabel("Thành viên phụ trách *", FontWeight.BOLD, 12, "#94a3b8"), cbMember);

        fields.getChildren().addAll(nameGroup, prioGroup, memberGroup);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(8, 0, 0, 0));

        Button btnCancel = getModalActionBtn("Quay lại", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> rootModalPane.getChildren().setAll(previousView));

        Button btnConfirm = getModalActionBtn("Giao việc", "#10b981", "white", "rgba(16,185,129,0.4)");
        btnConfirm.setOnAction(e -> {
            if (fTaskName.getText().trim().isEmpty() || cbMember.getValue() == null) {
                frame.getInstance().triggerToast("❌ Vui lòng nhập tên nhiệm vụ và chọn người thực hiện!");
                return;
            }
            frame.getInstance().triggerToast("Đã giao nhiệm vụ thành công!");
            rootModalPane.getChildren().setAll(previousView);
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().addAll(title, fields, actions);

        return box;
    }

    private Button getShadowBtn(String text, String bgColor, String textColor, String shadowColor) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Google Sans", FontWeight.BOLD, 13));
        btn.setTextFill(Color.web(textColor));
        btn.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 40px; -fx-padding: 8 20 8 20; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, " + shadowColor + ", 10, 0, 0, 4);");
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