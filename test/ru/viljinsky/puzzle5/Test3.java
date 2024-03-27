/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.viljinsky.puzzle5;

import java.awt.Graphics;

/**
 *
 * @author viljinsky
 */
public class Test3 extends Base implements Runnable{
    
    Puzzle puzzle;

    public Test3() {
        super("test3");
    }

    @Override
    public void run() {
        puzzle = new Puzzle();
        showInFrame(null);
        
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        puzzle.paint(g);
    }

    
    
    
    public static void main(String[] args) {
        new Test3().run();
    }
    
}
