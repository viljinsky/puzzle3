/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.viljinsky.puzzle5;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class PuzzleListener3 extends MouseAdapter {

    Puzzle3 puzzle;
    PuzzleItem3 selected;

    public PuzzleListener3(Puzzle3 puzzle) {
        this.puzzle = puzzle;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (selected != null) {
            selected.x = e.getX();
            selected.y = e.getY();
            puzzle.change();
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        PuzzleItem3 item = puzzle.getItem(e.getPoint());
        System.out.println(e.getWheelRotation());
        if (item!=null){
            
            item.theta += Math.PI/8*e.getPreciseWheelRotation(); 
            puzzle.change();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (selected != null) {
            selected = null;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        selected = puzzle.getItem(e.getPoint());
        if (selected != null) {
            puzzle.remove(selected);
            puzzle.add(0, selected);
            puzzle.change();
        }
        System.out.println(selected);
    }

}

class PuzzleImage3 extends BufferedImage {

    private void init() {
        Graphics2D g = (Graphics2D) getGraphics();
        Paint paint = new GradientPaint(0, 0, Color.yellow, getWidth(), getHeight(), Color.black);
        g.setPaint(paint);
        g.fill(new Rectangle(0, 0, getWidth(), getHeight()));
    }

    public PuzzleImage3() {
        super(100, 200, TYPE_INT_ARGB);
        init();
    }
    
    public PuzzleImage3(int width, int height) {
        super(width+20, height+20, TYPE_INT_ARGB);
        init();
    }


}

class PuzzleItem3 {

    int x;
    int y;
    int col;
    int row;
    Puzzle3 puzzle;
    Color color = Color.BLUE;
    BufferedImage image;
    Shape shape;

    public PuzzleItem3(Puzzle3 puzzle, int col, int row) {
        this.puzzle = puzzle;
        this.col = col;
        this.row = row;
        this.x = col * puzzle.col_size + puzzle.col_size / 2;
        this.y = row * puzzle.row_size + puzzle.row_size / 2;
    }

    protected Rectangle bound() {
        return new Rectangle(x - puzzle.col_size / 2, y - puzzle.row_size / 2, puzzle.col_size, puzzle.row_size);
    }
    
    double theta = 0.0;

    public void paint(Graphics g, ImageObserver observer) {
        
        g.setColor(color);
        Rectangle bound = bound();
//        g.drawImage(image, bound.x, bound.y, observer);
        
        if (image != null) {
            
            Graphics2D g2 = (Graphics2D)g;
            AffineTransform old = g2.getTransform();
            AffineTransform at = AffineTransform.getRotateInstance(theta, x, y);
            at.translate(bound.x-10, bound.y-10);
            g2.drawImage(image, at, observer);
            g2.setTransform(old);
            
//            Shape old = g.getClip();
//            g.setClip(bound);
//            g.drawImage(image, bound.x-10, bound.y-10, observer);
//            g.setClip(old);
            
        } else {
            g.drawRect(bound.x, bound.y, bound.width, bound.height);
        }
    }

    @Override
    public String toString() {
        return String.format("item (%d %d)", col, row);
    }

}

/**
 *
 * @author viljinsky
 */
public class Puzzle3 extends ArrayList<PuzzleItem3> implements ImageObserver {

    int cols;
    int rows;
    int col_size = 40;
    int row_size = 30;

    BufferedImage puzzleImage;
    ChangeListener changeListener;
    Slicer slicer;

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return true;
    }

    public Puzzle3() {
        this(12, 17);
        puzzleImage = new PuzzleImage3(cols * col_size, rows * row_size);
    }

    public Puzzle3(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        puzzleImage = new PuzzleImage3(cols * col_size, rows * row_size);
        init();
    }
    
    public Puzzle3(BufferedImage image){
        this.puzzleImage = image;
        this.cols = (image.getWidth()-20) /col_size;
        this.rows = (image.getHeight()-20) /col_size;
        slicer = new Slicer(this);
        init();
    }

    private PuzzleItem3 createItem(int col, int row) {
        PuzzleItem3 item = new PuzzleItem3(this, col, row);
//        Shape shape = slicer.shape(col, row);
//        item.image = new BufferedImage(col_size+20, row_size+20, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g2 = (Graphics2D)item.image.getGraphics();
//        g2.setClip(shape);
//        AffineTransform at = AffineTransform.getTranslateInstance(-col*col_size, -row*row_size);
//        g2.drawImage(puzzleImage, at, this);
        
        
        item.image = puzzleImage.getSubimage(col * col_size, row * row_size, col_size+20, row_size+20);
        return item;
    }

    private void init() {

        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                add(createItem(col, row));
            }
        }
    }


    PuzzleItem3 getItem(Point point) {
        for (PuzzleItem3 item : this) {
            if (item.bound().contains(point)) {
                return item;
            }
        }
        return null;
    }

    
    public void change() {
        if (changeListener != null) {
            changeListener.stateChanged(new ChangeEvent(this));
        }
    }

    public void paint(Graphics g) {
        for (int i = size() - 1; i >= 0; i--) {
            get(i).paint(g, this);
        }
    }

    protected Dimension preferredSize() {
        return new Dimension(cols * col_size, rows * row_size);
    }

    JPanel pane;

    public void showInFrame(Component parent) {
        pane = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Puzzle3.this.paint(g);
            }

        };
        changeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                pane.repaint();
            }
        };
        PuzzleListener3 listener = new PuzzleListener3(this);
        pane.addMouseListener(listener);
        pane.addMouseMotionListener(listener);
        pane.addMouseWheelListener(listener);

        pane.setPreferredSize(preferredSize());
        JFrame frame = new JFrame("Puzzle v3.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(pane);
        frame.pack();
        frame.setLocationRelativeTo(parent);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Puzzle3().showInFrame(null);
    }

}
