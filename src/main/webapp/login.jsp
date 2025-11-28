<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login - Gerenciador de Biblioteca</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="center-screen"> <div class="login-card"> <h2>📚 Biblioteca</h2>
    <p style="margin-bottom: 20px; color: #666;">Entre com suas credenciais</p>

    <form action="login" method="post">

        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" placeholder="ex: admin@biblioteca.com" required>
        </div>

        <div class="form-group">
            <label for="senha">Senha</label>
            <input type="password" id="senha" name="senha" placeholder="Sua senha" required>
        </div>

        <button type="submit">Entrar</button>
    </form>

    <c:if test="${not empty erroLogin}">
        <div class="error-msg">
            <c:out value="${erroLogin}" />
        </div>
    </c:if>

</div>

</body>
</html>