/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.viljinsky.puzzle3;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class PuzzleItem {

    int col;
    int row;
    Puzzle puzzle;
    Shape shape;
    int x;
    int y;

    public PuzzleItem(Puzzle puzzle, int col, int row) {
        this.col = col;
        this.row = row;
        this.puzzle = puzzle;
        shape = puzzle.itemRectangle(col, row);
        int w = puzzle.itemWidth();
        int h = puzzle.itemHeight();
        x = w * col + w/2;
        y = h * row + h/2;
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(Color.LIGHT_GRAY);
        g2.draw(shape);
        g2.setPaint(Color.GREEN);
        Shape center = new Rectangle(x-2,y-2,5,5);
        g2.draw(center);
    }

    public boolean hitTest(Point p) {
        return shape.contains(p);
    }

    @Override
    public String toString() {
        return String.format("item %d %d (%d %d)", col, row,x,y);
    }

}

class PuzzleListener implements MouseListener, MouseMotionListener {

    Puzzle puzzle;
    PuzzleItem selected;

    public PuzzleListener(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        selected = null;
        for (PuzzleItem item : puzzle.items) {
            if (item.hitTest(e.getPoint())) {
                selected = item;
                puzzle.onClick(item);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (selected!=null){
            puzzle.onMove(selected, e.getPoint());
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}

class Puzzle implements ImageObserver {

    Image image;
    int cols;
    int rows;
    ArrayList<PuzzleItem> items = new ArrayList<>();
    
    ChangeListener changeListener;
    
    public int itemWidth(){
        return image.getWidth(this) / cols;
    }
    
    public int itemHeight(){
        return image.getHeight(this) / rows;
    }

    Rectangle itemRectangle(int col, int row) {
        int width = image.getWidth(this) / cols;
        int height = image.getHeight(this) / rows;
        int x = width * col;
        int y = height * row;
        return new Rectangle(x, y, width, height);
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return true;
    }

    public Puzzle(Image image, int cols, int rows) {
        this.image = image;
        this.cols = cols;
        this.rows = rows;
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                items.add(new PuzzleItem(this, col, row));
            }
        }
    }

    public void paint(Graphics g, int xOffset, int yOffset) {
        g.drawImage(image, xOffset, yOffset, this);
        for (PuzzleItem item : items) {
            item.paint(g);
        }
    }
    
    public void change(){
        if (changeListener!=null){
            changeListener.stateChanged(new ChangeEvent(this));
        }
    }

    public void onClick(PuzzleItem item) {
        System.out.println(item);
        change();
        
    }
    
    public void onMove(PuzzleItem item,Point p){
        System.out.println("move "+item+" "+p);
        change();
    }

}

class Browser extends Base implements ChangeListener{

    int xOffset = 0;
    int yOffset = 0;
    Puzzle puzzle;
    PuzzleListener listener;

    @Override
    public void stateChanged(ChangeEvent e) {
        repaint();
    }

    

    public Browser(Puzzle puzzle) {
        this.puzzle = puzzle;
        puzzle.changeListener = this;
        
        listener = new PuzzleListener(puzzle);
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (puzzle != null) {
            puzzle.paint(g, xOffset, yOffset);
        }
    }

}

class Base extends Container implements WindowListener {

    JFrame frame;

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

    public Base() {
        super();
        setPreferredSize(new Dimension(800, 600));
    }

    public void showInFrame(Component parent) {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(this);
        frame.setContentPane(this);
        frame.pack();
        frame.setLocationRelativeTo(parent);
        frame.setVisible(true);
    }

}

class PuzzleImage extends BufferedImage {

    public PuzzleImage(int width, int height) {
        super(width, height, TYPE_INT_ARGB);
        Graphics g = getGraphics();
        g.setColor(Color.LIGHT_GRAY);
        for (int x = 0; x < width + height; x = x + 10) {
            g.drawLine(x - height, 0, x, height);
        }
    }

};

/**
 *
 * @author viljinsky
 */
public class App implements Runnable {

    Browser browser;

    @Override
    public void run() {

        Image image = new PuzzleImage(300, 200);
        Puzzle puzzle = new Puzzle(image, 10, 10);
        browser = new Browser(puzzle);
        browser.showInFrame(null);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new App());
    }

}
