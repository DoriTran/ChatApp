import java.awt.*;

public class GridBagStatus extends GridBagConstraints {
    // Constructor
    public GridBagStatus() {
        this.gridx = 0;
        this.gridy = 0;
    }
    GridBagStatus(Integer x, Integer y) {
        this.gridx = x;
        this.gridy = y;
    }

    // Chain setter
    public GridBagStatus setGrid(Integer x, Integer y) {
        this.gridx = x;
        this.gridy = y;
        return this;
    }
    public GridBagStatus setWitdh(Integer witdh) {
        this.gridwidth = witdh;
        return this;
    }
    public GridBagStatus setHeight(Integer height) {
        this.gridheight = height;
        return this;
    }
    public GridBagStatus setFill(Integer fill) {
        this.fill = fill;
        return this;
    }
    public GridBagStatus setWeight(Integer weightX, Integer weightY) {
        this.weightx = weightX;
        this.weighty = weightY;
        return this;
    }
    public GridBagStatus setInsets(Integer value) {
        this.insets = new Insets(value,value,value,value);
        return this;
    }
    public GridBagStatus setInsets(Integer top, Integer left, Integer bottom, Integer right) {
        this.insets = new Insets(top, left, bottom, right);
        return this;
    }
}
