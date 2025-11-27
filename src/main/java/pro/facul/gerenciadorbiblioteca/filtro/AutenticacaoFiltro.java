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
        // nada necessário aqui
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String caminho = req.getRequestURI(); // ex: /GerenciadorBiblioteca/admin/...
        String contexto = req.getContextPath(); // ex: /GerenciadorBiblioteca
        String pathRelativo = caminho.substring(contexto.length()); // ex: /admin/...

        // URLs públicas que NÃO precisam de autenticação
        boolean recursoPublico = pathRelativo.startsWith("/login.jsp")
                || pathRelativo.startsWith("/login")    // servlet /login
                || pathRelativo.startsWith("/static/")  // css/js/imagens (ajuste conforme seu projeto)
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
            // não logado → redirecionar para login
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // usuário logado — verificar tipo
        Object tipoObj = sessao.getAttribute("tipoUsuario");
        String tipo = tipoObj != null ? tipoObj.toString() : "";

        // Defina aqui as rotas que só o ADMIN pode acessar.
        // Ajuste as strings para os seus mapeamentos reais.
        boolean rotaSomenteAdmin =
                pathRelativo.startsWith("/admin")
                        || pathRelativo.startsWith("/livro/novo")
                        || pathRelativo.startsWith("/livro/salvar")
                        || pathRelativo.startsWith("/livro/editar")
                        || pathRelativo.startsWith("/livro/excluir");

        if (rotaSomenteAdmin && !"ADMIN".equalsIgnoreCase(tipo)) {
            // opcional: redirecionar para página de erro ou exibir 403
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado - restrito a administradores.");
            return;
        }

        // tudo ok
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // nada
    }
}