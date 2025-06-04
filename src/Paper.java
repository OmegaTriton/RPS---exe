import javax.swing.*;

public class Paper extends Player{
    private int grav = 0;
    private boolean falling, attacked;
    public static final int fall_damage = 6;
    public static final int rock_damage = 16;

    public Paper(LayeredPanel p, JFrame f, int x, int y, int play_num){
        super(p, f, "/images/paper pixel.png", 7.5, 50, 20, 16, play_num);
        setLocation(1,0);
        setLocation(x,y);
        attacked = false;
    }

    public void tick(){
        if(grav++ % 3 == 0 && !getReachedGround()) addY_velo(2);
        if(getY_velo() < 0) falling = true;
        else falling = false;
        super.tick();
    }

    public boolean isFalling(){
        return falling;
    }

    public String isA(){
        return "paper";
    }

    public void setAttacked(boolean a){
        attacked = a;
    }

    public boolean Attacked(){
        return attacked;
    }
}
