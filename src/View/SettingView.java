/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.Controller;
import Model.Converter;
import static View.BoardView.GAME_INFO_HEIGHT;
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
public class SettingView {

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

    private Group startGroup;
    private GridPane startPane;

    private int sizeSnake, width, height, speed, timetolive, interval, maxwormholes, screenWidth, screenHeight;
    private String leftKey, rightKey, restartKey;
    private double percentageIncrease;
    private boolean randomWormholes, fixedWormholes;
    private Color wormholeColor, appleColor, gameColor, snakeColor;

    //hashmap of references to the controlls in the settingsmenu (id is used to find the node)
    private HashMap<String, Node> controllNodes = new HashMap<>();
    private Label selectedLabel = null;

    private Controller controller;
    
    public SettingView(Controller controller) {
        this.controller = controller;
        initStart();
    }

    public boolean isLabelSelected() {
        if (selectedLabel == null) {
            return false;
        }
        return true;
    }

    public boolean isFixedWormholes() {
        return fixedWormholes;
    }

    public void setLabel(String key) {
        selectedLabel.setText(key);
        unselectLabel(selectedLabel);
        selectedLabel = null;
    }
    
    public Color getWormholeColor() {
        return wormholeColor;
    }
    
    public String getLeftKey() {
        return leftKey;
    }
    
    public String getRightKey() {
        return rightKey;
    }
    
    public String getRestartKey() {
        return restartKey;
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
        controllBox.getStylesheets().add("controlls.css");

        startPane.add(infoBox, 1, 1); //col, row
        startPane.add(controllBox, 2, 1);

        Button btn = new Button(buttonApply);
        btn.setOnMouseClicked(new SettingView.SettingSubmit());
        btn.setFocusTraversable(false);
        btn.getStylesheets().add("settings.css");
        VBox vbox = new VBox(startPane, btn);
        vbox.setPadding(new Insets(BORDERSIZE, 0, 60, BORDERSIZE));
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

    public void setNewSettings() {
        leftKey = getStringFromMap(controlLeft);
        rightKey = getStringFromMap(controlRight);
        restartKey = getStringFromMap(controlRestart);

        wormholeColor = getColorFromMap(titleWormhole);
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
        randomWormholes = Converter.strToBool(getStringFromMap(infoWormholeRandom));
        fixedWormholes = Converter.strToBool(getStringFromMap(infoWormholeFixed));
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
            label.setOnMouseClicked(new SettingView.SettingInteraction());
            label.setPrefWidth(200);
            controllBox.getChildren().add(label);
        } else if (node instanceof Rectangle) {
            Rectangle rect = (Rectangle) node;
            rect.setOnMouseClicked(new SettingView.SettingInteraction());
            controllBox.getChildren().add(rect);
        } else if (node instanceof HBox) {
            HBox rect = (HBox) node;
            rect.setOnMouseClicked(new SettingView.SettingInteraction());
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
