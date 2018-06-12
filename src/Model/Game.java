/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import View.BoardView;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javafx.concurrent.Task;

/**
 *
 * @author Jacob
 */
public class Game {

    private Snake snake;
    private int time;
    private int speed;
    private Timer timer;
    private ArrayList<Apple> apples;
    private ArrayList<Position> board;
    private BoardView view;
    private Timer timerForTime = new Timer();

    public Game(int snakeSize, int width, int height, int speed, BoardView view) {
        this.view = view;
        snake = new Snake(snakeSize, width / 2, height / 2);
        this.time = 0;
        this.speed = speed;
        timer = new Timer();
        apples = new ArrayList<>();
        initBoard(width, height);
    }

    public Game(int snakeSize, int speed) {
        snake = new Snake(snakeSize, 50, 50);
        this.speed = speed;
        this.time = 0;
        timer = new Timer();
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

    private void generateRandomApple() {
        ArrayList<Position> freePos = getFreePos(snake.getPosition());
        int size = freePos.size();
        int index = (int) (Math.random() * size);
        Position pos = freePos.get(index);
        Apple newApple = new Apple(pos.getX(), pos.getY());

        apples.add(newApple);
    }

    private void eatApple(Position p) {
        snake.eatApple(p);
        apples.remove(p);
    }

    private boolean headDeadColision() {
        ArrayList<Position> snakePos = snake.getPosition();
        snakePos.remove(snake.getHeadPosition());
        ArrayList<Position> freePosBoard = getFreePos(snakePos);

        if (freePosBoard.contains(snake.getHeadPosition())) {
            return false;
        }
        return true;
    }

    private ArrayList<Position> getFreePos(ArrayList<Position> snakeBody) {
        ArrayList<Position> tmpBoard = (ArrayList<Position>) board.clone();
        tmpBoard.removeAll(snakeBody);
        tmpBoard.removeAll(apples);
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

    public int increaseSpeed(int amount) {
        if (speed - amount <= 0) {
            amount /= 2;
        }
        speed -= amount;
        timer.cancel();
        timer = new Timer();
        resetGameTimer();
        return amount;
    }

    public int decreaseSpeed(int amount) {
        speed += amount;
        timer.cancel();
        timer = new Timer();
        resetGameTimer();
        return amount *= 2;
    }

    private void resetGameTimer() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (headColideWithApple()) {
                    eatApple(snake.getHeadPosition());
                    generateRandomApple();
                    System.out.println("Head colided with apple");
                } else if (headDeadColision()) {
                    System.out.println("Game over");
                    gameOver();
                    timer.cancel();
                    timerForTime.cancel();
                }
                snake.move();
                view.updateUI();
            }
        }, 0, speed);
    }

    public void start() {
        generateRandomApple();
        timerForTime.schedule(new TimerTask() {
            @Override
            public void run() {
                time += 1;
            }
        }, 1000);
        resetGameTimer();

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
