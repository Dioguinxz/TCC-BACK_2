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
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Autowired
    @InjectMocks
    private TarefaService tarefaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Deve criar a tarefa com sucesso")
    void criarTarefaSuccess() {
        Tarefa tarefa = new Tarefa(new ObjectId(), "Tarefa 1", "Descrição", false, LocalDate.now().plusDays(1), "usuario@teste.com");
        Usuario usuario = new Usuario(new ObjectId(), "Teste", "teste@teste.com", "Senha", new ArrayList<>());

        when(usuarioRepository.findByEmail(tarefa.getEmailUsuario())).thenReturn(Optional.of(usuario));
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        List<Tarefa> tarefas = tarefaService.criarTarefa(tarefa);

        assertNotNull(tarefas);
        assertEquals(1, tarefas.size());
        assertEquals("Tarefa 1", tarefas.get(0).getNome());
        verify(usuarioRepository, times(1)).save(usuario);
    }


    @Test
    @DisplayName("Deve listar todas as tarefas com sucesso")
    void listarTarefaSuccess() {
        List<Tarefa> tarefas = List.of(
                new Tarefa(new ObjectId(), "Tarefa 1", "Descrição 1", false, LocalDate.now().plusDays(1), "usuario@teste.com"),
                new Tarefa(new ObjectId(), "Tarefa 2", "Descrição 2", false, LocalDate.now().plusDays(1), "usuario@teste.com")
        );

        when(tarefaRepository.findAll()).thenReturn(tarefas);

        List<Tarefa> resultado = tarefaService.listarTarefa();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(tarefaRepository, times(1)).findAll();
    }


    @Test
    @DisplayName("Deve editar a tarefa com sucesso")
    void editarTarefaSuccess() {
        Tarefa tarefa = new Tarefa(new ObjectId(), "Tarefa Editada", "Descrição editada", false, LocalDate.now().plusDays(1), "usuario@teste.com");

        Usuario usuario = new Usuario(new ObjectId(), "Teste", "usuario@teste.com", "senha", new ArrayList<>());
        usuario.getTarefas().add(tarefa);

        when(usuarioRepository.findByEmail(tarefa.getEmailUsuario())).thenReturn(Optional.of(usuario));
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);

        List<Tarefa> resultado = tarefaService.editarTarefa(tarefa);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Tarefa Editada", resultado.get(0).getNome());
        verify(tarefaRepository, times(1)).save(tarefa);
    }


    @Test
    @DisplayName("Deve excluir a tarefa com sucesso")
    void excluirTarefaSuccess() {
        ObjectId idTarefa = new ObjectId();
        Tarefa tarefa = new Tarefa(idTarefa, "Tarefa para excluir", "Descrição", false, LocalDate.now().plusDays(1), "usuario@teste.com");

        Usuario usuario = new Usuario(new ObjectId(), "Teste", "usuario@teste.com", "senha", new ArrayList<>());
        usuario.getTarefas().add(tarefa);

        when(tarefaRepository.findById(idTarefa)).thenReturn(Optional.of(tarefa));
        when(usuarioRepository.findByEmail(tarefa.getEmailUsuario())).thenReturn(Optional.of(usuario));

        List<Tarefa> resultado = tarefaService.excluirTarefa(idTarefa);

        assertNotNull(resultado);
        assertTrue(usuario.getTarefas().isEmpty());
        verify(tarefaRepository, times(1)).deleteById(idTarefa);
    }

    @Test
    @DisplayName("Deve listar as tarefas do usuário pelo email com sucesso")
    void listarTarefasPorEmailSuccess() {
        String emailUsuario = "usuario@teste.com";
        Usuario usuario = new Usuario(new ObjectId(), "Teste", emailUsuario, "senha", new ArrayList<>());
        List<Tarefa> tarefas = new ArrayList<>();
        tarefas.add(new Tarefa(new ObjectId(), "Tarefa 1", "Descrição 1", false, LocalDate.now().plusDays(1), emailUsuario));

        usuario.setTarefas(tarefas);
        when(usuarioRepository.findByEmail(emailUsuario)).thenReturn(Optional.of(usuario));

        List<Tarefa> resultado = tarefaService.listarTarefasPorEmail(emailUsuario);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(usuarioRepository, times(1)).findByEmail(emailUsuario);
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar tarefa se usuário não existir")
    void criarTarefaUsuarioNaoExistente() {
        Tarefa tarefa = new Tarefa(new ObjectId(), "Nova Tarefa", "Descrição", false, LocalDate.now().plusDays(1), "usuario@teste.com");

        when(usuarioRepository.findByEmail(tarefa.getEmailUsuario())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            tarefaService.criarTarefa(tarefa);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(tarefaRepository, never()).save(any(Tarefa.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar tarefa com data passada")
    void criarTarefaComDataPassada() {
        Tarefa tarefa = new Tarefa(new ObjectId(), "Tarefa com Data Passada", "Descrição da tarefa", false, LocalDate.now().minusDays(1), "usuario@teste.com");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tarefa.validate();
        });

        assertEquals("A data final não pode ser uma data passada.", exception.getMessage());
    }

}