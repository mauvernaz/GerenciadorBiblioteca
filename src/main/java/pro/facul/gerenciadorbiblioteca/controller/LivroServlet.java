package pro.facul.gerenciadorbiblioteca.controller;

import pro.facul.gerenciadorbiblioteca.dao.LivroDAO;
import pro.facul.gerenciadorbiblioteca.model.Livro;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/livros")
public class LivroServlet extends HttpServlet {

    private LivroDAO livroDAO;

    public void init() {
        livroDAO = new LivroDAO();
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");
        if (acao == null) {
            acao = "listar";
        }

        try {
            switch (acao) {
                case "novo":
                    mostrarFormularioNovo(request, response);
                    break;
                case "editar":
                    mostrarFormularioEdicao(request, response);
                    break;
                case "deletar":
                    deletarLivro(request, response);
                    break;
                default:
                    listarLivros(request, response);
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        request.setCharacterEncoding("UTF-8");

        int id = 0;

        if (request.getParameter("id") != null && !request.getParameter("id").isEmpty()) {
            id = Integer.parseInt(request.getParameter("id"));
        }

        String titulo = request.getParameter("titulo");
        String autor = request.getParameter("autor");
        String isbn = request.getParameter("isbn");
        int quantidade = Integer.parseInt(request.getParameter("quantidade"));

        Livro livro = new Livro(titulo, autor, isbn, quantidade);
        livro.setId(id);

        if (id > 0) {
            livroDAO.atualizar(livro);
        } else {
            livroDAO.adicionar(livro);
        }

        response.sendRedirect("livros?acao=listar");
    }



    private void listarLivros(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Livro> listaLivros = livroDAO.listarTodos();
        request.setAttribute("listaLivros", listaLivros);
        RequestDispatcher dispatcher = request.getRequestDispatcher("admin/livros.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioNovo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("admin/form_livro.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioEdicao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Livro livroExistente = livroDAO.buscarPorId(id);
        request.setAttribute("livro", livroExistente);
        RequestDispatcher dispatcher = request.getRequestDispatcher("admin/form_livro.jsp");
        dispatcher.forward(request, response);
    }

    private void deletarLivro(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        livroDAO.excluir(id);
        response.sendRedirect("livros?acao=listar");
    }
}