/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Game;
import Model.Position;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author Jacob
 */
public class BoardView extends Application {

    private static int SIZE_SNAKE = 4;
    private static int WIDTH = 50;
    private static int HEIGHT = 50;
    private static int SPEED = 600;
    private static int MAGNIFIER = 5;
    private static int SCREEN_HEIGHT = 400;
    private static int SCREEN_WIDTH = 400;
    private static int INCREASE_SPEED = 200;

    private int heightBox, widthBox;
    private BorderPane mainPane;
    private GridPane boardPane;
    private Game game;
    private ArrayList<ImageView> board;
    private HashMap<String, Rectangle> rectMap = new HashMap<String, Rectangle>();
    private ArrayList<Position> snakePos;
    private int speedIncrease = INCREASE_SPEED;

    @Override
    public void start(Stage primaryStage) throws Exception {
        initGame();
        initView(primaryStage);
        game.start();
    }

    private void initGame() {
        game = new Game(SIZE_SNAKE, WIDTH, HEIGHT, SPEED, this);
        snakePos = game.getSnakePosition();
    }

    public void updateUI() {
        clearSnake();
        ArrayList<Position> position = game.getSnakePosition();
        for (Position p : position) {
            Rectangle rect = rectMap.get(p.getId());
            rect.setFill(Color.BLACK);
        }
        snakePos = position;

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

    private void initBoard() {
        boardPane = new GridPane();
        ArrayList<Position> boardPosition = game.getBoard();
        board = new ArrayList<>();
        //  Image img = new Image("http://www.avajava.com/images/avajavalogo.jpg");

        for (Position p : boardPosition) {
            String id = p.getId();
            Rectangle rect = new Rectangle(heightBox, widthBox, Color.WHITE);
            rect.setId(id);
            rectMap.put(id, rect);
            boardPane.add(rect, p.getX(), p.getY());
        }
    }

    private void initView(Stage primaryStage) {
        heightBox = SCREEN_HEIGHT / HEIGHT;
        widthBox = SCREEN_WIDTH / WIDTH;
        mainPane = new BorderPane();
        initBoard();
        mainPane.setCenter(boardPane);
        
        Scene scene = new Scene(mainPane, SCREEN_WIDTH, SCREEN_HEIGHT);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
             //   System.out.println("keycode: " + event.getCode().getName());
                if (event.getCode() == KeyCode.S) {
                    System.out.println("Pressed s");
                    game.goLeft();
                } else if (event.getCode() == KeyCode.D) {
                    game.goRight();
                }else if (event.getCode() == KeyCode.UP) {
                    speedIncrease = game.increaseSpeed(speedIncrease);
                }else if (event.getCode() == KeyCode.DOWN) {
                    speedIncrease = game.decreaseSpeed(speedIncrease);
                }
            }
        });
        primaryStage.setTitle("Snake");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private class Controller {

        private Game game;

        Controller(Game sharedGame) {
            game = sharedGame;
        }
    }
}
