package ProjetoTCC.TCC2.controller;

import ProjetoTCC.TCC2.entity.Tarefa;
import ProjetoTCC.TCC2.service.TarefaService;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {
    private TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @PostMapping
    List<Tarefa> criarTarefa(@RequestBody Tarefa tarefa) {
        return tarefaService.criarTarefa(tarefa);
    }

    @GetMapping
    List<Tarefa> listarTarefa() {
        return tarefaService.listarTarefa();
    }

    @PutMapping
    List<Tarefa> editarTarefa(@RequestBody Tarefa tarefa) {
        return tarefaService.editarTarefa(tarefa);
    }

    @DeleteMapping("{id}")
    List<Tarefa> excluirTarefa(@PathVariable("id") ObjectId id) {
        return tarefaService.excluirTarefa(id);
    }
}

