import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game implements KeyListener, ActionListener{
  private Player[] players;
  private JFrame frame;
  private LayeredPanel panel;
  private Button play, exit;
  private Button rock, paper, scissor;
  private Button home;
  private JLabel number;
  private JLabel[] pick_char;
  private JLabel scor1, scor2;
  private int TARGET_FPS = 165;
  private int currentPlayerSelect = 0;
  private boolean started = false;

  private Runnable gameStart = new Runnable(){
    public void run(){
      int score1 = 0, score2 = 0;
      scor1.setText("" + score1);
      scor2.setText("" + score2);
      panel.change_layer(0);
      long optimal_time = 1000000000/TARGET_FPS;
      while(score1 < 3 && score2 < 3){
        Thread.yield();
        players[0] = new Rock(panel, frame, 0 + 500*currentPlayerSelect, 400, currentPlayerSelect);
        currentPlayerSelect++;
        players[1] = new Rock(panel, frame, 0 + 500*currentPlayerSelect, 400, currentPlayerSelect);
        currentPlayerSelect--;
        //pickPlayers();
        countdown();
        started = true;
      while(!players[0].isDead() && !players[1].isDead()){
        long now = System.nanoTime();
        players[0].tick();
        players[1].tick();
        if(playersColliding()){
          if(players[0].isA().equals("scissor") && ((Scissors)players[0]).isDashing()){
            players[1].takeDmg(Scissors.dash_dmg);
          }
          else if(players[0].isA().equals("rock") && ((Rock)players[0]).isFalling()){
            if(players[1].isA().equals("paper")) players[1].takeDmg(Rock.paper_damage);
            else if(((Rock)players[0]).isFastFalling()) players[1].takeDmg(Rock.fast_fall_damage);
            else players[1].takeDmg(Rock.fall_damage);
          }
          else if(players[0].isA().equals("paper") && ((Paper)players[0]).isFalling() && !((Paper)players[0]).Attacked()){
            ((Paper)players[0]).setAttacked(true);
            if(players[1].isA().equals("rock")) players[1].takeDmg(Paper.rock_damage);
            else players[1].takeDmg(Paper.fall_damage);
          }
          if(players[1].isA().equals("scissor") && ((Scissors)players[1]).isDashing()){
            players[0].takeDmg(Scissors.dash_dmg);
          }
          else if(players[1].isA().equals("rock") && ((Rock)players[1]).isFalling()){
            if(players[0].isA().equals("paper")) players[0].takeDmg(Rock.paper_damage);
            else if(((Rock)players[1]).isFastFalling()) players[0].takeDmg(Rock.fast_fall_damage);
            else players[0].takeDmg(Rock.fall_damage);
          }
          else if(players[1].isA().equals("paper") && ((Paper)players[1]).isFalling() && (!((Paper)players[1]).Attacked())){
            ((Paper)players[1]).setAttacked(true);
            if(players[0].isA().equals("rock")) players[0].takeDmg(Paper.rock_damage);
            else players[0].takeDmg(Paper.fall_damage);
          }
        }
        else{
          if(players[0].isA().equals("paper") && ((Paper)players[0]).Attacked()) ((Paper)players[0]).setAttacked(false);
          if(players[1].isA().equals("paper") && ((Paper)players[1]).Attacked()) ((Paper)players[1]).setAttacked(false);
        }
        long update_time = System.nanoTime()-now;
        long wait = (optimal_time-update_time)/100000;//nano -> milli
        Thread.yield();
        try{
          Thread.sleep(wait);
        }catch(Exception e){
          e.printStackTrace();
        }
      }
      started = false;
      if(players[0].isDead()){
        score2++;
        scor2.setText("" + score2);
      }
      else if(players[1].isDead()){
        score1++;
        scor1.setText("" + score1);
      }
      players[0].setVisible(false);
      players[1].setVisible(false);
      panel.layerRemoveAll(0);
      panel.add(scor1, 0);
      panel.add(scor2, 0);}
      JLabel winner = new JLabel();
      int num;
      if(score1 == 3) num = 1;
      else num = 2;
      winner.setText("Player " + num + " won!");
      winner.setFont(new Font("Vinque Regular", Font.BOLD, 60));
      winner.setForeground(new Color(255,140,0));
      winner.setHorizontalAlignment(SwingConstants.CENTER);
      winner.setVerticalAlignment(SwingConstants.CENTER);
      winner.setBounds(0, 50, 500, 200);
      panel.add(winner, 2);
      panel.add(home, 2);
      panel.change_layer(2);
    }
  };

  private void pickPlayers(){
    panel.show(1);
    pick_char[1].setVisible(false);
    while(currentPlayerSelect == 0){Thread.yield();System.out.print("");}
    players[0].setVisible(false);
    pick_char[0].setVisible(false);
    pick_char[1].setVisible(true);
    while(currentPlayerSelect == 1){Thread.yield();System.out.print("");}
    players[1].setVisible(false);
    currentPlayerSelect = 0;
    panel.hide(1);
    players[0].setVisible(true);
    players[1].setVisible(true);
  }

  private void countdown(){
    number = new JLabel();
    number.setText("3");
    number.setFont(new Font("Vinque Regular", Font.BOLD, 90));
    number.setForeground(new Color(255,140,0));
    number.setHorizontalAlignment(SwingConstants.CENTER);
    number.setVerticalAlignment(SwingConstants.CENTER);
    number.setBounds(0, 50, 500, 200);
    panel.add(number, 0);
    try{Thread.sleep(1000);}catch(Exception e){e.printStackTrace();}
    number.setText("2");
    try{Thread.sleep(1000);}catch(Exception e){e.printStackTrace();}
    number.setText("1");
    try{Thread.sleep(1000);}catch(Exception e){e.printStackTrace();}
    number.setText("FIGHT");
    new Thread(new Runnable(){
      public void run(){
        try{Thread.sleep(250);}catch(Exception e){e.printStackTrace();}
        number.setVisible(false);
      }
    }).start();
  }

  private boolean playersColliding(){
     return players[0].getX() < players[1].getX() + players[1].getWidth() && players[1].getX() < players[0].getX() + players[0].getWidth() && players[0].getY() < players[1].getY() + players[1].getHeight() && players[1].getY() < players[0].getY() + players[0].getHeight();
  }

  public Game(){
    initializeGraphics();
    players = new Player[2];
  }

  private void initializeGraphics(){
    frame = new JFrame();
    frame.setTitle("Game");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(500,400);
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setUndecorated(true);
    frame.addKeyListener(this);

    panel = new LayeredPanel(3);
    panel.setLayout(null);
    panel.setOpaque(true);
    panel.setBGColor(new Color(47, 172, 237), 0);
    panel.setBGColor(new Color(47, 172, 237), 1);
    panel.setBGColor(new Color(0, 73, 144), 2);
    panel.setBGColor(new Color(47, 172, 237), 3);

    JLabel title = new JLabel();
    title.setText("RPS Fighter");
    title.setForeground(Color.BLACK);
    title.setFont(new Font("Ariel", Font.BOLD, 60));
    title.setBounds(0, 55, 500, 80);
    title.setHorizontalAlignment(SwingConstants.CENTER);
    title.setVerticalAlignment(SwingConstants.CENTER);
    panel.add(title, 3);

    play = new Button("Play", this, new Color(0, 73, 144), new Color(173, 200, 255), new Font("Ariel", Font.BOLD, 50), 125, 175, 250, 90);
    panel.add(play, 3);
    exit = new Button("Exit", this, new Color(0, 73, 144), new Color(173, 200, 255), new Font("Ariel", Font.BOLD, 50), 125, 275, 250, 90);
    panel.add(exit, 3);

    rock = new Button("/images/rock pixel.png", this, 30, 200, 6.5, panel);
    paper = new Button("/images/paper pixel.png", this, 220, 200, 7.5, panel);
    scissor = new Button("/images/scissors pixel.png", this, 360, 200, 5, panel);
    
    panel.add(rock, 1);
    panel.add(paper, 1);
    panel.add(scissor, 1);

    pick_char = new JLabel[2];
    JLabel one = new JLabel();
    one.setText("Player 1:");
    one.setForeground(Color.BLACK);
    one.setFont(new Font("Ariel", Font.BOLD, 60));
    one.setBounds(100, 50, 350, 150);
    pick_char[0] = one;
    panel.add(one, 1);

    JLabel two = new JLabel();
    two.setText("Player 2:");
    two.setForeground(Color.BLACK);
    two.setFont(new Font("Ariel", Font.BOLD, 60));
    two.setBounds(100, 50, 350, 150);
    pick_char[1] = two;
    panel.add(two, 1);

    scor1 = new JLabel();
    scor1.setForeground(Color.BLACK);
    scor1.setBackground(Color.LIGHT_GRAY);
    scor1.setOpaque(true);
    scor1.setHorizontalAlignment(SwingConstants.CENTER);
    scor1.setVerticalAlignment(SwingConstants.CENTER);
    scor1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    scor1.setFont(new Font("Ariel", Font.BOLD, 25));
    scor1.setBounds(200, 0, 50, 50);
    panel.add(scor1, 0);

    scor2 = new JLabel();
    scor2.setForeground(Color.BLACK);
    scor2.setBackground(Color.LIGHT_GRAY);
    scor2.setOpaque(true);
    scor2.setHorizontalAlignment(SwingConstants.CENTER);
    scor2.setVerticalAlignment(SwingConstants.CENTER);
    scor2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    scor2.setFont(new Font("Ariel", Font.BOLD, 25));
    scor2.setBounds(250, 0, 50, 50);
    panel.add(scor2, 0);

    home = new Button("Home", this, new Color(173, 200, 255), new Color(0, 73, 144), new Font("Ariel", Font.BOLD, 45), 150, 220, 200, 110);
    home.setBorder(BorderFactory.createEmptyBorder());

    frame.add(panel);
    frame.setVisible(true);
  }


  public void keyTyped(KeyEvent e){}//unused
  public void keyPressed(KeyEvent e){
    if(panel.layerIsVisible(1)){
      switch(e.getKeyCode()){
        case KeyEvent.VK_1: 
          players[currentPlayerSelect] = new Rock(panel, frame, 0 + 500*currentPlayerSelect, 400, currentPlayerSelect);
          currentPlayerSelect++; break;
        case KeyEvent.VK_2: 
          players[currentPlayerSelect] = new Paper(panel, frame, 0 + 500*currentPlayerSelect, 400, currentPlayerSelect);
          currentPlayerSelect++; break;
        case KeyEvent.VK_3: 
          players[currentPlayerSelect] = new Scissors(panel, frame, 0 + 500*currentPlayerSelect, 400, currentPlayerSelect);
          currentPlayerSelect++; break;
      }
    }
    else if(panel.getCurrentLayer() == 0 && started){
      switch(e.getKeyCode()){
      case KeyEvent.VK_W: players[0].input(KeyEvent.VK_W); break;
      case KeyEvent.VK_A: players[0].input(KeyEvent.VK_A); break;
      case KeyEvent.VK_S: players[0].input(KeyEvent.VK_S); break;
      case KeyEvent.VK_D: players[0].input(KeyEvent.VK_D); break;
      case KeyEvent.VK_UP: players[1].input(KeyEvent.VK_W); break;
      case KeyEvent.VK_LEFT: players[1].input(KeyEvent.VK_A); break;
      case KeyEvent.VK_DOWN: players[1].input(KeyEvent.VK_S); break;
      case KeyEvent.VK_RIGHT: players[1].input(KeyEvent.VK_D); break;
    }}
  }
  public void keyReleased(KeyEvent e){
    if(panel.getCurrentLayer() == 0 && started){
      switch(e.getKeyCode()){
      case KeyEvent.VK_W: players[0].uninput(KeyEvent.VK_W); break;
      case KeyEvent.VK_A: players[0].uninput(KeyEvent.VK_A); break;
      case KeyEvent.VK_S: players[0].uninput(KeyEvent.VK_S); break;
      case KeyEvent.VK_D: players[0].uninput(KeyEvent.VK_D); break;
      case KeyEvent.VK_UP: players[1].uninput(KeyEvent.VK_W); break;
      case KeyEvent.VK_LEFT: players[1].uninput(KeyEvent.VK_A); break;
      case KeyEvent.VK_DOWN: players[1].uninput(KeyEvent.VK_S); break;
      case KeyEvent.VK_RIGHT: players[1].uninput(KeyEvent.VK_D); break;
    }}
  }
  public void actionPerformed(ActionEvent e){
    if(e.getSource() == exit){
      System.exit(0);
    }
    else if(e.getSource() == play){
      new Thread(gameStart).start();
    }
    else if(e.getSource() == rock){
      players[currentPlayerSelect] = new Rock(panel, frame, 0 + 500*currentPlayerSelect, 400, currentPlayerSelect);
      currentPlayerSelect++;
    }
    else if(e.getSource() == paper){
      players[currentPlayerSelect] = new Paper(panel, frame, 0 + 500*currentPlayerSelect, 400, currentPlayerSelect);
      currentPlayerSelect++;
    }
    else if(e.getSource() == scissor){
      players[currentPlayerSelect] = new Scissors(panel, frame, 0 + 500*currentPlayerSelect, 400, currentPlayerSelect);
      currentPlayerSelect++;
    }
    else if(e.getSource() == home){
      panel.layerRemoveAll(2);
      panel.change_layer(3);
    }
  }
}