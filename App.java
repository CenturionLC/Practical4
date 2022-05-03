package za.ac.up.cs.cos221;

import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;
import java.awt.*;

public class App {
    JFrame fra, f;
    JTable staffTable;

    App() throws SQLException {
        fra = new JFrame();
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();
        JPanel p4 = new JPanel();
        JTabbedPane textword = new JTabbedPane();
        textword.setBounds(10, 0, 900, 700);
        textword.add("Staff", p1);
        textword.add("Films", p2);
        textword.add("Inventory", p3);
        textword.add("Clients", p4);
        fra.add(textword);
        fra.setSize(1000, 1000);
        fra.setLayout(null);
        fra.setVisible(true);
        staffTable = staffPanel();
        JScrollPane info = new JScrollPane(staffTable);
        p1.add(info);
        p1.revalidate();
        p1.repaint();
        JScrollPane info2 = new JScrollPane(filmPanel());
        p2.add(info2);
        JTextField filterStaff = new JTextField();
        filterStaff.setPreferredSize(new Dimension(100, 30));
        JButton Staffb = new JButton("Filter");
        p1.add(filterStaff);
        p1.add(Staffb);
        p1.revalidate();
        p1.repaint();
        JButton Filmb = new JButton("Add Data");
        p2.add(Filmb);

        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(staffTable.getModel());
        staffTable.setRowSorter(sorter);

        Staffb.addActionListener(a -> {
            String text = filterStaff.getText();
            if (text == "") {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));

            }
        });

        Filmb.addActionListener(a -> {
            try {
                addFilm();
                p2.revalidate();
                p2.repaint();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        

    }

    /*private static String driver = "jdbc:mariadb";
    private static String host = "localhost";
    private static int port = 3307;
    private static String database = "u21489549_sakila";
    private static String username = "root";
    private static String password = "Blackcat";*/

    private static String driver = System.getenv("SAKILA_DB_PROTO");
    private static String host = System.getenv("SAKILA_DB_HOST");
    private static String port = System.getenv("SAKILA_DB_PORT");
    private static String database = System.getenv("SAKILA_DB_NAME");
    private static String username = System.getenv("SAKILA_DB_USERNAME");
    private static String password = System.getenv("SAKILA_DB_PASSWORD");

    public JTable staffPanel() throws SQLException {
        int rows = 0;
        String url = new StringBuilder()
                .append(driver).append("://")
                .append(host).append(":").append(port).append("/")
                .append(database)
                .toString();
        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();

        /*
         * try (ResultSet result = statement.executeQuery("SELECT 1")) {
         * System.out.println("Connection to database " + database + " on host "
         * + host + ":" + port + " successfully established");
         * }
         */

        ResultSet result = statement.executeQuery(
                "SELECT * FROM staff");
        while (result.next()) {
            rows++;
        }
        ResultSet result2 = statement.executeQuery(
                "SELECT * FROM staff INNER JOIN address ON staff.address_id = address.address_id INNER JOIN city ON address.city_id = city.city_id INNER JOIN country ON city.country_id = country.country_id");
        String[][] staff = new String[rows][10];
        int i = 0;
        while (result2.next()) {
            staff[i][0] = result2.getString("first_name");
            staff[i][1] = result2.getString("last_name");
            staff[i][2] = result2.getString("address");
            staff[i][3] = result2.getString("address2");
            staff[i][4] = result2.getString("district");
            staff[i][5] = result2.getString("city");
            staff[i][6] = result2.getString("postal_code");
            staff[i][7] = result2.getString("phone");
            staff[i][8] = result2.getString("store_id");
            staff[i][9] = result2.getString("active");
            i++;
        }
        String coloumNames[] = { "First", "Last", "Address", "Address2", "District", "City", "Postal", "Phone", "Store",
                "Active" };

        JTable staffTable = new JTable(staff, coloumNames);
        return staffTable;
    }

    public JTable filmPanel() throws SQLException {
        int rows = 0;
        String url = new StringBuilder()
                .append(driver).append("://")
                .append(host).append(":").append(port).append("/")
                .append(database)
                .toString();
        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();

        try (ResultSet result = statement.executeQuery("SELECT 1")) {
            System.out.println("Connection to database " + database + " on host "
                    + host + ":" + port + " successfully established");
        }

        ResultSet result = statement.executeQuery(
                "SELECT * FROM film");
        while (result.next()) {
            rows++;
        }
        ResultSet result2 = statement.executeQuery(
                "SELECT * FROM film INNER JOIN film_category ON film.film_id = film_category.film_id INNER JOIN language ON film.language_id = language.language_id");
        String[][] film = new String[rows][8];
        int i = 0;
        while (result2.next()) {
            film[i][0] = result2.getString("title");
            film[i][1] = result2.getString("release_year");
            film[i][2] = result2.getString("name");
            film[i][3] = result2.getString("rental_duration");
            film[i][4] = result2.getString("rental_rate");
            film[i][5] = result2.getString("replacement_cost");
            film[i][6] = result2.getString("rating");
            film[i][7] = result2.getString("category_id");
            i++;
        }
        String coloumNames[] = { "Title", "Released", "Language", "Rent time", "Rent cost", "Lost cost", "rating",
                "category" };

        JTable filmTable = new JTable(film, coloumNames);
        return filmTable;
    }

    public void addFilm() throws SQLException {
            String url = new StringBuilder()
                    .append(driver).append("://")
                    .append(host).append(":").append(port).append("/")
                    .append(database)
                    .toString();
            Connection connection = DriverManager.getConnection(url, username, password);
        
            /*try (ResultSet result = statement.executeQuery("SELECT 1")) {
                System.out.println("Connection to database " + database + " on host "
                        + host + ":" + port + " successfully established");
            }*/

            f =new JFrame(); 
            JOptionPane.showMessageDialog(f, "Enter each bit of data one at a time, press ok after each entry!");
            String title = JOptionPane.showInputDialog(f, "Enter Title");
            String Des = JOptionPane.showInputDialog(f, "Enter Description");
            int rYear = Integer.parseInt(JOptionPane.showInputDialog(f, "Enter Release year"));
            int lanId = Integer.parseInt(JOptionPane.showInputDialog(f, "Enter Language id"));
            int orgLanId = Integer.parseInt(JOptionPane.showInputDialog(f, "Enter Original Language ID"));
            int rDur = Integer.parseInt(JOptionPane.showInputDialog(f, "Enter Rental Duration"));
            double rRate = Double.parseDouble(JOptionPane.showInputDialog(f, "Enter Rental Rate"));
            int len = Integer.parseInt(JOptionPane.showInputDialog(f, "Enter Length"));
            double rCost = Double.parseDouble(JOptionPane.showInputDialog(f, "Enter Replacement Cost"));
            String rating = JOptionPane.showInputDialog(f, "Enter Rating");
            String speF = JOptionPane.showInputDialog(f, "Enter Special features");

            try(PreparedStatement filmPrepared = connection.prepareStatement("INSERT INTO film (title, description, release_year, language_id, original_language_id, rental_duration, rental_rate, length, replacement_cost, rating, special_features) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")){
                filmPrepared.setString(1, title);
                filmPrepared.setString(2, Des);
                filmPrepared.setInt(3, rYear);
                filmPrepared.setInt(4, lanId);
                if(orgLanId == 0){
                    filmPrepared.setNull(5, orgLanId);
                }
                else{
                    filmPrepared.setInt(5, orgLanId);
                }
                filmPrepared.setInt(6, rDur);
                filmPrepared.setDouble(7, rRate);
                filmPrepared.setDouble(8, len);
                filmPrepared.setDouble(9, rCost);
                filmPrepared.setString(10, rating);
                filmPrepared.setString(11, speF);

                filmPrepared.execute();
            }
    }

    public static void main(String[] args) throws SQLException {

        new App();
    }
}
