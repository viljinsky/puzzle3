/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.viljinsky.puzzle5;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
//    public static int colSize = 30;
//    public static int rowSize = 40;
    double theta = .0;
    BufferedImage image;// = new PuzzleImage(colSize,rowSize);

    public PuzzleItem(int col, int row) {
        this.col = col;
        this.row = row;
        setCenter(col * Puzzle.COL_SIZE + Puzzle.COL_SIZE / 2, row * Puzzle.ROW_SIZE + Puzzle.ROW_SIZE / 2);
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        //      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        AffineTransform old = g2.getTransform();

        AffineTransform transform = AffineTransform.getRotateInstance(theta, x, y);
        g2.setTransform(transform);

        if (image != null) {
            g2.drawImage(image, x - Puzzle.COL_SIZE / 2, y - Puzzle.ROW_SIZE / 2, null);
        }
        g2.setColor(Color.red);
        g2.draw(shape);
        g2.drawOval(x - 2, y - 2, 4, 4);

        g2.setTransform(old);
    }

    public void setCenter(int x, int y) {
        this.x = x;
        this.y = y;
        shape = new Rectangle(x - (Puzzle.COL_SIZE / 2), y - (Puzzle.ROW_SIZE / 2), Puzzle.COL_SIZE, Puzzle.ROW_SIZE);
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

/**
 *
 * @author viljinsky
 */
class Puzzle {

    public static final int COL_SIZE = 30;
    public static final int ROW_SIZE = 40;

    int cols = 30;
    int rows = 40;
    ChangeListener changeListener;
    ArrayList<PuzzleItem> list = new ArrayList<>();
    BufferedImage image = new PuzzleImage(400, 500);

    public Puzzle() {
        this(30, 40);
    }

    public Puzzle(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                PuzzleItem item = new PuzzleItem(col, row);
                if ((row + 1) * Puzzle.ROW_SIZE < image.getHeight() && (col + 1) * Puzzle.COL_SIZE < image.getWidth()) {
                    //                    item.image = image.getSubimage(col*item.colSize,row*item.rowSize, item.colSize,item.rowSize);
                    item.image = image.getSubimage(col * Puzzle.COL_SIZE, row * Puzzle.ROW_SIZE, Puzzle.COL_SIZE, Puzzle.ROW_SIZE);
                }
                list.add(item);
            }
        }
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

    public Dimension prefferedSize() {
        return new Dimension(800, 600);
    }

}
