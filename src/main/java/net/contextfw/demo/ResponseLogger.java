package net.contextfw.demo;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.contextfw.web.application.util.XMLResponseLogger;

/**
 * Provides a simple logging facility for XML responses, so
 * that they are separated from default log. Feel free to modify
 * or remove this class.
 */
public class ResponseLogger implements XMLResponseLogger  {
    
    private JFrame frame;
    private JTextArea textArea;

    public ResponseLogger() {
        frame = new JFrame("XML Response Logger");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLocation(100, 100);
        frame.setSize(1000, 600);
        textArea = new JTextArea(10, 100);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.getContentPane().add(scrollPane);
        scrollPane.setPreferredSize(new Dimension(1000, 600));
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, 0, 10));
        textArea.setWrapStyleWord(false);
        textArea.setText("XML response logging will appear here");
        frame.setVisible(true);
        //frame.setState(Frame.ICONIFIED);
    }

    @Override
    public void logXML(String xml) {
        textArea.setText(xml);
    }
}