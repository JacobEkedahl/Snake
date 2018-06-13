/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Jacob
 */
public class WormHole {
    //consist of two Holes
    //a snake can enter wormhole
    //when a worm has exited wormhole wormhole is gone

    private Hole entry, exit;
    private int directionOfSnake;
    private boolean isInside;

    public WormHole(Position entry, Position exit) {
        this.entry = new Hole(entry);
        this.exit = new Hole(exit);
        isInside = false;
        directionOfSnake = 0;
    }
    
    public boolean collision(SnakePiece snakeHead, int direction) {
        if (snakeHead.getPos().equals(entry.getPos()) || snakeHead.equals(exit.getPos())) {
            directionOfSnake = direction;
            isInside = true;
            return true;
        }
        return false;
    }
    
    public void enter(SnakePiece piece, int direction) {
        
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

        public Position south() {
            return new Position(pos.getX(), pos.getY() + 1);
        }

        public Position left() {
            return new Position(pos.getX() - 1, pos.getY());
        }
    }
}
