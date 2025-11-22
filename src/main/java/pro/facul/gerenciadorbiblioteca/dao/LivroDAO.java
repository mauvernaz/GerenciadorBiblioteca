package pro.facul.gerenciadorbiblioteca.dao;

import pro.facul.gerenciadorbiblioteca.model.Livro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    public void adicionar(Livro livro) {
        String sql = "INSERT INTO Livro (titulo, autor, isbn, quantidade_disponivel) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConectorBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setString(3, livro.getIsbn());
            stmt.setInt(4, livro.getQuantidadeDisponivel());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar livro: " + e.getMessage(), e);
        }
    }

    public List<Livro> listarTodos() {
        String sql = "SELECT * FROM Livro";
        List<Livro> livros = new ArrayList<>();

        try (Connection conn = ConectorBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Livro livro = new Livro();
                livro.setId(rs.getInt("id"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAutor(rs.getString("autor"));
                livro.setIsbn(rs.getString("isbn"));
                livro.setQuantidadeDisponivel(rs.getInt("quantidade_disponivel"));

                livros.add(livro);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar livros: " + e.getMessage(), e);
        }
        return livros;
    }

    public Livro buscarPorId(int id) {
        String sql = "SELECT * FROM Livro WHERE id = ?";
        Livro livro = null;

        try (Connection conn = ConectorBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    livro = new Livro();
                    livro.setId(rs.getInt("id"));
                    livro.setTitulo(rs.getString("titulo"));
                    livro.setAutor(rs.getString("autor"));
                    livro.setIsbn(rs.getString("isbn"));
                    livro.setQuantidadeDisponivel(rs.getInt("quantidade_disponivel"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livro por ID: " + e.getMessage(), e);
        }
        return livro;
    }

    public void atualizar(Livro livro) {
        String sql = "UPDATE Livro SET titulo = ?, autor = ?, isbn = ?, quantidade_disponivel = ? WHERE id = ?";

        try (Connection conn = ConectorBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setString(3, livro.getIsbn());
            stmt.setInt(4, livro.getQuantidadeDisponivel());
            stmt.setInt(5, livro.getId()); // O ID vai no WHERE

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar livro: " + e.getMessage(), e);
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM Livro WHERE id = ?";

        try (Connection conn = ConectorBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir livro: " + e.getMessage(), e);
        }
    }
}