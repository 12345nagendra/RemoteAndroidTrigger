package com.example.android.remotesuitetrigger;


import android.database.Cursor;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ConnectToDB {

    long currentTime = System.currentTimeMillis();

    public static Connection connect() throws Exception {
        Connection connection = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Class.forName("org.postgresql.Driver");
//            DriverManager.registerDriver(new org.postgresql.Driver());
//                connection = DriverManager.getConnection("jdbc:postgresql://0.tcp.ngrok.io:18959/PaymentTiming", "sajeel", "sajeel");
            connection = DriverManager.getConnection("jdbc:postgresql://0.tcp.ngrok.io:12257/remotetriggerdb", "megha", "megha");

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        return connection;
    }

    public void insertSpeech(String platform, String triggerMethod, String command, String TableName) throws Exception {

        Connection con = connect();
        con.setAutoCommit(true);



        String query = "INSERT INTO \"" + TableName + "\" (\"Platform\",\"STARTTimestamp\",\"ENDTimestamp\",\"TriggerMethod\",\"Status\",\"Command\") " +
                "VALUES (?, ?, ?, ?,?,?);";

        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, platform);
        ps.setObject(2, currentTime);
        ps.setObject(3, null);
        ps.setString(4, triggerMethod);
        ps.setObject(5, "Queued");
        ps.setString(6, command);

        ps.execute();
        System.out.println(ps.toString());

        closedb(con);

    }
    public String showstatus() throws Exception {
        Connection con = connect();
        con.setAutoCommit(true);
        String sql = "select \"Status\" from \"TRIGGER\" where \"TriggerID\"= (select max(\"TriggerID\") from \"TRIGGER\"); ";
        Statement stmt1 = con.createStatement();
        ResultSet rs = stmt1.executeQuery(sql);
        ArrayList<String> commands = new ArrayList<>();

        while (rs.next()) {
            String raw = "";
            raw = rs.getString(1);
            commands.add(raw);
        }
        String abc =commands.get(0);
        System.out.println("abc"+abc);
        return abc;

    }

    public void update() throws Exception {
        Connection con = connect();
        con.setAutoCommit(true);
        String query = "update \"TRIGGER\" set \"Status\" = 'Force Stop' where  \"TriggerID\"= (select max(\"TriggerID\") from \"TRIGGER\"); ";

        PreparedStatement ps = con.prepareStatement(query);
        ps.execute();
        System.out.println(ps.toString());

        closedb(con);
    }

    public ArrayList<String> selectData(String TableName) throws Exception {
        String[] array = new String[5];
        int i =0;
        Connection con = connect();
        con.setAutoCommit(true);

        String query = "select * from  \""+TableName+"\"";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ArrayList<String> commands = new ArrayList<>();

        while (rs.next()) {
            String raw = "";
            raw = rs.getString(1);
            commands.add(raw);
        }
        closedb(con);
       return commands;
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