package src;
import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.Image;

public class Button extends JButton{
    public Button(String text, ActionListener a, Color background, Color textColor, Font font, int x, int y, int width, int height){
        setText(text);
        addActionListener(a);
        setBounds(x, y, width, height);
        setFocusable(false);
        setBackground(background);
        setForeground(textColor);
        setFont(font);
    }
    public Button(String filepath, ActionListener a, int x, int y, double scale, LayeredPanel p){
        byte[] contents;
        try {
        contents = getClass().getResourceAsStream(filepath).readAllBytes();
        } catch (IOException e) {
        throw new RuntimeException(e);
        }
        ImageIcon i = new ImageIcon(contents);

        // ImageIcon i = new ImageIcon(getClass().getResource(filepath));

        int width = ((Double) (i.getIconWidth() * scale)).intValue();
        int height = ((Double) (i.getIconHeight() * scale)).intValue();
        i = new ImageIcon(i.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        setIcon(i);
        addActionListener(a);
        setBounds(x, y, i.getIconWidth(), i.getIconHeight());
        setFocusable(false);
        setBorder(BorderFactory.createEmptyBorder());
        setBackground(p.getBGColor());
    }
}