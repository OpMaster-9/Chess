import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class GUIOnline{
    private static JTextField timer_b = new JTextField();
    private static JTextField timer_w  = new JTextField();
    private static int feld1 = 0;
    private static int feld2 = 0;
    private static boolean feld1_b = true;
    private static ChessTimer white = new ChessTimer(10, 0);
    private static ChessTimer black = new ChessTimer(10, 0);
    private static JButton[] feld = new JButton[64];
    private static JTextField indicator = new JTextField("Weiß am Zug.");
    private static int clientColour;
    private static String move;
    public void GUI() throws IOException {

        Socket socket = new Socket(Chooser.ip, Integer.parseInt(Chooser.port)); // Connect to server
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        clientColour = Integer.parseInt(input.readUTF());

        System.out.println("connected");

        JFrame fenster = new JFrame();
        fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (clientColour == 0){
            fenster.setTitle("Schach(white)");
        }else {
            fenster.setTitle("Schach(black)");
        }
        fenster.setResizable(false);
        fenster.setLayout(null);
        fenster.setSize(900,1000);
        fenster.getContentPane().setBackground(new Color(50,50,50));

        Font font = new Font("Arial", Font.PLAIN, 20);

        indicator.setVisible(true);
        indicator.setEditable(false);
        indicator.setFocusable(false);
        indicator.setFont(font);
        indicator.setBounds(50,25,400,50);
        indicator.setBorder(null);
        indicator.setBackground(new Color(100,100,100));
        indicator.setForeground(new Color(200,200,200));
        fenster.add(indicator);

        timer_w.setText("Weiß: " + white.getFormattedTime());
        timer_w.setVisible(true);
        timer_w.setEditable(false);
        timer_w.setFocusable(false);
        timer_w.setFont(font);
        timer_w.setBackground(new Color(100,100,100));
        timer_w.setForeground(new Color(200,200,200));
        timer_w.setBounds(475,25,175,50);
        timer_w.setBorder(null);
        fenster.add(timer_w);

        timer_b.setText("Schwarz: " + white.getFormattedTime());
        timer_b.setVisible(true);
        timer_b.setEditable(false);
        timer_b.setFocusable(false);
        timer_b.setFont(font);
        timer_b.setBackground(new Color(100,100,100));
        timer_b.setForeground(new Color(200,200,200));
        timer_b.setBounds(675,25,175,50);
        timer_b.setBorder(null);
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
                        if(Main.isRunning && Main.amZug == clientColour) {
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
                                Main.brett = Main.move(Main.brett,feld1,feld2,clientColour);
                                System.out.println("made move");
                                if (Main.richtig) {
                                    System.out.println("sending move");
                                    Main.writeToFile("FEN.txt", Main.getFEN(true));
                                    try {
                                        output.writeUTF(intArrayToString(Main.brett) + " " + intArrayToString(Main.letzte) + " " + booleanArrayToString(Main.hasMoved) + " " + Main.moveCounter + " " + Main.movementRule);
                                        System.out.println("sent move");
                                    } catch (IOException ex) {
                                        System.out.println("Error while sending.");
                                    }
                                }
                                if (Main.countOccurrences("FEN.txt",Main.getFEN(true)) >= 3){
                                    Main.isRunning = false;
                                    indicator.setText("Unentschieden durch Wiederholung.");
                                    Main.writeToFile("log.txt","Unentschieden durch Wiederholung.");
                                }
                                if (Main.movementRule >= 50){
                                    Main.isRunning = false;
                                    indicator.setText("Unentschieden durch 50 Zug regel.");
                                    Main.writeToFile("log.txt","Unentschieden durch 50 Zug regel.");
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
                            if (Main.stalemateTest(Main.brett, Main.amZug)) {
                                indicator.setText("Remis");
                                System.out.println("Remis");
                                Main.writeToFile("log.txt","Remis");
                                Main.isRunning = false;
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
                            if (Main.check(Main.brett, Main.findKing(Main.brett, Main.amZug), Main.amZug)) {
                                feld[Main.findKing(Main.brett, Main.amZug)].setBackground(new Color(250,128,114));
                                if (Main.mattTest(Main.brett, Main.amZug)) {
                                    if (Main.amZug == 0) {
                                        indicator.setText("Schwarz hat gewonnen");
                                        System.out.println("Schwarz hat gewonnen");
                                        Main.writeToFile("log.txt","Schwarz hat gewonnen");
                                        Main.isRunning = false;
                                    } else {
                                        indicator.setText("Weiß hat gewonnen.");
                                        System.out.println("Weiß hat gewonnen");
                                        Main.writeToFile("log.txt","Weiß hat gewonnen");
                                        Main.isRunning = false;
                                    }
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
        new Thread(() -> {
            while (Main.isRunning) {
                try {
                    String inputString = input.readUTF();
                    String[] parts = inputString.split(" ");
                    Main.brett = stringToIntArray(parts[0]);
                    Main.letzte = stringToIntArray(parts[1]);
                    Main.hasMoved = stringToBooleanArray(parts[2]);
                    Main.moveCounter = Integer.parseInt(parts[3]);
                    Main.movementRule = Integer.parseInt(parts[4]);
                    if (Main.amZug == 0) {
                        Main.amZug = 1;
                    } else {
                        Main.amZug = 0;
                    }
                    refresh();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();

    }
    public void refresh(){
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
        if (Main.amZug == 0) {
            indicator.setText("Weiß am Zug.");
            white.start();
        } else {
            indicator.setText("Schwarz am Zug.");
            black.start();
        }
    }
    public static void pressButton(int pos){
        feld[pos].doClick();
    }
    public static String intArrayToString(int[] array) {
        return Arrays.stream(array)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));
    }
    public static int[] stringToIntArray(String str) {
        return Arrays.stream(str.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
    }
    public static String booleanArrayToString(boolean[] array) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) {
                sb.append(",");  // Append comma for all but the last element
            }
        }
        return sb.toString();
    }

    public static boolean[] stringToBooleanArray(String str) {
        String[] parts = str.split(",");
        boolean[] result = new boolean[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Boolean.parseBoolean(parts[i]);
        }
        return result;
    }

}