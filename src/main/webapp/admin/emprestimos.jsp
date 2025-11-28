<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="usuario" value="${sessionScope.usuarioLogado}" />
<!DOCTYPE html>
<html>
<head>
    <title>Gerenciamento de Empréstimos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<nav>
    <a href="${pageContext.request.contextPath}/livros">📚 Livros</a> |
    <b>📖 Empréstimos</b>
    <c:if test="${usuario.tipo == 'ADMIN'}">
        | <a href="${pageContext.request.contextPath}/usuarios">👥 Usuários</a>
        | <a href="${pageContext.request.contextPath}/emprestimos?action=relatorios">📊 Relatórios</a>
    </c:if>
    <a href="${pageContext.request.contextPath}/logout" style="margin-left: auto; color: #dc3545;">Sair</a>
</nav>

<div class="main-container">
    <h1>Gerenciamento de Empréstimos</h1>

    <c:if test="${not empty mensagemSucesso}">
        <div class="msg-sucesso">✅ ${mensagemSucesso}</div>
    </c:if>
    <c:if test="${not empty mensagemErro}">
        <div class="msg-erro">❌ ${mensagemErro}</div>
    </c:if>

    <c:if test="${usuario.tipo == 'ADMIN'}">
        <h3 style="margin-top: 20px; color: #555;">➕ Novo Empréstimo</h3>
        <form action="${pageContext.request.contextPath}/emprestimos" method="post">
            <input type="hidden" name="action" value="criar"/>

            <div class="form-row">
                <div class="form-group" style="flex: 2;">
                    <label>Usuário:</label>
                    <select name="usuarioId" required>
                        <option value="">Selecione...</option>
                        <c:forEach var="u" items="${usuarios}">
                            <option value="${u.id}">${u.nome} (${u.email})</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group" style="flex: 2;">
                    <label>Livro:</label>
                    <select name="livroId" required>
                        <option value="">Selecione...</option>
                        <c:forEach var="l" items="${livrosDisponiveis}">
                            <option value="${l.id}">${l.titulo} - ${l.autor}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>Devolução Prevista:</label>
                    <input type="date" name="dataPrevista" required/>
                </div>
                <button type="submit">Registrar</button>
            </div>
        </form>
        <hr style="border: 0; border-top: 1px solid #eee; margin: 30px 0;">
    </c:if>

    <h3>📂 Empréstimos em Aberto</h3>
    <c:if test="${empty emprestimosAbertos}">
        <p style="color: #777; font-style: italic;">Nenhum empréstimo pendente.</p>
    </c:if>

    <c:if test="${not empty emprestimosAbertos}">
        <table>
            <thead>
            <tr>
                <th>Usuário</th>
                <th>Livro</th>
                <th>Saída</th>
                <th>Previsto</th>
                <th>Status</th>
                <th>Multa</th>
                <th>Ações</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="e" items="${emprestimosAbertos}">
                <tr>
                    <td>${e.usuario.nome}</td>
                    <td>${e.livro.titulo}</td>
                    <td>${e.dataEmprestimo}</td>
                    <td>${e.dataDevolucaoPrevista}</td>
                    <td>
                        <c:if test="${e.diasAtraso > 0}">
                            <span class="badge-atraso">${e.diasAtraso} dias atraso</span>
                        </c:if>
                        <c:if test="${e.diasAtraso <= 0}">
                            <span class="badge-ok">No prazo</span>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${e.multa > 0}"><strong style="color: #dc3545;">R$ ${e.multa}</strong></c:if>
                        <c:if test="${e.multa <= 0}">-</c:if>
                    </td>
                    <td>
                        <c:if test="${usuario.tipo == 'ADMIN'}">
                            <form style="display:inline;" action="${pageContext.request.contextPath}/emprestimos" method="post">
                                <input type="hidden" name="action" value="devolver"/>
                                <input type="hidden" name="emprestimoId" value="${e.id}"/>
                                <button type="submit" class="btn" style="width: auto; padding: 5px 10px; font-size: 0.8rem;">
                                    ↩️ Devolver
                                </button>
                            </form>
                        </c:if>

                        <c:if test="${usuario.tipo != 'ADMIN'}">
        <span style="color: #777; font-size: 0.85rem; font-style: italic;">
            Devolução presencial
        </span>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>

    <hr style="border: 0; border-top: 1px solid #eee; margin: 30px 0;">

    <h3>📜 Histórico Completo</h3>
    <form action="${pageContext.request.contextPath}/emprestimos" method="get">
        <input type="hidden" name="action" value="historico"/>

        <div class="form-row" style="background-color: #fff; border: 1px solid #ddd; padding: 10px;">
            <div class="form-group">
                <label>Usuário:</label>
                <select name="usuarioId">
                    <option value="">Todos</option>
                    <c:forEach var="u" items="${usuarios}">
                        <option value="${u.id}" ${param.usuarioId == u.id ? 'selected' : ''}>${u.nome}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label>Livro:</label>
                <select name="livroId">
                    <option value="">Todos</option>
                    <c:forEach var="l" items="${livros}">
                        <option value="${l.id}" ${param.livroId == l.id ? 'selected' : ''}>${l.titulo}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label>De:</label>
                <input type="date" name="dataInicio" value="${param.dataInicio}"/>
            </div>
            <div class="form-group">
                <label>Até:</label>
                <input type="date" name="dataFim" value="${param.dataFim}"/>
            </div>
            <button type="submit" style="background-color: #6c757d;">🔍 Filtrar</button>
        </div>
    </form>

    <c:if test="${not empty historico}">
        <table>
            <thead>
            <tr>
                <th>Usuário</th>
                <th>Livro</th>
                <th>Saída</th>
                <th>Previsto</th>
                <th>Devolução</th>
                <th>Situação</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="e" items="${historico}">
                <tr>
                    <td>${e.usuario.nome}</td>
                    <td>${e.livro.titulo}</td>
                    <td>${e.dataEmprestimo}</td>
                    <td>${e.dataDevolucaoPrevista}</td>
                    <td>${e.dataDevolucaoReal}</td>
                    <td>
                        <c:choose>
                            <c:when test="${e.dataDevolucaoReal == null}">
                                <span style="color: #0d6efd; font-weight: bold;">Em aberto</span>
                            </c:when>
                            <c:otherwise>
                                <span style="color: #666;">Finalizado</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>

    <c:if test="${empty historico and param.action == 'historico'}">
        <p style="padding: 10px; color: #777;">Nenhum histórico encontrado para este filtro.</p>
    </c:if>
</div>

</body>
</html>