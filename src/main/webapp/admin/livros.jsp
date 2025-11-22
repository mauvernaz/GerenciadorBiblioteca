<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Gerenciar Livros</title>
    <style>
        table { width: 100%; border-collapse: collapse; margin-top: 20px;}
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .btn { padding: 5px 10px; text-decoration: none; background: #ddd; border: 1px solid #ccc; color: black; border-radius: 3px;}
        .btn:hover { background: #ccc; }

        /* Estilos de navegação simples */
        nav a { text-decoration: none; color: blue; }
        nav b { color: black; }
    </style>
</head>
<body>
<h1>Gerenciamento de Livros</h1>

<nav>
    <b>📚 Livros</b> |
    <a href="${pageContext.request.contextPath}/usuarios">👥 Usuários</a> |
    <a href="${pageContext.request.contextPath}/login.jsp">Sair</a>
</nav>
<hr>

<p>
    <a href="livros?acao=novo" class="btn">➕ Cadastrar Novo Livro</a>
</p>

<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Título</th>
        <th>Autor</th>
        <th>ISBN</th>
        <th>Qtd.</th>
        <th>Ações</th>
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
                <a href="livros?acao=editar&id=${livro.id}">Editar</a>
                &nbsp;|&nbsp;
                <a href="livros?acao=deletar&id=${livro.id}"
                   onclick="return confirm('Tem certeza que deseja excluir o livro ${livro.titulo}?');"
                   style="color: red;">
                    Excluir
                </a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>