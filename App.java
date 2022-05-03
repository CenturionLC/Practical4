package za.ac.up.cs.cos221;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.sql.*;
import java.awt.*;

public class App {
    JFrame fra;
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
        JTextField filterFilm = new JTextField();
        filterFilm.setPreferredSize(new Dimension(100, 30));
        JButton Filmb = new JButton("Filter");
        p1.add(filterFilm);
        p1.add(Filmb);
        p1.revalidate();
        p1.repaint();

        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(staffTable.getModel());
        staffTable.setRowSorter(sorter);

        Filmb.addActionListener(a -> {
            String text = filterFilm.getText();
            if(text == ""){
                sorter.setRowFilter(null);
            }
            else{
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                
            }
        });
        
}
    
    private static String driver = "jdbc:mariadb";
    private static String host = "localhost";
    private static int port = 3307;
    private static String database = "u21489549_sakila";
    private static String username = "root";
    private static String password = "Blackcat";

    public JTable staffPanel() throws SQLException {
        int rows = 0;
        String url = new StringBuilder()
                .append(driver).append("://")
                .append(host).append(":").append(port).append("/")
                .append(database)
                .toString();
        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();

        /*try (ResultSet result = statement.executeQuery("SELECT 1")) {
            System.out.println("Connection to database " + database + " on host "
                    + host + ":" + port + " successfully established");
        }*/

        
        ResultSet result = statement.executeQuery(
                "SELECT * FROM staff");
        while(result.next()){
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
        while(result.next()){
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
        String coloumNames[] = {"Title", "Released", "Language", "Rent time", "Rent cost", "Lost cost", "rating", "category"};


        JTable filmTable = new JTable(film, coloumNames);
        return filmTable;
    }

    
    


    public static void main(String[] args) throws SQLException {

        new App();
    }
}
