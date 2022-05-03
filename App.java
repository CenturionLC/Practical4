package za.ac.up.cs.cos221;

import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.awt.*;
import java.util.Date;

public class App {
    JFrame fra, f;
    JTable staffTable;
    private static String driver = System.getenv("SAKILA_DB_PROTO");
    private static String host = System.getenv("SAKILA_DB_HOST");
    private static String port = System.getenv("SAKILA_DB_PORT");
    private static String database = System.getenv("SAKILA_DB_NAME");
    private static String username = System.getenv("SAKILA_DB_USERNAME");
    private static String password = System.getenv("SAKILA_DB_PASSWORD");

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
        JScrollPane info3 = new JScrollPane(inventoryPanel());
        p3.add(info3);
        p3.revalidate();
        p3.repaint();

        JButton ClAddb = new JButton("Add Client");
        JButton ClUp = new JButton("Update Client");
        JButton ClDeleb = new JButton("Delete Client");
        JButton Clviewb = new JButton("View Clients");

        p4.add(ClAddb);
        p4.add(ClUp);
        p4.add(ClDeleb);
        p4.add(Clviewb);
        p4.revalidate();
        p4.repaint();

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
                p1.revalidate();
                p1.repaint();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        ClAddb.addActionListener(a -> {
            try {
                addClient();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        ClUp.addActionListener(a -> {
            try {
                updateClient();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        ClDeleb.addActionListener(a -> {
            try {
                deleteClient();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        Clviewb.addActionListener(a -> {
            try {
                viewClient();
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

        ResultSet result = statement.executeQuery(
                "SELECT * FROM film");
        while (result.next()) {
            rows++;
        }
        ResultSet result2 = statement.executeQuery(
                "SELECT * FROM film INNER JOIN film_category ON film.film_id = film_category.film_id INNER JOIN category ON film_category.category_id = category.category_id");
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
        String coloumNames[] = { "Title", "Released", "Category", "Rent time", "Rent cost", "Lost cost", "rating",
                "category" };

        JTable filmTable = new JTable(film, coloumNames);
        return filmTable;
    }

    public JTable inventoryPanel() throws SQLException {
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

        try (ResultSet result = statement.executeQuery("SELECT 1")) {
            System.out.println("Connection to database " + database + " on host "
                    + host + ":" + port + " successfully established");
        }
        String sql = "SELECT CONCAT(ci.city,',',co.country) as Store, c.name genre, COUNT(i.store_id) as appear FROM category c INNER JOIN film_category fc ON c.category_id = fc.category_id INNER JOIN film fa ON fc.film_id = fa.film_id INNER JOIN inventory i ON i.film_id = fa.film_id INNER JOIN store s ON i.store_id = s.store_id INNER JOIN address a ON s.address_id = a.address_id INNER JOIN city ci ON a.city_id = ci.city_id INNER JOIN country co ON ci.country_id = co.country_id GROUP BY i.store_id, c.name ORDER By i.store_id";

        ResultSet result1 = statement.executeQuery(sql);

        String[][] in = new String[32][3];
        int i = 0;
        while (result1.next()) {
            in[i][0] = result1.getString("store");
            in[i][1] = result1.getString("genre");
            in[i][2] = result1.getString("appear");
            i++;
        }
        String coloumNames[] = { "Store", "Genre", "Amounts" };

        JTable InTable = new JTable(in, coloumNames);
        return InTable;

    }

    public void addFilm() throws SQLException {
        String url = new StringBuilder()
                .append(driver).append("://")
                .append(host).append(":").append(port).append("/")
                .append(database)
                .toString();
        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();
        int rows = 0;
        String title = "", des = "", rC = "", rating = "", speF = "";
        int rYear = 0, lanId = 0, orgLanId = 0, rDur = 0, len = 0, catId = 0;
        double rRate = 0, rCost = 0;
        JOptionPane.showMessageDialog(f, "Enter Information for the database");
        title = getTitle();
        des = getDescription();

        rYear = getRYear();
        lanId = getLanguageid();
        orgLanId = getOriginalLanguage();
        rDur = getRentalDuration();
        rRate = getRentalRate();
        len = getLength();
        rCost = getReplacement();
        rating = getRating();
        speF = getSpecialFeatures();
        catId = getCatID();

        try (PreparedStatement filmPrepared = connection.prepareStatement(
                "INSERT INTO film (title, description, release_year, language_id, original_language_id, rental_duration, rental_rate, length, replacement_cost, rating, special_features) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            filmPrepared.setString(1, title);
            filmPrepared.setString(2, des);
            filmPrepared.setInt(3, rYear);
            filmPrepared.setInt(4, lanId);
            if (orgLanId == 100) {
                filmPrepared.setNull(5, Types.TINYINT);
            } else {
                filmPrepared.setInt(5, orgLanId);
            }
            filmPrepared.setInt(6, rDur);
            filmPrepared.setDouble(7, rRate);
            filmPrepared.setInt(8, len);
            filmPrepared.setDouble(9, rCost);
            filmPrepared.setString(10, rating);
            filmPrepared.setString(11, speF);
            filmPrepared.execute();

            ResultSet result = statement.executeQuery(
                    "SELECT * FROM film");
            while (result.next()) {
                rows++;
            }
            String sql = "INSERT INTO film_category (film_id, category_id) VALUES (?, ?)";

            try (PreparedStatement stmt2 = connection.prepareStatement(sql)) {
                stmt2.setInt(1, rows);
                stmt2.setInt(2, catId);
                stmt2.execute();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(f, "Unable to insert into DB!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    public String getTitle() {
        String title = JOptionPane.showInputDialog(f, "Enter Title");
        if (title.isEmpty()) {
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
        if (rYear.length() != 4) {
            JOptionPane.showMessageDialog(f, "This year is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getRYear();
        }
        try {
            rYearInt = Integer.parseInt(rYear);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(f, "This year is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getRYear();
        }
        return rYearInt;
    }

    public int getLanguageid() {
        int lanInt = 0;
        String lan = JOptionPane.showInputDialog(f, "Enter language id");
        if (lan.length() != 1) {
            JOptionPane.showMessageDialog(f, "This language ID is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getLanguageid();
        }
        try {
            lanInt = Integer.parseInt(lan);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(f, "This language ID is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getLanguageid();
        }
        return lanInt;
    }

    public int getOriginalLanguage() {
        int lanInt = 0;
        String lan = JOptionPane.showInputDialog(f, "Enter Orginal language id");
        if (lan.isEmpty()) {
            lanInt = 100;
            return lanInt;
        }
        if (lan.length() != 1) {
            JOptionPane.showMessageDialog(f, "This language ID is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getLanguageid();
        }
        try {
            lanInt = Integer.parseInt(lan);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(f, "This language ID is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getLanguageid();
        }
        return lanInt;
    }

    public int getRentalDuration() {
        int rentDInt = 0;
        String des = JOptionPane.showInputDialog(f, "Enter rental duration");
        try {
            rentDInt = Integer.parseInt(des);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(f, "This rental duration is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getRentalDuration();
        }
        return rentDInt;
    }

    public double getRentalRate() {
        double rentRInt = 0;
        String des = JOptionPane.showInputDialog(f, "Enter rental rate");
        int pos = des.indexOf(".");
        if (pos != -1) {
            if (des.substring(pos + 1).length() > 2) {
                JOptionPane.showMessageDialog(f, "This rental rate is not valid", "Warning",
                        JOptionPane.WARNING_MESSAGE);
                getRentalDuration();
            }
        }
        if (des.length() > 4) {
            JOptionPane.showMessageDialog(f, "This rental rate is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getRentalDuration();
        }
        try {
            rentRInt = Double.parseDouble(des);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(f, "This rental rate is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getRentalDuration();
        }
        return rentRInt;
    }

    public int getLength() {
        int len = 0;
        String des = JOptionPane.showInputDialog(f, "Enter length of film");
        if (des.length() > 5) {
            JOptionPane.showMessageDialog(f, "This film length is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getLength();
        }
        try {
            len = Integer.parseInt(des);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(f, "This rental duration is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getLength();
        }
        return len;
    }

    public double getReplacement() {
        double rep = 0;
        String des = JOptionPane.showInputDialog(f, "Enter Replacement cost");
        int pos = des.indexOf(".");
        if (pos != -1) {
            if (des.substring(pos + 1).length() > 2) {
                JOptionPane.showMessageDialog(f, "This rental rate is not valid", "Warning",
                        JOptionPane.WARNING_MESSAGE);
                getReplacement();
            }
        }
        if (des.length() > 5) {
            JOptionPane.showMessageDialog(f, "This rental rate is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getReplacement();
        }
        try {
            rep = Double.parseDouble(des);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(f, "This rental rate is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getReplacement();
        }
        return rep;
    }

    public String getRating() {
        String vibes = "";
        String Des = JOptionPane.showInputDialog(f, "Enter rating of film");
        if (Des.length() > 5) {
            JOptionPane.showMessageDialog(f, "This rating is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getRating();
        }
        if (Des.equals("G") || Des.equals("PG") || Des.equals("PG-13") || Des.equals("R") || Des.equals("NC-17")) {
            vibes = Des;
        } else {
            JOptionPane.showMessageDialog(f, "This rating is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getRating();
        }
        return vibes;
    }

    public String getSpecialFeatures() {
        String vibes = "";
        String Des = JOptionPane.showInputDialog(f, "Enter Special Features of film");
        if (Des.length() > 18) {
            JOptionPane.showMessageDialog(f, "This rating is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getSpecialFeatures();
        }
        if (Des.equals("Trailers") || Des.equals("Commentaries") || Des.equals("Deleted Scenes")
                || Des.equals("Behind the Scenes")) {
            vibes = Des;
        } else {
            JOptionPane.showMessageDialog(f, "This rating is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getSpecialFeatures();
        }
        return vibes;
        // 'Trailers', 'Commentaries', 'Deleted Scenes', 'Behind the Scenes')
    }

    public int getCatID() {
        int len = 0;
        String des = JOptionPane.showInputDialog(f, "Enter category ID of film");
        if (des.length() > 3) {
            JOptionPane.showMessageDialog(f, "This category ID is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getCatID();
        }
        try {
            len = Integer.parseInt(des);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(f, "This category ID is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getCatID();
        }
        if (len > 16) {
            JOptionPane.showMessageDialog(f, "This category ID is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getCatID();
        }
        if (len < 1) {
            JOptionPane.showMessageDialog(f, "This category ID is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getCatID();
        }
        return len;
    }

    public void addClient() throws SQLException {
        String url = new StringBuilder()
                .append(driver).append("://")
                .append(host).append(":").append(port).append("/")
                .append(database)
                .toString();
        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();

        int storeID = 0, addressID = 0, act = 0;
        String first = "", last = "", email = "";
        

        storeID = getStoreID();
        first = getFName();
        last = getLName();
        email = getEmail();
        addressID = getAddressId();
        act = getActive();
        String time = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        
        try (PreparedStatement custPrepared = connection.prepareStatement(
            "INSERT INTO customer (store_id, first_name, last_name, email, address_id, active, create_date) VALUES (?,?,?,?,?,?,?)")){
                custPrepared.setInt(1, storeID);
                custPrepared.setString(2, first);
                custPrepared.setString(3, last);
                custPrepared.setString(4, email);
                custPrepared.setInt(5, addressID);
                custPrepared.setInt(6, act);
                custPrepared.setString(7, time);
                custPrepared.execute();
            }
    }

    public int getStoreID(){
        int id = Integer.parseInt(JOptionPane.showInputDialog(f, "Enter Store ID"));
        if(id != 1 || id != 2){
            JOptionPane.showMessageDialog(f, "This Store ID is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getStoreID();
        }
        return id;
    }

    public String getFName(){
        String des = JOptionPane.showInputDialog(f, "Enter First Name");
        if(des.isEmpty()){
            JOptionPane.showMessageDialog(f, "This Name is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getFName();
        }
        return des;
    }

    public String getLName(){
        String des = JOptionPane.showInputDialog(f, "Enter Last Name");
        if(des.isEmpty()){
            JOptionPane.showMessageDialog(f, "This Name is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getFName();
        }
        return des;
    }

    public String getEmail(){
        String email = "";
        String des = JOptionPane.showInputDialog(f, "Enter Email sName");
        if(des.contains("@") && des.contains(".")){
            email = des;
        }
        else{
            JOptionPane.showMessageDialog(f, "This Email is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getEmail();
        }
        return email;
    }

    public int getAddressId(){
        int id = 0;
        String des = JOptionPane.showInputDialog(f, "Enter Email sName");
        if(des.length() > 3){
            JOptionPane.showMessageDialog(f, "This address ID is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getAddressId();
        }
        try {
            id = Integer.parseInt(des);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(f, "This rental duration is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getAddressId();
        }

        return id; 
    }
    
    public int getActive(){
        int act = 0;
        String des = JOptionPane.showInputDialog(f, "Enter Email sName");
        if(des.length() > 1){
            JOptionPane.showMessageDialog(f, "This ActiveId is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getActive();
        }
        try {
            act = Integer.parseInt(des);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(f, "This rental duration is not valid", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            getActive();
        }

        return act;
    }
    
    public void updateClient() throws SQLException {
        String url = new StringBuilder()
                .append(driver).append("://")
                .append(host).append(":").append(port).append("/")
                .append(database)
                .toString();
        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();


    }

    public void deleteClient() throws SQLException {
        String url = new StringBuilder()
                .append(driver).append("://")
                .append(host).append(":").append(port).append("/")
                .append(database)
                .toString();
        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();
    }

    public void viewClient() throws SQLException {
        String url = new StringBuilder()
                .append(driver).append("://")
                .append(host).append(":").append(port).append("/")
                .append(database)
                .toString();
        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();
    }

    public static void main(String[] args) throws SQLException {
        new App();
    }
}
