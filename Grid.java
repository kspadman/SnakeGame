package org.cis120.snake;

import java.awt.*;

public class Grid {

    private static int row = 0;
    private static int column = 0;
    private SnakeObj[][] grid;

    public Grid() {
        grid = new SnakeObj[row][column];
        row = GameCourt.COURT_WIDTH / 20;
        column = GameCourt.COURT_HEIGHT / 20;
    }

    public static void draw(Graphics g, Snake s) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < row; j++) {
                for (SnakeObj bodyPart : s.getBody()) {
                    bodyPart.draw(g);
                }
            }
        }
    }

}
