package view.head;

import javafx.application.Application;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class frame extends Application {

    private static frame instance;

    public static frame getInstance() { return instance; }

    private StackPane rootPane;
    private HBox mainLayout;
    private StackPane modalOverlay;
    private VBox toastContainer;
    private DropShadow mainShadow;
    private Label mainTitle;
    private StackPane viewContainer;
    private Button[] allNavButtons;
    private Button activeNavBtn = null;
    private String currentUserName = "Nguyễn Văn A";

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
        tt1.setByX(150); tt1.setByY(100); tt1.setInterpolator(Interpolator.EASE_BOTH);
        tt1.setAutoReverse(true); tt1.setCycleCount(Animation.INDEFINITE); tt1.play();

        ScaleTransition st1 = new ScaleTransition(Duration.seconds(22), blob1);
        st1.setToX(1.2); st1.setToY(1.2); st1.setInterpolator(Interpolator.EASE_BOTH);
        st1.setAutoReverse(true); st1.setCycleCount(Animation.INDEFINITE); st1.play();

        TranslateTransition tt2 = new TranslateTransition(Duration.seconds(28), blob2);
        tt2.setByX(-150); tt2.setByY(-100); tt2.setInterpolator(Interpolator.EASE_BOTH);
        tt2.setAutoReverse(true); tt2.setCycleCount(Animation.INDEFINITE); tt2.play();

        ScaleTransition st2 = new ScaleTransition(Duration.seconds(28), blob2);
        st2.setToX(0.8); st2.setToY(0.8); st2.setInterpolator(Interpolator.EASE_BOTH);
        st2.setAutoReverse(true); st2.setCycleCount(Animation.INDEFINITE); st2.play();

        rootPane.getChildren().add(backgroundPane);

        mainLayout = new HBox();
        mainLayout.setMaxSize(1300, 750);
        mainLayout.setPrefSize(1300, 750);
        mainLayout.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        mainShadow = new DropShadow(45, 0, 16, Color.web("#311b92", 0.15));
        mainLayout.setEffect(mainShadow);

        VBox sidebar = new VBox();
        sidebar.setPrefWidth(192); sidebar.setMinWidth(192); sidebar.setMaxWidth(192);
        sidebar.setPadding(new Insets(24, 16, 24, 16));
        sidebar.setStyle("-fx-background-color: rgba(255,255,255,0.3); -fx-background-radius: 40px 0px 0px 40px;");
        sidebar.setAlignment(Pos.TOP_CENTER);

        VBox logoBox = new VBox(8);
        logoBox.setAlignment(Pos.CENTER);
        logoBox.setPadding(new Insets(0, 0, 24, 0));

        StackPane logoImgContainer = new StackPane();
        logoImgContainer.setPrefSize(44, 44); logoImgContainer.setMaxSize(44, 44);

        ImageView appLogo = new ImageView(new Image("cluby.png"));
        appLogo.setFitWidth(44); appLogo.setFitHeight(44);
        appLogo.setPreserveRatio(true);
        Rectangle clip = new Rectangle(44, 44); clip.setArcWidth(16); clip.setArcHeight(16);
        appLogo.setClip(clip);
        logoImgContainer.getChildren().add(appLogo);

        Label logoTitle = format.formatLabel("cluby", FontWeight.EXTRA_BOLD, 20, "#2d3748");
        logoTitle.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.15), 10, 0.4, 0, 4);");

        Label clockLabel = format.formatLabel("", FontWeight.BOLD, 10, "#64748b");
        clockLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        clockLabel.setAlignment(Pos.CENTER);

        Label greetingLabel = format.formatLabel("", FontWeight.BOLD, 11, "#7c4dff");
        greetingLabel.setPadding(new Insets(4, 0, 0, 0));

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss\nEEEE, dd/MM/yyyy", new Locale("vi", "VN"));
            clockLabel.setText(now.format(formatter));
            int hour = now.getHour();
            if (hour >= 4 && hour < 10) greetingLabel.setText("Chào buổi sáng, " + currentUserName + "!");
            else if (hour >= 10 && hour < 13) greetingLabel.setText("Chào buổi trưa, " + currentUserName + "!");
            else if (hour >= 13 && hour < 18) greetingLabel.setText("Chào buổi chiều, " + currentUserName + "!");
            else if (hour >= 18 && hour < 23) greetingLabel.setText("Chào buổi tối, " + currentUserName + "!");
            else greetingLabel.setText("Đã khuya rồi, " + currentUserName + "!");
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE); clock.play();

        logoBox.getChildren().addAll(logoImgContainer, logoTitle, clockLabel, greetingLabel);

        VBox navList = new VBox(4);
        navList.setAlignment(Pos.TOP_CENTER);

        Button navDashboard = createNavButton("🏠", "Chung", true, false);

        VBox hrGroup = new VBox(0);
        Button navHR = createNavButton("👥", "Nhân sự", false, false);
        VBox hrSubMenu = new VBox(2);
        hrSubMenu.setPadding(new Insets(2, 0, 4, 0));
        Button navHRMembers = createNavButton("•", "Thành viên", false, true);
        Button navHRDepts = createNavButton("•", "Ban", false, true);

        hrSubMenu.getChildren().addAll(navHRMembers, navHRDepts);
        hrSubMenu.setManaged(false); hrSubMenu.setVisible(false);
        hrGroup.getChildren().addAll(navHR, hrSubMenu);
        hrGroup.setOnMouseEntered(e -> { hrSubMenu.setManaged(true); hrSubMenu.setVisible(true); });
        hrGroup.setOnMouseExited(e -> { hrSubMenu.setManaged(false); hrSubMenu.setVisible(false); });

        VBox actGroup = new VBox(0);
        Button navActivities = createNavButton("📅", "Hoạt động", false, false);
        VBox actSubMenu = new VBox(2);
        actSubMenu.setPadding(new Insets(2, 0, 4, 0));
        Button navActEvents = createNavButton("•", "Sự kiện", false, true);
        Button navActTasks = createNavButton("•", "Công việc", false, true);

        actSubMenu.getChildren().addAll(navActEvents, navActTasks);
        actSubMenu.setManaged(false); actSubMenu.setVisible(false);

        actGroup.getChildren().addAll(navActivities, actSubMenu);
        actGroup.setOnMouseEntered(e -> { actSubMenu.setManaged(true); actSubMenu.setVisible(true); });
        actGroup.setOnMouseExited(e -> { actSubMenu.setManaged(false); actSubMenu.setVisible(false); });

        Button navFinance = createNavButton("💰", "Tài chính", false, false);

        allNavButtons = new Button[] {
                navDashboard, navHR, navHRMembers, navHRDepts,
                navActivities, navActEvents, navActTasks,
                navFinance
        };

        navList.getChildren().addAll(navDashboard, hrGroup, actGroup, navFinance);

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        VBox userProfile = new VBox(8);
        userProfile.setAlignment(Pos.CENTER);
        userProfile.setPadding(new Insets(12));
        userProfile.setStyle("-fx-background-color: rgba(255,255,255,0.35); -fx-background-radius: 30px; -fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.1), 12, 0.3, 0, 6);");

        HBox userActions = new HBox(8);
        userActions.setAlignment(Pos.CENTER);

        Button btnLogout = new Button("🚪");
        btnLogout.setPrefSize(44, 44);
        btnLogout.setStyle("-fx-background-color: #ef4444; -fx-background-radius: 22px; -fx-text-fill: white; -fx-font-size: 18px; -fx-padding: 0; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(239,68,68,0.5), 10, 0.4, 0, 4);");
        btnLogout.setOnMouseEntered(e -> { btnLogout.setScaleX(1.1); btnLogout.setScaleY(1.1); });
        btnLogout.setOnMouseExited(e -> { btnLogout.setScaleX(1.0); btnLogout.setScaleY(1.0); });
        btnLogout.setOnAction(e -> openLogoutModal());

        Button btnAvatar = new Button();
        btnAvatar.setPrefSize(44, 44);
        ImageView userImg = new ImageView(new Image("trish.jpeg"));
        userImg.setFitWidth(44); userImg.setFitHeight(44);
        userImg.setPreserveRatio(false); userImg.setClip(new Circle(22, 22, 22));
        btnAvatar.setGraphic(userImg);
        btnAvatar.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-cursor: hand; -fx-background-radius: 22px; -fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.2), 10, 0.4, 0, 4);");
        btnAvatar.setOnMouseEntered(e -> { btnAvatar.setScaleX(1.1); btnAvatar.setScaleY(1.1); });
        btnAvatar.setOnMouseExited(e -> { btnAvatar.setScaleX(1.0); btnAvatar.setScaleY(1.0); });
        btnAvatar.setOnAction(e -> switchTabFocus(null, "Hồ sơ cá nhân"));

        userActions.getChildren().addAll(btnLogout, btnAvatar);

        Label userName = format.formatLabel(currentUserName, FontWeight.BOLD, 12, "#1e293b");
        Label userRole = format.formatLabel("TRƯỞNG BAN", FontWeight.EXTRA_BOLD, 8, "#64748b");

        userProfile.getChildren().addAll(userActions, userName, userRole);

        sidebar.getChildren().addAll(logoBox, navList, sidebarSpacer, userProfile);

        VBox mainArea = new VBox();
        HBox.setHgrow(mainArea, Priority.ALWAYS);

        HBox topBar = new HBox();
        topBar.setPadding(new Insets(24, 32, 24, 32));
        topBar.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(2);
        mainTitle = format.formatLabel("Nắm tình hình nào!", FontWeight.BLACK, 32, "#1e293b");
        mainTitle.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.1), 10, 0.4, 0, 4);");
        titleBox.getChildren().add(mainTitle);

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);

        HBox topActions = new HBox(16);
        topActions.setAlignment(Pos.CENTER_RIGHT);

        topBar.getChildren().addAll(titleBox, topSpacer, topActions);

        viewContainer = new StackPane();
        viewContainer.setPadding(new Insets(0, 32, 32, 32));
        VBox.setVgrow(viewContainer, Priority.ALWAYS);

        setView(new dashboard());

        mainArea.getChildren().addAll(topBar, viewContainer);

        mainLayout.getChildren().addAll(sidebar, mainArea);

        modalOverlay = new StackPane();
        modalOverlay.setStyle("-fx-background-color: rgb(57 0 216 / 0.1);");
        modalOverlay.setVisible(false);

        toastContainer = new VBox(8);
        toastContainer.setMaxSize(300, 200);
        StackPane.setAlignment(toastContainer, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(toastContainer, new Insets(0, 30, 30, 0));

        rootPane.getChildren().addAll(mainLayout, modalOverlay, toastContainer);

        Scene scene = new Scene(rootPane, 1366, 800);
        primaryStage.setTitle("cluby - Trưởng Ban");
        primaryStage.getIcons().add(new Image("cluby.png"));
        primaryStage.setScene(scene);
        primaryStage.show();

        triggerToast("Một ngày tốt lành, " + currentUserName + "!");
    }

    public void setView(Node newView) {
        if (viewContainer != null) viewContainer.getChildren().setAll(newView);
    }

    public void setMainTitle(String title) {
        if (mainTitle != null) mainTitle.setText(title);
    }

    public void switchTabFocus(Button activeBtn, String title) {
        for (Button btn : allNavButtons) {
            format.formatNavBtnInactive(btn);
        }
        if (activeBtn != null) {
            format.formatNavBtnActive(activeBtn);
        }
        activeNavBtn = activeBtn;

        switch (title) {
            case "Chung": { setMainTitle("Nắm tình hình nào!"); setView(new dashboard()); break; }
            case "Thành viên": { setMainTitle(title); setView(new member()); break; }
            case "Ban": { setMainTitle(title); setView(new department()); break; }
            case "Sự kiện": { setMainTitle(title); setView(new event()); break; }
            case "Công việc": { setMainTitle(title); setView(new work()); break; }
            case "Tài chính": { setMainTitle(title); setView(new finance()); break; }
            case "Hồ sơ cá nhân": { setMainTitle(title); setView(new setting()); break; }
            default: setMainTitle(title); break;
        }
        triggerToast("Đã đến " + title.toUpperCase());
    }

    private Button createNavButton(String emoji, String title, boolean isActive, boolean isSubItem) {
        Button btn = format.formatNavBtn(emoji, title, isSubItem);
        if (isActive) {
            format.formatNavBtnActive(btn);
            activeNavBtn = btn;
        } else {
            format.formatNavBtnInactive(btn);
        }

        if(!title.equals("Nhân sự") && !title.equals("Hoạt động")) {
            btn.setOnAction(e -> switchTabFocus(btn, title));
        }

        btn.setOnMouseEntered(e -> {
            btn.setScaleX(1.02); btn.setScaleY(1.02);
            if (btn != activeNavBtn) {
                btn.setStyle("-fx-background-color: rgba(255,255,255,0.25); -fx-background-radius: 12px; -fx-border-color: transparent; -fx-border-width: 0px; -fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.1), 10, 0.4, 0, 4); -fx-cursor: hand;");
            }
        });

        btn.setOnMouseExited(e -> {
            btn.setScaleX(1.0); btn.setScaleY(1.0);
            if (btn != activeNavBtn) format.formatNavBtnInactive(btn);
        });

        return btn;
    }

    private void openLogoutModal() {
        modalOverlay.getChildren().clear();
        modalOverlay.getChildren().add(
                format.formatSimpleModal("Xác nhận Đăng xuất", "Bạn có chắc chắn muốn rời khỏi ứng dụng không?", "Huỷ", "Rời đi",
                        e -> closeOverlayModal(),
                        e -> { closeOverlayModal(); triggerToast("Đã đăng xuất khỏi hệ thống thành công!"); }
                )
        );
        GaussianBlur blur = new GaussianBlur(25);
        mainLayout.setEffect(blur);
        modalOverlay.setVisible(true);

        FadeTransition ft = new FadeTransition(Duration.millis(200), modalOverlay);
        ft.setFromValue(0); ft.setToValue(1); ft.play();
    }

    public void showCustomModal(Node modalContent) {
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
            mainLayout.setEffect(mainShadow);
        });
        ft.play();
    }

    public void triggerToast(String msg) {
        format.formatToast(toastContainer, msg);
    }

    public static void main(String[] args) { launch(args); }
}