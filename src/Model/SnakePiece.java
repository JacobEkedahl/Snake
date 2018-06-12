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
public class SnakePiece { 
    private Position pos;

    public SnakePiece(int x, int y) {
        pos = new Position(x, y);
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }
    
    @Override
    public String toString() {
        return "SnakePiece{" + "pos=" + pos + '}';
    }
    
    public void moveNorth() {
        
    }
    
}
