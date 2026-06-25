package view.president;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class request extends ScrollPane {

    public request() {
        VBox mainContent = new VBox(24);
        mainContent.setPadding(new Insets(32));
        mainContent.setStyle("-fx-background-color: transparent;");

        // KPI Row Tuyển dụng
        HBox kpiRow = new HBox(16);
        kpiRow.getChildren().addAll(
                createKPICard("📄 Tổng đơn nộp", "124", "#64748b", "#1e293b"),
                createKPICard("⏳ Chờ duyệt", "40", "#f59e0b", "#1e293b"),
                createKPICard("🟢 Đạt vòng đơn", "50", "#10b981", "#1e293b"),
                createKPICard("🔴 Từ chối", "34", "#f43f5e", "#1e293b")
        );
        for(javafx.scene.Node node : kpiRow.getChildren()) { HBox.setHgrow(node, Priority.ALWAYS); }

        // Bảng danh sách ứng viên (Giả lập VBox)
        VBox tableContainer = new VBox(12);
        tableContainer.setStyle("-fx-background-color: rgba(255,255,255,0.5); -fx-background-radius: 40px; -fx-padding: 24; -fx-border-color: rgba(255,255,255,0.6); -fx-border-radius: 40px;");

        HBox header = new HBox(16);
        header.setPadding(new Insets(12, 16, 12, 16));
        header.setStyle("-fx-border-color: transparent transparent rgba(255,255,255,0.4) transparent; -fx-border-width: 1px;");
        header.getChildren().addAll(
                createColLabel("Họ tên ứng viên", 200),
                createColLabel("Chuyên ngành", 150),
                createColLabel("Ngày nộp đơn", 120),
                createColLabel("Nguyện vọng ban", 150),
                createColLabel("Trạng thái", 100),
                createColLabel("Thao tác", 80)
        );

        tableContainer.getChildren().add(header);

        tableContainer.getChildren().addAll(
                createAppRow("Nguyễn Văn D", "CNTT", "24/06/2026", "Ban Kỹ thuật", "Chờ duyệt", "#f59e0b", "rgba(245,158,11,0.1)"),
                createAppRow("Trần Thị G", "Thiết kế Đồ họa", "23/06/2026", "Ban Truyền thông", "Mời phỏng vấn", "#6366f1", "rgba(99,102,241,0.1)"),
                createAppRow("Lê Hoàng Nam", "Quản trị Kinh doanh", "22/06/2026", "Ban Sự kiện", "Đạt", "#10b981", "rgba(16,185,129,0.1)"),
                createAppRow("Vũ Minh Khoa", "An toàn Thông tin", "21/06/2026", "Ban Kỹ thuật", "Từ chối", "#f43f5e", "rgba(244,63,94,0.1)")
        );

        mainContent.getChildren().addAll(kpiRow, tableContainer);

        styleGeminiScrollbar(this, mainContent, 12);
        this.setContent(mainContent);
    }

    private VBox createKPICard(String title, String value, String titleColor, String valColor) {
        VBox box = new VBox(4);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: rgba(255,255,255,0.3); -fx-background-radius: 32px; -fx-border-color: rgba(255,255,255,0.5); -fx-border-radius: 32px;");

        Label lblTitle = new Label(title.toUpperCase());
        lblTitle.setFont(Font.font("Google Sans", FontWeight.BOLD, 10));
        lblTitle.setTextFill(Color.web(titleColor));

        Label lblVal = new Label(value);
        lblVal.setFont(Font.font("Google Sans", FontWeight.BLACK, 24));
        lblVal.setTextFill(Color.web(valColor));

        box.getChildren().addAll(lblTitle, lblVal);
        return box;
    }

    private Label createColLabel(String text, double width) {
        Label lbl = new Label(text.toUpperCase());
        lbl.setFont(Font.font("Google Sans", FontWeight.BLACK, 10));
        lbl.setTextFill(Color.web("#94a3b8"));
        lbl.setPrefWidth(width);
        return lbl;
    }

    private HBox createAppRow(String name, String major, String date, String target, String status, String statusColor, String statusBg) {
        HBox row = new HBox(16);
        row.setPadding(new Insets(16));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent rgba(255,255,255,0.2) transparent; -fx-border-width: 1px; -fx-cursor: hand;");
        row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: rgba(255,255,255,0.4); -fx-border-color: transparent transparent rgba(255,255,255,0.2) transparent; -fx-border-width: 1px; -fx-cursor: hand; -fx-background-radius: 16px;"));
        row.setOnMouseExited(e -> row.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent rgba(255,255,255,0.2) transparent; -fx-border-width: 1px; -fx-cursor: hand;"));

        Label lblName = new Label(name);
        lblName.setFont(Font.font("Google Sans", FontWeight.BOLD, 14));
        lblName.setTextFill(Color.web("#1e293b"));
        lblName.setPrefWidth(200);

        Label lblMajor = new Label(major);
        lblMajor.setFont(Font.font("Google Sans", FontWeight.MEDIUM, 12));
        lblMajor.setTextFill(Color.web("#64748b"));
        lblMajor.setPrefWidth(150);

        Label lblDate = new Label(date);
        lblDate.setFont(Font.font("Google Sans", FontWeight.MEDIUM, 12));
        lblDate.setTextFill(Color.web("#94a3b8"));
        lblDate.setPrefWidth(120);

        Label lblTarget = new Label(target);
        lblTarget.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));
        lblTarget.setTextFill(Color.web("#475569"));
        lblTarget.setPrefWidth(150);

        Label lblStatus = new Label(status);
        lblStatus.setStyle("-fx-background-color: " + statusBg + "; -fx-text-fill: " + statusColor + "; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 4 12 4 12; -fx-background-radius: 20px;");
        lblStatus.setPrefWidth(100);

        Label lblAction = new Label("Xem đơn");
        lblAction.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));
        lblAction.setTextFill(Color.web("#7c4dff"));
        lblAction.setPrefWidth(80);

        row.getChildren().addAll(lblName, lblMajor, lblDate, lblTarget, lblStatus, lblAction);
        return row;
    }

    private void styleGeminiScrollbar(ScrollPane scrollPane, VBox contentBox, int paddingRight) {
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-padding: 0;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        contentBox.setPadding(new Insets(0, paddingRight, 0, 0));
        String css = ".scroll-pane > .viewport { -fx-background-color: transparent; } " +
                ".scroll-bar:vertical { -fx-background-color: transparent; -fx-pref-width: 12px; } " +
                ".scroll-bar:vertical .track { -fx-background-color: transparent; } " +
                ".scroll-bar:vertical .thumb { -fx-background-color: rgba(124,77,255,0.4); -fx-background-radius: 10px; -fx-max-height: 20px; } " +
                ".scroll-bar:vertical .thumb:hover { -fx-background-color: rgba(124,77,255,0.8); } " +
                ".scroll-bar:vertical .increment-button, .scroll-bar:vertical .decrement-button { -fx-padding: 0; }";
        scrollPane.getStylesheets().add("data:text/css," + css.replaceAll(" ", "%20"));
    }
}