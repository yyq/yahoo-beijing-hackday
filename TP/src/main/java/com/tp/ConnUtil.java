package com.tp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: naruto
 * Date: 13-5-19
 * Time: 上午1:32
 * To change this template use File | Settings | File Templates.
 */
public class ConnUtil {
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    private static Connection connection;
    private ConnUtil(){

    }
    public static Connection getConnection() throws SQLException {
        if(connection==null){
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/TP","root","root");
        }
        return connection;
    }
}
