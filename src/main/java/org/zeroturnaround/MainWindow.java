package org.zeroturnaround;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.border.Border;

public class MainWindow extends JFrame {
  public MainWindow() {
    setSize(400, 400);
    setTitle("Awesome Keytool");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    initMainPane();

    setVisible(true);
  }

  private void initMainPane() {
    JPanel mainPane = new JPanel();

    Border border = BorderFactory.createTitledBorder("Drag file");
    mainPane.setBorder(border);

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

    getContentPane().add(mainPane);
  }
}
