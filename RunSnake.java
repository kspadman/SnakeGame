package org.cis120.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RunSnake implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for
        // local variables.

        // Top-level frame in which game components live.
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("SNAKE");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);
        final JLabel score = new JLabel("Score: 1");

        // Main playing area
        final org.cis120.snake.GameCourt court = new GameCourt(status, score);
        court.setBackground(Color.BLACK);
        frame.add(court, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(control_panel, " " +
                        "Welcome to Snake! \n You control the snake with the arrow keys" +
                        " and the objective of the game is to collect as many fruits as possible." +
                        "\n If you hit a wall, you will die. Also, if you hit a poison (red) " +
                        "fruit, you will die. \n However, the green fruits are " +
                        "bonus which will boost your score!" +
                        "\n The white fruits are normal fruits, and add 1 to your score." +
                        "\n If you wish to load the current position of the snake during " +
                        "or after the game, hit" +
                        " the 'save' button. \n At anytime, you can load the saved state " +
                        "by hitting " +
                        "the 'load' button" +
                        "\n Hope you enjoy!");
                court.reset();
            }
        });
        final JButton load = new JButton("Load");
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.load();
            }
        });
        final JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.saveState();
            }
        });
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });
        control_panel.add(reset);
        control_panel.add(save);
        control_panel.add(load);
        control_panel.add(instructions);
        control_panel.add(score);


        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.reset();
    }
}
