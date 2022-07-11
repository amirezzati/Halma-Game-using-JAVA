import javax.swing.*;

public class Button extends JButton {

    private int i;
    private int x;
    private int y;
    private boolean isChosen = false;

    public Button(int i, int length) {
        super();
        this.i = i;
        x = i/length;
        y = i%length;
    }

    public int getx() {
        return x;
    }

    public int gety() {
        return y;
    }

    public int geti(){
        return i;
    }


    public void setIsChosen(boolean isChosen) {
        this.isChosen = isChosen;
    }
}
