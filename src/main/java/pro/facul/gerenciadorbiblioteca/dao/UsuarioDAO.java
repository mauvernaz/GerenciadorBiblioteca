package pro.facul.gerenciadorbiblioteca.dao;

import pro.facul.gerenciadorbiblioteca.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario validarLogin(String email, String senha) {

        String sql = "SELECT * FROM Usuario WHERE email = ? AND senha = ?";

        Usuario usuario = null;

        try (Connection conn = ConectorBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setTipo(rs.getString("tipo"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao validar login: " + e.getMessage());
            e.printStackTrace();
        }

        return usuario;
    }

    //crud...
}