package minesweeper;

import java.awt.HeadlessException;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

final class Logic extends JFrame {

  static ArrayList<Player> playerList;
  private JList lPlayersName, lPlayersLevel, lPlayersTime;
  private String playerName, playerLevel;
  private short playerTime, playerID;

  public Logic() throws HeadlessException {
    try {
//      loadXMLFile("c:/x.xml");
      loadXMLFile("./src/xml/x.xml");
    } catch (Exception e) {
//      writeXMLFile("c:/x.xml");
      writeXMLFile("./src/xml/x.xml");
    }
  }

  void writeXMLFile(String str) {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();

      Element rootElement = doc.createElement("Players");
      doc.appendChild(rootElement);

      Element Player = doc.createElement("Player");
      rootElement.appendChild(Player);
      Player.setAttribute("ID", "1");

      Element Name = doc.createElement("Name");
      Name.appendChild(doc.createTextNode("Name"));
      Player.appendChild(Name);

      Element Level = doc.createElement("Level");
      Level.appendChild(doc.createTextNode("Level"));
      Player.appendChild(Level);

      Element Time = doc.createElement("Time");
      Time.appendChild(doc.createTextNode(99 + ""));
      Player.appendChild(Time);

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(str));
      transformer.transform(source, result);
    } catch (ParserConfigurationException | TransformerException pce) {
      pce.printStackTrace();
    }
  }

  class Player {

    private String playerName;
    private String playerLevel;
    private short playerTime;
    private short playerID;

    Player(String playerName, String playerLevel, short playerTime, short playerID) {
      this.playerName = playerName;
      this.playerLevel = playerLevel;
      this.playerTime = playerTime;
      this.playerID = playerID;
    }

    protected String getName() {
      return playerName;
    }

    public void setName(String playerName) {
      this.playerName = playerName;
    }

    protected String getLevel() {
      return playerLevel;
    }

    protected short getTime() {
      return playerTime;
    }

    protected short getID() {
      return playerID;
    }

    @Override
    public String toString() {
      return (String.format("%-8s %-10s %6s %n", getLevel(), getName(), getTime()));
    }
  }

  void loadXMLFile(String str) {
    playerList = new ArrayList<>();
    File xmlFile = null;
    try {
      xmlFile = new File(str);
    } catch (Exception e) {
    }
    Document d = null;
    try {
      d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
    } catch (ParserConfigurationException | SAXException | IOException e) {
    }
    try {
      NodeList nodeList = d.getDocumentElement().getElementsByTagName("Player");
      for (int i = 0; i < nodeList.getLength(); i++) {
        Element node = (Element) nodeList.item(i);

        Node name = node.getElementsByTagName("Name").item(0);
        playerName = name.getFirstChild().getNodeValue();

        Node level = node.getElementsByTagName("Level").item(0);
        playerLevel = level.getFirstChild().getNodeValue();

        Node t = node.getElementsByTagName("Time").item(0);
        playerTime = Short.parseShort(t.getFirstChild().getNodeValue());

        playerList.add(new Player(playerName, playerLevel, playerTime, playerID));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @SuppressWarnings("unchecked")
  void PlayersName() {
    lPlayersName = new JList();
    DefaultListModel dlm = new DefaultListModel();
    lPlayersName.setModel(dlm);
    TreeSet<String> playerNameSet = new TreeSet<>();
    TreeSet<String> playerLevelSet = new TreeSet<>();
    TreeSet<Short> playerTimeSet = new TreeSet<>();
    for (Player player : playerList) {
      playerNameSet.add(String.format("%-8s %-10s %7s %n", player.getLevel(), player.getName(), player.getTime() + " sec"));
    }
    for (String pL : playerLevelSet) {
      dlm.addElement(pL + ":");
    }
    for (Iterator<Short> it = playerTimeSet.iterator(); it.hasNext();) {
      Short pT;
      pT = it.next();
      dlm.addElement(pT + " sec.");
    }
    for (String pN : playerNameSet) {
      dlm.addElement(pN);
    }
  }

  void PlayersID() {
    TreeSet<Short> playerIDSet = new TreeSet<>();
    for (Player player : playerList) {
      playerIDSet.add(player.getID());
    }
  }

  void ModifXMLFile(String str) throws TransformerConfigurationException, TransformerException, ParserConfigurationException, SAXException, IOException {
    try {
      boolean mod = true;
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.parse(str);
      NodeList nodeList = doc.getDocumentElement().getElementsByTagName("Player");
      String m;
      for (int i = 0; i < nodeList.getLength(); i++) {
        Element node = (Element) nodeList.item(i);
        Node level = node.getElementsByTagName("Level").item(0);
        m = level.getFirstChild().getNodeValue();
        if (msServer.gui.getPlayerLevel().equals(m)) {
          Node n = node.getElementsByTagName("Name").item(0);
          Node t = node.getElementsByTagName("Time").item(0);
          n.getFirstChild().setTextContent(playerName);
          t.getFirstChild().setTextContent(msServer.gui.getTime() + "");
          mod = false;
        }
//        Node t = node.getElementsByTagName("Time").item(0);
//        playerTime = Short.parseShort(t.getFirstChild().getNodeValue());
      }
      if (mod) {
        Node Players = doc.getFirstChild();
        Element newplayer = doc.createElement("Player");
        Players.appendChild(newplayer);

        Element newplayerName = doc.createElement("Name");
        newplayerName.appendChild(doc.createTextNode(playerName));
        newplayer.appendChild(newplayerName);

        Element newplayerLevel = doc.createElement("Level");
        newplayerLevel.appendChild(doc.createTextNode(msServer.gui.getPlayerLevel()));
        newplayer.appendChild(newplayerLevel);

        Element newplayerTime = doc.createElement("Time");
        newplayerTime.appendChild(doc.createTextNode(msServer.gui.getTime() + ""));
        newplayer.appendChild(newplayerTime);
      }
      // loop the player child node
//      NodeList list = player.getChildNodes();

//      for (int i = 0; i < list.getLength(); i++) {
//        Node node = list.item(i);
      // update
//          if ("Level".equals(node.getNodeName())) {
//            node.setTextContent(playerLevel);
//          }
      //remove Time
//          if ("Time".equals(node.getNodeName())) {
//            player.removeChild(node);
//          }
//      }

      // write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(str));
      transformer.transform(source, result);
    } catch (ParserConfigurationException | TransformerException | IOException | SAXException pce) {
      pce.printStackTrace();
    }
  }

  JList getLPlayersName() {
    return lPlayersName;
  }

  JList getLPlayersLevel() {
    return lPlayersLevel;
  }

  JList getLPlayersTime() {
    return lPlayersTime;
  }

  void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  void setPlayerLevel(String playerLevel) {
    this.playerLevel = playerLevel;
  }
}
