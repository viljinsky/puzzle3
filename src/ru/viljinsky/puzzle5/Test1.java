/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.viljinsky.puzzle5;

/**
 *
 * @author viljinsky
 */
public class Test1 implements Runnable{
    
    Browser browser;

    @Override
    public void run() {
        browser = new Browser();
        browser.showInFrame(null);        
    }
    
    public static void main(String[] args) {
        new Test1().run();
    }
    
}
