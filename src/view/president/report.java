package view.president;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.FontWeight;
import view.format;
import view.president.frame;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class report extends ScrollPane {

    public report() {
        VBox mainContent = new VBox(32);
        mainContent.setPadding(new Insets(32));
        mainContent.setStyle("-fx-background-color: transparent;");

        String forceBlackTextCss =
                "* { -fx-text-fill: #000000; } " +
                        "Text { -fx-fill: #000000; } " +
                        ".text-field { -fx-text-inner-color: #000000; -fx-prompt-text-fill: #000000; } " +
                        ".chart-legend-item { -fx-text-fill: #000000; } " +
                        ".axis-label { -fx-text-fill: #000000; } " +
                        ".axis-tick-mark-text { -fx-fill: #000000; } " +
                        ".list-cell { -fx-text-fill: #000000; }";
        mainContent.getStylesheets().add("data:text/css," + forceBlackTextCss.replaceAll(" ", "%20"));

        HBox kpiRow = new HBox(16);
        kpiRow.getChildren().addAll(
                format.formatKPICard("Tổng nhân sự", "120", "#000000", "#000000"),
                format.formatKPICard("Tổng sự kiện", "24", "#000000", "#000000"),
                format.formatKPICard("Hoàn thành công việc", "85%", "#000000", "#000000"),
                format.formatKPICard("Số dư quỹ", "15.800.000đ", "#000000", "#000000")
        );
        for (javafx.scene.Node node : kpiRow.getChildren()) { HBox.setHgrow(node, Priority.ALWAYS); }

        HBox filterBar = new HBox(16);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(12, 24, 12, 24));
        format.formatGlass(filterBar, 40, 0.4);

        Label lblFilter = format.formatLabel("Báo cáo theo:", FontWeight.BLACK, 14, "#000000");

        ComboBox<String> cbTime = format.formatSortBtn("Khoảng thời gian", "Tháng này", "Quý này", "Năm nay", "Toàn bộ");
        cbTime.setValue("Quý này");
        cbTime.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 20px; -fx-padding: 4 12; -fx-font-family: 'Google Sans'; -fx-font-weight: bold; -fx-text-fill: #000000; -fx-prompt-text-fill: #000000;");
        cbTime.setOnAction(e -> frame.getInstance().triggerToast("Đang cập nhật báo cáo..."));

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Chọn ngày cụ thể...");
        datePicker.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 20px; -fx-font-family: 'Google Sans'; -fx-font-weight: bold;");
        datePicker.setOnAction(e -> {
            if (datePicker.getValue() != null) {
                frame.getInstance().triggerToast("Đang tải dữ liệu: " + datePicker.getValue().toString());
            }
        });

        filterBar.getChildren().addAll(lblFilter, cbTime, datePicker);

        VBox chartGrid = new VBox(24);

        HBox row1 = new HBox(24);
        row1.getChildren().addAll(
                buildPieChart("TỶ LỆ BỘ PHẬN",
                        new PieChart.Data("Nội dung", 35), new PieChart.Data("Kỹ thuật", 28),
                        new PieChart.Data("Truyền thông", 30), new PieChart.Data("Hậu cần", 20), new PieChart.Data("Khác", 15)),
                buildPieChart("TRẠNG THÁI NHÂN SỰ",
                        new PieChart.Data("Hoạt động", 100), new PieChart.Data("Tạm nghỉ", 20), new PieChart.Data("Đã rời", 8))
        );

        HBox row2 = new HBox(24);
        row2.getChildren().addAll(
                buildPieChart("TIẾN ĐỘ CÔNG VIỆC",
                        new PieChart.Data("Hoàn thành", 45), new PieChart.Data("Đang làm", 15),
                        new PieChart.Data("Chưa làm", 10), new PieChart.Data("Quá hạn", 3)),
                buildPieChart("TỶ LỆ ĐIỂM DANH",
                        new PieChart.Data("Có mặt", 82), new PieChart.Data("Có phép", 10), new PieChart.Data("Vắng", 8))
        );

        HBox row3 = new HBox(24);
        row3.getChildren().addAll(
                buildSingleBarChart("SỰ KIỆN HÀNG THÁNG", "Tháng", "Số lượng",
                        new XYChart.Data<>("T1", 2), new XYChart.Data<>("T2", 1), new XYChart.Data<>("T3", 4),
                        new XYChart.Data<>("T4", 3), new XYChart.Data<>("T5", 5), new XYChart.Data<>("T6", 2)),
                buildDoubleBarChart("TÀI CHÍNH HÀNG THÁNG", "Tháng", "VNĐ")
        );

        HBox row4 = new HBox(24);
        row4.getChildren().addAll(
                buildPieChart("NGUỒN THU",
                        new PieChart.Data("Hội phí", 45), new PieChart.Data("Tài trợ", 40), new PieChart.Data("Khác", 15)),
                buildPieChart("CƠ CẤU CHI",
                        new PieChart.Data("Sự kiện", 65), new PieChart.Data("Hành chính", 15), new PieChart.Data("Vật tư", 20))
        );

        chartGrid.getChildren().addAll(row1, row2, row3, row4);

        mainContent.getChildren().addAll(kpiRow, filterBar, chartGrid);
        format.formatScrollbar(this, mainContent, 12);
        this.setContent(mainContent);
    }

    private VBox buildPieChart(String title, PieChart.Data... dataItems) {
        VBox card = format.formatBoxCard();
        HBox.setHgrow(card, Priority.ALWAYS);
        card.setPrefWidth(500);

        card.getChildren().add(format.formatLabel(title, FontWeight.BLACK, 14, "#000000"));

        PieChart chart = new PieChart(FXCollections.observableArrayList(dataItems));
        chart.setLegendVisible(true);
        chart.setLabelsVisible(false);
        chart.setPrefHeight(280);

        chart.setStyle("-fx-background-color: transparent;");
        chart.getStylesheets().add("data:text/css," +
                ".chart-legend { -fx-background-color: transparent; } " +
                ".chart-legend-item { -fx-text-fill: #000000; -fx-font-family: 'Google Sans'; -fx-font-weight: bold; } " +
                ".chart-pie { -fx-border-color: white; -fx-border-width: 2px; }".replaceAll(" ", "%20")
        );

        card.getChildren().add(chart);
        return card;
    }

    @SafeVarargs
    private final VBox buildSingleBarChart(String title, String xLabel, String yLabel, XYChart.Data<String, Number>... dataItems) {
        VBox card = format.formatBoxCard();
        HBox.setHgrow(card, Priority.ALWAYS);
        card.setPrefWidth(500);

        card.getChildren().add(format.formatLabel(title, FontWeight.BLACK, 14, "#000000"));

        CategoryAxis xAxis = new CategoryAxis(); xAxis.setLabel(xLabel);
        NumberAxis yAxis = new NumberAxis(); yAxis.setLabel(yLabel);

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);
        chart.setPrefHeight(280);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().addAll(dataItems);
        chart.getData().add(series);

        chart.setStyle("-fx-background-color: transparent;");
        chart.getStylesheets().add("data:text/css," +
                ".chart-plot-background { -fx-background-color: transparent; } " +
                ".chart-vertical-grid-lines { -fx-stroke: rgba(0,0,0,0.05); } " +
                ".chart-horizontal-grid-lines { -fx-stroke: rgba(0,0,0,0.05); } " +
                ".axis-label { -fx-text-fill: #000000; -fx-font-family: 'Google Sans'; -fx-font-weight: bold; } " +
                ".axis-tick-mark-text { -fx-fill: #000000; -fx-font-family: 'Google Sans'; -fx-font-weight: bold; } " +
                ".default-color0.chart-bar { -fx-bar-fill: #7c4dff; -fx-background-radius: 4 4 0 0; }".replaceAll(" ", "%20")
        );

        card.getChildren().add(chart);
        return card;
    }

    private VBox buildDoubleBarChart(String title, String xLabel, String yLabel) {
        VBox card = format.formatBoxCard();
        HBox.setHgrow(card, Priority.ALWAYS);
        card.setPrefWidth(500);

        card.getChildren().add(format.formatLabel(title, FontWeight.BLACK, 14, "#000000"));

        CategoryAxis xAxis = new CategoryAxis(); xAxis.setLabel(xLabel);
        NumberAxis yAxis = new NumberAxis(); yAxis.setLabel(yLabel);

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setLegendVisible(true);
        chart.setPrefHeight(280);

        XYChart.Series<String, Number> seriesThu = new XYChart.Series<>();
        seriesThu.setName("Thu");
        seriesThu.getData().addAll(
                new XYChart.Data<>("T1", 4500000), new XYChart.Data<>("T2", 2000000),
                new XYChart.Data<>("T3", 5500000), new XYChart.Data<>("T4", 3000000)
        );

        XYChart.Series<String, Number> seriesChi = new XYChart.Series<>();
        seriesChi.setName("Chi");
        seriesChi.getData().addAll(
                new XYChart.Data<>("T1", 1200000), new XYChart.Data<>("T2", 800000),
                new XYChart.Data<>("T3", 4500000), new XYChart.Data<>("T4", 1500000)
        );

        chart.getData().addAll(seriesThu, seriesChi);

        chart.setStyle("-fx-background-color: transparent;");
        chart.getStylesheets().add("data:text/css," +
                ".chart-plot-background { -fx-background-color: transparent; } " +
                ".chart-legend { -fx-background-color: transparent; } " +
                ".chart-legend-item { -fx-text-fill: #000000; -fx-font-family: 'Google Sans'; -fx-font-weight: bold; } " +
                ".axis-label { -fx-text-fill: #000000; -fx-font-family: 'Google Sans'; -fx-font-weight: bold; } " +
                ".axis-tick-mark-text { -fx-fill: #000000; -fx-font-family: 'Google Sans'; -fx-font-weight: bold; } " +
                ".chart-vertical-grid-lines { -fx-stroke: rgba(0,0,0,0.05); } " +
                ".chart-horizontal-grid-lines { -fx-stroke: rgba(0,0,0,0.05); } " +
                ".default-color0.chart-bar { -fx-bar-fill: #10b981; -fx-background-radius: 4 4 0 0; } " +
                ".default-color1.chart-bar { -fx-bar-fill: #ef4444; -fx-background-radius: 4 4 0 0; }".replaceAll(" ", "%20")
        );

        card.getChildren().add(chart);
        return card;
    }
}