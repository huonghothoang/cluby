package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.animation.*;
import javafx.util.Duration;

// wutisit: Thư viện xử lý thời gian thực cho đồng hồ
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class frame extends Application {

    // canbereuse: Mẫu Singleton để gọi frame.getInstance()... từ file khác
    private static frame instance;

    public static frame getInstance() {
        return instance;
    }

    private StackPane rootPane;
    private HBox mainLayout;
    private StackPane modalOverlay;
    private VBox toastContainer;
    private DropShadow mainShadow;

    // wutisit: Các biến lưu trữ vùng chứa để thao tác đổi view sau này
    private Label mainTitle;
    private StackPane viewContainer;

    // wutisit: Mảng lưu toàn bộ nút điều hướng để dễ quản lý trạng thái Active
    private Button[] allNavButtons;
    private Button activeNavBtn = null;

    @Override
    public void start(Stage primaryStage) {
        instance = this;
        rootPane = new StackPane();

        // --- 1. PHẦN NỀN ---
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
        blob1.setCenterX(300);
        blob1.setCenterY(150);

        Circle blob2 = new Circle(330, Color.web("#fca4c5", 0.15));
        blob2.setEffect(new BoxBlur(140, 120, 3));
        blob2.setCenterX(1100);
        blob2.setCenterY(650);

        backgroundPane.getChildren().addAll(blob1, blob2);

        TranslateTransition tt1 = new TranslateTransition(Duration.seconds(22), blob1);
        tt1.setByX(150); tt1.setByY(100);
        tt1.setInterpolator(Interpolator.EASE_BOTH);
        tt1.setAutoReverse(true); tt1.setCycleCount(Animation.INDEFINITE);
        tt1.play();

        ScaleTransition st1 = new ScaleTransition(Duration.seconds(22), blob1);
        st1.setToX(1.2); st1.setToY(1.2);
        st1.setInterpolator(Interpolator.EASE_BOTH);
        st1.setAutoReverse(true); st1.setCycleCount(Animation.INDEFINITE);
        st1.play();

        TranslateTransition tt2 = new TranslateTransition(Duration.seconds(28), blob2);
        tt2.setByX(-150); tt2.setByY(-100);
        tt2.setInterpolator(Interpolator.EASE_BOTH);
        tt2.setAutoReverse(true); tt2.setCycleCount(Animation.INDEFINITE);
        tt2.play();

        ScaleTransition st2 = new ScaleTransition(Duration.seconds(28), blob2);
        st2.setToX(0.8); st2.setToY(0.8);
        st2.setInterpolator(Interpolator.EASE_BOTH);
        st2.setAutoReverse(true); st2.setCycleCount(Animation.INDEFINITE);
        st2.play();

        rootPane.getChildren().add(backgroundPane);

        // --- 2. KHUNG KÍNH MỜ CHÍNH ---
        mainLayout = new HBox();
        mainLayout.setMaxSize(1300, 750);
        mainLayout.setPrefSize(1300, 750);

        mainLayout.setStyle(
                "-fx-background-color: rgba(255,255,255,0.7);" +
                        "-fx-background-radius: 40px;" +
                        "-fx-border-color: transparent;" +
                        "-fx-border-width: 0px;" +
                        "-fx-border-radius: 40px;" +
                        "-fx-font-family: 'Google Sans';"
        );

        mainShadow = new DropShadow();
        mainShadow.setRadius(45);
        mainShadow.setColor(Color.web("#311b92", 0.15));
        mainShadow.setOffsetX(0);
        mainShadow.setOffsetY(16);
        mainLayout.setEffect(mainShadow);

        // --- 3. SIDEBAR ---
        VBox sidebar = new VBox();

        // canbeimprove: Giảm độ rộng 20% (từ 240px -> 192px)
        sidebar.setPrefWidth(192); sidebar.setMinWidth(192); sidebar.setMaxWidth(192);
        sidebar.setPadding(new Insets(24, 16, 24, 16));
        sidebar.setStyle(
                "-fx-background-color: rgba(255,255,255,0.3);" +
                        "-fx-background-radius: 40px 0px 0px 40px;" +
                        "-fx-border-color: transparent;" +
                        "-fx-border-width: 0px;"
        );
        sidebar.setAlignment(Pos.TOP_CENTER);

        VBox logoBox = new VBox(8);
        logoBox.setAlignment(Pos.CENTER);
        logoBox.setPadding(new Insets(0, 0, 24, 0));

        // URL trực tiếp tới ảnh Google Drive (đã đổi format view -> uc?id=)
        String appLogoUrl = "cluby.png";

        StackPane logoImgContainer = new StackPane();
        logoImgContainer.setPrefSize(44, 44); logoImgContainer.setMaxSize(44, 44);

        ImageView appLogo = new ImageView(new Image(appLogoUrl));
        appLogo.setFitWidth(44);
        appLogo.setFitHeight(44);
        appLogo.setPreserveRatio(true);

        // Cắt ảnh logo cho bo góc (tùy chọn, để đồng bộ UI cũ)
        Rectangle clip = new Rectangle(44, 44);
        clip.setArcWidth(16);
        clip.setArcHeight(16);
        appLogo.setClip(clip);

        logoImgContainer.getChildren().add(appLogo);

        Label logoTitle = new Label("cluby");
        logoTitle.setFont(Font.font("Google Sans", FontWeight.EXTRA_BOLD, 20));
        logoTitle.setTextFill(Color.web("#2d3748"));
        logoTitle.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.15), 10, 0.4, 0, 4);");

        Label clockLabel = new Label();
        clockLabel.setFont(Font.font("Google Sans", FontWeight.BOLD, 10));
        clockLabel.setTextFill(Color.web("#64748b"));
        clockLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        clockLabel.setAlignment(Pos.CENTER);

        // wutisit: Dòng chào mừng tự động đổi theo thời gian thực
        Label greetingLabel = new Label();
        greetingLabel.setFont(Font.font("Google Sans", FontWeight.BOLD, 11));
        greetingLabel.setTextFill(Color.web("#7c4dff"));
        greetingLabel.setPadding(new Insets(4, 0, 0, 0));

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss\nEEEE, dd/MM/yyyy", new Locale("vi", "VN"));
            clockLabel.setText(now.format(formatter));

            // Logic đổi lời chào dựa theo múi giờ
            int hour = now.getHour();
            if (hour >= 4 && hour < 10) {
                greetingLabel.setText("Chào buổi sáng, Alexandra!");
            } else if (hour >= 10 && hour < 13) {
                greetingLabel.setText("Chào buổi trưa, Alexandra!");
            } else if (hour >= 13 && hour < 18) {
                greetingLabel.setText("Chào buổi chiều, Alexandra!");
            } else if (hour >= 18 && hour < 23) {
                greetingLabel.setText("Chào buổi tối, Alexandra!");
            } else {
                greetingLabel.setText("Đã khuya rồi, Alexandra!");
            }
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        logoBox.getChildren().addAll(logoImgContainer, logoTitle, clockLabel, greetingLabel);

        // --- DANH SÁCH MENU HOVER ---
        VBox navList = new VBox(4);
        navList.setAlignment(Pos.TOP_CENTER);

        // Nút Tổng quan - "Chung"
        Button navDashboard = createNavButton("🏠", "Chung", true, false);

        // --- Nhóm: NHÂN SỰ ---
        VBox hrGroup = new VBox(0);
        Button navHR = createNavButton("👥", "Nhân sự", false, false);

        VBox hrSubMenu = new VBox(2);
        hrSubMenu.setPadding(new Insets(2, 0, 4, 0));
        Button navHRMembers = createNavButton("•", "Thành viên", false, true);
        Button navHRDepts = createNavButton("•", "Ban", false, true);
        Button navHRRecruit = createNavButton("•", "Tuyển thành viên", false, true);
        hrSubMenu.getChildren().addAll(navHRMembers, navHRDepts, navHRRecruit);

        hrSubMenu.setManaged(false);
        hrSubMenu.setVisible(false);
        hrGroup.getChildren().addAll(navHR, hrSubMenu);

        hrGroup.setOnMouseEntered(e -> {
            hrSubMenu.setManaged(true);
            hrSubMenu.setVisible(true);
        });
        hrGroup.setOnMouseExited(e -> {
            hrSubMenu.setManaged(false);
            hrSubMenu.setVisible(false);
        });

        // --- Nhóm: HOẠT ĐỘNG ---
        VBox actGroup = new VBox(0);
        Button navActivities = createNavButton("📅", "Hoạt động", false, false);

        VBox actSubMenu = new VBox(2);
        actSubMenu.setPadding(new Insets(2, 0, 4, 0));
        Button navActEvents = createNavButton("•", "Sự kiện", false, true);
        Button navActAttendance = createNavButton("•", "Điểm danh", false, true);
        Button navActTasks = createNavButton("•", "Công việc", false, true);
        actSubMenu.getChildren().addAll(navActEvents, navActAttendance, navActTasks);

        actSubMenu.setManaged(false);
        actSubMenu.setVisible(false);
        actGroup.getChildren().addAll(navActivities, actSubMenu);

        actGroup.setOnMouseEntered(e -> {
            actSubMenu.setManaged(true);
            actSubMenu.setVisible(true);
        });
        actGroup.setOnMouseExited(e -> {
            actSubMenu.setManaged(false);
            actSubMenu.setVisible(false);
        });

        // Các nút đơn giản còn lại
        Button navFinance = createNavButton("💰", "Tài chính", false, false);
        Button navNotif = createNavButton("📢", "Thông báo", false, false);
        Button navReports = createNavButton("📊", "Báo cáo", false, false);
        Button navAccount = createNavButton("👤", "Tài khoản", false, false);

        allNavButtons = new Button[] {
                navDashboard, navHR, navHRMembers, navHRDepts, navHRRecruit,
                navActivities, navActEvents, navActAttendance, navActTasks,
                navFinance, navNotif, navReports, navAccount
        };

        // Gắn các cụm vào thanh sidebar
        navList.getChildren().addAll(navDashboard, hrGroup, actGroup, navFinance, navNotif, navReports, navAccount);

        for (Button btn : allNavButtons) {
            btn.setOnAction(e -> {
                HBox graphic = (HBox) btn.getGraphic();
                String rawTitle = graphic.getChildren().size() > 1
                        ? ((Label) graphic.getChildren().get(1)).getText()
                        : ((Label) graphic.getChildren().get(0)).getText();
                switchTabFocus(btn, rawTitle);
            });
        }

        // --- HỒ SƠ & BUTTON ĐĂNG XUẤT ---
        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        VBox userProfile = new VBox(8);
        userProfile.setAlignment(Pos.CENTER);
        userProfile.setPadding(new Insets(12));
        userProfile.setStyle(
                "-fx-background-color: rgba(255,255,255,0.35);" +
                        "-fx-background-radius: 30px;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.1), 12, 0.3, 0, 6);"
        );

        HBox userActions = new HBox(8);
        userActions.setAlignment(Pos.CENTER);

        Button btnLogout = new Button("🚪");
        btnLogout.setPrefSize(44, 44);
        btnLogout.setStyle(
                "-fx-background-color: #ef4444;" +
                        "-fx-background-radius: 22px;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 18px;" +
                        "-fx-padding: 0;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(239,68,68,0.5), 10, 0.4, 0, 4);"
        );
        btnLogout.setOnMouseEntered(e -> { btnLogout.setScaleX(1.1); btnLogout.setScaleY(1.1); });
        btnLogout.setOnMouseExited(e -> { btnLogout.setScaleX(1.0); btnLogout.setScaleY(1.0); });
        btnLogout.setOnAction(e -> openLogoutModal());

        Button btnAvatar = new Button();
        btnAvatar.setPrefSize(44, 44);

        ImageView userImg = new ImageView(new Image("trish.jpeg"));
        userImg.setFitWidth(44); userImg.setFitHeight(44);
        userImg.setPreserveRatio(false);
        userImg.setClip(new Circle(22, 22, 22));

        btnAvatar.setGraphic(userImg);
        btnAvatar.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-padding: 0;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-radius: 22px;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.2), 10, 0.4, 0, 4);"
        );
        btnAvatar.setOnMouseEntered(e -> { btnAvatar.setScaleX(1.1); btnAvatar.setScaleY(1.1); });
        btnAvatar.setOnMouseExited(e -> { btnAvatar.setScaleX(1.0); btnAvatar.setScaleY(1.0); });
        btnAvatar.setOnAction(e -> triggerToast("Đang mở phần cài đặt..."));

        userActions.getChildren().addAll(btnLogout, btnAvatar);

        Label userName = new Label("Alexandra");
        userName.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));
        userName.setTextFill(Color.web("#1e293b"));

        Label userRole = new Label("jobless hihi");
        userRole.setFont(Font.font("Google Sans", FontWeight.EXTRA_BOLD, 8));
        userRole.setTextFill(Color.web("#64748b"));

        userProfile.getChildren().addAll(userActions, userName, userRole);
        sidebar.getChildren().addAll(logoBox, navList, sidebarSpacer, userProfile);

        // --- 4. MAIN AREA ---
        VBox mainArea = new VBox();
        HBox.setHgrow(mainArea, Priority.ALWAYS);

        HBox topBar = new HBox();
        topBar.setPadding(new Insets(24, 32, 24, 32));
        topBar.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(2);

        // Cài đặt tên Tiêu đề mặc định khi mới mở App
        mainTitle = new Label("Nắm tình hình nào!");
        mainTitle.setFont(Font.font("Google Sans", FontWeight.BLACK, 32));
        mainTitle.setTextFill(Color.web("#1e293b"));
        mainTitle.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.1), 10, 0.4, 0, 4);");

        titleBox.getChildren().add(mainTitle);

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);

        // Vùng chứa thanh công cụ bên phải (hiện tại trống do đã xoá Search và Button Footage)
        HBox topActions = new HBox(16);
        topActions.setAlignment(Pos.CENTER_RIGHT);

        topBar.getChildren().addAll(titleBox, topSpacer, topActions);

        // --- 5. VÙNG VIEW ĐỘNG ---
        viewContainer = new StackPane();
        viewContainer.setPadding(new Insets(0, 32, 32, 32));
        VBox.setVgrow(viewContainer, Priority.ALWAYS);

        mainArea.getChildren().addAll(topBar, viewContainer);
        mainLayout.getChildren().addAll(sidebar, mainArea);

        // --- 6. OVERLAY CHỨA MODAL ---
        modalOverlay = new StackPane();
        modalOverlay.setStyle("-fx-background-color: rgb(57 0 216);");
        modalOverlay.setVisible(false);

        // --- 7. TOAST NOTIFICATIONS ---
        toastContainer = new VBox(8);
        toastContainer.setMaxSize(300, 200);
        StackPane.setAlignment(toastContainer, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(toastContainer, new Insets(0, 30, 30, 0));

        rootPane.getChildren().addAll(mainLayout, modalOverlay, toastContainer);

        Scene scene = new Scene(rootPane, 1366, 800);
        primaryStage.setTitle("cluby"); // Cập nhật tên cửa sổ ứng dụng

        // Cài đặt Window Icon để logo hiển thị trên taskbar và góc trên cửa sổ
        primaryStage.getIcons().add(new Image(appLogoUrl));

        primaryStage.setScene(scene);
        primaryStage.show();

        triggerToast("Cập nhật giờ tự động kích hoạt!");
    }

    public void setView(Node newView) {
        if (viewContainer != null) {
            viewContainer.getChildren().setAll(newView);
        }
    }

    public void setMainTitle(String title) {
        if (mainTitle != null) {
            mainTitle.setText(title);
        }
    }

    private void switchTabFocus(Button activeBtn, String title) {
        for (Button btn : allNavButtons) {
            setButtonInactiveStyle(btn);
        }
        setButtonActiveStyle(activeBtn);
        activeNavBtn = activeBtn;

        // Nếu tiêu đề mục là "Chung", chuyển thành dòng ngầu ngầu
        if (title.equals("Chung")) {
            setMainTitle("Nắm tình hình nào!");
        } else {
            setMainTitle(title);
        }

        triggerToast("Đã đến " + title.toUpperCase());
    }

    // canbereuse: Builder tuỳ biến tạo nút.
    private Button createNavButton(String emoji, String title, boolean isActive, boolean isSubItem) {
        Button btn = new Button();
        btn.setMaxWidth(Double.MAX_VALUE);

        btn.setPrefHeight(isSubItem ? 32 : 36);
        btn.setAlignment(Pos.CENTER_LEFT);

        // Giảm padding trái đi đôi chút do tổng sidebar chỉ còn 192px
        if (isSubItem) {
            btn.setPadding(new Insets(6, 12, 6, 36));
        } else {
            btn.setPadding(new Insets(8, 12, 8, 12));
        }

        HBox graphicBox = new HBox(8);
        graphicBox.setAlignment(Pos.CENTER_LEFT);

        if (!emoji.isEmpty()) {
            Label iconLbl = new Label(emoji);
            iconLbl.setStyle("-fx-font-size: " + (isSubItem ? "14px;" : "16px;"));
            Label titleLbl = new Label(title);
            titleLbl.setFont(Font.font("Google Sans", isSubItem ? FontWeight.MEDIUM : FontWeight.BOLD, 12));
            graphicBox.getChildren().addAll(iconLbl, titleLbl);
        } else {
            Label titleLbl = new Label(title);
            titleLbl.setFont(Font.font("Google Sans", isSubItem ? FontWeight.MEDIUM : FontWeight.BOLD, 12));
            graphicBox.getChildren().add(titleLbl);
        }

        btn.setGraphic(graphicBox);

        if (isActive) {
            setButtonActiveStyle(btn);
            activeNavBtn = btn;
        } else {
            setButtonInactiveStyle(btn);
        }

        btn.setOnMouseEntered(e -> {
            btn.setScaleX(1.02);
            btn.setScaleY(1.02);
            if (btn != activeNavBtn) {
                btn.setStyle(
                        "-fx-background-color: rgba(255,255,255,0.25);" +
                                "-fx-background-radius: 12px;" +
                                "-fx-border-color: transparent;" +
                                "-fx-border-width: 0px;" +
                                "-fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.1), 10, 0.4, 0, 4);" +
                                "-fx-cursor: hand;"
                );
            }
        });

        btn.setOnMouseExited(e -> {
            btn.setScaleX(1.0);
            btn.setScaleY(1.0);
            if (btn != activeNavBtn) {
                setButtonInactiveStyle(btn);
            }
        });

        return btn;
    }

    private void setButtonActiveStyle(Button btn) {
        btn.setStyle(
                "-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, rgba(255,255,255,0.92) 0%, rgba(255,255,255,0.65) 100%);" +
                        "-fx-background-radius: 12px;" +
                        "-fx-border-color: transparent;" +
                        "-fx-border-width: 0px;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.12), 10, 0.4, 0, 4);"
        );
        HBox box = (HBox) btn.getGraphic();
        for (Node n : box.getChildren()) {
            if (n instanceof Label) ((Label)n).setTextFill(Color.web("#7c4dff"));
        }
    }

    private void setButtonInactiveStyle(Button btn) {
        btn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background-radius: 12px;" +
                        "-fx-border-color: transparent;" +
                        "-fx-border-width: 0px;"
        );
        HBox box = (HBox) btn.getGraphic();
        for (Node n : box.getChildren()) {
            if (n instanceof Label) ((Label)n).setTextFill(Color.web("#475569"));
        }
    }

    // --- HỆ THỐNG MODALS ---
    private void openLogoutModal() {
        modalOverlay.getChildren().clear();
        modalOverlay.getChildren().add(createLogoutModalBody());
        showOverlay();
    }

    private void showOverlay() {
        GaussianBlur blur = new GaussianBlur(25);
        ColorAdjust darken = new ColorAdjust();
        darken.setBrightness(-0.4);
        darken.setInput(blur);
        mainLayout.setEffect(darken);
        modalOverlay.setVisible(true);

        FadeTransition ft = new FadeTransition(Duration.millis(200), modalOverlay);
        ft.setInterpolator(Interpolator.EASE_BOTH);
        ft.setFromValue(0); ft.setToValue(1);
        ft.play();
    }

    public void closeOverlayModal() {
        FadeTransition ft = new FadeTransition(Duration.millis(200), modalOverlay);
        ft.setInterpolator(Interpolator.EASE_BOTH);
        ft.setFromValue(1); ft.setToValue(0);
        ft.setOnFinished(e -> {
            modalOverlay.setVisible(false);
            mainLayout.setEffect(mainShadow);
        });
        ft.play();
    }

    private VBox createLogoutModalBody() {
        VBox box = new VBox(16);
        box.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(28));
        box.setStyle(
                "-fx-background-color: rgba(255,255,255);" +
                        "-fx-background-radius: 40px;" +
                        "-fx-border-color: transparent;" +
                        "-fx-border-width: 0px;" +
                        "-fx-font-family: 'Google Sans';"
        );

        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = new Label("Xác nhận Đăng xuất");
        title.setFont(Font.font("Google Sans", FontWeight.BLACK, 22));
        title.setTextFill(Color.web("#1e293b"));

        Label sub = new Label("Bạn có chắc chắn muốn rời khỏi ứng dụng không?");
        sub.setFont(Font.font("Google Sans", FontWeight.MEDIUM, 11));
        sub.setTextFill(Color.web("#94a3b8"));

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);
        actions.setPadding(new Insets(10, 0, 0, 0));

        Button btnCancel = new Button("Huỷ");
        btnCancel.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));
        btnCancel.setTextFill(Color.web("#64748b"));
        btnCancel.setStyle(
                "-fx-background-color: rgb(178, 162, 228,0.2);" +
                        "-fx-background-radius: 40px;" +
                        "-fx-border-color: transparent;" +
                        "-fx-border-width: 0px;" +
                        "-fx-padding: 8 16 8 16;" +
                        "-fx-cursor: hand;"
        );

        btnCancel.setOnMouseEntered(e -> { btnCancel.setScaleX(1.05); btnCancel.setScaleY(1.05); });
        btnCancel.setOnMouseExited(e -> { btnCancel.setScaleX(1.0); btnCancel.setScaleY(1.0); });
        btnCancel.setOnAction(e -> closeOverlayModal());

        Button btnLeave = new Button("Rời đi");
        btnLeave.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));
        btnLeave.setTextFill(Color.WHITE);
        btnLeave.setStyle(
                "-fx-background-color: #ef4444;" +
                        "-fx-background-radius: 40px;" +
                        "-fx-border-color: transparent;" +
                        "-fx-border-width: 0px;" +
                        "-fx-padding: 8 16 8 16;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(239,68,68,0.3), 10, 0.3, 0, 6);" +
                        "-fx-cursor: hand;"
        );

        btnLeave.setOnMouseEntered(e -> { btnLeave.setScaleX(1.05); btnLeave.setScaleY(1.05); });
        btnLeave.setOnMouseExited(e -> { btnLeave.setScaleX(1.0); btnLeave.setScaleY(1.0); });
        btnLeave.setOnAction(e -> {
            closeOverlayModal();
            triggerToast("Đã đăng xuất khỏi hệ thống thành công!");
        });

        actions.getChildren().addAll(btnCancel, btnLeave);
        box.getChildren().addAll(title, sub, actions);

        return box;
    }

    // --- HỆ THỐNG THÔNG BÁO TOAST ---
    public void triggerToast(String msg) {
        HBox toast = new HBox(12);
        toast.setAlignment(Pos.CENTER_LEFT);
        toast.setPadding(new Insets(12, 20, 12, 20));
        toast.setStyle(
                "-fx-background-color: rgba(255,255,255,0.92);" +
                        "-fx-background-radius: 40px;" +
                        "-fx-border-color: transparent;" +
                        "-fx-border-width: 0px;" +
                        "-fx-font-family: 'Google Sans';" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.12), 20, 0.4, 0, 8);"
        );

        Label icon = new Label("✨");
        icon.setStyle("-fx-text-fill: #000000; -fx-font-size: 14px;");

        Label text = new Label(msg);
        text.setFont(Font.font("Google Sans", FontWeight.BOLD, 12));
        text.setTextFill(Color.web("#1e293b"));

        toast.getChildren().addAll(icon, text);
        toastContainer.getChildren().add(toast);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(350), toast);
        fadeIn.setInterpolator(Interpolator.EASE_BOTH);
        fadeIn.setFromValue(0); fadeIn.setToValue(1);
        fadeIn.play();

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(350), toast);
        slideIn.setInterpolator(Interpolator.EASE_BOTH);
        slideIn.setFromY(15); slideIn.setToY(0);
        slideIn.play();

        FadeTransition fadeOut = new FadeTransition(Duration.millis(400), toast);
        fadeOut.setInterpolator(Interpolator.EASE_BOTH);
        fadeOut.setFromValue(1); fadeOut.setToValue(0);
        fadeOut.setDelay(Duration.seconds(3.5));

        TranslateTransition slideOut = new TranslateTransition(Duration.millis(400), toast);
        slideOut.setInterpolator(Interpolator.EASE_BOTH);
        slideOut.setFromY(0); slideOut.setToY(15);
        slideOut.setDelay(Duration.seconds(3.5));

        fadeOut.setOnFinished(e -> toastContainer.getChildren().remove(toast));
        fadeOut.play();
        slideOut.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}