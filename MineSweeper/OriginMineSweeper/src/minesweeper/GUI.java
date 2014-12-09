package minesweeper;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import static minesweeper.Interface.*;

class GUI extends JFrame implements MouseListener, ActionListener {

  private boolean first, win;
  private String playerLevel;
  short time, MinesNumber, btSum, windowsWidth = (short) (pic.width / 2);
  private byte stepNo, rows, cols, playerID;
  private JButton[][] btTomb;
  private JLabel[][] lbTomb;
  private Timer stopper;

  GUI(){
    rows = cols = 8;
    miHard.setForeground(Color.RED);
    miEasy.setForeground(Color.GREEN);
    miMedium.setForeground(Color.BLUE);
    miMegold.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
    stopper = new Timer(1000, this);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle(text);
    setBounds((pic.width - windowsWidth) / 2, (pic.height - windowsWidth) / 2,
            windowsWidth, windowsWidth);
    setResizable(false);
    setJMenuBar(mbMenuBar);
    mbMenuBar.add(mFile);
    mbMenuBar.add(mHelp);
    byte i = 0;
    for (JMenuItem jMenuItem : miTomb) {
      if (i < 4) {
        miNewGame.add(jMenuItem);
        if (i == 2) {
          miNewGame.addSeparator();
        }
      } else if (i < 8) {
        mFile.add(jMenuItem);
        if (i == 6) {
          mFile.addSeparator();
        }
      } else {
        mHelp.add(jMenuItem);
      }
      jMenuItem.setToolTipText(tool[i]);
      jMenuItem.addActionListener(this);
      i++;
    }
    newgame(rows, cols, strLevelTomb[1]);
    setVisible(true);
  }

  private void newgame(byte r, byte c, String s) {
    MinesNumber = time = stepNo = 0;
    stopper.stop();
    win = first = true;
    playerLevel = s;
    setLayout(new GridLayout(r - 2, c - 2));
    btTomb = new JButton[r][c];
    lbTomb = new JLabel[r][c];
    btSum = (short) ((r - 2) * (c - 2));
    boolean b;
    int rc = r * c;
    double d = (1.0 * About.getMine() / btSum == 0) ? 0.2 : (1.0 * About.getMine() / btSum);
    while (MinesNumber < 3) {
      for (byte i = 0; i < r; i++) {
        for (byte j = 0; j < c; j++) {
          b = (i > 0 && j > 0 && i < r - 1 && j < c - 1);
          if (rc > 0) {
            JButton bt = new JButton();
            btTomb[i][j] = bt;
            if (b) {
              bt.addActionListener(this);
              bt.addMouseListener(this);
              add(bt);
            }
            rc--;
          }
          if (btTomb[i][j].getText() == null || btTomb[i][j].getText() == "") {
            JLabel lb = new JLabel("");
            if (b) {
              if (MinesNumber < About.getMine() || About.getMine() == 0) {
                lb.setText(Math.random() < d ? "B" : "");
              } else {
                lb.setText("");
              }
              lbTomb[i][j] = lb;
//              btTomb[i][j].setText(lb.getText());//Show Mines
              if (lbTomb[i][j].getText().equals("B")) {
                MinesNumber++;
              }
            } else {
              lbTomb[i][j] = lb;
            }
          }
        }
      }
    }
    setTitle(String.format("%-62s %-22s %1s", text, "0:00", "Aknák: " + MinesNumber));
    setVisible(true);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (e.getButton() == MouseEvent.BUTTON3) {
      for (byte i = 1; i < rows - 1; i++) {
        for (byte j = 1; j < cols - 1; j++) {
          if (e.getSource() == btTomb[i][j] && btTomb[i][j].isEnabled()) {
            btTomb[i][j].setIcon(btTomb[i][j].getIcon() != imgFlag ? imgFlag : null);
          }
        }
      }
    } else if (first) {
      stopper.restart();
      first = false;
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == stopper) {
      stopperStep();
    } else {
      for (byte i = 1; i < rows - 1; i++) {
        for (byte j = 1; j < cols - 1; j++) {
          if (e.getSource() == btTomb[i][j] && btTomb[i][j].isEnabled()) {
            stepNo++;
            if (!((imgFlag.equals(btTomb[i][j].getIcon())) || (lbTomb[i][j].getText().equals("B")))) {
              btTomb[i][j].setEnabled(false);
              expand(i, j);
            } else if ((btTomb[i][j].getIcon()) == imgFlag) {
              btTomb[i][j].setIcon(null);
            } else {
              btTomb[i][j].setBackground(Color.red);
              megold();
              game("lose");
            }
          }
        }
      }
      if (e.getSource() == miRestart) {
        remove(rows, cols, playerLevel);
      } else if (e.getSource() == miMegold) {
        megold();
      } //Teszteles miatt!
      else if (e.getSource() == miMedium) {
        About.setMine((short) 0);
        remove((byte) 10, (byte) 10, strLevelTomb[1]);
      } else if (e.getSource() == miEasy) {
        About.setMine((short) 0);
        remove((byte) 6, (byte) 6, strLevelTomb[0]);
      } else if (e.getSource() == miHard) {
        About.setMine((short) 0);
        remove((byte) 14, (byte) 14, strLevelTomb[2]);
      } else if (e.getSource() == miCustom) {
        About about = new About(rootPane, true, miCustom.getText());
      } else if (e.getSource() == miHightScore) {
        logicInit();
        About about = new About(rootPane, true, miHightScore.getText());
      } else if (e.getSource() == miAbout) {
        About about = new About(rootPane, true, miAbout.getText());
      } else if (e.getSource() == miExit) {
        System.exit(0);
      } else {
      }
    }
  }

  private void stopperStep() {
    time++;
    int min = time / 60, sec = time % 60;
    setTitle(String.format("%-62s %-22s %1s", text, min + ":" + (sec < 10 ? "0" : "") + sec, "Aknák: " + MinesNumber));
  }

  protected void remove(byte x, byte y, String s) {
    for (byte a = 1; a < rows - 1; a++) {
      for (byte d = 1; d < cols - 1; d++) {
        remove(btTomb[a][d]);
      }
    }
    rows = x;
    cols = y;
    newgame(x, y, s);
  }

  private void megold() {
    win = false;
    stopper.stop();
    boolean isBomb, isFlag;
    for (byte i = 1; i < (rows - 1); i++) {
      for (byte j = 1; j < (cols - 1); j++) {
        if (btTomb[i][j].isEnabled()) {
          btTomb[i][j].setEnabled(false);
          isBomb = "B".equals(lbTomb[i][j].getText());
          isFlag = btTomb[i][j].getIcon() == imgFlag;
          if (!isBomb) {
            if (!isFlag) {
              expand(i, j);
            } else if (isFlag) {
              btTomb[i][j].setIcon(imgNoBomb);
//                btTomb[i][j].setDisabledIcon(imgNoBomb);//Icon rossz helyen van
            }
          } else if (isBomb) {
            if (isFlag) {
              btTomb[i][j].setIcon(imgYesBomb);
//                btTomb[i][j].setDisabledIcon(imgYesBomb);//Icon rossz helyen van
            } else if (!isFlag) {
              btTomb[i][j].setIcon(imgBomb);
            }
          }
        }
      }
    }
  }

  private void expand(byte i, byte j) {
    byte bombsNumber = 0;
    if (i < rows - 1 && j < cols - 1 && i > 0 && j > 0) {
      for (byte ii = (byte) (i - 1); ii <= i + 1; ii++) {
        for (byte jj = (byte) (j - 1); jj <= j + 1; jj++) {
          if ("B".equals(lbTomb[ii][jj].getText())) {
            bombsNumber++;
          }
        }
      }
    }
    if (imgFlag.equals(btTomb[i][j].getIcon())) {
      btTomb[i][j].setIcon(null);
    }
    btTomb[i][j].setEnabled(false);
    if (bombsNumber > 0 ? true : false) {
      btTomb[i][j].setFont(new java.awt.Font("Tahoma", 1, 8));
      btTomb[i][j].setText("" + bombsNumber);
    } else {
      if (i > 0 && j > 0 && i < rows - 1 && j < cols - 1) {
        for (byte ii = (byte) (i - 1); ii <= i + 1; ii++) {
          for (byte jj = (byte) (j - 1); jj <= j + 1; jj++) {
            if (btTomb[ii][jj].isEnabled()) {
              expand((ii), (jj));
            }
          }
        }
      }
    }
    if (win) {
      areYouWin();
    }
  }

  private void areYouWin() {
    short disableButtonsNumber = 0;
    for (byte b = 1; b < rows - 1; b++) {
      for (byte d = 1; d < cols - 1; d++) {
        if (!btTomb[b][d].isEnabled()) {
          disableButtonsNumber++;
        }
      }
    }
    if (btSum - MinesNumber == disableButtonsNumber) {
      stopper.stop();
      for (byte b = 1; b < rows - 1; b++) {
        for (byte y = 1; y < cols - 1; y++) {
          if ("B".equals(lbTomb[b][y].getText())) {
            lbTomb[b][y].setText(null);
            btTomb[b][y].setIcon(imgBomb);
            btTomb[b][y].setEnabled(false);
          }
        }
      }
      game("win");
    }
  }

  private void game(String s) {
    int min = time / 60, sec = time % 60;
    boolean n = true, q = true;
    byte mintime = Byte.MAX_VALUE;
    if ("win".equals(s)) {
      for (Logic.Player player : Logic.playerList) {
        if (playerLevel.equals(player.getLevel()) || "Level".equals(player.getLevel())) {
          if (!"Level".equals(player.getLevel())) {
            n = false;
          }
          if (player.getTime() < time) {
            q = false;
            break;
          }
          if (mintime > (byte) player.getTime()) {
            mintime = (byte) player.getTime();
          }
        }
      }
      if (q) {
        if (n) {
          About about = new About(rootPane, false, "Add meg a neved");
        } else if (mintime > time) {
          About about = new About(rootPane, false, "Add meg a neved");
        }
      }
    } else if ("lose".equals(s)) {
      JOptionPane.showMessageDialog(rootPane, "<html>A játék " + stepNo + " lépésben ért véget.<br>Az eltelt idő: <u>" + (min > 0 ? min + "</u> perc " : "") + sec + " mp.</html>", "Veszítettél", -1);
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }

  protected void logicInit() {
//    msServer.logic.loadXMLFile("c:/x.xml");
    msServer.logic.loadXMLFile("./src/xml/x.xml");
    msServer.logic.PlayersName();
  }

  public String getPlayerLevel() {
    return playerLevel;
  }

  protected short getTime() {
    return time;
  }

  protected short getID() {
    return playerID;
  }

  protected void setMinesNumber(short MinesNumber) {
    this.MinesNumber = MinesNumber;
  }
}
