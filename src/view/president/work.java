package view.president;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import view.format;
import view.president.frame;

public class work extends ScrollPane {

    private VBox dashboardContent;

    public work() {
        setPickOnBounds(false);

        dashboardContent = new VBox(32);
        dashboardContent.setPadding(new Insets(32));
        dashboardContent.setStyle("-fx-background-color: transparent;");

        HBox kpiRow = new HBox(16);
        kpiRow.getChildren().addAll(
                format.formatKPICard("Tổng số", "42", "#64748b", "#1e293b"),
                format.formatKPICard("Đang làm", "15", "#3b82f6", "#1e293b"),
                format.formatKPICard("Hoàn thành", "24", "#10b981", "#1e293b"),
                format.formatKPICard("Quá hạn", "3", "#ef4444", "#ef4444")
        );
        for (Node node : kpiRow.getChildren()) { HBox.setHgrow(node, Priority.ALWAYS); }

        HBox filterBar = new HBox(16);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(12, 24, 12, 24));
        format.formatGlass(filterBar, 40, 0.4);

        HBox searchBox = new HBox(8);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = format.formatTextField("Tìm kiếm...");
        Button btnSearch = format.formatFindBtn();
        searchBox.getChildren().addAll(searchField, btnSearch);

        ComboBox<String> cbTimeSort = format.formatSortBtn("Thời gian", "Mới nhất", "Cũ nhất");
        ComboBox<String> cbStatus = format.formatSortBtn("Trạng thái", "Tất cả", "Chưa bắt đầu", "Đang tiến hành", "Hoàn thành", "Đã hủy");
        ComboBox<String> cbDept = format.formatSortBtn("Bộ phận", "Tất cả", "Nội dung", "Kỹ thuật", "Truyền thông", "Hậu cần");

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnAdd = getShadowBtn("Tạo công việc", "➕", "#5020d8", "white", "rgba(80,32,216,0.4)");
        btnAdd.setOnAction(e -> {
            StackPane addModalRoot = new StackPane();
            VBox addForm = createWorkFormModal("Tạo công việc", "", "", "Việc chung", "Truyền thông", "26/06/2026", "28/06/2026", "Trung bình", "Chưa bắt đầu", false, addModalRoot, null);
            addModalRoot.getChildren().add(addForm);
            frame.getInstance().showCustomModal(addModalRoot);
        });

        filterBar.getChildren().addAll(searchBox, cbTimeSort, cbStatus, cbDept, spacer, btnAdd);

        VBox workTableContainer = format.formatTableContainer();
        Label tableTitle = format.formatLabel("DANH SÁCH CÔNG VIỆC", FontWeight.BLACK, 14, "#1e293b");
        tableTitle.setPadding(new Insets(12, 16, 0, 16));
        workTableContainer.getChildren().addAll(tableTitle, createWorkTableHeader());

        VBox workRows = new VBox(4);
        workRows.getChildren().addAll(
                createWorkRow("Thiết kế Poster cho Sự kiện mới", "Tech Share #01: Git & GitHub", "Truyền thông", "28/06/2026", "Đang tiến hành", "Cao", false),
                createWorkRow("Liên hệ đối tác tài trợ", "Việc chung", "Nội dung", "20/06/2026", "Đang tiến hành", "Cao", true),
                createWorkRow("Chuẩn bị thiết bị kỹ thuật", "Tech Share #01: Git & GitHub", "Kỹ thuật", "27/06/2026", "Hoàn thành", "Trung bình", false),
                createWorkRow("Mua đồ hậu cần", "Networking Day", "Hậu cần", "10/07/2026", "Đã hủy", "Thấp", false)
        );

        ScrollPane workScroll = new ScrollPane(workRows);
        workScroll.setPrefHeight(380);
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

        Label l1 = format.formatLabel("CÔNG VIỆC / TRẠNG THÁI", FontWeight.BLACK, 10, "#94a3b8"); l1.setPrefWidth(240);
        Label l2 = format.formatLabel("LIÊN KẾT SỰ KIỆN", FontWeight.BLACK, 10, "#94a3b8"); l2.setPrefWidth(160);
        Label l3 = format.formatLabel("BỘ PHẬN PHỤ TRÁCH", FontWeight.BLACK, 10, "#94a3b8"); l3.setPrefWidth(140);
        Label l4 = format.formatLabel("HẠN HOÀN THÀNH", FontWeight.BLACK, 10, "#94a3b8"); l4.setPrefWidth(120);
        Label l5 = format.formatLabel("MỨC ƯU TIÊN", FontWeight.BLACK, 10, "#94a3b8"); l5.setPrefWidth(100);
        Label l6 = format.formatLabel("HÀNH ĐỘNG", FontWeight.BLACK, 10, "#94a3b8"); l6.setPrefWidth(100);

        header.getChildren().addAll(l1, l2, l3, l4, l5, l6);
        return header;
    }

    private HBox createWorkRow(String title, String event, String dept, String deadline, String status, String priority, boolean isOverdue) {
        HBox row = new HBox(16);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand;");
        row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand; -fx-background-radius: 12px;"));
        row.setOnMouseExited(e -> row.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand;"));

        VBox titleLayout = new VBox(4);
        titleLayout.setPrefWidth(240);
        Label lblTitle = format.formatLabel(title, FontWeight.BOLD, 14, "#1e293b"); lblTitle.setWrapText(true);

        String stBg = status.equals("Chưa bắt đầu") ? "rgba(100,116,139,0.12)" : status.equals("Đang tiến hành") ? "rgba(59,130,246,0.12)" : status.equals("Hoàn thành") ? "rgba(16,185,129,0.12)" : "rgba(239,68,68,0.12)";
        String stText = status.equals("Chưa bắt đầu") ? "#64748b" : status.equals("Đang tiến hành") ? "#3b82f6" : status.equals("Hoàn thành") ? "#10b981" : "#ef4444";
        Label statusBadge = format.formatBadge(status, stBg, stText);
        statusBadge.setStyle(statusBadge.getStyle() + "-fx-font-size: 10px; -fx-padding: 2 6;");

        HBox badges = new HBox(6, statusBadge);
        if (isOverdue && !status.equals("Hoàn thành") && !status.equals("Đã hủy")) {
            Label overdueBadge = format.formatBadge("Quá hạn", "rgba(239,68,68,0.12)", "#ef4444");
            overdueBadge.setStyle(overdueBadge.getStyle() + "-fx-font-size: 10px; -fx-padding: 2 6;");
            badges.getChildren().add(0, overdueBadge);
        }
        titleLayout.getChildren().addAll(lblTitle, badges);

        String eventLabel = event.equals("Việc chung") ? "🏢 Việc chung" : "📅 " + event;
        Label lblEvent = format.formatLabel(eventLabel, FontWeight.BOLD, 12, event.equals("Việc chung") ? "#94a3b8" : "#7c4dff");
        lblEvent.setPrefWidth(160); lblEvent.setWrapText(true);

        Label lblDept = format.formatLabel(dept, FontWeight.BOLD, 13, "#475569");
        lblDept.setPrefWidth(140); lblDept.setWrapText(true);

        Label lblDeadline = format.formatLabel(deadline, FontWeight.BOLD, 13, isOverdue ? "#ef4444" : "#475569");
        lblDeadline.setPrefWidth(120);

        Label lblPrio = format.formatLabel(priority, FontWeight.BLACK, 13, priority.equals("Cao") ? "#ef4444" : priority.equals("Trung bình") ? "#f59e0b" : "#10b981");
        lblPrio.setPrefWidth(100);

        HBox actionBox = new HBox(8); actionBox.setAlignment(Pos.CENTER_LEFT); actionBox.setPrefWidth(100);
        Button btnView = format.formatCircleBtn("👁️‍🗨️", "#448aff", "#7c4dff");
        btnView.setPrefSize(36, 36);
        btnView.setOnAction(e -> {
            StackPane detailModal = createDetailModal(title, "Thông tin chi tiết công việc nội bộ.", event, dept, "24/06/2026", deadline, priority, status, isOverdue);
            frame.getInstance().showCustomModal(detailModal);
        });
        actionBox.getChildren().add(btnView);

        if (!status.equals("Hoàn thành") && !status.equals("Đã hủy")) {
            Button btnCancelWork = format.formatCircleBtn("➖", "#ef4444", "#dc2626");
            btnCancelWork.setPrefSize(36, 36);
            btnCancelWork.setOnAction(e -> {
                StackPane cancelModal = createCancelModalDashboard(title);
                frame.getInstance().showCustomModal(cancelModal);
            });
            actionBox.getChildren().add(btnCancelWork);
        }

        row.getChildren().addAll(titleLayout, lblEvent, lblDept, lblDeadline, lblPrio, actionBox);
        return row;
    }

    private StackPane createDetailModal(String title, String desc, String event, String dept, String startDate, String deadline, String priority, String status, boolean isOverdue) {
        StackPane rootModalPane = new StackPane();

        VBox modalContent = new VBox(24);
        modalContent.setPrefWidth(784);
        modalContent.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        modalContent.setMaxSize(784, Region.USE_PREF_SIZE);
        modalContent.setPadding(new Insets(32));
        modalContent.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        modalContent.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        Label lblTitle = format.formatLabel(title, FontWeight.BLACK, 24, "#1e293b");
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox badges = new HBox(8);
        badges.setAlignment(Pos.CENTER_RIGHT);
        if (isOverdue && !status.equals("Hoàn thành") && !status.equals("Đã hủy")) {
            Label overdueBadge = format.formatBadge("⚠ Quá hạn", "rgba(239,68,68,0.15)", "#ef4444");
            overdueBadge.setStyle(overdueBadge.getStyle() + "-fx-font-size: 12px; -fx-padding: 6 12;");
            badges.getChildren().add(overdueBadge);
        }

        String stBg = status.equals("Chưa bắt đầu") ? "rgba(100,116,139,0.15)" : status.equals("Đang tiến hành") ? "rgba(59,130,246,0.15)" : status.equals("Hoàn thành") ? "rgba(16,185,129,0.15)" : "rgba(239,68,68,0.15)";
        String stText = status.equals("Chưa bắt đầu") ? "#64748b" : status.equals("Đang tiến hành") ? "#3b82f6" : status.equals("Hoàn thành") ? "#10b981" : "#ef4444";
        Label statusBadge = format.formatBadge(status, stBg, stText);
        statusBadge.setStyle(statusBadge.getStyle() + "-fx-font-size: 14px; -fx-padding: 6 16;");
        badges.getChildren().add(statusBadge);

        header.getChildren().addAll(lblTitle, spacer, badges);

        VBox infoBox = format.formatBoxCard();
        infoBox.getChildren().add(format.formatLabel("THÔNG TIN CÔNG VIỆC", FontWeight.BLACK, 12, "#94a3b8"));

        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(40); infoGrid.setVgap(16);

        String eventLabel = event.equals("Việc chung") ? "🏢 Việc chung" : "📅 " + event;
        infoGrid.add(new VBox(4, format.formatLabel("Liên kết sự kiện", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(eventLabel, FontWeight.BOLD, 14, event.equals("Việc chung") ? "#64748b" : "#7c4dff")), 0, 0, 2, 1);

        infoGrid.add(new VBox(4, format.formatLabel("Ngày bắt đầu", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(startDate, FontWeight.BLACK, 14, "#1e293b")), 0, 1);
        infoGrid.add(new VBox(4, format.formatLabel("Hạn hoàn thành", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(deadline, FontWeight.BLACK, 14, isOverdue ? "#ef4444" : "#1e293b")), 1, 1);

        Label lblPrio = format.formatLabel(priority, FontWeight.BOLD, 14, priority.equals("Cao") ? "#ef4444" : priority.equals("Trung bình") ? "#f59e0b" : "#10b981");
        infoGrid.add(new VBox(4, format.formatLabel("Mức độ ưu tiên", FontWeight.BOLD, 12, "#94a3b8"), lblPrio), 2, 1);

        Label lblDesc = format.formatLabel(desc, FontWeight.MEDIUM, 14, "#475569");
        lblDesc.setWrapText(true);
        infoGrid.add(new VBox(4, format.formatLabel("Mô tả chi tiết", FontWeight.BOLD, 12, "#94a3b8"), lblDesc), 0, 2, 3, 1);
        infoBox.getChildren().add(infoGrid);

        VBox hrBox = format.formatBoxCard();
        hrBox.getChildren().addAll(
                format.formatLabel("BỘ PHẬN PHỤ TRÁCH", FontWeight.BLACK, 12, "#94a3b8"),
                format.formatLabel("🏢 " + dept, FontWeight.BLACK, 16, "#5020d8"),
                format.formatLabel("Trưởng nhóm chịu trách nhiệm điều hành phân bổ cụ thể.", FontWeight.MEDIUM, 12, "#64748b")
        );

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);

        Button btnClose = getShadowBtn("Đóng", "", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnClose.setOnAction(e -> frame.getInstance().closeOverlayModal());
        actions.getChildren().add(btnClose);

        if (!status.equals("Đã hủy")) {
            Button btnEdit = getShadowBtn("Sửa công việc", "", "#5020d8", "white", "rgba(80,32,216,0.4)");
            btnEdit.setOnAction(e -> {
                VBox editModal = createWorkFormModal("Sửa công việc", title, desc, event, dept, startDate, deadline, priority, status, true, rootModalPane, modalContent);
                rootModalPane.getChildren().setAll(editModal);
            });
            actions.getChildren().add(btnEdit);
        }

        modalContent.getChildren().addAll(header, infoBox, hrBox, actions);
        rootModalPane.getChildren().add(modalContent);
        return rootModalPane;
    }

    private VBox createWorkFormModal(String titleText, String initTitle, String initDesc, String initEvent, String initDept, String initStart, String initEnd, String initPrio, String status, boolean isEdit, StackPane rootModalPane, VBox previousView) {
        VBox box = new VBox(20);
        box.setPrefWidth(540);
        box.setMaxSize(540, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel(titleText, FontWeight.BLACK, 24, "#1e293b");

        VBox fields = new VBox(16);

        TextField fName = format.formatTextField("Nhập tên..."); fName.setText(initTitle);
        VBox nameGroup = new VBox(6, format.formatLabel("Tên công việc *", FontWeight.BOLD, 12, "#94a3b8"), fName);

        TextField fDesc = format.formatTextField("Nhập mô tả..."); fDesc.setText(initDesc);
        VBox descGroup = new VBox(6, format.formatLabel("Mô tả công việc", FontWeight.BOLD, 12, "#94a3b8"), fDesc);

        ComboBox<String> cbEvent = format.formatSortBtn("Chọn sự kiện", "Việc chung", "Tech Share #01: Git & GitHub", "Team Building", "Networking Day");
        cbEvent.setValue(initEvent.isEmpty() ? "Việc chung" : initEvent); cbEvent.setPrefWidth(Double.MAX_VALUE);
        VBox eventGroup = new VBox(6, format.formatLabel("Liên kết sự kiện", FontWeight.BOLD, 12, "#94a3b8"), cbEvent);

        ComboBox<String> cbDept = format.formatSortBtn("Bộ phận", "Nội dung", "Kỹ thuật", "Truyền thông", "Hậu cần");
        cbDept.setValue(initDept.isEmpty() ? "Truyền thông" : initDept); cbDept.setMaxWidth(Double.MAX_VALUE);
        VBox deptGroup = new VBox(6, format.formatLabel("Bộ phận chịu trách nhiệm *", FontWeight.BOLD, 12, "#94a3b8"), cbDept);

        HBox timeRow = new HBox(12);
        TextField fStart = format.formatTextField("dd/MM/yyyy"); fStart.setText(initStart);
        VBox startGroup = new VBox(6, format.formatLabel("Ngày bắt đầu", FontWeight.BOLD, 12, "#94a3b8"), fStart);
        HBox.setHgrow(startGroup, Priority.ALWAYS);

        TextField fEnd = format.formatTextField("dd/MM/yyyy"); fEnd.setText(initEnd);
        VBox endGroup = new VBox(6, format.formatLabel("Hạn hoàn thành *", FontWeight.BOLD, 12, "#94a3b8"), fEnd);
        HBox.setHgrow(endGroup, Priority.ALWAYS);
        timeRow.getChildren().addAll(startGroup, endGroup);

        HBox optRow = new HBox(12);
        ComboBox<String> cbPrio = format.formatSortBtn("Ưu tiên", "Thấp", "Trung bình", "Cao");
        cbPrio.setValue(initPrio.isEmpty() ? "Trung bình" : initPrio); HBox.setHgrow(cbPrio, Priority.ALWAYS); cbPrio.setMaxWidth(Double.MAX_VALUE);
        VBox prioGroup = new VBox(6, format.formatLabel("Mức độ ưu tiên", FontWeight.BOLD, 12, "#94a3b8"), cbPrio);
        HBox.setHgrow(prioGroup, Priority.ALWAYS);
        optRow.getChildren().add(prioGroup);

        if (isEdit) {
            ComboBox<String> cbStatus = format.formatSortBtn("Trạng thái", "Chưa bắt đầu", "Đang tiến hành", "Hoàn thành", "Đã hủy");
            cbStatus.setValue(status); HBox.setHgrow(cbStatus, Priority.ALWAYS); cbStatus.setMaxWidth(Double.MAX_VALUE);
            VBox statusGroup = new VBox(6, format.formatLabel("Trạng thái công việc", FontWeight.BOLD, 12, "#94a3b8"), cbStatus);
            HBox.setHgrow(statusGroup, Priority.ALWAYS);
            optRow.getChildren().add(statusGroup);
        }

        fields.getChildren().addAll(nameGroup, descGroup, eventGroup, deptGroup, timeRow, optRow);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);

        Button btnCancel = getShadowBtn(isEdit ? "Quay lại" : "Huỷ", "", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> {
            if (!isEdit) frame.getInstance().closeOverlayModal();
            else rootModalPane.getChildren().setAll(previousView);
        });

        Button btnConfirm = getShadowBtn("Lưu công việc", "", "#5020d8", "white", "rgba(80,32,216,0.4)");
        btnConfirm.setOnAction(e -> {
            if (fName.getText().trim().isEmpty()) {
                frame.getInstance().triggerToast("Tên công việc không được để trống");
                return;
            }
            if (fEnd.getText().trim().isEmpty()) {
                frame.getInstance().triggerToast("Vui lòng nhập hạn hoàn thành");
                return;
            }
            frame.getInstance().closeOverlayModal();
            frame.getInstance().triggerToast(isEdit ? "Đã cập nhật công việc" : "Đã tạo công việc");
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().addAll(title, fields, actions);

        return box;
    }

    private StackPane createCancelModalDashboard(String workTitle) {
        StackPane rootModalPane = new StackPane();

        VBox box = new VBox(20);
        box.setPrefWidth(420);
        box.setMaxSize(420, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel("Hủy công việc", FontWeight.BLACK, 20, "#ef4444");
        Label sub = format.formatLabel("Hệ thống sẽ chuyển trạng thái công việc sang Đã hủy để bảo lưu lịch sử.", FontWeight.MEDIUM, 12, "#64748b");
        sub.setWrapText(true);

        VBox fields = new VBox(8);
        fields.getChildren().add(format.formatLabel("Lý do hủy *", FontWeight.BOLD, 12, "#94a3b8"));
        TextField fReason = format.formatTextField("Nhập lý do...");
        fields.getChildren().add(fReason);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);

        Button btnCancel = getShadowBtn("Đóng", "", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> frame.getInstance().closeOverlayModal());

        Button btnConfirm = getShadowBtn("Xác nhận", "", "#ef4444", "white", "rgba(239,68,68,0.4)");
        btnConfirm.setOnAction(e -> {
            if (fReason.getText().trim().isEmpty()) {
                frame.getInstance().triggerToast("Vui lòng nhập lý do hủy");
                return;
            }
            frame.getInstance().closeOverlayModal();
            frame.getInstance().triggerToast("Đã hủy công việc");
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().addAll(title, sub, fields, actions);

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