package pro.facul.gerenciadorbiblioteca.filtro;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AutenticacaoFiltro implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String caminho = req.getRequestURI();
        String contexto = req.getContextPath();
        String pathRelativo = caminho.substring(contexto.length());


        boolean recursoPublico = pathRelativo.startsWith("/login.jsp")
                || pathRelativo.startsWith("/login")
                || pathRelativo.startsWith("/static/")
                || pathRelativo.startsWith("/resources/")
                || pathRelativo.endsWith(".css")
                || pathRelativo.endsWith(".js")
                || pathRelativo.endsWith(".png")
                || pathRelativo.endsWith(".jpg");

        if (recursoPublico) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession sessao = req.getSession(false);

        if (sessao == null || sessao.getAttribute("usuarioLogado") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }


        Object tipoObj = sessao.getAttribute("tipoUsuario");
        String tipo = tipoObj != null ? tipoObj.toString() : "";



        boolean rotaSomenteAdmin =
                pathRelativo.startsWith("/admin")
                        || pathRelativo.startsWith("/livro/novo")
                        || pathRelativo.startsWith("/livro/salvar")
                        || pathRelativo.startsWith("/livro/editar")
                        || pathRelativo.startsWith("/livro/excluir");

        if (rotaSomenteAdmin && !"ADMIN".equalsIgnoreCase(tipo)) {

            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado - restrito a administradores.");
            return;
        }


        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}