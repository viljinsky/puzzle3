/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.viljinsky.puzzle5;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

interface CommandListener {

    public void doCommand(String command);
}

class CommandManager extends ArrayList<Action> {

    CommandListener commandListener;

    public void doCommand(String command) {
        if (commandListener != null) {
            commandListener.doCommand(command);
        }
    }

    Action commandAction(String command) {
        return new AbstractAction(command) {
            @Override
            public void actionPerformed(ActionEvent e) {
                doCommand(e.getActionCommand());
            }
        };
    }

    public CommandManager(CommandListener commandListener, String... commands) {
        this.commandListener = commandListener;
        for (String command : commands) {
            add(commandAction(command));
        }
    }

}

class CommandBar extends Container {

    public CommandBar(ArrayList<Action> list) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        for (Action a : list) {
            if (a != null) {
                add(new JButton(a));
            }
        }
    }

}

class Browser extends Base implements ChangeListener, CommandListener {

    CommandManager commandManager = new CommandManager(this, "cmd1", "cmd2");
    CommandBar commandBar = new CommandBar(commandManager);

    Puzzle puzzle = new Puzzle();

    @Override
    public void stateChanged(ChangeEvent e) {
        repaint();
    }

    @Override
    public void doCommand(String command) {
        System.out.println(command);
        switch (command) {
            case "cmd1":

                for (PuzzleItem item : puzzle.list) {
                    item.theta += Math.PI / 8;
                }
                puzzle.change();
                break;

            case "cmd2":
                for (PuzzleItem item : puzzle.list) {
                    int x = (int) (Math.random() * 800);
                    int y = (int) (Math.random() * 800);
                    item.setCenter(x, y);
                }
                puzzle.change();
                break;

        }

    }

    public Browser() {
        super("browser");
        setLayout(new BorderLayout());
        add(commandBar, BorderLayout.PAGE_START);
        setPreferredSize(new Dimension(800, 600));
        PuzzleListener listener = new PuzzleListener(puzzle);
        addMouseListener(listener);
        addMouseMotionListener(listener);
        addMouseWheelListener(listener);
        puzzle.changeListener = this;
    }

    @Override
    public void paint(Graphics g
    ) {
        if (puzzle != null) {
            //     g.drawImage(puzzle.image,0,0,null);
            puzzle.paint(g);
        }
        super.paint(g);
    }

}

class Base extends Container implements WindowListener {

    private String title = "Base";
    private JFrame frame;

    public Base(String title) {
        this.title = title;
    }

    public void showInFrame(Component parent) {
        frame = new JFrame(title);
        frame.addWindowListener(this);
        frame.setContentPane(this);
        frame.pack();
        frame.setLocationRelativeTo(parent);
        frame.setVisible(true);
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (frame != null) {
            frame.setVisible(false);
            frame.dispose();
            frame = null;
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

}

/**
 *
 * @author viljinsky
 */
public class App implements Runnable {

    Browser browser = new Browser();

    @Override
    public void run() {

        for (int i = 0; i < 10; i++) {
            System.out.println((int) (Math.random() * 100));
        }
        browser.showInFrame(null);

    }

    public static void main(String[] args) {
        new App().run();
    }

}
