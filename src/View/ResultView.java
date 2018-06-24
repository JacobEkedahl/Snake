/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Result;
import Model.Settings;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author Jacob
 */
public class ResultView {

    private TilePane resultView;
    private BorderPane resultGroup;

    private Label timeLbl;
    private Label scoreLbl;
    private Label gameOverLbl;

    private Settings settings;

    public ResultView() {
        settings = Settings.getInstance();
        initResult();
    }

    public BorderPane getPane() {
        return resultGroup;
    }

    private void initResult() {
        resultView = new TilePane(Orientation.VERTICAL);
        resultView.setPrefRows(3);
        gameOverLbl = new Label("GAME OVER");
        scoreLbl = new Label("");
        timeLbl = new Label("");

        resultView.getChildren().addAll(gameOverLbl, scoreLbl, timeLbl);
        initBackground();
        setColors();
    }

    private void initBackground() {
        Group group = new Group(resultView);
        resultGroup = new BorderPane();
        resultGroup.setCenter(group);
        resultGroup.getStylesheets().add("css/result.css");
        BorderStroke stroke = new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                new BorderWidths(Settings.BORDERSIZE));
        Border border = new Border(stroke);
        resultGroup.setBorder(border);
    }

    private void setColors() {
        //labels
        gameOverLbl.setTextFill(settings.getSnakeColor());
        scoreLbl.setTextFill(settings.getSnakeColor());
        timeLbl.setTextFill(settings.getSnakeColor());

        BackgroundFill fill = new BackgroundFill(settings.getGameColor(), null, null);
        Background background = new Background(fill);
        resultGroup.setBackground(background);
    }

    public void gameOver(Result result) {
        timeLbl.setText("Time: " + result.getTime() + " seconds");
        scoreLbl.setText("Score: " + result.getSizeSnake());
        setColors();
    }
}
