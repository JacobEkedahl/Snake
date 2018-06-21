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
import com.sun.javafx.scene.control.skin.CustomColorDialog;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javax.swing.JFrame;

/**
 *
 * @author Jacob
 */
public class BoardView extends Application {

    //standard settings for the game
    private static int SIZE_SNAKE = 20;
    private static int SPEED = 150;
    private static int WIDTH = 40;
    private static int HEIGHT = 40;
    private static int SCREEN_WIDTH = 600;
    private static int SCREEN_HEIGHT = 800;
    private static int GAME_INFO_HEIGHT = 50;
    private static double INCREASE_SPEED = 0.95;
    private static int BORDERSIZE = 3;
    private static int WORMHOLE_TIMETOLIVE = 30;
    private static int WORMHOLE_INTERVAL = 1;
    private static int MAX_WORMHOLES = 5;
    private static boolean RANDOM_WORMHOLES = true;
    private static boolean FIXED_WORMHOLES = false;

    //description of what to change and the id of the controlnodes
    private static final String controlLeft = "Left: ";
    private static final String controlRight = "Right: ";
    private static final String controlRestart = "Restart game: ";
    private static final String titleWormhole = "Wormhole";
    private static final String infoWormholeRandom = "Random wormholes: ";
    private static final String infoWormholeFixed = "Fixed wormholes: ";
    private static final String infoWormholeInterval = "Generate new wormhole every (seconds): ";
    private static final String infoWormholeLifespan = "Wormholes lifespan (seconds): ";
    private static final String infoWormholeLimit = "Max wormholes to exist: ";
    private static final String titleApple = "Apple";
    private static final String titleScreen = "Game";
    private static final String titleSnake = "Snake";

    //the nodes actnig as button to change the settings
    private static final String buttonLeft = "S";
    private static final String buttonRight = "D";
    private static final String buttonRestart = "SPACE";
    private static final String buttonRandom = "NO";
    private static final String buttonInterval = "10";
    private static final String buttonFixed = "YES";
    private static final String buttonLifespan = "10";
    private static final String buttonLimit = "1";
    private static final String buttonApply = "SAVE";

    //hashmap of references to the controlls in the settingsmenu (id is used to find the node)
    private HashMap<String, Node> controllNodes = new HashMap<>();

    private int heightBox, widthBox;

    //view controlers
    private Region leftInfo, rightInfo;
    private HBox gameInfoBox;
    private Rectangle frame;
    private Group startGroup;
    private GridPane startPane;
    private BorderPane mainPane;
    private GridPane boardPane;
    private Group boardGroup;
    private TilePane resultView;
    private Group resultGroup;
    private Label timeLbl;
    private Label scoreLbl;
    private Game game;
    private ArrayList<ImageView> board;
    private HashMap<String, Rectangle> rectMap = new HashMap<>();
    private ArrayList<Position> snakePos;
    private ArrayList<Position> wormholes = new ArrayList<>();
    private ArrayList<Group> wormholes_info = new ArrayList<>();
    private ArrayList<Label> wormholes_lbl = new ArrayList<>();
    private HBox wormholes_box;

    private HBox timeAndSpeed_info;
    private Label timefornextspeed_lbl;
    private Label currentscore_lbl;
    private Label currentspeed_lbl;
    private int currentspeed;

    private Converter converter;

    Controller controller;
    Stage primaryStage;

    //settings variables
    private int sizeSnake, width, height, speed, timetolive, interval, maxwormholes, screenWidth, screenHeight;
    private String leftKey, rightKey, restartKey;
    private double percentageIncrease;
    private boolean randomWormholes, fixedWormholes;
    private Color wormholeColor, appleColor, gameColor, snakeColor;

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
        appleColor = getColorFromMap(titleApple);
        gameColor = getColorFromMap(titleScreen);
        snakeColor = getColorFromMap(titleSnake);

        sizeSnake = SIZE_SNAKE; //controllNodes.get(this)
        width = WIDTH;
        height = HEIGHT;
        speed = SPEED;
        percentageIncrease = INCREASE_SPEED;
        timetolive = Integer.parseInt(getStringFromMap(infoWormholeLifespan));
        interval = Integer.parseInt(getStringFromMap(infoWormholeInterval));
        maxwormholes = Integer.parseInt(getStringFromMap(infoWormholeLimit));
        screenWidth = SCREEN_WIDTH;
        screenHeight = SCREEN_HEIGHT;
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
                System.out.println("wormholes: size: " + wormholes_info.size());
                try {
                    Group tmpGroup = wormholes_info.get(index);
                    wormholes_box.getChildren().remove(tmpGroup);
                    wormholes_info.remove(index);
                    wormholes_lbl.remove(index);
                    currentSizeWormholes--;
                } catch (IndexOutOfBoundsException ex) {
                    //continue
                    System.out.println("Tried to remove, but no");
                }
            }
        });
    }

    private int currentSizeWormholes = 0;
    private void showInfoWormholes(ArrayList<Wormhole> tmpWormholes) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int size = tmpWormholes.size();
                if (currentSizeWormholes < size) {
                    int moreToAdd = size - currentSizeWormholes;
                    for (int i = 0; i < moreToAdd; i++) {
                        addWormholeInfo();
                        System.out.println("Wormhole size: " + wormholes_info.size());
                    }
                    currentSizeWormholes = size;

                }

                for (int i = 0; i < tmpWormholes.size(); i++) {
                    showWormholeInfo(i, tmpWormholes.get(i).getTimeToLive());
                }
            }
        });
    }

    private void showWormholeInfo(int index, int timeToLive) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    wormholes_lbl.get(index).setText("" + timeToLive);
                } catch (Exception ex) {
                    System.out.println("index: " + index + " size: " + wormholes_info.size());
                    System.out.println("Could not show more info");
                }
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

    private void clearWormholeInfo() {
        wormholes_box.getChildren().clear();
        currentSizeWormholes = 0;
        wormholes_info.clear();
        wormholes_lbl.clear();
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
        infoBox.getChildren().add(new Label(titleSnake));

        VBox controllBox = new VBox();
        controllBox.setSpacing(10);
        controllBox.setStyle("-fx-background-color: #e4fbfd");
        controllBox.setPadding(new Insets(15, 12, 15, 12));
        initControllLabels(controllBox);
        controllBox.getStylesheets().add("controlls.css");

        startPane.add(infoBox, 1, 1); //col, row
        startPane.add(controllBox, 2, 1);
        startPane.getStylesheets().add("settings.css");

        Button btn = new Button(buttonApply);
        btn.setOnMouseClicked(new SettingSubmit());
        btn.setFocusTraversable(false);
        VBox vbox = new VBox(startPane, btn);
        vbox.setPadding(new Insets(15, 12, 15, 12));
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);

        startGroup = new Group(vbox);
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
        generatePicker(controllBox, snakeColor, titleSnake);
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

    private void initHBox() {
        gameInfoBox = new HBox();
        gameInfoBox.setSpacing(10);
        gameInfoBox.setStyle("-fx-background-color: #e4fbfd;");
        gameInfoBox.setPrefHeight(GAME_INFO_HEIGHT);
        leftInfo = new Region();
        HBox.setHgrow(leftInfo, Priority.ALWAYS);
        initWormholeInfo();
        initGameInfo();
        gameInfoBox.getChildren().addAll(wormholes_box, leftInfo, timeAndSpeed_info);
    }

    private void initGameInfo() {
        timeAndSpeed_info = new HBox();
        timeAndSpeed_info.setPadding(new Insets(15, 12, 15, 12));
        timeAndSpeed_info.setSpacing(10);
        timeAndSpeed_info.setStyle("-fx-background-color: #e4fbfd;");
        timefornextspeed_lbl = new Label("Hello");
        currentscore_lbl = new Label("there");
        currentspeed_lbl = new Label(":)");
        timeAndSpeed_info.getChildren().addAll(timefornextspeed_lbl, currentscore_lbl, currentspeed_lbl);
    }

    private void initWormholeInfo() {
        wormholes_box = new HBox();
        wormholes_box.setPadding(new Insets(0, 12, 0, 12));
        wormholes_box.setSpacing(10);
        wormholes_box.setStyle("-fx-background-color: #e4fbfd;");
    }

    private void addWormholeInfo() {
        TilePane wormholePane = new TilePane(Orientation.VERTICAL);
        wormholePane.setPrefRows(2);
        Rectangle rect = new Rectangle(GAME_INFO_HEIGHT * 0.3, GAME_INFO_HEIGHT * 0.3, wormholeColor);
        Label wormhole_lbl = new Label("10");
        wormholePane.getChildren().addAll(rect, wormhole_lbl);
        Group wormhole = new Group(wormholePane);
        wormholes_info.add(wormhole);
        wormholes_lbl.add(wormhole_lbl);
        wormholes_box.getChildren().add(wormhole);
    }

    private void initResult() {
        resultView = new TilePane(Orientation.VERTICAL);
        resultView.setPrefRows(3);
        Label gameOverLbl = new Label("GAME OVER");
        scoreLbl = new Label("test");
        timeLbl = new Label("test");
        resultView.getChildren().addAll(gameOverLbl, scoreLbl, timeLbl);
        resultGroup = new Group(resultView);
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

        frame = new Rectangle((widthBox * width) + (BORDERSIZE * 2), heightBox * height + (BORDERSIZE * 2), Color.BLACK);
        boardGroup.getChildren().addAll(frame, boardPane);
        boardPane.setLayoutX(BORDERSIZE);
        boardPane.setLayoutY(BORDERSIZE);
        mainPane.setCenter(boardGroup);
        mainPane.setBottom(gameInfoBox);
    }

    private void initView(Stage primaryStage) {
        heightBox = converter.getHeightBox();
        widthBox = converter.getWidthBox();
        mainPane = new BorderPane();
        initResult();
        initBoard();
        initStart();
        initHBox();
        mainPane.setCenter(startGroup); //startGroup

        Scene scene = new Scene(mainPane, screenWidth + (BORDERSIZE * 2), screenHeight + (BORDERSIZE * 2) + GAME_INFO_HEIGHT);
        scene.setOnKeyPressed(new GameInteraction());
        scene.setOnMouseClicked(new ClickInteraction());
        primaryStage.setTitle("Snake");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void updateLabel(Label label, String text) {
        label.setText(text);
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
            if (event.getCode() == KeyCode.valueOf(leftKey)) {
                System.out.println(KeyCode.S);
                game.goLeft();
            } else if (event.getCode() == KeyCode.valueOf(rightKey)) {
                game.goRight();
            } else if (event.getCode() == KeyCode.UP) {
                game.increaseSpeed(INCREASE_SPEED);
            } else if (event.getCode() == KeyCode.DOWN) {
                game.decreaseSpeed(INCREASE_SPEED);
            } else if (event.getCode() == KeyCode.valueOf(restartKey)) {
                game.init();
                clearWormholeInfo();
                initBoard();
                game.start();
            }
        }
    }
}
