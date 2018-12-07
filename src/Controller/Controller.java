/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Converter;
import Model.Game;
import Model.Position;
import Model.Settings;
import View.BoardInfo;
import View.BoardView;
import View.SettingsView;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Jacob
 */
public class Controller {

    private Game game;
    private BoardView boardView;
    private SettingsView settingView;
    private BoardInfo boardInfo;
    private Settings settings;

    private Converter converter;

    public Controller(Game sharedGame, Converter converter) {
        settings = Settings.getInstance();
        game = sharedGame;
        this.converter = converter;
    }
    
    public void goToSettings() {
        boardView.goToSettings();
    }

    public void addViews(BoardInfo boardInfo, SettingsView settingView, BoardView boardView) {
        this.boardInfo = boardInfo;
        this.settingView = settingView;
        this.boardView = boardView;
    }

    public void saveChanges(MouseEvent event) {
        settingView.setNewSettings();
        try {
            boardView.setupGame();
            boardView.updateBoardSize();
        } catch (Exception ex) {
            Logger.getLogger(BoardView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void settingEvent(MouseEvent event) {
        settingView.chosedNode((Node) event.getSource());
    }

    public void clickedEvent(MouseEvent event) {
        if (settingView.isFixedWormholes()) {
            Position clickedPos = converter.convertToPos(event.getX(), event.getY());
            game.generateFixedWormhole(clickedPos);
        }
    }
    
    public void userInteraction(KeyEvent event) {
        //if user has selected a label to change (left, right or restart controller)
        if (settingView.isLabelSelected()) {
            String keySelected = event.getCode().getName().toUpperCase();
            settingView.setLabel(keySelected);
        //ingame functions
        } else if (event.getCode() == KeyCode.valueOf(settings.getLeftKey())) {
            game.goLeft();
        } else if (event.getCode() == KeyCode.valueOf(settings.getRightKey())) {
            game.goRight();
        } else if (event.getCode() == KeyCode.valueOf(settings.getRestartKey())) {
            boardView.changeSizeToBoard();
            game.init();
            boardInfo.clearWormholeInfo();
            boardView.initBoard();
            boardView.showBoard();
            game.start();
        }
    }
}
