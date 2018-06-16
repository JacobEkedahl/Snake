/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Jacob
 */
public class Snake {

    public final static int NORTH = 0;
    public final static int EAST = 1;
    public final static int SOUTH = 2;
    public final static int WEST = 3;

    private boolean canTurn;
    private LinkedList<SnakePiece> pieces;
    private int direction;

    public Snake(int size, int centerX, int centerY) {
        initSnake(size, centerX, centerY);
    }

    private void initSnake(int size, int centerX, int centerY) {
        pieces = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            SnakePiece snakePiece = new SnakePiece(centerX + i, centerY);
            pieces.add(snakePiece);
            System.out.println("i: " + i);
        }

        direction = WEST;
        canTurn = true;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    //move
    public void move() {
        int firstX, firstY;
        SnakePiece neighbour = pieces.getFirst();
        Position neighboutPos = neighbour.getPos();
        firstX = neighboutPos.getX();
        firstY = neighboutPos.getY();

        if (direction == NORTH) {
            firstY -= 1;
        } else if (direction == EAST) {
            firstX += 1;
        } else if (direction == SOUTH) {
            firstY += 1;
        } else if (direction == WEST) {
            firstX -= 1;
        }

        SnakePiece last = pieces.getLast();
        Position posLast = new Position(firstX, firstY);
        last.setPos(posLast);
        pieces.addFirst(last);
        pieces.removeLast();
        canTurn = true;
    }

    public boolean goLeft() {
        if (canTurn) {
            direction = ((direction - 1) + 4) % 4;
            canTurn = false;
            return true;
        }
        return false;
    }

    public boolean goRight() {
        if (canTurn) {
            direction = (direction + 1) % 4;
            canTurn = false;
            return true;
        }
        return false;
    }

    public void eatApple(Position p) {
        SnakePiece piece = new SnakePiece(p.getX(), p.getY());
        pieces.addFirst(piece);
    }
    
    public SnakePiece getSnakePiece(int index) {
        if (index < 0 || index == pieces.size()) {
            return null;
        }
        return pieces.get(index);
    }

    public Position getHeadPosition() {
        return pieces.getFirst().getPos();
    }

    public synchronized ArrayList<Position> getPosition() {
        ArrayList<Position> snakePos = new ArrayList<>();
        for (SnakePiece snakeP : pieces) {
            snakePos.add(snakeP.getPos());
        }
        return snakePos;
    }
    
    public int getSize() {
        return pieces.size();
    }

    @Override
    public String toString() {
        return "Snake{" + "canTurn=" + canTurn + ", pieces=" + pieces + ", direction=" + direction + '}';
    }

}
