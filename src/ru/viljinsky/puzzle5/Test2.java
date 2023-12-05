/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.viljinsky.puzzle5;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

class PuzzleImage2 extends BufferedImage {

    private void init() {
        Graphics2D g = (Graphics2D) getGraphics();
        Point p1 = new Point(0, 0);
        Point p2 = new Point(getWidth(), 0);
        GradientPaint paint = new GradientPaint(p1, Color.yellow, p2, Color.black);
        g.setPaint(paint);
        g.fill(new Rectangle(0, 0, getWidth(), getHeight()));
    }

    public PuzzleImage2(int width, int height) {
        super(width, height, TYPE_INT_ARGB);
        init();
    }

}

class PuzzlePanel2 extends JPanel {

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
        return true;
    }

    Puzzle puzzle;
    BufferedImage img;
    Slicer slicer;

    public PuzzlePanel2() {

        img = new PuzzleImage2(400, 500);

        int cols = img.getWidth(null) / Puzzle.COL_SIZE;
        int rows = img.getHeight(null) / Puzzle.ROW_SIZE;

        puzzle = new Puzzle(cols, rows);
        slicer = new Slicer(puzzle);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        

        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(img, null, this);
        AffineTransform old = g2.getTransform();
        for (int col = 0; col < puzzle.cols; col++) {
            for (int row = 0; row < puzzle.rows; row++) {

                Shape shape = slicer.shape(col, row);

                int tx = col * Puzzle.COL_SIZE + 20;
                int ty = row * Puzzle.ROW_SIZE + 20;

                AffineTransform tr = AffineTransform.getTranslateInstance(tx, ty);

//                g2.setTransform(tr);

            BufferedImage o =img.getSubimage(col*Puzzle.COL_SIZE, row*Puzzle.ROW_SIZE, Puzzle.COL_SIZE, Puzzle.ROW_SIZE);
            g2.drawImage(o, null, 10, 10);

                

            }
        }
        g2.setTransform(old);
        

    }

}

/**
 *
 * @author viljinsky
 */
public class Test2 extends Base implements Runnable {

    PuzzlePanel2 panel = new PuzzlePanel2();

    @Override
    public void run() {
        showInFrame(null);
    }

    public Test2() {
        super("Test2");
        setLayout(new BorderLayout());
        add(panel);
        setPreferredSize(new Dimension(800, 600));
    }

    public static void main(String[] args) {
        new Test2().run();
    }

}
