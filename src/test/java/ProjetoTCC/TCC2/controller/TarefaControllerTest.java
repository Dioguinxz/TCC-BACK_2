package ProjetoTCC.TCC2.controller;

import ProjetoTCC.TCC2.entity.Tarefa;
import ProjetoTCC.TCC2.service.TarefaService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TarefaControllerTest {

    @InjectMocks
    private TarefaController tarefaController;

    @Mock
    private TarefaService tarefaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Deve criar uma tarefa com sucesso")
    void criarTarefaSuccess() {
        Tarefa tarefa = new Tarefa(new ObjectId(), "Tarefa de Teste", "Descrição da tarefa", false, LocalDate.now().plusDays(1), "usuario@teste.com");
        when(tarefaService.criarTarefa(any(Tarefa.class))).thenReturn(tarefa);

        Tarefa resultado = tarefaController.criarTarefa(tarefa);

        assertNotNull(resultado);
        assertEquals("Tarefa de Teste", resultado.getNome());
        verify(tarefaService, times(1)).criarTarefa(any(Tarefa.class));
    }

    @Test
    @DisplayName("Deve editar uma tarefa com sucesso")
    void editarTarefaSuccess() {
        ObjectId id = new ObjectId();
        Tarefa tarefa = new Tarefa(id, "Tarefa de Teste", "Descrição da tarefa", false, LocalDate.now().plusDays(1), "usuario@teste.com");
        when(tarefaService.editarTarefa(eq(id), any(Tarefa.class))).thenReturn(tarefa);

        Tarefa resultado = tarefaController.editarTarefa(id, tarefa);

        assertNotNull(resultado);
        assertEquals("Tarefa de Teste", resultado.getNome());
        verify(tarefaService, times(1)).editarTarefa(eq(id), any(Tarefa.class));
    }


    @Test
    @DisplayName("Deve listar as tarefas com sucesso")
    void listarTarefaSuccess() {
        List<Tarefa> tarefas = List.of(
                new Tarefa(new ObjectId(), "Tarefa 1", "Descrição 1", false, LocalDate.now().plusDays(1), "usuario@teste.com"),
                new Tarefa(new ObjectId(), "Tarefa 2", "Descrição 2", false, LocalDate.now().plusDays(1), "usuario@teste.com")
        );
        when(tarefaService.listarTarefa()).thenReturn(tarefas);

        List<Tarefa> resultado = tarefaController.listarTarefa();
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(tarefaService, times(1)).listarTarefa();
    }

    @Test
    @DisplayName("Deve excluir uma tarefa com sucesso")
    void excluirTarefaSuccess() {
        List<Tarefa> tarefas = new ArrayList<>();
        Tarefa tarefa = new Tarefa(new ObjectId(), "Tarefa de Teste", "Descrição da tarefa", false, LocalDate.now().plusDays(1), "usuario@teste.com");
        tarefas.add(tarefa);
        when(tarefaService.excluirTarefaPeloId(any(ObjectId.class))).thenReturn(tarefas);

        List<Tarefa> resultado = tarefaController.excluirTarefa(tarefa.getId());

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Tarefa de Teste", resultado.get(0).getNome());
        verify(tarefaService, times(1)).excluirTarefaPeloId(any(ObjectId.class));
    }

    @Test
    @DisplayName("Deve buscar tarefas por email com sucesso")
    void buscarTarefaporEmailSuccess() {
        Tarefa tarefa = new Tarefa(new ObjectId(), "Tarefa de Teste", "Descrição da tarefa", false, LocalDate.now().plusDays(1), "usuario@teste.com");
        String email = "usuario@teste.com";
        List<Tarefa> tarefas = List.of(tarefa);
        when(tarefaService.listarTarefasPorEmail(email)).thenReturn(tarefas);

        List<Tarefa> resultado = tarefaController.buscarTarefaporEmail(email);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Tarefa de Teste", resultado.get(0).getNome());
        verify(tarefaService, times(1)).listarTarefasPorEmail(email);
    }

    @Test
    @DisplayName("Deve lançar exceção ao editar tarefa não encontrada")
    void editarTarefaNaoEncontrada() {
        Tarefa tarefa = new Tarefa(new ObjectId(), "Tarefa Editada", "Descrição editada", false, LocalDate.now().plusDays(1), "usuario@teste.com");

        when(tarefaService.editarTarefa(any(ObjectId.class), any(Tarefa.class))).thenThrow(new RuntimeException("Tarefa não encontrada"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            tarefaController.editarTarefa(tarefa.getId(), tarefa);
        });

        assertEquals("Tarefa não encontrada", exception.getMessage());
        verify(tarefaService, times(1)).editarTarefa(any(ObjectId.class), any(Tarefa.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir tarefa não encontrada")
    void excluirTarefaNaoEncontrada() {
        ObjectId tarefaId = new ObjectId();

        when(tarefaService.excluirTarefaPeloId(tarefaId)).thenThrow(new RuntimeException("Tarefa não encontrada"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            tarefaController.excluirTarefa(tarefaId);
        });

        assertEquals("Tarefa não encontrada", exception.getMessage());
        verify(tarefaService, times(1)).excluirTarefaPeloId(tarefaId);
    }
}
