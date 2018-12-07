/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kanonkod.snake.Model;

import java.util.ArrayList;

/**
 *
 * @author Jacob
 */
public class Converter {

    private static int widthScreen, heightScreen, width, height, gameInfoHeight;
    private final static int normalWidth = 600;

    public Converter(int widthScreen, int heightScreen, int width, int height, int gameInfoHeight) {
        this.widthScreen = widthScreen;
        this.heightScreen = heightScreen;
        this.width = width;
        this.height = height;
        this.gameInfoHeight = gameInfoHeight;
    }
    
    public static boolean strToBool(String text) {
        if (text.equals("YES")) {
            return true;
        } else if (text.equals("NO")) {
            return false;
        }
        return false;
    }
    
    public static String boolToString(boolean bool) {
        if (bool) {
            return "YES";
        } else {
            return "NO";
        }
    }
    
    public static int getHeightSettingButton() {
        return gameInfoHeight - (Settings.BORDERSIZE);
    }
    
    public static int getWidthInfoBox() {
        return (widthScreen - (Settings.BORDERSIZE * 2) - getHeightSettingButton() / 2);
    }
    
    public static String intToStr(int integer) {
        return String.valueOf(integer);
    }

    public boolean isInteger(String s, int radix) {
        if (s.isEmpty()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) {
                    return false;
                } else {
                    continue;
                }
            }
            if (Character.digit(s.charAt(i), radix) < 0) {
                return false;
            }
        }
        return true;
    }

    public int getHeightBox() {
        return heightScreen / height;
    }

    public int getWidthBox() {
        return widthScreen / width;
    }

    public int getSizeRectInfo() {
        return (int) (gameInfoHeight * 0.5);
    }

    public int getMaxWormholes() {
        return (widthScreen / 2) / ((getSizeRectInfo() + 10));
    }

    public Position convertToPos(double x, double y) {
        return new Position((int) x / getWidthBox(), (int) y / getHeightBox());
    }

    public ArrayList<Position> getWormholes(ArrayList<Wormhole> wormholes) {
        ArrayList<Position> wormholePos = new ArrayList<>();
        for (Wormhole hole : wormholes) {
            if (!hole.isIsInside()) {
                for (Position entries : hole.posOfHoles()) {
                    wormholePos.add(entries);
                }
            }
        }
        return wormholePos;
    }

}
