package org.cis120.snake;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.LinkedList;
import javax.swing.*;

public class GameCourt extends JPanel {

    // the state of the game logic
    private Snake snake; // the snake, keyboard control
    private Fruit currentFruit; // the current fruit, stays in a square

    private boolean playing = false; // whether the game is running
    private JLabel status; // Current status text, i.e. "Running..."
    private JLabel score;
    private String filePath = "/Users/ksai/Downloads/hw09_local_temp/" +
            "src/main/java/org/cis120/snake/gameState.txt";

    // Game constants
    public static final int COURT_WIDTH = 500;
    public static final int COURT_HEIGHT = 500;
    public static final int SNAKE_VELOCITY = 20;
    private int totalScore;
    private LinkedList<Fruit> poisonFruits;
    private Timer timer;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 120;

    public GameCourt(JLabel status, JLabel score) {

        snake = new Snake();
        poisonFruits = new LinkedList<Fruit>();
        Grid g = new Grid();

        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // The timer is an object which triggers an action periodically with the
        // given INTERVAL. We register an ActionListener with this timer, whose
        // actionPerformed() method is called each time the timer triggers. We
        // define a helper method called tick() that actually does everything
        // that should be done in a single timestep.
        timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key
        // is pressed, by changing the square's velocity accordingly. (The tick
        // method below actually moves the square.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT && snake.getDirection() != Direction.RIGHT) {
                    snake.changeDirection(Direction.LEFT);
                    for (SnakeObj piece : snake.getBody()) {
                        piece.setVx(-SNAKE_VELOCITY);
                        piece.setVy(0);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT
                        && snake.getDirection() != Direction.LEFT) {
                    snake.changeDirection(Direction.RIGHT);
                    for (SnakeObj piece : snake.getBody()) {
                        piece.setVx(SNAKE_VELOCITY);
                        piece.setVy(0);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN
                        && snake.getDirection() != Direction.UP) {
                    snake.changeDirection(Direction.DOWN);
                    for (SnakeObj piece : snake.getBody()) {
                        piece.setVy(SNAKE_VELOCITY);
                        piece.setVx(0);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_UP
                        && snake.getDirection() != Direction.DOWN) {
                    snake.changeDirection(Direction.UP);
                    for (SnakeObj piece : snake.getBody()) {
                        piece.setVy(-SNAKE_VELOCITY);
                        piece.setVx(0);
                    }
                }
            }

            public void keyReleased(KeyEvent e) {

            }
        });

        this.status = status;
        this.score = score;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {

        totalScore = 0;
        snake = new Snake();
        generateNewFruit();
        poisonFruits.clear();
        int numPoison = 5;
        for (int i = 0; i < numPoison; i++) {
            poisonFruits.add(generatePoisonFruit());
        }


        playing = true;
        status.setText("Running...");
        score.setText("Score: " + totalScore);

        status.repaint();

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        if (playing) {

            snake.move();

            // check for the game end conditions
            for (int i = 3; i < snake.getBody().size(); i++) {
                if (snake.getHead().getPx() == snake.getBody().get(i).getPx()
                        && snake.getHead().getPy() == snake.getBody().get(i).getPy()) {
                    playing = false;
                    status.setText("End of game! You ran into yourself...");
                }
            }
            for (Fruit f : poisonFruits) {
                if (snake.getHead().willIntersect(f)) {
                    snake.move();
                    saveState();
                    playing = false;
                    status.setText("End of game! You hit a poison fruit :(");
                }
            }
            if (snake.getHead().hitWall()) {
                if (snake.getBody().size() == 1) {
                    saveState();
                    playing = false;
                    status.setText("End of game! You hit a wall...");
                } else if (snake.getBody().get(1).hitWall()) {
                    saveState();
                    playing = false;
                    status.setText("End of game! You hit a wall...");
                }
            } else if (snake.getHead().willIntersect(currentFruit)) {
                playing = true;

                if (currentFruit instanceof BonusFruit) {
                    totalScore += 2;
                    for (int i = 0; i < 2; i++) {
                        snake.addFruit();
                    }
                    status.setText("Nice! You ate a bonus fruit, " +
                            "which added 3 to your score :)");
                }
                snake.addFruit();
                generateNewFruit();
                poisonFruits.add(generatePoisonFruit());
                totalScore += 1;
                score.setText("Score: " + totalScore);


            }

            // update the display
            repaint();
        }
    }

    public void load() {

        Direction newD;
        if (snake.getDirection() == Direction.UP || snake.getDirection() == Direction.DOWN) {
            newD = Direction.LEFT;
        } else {
            newD = Direction.DOWN;
        }

        snake = new Snake();
        generateNewFruit();
        snake.getBody().remove(0);
        poisonFruits.clear();
        int numPoison = 5;
        for (int i = 0; i < numPoison; i++) {
            poisonFruits.add(generatePoisonFruit());
        }



        FileLineIterator fr = new FileLineIterator(filePath);
        if (fr.hasNext()) {
            int headX = Integer.parseInt(fr.next());
            int headY = Integer.parseInt(fr.next());
            SnakeObj newHead = new SnakeObj(headX, headY, Color.PINK);
            snake.getBody().add(newHead);
            snake.setHead(snake.getBody().getFirst());
        }
        while (fr.hasNext()) {
            int partX = Integer.parseInt(fr.next());
            int partY = Integer.parseInt(fr.next());

            SnakeObj newPart = new SnakeObj(partX, partY, Color.ORANGE);
            snake.getBody().add(newPart);
        }

        status.setText("Running...");
        score.setText("Score: " + snake.getBody().size());

        status.repaint();

        repaint();
        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
        snake.changeDirection(newD);
        if (snake.getDirection() == Direction.LEFT) {
            snake.getHead().setVx(-20);
            snake.getHead().setVy(0);
        } else {
            snake.getHead().setVx(0);
            snake.getHead().setVy(20);
        }
        playing = true;

            /*addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        playing = true;
                    }
                }
            });

             */

    }

    public void saveState() {
        playing = false;
        LinkedList<String> stringsToWrite = new LinkedList<>();

        for (SnakeObj bodyPart : snake.getBody()) {
            stringsToWrite.add(Integer.toString(bodyPart.getPx()));
            stringsToWrite.add(Integer.toString(bodyPart.getPy()));
        }

        File file = new File(filePath);
        if (file.delete()) {
            try {
                file.createNewFile();
            } catch (IOException e) {

            }
        } else {
            System.out.println("file not deleted");
        }

        try {
            Writer w = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(w);
            for (String s : stringsToWrite) {
                System.out.println("WRITING!");
                bw.write(s);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("error writing to file");
        }
    }

    private void generateNewFruit() {
        int random = (int) (Math.random() * 10);
        int xCoord = (int) (Math.random() * 480);
        int yCoord = (int) (Math.random() * 480);
        for (Fruit f : poisonFruits) {
            if (new Fruit(xCoord, yCoord).willIntersect(f)) {
                generateNewFruit();
            }
        }
        for (SnakeObj piece : snake.getBody()) {
            if (new Fruit(xCoord, yCoord).willIntersect(piece)) {
                generateNewFruit();
            }
        }
        if (random < 3) {
            currentFruit = new BonusFruit(xCoord, yCoord);
        } else {
            currentFruit = new Fruit(xCoord, yCoord);
        }
    }


    private PoisonFruit generatePoisonFruit() {
        int xCoord = (int) (Math.random() * 480);
        int yCoord = (int) (Math.random() * 480);
        if (xCoord == 0 && yCoord == 0) {
            generatePoisonFruit();
        }
        if (new PoisonFruit(xCoord, yCoord).willIntersect(currentFruit)) {
            generatePoisonFruit();
        }
        for (SnakeObj piece : snake.getBody()) {
            if (new PoisonFruit(xCoord, yCoord).willIntersect(piece)) {
                generatePoisonFruit();
            }
        }
        for (Fruit f : poisonFruits) {
            if (new PoisonFruit(xCoord, yCoord).willIntersect(f)) {
                generatePoisonFruit();
            }
        }

        return new PoisonFruit(xCoord, yCoord);
    }

    public void setPlaying(boolean b) {
        playing = b;
    }

    public Snake getSnake() {
        return snake;
    }

    public LinkedList<Fruit> getPoisonFruits() {
        return poisonFruits;
    }

    public boolean getPlaying() {
        return playing;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        snake.draw(g);
        currentFruit.draw(g);
        Grid.draw(g, snake);
        for (Fruit fr : poisonFruits) {
            fr.draw(g);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}
