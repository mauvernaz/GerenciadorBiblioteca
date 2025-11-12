package pro.facul.gerenciadorbiblioteca.controller;

import pro.facul.gerenciadorbiblioteca.dao.UsuarioDAO;
import pro.facul.gerenciadorbiblioteca.model.Usuario;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        Usuario usuario = usuarioDAO.validarLogin(email, senha);

        if (usuario != null) {

            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogado", usuario);

            response.sendRedirect(request.getContextPath() + "/admin/livros.jsp");

        } else {

            request.setAttribute("erroLogin", "Email ou senha inválidos.");
            
            RequestDispatcher rd = request.getRequestDispatcher("/login.jsp");
            rd.forward(request, response);
        }
    }
}