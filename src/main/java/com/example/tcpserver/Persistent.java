package com.example.tcpserver;

import java.sql.*;


public class Persistent {
    static final String JDBC = "jdbc:sqlite:src/main/resources/networks.db";

    static void addUser(String user, String password) throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC);
        Statement statement = connection.createStatement();
        String sql = "insert into users values ('" + user + "','" + password + "')";
        statement.executeUpdate(sql);
        connection.close();
    }

    static void addActive(String info) throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC);
        Statement statement = connection.createStatement();
        String sql = "insert into active values ('" + info.split("==")[0] + "','"+info.split("==")[1]+"')";
        statement.executeUpdate(sql);
        connection.close();
    }

    static String fetchActive() throws SQLException {
        String res = "";
        String sql = "SELECT * FROM active";
        Connection connection = DriverManager.getConnection(JDBC);
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            res = res + rs.getString(1)+","+rs.getString(2) + "^";
        }
        connection.close();
        return res;
    }

    public static boolean loginUser(String username, String password) throws SQLException {
        boolean isLoggedIn = false;
        String sql = "SELECT * FROM users WHERE user = ? AND password = ?";

        Connection connection = DriverManager.getConnection(JDBC);
        PreparedStatement stmt = connection.prepareStatement(sql);

        stmt.setString(1, username);
        stmt.setString(2, password);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            isLoggedIn = true;
        }
        connection.close();
        return isLoggedIn;
    }


//    static void addMessageToPersistent(String sender, String receiver, String body, long rank) throws SQLException {
//        Connection connection = DriverManager.getConnection(jdbc);
//        String sql = "INSERT INTO MESSAGE VALUES ('" + sender + "','" + receiver + "','" + body + "'," + rank + ")";
//        Statement statement = connection.createStatement();
//        statement.executeUpdate(sql);
//        connection.close();
//
//    }

//   static int getConvoLength(String sender, String receiver) throws SQLException {
//        Connection connection = DriverManager.getConnection(jdbc);
//        String sql = "SELECT COUNT (*) FROM MESSAGE WHERE((sender='" + sender + "' AND receiver='" + receiver + "') OR (receiver='" + sender + "' AND sender='" + receiver + "'))";
//        Statement statement = connection.createStatement();
//        ResultSet resultSet = statement.executeQuery(sql);
//        int tmp = resultSet.getInt(1);
//        connection.close();
//        return tmp;
//    }

//    static boolean convoExists(String user1, String user2) throws SQLException {
//        Connection connection = DriverManager.getConnection(jdbc);
//        String sql = "SELECT COUNT (*) FROM convo WHERE((user1='" + user1 + "' AND user2='" + user2 + "') OR (user1='" + user2 + "' AND user2='" + user1 + "'))";
//        Statement statement = connection.createStatement();
//        ResultSet resultSet = statement.executeQuery(sql);
//        int tmp = resultSet.getInt(1);
//        connection.close();
//        return tmp > 0;
//    }

//    static void createConvo(String user1, String user2) throws SQLException {
//        Connection connection = DriverManager.getConnection(jdbc);
//        String sql = "INSERT INTO CONVO VALUES('" + user1 + "','" + user2 + "');";
//        Statement statement = connection.createStatement();
//        statement.executeUpdate(sql);
//        connection.close();
//    }

//    static ArrayList<Message> fetchConvoFromPersistent(String user1, String user2) throws SQLException {
//        Connection connection = DriverManager.getConnection(jdbc);
//        String sql = "SELECT * FROM message WHERE((sender='" + user1 + "' AND receiver='" + user2 + "') OR (receiver='" + user2 + "' AND sender='" + user1 + "'))";
//        Statement statement = connection.createStatement();
//        ResultSet resultSet = statement.executeQuery(sql);
//        ArrayList<Message> messages = new ArrayList<>();
//        while (resultSet.next()) {
//            messages.add(new Message(resultSet.getString(3), resultSet.getString(1), resultSet.getString(2), resultSet.getLong(4)));
//        }
//        return messages;
//    }

    static void removeUserFromActive(String user) throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC);
        String sql = "DELETE FROM active WHERE(user='"+ user +"')";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }
}


