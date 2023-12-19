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
    Puzzle3 puzzle3;
    Slicer slicer;
    PuzzleListener3 listener3;

    @Override
    public void stateChanged(ChangeEvent e) {
        repaint();
    }
    
    

    public Test1() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800,600));
        try{
            image = /*new PuzzleImage3(800, 600) ;*/ImageIO.read(new File("background.jpg"));
            puzzle3 = new Puzzle3(image);
            slicer = new Slicer(puzzle3);
            listener3 = new PuzzleListener3(puzzle3);
            addMouseListener(listener3);
            addMouseMotionListener(listener3);
            addMouseWheelListener(listener3);
            puzzle3.changeListener = this;
            
            
            
            for(PuzzleItem3 item:puzzle3){
                item.image = new BufferedImage(puzzle3.col_size+20, puzzle3.row_size+20, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = (Graphics2D)item.image.getGraphics();
                AffineTransform at = AffineTransform.getTranslateInstance(-item.col*puzzle3.col_size+10,-item.row*puzzle3.row_size+10);
                Shape shape = slicer.shape(item.col, item.row);
                GeneralPath path = new GeneralPath(shape);
                path.transform(at);
                g.setClip(path);
                g.drawImage(image,at,this);
                g.setColor(Color.red);
                g.draw(path);
//                g.fillRect(0,0, puzzle3.col_size,puzzle3.row_size);
            }
            
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        puzzle3.paint(g);

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
