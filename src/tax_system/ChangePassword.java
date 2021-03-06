package tax_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ChangePassword extends JFrame {

    private JLabel notification = new JLabel();

    // criptare parole
    private String encypherPassword (String password) {
        String cypher = "";
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            cypher = hexString.toString().toUpperCase();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return cypher;
    }

    public ChangePassword () {
        super ("Schimbarea parolei");
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setMinimumSize(new Dimension(600, 400));
        this.setLayout(new FlowLayout());
        this.setIconImage(new ImageIcon("icons\\Apps-preferences-desktop-user-password-icon.png").getImage());
        this.getContentPane().setBackground(new Color(66, 167, 244));

        JPanel panel = new JPanel();
        panel.setBackground(new Color(66, 167, 244));
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setMinimumSize(new Dimension (400, 400));
        JTextField username = new JTextField(25);
        JPasswordField old_password = new JPasswordField(25);
        JPasswordField new_password = new JPasswordField(25);
        JLabel username_info = new JLabel("Numele utilizatorului");
        JLabel old_password_info = new JLabel("Vechea parolă");
        JLabel new_password_info = new JLabel("Noua parolă");
        username_info.setForeground(Color.white);
        old_password_info.setForeground(Color.white);
        new_password_info.setForeground(Color.white);
        JButton change = new JButton("Salvează setările");
        JButton close = new JButton("Închide");
        username_info.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        change.setAlignmentX(JButton.CENTER_ALIGNMENT);
        close.setAlignmentX(JButton.CENTER_ALIGNMENT);
        old_password_info.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        new_password_info.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        username_info.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        old_password_info.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        new_password_info.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        username.setFont(new Font("Calibri Light", Font.PLAIN, 20));
        old_password.setFont(new Font("Calibri Light", Font.PLAIN, 20));
        new_password.setFont(new Font("Calibri Light", Font.PLAIN, 20));
        change.setFont(new Font("Calibri Light", Font.PLAIN, 20));
        close.setFont(new Font("Calibri Light", Font.PLAIN, 20));
        this.notification.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        this.notification.setFont(new Font("Segoe UI", Font.PLAIN, 20));

        change.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton) e.getSource();
                if (button.getText().equals(change.getText())) {
                    if (username.getText().length() == 0
                            || old_password.getText().length() == 0
                            || new_password.getText().length() == 0) {
                        notification.setText("Completați toate câmpurile!");
                        // toate campurile trebuie sa fie completate
                        notification.setForeground(new Color(186, 26, 63));
                    } else {
                        HashMap<String, String> map = new HashMap <>();
                        Scanner scan = null;
                        String user = username.getText();
                        String old_pass = old_password.getText();
                        String new_pass = new_password.getText();
                        if (new_pass.length() == 0) { // parola nu trebuie sa fie goala
                            notification.setForeground(new Color(186, 26, 63));
                            notification.setText("Parola trebuie să conțină caractere!");
                            return;
                        } // criptare parole
                        old_pass = encypherPassword(old_pass);
                        new_pass = encypherPassword(new_pass);
                        try {
                            scan = new Scanner(new File("login.txt"));
                            boolean found = false; // verificare daca username-ul e corect sau nu
                            while (scan.hasNextLine()) {
                                String line = scan.nextLine();
                                String[] data = line.split(" ");
                                map.put(data[0], data[1]);
                                if (data[0].equals(user)) {
                                    if (data[1].equals(old_pass)) {
                                        found = true;
                                        map.put(data[0], new_pass); // adaugam in dictionar username-ul si parola noua
                                    } else {
                                        notification.setForeground(Color.RED);
                                        notification.setText("Parola veche introdusă greșit!");
                                        break;
                                    }
                                }
                            }
                            if (found) { // rescriem in login.txt baza de date ale user-ilor
                                System.setOut(new PrintStream(new File("login.txt")));
                                notification.setForeground(new Color(22, 122, 72));
                                notification.setText("Parola schimbată cu succes!");
                                for (Map.Entry<String, String> entry: map.entrySet()) {
                                    System.out.println(entry.getKey() + " " + entry.getValue());
                                }
                            } else {
                                notification.setForeground(new Color(186, 26, 63));
                                notification.setText("Username greșit!");
                            }
                        } catch (FileNotFoundException k) {
                            k.printStackTrace();
                        }
                    }
                }
            }
        });

        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton) e.getSource();
                if (button.getText().equals(close.getText())) {
                    dispose();
                    setVisible(false);
                }
            }
        });

        panel.add(Box.createRigidArea(new Dimension(5,10)));
        panel.add(username_info);
        panel.add(Box.createRigidArea(new Dimension(5,5)));
        panel.add(username);
        panel.add(Box.createRigidArea(new Dimension(5,5)));
        panel.add(old_password_info);
        panel.add(Box.createRigidArea(new Dimension(5,5)));
        panel.add(old_password);
        panel.add(Box.createRigidArea(new Dimension(5,5)));
        panel.add(new_password_info);
        panel.add(Box.createRigidArea(new Dimension(5,5)));
        panel.add(new_password);
        panel.add(Box.createRigidArea(new Dimension(5,10)));
        panel.add(change);
        panel.add(Box.createRigidArea(new Dimension(5,10)));
        panel.add(close);
        panel.add(Box.createRigidArea(new Dimension(5,10)));
        panel.add(this.notification);

        this.add(panel);
        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }
}
