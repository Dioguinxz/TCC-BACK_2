package ProjetoTCC.TCC2.entity;

import ProjetoTCC.TCC2.validator.EmailValidator;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document(collection = "usuarios")
public class Usuario {

    @Id
    private ObjectId id;
    private String nome;
    private String email;
    private String senha;
    private List<Tarefa> tarefas;


    public Usuario(ObjectId id, String nome, String email, String senha, List<Tarefa> tarefas) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tarefas = tarefas;
    }

    public Usuario() {

    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }
}
