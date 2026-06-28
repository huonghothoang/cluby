package view.president;

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

public class attendance extends ScrollPane {

    public attendance() {
        VBox mainContent = new VBox(24);
        mainContent.setPadding(new Insets(32));
        mainContent.setStyle("-fx-background-color: transparent;");

        HBox kpiRow = new HBox(16);
        kpiRow.getChildren().addAll(
                format.formatKPICard("Tổng số", "24", "#64748b", "#1e293b"),
                format.formatKPICard("Hoàn thành", "18", "#10b981", "#1e293b"),
                format.formatKPICard("Đang diễn ra", "2", "#f59e0b", "#1e293b"),
                format.formatKPICard("Chưa bắt đầu", "4", "#94a3b8", "#1e293b")
        );
        for (javafx.scene.Node node : kpiRow.getChildren()) { HBox.setHgrow(node, Priority.ALWAYS); }

        HBox filterBar = new HBox(16);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(12, 24, 12, 24));
        format.formatGlass(filterBar, 40, 0.4);

        HBox searchBox = new HBox(8);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = format.formatTextField("Tìm kiếm...");
        Button btnSearch = format.formatFindBtn();
        searchBox.getChildren().addAll(searchField, btnSearch);

        ComboBox<String> cbStatus = format.formatSortBtn("Trạng thái", "Tất cả", "Chưa bắt đầu", "Đang diễn ra", "Hoàn thành");
        ComboBox<String> cbTime = format.formatSortBtn("Thời gian", "Tất cả", "Tháng này", "Quý này", "Năm nay");

        filterBar.getChildren().addAll(searchBox, cbStatus, cbTime);

        VBox tableContainer = format.formatTableContainer();
        tableContainer.getChildren().add(createTableHeader());

        tableContainer.getChildren().addAll(
                createEventRow("Tech Share #01: Git & GitHub", "28/06/2026", "08:00 - 11:30", 54, "Hoàn thành"),
                createEventRow("Networking Day", "26/06/2026", "19:00 - 21:00", 128, "Đang diễn ra"),
                createEventRow("Team Building", "12/07/2026", "07:00 - 17:00", 120, "Chưa bắt đầu")
        );

        mainContent.getChildren().addAll(kpiRow, filterBar, tableContainer);
        format.formatScrollbar(this, mainContent, 12);
        this.setContent(mainContent);
    }

    private HBox createTableHeader() {
        HBox header = new HBox(16);
        header.setPadding(new Insets(12, 16, 12, 16));
        header.setStyle("-fx-border-color: transparent transparent rgba(255,255,255,0.4) transparent; -fx-border-width: 1px;");

        Label l1 = format.formatLabel("SỰ KIỆN", FontWeight.BLACK, 10, "#94a3b8"); l1.setPrefWidth(250);
        Label l2 = format.formatLabel("THỜI GIAN", FontWeight.BLACK, 10, "#94a3b8"); l2.setPrefWidth(180);
        Label l3 = format.formatLabel("NHÂN SỰ", FontWeight.BLACK, 10, "#94a3b8"); l3.setPrefWidth(120);
        Label l4 = format.formatLabel("TRẠNG THÁI", FontWeight.BLACK, 10, "#94a3b8"); l4.setPrefWidth(150);
        Label l5 = format.formatLabel("HÀNH ĐỘNG", FontWeight.BLACK, 10, "#94a3b8"); l5.setPrefWidth(60);

        header.getChildren().addAll(l1, l2, l3, l4, l5);
        return header;
    }

    private HBox createEventRow(String name, String date, String time, int count, String status) {
        HBox row = new HBox(16);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand;");
        row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand; -fx-background-radius: 16px;"));
        row.setOnMouseExited(e -> row.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand;"));

        Label lblName = format.formatLabel(name, FontWeight.BOLD, 14, "#1e293b"); lblName.setPrefWidth(250);
        Label lblTime = format.formatLabel(date + " | " + time, FontWeight.MEDIUM, 13, "#475569"); lblTime.setPrefWidth(180);
        Label lblCount = format.formatLabel(count + " tv", FontWeight.BOLD, 13, "#7c4dff"); lblCount.setPrefWidth(120);

        String stBg = status.equals("Hoàn thành") ? "rgba(16,185,129,0.15)" : status.equals("Đang diễn ra") ? "rgba(245,158,11,0.15)" : "rgba(100,116,139,0.15)";
        String stText = status.equals("Hoàn thành") ? "#10b981" : status.equals("Đang diễn ra") ? "#f59e0b" : "#64748b";
        HBox statusBox = new HBox(format.formatBadge(status, stBg, stText)); statusBox.setAlignment(Pos.CENTER_LEFT); statusBox.setPrefWidth(150);

        Button btnView = format.formatCircleBtn("👁️‍🗨️", "#448aff", "#7c4dff");
        btnView.setOnAction(e -> {
            StackPane sessionModal = createAttendanceSession(name, date, time, count, status);
            frame.getInstance().showCustomModal(sessionModal);
        });
        HBox actionBox = new HBox(btnView); actionBox.setAlignment(Pos.CENTER_LEFT); actionBox.setPrefWidth(60);

        row.getChildren().addAll(lblName, lblTime, lblCount, statusBox, actionBox);
        return row;
    }

    private StackPane createAttendanceSession(String eventName, String date, String time, int totalMembers, String initStatus) {
        StackPane rootModalPane = new StackPane();

        VBox box = new VBox(20);
        box.setPrefWidth(840);
        box.setMaxSize(840, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        boolean isFinished = initStatus.equals("Hoàn thành");

        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(4);
        titleBox.getChildren().addAll(
                format.formatLabel(eventName, FontWeight.BLACK, 24, "#1e293b"),
                format.formatLabel(date + "   " + time + "   " + totalMembers + " tv", FontWeight.BOLD, 13, "#64748b")
        );

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        String stBg = isFinished ? "rgba(16,185,129,0.15)" : initStatus.equals("Đang diễn ra") ? "rgba(245,158,11,0.15)" : "rgba(100,116,139,0.15)";
        String stText = isFinished ? "#10b981" : initStatus.equals("Đang diễn ra") ? "#f59e0b" : "#64748b";
        Label statusBadge = format.formatBadge(initStatus, stBg, stText);
        statusBadge.setStyle(statusBadge.getStyle() + "-fx-font-size: 14px; -fx-padding: 8 16;");

        header.getChildren().addAll(titleBox, spacer, statusBadge);

        VBox statsBox = new VBox(12);
        if (isFinished) {
            statsBox.setPadding(new Insets(16));
            statsBox.setStyle("-fx-background-color: rgba(248, 250, 252, 0.8); -fx-background-radius: 20px; -fx-border-color: rgba(226, 232, 240, 1); -fx-border-radius: 20px;");

            statsBox.getChildren().add(format.formatLabel("TỔNG KẾT", FontWeight.BLACK, 12, "#94a3b8"));
            HBox statsRow = new HBox(32);
            statsRow.getChildren().addAll(
                    new VBox(4, format.formatLabel("Tổng số", FontWeight.BOLD, 12, "#64748b"), format.formatLabel(String.valueOf(totalMembers), FontWeight.BLACK, 20, "#1e293b")),
                    new VBox(4, format.formatLabel("Có mặt", FontWeight.BOLD, 12, "#10b981"), format.formatLabel("48", FontWeight.BLACK, 20, "#10b981")),
                    new VBox(4, format.formatLabel("Có phép", FontWeight.BOLD, 12, "#f59e0b"), format.formatLabel("2", FontWeight.BLACK, 20, "#f59e0b")),
                    new VBox(4, format.formatLabel("Vắng", FontWeight.BOLD, 12, "#ef4444"), format.formatLabel("4", FontWeight.BLACK, 20, "#ef4444"))
            );
            statsBox.getChildren().add(statsRow);
        }

        HBox toolbar = new HBox(12);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        TextField fSearch = format.formatTextField("Tìm kiếm...");
        ComboBox<String> cbDept = format.formatSortBtn("Bộ phận", "Tất cả", "Nội dung", "Kỹ thuật", "Truyền thông", "Hậu cần");
        ComboBox<String> cbState = format.formatSortBtn("Trạng thái", "Tất cả", "Chưa đến", "Có mặt", "Vắng", "Có phép");

        toolbar.getChildren().addAll(fSearch, cbDept, cbState);

        Region tbSpacer = new Region(); HBox.setHgrow(tbSpacer, Priority.ALWAYS);
        toolbar.getChildren().add(tbSpacer);

        if (!isFinished) {
            Button btnMarkAll = getShadowBtn("Chọn tất cả có mặt", "✓", "rgba(16,185,129,0.15)", "#10b981", "rgba(0,0,0,0.05)");
            btnMarkAll.setOnAction(e -> frame.getInstance().triggerToast("Đã chọn tất cả có mặt"));
            toolbar.getChildren().add(btnMarkAll);
        }

        VBox listContainer = format.formatBoxCard();
        listContainer.setPadding(new Insets(16, 8, 16, 16));

        HBox listHeader = new HBox(16);
        listHeader.setPadding(new Insets(0, 16, 12, 16));
        listHeader.setStyle("-fx-border-color: transparent transparent rgba(0,0,0,0.05) transparent; -fx-border-width: 1px;");
        Label l1 = format.formatLabel("LOGO", FontWeight.BLACK, 10, "#94a3b8"); l1.setPrefWidth(50);
        Label l2 = format.formatLabel("NHÂN SỰ", FontWeight.BLACK, 10, "#94a3b8"); l2.setPrefWidth(220);
        Label l3 = format.formatLabel("BỘ PHẬN", FontWeight.BLACK, 10, "#94a3b8"); l3.setPrefWidth(120);
        Label l4 = format.formatLabel("TRẠNG THÁI", FontWeight.BLACK, 10, "#94a3b8"); l4.setPrefWidth(140);
        listHeader.getChildren().addAll(l1, l2, l3, l4);
        listContainer.getChildren().add(listHeader);

        VBox rows = new VBox(4);
        rows.getChildren().addAll(
                createMemberRow("temp.png", "Nguyễn Văn A", "Truyền thông", isFinished ? "Có mặt" : "Chưa đến", isFinished),
                createMemberRow("temp.png", "Lê Văn B", "Kỹ thuật", isFinished ? "Vắng" : "Chưa đến", isFinished),
                createMemberRow("temp.png", "Trần Thị C", "Nội dung", isFinished ? "Có phép" : "Có mặt", isFinished),
                createMemberRow("temp.png", "Hoàng Hữu D", "Hậu cần", isFinished ? "Có mặt" : "Chưa đến", isFinished),
                createMemberRow("temp.png", "Phạm Văn E", "Chưa phân ban", isFinished ? "Có mặt" : "Vắng", isFinished)
        );

        ScrollPane scrollList = new ScrollPane(rows);
        scrollList.setPrefHeight(280);
        format.formatScrollbar(scrollList, rows, 8);
        listContainer.getChildren().add(scrollList);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnClose = getShadowBtn("Đóng", "", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnClose.setOnAction(e -> frame.getInstance().closeOverlayModal());

        if (!isFinished) {
            Button btnFinish = getShadowBtn("Kết thúc", "🔒", "#5020d8", "white", "rgba(80,32,216,0.4)");
            btnFinish.setOnAction(e -> {
                VBox confirmModal = createConfirmFinishModal(eventName, rootModalPane, box);
                rootModalPane.getChildren().setAll(confirmModal);
            });
            actions.getChildren().addAll(btnClose, btnFinish);
        } else {
            actions.getChildren().add(btnClose);
        }

        if (isFinished) box.getChildren().add(statsBox);
        box.getChildren().addAll(header, toolbar, listContainer, actions);
        rootModalPane.getChildren().add(box);
        return rootModalPane;
    }

    private HBox createMemberRow(String avatarUrl, String name, String dept, String currentStatus, boolean isFinished) {
        HBox row = new HBox(16);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-border-color: transparent transparent rgba(0,0,0,0.05) transparent; -fx-border-width: 1px;");

        ImageView avatar = new ImageView(new Image(avatarUrl));
        avatar.setFitWidth(36); avatar.setFitHeight(36); avatar.setClip(new Circle(18, 18, 18));
        HBox avatarBox = new HBox(avatar); avatarBox.setPrefWidth(50);

        Label lblName = format.formatLabel(name, FontWeight.BOLD, 14, "#1e293b"); lblName.setPrefWidth(220);
        Label lblDept = format.formatLabel(dept, FontWeight.MEDIUM, 13, "#64748b"); lblDept.setPrefWidth(120);

        HBox statusBox = new HBox();
        statusBox.setPrefWidth(140);

        if (isFinished) {
            String stBg = currentStatus.equals("Có mặt") ? "rgba(16,185,129,0.15)" : currentStatus.equals("Có phép") ? "rgba(245,158,11,0.15)" : "rgba(239,68,68,0.15)";
            String stText = currentStatus.equals("Có mặt") ? "#10b981" : currentStatus.equals("Có phép") ? "#f59e0b" : "#ef4444";
            statusBox.getChildren().add(format.formatBadge(currentStatus, stBg, stText));
        } else {
            ComboBox<String> cbStatus = format.formatSortBtn("", "Chưa đến", "Có mặt", "Vắng", "Có phép");
            cbStatus.setValue(currentStatus);
            cbStatus.setPrefWidth(140);

            cbStatus.setOnAction(e -> {
                String val = cbStatus.getValue();
                if(val.equals("Có mặt")) cbStatus.setStyle(cbStatus.getStyle() + "-fx-text-fill: #10b981;");
                else if(val.equals("Vắng")) cbStatus.setStyle(cbStatus.getStyle() + "-fx-text-fill: #ef4444;");
                else if(val.equals("Có phép")) cbStatus.setStyle(cbStatus.getStyle() + "-fx-text-fill: #f59e0b;");
                else cbStatus.setStyle(cbStatus.getStyle() + "-fx-text-fill: #64748b;");
            });
            cbStatus.getOnAction().handle(null);

            statusBox.getChildren().add(cbStatus);
        }

        row.getChildren().addAll(avatarBox, lblName, lblDept, statusBox);
        return row;
    }

    private VBox createConfirmFinishModal(String eventName, StackPane rootModalPane, VBox previousView) {
        VBox box = new VBox(20);
        box.setPrefWidth(450);
        box.setMaxSize(450, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel("Chốt danh sách", FontWeight.BLACK, 20, "#ef4444");

        VBox warningBox = new VBox(8);
        warningBox.setPadding(new Insets(16));
        warningBox.setStyle("-fx-background-color: rgba(239, 68, 68, 0.1); -fx-background-radius: 16px;");
        Label wTitle = format.formatLabel("Cảnh báo: Còn thành viên chưa điểm danh", FontWeight.BOLD, 13, "#ef4444");
        Label wDesc = format.formatLabel("Hệ thống tự động chuyển trạng thái chưa điểm danh thành 'Vắng'. Dữ liệu sau khi khóa sẽ không thể chỉnh sửa.", FontWeight.MEDIUM, 12, "#dc2626");
        wDesc.setWrapText(true);
        warningBox.getChildren().addAll(wTitle, wDesc);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnCancel = getShadowBtn("Quay lại", "", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> rootModalPane.getChildren().setAll(previousView));

        Button btnConfirm = getShadowBtn("Xác nhận khóa", "🔒", "#ef4444", "white", "rgba(239,68,68,0.4)");
        btnConfirm.setOnAction(e -> {
            frame.getInstance().closeOverlayModal();
            frame.getInstance().triggerToast("Đã chốt sổ điểm danh");
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().addAll(title, warningBox, actions);

        return box;
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