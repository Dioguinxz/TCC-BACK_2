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
    Tarefa criarTarefa(@RequestBody Tarefa tarefa) {
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
     * @param
     * @return Lista das tarefas atualizada.
     */
    @PutMapping("/{id}")
    Tarefa editarTarefa(@PathVariable ObjectId id, @RequestBody Tarefa tarefaAtualizada) {
            return tarefaService.editarTarefa(id, tarefaAtualizada);
        }

    /**
     * Exclui uma tarefa pelo id.
     *
     * @param id da Tarefa.
     * @return Lista das tarefas atualizada.
     */
    @DeleteMapping("{id}")
    List<Tarefa> excluirTarefa(@PathVariable("id") ObjectId id) {
        return tarefaService.excluirTarefaPeloId(id);
    }

    /**
     * Busca as tarefas pelo email.
     *
     * @param email do usuário.
     * @return Lista das tarefas do usuário.
     */
    @GetMapping("/buscarPorEmail/{email}")
    List<Tarefa> buscarTarefaporEmail(@PathVariable String email) {
        return tarefaService.listarTarefasPorEmail(email);
    }
}

