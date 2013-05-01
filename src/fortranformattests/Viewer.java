/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fortranformattests;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author sheldon.chi
 */
public class Viewer extends JPanel {

    Graphics g;
    ModelPanel panel;
    JTextField tf;
    JButton upButton, downButton;

    /**
     *
     * @param imagePanel
     */
    public Viewer() {
        tf = new JTextField(5);
        upButton = new JButton("Up");
        downButton = new JButton("Down");

    }

    public Viewer(ModelPanel imagePanel) {
        
        this();
        this.panel = imagePanel;


        JPanel cp = new JPanel();
        cp.setLayout(new FlowLayout());
        cp.add(upButton);
        cp.add(downButton);

       

        cp.add(tf);

        String[] comboboxText = {"IBOUND", "K"};
        JComboBox cb = new JComboBox(comboboxText);

        downButton.addActionListener(
                new ActionListener() {
            //========================================= listener
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setLayer(panel.getLayer() + 1);
                tf.setText(Integer.toString(panel.getLayer()));

            }//end listener
        });

        upButton.addActionListener(
                new ActionListener() {
            //========================================= listener
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setLayer(panel.getLayer() - 1);
                tf.setText(Integer.toString(panel.getLayer()));

            }//end listener
        });
      

        tf.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                panel.setLayer(Integer.parseInt(tf.getText()));
            }

            public void removeUpdate(DocumentEvent e) {
          //      warn();
            }

            public void insertUpdate(DocumentEvent e) {
                panel.setLayer(Integer.parseInt(tf.getText()));
            }
        });

        this.setLayout(new BorderLayout());
        this.add(cp, BorderLayout.NORTH);
        this.add(imagePanel, BorderLayout.CENTER);



    }
    
    public void updateUI(ModelPanel imagePanel) {
        this.removeAll();

        this.panel = imagePanel;


        JPanel cp = new JPanel();
        cp.setLayout(new FlowLayout());
        cp.add(upButton);
        cp.add(downButton);



        cp.add(tf);

        String[] comboboxText = {"IBOUND", "K"};
        JComboBox cb = new JComboBox(comboboxText);

        downButton.addActionListener(
                new ActionListener() {
            //========================================= listener
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setLayer(panel.getLayer() + 1);
                tf.setText(Integer.toString(panel.getLayer()));

            }//end listener
        });

        upButton.addActionListener(
                new ActionListener() {
            //========================================= listener
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setLayer(panel.getLayer() - 1);
                tf.setText(Integer.toString(panel.getLayer()));

            }//end listener
        });


        tf.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                panel.setLayer(Integer.parseInt(tf.getText()));
            }

            public void removeUpdate(DocumentEvent e) {
                //      warn();
            }

            public void insertUpdate(DocumentEvent e) {
                panel.setLayer(Integer.parseInt(tf.getText()));
            }
        });

        this.setLayout(new BorderLayout());
        this.add(cp, BorderLayout.NORTH);
        this.add(imagePanel, BorderLayout.CENTER);

        

    }

    public void setLayer(int lay)
    {
        this.tf.setText(Integer.toString(lay));
    }
}
