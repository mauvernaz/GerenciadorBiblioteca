USE gerenciador_biblioteca;

CREATE TABLE Usuario (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         nome VARCHAR(255) NOT NULL,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         senha VARCHAR(255) NOT NULL,
                         tipo VARCHAR(50) NOT NULL DEFAULT 'ALUNO'
);

CREATE TABLE Livro (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       titulo VARCHAR(255) NOT NULL,
                       autor VARCHAR(255) NOT NULL,
                       isbn VARCHAR(100) UNIQUE,
                       quantidade_disponivel INT NOT NULL DEFAULT 0
);

CREATE TABLE Emprestimo (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            id_usuario INT NOT NULL,
                            id_livro INT NOT NULL,
                            data_emprestimo DATE NOT NULL,
                            data_devolucao_prevista DATE NOT NULL,
                            data_devolucao_real DATE,
                            status VARCHAR(50) NOT NULL DEFAULT 'ATIVO',
                            FOREIGN KEY (id_usuario) REFERENCES Usuario(id),
                            FOREIGN KEY (id_livro) REFERENCES Livro(id)
);


INSERT INTO Usuario (nome, email, senha, tipo)
VALUES ('Admin', 'admin@biblioteca.com', 'admin123', 'ADMIN');

INSERT INTO Usuario (nome, email, senha, tipo)
VALUES ('Aluno Teste', 'aluno@facul.com', 'aluno123', 'ALUNO');