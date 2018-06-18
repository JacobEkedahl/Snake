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
public class Converter {
    private int widthScreen, heightScreen, width, height;

    public Converter(int widthScreen, int heightScreen, int width, int height) {
        this.widthScreen = widthScreen;
        this.heightScreen = heightScreen;
        this.width = width;
        this.height = height;
    }
    
    public int getHeightBox() {
        return heightScreen / height;
    }
    
    public int getWidthBox() {
        return widthScreen / width;
    }
    
    public Position convertToPos(double x, double y) {
        return new Position((int) x / getWidthBox(), (int) y / getHeightBox());
    }
    
}
