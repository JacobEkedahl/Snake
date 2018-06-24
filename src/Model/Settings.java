/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.scene.paint.Color;

/**
 *
 * @author Jacob
 */
public class Settings {

    //standard settings for the game
    private final static int SIZE_SNAKE = 10;
    public final static int SPEED = 150;
    private final static int WIDTH = 40;
    private final static int HEIGHT = 40;
    private final static int SCREEN_WIDTH = 600;
    private final static int SCREEN_HEIGHT = 800;
    public final static int GAME_INFO_HEIGHT = 50;
    public final static double INCREASE_SPEED = 0.95;
    public final static int BORDERSIZE = 3;
    private final static int WORMHOLE_TIMETOLIVE = 30;
    private final static int WORMHOLE_INTERVAL = 1;
    private final static int MAX_WORMHOLES = 5;
    private final static boolean RANDOM_WORMHOLES = true;
    private final static boolean FIXED_WORMHOLES = false;
    
    //colors of elements in game
    private final static Color WORMHOLE_COLOR = Color.BLUEVIOLET;
    private final static Color APPLE_COLOR = Color.GREEN;
    private final static Color GAME_COLOR = Color.BLACK;
    private final static Color SNAKE_COLOR = Color.AQUA;

    //the nodes actnig as button to change the settings
    private static final String buttonLeft = "S";
    private static final String buttonRight = "D";
    private static final String buttonRestart = "SPACE";

    private int sizeSnake, width, height, speed, timetolive, interval, maxwormholes, screenWidth, screenHeight;
    private String leftKey, rightKey, restartKey;
    private double percentageIncrease;
    private boolean randomWormholes, fixedWormholes;
    private Color wormholeColor, appleColor, gameColor, snakeColor;

    public Settings() {
        initStandardSettings();
    }

    public Settings(int sizeSnake, int width, int height, int speed, int timetolive, int interval, int maxwormholes, int screenWidth, int screenHeight, String leftKey, String rightKey, String restartKey, double percentageIncrease, boolean randomWormholes, boolean fixedWormholes, Color wormholeColor, Color appleColor, Color gameColor, Color snakeColor) {
        this.sizeSnake = sizeSnake;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.timetolive = timetolive;
        this.interval = interval;
        this.maxwormholes = maxwormholes;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.restartKey = restartKey;
        this.percentageIncrease = percentageIncrease;
        this.randomWormholes = randomWormholes;
        this.fixedWormholes = fixedWormholes;
        this.wormholeColor = wormholeColor;
        this.appleColor = appleColor;
        this.gameColor = gameColor;
        this.snakeColor = snakeColor;
    }

    public static String getButtonLeft() {
        return buttonLeft;
    }

    public static String getButtonRight() {
        return buttonRight;
    }

    public static String getButtonRestart() {
        return buttonRestart;
    }

    public int getSizeSnake() {
        return sizeSnake;
    }

    public void setSizeSnake(int sizeSnake) {
        this.sizeSnake = sizeSnake;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getTimetolive() {
        return timetolive;
    }

    public void setTimetolive(int timetolive) {
        this.timetolive = timetolive;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getMaxwormholes() {
        return maxwormholes;
    }

    public void setMaxwormholes(int maxwormholes) {
        this.maxwormholes = maxwormholes;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public String getLeftKey() {
        return leftKey;
    }

    public void setLeftKey(String leftKey) {
        this.leftKey = leftKey;
    }

    public String getRightKey() {
        return rightKey;
    }

    public void setRightKey(String rightKey) {
        this.rightKey = rightKey;
    }

    public String getRestartKey() {
        return restartKey;
    }

    public void setRestartKey(String restartKey) {
        this.restartKey = restartKey;
    }

    public double getPercentageIncrease() {
        return percentageIncrease;
    }

    public void setPercentageIncrease(double percentageIncrease) {
        this.percentageIncrease = percentageIncrease;
    }

    public boolean isRandomWormholes() {
        return randomWormholes;
    }

    public void setRandomWormholes(boolean randomWormholes) {
        this.randomWormholes = randomWormholes;
    }

    public boolean isFixedWormholes() {
        return fixedWormholes;
    }

    public void setFixedWormholes(boolean fixedWormholes) {
        this.fixedWormholes = fixedWormholes;
    }

    public Color getWormholeColor() {
        return wormholeColor;
    }

    public void setWormholeColor(Color wormholeColor) {
        this.wormholeColor = wormholeColor;
    }

    public Color getAppleColor() {
        return appleColor;
    }

    public void setAppleColor(Color appleColor) {
        this.appleColor = appleColor;
    }

    public Color getGameColor() {
        return gameColor;
    }

    public void setGameColor(Color gameColor) {
        this.gameColor = gameColor;
    }

    public Color getSnakeColor() {
        return snakeColor;
    }

    public void setSnakeColor(Color snakeColor) {
        this.snakeColor = snakeColor;
    }
    
    

    private void initStandardSettings() {
        leftKey = buttonLeft;
        rightKey = buttonRight;
        restartKey = buttonRestart;

        wormholeColor = WORMHOLE_COLOR;
        appleColor = APPLE_COLOR;
        gameColor = GAME_COLOR;
        snakeColor = SNAKE_COLOR;

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

}
