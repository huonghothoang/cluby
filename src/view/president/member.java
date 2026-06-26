package view.president;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import view.president.frame;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class member extends ScrollPane {

    // Khởi tạo giao diện chính quản lý danh sách thành viên và các bộ lọc tìm kiếm
    public member() {
        VBox mainContent = new VBox(24);
        mainContent.setPadding(new Insets(32));
        mainContent.setStyle("-fx-background-color: transparent;");

        HBox kpiRow = new HBox(16);
        kpiRow.getChildren().addAll(
                format.formatKPICard("Tổng thành viên", "128", "#64748b", "#1e293b"),
                format.formatKPICard("🟢 Đang hoạt động", "100", "#10b981", "#1e293b"),
                format.formatKPICard("🟡 Tạm nghỉ", "20", "#f59e0b", "#1e293b"),
                format.formatKPICard("🔴 Đã rời CLB", "8", "#64748b", "#1e293b")
        );
        for(javafx.scene.Node node : kpiRow.getChildren()) { HBox.setHgrow(node, Priority.ALWAYS); }

        HBox filterBar = new HBox(16);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(12, 24, 12, 24));
        format.formatGlass(filterBar, 40, 0.4);

        HBox searchBox = new HBox(8);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = format.formatTextField("Nhập tên thành viên...");
        Button btnSearch = format.formatFindBtn();
        searchBox.getChildren().addAll(searchField, btnSearch);

        ComboBox<String> cbBan = format.formatSortBtn("Lọc theo Ban", "Tất cả Ban", "Truyền thông", "Kỹ thuật", "Sự kiện", "Nhân sự", "Đối ngoại", "Chưa phân công");
        ComboBox<String> cbRole = format.formatSortBtn("Lọc chức vụ", "Tất cả Chức vụ", "Hội trưởng", "Hội phó", "Trưởng ban", "Thành viên");
        ComboBox<String> cbStatus = format.formatSortBtn("Trạng thái", "Tất cả", "Đang hoạt động", "Tạm nghỉ", "Đã rời CLB", "Bị khóa");

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnAdd = format.formatBtn("Thêm thành viên", "➕");
        btnAdd.setOnAction(e -> {
            StackPane addModal = createAddMemberModal(() -> frame.getInstance().closeOverlayModal());
            frame.getInstance().showCustomModal(addModal);
        });

        filterBar.getChildren().addAll(searchBox, cbBan, cbRole, cbStatus, spacer, btnAdd);

        VBox tableContainer = format.formatTableContainer();
        tableContainer.getChildren().add(createMemberTableHeader());

        EventHandler<ActionEvent> markAsLeftAction = e -> {
            VBox confirmModal = format.formatSimpleModal(
                    "Xác nhận thay đổi", "Chuyển trạng thái thành viên này sang 'Đã rời CLB'?", "Hủy", "Xác nhận",
                    ev -> frame.getInstance().closeOverlayModal(),
                    ev -> {
                        frame.getInstance().closeOverlayModal();
                        frame.getInstance().triggerToast("Đã thay đổi trạng thái thành viên!");
                    });
            frame.getInstance().showCustomModal(confirmModal);
        };

        tableContainer.getChildren().addAll(
                createMemberRow("trish.jpeg", "Alexandra Đặng", "23HT001", "alex@gmail.com", "0901234567", "Chưa phân công", "Hội trưởng", "01/01/2023", "Đang hoạt động", "Phụ trách điều hành tổng quan", markAsLeftAction),
                createMemberRow("trish.jpeg", "Hoàng Kim Yến", "24HP002", "yen@gmail.com", "0909888777", "Chưa phân công", "Hội phó", "15/05/2024", "Đang hoạt động", "Quản lý trực tiếp các trưởng ban", markAsLeftAction),
                createMemberRow("trish.jpeg", "Nguyễn Văn A", "25GT020", "abc@gmail.com", "0123456789", "Truyền thông", "Trưởng ban", "01/03/2025", "Đang hoạt động", "", markAsLeftAction),
                createMemberRow("trish.jpeg", "Lê Văn C", "24SK011", "levanc@gmail.com", "0988776655", "Sự kiện", "Thành viên", "15/09/2024", "Tạm nghỉ", "Xin nghỉ ôn thi đến tháng 8", markAsLeftAction)
        );

        mainContent.getChildren().addAll(kpiRow, filterBar, tableContainer);
        format.formatScrollbar(this, mainContent, 12);
        this.setContent(mainContent);
    }

    // Tạo thanh tiêu đề cột cho bảng danh sách thành viên
    private HBox createMemberTableHeader() {
        HBox header = new HBox(16);
        header.setPadding(new Insets(12, 16, 12, 16));
        header.setStyle("-fx-border-color: transparent transparent rgba(255,255,255,0.4) transparent; -fx-border-width: 1px;");
        Label l1 = format.formatLabel("AVATAR", FontWeight.BLACK, 10, "#94a3b8"); l1.setPrefWidth(60);
        Label l2 = format.formatLabel("THÀNH VIÊN", FontWeight.BLACK, 10, "#94a3b8"); l2.setPrefWidth(200);
        Label l3 = format.formatLabel("BAN", FontWeight.BLACK, 10, "#94a3b8"); l3.setPrefWidth(120);
        Label l4 = format.formatLabel("CHỨC VỤ", FontWeight.BLACK, 10, "#94a3b8"); l4.setPrefWidth(120);
        Label l5 = format.formatLabel("TRẠNG THÁI", FontWeight.BLACK, 10, "#94a3b8"); l5.setPrefWidth(120);
        Label l6 = format.formatLabel(" ", FontWeight.BLACK, 10, "#94a3b8"); l6.setPrefWidth(40);
        Label l7 = format.formatLabel(" ", FontWeight.BLACK, 10, "#94a3b8"); l7.setPrefWidth(40);
        header.getChildren().addAll(l1, l2, l3, l4, l5, l6, l7);
        return header;
    }

    // Tạo một hàng hiển thị thông tin rút gọn của thành viên trong bảng danh sách kèm nút xem chi tiết và xóa
    private HBox createMemberRow(String avatarUrl, String name, String id, String email, String phone, String dept, String role, String date, String status, String note, EventHandler<ActionEvent> onDelete) {
        HBox row = new HBox(16);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand;");
        row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand; -fx-background-radius: 16px;"));
        row.setOnMouseExited(e -> row.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand;"));

        ImageView avatar = new ImageView(new Image(avatarUrl));
        avatar.setFitWidth(40); avatar.setFitHeight(40); avatar.setClip(new Circle(20, 20, 20));
        HBox avatarBox = new HBox(avatar); avatarBox.setPrefWidth(60);

        Label lblName = format.formatLabel(name, FontWeight.BOLD, 14, "#1e293b"); lblName.setPrefWidth(200);
        Label lblBan = format.formatLabel(dept, FontWeight.MEDIUM, 13, "#475569"); lblBan.setPrefWidth(120);

        String roleBg = (role.equals("Hội trưởng") || role.equals("Hội phó")) ? "rgba(245,158,11,0.15)" : (role.equals("Trưởng ban") ? "rgba(16,185,129,0.15)" : "rgba(124,77,255,0.1)");
        String roleText = (role.equals("Hội trưởng") || role.equals("Hội phó")) ? "#f59e0b" : (role.equals("Trưởng ban") ? "#10b981" : "#7c4dff");
        HBox roleBox = new HBox(format.formatBadge(role, roleBg, roleText)); roleBox.setAlignment(Pos.CENTER_LEFT); roleBox.setPrefWidth(120);

        String stBg = status.equals("Đang hoạt động") ? "rgba(16,185,129,0.15)" : status.equals("Tạm nghỉ") ? "rgba(245,158,11,0.15)" : status.equals("Bị khóa") ? "rgba(239,68,68,0.15)" : "rgba(100,116,139,0.15)";
        String stText = status.equals("Đang hoạt động") ? "#10b981" : status.equals("Tạm nghỉ") ? "#f59e0b" : status.equals("Bị khóa") ? "#ef4444" : "#64748b";
        HBox statusBox = new HBox(format.formatBadge(status, stBg, stText)); statusBox.setAlignment(Pos.CENTER_LEFT); statusBox.setPrefWidth(120);

        Button btnEdit = format.formatCircleBtn("👁️‍🗨️", "#448aff", "#7c4dff");
        btnEdit.setOnAction(e -> {
            StackPane modal = createMemberProfileModal(avatarUrl, name, id, email, phone, dept, role, date, status, note, () -> frame.getInstance().closeOverlayModal());
            frame.getInstance().showCustomModal(modal);
        });
        HBox editBox = new HBox(btnEdit); editBox.setAlignment(Pos.CENTER); editBox.setPrefWidth(40);

        Button btnDelete = format.formatCircleBtn("➖", "#ef4444", "#dc2626");
        if (onDelete != null) btnDelete.setOnAction(onDelete);
        HBox delBox = new HBox(btnDelete); delBox.setAlignment(Pos.CENTER); delBox.setPrefWidth(40);

        row.getChildren().addAll(avatarBox, lblName, lblBan, roleBox, statusBox, editBox, delBox);
        return row;
    }

    // Tạo cửa sổ modal hỗ trợ nhập liệu, tra cứu thông tin ứng viên để lập hồ sơ thêm thành viên mới
    private StackPane createAddMemberModal(Runnable onClose) {
        StackPane rootModalPane = new StackPane();

        VBox box = new VBox(16);
        box.setPrefWidth(450);
        box.setMaxSize(450, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(28));
        box.setStyle("-fx-background-color: rgba(255,255,255); -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel("Thêm thành viên mới", FontWeight.BLACK, 22, "#1e293b");
        Label sub = format.formatLabel("Tra cứu theo Mã định danh để cấp quyền và xếp Ban nội bộ.", FontWeight.MEDIUM, 11, "#94a3b8");

        VBox fields = new VBox(12);

        VBox idGroup = new VBox(4);
        Label lblId = format.formatLabel("Mã định danh (ID Sinh viên / ID Ứng viên)", FontWeight.BOLD, 12, "#94a3b8");
        HBox idRow = new HBox(8);
        TextField fId = format.formatTextField("Nhập mã định danh (Ví dụ: 25GT020)...");
        HBox.setHgrow(fId, Priority.ALWAYS);
        Button btnSearch = getFormBtn("🔍", "#5020d8", "white");
        idRow.getChildren().addAll(fId, btnSearch);
        idGroup.getChildren().addAll(lblId, idRow);

        VBox deptGroup = new VBox(4);
        Label lblDept = format.formatLabel("Ban hoạt động (Thành viên)", FontWeight.BOLD, 12, "#94a3b8");
        ComboBox<String> cbDept = format.formatSortBtn("Chọn Ban tham gia", "Truyền thông", "Kỹ thuật", "Sự kiện", "Nhân sự", "Đối ngoại", "Chưa phân công");
        cbDept.setPrefWidth(Double.MAX_VALUE);
        deptGroup.getChildren().addAll(lblDept, cbDept);

        VBox dateGroup = new VBox(4);
        Label lblDate = format.formatLabel("Ngày tham gia hệ thống", FontWeight.BOLD, 12, "#94a3b8");
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        TextField fDate = format.formatTextField(today);
        fDate.setText(today);
        fDate.setDisable(true);
        dateGroup.getChildren().addAll(lblDate, fDate);

        VBox noteGroup = new VBox(4);
        Label lblNote = format.formatLabel("Ghi chú (Tuỳ chọn)", FontWeight.BOLD, 12, "#94a3b8");
        TextField fNote = format.formatTextField("Điền thông tin bổ sung...");
        noteGroup.getChildren().addAll(lblNote, fNote);

        fields.getChildren().addAll(idGroup, deptGroup, dateGroup, noteGroup);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnCancel = getFormBtn("Huỷ", "rgba(178, 162, 228, 0.2)", "#64748b");
        btnCancel.setOnAction(e -> onClose.run());
        Button btnConfirm = getFormBtn("Tạo hồ sơ", "#5020d8", "white");

        btnConfirm.setOnAction(e -> {
            if (fId.getText().trim().isEmpty() || cbDept.getValue() == null) {
                frame.getInstance().triggerToast("Lỗi: Bạn cần tra cứu ID hợp lệ và chọn Ban để lưu hồ sơ!"); return;
            }
            frame.getInstance().triggerToast("Thêm Thành viên mới vào hệ thống thành công!");
            onClose.run();
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().addAll(title, sub, fields, actions);
        rootModalPane.getChildren().add(box);

        btnSearch.setOnAction(e -> {
            String query = fId.getText().trim();
            if (query.isEmpty()) {
                frame.getInstance().triggerToast("Vui lòng nhập mã định danh để tiến hành tra cứu!");
                return;
            }

            if (query.equals("000")) {
                frame.getInstance().triggerToast("Không tìm thấy người dùng với mã định danh này.");
            } else {
                VBox mockProfile = new VBox(20);
                mockProfile.setPrefWidth(420);
                mockProfile.setMaxSize(420, Region.USE_PREF_SIZE);
                mockProfile.setPadding(new Insets(32));
                mockProfile.setStyle("-fx-background-color: white; -fx-background-radius: 40px;");
                mockProfile.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.4)));

                VBox headerInfo = new VBox(8);
                headerInfo.setAlignment(Pos.CENTER);
                ImageView avt = new ImageView(new Image("trish.jpeg"));
                avt.setFitWidth(80); avt.setFitHeight(80); avt.setClip(new Circle(40, 40, 40));
                headerInfo.getChildren().addAll(avt, format.formatLabel("Ứng viên Mẫu", FontWeight.BLACK, 24, "#1e293b"));

                VBox infoList = new VBox(12);
                infoList.setAlignment(Pos.CENTER_LEFT);
                infoList.setPadding(new Insets(0, 0, 0, 20));
                infoList.getChildren().addAll(
                        new VBox(2, format.formatLabel("Mã định danh", FontWeight.BOLD, 10, "#94a3b8"), format.formatLabel(query, FontWeight.BOLD, 14, "#1e293b")),
                        new VBox(2, format.formatLabel("Email", FontWeight.BOLD, 10, "#94a3b8"), format.formatLabel("mock.candidate@glimpz.com", FontWeight.BOLD, 14, "#1e293b")),
                        new VBox(2, format.formatLabel("SĐT", FontWeight.BOLD, 10, "#94a3b8"), format.formatLabel("0901234567", FontWeight.BOLD, 14, "#1e293b"))
                );

                HBox profileActions = new HBox(16);
                profileActions.setAlignment(Pos.CENTER);
                profileActions.setPadding(new Insets(10, 0, 0, 0));

                Button btnNotMatch = new Button("Không phải\nứng viên này");
                btnNotMatch.setPrefSize(130, 70);
                btnNotMatch.setWrapText(true);
                btnNotMatch.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
                btnNotMatch.setStyle("-fx-background-color: rgba(178, 162, 228, 0.2); -fx-text-fill: #64748b; -fx-font-family: 'Google Sans'; -fx-font-weight: bold; -fx-background-radius: 16px; -fx-cursor: hand;");
                btnNotMatch.setOnMouseEntered(ev -> { btnNotMatch.setScaleX(1.05); btnNotMatch.setScaleY(1.05); });
                btnNotMatch.setOnMouseExited(ev -> { btnNotMatch.setScaleX(1.0); btnNotMatch.setScaleY(1.0); });
                btnNotMatch.setOnAction(ev -> rootModalPane.getChildren().remove(mockProfile));

                Button btnMatch = new Button("Đây đúng là thành\nviên tôi muốn thêm");
                btnMatch.setPrefSize(130, 70);
                btnMatch.setWrapText(true);
                btnMatch.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
                btnMatch.setStyle("-fx-background-color: #5020d8; -fx-text-fill: white; -fx-font-family: 'Google Sans'; -fx-font-weight: bold; -fx-background-radius: 16px; -fx-cursor: hand;");
                btnMatch.setOnMouseEntered(ev -> { btnMatch.setScaleX(1.05); btnMatch.setScaleY(1.05); });
                btnMatch.setOnMouseExited(ev -> { btnMatch.setScaleX(1.0); btnMatch.setScaleY(1.0); });
                btnMatch.setOnAction(ev -> {
                    rootModalPane.getChildren().remove(mockProfile);
                    frame.getInstance().triggerToast("Mã định danh người dùng hợp lệ, vui lòng tiến hành các bước tiếp theo");
                });

                profileActions.getChildren().addAll(btnNotMatch, btnMatch);
                mockProfile.getChildren().addAll(headerInfo, infoList, profileActions);

                rootModalPane.getChildren().add(mockProfile);
            }
        });

        return rootModalPane;
    }

    // Tạo cửa sổ hai chế độ hỗ trợ việc xem chi tiết lý lịch thành viên hoặc chuyển đổi sang cấu trúc chỉnh sửa phân ban chức vụ
    private StackPane createMemberProfileModal(String avatarUrl, String name, String id, String email, String phone, String dept, String role, String date, String status, String note, Runnable onClose) {
        StackPane rootModalPane = new StackPane();

        VBox viewMode = new VBox(20);
        viewMode.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        viewMode.setPadding(new Insets(32));
        viewMode.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        viewMode.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        VBox headerInfo = new VBox(8);
        headerInfo.setAlignment(Pos.CENTER);
        ImageView avt = new ImageView(new Image(avatarUrl));
        avt.setFitWidth(80); avt.setFitHeight(80); avt.setClip(new Circle(40, 40, 40));
        headerInfo.getChildren().addAll(avt, format.formatLabel(name, FontWeight.BLACK, 24, "#1e293b"));

        GridPane grid = new GridPane();
        grid.setHgap(32); grid.setVgap(16);
        grid.add(new VBox(2, format.formatLabel("Mã định danh", FontWeight.BOLD, 10, "#94a3b8"), format.formatLabel(id, FontWeight.BOLD, 14, "#1e293b")), 0, 0);
        grid.add(new VBox(2, format.formatLabel("Email", FontWeight.BOLD, 10, "#94a3b8"), format.formatLabel(email, FontWeight.BOLD, 14, "#1e293b")), 1, 0);
        grid.add(new VBox(2, format.formatLabel("SĐT", FontWeight.BOLD, 10, "#94a3b8"), format.formatLabel(phone, FontWeight.BOLD, 14, "#1e293b")), 0, 1);
        grid.add(new VBox(2, format.formatLabel("Ban", FontWeight.BOLD, 10, "#94a3b8"), format.formatLabel(dept, FontWeight.BOLD, 14, "#1e293b")), 1, 1);
        grid.add(new VBox(2, format.formatLabel("Chức vụ", FontWeight.BOLD, 10, "#94a3b8"), format.formatLabel(role, FontWeight.BOLD, 14, "#1e293b")), 0, 2);
        grid.add(new VBox(2, format.formatLabel("Ngày tham gia", FontWeight.BOLD, 10, "#94a3b8"), format.formatLabel(date, FontWeight.BOLD, 14, "#1e293b")), 1, 2);

        String displayNote = (note == null || note.isEmpty()) ? "Không có ghi chú" : note;
        grid.add(new VBox(2, format.formatLabel("Ghi chú", FontWeight.BOLD, 10, "#94a3b8"), format.formatLabel(displayNote, FontWeight.BOLD, 14, "#1e293b")), 0, 3, 2, 1);

        String stBg = status.equals("Đang hoạt động") ? "rgba(16,185,129,0.15)" : status.equals("Tạm nghỉ") ? "rgba(245,158,11,0.15)" : status.equals("Bị khóa") ? "rgba(239,68,68,0.15)" : "rgba(100,116,139,0.15)";
        String stText = status.equals("Đang hoạt động") ? "#10b981" : status.equals("Tạm nghỉ") ? "#f59e0b" : status.equals("Bị khóa") ? "#ef4444" : "#64748b";
        HBox statusLine = new HBox(12, format.formatLabel("Trạng thái:", FontWeight.BOLD, 12, "#94a3b8"), format.formatBadge(status, stBg, stText));
        statusLine.setAlignment(Pos.CENTER_LEFT);

        HBox viewActions = new HBox(12);
        viewActions.setAlignment(Pos.CENTER_RIGHT);

        Button btnClose = getFormBtn("Đóng", "rgba(178, 162, 228, 0.2)", "#64748b");
        btnClose.setOnAction(e -> onClose.run());
        Button btnSwitchToEdit = getFormBtn("Sửa", "#5020d8", "white");

        viewActions.getChildren().addAll(btnClose, btnSwitchToEdit);
        viewMode.getChildren().addAll(headerInfo, grid, statusLine, viewActions);

        VBox editMode = new VBox(20);
        editMode.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        editMode.setPadding(new Insets(32));
        editMode.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        editMode.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        VBox editHeader = new VBox(8);
        editHeader.setAlignment(Pos.CENTER);
        editHeader.getChildren().addAll(format.formatLabel("Chỉnh sửa Hồ sơ", FontWeight.BLACK, 24, "#1e293b"), format.formatLabel(name, FontWeight.BOLD, 14, "#7c4dff"));

        VBox editForm = new VBox(16);
        ComboBox<String> cbDept = format.formatSortBtn("Chọn Ban", "Truyền thông", "Kỹ thuật", "Sự kiện", "Nhân sự", "Đối ngoại", "Chưa phân công");
        cbDept.setValue(dept); cbDept.setPrefWidth(350);
        ComboBox<String> cbRole = format.formatSortBtn("Chọn Chức vụ", "Hội trưởng", "Hội phó", "Trưởng ban", "Thành viên");
        cbRole.setValue(role); cbRole.setPrefWidth(350);
        ComboBox<String> cbStatus = format.formatSortBtn("Chọn Trạng thái", "Đang hoạt động", "Tạm nghỉ", "Đã rời CLB", "Bị khóa");
        cbStatus.setValue(status); cbStatus.setPrefWidth(350);
        TextField fNote = format.formatTextField("Nhập ghi chú mới..."); fNote.setText(note);

        editForm.getChildren().addAll(
                new VBox(4, format.formatLabel("Ban hoạt động", FontWeight.BOLD, 12, "#94a3b8"), cbDept),
                new VBox(4, format.formatLabel("Chức vụ", FontWeight.BOLD, 12, "#94a3b8"), cbRole),
                new VBox(4, format.formatLabel("Trạng thái", FontWeight.BOLD, 12, "#94a3b8"), cbStatus),
                new VBox(4, format.formatLabel("Ghi chú", FontWeight.BOLD, 12, "#94a3b8"), fNote)
        );

        HBox editActions = new HBox(12); editActions.setAlignment(Pos.CENTER_RIGHT);

        Button btnCancelEdit = getFormBtn("Huỷ", "rgba(178, 162, 228, 0.2)", "#64748b");
        btnCancelEdit.setOnAction(e -> rootModalPane.getChildren().setAll(viewMode));

        Button btnNext = getFormBtn("Tiếp", "#10b981", "white");
        btnNext.setOnAction(e -> {
            String newDept = cbDept.getValue(); String newRole = cbRole.getValue(); String newStatus = cbStatus.getValue(); String newNote = fNote.getText();
            if (newRole.equals("Trưởng ban") && newDept.equals("Chưa phân công")) { frame.getInstance().triggerToast("Lỗi: Trưởng ban bắt buộc phải thuộc một ban!"); return; }
            if ((newRole.equals("Hội trưởng") || newRole.equals("Hội phó")) && !newDept.equals("Chưa phân công")) { frame.getInstance().triggerToast("Lỗi: Hội trưởng và Hội phó không được thuộc ban nào!"); return; }

            boolean isDeptChanged = !newDept.equals(dept); boolean isRoleChanged = !newRole.equals(role); boolean isStatusChanged = !newStatus.equals(status);
            String oldNoteTrimmed = (note == null) ? "" : note.trim(); String newNoteTrimmed = (newNote == null) ? "" : newNote.trim();
            boolean isNoteChanged = !oldNoteTrimmed.equals(newNoteTrimmed);

            if (!isDeptChanged && !isRoleChanged && !isStatusChanged && !isNoteChanged) { frame.getInstance().triggerToast("Bạn chưa thay đổi thông tin nào!"); return; }

            StringBuilder msg = new StringBuilder();
            if (isRoleChanged) msg.append(newRole.equals("Thành viên") ? "Xác nhận hạ chức Thành viên đối với " : "Xác nhận thăng chức " + newRole + " cho ").append(name);
            if (isDeptChanged) {
                if (msg.length() > 0) msg.append("\n& ");
                if (newDept.equals("Chưa phân công")) msg.append("Đưa ").append(name).append(" ra khỏi ban ").append(dept);
                else if (dept.equals("Chưa phân công")) msg.append("Đưa ").append(name).append(" vào ban ").append(newDept);
                else msg.append("Chuyển ").append(name).append(" từ ban ").append(dept).append(" sang ban ").append(newDept);
            }
            if (isStatusChanged) { if (msg.length() > 0) msg.append("\n& "); msg.append("Thay đổi trạng thái thành: ").append(newStatus); }
            if (isNoteChanged) { if (msg.length() > 0) msg.append("\n& "); msg.append("Cập nhật Ghi chú nội bộ."); }

            VBox confirmModal = format.formatSimpleModal("Xác nhận thay đổi", msg.toString(), "Quay lại", "Xác nhận",
                    ev -> { rootModalPane.getChildren().setAll(editMode); },
                    ev -> { frame.getInstance().triggerToast("Cập nhật hồ sơ thành công!"); onClose.run(); }
            );
            rootModalPane.getChildren().add(confirmModal);
        });

        editActions.getChildren().addAll(btnCancelEdit, btnNext);
        editMode.getChildren().addAll(editHeader, editForm, editActions);
        btnSwitchToEdit.setOnAction(e -> rootModalPane.getChildren().setAll(editMode));
        rootModalPane.getChildren().add(viewMode);
        return rootModalPane;
    }

    // Thiết lập màu sắc, font chữ và kiểm soát tỷ lệ co giãn đồ họa khi người dùng tương tác chuột trên nút bấm
    private Button getFormBtn(String text, String bgColor, String textColor) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));
        btn.setTextFill(Color.web(textColor));
        btn.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 40px; -fx-padding: 8 16 8 16; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> { btn.setScaleX(1.05); btn.setScaleY(1.05); });
        btn.setOnMouseExited(e -> { btn.setScaleX(1.0); btn.setScaleY(1.0); });
        return btn;
    }
}