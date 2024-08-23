package ProjetoTCC.TCC2.service;

import ProjetoTCC.TCC2.validator.EmailValidator;
import ProjetoTCC.TCC2.entity.Usuario;
import ProjetoTCC.TCC2.repository.UsuarioRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

//    public List<Usuario> criarUsuario(Usuario usuario) {
//        usuarioRepository.save(usuario);
//        return listarUsuario();
//    }

    public Usuario criarUsuario(Usuario usuario) {
        if (!EmailValidator.isValid(usuario.getEmail())) {
            throw new IllegalArgumentException("Email inválido");
        }
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuario() {
        Sort sort = Sort.by("nome").ascending();
        return usuarioRepository.findAll();
    }

    public List<Usuario> editarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
        return listarUsuario();
    }

    public List<Usuario> excluirUsuario(Long id) {
        usuarioRepository.deleteById(id);
        return listarUsuario();
    }
}
