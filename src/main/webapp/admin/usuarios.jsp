<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="usuario" value="${sessionScope.usuarioLogado}" />

<html>
<head>
    <title>Gerenciar Usuários</title>
    <style>
        table { width: 100%; border-collapse: collapse; margin-top: 20px;}
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .btn { padding: 5px 10px; text-decoration: none; background: #ddd; border: 1px solid #ccc; color: black;}
        .btn-danger { background: #ffcccc; color: darkred; }
    </style>
</head>
<body>
<h1>Gerenciamento de Usuários (Alunos e Admins)</h1>

<nav>
    <a href="${pageContext.request.contextPath}/livros">📚 Livros</a> |
<a href="${pageContext.request.contextPath}/emprestimos">📝 Empréstimos</a>

    <c:if test="${usuario.tipo == 'ADMIN'}">
    <b>👥 Usuários</b> |
    </c:if>

    <a href="${pageContext.request.contextPath}/logout">Sair</a>
</nav>
<hr>

<c:if test="${usuario.tipo == 'ADMIN'}">
<p>
    <a href="usuarios?acao=novo" class="btn">➕ Cadastrar Novo Usuário</a>
</p>
</c:if>

<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Nome</th>
        <th>Email</th>
        <th>Tipo</th> <th>Ações</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="u" items="${listaUsuarios}">
        <tr>
            <td>${u.id}</td>
            <td>${u.nome}</td>
            <td>${u.email}</td>
            <td>
                <c:if test="${usuario.tipo == 'ADMIN'}">
                    <a href="usuarios?acao=editar&id=${u.id}">Editar</a>
                    &nbsp;|&nbsp;
                    <a href="usuarios?acao=deletar&id=${u.id}"
                       onclick="return confirm('Tem certeza que deseja excluir ${u.nome}?');"
                       style="color: red;">
                        Excluir
                    </a>
                </c:if>

                <c:if test="${usuario.tipo != 'ADMIN'}">
                    (Sem permissões)
                </c:if>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>