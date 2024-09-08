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

@Service
public class TarefaService {
    private TarefaRepository tarefaRepository;
    private UsuarioRepository usuarioRepository;

    public TarefaService(TarefaRepository tarefaRepository, UsuarioRepository usuarioRepository) {
        this.tarefaRepository = tarefaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Tarefa> criarTarefa(Tarefa tarefa) {

        tarefa.validate();

        Usuario usuario = usuarioRepository.findById(tarefa.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o ID: " + tarefa.getUsuarioId()));

        if (usuario.getTarefas() == null) {
            usuario.setTarefas(new ArrayList<>());
        }
        usuario.getTarefas().add(tarefa);

        tarefaRepository.save(tarefa);
        usuarioRepository.save(usuario);

        return listarTarefa();
    }

    public List<Tarefa> listarTarefa() {
        Sort sort = Sort.by("nome").ascending();
        return tarefaRepository.findAll(sort);
    }

    public List<Tarefa> editarTarefa(Tarefa tarefa) {
        tarefaRepository.save(tarefa);
        return listarTarefa();
    }

    public List<Tarefa> excluirTarefa(ObjectId id) {
        tarefaRepository.deleteById(id);
        return listarTarefa();
    }
}