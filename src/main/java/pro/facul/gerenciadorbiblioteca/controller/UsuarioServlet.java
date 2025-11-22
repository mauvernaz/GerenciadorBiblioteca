package pro.facul.gerenciadorbiblioteca.controller;

import pro.facul.gerenciadorbiblioteca.dao.UsuarioDAO;
import pro.facul.gerenciadorbiblioteca.model.Usuario;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");
        if (acao == null) acao = "listar";

        switch (acao) {
            case "novo":
                mostrarFormulario(request, response);
                break;
            case "editar":
                mostrarFormularioEdicao(request, response);
                break;
            case "deletar":
                deletarUsuario(request, response);
                break;
            default:
                listarUsuarios(request, response);
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        int id = 0;
        if (request.getParameter("id") != null && !request.getParameter("id").isEmpty()) {
            id = Integer.parseInt(request.getParameter("id"));
        }

        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String tipo = request.getParameter("tipo");

        Usuario usuario = new Usuario(nome, email, senha);
        usuario.setTipo(tipo);
        usuario.setId(id);

        if (id > 0) {
            usuarioDAO.atualizar(usuario);
        } else {
            usuarioDAO.adicionar(usuario);
        }
        response.sendRedirect("usuarios?acao=listar");
    }

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Usuario> lista = usuarioDAO.listarTodos();
        request.setAttribute("listaUsuarios", lista);
        RequestDispatcher dispatcher = request.getRequestDispatcher("admin/usuarios.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("admin/form_usuario.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioEdicao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Usuario usuario = usuarioDAO.buscarPorId(id);
        request.setAttribute("usuario", usuario);
        RequestDispatcher dispatcher = request.getRequestDispatcher("admin/form_usuario.jsp");
        dispatcher.forward(request, response);
    }

    private void deletarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        usuarioDAO.excluir(id);
        response.sendRedirect("usuarios?acao=listar");
    }
}