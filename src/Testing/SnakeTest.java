/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Testing;

import Model.Snake;
import java.util.Scanner;

/**
 *
 * @author Jacob
 */
public class SnakeTest {

    public static void main(String[] args) {
        Snake snake = new Snake(4,2,2);
        Scanner scan = new Scanner(System.in);
        String res = null;
        System.out.println(snake.toString());
        while (true) {
            res = scan.next();
            if (res.contains("S")) {
                snake.goLeft();
            } else if (res.contains("D")) {
                snake.goRight();
            } else if (res.contains("W")) {
            } else {
                snake.move();
            }
        }
    }
}
