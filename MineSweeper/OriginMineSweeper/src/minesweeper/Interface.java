package minesweeper;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.*;

interface Interface {

  Dimension pic = Toolkit.getDefaultToolkit().getScreenSize();
  String[] strLevelTomb = new String[]{"Könnyű", "Közepes", "Nehéz", "Egyéni", "Fájl", "Új játék", "Súgó"};
  String text = "Aknakereső Készítette: Dóra Dávid";

//  Icon imgYesBomb = new ImageIcon("yesbomb.jpg");
//  Icon imgNoBomb = new ImageIcon("nobomb.tiff");
//  Icon imgBomb = new ImageIcon("bomb.bmp");
//  Icon imgFlag = new ImageIcon("flag.gif");
  ImageIcon imgYesBomb = new ImageIcon("./src/img/yesbomb.jpg");
  ImageIcon imgNoBomb = new ImageIcon("./src/img/nobomb.tiff");
  ImageIcon imgBomb = new ImageIcon("./src/img/bomb.bmp");
  ImageIcon imgFlag = new ImageIcon("./src/img/flag.gif");
  JMenuBar mbMenuBar = new JMenuBar();
  JMenu mFile = new JMenu(strLevelTomb[4]), miNewGame = new JMenu(strLevelTomb[5]), mHelp = new JMenu(strLevelTomb[6]);
  JMenuItem miExit = new JMenuItem("Kilép"), miMegold = new JMenuItem("Megold"), miRestart = new JMenuItem("Újrakezd"), miHightScore = new JMenuItem("Legjobbak"), miAbout = new JMenuItem("Programról"), miEasy = new JMenuItem(strLevelTomb[0]), miMedium = new JMenuItem(strLevelTomb[1]), miHard = new JMenuItem(strLevelTomb[2]), miCustom = new JMenuItem(strLevelTomb[3]);
  JMenuItem[] miTomb = new JMenuItem[]{miEasy, miMedium, miHard, miCustom, miMegold, miRestart, miNewGame, miExit, miHightScore, miAbout};
  String[] tool = new String[]{"Legkönyebb szint (Easy-nek számít a 5x5-nél kisebb tábla).", "Közepes szint (Medium-nak számít a 5x5<=Medium<=10x10 tábla).", "Legnehezebb szint (Hard-nak számít a 10x10-nél nagyobb tábla).", "Új játék a megadott -helyes- paraméterekkel.", "Játék befejezése, aknák felfedése.", "Új játék, az aktuális sor és oszlop számmal.", "", "Program leállítása", "Top lista", "Rövid leírás a programról."};
}
