package ProjetoTCC.TCC2.controller;

import ProjetoTCC.TCC2.entity.Usuario;
import ProjetoTCC.TCC2.service.UsuarioService;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável por disponibilizar os endpoints HTTP para o CRUD do Usuario.
 */
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Criar um novo usuário.
     *
     * @param usuario
     * @return O usuário criado.
     */
    @PostMapping
    Usuario criarUsuario(@RequestBody Usuario usuario) {
        return usuarioService.criarUsuario(usuario);
    }

    /**
     * Lista dos usuários.
     *
     * @return Lista dos usuários.
     */
    @GetMapping
    List<Usuario> listarUsuario() {
        return usuarioService.listarUsuario();
    }

    /**
     * Edita um usuário existente, atualizando seu nome e email.
     * Também atualiza o email associado a todas as tarefas do usuário.
     *
     * @param email do usuário a ser editado.
     * @param usuario O objeto Usuario contendo as novas informações a serem atualizadas.
     * @return O usuário atualizado.
     * @throws IllegalArgumentException Se o usuário não for encontrado ou se o novo email já estiver registrado.
     */
    @PutMapping("{email}")
    Usuario editarUsuario(@RequestBody Usuario usuario, @PathVariable String email) {
        return usuarioService.editarUsuario(email, usuario);
    }

    /**
     * Exclui o usuário pelo id.
     *
     * @param id do usuário.
     * @return Lista dos usuários atualizada.
     */
    @DeleteMapping("{id}")
    List<Usuario> excluirUsuario(@PathVariable("id") ObjectId id) {
        return usuarioService.excluirUsuarioPorId(id);
    }

    /**
     * Exclui o usuário pelo seu email.
     *
     * @param email do usuário.
     * @return Lista de usuário atualizada.
     */
    @DeleteMapping("/excluirPorEmail/{email}")
    public List<Usuario> excluirUsuarioPorEmail(@PathVariable String email) {
        return usuarioService.excluirUsuarioPorEmail(email);
    }

    /**
     * Busca um usuário pelo email.
     *
     * @param email do usuário.
     * @return o usuário encontrado.
     */
    @GetMapping("{email}")
    public ResponseEntity<Usuario> buscarUsuarioPorEmail(@PathVariable String email) {
        Usuario usuario = usuarioService.buscarUsuarioPorEmail(email);
        return ResponseEntity.ok(usuario);
    }
}
