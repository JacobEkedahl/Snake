/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.Controller;
import Model.Converter;
import Model.Game;
import Model.Position;
import Model.Result;
import Model.Settings;
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

    private BoardInfo boardInfo;
    private SettingView settingView;
    private Settings settings;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.getIcons().add(new Image("img/icon.png"));
        settings = new Settings();
        setupGame();
        controller = new Controller(game, converter);
        initView(primaryStage);
        addViewsToController();
    }

    private boolean first = true;

    public void setupGame() throws Exception {
        converter = new Converter(settings.getScreenWidth(), settings.getScreenHeight(),
                                settings.getWidth(), settings.getHeight(), 50);

        if (settings.getSizeSnake() > settings.getWidth() / 2) {
            throw new Exception("Snake size too large: " + settings.getSizeSnake());
        }
        if (settings.getMaxwormholes() > converter.getMaxWormholes()) {
            throw new Exception("Max wormholes size is too large: " + settings.getMaxwormholes() + ", possible max: " + converter.getMaxWormholes());
        }

        if (first) {
            game = new Game(settings, this);
            first = false;
        } else {
            game.updateSettings(settings);
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
            rect.setFill(settings.getSnakeColor());
        }
        snakePos = position;
    }

    private void clearWormholes() {
        for (Position p : wormholes) {
            Rectangle rect = rectMap.get(p.getId());
            rect.setFill(settings.getGameColor());
        }
    }

    private void showWormholes() {
        ArrayList<Wormhole> tmpWormholes = game.getWormholes();

        wormholes = converter.getWormholes(tmpWormholes);
        for (Position p : wormholes) {
            Rectangle rect = rectMap.get(p.getId());
            rect.setFill(settings.getWormholeColor());
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
            rect.setFill(settings.getAppleColor());
        }
    }

    private void clearSnake() {
        for (Position p : snakePos) {
            Rectangle rect = rectMap.get(p.getId());
            rect.setFill(settings.getGameColor());
        }
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
    
    public String getLeftKey() {
        return settings.getLeftKey();
    }
    
    public String getRightKey() {
        return settings.getRightKey();
    }
    
    public String getRestartKey() {
        return settings.getRestartKey();
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
        resultGroup.getStylesheets().add("css/result.css");
        BorderStroke stroke = new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                new BorderWidths(3));
        Border border = new Border(stroke);
        resultGroup.setBorder(border);
    }

    public void initBoard() {
        boardGroup = new Group();
        boardPane = new GridPane();
        ArrayList<Position> boardPosition = game.getBoard();
        board = new ArrayList<>();

        for (Position p : boardPosition) {
            String id = p.getId();
            Rectangle rect = new Rectangle(widthBox, heightBox, settings.getGameColor());
            rect.setId(id);
            rectMap.put(id, rect);
            boardPane.add(rect, p.getX(), p.getY());
        }

        updateBoardSize();

        frame = new Rectangle((widthBox * settings.getWidth()) + (Settings.BORDERSIZE  * 2), 
                heightBox * settings.getHeight() + (Settings.BORDERSIZE * 2), Color.BLACK);
        boardGroup.getChildren().addAll(frame, boardPane);
        boardPane.setLayoutX(3);
        boardPane.setLayoutY(3);
    }

    public void showBoard() {
        mainPane.setCenter(boardGroup);
        mainPane.setBottom(boardInfo.getGameInfoBox());
    }

    public void updateBoardSize() {
        heightBox = converter.getHeightBox();
        widthBox = converter.getWidthBox();

        ArrayList<Position> boardPosition = game.getBoard();
        board = new ArrayList<>();

        for (Position p : boardPosition) {
            String id = p.getId();
            Rectangle rect = new Rectangle(widthBox, heightBox, settings.getGameColor());
            rect.setId(id);
            rectMap.put(id, rect);
            boardPane.add(rect, p.getX(), p.getY());
        }
    }

    private void initView(Stage primaryStage) {
        settingView = new SettingView(controller, settings);
        boardInfo = new BoardInfo(settings.getWormholeColor());
        mainPane = new BorderPane();
        initResult();
        initBoard();
        changeSizeToSettings();
        mainPane.setCenter(settingView.getGroup());
        primaryStage.show();
        settingView.addRectSettings();
    }
    
    private void addViewsToController() {
        controller.addViews(boardInfo, settingView, this);
    }

    public void changeSizeToSettings() {
        updateScreenSize(SCREEN_WIDTH_SETTINGS, SCREEN_HEIGHT_SETTINGS, 0);
    }

    public void changeSizeToBoard() {
        updateScreenSize(settings.getScreenWidth(), settings.getScreenHeight(), 50);
    }

    private void updateScreenSize(int width, int height, int bottomMenuHeight) {
        mainPane = new BorderPane();
        mainPane.setCenter(null); //startGroup
        Scene scene = new Scene(mainPane, width + (3 * 2), height + (3 * 2) + bottomMenuHeight);
        scene.setOnKeyPressed(new GameInteraction());
        scene.setOnMouseClicked(new ClickInteraction());
        primaryStage.sizeToScene();
        primaryStage.setTitle("Snake");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
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
}
