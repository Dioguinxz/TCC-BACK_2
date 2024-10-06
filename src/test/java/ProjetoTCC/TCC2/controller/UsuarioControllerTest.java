package ProjetoTCC.TCC2.controller;

import ProjetoTCC.TCC2.entity.Usuario;
import ProjetoTCC.TCC2.service.UsuarioService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {
    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Deve criar um usuário com sucesso")
    void criarUsuarioSuccess() {
        Usuario usuario = new Usuario(new ObjectId(), "Teste", "teste@teste.com", "senha", new ArrayList<>());
        when(usuarioService.criarUsuario(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioController.criarUsuario(usuario);

        assertNotNull(resultado);
        assertEquals("Teste", resultado.getNome());
        verify(usuarioService, times(1)).criarUsuario(usuario);
    }

    @Test
    @DisplayName("Deve listar os usuários com sucesso")
    void listarUsuarioSuccess() {
        List<Usuario> usuarios = List.of(
                new Usuario(new ObjectId(), "Usuario 1", "usuario1@teste.com", "senha1", new ArrayList<>()),
                new Usuario(new ObjectId(), "Usuario 2", "usuario2@teste.com", "senha2", new ArrayList<>())
        );

        when(usuarioService.listarUsuario()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioController.listarUsuario();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(usuarioService, times(1)).listarUsuario();
    }

    @Test
    @DisplayName("Deve editar o usuário com sucesso")
    void editarUsuarioSuccess() {
        String email = "teste@teste.com";
        Usuario usuario = new Usuario(new ObjectId(), "Teste Editado", "teste@teste.com", "senha", new ArrayList<>());
        when(usuarioService.editarUsuario(eq(email), any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioController.editarUsuario(usuario, email);

        assertNotNull(resultado);
        assertEquals("Teste Editado", resultado.getNome());
        verify(usuarioService, times(1)).editarUsuario(eq(email), any(Usuario.class));
    }

    @Test
    @DisplayName("Deve excluir um usuário com sucesso")
    void excluirUsuarioSuccess() {
        ObjectId id = new ObjectId();
        List<Usuario> usuarios = List.of(
                new Usuario(id, "Usuario Removido", "teste@teste.com", "senha", new ArrayList<>())
        );
        when(usuarioService.excluirUsuarioPorId(id)).thenReturn(usuarios);

        List<Usuario> resultado = usuarioController.excluirUsuario(id);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Usuario Removido", resultado.get(0).getNome());
        verify(usuarioService, times(1)).excluirUsuarioPorId(id);
    }

    @Test
    @DisplayName("Deve buscar um usuário por email com sucesso")
    void buscarUsuarioPorEmailSuccess() {
        Usuario usuario = new Usuario(new ObjectId(), "Teste", "teste@teste.com", "senha", new ArrayList<>());
        when(usuarioService.buscarUsuarioPorEmail("teste@teste.com")).thenReturn(usuario);

        ResponseEntity<Usuario> resultado = usuarioController.buscarUsuarioPorEmail("teste@teste.com");

        assertNotNull(resultado);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals("Teste", resultado.getBody().getNome());
        verify(usuarioService, times(1)).buscarUsuarioPorEmail("teste@teste.com");
    }

    @Test
    @DisplayName("Deve excluir um usuário por email com sucesso")
    void excluirUsuarioPorEmailSuccess() {
        String email = "teste@teste.com";
        List<Usuario> usuarios = List.of(
                new Usuario(new ObjectId(), "Usuario Removido", email, "senha", new ArrayList<>())
        );

        when(usuarioService.excluirUsuarioPorEmail(email)).thenReturn(usuarios);

        List<Usuario> resultado = usuarioController.excluirUsuarioPorEmail(email);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Usuario Removido", resultado.get(0).getNome());
        verify(usuarioService, times(1)).excluirUsuarioPorEmail(email);
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir usuário por email não encontrado")
    void excluirUsuarioPorEmailNaoEncontrado() {
        String email = "naoexiste@teste.com";

        when(usuarioService.excluirUsuarioPorEmail(email)).thenThrow(new RuntimeException("Usuário não encontrado com o email: " + email));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioController.excluirUsuarioPorEmail(email);
        });

        assertEquals("Usuário não encontrado com o email: " + email, exception.getMessage());
        verify(usuarioService, times(1)).excluirUsuarioPorEmail(email);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar usuário com email existente")
    void criarUsuarioEmailExistente() {
        Usuario usuario = new Usuario(new ObjectId(), "Teste", "teste@teste.com", "senha", new ArrayList<>());
        when(usuarioService.criarUsuario(any(Usuario.class))).thenThrow(new RuntimeException("Email já cadastrado"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioController.criarUsuario(usuario);
        });

        assertEquals("Email já cadastrado", exception.getMessage());
        verify(usuarioService, times(1)).criarUsuario(usuario);
    }

    @Test
    @DisplayName("Deve lançar exceção ao editar usuário não encontrado")
    void editarUsuarioNaoEncontrado() {
        String email = "naoexiste@teste.com";
        Usuario usuario = new Usuario(new ObjectId(), "Teste", "teste@teste.com", "senha", new ArrayList<>());
        when(usuarioService.editarUsuario(eq(email), any(Usuario.class))).thenThrow(new RuntimeException("Usuário não encontrado"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioController.editarUsuario(usuario, email);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(usuarioService, times(1)).editarUsuario(eq(email), any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir usuário não encontrado")
    void excluirUsuarioNaoEncontrado() {
        ObjectId id = new ObjectId();
        when(usuarioService.excluirUsuarioPorId(id)).thenThrow(new RuntimeException("Usuário não encontrado"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioController.excluirUsuario(id);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(usuarioService, times(1)).excluirUsuarioPorId(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar usuário por email não encontrado")
    void buscarUsuarioPorEmailNaoEncontrado() {
        String email = "naoexiste@teste.com";
        when(usuarioService.buscarUsuarioPorEmail(email)).thenThrow(new RuntimeException("Usuário não encontrado"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioController.buscarUsuarioPorEmail(email);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(usuarioService, times(1)).buscarUsuarioPorEmail(email);
    }
}