package view;

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

public class member extends ScrollPane {

    public member() {
        VBox mainContent = new VBox(24);
        mainContent.setPadding(new Insets(32));
        mainContent.setStyle("-fx-background-color: transparent;");
        mainContent.setMaxWidth(1000);

        HBox kpiRow = new HBox(16);
        VBox card1 = createStatCard("Tổng số", "120", "#475569", "#1e293b", "#a5b4fc", "👤");
        VBox card2 = createStatCard("Hoạt động", "100", "#475569", "#1e293b", "#6ee7b7", "✔");
        VBox card3 = createStatCard("Tạm nghỉ", "15", "#475569", "#1e293b", "#fde68a", "⏸");
        VBox card4 = createStatCard("Đã rời", "5", "#475569", "#1e293b", "#cbd5e1", "✖");

        HBox.setHgrow(card1, Priority.ALWAYS); HBox.setHgrow(card2, Priority.ALWAYS);
        HBox.setHgrow(card3, Priority.ALWAYS); HBox.setHgrow(card4, Priority.ALWAYS);
        kpiRow.getChildren().addAll(card1, card2, card3, card4);

        HBox filterBar = new HBox(16);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(12, 24, 12, 24));
        format.formatGlass(filterBar, 40, 0.4);

        HBox searchBox = new HBox(8);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = format.formatTextField("Tìm tên...");
        Button btnSearch = format.formatFindBtn();
        searchBox.getChildren().addAll(searchField, btnSearch);

        ComboBox<String> cbBan = format.formatSortBtn("Bộ phận", "Tất cả", "Nội dung", "Kỹ thuật", "Truyền thông", "Hậu cần", "Chưa phân ban");
        ComboBox<String> cbRole = format.formatSortBtn("Chức vụ", "Tất cả", "Hội trưởng", "Trưởng ban", "Thành viên");
        ComboBox<String> cbStatus = format.formatSortBtn("Trạng thái", "Tất cả", "Hoạt động", "Tạm nghỉ", "Đã rời");

        fixHover(cbBan); fixHover(cbRole); fixHover(cbStatus);

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        filterBar.getChildren().addAll(searchBox, cbBan, cbRole, cbStatus, spacer);

        VBox tableContainer = format.formatTableContainer();
        tableContainer.getChildren().add(createMemberTableHeader());

        VBox rows = new VBox(4);
        rows.getChildren().addAll(
                createMemberRow("temp.png", "Nguyễn Minh Quân", "24KT001", "quan.nm@gmail.com", "0901234567", "Chưa phân ban", "Hội trưởng", "01/01/2024", "Hoạt động", "Chịu trách nhiệm điều hành chung.", 0, 15, 15, 0),
                createMemberRow("temp.png", "Trần Thu Hà", "24ND015", "ha.tt@gmail.com", "0912345678", "Nội dung", "Trưởng ban", "15/03/2024", "Hoạt động", "", 2, 10, 12, 0),
                createMemberRow("temp.png", "Lê Đức Anh", "25KT022", "anh.ld@gmail.com", "0923456789", "Kỹ thuật", "Thành viên", "10/10/2025", "Tạm nghỉ", "Nghỉ cá nhân.", 0, 4, 4, 0)
        );

        ScrollPane scrollList = new ScrollPane(rows);
        scrollList.setPrefHeight(450);
        format.formatScrollbar(scrollList, rows, 8);
        applySmoothScroll(scrollList, rows);

        tableContainer.getChildren().add(scrollList);

        mainContent.getChildren().addAll(kpiRow, filterBar, tableContainer);
        format.formatScrollbar(this, mainContent, 12);
        this.setContent(mainContent);
    }

    private void invokeShowModal(VBox modal) {
        if (view.head.frame.getInstance() != null) {
            view.head.frame.getInstance().showCustomModal(modal);
        } else if (view.mem.frame.getInstance() != null) {
            view.mem.frame.getInstance().showCustomModal(modal);
        }
    }

    private void invokeCloseModal() {
        if (view.head.frame.getInstance() != null) {
            view.head.frame.getInstance().closeOverlayModal();
        } else if (view.mem.frame.getInstance() != null) {
            view.mem.frame.getInstance().closeOverlayModal();
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
                if (contentHeight > viewportHeight) scroll.setVvalue(vvalue - delta / (contentHeight - viewportHeight));
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

    private HBox createMemberTableHeader() {
        HBox header = new HBox(16);
        header.setPadding(new Insets(12, 16, 12, 16));
        header.setStyle("-fx-border-color: transparent transparent rgba(255,255,255,0.4) transparent; -fx-border-width: 1px;");

        Label l1 = format.formatLabel("LOGO", FontWeight.BLACK, 10, "#94a3b8"); l1.setPrefWidth(60);
        Label l2 = format.formatLabel("THÀNH VIÊN", FontWeight.BLACK, 10, "#94a3b8"); l2.setPrefWidth(220);
        Label l3 = format.formatLabel("BỘ PHẬN", FontWeight.BLACK, 10, "#94a3b8"); l3.setPrefWidth(140);
        Label l4 = format.formatLabel("CHỨC VỤ", FontWeight.BLACK, 10, "#94a3b8"); l4.setPrefWidth(120);
        Label l5 = format.formatLabel("TRẠNG THÁI", FontWeight.BLACK, 10, "#94a3b8"); l5.setPrefWidth(120);
        Label l6 = format.formatLabel("HÀNH ĐỘNG", FontWeight.BLACK, 10, "#94a3b8"); l6.setPrefWidth(100);

        header.getChildren().addAll(l1, l2, l3, l4, l5, l6);
        return header;
    }

    private HBox createMemberRow(String avatarUrl, String name, String id, String email, String phone, String dept, String role, String date, String status, String note, int dangCo, int daLam, int daNhan, int thieuSot) {
        HBox row = new HBox(16);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand;");

        row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand; -fx-background-radius: 16px;"));
        row.setOnMouseExited(e -> row.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand;"));

        ImageView avatar = new ImageView(new Image(avatarUrl));
        avatar.setFitWidth(40); avatar.setFitHeight(40); avatar.setClip(new Circle(20, 20, 20));
        HBox avatarBox = new HBox(avatar); avatarBox.setPrefWidth(60);

        Label lblName = format.formatLabel(name, FontWeight.BOLD, 14, "#1e293b"); lblName.setPrefWidth(220); lblName.setWrapText(true);
        Label lblBan = format.formatLabel(dept, FontWeight.MEDIUM, 13, "#475569"); lblBan.setPrefWidth(140); lblBan.setWrapText(true);

        HBox roleBox = new HBox(createDynamicBadge(role, true)); roleBox.setAlignment(Pos.CENTER_LEFT); roleBox.setPrefWidth(120);
        HBox statusBox = new HBox(createDynamicBadge(status, false)); statusBox.setAlignment(Pos.CENTER_LEFT); statusBox.setPrefWidth(120);

        HBox actionBox = new HBox(8); actionBox.setAlignment(Pos.CENTER_LEFT); actionBox.setPrefWidth(100);

        Button btnView = format.formatCircleBtn("👁️‍🗨️", "#448aff", "#7c4dff");
        btnView.setOnAction(e -> {
            VBox modal = createMemberProfileModal(avatarUrl, name, id, email, phone, dept, role, date, status, note, dangCo, daLam, daNhan, thieuSot);
            invokeShowModal(modal);
        });

        actionBox.getChildren().addAll(btnView);
        row.getChildren().addAll(avatarBox, lblName, lblBan, roleBox, statusBox, actionBox);

        return row;
    }

    private Label createDynamicBadge(String text, boolean isRoleType) {
        String bg = "rgba(124,77,255,0.1)";
        String fg = "#7c4dff";

        if (isRoleType) {
            if (text.equals("Hội trưởng")) { bg = "rgba(245,158,11,0.15)"; fg = "#f59e0b"; }
            else if (text.equals("Trưởng ban")) { bg = "rgba(16,185,129,0.15)"; fg = "#10b981"; }
        } else {
            if (text.equals("Hoạt động")) { bg = "rgba(16,185,129,0.15)"; fg = "#10b981"; }
            else if (text.equals("Tạm nghỉ")) { bg = "rgba(245,158,11,0.15)"; fg = "#f59e0b"; }
            else if (text.equals("Đã rời") || text.equals("Bị khóa")) { bg = "rgba(239,68,68,0.15)"; fg = "#ef4444"; }
        }
        return format.formatBadge(text, bg, fg);
    }

    private VBox createMemberProfileModal(String avatarUrl, String name, String id, String email, String phone, String dept, String role, String date, String status, String note, int dangCo, int daLam, int daNhan, int thieuSot) {
        VBox rootModal = new VBox();
        rootModal.setAlignment(Pos.CENTER);

        StackPane modalContentStack = new StackPane();

        VBox viewMode = new VBox(12);
        viewMode.setPrefWidth(900);
        viewMode.setMaxWidth(900);
        viewMode.setPadding(new Insets(36));
        viewMode.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        viewMode.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        HBox row1 = new HBox(24);

        VBox part1 = new VBox(16);
        part1.setAlignment(Pos.TOP_CENTER);
        part1.setPrefWidth(200); part1.setMaxWidth(200);

        ImageView avt = new ImageView(new Image(avatarUrl));
        avt.setFitWidth(120); avt.setFitHeight(120); avt.setClip(new Circle(60, 60, 60));

        Label lblHeaderName = format.formatLabel(name, FontWeight.BLACK, 24, "#1e293b");
        lblHeaderName.setWrapText(true); lblHeaderName.setAlignment(Pos.CENTER);
        lblHeaderName.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Label roleBadge = createDynamicBadge(role, true);
        roleBadge.setWrapText(true);
        roleBadge.setStyle(roleBadge.getStyle() + "-fx-font-size: 13px; -fx-padding: 8 16;");

        Label lblDateTitle = format.formatLabel("Ngày gia nhập", FontWeight.BOLD, 11, "#94a3b8"); lblDateTitle.setWrapText(true);
        Label lblDateVal = format.formatLabel(date, FontWeight.BOLD, 14, "#1e293b"); lblDateVal.setWrapText(true);
        VBox dateBox = new VBox(4, lblDateTitle, lblDateVal);
        dateBox.setAlignment(Pos.CENTER);

        part1.getChildren().addAll(avt, lblHeaderName, roleBadge, dateBox);

        VBox part2 = format.formatBoxCard();
        part2.setPrefWidth(280); part2.setMaxWidth(280);
        part2.setPadding(new Insets(24));

        Label lblP2Title = format.formatLabel("THÔNG TIN CÁ NHÂN", FontWeight.BLACK, 13, "#94a3b8"); lblP2Title.setWrapText(true);
        part2.getChildren().add(lblP2Title);

        VBox infoList = new VBox(16);
        infoList.getChildren().addAll(
                new VBox(4, format.formatLabel("Mã định danh", FontWeight.BOLD, 11, "#94a3b8"), format.formatLabel(id, FontWeight.BOLD, 14, "#1e293b")),
                new VBox(4, format.formatLabel("Email", FontWeight.BOLD, 11, "#94a3b8"), format.formatLabel(email, FontWeight.BOLD, 14, "#1e293b")),
                new VBox(4, format.formatLabel("Số điện thoại", FontWeight.BOLD, 11, "#94a3b8"), format.formatLabel(phone, FontWeight.BOLD, 14, "#1e293b")),
                new VBox(4, format.formatLabel("Bộ phận", FontWeight.BOLD, 11, "#94a3b8"), format.formatLabel(dept, FontWeight.BOLD, 14, "#1e293b"))
        );

        Label lStT = format.formatLabel("Trạng thái:", FontWeight.BOLD, 11, "#94a3b8"); lStT.setWrapText(true);
        Label lStV = createDynamicBadge(status, false); lStV.setWrapText(true);
        HBox statusLine = new HBox(8, lStT, lStV);
        statusLine.setAlignment(Pos.CENTER_LEFT);
        infoList.getChildren().add(statusLine);

        part2.getChildren().add(infoList);

        VBox part3 = format.formatBoxCard();
        part3.setPrefWidth(300); part3.setMaxWidth(300);
        part3.setPadding(new Insets(24));

        Label p3Title = format.formatLabel("SỰ KIỆN & CÔNG VIỆC", FontWeight.BLACK, 13, "#94a3b8"); p3Title.setWrapText(true);
        part3.getChildren().add(p3Title);

        GridPane statGrid = new GridPane();
        statGrid.setHgap(20); statGrid.setVgap(16);
        statGrid.add(new VBox(6, format.formatLabel("Đang có", FontWeight.BOLD, 11, "#64748b"), format.formatLabel(String.valueOf(dangCo), FontWeight.BLACK, 24, "#3b82f6")), 0, 0);
        statGrid.add(new VBox(6, format.formatLabel("Đã hoàn thành", FontWeight.BOLD, 11, "#64748b"), format.formatLabel(String.valueOf(daLam), FontWeight.BLACK, 24, "#10b981")), 1, 0);
        statGrid.add(new VBox(6, format.formatLabel("Đã giao", FontWeight.BOLD, 11, "#64748b"), format.formatLabel(String.valueOf(daNhan), FontWeight.BLACK, 24, "#7c4dff")), 0, 1);
        statGrid.add(new VBox(6, format.formatLabel("Quá hạn", FontWeight.BOLD, 11, "#64748b"), format.formatLabel(String.valueOf(thieuSot), FontWeight.BLACK, 24, "#ef4444")), 1, 1);
        part3.getChildren().add(statGrid);

        VBox curWork = new VBox(8, format.formatLabel("Sự kiện ưu tiên hiện tại:", FontWeight.BOLD, 12, "#475569"));
        curWork.setPadding(new Insets(16, 0, 0, 0));
        if (dangCo > 0) {
            Label lblWork = format.formatLabel("• Thiết kế Poster cho Sự kiện mới", FontWeight.BOLD, 14, "#7c4dff");
            lblWork.setWrapText(true); curWork.getChildren().add(lblWork);
        } else {
            Label lblNoWork = format.formatLabel("Không có sự kiện nào.", FontWeight.MEDIUM, 13, "#94a3b8");
            lblNoWork.setWrapText(true); curWork.getChildren().add(lblNoWork);
        }
        part3.getChildren().add(curWork);

        row1.getChildren().addAll(part1, part2, part3);

        VBox row2 = format.formatBoxCard();
        row2.setSpacing(4); row2.setMaxWidth(Double.MAX_VALUE);
        row2.setPadding(new Insets(16, 24, 16, 24));
        Label r2Title = format.formatLabel("GHI CHÚ", FontWeight.BLACK, 13, "#94a3b8"); r2Title.setWrapText(true);
        String displayNote = (note == null || note.isEmpty()) ? "Chưa có ghi chú." : note;
        Label noteLbl = format.formatLabel(displayNote, FontWeight.BOLD, 14, "#1e293b"); noteLbl.setWrapText(true);
        row2.getChildren().addAll(r2Title, noteLbl);

        HBox row3 = new HBox(16);
        row3.setAlignment(Pos.CENTER);
        Button btnClose = getModalActionBtn("Đóng", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnClose.setOnAction(e -> invokeCloseModal());

        row3.getChildren().add(btnClose);
        viewMode.getChildren().addAll(row1, row2, row3);

        modalContentStack.getChildren().add(viewMode);
        rootModal.getChildren().add(modalContentStack);

        return rootModal;
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