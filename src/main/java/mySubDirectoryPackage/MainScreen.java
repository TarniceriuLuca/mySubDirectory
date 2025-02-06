package mySubDirectoryPackage;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Clasa ecranului principal, in care poate fi vizualizate toate subscriptiile utilizatorului si o notificare cu privire la numarul de subscriptii care urmeaza sa fie platite.
 * Este afisat de asemena si pretul total pe luna si pe an
 * Din acest ecran se pot edita subscriptiile folosind butonul "edit".
 */
public class MainScreen extends JFrame implements ActionListener {
    String username;

    JLabel notificationLabel;
    JPanel subListPanel;


    JButton addSubButton, logoutButton;
    JPanel buttonPanel;


    List<Subscription> subscriptionList;
    List<JLabel> subLabelList;
    List<JButton> editButtonList;

    public MainScreen(String username){
        this.username = username;
        System.out.println(notification(username));

        this.setTitle("MySubDirectory - " + username);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(800,550);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());


        subListPanel = new JPanel();
        subListPanel.setLayout(new BoxLayout(subListPanel, BoxLayout.Y_AXIS));
        subListPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 100, 100));


        notificationLabel = new JLabel(notification(username));
        notificationLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 300), BorderFactory.createLoweredBevelBorder()));
        this.add(notificationLabel, BorderLayout.NORTH);


        subscriptionList = loadData(username);
        subLabelList = new ArrayList<>();
        editButtonList = new ArrayList<>();

        for(int subIndex = 0; subIndex < subscriptionList.size(); subIndex++){
            JLabel subLabel = new JLabel(subscriptionList.get(subIndex).toString());
            JButton subEditButton = new JButton("edit");

            subEditButton.setBackground(new Color(238, 238, 238));
            subEditButton.setBorder(BorderFactory.createRaisedBevelBorder());

            subLabelList.add(subLabel);
            editButtonList.add(subEditButton);
        }


        for(int subIndex = 0; subIndex < subLabelList.size(); subIndex++){


            JLabel subLabel = subLabelList.get(subIndex);
            JButton editButton = editButtonList.get(subIndex);

            editButton.setContentAreaFilled(false);
            editButton.setFocusPainted(false);
            editButton.addActionListener(this);

            Border originalBorder = BorderFactory.createRaisedBevelBorder();
            Border clickedBorder = BorderFactory.createLoweredBevelBorder();

            editButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    editButton.setBorder(clickedBorder);
                    editButton.setBackground(new Color(238, 238, 238));
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    editButton.setBorder(originalBorder);
                }
            });


            JPanel textButtonPairPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            textButtonPairPanel.setBorder(BorderFactory.createEmptyBorder(1, 10, 1, 10));
            textButtonPairPanel.add(subLabel);
            textButtonPairPanel.add(editButton);

            subListPanel.add(textButtonPairPanel);
            subListPanel.add(Box.createRigidArea(new Dimension(0, 5)));


        }

        JScrollPane scrollPane = new JScrollPane(subListPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(30, 100, 0, 100), BorderFactory.createEtchedBorder()));
        scrollPane.getVerticalScrollBar().setBackground(new Color(238, 238, 238));
        scrollPane.getVerticalScrollBar().setBorder(BorderFactory.createRaisedBevelBorder());
        this.add(scrollPane, BorderLayout.CENTER);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        addSubButton = new JButton("Add subscription");
        addSubButton.addActionListener(this);
        addSubButton.setBackground(new Color(238, 238, 238));
        addSubButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 200, 2, 200), BorderFactory.createRaisedBevelBorder()));
        addSubButton.setContentAreaFilled(false);
        addSubButton.setFocusPainted(false);

        Border addButtonOriginalBorder = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 200, 2, 200), BorderFactory.createRaisedBevelBorder());
        Border addButtonClickedBorder = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 200, 2, 200), BorderFactory.createLoweredBevelBorder());

        addSubButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                addSubButton.setBorder(addButtonClickedBorder);
                addSubButton.setBackground(new Color(238, 238, 238));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                addSubButton.setBorder(addButtonOriginalBorder);
            }
        });


        buttonPanel.add(addSubButton, BorderLayout.SOUTH);


        buttonPanel.add(Box.createRigidArea(new Dimension(0, 0)));


        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(this);
        logoutButton.setBackground(new Color(238, 238, 238));
        logoutButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 200, 10, 200), BorderFactory.createRaisedBevelBorder()));
        logoutButton.setContentAreaFilled(false);
        logoutButton.setFocusPainted(false);

        Border logoutOriginalBorder = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 200, 10, 200), BorderFactory.createRaisedBevelBorder());
        Border logoutClickedBorder = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 200, 10, 200), BorderFactory.createLoweredBevelBorder());

        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                logoutButton.setBorder(logoutClickedBorder);
                logoutButton.setBackground(new Color(238, 238, 238));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                logoutButton.setBorder(logoutOriginalBorder);
            }
        });
        buttonPanel.add(logoutButton, BorderLayout.SOUTH);


        this.add(buttonPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    /**
     * Aceasta functie primeste username-ul utilizatorului, si formeaza o lista de obiecte Subscription, care contine toate subscriptiile utilizatorului, pentru prelucrari ulterioare
     * @param username String cu username-ul utilizatorului conectat
     * @return List &lt Subscription &gt o lista cu toate subscriptiile utilizatorului
     */
    public static List<Subscription> loadData(String username){
        String jdbcURL = "jdbc:mysql://localhost:3306/mySubDirectoryDB";
        String dbUsername = "luca";
        String dbPassword = System.getenv("MYSQL_PASS");

        try{
            Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
            String getSubsQuery = "select * from " + username;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getSubsQuery);

            List<Subscription> subscriptionList = new ArrayList<>();

            while(resultSet.next()){
                String subName = resultSet.getString("name");
                float subPrice = resultSet.getFloat("price");
                String subBilling = resultSet.getString("billingPeriod");
                LocalDate subStartDate = LocalDate.parse(resultSet.getString("startDate"));
                String subType = resultSet.getString("type");
                Subscription subscription = new Subscription(subName, subPrice, subBilling, subStartDate, subType);

                subscriptionList.add(subscription);
            }

            return subscriptionList;

        } catch (SQLException e){
            System.out.println("A database error occured");
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    /**
     * Functia calculeaza pretul total pe luna, si pretul total pe an al tuturor subscriptiilor
     * @param username String cu username-ul utilizatorului conectat
     * @return List &lt Float &gt primul element este pretul total pe luna, iar al doilea element este pretul total pe an
     */
    public List<Float> totalPrice(String username){
        float yearlyPrice = 0, monthlyPrice = 0;
        List<Subscription> subList =  loadData(username);
        for(Subscription s : subList){
            if(Objects.equals(s.getBillingPeriod(), "monthly")){
                monthlyPrice += s.getPrice();
                yearlyPrice += s.getPrice() * 12;
            }

            if(Objects.equals(s.getBillingPeriod(), "yearly")){
                yearlyPrice += s.getPrice();
            }
        }

        List<Float> priceList = new ArrayList<>();
        priceList.add(monthlyPrice);
        priceList.add(yearlyPrice);
        return priceList;
    }


    /**
     * Functia calculeaza urmatoarea data de plata a fiecarei subscriptii si numara cate dintre ele trebuie platite la momentul deschiderii aplicatiei, si cate dintre ele trebuie platite in mai putin de doua zile din momentul deschiderii aplicatiei
     * @param username String cu username-ul utilizatorului conectat
     * @return String cu mesajul contorizarii subscriptilor
     */
    public String notification(String username){
        List<Subscription> subList = loadData(username);
        List<Subscription> paymentSoon = new ArrayList<>();
        List<Subscription> paymentToday = new ArrayList<>();

        for(Subscription s : subList){
            if(s.isPaymentIn(2) <= 0){
                paymentSoon.add(s);
            }
            if(s.isPaymentIn(0) == 2){
                paymentToday.add(s);
            }
        }
        String firstSubText, secondSubText;
        if(paymentSoon.size() == 1) {
            firstSubText = "subscriptie";
        }
        else{
            firstSubText = "subscriptii";
        }

        if(paymentToday.size() == 1){
            secondSubText = "subscriptie";
        }
        else{
            secondSubText = "subscriptii";
        }

        List<Float> prices = totalPrice(username);
        return "<html>Plata a " + paymentSoon.size() + " " + firstSubText + " in urmatoarele doua zile<br>Plata a " + paymentToday.size() + " " + secondSubText + " astazi<br><br>Pret total pe luna: " + prices.getFirst() + "<br>Pret total pe an: "+ prices.getLast() + "</html>";
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        for(int buttonIndex = 0; buttonIndex < editButtonList.size(); buttonIndex++) {
            if (e.getSource() == editButtonList.get(buttonIndex)) {
                System.out.println("button number: " + buttonIndex);
                String subscriptionName = subscriptionList.get(buttonIndex).getName();
                new EditScreen(username, subscriptionName);
                this.setVisible(false);
                dispose();
            }
        }

        if(e.getSource() == addSubButton){
            new AddSubScreen(username);
            this.setVisible(false);
            dispose();
        }
        if(e.getSource() == logoutButton){
            new LoginScreen();
            this.setVisible(false);
            dispose();
        }

    }
}
