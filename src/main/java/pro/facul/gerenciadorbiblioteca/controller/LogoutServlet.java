package pro.facul.gerenciadorbiblioteca.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession sessao = req.getSession(false);
        if (sessao != null) {
            sessao.invalidate();
        }
        resp.sendRedirect(req.getContextPath() + "/login.jsp");
    }
}