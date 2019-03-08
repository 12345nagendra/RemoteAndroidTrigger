package com.example.android.remotesuitetrigger;


import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectToDB {
    public Connection connect() throws Exception {
        Connection connection = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Class.forName("org.postgresql.Driver");
//            DriverManager.registerDriver(new org.postgresql.Driver());
//                connection = DriverManager.getConnection("jdbc:postgresql://0.tcp.ngrok.io:18959/PaymentTiming", "sajeel", "sajeel");
            connection = DriverManager.getConnection("jdbc:postgresql://0.tcp.ngrok.io:10427/RebootSajeel", "sajeel", "sajeel");

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        return connection;
    }

    public void insertSpeech(String Speech, Boolean processed, String TableName) throws Exception {

        Connection con = connect();
        con.setAutoCommit(true);

        String query = "INSERT INTO \"" + TableName + "\" (\"RECOGNIZED_SPEECH\", \"TIMESTAMP\"," +
                " \"PROCESSED\") VALUES (?,?,?);";

        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, Speech);
        ps.setObject(2, System.currentTimeMillis());
        ps.setBoolean(3, processed);
        ps.execute();
        System.out.println(ps.toString());
        closedb(con);
    }

    void closedb(Connection con) throws Exception {
        try {
            con.close();
        } catch (Exception e) {
            //Do nothing
        }
    }

//    public static void main(String args[]) throws Exception {
//        new ConnectToDB().insertSpeech("Trigger snkjdskjfh ",false,"Speech");
//    }
}