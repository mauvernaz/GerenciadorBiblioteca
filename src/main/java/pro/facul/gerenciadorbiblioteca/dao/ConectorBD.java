package pro.facul.gerenciadorbiblioteca.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectorBD {

    private static final String URL = "jdbc:mysql://localhost:3306/gerenciador_biblioteca";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    public static Connection getConnection() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro de conexão com banco: " + e.getMessage(), e);
        }
    }
}