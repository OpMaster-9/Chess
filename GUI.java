import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class GUI{
    private static JTextField timer_b = new JTextField();
    private static JTextField timer_w  = new JTextField();
    private static int feld1 = 0;
    private static int feld2 = 0;
    private static boolean feld1_b = true;
    private static int feld12 = 0;
    private static int feld22 = 0;
    private static boolean feld1_b2 = true;
    private static ChessTimer white = new ChessTimer(10, 0);
    private static ChessTimer black = new ChessTimer(10, 0);
    private static JButton[] feld = new JButton[64];
    private static boolean bot = true;
    private static int[] botMove = new int[3];
    private static boolean refresh = false;
    private static JTextField indicator = new JTextField("Weiß am Zug.");
    private static Bot bot1 = new Bot();
    public static void GUI(){
        JFrame fenster = new JFrame();
        fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenster.setTitle("Schach");
        fenster.setResizable(false);
        fenster.setLayout(null);
        fenster.setSize(900,1000);
        ImageIcon img = new ImageIcon("chess.png");
        fenster.setIconImage(img.getImage());

        indicator.setVisible(true);
        indicator.setEditable(false);
        indicator.setFocusable(false);
        indicator.setFont(new Font("Arial", Font.PLAIN, 20));
        indicator.setBounds(50,25,400,50);
        fenster.add(indicator);

        timer_w.setText("Weiß: " + white.getFormattedTime());
        timer_w.setVisible(true);
        timer_w.setEditable(false);
        timer_w.setFocusable(false);
        timer_w.setFont(new Font("Arial", Font.PLAIN, 20));
        timer_w.setBounds(475,25,175,50);
        fenster.add(timer_w);

        timer_b.setText("Schwarz: " + white.getFormattedTime());
        timer_b.setVisible(true);
        timer_b.setEditable(false);
        timer_b.setFocusable(false);
        timer_b.setFont(new Font("Arial", Font.PLAIN, 20));
        timer_b.setBounds(675,25,175,50);
        fenster.add(timer_b);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8,8));
        panel.setBounds(50,100,800,800);
        fenster.add(panel);


        int l = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                feld[l] = new JButton(Main.figur(Main.brett,l));
                feld[l].setFont(new Font("Monospaced", Font.PLAIN, 65));
                int pos = l;
                if ((pos >= 8 && pos < 16) || (pos >= 24 && pos < 32) || (pos >= 40 && pos < 48) || pos >= 56){
                    pos++;
                }
                if (pos % 2 == 0){
                    feld[l].setBackground(new Color(240,236,212));
                }else {
                    feld[l].setBackground(new Color(120,148,84));
                }
                final int index = l;
                feld[l].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton clickedButton = (JButton) e.getSource(); // Der Button, der das Event ausgelöst hat
                        if(Main.isRunning && Main.amZug == 0) {
                            Main.richtig = true;
                            if (feld1_b) {
                                feld1 = index;
                                feld1_b = false;
                                if (!Objects.equals(feld[index].getBackground(), new Color(250, 128, 114))) {
                                    if (Objects.equals(feld[index].getBackground(), new Color(240, 236, 212))) {
                                        feld[index].setBackground(new Color(170, 168, 153));
                                    } else {
                                        feld[index].setBackground(new Color(81, 99, 57));
                                    }
                                }
                                java.util.List<Integer> possibilities = MovementCheck.wohinGehtBittiBitti(Main.brett, index,Main.amZug);
                                List<Integer> possible = new ArrayList<>();
                                for (int m = 0; m < possibilities.size(); m++) {
                                    int[] temp = Arrays.copyOf(Main.brett, Main.brett.length);
                                    if (!Main.check(Main.moveWithoutCheck(temp, index, possibilities.get(m),Main.amZug), Main.findKing(temp, Main.amZug), Main.amZug)) {
                                        possible.add(possibilities.get(m));
                                    }
                                }
                                for (int k = 0; k < 64; k++) {
                                    if (possible.contains(k)) {
                                        if (Objects.equals(feld[k].getBackground(), new Color(240, 236, 212))) {
                                            feld[k].setBackground(new Color(170, 168, 153));
                                        } else {
                                            feld[k].setBackground(new Color(81, 99, 57));
                                        }
                                    }
                                }
                            } else {
                                feld2 = index;
                                feld1_b = true;
                                int[] temp = Arrays.copyOf(Main.brett, Main.brett.length);
                                Main.writeToFile("log.txt",Main.getFEN(false));
                                Main.writeToFile("log.txt",Main.zahlZuFeld(feld1) + ", " + Main.zahlZuFeld(feld2));
                                Main.brett = Main.move(Main.brett, feld1, feld2);
                                Main.writeToFile("FEN.txt",Main.getFEN(true));
                                if (Main.countOccurrences("FEN.txt",Main.getFEN(true)) >= 3){
                                    Main.isRunning = false;
                                    indicator.setText("Unentschieden durch Wiederholung.");
                                }
                                if (Main.movementRule >= 50){
                                    Main.isRunning = false;
                                    indicator.setText("Unentschieden durch 50 Zug regel.");
                                }
                                if (Main.richtig) {
                                    Main.letzte = Arrays.copyOf(temp, temp.length);
                                }
                                for (int k = 0; k < 64; k++) {
                                    int l = k;
                                    if ((k >= 8 && k < 16) || (k >= 24 && k < 32) || (k >= 40 && k < 48) || k >= 56){
                                        l++;
                                    }
                                    if (l % 2 == 0){
                                        feld[k].setBackground(new Color(240,236,212));
                                    }else {
                                        feld[k].setBackground(new Color(120,148,84));
                                    }
                                }
                            }
                            if (Main.richtig && feld1_b) {
                                if (Main.amZug == 0) {
                                    Main.amZug = 1;
                                } else {
                                    Main.amZug = 0;
                                }
                            }
                            int l = 0;
                            for (int i = 0; i < 8; i++) {
                                for (int j = 0; j < 8; j++) {
                                    feld[l].setText(Main.figur(Main.brett, l));
                                    l += 8;
                                }
                                l -= 63;
                            }
                            white.stop();
                            black.stop();
                            if (Main.isRunning) {
                                if (Main.amZug == 0) {
                                    indicator.setText("Weiß am Zug.");
                                    white.start();
                                } else {
                                    indicator.setText("Schwarz am Zug.");
                                    black.start();
                                }
                            }
                            if (Main.stalemateTest(Main.brett, Main.findKing(Main.brett, Main.amZug))) {
                                indicator.setText("Remis");
                                System.out.println("Remis");
                                Main.isRunning = false;
                            }
                            if (Main.check(Main.brett, Main.findKing(Main.brett, Main.amZug), Main.amZug)) {
                                System.out.println("Schach" + Main.amZug);
                                feld[Main.findKing(Main.brett, Main.amZug)].setBackground(new Color(250,128,114));
                                if (Main.mattTest(Main.brett, Main.amZug)) {
                                    if (Main.amZug == 0) {
                                        indicator.setText("Schwarz hat gewonnen");
                                        System.out.println("Schwarz hat gewonnen");
                                        Main.isRunning = false;
                                    } else {
                                        indicator.setText("Weiß hat gewonnen.");
                                        System.out.println("Weiß hat gewonnen");
                                        Main.isRunning = false;
                                    }
                                }
                            }
                            if(Main.amZug == 1 && bot) {
                                bot = false;
                                Thread thread = new Thread(() -> {
                                    botMove = Arrays.copyOf(bot1.miniMax(Main.brett, 1, 5, false,Integer.MIN_VALUE,Integer.MAX_VALUE),3);
                                    System.out.println(botMove[0]);
                                    System.out.println(Main.zahlZuFeld(botMove[1]));
                                    System.out.println(Main.zahlZuFeld(botMove[2]));
                                    GUI.simulateButtonPress(botMove[1]);
                                    GUI.simulateButtonPress(botMove[2]);
                                    System.out.println("Berechnet.");
                                });
                                thread.start();

                            }else {
                                if (Main.amZug == 0){
                                    bot = true;
                                }
                            }
                        }
                    }
                });
                feld[l].setFocusable(false);
                panel.add(feld[l]);
                l += 8;
            }
            l -= 63;
        }
        fenster.setVisible(true);
        new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (white.getRemainingTime() > 0) {
                    timer_w.setText("Weiß: " + white.getFormattedTime());
                }else {
                    Main.isRunning = false;
                    timer_w.setText("Weiß: " + white.getFormattedTime());
                    indicator.setText("Schwarz hat gewonnen.");
                }
                if (black.getRemainingTime() > 0) {
                    timer_b.setText("Schwarz: " + black.getFormattedTime());
                }else {
                    Main.isRunning = false;
                    timer_b.setText("Schwarz: " + black.getFormattedTime());
                    indicator.setText("Weiß hat gewonnen.");
                }
            }
        }).start();
    }
    public static void refresh(){
        int l = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                feld[l].setText(Main.figur(Main.brett, l));
                l += 8;
            }
            l -= 63;
        }
    }
    public static void pressButton(int pos){
        feld[pos].doClick();
    }
    public static void simulateButtonPress(int index){
        if(Main.isRunning) {
            Main.richtig = true;
            if (feld1_b2) {
                feld12 = index;
                feld1_b2 = false;
                if (!Objects.equals(feld[index].getBackground(), new Color(250, 128, 114))) {
                    if (Objects.equals(feld[index].getBackground(), new Color(240, 236, 212))) {
                        feld[index].setBackground(new Color(170, 168, 153));
                    } else {
                        feld[index].setBackground(new Color(81, 99, 57));
                    }
                }
                java.util.List<Integer> possibilities = MovementCheck.wohinGehtBittiBitti(Main.brett, index,Main.amZug);
                List<Integer> possible = new ArrayList<>();
                for (int m = 0; m < possibilities.size(); m++) {
                    int[] temp = Arrays.copyOf(Main.brett, Main.brett.length);
                    if (!Main.check(Main.moveWithoutCheck(temp, index, possibilities.get(m),Main.amZug), Main.findKing(temp, Main.amZug), Main.amZug)) {
                        possible.add(possibilities.get(m));
                    }
                }
                for (int k = 0; k < 64; k++) {
                    if (possible.contains(k)) {
                        if (Objects.equals(feld[k].getBackground(), new Color(240, 236, 212))) {
                            feld[k].setBackground(new Color(170, 168, 153));
                        } else {
                            feld[k].setBackground(new Color(81, 99, 57));
                        }
                    }
                }
            } else {
                feld22 = index;
                feld1_b2 = true;
                int[] temp = Arrays.copyOf(Main.brett, Main.brett.length);
                Main.brett = Main.move(Main.brett, feld12, feld22);
                Main.writeToFile("FEN.txt",Main.getFEN(true));
                System.out.println("occurances: " + Main.countOccurrences("FEN.txt",Main.getFEN(true)));
                if (Main.countOccurrences("FEN.txt",Main.getFEN(true)) >= 3){
                    Main.isRunning = false;
                    indicator.setText("Unentschieden durch Wiederholung.");
                }
                if (Main.movementRule >= 50){
                    Main.isRunning = false;
                    indicator.setText("Unentschieden durch 50 Zug regel.");
                }
                if (Main.richtig) {
                    Main.letzte = Arrays.copyOf(temp, temp.length);
                }
                for (int k = 0; k < 64; k++) {
                    int l = k;
                    if ((k >= 8 && k < 16) || (k >= 24 && k < 32) || (k >= 40 && k < 48) || k >= 56){
                        l++;
                    }
                    if (l % 2 == 0){
                        feld[k].setBackground(new Color(240,236,212));
                    }else {
                        feld[k].setBackground(new Color(120,148,84));
                    }
                }
            }
            if (Main.richtig && feld1_b2) {
                if (Main.amZug == 0) {
                    Main.amZug = 1;
                } else {
                    Main.amZug = 0;
                }
            }
            int l = 0;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    feld[l].setText(Main.figur(Main.brett, l));
                    l += 8;
                }
                l -= 63;
            }
            white.stop();
            black.stop();
            if (Main.isRunning) {
                if (Main.amZug == 0) {
                    indicator.setText("Weiß am Zug.");
                    white.start();
                } else {
                    indicator.setText("Schwarz am Zug.");
                    black.start();
                }
            }
            if (Main.stalemateTest(Main.brett, Main.findKing(Main.brett, Main.amZug))) {
                indicator.setText("Remis");
                System.out.println("Remis");
                Main.isRunning = false;
            }
            if (Main.check(Main.brett, Main.findKing(Main.brett, Main.amZug), Main.amZug)) {
                System.out.println("Schach" + Main.amZug);
                feld[Main.findKing(Main.brett, Main.amZug)].setBackground(new Color(250,128,114));
                if (Main.mattTest(Main.brett, Main.amZug)) {
                    if (Main.amZug == 0) {
                        indicator.setText("Schwarz hat gewonnen");
                        System.out.println("Schwarz hat gewonnen");
                        Main.isRunning = false;
                    } else {
                        indicator.setText("Weiß hat gewonnen.");
                        System.out.println("Weiß hat gewonnen");
                        Main.isRunning = false;
                    }
                }
            }
            Main.writeToFile("log.txt",Main.getFEN(false));
            Main.writeToFile("log.txt",Main.zahlZuFeld(feld12) + ", " + Main.zahlZuFeld(feld22));
        }
    }
}