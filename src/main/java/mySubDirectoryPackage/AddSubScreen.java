package mySubDirectoryPackage;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

/**
 * Clasa ecranului pentru adaugarea noilor subscriptii.
 * Prin interfata grafica sunt preluate datele despre subscriptia care trebuie adaugata.
 * Se poate continua cu adaugarea informatiilor in baza de date, sau anularea.
 */
public class AddSubScreen extends JFrame implements ActionListener {

    String username;
    JTextField nameFiled, priceField, billingFiled, startDateField, typeFiled;
    JLabel nameLabel, priceLabel, billingLabel, startDateLabel, typeLabel, messageLabel, dateExampleLabel, billingExampleLabel, typeExampleLabel;

    JButton backButton, continueButton;

    public AddSubScreen(String username){
        this.setTitle("MySubDirectory - add subscription");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(800,550);
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        this.username = username;


        nameLabel = new JLabel("Name: ");
        nameLabel.setBounds(50, 50, 200, 24);
        this.add(nameLabel);

        nameFiled = new JTextField();
        nameFiled.setBounds(160, 50, 200, 24);
        this.add(nameFiled);


        priceLabel = new JLabel("Price: ");
        priceLabel.setBounds(50, 98, 200, 24);
        this.add(priceLabel);

        priceField = new JTextField();
        priceField.setBounds(160, 98, 200, 24);
        this.add(priceField);


        billingLabel = new JLabel("Billing period: ");
        billingLabel.setBounds(50, 146, 200, 24);
        this.add(billingLabel);

        billingExampleLabel = new JLabel("(monthly/ yearly)");
        billingExampleLabel.setBounds(400, 146, 200, 24);
        this.add(billingExampleLabel);


        billingFiled = new JTextField();
        billingFiled.setBounds(160, 146, 200, 24);
        this.add(billingFiled);


        startDateLabel = new JLabel("startDate: ");
        startDateLabel.setBounds(50, 194, 200, 24);
        this.add(startDateLabel);

        startDateField = new JTextField();
        startDateField.setBounds(160, 194, 200, 24);
        this.add(startDateField);

        dateExampleLabel = new JLabel("format: YYYY-MM-DD");
        dateExampleLabel.setBounds(400, 192, 200, 24);
        this.add(dateExampleLabel);


        typeLabel = new JLabel("Type: ");
        typeLabel.setBounds(50, 242, 200, 24);
        this.add(typeLabel);

        typeExampleLabel = new JLabel("(student/ individual/ family)");
        typeExampleLabel.setBounds(400, 242, 200, 24);
        this.add(typeExampleLabel);


        typeFiled = new JTextField();
        typeFiled.setBounds(160, 242, 200, 24);
        this.add(typeFiled);



        backButton = new JButton("Back");
        backButton.setBounds(75, 290, 100, 24);
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
        continueButton.setBounds(225, 290, 100, 24);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == backButton){
            new MainScreen(username);
            this.setVisible(false);
            dispose();
        }

        if(e.getSource() == continueButton){
            if(nameFiled.getText().isEmpty() || priceField.getText().isEmpty() || billingFiled.getText().isEmpty() || typeFiled.getText().isEmpty()){
                messageLabel.setText("You need fill all the fields");
            }
            else{
                String subName;
                float subPrice = 0;
                String subBillingPeriod = "n/a";
                String subStartDate = "01-01-1970";
                String subType;

                boolean allPassed = true;




                subName = nameFiled.getText().replaceAll("\\s+", " ");
                subName = subName.replaceAll(" ", "_");

               String priceRegex = "\\d+";
                if(!priceField.getText().matches(priceRegex)){
                    messageLabel.setText("Wrong price format");
                    allPassed = false;
                }
                else{
                    subPrice = Float.parseFloat(priceField.getText());
                }

                String billingRegex = "monthly|yearly";
                if(!billingFiled.getText().matches(billingRegex)){
                    messageLabel.setText("Wrong billing period format");
                    allPassed = false;
                }
                else{
                    subBillingPeriod = billingFiled.getText();
                }

//                String dateRegex = "^[0-9]{4}-(0[1-9])|1[012]-(0[1-9])|([1-3][0-9])$";
                String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";

                if(!startDateField.getText().matches(dateRegex)){
                    messageLabel.setText("Wrong date format");
                    allPassed = false;
                }
                else{
                    subStartDate = startDateField.getText();
                }

                subType = typeFiled.getText().replaceAll("\\s+", " ");
                subType = subType.replaceAll(" ", "_");

                if(allPassed) {

                    String jdbcURL = "jdbc:mysql://localhost:3306/mySubDirectoryDB";
                    String dbUsername = "luca";
                    String dbPassword = System.getenv("MYSQL_PASS");


                    try{
                        Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
                        System.out.println("connected to db");


                        String insertQuery = "insert into " + username + " (name, price, billingPeriod, startDate, type) values (?, ?, ?, ?, ?);";
                        PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                        insertStatement.setString(1, subName);
                        insertStatement.setFloat(2, subPrice);
                        insertStatement.setString(3, subBillingPeriod);
                        insertStatement.setString(4, subStartDate);
                        insertStatement.setString(5, subType);

                        insertStatement.executeUpdate();
                        insertStatement.close();
                        connection.close();

                        messageLabel.setText("Item added successfully");
                        nameFiled.setText("");
                        priceField.setText("");
                        billingFiled.setText("");
                        startDateField.setText("");
                        typeFiled.setText("");

                    } catch (SQLException sqlException){
                        System.out.println("Database connection error occured");
                        sqlException.printStackTrace();
                    }


                }


            }
        }
    }
}
