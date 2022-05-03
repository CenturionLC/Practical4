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

    /*
     * private static String driver = "jdbc:mariadb";
     * private static String host = "localhost";
     * private static int port = 3307;
     * private static String database = "u21489549_sakila";
     * private static String username = "root";
     * private static String password = "Blackcat";
     */

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

        String title = "", des = "", rDur, len, rC = "", rating = "", speF = "";
        int rYearInt = 0, lanIdInt = 0, orgLanIdInt = 0, rDurInt = 0;
        double rRateDub = 0, lenDub = 0, rCostDub = 0;
        boolean pass = true;
        JOptionPane.showMessageDialog(f, "Enter Information for the database");
        title = getTitle();
        des = getDescription();
        
        rYearInt = getRYear();
        lanIdInt = getLanguageid();
        orgLanIdInt = getOriginalLanguage();
        rDurInt = getRentalDuration();

        try (PreparedStatement filmPrepared = connection.prepareStatement(
                "INSERT INTO film (title, description, release_year, language_id, original_language_id, rental_duration, rental_rate, length, replacement_cost, rating, special_features) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            filmPrepared.setString(1, title);
            filmPrepared.setString(2, des);
            filmPrepared.setInt(3, rYearInt);
            filmPrepared.setInt(4, lanIdInt);
            filmPrepared.setInt(5, orgLanIdInt);
            filmPrepared.setInt(6, rDurInt);
            filmPrepared.setDouble(7, rRateDub);
            filmPrepared.setDouble(8, lenDub);
            filmPrepared.setDouble(9, rCostDub);
            filmPrepared.setString(10, rating);
            filmPrepared.setString(11, speF);

            filmPrepared.execute();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(f, "Unable to insert into DB!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    public String getTitle() {
        String title = JOptionPane.showInputDialog(f, "Enter Title");
        if(title.isEmpty()){
            JOptionPane.showMessageDialog(f, "This title is not valid", "Warning",
            JOptionPane.WARNING_MESSAGE);
            getTitle();
        }
        return title;
    }

    public String getDescription() {

        String Des = JOptionPane.showInputDialog(f, "Enter Description");
        return Des;
    }

    public int getRYear() {
        int rYearInt = 0;
        String rYear = JOptionPane.showInputDialog(f, "Enter Release year");
        if(rYear.length() != 4){
            JOptionPane.showMessageDialog(f, "This year is not valid", "Warning",
            JOptionPane.WARNING_MESSAGE);
            getRYear();
        }
        try{
            rYearInt = Integer.parseInt(rYear);
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(f, "This year is not valid", "Warning",
            JOptionPane.WARNING_MESSAGE);
            getRYear();
        }
        return rYearInt;
    }

    public int getLanguageid(){
        int lanInt = 0;
        String lan = JOptionPane.showInputDialog(f, "Enter language id");
        if(lan.length() != 1){
            JOptionPane.showMessageDialog(f, "This language ID is not valid", "Warning",
            JOptionPane.WARNING_MESSAGE);
            getLanguageid();
        }
        try{
            lanInt = Integer.parseInt(lan);
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(f, "This language ID is not valid", "Warning",
            JOptionPane.WARNING_MESSAGE);
            getLanguageid();
        }
        return lanInt;
    }

    public int getOriginalLanguage() {
        int lanInt = 0;
        String lan = JOptionPane.showInputDialog(f, "Enter Orginal language id");
        if(lan.isEmpty()){
            return 0;
        }
        if(lan.length() != 1){
            JOptionPane.showMessageDialog(f, "This language ID is not valid", "Warning",
            JOptionPane.WARNING_MESSAGE);
            getLanguageid();
        }
        try{
            lanInt = Integer.parseInt(lan);
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(f, "This language ID is not valid", "Warning",
            JOptionPane.WARNING_MESSAGE);
            getLanguageid();
        }
        return lanInt;
    }

    public int getRentalDuration() {
        int rentDInt = 0;
        String des = JOptionPane.showInputDialog(f, "Enter rental duration");
        try{
            rentDInt = Integer.parseInt(des);
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(f, "This rental duration is not valid", "Warning",
            JOptionPane.WARNING_MESSAGE);
            getRentalDuration();
        }
        return rentDInt;
    }

    public String getRentalRate() {
        String Des = JOptionPane.showInputDialog(f, "Enter rental rate");
        return Des;
    }

    public String getLength() {
        String Des = JOptionPane.showInputDialog(f, "Enter length of film");
        return Des;
    }

    public String getReplacement() {
        String Des = JOptionPane.showInputDialog(f, "Enter cost of replacement");
        return Des;
    }

    public String getRating() {
        String Des = JOptionPane.showInputDialog(f, "Enter rating of film");
        return Des;
    }

    public String getSpecialFeatures() {
        String Des = JOptionPane.showInputDialog(f, "Enter special features of film");
        return Des;
    }

    public static void main(String[] args) throws SQLException {
        new App();
    }
}
