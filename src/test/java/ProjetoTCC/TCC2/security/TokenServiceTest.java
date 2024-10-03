package ProjetoTCC.TCC2.security;

import ProjetoTCC.TCC2.entity.Usuario;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenServiceTest {

    @Autowired
    @InjectMocks
    private TokenService tokenService;

    @Mock
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(tokenService, "secret", "my-secret-key");
        when(usuario.getEmail()).thenReturn("usuario@teste.com");
    }

    @Test
    @DisplayName("Deve gerar token com sucesso")
    void gerarTokenComSucesso() {
        String token = tokenService.generateToken(usuario);

        assertNotNull(token, "O token gerado não deve ser nulo");
        assertTrue(token.length() > 0, "O token gerado deve ter algum comprimento");
        verify(usuario, times(1)).getEmail();
    }

    @Test
    @DisplayName("Deve validar token com sucesso")
    void validarTokenComSucesso() {
        String token = tokenService.generateToken(usuario);
        String email = tokenService.validateToken(token);

        assertNotNull(email, "O email não deve ser nulo para um token válido");
        assertEquals("usuario@teste.com", email, "O email deve corresponder ao do usuário");
    }

    @Test
    @DisplayName("Deve retornar nulo para token inválido")
    void retornarNuloParaTokenInvalido() {
        String invalidToken = "tokenInvalido";
        String email = tokenService.validateToken(invalidToken);

        assertNull(email, "O email deve ser nulo para um token inválido");
    }

    @Test
    @DisplayName("Deve lançar exceção ao falhar na geração do token")
    void falharNaGeracaoDoToken() {
        when(usuario.getEmail()).thenThrow(JWTCreationException.class);

        assertThrows(RuntimeException.class, () -> {
            tokenService.generateToken(usuario);
        }, "Deveria lançar uma RuntimeException ao gerar um token com erro");
    }
}
