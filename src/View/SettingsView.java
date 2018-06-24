/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.Controller;
import Model.Converter;
import Model.Settings;
import java.util.HashMap;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author Jacob
 */
public class SettingsView {

    //standard settings for the settings
    private static int SCREEN_WIDTH_SETTINGS = 540;
    private static int SCREEN_HEIGHT_SETTINGS = 600;

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

    private static final String buttonApply = "APPLY";
    private Group startGroup;
    private GridPane startPane;

    //hashmap of references to the controlls in the settingsmenu (id is used to find the node)
    private HashMap<String, Node> controllNodes = new HashMap<>();
    private Label selectedLabel = null;

    private Controller controller;
    private Settings settings;
    
    public SettingsView(Controller controller, Settings settings) {
        this.controller = controller;
        this.settings = settings;
        initStart();
    }
    
    public Group getGroup() {
        return startGroup;
    }
    
    public boolean isLabelSelected() {
        if (selectedLabel == null) {
            return false;
        }
        return true;
    }

    public boolean isFixedWormholes() {
        return settings.isFixedWormholes();
    }

    public void setLabel(String key) {
        selectedLabel.setText(key);
        unselectLabel(selectedLabel);
        selectedLabel = null;
    }
    
    public Color getWormholeColor() {
        return settings.getWormholeColor();
    }
    
    public void chosedNode(Node node) {
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
        controllBox.getStylesheets().add("css/controlls.css");

        startPane.add(infoBox, 1, 1); //col, row
        startPane.add(controllBox, 2, 1);

        Button btn = new Button(buttonApply);
        btn.setOnMouseClicked(new SettingsView.SettingSubmit());
        btn.setFocusTraversable(false);
        btn.getStylesheets().add("css/settings.css");
        VBox vbox = new VBox(startPane, btn);
        vbox.setPadding(new Insets(Settings.BORDERSIZE, 0, 60, Settings.BORDERSIZE));
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        startGroup = new Group(vbox);
    }

    private Color getColorFromMap(String id) {
        return (Color) ((Rectangle) controllNodes.get(id)).getFill();
    }

    private String getStringFromMap(String id) {
        return ((Label) controllNodes.get(id)).getText().toUpperCase();
    }

    public void setNewSettings() {
        settings.setLeftKey(getStringFromMap(controlLeft));
        settings.setRightKey(getStringFromMap(controlRight));
        settings.setRestartKey(getStringFromMap(controlRestart));

        settings.setWormholeColor(getColorFromMap(titleWormhole));
        settings.setAppleColor(getColorFromMap(titleApple));
        settings.setGameColor(getColorFromMap(titleScreen));
        settings.setSnakeColor(getColorFromMap(titleSnake));
        settings.setSizeSnake(Integer.parseInt(getStringFromMap(infoSnakesize)));

        settings.setWidth(Integer.parseInt(getStringFromMap(infoGameWidth)));
        settings.setHeight(Integer.parseInt(getStringFromMap(infoGameHeight)));
        settings.setScreenWidth(Integer.parseInt(getStringFromMap(infoScreenWidth)));
        settings.setScreenHeight(Integer.parseInt(getStringFromMap(infoScreenHeight)));
        settings.setPercentageIncrease(Settings.INCREASE_SPEED);
        settings.setSpeed(Settings.SPEED);

        settings.setTimetolive(Integer.parseInt(getStringFromMap(infoWormholeLifespan)));
        settings.setInterval(Integer.parseInt(getStringFromMap(infoWormholeInterval)));
        settings.setMaxwormholes(Integer.parseInt(getStringFromMap(infoWormholeLimit)));
        settings.setRandomWormholes(Converter.strToBool(getStringFromMap(infoWormholeRandom)));
        settings.setFixedWormholes(Converter.strToBool(getStringFromMap(infoWormholeFixed)));
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

            Rectangle rect = new Rectangle(width + (Settings.BORDERSIZE * 2), height + (Settings.BORDERSIZE * 2), Color.BLACK);
            startGroup.getChildren().add(rect);
            vbox.toFront();
        }
    }

    private void initControllLabels(VBox controllBox) {
        helpInitControlls(new Label(settings.getLeftKey()), controllBox, controlLeft);
        helpInitControlls(new Label(settings.getRightKey()), controllBox, controlRight);
        helpInitControlls(new Label(settings.getRestartKey()), controllBox, controlRestart);
        generatePicker(controllBox, settings.getWormholeColor(), titleWormhole);
        helpInitControlls(new Label(Converter.boolToString(settings.isRandomWormholes())),
                controllBox, infoWormholeRandom);
        helpInitControlls(new Label(String.valueOf(settings.getInterval())),
                controllBox, infoWormholeInterval);
        helpInitControlls(new Label(Converter.boolToString(settings.isFixedWormholes())),
                controllBox, infoWormholeFixed);
        helpInitControlls(new Label(String.valueOf(settings.getTimetolive())),
                controllBox, infoWormholeLifespan);
        helpInitControlls(new Label(String.valueOf(settings.getMaxwormholes())), controllBox, infoWormholeLimit);
        generatePicker(controllBox, settings.getAppleColor(), titleApple);
        generatePicker(controllBox, settings.getGameColor(), titleScreen);
        helpInitControlls(new Label(String.valueOf(settings.getWidth())),
                controllBox, infoGameWidth);
        helpInitControlls(new Label(String.valueOf(settings.getHeight())), controllBox, infoGameHeight);
        helpInitControlls(new Label(String.valueOf(settings.getScreenWidth())), controllBox, infoScreenWidth);
        helpInitControlls(new Label(String.valueOf(settings.getScreenHeight())), controllBox, infoScreenHeight);
        generatePicker(controllBox, settings.getSnakeColor(), titleSnake);
        helpInitControlls(new Label(String.valueOf(settings.getSizeSnake())), controllBox, infoSnakesize);
    }

    private void generatePicker(VBox controllBox, Color color, String id) {
        Rectangle rect = new Rectangle(Settings.GAME_INFO_HEIGHT * 0.4, Settings.GAME_INFO_HEIGHT * 0.4, color);
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
            label.setOnMouseClicked(new SettingsView.SettingInteraction());
            label.setPrefWidth(200);
            controllBox.getChildren().add(label);
        } else if (node instanceof Rectangle) {
            Rectangle rect = (Rectangle) node;
            rect.setOnMouseClicked(new SettingsView.SettingInteraction());
            controllBox.getChildren().add(rect);
        } else if (node instanceof HBox) {
            HBox rect = (HBox) node;
            rect.setOnMouseClicked(new SettingInteraction());
            controllBox.getChildren().add(rect);
        }
        node.setId(id);
        controllNodes.put(id, node);
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

}
