/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.viljinsky.puzzle5;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author viljinsky
 */
public class StatusBar extends JPanel{
    JLabel label = new JLabel("StatusBar");

    public StatusBar() {
        setLayout(new BorderLayout(1,1));
        add(label);
    }
    
    
}
