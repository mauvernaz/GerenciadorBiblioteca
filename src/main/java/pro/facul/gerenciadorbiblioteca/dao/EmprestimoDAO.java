package pro.facul.gerenciadorbiblioteca.dao;

import pro.facul.gerenciadorbiblioteca.model.Emprestimo;
import pro.facul.gerenciadorbiblioteca.model.Livro;
import pro.facul.gerenciadorbiblioteca.model.Usuario;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EmprestimoDAO {

    public List<Usuario> listarTodosUsuarios() {
        String sql = "SELECT * FROM Usuario ORDER BY nome";
        List<Usuario> lista = new ArrayList<>();
        try (Connection conn = ConectorBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                lista.add(u);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários: " + e.getMessage(), e);
        }
        return lista;
    }

    public List<Livro> listarLivrosDisponiveis() {
        String sql = "SELECT * FROM Livro WHERE quantidade_disponivel > 0 ORDER BY titulo";
        List<Livro> lista = new ArrayList<>();
        try (Connection conn = ConectorBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Livro l = new Livro();
                l.setId(rs.getInt("id"));
                l.setTitulo(rs.getString("titulo"));
                l.setAutor(rs.getString("autor"));
                l.setQuantidadeDisponivel(rs.getInt("quantidade_disponivel"));
                lista.add(l);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar livros disponíveis: " + e.getMessage(), e);
        }
        return lista;
    }

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
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
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

        emprestimo.registrarDevolucao();

        Connection conn = null;
        PreparedStatement stmtEmprestimo = null;
        PreparedStatement stmtLivro = null;

        try {
            conn = ConectorBD.getConnection();
            conn.setAutoCommit(false);
            stmtEmprestimo = conn.prepareStatement(sqlUpdateEmprestimo);
            stmtEmprestimo.setDate(1, Date.valueOf(emprestimo.getDataDevolucaoReal()));
            stmtEmprestimo.setString(2, emprestimo.getStatus());
            stmtEmprestimo.setDouble(3, emprestimo.getMulta());
            stmtEmprestimo.setInt(4, emprestimo.getId());
            stmtEmprestimo.executeUpdate();

            stmtLivro = conn.prepareStatement(sqlUpdateLivro);
            stmtLivro.setInt(1, emprestimo.getLivro().getId());
            stmtLivro.executeUpdate();

            conn.commit();

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
        String sql = "SELECT e.*, u.nome, l.titulo, l.autor " +
                "FROM Emprestimo e " +
                "JOIN Usuario u ON e.id_usuario = u.id " +
                "JOIN Livro l ON e.id_livro = l.id " +
                "WHERE e.data_devolucao_prevista < CURDATE() AND e.status = 'ATIVO'";

        List<Emprestimo> lista = new ArrayList<>();

        try (Connection conn = ConectorBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(montarEmprestimo(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar atrasados: " + e.getMessage(), e);
        }
        return lista;
    }

    public Map<String, Integer> listarLivrosMaisEmprestados() {
        Map<String, Integer> resultado = new LinkedHashMap<>();
        String sql = "SELECT l.titulo, COUNT(e.id) as total " +
                "FROM Emprestimo e " +
                "JOIN Livro l ON e.id_livro = l.id " +
                "GROUP BY l.titulo " +
                "ORDER BY total DESC LIMIT 10";

        try (Connection conn = ConectorBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                resultado.put(rs.getString("titulo"), rs.getInt("total"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao gerar relatório de livros: " + e.getMessage(), e);
        }
        return resultado;
    }

    public Map<String, Integer> listarUsuariosMaisAtivos() {
        Map<String, Integer> resultado = new LinkedHashMap<>();
        String sql = "SELECT u.nome, COUNT(e.id) as total " +
                "FROM Emprestimo e " +
                "JOIN Usuario u ON e.id_usuario = u.id " +
                "GROUP BY u.nome " +
                "ORDER BY total DESC LIMIT 10";

        try (Connection conn = ConectorBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                resultado.put(rs.getString("nome"), rs.getInt("total"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao gerar relatório de usuários: " + e.getMessage(), e);
        }
        return resultado;
    }

    public List<Emprestimo> listarTodosEmAberto() {
        String sql = "SELECT e.*, u.nome, l.titulo, l.autor " +
                "FROM Emprestimo e " +
                "JOIN Usuario u ON e.id_usuario = u.id " +
                "JOIN Livro l ON e.id_livro = l.id " +
                "WHERE e.status = 'ATIVO' " +
                "ORDER BY e.data_devolucao_prevista ASC";

        List<Emprestimo> lista = new ArrayList<>();

        try (Connection conn = ConectorBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(montarEmprestimo(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar empréstimos em aberto: " + e.getMessage(), e);
        }
        return lista;
    }

    public List<Emprestimo> buscarHistorico(Integer idUsuario, Integer idLivro, LocalDate dataInicio, LocalDate dataFim) {
        StringBuilder sql = new StringBuilder(
                "SELECT e.*, u.nome, l.titulo, l.autor " +
                        "FROM Emprestimo e " +
                        "JOIN Usuario u ON e.id_usuario = u.id " +
                        "JOIN Livro l ON e.id_livro = l.id " +
                        "WHERE 1=1 ");

        if (idUsuario != null) sql.append(" AND e.id_usuario = ? ");
        if (idLivro != null) sql.append(" AND e.id_livro = ? ");
        if (dataInicio != null) sql.append(" AND e.data_emprestimo >= ? ");
        if (dataFim != null) sql.append(" AND e.data_emprestimo <= ? ");

        sql.append(" ORDER BY e.data_emprestimo DESC");

        List<Emprestimo> lista = new ArrayList<>();

        try (Connection conn = ConectorBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (idUsuario != null) stmt.setInt(index++, idUsuario);
            if (idLivro != null) stmt.setInt(index++, idLivro);
            if (dataInicio != null) stmt.setDate(index++, Date.valueOf(dataInicio));
            if (dataFim != null) stmt.setDate(index++, Date.valueOf(dataFim));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(montarEmprestimo(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar histórico: " + e.getMessage(), e);
        }
        return lista;
    }

    private Emprestimo montarEmprestimo(ResultSet rs) throws SQLException {
        Emprestimo emp = new Emprestimo();
        emp.setId(rs.getInt("id"));
        emp.setDataEmprestimo(rs.getDate("data_emprestimo").toLocalDate());
        emp.setDataDevolucaoPrevista(rs.getDate("data_devolucao_prevista").toLocalDate());

        Date dataReal = rs.getDate("data_devolucao_real");
        if (dataReal != null) {
            emp.setDataDevolucaoReal(dataReal.toLocalDate());
        }

        emp.setStatus(rs.getString("status"));
        emp.setMulta(rs.getDouble("multa"));

        Usuario u = new Usuario();
        u.setId(rs.getInt("id_usuario"));
        u.setNome(rs.getString("nome"));
        emp.setUsuario(u);

        Livro l = new Livro();
        l.setId(rs.getInt("id_livro"));
        l.setTitulo(rs.getString("titulo"));
        l.setAutor(rs.getString("autor"));
        emp.setLivro(l);

        return emp;
    }
}