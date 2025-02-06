package mySubDirectoryPackage;

import org.mindrot.jbcrypt.BCrypt;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Clasa ecranului de login care veirifca si valideaza datele de login.
 * Dupa logarea cu succes, se creaza ecranul principal, catre care este transmis username-ul utilizatorului
 */
public class LoginScreen extends JFrame implements ActionListener {

    JTextField usernameField;
    JPasswordField passwordField;
    JLabel passwordLabel, usernameLabel, messageLabel;

    JButton loginButton, signupButton;

    LoginScreen(){
        this.setTitle("MySubDirectory - login");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(800,550);
        this.setLocationRelativeTo(null);
        this.setLayout(null);



        usernameLabel = new JLabel("Username: ");
        usernameLabel.setBounds(50, 128, 100, 24);
        this.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 128, 200, 24);
        this.add(usernameField);

        passwordLabel = new JLabel("Password: ");
        passwordLabel.setBounds(50, 176, 100, 24);
        this.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 176, 200, 24);
        this.add(passwordField);



        loginButton = new JButton("Login");
        loginButton.setBounds(75, 272, 100, 24);
        this.add(loginButton);
        loginButton.addActionListener(this);

        loginButton.setBackground(new Color(238, 238, 238));
        loginButton.setBorder(BorderFactory.createRaisedBevelBorder());
        loginButton.setContentAreaFilled(false);
        loginButton.setFocusPainted(false);

        Border logoutOriginalBorder = BorderFactory.createRaisedBevelBorder();
        Border logoutClickedBorder = BorderFactory.createLoweredBevelBorder();

        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                loginButton.setBorder(logoutClickedBorder);
                loginButton.setBackground(new Color(238, 238, 238));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                loginButton.setBorder(logoutOriginalBorder);
            }
        });

        signupButton = new JButton("Signup");
        signupButton.setBounds(225, 272, 100, 24);
        this.add(signupButton);
        signupButton.addActionListener(this);

        signupButton.setBackground(new Color(238, 238, 238));
        signupButton.setBorder(BorderFactory.createRaisedBevelBorder());
        signupButton.setContentAreaFilled(false);
        signupButton.setFocusPainted(false);

        signupButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                signupButton.setBorder(logoutClickedBorder);
                signupButton.setBackground(new Color(238, 238, 238));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                signupButton.setBorder(logoutOriginalBorder);
            }
        });

        messageLabel = new JLabel();
        messageLabel.setBounds(75, 350, 425, 48);
        this.add(messageLabel);


        this.setVisible(true);
    }




    /**
     * Functia compara cu utilizatorii si parolele encriptate din baza de date, si returneaza un cod conform cu starea operatiei de login
     * @param username String cu username-ul utilizatorului
     * @param password String cu parola utilizatorului
     * @return 0-login successful, 1-parola incorecta, 2-userul nu exista, 3-alta eroare
     */
    public int login(String username, String password){
        String jdbcURL = "jdbc:mysql://localhost:3306/mySubDirectoryDB";
        String dbUsername = "luca";
        String dbPassword = System.getenv("MYSQL_PASS");

        try{
            Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
            System.out.println("connected to db");


            String query = "select * from users where username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                if(BCrypt.checkpw(password, resultSet.getString("encrypted_password"))){
                    statement.close();
                    connection.close();
                    new MainScreen(username);
                    return 0;
                }
                else{
                    statement.close();
                    connection.close();
                    return 1;
                }

            }
            else{
                statement.close();
                connection.close();
                return 2;
            }


        } catch (SQLException e){
            System.out.println("Database connection error occured");
            e.printStackTrace();
        }

        return 3;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == loginButton) {
            if (usernameField.getText().isEmpty() || passwordField.getPassword().length == 0) {
                messageLabel.setText("You need to enter a username and a password");
            } else {
                int loginResult = login(usernameField.getText(), new String(passwordField.getPassword()));
                String message = "";
                if (loginResult == 0) {
                    message = "Welcome " + usernameField.getText() + "!";
                    this.setVisible(false);
                    dispose();
                } else if (loginResult == 1) {
                    message = "Incorrect password";
                } else if (loginResult == 2) {
                    message = "User does not exist. Please sign up!";
                }
                messageLabel.setText(message);
            }
        }
        if(e.getSource() == signupButton){
            new SignupScreen();
            this.setVisible(false);
            dispose();
        }
    }
}