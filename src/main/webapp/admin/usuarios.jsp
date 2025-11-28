<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="usuario" value="${sessionScope.usuarioLogado}" />
<!DOCTYPE html>
<html>
<head>
    <title>Gerenciar Usuários</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<nav>
    <a href="${pageContext.request.contextPath}/livros">📚 Livros</a> |
    <a href="${pageContext.request.contextPath}/emprestimos">📖 Empréstimos</a>
    <c:if test="${usuario.tipo == 'ADMIN'}">
        | <b>👥 Usuários</b>
        | <a href="${pageContext.request.contextPath}/emprestimos?action=relatorios">📊 Relatórios</a>
    </c:if>
    <a href="${pageContext.request.contextPath}/logout" style="margin-left: auto; color: #dc3545;">Sair</a>
</nav>

<div class="main-container">
    <h1>Gerenciamento de Usuários</h1>

    <c:if test="${usuario.tipo == 'ADMIN'}">
        <p style="text-align: right;">
            <a href="usuarios?acao=novo" class="btn">➕ Cadastrar Novo Usuário</a>
        </p>
    </c:if>

    <table>
        <thead>
        <tr>
            <th width="5%">ID</th>
            <th width="30%">Nome</th>
            <th width="30%">Email</th>
            <th width="15%">Tipo</th>
            <th width="20%">Ações</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="u" items="${listaUsuarios}">
            <tr>
                <td>${u.id}</td>
                <td>${u.nome}</td>
                <td>${u.email}</td>
                <td>
                    <c:if test="${u.tipo == 'ADMIN'}"><strong style="color: #d63384;">ADMIN</strong></c:if>
                    <c:if test="${u.tipo != 'ADMIN'}"><span style="color: #0d6efd;">ALUNO</span></c:if>
                </td>
                <td>
                    <c:if test="${usuario.tipo == 'ADMIN'}">
                        <a href="usuarios?acao=editar&id=${u.id}" class="action-link edit-btn">Editar</a>
                        <a href="usuarios?acao=deletar&id=${u.id}"
                           onclick="return confirm('Tem certeza que deseja excluir ${u.nome}?');"
                           class="action-link delete-btn">Excluir</a>
                    </c:if>
                    <c:if test="${usuario.tipo != 'ADMIN'}">
                        <span style="color: #999; font-size: 0.85rem;">Leitura</span>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

</body>
</html>