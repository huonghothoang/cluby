package view.president;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import view.format;

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

        Button btnAddDept = getShadowBtn("Thêm bộ phận", "", "#5020d8", "white", "rgba(80,32,216,0.4)");
        btnAddDept.setOnAction(e -> {
            StackPane addModal = createDeptFormModal("Thêm bộ phận", "", "", "01/03/2026", false);
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
        Label lblLeader = format.formatLabel("Trưởng ban: " + leader, FontWeight.BOLD, 14, leader.equals("Chưa phân công") ? "#f59e0b" : "#475569");
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

        VBox modalContent = new VBox(17);
        modalContent.setPrefSize(550, Region.USE_COMPUTED_SIZE);
        modalContent.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        modalContent.setMaxSize(550, Region.USE_PREF_SIZE);
        modalContent.setPadding(new Insets(22));
        modalContent.setStyle("-fx-background-color: white; -fx-background-radius: 28px; -fx-font-family: 'Google Sans';");
        modalContent.setEffect(new DropShadow(31, 0, 10, Color.web("#311b92", 0.3)));

        HBox header = new HBox(11);
        header.setAlignment(Pos.CENTER_LEFT);
        Label lblTitle = format.formatLabel("Bộ phận " + name, FontWeight.BLACK, 18, "#1e293b");
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(lblTitle, spacer);

        VBox infoBox = format.formatBoxCard();
        infoBox.getChildren().add(format.formatLabel("THÔNG TIN CHUNG", FontWeight.BLACK, 9, "#94a3b8"));

        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(28); infoGrid.setVgap(11);
        infoGrid.add(new VBox(3, format.formatLabel("Tên bộ phận", FontWeight.BOLD, 9, "#94a3b8"), format.formatLabel(name, FontWeight.BLACK, 11, "#1e293b")), 0, 0);
        infoGrid.add(new VBox(3, format.formatLabel("Mô tả", FontWeight.BOLD, 9, "#94a3b8"), format.formatLabel(desc, FontWeight.BOLD, 10, "#475569")), 1, 0);
        infoGrid.add(new VBox(3, format.formatLabel("Ngày tạo", FontWeight.BOLD, 9, "#94a3b8"), format.formatLabel(date, FontWeight.BOLD, 10, "#475569")), 0, 1);

        Label statusBadge = format.formatBadge(status, "rgba(16,185,129,0.15)", "#10b981");
        statusBadge.setStyle(statusBadge.getStyle() + "-fx-font-size: 9px; -fx-padding: 3 8 3 8;");
        infoGrid.add(new VBox(3, format.formatLabel("Trạng thái", FontWeight.BOLD, 9, "#94a3b8"), statusBadge), 1, 1);
        infoBox.getChildren().add(infoGrid);

        HBox bottomView = new HBox(17);

        VBox leftCol = new VBox(14);
        leftCol.setPrefWidth(224);

        VBox leaderBox = format.formatBoxCard();
        leaderBox.getChildren().add(format.formatLabel("QUẢN LÝ", FontWeight.BLACK, 9, "#94a3b8"));

        HBox leaderRow = new HBox(6);
        leaderRow.setAlignment(Pos.CENTER_LEFT);
        Label lblLeaderName = format.formatLabel(leader, FontWeight.BLACK, 10, "#1e293b");
        lblLeaderName.setWrapText(true);
        Label leaderBadge = format.formatBadge("Trưởng ban", "rgba(16,185,129,0.15)", "#10b981");
        leaderBadge.setStyle(leaderBadge.getStyle() + "-fx-font-size: 9px; -fx-padding: 3 8;");
        leaderRow.getChildren().addAll(lblLeaderName, leaderBadge);

        Button btnChangeLeader = getSmallModalActionBtn("Thay đổi", "rgba(124,77,255,0.15)", "#5020d8", "rgba(124,77,255,0.3)");
        btnChangeLeader.setOnAction(e -> {
            StackPane assignModal = createChangeLeaderModal(name, leader);
            frame.getInstance().showCustomModal(assignModal);
        });

        leaderBox.getChildren().addAll(leaderRow, btnChangeLeader);

        VBox statBox = format.formatBoxCard();
        statBox.getChildren().add(format.formatLabel("THỐNG KÊ", FontWeight.BLACK, 9, "#94a3b8"));

        VBox stats = new VBox(8);
        stats.getChildren().addAll(
                new HBox(8, format.formatLabel("Thành viên:", FontWeight.BOLD, 9, "#64748b"), format.formatLabel(String.valueOf(memberCount), FontWeight.BLACK, 10, "#1e293b")),
                new HBox(8, format.formatLabel("Trưởng ban:", FontWeight.BOLD, 9, "#64748b"), format.formatLabel(leader, FontWeight.BLACK, 10, "#1e293b")),
                new HBox(8, format.formatLabel("Nhân sự:", FontWeight.BOLD, 9, "#64748b"), format.formatLabel(String.valueOf(Math.max(0, leader.equals("Chưa phân công") ? memberCount : memberCount - 1)), FontWeight.BLACK, 10, "#1e293b"))
        );
        statBox.getChildren().add(stats);
        leftCol.getChildren().addAll(leaderBox, statBox);

        VBox rightCol = format.formatBoxCard();
        rightCol.setPrefWidth(259);
        rightCol.getChildren().add(format.formatLabel("DANH SÁCH NHÂN SỰ", FontWeight.BLACK, 9, "#94a3b8"));

        VBox memberList = new VBox(6);
        if (!leader.equals("Chưa phân công")) memberList.getChildren().add(createSimpleMemberRow(leader, "Trưởng ban"));
        if (memberCount > 1) {
            memberList.getChildren().addAll(
                    createSimpleMemberRow("Lê Văn B", "Thành viên"),
                    createSimpleMemberRow("Trần Văn C", "Thành viên"),
                    createSimpleMemberRow("Nguyễn Hoàng D", "Thành viên")
            );
        }

        ScrollPane memberScroll = new ScrollPane(memberList);
        memberScroll.setPrefHeight(140);
        format.formatScrollbar(memberScroll, memberList, 6);

        rightCol.getChildren().add(memberScroll);
        bottomView.getChildren().addAll(leftCol, rightCol);

        HBox actions = new HBox(11);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(11, 0, 0, 0));

        Button btnClose = getSmallModalActionBtn("Đóng", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnClose.setOnAction(e -> frame.getInstance().closeOverlayModal());

        Button btnEdit = getSmallModalActionBtn("Sửa thông tin bộ phận", "#5020d8", "white", "rgba(80,32,216,0.4)");
        btnEdit.setOnAction(e -> {
            StackPane editModal = createDeptFormModal("Sửa thông tin bộ phận", name, desc, date, true);
            frame.getInstance().showCustomModal(editModal);
        });

        actions.getChildren().addAll(btnClose, btnEdit);
        modalContent.getChildren().addAll(header, infoBox, bottomView, actions);
        rootModalPane.getChildren().add(modalContent);

        return rootModalPane;
    }

    private HBox createSimpleMemberRow(String name, String role) {
        HBox row = new HBox(8);
        row.setPadding(new Insets(8));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-border-color: transparent transparent rgba(0,0,0,0.05) transparent; -fx-border-width: 1px;");

        Label lblAvatar = format.formatLabel("👤", FontWeight.NORMAL, 13, "#64748b");
        Label lblName = format.formatLabel(name, FontWeight.BOLD, 10, "#1e293b");
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        String roleBg = role.equals("Trưởng ban") ? "rgba(16,185,129,0.15)" : "rgba(100,116,139,0.15)";
        String roleText = role.equals("Trưởng ban") ? "#10b981" : "#64748b";

        Label badge = format.formatBadge(role, roleBg, roleText);
        badge.setStyle(badge.getStyle() + "-fx-font-size: 9px; -fx-padding: 3 8;");
        row.getChildren().addAll(lblAvatar, lblName, spacer, badge);
        return row;
    }

    private StackPane createDeptFormModal(String titleText, String initName, String initDesc, String initDate, boolean isEdit) {
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

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(16, 0, 0, 0));

        Button btnCancel = getModalActionBtn("Huỷ", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> frame.getInstance().closeOverlayModal());

        Button btnConfirm = getModalActionBtn("Xác nhận", "#5020d8", "white", "rgba(80,32,216,0.4)");
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

        Label title = format.formatLabel("Bổ nhiệm trưởng ban", FontWeight.BLACK, 20, "#1e293b");
        title.setWrapText(true);

        Label warning = format.formatLabel("Trưởng ban cũ (" + currentLeader + ") sẽ chuyển thành thành viên.", FontWeight.MEDIUM, 12, "#94a3b8");
        warning.setWrapText(true);
        box.getChildren().addAll(title, warning);

        ComboBox<String> cbMembers = format.formatSortBtn("Chọn thành viên", "Lê Văn B", "Trần Văn C", "Chưa phân công");
        cbMembers.setPrefWidth(Double.MAX_VALUE);
        fixHover(cbMembers);
        box.getChildren().add(cbMembers);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(16, 0, 0, 0));

        Button btnCancel = getModalActionBtn("Huỷ", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> frame.getInstance().closeOverlayModal());

        Button btnConfirm = getModalActionBtn("Xác nhận", "#5020d8", "white", "rgba(80,32,216,0.4)");
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
                Label w2 = format.formatLabel("• Bộ phận đang có trưởng ban.", FontWeight.BOLD, 13, "#475569");
                w2.setWrapText(true);
                Label d2 = format.formatLabel("Vui lòng miễn nhiệm vị trí trưởng ban trước.", FontWeight.MEDIUM, 12, "#94a3b8");
                d2.setWrapText(true);
                warningBox.getChildren().addAll(w2, d2);
            }
            box.getChildren().add(warningBox);

            HBox actions = new HBox(12);
            actions.setAlignment(Pos.CENTER);
            actions.setPadding(new Insets(16, 0, 0, 0));
            Button btnClose = getModalActionBtn("Đóng", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
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
            actions.setAlignment(Pos.CENTER);
            actions.setPadding(new Insets(16, 0, 0, 0));

            Button btnCancel = getModalActionBtn("Huỷ", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
            btnCancel.setOnAction(e -> frame.getInstance().closeOverlayModal());

            Button btnConfirm = getModalActionBtn("Xác nhận", "#ef4444", "white", "rgba(239,68,68,0.4)");
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

    private Button getSmallModalActionBtn(String text, String bgColor, String textColor, String shadowColor) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(32);
        HBox.setHgrow(btn, Priority.ALWAYS);
        btn.setFont(Font.font("Google Sans", FontWeight.BOLD, 9));
        btn.setTextFill(Color.web(textColor));
        btn.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 14px; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, " + shadowColor + ", 7, 0, 0, 3);");
        btn.setOnMouseEntered(e -> { btn.setScaleX(1.02); btn.setScaleY(1.02); });
        btn.setOnMouseExited(e -> { btn.setScaleX(1.0); btn.setScaleY(1.0); });
        return btn;
    }
}