package pro.facul.gerenciadorbiblioteca.controller;

import pro.facul.gerenciadorbiblioteca.dao.EmprestimoDAO;
import pro.facul.gerenciadorbiblioteca.dao.LivroDAO;
import pro.facul.gerenciadorbiblioteca.model.Emprestimo;
import pro.facul.gerenciadorbiblioteca.model.Livro;
import pro.facul.gerenciadorbiblioteca.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/emprestimos")
public class EmprestimoServlet extends HttpServlet {

    private EmprestimoDAO emprestimoDAO;
    private LivroDAO livroDAO;

    @Override
    public void init() throws ServletException {
        this.emprestimoDAO = new EmprestimoDAO();
        this.livroDAO = new LivroDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("relatorios".equals(action)) {

            request.setAttribute("topLivros", emprestimoDAO.listarLivrosMaisEmprestados());

            request.setAttribute("topUsuarios", emprestimoDAO.listarUsuariosMaisAtivos());

            request.setAttribute("atrasados", emprestimoDAO.listarAtrasados());

            request.getRequestDispatcher("/admin/relatorios.jsp").forward(request, response);
            return;
        }

        List<Usuario> usuarios = emprestimoDAO.listarTodosUsuarios();
        List<Livro> livrosDisponiveis = emprestimoDAO.listarLivrosDisponiveis();
        List<Livro> todosLivros = livroDAO.listarTodos();

        request.setAttribute("usuarios", usuarios);
        request.setAttribute("livrosDisponiveis", livrosDisponiveis);
        request.setAttribute("livros", todosLivros);

        List<Emprestimo> abertos = emprestimoDAO.listarTodosEmAberto();
        request.setAttribute("emprestimosAbertos", abertos);

        if ("historico".equals(action)) {
            filtrarHistorico(request);
        }

        request.getRequestDispatcher("/admin/emprestimos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        try {
            if ("criar".equals(action)) {
                criarEmprestimo(request);
                session.setAttribute("mensagemSucesso", "Empréstimo realizado com sucesso!");
            } else if ("devolver".equals(action)) {
                double multa = devolverLivro(request);

                if (multa > 0) {
                    session.setAttribute("mensagemSucesso", "Livro devolvido com atraso. Multa gerada: R$ " + String.format("%.2f", multa));
                } else {
                    session.setAttribute("mensagemSucesso", "Livro devolvido com sucesso! (Sem multa)");
                }
            }
        } catch (Exception e) {
            session.setAttribute("mensagemErro", "Erro ao processar solicitação: " + e.getMessage());
            e.printStackTrace();
        }

        response.sendRedirect("emprestimos");
    }

    private void criarEmprestimo(HttpServletRequest request) {
        int idUsuario = Integer.parseInt(request.getParameter("usuarioId"));
        int idLivro = Integer.parseInt(request.getParameter("livroId"));
        LocalDate dataPrevista = LocalDate.parse(request.getParameter("dataPrevista"));

        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);

        Livro livro = new Livro();
        livro.setId(idLivro);

        Emprestimo emprestimo = new Emprestimo(usuario, livro, LocalDate.now(), dataPrevista);

        emprestimoDAO.realizarEmprestimo(emprestimo);
    }

    private double devolverLivro(HttpServletRequest request) {
        int idEmprestimo = Integer.parseInt(request.getParameter("emprestimoId"));

        List<Emprestimo> abertos = emprestimoDAO.listarTodosEmAberto();
        Emprestimo emprestimoAlvo = null;

        for (Emprestimo e : abertos) {
            if (e.getId() == idEmprestimo) {
                emprestimoAlvo = e;
                break;
            }
        }

        if (emprestimoAlvo != null) {
            emprestimoDAO.devolverLivro(emprestimoAlvo);

            return emprestimoAlvo.getMulta();
        } else {
            throw new RuntimeException("Empréstimo não encontrado ou já finalizado.");
        }
    }

    private void filtrarHistorico(HttpServletRequest request) {
        String idUsuarioStr = request.getParameter("usuarioId");
        String idLivroStr = request.getParameter("livroId");
        String dataInicioStr = request.getParameter("dataInicio");
        String dataFimStr = request.getParameter("dataFim");

        Integer idUsuario = (idUsuarioStr != null && !idUsuarioStr.isEmpty()) ? Integer.parseInt(idUsuarioStr) : null;
        Integer idLivro = (idLivroStr != null && !idLivroStr.isEmpty()) ? Integer.parseInt(idLivroStr) : null;

        LocalDate dataInicio = (dataInicioStr != null && !dataInicioStr.isEmpty())
                ? LocalDate.parse(dataInicioStr) : null;
        LocalDate dataFim = (dataFimStr != null && !dataFimStr.isEmpty())
                ? LocalDate.parse(dataFimStr) : null;

        List<Emprestimo> historico = emprestimoDAO.buscarHistorico(idUsuario, idLivro, dataInicio, dataFim);
        request.setAttribute("historico", historico);
    }
}