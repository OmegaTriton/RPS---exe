import java.awt.Image;
import java.io.IOException;

import javax.swing.*;

public abstract class Entity{
  private ImageIcon image;
  private JLabel image_pic;
  private int width, height;
  private int x_bound, y_bound;
  private boolean running = true;

  public Entity(LayeredPanel p, JFrame f, String filePath, double scale){
    x_bound = f.getWidth();
    y_bound = f.getHeight();

    byte[] contents;
    try {
      contents = getClass().getResourceAsStream(filePath).readAllBytes();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    image = new ImageIcon(contents);

    // image = new ImageIcon(getClass().getResource(filePath));

    width = ((Double) (image.getIconWidth() * scale)).intValue();
    height = ((Double) (image.getIconHeight() * scale)).intValue();
    image = new ImageIcon(image.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
    image_pic = new JLabel(image);
    image_pic.setSize(width, height);
    p.add(image_pic);
  }
  public void setLocation(int x, int y){
    if (x > x_bound - width)
      x = x_bound - width;
    if (x < 0)
      x = 0;
    if (y < 0)
      y = 0;
    if (y > y_bound - height)
      y = y_bound - height;
    image_pic.setLocation(x,y);
  }
  public void setVisible(boolean b){
    image_pic.setVisible(b);
  }
  public int getX(){
    return image_pic.getX();
  }
  public int getY(){
    return image_pic.getY();
  }
  public int getWidth(){
    return width;
  }
  public int getHeight(){
    return height;
  }
  public boolean isRunning(){
    return running;
  }
  public void setRunning(boolean r){
    running = r;
  }
  public int getX_bound(){
    return x_bound;
  }
  public int getY_bound(){
    return y_bound;
  }
  public String isA(){
    return "entity";
  }
}