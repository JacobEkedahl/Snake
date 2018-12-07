/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kanonkod.snake.Model;

/**
 *
 * @author Jacob
 */
public class Result {
    private int time, sizeSnake;
    private static int multiplier = 30;

    public Result(int time, int sizeSnake) {
        this.time = time;
        this.sizeSnake = sizeSnake * multiplier;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getSizeSnake() {
        return sizeSnake;
    }

    public void setSizeSnake(int sizeSnake) {
        this.sizeSnake = sizeSnake * multiplier;
    }
    
    
}
