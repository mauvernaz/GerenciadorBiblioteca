<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>${livro != null ? 'Editar Livro' : 'Novo Livro'}</title>
</head>
<body>
<h1>${livro != null ? 'Editar Livro' : 'Novo Livro'}</h1>

<form action="livros" method="post">

    <c:if test="${livro != null}">
        <input type="hidden" name="id" value="${livro.id}" />
    </c:if>

    <p>
        <label>Título:</label> <br>
        <input type="text" name="titulo" value="${livro.titulo}" required />
    </p>

    <p>
        <label>Autor:</label> <br>
        <input type="text" name="autor" value="${livro.autor}" required />
    </p>

    <p>
        <label>ISBN:</label> <br>
        <input type="text" name="isbn" value="${livro.isbn}" />
    </p>

    <p>
        <label>Quantidade Disponível:</label> <br>
        <input type="number" name="quantidade" value="${livro.quantidadeDisponivel}" required />
    </p>

    <button type="submit">Salvar</button>
    <a href="livros?acao=listar">Cancelar</a>
</form>
</body>
</html>