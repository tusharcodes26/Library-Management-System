package ui;

import service.AdminService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginFrame() {

        setTitle("Admin Login");

        setSize(400,250);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel =
                new JPanel(new GridLayout(3,2,10,10));

        panel.add(new JLabel("Username"));

        txtUsername =
                new JTextField();

        panel.add(txtUsername);

        panel.add(new JLabel("Password"));

        txtPassword =
                new JPasswordField();

        panel.add(txtPassword);

        JButton btnLogin =
                new JButton("Login");

        panel.add(btnLogin);

        add(panel);

        btnLogin.addActionListener(e -> {

            String username =
                    txtUsername.getText();

            String password =
                    new String(
                            txtPassword.getPassword()
                    );

            AdminService service =
                    new AdminService();

            if(service.login(
                    username,
                    password)) {

                JOptionPane.showMessageDialog(
                        null,
                        "Login Successful"
                );

                dispose();

                new LibraryGUI();

            } else {

                JOptionPane.showMessageDialog(
                        null,
                        "Invalid Username or Password"
                );
            }
        });

        setVisible(true);
    }
}