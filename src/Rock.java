package src;
import javax.swing.*;
import java.awt.event.KeyEvent;

public class Rock extends Player{
  private boolean falling, fast_falling, down_key_pressed;
  public static final double fall_damage = 3.5;
  public static final int fast_fall_damage = 5;
  public static final double paper_damage = 2.5;

  public Rock(LayeredPanel p, JFrame f, int x, int y, int play_num){
    super(p, f, "images\\rock pixel.png", 6.5, 75, 10, 32, play_num);
    setLocation(1,0);
    setLocation(x,y);
  }

  public void tick(){
    if(getY_velo() < 0){
      falling = true;
      if(down_key_pressed) fast_falling = true;
    }
    else {falling = false; fast_falling = false;}
    super.tick();
  }

  public boolean isFastFalling(){
    return fast_falling;
  }

  public boolean isFalling(){
    return falling;
  }

  public void input(int key){
    if(key == KeyEvent.VK_S) down_key_pressed = true;
    super.input(key);
  }

  public void uninput(int key){
    if(key == KeyEvent.VK_S) down_key_pressed = false;
    super.uninput(key);
  }
  public String isA(){
    return "rock";
  }
}