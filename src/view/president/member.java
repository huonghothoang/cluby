package view.president;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class member extends ScrollPane {

    public member() {
        VBox mainContent = new VBox(24);
        mainContent.setPadding(new Insets(32));
        mainContent.setStyle("-fx-background-color: transparent;");

        // Dòng 1: Thẻ KPI Tổng quan
        HBox kpiRow = new HBox(16);
        kpiRow.getChildren().addAll(
                createKPICard("Tổng thành viên", "128", "#64748b", "#1e293b"),
                createKPICard("🟢 Đang hoạt động", "112", "#10b981", "#1e293b"),
                createKPICard("🔴 Không hoạt động", "16", "#f43f5e", "#1e293b"),
                createKPICard("📝 Đang thử việc", "23", "#a855f7", "#1e293b")
        );
        for(javafx.scene.Node node : kpiRow.getChildren()) { HBox.setHgrow(node, Priority.ALWAYS); }

        // Dòng 2: Bộ lọc (Filter Bar)
        HBox filterBar = new HBox(12);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(20));
        filterBar.setStyle("-fx-background-color: rgba(255,255,255,0.3); -fx-background-radius: 40px; -fx-border-color: rgba(255,255,255,0.5); -fx-border-radius: 40px;");

        TextField searchField = new TextField();
        searchField.setPromptText("Tìm theo tên...");
        searchField.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-background-radius: 20px; -fx-padding: 8 16 8 16;");

        ComboBox<String> banFilter = new ComboBox<>();
        banFilter.getItems().addAll("Tất cả Ban", "Truyền thông", "Kỹ thuật", "Sự kiện", "Nhân sự");
        banFilter.getSelectionModel().selectFirst();
        banFilter.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-background-radius: 20px;");

        ComboBox<String> roleFilter = new ComboBox<>();
        roleFilter.getItems().addAll("Chức vụ", "Trưởng ban", "Thành viên", "Phó ban", "Cộng tác viên");
        roleFilter.getSelectionModel().selectFirst();
        roleFilter.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-background-radius: 20px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnAdd = new Button("+ Thêm thành viên");
        btnAdd.setStyle("-fx-background-color: linear-gradient(to bottom right, #7c4dff, #448aff); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20px; -fx-padding: 8 16 8 16; -fx-cursor: hand;");
        filterBar.getChildren().addAll(searchField, banFilter, roleFilter, spacer, btnAdd);

        // Dòng 3: Bảng danh sách thành viên (Giả lập bằng VBox & HBox để style dễ dàng)
        VBox tableContainer = new VBox(12);
        tableContainer.setStyle("-fx-background-color: rgba(255,255,255,0.5); -fx-background-radius: 40px; -fx-padding: 24; -fx-border-color: rgba(255,255,255,0.6); -fx-border-radius: 40px;");

        HBox header = new HBox(16);
        header.setPadding(new Insets(12, 16, 12, 16));
        header.setStyle("-fx-border-color: transparent transparent rgba(255,255,255,0.4) transparent; -fx-border-width: 1px;");
        header.getChildren().addAll(
                createColLabel("Thành viên", 200),
                createColLabel("Ban", 150),
                createColLabel("Chức vụ", 120),
                createColLabel("Trạng thái", 120),
                createColLabel("Thao tác", 80)
        );
        tableContainer.getChildren().add(header);

        tableContainer.getChildren().addAll(
                createMemberRow("Nguyễn Văn A", "Truyền thông", "Trưởng ban", "Hoạt động", "#10b981", "rgba(16,185,129,0.1)"),
                createMemberRow("Trần Văn B", "Kỹ thuật", "Thành viên", "Hoạt động", "#10b981", "rgba(16,185,129,0.1)"),
                createMemberRow("Lê Văn C", "Sự kiện", "Thành viên", "Không hoạt động", "#f43f5e", "rgba(244,63,94,0.1)"),
                createMemberRow("Phạm Thị D", "Nhân sự", "Phó ban", "Hoạt động", "#10b981", "rgba(16,185,129,0.1)"),
                createMemberRow("Hoàng Văn E", "Truyền thông", "Cộng tác viên", "Đang thử việc", "#a855f7", "rgba(168,85,247,0.1)")
        );

        mainContent.getChildren().addAll(kpiRow, filterBar, tableContainer);

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

    private HBox createMemberRow(String name, String ban, String role, String status, String statusColor, String statusBg) {
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

        Label lblBan = new Label(ban);
        lblBan.setFont(Font.font("Google Sans", FontWeight.MEDIUM, 12));
        lblBan.setTextFill(Color.web("#475569"));
        lblBan.setPrefWidth(150);

        Label lblRole = new Label(role);
        lblRole.setFont(Font.font("Google Sans", FontWeight.MEDIUM, 12));
        lblRole.setTextFill(Color.web("#64748b"));
        lblRole.setPrefWidth(120);

        Label lblStatus = new Label(status);
        lblStatus.setStyle("-fx-background-color: " + statusBg + "; -fx-text-fill: " + statusColor + "; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 4 12 4 12; -fx-background-radius: 20px;");
        lblStatus.setPrefWidth(120);

        Label lblAction = new Label("Chi tiết >");
        lblAction.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));
        lblAction.setTextFill(Color.web("#7c4dff"));
        lblAction.setPrefWidth(80);

        row.getChildren().addAll(lblName, lblBan, lblRole, lblStatus, lblAction);
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