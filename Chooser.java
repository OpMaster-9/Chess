import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Chooser {
    public static boolean botActivated = false;
    public static String ipAdress;
    public static String port1;
    public static void GUI (){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Chess");
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setLayout(null);
        frame.setSize(500, 350);
        frame.getContentPane().setBackground(new Color(50, 50, 50));

        Font font = new Font("Arial", Font.PLAIN, 35);

        JButton singleplayer = new JButton();
        singleplayer.setVisible(true);
        singleplayer.setBounds(25, 25, 450, 50);
        singleplayer.setBackground(new Color(100, 100, 100));
        singleplayer.setForeground(new Color(200, 200, 200));
        singleplayer.setBorder(null);
        singleplayer.setFont(font);
        singleplayer.setFocusable(false);
        singleplayer.setText("Singleplayer (Against Bot)");
        singleplayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.writeToFile("log.txt", "Neues Game");
                Main.clearFile("FEN.txt");
                botActivated = true;
                GUI.GUI();
                frame.setVisible(false);
            }
        });
        frame.add(singleplayer);

        JButton local = new JButton();
        local.setBounds(25, 100,450,50);
        local.setBackground(new Color(100, 100, 100));
        local.setForeground(new Color(200, 200, 200));
        local.setVisible(true);
        local.setBorder(null);
        local.setFont(font);
        local.setFocusable(false);
        local.setText("Multiplayer (local)");
        local.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.writeToFile("log.txt", "Neues Game");
                Main.clearFile("FEN.txt");
                botActivated = false;
                GUI.GUI();
                frame.setVisible(false);
            }
        });
        frame.add(local);

        JTextField ip = new JTextField();
        ip.setVisible(true);
        ip.setBounds(25,250,225,50);
        ip.setBackground(new Color(100, 100, 100));
        ip.setForeground(new Color(200, 200, 200));
        ip.setBorder(null);
        ip.setFont(font);
        ip.setText("Ip here");
        frame.add(ip);

        JTextField port = new JTextField();
        port.setVisible(true);
        port.setBounds(275,250,200,50);
        port.setBackground(new Color(100, 100, 100));
        port.setForeground(new Color(200, 200, 200));
        port.setBorder(null);
        port.setFont(font);
        port.setText("Port");
        frame.add(port);

        JButton online = new JButton();
        online.setVisible(true);
        online.setBounds(25, 175, 450, 50);
        online.setBackground(new Color(100, 100, 100));
        online.setForeground(new Color(200, 200, 200));
        online.setBorder(null);
        online.setFont(font);
        online.setText("Multiplayer (online)");
        online.setFocusable(false);
        online.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ipAdress = ip.getText();
                port1 = port.getText();
                Main.writeToFile("log.txt", "Neues Game");
                Main.clearFile("FEN.txt");
                try {
                    GUIOnline game = new GUIOnline();
                    game.GUI();
                } catch (IOException ex) {
                    System.out.println("Error");
                }
                frame.setVisible(false);
            }
        });
        frame.add(online);

        frame.setVisible(true);
    }
}
