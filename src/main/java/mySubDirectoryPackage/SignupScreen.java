package mySubDirectoryPackage;

import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

/**
 * Clasa ecranului pentru crearea conturilor. Aceasta clasa introduce in baza de date un nou utilizator, cu atributele: username si parola encriptata
 */
public class SignupScreen extends JFrame implements ActionListener {

    JTextField usernameField;
    JPasswordField passwordField,rePasswordField;
    JLabel passwordLabel, usernameLabel, messageLabel, rePasswordLabel;

    JButton backButton, continueButton;

    public SignupScreen(){
        this.setTitle("MySubDirectory - signup");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(800,550);
        this.setLocationRelativeTo(null);
        this.setLayout(null);



        usernameLabel = new JLabel("Username: ");
        usernameLabel.setBounds(50, 128, 200, 24);
        this.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 128, 200, 24);
        this.add(usernameField);

        passwordLabel = new JLabel("Password: ");
        passwordLabel.setBounds(50, 176, 200, 24);
        this.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 176, 200, 24);
        this.add(passwordField);

        rePasswordLabel = new JLabel("Confirm Pass: ");
        rePasswordLabel.setBounds(50, 224, 200, 24);
        this.add(rePasswordLabel);

        rePasswordField = new JPasswordField();
        rePasswordField.setBounds(150, 224, 200, 24);
        this.add(rePasswordField);



        backButton = new JButton("Back");
        backButton.setBounds(75, 272, 100, 24);
        this.add(backButton);
        backButton.addActionListener(this);

        backButton.setBackground(new Color(238, 238, 238));
        backButton.setBorder(BorderFactory.createRaisedBevelBorder());
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);

        Border logoutOriginalBorder = BorderFactory.createRaisedBevelBorder();
        Border logoutClickedBorder = BorderFactory.createLoweredBevelBorder();

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                backButton.setBorder(logoutClickedBorder);
                backButton.setBackground(new Color(238, 238, 238));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                backButton.setBorder(logoutOriginalBorder);
            }
        });



        continueButton = new JButton("Continue");
        continueButton.setBounds(225, 272, 100, 24);
        this.add(continueButton);
        continueButton.addActionListener(this);

        continueButton.setBackground(new Color(238, 238, 238));
        continueButton.setBorder(BorderFactory.createRaisedBevelBorder());
        continueButton.setContentAreaFilled(false);
        continueButton.setFocusPainted(false);

        continueButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                continueButton.setBorder(logoutClickedBorder);
                continueButton.setBackground(new Color(238, 238, 238));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                continueButton.setBorder(logoutOriginalBorder);
            }
        });


        messageLabel = new JLabel();
        messageLabel.setBounds(75, 350, 425, 48);
        this.add(messageLabel);


        this.setVisible(true);
    }


    /**
     * Functia preia ca argumente username-ul si parola si daca acestea nu exista, le introduce in baza de date, encriptand parola inainte de introducere
     * @param username String cu username-ul utilizatorului
     * @param password String cu parola utilizatorului
     * @return 0-registered successfully,
     *         1-username-ul este folosit,
     *         2-alta eroare
     */
    public int signup(String username, String password){

        String jdbcURL = "jdbc:mysql://localhost:3306/mySubDirectoryDB";
        String dbUsername = "luca";
        String dbPassword = System.getenv("MYSQL_PASS");

        try{
            Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
            System.out.println("connected to db");

            String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            String query = "select * from users where username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                statement.close();
                return 1;
            }


            String insertQuery = "insert into users (username, encrypted_password) values (?, ?);";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, username);
            insertStatement.setString(2, encryptedPassword);
            insertStatement.executeUpdate();
            insertStatement.close();

            String createTableQuery = "create table if not exists " + username + " (" +
                    "name varchar(100) not null primary key, " +
                    "price int not null, " +
                    "billingPeriod varchar(100) not null, " +
                    "startDate varchar(100) not null, " +
                    "type varchar(100) not null" +
                    ");";

            Statement tableStatement = connection.createStatement();
            tableStatement.execute(createTableQuery);
            tableStatement.close();



            connection.close();
            return 0;
        } catch (SQLException e){
            System.out.println("Database connection error occured");
            e.printStackTrace();
        }

        return 2;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String message = "";
        if (e.getSource() == backButton) {
            new LoginScreen();
            this.setVisible(false);
            dispose();
        }

            if (e.getSource() == continueButton) {
                if (usernameField.getText().isEmpty() || passwordField.getPassword().length == 0) {
                    messageLabel.setText("You need to enter a username and a password");
                }else{
                    if (new String(passwordField.getPassword()).equals(new String(rePasswordField.getPassword()))) {
                        int signupResult = signup(usernameField.getText(), new String(passwordField.getPassword()));
                        if (signupResult == 0) {
                            message = "Signup successful! You can now go back to login.";
                        } else if (signupResult == 1) {
                            message = "Username already taken. Please choose another!";
                        }
                        messageLabel.setText(message);
                    } else {
                        messageLabel.setText("Passwords do not match");
                    }
                }
            }
    }

}

