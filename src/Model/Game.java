/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import View.BoardView;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import javafx.concurrent.Task;

//TODO reset increasespeed timer when user eats an apple
/**
 *
 * @author Jacob
 */
public class Game {

    private int intervalWormhole;
    private double percentageIncrease;
    private Snake snake;
    private int time;
    private int speed;
    private int originalSpeed;
    private ArrayList<WormHole> wormholes;
    private ArrayList<Apple> apples;
    private ArrayList<Position> board;
    private BoardView view;
    private Timer timer = new Timer();
    private Timer timerForTime = new Timer();

    private int snakeSize, width, height;

    public Game(int snakeSize, int width, int height, int speed, double percentageIncrease, int intervalWormhole, BoardView view) {
        this.intervalWormhole = intervalWormhole;
        this.percentageIncrease = percentageIncrease;
        this.originalSpeed = speed;
        this.snakeSize = snakeSize;
        this.width = width;
        this.height = height;
        this.view = view;
        reset();
    }

    /**
     * Snake centered in screen with starting direction to left Resetting
     * variables which changes during runtime
     */
    public void reset() {
        wormholes = new ArrayList<>();
        snake = new Snake(snakeSize, width / 2, height / 2);
        this.time = 0;
        this.speed = originalSpeed;
        apples = new ArrayList<>();
        wormholes = new ArrayList<>();
        initBoard(width, height);
    }

    public void start() {
        resetTime();
        generateRandomApple();
        resetGameTimer();
    }

    private void initBoard(int width, int height) {
        board = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                board.add(new Position(i, j));
            }
        }
    }

    private boolean headColideWithApple() {
        for (int i = 0; i < apples.size(); i++) {
            if (snake.getHeadPosition().equals(apples.get(i).getPos())) {
                apples.remove(i);
                return true;
            }
        }
        return false;
    }

    private Position getSingleFreePos() {
        ArrayList<Position> freePos = getFreePos(snake.getPosition());
        int size = freePos.size();
        int index = (int) (Math.random() * size);
        return freePos.get(index);
    }

    private void generateRandomWormhole() {
        Position entry = getSingleFreePos();
        Position entry2 = getSingleFreePos();
        while (entry.equals(entry2)) {
            entry2 = getSingleFreePos();
        }
        WormHole newWormhole = new WormHole(entry, entry2);
        wormholes.add(newWormhole);
    }

    private void generateRandomApple() {
        Position pos = getSingleFreePos();
        Apple newApple = new Apple(pos.getX(), pos.getY());
        apples.add(newApple);
    }

    private void eatApple(Position p) {
        snake.eatApple(p);
        apples.remove(p);
    }

    private boolean headDeadColision() {
        ArrayList<Position> freePosBoard = safePositions();
        if (freePosBoard.contains(snake.getHeadPosition())) {
            return false;
        }
        return true;
    }

    private ArrayList<Position> safePositions() {
        ArrayList<Position> safePos = (ArrayList<Position>) board.clone();
        ArrayList<Position> snakePos = snake.getPosition();
        snakePos.remove(snake.getHeadPosition());
        safePos.removeAll(snakePos);
        return safePos;
    }

    private ArrayList<Position> getFreePos(ArrayList<Position> snakeBody) {
        ArrayList<Position> tmpBoard = (ArrayList<Position>) board.clone();
        tmpBoard.removeAll(snakeBody);
        tmpBoard.removeAll(apples);
        for (WormHole hole : wormholes) {
            tmpBoard.removeAll(hole.posOfHoles());
        }
        return tmpBoard;
    }

    public ArrayList<Position> getBoard() {
        return (ArrayList<Position>) board.clone();
    }

    public ArrayList<Position> getSnakePosition() {
        return snake.getPosition();
    }

    public ArrayList<Position> getApples() {
        ArrayList<Position> applesPos = new ArrayList<>();
        for (Apple a : apples) {
            applesPos.add(a.getPos());
        }
        return applesPos;
    }

    /**
     * Linear increase of speed with set percentage Maximum speed is 1ms per
     * iteration
     *
     * @param percentage
     */
    public void increaseSpeed(double percentage) {
        speed *= percentage;
        resetGameTimer();
    }

    /**
     * No set limit for how slow the snake can move Slows down same rate as its
     * gets faster
     *
     * @param percentage
     */
    public void decreaseSpeed(double percentage) {
        double realPercentage = 2 - percentage;
        speed *= realPercentage;
        resetGameTimer();
    }

    /**
     * When game ends the viewclass ask for the current result from game Score
     * is number of apples eaten
     *
     * @return
     */
    public Result getResult() {
        return new Result(time, snake.getSize() - snakeSize);
    }

    public ArrayList<Position> getWormholes() {
        ArrayList<Position> wormholePos = new ArrayList<>();
        for (WormHole hole : wormholes) {
            for (Position entries : hole.posOfHoles()) {
                wormholePos.add(entries);
            }
        }
        return wormholePos;
    }

    private void headCollisionWormhole() {
        for (WormHole hole : wormholes) {
            if (hole.isIsInside()) {
                hole.redirect();
                System.out.println("Snakepos redir: " + snake.toString());
              //  System.out.println("Redirecting..");
            } else {
                hole.testCollisionWithWormhole(snake);
            }
        }
    }

    private void resetGameTimer() {
        if (timer != null) {
            timer.cancel();
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (headColideWithApple()) {
                    eatApple(snake.getHeadPosition());
                    generateRandomApple();
                    System.out.println("Apples: " + apples.toString());
                    System.out.println("Head colided with apple");
                    view.updateUI();
                    snake.move();
                } else if (headDeadColision()) {
                    view.showGameOver();
                    timer.cancel();
                    timerForTime.cancel();
                } else {
                    view.updateUI();
                    snake.move();
                }
                headCollisionWormhole();
            }
        }, 0, speed);
    }

    private void resetTime() {
        if (timerForTime != null) {
            speed = originalSpeed;
            timerForTime.cancel();
            timerForTime = new Timer();
        }
        timerForTime.schedule(new TimerTask() {
            @Override
            public void run() {
                if (time % 5 == 0) {
                    increaseSpeed(percentageIncrease);
                }
                
                if (time % intervalWormhole == 0) {
                    wormholes.clear();
                    generateRandomWormhole();
                }
                time += 1;
            }
        }, 0, 1000);
    }

    private void gameOver() {
        System.out.println("Time: " + time);
    }

    public boolean goLeft() {
        return snake.goLeft();
    }

    public boolean goRight() {
        return snake.goRight();
    }

    public Snake getSnake() {
        return snake;
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "Game{" + "snake=" + snake + ", time=" + time + ", apples=" + apples + '}';
    }

}
