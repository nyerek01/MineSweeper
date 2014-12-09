package minesweeper;

class msServer {

  protected static GUI gui = null;
  protected static Logic logic = null;

  public static void main(String[] args){
    logic = new Logic();
    gui = new GUI();
  }
}