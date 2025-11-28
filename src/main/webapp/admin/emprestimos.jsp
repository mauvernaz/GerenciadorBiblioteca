<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="usuario" value="${sessionScope.usuarioLogado}" />

<html>
<head>
    <title>Gerenciamento de Empréstimos</title>
    <style>
        table { width: 100%; border-collapse: collapse; margin-top: 20px;}
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .btn { padding: 5px 10px; text-decoration: none; background: #ddd; border: 1px solid #ccc; color: black; border-radius: 3px;}
        .btn-danger { background: #ffcccc; color: darkred; border-radius: 3px; }
        .btn:hover { background: #ccc; }
        nav a { text-decoration: none; color: blue; }
        nav b { color: black; }
        .msg-sucesso { color: green; margin-top: 10px; }
        .msg-erro { color: red; margin-top: 10px; }
        form.inline { display: inline; }
        h1, h2, h3 { margin-bottom: 5px; }
    </style>
</head>
<body>

<h1>Gerenciamento de Empréstimos</h1>

<nav>
    <a href="${pageContext.request.contextPath}/livros">📚 Livros</a> |
    <b>📖 Empréstimos</b> |

    <c:if test="${usuario.tipo == 'ADMIN'}">
        <a href="${pageContext.request.contextPath}/usuarios">👥 Usuários</a> |
    </c:if>

    <a href="${pageContext.request.contextPath}/logout">Sair</a>
</nav>
<hr/>

<c:if test="${not empty mensagemSucesso}">
    <p class="msg-sucesso">${mensagemSucesso}</p>
</c:if>

<c:if test="${not empty mensagemErro}">
    <p class="msg-erro">${mensagemErro}</p>
</c:if>

<!-- =========================
           EMPRÉSTIMO
     ========================= -->
<c:if test="${usuario.tipo == 'ADMIN'}">
    <h2>Novo Empréstimo</h2>
    <form action="${pageContext.request.contextPath}/emprestimos" method="post">
        <input type="hidden" name="action" value="criar"/>

        <label>Usuário:</label>
        <select name="usuarioId" required>
            <option value="">Selecione...</option>
            <c:forEach var="u" items="${usuarios}">
                <option value="${u.id}">${u.nome} (${u.email})</option>
            </c:forEach>
        </select>

        <label>Livro:</label>
        <select name="livroId" required>
            <option value="">Selecione...</option>
            <c:forEach var="l" items="${livrosDisponiveis}">
                <option value="${l.id}">${l.titulo} - ${l.autor}</option>
            </c:forEach>
        </select>

        <label>Data prevista de devolução:</label>
        <input type="date" name="dataPrevista" required/>

        <button type="submit" class="btn">Registrar Empréstimo</button>
    </form>
    <hr/>
</c:if>

<!-- =========================
     EMPRÉSTIMOS EM ABERTO
     ========================= -->
<h2>Empréstimos em Aberto</h2>

<c:if test="${empty emprestimosAbertos}">
    <p>Nenhum empréstimo em aberto.</p>
</c:if>

<c:if test="${not empty emprestimosAbertos}">
    <table>
        <thead>
        <tr>
            <th>Usuário</th>
            <th>Livro</th>
            <th>Data Empréstimo</th>
            <th>Previsto p/ Devolução</th>
            <th>Dias de Atraso</th>
            <th>Multa Atual</th>
            <th>Ações</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="e" items="${emprestimosAbertos}">
            <tr>
                <td>${e.usuario.nome}</td>
                <td>${e.livro.titulo}</td>
                <td>${e.dataEmprestimo}</td>
                <td>${e.dataPrevista}</td>
                <td>${e.diasAtraso}</td>
                <td>
                    <c:choose>
                        <c:when test="${e.multa > 0}">
                            <span style="color: darkred;">R$ ${e.multa}</span>
                        </c:when>
                        <c:otherwise>
                            R$ 0,00
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <!-- Registrar devolução -->
                    <form class="inline" action="${pageContext.request.contextPath}/emprestimos" method="post">
                        <input type="hidden" name="action" value="devolver"/>
                        <input type="hidden" name="emprestimoId" value="${e.id}"/>
                        <button type="submit" class="btn">Registrar Devolução</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>

<hr/>

<!-- =========================
           HISTÓRICO
     ========================= -->
<h2>Histórico de Empréstimos</h2>

<form action="${pageContext.request.contextPath}/emprestimos" method="get">
    <input type="hidden" name="action" value="historico"/>

    <label>Usuário:</label>
    <select name="usuarioId">
        <option value="">Todos</option>
        <c:forEach var="u" items="${usuarios}">
            <option value="${u.id}" ${param.usuarioId == u.id ? 'selected' : ''}>${u.nome}</option>
        </c:forEach>
    </select>

    <label>Livro:</label>
    <select name="livroId">
        <option value="">Todos</option>
        <c:forEach var="l" items="${livros}">
            <option value="${l.id}" ${param.livroId == l.id ? 'selected' : ''}>${l.titulo}</option>
        </c:forEach>
    </select>

    <label>De:</label>
    <input type="date" name="dataInicio" value="${param.dataInicio}"/>

    <label>Até:</label>
    <input type="date" name="dataFim" value="${param.dataFim}"/>

    <button type="submit" class="btn">Filtrar</button>
</form>

<c:if test="${empty historico}">
    <p>Nenhum empréstimo encontrado para o filtro informado.</p>
</c:if>

<c:if test="${not empty historico}">
    <table>
        <thead>
        <tr>
            <th>Usuário</th>
            <th>Livro</th>
            <th>Empréstimo</th>
            <th>Previsto</th>
            <th>Devolução</th>
            <th>Dias de Atraso</th>
            <th>Multa</th>
            <th>Status</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="e" items="${historico}">
            <tr>
                <td>${e.usuario.nome}</td>
                <td>${e.livro.titulo}</td>
                <td>${e.dataEmprestimo}</td>
                <td>${e.dataPrevista}</td>
                <td>${e.dataDevolucaoReal}</td>
                <td>${e.diasAtraso}</td>
                <td>R$ ${e.multa}</td>
                <td>
                    <c:choose>
                        <c:when test="${e.dataDevolucaoReal == null}">
                            Em aberto
                        </c:when>
                        <c:otherwise>
                            Finalizado
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>

</body>
</html>