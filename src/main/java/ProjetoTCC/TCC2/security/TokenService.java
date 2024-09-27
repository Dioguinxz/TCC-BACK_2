package ProjetoTCC.TCC2.security;

import ProjetoTCC.TCC2.entity.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Serviço responsável pela geração e validação de tokens JWT.
 */
@Service
public class TokenService {
    /**
     * Chave secreta usada para assinar o token JWT.
     */
    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Gera um token JWT para um determinado usuário.
     *
     * @param usuario para qual será gerado o token.
     * @return O token JWT gerado.
     * @throws RuntimeException se houver um erro durante a geração do token.
     */
    public String generateToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create()
                    .withIssuer("simple")
                    .withSubject(usuario.getEmail())
                    .withExpiresAt(this.generateExpirationDate()).sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("error while authentincating");
        }
    }

    /**
     * Valida um token JWT e retorna o email do usuário, caso seja válido.
     *
     * @param token a ser validado.
     * @return o email do usuário associado ao token ou null se o token for inválido.
     */
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("simple")
                    .build().verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    /**
     * Gera a data de expiração do token, definida como 2 horas.
     *
     * @return um objeto Instant representando a data de expiração do token
     */
    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
