import javax.swing.*;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Component;

public class LayeredPanel extends JPanel{
  private ArrayList<ArrayList<JComponent>> panels;
  private ArrayList<JComponent> layer0, layer1, layer2, layer3;
  private Color[] bgs;
  private boolean[] visible;
  private int currentLayer;

  public LayeredPanel(int currentLayer){
    panels = new ArrayList<ArrayList<JComponent>>(4);
    layer0 = new ArrayList<JComponent>();
    layer1 = new ArrayList<JComponent>();
    layer2 = new ArrayList<JComponent>();
    layer3 = new ArrayList<JComponent>();
    panels.add(layer0);
    panels.add(layer1);
    panels.add(layer2);
    panels.add(layer3);
    bgs = new Color[4];
    visible = new boolean[4];
    visible[currentLayer] = true;
    this.currentLayer = currentLayer;
  }

  public void add(JComponent c, int layer){
    super.add(c);
    if(layer != currentLayer) c.setVisible(false);
    panels.get(layer).add(c);
  }

  public void remove(Component comp){
    super.remove(comp);
    for(int i = 0; i < panels.size(); i++){
      for(int j = 0; j < panels.get(i).size(); j++){
        if(comp.equals(panels.get(i).get(j))) panels.get(i).remove(j);
      }
    }
  }

  public void setBGColor(Color c, int layer){
    if(layer >= 0 && layer < bgs.length)
      bgs[layer] = c;
    if(layer == currentLayer)
      setBackground(c);
  }

  public Color getBGColor(){
    return bgs[currentLayer];
  }

  public void show(int layer){
    if(layer >= 0 && layer < panels.size()){
      visible[layer] = true;
      for(int i = 0; i < panels.get(layer).size(); i++){
        panels.get(layer).get(i).setVisible(true);
      }
    }
  }

  public void hide(int layer){
    if(layer >= 0 && layer < panels.size()){
      visible[layer] = false;
      for(int i = 0; i < panels.get(layer).size(); i++){
        panels.get(layer).get(i).setVisible(false);
      }
    }
  }

  public void change_layer(int layer){
    if(layer >= 0 && layer < panels.size()){
      currentLayer = layer;
      for(int i = 0; i < panels.size(); i++){
        visible[i] = i == layer;
        for(int j = 0; j < panels.get(i).size(); j++){
          panels.get(i).get(j).setVisible(i == layer);
        }
      }
      setBackground(bgs[layer]);
    }
  }

  public void layerRemoveAll(int layer){
    if(layer >= 0 && layer < panels.size()){
      for(int i = 0; i < panels.get(layer).size(); i++){
        super.remove(panels.get(layer).get(i));
      }
      panels.get(layer).clear();
    }
  }

  public boolean layerIsVisible(int layer){
    if(visible[layer]) return true;
    else return false;
  }

  public int getCurrentLayer(){
    return currentLayer;
  }
}