package org.zeroturnaround;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.TransferHandler;

public class MainWindow extends JFrame {

  public MainWindow() {
    setSize(400, 400);
    setTitle("Awesome Keytool 2");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    initGUI();

    setVisible(true);
  }

  private void initGUI() {
    JTabbedPane tabbedPane = new JTabbedPane();

    JComponent dragFilePane = initDragPane();
    tabbedPane.addTab("Drag File", dragFilePane);

    JComponent convertPane = initConvertPane();
    tabbedPane.addTab("Convert File", convertPane);

    JComponent prefPane = initPrefPane();
    tabbedPane.addTab("Preferences", prefPane);

    getContentPane().add(tabbedPane);
  }

  private JComponent initPrefPane() {
    JPanel panel = new JPanel();

    // 1st row
    GridBagLayout gbl = new GridBagLayout();
    GridBagConstraints gc = new GridBagConstraints();
    panel.setLayout(gbl);

    JLabel label = new JLabel("OpenSSL");
    gc.fill = GridBagConstraints.HORIZONTAL;
    gc.weightx = 1.0;
    gc.gridx = 0;
    gc.gridy = 0;
    gc.insets = new Insets(0, 20, 0, 0);
    panel.add(label, gc);

    JTextField textField = new JTextField(Settings.getOpenSSLExec());
    textField.setEnabled(false);
    gc.insets = new Insets(0, 0, 0, 20);
    gc.gridx = 1;
    gc.gridy = 0;
    panel.add(textField, gc);

    return panel;
  }

  private JComponent initConvertPane() {
    JPanel panel = new JPanel();

    // 1st row
    GridBagLayout gbl = new GridBagLayout();
    GridBagConstraints gc = new GridBagConstraints();
    panel.setLayout(gbl);

    JLabel label = new JLabel("Source");
    gc.fill = GridBagConstraints.HORIZONTAL;
    gc.weightx = 1.0;
    gc.gridx = 0;
    gc.gridy = 0;
    gc.insets = new Insets(0, 20, 0, 0);
    panel.add(label, gc);

    JTextField textField = new JTextField("/home/toomasr/tmp/private.pem");
    textField.setEnabled(false);
    gc.insets = new Insets(0, 0, 0, 20);
    gc.gridx = 1;
    gc.gridy = 0;
    panel.add(textField, gc);

    // 2nd row

    label = new JLabel("Type");
    gc.insets = new Insets(0, 20, 0, 0);
    gc.gridx = 0;
    gc.gridy = 1;
    panel.add(label, gc);

    textField = new JTextField("PEM x509 Certificate");
    textField.setEnabled(false);
    gc.insets = new Insets(0, 0, 0, 20);
    gc.gridx = 1;
    gc.gridy = 1;
    panel.add(textField, gc);

    // 3rd row

    label = new JLabel("Convert");
    gc.insets = new Insets(0, 20, 0, 0);
    gc.gridx = 0;
    gc.gridy = 2;
    panel.add(label, gc);

    String[] petStrings = { "Choose", "PEM" };
    JComboBox petList = new JComboBox(petStrings);
    gc.insets = new Insets(0, 0, 0, 20);
    gc.gridx = 1;
    gc.gridy = 2;
    panel.add(petList, gc);
    // //

    return panel;
  }

  private JComponent initDragPane() {
    JPanel mainPane = new JPanel();

    TransferHandler th = new TransferHandler() {

      @Override
      public boolean importData(TransferSupport support) {
        System.out.println("importData");
        Transferable t = support.getTransferable();

        try {
          java.util.List<File> l = (java.util.List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);

          for (File f : l) {
            System.out.println(f);
          }
        }
        catch (UnsupportedFlavorException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

        return true;
      }

      public boolean canImport(TransferHandler.TransferSupport support) {
        // we support importing all kinds of files
        return true;
      }

    };

    mainPane.setTransferHandler(th);

    mainPane.setLayout(new GridBagLayout());
    GridBagConstraints gc = new GridBagConstraints();

    JLabel label = new JLabel("Drag file");
    mainPane.add(label, gc);

    return mainPane;
  }
}
