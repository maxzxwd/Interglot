/*
 * Created by JFormDesigner on Mon Feb 27 18:03:53 YEKT 2017
 */

package org.spigotmc.interglot;

import java.awt.Container;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Gui extends JFrame {
    private final Logger logger = Logger.getLogger("Interglot");
    private final JFileChooser chooser = new JFileChooser();

    {
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(new FileNameExtensionFilter("Plugins (.jar)", "jar"));
        logger.setUseParentHandlers(false);
        logger.addHandler(new Handler() {
            @Override
            public void publish(LogRecord record) {
                logText.append(record.getMessage() + '\n');
                logText.setCaretPosition(logText.getText().length());
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() throws SecurityException {
            }
        });
    }


    public Gui() {
        initComponents();
    }

    private void inputFileMouseClicked(MouseEvent e) {
        if (chooser.showOpenDialog(inputFile) == JFileChooser.APPROVE_OPTION) {
            inputFile.setText(chooser.getSelectedFile().getPath());
        }
    }

    private void outputFileMouseClicked(MouseEvent e) {
        if (chooser.showOpenDialog(outputFile) == JFileChooser.APPROVE_OPTION) {
            outputFile.setText(chooser.getSelectedFile().getPath());
        }
    }

    private void transformButtonMouseClicked(MouseEvent e) {
        if (inputFile.getText().isEmpty() || outputFile.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please ensure both an input file and output file are selected", "No files", JOptionPane.ERROR_MESSAGE);
            return;
        }
        new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                App.process(new File(inputFile.getText()), outputFile.getText(), 'v' + minecraftVersion.getText().replace('.', '_'), logger, regexp.getText());
                return null;
            }
        }.execute();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        inputFile = new JTextField();
        outputFile = new JTextField();
        inputLabel = new JLabel();
        outputLabel = new JLabel();
        scrollPanel = new JScrollPane();
        logText = new JTextArea();
        transformButton = new JButton();
        copyright = new JLabel();
        libigot = new JLabel();
        jScrollPane1 = new JScrollPane();
        warning = new JTextArea();
        regexp = new JTextField();
        regexpLabel = new JLabel();
        versionLabel = new JLabel();
        minecraftVersion = new JTextField();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Interglot Jar Repackager");
        setResizable(false);
        Container contentPane = getContentPane();

        //---- inputFile ----
        inputFile.setEditable(false);
        inputFile.setFocusable(false);
        inputFile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                inputFileMouseClicked(e);
            }
        });

        //---- outputFile ----
        outputFile.setEditable(false);
        outputFile.setFocusable(false);
        outputFile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                outputFileMouseClicked(e);
            }
        });

        //---- inputLabel ----
        inputLabel.setText("Input File:");

        //---- outputLabel ----
        outputLabel.setText("Output File:");

        //======== scrollPanel ========
        {

            //---- logText ----
            logText.setEditable(false);
            logText.setColumns(20);
            logText.setLineWrap(true);
            logText.setRows(5);
            logText.setText("Welcome to Interglot. This program enables package renaming across plugins to quickly enable them to work with later CraftBukkit and Spigot versions. Simply select an input file and an output file Minecraft version, and then transform!\nTHIS SOFTWARE IS PROVIDED AS IS AND COMES WITH NO WARRANTY, EXPLICIT OR IMPLIED.\n\n");
            logText.setWrapStyleWord(true);
            logText.setMargin(new Insets(5, 5, 5, 5));
            scrollPanel.setViewportView(logText);
        }

        //---- transformButton ----
        transformButton.setText("Transform!");
        transformButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                transformButtonMouseClicked(e);
            }
        });

        //---- copyright ----
        copyright.setText("Copyright SpigotMC 2017");
        copyright.setAlignmentY(0.6F);

        //---- libigot ----
        libigot.setIcon(new ImageIcon(getClass().getResource("/libigot.png")));

        //======== jScrollPane1 ========
        {

            //---- warning ----
            warning.setEditable(false);
            warning.setColumns(20);
            warning.setFont(new Font("Ubuntu", Font.PLAIN, 11));
            warning.setLineWrap(true);
            warning.setRows(5);
            warning.setText("This tool is provided to update plugins which might not otherwise work on unmodified\nCraftBukkit builds. Use with caution.\n\nHere is a guy with his foot in his mouth to remind you.");
            warning.setWrapStyleWord(true);
            warning.setFocusable(false);
            jScrollPane1.setViewportView(warning);
        }

        //---- regexp ----
        regexp.setText("/v[\\d_]+.*?(?=/)");

        //---- regexpLabel ----
        regexpLabel.setText("Regexp:");

        //---- versionLabel ----
        versionLabel.setText("Minecraft Version:");

        //---- minecraftVersion ----
        minecraftVersion.setText("1_6_R3");

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                    .addComponent(copyright)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(transformButton))
                                .addComponent(scrollPanel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 740, GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                .addComponent(regexpLabel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                                .addComponent(regexp, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                                .addComponent(libigot, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGroup(contentPaneLayout.createParallelGroup()
                                .addComponent(outputLabel)
                                .addComponent(inputLabel, GroupLayout.Alignment.TRAILING))
                            .addGap(18, 18, 18)
                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                .addComponent(outputFile, GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
                                .addComponent(inputFile))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                            .addGroup(contentPaneLayout.createParallelGroup()
                                .addComponent(versionLabel, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)
                                .addComponent(minecraftVersion, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE))))
                    .addContainerGap(17, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(inputFile, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(inputLabel)
                        .addComponent(versionLabel))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(outputLabel)
                            .addComponent(outputFile, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addComponent(minecraftVersion, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addComponent(regexpLabel)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(regexp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(libigot)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jScrollPane1))
                        .addComponent(scrollPanel, GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(transformButton)
                        .addComponent(copyright))
                    .addContainerGap())
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JTextField inputFile;
    private JTextField outputFile;
    private JLabel inputLabel;
    private JLabel outputLabel;
    private JScrollPane scrollPanel;
    private JTextArea logText;
    private JButton transformButton;
    private JLabel copyright;
    private JLabel libigot;
    private JScrollPane jScrollPane1;
    private JTextArea warning;
    private JTextField regexp;
    private JLabel regexpLabel;
    private JLabel versionLabel;
    private JTextField minecraftVersion;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
