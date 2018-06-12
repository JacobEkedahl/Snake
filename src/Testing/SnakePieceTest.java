/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Testing;

import Model.SnakePiece;

/**
 *
 * @author Jacob
 */
public class SnakePieceTest {
    public static void main(String[] args) {
        SnakePiece piece = new SnakePiece(5,10);
        
        System.out.println(piece.toString());
    }
}
