/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.viljinsky.puzzle5;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class PuzzlePanel extends Base {

    Puzzle puzzle = new Puzzle();

    public PuzzlePanel() {
        super("PuzzlePanel");
        setPreferredSize(new Dimension(800,600));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        puzzle.paint(g);
    }

}

/**
 *
 * @author viljinsky
 */
public class Test1 implements Runnable, CommandListener, ChangeListener {

    @Override
    public void doCommand(String command) {
        System.out.println(command);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        panel.repaint();
    }

    PuzzlePanel panel;
    CommandManager commandManager = new CommandManager(this, "cmd1", "cmd2");
    CommandBar commandBar = new CommandBar(commandManager);

    public Test1() {
        panel = new PuzzlePanel();
        panel.puzzle.changeListener = this;
        PuzzleListener listener = new PuzzleListener(panel.puzzle);
        panel.addMouseListener(listener);
        panel.addMouseMotionListener(listener);
        panel.addMouseWheelListener(listener);
    }

    @Override
    public void run() {

        Base b = new Base("test1");
        b.setLayout(new BorderLayout());
        b.add(panel);
        b.add(commandBar, BorderLayout.PAGE_START);
        b.showInFrame(null);
    }

    public static void main(String[] args) {
        new Test1().run();
    }
}
