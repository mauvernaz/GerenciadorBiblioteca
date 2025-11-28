<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="usuario" value="${sessionScope.usuarioLogado}" />
<!DOCTYPE html>
<html>
<head>
    <title>Gerenciar Livros</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<nav>
    <b>📚 Livros</b> |
    <a href="${pageContext.request.contextPath}/emprestimos">📝 Empréstimos</a>

    <c:if test="${usuario.tipo =='ADMIN'}">
        | <a href="${pageContext.request.contextPath}/usuarios">👥 Usuários</a>
    </c:if>

    <a href="${pageContext.request.contextPath}/logout" style="margin-left: auto; color: #dc3545;">Sair</a>
</nav>

<div class="main-container">

    <h1>Gerenciamento de Livros</h1>

    <c:if test="${usuario.tipo == 'ADMIN'}">
        <p style="text-align: right;">
            <a href="livros?acao=novo" class="btn">➕ Novo Livro</a>
        </p>
    </c:if>

    <table>
        <thead>
        <tr>
            <th width="5%">ID</th>
            <th width="30%">Título</th>
            <th width="20%">Autor</th>
            <th width="15%">ISBN</th>
            <th width="10%">Qtd.</th>
            <th width="20%">Ações</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="livro" items="${listaLivros}">
            <tr>
                <td>${livro.id}</td>
                <td>${livro.titulo}</td>
                <td>${livro.autor}</td>
                <td>${livro.isbn}</td>
                <td>${livro.quantidadeDisponivel}</td>
                <td>
                    <c:if test="${usuario.tipo == 'ADMIN'}">
                        <a href="livros?acao=editar&id=${livro.id}" class="action-link edit-btn">Editar</a>

                        <a href="livros?acao=deletar&id=${livro.id}"
                           onclick="return confirm('Tem certeza que deseja excluir o livro ${livro.titulo}?');"
                           class="action-link delete-btn">
                            Excluir
                        </a>
                    </c:if>

                    <c:if test="${usuario.tipo != 'ADMIN'}">
                        <span style="color: #999; font-size: 0.85rem;">Apenas leitura</span>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <c:if test="${empty listaLivros}">
        <p style="padding: 20px; text-align: center; color: #777;">Nenhum livro cadastrado ainda.</p>
    </c:if>

</div>

</body>
</html>