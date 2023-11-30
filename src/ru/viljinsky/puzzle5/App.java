/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.viljinsky.puzzle5;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
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

class PuzzleImage extends BufferedImage {

    private void init() {
        Graphics g = getGraphics();
        g.setColor(Color.GREEN);
        for (int x = 0; x < getWidth() + getHeight(); x += 10) {
            g.drawLine(x - getHeight(), 0, x, getHeight());
        }
    }

    public PuzzleImage(int width, int height) {
        super(width, height, TYPE_INT_ARGB);
        init();
    }

}

class PuzzleItem {

    int x;
    int y;
    int col;
    int row;
    Shape shape;
    int colSize = 30;
    int rowSize = 40;
    double theta = .0;
    BufferedImage image;// = new PuzzleImage(colSize,rowSize);

    public PuzzleItem(int col, int row) {
        this.col = col;
        this.row = row;
        setCenter(col * colSize + colSize / 2, row * rowSize + rowSize / 2);
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        //      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        AffineTransform old = g2.getTransform();

        AffineTransform transform = AffineTransform.getRotateInstance(theta, x, y);
        g2.setTransform(transform);

        if (image != null) {
            g2.drawImage(image, x - colSize / 2, y - rowSize / 2, null);
        }
        g2.setColor(Color.red);
        g2.draw(shape);
        g2.drawOval(x - 2, y - 2, 4, 4);

        g2.setTransform(old);
    }

    public void setCenter(int x, int y) {
        this.x = x;
        this.y = y;
        shape = new Rectangle(x - (colSize / 2), y - (rowSize / 2), colSize, rowSize);
    }

    public void setCenter(Point p, Point offset) {
        setCenter(p.x - offset.x, p.y - offset.y);
    }

    public Point getCenter() {
        return new Point(x, y);
    }

    public boolean hitTest(Point p) {
        return shape.contains(p);
    }

    @Override
    public String toString() {
        return String.format("item %d %d", col, row);
    }

}

class Puzzle {

    int cols = 30;
    int rows = 40;

    ChangeListener changeListener;

    ArrayList<PuzzleItem> list = new ArrayList<>();

    BufferedImage image = new PuzzleImage(400, 500);

    public Puzzle() {
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                PuzzleItem item = new PuzzleItem(col, row);
                if ((row + 1) * item.rowSize < image.getHeight() && (col + 1) * item.colSize < image.getWidth()) {
//                    item.image = image.getSubimage(col*item.colSize,row*item.rowSize, item.colSize,item.rowSize);
                    item.image = image.getSubimage(col * item.colSize, row * item.rowSize, item.colSize, item.rowSize);
                }
                list.add(item);
            }
        }

        list.get(5).setCenter(105, 55);
    }

    public void paint(Graphics g) {
        for (PuzzleItem item : list) {
            item.paint(g);
        }
    }

    public void change() {
        if (changeListener != null) {
            changeListener.stateChanged(new ChangeEvent(this));
        }
    }

    PuzzleItem itemAt(Point point) {
        for (PuzzleItem item : list) {
            if (item.hitTest(point)) {
                return item;
            }
        }
        return null;
    }
}

class PuzzleListener extends MouseAdapter {

    Puzzle puzzle;
    PuzzleItem selected;
    Point offset;

    public PuzzleListener(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        System.out.println("mouseWheel" + e.getWheelRotation());
        PuzzleItem item = puzzle.itemAt(e.getPoint());
        if (item != null) {
            switch (e.getWheelRotation()) {
                case 1:
                    item.theta += Math.PI / 8;
                    break;
                case -1:
                    item.theta -= Math.PI / 8;
                    break;
            }
            puzzle.change();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (selected != null) {
            selected.setCenter(e.getPoint(), offset);
            puzzle.change();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (selected != null) {
            selected.setCenter(e.getPoint(), offset);
            System.out.println("mouseReleased " + selected);
            selected = null;
            puzzle.change();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        selected = null;
        for (PuzzleItem item : puzzle.list) {
            if (item.hitTest(e.getPoint())) {
                selected = item;
                offset = new Point(e.getX() - item.x, e.getY() - item.y);
                System.out.println("mousePressed " + selected + " " + offset);
                puzzle.change();
                return;
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
