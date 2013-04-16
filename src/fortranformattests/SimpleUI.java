/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fortranformattests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author sheldon.chi
 */
public class SimpleUI extends javax.swing.JFrame {

    String path, name;
    Thread watcherThread;
    int j,i,k;


    
    /**
     * Creates new form SimpleUI
     */
    public SimpleUI() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        startButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Select");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jTextField1.setEditable(false);

        startButton.setText("Start logging");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jTextField1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addGap(42, 42, 42))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 529, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(startButton)
                .addGap(232, 232, 232))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(startButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>                        

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:

        JFileChooser fc = new JFileChooser("Select vmf file");
        fc.setFileFilter(new SimpleUI.vmF_Filter());
        int returnVal = fc.showDialog(null, "Open");


        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String fileName = fc.getSelectedFile().getPath();
            this.path = fc.getSelectedFile().getParent() + File.separator; // directory
            jTextField1.setText(fileName);
            System.out.println(path);
            name = fc.getSelectedFile().getName().split("\\.")[0];
            System.out.println(name);
        }

    }                                        

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {                                            
      
        if (path == null || path.length() == 0) {
            JOptionPane.showMessageDialog(null, "Select a project first");
        } else {                                  
            getDimensionFromDIS(path + File.separator + name + ".DIS"); // get j i k
            updateProgress("Logging is started... reading BAS file");
            try {
                Files.copy(Paths.get(path+File.separator+name+".BAS"), Paths.get(path+File.separator+name+".BAS"+"_Original"), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                Logger.getLogger(SimpleUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            watcherThread = new Thread() {
                Path dir;
                
                @Override
                public void run() {
                    dir = Paths.get(path);
                    WatchDir wd;
                    try {

                        String BAS = path + File.separator + name + ".BAS";
                        Model model = new Model(SimpleUI.this.j, SimpleUI.this.i, SimpleUI.this.k);
                        model.readBAS(BAS, true);
                        model.setBAS(BAS);
                        wd = new WatchDir(dir, false, SimpleUI.this); //non-recusive\
                        wd.setModel(model);
                        wd.processEvents();
                        
                    } catch (IOException ex) {
                        Logger.getLogger(WatchDir.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };

            watcherThread.start();
        }
    }                                           

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SimpleUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SimpleUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SimpleUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SimpleUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SimpleUI().setVisible(true);
            }
        });
    }

    public void updateProgress(final String text) {
                
        java.awt.EventQueue.invokeLater(new Runnable() {

          
            @Override
            public void run() {
                jTextArea1.append("\n"+text);
            }
        });
    }
    
    private void getDimensionFromDIS(String dis)
    {
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(dis));
            // skip #'s
            in.readLine();
            in.readLine();
            
            Pattern pattern = Pattern.compile("[\t ]+");
            String[] splitarray = pattern.split(in.readLine().trim()); // trimmed
            this.k = Integer.parseInt(splitarray[0]);
            this.i = Integer.parseInt(splitarray[1]);
            this.j = Integer.parseInt(splitarray[2]);
            in.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(SimpleUI.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Cannot find DIS file"); 
        } catch (IOException ex) {
            Logger.getLogger(SimpleUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    class vmF_Filter extends javax.swing.filechooser.FileFilter {

        @Override
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".vmf");
        }

        @Override
        public String getDescription() {
            return "vmf files";
        }
    }
    
    public void saveBASOriginal()
    {
        try {
            Files.copy(Paths.get(path+File.separator+name+".BAS"), Paths.get(path+File.separator+name+".BAS"+"_Original"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(SimpleUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // Variables declaration - do not modify                     
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton startButton;
    // End of variables declaration                   
}
