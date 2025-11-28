package pro.facul.gerenciadorbiblioteca.dao;

import pro.facul.gerenciadorbiblioteca.model.Emprestimo;
import pro.facul.gerenciadorbiblioteca.model.Livro;
import pro.facul.gerenciadorbiblioteca.model.Usuario;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {

    public void realizarEmprestimo(Emprestimo emprestimo) {
        String sqlInsert = "INSERT INTO Emprestimo (id_usuario, id_livro, data_emprestimo, data_devolucao_prevista, status) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdateLivro = "UPDATE Livro SET quantidade_disponivel = quantidade_disponivel - 1 WHERE id = ?";

        Connection conn = null;
        PreparedStatement stmtInsert = null;
        PreparedStatement stmtUpdate = null;

        try {
            conn = ConectorBD.getConnection();

            conn.setAutoCommit(false);

            stmtInsert = conn.prepareStatement(sqlInsert);
            stmtInsert.setInt(1, emprestimo.getUsuario().getId());
            stmtInsert.setInt(2, emprestimo.getLivro().getId());
            stmtInsert.setDate(3, Date.valueOf(emprestimo.getDataEmprestimo()));
            stmtInsert.setDate(4, Date.valueOf(emprestimo.getDataDevolucaoPrevista()));
            stmtInsert.setString(5, "ATIVO");
            stmtInsert.executeUpdate();

            stmtUpdate = conn.prepareStatement(sqlUpdateLivro);
            stmtUpdate.setInt(1, emprestimo.getLivro().getId());
            stmtUpdate.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("Erro ao realizar empréstimo: " + e.getMessage(), e);
        } finally {
            try { if (stmtInsert != null) stmtInsert.close(); } catch (SQLException e) {}
            try { if (stmtUpdate != null) stmtUpdate.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
    }

    public void devolverLivro(Emprestimo emprestimo) {
        String sqlUpdateEmprestimo = "UPDATE Emprestimo SET data_devolucao_real = ?, status = ?, multa = ? WHERE id = ?";
        String sqlUpdateLivro = "UPDATE Livro SET quantidade_disponivel = quantidade_disponivel + 1 WHERE id = ?";

        LocalDate dataPrevista = emprestimo.getDataDevolucaoPrevista();
        LocalDate dataReal = LocalDate.now();
        long diasAtraso = ChronoUnit.DAYS.between(dataPrevista, dataReal);
        double valorMulta = 0.0;

        if (diasAtraso > 0) {
            valorMulta = diasAtraso * 2.0;
        }

        Connection conn = null;
        PreparedStatement stmtEmprestimo = null;
        PreparedStatement stmtLivro = null;

        try {
            conn = ConectorBD.getConnection();
            conn.setAutoCommit(false);

            stmtEmprestimo = conn.prepareStatement(sqlUpdateEmprestimo);
            stmtEmprestimo.setDate(1, Date.valueOf(dataReal));
            stmtEmprestimo.setString(2, "DEVOLVIDO");
            stmtEmprestimo.setDouble(3, valorMulta);
            stmtEmprestimo.setInt(4, emprestimo.getId());
            stmtEmprestimo.executeUpdate();

            stmtLivro = conn.prepareStatement(sqlUpdateLivro);
            stmtLivro.setInt(1, emprestimo.getLivro().getId());
            stmtLivro.executeUpdate();

            conn.commit();

            emprestimo.setDataDevolucaoReal(dataReal);
            emprestimo.setMulta(valorMulta);
            emprestimo.setStatus("DEVOLVIDO");

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw new RuntimeException("Erro ao devolver livro: " + e.getMessage(), e);
        } finally {
            try { if (stmtEmprestimo != null) stmtEmprestimo.close(); } catch (SQLException e) {}
            try { if (stmtLivro != null) stmtLivro.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
    }

    public List<Emprestimo> listarAtrasados() {
        String sql = "SELECT e.*, u.nome, l.titulo " +
                "FROM Emprestimo e " +
                "JOIN Usuario u ON e.id_usuario = u.id " +
                "JOIN Livro l ON e.id_livro = l.id " +
                "WHERE e.data_devolucao_prevista < CURDATE() AND e.status = 'ATIVO'";

        List<Emprestimo> lista = new ArrayList<>();

        try (Connection conn = ConectorBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Emprestimo emp = new Emprestimo();
                emp.setId(rs.getInt("id"));
                emp.setDataEmprestimo(rs.getDate("data_emprestimo").toLocalDate());
                emp.setDataDevolucaoPrevista(rs.getDate("data_devolucao_prevista").toLocalDate());
                emp.setStatus(rs.getString("status"));
                emp.setMulta(rs.getDouble("multa"));

                Usuario u = new Usuario();
                u.setId(rs.getInt("id_usuario"));
                u.setNome(rs.getString("nome"));
                emp.setUsuario(u);

                Livro l = new Livro();
                l.setId(rs.getInt("id_livro"));
                l.setTitulo(rs.getString("titulo"));
                emp.setLivro(l);

                lista.add(emp);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar atrasados: " + e.getMessage(), e);
        }

        return lista;
    }
}