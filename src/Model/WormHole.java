/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;

/**
 *
 * @author Jacob
 */
public class WormHole {
    //consist of two Holes
    //a snake can enter wormhole
    //when a worm has exited wormhole wormhole is gone

    private Hole entry, entry2;
    private int directionOfSnake;
    private int sizeOfSnake;
    private boolean isInside;
    private int indexOfSnake;
    private Hole exit;
    private Snake snake;

    public WormHole(Position entry, Position entry2) {
        this.entry = new Hole(entry);
        this.entry2 = new Hole(entry2);
        isInside = false;
        directionOfSnake = 0;
    }

    public boolean testCollisionWithWormhole(Snake snake) {
        if (snake.getHeadPosition().equals(entry.getPos())) {
            return collisionDetected(entry2, snake);
        } else if (snake.getHeadPosition().equals(entry2.getPos())) {
            return collisionDetected(entry, snake);
        }
        return false;
    }

    private boolean collisionDetected(Hole whichEntry, Snake snake) {
        this.snake = snake;
        this.sizeOfSnake = snake.getSize();
        exit = whichEntry;
        isInside = true;
        directionOfSnake = snake.getDirection();
        this.sizeOfSnake = sizeOfSnake;
        indexOfSnake = 0;
        return true;
    }

    public boolean isIsInside() {
        return isInside;
    }

    public void setIsInside(boolean isInside) {
        this.isInside = isInside;
    }
    
    public ArrayList<Position> posOfHoles() {
        ArrayList<Position> positions = new ArrayList<>();
        positions.add(entry.getPos());
        positions.add(entry2.getPos());
        return positions;
    }

    @Override
    public String toString() {
        return "WormHole{" + "entry=" + entry.pos.getId() + ", entry2=" + entry2.pos.getId() + '}';
    }
    
    

    public void redirect() {
        if (sizeOfSnake > 0) {
            switch (directionOfSnake) {
                case Snake.NORTH:
                    snake.getSnakePiece(indexOfSnake++).setPos(exit.up());
                    break;
                case Snake.EAST:
                    snake.getSnakePiece(indexOfSnake++).setPos(exit.right());
                    break;
                case Snake.SOUTH:
                    snake.getSnakePiece(indexOfSnake++).setPos(exit.down());
                    break;
                case Snake.WEST:
                    snake.getSnakePiece(indexOfSnake++).setPos(exit.left());
                    break;
            }
            sizeOfSnake--;
            System.out.println("new snake: " + snake.toString());
        } else {
            isInside = false;
        }
    }

    private class Hole {

        //has a positions
        private Position pos;

        public Hole(Position pos) {
            this.pos = pos;
        }

        public Position getPos() {
            return pos;
        }

        public void setPos(Position pos) {
            this.pos = pos;
        }

        public Position up() {
            return new Position(pos.getX(), pos.getY() - 1);
        }

        public Position right() {
            return new Position(pos.getX() + 1, pos.getY());
        }

        public Position down() {
            return new Position(pos.getX(), pos.getY() + 1);
        }

        public Position left() {
            return new Position(pos.getX() - 1, pos.getY());
        }
    }
}
