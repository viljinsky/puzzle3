/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.viljinsky.puzzle5;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author viljinsky
 */
public class Test1 extends JPanel implements ChangeListener{
    
    BufferedImage image;        
    Puzzle puzzle;
    Slicer slicer;
    PuzzleListener listener;

    @Override
    public void stateChanged(ChangeEvent e) {
        repaint();
    }
    
    

    public Test1() {
        setLayout(new BorderLayout());
        add(new StatusBar(),BorderLayout.PAGE_END);
        setPreferredSize(new Dimension(800,600));
        try{
            image = /*new PuzzleImage3(800, 600) ;*/ImageIO.read(new File("background.jpg"));
            puzzle = new Puzzle(image);
            slicer = new Slicer(puzzle);
            listener = new PuzzleListener(puzzle);
            addMouseListener(listener);
            addMouseMotionListener(listener);
            addMouseWheelListener(listener);
            puzzle.changeListener = Test1.this;
            
            
            
            for(PuzzleItem item:puzzle){
                item.image = new BufferedImage(puzzle.col_size+20, puzzle.row_size+20, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = (Graphics2D)item.image.getGraphics();
                AffineTransform at = AffineTransform.getTranslateInstance(-item.col*puzzle.col_size+10,-item.row*puzzle.row_size+10);
                Shape shape = slicer.shape(item.col, item.row);
                GeneralPath path = new GeneralPath(shape);
                path.transform(at);
                g.setClip(path);
                g.drawImage(image,at,Test1.this);
                g.setColor(Color.red);
                g.draw(path);
            }
            
        } catch (IOException e){
            System.err.println("IOException :"+e.getMessage());
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        puzzle.paint(g);

//        int col = 3;
//        int row = 5;
//        Graphics2D g2 = (Graphics2D)g;
//        Shape shape = slicer.shape(col, row);  
//        
//        
//        AffineTransform at = AffineTransform.getTranslateInstance(-col*puzzle3.col_size,-row*puzzle3.row_size);
//        GeneralPath p = new GeneralPath(shape);
//        p.transform(at);
////        g2.draw(shape);
//        g2.setClip(p);
//        g2.drawImage(image, at, this);
//        
//        
//  
////        for(PuzzleItem3 item: puzzle3){
////            item.paint(g, this);
////        }
////        puzzle3.paint(g);
////        slicer.paint(g);
////        g.drawImage(image, 0,0,this);
        
    }
    
    
    
    
    
    public static void main(String[] args) {
        new Test1().showInFrame(null);
    }

    private void showInFrame(Component parent) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(this);
        frame.pack();
        frame.setLocationRelativeTo(parent);
        frame.setVisible(true);
        
    }
    
}
