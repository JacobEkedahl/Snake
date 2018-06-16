/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Game;
import Model.Position;
import Model.Result;
import Model.WormHole;
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
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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

    private static int SIZE_SNAKE = 15;
    private static int WIDTH = 40;
    private static int HEIGHT = 40;
    private static int SPEED = 150;
    private static int SCREEN_WIDTH = 600;
    private static int SCREEN_HEIGHT = 800;
    private static double INCREASE_SPEED = 0.95;
    private static int BORDERSIZE = 3;
    private static int INTERVAL_WORMHOLE = 15;

    private int heightBox, widthBox;
    private Rectangle frame;
    private GridPane infoPane;
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

    Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        initGame();
        controller = new Controller(game);
        initView(primaryStage);
        game.start();
    }

    public void initGame() {
        game = new Game(SIZE_SNAKE, WIDTH, HEIGHT, SPEED, INCREASE_SPEED, INTERVAL_WORMHOLE, this);
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

    private ArrayList<Position> wormholes = new ArrayList<>();

    private void showWormholes() {
        ArrayList<Position> tmpWormholes = game.getWormholes();
        if (!tmpWormholes.equals(wormholes)) {
            clearWormholes();
        }

        wormholes = tmpWormholes;
        for (Position p : wormholes) {
            Rectangle rect = rectMap.get(p.getId());
            rect.setFill(Color.BLUE);
        }
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

        System.out.println("Height: " + boardPane.getHeight());

        frame = new Rectangle((widthBox * WIDTH) + (BORDERSIZE * 2), heightBox * HEIGHT + (BORDERSIZE * 2), Color.BLACK);
        boardGroup.getChildren().addAll(frame, boardPane);
        boardPane.setLayoutX(BORDERSIZE);
        boardPane.setLayoutY(BORDERSIZE);
        mainPane.setCenter(boardGroup);
    }

    private void initView(Stage primaryStage) {
        heightBox = SCREEN_HEIGHT / HEIGHT;
        widthBox = SCREEN_WIDTH / WIDTH;
        System.out.println("width box: " + widthBox + ", width: " + WIDTH);
        System.out.println("height box: " + heightBox + ", height: " + HEIGHT);
        mainPane = new BorderPane();
        initResult();
        initBoard();

        Scene scene = new Scene(mainPane, SCREEN_WIDTH + (BORDERSIZE * 2), SCREEN_HEIGHT + (BORDERSIZE * 2));
        scene.setOnKeyPressed(new GameInteraction());
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

    private class Controller {

        private Game game;

        Controller(Game sharedGame) {
            game = sharedGame;
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
                game.reset();
                initBoard();
                game.start();
            }
        }
    }
}
