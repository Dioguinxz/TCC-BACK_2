package ProjetoTCC.TCC2.controller;

import ProjetoTCC.TCC2.entity.Usuario;
import ProjetoTCC.TCC2.service.UsuarioService;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

        private UsuarioService usuarioService;

        public UsuarioController(UsuarioService usuarioService) {
            this.usuarioService = usuarioService;
        }

        @PostMapping
        Usuario criarUsuario(@RequestBody Usuario usuario) {
            return usuarioService.criarUsuario(usuario);
        }

        @GetMapping
        List<Usuario> listarUsuario() {
            return usuarioService.listarUsuario();
        }

        @PutMapping("{id}")
        List<Usuario> editarUsuario(@RequestBody Usuario usuario) {
            return usuarioService.editarUsuario(usuario);
        }

        @DeleteMapping("{id}")
        List<Usuario> excluirUsuario(@PathVariable("id") ObjectId id) {
            return usuarioService.excluirUsuario(id);
        }
}
