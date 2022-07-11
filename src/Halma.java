import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Halma {

    private int length;
    private int piece;
    JFrame f;
    Button btn;
    ArrayList<Button> button = new ArrayList<>();
    Player p1;
    Player p2;
    Icon bblock;
    Icon rblock;
    Icon windowIcon = new ImageIcon("img\\gamelogo.png");
    JMenuBar menuBar;
    JMenu reStart, menU;


    public Halma(int length, int piece, Player p1, Player p2, Icon bblock, Icon rblock) {
        this.length = length;
        this.piece = piece;
        this.p1 = p1;
        this.p2 = p2;
        this.bblock = resizeIcon(bblock, length);
        this.rblock = resizeIcon(rblock, length);
        this.p1.setIcon(this.bblock);
        this.p2.setIcon(this.rblock);
    }

    public void Game(JFrame menu) { // This Function is a Frame that can Show Board Game
        f = new JFrame();
        f.setTitle("HALMA");
        ImageIcon icon = (ImageIcon) windowIcon;
        Image img = icon.getImage();
        f.setIconImage(img); // add image icon to Board Game Frame

        // Create JMenuBar and add restart and exit to menubar
        menuBar = new JMenuBar();

        reStart = new JMenu("Restart"); // JMenuBar item
        menU = new JMenu("Menu"); // JMenuBar Item
        menuBar.add(reStart);
        menuBar.add(menU);
        f.setJMenuBar(menuBar);


        // add mouse listener to Menubar Items
        reStart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                returnPiecesToFirstPosition();
                p1.setTurn(true);
                p2.setTurn(false);
                p1.setNumberOfMovement(0);
                p2.setNumberOfMovement(0);
            }
        });
        menU.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                f.dispose();
            }
        });

        // add window listener to Board Frame
        f.addWindowListener(new WindowAdapter() { // when board game is closed, Menu frame visibility should be true.
            @Override
            public void windowClosed(WindowEvent e) {
                menu.setVisible(true);
            }
        });


        /*try {
            writer = new FileWriter("records.txt");
        } catch (IOException e) {
            e.getStackTrace();
        }*/

        for (int i = 0; i < length * length; i++) { // arrange pieces in board game

            button.add(new Button(i, length));
            button.get(i).setBorderPainted(false);


            if (i / length <= piece && piece - i % length - i / length > 0) { // player 1 pieces range
                button.get(i).setIcon(bblock);
            } else if (i / length > length - piece - 1 && i / length + i % length > 2*length - 2 - piece) { // player 2 pieces range
                button.get(i).setIcon(rblock);
            }

            if ( ((i/length)+(i%length)) % 2 != 0 ) {
                // table background color
                // buttons whose total row and column are odd number should be black.
                button.get(i).setBackground(new Color(71, 42, 0));
                /*if( (i/length)+(i%length) == length-1 ) {
                    button.get(i).setBackground(new Color(71, 42, 0, 216));
                }*/
            } else {
                button.get(i).setBackground(Color.white);
            }


            f.add(button.get(i));
        }

        final Button[] sd = new Button[2]; // save source button and destination button
        /*
            sd[0] source button
            sd[1] destination button

         */
        final int[] counter = {1}; // for get source button and destination button, if get true source button then get destination button.
        p1.setTurn(true);


        for (int i = 0; i < button.size(); i++) {

            // Add Mouse Listener To All buttons in Button ArrayList
            button.get(i).addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (counter[0] == 1) {
                        sd[0] = (Button) e.getSource();
                        if (havePiece(sd[0])) { // check that the first selection is empty.

                            if(sd[0].getIcon() == p1.getIcon() && p1.isTurn() == true) { // First Player Turn
                                counter[0] = 2; // if source button is correct should select and give destination button.

                            } else if (sd[0].getIcon() == p1.getIcon() && p1.isTurn() == false) { // Error when It's not First Player's Turn
                                JOptionPane.showMessageDialog(null,
                                            "" +
                                                    "It's " + p2.getName() + " Turn!",
                                            "Error",
                                            JOptionPane.ERROR_MESSAGE);


                            } else if (sd[0].getIcon() == p2.getIcon() && p2.isTurn() == true) { // Second Player Turn
                                counter[0] = 2; // if source button is correct should select and give destination button.

                            } else if (sd[0].getIcon() == p2.getIcon() && p2.isTurn() == false) { // Error when It's not Second Player's Turn
                                JOptionPane.showMessageDialog(null,
                                        "It's " + p1.getName() + " Turn!",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }

                        } else { // if you select empty button as first choice
                            JOptionPane.showMessageDialog(null, "Your Selection Is Empty, Choose Another", "Error", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                    else if (counter[0] == 2){

                        sd[1] = (Button) e.getSource();
                        if (!havePiece(sd[1])){  // Second Choice Should Be Empty!

                            counter[0] = 1;

                            if (checkMovement(sd[0], sd[1])) { // call checkMovement function with two button to check movement.

                                if (sd[1].getIcon() == p1.getIcon()) { // calculate number of movement
                                    p1.moved();
                                } else {
                                    p2.moved();
                                }
                                // If movement is ok, should change turn
                                if (sd[1].getIcon() == p1.getIcon()) {
                                    p1.setTurn(false);
                                    p2.setTurn(true);
                                } else if (sd[1].getIcon() == p2.getIcon()){
                                    p1.setTurn(true);
                                    p2.setTurn(false);
                                }

                            }
                            if (checkWinner(p1)) { // check whether any player is winner or not?
                                JOptionPane.showMessageDialog(null, p1.getName() + " Is Winner", "WINNER", JOptionPane.PLAIN_MESSAGE);
                                try {
                                    FileWriter writer = new FileWriter("records.txt", true);
                                    writer.write(p1.getName()+"\n");
                                    writer.write(Integer.toString(p1.getNumberOfMovement())+"\n");
                                    writer.close();
                                } catch (IOException f) {
                                    f.getStackTrace();
                                }
                                menu.setVisible(true);
                                f.dispose();
                            } else if (checkWinner(p2)) {
                                JOptionPane.showMessageDialog(null, p2.getName() + " Is Winner", "WINNER", JOptionPane.PLAIN_MESSAGE);
                                try {
                                    FileWriter writer = new FileWriter("records.txt", true);
                                    writer.write(p2.getName()+"\n");
                                    writer.write(Integer.toString(p1.getNumberOfMovement())+"\n");
                                    writer.close();
                                } catch (IOException f) {
                                    f.getStackTrace();
                                }
                                menu.setVisible(true);
                                f.dispose();
                            }

                        } else { // if second choice isn't empty, you can't do this movement.
                            JOptionPane.showMessageDialog(null, "Your Can't Do This Movement", "Error", JOptionPane.WARNING_MESSAGE);
                            counter[0] = 1;
                        }
                    }

                }
            });

        }


        // setting grid layout of 3 rows and 3 columns

        setSizeOfBoard(f, length);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.setLayout(new GridLayout(length, length, 0, 0));
        f.setVisible(true);

    }

    public void setSizeOfBoard(JFrame f, int length) {
        if (length<=2) {
            f.setSize(300, 330);
        } else if (length<=4) {
            f.setSize(400, 430);
        } else if (length<=6) {
            f.setSize(500, 530);
        } else if (length>6) {
            f.setSize(650, 680);
        }
    }

    public ImageIcon resizeIcon(Icon i, int length) {
        ImageIcon im = (ImageIcon) i;
        Image img = im.getImage();
        ImageIcon output = null;
        if (length<=2) {
            Image img2 = img.getScaledInstance(300/length, 300/length, Image.SCALE_SMOOTH);
            output = new ImageIcon(img2);
        } else if (length<=4) {
            Image img2 = img.getScaledInstance(400/length, 400/length, Image.SCALE_SMOOTH);
            output = new ImageIcon(img2);
        } else if (length<=6) {
            Image img2 = img.getScaledInstance(500/length, 500/length, Image.SCALE_SMOOTH);
            output = new ImageIcon(img2);
        } else if (length>6) {
            Image img2 = img.getScaledInstance(650/length, 650/length, Image.SCALE_SMOOTH);
            output = new ImageIcon(img2);
        }
        return output;
    }

    public boolean checkMovement(Button source, Button destination) {
        boolean isOk = false;
        int counter = 0;
        if (canMove(source, destination))  {
            //System.out.println(canJump(source, destination));
            destination.setIcon(source.getIcon());
            source.setIcon(null);
            isOk = true;
            //System.out.println("mitooni beri in khoone :)");

        } else if (oneJump(source, destination, 'o')) { // Char c = 'o' means ok and dont have limitations
            destination.setIcon(source.getIcon());
            source.setIcon(null);
            isOk = true;
            //System.out.println("mitooni beri in khoone :)");
        }
        else if (canJump(source, destination, 'o', counter)) {
            destination.setIcon(source.getIcon());
            source.setIcon(null);
            isOk = true;
            //System.out.println("mitooni beri in khoone :)");
        } else {
            JOptionPane.showMessageDialog(null, "You Cant Jump Here!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return isOk;
        /*for (int i = 0; i < button.size(); i++) {
                button.get(i).addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Button c = (Button) e.getSource();
                        if (!havePiece(c)) {

                        } else {
                            JOptionPane.showMessageDialog(null, "You Can't Do This Movement", "Error", JOptionPane.WARNING_MESSAGE);
                            game(b);
                        }
                        //System.out.println(b.geti() + " " + b.gety());
                        //System.out.println(havePiece(b));
                    }
                });
        }*/

    }

    public boolean canMove(Button source, Button destination) { // check whether piece can move right, left, up and etc
        boolean canMove = false;
        if ( destination.getx() == source.getx() && (destination.gety() == source.gety() + 1 || destination.gety() == source.gety() - 1) ) {
            canMove = true;   // destination is left or right
        }
        else if ( destination.gety() == source.gety() && (destination.getx() == source.getx() + 1 || destination.getx() == source.getx() - 1) ) {
            canMove = true;   // destination is up or down
        }
        else if ( destination.getx() == source.getx() + 1 && (destination.gety() == source.gety() + 1 || destination.gety() == source.gety() - 1) ) {
            canMove = true; // Down-Left and Down-Right
        }
        else if ( destination.getx() == source.getx() - 1 && (destination.gety() == source.gety() + 1 || destination.gety() == source.gety() - 1) ) {
            canMove = true; // Top-Left and Top-Right
        }

        return canMove;

    }

    public boolean oneJump(Button source, Button destination, char c) { // one jump
        boolean oneJump = false;

        // this function check one jump for piece
        /*
                $ : Source Button

                a     b     c
                  _________
                  | @ @ @ |
                d | @ $ @ | e
                  | @ @ @ |
                  ---------
                f     g     h

         */

        try {
            if (destination.getx() == source.getx()+2 && destination.gety() == source.gety()+2 && c!='a') {
                if (havePiece(getbutton(source.getx()+1, source.gety()+1))) { // for jump
                    // from left piece
                    if (!havePiece(getbutton(source.getx()+2, source.gety()+2))) {
                        oneJump = true;
                    }
                }
            }
        } catch (Exception e) {

        }

        try {
            if (destination.getx() == source.getx()+2 && destination.gety() == source.gety() && c!='b') {
                if (havePiece(getbutton(source.getx()+1, source.gety()))) { // for jump from left piece
                    if (!havePiece(getbutton(source.getx()+2, source.gety()))) {
                        oneJump = true;
                    }
                }
            }
        } catch (Exception e) {

        }

        try {
            if (destination.getx() == source.getx()+2 && destination.gety() == source.gety()-2 && c!='c') {
                if (havePiece(getbutton(source.getx()+1, source.gety()-1))) { // for jump from left piece
                    if (!havePiece(getbutton(source.getx()+2, source.gety()-2))) {
                        oneJump = true;
                    }
                }
            }
        } catch (Exception e) {

        }

        try {
            if (destination.getx() == source.getx() && destination.gety() == source.gety()+2 && c!='d') {
                if (havePiece(getbutton(source.getx(), source.gety() + 1))) { // for jump from right piece
                    if (!havePiece(getbutton(source.getx(), source.gety() + 2))) {
                        oneJump = true;
                    }
                }
            }
        } catch (Exception e) {

        }
        try {
            if (destination.getx() == source.getx() && destination.gety() == source.gety()-2 && c!='e') {
                if (havePiece(getbutton(source.getx(), source.gety() - 1))) { // for jump from left piece
                    if (!havePiece(getbutton(source.getx(), source.gety() - 2))) {
                        oneJump = true;
                    }
                }
            }
        } catch (Exception e) {

        }

        try {
            if (destination.getx() == source.getx()-2 && destination.gety() == source.gety()+2 && c!='f') {
                if (havePiece(getbutton(source.getx()-1, source.gety()+1))) { // for jump from left piece
                    if (!havePiece(getbutton(source.getx()-2, source.gety()+2))) {
                        oneJump = true;
                    }
                }
            }
        } catch (Exception e) {

        }

        try {
            if (destination.getx() == source.getx()-2 && destination.gety() == source.gety() && c!='g') {
                if (havePiece(getbutton(source.getx()-1, source.gety()))) { // for jump from left piece
                    if (!havePiece(getbutton(source.getx()-2, source.gety()))) {
                        oneJump = true;
                    }
                }
            }
        } catch (Exception e) {

        }

        try {

            if (destination.getx() == source.getx()-2 && destination.gety() == source.gety()-2 && c!='h') {
                if (havePiece(getbutton(source.getx()-1, source.gety()-1))) { // for jump from left piece
                    if (!havePiece(getbutton(source.getx()-2, source.gety()-2))) {
                        oneJump = true;
                    }
                }
            }
        } catch (Exception e) {

        }

        return oneJump;
    }

    public boolean canJump(Button source, Button destination, char c, int counter) { // Two and More Jump
        boolean canJump = false;

        /*
                $ : Source Button

                a     b     c
                  _________
                  | @ @ @ |
                d | @ $ @ | e
                  | @ @ @ |
                  ---------
                f     g     h

         */

        if (source.getx() == destination.getx() && source.gety() == destination.gety()) {
            return true;
        } else if (counter > length*length/4) {
            return false;
        }

        // It should notify type of previous movement!!!
        // "Char c" is type of movement
        if (oneJump(source, getbutton(source.getx(), source.gety()+2), c)) { // right
            c = 'e';
            counter++;
            if (canJump(getbutton(source.getx(), source.gety()+2), destination, c, counter)){
                return true;
            }
        }
        if (oneJump(source, getbutton(source.getx(), source.gety()-2), c)) { // left
            c = 'd';
            counter++;
            if (canJump(getbutton(source.getx(), source.gety()-2), destination, c, counter)){
                return true;
            }
        }
        if (oneJump(source, getbutton(source.getx()-2, source.gety()-2), c)) { // top-left
            c = 'a';
            counter++;
            if (canJump(getbutton(source.getx()-2, source.gety()-2), destination, c, counter)){
                return true;
            }
        }
        if (oneJump(source, getbutton(source.getx()-2, source.gety()), c)) { // top
            c = 'b';
            counter++;
            if (canJump(getbutton(source.getx()-2, source.gety()), destination, c, counter)){
                return true;
            }
        }
        if (oneJump(source, getbutton(source.getx()-2, source.gety()+2), c)) { // top-right
            c = 'c';
            counter++;
            if (canJump(getbutton(source.getx()-2, source.gety()+2), destination, c, counter)){
                return true;
            }
        }
        if (oneJump(source, getbutton(source.getx()+2, source.gety()-2), c)) { // bottom-left
            c = 'f';
            counter++;
            if (canJump(getbutton(source.getx()+2, source.gety()-2), destination, c, counter)){
                return true;
            }
        }
        if (oneJump(source, getbutton(source.getx()+2, source.gety()), c)) { // bottom
            c = 'g';
            counter++;
            if (canJump(getbutton(source.getx()+2, source.gety()), destination, c, counter)){
                return true;
            }
        }
        if (oneJump(source, getbutton(source.getx()+2, source.gety()+2), c)) { // bottom-right
            c = 'h';
            counter++;
            if (canJump(getbutton(source.getx()+2, source.gety()+2), destination, c, counter)){
                return true;
            }
        }

        return canJump;

    }

    public Boolean havePiece(Button btn) {
        boolean havePiece = false;
        if (button.get(btn.geti()).getIcon() == bblock || button.get(btn.geti()).getIcon() == rblock) {
            havePiece = true;
        }
        return havePiece;
    }

    public Button getbutton(int x, int y) {
        if ( x<length && x>=0 && y<length && y>=0 ) {
            return button.get(x*length + y);

        }
        return null;
    }

    public boolean checkWinner (Player p) { // check who player is winner?
        int counter = 0;
        boolean pISWinner = false;
        if (p.getIcon() == bblock) {
            for (int i=0; i<button.size(); i++) {
                if( button.get(i).getx() + button.get(i).gety() >= length) {
                    if (button.get(i).getIcon() == bblock) {
                        counter++;
                    }
                }
            }
        } else if (p.getIcon() == rblock) {
            for (int i=0; i<button.size(); i++) {
                if( button.get(i).getx() + button.get(i).gety() < length - 1) {
                    if (button.get(i).getIcon() == rblock) {
                        counter++;
                    }
                }
            }
        }
        if (counter == piece*(piece+1)/2) {
            pISWinner = true;
        }

        return pISWinner;
    }

    public void returnPiecesToFirstPosition () {

        for (int i = 0; i < length * length; i++) {

            if (i / length <= piece && piece - i % length - i / length > 0) { // player 1 pieces range
                button.get(i).setIcon(bblock);
            } else if (i / length > length - piece - 1 && i / length + i % length > 2 * length - 2 - piece) { // player 2 pieces range
                button.get(i).setIcon(rblock);
            } else {
                button.get(i).setIcon(null);
            }
        }
    }
}
