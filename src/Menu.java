import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Scanner;

public class Menu {

    Icon halma = new ImageIcon("img\\halma.png"); // halma text png
    JFrame Menu = new JFrame("HALMA MENU"); // menu JFrame
    JPanel panel = new JPanel();
    Player p1;
    Player p2;
    Icon bblock = new ImageIcon("img\\blueblock.png"); // blue block
    Icon rblock = new ImageIcon("img\\redblock.png"); // red block
    String [][] records;


    public static void main(String[] args) {
        ImageIcon i = new ImageIcon("img\\menu.png");
        Image img = i.getImage();
        Menu menu = new Menu(img); // show game menu

    }

    public Menu(Image icon) {

        // --------------------------------- Creating Menu Frame ------------------------------------
        Menu.setIconImage(icon);

        panel.setLayout(null); // JPanel should don't have any default Layout
        panel.setBackground(new Color(188, 228, 255));
        // For Image at Top of Menu
        JLabel halmalabel = new JLabel(resizeIcon(halma, 100, 300));
        halmalabel.setBounds(95, 30 , 300, 100);
        panel.add(halmalabel);

        Menu.setSize(500,400);
        panel.setBounds(0, 0 , 500, 400);

        // Create 3 JButtons ,Edit and Add to JPanel
        JButton [] btn = new JButton[3];
        btn[0] = new JButton("Start");
        btn[1] = new JButton("Records");
        btn[2] = new JButton("Exit");

        btn[0].setFocusPainted(false);
        btn[0].setForeground(new Color(50,250,100));
        btn[0].setBackground(new Color(70,130, 250));

        btn[0].setBounds(195, 170, 100, 35); // Start
        btn[1].setBounds(195, 220, 100, 35); // Record
        btn[2].setBounds(195, 270, 100, 35); // Exit


        /*try {
            FileWriter file = new FileWriter("records.txt");
        } catch (IOException e) {
            e.getStackTrace();
        }*/

        for (int i=0; i<3; i++) {
            // add buttons to frame
            panel.add(btn[i]);

            // ------------------------ Add Mouse listener to three button's -----------------------
            btn[i].addMouseListener(new MouseAdapter() {

                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    JButton selection = (JButton) e.getSource();

                    if (selection.getText() == "Start") { // if select start buttons

                        // ----------------------------------- Getting Players Data and size of board game -----------------------------------------
                        p1 = new Player(JOptionPane.showInputDialog("Player 1 Full Name: "));
                        while (p1.getName() == null || p1.getName() == "") { // if player1 dont' write his name
                            p1 = new Player(JOptionPane.showInputDialog("Please Enter Player 1 Full Name: "));
                        }

                        p2 = new Player(JOptionPane.showInputDialog("Player 2 Full Name: ")); // if player2 dont' write his name
                        while (p2.getName() == null || p2.getName() == "\n") {
                            p2 = new Player(JOptionPane.showInputDialog("Please Enter Player 2 Full Name: "));
                        }

                        int length = Integer.parseInt(JOptionPane.showInputDialog("Number of Table Rows or Table Column: "));
                        while (length%2!=0 || length==0) {
                            length = Integer.parseInt(JOptionPane.showInputDialog("Please Enter True Entrance.(Even Integer Except Zero)\n" + "Number of Table Rows or Table Column: "));
                        }

                        int rowsOfPiece = Integer.parseInt(JOptionPane.showInputDialog("Number Rows of pieces: "));
                        while (rowsOfPiece>=length || rowsOfPiece==0) {
                            rowsOfPiece = Integer.parseInt(JOptionPane.showInputDialog("Please Enter True Entrance.(Smaller Than Table length)\n" + "Number Rows of pieces: "));
                        }

                        // ------------ create halma game frame -------------
                        Halma h = new Halma (length,
                                            rowsOfPiece,
                                            p1,
                                            p2,
                                            bblock,
                                            rblock);
                        Menu.setVisible(false);
                        h.Game(Menu);

                    }
                    else if (selection.getText() == "Records") { // if select records button
                        try {
                            // --------------------------- Create FileReader and Scanner for find size of Record array
                            FileReader reader1 = new FileReader("records.txt");
                            Scanner scanner1 = new Scanner(reader1);
                            int i = 0, sizeOfArray = 0;
                            while (scanner1.hasNext()) {
                                scanner1.nextLine();
                                scanner1.nextLine();
                                sizeOfArray++;
                            }
                            records = new String[sizeOfArray][2];

                            // --------------------------- Create another FileReader and Scanner for assign value to String array
                            FileReader reader2 = new FileReader("records.txt");
                            Scanner scanner2 = new Scanner(reader2);
                            while (scanner2.hasNext()) {
                                String name = scanner2.nextLine();
                                String score = scanner2.nextLine();
                                records[i][0] = name;
                                records[i][1] = score;
                                i++;
                            }

                            // Create Frame for show Record in JTable with String Array
                            JFrame record = new JFrame("Records");
                            String header[] = {"Name", "Score"};

                            JTable table = new JTable(records, header);
                            JScrollPane scrollPane = new JScrollPane(table);

                            record.add(scrollPane);
                            record.setSize(400, 400);

                            record.setVisible(true);
                            record.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            record.setResizable(false);
                            record.setLocationRelativeTo(null);

                        } catch (IOException ex) {
                            ex.getStackTrace();
                        }
                    }
                    else if (selection.getText() == "Exit") { // if select exit buttons
                        Menu.dispose();
                    }
                }
            });
        }


        Menu.add(panel);
        Menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Menu.setLocationRelativeTo(null);
        Menu.setVisible(true);
    }

    public Icon resizeIcon(Icon ic, int height, int width) {  // for resize Halma LOGO
        ImageIcon im = (ImageIcon) ic;
        Image img = im.getImage();

        Image img2 = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon output = new ImageIcon(img2);

        return output;
    }

}
