package view.president;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class department extends ScrollPane {

    public department() {
        VBox mainContent = new VBox(24);
        mainContent.setPadding(new Insets(32));
        mainContent.setStyle("-fx-background-color: transparent;");

        // Header Tiêu đề
        VBox header = new VBox(4);
        Label title = new Label("📊 Cơ cấu CLB Glimpz");
        title.setFont(Font.font("Google Sans", FontWeight.BLACK, 24));
        title.setTextFill(Color.web("#1e293b"));
        Label subTitle = new Label("Mô hình phân rã 5 ban cốt lõi");
        subTitle.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));
        subTitle.setTextFill(Color.web("#64748b"));
        header.getChildren().addAll(title, subTitle);

        // Lưới hiển thị các ban (GridPane)
        GridPane grid = new GridPane();
        grid.setHgap(24);
        grid.setVgap(24);

        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(50);
        grid.getColumnConstraints().addAll(cc, cc);

        grid.add(createDeptCard("📢", "Ban Truyền thông", "35 thành viên", "Nguyễn Văn A", "12 nhiệm vụ"), 0, 0);
        grid.add(createDeptCard("💻", "Ban Kỹ thuật", "28 thành viên", "Trần Văn B", "7 nhiệm vụ"), 1, 0);
        grid.add(createDeptCard("🎪", "Ban Sự kiện", "30 thành viên", "Lê Văn C", "15 nhiệm vụ"), 0, 1);
        grid.add(createDeptCard("👥", "Ban Nhân sự", "20 thành viên", "Phạm Thị D", "5 nhiệm vụ"), 1, 1);

        mainContent.getChildren().addAll(header, grid);

        styleGeminiScrollbar(this, mainContent, 12);
        this.setContent(mainContent);
    }

    private VBox createDeptCard(String icon, String name, String membersCount, String leader, String tasks) {
        VBox box = new VBox(16);
        box.setPadding(new Insets(24));
        box.setStyle("-fx-background-color: rgba(255,255,255,0.42); -fx-background-radius: 40px; -fx-border-color: rgba(255,255,255,0.65); -fx-border-radius: 40px; -fx-cursor: hand;");

        HBox topRow = new HBox();
        topRow.setAlignment(Pos.CENTER_LEFT);
        Label lblName = new Label(icon + " " + name);
        lblName.setFont(Font.font("Google Sans", FontWeight.BLACK, 18));
        lblName.setTextFill(Color.web("#1e293b"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblCount = new Label(membersCount);
        lblCount.setStyle("-fx-background-color: rgba(124,77,255,0.1); -fx-text-fill: #7c4dff; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 4 12 4 12; -fx-background-radius: 20px;");

        topRow.getChildren().addAll(lblName, spacer, lblCount);

        VBox infoBox = new VBox(8);
        Label lblLeader = new Label("👤 Trưởng ban: " + leader);
        lblLeader.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));
        lblLeader.setTextFill(Color.web("#64748b"));

        Label lblTasks = new Label("🎯 Hoạt động tháng: " + tasks);
        lblTasks.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));
        lblTasks.setTextFill(Color.web("#4f46e5"));
        infoBox.getChildren().addAll(lblLeader, lblTasks);

        HBox bottomRow = new HBox();
        bottomRow.setAlignment(Pos.CENTER_RIGHT);
        Label lblAction = new Label("Chi tiết >");
        lblAction.setFont(Font.font("Google Sans", FontWeight.BOLD, 10));
        lblAction.setTextFill(Color.web("#7c4dff"));
        bottomRow.getChildren().add(lblAction);

        box.getChildren().addAll(topRow, infoBox, bottomRow);
        GridPane.setHgrow(box, Priority.ALWAYS);
        return box;
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