package view;

import javafx.animation.*;
import javafx.application.Application;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class lobby extends Application {

    private static lobby instance;
    public static lobby getInstance() { return instance; }

    private StackPane rootPane;
    private HBox mainLayout;
    private StackPane modalOverlay;
    private VBox toastContainer;
    private StackPane viewContainer;
    private DropShadow mainShadow;
    private Button btnNavExplore;
    private Button btnNavSetting;
    private Button activeNavBtn;

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
        mainLayout.setMaxSize(1300, 750);
        mainLayout.setPrefSize(1300, 750);
        mainLayout.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 40px;");

        mainShadow = new DropShadow(45, 0, 16, Color.web("#311b92", 0.15));
        mainLayout.setEffect(mainShadow);

        VBox sidebar = createSidebar();

        VBox mainArea = new VBox();
        HBox.setHgrow(mainArea, Priority.ALWAYS);

        viewContainer = new StackPane();
        VBox.setVgrow(viewContainer, Priority.ALWAYS);

        mainArea.getChildren().add(viewContainer);
        mainLayout.getChildren().addAll(sidebar, mainArea);

        modalOverlay = new StackPane();
        modalOverlay.setStyle("-fx-background-color: rgb(57 0 216 / 0.15);");
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
        primaryStage.getIcons().add(new Image("cluby.png"));
        primaryStage.setScene(scene);
        primaryStage.show();

        rootPane.requestFocus();
        switchView("Khám phá");
        triggerToast("Đã kết nối");
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox();
        sidebar.setPrefWidth(220); sidebar.setMinWidth(220); sidebar.setMaxWidth(220);
        sidebar.setPadding(new Insets(32, 16, 24, 16));
        sidebar.setStyle("-fx-background-color: rgba(255,255,255,0.3); -fx-background-radius: 40px 0px 0px 40px;");
        sidebar.setAlignment(Pos.TOP_CENTER);

        VBox logoBox = new VBox(12);
        logoBox.setAlignment(Pos.CENTER);
        logoBox.setPadding(new Insets(0, 0, 32, 0));

        ImageView appLogo = new ImageView(new Image("cluby.png"));
        appLogo.setFitWidth(60); appLogo.setFitHeight(60);

        Label logoTitle = format.formatLabel("cluby", FontWeight.BLACK, 24, "#2d3748");
        logoTitle.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(49,27,146,0.15), 10, 0.4, 0, 4);");

        logoBox.getChildren().addAll(appLogo, logoTitle);

        VBox navList = new VBox(8);
        navList.setAlignment(Pos.TOP_CENTER);

        btnNavExplore = createNavButton("🌍", "Khám phá");
        btnNavSetting = createNavButton("⚙", "Cài đặt");

        navList.getChildren().addAll(btnNavExplore, btnNavSetting);

        Region spacer = new Region(); VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox userProfile = new VBox(12);
        userProfile.setAlignment(Pos.CENTER);

        Button btnLogout = getShadowBtn("Đăng xuất", "", "#ef4444", "white", "rgba(239,68,68,0.4)");
        btnLogout.setMaxWidth(Double.MAX_VALUE);
        btnLogout.setOnAction(e -> System.exit(0));

        Button btnDelete = new Button("Xóa tài khoản");
        btnDelete.setMaxWidth(Double.MAX_VALUE);
        btnDelete.setStyle("-fx-background-color: transparent; -fx-text-fill: #ef4444; -fx-font-family: 'Google Sans'; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 8 16; -fx-border-color: #ef4444; -fx-border-radius: 40px; -fx-cursor: hand;");
        btnDelete.setOnMouseEntered(e -> btnDelete.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-font-family: 'Google Sans'; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 8 16; -fx-border-color: #ef4444; -fx-border-radius: 40px; -fx-cursor: hand;"));
        btnDelete.setOnMouseExited(e -> btnDelete.setStyle("-fx-background-color: transparent; -fx-text-fill: #ef4444; -fx-font-family: 'Google Sans'; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 8 16; -fx-border-color: #ef4444; -fx-border-radius: 40px; -fx-cursor: hand;"));

        btnDelete.setOnAction(e -> showCustomModal(createDeleteAccountModal()));

        userProfile.getChildren().addAll(btnLogout, btnDelete);
        sidebar.getChildren().addAll(logoBox, navList, spacer, userProfile);

        return sidebar;
    }

    private Button createNavButton(String emoji, String title) {
        Button btn = format.formatNavBtn(emoji, title, false);
        btn.setFocusTraversable(false);
        format.formatNavBtnInactive(btn);
        btn.setOnAction(e -> switchView(title));

        btn.setOnMouseEntered(e -> {
            btn.setScaleX(1.02); btn.setScaleY(1.02);
            if (btn != activeNavBtn) {
                btn.setStyle("-fx-background-color: rgba(255,255,255,0.25); -fx-background-radius: 12px; -fx-cursor: hand;");
            }
        });
        btn.setOnMouseExited(e -> {
            btn.setScaleX(1.0); btn.setScaleY(1.0);
            if (btn != activeNavBtn) format.formatNavBtnInactive(btn);
        });
        return btn;
    }

    private void switchView(String title) {
        if (activeNavBtn != null) format.formatNavBtnInactive(activeNavBtn);
        if (title.equals("Khám phá")) {
            activeNavBtn = btnNavExplore;
            format.formatNavBtnActive(btnNavExplore);
            viewContainer.getChildren().setAll(createExploreView());
        } else if (title.equals("Cài đặt")) {
            activeNavBtn = btnNavSetting;
            format.formatNavBtnActive(btnNavSetting);
            viewContainer.getChildren().setAll(createSettingView());
        }
    }

    private ScrollPane createExploreView() {
        VBox mainContent = new VBox(24);
        mainContent.setPadding(new Insets(68, 26, 26, 26));
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setStyle("-fx-background-color: transparent;");

        VBox centerWrapper = new VBox(24);
        centerWrapper.setAlignment(Pos.TOP_CENTER);
        centerWrapper.setMaxWidth(950);

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = format.formatLabel("Câu lạc bộ", FontWeight.BLACK, 32, "#1e293b");
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnCreateClub = getShadowBtn("Tạo Câu lạc bộ", "", "#5020d8", "white", "rgba(80,32,216,0.4)");
        btnCreateClub.setOnAction(e -> showCustomModal(createAddClubModal()));

        header.getChildren().addAll(title, spacer, btnCreateClub);

        HBox filterBar = new HBox(16);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(12, 24, 12, 24));
        format.formatGlass(filterBar, 40, 0.4);

        HBox searchBox = new HBox(8);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = format.formatTextField("Tìm kiếm...");
        Button btnSearch = format.formatFindBtn();
        searchBox.getChildren().addAll(searchField, btnSearch);

        ComboBox<String> cbSort = format.formatSortBtn("Sắp xếp", "Mới nhất", "Cũ nhất");

        Region fSpacer = new Region(); HBox.setHgrow(fSpacer, Priority.ALWAYS);
        filterBar.getChildren().addAll(searchBox, cbSort, fSpacer);

        VBox tableContainer = format.formatTableContainer();

        HBox tHeader = new HBox(16);
        tHeader.setPadding(new Insets(12, 16, 12, 16));
        tHeader.setStyle("-fx-border-color: transparent transparent rgba(255,255,255,0.4) transparent; -fx-border-width: 1px;");

        Label l1 = format.formatLabel("LOGO", FontWeight.BLACK, 10, "#94a3b8"); l1.setPrefWidth(60);
        Label l2 = format.formatLabel("TÊN CÂU LẠC BỘ", FontWeight.BLACK, 10, "#94a3b8"); l2.setPrefWidth(200);
        Label l3 = format.formatLabel("QUẢN LÝ", FontWeight.BLACK, 10, "#94a3b8"); l3.setPrefWidth(160);
        Label l4 = format.formatLabel("NGÀY TẠO", FontWeight.BLACK, 10, "#94a3b8"); l4.setPrefWidth(120);
        Label l5 = format.formatLabel("HÀNH ĐỘNG", FontWeight.BLACK, 10, "#94a3b8"); l5.setPrefWidth(240);

        tHeader.getChildren().addAll(l1, l2, l3, l4, l5);
        tableContainer.getChildren().add(tHeader);

        VBox rows = new VBox(4);
        rows.getChildren().addAll(
                createClubRow("temp.png", "Google Developer Student Club", "Minh Quân", "12/05/2024", "Cộng đồng lập trình và phát triển giải pháp công nghệ."),
                createClubRow("temp.png", "English Speaking Club", "Thu Hà", "08/09/2024", "Không gian giao tiếp và rèn luyện tiếng Anh thực tế."),
                createClubRow("temp.png", "Melody Music Club", "Đức Anh", "15/11/2024", "Nơi kết nối đam mê âm nhạc và tổ chức các đêm nhạc nội bộ.")
        );

        ScrollPane scrollList = new ScrollPane(rows);
        scrollList.setPrefHeight(400);
        format.formatScrollbar(scrollList, rows, 8);
        applySmoothScroll(scrollList, rows);

        tableContainer.getChildren().add(scrollList);
        centerWrapper.getChildren().addAll(header, filterBar, tableContainer);
        mainContent.getChildren().add(centerWrapper);

        ScrollPane mainScroll = new ScrollPane(mainContent);
        mainScroll.setPadding(new Insets(0));
        mainScroll.setFitToWidth(true);
        mainScroll.setFitToHeight(true);
        format.formatScrollbar(mainScroll, mainContent, 12);

        return mainScroll;
    }

    private HBox createClubRow(String logoUrl, String name, String president, String date, String desc) {
        HBox row = new HBox(16);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand;");

        row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand; -fx-background-radius: 16px;"));
        row.setOnMouseExited(e -> row.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent rgba(255,255,255,0.3) transparent; -fx-border-width: 1px; -fx-cursor: hand;"));

        ImageView logo = new ImageView(new Image(logoUrl));
        logo.setFitWidth(40); logo.setFitHeight(40); logo.setClip(new Circle(20, 20, 20));
        HBox logoBox = new HBox(logo); logoBox.setPrefWidth(60);

        Label lblName = format.formatLabel(name, FontWeight.BLACK, 14, "#5020d8"); lblName.setPrefWidth(200); lblName.setWrapText(true);
        Label lblPres = format.formatLabel(president, FontWeight.BOLD, 13, "#475569"); lblPres.setPrefWidth(160); lblPres.setWrapText(true);
        Label lblDate = format.formatLabel(date, FontWeight.MEDIUM, 13, "#64748b"); lblDate.setPrefWidth(120);

        HBox actionBox = new HBox(12);
        actionBox.setAlignment(Pos.CENTER_LEFT); actionBox.setPrefWidth(240);

        Button btnView = format.formatCircleBtn("👁️‍🗨️", "#448aff", "#7c4dff");
        btnView.setMinWidth(40);
        btnView.setOnAction(e -> showCustomModal(createClubDetailModal(name, president, date, desc)));

        Button btnJoin = getShadowBtn("Tham gia", "", "#3b82f6", "white", "rgba(59,130,246,0.4)");
        btnJoin.setOnAction(e -> showCustomModal(createJoinRequestModal(name)));

        actionBox.getChildren().addAll(btnView, btnJoin);

        row.getChildren().addAll(logoBox, lblName, lblPres, lblDate, actionBox);
        return row;
    }

    private StackPane createClubDetailModal(String name, String pres, String date, String desc) {
        StackPane rootModalPane = new StackPane();

        VBox box = new VBox(24);
        box.setPrefWidth(650); box.setMaxSize(650, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(8);
        Label lblName = format.formatLabel(name, FontWeight.BLACK, 28, "#5020d8");
        lblName.setWrapText(true);

        HBox badgeBox = new HBox(8);
        badgeBox.getChildren().addAll(
                format.formatBadge("120 thành viên", "rgba(59,130,246,0.15)", "#3b82f6"),
                format.formatBadge("4 bộ phận", "rgba(16,185,129,0.15)", "#10b981")
        );

        titleBox.getChildren().addAll(lblName, badgeBox);
        header.getChildren().addAll(titleBox);

        VBox scrollContent = new VBox(24);
        scrollContent.setStyle("-fx-background-color: transparent;");

        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(40); infoGrid.setVgap(16);
        infoGrid.add(new VBox(4, format.formatLabel("Quản lý", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(pres, FontWeight.BLACK, 15, "#1e293b")), 0, 0);
        infoGrid.add(new VBox(4, format.formatLabel("Ngày tạo", FontWeight.BOLD, 12, "#94a3b8"), format.formatLabel(date, FontWeight.BLACK, 15, "#1e293b")), 1, 0);

        Label lblDesc = format.formatLabel(desc, FontWeight.MEDIUM, 14, "#475569"); lblDesc.setWrapText(true);
        infoGrid.add(new VBox(4, format.formatLabel("Giới thiệu", FontWeight.BOLD, 12, "#94a3b8"), lblDesc), 0, 1, 2, 1);

        scrollContent.getChildren().add(infoGrid);

        VBox deptBox = format.formatBoxCard();
        deptBox.setPadding(new Insets(20));
        deptBox.getChildren().add(format.formatLabel("CƠ CẤU", FontWeight.BLACK, 12, "#94a3b8"));

        FlowPane deptGrid = new FlowPane();
        deptGrid.setHgap(12); deptGrid.setVgap(12);
        deptGrid.getChildren().addAll(
                createDeptTag("Nội dung", "15"),
                createDeptTag("Kỹ thuật", "20"),
                createDeptTag("Truền thông", "25"),
                createDeptTag("Hậu cần", "12")
        );

        deptBox.getChildren().add(deptGrid);
        scrollContent.getChildren().add(deptBox);

        ScrollPane scrollPane = new ScrollPane(scrollContent);
        scrollPane.setPrefHeight(300);
        format.formatScrollbar(scrollPane, scrollContent, 12);
        applySmoothScroll(scrollPane, scrollContent);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(16, 0, 0, 0));

        Button btnClose = getModalActionBtn("Đóng", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnClose.setOnAction(e -> closeOverlayModal());

        Button btnJoin = getModalActionBtn("Tham gia Câu lạc bộ", "#3b82f6", "white", "rgba(59,130,246,0.4)");
        btnJoin.setOnAction(e -> {
            showCustomModal(createJoinRequestModal(name));
        });

        actions.getChildren().addAll(btnClose, btnJoin);

        box.getChildren().addAll(header, scrollPane, actions);
        rootModalPane.getChildren().add(box);

        return rootModalPane;
    }

    private HBox createDeptTag(String name, String count) {
        HBox box = new HBox(8);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(8, 16, 8, 16));
        box.setStyle("-fx-background-color: rgba(124,77,255,0.08); -fx-background-radius: 20px;");
        Label lblName = format.formatLabel(name, FontWeight.BOLD, 13, "#5020d8");
        Label lblCount = format.formatLabel(count, FontWeight.BOLD, 11, "#7c4dff");
        box.getChildren().addAll(lblName, lblCount);
        return box;
    }

    private VBox createJoinRequestModal(String clubName) {
        VBox box = new VBox(20);
        box.setPrefWidth(380); box.setMaxSize(380, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel("Tham gia Câu lạc bộ", FontWeight.BLACK, 24, "#5020d8");
        title.setWrapText(true);

        VBox fields = new VBox(16);

        ComboBox<String> cbDept = format.formatSortBtn("Bộ phận", "Nội dung", "Kỹ thuật", "Truyền thông", "Hậu cần");
        cbDept.setMaxWidth(Double.MAX_VALUE);
        fields.getChildren().add(new VBox(6, format.formatLabel("Bộ phận đăng ký *", FontWeight.BOLD, 12, "#94a3b8"), cbDept));

        TextField fIntro = format.formatTextField("Giới thiệu ngắn...");
        fields.getChildren().add(new VBox(6, format.formatLabel("Giới thiệu *", FontWeight.BOLD, 12, "#94a3b8"), fIntro));

        TextField fExp = format.formatTextField("Kinh nghiệm nếu có...");
        fields.getChildren().add(new VBox(6, format.formatLabel("Kinh nghiệm", FontWeight.BOLD, 12, "#94a3b8"), fExp));

        TextField fReason = format.formatTextField("Lý do tham gia...");
        fields.getChildren().add(new VBox(6, format.formatLabel("Lý do *", FontWeight.BOLD, 12, "#94a3b8"), fReason));

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(16, 0, 0, 0));

        Button btnCancel = getModalActionBtn("Hủy", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> closeOverlayModal());

        Button btnSubmit = getModalActionBtn("Gửi", "#3b82f6", "white", "rgba(59,130,246,0.4)");
        btnSubmit.setOnAction(e -> {
            if (cbDept.getValue() == null || fIntro.getText().trim().isEmpty() || fReason.getText().trim().isEmpty()) {
                triggerToast("Vui lòng nhập đủ thông tin bắt buộc");
                return;
            }
            closeOverlayModal();
            triggerToast("Đã gửi đơn thành công");
        });

        actions.getChildren().addAll(btnCancel, btnSubmit);
        box.getChildren().addAll(title, fields, actions);

        return box;
    }

    private VBox createAddClubModal() {
        VBox box = new VBox(20);
        box.setPrefWidth(400); box.setMaxSize(400, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel("Tạo Câu lạc bộ", FontWeight.BLACK, 24, "#5020d8");

        VBox fields = new VBox(16);

        TextField fName = format.formatTextField("Tên câu lạc bộ...");
        fields.getChildren().add(new VBox(6, format.formatLabel("Tên Câu lạc bộ *", FontWeight.BOLD, 12, "#94a3b8"), fName));

        TextField fDesc = format.formatTextField("Mô tả hoạt động...");
        fields.getChildren().add(new VBox(6, format.formatLabel("Mô tả *", FontWeight.BOLD, 12, "#94a3b8"), fDesc));

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(16, 0, 0, 0));

        Button btnCancel = getModalActionBtn("Hủy", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> closeOverlayModal());

        Button btnConfirm = getModalActionBtn("Xác nhận", "#3b82f6", "white", "rgba(59,130,246,0.4)");
        btnConfirm.setOnAction(e -> {
            if(fName.getText().trim().isEmpty() || fDesc.getText().trim().isEmpty()) {
                triggerToast("Vui lòng điền tên và mô tả"); return;
            }
            closeOverlayModal();
            triggerToast("Đã gửi yêu cầu tạo Câu lạc bộ");
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().addAll(title, fields, actions);

        return box;
    }

    private ScrollPane createSettingView() {
        VBox mainContent = new VBox(32);
        // Padding vừa phải, Alignment CHÍNH GIỮA
        mainContent.setPadding(new Insets(20, 20, 20, 20));
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setStyle("-fx-background-color: transparent;");

        VBox centerWrapper = new VBox(24);
        centerWrapper.setAlignment(Pos.CENTER);
        centerWrapper.setPrefWidth(665);
        centerWrapper.setMaxWidth(665);

        VBox formBox = format.formatBoxCard();
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(40));

        Label sectionTitle = format.formatLabel("THÔNG TIN CÁ NHÂN", FontWeight.BLACK, 16, "#5020d8");
        HBox titleBox = new HBox(sectionTitle);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(0, 0, 24, 0));

        Pane pane = new Pane();
        pane.setPrefSize(585, 450);

        ImageView avt = new ImageView(new Image("temp.png"));
        avt.setFitWidth(150); avt.setFitHeight(150);
        avt.setClip(new Circle(75, 75, 75));
        avt.setLayoutX(35);
        avt.setLayoutY(80);

        Button btnChangeAvt = new Button("Thay đổi ảnh");
        btnChangeAvt.setFont(Font.font("Google Sans", FontWeight.BOLD, 13));
        btnChangeAvt.setTextFill(Color.web("#475569"));
        btnChangeAvt.setStyle("-fx-background-color: rgba(100,116,139,0.1); -fx-background-radius: 20px; -fx-padding: 8 20; -fx-cursor: hand;");
        btnChangeAvt.setOnMouseEntered(e -> btnChangeAvt.setStyle("-fx-background-color: rgba(100,116,139,0.2); -fx-background-radius: 20px; -fx-padding: 8 20; -fx-cursor: hand;"));
        btnChangeAvt.setOnMouseExited(e -> btnChangeAvt.setStyle("-fx-background-color: rgba(100,116,139,0.1); -fx-background-radius: 20px; -fx-padding: 8 20; -fx-cursor: hand;"));
        btnChangeAvt.setOnAction(e -> triggerToast("Đã mở trình chọn ảnh"));
        btnChangeAvt.setLayoutX(54);
        btnChangeAvt.setLayoutY(246);

        double startX = 230;
        double fieldWidth = 355;

        Label lId = format.formatLabel("Mã định danh hệ thống", FontWeight.BOLD, 13, "#94a3b8");
        lId.setLayoutX(startX); lId.setLayoutY(0);
        TextField fId = format.formatTextField("26MB001");
        fId.setDisable(true);
        fId.setPrefWidth(fieldWidth);
        fId.setLayoutX(startX); fId.setLayoutY(22);

        Label lName = format.formatLabel("Họ và tên", FontWeight.BOLD, 13, "#94a3b8");
        lName.setLayoutX(startX); lName.setLayoutY(76);
        TextField fName = format.formatTextField("Họ và tên"); fName.setText("Nguyễn Văn A");
        fName.setPrefWidth(fieldWidth);
        fName.setLayoutX(startX); fName.setLayoutY(98);

        Label lEmail = format.formatLabel("Email", FontWeight.BOLD, 13, "#94a3b8");
        lEmail.setLayoutX(startX); lEmail.setLayoutY(152);
        TextField fEmail = format.formatTextField("Email"); fEmail.setText("vanya@gmail.com");
        fEmail.setPrefWidth(fieldWidth);
        fEmail.setLayoutX(startX); fEmail.setLayoutY(174);

        Label lPhone = format.formatLabel("Số điện thoại", FontWeight.BOLD, 13, "#94a3b8");
        lPhone.setLayoutX(startX); lPhone.setLayoutY(228);
        TextField fPhone = format.formatTextField("Số điện thoại"); fPhone.setText("0901234567");
        fPhone.setPrefWidth(fieldWidth);
        fPhone.setLayoutX(startX); fPhone.setLayoutY(250);

        Label lPass = format.formatLabel("Mật khẩu mới", FontWeight.BOLD, 13, "#94a3b8");
        lPass.setLayoutX(startX); lPass.setLayoutY(304);
        PasswordField fPass = new PasswordField();
        fPass.setPromptText("Để trống nếu không thay đổi...");
        fPass.setStyle("-fx-background-color: rgb(208 197 244 / 0.5); -fx-background-radius: 40px; -fx-padding: 12 16; -fx-font-family: 'Google Sans'; -fx-border-width: 0;");
        fPass.setPrefWidth(fieldWidth);
        fPass.setLayoutX(startX); fPass.setLayoutY(326);

        Button btnSave = getShadowBtn("Lưu thay đổi", "", "#3b82f6", "white", "rgba(59,130,246,0.4)");
        btnSave.setPrefWidth(585);
        btnSave.setPrefHeight(45);
        btnSave.setLayoutX(0);
        btnSave.setLayoutY(396);
        btnSave.setOnAction(e -> triggerToast("Đã cập nhật thông tin"));

        pane.getChildren().addAll(avt, btnChangeAvt, lId, fId, lName, fName, lEmail, fEmail, lPhone, fPhone, lPass, fPass, btnSave);

        formBox.getChildren().addAll(titleBox, pane);
        centerWrapper.getChildren().add(formBox);

        HBox layoutCentering = new HBox(centerWrapper);
        layoutCentering.setAlignment(Pos.CENTER); // Căn chính giữa khung ngang

        mainContent.getChildren().add(layoutCentering);

        ScrollPane mainScroll = new ScrollPane(mainContent);
        mainScroll.setPadding(new Insets(0));
        mainScroll.setFitToWidth(true);
        mainScroll.setFitToHeight(true);
        format.formatScrollbar(mainScroll, mainContent, 12);

        return mainScroll;
    }

    private VBox createDeleteAccountModal() {
        VBox box = new VBox(20);
        box.setPrefWidth(360); box.setMaxSize(360, Region.USE_PREF_SIZE);
        box.setPadding(new Insets(32));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 40px; -fx-font-family: 'Google Sans';");
        box.setEffect(new DropShadow(45, 0, 15, Color.web("#311b92", 0.3)));

        Label title = format.formatLabel("Xóa tài khoản?", FontWeight.BLACK, 20, "#ef4444");
        Label desc = format.formatLabel("Mọi dữ liệu sẽ bị xóa vĩnh viễn và không thể khôi phục. Bạn có chắc chắn muốn thực hiện hành động này?", FontWeight.MEDIUM, 13, "#64748b");
        desc.setWrapText(true);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(16, 0, 0, 0));

        Button btnCancel = getModalActionBtn("Hủy", "rgba(178, 162, 228, 0.2)", "#64748b", "rgba(0,0,0,0.1)");
        btnCancel.setOnAction(e -> closeOverlayModal());

        Button btnConfirm = getModalActionBtn("Xóa vĩnh viễn", "#ef4444", "white", "rgba(239,68,68,0.4)");
        btnConfirm.setOnAction(e -> {
            closeOverlayModal();
            triggerToast("Đang xử lý...");
            PauseTransition pt = new PauseTransition(Duration.seconds(2));
            pt.setOnFinished(ev -> System.exit(0));
            pt.play();
        });

        actions.getChildren().addAll(btnCancel, btnConfirm);
        box.getChildren().addAll(title, desc, actions);

        return box;
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

    private Button getShadowBtn(String text, String icon, String bgColor, String textColor, String shadowColor) {
        Button btn = new Button();
        btn.setFocusTraversable(false);
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
        btn.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 40px; -fx-padding: 8 20 8 20; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, " + shadowColor + ", 10, 0, 0, 4);");
        btn.setOnMouseEntered(e -> { btn.setScaleX(1.03); btn.setScaleY(1.03); });
        btn.setOnMouseExited(e -> { btn.setScaleX(1.0); btn.setScaleY(1.0); });
        return btn;
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