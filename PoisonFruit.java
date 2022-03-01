package org.cis120.snake;


import java.awt.*;
import java.awt.image.BufferedImage;

public class PoisonFruit extends Fruit {

    private BufferedImage img;
    /**
     * Note that, because we don't need to do anything special when constructing
     * a Square, we simply use the superclass constructor called with the
     * correct parameters.
     *
     * @param
     * @param
     */
    public PoisonFruit(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}
