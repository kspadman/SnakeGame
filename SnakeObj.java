package org.cis120.snake;

import java.awt.*;

public class SnakeObj extends GameObj {
    private Color color;

    public SnakeObj(int x, int y, Color c) {
        super(0, 0, x, y, 20, 20, GameCourt.COURT_HEIGHT, GameCourt.COURT_WIDTH);
        this.color = c;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(this.getPx(), this.getPy() , 20, 20);
    }
}
