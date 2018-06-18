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
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author Jacob
 */
public class BoardView extends Application {

    private static int SIZE_SNAKE = 10;
    private static int WIDTH = 20;
    private static int HEIGHT = 20;
    private static int SPEED = 150;
    private static int SCREEN_WIDTH = 600;
    private static int SCREEN_HEIGHT = 600;
    private static int GAME_INFO_HEIGHT = 50;
    private static double INCREASE_SPEED = 0.95;
    private static int BORDERSIZE = 3;
    private static int WORMHOLE_TIMETOLIVE = 10;
    private static int WORMHOLE_INTERVAL = 3;
    private static int MAX_WORMHOLES = 4;
    private static boolean RANDOM_WORMHOLES = true;
    private static boolean FIXED_WORMHOLES = false;

    private int heightBox, widthBox;

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

    private HashMap<String, Rectangle> rectMap = new HashMap<String, Rectangle>();
    private ArrayList<Position> snakePos;
    private ArrayList<Position> wormholes = new ArrayList<>();
    private ArrayList<Group> wormholes_info = new ArrayList<>();
    private ArrayList<Label> wormholes_lbl = new ArrayList<>();

    private Converter converter;

    Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        setupGame();
        controller = new Controller(game);
        initView(primaryStage);
    }

    public void setupGame() throws Exception {
        if (SIZE_SNAKE > WIDTH / 2) {
            throw new Exception("Snake size to large: " + SIZE_SNAKE);
        }
        game = new Game(SIZE_SNAKE, WIDTH, HEIGHT, SPEED, INCREASE_SPEED,
                WORMHOLE_TIMETOLIVE, WORMHOLE_INTERVAL, RANDOM_WORMHOLES, MAX_WORMHOLES, this);
        converter = new Converter(SCREEN_WIDTH, SCREEN_HEIGHT, WIDTH, HEIGHT);
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
            rect.setFill(Color.BLACK);
        }
        snakePos = position;
    }

    private void clearWormholes() {
        for (Position p : wormholes) {
            Rectangle rect = rectMap.get(p.getId());
            rect.setFill(Color.WHITE);
        }
    }

    private void showWormholes() {
        ArrayList<Wormhole> tmpWormholes = game.getWormholes();

        wormholes = converter.getWormholes(tmpWormholes);
        for (Position p : wormholes) {
            Rectangle rect = rectMap.get(p.getId());
            rect.setFill(Color.BLUE);
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
                    gameInfoBox.getChildren().remove(tmpGroup);
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
            rect.setFill(Color.GREEN);
        }
    }

    private void clearSnake() {
        for (Position p : snakePos) {
            Rectangle rect = rectMap.get(p.getId());
            rect.setFill(Color.WHITE);
        }
    }

    private void clearWormholeInfo() {
        gameInfoBox.getChildren().clear();
        currentSizeWormholes = 0;
        wormholes_info.clear();
        wormholes_lbl.clear();
    }

    private static final String controlLeft = "Left: S";
    private static final String controlRight = "Right: D";
    private static final String controlRestart = "Restart game: SPACEBAR";
    private static final String infoWormhole = "Wormholes";
    private static final String infoApple = "Apple";

    private void initStart() {
        startPane = new GridPane();
        ArrayList<Label> infoLabels = new ArrayList<>();
        infoLabels.add(new Label(controlLeft));
        infoLabels.add(new Label(controlRight));
        infoLabels.add(new Label(controlRestart));
        infoLabels.add(new Label(infoWormhole));
        infoLabels.add(new Label(infoApple));

        for (int row = 1; row <= infoLabels.size(); row++) {
            startPane.add(infoLabels.get(row - 1), 1, row);
        }
        startGroup = new Group(startPane);
    }

    private void initHBox() {
        gameInfoBox = new HBox();
        gameInfoBox.setPadding(new Insets(15, 12, 15, 12));
        gameInfoBox.setSpacing(10);
        gameInfoBox.setStyle("-fx-background-color: #e4fbfd;");
        gameInfoBox.setPrefHeight(100);
    }

    private void addWormholeInfo() {
        TilePane wormholePane = new TilePane(Orientation.VERTICAL);
        wormholePane.setPrefRows(2);
        Rectangle rect = new Rectangle(GAME_INFO_HEIGHT * 0.4, GAME_INFO_HEIGHT * 0.4, Color.BLUE);
        Label wormhole_lbl = new Label("10");
        wormholePane.getChildren().addAll(rect, wormhole_lbl);
        Group wormhole = new Group(wormholePane);
        wormholes_info.add(wormhole);
        wormholes_lbl.add(wormhole_lbl);
        gameInfoBox.getChildren().add(wormhole);
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
            Rectangle rect = new Rectangle(widthBox, heightBox, Color.WHITE);
            rect.setId(id);
            rectMap.put(id, rect);
            boardPane.add(rect, p.getX(), p.getY());
        }

        frame = new Rectangle((widthBox * WIDTH) + (BORDERSIZE * 2), heightBox * HEIGHT + (BORDERSIZE * 2), Color.BLACK);
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

        Scene scene = new Scene(mainPane, SCREEN_WIDTH + (BORDERSIZE * 2), SCREEN_HEIGHT + (BORDERSIZE * 2) + GAME_INFO_HEIGHT);
        scene.setOnKeyPressed(new GameInteraction());
        scene.setOnMouseClicked(new ClickInteraction());
        primaryStage.setTitle("Snake");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
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

    private class Controller {

        private Game game;

        Controller(Game sharedGame) {
            game = sharedGame;
        }

        public void clickedEvent(MouseEvent event) {

            if (FIXED_WORMHOLES) {
                Position clickedPos = converter.convertToPos(event.getX(), event.getY());
                game.generateFixedWormhole(clickedPos);
            }
        }

        public void userInteraction(KeyEvent event) {
            if (event.getCode() == KeyCode.S) {
                game.goLeft();
            } else if (event.getCode() == KeyCode.D) {
                game.goRight();
            } else if (event.getCode() == KeyCode.UP) {
                game.increaseSpeed(INCREASE_SPEED);
            } else if (event.getCode() == KeyCode.DOWN) {
                game.decreaseSpeed(INCREASE_SPEED);
            } else if (event.getCode() == KeyCode.SPACE) {
                game.init();
                clearWormholeInfo();
                initBoard();
                game.start();
            }
        }
    }
}
