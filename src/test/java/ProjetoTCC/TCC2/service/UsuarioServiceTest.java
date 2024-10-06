package ProjetoTCC.TCC2.service;

import ProjetoTCC.TCC2.entity.Usuario;
import ProjetoTCC.TCC2.repository.UsuarioRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Autowired
    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Deve criar usuário com sucesso")
    void criarUsuarioSuccess() {
        Usuario user = new Usuario(new ObjectId(), "Teste", "teste@teste.com", "senha", new ArrayList<>());

        when(usuarioRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(user);

        Usuario usuarioCriado = usuarioService.criarUsuario(user);

        assertNotNull(usuarioCriado, "O usuário não deveria ser nulo");
        assertEquals("teste@teste.com", usuarioCriado.getEmail());

        verify(usuarioRepository, times(1)).findByEmail(user.getEmail());
        verify(usuarioRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Deve listar todos os usuários com sucesso")
    void listarUsuarioSuccess() {
        List<Usuario> usuarios = List.of(
                new Usuario(new ObjectId(), "Teste 1", "teste1@teste.com", "senha1", new ArrayList<>()),
                new Usuario(new ObjectId(), "Teste 2", "teste2@teste.com", "senha2", new ArrayList<>())
        );

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioService.listarUsuario();

        assertNotNull(resultado, "A lista de usuários não deveria ser nula");
        assertEquals(2, resultado.size(), "A lista de usuários deveria conter 2 usuários");
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve editar usuário existente com sucesso")
    void editarUsuarioSuccess() {
        Usuario usuarioExistente = new Usuario(new ObjectId(), "Teste Existente", "existente@teste.com", "senha", new ArrayList<>());
        Usuario usuarioEditado = new Usuario(new ObjectId(), "Teste novo", "novo@teste.com", "novaSenha", new ArrayList<>());

        when(usuarioRepository.findByEmail(usuarioExistente.getEmail())).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.findByEmail(usuarioEditado.getEmail())).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario usuarioSalvo = usuarioService.editarUsuario(usuarioExistente.getEmail(), usuarioEditado);

        assertNotNull(usuarioSalvo, "O usuário salvo não deveria ser nulo");
        assertEquals("Teste novo", usuarioSalvo.getNome(), "O nome do usuário deveria ser atualizado");
        assertEquals("novo@teste.com", usuarioSalvo.getEmail(), "O email do usuário deveria ser atualizado");
        verify(usuarioRepository, times(1)).findByEmail(usuarioExistente.getEmail());
        verify(usuarioRepository, times(1)).save(usuarioExistente);
    }

    @Test
    @DisplayName("Deve excluir usuário com sucesso")
    void excluirUsuarioSuccess() {
        List<Usuario> usuariosAposExclusao = List.of(
                new Usuario(new ObjectId(), "Teste", "teste@teste.com", "senha", new ArrayList<>())
        );

        when(usuarioRepository.findAll()).thenReturn(usuariosAposExclusao);

        List<Usuario> resultado = usuarioService.excluirUsuarioPorId(new ObjectId());

        assertNotNull(resultado, "A lista de usuários não deveria ser nula");
        assertEquals(1, resultado.size(), "A lista de usuários deveria conter 1 usuário após a exclusão");
        verify(usuarioRepository, times(1)).deleteById(any(ObjectId.class));
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar um usuário por email com sucesso")
    void buscarUsuarioPorEmailSuccess() {
        Usuario usuario = new Usuario(new ObjectId(), "Teste", "teste@teste.com", "senha", new ArrayList<>());

        when(usuarioRepository.findByEmail("teste@teste.com")).thenReturn(Optional.of(usuario));

        Usuario usuarioEncontrado = usuarioService.buscarUsuarioPorEmail("teste@teste.com");

        assertNotNull(usuarioEncontrado, "O usuário não deveria ser nulo");
        assertEquals("teste@teste.com", usuarioEncontrado.getEmail(), "O email do usuário deveria ser 'teste@teste.com'");
        verify(usuarioRepository, times(1)).findByEmail("teste@teste.com");
    }

    @Test
    @DisplayName("Deve excluir um usuário por email com sucesso")
    public void excluirUsuarioPorEmailSuccess() {
        String email = "usuario@teste.com";
        Usuario usuario = new Usuario();
        usuario.setEmail(email);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        List<Usuario> usuarios = usuarioService.excluirUsuarioPorEmail(email);

        verify(usuarioRepository).deleteByEmail(email);
        assertNotNull(usuarios);
        assertEquals(0, usuarios.size());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar excluir usuário com email não encontrado")
    public void excluirUsuarioPorEmailUsuarioNaoEncontrado() {
        String email = "usuario@naoencontrado.com";

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.excluirUsuarioPorEmail(email);
        });

        assertEquals("Usuário não encontrado com o email: " + email, exception.getMessage());
        verify(usuarioRepository, never()).deleteByEmail(email);
    }

    @Test
    @DisplayName("Não deve criar um usuário com email já registrado")
    void criarUsuarioComEmailJaRegistrado() {
        Usuario usuario = new Usuario(new ObjectId(), "Teste", "teste@teste.com", "senha", new ArrayList<>());

        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        assertThrows(IllegalArgumentException.class, () -> usuarioService.criarUsuario(usuario), "Email já registrado");

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Não deve editar um usuário inexistente")
    void editarUsuarioInexistente() {
        Usuario usuarioEditado = new Usuario(new ObjectId(), "Novo Teste", "novoemail@teste.com", "novaSenha", new ArrayList<>());

        when(usuarioRepository.findByEmail("email@inexistente.com")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.editarUsuario("email@inexistente.com", usuarioEditado);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
    }
}