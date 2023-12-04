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
import java.awt.geom.GeneralPath;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;

class PuzzleSlicing extends ArrayList<Shape> {

    ArrayList<Point> points = new ArrayList<>();

    Point nextRight(Point point) {
        Point result = null;
        for (Point p : points) {
            if (p.y == point.y && p.x > point.x) {
                if (result == null || result.x > p.x) {
                    result = p;
                }
            }
        }
        return result;
    }

    Point nextDown(Point point) {
        Point result = null;
        for (Point p : points) {
            if (p.x == point.x && p.y > point.y) {
                if (result == null || result.y > p.y) {
                    result = p;
                }
            }
        }
        return result;
    }

    Shape horizontal(Point p1, Point p2, int ctrl) {

        int k = (int) Math.signum(0.5 - Math.random());
        int ctrlx = (p1.x + p2.x) / 2;
        int ctrly = p1.y + ctrl * k;
        return new QuadCurve2D.Float(p1.x, p1.y, ctrlx, ctrly, p2.x, p2.y);

    }

    Shape vertical(Point p1, Point p2, int ctrl) {
        int k = (int) Math.signum(0.5 - Math.random());
        int ctrlx = p1.x + ctrl * k;
        int ctrly = (p1.y + p2.y) / 2;
        return new QuadCurve2D.Float(p1.x, p1.y, ctrlx, ctrly, p2.x, p2.y);
    }

    public Shape shape(int col, int row) {
        Shape s;
        GeneralPath path = new GeneralPath();
        
        s = get((row*(cols) + col)*2);
        path.append(s, false);
        
        s = get((row*(cols) + col+1)*2+1);
        path.append(s, false);
        
      //  GeneralPath tmp = new GeneralPath();
       

        s = get(((row+1)*(cols) + col)*2);
       
        path.append(s, false);
        
        s = get((row*(cols) + col)*2+1);
        path.append(s, false);
        
        
        
        return path;
    }

    int cols;
    public PuzzleSlicing(Puzzle puzzle) {
        cols = puzzle.cols+1;
        for (int row = 0; row < puzzle.rows + 1; row++) {
            for (int col = 0; col < puzzle.cols + 1; col++) {
                Point p = new Point(col * Puzzle.COL_SIZE, row * Puzzle.ROW_SIZE);
                points.add(p);
                Point p1;
                p1 = new Point(p.x+Puzzle.COL_SIZE,p.y);
                add(horizontal(p, p1, (row == 0 || row == puzzle.rows) ? 0 : 10));                
                p1 = new Point(p.x,p.y+Puzzle.ROW_SIZE);
                add(vertical(p, p1, (col == 0 || col == puzzle.cols) ? 0 : 10));
            }
        }
    }

    public void paint(Graphics g) {
        for (Point p : points) {
            g.drawOval(p.x - 3, p.y - 3, 6, 6);
        }

        Graphics2D g2 = (Graphics2D) g;

        System.out.println("--");
        
        g2.setColor(Color.red);
        for(int i=0;i<size()/2;i++){
            System.out.println(i*2);
            g2.draw(get(i*2));
        }
        
        g2.setColor(Color.blue);
        for(int i=0;i<size()/2;i++){
            System.out.println(i*2+1);
            g2.draw(get(i*2+1));
        }

        System.out.println("--");

    }

}

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
    Image img;
    PuzzleSlicing slicing;

    public PuzzlePanel2() {

        img = new PuzzleImage2(400, 500);
        
        int cols = img.getWidth(null) / Puzzle.COL_SIZE;
        int rows = img.getHeight(null) / Puzzle.ROW_SIZE;
        
        puzzle = new Puzzle(cols, rows);
        slicing = new PuzzleSlicing(puzzle);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        g.drawImage(img, 0, 0, this);
        
        Graphics2D g2 = (Graphics2D)g;
        for(int col =0;col<puzzle.cols;col++){
            for(int row=0;row<puzzle.rows;row++){
                if(col==4 && row==6){
                g2.draw(slicing.shape(col, row));
                }
            }
        }
        
//        int col = 1;
//        int row = 1;
//        Graphics2D g2 = (Graphics2D)g;
//        g2.draw(slicing.shape(col, row));
        
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
