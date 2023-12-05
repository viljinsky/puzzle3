/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.viljinsky.puzzle5;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author viljinsky
 */
public class Slicer extends ArrayList<Shape> {

    int cols;
    int rows;

    private Shape horizontal(Point p1, Point p2, int s) {
        int ctrlx = (p1.x + p2.x) / 2;
        int ctrly = (p1.y + p2.y) / 2 + 10 * s;
        return new QuadCurve2D.Float(p1.x, p1.y, ctrlx, ctrly, p2.x, p2.y);
    }

    private Shape vertical(Point p1, Point p2, int s) {
        int ctrlx = (p1.x + p2.x) / 2 + 10 * s;
        int ctrly = (p1.y + p2.y) / 2;
        return new QuadCurve2D.Float(p1.x, p1.y, ctrlx, ctrly, p2.x, p2.y);
    }

    public Shape shape(int col, int row) {
        int n;
        GeneralPath path = new GeneralPath();
        if (col % 2 + row % 2 == 1) {
            n = col * rows * 2 + row * 2;

            path.append(get(n), true);
            path.append(get(n + 1), true);

            n = col * rows * 2 + (row + 1) * 2;
            path.append(get(n), true);

            n = (col + 1) * rows * 2 + row * 2 + 1;
            path.append(get(n), true);

        } else {

            n = (col + 1) * rows * 2 + row * 2 + 1;
            path.append(get(n), true);

            n = col * rows * 2 + (row + 1) * 2;
            path.append(get(n), true);

            n = col * rows * 2 + row * 2;

            path.append(get(n + 1), true);
            path.append(get(n), true);

        }

        return path;
    }

    protected void paint(Graphics g) {
        
        Graphics2D g2 = (Graphics2D)g;
        for (int col = 0; col < cols - 1; col++) {
            for (int row = 0; row < rows - 1; row++) {
                Shape s = shape(col, row);
                g2.setColor(Color.red);
                g2.fill(s);
                g2.setColor(Color.blue);
                g2.draw(s);
            }
        }
    }
    
    Puzzle3 puzzle;
    
    public Slicer(Puzzle3 puzzle) {
        this.puzzle = puzzle;
        cols = puzzle.cols + 1;
        rows = puzzle.rows + 1;
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                Point p = new Point(col * puzzle.col_size, row * puzzle.row_size);
                Point p1 = new Point(p.x + puzzle.col_size, p.y);
                Point p2 = new Point(p.x, p.y + puzzle.row_size);

                int s1 = (int) (Math.signum(0.5 - Math.random()));
                int s2 = (int) (Math.signum(0.5 - Math.random()));

                if (row % 2 + col % 2 == 1) {
                    add(horizontal(p1, p, (row == 0 || row == puzzle.rows) ? 0 : s1));
                    add(vertical(p, p2, col == 0 || col == puzzle.cols ? 0 : s2));
                } else {
                    add(horizontal(p, p1, (row == 0 || row == puzzle.rows) ? 0 : s1));
                    add(vertical(p2, p, col == 0 || col == puzzle.cols ? 0 : s2));
                }
            }
        }
    }

    public void showInFrame(Component parent) {
        JPanel panel = new JPanel(){
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Slicer.this.paint(g);
            }
            
        };
        panel.setPreferredSize(new Dimension((cols-1)*puzzle.col_size, (rows-1)*puzzle.row_size));
        JFrame frame = new JFrame("Slicer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.pack();
                
        frame.setLocationRelativeTo(parent);
        frame.setVisible(true);

    }

    public static void main(String[] args) {

        Puzzle3 puzzle = new Puzzle3(12, 17);
        Slicer graph = new Slicer(puzzle);
        graph.showInFrame(null);

    }


}
