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
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import view.format;

public class event extends ScrollPane {

    private VBox dashboardContent;

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

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnAdd = getShadowBtn("Tạo sự kiện", "", "#5020d8", "white", "rgba(80,32,216,0.4)");
        btnAdd.setOnAction(e -> {
            StackPane addModalRoot = new StackPane();
            VBox addForm = createEventFormModal("Tạo sự kiện", "", "", "", "", "", "Chưa phân công", "Sắp diễn ra", false, false, addModalRoot, null);
            addModalRoot.getChildren().add(addForm);
            frame.getInstance().showCustomModal(addModalRoot);
        });

        filterBar.getChildren().addAll(searchBox, cbStatus, cbTime, spacer, btnAdd);

        VBox eventTableContainer = format.formatTableContainer();
        Label tableTitle = format.formatLabel("DANH SÁCH SỰ KIỆN", FontWeight.BLACK, 14, "#1e293b");
        tableTitle.setPadding(new Insets(12, 16, 0, 16));

        eventTableContainer.getChildren().addAll(tableTitle, createEventTableHeader());

        VBox eventRows = new VBox(4);
        eventRows.getChildren().addAll(
                createEventRow("Tech Share #01: Git & GitHub", "28/06/2026", "08:00 - 11:30", "Phòng 202", "Kỹ thuật", "Sắp diễn ra", 80, false, ""),
                createEventRow("Networking Day", "26/06/2026", "19:00 - 21:00", "Online", "Nội dung", "Đang diễn ra", 128, true, ""),
                createEventRow("Team Building", "12/06/2026", "07:00 - 17:00", "Biển Mỹ Khê", "Hậu cần", "Đã kết thúc", 100, false, ""),
                createEventRow("Talkshow Kỹ năng", "05/05/2026", "18:00 - 20:30", "Hội trường A", "Chưa phân công", "Đã hủy", 0, false, "Trùng lịch thi của đa số sinh viên và không thuê được địa điểm.")
        );

        ScrollPane eventScroll = new ScrollPane(eventRows);
        eventScroll.setPrefHeight(350);
        format.formatScrollbar(eventScroll, eventRows, 8);
        applySmoothScroll(eventScroll, eventRows);

        eventTableContainer.getChildren().add(eventScroll);

        dashboardContent.getChildren().addAll(kpiRow, filterBar, eventTableContainer);
        format.formatScrollbar(this, dashboardContent, 12);
        this.setContent(dashboardContent);
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

    private HBox createEventTableHeader() {
        HBox header = new HBox(16);
        header.setPadding(new Insets(12, 16, 12, 16));
        header.setStyle("-fx-border-color: transparent transparent rgba(255,255,255,0.4) transparent; -fx-border-width: 1px;");

        Label l1 = format.formatLabel("SỰ KIỆN", FontWeight.BLACK, 10, "#94a3b8"); l1.setPrefWidth(220);
        Label l2 = format.formatLabel("THỜI GIAN", FontWeight.BLACK, 10, "#94a3b8"); l2.setPrefWidth(160);
        Label l3 = format.formatLabel("ĐỊA ĐIỂM", FontWeight.BLACK, 10, "#94a3b8"); l3.setPrefWidth(160);
        Label l4 = format.formatLabel("BỘ PHẬN", FontWeight.BLACK, 10, "#94a3b8"); l4.setPrefWidth(110);
        Label l5 = format.formatLabel("THAM GIA", FontWeight.BLACK, 10, "#94a3b8"); l5.setPrefWidth(120);
        Label l6 = format.formatLabel("", FontWeight.BLACK, 10, "#94a3b8"); l6.setPrefWidth(90);

        header.getChildren().addAll(l1, l2, l3, l4, l5, l6);
        return header;
    }

    private HBox createEventRow(String title, String date, String time, String loc, String leader, String status, int count, boolean isAllClub, String cancelReason) {
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
            StackPane detailModal = createDetailModal(title, date, time, loc, leader, status, count, isAllClub, "Thông tin chi tiết về sự kiện nội bộ.", cancelReason);
            frame.getInstance().showCustomModal(detailModal);
        });

        actionBox.getChildren().add(btnView);

        if (!status.equals("Đã kết thúc") && !status.equals("Đã hủy")) {
            Button btnCancelEvent = format.formatCircleBtn("➖", "#ef4444", "#dc2626");
            btnCancelEvent.setPrefSize(36, 36);
            btnCancelEvent.setOnAction(e -> {
                StackPane cancelModal = createCancelModalDashboard(title);
                frame.getInstance().showCustomModal(cancelModal);
            });
            actionBox.getChildren().add(btnCancelEvent);
        }

        row.getChildren().addAll(titleLayout, lblTime, lblLoc, lblLeader, lblCount, actionBox);
        return row;
    }

    private StackPane createDetailModal(String title, String date, String time, String loc, String leader, String status, int participants, boolean isAllClub, String desc, String cancelReason) {
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

        if (status.equals("Đã hủy") && cancelReason != null && !cancelReason.isEmpty()) {
            VBox cancelBox = new VBox(6);
            cancelBox.setPadding(new Insets(12));
            cancelBox.setStyle("-fx-background-color: rgba(239, 68, 68, 0.1); -fx-background-radius: 12px; -fx-border-color: rgba(239, 68, 68, 0.3); -fx-border-width: 1px; -fx-border-radius: 12px;");
            Label lblReasonTitle = format.formatLabel("LÝ DO HỦY", FontWeight.BLACK, 11, "#ef4444");
            Label lblReasonVal = format.formatLabel(cancelReason, FontWeight.MEDIUM, 13, "#dc2626");
            lblReasonVal.setWrapText(true);
            cancelBox.getChildren().addAll(lblReasonTitle, lblReasonVal);
            infoGrid.add(cancelBox, 0, 4, 2, 1);
        }

        infoBox.getChildren().add(infoGrid);

        VBox memberBox = format.formatBoxCard();
        memberBox.setPrefWidth(370);
        memberBox.getChildren().add(format.formatLabel(isAllClub ? "THÀNH VIÊN THAM GIA" : "THÀNH VIÊN THAM GIA (" + participants + ")", FontWeight.BLACK, 12, "#94a3b8"));

        if (isAllClub) {
            VBox noticeBox = new VBox();
            noticeBox.setAlignment(Pos.CENTER);
            noticeBox.setPrefHeight(250);
            Label notice = format.formatLabel("Toàn bộ CLB đều phải tham gia.", FontWeight.MEDIUM, 14, "#64748b");
            notice.setWrapText(true);
            noticeBox.getChildren().add(notice);
            memberBox.getChildren().add(noticeBox);
        } else {
            VBox memberList = new VBox(8);
            if (participants > 0) {
                memberList.getChildren().addAll(
                        createSimpleMemberRow("Nguyễn Minh Quân", "Quản lý"),
                        createSimpleMemberRow("Trần Văn B", "Thành viên"),
                        createSimpleMemberRow("Lê Văn C", "Thành viên"),
                        createSimpleMemberRow("Phạm Thị D", "Thành viên"),
                        createSimpleMemberRow("Hoàng Minh E", "Thành viên"),
                        createSimpleMemberRow("Vũ Tuấn F", "Thành viên")
                );
            } else {
                memberList.getChildren().add(format.formatLabel("Chưa có danh sách.", FontWeight.MEDIUM, 13, "#64748b"));
            }

            ScrollPane memberScroll = new ScrollPane(memberList);
            memberScroll.setPrefHeight(250);
            format.formatScrollbar(memberScroll, memberList, 8);
            applySmoothScroll(memberScroll, memberList);

            memberBox.getChildren().add(memberScroll);
        }

        bodyRow.getChildren().addAll(infoBox, memberBox);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);

        Button btnClose = getModalActionBtn("Đóng", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnClose.setOnAction(e -> frame.getInstance().closeOverlayModal());
        actions.getChildren().add(btnClose);

        if (!status.equals("Đã kết thúc") && !status.equals("Đã hủy")) {
            Button btnEdit = getModalActionBtn("Sửa", "#5020d8", "white", "rgba(80,32,216,0.4)");
            btnEdit.setOnAction(e -> {
                VBox editModal = createEventFormModal("Sửa sự kiện", title, desc, date, time, loc, leader, status, true, isAllClub, rootModalPane, modalContent);
                rootModalPane.getChildren().setAll(editModal);
            });
            actions.getChildren().add(btnEdit);
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

        row.getChildren().addAll(lblName, spacer, format.formatLabel(role, FontWeight.MEDIUM, 11, "#94a3b8"));
        return row;
    }

    private VBox createEventFormModal(String titleText, String initTitle, String initDesc, String initDate, String initTime, String initLoc, String initLeader, String status, boolean isEdit, boolean isAllClub, StackPane rootModalPane, VBox previousView) {
        VBox box = new VBox(20);
        box.setPrefWidth(540);
        box.setMaxSize(540, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel(titleText, FontWeight.BLACK, 24, "#1e293b");

        VBox fields = new VBox(16);

        TextField fName = format.formatTextField("Nhập tên..."); fName.setText(initTitle);
        VBox nameGroup = new VBox(6, format.formatLabel("Tên sự kiện", FontWeight.BOLD, 12, "#94a3b8"), fName);

        TextField fDesc = format.formatTextField("Nhập mô tả..."); fDesc.setText(initDesc);
        VBox descGroup = new VBox(6, format.formatLabel("Mô tả", FontWeight.BOLD, 12, "#94a3b8"), fDesc);

        TextField fDate = format.formatTextField("dd/MM/yyyy"); fDate.setText(initDate);
        VBox dateGroup = new VBox(6, format.formatLabel("Ngày tổ chức", FontWeight.BOLD, 12, "#94a3b8"), fDate);
        HBox.setHgrow(dateGroup, Priority.ALWAYS);

        TextField fTime = format.formatTextField("HH:mm - HH:mm"); fTime.setText(initTime);
        VBox timeGroup = new VBox(6, format.formatLabel("Thời gian", FontWeight.BOLD, 12, "#94a3b8"), fTime);
        HBox.setHgrow(timeGroup, Priority.ALWAYS);

        HBox timeRow = new HBox(16, dateGroup, timeGroup);

        TextField fLoc = format.formatTextField("Nhập địa điểm..."); fLoc.setText(initLoc);
        VBox locGroup = new VBox(6, format.formatLabel("Địa điểm", FontWeight.BOLD, 12, "#94a3b8"), fLoc);

        ComboBox<String> cbDept = format.formatSortBtn("Bộ phận phụ trách", "Chưa phân công", "Nội dung", "Kỹ thuật", "Truền thông", "Hậu cần");
        cbDept.setValue(initLeader.isEmpty() ? "Chưa phân công" : initLeader);
        cbDept.setPrefWidth(Double.MAX_VALUE);
        cbDept.setStyle("-fx-background-color: rgba(59,130,246,0.15); -fx-background-radius: 20px; -fx-padding: 4 12; -fx-font-family: 'Google Sans'; -fx-font-weight: bold; -fx-text-fill: #3b82f6; -fx-cursor: hand;");

        VBox leaderGroup = new VBox(6, format.formatLabel("Bộ phận phụ trách", FontWeight.BOLD, 12, "#94a3b8"), cbDept);

        CheckBox chkAll = format.formatCheckBox("Toàn CLB");
        chkAll.setSelected(isAllClub);
        VBox chkGroup = new VBox(6, format.formatLabel("Thành phần tham gia", FontWeight.BOLD, 12, "#94a3b8"), chkAll);
        chkGroup.setAlignment(Pos.CENTER_LEFT);

        HBox partRow = new HBox(24, chkGroup);
        partRow.setAlignment(Pos.CENTER_LEFT);

        if (isEdit && status.equals("Đang diễn ra")) {
            fDate.setDisable(true); fTime.setDisable(true); fLoc.setDisable(true);
            fields.getChildren().add(format.formatLabel("Sự kiện đang diễn ra. Chỉ được sửa mô tả và bộ phận phụ trách.", FontWeight.BOLD, 12, "#f59e0b"));
        }

        fields.getChildren().addAll(nameGroup, descGroup, timeRow, locGroup, leaderGroup, partRow);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(16, 0, 0, 0));

        Button btnCancel = getModalActionBtn(isEdit ? "Quay lại" : "Huỷ", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> {
            if (!isEdit) frame.getInstance().closeOverlayModal();
            else rootModalPane.getChildren().setAll(previousView);
        });

        Button btnConfirm = getModalActionBtn("Xác nhận", "#5020d8", "white", "rgba(80,32,216,0.4)");
        btnConfirm.setOnAction(e -> {
            frame.getInstance().closeOverlayModal();
            frame.getInstance().triggerToast(isEdit ? "Đã cập nhật sự kiện" : "Đã tạo sự kiện");
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().addAll(title, fields, actions);

        return box;
    }

    private StackPane createCancelModalDashboard(String titleEvent) {
        StackPane rootModalPane = new StackPane();

        VBox box = new VBox(20);
        box.setPrefWidth(420);
        box.setMaxSize(420, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel("Hủy sự kiện", FontWeight.BLACK, 20, "#ef4444");

        Label sub = format.formatLabel("Hệ thống sẽ chuyển trạng thái sự kiện sang Đã hủy để bảo lưu lịch sử.", FontWeight.MEDIUM, 12, "#64748b");
        sub.setWrapText(true);

        VBox fields = new VBox(8);
        fields.getChildren().add(format.formatLabel("Lý do hủy", FontWeight.BOLD, 12, "#94a3b8"));

        TextField fReason = format.formatTextField("Nhập lý do...");
        fields.getChildren().add(fReason);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(16, 0, 0, 0));

        Button btnCancel = getModalActionBtn("Đóng", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> frame.getInstance().closeOverlayModal());

        Button btnConfirm = getModalActionBtn("Xác nhận", "#ef4444", "white", "rgba(239,68,68,0.4)");
        btnConfirm.setOnAction(e -> {
            if (fReason.getText().trim().isEmpty()) {
                frame.getInstance().triggerToast("Vui lòng nhập lý do hủy sự kiện");
                return;
            }
            if (titleEvent.equals("Tech Share #01: Git & GitHub") || titleEvent.equals("Networking Day")) {
                frame.getInstance().triggerToast("Không thể hủy! Sự kiện đang có công việc liên kết.");
                return;
            }
            frame.getInstance().closeOverlayModal();
            frame.getInstance().triggerToast("Đã hủy sự kiện");
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