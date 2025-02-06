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
 * Clasa ecranului pentru meniul de editare al unei subscriptii.
 * Incarca din baza de date detaliile despre subscriptia selectata, transmisa prin parametru constructorului, iar dupa modificarea datelor din campurile interfetei grafice, este modificata intrarea in baza de date.
 * Tot din acest ecran se poate anula o subscriptie, ceea ce va sterge intrarea din baza de date.
 */
public class EditScreen extends JFrame implements ActionListener {

    String username;
    JTextField nameFiled, priceField, billingFiled, startDateField, typeFiled;
    JLabel nameLabel, priceLabel, billingLabel, startDateLabel, typeLabel, messageLabel, dateExampleLabel, billingExampleLabel, typeExampleLabel;

    JButton backButton, continueButton, cancelButton;

    String subNametoEdit;

    boolean hasBeenDeleted;

    public EditScreen(String username, String subNametoEdit){
        this.hasBeenDeleted = false;
        this.subNametoEdit = subNametoEdit;
        this.setTitle("MySubDirectory - edit subscription");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(800,550);
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        this.username = username;



        String jdbcURL = "jdbc:mysql://localhost:3306/mySubDirectoryDB";
        String dbUsername = "luca";
        String dbPassword = System.getenv("MYSQL_PASS");

        try{
            Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
            System.out.println("connected to db");


            String selectQuery = "select * from " + username + " where name=?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setString(1, subNametoEdit);

            ResultSet resultSet = selectStatement.executeQuery();

            if(resultSet.next()){

                this.nameFiled = new JTextField(resultSet.getString("name"));
                this.priceField = new JTextField(resultSet.getString("price"));
                this.billingFiled = new JTextField(resultSet.getString("billingPeriod"));
                this.startDateField = new JTextField(resultSet.getString("startDate"));
                this.typeFiled = new JTextField(resultSet.getString("type"));
            }

            selectStatement.close();
            connection.close();

        } catch (SQLException sqlException){
            System.out.println("Database connection error occured");
            sqlException.printStackTrace();
        }



        nameLabel = new JLabel("Name: ");
        nameLabel.setBounds(50, 50, 200, 24);
        this.add(nameLabel);

        nameFiled.setBounds(160, 50, 200, 24);
//        nameFiled.setEditable(false);
        this.add(nameFiled);


        priceLabel = new JLabel("Price: ");
        priceLabel.setBounds(50, 98, 200, 24);
        this.add(priceLabel);

        priceField.setBounds(160, 98, 200, 24);
        this.add(priceField);


        billingLabel = new JLabel("Billing period: ");
        billingLabel.setBounds(50, 146, 200, 24);
        this.add(billingLabel);

        billingExampleLabel = new JLabel("(monthly/ yearly)");
        billingExampleLabel.setBounds(400, 146, 200, 24);
        this.add(billingExampleLabel);


        billingFiled.setBounds(160, 146, 200, 24);
        this.add(billingFiled);


        startDateLabel = new JLabel("startDate: ");
        startDateLabel.setBounds(50, 194, 200, 24);
        this.add(startDateLabel);

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


        cancelButton = new JButton("Cancel Sub");
        cancelButton.setBounds(375, 290, 100, 24);
        this.add(cancelButton);
        cancelButton.addActionListener(this);

        cancelButton.setBackground(new Color(238, 238, 238));
        cancelButton.setBorder(BorderFactory.createRaisedBevelBorder());
        cancelButton.setContentAreaFilled(false);
        cancelButton.setFocusPainted(false);

        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                cancelButton.setBorder(logoutClickedBorder);
                cancelButton.setBackground(new Color(238, 238, 238));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                cancelButton.setBorder(logoutOriginalBorder);
            }
        });




        messageLabel = new JLabel();
        messageLabel.setBounds(75, 350, 425, 48);
        this.add(messageLabel);


        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == cancelButton){
            String jdbcURL = "jdbc:mysql://localhost:3306/mySubDirectoryDB";
            String dbUsername = "luca";
            String dbPassword = System.getenv("MYSQL_PASS");

            try{
                Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
                System.out.println("connected to db");


                String deleteQuery = "delete from " + username + " where name=?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setString(1, subNametoEdit);

                deleteStatement.executeUpdate();
                deleteStatement.close();
                connection.close();

                messageLabel.setText("Item deleted");
                this.hasBeenDeleted = true;
                nameFiled.setEditable(false);
                priceField.setEditable(false);
                billingFiled.setEditable(false);
                startDateField.setEditable(false);
                typeFiled.setEditable(false);

            } catch (SQLException sqlException){
                System.out.println("Database connection error occured");
                sqlException.printStackTrace();
            }
        }

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
                if(hasBeenDeleted){
                    messageLabel.setText("The item has been deleted. You cannot modify it.");
                }
                else {
                    String subName;
                    float subPrice = 0;
                    String subBillingPeriod = "n/a";
                    String subStartDate = "01-01-1970";
                    String subType;

                    boolean allPassed = true;


                    subName = nameFiled.getText().replaceAll("\\s+", " ");
                    subName = subName.replaceAll(" ", "_");

                    String priceRegex = "\\d+";
                    if (!priceField.getText().matches(priceRegex)) {
                        messageLabel.setText("Wrong price format");
                        allPassed = false;
                    } else {
                        subPrice = Float.parseFloat(priceField.getText());
                    }

                    String billingRegex = "monthly|yearly";
                    if (!billingFiled.getText().matches(billingRegex)) {
                        messageLabel.setText("Wrong billing period format");
                        allPassed = false;
                    } else {
                        subBillingPeriod = billingFiled.getText();
                    }

                    String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";

                    if (!startDateField.getText().matches(dateRegex)) {
                        messageLabel.setText("Wrong date format");
                        allPassed = false;
                    } else {
                        subStartDate = startDateField.getText();
                    }

                    subType = typeFiled.getText().replaceAll("\\s+", " ");
                    subType = subType.replaceAll(" ", "_");

                    if (allPassed) {
                        String jdbcURL = "jdbc:mysql://localhost:3306/mySubDirectoryDB";
                        String dbUsername = "luca";
                        String dbPassword = System.getenv("MYSQL_PASS");


                        try {
                            Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
                            System.out.println("connected to db");


                            String updateQuery = "update " + username + " set name=?, price=?, billingPeriod=?, startDate=?, type=? where name=?";
                            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                            updateStatement.setString(1, subName);
                            updateStatement.setFloat(2, subPrice);
                            updateStatement.setString(3, subBillingPeriod);
                            updateStatement.setString(4, subStartDate);
                            updateStatement.setString(5, subType);
                            updateStatement.setString(6, subNametoEdit);

                            updateStatement.executeUpdate();
                            updateStatement.close();
                            connection.close();

                            messageLabel.setText("Item updated");

                        } catch (SQLException sqlException) {
                            System.out.println("Database connection error occured");
                            sqlException.printStackTrace();
                        }


                    }
                }


            }
        }
    }
}
