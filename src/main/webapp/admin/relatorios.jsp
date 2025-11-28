<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="usuario" value="${sessionScope.usuarioLogado}" />

<c:if test="${usuario.tipo != 'ADMIN'}">
    <c:redirect url="/emprestimos"/>
</c:if>

<!DOCTYPE html>
<html>
<head>
    <title>Relatórios - Biblioteca</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<nav>
    <a href="${pageContext.request.contextPath}/livros">📚 Livros</a> |
    <a href="${pageContext.request.contextPath}/emprestimos">📖 Empréstimos</a> |
    <c:if test="${usuario.tipo == 'ADMIN'}">
        <a href="${pageContext.request.contextPath}/usuarios">👥 Usuários</a> |
        <b>📊 Relatórios</b>
    </c:if>
    <a href="${pageContext.request.contextPath}/logout" style="margin-left: auto; color: #dc3545;">Sair</a>
</nav>

<div class="main-container">
    <h1>Painel de Relatórios</h1>

    <h3>📚 Livros Mais Emprestados</h3>
    <table>
        <thead>
        <tr>
            <th class="text-left">Livro</th>
            <th class="text-center col-mini">Quantidade de Empréstimos</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="entry" items="${topLivros}">
            <tr>
                <td class="text-left">${entry.key}</td>
                <td class="text-center"><strong>${entry.value}</strong></td>
            </tr>
        </c:forEach>
        <c:if test="${empty topLivros}">
            <tr><td colspan="2" class="text-center">Nenhum registro encontrado.</td></tr>
        </c:if>
        </tbody>
    </table>
    <br><hr style="border:0; border-top:1px solid #eee;"><br>

    <h3>👥 Usuários Mais Ativos</h3>
    <table>
        <thead>
        <tr>
            <th class="text-left">Usuário</th>
            <th class="text-center col-mini">Quantidade de Empréstimos</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="entry" items="${topUsuarios}">
            <tr>
                <td class="text-left">${entry.key}</td>
                <td class="text-center"><strong>${entry.value}</strong></td>
            </tr>
        </c:forEach>
        <c:if test="${empty topUsuarios}">
            <tr><td colspan="2" class="text-center">Nenhum registro encontrado.</td></tr>
        </c:if>
        </tbody>
    </table>
    <br><hr style="border:0; border-top:1px solid #eee;"><br>

    <h3>⚠ Empréstimos em Atraso (Atual)</h3>
    <table>
        <thead>
        <tr>
            <th class="text-left">Usuário</th>
            <th class="text-left">Livro</th>
            <th class="text-center">Data Prevista</th>
            <th class="text-center">Dias de Atraso</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="e" items="${atrasados}">
            <tr>
                <td class="text-left">${e.usuario.nome}</td>
                <td class="text-left">${e.livro.titulo}</td>
                <td class="text-center">${e.dataDevolucaoPrevista}</td>
                <td class="text-center"><span class="badge-atraso">${e.diasAtraso} dias</span></td>
            </tr>
        </c:forEach>
        <c:if test="${empty atrasados}">
            <tr><td colspan="4" class="text-center" style="color: green;">Nenhum empréstimo atrasado no momento.</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

</body>
</html>