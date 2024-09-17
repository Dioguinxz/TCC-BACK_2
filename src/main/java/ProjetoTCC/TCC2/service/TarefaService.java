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
     * Cria uma tarefa e a associa a um usuário, antes a tarefa é validada e o verifica se o usuário existe.
     *
     * @param tarefa
     * @return Lista das tarefas.
     * @throws IllegalArgumentException Se o usuário associado à tarefa não for encotrado;
     */
    public List<Tarefa> criarTarefa(Tarefa tarefa) {

        tarefa.validate();

        Usuario usuario = usuarioRepository.findById(tarefa.getUsuarioId()).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (usuario.getTarefas() == null) {
            usuario.setTarefas(new ArrayList<>());
        }
        usuario.getTarefas().add(tarefa);

        tarefaRepository.save(tarefa);
        usuarioRepository.save(usuario);

        return listarTarefa();
    }

    /**
     * Lista todas as tarefas em ordem crescente.
     *
     * @return Lista das tarefas.
     */
    public List<Tarefa> listarTarefa() {
        Sort sort = Sort.by("nome").ascending();
        return tarefaRepository.findAll(sort);
    }

    /**
     * Edita uma tarefa existente e atualiza a lista de tarefas associada ao usuário.
     *
     * @param tarefa
     * @return Lista das tarefas atualizada.
     * @throws IllegalArgumentException Se o usuário associado à tarefa não for encontrado.
     */
    public List<Tarefa> editarTarefa(Tarefa tarefa) {
        tarefaRepository.save(tarefa);

        Usuario usuario = usuarioRepository.findById(tarefa.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o ID: " + tarefa.getUsuarioId()));

        List<Tarefa> tarefasDoUsuario = usuario.getTarefas();

        for (int i = 0; i < tarefasDoUsuario.size(); i++) {
            if (tarefasDoUsuario.get(i).getId().equals(tarefa.getId())) {
                tarefasDoUsuario.set(i, tarefa);
                break;
            }
        }
        usuarioRepository.save(usuario);
        return listarTarefa();
    }

    /**
     * Exclui uma tarefa pelo id, removendo-a da lista de tarefas do usuário associado.
     *
     * @param id da tarefa.
     * @return Lista das tarefas atualizada.
     * @throws IllegalArgumentException Se a tarefa ou o usuário associado não forem encontrados.
     */
    public List<Tarefa> excluirTarefa(ObjectId id) {
        Tarefa tarefa = tarefaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada com o ID: " + id));

        Usuario usuario = usuarioRepository.findById(tarefa.getUsuarioId()).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o ID: " + tarefa.getUsuarioId()));
        usuario.getTarefas().removeIf(t -> t.getId().equals(id));
        usuarioRepository.save(usuario);
        tarefaRepository.deleteById(id);

        return listarTarefa();
    }
}