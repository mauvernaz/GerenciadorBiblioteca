<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>${livro != null ? 'Editar Livro' : 'Novo Livro'}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="center-screen"> <div class="login-card" style="max-width: 500px;"> <h2>${livro != null ? 'Editar Livro' : 'Novo Livro'}</h2>

    <form action="livros" method="post">

        <c:if test="${livro != null}">
            <input type="hidden" name="id" value="${livro.id}" />
        </c:if>

        <div class="form-group">
            <label>Título:</label>
            <input type="text" name="titulo" value="${livro.titulo}" required placeholder="Ex: Clean Code" />
        </div>

        <div class="form-group">
            <label>Autor:</label>
            <input type="text" name="autor" value="${livro.autor}" required placeholder="Ex: Robert C. Martin" />
        </div>

        <div class="form-group">
            <label>ISBN:</label>
            <input type="text" name="isbn" value="${livro.isbn}" placeholder="000000000000" />
        </div>

        <div class="form-group">
            <label>Quantidade Disponível:</label>
            <input type="number" name="quantidade" value="${livro.quantidadeDisponivel}" required min="0" />
        </div>

        <button type="submit" style="margin-top: 10px;">Salvar</button>

        <a href="livros?acao=listar" class="btn-link">Cancelar</a>
    </form>

</div>

</body>
</html>