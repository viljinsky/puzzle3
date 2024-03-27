/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.viljinsky.puzzle5;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JPanel;

/**
 *
 * @author viljinsky
 */
public class Test2 extends Base implements Runnable{
    
    Puzzle puzzle;
    Slicer slicer;
    
    @Override
    public void run() {
        showInFrame(null);        
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        System.out.println("paint");
        for(PuzzleItem item: puzzle){
            Rectangle r = item.bound();
            g.drawRect(r.x,r.y,r.width,r.height);
//            item.paint(g, null);
        }
    }
    
    

    public Test2() {
        super("Test2");
        puzzle = new Puzzle();
//        slicer = new Slicer(puzzle);
        setPreferredSize(puzzle.preferredSize());
    }

    public static void main(String[] args) {
       new Test2().run();
    }

    
    
}
