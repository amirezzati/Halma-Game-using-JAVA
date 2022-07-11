import javax.swing.*;

public class Player {
    private String name = null;
    Icon icon = null;
    private boolean turn = false;
    private int numberOfMovement = 0;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public void moved() {
        numberOfMovement ++;
    }

    public int getNumberOfMovement() {
        return numberOfMovement;
    }

    public void setNumberOfMovement(int numberOfMovement) {
        this.numberOfMovement = numberOfMovement;
    }
}
