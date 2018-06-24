/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Converter;
import Model.Game;
import Model.Position;
import Model.Result;
import Model.Wormhole;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author Jacob
 */
public class BoardView extends Application {

    //standard settings for the settings
    private static int SCREEN_WIDTH_SETTINGS = 540;
    private static int SCREEN_HEIGHT_SETTINGS = 600;

    //standard settings for the game
    private static int SIZE_SNAKE = 10;
    private static int SPEED = 150;
    private static int WIDTH = 40;
    private static int HEIGHT = 40;
    private static int SCREEN_WIDTH = 600;
    private static int SCREEN_HEIGHT = 800;
    public static int GAME_INFO_HEIGHT = 50;
    private static double INCREASE_SPEED = 0.95;
    private static int BORDERSIZE = 3;
    private static int WORMHOLE_TIMETOLIVE = 30;
    private static int WORMHOLE_INTERVAL = 1;
    private static int MAX_WORMHOLES = 5;
    private static boolean RANDOM_WORMHOLES = true;
    private static boolean FIXED_WORMHOLES = false;

    //description of what to change and the id of the controlnodes
    private static final String controlLeft = "Left";
    private static final String controlRight = "Right";
    private static final String controlRestart = "Restart game";
    private static final String titleWormhole = "Wormhole";
    private static final String infoWormholeRandom = "Random wormholes: ";
    private static final String infoWormholeFixed = "Fixed wormholes: ";
    private static final String infoWormholeInterval = "Generate new wormhole every (seconds): ";
    private static final String infoWormholeLifespan = "Wormholes lifespan (seconds): ";
    private static final String infoWormholeLimit = "Max wormholes to exist: ";
    private static final String titleApple = "Apple";
    private static final String titleScreen = "Game";
    private static final String infoGameWidth = "Game width squares (Integer)";
    private static final String infoGameHeight = "Game height squares (Integer)";
    private static final String infoScreenWidth = "Screen width (Integer)";
    private static final String infoScreenHeight = "Screen height (Integer)";
    private static final String titleSnake = "Snake";
    private static final String infoSnakesize = "Snake size (Integer)";

    //the nodes actnig as button to change the settings
    private static final String buttonLeft = "S";
    private static final String buttonRight = "D";
    private static final String buttonRestart = "SPACE";
    private static final String buttonRandom = "NO";
    private static final String buttonInterval = "10";
    private static final String buttonFixed = "YES";
    private static final String buttonLifespan = "10";
    private static final String buttonLimit = "1";
    private static final String buttonGameWidth = "40";
    private static final String buttonGameHeight = "40";
    private static final String buttonScreenWidth = "600";
    private static final String buttonScreenHeight = "800";
    private static final String buttonSnakeSize = "10";
    private static final String buttonApply = "APPLY";

    //hashmap of references to the controlls in the settingsmenu (id is used to find the node)
    private HashMap<String, Node> controllNodes = new HashMap<>();
    private Label selectedLabel = null;

    private int heightBox, widthBox;

    //view controlers
    private Rectangle settingsFrame;
    private Region leftInfo, rightInfo;
    private HBox gameInfoBox;
    private Rectangle frame;
    private Group startGroup;
    private GridPane startPane;
    private BorderPane mainPane;
    private GridPane boardPane;
    private Group boardGroup;
    private TilePane resultView;
    private BorderPane resultGroup;
    private Label timeLbl;
    private Label scoreLbl;
    private Game game;
    private ArrayList<ImageView> board;
    private HashMap<String, Rectangle> rectMap = new HashMap<>();
    private ArrayList<Position> snakePos;
    private ArrayList<Position> wormholes = new ArrayList<>();

    private Converter converter;

    Controller controller;
    Stage primaryStage;

    //settings variables
    private int sizeSnake, width, height, speed, timetolive, interval, maxwormholes, screenWidth, screenHeight;
    private String leftKey, rightKey, restartKey;
    private double percentageIncrease;
    private boolean randomWormholes, fixedWormholes;
    private Color wormholeColor, appleColor, gameColor, snakeColor;
    
    private BoardInfo boardInfo;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.getIcons().add(new Image("snakeIcon.png"));
        initStandardSettings();
        setupGame();
        controller = new Controller(game);
        initView(primaryStage);
    }

    private void initStandardSettings() {
        leftKey = buttonLeft;
        rightKey = buttonRight;
        restartKey = buttonRestart;

        wormholeColor = Color.BLUE;
        appleColor = Color.GREEN;
        gameColor = Color.WHITE;
        snakeColor = Color.BLACK;

        sizeSnake = SIZE_SNAKE;
        width = WIDTH;
        height = HEIGHT;
        speed = SPEED;
        percentageIncrease = INCREASE_SPEED;
        timetolive = WORMHOLE_TIMETOLIVE;
        interval = WORMHOLE_INTERVAL;
        maxwormholes = MAX_WORMHOLES;
        screenWidth = SCREEN_WIDTH;
        screenHeight = SCREEN_HEIGHT;
        randomWormholes = RANDOM_WORMHOLES;
        fixedWormholes = FIXED_WORMHOLES;
    }

    private void setNewSettings() {
        leftKey = getStringFromMap(controlLeft);
        rightKey = getStringFromMap(controlRight);
        restartKey = getStringFromMap(controlRestart);

        wormholeColor = getColorFromMap(titleWormhole);
        boardInfo.setWormholeColor(wormholeColor);
        appleColor = getColorFromMap(titleApple);
        gameColor = getColorFromMap(titleScreen);
        snakeColor = getColorFromMap(titleSnake);
        sizeSnake = Integer.parseInt(getStringFromMap(infoSnakesize));

        width = Integer.parseInt(getStringFromMap(infoGameWidth));
        height = Integer.parseInt(getStringFromMap(infoGameHeight));
        screenWidth = Integer.parseInt(getStringFromMap(infoScreenWidth));
        screenHeight = Integer.parseInt(getStringFromMap(infoScreenHeight));
        percentageIncrease = INCREASE_SPEED;
        speed = SPEED;

        timetolive = Integer.parseInt(getStringFromMap(infoWormholeLifespan));
        interval = Integer.parseInt(getStringFromMap(infoWormholeInterval));
        maxwormholes = Integer.parseInt(getStringFromMap(infoWormholeLimit));
        randomWormholes = converter.strToBool(getStringFromMap(infoWormholeRandom));
        fixedWormholes = converter.strToBool(getStringFromMap(infoWormholeFixed));
    }

    private Color getColorFromMap(String id) {
        return (Color) ((Rectangle) controllNodes.get(id)).getFill();
    }

    private String getStringFromMap(String id) {
        return ((Label) controllNodes.get(id)).getText().toUpperCase();
    }

    private boolean first = true;

    public void setupGame() throws Exception {
        converter = new Converter(screenWidth, screenHeight, width, height, GAME_INFO_HEIGHT);

        if (sizeSnake > width / 2) {
            throw new Exception("Snake size too large: " + sizeSnake);
        }
        if (maxwormholes > converter.getMaxWormholes()) {
            throw new Exception("Max wormholes size is too large: " + maxwormholes + ", possible max: " + converter.getMaxWormholes());
        }

        if (first) {
            game = new Game(sizeSnake, width, height, speed, percentageIncrease,
                    timetolive, interval, randomWormholes, maxwormholes, this);
            first = false;
        } else {
            game.updateSettings(sizeSnake, width, height, speed, percentageIncrease,
                    timetolive, interval, randomWormholes, maxwormholes);
        }
        game.init();
        snakePos = game.getSnakePosition();
    }

    public void showGameOver() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //show text game over, time, and size of snake
                Result res = game.getResult();
                timeLbl.setText("Time: " + res.getTime() + " seconds");
                scoreLbl.setText("Score: " + res.getSizeSnake());
                mainPane.setCenter(resultGroup);
                mainPane.setBottom(null);
            }
        });
    }

    public void updateUI() {
        clearSnake();
        showSnake();
        showApples();
        clearWormholes();
        showWormholes();
    }

    private void showSnake() {
        ArrayList<Position> position = game.getSnakePosition();
        for (Position p : position) {
            Rectangle rect = rectMap.get(p.getId());
            rect.setFill(snakeColor);
        }
        snakePos = position;
    }

    private void clearWormholes() {
        for (Position p : wormholes) {
            Rectangle rect = rectMap.get(p.getId());
            rect.setFill(gameColor);
        }
    }

    private void showWormholes() {
        ArrayList<Wormhole> tmpWormholes = game.getWormholes();

        wormholes = converter.getWormholes(tmpWormholes);
        for (Position p : wormholes) {
            Rectangle rect = rectMap.get(p.getId());
            rect.setFill(wormholeColor);
        }

        showInfoWormholes(tmpWormholes);
    }

    public void removeWormhole(int index) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                boardInfo.removeWormhole(index);
            }
        });
    }

    private void showInfoWormholes(ArrayList<Wormhole> tmpWormholes) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                boardInfo.showInfoWormholes(tmpWormholes);
            }
        });
    }

    private void showApples() {
        ArrayList<Position> apples = game.getApples();
        //   System.out.println("Apple size: " + apples.size());
        for (Position p : apples) {
            Rectangle rect = rectMap.get(p.getId());
            rect.setFill(appleColor);
        }
    }

    private void clearSnake() {
        for (Position p : snakePos) {
            Rectangle rect = rectMap.get(p.getId());
            rect.setFill(gameColor);
        }
    }

    private void initStart() {
        startPane = new GridPane();
        VBox infoBox = new VBox();
        infoBox.setPadding(new Insets(15, 12, 15, 12));
        infoBox.setSpacing(10);
        infoBox.setStyle("-fx-background-color: #e4fbfd;");

        infoBox.getChildren().add(new Label(controlLeft));
        infoBox.getChildren().add(new Label(controlRight));
        infoBox.getChildren().add(new Label(controlRestart));
        infoBox.getChildren().add(new Label(titleWormhole));
        infoBox.getChildren().add(new Label(infoWormholeRandom));
        infoBox.getChildren().add(new Label(infoWormholeInterval));
        infoBox.getChildren().add(new Label(infoWormholeFixed));
        infoBox.getChildren().add(new Label(infoWormholeLifespan));
        infoBox.getChildren().add(new Label(infoWormholeLimit));
        infoBox.getChildren().add(new Label(titleApple));
        infoBox.getChildren().add(new Label(titleScreen));
        infoBox.getChildren().add(new Label(infoGameWidth));
        infoBox.getChildren().add(new Label(infoGameHeight));
        infoBox.getChildren().add(new Label(infoScreenWidth));
        infoBox.getChildren().add(new Label(infoScreenHeight));
        infoBox.getChildren().add(new Label(titleSnake));
        infoBox.getChildren().add(new Label(infoSnakesize));

        VBox controllBox = new VBox();
        controllBox.setSpacing(10);
        controllBox.setStyle("-fx-background-color: #e4fbfd");
        controllBox.setPadding(new Insets(15, 12, 15, 12));
        initControllLabels(controllBox);
        controllBox.getStylesheets().add("controlls.css");

        startPane.add(infoBox, 1, 1); //col, row
        startPane.add(controllBox, 2, 1);

        Button btn = new Button(buttonApply);
        btn.setOnMouseClicked(new SettingSubmit());
        btn.setFocusTraversable(false);
        btn.getStylesheets().add("settings.css");
        VBox vbox = new VBox(startPane, btn);
        vbox.setPadding(new Insets(BORDERSIZE, 0, 60, BORDERSIZE));
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        startGroup = new Group(vbox);
    }

    private void addRectToGroup(Node node) {
        if (node instanceof Group) {
            Group group = (Group) node;
            System.out.println("");
            VBox vbox = (VBox) group.getChildren().get(0);
            double height = startPane.getHeight();
            double width = startPane.getWidth();
            System.out.println("width: " + width + "height: " + height);
            VBox parent = (VBox) startPane.getParent();

            Rectangle rect = new Rectangle(width + (BORDERSIZE * 2), height + (BORDERSIZE * 2), Color.BLACK);
            startGroup.getChildren().add(rect);
            vbox.toFront();
        }
    }

    private void initControllLabels(VBox controllBox) {
        helpInitControlls(new Label(buttonLeft), controllBox, controlLeft);
        helpInitControlls(new Label(buttonRight), controllBox, controlRight);
        helpInitControlls(new Label(buttonRestart), controllBox, controlRestart);
        generatePicker(controllBox, wormholeColor, titleWormhole);
        helpInitControlls(new Label(buttonRandom), controllBox, infoWormholeRandom);
        helpInitControlls(new Label(buttonInterval), controllBox, infoWormholeInterval);
        helpInitControlls(new Label(buttonFixed), controllBox, infoWormholeFixed);
        helpInitControlls(new Label(buttonLifespan), controllBox, infoWormholeLifespan);
        helpInitControlls(new Label(buttonLimit), controllBox, infoWormholeLimit);
        generatePicker(controllBox, appleColor, titleApple);
        generatePicker(controllBox, gameColor, titleScreen);
        helpInitControlls(new Label(buttonGameWidth), controllBox, infoGameWidth);
        helpInitControlls(new Label(buttonGameHeight), controllBox, infoGameHeight);
        helpInitControlls(new Label(buttonScreenWidth), controllBox, infoScreenWidth);
        helpInitControlls(new Label(buttonScreenHeight), controllBox, infoScreenHeight);
        generatePicker(controllBox, snakeColor, titleSnake);
        helpInitControlls(new Label(buttonSnakeSize), controllBox, infoSnakesize);
    }

    private void generatePicker(VBox controllBox, Color color, String id) {
        Rectangle rect = new Rectangle(GAME_INFO_HEIGHT * 0.4, GAME_INFO_HEIGHT * 0.4, color);
        rect.setId(id);
        ColorPicker picker = new ColorPicker();
        picker.setVisible(false);
        rect.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                picker.show();
            }
        });

        picker.setOnAction((ActionEvent t) -> {
            rect.setFill(picker.getValue());
        });

        controllBox.getChildren().add(new Group(rect, picker));
        controllNodes.put(id, rect);
    }

    private void helpInitControlls(Node node, VBox controllBox, String id) {
        if (node instanceof Label) {
            Label label = (Label) node;
            label.setOnMouseClicked(new SettingInteraction());
            label.setPrefWidth(200);
            controllBox.getChildren().add(label);
        } else if (node instanceof Rectangle) {
            Rectangle rect = (Rectangle) node;
            rect.setOnMouseClicked(new SettingInteraction());
            controllBox.getChildren().add(rect);
        } else if (node instanceof HBox) {
            HBox rect = (HBox) node;
            rect.setOnMouseClicked(new SettingInteraction());
            controllBox.getChildren().add(rect);
        }
        node.setId(id);
        controllNodes.put(id, node);
    }

    public void updateResult() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int next = game.getTimeToNext();
                int level = game.getLevel();
                boardInfo.updateNext(next);
                boardInfo.updateLevel(level);
            }
        });
    }

    public void updateScore() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int score = game.getResult().getSizeSnake();
                boardInfo.updateScore(score);
            }
        });
    }

    private void initResult() {
        resultView = new TilePane(Orientation.VERTICAL);
        resultView.setPrefRows(3);
        Label gameOverLbl = new Label("GAME OVER");
        scoreLbl = new Label("test");
        timeLbl = new Label("test");
        resultView.getChildren().addAll(gameOverLbl, scoreLbl, timeLbl);
        Group group = new Group(resultView);
        resultGroup = new BorderPane();
        resultGroup.setCenter(group);
        resultGroup.getStylesheets().add("result.css");
        BorderStroke stroke = new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                new BorderWidths(BORDERSIZE));
        Border border = new Border(stroke);
        resultGroup.setBorder(border);
    }

    private void initBoard() {
        boardGroup = new Group();
        boardPane = new GridPane();
        ArrayList<Position> boardPosition = game.getBoard();
        board = new ArrayList<>();

        for (Position p : boardPosition) {
            String id = p.getId();
            Rectangle rect = new Rectangle(widthBox, heightBox, gameColor);
            rect.setId(id);
            rectMap.put(id, rect);
            boardPane.add(rect, p.getX(), p.getY());
        }

        updateBoardSize();

        frame = new Rectangle((widthBox * width) + (BORDERSIZE * 2), heightBox * height + (BORDERSIZE * 2), Color.BLACK);
        boardGroup.getChildren().addAll(frame, boardPane);
        boardPane.setLayoutX(BORDERSIZE);
        boardPane.setLayoutY(BORDERSIZE);
    }

    public void showBoard() {
        mainPane.setCenter(boardGroup);
        mainPane.setBottom(boardInfo.getGameInfoBox());
    }

    private void updateBoardSize() {
        heightBox = converter.getHeightBox();
        widthBox = converter.getWidthBox();

        ArrayList<Position> boardPosition = game.getBoard();
        board = new ArrayList<>();

        for (Position p : boardPosition) {
            String id = p.getId();
            Rectangle rect = new Rectangle(widthBox, heightBox, gameColor);
            rect.setId(id);
            rectMap.put(id, rect);
            boardPane.add(rect, p.getX(), p.getY());
        }
    }

    private void initView(Stage primaryStage) {
        mainPane = new BorderPane();
        initResult();
        initBoard();
        initStart();
        boardInfo = new BoardInfo();
        updateScreenSize(SCREEN_WIDTH_SETTINGS, SCREEN_HEIGHT_SETTINGS, 0);
        mainPane.setCenter(startGroup);
        primaryStage.show();
        addRectSettings();
    }

    public void updateScreenSize(int width, int height, int bottomMenuHeight) {
        mainPane = new BorderPane();
        mainPane.setCenter(null); //startGroup
        Scene scene = new Scene(mainPane, width + (BORDERSIZE * 2), height + (BORDERSIZE * 2) + bottomMenuHeight);
        scene.setOnKeyPressed(new GameInteraction());
        scene.setOnMouseClicked(new ClickInteraction());
        primaryStage.sizeToScene();
        primaryStage.setTitle("Snake");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
    }

    public void addRectSettings() {
        addRectToGroup(startGroup);
    }

    public void updateLabel(Label label, String text) {
        label.setText(text);
    }

    public void selectLabel(Label label) {
        if (selectedLabel == null) {
            selectedLabel = label;
            selectedLabel.setStyle("-fx-background-color: #7c8181;");
        } else if (selectedLabel.equals(label)) {
            selectedLabel = null;
            unselectLabel(label);
        } else {
            selectedLabel = label;
            selectedLabel.setStyle("-fx-background-color: #7c8181;");
        }
    }

    public void unselectLabel(Label label) {
        label.setStyle("-fx-background-color: #444b4b;");
    }

    public void dialogMessage(Label label) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("");
        dialog.setHeaderText("");
        dialog.setContentText("");
        dialog.setHeaderText("");
        dialog.setGraphic(null);
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(
                new Image("snakeIcon.png"));
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String input = result.get();
            input = input.toUpperCase();
            if (input.equals(" ")) {
                input = "SPACE";
            }
            if (!input.equals("")) {
                label.setText(input);
            }
        }
    }

    private class GameInteraction implements EventHandler<KeyEvent> {

        @Override
        public void handle(KeyEvent event) {
            controller.userInteraction(event);
        }
    }

    private class ClickInteraction implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            controller.clickedEvent(event);
        }

    }

    private class SettingInteraction implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            controller.settingEvent(event);
        }
    }

    private class SettingSubmit implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            controller.saveChanges(event);
        }
    }

    private class Controller {

        private Game game;

        Controller(Game sharedGame) {
            game = sharedGame;
        }

        public void saveChanges(MouseEvent event) {
            setNewSettings();
            try {
                setupGame();
                updateBoardSize();
            } catch (Exception ex) {
                Logger.getLogger(BoardView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void settingEvent(MouseEvent event) {
            Node node = (Node) event.getSource();

            if (node instanceof Label) {
                Label label = (Label) node;
                String currentText = label.getText();
                if (currentText.equals("YES")) {
                    updateLabel(label, "NO");
                } else if (currentText.equals("NO")) {
                    updateLabel(label, "YES");
                } else if (label.getId().equals(controlLeft) || label.getId().equals(controlRight) || label.getId().equals(controlRestart)) {
                    selectLabel(label);
                } else {
                    dialogMessage(label);
                }
            } else if (node instanceof Rectangle) {
                Rectangle rect = (Rectangle) node;
                System.out.println("Color: " + rect.getFill().toString());
            }
        }

        public void clickedEvent(MouseEvent event) {

            if (fixedWormholes) {
                Position clickedPos = converter.convertToPos(event.getX(), event.getY());
                game.generateFixedWormhole(clickedPos);
            }
        }

        public void userInteraction(KeyEvent event) {
            if (selectedLabel != null) {
                selectedLabel.setText(event.getCode().getName().toUpperCase());
                unselectLabel(selectedLabel);
                selectedLabel = null;
            } else if (event.getCode() == KeyCode.valueOf(leftKey)) {
                System.out.println(KeyCode.S);
                game.goLeft();
            } else if (event.getCode() == KeyCode.valueOf(rightKey)) {
                game.goRight();
            } else if (event.getCode() == KeyCode.UP) {
                game.increaseSpeed(INCREASE_SPEED);
            } else if (event.getCode() == KeyCode.DOWN) {
                game.decreaseSpeed(INCREASE_SPEED);
            } else if (event.getCode() == KeyCode.valueOf(restartKey)) {
                updateScreenSize(screenWidth, screenHeight, GAME_INFO_HEIGHT);
                game.init();
                boardInfo.clearWormholeInfo();
                initBoard();
                showBoard();
                game.start();
            }
        }
    }
}
