/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.Controller;
import Model.Converter;
import Model.Settings;
import Model.Wormhole;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Jacob
 */
public class BoardInfo {

    private ArrayList<Group> wormholes_info = new ArrayList<>();
    private ArrayList<Label> wormholes_lbl = new ArrayList<>();
    private HBox wormholes_box;

    private HBox timeAndSpeed_info;
    private Label timefornextspeed_lbl;
    private Label currentscore_lbl;
    private Label currentspeed_lbl;
    private ImageView settingImage;
    private int currentSizeWormholes = 0;

    private Color wormholeColor;

    private HBox gameInfoBox;

    private Controller controller;

    public BoardInfo(Controller controller, Color wormholeColor) {
        this.controller = controller;
        initGameInfoBox();
        setWormholeColor(wormholeColor);
    }

    public void resultVersion() {
        wormholes_box.setVisible(false);
        timeAndSpeed_info.setVisible(false);
    }

    public void gameVersion() {
        wormholes_box.setVisible(true);
        timeAndSpeed_info.setVisible(true);
    }

    public HBox getGameInfoBox() {
        return gameInfoBox;
    }

    public void setWormholeColor(Color wormholeColor) {
        this.wormholeColor = wormholeColor;
    }

    public void showInfoWormholes(ArrayList<Wormhole> tmpWormholes) {
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

    public void clearWormholeInfo() {
        wormholes_box.getChildren().clear();
        currentSizeWormholes = 0;
        wormholes_info.clear();
        wormholes_lbl.clear();
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

    public void removeWormhole(int index) {
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

    public void updateNext(int next) {
        String nextInfo = "";
        if (next == -1) {
            nextInfo = "!";
        } else {
            nextInfo = "" + next;
        }
        timefornextspeed_lbl.setText(nextInfo);
    }

    public void updateScore(int score) {
        currentscore_lbl.setText(String.valueOf(score));
    }

    public void updateLevel(int level) {
        currentspeed_lbl.setText(String.valueOf(level));
    }

    private void initGameInfoBox() {
        gameInfoBox = new HBox();
        gameInfoBox.setSpacing(10);
        gameInfoBox.setStyle("-fx-background-color: #e4fbfd;");
        gameInfoBox.setPrefHeight(Settings.GAME_INFO_HEIGHT);

        //regions that give spacing between the elements in gameinfobox
        Region leftInfo = new Region();
        HBox.setHgrow(leftInfo, Priority.ALWAYS);
        Region rightInfo = new Region();
        HBox.setHgrow(rightInfo, Priority.ALWAYS);

        initSettingBtn();
        initWormholeInfo();
        initGameInfo();
        gameInfoBox.getChildren().addAll(wormholes_box, leftInfo, settingImage, rightInfo, timeAndSpeed_info);
        gameInfoBox.getStylesheets().add("css/bottomview.css");
    }

    private void initSettingBtn() {
        settingImage = new ImageView();
        Image image = new Image("img/settings_img.png");
        settingImage.setImage(image);
        settingImage.setId("setting_btn");

        settingImage.setFitWidth(Converter.getHeightSettingButton());
        settingImage.setPreserveRatio(true);
        settingImage.setSmooth(true);
        settingImage.setCache(true);
        settingImage.setOnMouseClicked(new BoardInfo.SelectSettings());
    }

    private void initGameInfo() {
        timeAndSpeed_info = new HBox();
        timeAndSpeed_info.setPadding(new Insets(15, 12, 15, 12));
        timeAndSpeed_info.setSpacing(10);
        timeAndSpeed_info.setStyle("-fx-background-color: #e4fbfd;");

        Label nextLbl = new Label("NEXT: ");
        timefornextspeed_lbl = new Label("");
        timefornextspeed_lbl.setId("info_lbl");
        HBox nextBox = new HBox(nextLbl, timefornextspeed_lbl);

        Label scoreLbl = new Label("SCORE: ");
        currentscore_lbl = new Label("");
        currentscore_lbl.setId("info_lbl");
        HBox scoreBox = new HBox(scoreLbl, currentscore_lbl);

        Label speedLbl = new Label("LEVEL: ");
        currentspeed_lbl = new Label("");
        currentspeed_lbl.setId("info_lbl");
        HBox speedBox = new HBox(speedLbl, currentspeed_lbl);
        timeAndSpeed_info.getChildren().addAll(scoreBox, nextBox, speedBox);
        timeAndSpeed_info.setPrefWidth(Converter.getWidthInfoBox());
        timeAndSpeed_info.setAlignment(Pos.BASELINE_RIGHT);
    }

    private void initWormholeInfo() {
        wormholes_box = new HBox();
        wormholes_box.setPadding(new Insets(5, 12, 0, 12));
        wormholes_box.setSpacing(10);
        wormholes_box.setStyle("-fx-background-color: #e4fbfd;");
        wormholes_box.setPrefWidth(Converter.getWidthInfoBox());
    }

    public void addWormholeInfo() {
        TilePane wormholePane = new TilePane(Orientation.VERTICAL);
        wormholePane.setPrefRows(2);
        Rectangle rect = new Rectangle(Settings.GAME_INFO_HEIGHT * 0.3, Settings.GAME_INFO_HEIGHT * 0.3, wormholeColor);
        Label wormhole_lbl = new Label("10");
        wormholePane.getChildren().addAll(rect, wormhole_lbl);
        Group wormhole = new Group(wormholePane);
        wormholes_info.add(wormhole);
        wormholes_lbl.add(wormhole_lbl);
        wormholes_box.getChildren().add(wormhole);
    }

    private class SelectSettings implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            controller.goToSettings();
        }

    }
}
