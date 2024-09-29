package ProjetoTCC.TCC2.controller;

import ProjetoTCC.TCC2.entity.Tarefa;
import ProjetoTCC.TCC2.service.TarefaService;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável por disponibilizar os endpoints HTTP para o CRUD da Tarefa.
 */
@RestController
@RequestMapping("/tarefas")
public class TarefaController {
    private TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    /**
     * Criar uma nova tarefa.
     *
     * @param tarefa
     * @return Lista das tarefas.
     */
    @PostMapping
    List<Tarefa> criarTarefa(@RequestBody Tarefa tarefa) {
        return tarefaService.criarTarefa(tarefa);
    }

    /**
     * Lista as tarefas.
     *
     * @return Lista das tarefas.
     */
    @GetMapping
    List<Tarefa> listarTarefa() {
        return tarefaService.listarTarefa();
    }

    /**
     * Edita uma tarefa existente.
     *
     * @param tarefa
     * @return Lista das tarefas atualizada.
     */
    @PutMapping
    List<Tarefa> editarTarefa(@RequestBody Tarefa tarefa) {
        return tarefaService.editarTarefa(tarefa);
    }

    /**
     * Exclui uma tarefa pelo id.
     *
     * @param id da Tarefa.
     * @return Lista das tarefas atualizada.
     */
    @DeleteMapping("{id}")
    List<Tarefa> excluirTarefa(@PathVariable("id") ObjectId id) {
        return tarefaService.excluirTarefa(id);
    }

    @GetMapping("{email}")
    List<Tarefa> buscarTarefaporEmail(@PathVariable String email) {
        return tarefaService.listarTarefasPorEmail(email);
    }
}

