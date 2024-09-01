package ProjetoTCC.TCC2.service;

import ProjetoTCC.TCC2.validator.EmailValidator;
import ProjetoTCC.TCC2.entity.Usuario;
import ProjetoTCC.TCC2.repository.UsuarioRepository;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario criarUsuario(Usuario usuario) {
        if (!EmailValidator.isValid(usuario.getEmail())) {
            throw new IllegalArgumentException("Email inválido");
        }
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já registrado");
        }
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuario() {
        Sort sort = Sort.by("nome").ascending();
        return usuarioRepository.findAll();
    }

    public List<Usuario> editarUsuario(Usuario usuario) {
        if (!EmailValidator.isValid(usuario.getEmail())) {
            throw new IllegalArgumentException("Email inválido");
        }
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já registrado");
        }
        usuarioRepository.save(usuario);
        return listarUsuario();
    }

    public List<Usuario> excluirUsuario(ObjectId id) {
        usuarioRepository.deleteById(id);
        return listarUsuario();
    }
}
