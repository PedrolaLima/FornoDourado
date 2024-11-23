/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.data;
import java.sql.*;

/**
 *
 * @author alberto
 */
public class Database {
    
    public static String server,user,psswd,db;
    public static int port;
    public static java.sql.Connection connection = null;
    
    //Terminar de preencher apos criar o banco
    
    static{
        server = "localhost";
        user="root";
        psswd="";
        db="FornoDourado";
        port=3306;
        
    }
    
     public static void connect() throws SQLException {
        String url = "jdbc:mysql://" + server +
                     ":" + port + "/" + db;

        connection = DriverManager.getConnection(url, user, psswd);
    }
    
    public static void close() throws SQLException {
        connection.close();
    }
    
    public static java.sql.Connection getConnection() throws SQLException {
        if (connection == null) {
            throw new SQLException("Conexão está fechada..");
        } else {
            return connection;
        }
    }
}
