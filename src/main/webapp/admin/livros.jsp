<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Painel Admin</title>
</head>
<body>
<h1>Bem-vindo, Administrador!</h1>
<p>Você está logado.</p>

<p>Usuário: ${sessionScope.usuarioLogado.nome}</p>
<p>Email: ${sessionScope.usuarioLogado.email}</p>
<p>Tipo: ${sessionScope.usuarioLogado.tipo}</p>
</body>
</html>