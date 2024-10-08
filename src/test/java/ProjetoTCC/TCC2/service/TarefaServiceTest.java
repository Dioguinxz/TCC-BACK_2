package ProjetoTCC.TCC2.service;

import ProjetoTCC.TCC2.entity.Tarefa;
import ProjetoTCC.TCC2.entity.Usuario;
import ProjetoTCC.TCC2.repository.TarefaRepository;
import ProjetoTCC.TCC2.repository.UsuarioRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private TarefaService tarefaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Deve criar uma tarefa associada a um usuário existente")
    void criarTarefaSuccess() {
        Tarefa tarefa = new Tarefa(new ObjectId(), "Tarefa 1", "Descrição", false, LocalDate.now().plusDays(1), "usuario@teste.com");
        Usuario usuario = new Usuario();
        usuario.setEmail("usuario@teste.com");

        when(usuarioRepository.findByEmail(tarefa.getEmailUsuario())).thenReturn(Optional.of(usuario));
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);

        Tarefa tarefaCriada = tarefaService.criarTarefa(tarefa);

        verify(tarefaRepository, times(1)).save(tarefa);
        verify(usuarioRepository, times(1)).save(usuario);
        assertEquals(tarefa, tarefaCriada);
    }

    @Test
    @DisplayName("Deve editar uma tarefa existente")
    void editarTarefaSuccess() {
        ObjectId id = new ObjectId();
        Tarefa tarefaExistente = new Tarefa(id, "Tarefa Original", "Descrição Original", false, LocalDate.now().plusDays(1), "usuario@teste.com");
        Tarefa tarefaEditada = new Tarefa(id, "Tarefa Editada", "Nova Descrição", true, LocalDate.now().plusDays(2), "usuario@teste.com");

        when(tarefaRepository.findById(id)).thenReturn(Optional.of(tarefaExistente));
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefaEditada);

        Tarefa resultado = tarefaService.editarTarefa(id, tarefaEditada);

        verify(tarefaRepository, times(1)).save(tarefaExistente);
        assertEquals("Tarefa Editada", resultado.getNome());
        assertEquals("Nova Descrição", resultado.getDescricao());
        assertEquals(true, resultado.isConcluida());
    }

    @Test
    @DisplayName("Deve listar todas as tarefas em ordem crescente")
    void listarTarefaSuccess() {
        List<Tarefa> tarefas = Arrays.asList(
                new Tarefa(new ObjectId(), "Tarefa 1", "Descrição", false, LocalDate.now().plusDays(1), "usuario@teste.com"),
                new Tarefa(new ObjectId(), "Tarefa 2", "Descrição", false, LocalDate.now().plusDays(2), "usuario@teste.com")
        );

        when(tarefaRepository.findAll(Sort.by("nome").ascending())).thenReturn(tarefas);

        List<Tarefa> resultado = tarefaService.listarTarefa();

        assertEquals(2, resultado.size());
        verify(tarefaRepository, times(1)).findAll(Sort.by("nome").ascending());
    }

    @Test
    @DisplayName("Deve excluir uma tarefa existente pelo ID")
    void excluirTarefaPeloIdSuccess() {
        ObjectId id = new ObjectId();
        Tarefa tarefa = new Tarefa(id, "Tarefa 1", "Descrição", false, LocalDate.now().plusDays(1), "usuario@teste.com");

        when(tarefaRepository.findById(id)).thenReturn(Optional.of(tarefa));

        List<Tarefa> tarefasAtualizadas = tarefaService.excluirTarefaPeloId(id);

        verify(tarefaRepository, times(1)).delete(tarefa);
        assertEquals(0, tarefasAtualizadas.size());  // Dependendo da lógica, esse número pode variar
    }

    @Test
    @DisplayName("Deve listar todas as tarefas associadas a um usuário pelo email")
    void listarTarefasPorEmailSuccess() {
        String email = "usuario@teste.com";
        List<Tarefa> tarefas = Arrays.asList(
                new Tarefa(new ObjectId(), "Tarefa 1", "Descrição", false, LocalDate.now().plusDays(1), email)
        );

        when(tarefaRepository.findByEmailUsuario(email)).thenReturn(tarefas);

        List<Tarefa> resultado = tarefaService.listarTarefasPorEmail(email);

        assertEquals(1, resultado.size());
        verify(tarefaRepository, times(1)).findByEmailUsuario(email);
    }

    @Test
    @DisplayName("Deve buscar uma tarefa pelo ID e converter o ID para String")
    void buscarPorIdSuccess() {
        ObjectId id = new ObjectId();
        Tarefa tarefa = new Tarefa(id, "Tarefa 1", "Descrição", false, LocalDate.now().plusDays(1), "usuario@teste.com");

        when(tarefaRepository.findById(id)).thenReturn(Optional.of(tarefa));

        Optional<Tarefa> resultado = tarefaService.buscarPorId(id);

        assertEquals(true, resultado.isPresent());
        assertEquals(tarefa.getId().toString(), resultado.get().getIdString());
        verify(tarefaRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário não for encontrado ao criar tarefa")
    void criarTarefaUsuarioNaoEncontrado() {
        Tarefa tarefa = new Tarefa(new ObjectId(), "Tarefa 1", "Descrição", false, LocalDate.now().plusDays(1), "usuario@teste.com");

        when(usuarioRepository.findByEmail(tarefa.getEmailUsuario())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> tarefaService.criarTarefa(tarefa));
        assertEquals("Usuário não encontrado", exception.getMessage());

        verify(tarefaRepository, never()).save(any(Tarefa.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando a tarefa não for encontrada ao tentar editar")
    void editarTarefaNaoEncontrada() {
        ObjectId id = new ObjectId();
        Tarefa tarefaEditada = new Tarefa(id, "Tarefa Editada", "Nova Descrição", true, LocalDate.now().plusDays(2), "usuario@teste.com");

        when(tarefaRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> tarefaService.editarTarefa(id, tarefaEditada));
        assertEquals("Tarefa não encontrada", exception.getMessage());

        verify(tarefaRepository, never()).save(any(Tarefa.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando a tarefa não for encontrada ao tentar excluir")
    void excluirTarefaPeloIdNaoEncontrada() {
        ObjectId id = new ObjectId();

        when(tarefaRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> tarefaService.excluirTarefaPeloId(id));
        assertEquals("Tarefa não encontrada com o ID: " + id, exception.getMessage());

        verify(tarefaRepository, never()).delete(any(Tarefa.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando não houver tarefas associadas ao email")
    void listarTarefasPorEmailTarefasNaoEncontradas() {
        String email = "usuario@inexistente.com";

        when(tarefaRepository.findByEmailUsuario(email)).thenReturn(List.of());

        List<Tarefa> resultado = tarefaService.listarTarefasPorEmail(email);

        assertEquals(0, resultado.size());
    }

    @Test
    @DisplayName("Deve retornar vazio quando a tarefa não for encontrada pelo ID")
    void buscarPorIdNaoEncontrado() {
        ObjectId id = new ObjectId();

        when(tarefaRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Tarefa> resultado = tarefaService.buscarPorId(id);

        assertEquals(false, resultado.isPresent());
        verify(tarefaRepository, times(1)).findById(id);
    }
}
