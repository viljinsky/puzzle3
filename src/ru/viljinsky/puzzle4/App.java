/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.viljinsky.puzzle4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import javax.swing.JFrame;

class PuzzleImage extends BufferedImage {

    private void init() {
        Graphics g = getGraphics();
        g.setColor(Color.LIGHT_GRAY);
        int width = getWidth();
        int height = getHeight();
        for (int x = 0; x < width+height; x = x + 10) {
            g.drawLine(x - height, 0, x, height);
        }
    }

    public PuzzleImage(int width, int height) {
        super(width, height, TYPE_INT_ARGB);
        init();
    }

}

class PuzzleItem {
    
    
    Rectangle bound;
    Shape shape;
    
    public void paint(Graphics g){
        if (bound!=null){
            g.setColor(Color.red);
            g.drawRect(bound.x, bound.y, bound.width,bound.height);
        }
        if(shape!=null){
            Graphics2D g2 = (Graphics2D)g;
            g2.draw(shape);
        }
    }
}

class Puzzle implements ImageObserver {

    int columnCount;
    int rowCount;
    
    Image image;
    
    ArrayList<PuzzleItem> items = new ArrayList<>();

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return true;
    }

    public Puzzle() {
        image = new PuzzleImage(800, 1200);
        columnCount = 30;
        rowCount = 40;
        int puzzleWidth = image.getWidth(this)/columnCount;
        int puzzleHeight = image.getHeight(this)/rowCount;
        for(int col=0;col<columnCount;col++){
            for(int row=0;row<rowCount;row++){
                PuzzleItem item =new PuzzleItem();
                item.bound = new Rectangle(col*puzzleWidth,row*puzzleHeight,puzzleWidth,puzzleHeight);
                int dx = (int)Math.signum(.5-Math.random());
                int dy = (int)Math.signum(.5-Math.random());
                item.shape = new Rectangle(item.bound.x +dx,item.bound.y+dy,item.bound.width,item.bound.height);
                
                items.add(item);
            }
        }
        
    }

    public void paint(Graphics g) {
        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }
        
        for(PuzzleItem item:items){
            item.paint(g);
        }
    }
}

class Browser extends Base {

    Puzzle puzzle;

    public Browser() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));
        puzzle = new Puzzle();
    }

    @Override
    public void paint(Graphics g) {
        if (puzzle != null) {
            puzzle.paint(g);
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

    public void showInFrame(Component parent) {
        if (frame == null) {
            frame = new JFrame();
            frame.addWindowListener(this);
            frame.setContentPane(this);
            frame.pack();
            frame.setLocationRelativeTo(parent);
            frame.setVisible(true);
        }
    }

}

/**
 *
 * @author viljinsky
 */
public class App implements Runnable {

    @Override
    public void run() {
        Browser browser = new Browser();
        browser.showInFrame(null);
    }

    public static void main(String[] args) {
        
        for(int i=0;i<100;i++){
            
            System.out.println(i+" "+(int)(Math.random()*10)+" "+ (int)(Math.signum(.5-Math.random())*Math.random()*3));
        }
        new App().run();
    }

}
