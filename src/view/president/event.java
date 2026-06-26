package view.president;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import view.format;
import view.president.frame;

public class event extends ScrollPane {

    private VBox dashboardContent;
    private VBox cardGrid;

    // Khởi tạo cấu trúc giao diện quản lý danh sách và các phiên sự kiện của câu lạc bộ
    public event() {
        dashboardContent = new VBox(32);
        dashboardContent.setPadding(new Insets(32));
        dashboardContent.setStyle("-fx-background-color: transparent;");

        HBox kpiRow = new HBox(16);
        kpiRow.getChildren().addAll(
                format.formatKPICard("Tổng sự kiện", "24", "#64748b", "#1e293b"),
                format.formatKPICard("🔵 Sắp diễn ra", "3", "#3b82f6", "#1e293b"),
                format.formatKPICard("🟡 Đang diễn ra", "1", "#f59e0b", "#1e293b"),
                format.formatKPICard("🟢 Đã kết thúc", "18", "#10b981", "#1e293b")
        );
        for (javafx.scene.Node node : kpiRow.getChildren()) { HBox.setHgrow(node, Priority.ALWAYS); }

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

        Button btnAdd = getShadowBtn("Tạo sự kiện", "➕", "#5020d8", "white", "rgba(80,32,216,0.4)");
        btnAdd.setOnAction(e -> {
            StackPane addModalRoot = new StackPane();
            VBox addForm = createEventFormModal("Tạo sự kiện mới", "", "", "", "", "", "Chưa phân công", "Sắp diễn ra", false, addModalRoot, null);
            addModalRoot.getChildren().add(addForm);
            frame.getInstance().showCustomModal(addModalRoot);
        });

        filterBar.getChildren().addAll(searchBox, cbStatus, cbTime, spacer, btnAdd);

        cardGrid = new VBox(24);

        addCardToGrid(cardGrid, createEventCard("Workshop GitHub", "28/06/2026", "08:00 - 11:30", "Hội trường A", "Trần Văn B", "Sắp diễn ra", 80));
        addCardToGrid(cardGrid, createEventCard("Họp toàn CLB", "26/06/2026", "19:00 - 21:00", "Google Meet (Online)", "Alexandra Đặng", "Đang diễn ra", 128));
        addCardToGrid(cardGrid, createEventCard("Team Building ĐN", "12/06/2026", "07:00 - 17:00", "Biển Mỹ Khê", "Lê Văn C", "Đã kết thúc", 100));
        addCardToGrid(cardGrid, createEventCard("Talkshow Hành trang", "05/05/2026", "18:00 - 20:30", "Phòng B302", "Chưa phân công", "Đã hủy", 0));

        dashboardContent.getChildren().addAll(kpiRow, filterBar, cardGrid);
        format.formatScrollbar(this, dashboardContent, 12);
        this.setContent(dashboardContent);
    }

    // Thực hiện phân bổ và sắp xếp các thẻ sự kiện tự động theo cấu trúc lưới hai cột
    private void addCardToGrid(VBox grid, VBox card) {
        if (grid.getChildren().isEmpty()) {
            HBox row = new HBox(24);
            row.getChildren().add(card);
            grid.getChildren().add(row);
        } else {
            HBox lastRow = (HBox) grid.getChildren().get(grid.getChildren().size() - 1);
            if (lastRow.getChildren().size() < 2) {
                lastRow.getChildren().add(card);
            } else {
                HBox newRow = new HBox(24);
                newRow.getChildren().add(card);
                grid.getChildren().add(newRow);
            }
        }
    }

    // Thiết lập giao diện hiển thị tóm tắt của một sự kiện bao gồm thông tin cơ bản và nút tác vụ hủy nhanh
    private VBox createEventCard(String title, String date, String time, String loc, String leader, String status, int count) {
        VBox box = new VBox(16);
        box.setPrefSize(470, 180);
        box.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        box.setMaxSize(470, 180);
        box.setPadding(new Insets(24));
        box.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 32px; -fx-border-color: rgba(255,255,255,0.9); -fx-border-width: 2px; -fx-border-radius: 32px; -fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.1), 15, 0, 0, 6);");

        HBox topRow = new HBox(12);
        topRow.setAlignment(Pos.CENTER_LEFT);
        Label lblTitle = format.formatLabel(title, FontWeight.BLACK, 20, "#1e293b");
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        String stBg = status.equals("Sắp diễn ra") ? "rgba(59,130,246,0.15)" : status.equals("Đang diễn ra") ? "rgba(245,158,11,0.15)" : status.equals("Đã kết thúc") ? "rgba(16,185,129,0.15)" : "rgba(239,68,68,0.15)";
        String stText = status.equals("Sắp diễn ra") ? "#3b82f6" : status.equals("Đang diễn ra") ? "#f59e0b" : status.equals("Đã kết thúc") ? "#10b981" : "#ef4444";
        Label statusBadge = format.formatBadge(status, stBg, stText);
        statusBadge.setStyle(statusBadge.getStyle() + "-fx-font-size: 12px; -fx-padding: 6 12 6 12;");
        topRow.getChildren().addAll(lblTitle, spacer, statusBadge);

        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(20); infoGrid.setVgap(8);
        infoGrid.add(format.formatLabel("📅 " + date + " | ⏰ " + time, FontWeight.BOLD, 13, "#475569"), 0, 0, 2, 1);
        infoGrid.add(format.formatLabel("📍 " + loc, FontWeight.BOLD, 13, "#475569"), 0, 1, 2, 1);

        HBox actionsRow = new HBox(12);
        actionsRow.setAlignment(Pos.CENTER_RIGHT);

        Button btnView = format.formatCircleBtn("👁️‍🗨️", "#448aff", "#7c4dff");
        btnView.setPrefSize(40, 40);
        btnView.setOnAction(e -> {
            StackPane detailModal = createDetailModal(title, date, time, loc, leader, status, count, "Đây là mô tả chi tiết của sự kiện...");
            frame.getInstance().showCustomModal(detailModal);
        });

        actionsRow.getChildren().add(btnView);

        if (!status.equals("Đã kết thúc") && !status.equals("Đã hủy")) {
            Button btnCancelEvent = format.formatCircleBtn("➖", "#ef4444", "#dc2626");
            btnCancelEvent.setPrefSize(40, 40);
            btnCancelEvent.setOnAction(e -> {
                StackPane cancelModal = createCancelModalDashboard(title);
                frame.getInstance().showCustomModal(cancelModal);
            });
            actionsRow.getChildren().add(btnCancelEvent);
        }

        box.getChildren().addAll(topRow, infoGrid, actionsRow);
        return box;
    }

    // Tạo cửa sổ modal chi tiết hiển thị toàn bộ nội dung mô tả, nhân sự và số liệu thống kê sự kiện
    private StackPane createDetailModal(String title, String date, String time, String loc, String leader, String status, int participants, String desc) {
        StackPane rootModalPane = new StackPane();

        VBox modalContent = new VBox(24);
        modalContent.setPrefSize(784, Region.USE_COMPUTED_SIZE);
        modalContent.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        modalContent.setMaxSize(784, Region.USE_PREF_SIZE);
        modalContent.setPadding(new Insets(32));
        modalContent.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        modalContent.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        Label lblTitle = format.formatLabel(title, FontWeight.BLACK, 26, "#1e293b");
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        String stBg = status.equals("Sắp diễn ra") ? "rgba(59,130,246,0.15)" : status.equals("Đang diễn ra") ? "rgba(245,158,11,0.15)" : status.equals("Đã kết thúc") ? "rgba(16,185,129,0.15)" : "rgba(239,68,68,0.15)";
        String stText = status.equals("Sắp diễn ra") ? "#3b82f6" : status.equals("Đang diễn ra") ? "#f59e0b" : status.equals("Đã kết thúc") ? "#10b981" : "#ef4444";
        Label statusBadge = format.formatBadge(status, stBg, stText);
        statusBadge.setStyle(statusBadge.getStyle() + "-fx-font-size: 14px; -fx-padding: 8 16;");
        header.getChildren().addAll(lblTitle, spacer, statusBadge);

        VBox infoBox = format.formatBoxCard();
        infoBox.getChildren().add(format.formatLabel("THÔNG TIN SỰ KIỆN", FontWeight.BLACK, 12, "#94a3b8"));

        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(40); infoGrid.setVgap(16);
        infoGrid.add(new VBox(4, format.formatLabel("Ngày tổ chức", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(date, FontWeight.BLACK, 15, "#1e293b")), 0, 0);
        infoGrid.add(new VBox(4, format.formatLabel("Thời gian", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(time, FontWeight.BLACK, 15, "#1e293b")), 1, 0);
        infoGrid.add(new VBox(4, format.formatLabel("Địa điểm", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(loc, FontWeight.BLACK, 15, "#1e293b")), 0, 1);

        Label lblLeader = format.formatLabel(leader, FontWeight.BLACK, 15, leader.equals("Chưa phân công") ? "#f59e0b" : "#7c4dff");
        infoGrid.add(new VBox(4, format.formatLabel("Người phụ trách", FontWeight.BOLD, 12, "#94a3b8"), lblLeader), 1, 1);

        Label lblDesc = format.formatLabel(desc, FontWeight.MEDIUM, 14, "#475569");
        lblDesc.setWrapText(true);
        infoGrid.add(new VBox(4, format.formatLabel("Mô tả", FontWeight.BOLD, 12, "#94a3b8"), lblDesc), 0, 2, 2, 1);
        infoBox.getChildren().add(infoGrid);

        HBox bottomView = new HBox(24);

        VBox leftCol = format.formatBoxCard();
        leftCol.setPrefWidth(380);
        leftCol.getChildren().add(format.formatLabel("THÀNH VIÊN THAM GIA", FontWeight.BLACK, 12, "#94a3b8"));

        VBox memberList = new VBox(8);
        if (participants > 0) {
            memberList.getChildren().addAll(
                    createSimpleMemberRow("Alexandra Đặng", "Hội trưởng"),
                    createSimpleMemberRow("Trần Văn B", "Thành viên"),
                    createSimpleMemberRow("Lê Văn C", "Thành viên")
            );
        } else {
            memberList.getChildren().add(format.formatLabel("Chưa có danh sách.", FontWeight.MEDIUM, 13, "#64748b"));
        }
        ScrollPane memberScroll = new ScrollPane(memberList);
        memberScroll.setPrefHeight(150);
        format.formatScrollbar(memberScroll, memberList, 8);
        leftCol.getChildren().add(memberScroll);

        VBox rightCol = format.formatBoxCard();
        rightCol.setPrefWidth(310);
        rightCol.getChildren().add(format.formatLabel("THỐNG KÊ", FontWeight.BLACK, 12, "#94a3b8"));

        VBox stats = new VBox(12);
        stats.getChildren().addAll(
                new HBox(12, format.formatLabel("Tổng người tham gia:", FontWeight.BOLD, 13, "#64748b"), format.formatLabel(String.valueOf(participants), FontWeight.BLACK, 14, "#1e293b")),
                new HBox(12, format.formatLabel("Đã phân công việc:", FontWeight.BOLD, 13, "#64748b"), format.formatLabel("12", FontWeight.BLACK, 14, "#10b981")),
                new HBox(12, format.formatLabel("Chưa phân công:", FontWeight.BOLD, 13, "#64748b"), format.formatLabel("5", FontWeight.BLACK, 14, "#f59e0b"))
        );
        rightCol.getChildren().add(stats);
        bottomView.getChildren().addAll(leftCol, rightCol);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnClose = getShadowBtn("Đóng", "", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnClose.setOnAction(e -> frame.getInstance().closeOverlayModal());
        actions.getChildren().add(btnClose);

        if (!status.equals("Đã kết thúc") && !status.equals("Đã hủy")) {
            Button btnEdit = getShadowBtn("Sửa", "", "#5020d8", "white", "rgba(80,32,216,0.4)");
            btnEdit.setOnAction(e -> {
                VBox editModal = createEventFormModal("Sửa Sự kiện", title, desc, date, time, loc, leader, status, true, rootModalPane, modalContent);
                rootModalPane.getChildren().setAll(editModal);
            });
            actions.getChildren().add(btnEdit);
        }

        modalContent.getChildren().addAll(header, infoBox, bottomView, actions);
        rootModalPane.getChildren().add(modalContent);
        return rootModalPane;
    }

    // Thiết lập một hàng hiển thị thông tin rút gọn bao gồm họ tên và vai trò thành viên tham gia sự kiện
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

    // Tạo cấu trúc biểu mẫu nhập liệu dạng modal dùng để khởi tạo mới hoặc cập nhật thông số sự kiện
    private VBox createEventFormModal(String titleText, String initTitle, String initDesc, String initDate, String initTime, String initLoc, String initLeader, String status, boolean isEdit, StackPane rootModalPane, VBox previousView) {
        VBox box = new VBox(20);
        box.setPrefWidth(540);
        box.setMaxSize(540, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel(titleText, FontWeight.BLACK, 24, "#1e293b");

        VBox fields = new VBox(16);

        TextField fName = format.formatTextField("Ví dụ: Workshop GitHub"); fName.setText(initTitle);
        VBox nameGroup = new VBox(6, format.formatLabel("Tên sự kiện *", FontWeight.BOLD, 12, "#94a3b8"), fName);

        TextField fDesc = format.formatTextField("Ví dụ: Hướng dẫn sử dụng Git..."); fDesc.setText(initDesc);
        VBox descGroup = new VBox(6, format.formatLabel("Mô tả sự kiện", FontWeight.BOLD, 12, "#94a3b8"), fDesc);

        TextField fDate = format.formatTextField("dd/MM/yyyy"); fDate.setText(initDate);
        VBox dateGroup = new VBox(6, format.formatLabel("Ngày tổ chức *", FontWeight.BOLD, 12, "#94a3b8"), fDate);
        HBox.setHgrow(dateGroup, Priority.ALWAYS);

        TextField fTime = format.formatTextField("VD: 08:00 - 11:30"); fTime.setText(initTime);
        VBox timeGroup = new VBox(6, format.formatLabel("Giờ *", FontWeight.BOLD, 12, "#94a3b8"), fTime);
        HBox.setHgrow(timeGroup, Priority.ALWAYS);

        HBox timeRow = new HBox(16, dateGroup, timeGroup);

        TextField fLoc = format.formatTextField("Ví dụ: Hội trường A"); fLoc.setText(initLoc);
        VBox locGroup = new VBox(6, format.formatLabel("Địa điểm *", FontWeight.BOLD, 12, "#94a3b8"), fLoc);

        ComboBox<String> cbLeader = format.formatSortBtn("Người phụ trách", "Chưa phân công", "Nguyễn Văn A", "Trần Văn B", "Lê Văn C");
        cbLeader.setValue(initLeader); cbLeader.setPrefWidth(Double.MAX_VALUE);
        VBox leaderGroup = new VBox(6, format.formatLabel("Người phụ trách", FontWeight.BOLD, 12, "#94a3b8"), cbLeader);

        CheckBox chkAll = format.formatCheckBox("Toàn CLB");
        VBox chkGroup = new VBox(6, format.formatLabel("Thành phần tham gia", FontWeight.BOLD, 12, "#94a3b8"), chkAll);
        chkGroup.setAlignment(Pos.CENTER_LEFT);

        TextField fLimit = format.formatTextField("Ví dụ: 80");
        VBox limitGroup = new VBox(6, format.formatLabel("Giới hạn số lượng (Bỏ trống = Tự do)", FontWeight.BOLD, 12, "#94a3b8"), fLimit);
        HBox.setHgrow(limitGroup, Priority.ALWAYS);

        HBox partRow = new HBox(24, chkGroup, limitGroup);
        partRow.setAlignment(Pos.CENTER_LEFT);

        if (isEdit && status.equals("Đang diễn ra")) {
            fDate.setDisable(true); fTime.setDisable(true); fLoc.setDisable(true);
            fields.getChildren().add(format.formatLabel("Sự kiện đang diễn ra. Chỉ được sửa Mô tả & Phụ trách.", FontWeight.BOLD, 12, "#f59e0b"));
        }

        fields.getChildren().addAll(nameGroup, descGroup, timeRow, locGroup, leaderGroup, partRow);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnCancel = getShadowBtn(isEdit ? "Quay lại" : "Huỷ", "", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> {
            if (!isEdit) frame.getInstance().closeOverlayModal();
            else rootModalPane.getChildren().setAll(previousView);
        });

        Button btnConfirm = getShadowBtn("Hoàn thành", "", "#5020d8", "white", "rgba(80,32,216,0.4)");
        btnConfirm.setOnAction(e -> {
            frame.getInstance().closeOverlayModal();
            frame.getInstance().triggerToast(isEdit ? "Tạo sự kiện thành công!" : "Tạo sự kiện thành công!");
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().addAll(title, fields, actions);

        return box;
    }

    // Tạo cửa sổ modal yêu cầu nhập lý do bắt buộc để phục vụ việc xác nhận hủy bỏ phiên sự kiện
    private StackPane createCancelModalDashboard(String titleEvent) {
        StackPane rootModalPane = new StackPane();

        VBox box = new VBox(20);
        box.setPrefWidth(420);
        box.setMaxSize(420, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel("Xác nhận Hủy Sự kiện", FontWeight.BLACK, 20, "#ef4444");
        Label sub = format.formatLabel("Sự kiện [" + titleEvent + "] sẽ chuyển sang trạng thái Đã hủy để bảo lưu lịch sử, không xóa khỏi hệ thống.", FontWeight.MEDIUM, 12, "#64748b");
        sub.setWrapText(true);

        VBox fields = new VBox(8);
        fields.getChildren().add(format.formatLabel("Lý do hủy (Bắt buộc)", FontWeight.BOLD, 12, "#94a3b8"));
        TextField fReason = format.formatTextField("Nhập lý do hủy...");
        fields.getChildren().add(fReason);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnCancel = getShadowBtn("Đóng", "", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> frame.getInstance().closeOverlayModal());

        Button btnConfirm = getShadowBtn("Xác nhận Hủy", "", "#ef4444", "white", "rgba(239,68,68,0.4)");
        btnConfirm.setOnAction(e -> {
            frame.getInstance().closeOverlayModal();
            frame.getInstance().triggerToast("Đã hủy sự kiện [" + titleEvent + "]!");
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().addAll(title, sub, fields, actions);

        rootModalPane.getChildren().add(box);
        return rootModalPane;
    }

    // Thiết lập thông số đồ họa, văn bản hiển thị và hiệu ứng co dãn chuyển động cho nút bấm có đổ bóng
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