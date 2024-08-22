package ProjetoTCC.TCC2.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "tarefas")
public class Tarefa {

    @Id
    private long id;
    private String nome;
    private String descricao;
    private boolean concluida;
    private LocalDate dataFinal;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    public LocalDate getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(LocalDate dataFinal) {
        this.dataFinal = dataFinal;
    }

    public void validate() {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da tarefa não pode ser vazio.");
        }
        if (descricao == null || descricao.length() > 100) {
            throw new IllegalArgumentException("A descrição não pode exceder 100 caracteres.");
        }
        if (dataFinal != null && dataFinal.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data final não pode ser uma data passada.");
        }
    }
}
