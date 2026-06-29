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

public class department extends ScrollPane {

    public department() {
        VBox mainContent = new VBox(24);
        mainContent.setPadding(new Insets(32));
        mainContent.setStyle("-fx-background-color: transparent;");

        setFixedWidth(mainContent, 1000);

        mainContent.getChildren().addAll(
                buildHeader(),
                buildKPIs(),
                buildRow1(),
                buildRow2()
        );

        format.formatScrollbar(this, mainContent, 12);
        this.setContent(mainContent);
    }

    private void invokeShowModal(Node modal) {
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

    private void invokeToast(String msg) {
        if (view.head.frame.getInstance() != null) {
            view.head.frame.getInstance().triggerToast(msg);
        } else if (view.mem.frame.getInstance() != null) {
            view.mem.frame.getInstance().triggerToast(msg);
        }
    }

    private void setFixedWidth(Region r, double w) {
        r.setPrefWidth(w);
        r.setMinWidth(w);
        r.setMaxWidth(w);
        r.setMinHeight(Region.USE_PREF_SIZE);
        if (r instanceof Label) {
            ((Label) r).setWrapText(true);
        }
    }

    private void setFixedSquare(Region r, double size) {
        r.setPrefWidth(size);
        r.setMinWidth(size);
        r.setMaxWidth(size);
        r.setPrefHeight(size);
        r.setMinHeight(size);
        r.setMaxHeight(size);
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

    private HBox buildHeader() {
        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        setFixedWidth(header, 1000);

        Label title = format.formatLabel("Ban Truyền thông", FontWeight.BLACK, 32, "#1e293b");

        Label badge1 = format.formatBadge("24 thành viên", "rgba(124,77,255,0.15)", "#7c4dff");
        badge1.setStyle(badge1.getStyle() + "-fx-font-size: 14px; -fx-padding: 8 16;");

        Label badge2 = format.formatBadge("Đang hoạt động", "rgba(16,185,129,0.15)", "#10b981");
        badge2.setStyle(badge2.getStyle() + "-fx-font-size: 14px; -fx-padding: 8 16;");

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(title, badge1, badge2, spacer);

        return header;
    }

    private HBox buildKPIs() {
        HBox kpiRow = new HBox(16);
        setFixedWidth(kpiRow, 1000);

        VBox k1 = createStatCard("Thành viên", "24", "#475569", "#1e293b", "#a5b4fc", "👥");
        VBox k2 = createStatCard("Công việc đang làm", "8", "#475569", "#3b82f6", "#bfdbfe", "⏳");
        VBox k3 = createStatCard("Sự kiện tham gia", "12", "#475569", "#f59e0b", "#fde68a", "📅");
        VBox k4 = createStatCard("Tỷ lệ điểm danh", "92%", "#475569", "#10b981", "#a7f3d0", "🎯");

        HBox.setHgrow(k1, Priority.ALWAYS); HBox.setHgrow(k2, Priority.ALWAYS);
        HBox.setHgrow(k3, Priority.ALWAYS); HBox.setHgrow(k4, Priority.ALWAYS);

        kpiRow.getChildren().addAll(k1, k2, k3, k4);
        return kpiRow;
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

    private HBox buildRow1() {
        HBox row = new HBox(24);
        setFixedWidth(row, 1000);

        VBox infoBox = format.formatBoxCard();
        setFixedWidth(infoBox, 1000);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        HBox infoHeader = new HBox();
        infoHeader.setAlignment(Pos.CENTER_LEFT);
        Label infoTitle = format.formatLabel("THÔNG TIN BAN", FontWeight.BLACK, 14, "#94a3b8");
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnEdit = getFormBtn("Sửa mô tả", "rgba(124,77,255,0.15)", "#5020d8");
        btnEdit.setOnAction(e -> showEditDescModal());

        infoHeader.getChildren().addAll(infoTitle, spacer, btnEdit);

        GridPane grid = new GridPane();
        grid.setHgap(32); grid.setVgap(16);
        grid.add(new VBox(4, format.formatLabel("Tên ban", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel("Truyền thông", FontWeight.BLACK, 16, "#1e293b")), 0, 0);
        grid.add(new VBox(4, format.formatLabel("Ngày thành lập", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel("20/09/2024", FontWeight.BOLD, 14, "#475569")), 1, 0);
        grid.add(new VBox(4, format.formatLabel("Trưởng ban", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel("Nguyễn Văn A", FontWeight.BOLD, 14, "#1e293b")), 0, 1);

        Label lblDesc = format.formatLabel("Phụ trách toàn bộ hình ảnh truyền thông và nhận diện thương hiệu của câu lạc bộ. Lên ý tưởng, thiết kế ấn phẩm và quản lý các mạng xã hội.", FontWeight.MEDIUM, 14, "#475569");
        setFixedWidth(lblDesc, 944);
        grid.add(new VBox(4, format.formatLabel("Mô tả", FontWeight.BOLD, 12, "#94a3b8"), lblDesc), 0, 2, 2, 1);

        infoBox.getChildren().addAll(infoHeader, grid);

        row.getChildren().add(infoBox);
        return row;
    }

    private VBox buildRow2() {
        VBox memBox = format.formatBoxCard();
        setFixedWidth(memBox, 1000);
        Label topTitle = format.formatLabel("NỔI BẬT HÔM NAY", FontWeight.BLACK, 14, "#94a3b8");
        memBox.getChildren().add(topTitle);

        HBox rowCards = new HBox(24);
        rowCards.setAlignment(Pos.CENTER_LEFT);
        setFixedWidth(rowCards, 944);

        VBox top1Card = createTopMemberCard(1, "trish.jpeg", "Nguyễn Văn A", 4, 12, "100%");
        setFixedSquare(top1Card, 298);

        VBox top2Card = createTopMemberCard(2, "trish.jpeg", "Trần Văn B", 3, 10, "95%");
        setFixedSquare(top2Card, 298);

        VBox top3Card = createTopMemberCard(3, "trish.jpeg", "Lê Văn C", 2, 9, "90%");
        setFixedSquare(top3Card, 298);

        rowCards.getChildren().addAll(top1Card, top2Card, top3Card);
        memBox.getChildren().addAll(rowCards);
        return memBox;
    }

    private VBox createTopMemberCard(int rank, String avatarUrl, String name, int doing, int events, String attendance) {
        VBox card = new VBox(12);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(16));
        card.setStyle("-fx-cursor: hand;");

        if (rank == 1) {
            card.setStyle(card.getStyle() + "-fx-background-color: linear-gradient(to bottom, #fffbeb, #fef3c7); -fx-background-radius: 24px; -fx-border-color: rgba(245,158,11,0.6); -fx-border-width: 2px; -fx-border-radius: 24px; -fx-effect: dropshadow(three-pass-box, rgba(245,158,11,0.4), 15, 0, 0, 4);");
        } else if (rank == 2) {
            card.setStyle(card.getStyle() + "-fx-background-color: linear-gradient(to bottom, #f8fafc, #f1f5f9); -fx-background-radius: 24px; -fx-border-color: rgba(148,163,184,0.6); -fx-border-width: 2px; -fx-border-radius: 24px; -fx-effect: dropshadow(three-pass-box, rgba(148,163,184,0.3), 15, 0, 0, 4);");
        } else {
            card.setStyle(card.getStyle() + "-fx-background-color: linear-gradient(to bottom, #fff7ed, #ffedd5); -fx-background-radius: 24px; -fx-border-color: rgba(217,119,6,0.6); -fx-border-width: 2px; -fx-border-radius: 24px; -fx-effect: dropshadow(three-pass-box, rgba(217,119,6,0.3), 15, 0, 0, 4);");
        }

        card.setOnMouseEntered(e -> { card.setScaleX(1.02); card.setScaleY(1.02); });
        card.setOnMouseExited(e -> { card.setScaleX(1.0); card.setScaleY(1.0); });
        card.setOnMouseClicked(e -> invokeToast("Sẽ chuyển sang hồ sơ của " + name));

        VBox profileBox = new VBox(6);
        profileBox.setAlignment(Pos.CENTER);

        String rankText = rank == 1 ? "👑 TOP 1" : rank == 2 ? "🥈 TOP 2" : "🥉 TOP 3";
        String rankColor = rank == 1 ? "#d97706" : rank == 2 ? "#475569" : "#9a3412";
        String rankBg = rank == 1 ? "rgba(245,158,11,0.3)" : rank == 2 ? "rgba(148,163,184,0.3)" : "rgba(217,119,6,0.3)";

        Label lblRank = format.formatLabel(rankText, FontWeight.BLACK, 12, rankColor);
        StackPane rankBadge = new StackPane(lblRank);
        rankBadge.setPadding(new Insets(4, 12, 4, 12));
        rankBadge.setStyle("-fx-background-color: " + rankBg + "; -fx-background-radius: 20px;");

        ImageView avt = new ImageView(new Image(avatarUrl));
        avt.setFitWidth(70); avt.setFitHeight(70);
        avt.setClip(new Circle(35, 35, 35));

        profileBox.getChildren().addAll(rankBadge, avt);

        Label lblName = format.formatLabel(name, FontWeight.BLACK, 16, "#1e293b");
        setFixedWidth(lblName, 200);
        lblName.setAlignment(Pos.CENTER);
        lblName.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        VBox vStats = new VBox(4);
        vStats.setAlignment(Pos.CENTER);
        vStats.getChildren().addAll(
                createMiniStatRow("Việc đang làm:", String.valueOf(doing), "#3b82f6"),
                createMiniStatRow("Sự kiện tham gia:", String.valueOf(events), "#10b981"),
                createMiniStatRow("Tỷ lệ điểm danh:", attendance, "#7c4dff")
        );

        card.getChildren().addAll(profileBox, lblName, vStats);
        return card;
    }

    private HBox createMiniStatRow(String title, String val, String color) {
        HBox box = new HBox(6);
        box.setAlignment(Pos.CENTER);
        Label lblTitle = format.formatLabel(title, FontWeight.BOLD, 11, "#94a3b8");
        Label lblVal = format.formatLabel(val, FontWeight.BLACK, 12, color);
        box.getChildren().addAll(lblTitle, lblVal);
        return box;
    }

    private void showEditDescModal() {
        StackPane rootModal = new StackPane();
        VBox box = new VBox(20);
        setFixedWidth(box, 500);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        box.getChildren().add(format.formatLabel("Chỉnh sửa Mô tả Ban", FontWeight.BLACK, 20, "#1e293b"));

        TextArea taDesc = new TextArea("Phụ trách toàn bộ hình ảnh truyền thông và nhận diện thương hiệu của câu lạc bộ. Lên ý tưởng, thiết kế ấn phẩm và quản lý các mạng xã hội.");
        taDesc.setWrapText(true);
        taDesc.setPrefRowCount(4);
        taDesc.setStyle("-fx-background-radius: 12px; -fx-border-radius: 12px; -fx-font-family: 'Google Sans'; -fx-font-size: 14px; -fx-padding: 8;");

        box.getChildren().add(new VBox(8, format.formatLabel("Nội dung mô tả", FontWeight.BOLD, 12, "#94a3b8"), taDesc));

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnCancel = getFormBtn("Hủy", "rgba(178, 162, 228, 0.2)", "#64748b");
        btnCancel.setOnAction(e -> invokeCloseModal());

        Button btnSave = getFormBtn("Lưu thay đổi", "#5020d8", "white");
        btnSave.setOnAction(e -> {
            invokeCloseModal();
            invokeToast("Đã cập nhật mô tả Ban thành công!");
        });

        actions.getChildren().addAll(btnCancel, btnSave);
        box.getChildren().add(actions);

        rootModal.getChildren().add(box);
        invokeShowModal(rootModal);
    }

    private Button getFormBtn(String text, String bgColor, String textColor) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));
        btn.setTextFill(Color.web(textColor));
        btn.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 40px; -fx-padding: 8 16 8 16; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 4);");
        btn.setOnMouseEntered(e -> { btn.setScaleX(1.05); btn.setScaleY(1.05); });
        btn.setOnMouseExited(e -> { btn.setScaleX(1.0); btn.setScaleY(1.0); });
        return btn;
    }
}