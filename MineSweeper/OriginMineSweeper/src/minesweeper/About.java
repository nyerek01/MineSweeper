package minesweeper;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import static minesweeper.Interface.miHightScore;
import static minesweeper.Interface.strLevelTomb;
import org.xml.sax.SAXException;

class About extends JDialog implements ActionListener, KeyListener {

  private static short mine = 0;

  static short getMine() {
    return mine;
  }

  public static void setMine(short mine) {
    About.mine = mine;
  }
  private JTextField txtColumsNumber = new JTextField(), txtRowsNumber = new JTextField(), txtMines = new JTextField(), txtPlayerName = new JTextField("Anonymus");
  private JLabel lbColums = new JLabel("oszlopok"), lbRows = new JLabel("sorok"), lbMines = new JLabel("aknák");
  private JButton btYourNameOK = new JButton("OK"), btCustomOK = new JButton("OK"), btHightScore = new JButton("OK"), btCustomCancel = new JButton("Mégse"), btClear = new JButton("Töröl");
  private Object[] o = new Object[]{lbColums, txtColumsNumber, lbRows, txtRowsNumber, lbMines, txtMines, btCustomOK, btCustomCancel};

  About(JRootPane tulajdonos, boolean modal, String s) {
    setResizable(false);
    setTitle(s);
    setVisible(true);
    if ("Add meg a neved".equals(s)) {
      setBounds(200, 200, 235, 125);
      JPanel pn1, pn2, pn0;
      pn0 = new JPanel();
      pn1 = new JPanel();
      pn2 = new JPanel();
      pn0.add(new JLabel(msServer.gui.getPlayerLevel() + " szinten te voltál a leggyorsabb."));
      pn1.add(new JLabel("Írd be a neved: "));
      txtPlayerName.setSelectionStart(0);
      txtPlayerName.setSelectionEnd(txtPlayerName.getText().length());
      pn1.add(txtPlayerName);
      btYourNameOK.addActionListener(this);
      pn2.add(btYourNameOK);
      add(pn0, BorderLayout.PAGE_START);
      add(pn1, BorderLayout.CENTER);
      add(pn2, BorderLayout.PAGE_END);
    } else if ("Egyéni".equals(s)) {
      setBounds(200, 200, 175, 100);
      setLayout(new GridLayout(4, 2));
      for (Object ob : o) {
        add((Component) ob);
      }
      btCustomOK.addActionListener(this);
      btCustomCancel.addActionListener(this);
    } else if ("Legjobbak".equals(s)) {
      setBounds(200, 200, 200, 125);
      JPanel pn1 = new JPanel();
      JPanel pn2 = new JPanel();
      msServer.logic.getLPlayersName().setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
      pn1.add(msServer.logic.getLPlayersName());
      btHightScore.addActionListener(this);
      btClear.addActionListener(this);
      pn2.add(btHightScore);
      pn2.add(btClear);
      add(pn1, BorderLayout.NORTH);
      add(pn2, BorderLayout.SOUTH);
    } else {
      setBounds(200, 200, 200, 100);
      setLayout(new GridLayout(1, 1));
      add(new JLabel("<html>Aknakereső Játék.<br/>Verzió: 1.1<br/>Készítette: Dóra Dávid<br/>2013.</html>"));
      addKeyListener(this);
    }
  }

  @Override
  public void keyPressed(KeyEvent kl) {
    dispose();
  }

  @Override
  public void actionPerformed(ActionEvent al) {
    if (al.getSource().equals(btCustomOK)) {
      try {
        byte row = (byte) (Byte.parseByte(txtRowsNumber.getText()) + 2);
        byte col = (byte) (Byte.parseByte(txtColumsNumber.getText()) + 2);
        mine = (Short.parseShort(txtMines.getText()));
        if (row >= 4 && col >= 4 && mine > 2 && mine < (row - 2) * (col - 2)) {
          String s;
          if (row * col < 17) {
            s = strLevelTomb[0];//Easy
          } else if (row * col < 101) {
            s = strLevelTomb[1];//Medium
          } else if (row * col < 226) {
            s = strLevelTomb[2];//Hard
          } else {
            s = strLevelTomb[3];//Custom
          }
          msServer.gui.remove(row, col, s);
          dispose();
        } else {
          if (row < 2 || col < 2 || mine < 3) {
            JOptionPane.showMessageDialog(rootPane, "Nagyobb értékeket írjon be'", "Érvénytelen számok", -1);
          } else {
            JOptionPane.showMessageDialog(rootPane, "Kisebb értékeket írjon be'", "Érvénytelen számok", -1);
          }
        }
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(rootPane, "Számokat írjon be.", "NumberFormetException", -1);
      }
    } else if (al.getSource().equals(btCustomCancel) || al.getSource().equals(btHightScore)) {
      dispose();
    } else if (al.getSource().equals(btYourNameOK)) {
      String name = txtPlayerName.getText().trim();
      msServer.logic.setPlayerName(name);
      byte b = (byte) name.length();
      if (b > 0 == b < 11) {
        dispose();
        try {
//          msServer.logic.ModifXMLFile("c:\\x.xml");
          msServer.logic.ModifXMLFile("./src/xml/x.xml");
          msServer.gui.logicInit();
          About about = new About(rootPane, true, miHightScore.getText());
        } catch (TransformerConfigurationException ex) {
        } catch (TransformerException ex) {
        } catch (ParserConfigurationException | SAXException | IOException ex) {
        }
      } else {
        if (b == 0) {
          JOptionPane.showMessageDialog(rootPane, "<html>Nem megfelelő névhosszúság,<br/>kérem töltse ki a név mezőt!</html>", "Név nincs megadva!", -1);
        } else {
          JOptionPane.showMessageDialog(rootPane, "<html>Nem megfelelő névhosszúság,<br/>a név legyen 11 karakternél kevesebb!</html>", "Túl hosszú név!", -1);
        }
      }
    } else if (al.getSource().equals(btClear)) {
//      msServer.logic.writeXMLFile("c:\\x.xml");
      msServer.logic.writeXMLFile("./src/xml/x.xml");
      msServer.gui.logicInit();
      dispose();
      About about = new About(rootPane, true, miHightScore.getText());
    }
  }

  @Override
  public void keyTyped(KeyEvent kt) {
  }

  @Override
  public void keyReleased(KeyEvent kl) {
  }
}
