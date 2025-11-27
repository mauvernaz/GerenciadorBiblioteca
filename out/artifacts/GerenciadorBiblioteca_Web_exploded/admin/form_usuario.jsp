<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>${usuario != null ? 'Editar Usuário' : 'Novo Usuário'}</title>
</head>
<body>
<h1>${usuario != null ? 'Editar Usuário' : 'Novo Usuário'}</h1>

<form action="usuarios" method="post">

    <c:if test="${usuario != null}">
        <input type="hidden" name="id" value="${usuario.id}" />
    </c:if>

    <p>
        <label>Nome Completo:</label><br>
        <input type="text" name="nome" value="${usuario.nome}" required style="width: 300px;" />
    </p>

    <p>
        <label>Email:</label><br>
        <input type="email" name="email" value="${usuario.email}" required style="width: 300px;" />
    </p>

    <p>
        <label>Senha:</label><br>
        <input type="password" name="senha" value="${usuario.senha}" required />
        <br><i>(Para editar, confirme a senha atual ou digite uma nova)</i>
    </p>

    <p>
        <label>Tipo de Usuário:</label><br>
        <select name="tipo">
            <option value="ALUNO" ${usuario.tipo == 'ALUNO' ? 'selected' : ''}>Aluno</option>
            <option value="ADMIN" ${usuario.tipo == 'ADMIN' ? 'selected' : ''}>Administrador</option>
        </select>
    </p>

    <br>
    <button type="submit">Salvar Usuário</button>
    <a href="usuarios?acao=listar">Cancelar</a>
</form>
</body>
</html>