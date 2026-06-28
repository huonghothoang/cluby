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

public class department extends ScrollPane {

    private VBox dashboardContent;
    private VBox cardGrid;

    public department() {
        dashboardContent = new VBox(32);
        dashboardContent.setPadding(new Insets(32));
        dashboardContent.setStyle("-fx-background-color: transparent;");

        HBox actionRow = new HBox(16);
        actionRow.setAlignment(Pos.CENTER_LEFT);
        Label title = format.formatLabel("Cơ cấu bộ phận", FontWeight.BLACK, 28, "#1e293b");
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnAddDept = getShadowBtn("Thêm bộ phận", "➕", "#5020d8", "white", "rgba(80,32,216,0.4)");
        btnAddDept.setOnAction(e -> {
            StackPane addModal = createDeptFormModal("Thêm bộ phận", "", "", "01/03/2026", "Hoạt động", false);
            frame.getInstance().showCustomModal(addModal);
        });
        actionRow.getChildren().addAll(title, spacer, btnAddDept);

        cardGrid = new VBox(24);

        addCardToGrid(cardGrid, createDeptCard("📢", "Nội dung", "Nguyễn Văn A", 12));
        addCardToGrid(cardGrid, createDeptCard("💻", "Kỹ thuật", "Trần Văn B", 8));
        addCardToGrid(cardGrid, createDeptCard("🎪", "Truyền thông", "Lê Văn C", 15));
        addCardToGrid(cardGrid, createDeptCard("🎨", "Hậu cần", "Chưa phân công", 0));

        dashboardContent.getChildren().addAll(actionRow, cardGrid);

        format.formatScrollbar(this, dashboardContent, 12);
        this.setContent(dashboardContent);
    }

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

    private VBox createDeptCard(String icon, String name, String leader, int memberCount) {
        VBox box = new VBox(16);
        box.setPrefSize(470, 168);
        box.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        box.setMaxSize(470, 168);
        box.setPadding(new Insets(24));
        box.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 32px; -fx-border-color: rgba(255,255,255,0.9); -fx-border-width: 2px; -fx-border-radius: 32px; -fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.1), 15, 0, 0, 6);");

        HBox topRow = new HBox(12);
        topRow.setAlignment(Pos.CENTER_LEFT);
        Label lblName = format.formatLabel(icon + " " + name, FontWeight.BLACK, 20, "#1e293b");
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        Label lblCount = format.formatBadge(memberCount + " thành viên", "rgba(124,77,255,0.15)", "#7c4dff");
        lblCount.setStyle(lblCount.getStyle() + "-fx-font-size: 12px; -fx-padding: 6 12 6 12; -fx-background-radius: 16px;");
        topRow.getChildren().addAll(lblName, spacer, lblCount);

        VBox infoBox = new VBox(8);
        Label lblLeader = format.formatLabel("Trưởng nhóm: " + leader, FontWeight.BOLD, 14, leader.equals("Chưa phân công") ? "#f59e0b" : "#475569");
        infoBox.getChildren().add(lblLeader);

        HBox actionsRow = new HBox(12);
        actionsRow.setAlignment(Pos.CENTER_RIGHT);

        Button btnView = format.formatCircleBtn("👁️‍🗨️", "#448aff", "#7c4dff");
        btnView.setPrefSize(40, 40);
        btnView.setOnAction(e -> {
            StackPane detailModal = createDetailModal(name, leader, memberCount, "Phụ trách kế hoạch và triển khai công việc.", "01/03/2026", "Hoạt động");
            frame.getInstance().showCustomModal(detailModal);
        });

        Button btnDelete = format.formatCircleBtn("➖", "#ef4444", "#dc2626");
        btnDelete.setPrefSize(40, 40);
        btnDelete.setOnAction(e -> {
            StackPane dissolveModal = createDeleteModal(name, leader, memberCount);
            frame.getInstance().showCustomModal(dissolveModal);
        });

        actionsRow.getChildren().addAll(btnView, btnDelete);
        box.getChildren().addAll(topRow, infoBox, actionsRow);

        return box;
    }

    private StackPane createDetailModal(String name, String leader, int memberCount, String desc, String date, String status) {
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
        Label lblTitle = format.formatLabel("Bộ phận " + name, FontWeight.BLACK, 26, "#1e293b");

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnBack = getShadowBtn("Đóng", "", "rgba(100,116,139,0.15)", "#475569", "rgba(0,0,0,0.1)");
        Button btnEdit = getShadowBtn("Sửa", "", "#5020d8", "white", "rgba(80,32,216,0.4)");

        btnBack.setOnAction(e -> frame.getInstance().closeOverlayModal());
        btnEdit.setOnAction(e -> {
            frame.getInstance().closeOverlayModal();
            StackPane editModal = createDeptFormModal("Sửa bộ phận", name, desc, date, status, true);
            frame.getInstance().showCustomModal(editModal);
        });
        header.getChildren().addAll(lblTitle, spacer, btnBack, btnEdit);

        VBox infoBox = format.formatBoxCard();
        infoBox.getChildren().add(format.formatLabel("THÔNG TIN CHUNG", FontWeight.BLACK, 12, "#94a3b8"));

        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(40); infoGrid.setVgap(16);
        infoGrid.add(new VBox(4, format.formatLabel("Tên bộ phận", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(name, FontWeight.BLACK, 16, "#1e293b")), 0, 0);
        infoGrid.add(new VBox(4, format.formatLabel("Mô tả", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(desc, FontWeight.BOLD, 14, "#475569")), 1, 0);
        infoGrid.add(new VBox(4, format.formatLabel("Ngày tạo", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(date, FontWeight.BOLD, 14, "#475569")), 0, 1);

        Label statusBadge = format.formatBadge(status, "rgba(16,185,129,0.15)", "#10b981");
        statusBadge.setStyle(statusBadge.getStyle() + "-fx-font-size: 12px; -fx-padding: 4 12 4 12;");
        infoGrid.add(new VBox(4, format.formatLabel("Trạng thái", FontWeight.BOLD, 12, "#94a3b8"), statusBadge), 1, 1);

        infoBox.getChildren().add(infoGrid);

        HBox bottomView = new HBox(24);

        VBox leftCol = new VBox(20);
        leftCol.setPrefWidth(320);

        VBox leaderBox = format.formatBoxCard();
        leaderBox.getChildren().add(format.formatLabel("QUẢN LÝ", FontWeight.BLACK, 12, "#94a3b8"));
        HBox leaderRow = new HBox(8);
        leaderRow.setAlignment(Pos.CENTER_LEFT);
        Label lblLeaderName = format.formatLabel(leader, FontWeight.BLACK, 14, "#1e293b");
        lblLeaderName.setWrapText(true);
        leaderRow.getChildren().addAll(lblLeaderName, format.formatBadge("Trưởng nhóm", "rgba(16,185,129,0.15)", "#10b981"));

        Button btnChangeLeader = getShadowBtn("Thay đổi", "", "rgba(124,77,255,0.15)", "#5020d8", "rgba(124,77,255,0.3)");
        btnChangeLeader.setOnAction(e -> {
            frame.getInstance().closeOverlayModal();
            StackPane assignModal = createChangeLeaderModal(name, leader);
            frame.getInstance().showCustomModal(assignModal);
        });
        leaderBox.getChildren().addAll(leaderRow, btnChangeLeader);

        VBox statBox = format.formatBoxCard();
        statBox.getChildren().add(format.formatLabel("THỐNG KÊ", FontWeight.BLACK, 12, "#94a3b8"));
        VBox stats = new VBox(12);
        stats.getChildren().addAll(
                new HBox(12, format.formatLabel("Thành viên:", FontWeight.BOLD, 13, "#64748b"), format.formatLabel(String.valueOf(memberCount), FontWeight.BLACK, 14, "#1e293b")),
                new HBox(12, format.formatLabel("Trưởng nhóm:", FontWeight.BOLD, 13, "#64748b"), format.formatLabel(leader, FontWeight.BLACK, 14, "#1e293b")),
                new HBox(12, format.formatLabel("Nhân sự:", FontWeight.BOLD, 13, "#64748b"), format.formatLabel(String.valueOf(Math.max(0, leader.equals("Chưa phân công") ? memberCount : memberCount - 1)), FontWeight.BLACK, 14, "#1e293b"))
        );
        statBox.getChildren().add(stats);
        leftCol.getChildren().addAll(leaderBox, statBox);

        VBox rightCol = format.formatBoxCard();
        rightCol.setPrefWidth(370);
        rightCol.getChildren().add(format.formatLabel("DANH SÁCH NHÂN SỰ", FontWeight.BLACK, 12, "#94a3b8"));

        VBox memberList = new VBox(8);
        if (!leader.equals("Chưa phân công")) memberList.getChildren().add(createSimpleMemberRow(leader, "Trưởng nhóm"));
        if (memberCount > 1) {
            memberList.getChildren().addAll(
                    createSimpleMemberRow("Lê Văn B", "Thành viên"),
                    createSimpleMemberRow("Trần Văn C", "Thành viên"),
                    createSimpleMemberRow("Nguyễn Hoàng D", "Thành viên")
            );
        }
        ScrollPane memberScroll = new ScrollPane(memberList);
        memberScroll.setPrefHeight(200);
        format.formatScrollbar(memberScroll, memberList, 8);

        Button btnViewModule = getShadowBtn("Quản lý thành viên", "", "rgba(100,116,139,0.1)", "#475569", "rgba(0,0,0,0.1)");
        btnViewModule.setOnAction(e -> {
            frame.getInstance().closeOverlayModal();
            frame.getInstance().triggerToast("Chuyển hướng...");
        });

        rightCol.getChildren().addAll(memberScroll, btnViewModule);
        bottomView.getChildren().addAll(leftCol, rightCol);

        modalContent.getChildren().addAll(header, infoBox, bottomView);
        rootModalPane.getChildren().add(modalContent);

        return rootModalPane;
    }

    private HBox createSimpleMemberRow(String name, String role) {
        HBox row = new HBox(12);
        row.setPadding(new Insets(12));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-border-color: transparent transparent rgba(0,0,0,0.05) transparent; -fx-border-width: 1px;");
        Label lblAvatar = format.formatLabel("👤", FontWeight.NORMAL, 18, "#64748b");
        Label lblName = format.formatLabel(name, FontWeight.BOLD, 14, "#1e293b");
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        String roleBg = role.equals("Trưởng nhóm") ? "rgba(16,185,129,0.15)" : "rgba(100,116,139,0.15)";
        String roleText = role.equals("Trưởng nhóm") ? "#10b981" : "#64748b";
        row.getChildren().addAll(lblAvatar, lblName, spacer, format.formatBadge(role, roleBg, roleText));
        return row;
    }

    private StackPane createDeptFormModal(String titleText, String initName, String initDesc, String initDate, String initStatus, boolean isEdit) {
        StackPane rootModalPane = new StackPane();

        VBox box = new VBox(16);
        box.setPrefSize(384, Region.USE_COMPUTED_SIZE);
        box.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        box.setMaxSize(384, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(28));

        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel(titleText, FontWeight.BLACK, 20, "#1e293b");

        VBox fields = new VBox(12);
        VBox nameGroup = new VBox(4, format.formatLabel("Tên bộ phận *", FontWeight.BOLD, 12, "#94a3b8"), format.formatTextField("Nhập tên..."));
        ((TextField)nameGroup.getChildren().get(1)).setText(initName);

        VBox descGroup = new VBox(4, format.formatLabel("Mô tả", FontWeight.BOLD, 12, "#94a3b8"), format.formatTextField("Nhập mô tả..."));
        ((TextField)descGroup.getChildren().get(1)).setText(initDesc);

        TextField fDate = format.formatTextField(initDate);
        fDate.setDisable(true);
        VBox dateGroup = new VBox(4, format.formatLabel("Ngày tạo", FontWeight.BOLD, 12, "#94a3b8"), fDate);

        fields.getChildren().addAll(nameGroup, descGroup, dateGroup);

        if (isEdit) {
            ComboBox<String> cbStatus = format.formatSortBtn("Trạng thái", "Hoạt động", "Tạm ngưng");
            cbStatus.setValue(initStatus); cbStatus.setPrefWidth(Double.MAX_VALUE);
            VBox statusGroup = new VBox(4, format.formatLabel("Trạng thái", FontWeight.BOLD, 12, "#94a3b8"), cbStatus);
            fields.getChildren().add(statusGroup);
        }

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);
        actions.setPadding(new Insets(8, 0, 0, 0));

        Button btnCancel = getShadowBtn("Huỷ", "", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> frame.getInstance().closeOverlayModal());

        Button btnConfirm = getShadowBtn("Xác nhận", "", "#5020d8", "white", "rgba(80,32,216,0.4)");
        btnConfirm.setOnAction(e -> {
            frame.getInstance().closeOverlayModal();
            frame.getInstance().triggerToast("Đã lưu thông tin");
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().addAll(title, fields, actions);
        rootModalPane.getChildren().add(box);
        return rootModalPane;
    }

    private StackPane createChangeLeaderModal(String deptName, String currentLeader) {
        StackPane rootModalPane = new StackPane();

        VBox box = new VBox(16);
        box.setPrefSize(336, Region.USE_COMPUTED_SIZE);
        box.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        box.setMaxSize(336, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(28));

        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel("Bổ nhiệm trưởng nhóm", FontWeight.BLACK, 20, "#1e293b");
        title.setWrapText(true);
        Label warning = format.formatLabel("Trưởng nhóm cũ (" + currentLeader + ") sẽ chuyển thành thành viên.", FontWeight.MEDIUM, 12, "#94a3b8");
        warning.setWrapText(true);

        box.getChildren().addAll(title, warning);

        ComboBox<String> cbMembers = format.formatSortBtn("Chọn thành viên", "Lê Văn B", "Trần Văn C", "Chưa phân công");
        cbMembers.setPrefWidth(Double.MAX_VALUE);
        box.getChildren().add(cbMembers);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);
        actions.setPadding(new Insets(8, 0, 0, 0));

        Button btnCancel = getShadowBtn("Huỷ", "", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> frame.getInstance().closeOverlayModal());

        Button btnConfirm = getShadowBtn("Xác nhận", "", "#10b981", "white", "rgba(16,185,129,0.4)");
        btnConfirm.setOnAction(e -> {
            frame.getInstance().closeOverlayModal();
            frame.getInstance().triggerToast("Đã cập nhật nhân sự");
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().add(actions);
        rootModalPane.getChildren().add(box);
        return rootModalPane;
    }

    private StackPane createDeleteModal(String deptName, String leader, int memberCount) {
        StackPane rootModalPane = new StackPane();

        VBox box = new VBox(16);
        box.setPrefSize(336, Region.USE_COMPUTED_SIZE);
        box.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        box.setMaxSize(336, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(28));

        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        boolean hasLeader = !leader.equals("Chưa phân công");
        boolean hasMembers = memberCount > 0;

        if (hasLeader || hasMembers) {
            Label mainLabel = format.formatLabel("Không thể ngưng hoạt động", FontWeight.BLACK, 20, "#ef4444");
            mainLabel.setWrapText(true);
            box.getChildren().add(mainLabel);

            VBox warningBox = new VBox(12);
            if (hasMembers) {
                Label w1 = format.formatLabel("• Bộ phận còn " + memberCount + " thành viên.", FontWeight.BOLD, 13, "#475569");
                w1.setWrapText(true);
                Label d1 = format.formatLabel("Vui lòng chuyển nhân sự sang bộ phận khác trước.", FontWeight.MEDIUM, 12, "#94a3b8");
                d1.setWrapText(true);
                warningBox.getChildren().addAll(w1, d1);
            }
            if (hasLeader) {
                Label w2 = format.formatLabel("• Bộ phận đang có trưởng nhóm.", FontWeight.BOLD, 13, "#475569");
                w2.setWrapText(true);
                Label d2 = format.formatLabel("Vui lòng miễn nhiệm vị trí trưởng nhóm trước.", FontWeight.MEDIUM, 12, "#94a3b8");
                d2.setWrapText(true);
                warningBox.getChildren().addAll(w2, d2);
            }
            box.getChildren().add(warningBox);

            HBox actions = new HBox(12);
            actions.setAlignment(Pos.CENTER_RIGHT);
            actions.setPadding(new Insets(8, 0, 0, 0));
            Button btnClose = getShadowBtn("Đóng", "", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
            btnClose.setOnAction(e -> frame.getInstance().closeOverlayModal());
            actions.getChildren().add(btnClose);
            box.getChildren().add(actions);
        } else {
            Label title = format.formatLabel("Xác nhận ngưng hoạt động", FontWeight.BLACK, 20, "#ef4444");
            title.setWrapText(true);
            Label desc = format.formatLabel("Bộ phận không còn thành viên. Hệ thống sẽ lưu trữ thông tin nội bộ.", FontWeight.MEDIUM, 12, "#64748b");
            desc.setWrapText(true);

            box.getChildren().addAll(title, desc);

            HBox actions = new HBox(12);
            actions.setAlignment(Pos.CENTER_RIGHT);
            actions.setPadding(new Insets(8, 0, 0, 0));

            Button btnCancel = getShadowBtn("Huỷ", "", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
            btnCancel.setOnAction(e -> frame.getInstance().closeOverlayModal());

            Button btnConfirm = getShadowBtn("Xác nhận", "", "#ef4444", "white", "rgba(239,68,68,0.4)");
            btnConfirm.setOnAction(e -> {
                frame.getInstance().closeOverlayModal();
                frame.getInstance().triggerToast("Đã tạm ngưng bộ phận");
            });
            actions.getChildren().addAll(btnCancel, btnConfirm);
            box.getChildren().add(actions);
        }

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