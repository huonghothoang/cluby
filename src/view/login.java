package view;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class login extends Application {

    private static login instance;
    public static login getInstance() { return instance; }

    private StackPane rootPane;
    private StackPane modalOverlay;
    private VBox toastContainer;
    private HBox mainLayout;

    private VBox formContainerBox;

    @Override
    public void start(Stage primaryStage) {
        instance = this;
        rootPane = new StackPane();

        Pane backgroundPane = new Pane();
        Stop[] stops = new Stop[] {
                new Stop(0.0, Color.web("#d3dbf4")),
                new Stop(0.35, Color.web("#e8d6f5")),
                new Stop(0.7, Color.web("#fce5de")),
                new Stop(1.0, Color.web("#d7c7f5"))
        };
        LinearGradient bgGrad = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
        backgroundPane.setBackground(new Background(new BackgroundFill(bgGrad, CornerRadii.EMPTY, Insets.EMPTY)));

        Circle blob1 = new Circle(280, Color.web("#7c4dff", 0.12));
        blob1.setEffect(new BoxBlur(120, 120, 3));
        blob1.setCenterX(300); blob1.setCenterY(150);

        Circle blob2 = new Circle(330, Color.web("#fca4c5", 0.15));
        blob2.setEffect(new BoxBlur(140, 120, 3));
        blob2.setCenterX(1100); blob2.setCenterY(650);

        backgroundPane.getChildren().addAll(blob1, blob2);

        TranslateTransition tt1 = new TranslateTransition(Duration.seconds(22), blob1);
        tt1.setByX(150); tt1.setByY(100); tt1.setAutoReverse(true); tt1.setCycleCount(Animation.INDEFINITE); tt1.play();

        TranslateTransition tt2 = new TranslateTransition(Duration.seconds(28), blob2);
        tt2.setByX(-150); tt2.setByY(-100); tt2.setAutoReverse(true); tt2.setCycleCount(Animation.INDEFINITE); tt2.play();

        rootPane.getChildren().add(backgroundPane);

        mainLayout = new HBox();
        mainLayout.setPrefSize(1366, 800);
        mainLayout.setAlignment(Pos.CENTER);

        VBox leftPane = new VBox(24);
        leftPane.setAlignment(Pos.CENTER);
        leftPane.setPrefWidth(683);
        HBox.setHgrow(leftPane, Priority.ALWAYS);

        ImageView hugeLogo = new ImageView(new Image("cluby.png"));
        hugeLogo.setFitWidth(250);
        hugeLogo.setFitHeight(250);
        hugeLogo.setPreserveRatio(true);

        Label logoText = format.formatLabel("cluby", FontWeight.BLACK, 72, "#1e293b");
        logoText.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.15), 10, 0.4, 0, 8);");

        Label slogan = format.formatLabel("Quản lý Câu lạc bộ hiệu quả", FontWeight.BOLD, 18, "#5020d8");

        leftPane.getChildren().addAll(hugeLogo, logoText, slogan);

        VBox rightPane = new VBox();
        rightPane.setAlignment(Pos.CENTER);
        rightPane.setPrefWidth(683);
        HBox.setHgrow(rightPane, Priority.ALWAYS);

        formContainerBox = new VBox();
        formContainerBox.setAlignment(Pos.CENTER);

        formContainerBox.setPrefSize(450, 600);
        formContainerBox.setMinSize(450, 600);
        formContainerBox.setMaxSize(450, 600);

        formContainerBox.setPadding(new Insets(40));
        formContainerBox.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-background-radius: 40px;");
        formContainerBox.setEffect(new DropShadow(45, 0, 16, Color.web("#311b92", 0.15)));

        formContainerBox.getChildren().setAll(createLoginForm());

        rightPane.getChildren().add(formContainerBox);

        mainLayout.getChildren().addAll(leftPane, rightPane);

        modalOverlay = new StackPane();
        modalOverlay.setStyle("-fx-background-color: rgb(57 0 216 / 0.1);");
        modalOverlay.setVisible(false);

        toastContainer = new VBox(8);
        toastContainer.setMaxSize(300, 200);

        toastContainer.setPickOnBounds(false);
        toastContainer.setMouseTransparent(true);

        StackPane.setAlignment(toastContainer, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(toastContainer, new Insets(0, 30, 30, 0));

        rootPane.getChildren().addAll(mainLayout, modalOverlay, toastContainer);

        Scene scene = new Scene(rootPane, 1366, 800);
        primaryStage.setTitle("cluby");
        primaryStage.getIcons().add(new javafx.scene.image.Image("cluby.png"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createLoginForm() {
        VBox box = new VBox(24);
        box.setAlignment(Pos.CENTER);

        Label title = format.formatLabel("Đăng nhập", FontWeight.BLACK, 32, "#1e293b");
        Label sub = format.formatLabel("Chào mừng bạn quay trở lại.", FontWeight.MEDIUM, 14, "#64748b");
        VBox header = new VBox(8, title, sub);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 16, 0));

        VBox fields = new VBox(16);
        TextField fEmail = format.formatTextField("Email hoặc mã định danh...");
        PasswordField fPass = createPasswordField("Mật khẩu...");

        fields.getChildren().addAll(
                new VBox(6, format.formatLabel("Tài khoản", FontWeight.BOLD, 12, "#94a3b8"), fEmail),
                new VBox(6, format.formatLabel("Mật khẩu", FontWeight.BOLD, 12, "#94a3b8"), fPass)
        );

        Button btnLogin = getShadowBtn("Đăng nhập", "", "#5020d8", "white", "rgba(80,32,216,0.4)");
        btnLogin.setPrefWidth(Double.MAX_VALUE);
        btnLogin.setPrefHeight(48);

        btnLogin.setOnAction(e -> {
            if (fEmail.getText().trim().isEmpty() || fPass.getText().trim().isEmpty()) {
                triggerToast("Vui lòng điền đủ thông tin");
                return;
            }
            triggerToast("Đăng nhập thành công");
        });

        HBox switchRow = new HBox(8);
        switchRow.setAlignment(Pos.CENTER);
        switchRow.setPadding(new Insets(24, 0, 0, 0));
        Label l1 = format.formatLabel("Chưa có tài khoản?", FontWeight.MEDIUM, 14, "#64748b");

        Hyperlink linkReg = new Hyperlink("Đăng ký");
        linkReg.setFont(Font.font("Google Sans", FontWeight.BOLD, 14));
        linkReg.setTextFill(Color.web("#5020d8"));
        linkReg.setBorder(Border.EMPTY);
        linkReg.setPadding(new Insets(0));
        linkReg.setOnAction(e -> formContainerBox.getChildren().setAll(createRegisterForm()));

        switchRow.getChildren().addAll(l1, linkReg);

        box.getChildren().addAll(header, fields, btnLogin, switchRow);
        return box;
    }

    private VBox createRegisterForm() {
        VBox box = new VBox(16);
        box.setAlignment(Pos.CENTER);

        Label title = format.formatLabel("Đăng ký", FontWeight.BLACK, 30, "#1e293b");
        Label sub = format.formatLabel("Tạo tài khoản mới để tham gia.", FontWeight.MEDIUM, 14, "#64748b");
        VBox header = new VBox(4, title, sub);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 8, 0));

        VBox fields = new VBox(12);

        String generatedId = "26MB" + String.format("%03d", (int)(Math.random() * 999));
        TextField fId = format.formatTextField(generatedId);
        fId.setDisable(true);

        TextField fName = format.formatTextField("Họ và tên...");

        TextField fEmail = format.formatTextField("Địa chỉ email...");
        Label emailNote = format.formatLabel("Hệ thống chỉ chấp nhận email chưa từng đăng ký.", FontWeight.BOLD, 10, "#ef4444");
        VBox emailBox = new VBox(6, format.formatLabel("Email *", FontWeight.BOLD, 12, "#94a3b8"), fEmail, emailNote);

        PasswordField fPass = createPasswordField("Mật khẩu...");

        fields.getChildren().addAll(
                new VBox(6, format.formatLabel("Mã định danh hệ thống cấp", FontWeight.BOLD, 12, "#94a3b8"), fId),
                new VBox(6, format.formatLabel("Họ và tên *", FontWeight.BOLD, 12, "#94a3b8"), fName),
                emailBox,
                new VBox(6, format.formatLabel("Mật khẩu *", FontWeight.BOLD, 12, "#94a3b8"), fPass)
        );

        Button btnRegister = getShadowBtn("Đăng ký", "", "#3b82f6", "white", "rgba(59,130,246,0.4)");
        btnRegister.setPrefWidth(Double.MAX_VALUE);
        btnRegister.setPrefHeight(45);

        btnRegister.setOnAction(e -> {
            if (fName.getText().trim().isEmpty() || fEmail.getText().trim().isEmpty() || fPass.getText().trim().isEmpty()) {
                triggerToast("Vui lòng điền đủ thông tin bắt buộc");
                return;
            }
            showCustomModal(createSuccessModal(generatedId));
        });

        HBox switchRow = new HBox(8);
        switchRow.setAlignment(Pos.CENTER);
        switchRow.setPadding(new Insets(12, 0, 0, 0));
        Label l1 = format.formatLabel("Đã có tài khoản?", FontWeight.MEDIUM, 14, "#64748b");

        Hyperlink linkLogin = new Hyperlink("Đăng nhập");
        linkLogin.setFont(Font.font("Google Sans", FontWeight.BOLD, 14));
        linkLogin.setTextFill(Color.web("#5020d8"));
        linkLogin.setBorder(Border.EMPTY);
        linkLogin.setPadding(new Insets(0));
        linkLogin.setOnAction(e -> formContainerBox.getChildren().setAll(createLoginForm()));

        switchRow.getChildren().addAll(l1, linkLogin);

        box.getChildren().addAll(header, fields, btnRegister, switchRow);
        return box;
    }

    private VBox createSuccessModal(String generatedId) {
        VBox box = new VBox(20);
        box.setPrefSize(420, 280);
        box.setMinSize(420, 280);
        box.setMaxSize(420, 280);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel("Đăng ký hoàn tất", FontWeight.BLACK, 24, "#5020d8");

        Label sub = format.formatLabel("Mã định danh tài khoản của bạn:", FontWeight.MEDIUM, 13, "#64748b");
        sub.setWrapText(true); sub.setAlignment(Pos.CENTER); sub.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Label idLabel = format.formatLabel(generatedId, FontWeight.BLACK, 32, "#3b82f6");

        Label note = format.formatLabel("Vui lòng đợi quản trị viên phê duyệt hồ sơ.", FontWeight.MEDIUM, 12, "#94a3b8");
        note.setWrapText(true); note.setAlignment(Pos.CENTER); note.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Button btnNext = getShadowBtn("Quay lại đăng nhập", "", "#5020d8", "white", "rgba(80,32,216,0.4)");
        btnNext.setPrefWidth(Double.MAX_VALUE);
        btnNext.setPrefHeight(45);
        btnNext.setOnAction(e -> {
            closeOverlayModal();
            formContainerBox.getChildren().setAll(createLoginForm());
        });

        box.getChildren().addAll(title, sub, idLabel, note, btnNext);
        return box;
    }

    private PasswordField createPasswordField(String prompt) {
        PasswordField pf = new PasswordField();
        pf.setPromptText(prompt);
        pf.setStyle(
                "-fx-background-color: rgb(208 197 244 / 0.5);" +
                        "-fx-background-radius: 40px;" +
                        "-fx-border-color: transparent;" +
                        "-fx-border-width: 0px;" +
                        "-fx-padding: 12 20 12 20;" +
                        "-fx-font-family: 'Google Sans';"
        );
        return pf;
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
        lblText.setFont(Font.font("Google Sans", FontWeight.BOLD, 13));
        lblText.setTextFill(Color.web(textColor));
        content.getChildren().add(lblText);
        btn.setGraphic(content);
        btn.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 40px; -fx-padding: 10 20 10 20; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, " + shadowColor + ", 10, 0, 0, 4);");
        btn.setOnMouseEntered(e -> { btn.setScaleX(1.03); btn.setScaleY(1.03); });
        btn.setOnMouseExited(e -> { btn.setScaleX(1.0); btn.setScaleY(1.0); });
        return btn;
    }

    public void showCustomModal(javafx.scene.Node modalContent) {
        modalOverlay.getChildren().clear();
        modalOverlay.getChildren().add(modalContent);
        GaussianBlur blur = new GaussianBlur(25);
        mainLayout.setEffect(blur);
        modalOverlay.setVisible(true);
        FadeTransition ft = new FadeTransition(Duration.millis(200), modalOverlay);
        ft.setFromValue(0); ft.setToValue(1); ft.play();
    }

    public void closeOverlayModal() {
        FadeTransition ft = new FadeTransition(Duration.millis(200), modalOverlay);
        ft.setFromValue(1); ft.setToValue(0);
        ft.setOnFinished(e -> {
            modalOverlay.setVisible(false);
            mainLayout.setEffect(null);
        });
        ft.play();
    }

    public void triggerToast(String msg) {
        format.formatToast(toastContainer, msg);
    }

    public static void main(String[] args) { launch(args); }
}