//package ProjetoTCC.TCC2.security;
//
//import ProjetoTCC.TCC2.entity.Usuario;
//import com.auth0.jwt.exceptions.JWTCreationException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.test.context.TestPropertySource;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.doThrow;
//
//@TestPropertySource(properties = "api.security.token.secret=my-secret-key")
//class TokenServiceTest {
//
//   package ProjetoTCC.TCC2.security;
//
//import ProjetoTCC.TCC2.entity.Usuario;
//import com.auth0.jwt.exceptions.JWTCreationException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.MockitoAnnotations;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//    class TokenServiceTest {
//
//        @InjectMocks
//        private TokenService tokenService;
//
//        @BeforeEach
//        void setUp() {
//            MockitoAnnotations.openMocks(this);
//            tokenService.secret = "my-secret-key"; // Define a chave secreta diretamente
//        }
//
//        @Test
//        @DisplayName("Deve gerar um token JWT com sucesso")
//        void generateTokenSuccess() {
//            Usuario usuario = new Usuario();
//            usuario.setEmail("usuario@teste.com");
//
//            String token = tokenService.generateToken(usuario);
//
//            assertNotNull(token);
//            assertTrue(token.startsWith("ey")); // Verifica se o token tem o formato JWT
//        }
//
//        @Test
//        @DisplayName("Deve lançar exceção ao gerar token JWT com usuário nulo")
//        void generateTokenWithNullUser() {
//            assertThrows(RuntimeException.class, () -> {
//                tokenService.generateToken(null);
//            });
//        }
//
//        @Test
//        @DisplayName("Deve validar o token JWT com sucesso")
//        void validateTokenSuccess() {
//            Usuario usuario = new Usuario();
//            usuario.setEmail("usuario@teste.com");
//
//            String token = tokenService.generateToken(usuario);
//            String email = tokenService.validateToken(token);
//
//            assertNotNull(email);
//            assertEquals("usuario@teste.com", email);
//        }
//
//        @Test
//        @DisplayName("Deve retornar null ao validar um token inválido")
//        void validateTokenInvalid() {
//            String invalidToken = "tokenInvalido";
//            String email = tokenService.validateToken(invalidToken);
//
//            assertNull(email);
//        }
//
//        @Test
//        @DisplayName("Deve lançar exceção ao gerar token JWT em caso de erro")
//        void generateTokenThrowsException() {
//            assertThrows(RuntimeException.class, () -> {
//                tokenService.secret = null; // Simula erro ao definir a chave como nula
//                Usuario usuario = new Usuario();
//                usuario.setEmail("usuario@teste.com");
//                tokenService.generateToken(usuario);
//            });
//        }
//    }
//
//}