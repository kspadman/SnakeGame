package org.cis120.snake;

import java.awt.*;

public class Fruit extends GameObj {
    public static final int SIZE = 13;
    public static final int INIT_POS_X = 100;
    public static final int INIT_POS_Y = 150;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;

    /**
     * Note that, because we don't need to do anything special when constructing
     * a Square, we simply use the superclass constructor called with the
     * correct parameters.
     */
    public Fruit(int x, int y) {
        super(INIT_VEL_X, INIT_VEL_Y, x, y, SIZE, SIZE,
                GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}
