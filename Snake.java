package org.cis120.snake;

/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 *
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.util.LinkedList;

/**
 * A basic game object starting in the upper left corner of the game court. It
 * is displayed as a square of a specified color.
 */
public class Snake {

    private Direction direction;
    private SnakeObj head;
    private LinkedList<SnakeObj> body;

    /**
     * Note that, because we don't need to do anything special when constructing
     * a Square, we simply use the superclass constructor called with the
     * correct parameters.
     */
    public Snake() {
        body = new LinkedList<SnakeObj>();
        body.add(new SnakeObj(0, 0, Color.pink));
        direction = Direction.UP;
        head = body.getFirst();
    }

    public void draw(Graphics g) {
        for (int i = 0; i < body.size(); i++) {
            body.get(i).draw(g);
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public void changeDirection(Direction d) {
        direction = d;
    }

    public void addFruit() {

        int newX = body.getLast().getPx();
        int newY = body.getLast().getPy();

        if (direction == Direction.RIGHT) {
            newX -= 20;
        } else if (direction == Direction.LEFT) {
            newX += 20;
        } else if (direction == Direction.UP) {
            newY += 20;
        } else if (direction == Direction.DOWN) {
            newY -= 20;
        }

        System.out.println("added piece at: (" + newX  + ", " + newY + ")");
        body.add(new SnakeObj(newX, newY, Color.ORANGE));
        System.out.println("head : " + head.getPx() + ", " + head.getPy());
    }

    public void setHead(SnakeObj h) {
        this.head = h;
    }

    public SnakeObj getHead() {
        return head;
    }

    public LinkedList<SnakeObj> getBody() {
        return body;
    }

    public void move() {


        for (int i = body.size() - 1; i > 0; i--) {
            body.get(i).setPx(body.get(i - 1).getPx());
            body.get(i).setPy(body.get(i - 1).getPy());
        }

        if (direction == Direction.RIGHT) {
            body.getFirst().setPx(head.getPx() + head.getVx());
        } else if (direction == Direction.LEFT) {
            body.getFirst().setPx(head.getPx() + head.getVx());
        } else if (direction == Direction.UP) {
            body.getFirst().setPy(head.getPy() + head.getVy());
        } else if (direction == Direction.DOWN) {
            body.getFirst().setPy(head.getPy() + head.getVy());
        }

        head = body.getFirst();
        System.out.println("Head is at: (" + head.getPx() + ", " + head.getPy() + ")");
    }
}