package src;
import javax.swing.*;
import java.awt.Image;
import java.awt.event.KeyEvent;

public class Scissors extends Player{
  private int ticks_since_dash_start;
  private boolean dashing, direction; //true = right, false = left
  private double dash_modifier = 2;
  private double dash_time = 0.45;
  private int dash_right = 0, dash_left = 0; /*
    0 = released
    1 = pressed for short time
    2 = dash()*/
  private long time_to_wait = 0;
  private long time_pressed;
  private long time_of_dash_start, time_of_dash_end;
  private ImageIcon left_dashing, right_dashing;
  private JLabel left_dashing_pic, right_dashing_pic;
  private int dash_width, dash_height;
  private static final int dash_delay = 150 * 1000000;
  public static final double dash_dmg = 1.5;

  public Scissors(LayeredPanel p, JFrame f, int x, int y, int play_num){
    super(p, f, "images\\scissors pixel.png", f.getWidth()*5/500, 45, 15, 24, play_num);
    left_dashing = new ImageIcon("images\\scissors pixel left dash.gif");
    right_dashing = new ImageIcon("images\\scissors pixel right dash.gif");
    dash_width = ((Double) (left_dashing.getIconWidth() * f.getWidth()*5.0/500)).intValue();
    dash_height = ((Double) (left_dashing.getIconHeight() * f.getHeight()*5.0/400)).intValue();
    left_dashing = new ImageIcon(left_dashing.getImage().getScaledInstance(dash_width, dash_height, Image.SCALE_DEFAULT));
    right_dashing = new ImageIcon(right_dashing.getImage().getScaledInstance(dash_width, dash_height, Image.SCALE_DEFAULT));
    left_dashing_pic = new JLabel(left_dashing);
    left_dashing_pic.setSize(dash_width, dash_height);
    right_dashing_pic = new JLabel(right_dashing);
    right_dashing_pic.setSize(dash_width, dash_height);
    p.add(left_dashing_pic, 0);
    p.add(right_dashing_pic, 0);
    left_dashing_pic.setVisible(false);
    right_dashing_pic.setVisible(false);
    setLocation(1,0);
    setLocation(x,y);
    time_of_dash_end = System.nanoTime() - dash_delay;
  }

  public void input(int key){
    if(key == KeyEvent.VK_A){
      dash_right = -1;
      if(dash_left == 0){
        dash_left++;
        time_to_wait = 175 * 1000000; //300 milliseconds -> nano
        time_pressed = System.nanoTime();
      }
    }
    else if(key == KeyEvent.VK_D){
      dash_left = -1;
      if(dash_right == 0){
        dash_right++;
        time_to_wait = 175 * 1000000; //300 milliseconds -> nano
        time_pressed = System.nanoTime();
      }
    }
    super.input(key);
  }

  public void uninput(int key){
    if(key == KeyEvent.VK_A){
      if(dash_left < 0 && System.nanoTime() - time_of_dash_end > dash_delay && !dashing) dash_left = 0;
      else if(dash_left == 1 && System.nanoTime() - time_pressed < time_to_wait) {
        dash_left = -1;
        dash(false);
      }
    }
    else if(key == KeyEvent.VK_D){
      if(dash_right < 0 && System.nanoTime() - time_of_dash_end > dash_delay && !dashing) dash_right = 0;
      else if(dash_right == 1 && System.nanoTime() - time_pressed < time_to_wait) {
        dash_right = -1;
        dash(true);
      }
    }
    super.uninput(key);
  }

  public void dash(boolean b){//true = right, false = left
    setVisible(false);
    time_of_dash_start = System.nanoTime();
    ticks_since_dash_start = 0;
    dashing = true;
    direction = false;
    double amt = dash_modifier*dash_time/2;
    if(!b){//left
        amt *= -1;
        direction = true;
        left_dashing_pic.setVisible(true);
    }
    else{//right
        right_dashing_pic.setVisible(true);
    }
    addX_velo(amt);
    setGravity(false);
    setMovable(false);
  }

  public void tick(){
    if(dashing && !direction) {right_dashing_pic.setVisible(true); left_dashing_pic.setVisible(false); setVisible(false);}
    else if(dashing && direction) {left_dashing_pic.setVisible(true); right_dashing_pic.setVisible(false); setVisible(false);}
    else if(!dashing) {setVisible(true); right_dashing_pic.setVisible(false); left_dashing_pic.setVisible(false);}
    if(dashing){
      double amt = (-1)*dash_modifier*ticks_since_dash_start;
      if(!direction) amt *= -1;
      if(System.nanoTime() - time_of_dash_start >= dash_time * 1000000000){
        stopDash();
        time_of_dash_end = System.nanoTime();
      }
      else {
        addX_velo(amt);
        ticks_since_dash_start++;
      }
    }
    super.tick();
  }

  public void stopDash(){
    addX_velo(-1 * getX_velo());
    dashing = false;
    setGravity(true);
    setMovable(true);
    left_dashing_pic.setVisible(false);
    right_dashing_pic.setVisible(false);
    setVisible(true);
  }

  public void setLocation(int x, int y){
    int rx = x, ry = y+getHeight()-dash_height;
    if (rx > getX_bound() - dash_width){
      rx = getX_bound() - dash_width;
      stopDash();
    }
    if (rx < 0)
      rx = 0;
    if (ry < 0)
      ry = 0;
    if (ry > getY_bound() - dash_height)
      ry = getY_bound() - dash_height;
    right_dashing_pic.setLocation(rx, ry);

    int lx = x+getWidth()-dash_width, ly = y+getHeight()-dash_height;
    if (lx > getX_bound() - dash_width)
      lx = getX_bound() - dash_width;
    if (lx < 0){
      lx = 0;
      stopDash();
    }
    if (ly < 0)
      ly = 0;
    if (ly > getY_bound() - dash_height)
      ly = getY_bound() - dash_height;
    left_dashing_pic.setLocation(lx, ly);
    super.setLocation(x, y);
  }

  public int getWidth(){
    if(dashing) return dash_width;
    else return super.getWidth();
  }

  public int getHeight(){
    if(dashing) return dash_height;
    else return super.getHeight();
  }
  public String isA(){
    return "scissor";
  }

  public boolean isDashing(){
    return dashing;
  }

  public void die(){
    right_dashing_pic.setVisible(false);
    left_dashing_pic.setVisible(false);
    super.die();
  }
}