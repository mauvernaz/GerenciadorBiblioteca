package pro.facul.gerenciadorbiblioteca.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Emprestimo {

    private int id;
    private Usuario usuario;
    private Livro livro;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal;
    private String status;
    private double multa;

    public Emprestimo() {
    }

    public Emprestimo(Usuario usuario, Livro livro, LocalDate dataEmprestimo, LocalDate dataDevolucaoPrevista) {
        this.usuario = usuario;
        this.livro = livro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.status = "ATIVO";
        this.multa = 0.0;
    }

    public void registrarDevolucao() {
        this.dataDevolucaoReal = LocalDate.now();
        this.status = "DEVOLVIDO";

        long dias = getDiasAtraso();
        if (dias > 0) {
            this.multa = dias * 2.0;
        } else {
            this.multa = 0.0;
        }
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Livro getLivro() { return livro; }
    public void setLivro(Livro livro) { this.livro = livro; }

    public LocalDate getDataEmprestimo() { return dataEmprestimo; }
    public void setDataEmprestimo(LocalDate dataEmprestimo) { this.dataEmprestimo = dataEmprestimo; }

    public LocalDate getDataDevolucaoPrevista() { return dataDevolucaoPrevista; }
    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) { this.dataDevolucaoPrevista = dataDevolucaoPrevista; }

    public LocalDate getDataDevolucaoReal() { return dataDevolucaoReal; }
    public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) { this.dataDevolucaoReal = dataDevolucaoReal; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getMulta() {
        if ("DEVOLVIDO".equalsIgnoreCase(this.status) || this.dataDevolucaoReal != null) {
            return this.multa;
        }
        long dias = getDiasAtraso();
        if (dias > 0) {
            return dias * 2.0;
        }
        return 0.0;
    }

    public void setMulta(double multa) { this.multa = multa; }

    public long getDiasAtraso() {
        LocalDate dataFim = (dataDevolucaoReal != null) ? dataDevolucaoReal : LocalDate.now();

        if (dataDevolucaoPrevista.isBefore(dataFim)) {
            return ChronoUnit.DAYS.between(dataDevolucaoPrevista, dataFim);
        }
        return 0;
    }
}