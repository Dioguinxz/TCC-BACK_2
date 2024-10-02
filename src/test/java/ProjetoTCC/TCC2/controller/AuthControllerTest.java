package ProjetoTCC.TCC2.controller;

import ProjetoTCC.TCC2.dto.LoginRequestDTO;
import ProjetoTCC.TCC2.dto.RegisterRequestDTO;
import ProjetoTCC.TCC2.dto.ResponseDTO;
import ProjetoTCC.TCC2.entity.Usuario;
import ProjetoTCC.TCC2.repository.UsuarioRepository;
import ProjetoTCC.TCC2.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("usuario@teste.com");
        usuario.setSenha("senha123");
    }

    @Test
    @DisplayName("Deve permitir login com sucesso")
    void testLoginSuccess() {
        LoginRequestDTO loginRequest = new LoginRequestDTO("usuario@teste.com", "senha123");
        when(usuarioRepository.findByEmail("usuario@teste.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senha123", "senha123")).thenReturn(true);
        when(tokenService.generateToken(usuario)).thenReturn("token123");

        ResponseEntity<ResponseDTO> response = authController.login(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Teste", response.getBody().nome());
        assertEquals("token123", response.getBody().token());
    }

    @Test
    @DisplayName("Deve permitir registro com sucesso")
    void testRegisterSuccess() {
        RegisterRequestDTO registerRequest = new RegisterRequestDTO(null, "Novo Usuario", "usuario@teste.com", "senha123");
        when(usuarioRepository.findByEmail("usuario@teste.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("senha123")).thenReturn("senhaCriptografada");
        when(tokenService.generateToken(any(Usuario.class))).thenReturn("token123");

        ResponseEntity<ResponseDTO> response = authController.register(registerRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Novo Usuario", response.getBody().nome());
        assertEquals("token123", response.getBody().token());
    }

    @Test
    @DisplayName("Deve retornar erro ao fornecer senha inválida")
    void testLoginInvalidPassword() {
        LoginRequestDTO loginRequest = new LoginRequestDTO("usuario@teste.com", "senha123");
        when(usuarioRepository.findByEmail("usuario@teste.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senha123", "senhaErrada")).thenReturn(false);

        ResponseEntity<ResponseDTO> response = authController.login(loginRequest);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("Deve retornar erro ao registrar email já existente")
    void testRegisterEmailJaRegistrado() {
        RegisterRequestDTO registerRequest = new RegisterRequestDTO(null, "Novo Usuario", "usuario@teste.com", "senha123");
        when(usuarioRepository.findByEmail("usuario@teste.com")).thenReturn(Optional.of(usuario));

        ResponseEntity<ResponseDTO> response = authController.register(registerRequest);

        assertEquals(400, response.getStatusCodeValue());
    }
}