# Gerenciador de Biblioteca (Introdução ao Desenvolvimento Web - UFF)

Projeto acadêmico de um sistema de gerenciamento de biblioteca, utilizando Servlets, JSP e JDBC com o padrão MVC.

## Tecnologias
* Java 11
* Servlets
* JSP (JavaServer Pages) + JSTL
* JDBC (com driver MySQL)
* Apache Tomcat 9
* MySQL 8 (via Docker)
* Maven

---

## Como Rodar

### 1. Pré-requisitos
* Java JDK 11+
* Docker Desktop
* Apache Tomcat 9
* IntelliJ IDEA Ultimate (ou outra IDE com suporte a Java EE) (foi usado IntelliJ)

### 2. Configuração
1.  **Clone o projeto.**
    ```bash
    git clone https://github.com/mauvernaz/GerenciadorBiblioteca
    cd GerenciadorBiblioteca
    ```

2.  **Inicie o Banco de Dados (Docker):**
    ```bash
    docker run -d --name gerenciadorbibliotecabd -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=gerenciador_biblioteca mysql:8.0
    ```

3.  **Crie as Tabelas:**
    * Conecte-se ao banco (localhost:3306, user: `root`, pass: `root`) usando um cliente SQL (foi usado MySQL).
    * Execute o script `init.sql` (que está na raiz do projeto) para criar as tabelas e inserir os dados de teste.

4.  **Configure o Servidor no IntelliJ:**
    * Vá em **Run > Edit Configurations...**
    * Adicione um novo servidor **Tomcat (Local)** e aponte para a pasta do seu Tomcat 9.
    * Na aba **"Deployment"**, adicione o artefato `GerenciadorBiblioteca:war exploded`.
    * Mude o **"Application context"** para apenas **`/`**.

### 3. Execução
1.  Rode o projeto no IntelliJ (na config do Tomcat).
2.  Acesse o sistema no seu navegador:
    **[http://localhost:8080/login.jsp](http://localhost:8080/login.jsp)**

---

### Login de Teste (Admin)
* **Email:** `admin@biblioteca.com`
* **Senha:** `admin123`
