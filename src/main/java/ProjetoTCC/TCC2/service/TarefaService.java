package ProjetoTCC.TCC2.service;

import ProjetoTCC.TCC2.entity.Tarefa;
import ProjetoTCC.TCC2.entity.Usuario;
import ProjetoTCC.TCC2.repository.TarefaRepository;
import ProjetoTCC.TCC2.repository.UsuarioRepository;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelas operações de CRUD da Tarefa e associação das tarefas a usuários.
 */
@Service
public class TarefaService {
    private TarefaRepository tarefaRepository;
    private UsuarioRepository usuarioRepository;

    public TarefaService(TarefaRepository tarefaRepository, UsuarioRepository usuarioRepository) {
        this.tarefaRepository = tarefaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Cria uma tarefa e a associa a um usuário.
     * A tarefa é validada e verifica se o usuário existe.
     *
     * @param tarefa A tarefa a ser criada.
     * @return tarefa criada.
     * @throws IllegalArgumentException Se o usuário associado à tarefa não for encontrado.
     */
    public Tarefa criarTarefa(Tarefa tarefa) {

        tarefa.validate();

        Usuario usuario = usuarioRepository.findByEmail(tarefa.getEmailUsuario()).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (usuario.getTarefas() == null) {
            usuario.setTarefas(new ArrayList<>());
        }
        usuario.getTarefas().add(tarefa);

        tarefaRepository.save(tarefa);
        usuarioRepository.save(usuario);

        return tarefa;
    }

    /**
     * Lista todas as tarefas em ordem crescente.
     * Converte o id de ObjectId para String.
     *
     * @return Lista das tarefas.
     */
    public List<Tarefa> listarTarefa() {
        Sort sort = Sort.by("nome").ascending();
        List<Tarefa> tarefas = tarefaRepository.findAll(sort);

        for (Tarefa tarefa : tarefas) {
            tarefa.setIdString(tarefa.getId().toString());
        }

        return tarefas;
    }

    /**
     * Edita uma tarefa existente pelo ID.
     *
     * @param id               da tarefa.
     * @param tarefaAtualizada Novos dados da tarefa.
     * @return A tarefa atualizada.
     * @throws RuntimeException Se a tarefa com o ID fornecido não for encontrada.
     */
    public Tarefa editarTarefa(ObjectId id, Tarefa tarefaAtualizada) {
        tarefaAtualizada.validate();
        Tarefa tarefaExistente = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        tarefaExistente.setNome(tarefaAtualizada.getNome());
        tarefaExistente.setDescricao(tarefaAtualizada.getDescricao());
        tarefaExistente.setDataFinal(tarefaAtualizada.getDataFinal());
        tarefaExistente.setConcluida(tarefaAtualizada.isConcluida());
        tarefaExistente.setEmailUsuario(tarefaAtualizada.getEmailUsuario());

        return tarefaRepository.save(tarefaExistente);
    }

    /**
     * Exclui uma tarefa pelo id.
     *
     * @param id da tarefa.
     * @return Lista das tarefas atualizada.
     * @throws IllegalArgumentException Se a tarefa não for encontrada.
     */
    public List<Tarefa> excluirTarefaPeloId(ObjectId id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada com o ID: " + id));

        tarefaRepository.delete(tarefa);

        return listarTarefa();
    }

    /**
     * Lista todas as tarefas associadas a um usuário pelo seu email.
     * Converte o id de ObjectId para String.
     *
     * @param email do usuário.
     * @return Lista de tarefas associadas ao usuário.
     * @throws IllegalArgumentException Se o usuário não for encontrado.
     */
    public List<Tarefa> listarTarefasPorEmail(String email) {
        List<Tarefa> tarefas = tarefaRepository.findByEmailUsuario(email);

        for (Tarefa tarefa : tarefas) {
            tarefa.setIdString(tarefa.getId().toString());
        }
        return tarefas;
    }

    /**
     * Busca uma tarefa pelo id.
     * Converte o id de ObjectId para String.
     *
     * @param id da tarefa.
     * @return A tarefa encontrada.
     */
    public Optional<Tarefa> buscarPorId(ObjectId id) {
        Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id);

        if (tarefaOptional.isPresent()) {
            Tarefa tarefa = tarefaOptional.get();
            tarefa.setIdString(tarefa.getId().toString());  // Converte o id para String
        }

        return tarefaOptional;
    }
}
