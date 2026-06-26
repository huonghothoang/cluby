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

public class work extends ScrollPane {

    private VBox dashboardContent;
    private VBox cardGrid;

    // Khởi tạo giao diện chính trang quản lý công việc và các bộ lọc tìm kiếm dữ liệu kpi
    public work() {
        dashboardContent = new VBox(32);
        dashboardContent.setPadding(new Insets(32));
        dashboardContent.setStyle("-fx-background-color: transparent;");

        // === 1. THẺ KPI ===
        HBox kpiRow = new HBox(16);
        kpiRow.getChildren().addAll(
                format.formatKPICard("Tổng công việc", "42", "#64748b", "#1e293b"),
                format.formatKPICard("🔵 Đang thực hiện", "15", "#3b82f6", "#1e293b"),
                format.formatKPICard("🟢 Đã hoàn thành", "24", "#10b981", "#1e293b"),
                format.formatKPICard("🔴 Quá hạn", "3", "#ef4444", "#ef4444") // Highlight đỏ
        );
        for (javafx.scene.Node node : kpiRow.getChildren()) { HBox.setHgrow(node, Priority.ALWAYS); }

        // === 2. THANH CÔNG CỤ TÌM KIẾM & LỌC ===
        HBox filterBar = new HBox(16);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(12, 24, 12, 24));
        format.formatGlass(filterBar, 40, 0.4);

        HBox searchBox = new HBox(8);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = format.formatTextField("Tìm tên công việc...");
        Button btnSearch = format.formatFindBtn();
        searchBox.getChildren().addAll(searchField, btnSearch);

        ComboBox<String> cbStatus = format.formatSortBtn("Lọc trạng thái", "Tất cả", "Chưa bắt đầu", "Đang thực hiện", "Hoàn thành", "Đã hủy");
        ComboBox<String> cbEvent = format.formatSortBtn("Lọc sự kiện", "Tất cả", "Không thuộc sự kiện", "Workshop GitHub", "Team Building");
        ComboBox<String> cbDept = format.formatSortBtn("Lọc ban phụ trách", "Tất cả", "Ban Truyền thông", "Ban Kỹ thuật", "Ban Sự kiện", "Ban Nhân sự");

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnAdd = getShadowBtn("Tạo công việc", "➕", "#5020d8", "white", "rgba(80,32,216,0.4)");
        btnAdd.setOnAction(e -> {
            StackPane addModalRoot = new StackPane();
            VBox addForm = createWorkFormModal("Tạo công việc mới", "", "", "Không thuộc sự kiện", "Chưa phân công", "26/06/2026", "28/06/2026", "Trung bình", "Chưa bắt đầu", false, addModalRoot, null);
            addModalRoot.getChildren().add(addForm);
            frame.getInstance().showCustomModal(addModalRoot);
        });

        filterBar.getChildren().addAll(searchBox, cbStatus, cbEvent, cbDept, spacer, btnAdd);

        // === 3. LƯỚI DANH SÁCH CARD CÔNG VIỆC ===
        cardGrid = new VBox(24);

        // Mock Data - Thể hiện rõ các ràng buộc nghiệp vụ
        addCardToGrid(cardGrid, createWorkCard("Thiết kế Poster Workshop", "Workshop GitHub", "Ban Truyền thông", "28/06/2026", "Đang thực hiện", "Cao", false));
        addCardToGrid(cardGrid, createWorkCard("Liên hệ nhà tài trợ", "Không thuộc sự kiện", "Ban Đối ngoại", "20/06/2026", "Đang thực hiện", "Cao", true)); // Quá hạn
        addCardToGrid(cardGrid, createWorkCard("Chuẩn bị máy chiếu, mic", "Workshop GitHub", "Ban Kỹ thuật", "27/06/2026", "Hoàn thành", "Trung bình", false));
        addCardToGrid(cardGrid, createWorkCard("Mua nước uống, bánh kẹo", "Team Building ĐN", "Ban Hậu cần", "10/07/2026", "Đã hủy", "Thấp", false));

        dashboardContent.getChildren().addAll(kpiRow, filterBar, cardGrid);
        format.formatScrollbar(this, dashboardContent, 12);
        this.setContent(dashboardContent);
    }

    // Thực hiện sắp xếp tự động các thẻ công việc theo cấu trúc lưới hai cột song song
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

    // Thiết lập giao diện hiển thị tóm tắt của một đầu việc kèm cảnh báo quá hạn động và nút tác vụ hủy nhanh
    private VBox createWorkCard(String title, String event, String dept, String deadline, String status, String priority, boolean isOverdue) {
        VBox box = new VBox(16);
        box.setPrefSize(470, 180);
        box.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        box.setMaxSize(470, 180);
        box.setPadding(new Insets(24));
        box.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 32px; -fx-border-color: rgba(255,255,255,0.9); -fx-border-width: 2px; -fx-border-radius: 32px; -fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.1), 15, 0, 0, 6);");

        HBox topRow = new HBox(12);
        topRow.setAlignment(Pos.CENTER_LEFT);
        Label lblTitle = format.formatLabel(title, FontWeight.BLACK, 18, "#1e293b");
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        // Trạng thái cơ bản
        String stBg = status.equals("Chưa bắt đầu") ? "rgba(100,116,139,0.15)" : status.equals("Đang thực hiện") ? "rgba(59,130,246,0.15)" : status.equals("Hoàn thành") ? "rgba(16,185,129,0.15)" : "rgba(239,68,68,0.15)";
        String stText = status.equals("Chưa bắt đầu") ? "#64748b" : status.equals("Đang thực hiện") ? "#3b82f6" : status.equals("Hoàn thành") ? "#10b981" : "#ef4444";
        HBox badgeBox = new HBox(8);
        badgeBox.setAlignment(Pos.CENTER_RIGHT);
        Label statusBadge = format.formatBadge(status, stBg, stText);
        statusBadge.setStyle(statusBadge.getStyle() + "-fx-font-size: 11px; -fx-padding: 4 10;");
        badgeBox.getChildren().add(statusBadge);

        // LOGIC NGHIỆP VỤ: Cảnh báo Quá hạn động
        if (isOverdue && !status.equals("Hoàn thành") && !status.equals("Đã hủy")) {
            Label overdueBadge = format.formatBadge("⚠ Quá hạn", "rgba(239,68,68,0.15)", "#ef4444");
            overdueBadge.setStyle(overdueBadge.getStyle() + "-fx-font-size: 11px; -fx-padding: 4 10;");
            badgeBox.getChildren().add(0, overdueBadge);
        }

        topRow.getChildren().addAll(lblTitle, spacer, badgeBox);

        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(20); infoGrid.setVgap(8);

        String eventLabel = event.equals("Không thuộc sự kiện") ? "🏢 Việc chung CLB" : "📅 " + event;
        infoGrid.add(format.formatLabel(eventLabel, FontWeight.BOLD, 12, event.equals("Không thuộc sự kiện") ? "#94a3b8" : "#7c4dff"), 0, 0, 2, 1);
        infoGrid.add(format.formatLabel("👥 Phụ trách: " + dept, FontWeight.BOLD, 13, "#475569"), 0, 1);
        infoGrid.add(format.formatLabel("⏳ Hạn: " + deadline, FontWeight.BOLD, 13, isOverdue ? "#ef4444" : "#475569"), 1, 1);

        HBox actionsRow = new HBox(12);
        actionsRow.setAlignment(Pos.CENTER_RIGHT);

        Button btnView = format.formatCircleBtn("👁️‍🗨️", "#448aff", "#7c4dff");
        btnView.setPrefSize(40, 40);
        btnView.setOnAction(e -> {
            StackPane detailModal = createDetailModal(title, "Mô tả chi tiết công việc...", event, dept, "24/06/2026", deadline, priority, status, isOverdue);
            frame.getInstance().showCustomModal(detailModal);
        });
        actionsRow.getChildren().add(btnView);

        // NẾU CÔNG VIỆC CHƯA XONG HOẶC HỦY -> HIỆN NÚT HỦY NGAY TRÊN CARD
        if (!status.equals("Hoàn thành") && !status.equals("Đã hủy")) {
            Button btnCancelWork = format.formatCircleBtn("➖", "#ef4444", "#dc2626");
            btnCancelWork.setPrefSize(40, 40);
            btnCancelWork.setOnAction(e -> {
                StackPane cancelModal = createCancelModalDashboard(title);
                frame.getInstance().showCustomModal(cancelModal);
            });
            actionsRow.getChildren().add(btnCancelWork);
        }

        box.getChildren().addAll(topRow, infoGrid, actionsRow);
        return box;
    }

    // Tạo cửa sổ modal chi tiết hiển thị đầy đủ thuộc tính công việc, ban chịu trách nhiệm và nút liên kết biểu mẫu sửa
    private StackPane createDetailModal(String title, String desc, String event, String dept, String startDate, String deadline, String priority, String status, boolean isOverdue) {
        StackPane rootModalPane = new StackPane();

        VBox modalContent = new VBox(24);
        modalContent.setPrefSize(784, Region.USE_COMPUTED_SIZE);
        modalContent.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        modalContent.setMaxSize(784, Region.USE_PREF_SIZE);
        modalContent.setPadding(new Insets(32));
        modalContent.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        modalContent.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        // 1. Header
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

        String stBg = status.equals("Chưa bắt đầu") ? "rgba(100,116,139,0.15)" : status.equals("Đang thực hiện") ? "rgba(59,130,246,0.15)" : status.equals("Hoàn thành") ? "rgba(16,185,129,0.15)" : "rgba(239,68,68,0.15)";
        String stText = status.equals("Chưa bắt đầu") ? "#64748b" : status.equals("Đang thực hiện") ? "#3b82f6" : status.equals("Hoàn thành") ? "#10b981" : "#ef4444";
        Label statusBadge = format.formatBadge(status, stBg, stText);
        statusBadge.setStyle(statusBadge.getStyle() + "-fx-font-size: 14px; -fx-padding: 6 16;");
        badges.getChildren().add(statusBadge);

        header.getChildren().addAll(lblTitle, spacer, badges);

        // 2. Thông tin cơ bản
        VBox infoBox = format.formatBoxCard();
        infoBox.getChildren().add(format.formatLabel("THÔNG TIN CÔNG VIỆC", FontWeight.BLACK, 12, "#94a3b8"));

        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(40); infoGrid.setVgap(16);

        String eventLabel = event.equals("Không thuộc sự kiện") ? "🏢 Việc chung CLB" : "📅 " + event;
        infoGrid.add(new VBox(4, format.formatLabel("Liên kết sự kiện", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(eventLabel, FontWeight.BOLD, 14, event.equals("Không thuộc sự kiện") ? "#64748b" : "#7c4dff")), 0, 0, 2, 1);

        infoGrid.add(new VBox(4, format.formatLabel("Ngày bắt đầu", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(startDate, FontWeight.BLACK, 14, "#1e293b")), 0, 1);
        infoGrid.add(new VBox(4, format.formatLabel("Hạn hoàn thành", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(deadline, FontWeight.BLACK, 14, isOverdue ? "#ef4444" : "#1e293b")), 1, 1);

        String prioColor = priority.equals("Cao") ? "#ef4444" : priority.equals("Trung bình") ? "#f59e0b" : "#10b981";
        infoGrid.add(new VBox(4, format.formatLabel("Mức độ ưu tiên", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(priority, FontWeight.BOLD, 14, prioColor)), 2, 1);

        Label lblDesc = format.formatLabel(desc, FontWeight.MEDIUM, 14, "#475569");
        lblDesc.setWrapText(true);
        infoGrid.add(new VBox(4, format.formatLabel("Mô tả chi tiết", FontWeight.BOLD, 12, "#94a3b8"), lblDesc), 0, 2, 3, 1);
        infoBox.getChildren().add(infoGrid);

        // 3. Nhân sự (Đã xóa người tham gia tùy chọn, giao toàn quyền cho Ban)
        VBox hrBox = format.formatBoxCard();
        hrBox.getChildren().addAll(
                format.formatLabel("BAN PHỤ TRÁCH", FontWeight.BLACK, 12, "#94a3b8"),
                format.formatLabel("🏢 " + dept, FontWeight.BLACK, 16, "#5020d8"),
                format.formatLabel("Trưởng ban sẽ chịu trách nhiệm phân bổ công việc chi tiết cho các thành viên trong ban.", FontWeight.MEDIUM, 12, "#64748b")
        );

        // 4. Các nút Hành động
        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnClose = getShadowBtn("Đóng", "", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnClose.setOnAction(e -> frame.getInstance().closeOverlayModal());
        actions.getChildren().add(btnClose);

        // LOGIC NGHIỆP VỤ: Đã hủy -> Chỉ đọc
        if (!status.equals("Đã hủy")) {
            Button btnEdit = getShadowBtn("Sửa công việc", "✏️", "#5020d8", "white", "rgba(80,32,216,0.4)");
            btnEdit.setOnAction(e -> {
                VBox editModal = createWorkFormModal("Sửa Công việc", title, desc, event, dept, startDate, deadline, priority, status, true, rootModalPane, modalContent);
                rootModalPane.getChildren().setAll(editModal);
            });
            actions.getChildren().add(btnEdit);
        }

        modalContent.getChildren().addAll(header, infoBox, hrBox, actions);
        rootModalPane.getChildren().add(modalContent);
        return rootModalPane;
    }

    // Tạo cấu trúc biểu mẫu nhập liệu dạng bổ sung trường ranh giới kiểm tra rỗng phục vụ thêm mới hoặc sửa đổi nội dung việc
    private VBox createWorkFormModal(String titleText, String initTitle, String initDesc, String initEvent, String initDept, String initStart, String initEnd, String initPrio, String status, boolean isEdit, StackPane rootModalPane, VBox previousView) {
        VBox box = new VBox(20);
        box.setPrefWidth(540);
        box.setMaxSize(540, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel(titleText, FontWeight.BLACK, 24, "#1e293b");

        VBox fields = new VBox(16);

        // --- 1. Thông tin cơ bản ---
        TextField fName = format.formatTextField("Ví dụ: Thiết kế Poster Workshop"); fName.setText(initTitle);
        VBox nameGroup = new VBox(6, format.formatLabel("Tên công việc *", FontWeight.BOLD, 12, "#94a3b8"), fName);

        TextField fDesc = format.formatTextField("Ví dụ: Kích thước 1920x1080..."); fDesc.setText(initDesc);
        VBox descGroup = new VBox(6, format.formatLabel("Mô tả công việc", FontWeight.BOLD, 12, "#94a3b8"), fDesc);

        // --- 2. Liên kết Sự kiện ---
        ComboBox<String> cbEvent = format.formatSortBtn("Chọn Sự kiện", "Không thuộc sự kiện", "Workshop GitHub", "Team Building ĐN", "Họp toàn CLB");
        cbEvent.setValue(initEvent.isEmpty() ? "Không thuộc sự kiện" : initEvent); cbEvent.setPrefWidth(Double.MAX_VALUE);
        VBox eventGroup = new VBox(6, format.formatLabel("Liên kết sự kiện", FontWeight.BOLD, 12, "#94a3b8"), cbEvent);

        // --- 3. Phân công Nhân sự (Cho BAN thay vì Cá nhân) ---
        ComboBox<String> cbDept = format.formatSortBtn("Ban phụ trách", "Ban Truyền thông", "Ban Kỹ thuật", "Ban Sự kiện", "Ban Nhân sự", "Ban Đối ngoại");
        cbDept.setValue(initDept.isEmpty() ? "Ban Truyền thông" : initDept); cbDept.setMaxWidth(Double.MAX_VALUE);
        VBox deptGroup = new VBox(6, format.formatLabel("Ban chịu trách nhiệm chính *", FontWeight.BOLD, 12, "#94a3b8"), cbDept);

        // --- 4. Thời gian ---
        HBox timeRow = new HBox(12);
        TextField fStart = format.formatTextField("dd/MM/yyyy"); fStart.setText(initStart);
        VBox startGroup = new VBox(6, format.formatLabel("Ngày bắt đầu", FontWeight.BOLD, 12, "#94a3b8"), fStart);
        HBox.setHgrow(startGroup, Priority.ALWAYS);

        TextField fEnd = format.formatTextField("dd/MM/yyyy"); fEnd.setText(initEnd);
        VBox endGroup = new VBox(6, format.formatLabel("Hạn hoàn thành *", FontWeight.BOLD, 12, "#94a3b8"), fEnd);
        HBox.setHgrow(endGroup, Priority.ALWAYS);
        timeRow.getChildren().addAll(startGroup, endGroup);

        // --- 5. Tùy chọn (Mức độ ưu tiên & Trạng thái) ---
        HBox optRow = new HBox(12);
        ComboBox<String> cbPrio = format.formatSortBtn("Ưu tiên", "Thấp", "Trung bình", "Cao");
        cbPrio.setValue(initPrio.isEmpty() ? "Trung bình" : initPrio); HBox.setHgrow(cbPrio, Priority.ALWAYS); cbPrio.setMaxWidth(Double.MAX_VALUE);
        VBox prioGroup = new VBox(6, format.formatLabel("Mức độ ưu tiên", FontWeight.BOLD, 12, "#94a3b8"), cbPrio);
        HBox.setHgrow(prioGroup, Priority.ALWAYS);
        optRow.getChildren().add(prioGroup);

        if (isEdit) {
            ComboBox<String> cbStatus = format.formatSortBtn("Trạng thái", "Chưa bắt đầu", "Đang thực hiện", "Hoàn thành", "Đã hủy");
            cbStatus.setValue(status); HBox.setHgrow(cbStatus, Priority.ALWAYS); cbStatus.setMaxWidth(Double.MAX_VALUE);
            VBox statusGroup = new VBox(6, format.formatLabel("Trạng thái công việc", FontWeight.BOLD, 12, "#94a3b8"), cbStatus);
            HBox.setHgrow(statusGroup, Priority.ALWAYS);
            optRow.getChildren().add(statusGroup);
        }

        fields.getChildren().addAll(nameGroup, descGroup, eventGroup, deptGroup, timeRow, optRow);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnCancel = getShadowBtn(isEdit ? "Quay lại" : "Huỷ", "", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> {
            if (!isEdit) frame.getInstance().closeOverlayModal();
            else rootModalPane.getChildren().setAll(previousView);
        });

        Button btnConfirm = getShadowBtn("Lưu Công việc", "", "#5020d8", "white", "rgba(80,32,216,0.4)");
        btnConfirm.setOnAction(e -> {
            // UNHAPPY PATH VALIDATION: Tên không được để trống
            if (fName.getText().trim().isEmpty()) {
                frame.getInstance().triggerToast("❌ Tên công việc không được để trống!");
                return; // Dừng, không đóng modal
            }
            // UNHAPPY PATH VALIDATION: Deadline không được để trống
            if (fEnd.getText().trim().isEmpty()) {
                frame.getInstance().triggerToast("❌ Vui lòng nhập hạn hoàn thành!");
                return;
            }

            // Mọi thứ hoàn hảo -> Lưu
            frame.getInstance().closeOverlayModal();
            frame.getInstance().triggerToast(isEdit ? "Cập nhật công việc thành công!" : "Tạo công việc thành công!");
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().addAll(title, fields, actions);

        return box;
    }

    // Tạo ô biểu mẫu xác nhận dạng nhỏ có kèm điều kiện kiểm tra dữ liệu chuỗi rỗng khi tiến hành yêu cầu hủy bỏ công việc
    private StackPane createCancelModalDashboard(String workTitle) {
        StackPane rootModalPane = new StackPane();

        VBox box = new VBox(20);
        box.setPrefWidth(420);
        box.setMaxSize(420, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel("Xác nhận Hủy Công việc", FontWeight.BLACK, 20, "#ef4444");
        Label sub = format.formatLabel("Công việc [" + workTitle + "] sẽ được chuyển sang trạng thái Đã hủy để bảo lưu lịch sử.", FontWeight.MEDIUM, 12, "#64748b");
        sub.setWrapText(true);

        VBox fields = new VBox(8);
        fields.getChildren().add(format.formatLabel("Lý do hủy (Bắt buộc)", FontWeight.BOLD, 12, "#94a3b8"));
        TextField fReason = format.formatTextField("Ví dụ: Event đã thay đổi kịch bản...");
        fields.getChildren().add(fReason);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnCancel = getShadowBtn("Đóng", "", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> frame.getInstance().closeOverlayModal());

        Button btnConfirm = getShadowBtn("Xác nhận Hủy", "", "#ef4444", "white", "rgba(239,68,68,0.4)");
        btnConfirm.setOnAction(e -> {
            // UNHAPPY PATH VALIDATION: Phải có lý do hủy
            if (fReason.getText().trim().isEmpty()) {
                frame.getInstance().triggerToast("❌ Vui lòng nhập lý do hủy để bảo lưu lịch sử!");
                return;
            }

            frame.getInstance().closeOverlayModal();
            frame.getInstance().triggerToast("Đã hủy công việc: " + workTitle);
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().addAll(title, sub, fields, actions);

        rootModalPane.getChildren().add(box);
        return rootModalPane;
    }

    // Thiết lập phong cách hiển thị đồ họa nền và kiểm soát tỷ lệ co giãn động khi di chuyển chuột qua lại trên nút
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