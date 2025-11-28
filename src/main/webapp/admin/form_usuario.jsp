<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>${usuario != null ? 'Editar Usuário' : 'Novo Usuário'}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="center-screen"> <div class="login-card" style="max-width: 500px;"> <h2>${usuario != null ? 'Editar Usuário' : 'Novo Usuário'}</h2>

    <form action="usuarios" method="post">

        <c:if test="${usuario != null}">
            <input type="hidden" name="id" value="${usuario.id}" />
        </c:if>

        <div class="form-group">
            <label>Nome Completo:</label>
            <input type="text" name="nome" value="${usuario.nome}" required placeholder="Ex: Fulanin da Silva" />
        </div>

        <div class="form-group">
            <label>Email:</label>
            <input type="email" name="email" value="${usuario.email}" required placeholder="Ex: fulanin@email.com" />
        </div>

        <div class="form-group">
            <label>Senha:</label>
            <input type="password" name="senha" value="${usuario.senha}" required placeholder="Digite a senha" />
            <c:if test="${usuario != null}">
                <small style="color: #666; font-size: 0.8rem; display: block; margin-top: 5px;">
                    ℹ️ Digite uma nova para alterar.
                </small>
            </c:if>
        </div>

        <div class="form-group">
            <label>Tipo de Usuário:</label>
            <select name="tipo">
                <option value="ALUNO" ${usuario.tipo == 'ALUNO' ? 'selected' : ''}>Aluno 🎓</option>
                <option value="ADMIN" ${usuario.tipo == 'ADMIN' ? 'selected' : ''}>Administrador 🛡️</option>
            </select>
        </div>

        <button type="submit" style="margin-top: 15px;">Salvar Usuário</button>

        <a href="usuarios?acao=listar" class="btn-link">Cancelar e Voltar</a>
    </form>

</div>

</body>
</html>