package ProjetoTCC.TCC2.controller;

import ProjetoTCC.TCC2.dto.LoginRequestDTO;
import ProjetoTCC.TCC2.dto.RegisterRequestDTO;
import ProjetoTCC.TCC2.dto.ResponseDTO;
import ProjetoTCC.TCC2.entity.Usuario;
import ProjetoTCC.TCC2.repository.UsuarioRepository;
import ProjetoTCC.TCC2.security.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Controlador responsável por gerenciar a autenticação dos usuários, incluindo login e registro.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthController(UsuarioRepository repository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    /**
     * Login de um usuário existente.
     * Verifica se o usuário existe e se a senha está correta.
     * Se a autenticação for bem-sucedida, gera um token JWT.
     *
     * @param body(contendo o email e a senha do usuário)
     * @return Nome do usuário e o token JWT, ou um erro 400 se a autenticação falhar.
     */
    @RequestMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body) {
        Usuario usuario = this.repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(body.senha(), usuario.getSenha())) {
            String token = this.tokenService.generateToken(usuario);
            return ResponseEntity.ok(new ResponseDTO(usuario.getNome(), token));
        }

        return ResponseEntity.badRequest().build();
    }

    /**
     * Registro de um usuário.
     * Cria um novo usuário, antes verificando se o email não está registrado.
     * Gera um token JWT.
     *
     * @param body(contendo o nome,email e senha).
     * @return Nome do usuário e o token JWT, ou um erro 400 se o email já estiver registrado.
     */
    @RequestMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body) {
        Optional<Usuario> usuario = this.repository.findByEmail(body.email());

        if (usuario.isEmpty()) {
            Usuario newUsuario = new Usuario();
            newUsuario.setSenha(passwordEncoder.encode(body.senha()));
            newUsuario.setEmail(body.email());
            newUsuario.setNome(body.nome());
            this.repository.save(newUsuario);

            String token = this.tokenService.generateToken(newUsuario);
            return ResponseEntity.ok(new ResponseDTO(newUsuario.getNome(), token));
        }

        return ResponseEntity.badRequest().build();
    }
}
