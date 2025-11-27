<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login - Gerenciador de Biblioteca</title>
</head>
<body>

<h2>Login do Sistema</h2>

<form action="login" method="post">
    <div>
        <label for="email">Email:</label>
        <input type="email" id="email" name="email">
    </div>
    <div>
        <label for="senha">Senha:</label>
        <input type="password" id="senha" name="senha">
    </div>
    <div>
        <button type="submit">Entrar</button>
    </div>
</form>

<br>

<c:if test="${not empty erroLogin}">
    <p style="color: red;">
        <c:out value="${erroLogin}" />
    </p>
</c:if>

</body>
</html>