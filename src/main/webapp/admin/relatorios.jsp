<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="usuario" value="${sessionScope.usuarioLogado}" />

<c:if test="${usuario.tipo != 'ADMIN'}">
    <c:redirect url="/emprestimos"/>
</c:if>

<html>
<head>
    <title>Relatórios - Biblioteca</title>
</head>
<body>

    <h1>Painel de Relatórios</h1>

    <nav>
        <a href="${pageContext.request.contextPath}/livros">📚 Livros</a> |
        <a href="${pageContext.request.contextPath}/emprestimos">📝 Empréstimos</a> |

        <c:if test="${usuario.tipo == 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/usuarios">👥 Usuários</a> |
            <b>📊 Relatórios</b> |
        </c:if>

        <a href="${pageContext.request.contextPath}/logout">Sair</a>
    </nav>
    <hr/>

    <h2>📚 Livros Mais Emprestados</h2>
    <table border="1" width="100%">
        <thead>
            <tr>
                <th>Livro</th>
                <th>Quantidade de Empréstimos</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="entry" items="${topLivros}">
                <tr>
                    <td>${entry.key}</td>
                    <td><strong>${entry.value}</strong></td>
                </tr>
            </c:forEach>
            <c:if test="${empty topLivros}">
                <tr><td colspan="2">Nenhum registro encontrado.</td></tr>
            </c:if>
        </tbody>
    </table>

    <br>

    <h2>👥 Usuários Mais Ativos</h2>
    <table border="1" width="100%">
        <thead>
            <tr>
                <th>Usuário</th>
                <th>Quantidade de Empréstimos</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="entry" items="${topUsuarios}">
                <tr>
                    <td>${entry.key}</td>
                    <td><strong>${entry.value}</strong></td>
                </tr>
            </c:forEach>
            <c:if test="${empty topUsuarios}">
                <tr><td colspan="2">Nenhum registro encontrado.</td></tr>
            </c:if>
        </tbody>
    </table>

    <br>

    <h2>⚠ Empréstimos em Atraso (Atual)</h2>
    <table border="1" width="100%">
        <thead>
            <tr>
                <th>Usuário</th>
                <th>Livro</th>
                <th>Data Prevista</th>
                <th>Dias de Atraso</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="e" items="${atrasados}">
                <tr>
                    <td>${e.usuario.nome}</td>
                    <td>${e.livro.titulo}</td>
                    <td>${e.dataDevolucaoPrevista}</td>
                    <td style="color: red; font-weight: bold;">${e.diasAtraso} dias</td>
                </tr>
            </c:forEach>
            <c:if test="${empty atrasados}">
                <tr><td colspan="4" align="center">Nenhum empréstimo atrasado no momento! 🎉</td></tr>
            </c:if>
        </tbody>
    </table>

</body>
</html>