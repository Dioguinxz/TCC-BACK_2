package ProjetoTCC.TCC2.service;

import ProjetoTCC.TCC2.validator.EmailValidator;
import ProjetoTCC.TCC2.entity.Usuario;
import ProjetoTCC.TCC2.repository.UsuarioRepository;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {
    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
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

    public Usuario editarUsuario(Usuario usuario) {
        if (!EmailValidator.isValid(usuario.getEmail())) {
            throw new IllegalArgumentException("Email inválido");
        }

        Optional<Usuario> usuarioExistente = usuarioRepository.findById(usuario.getId());

        if (usuarioExistente.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }


        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent() && !usuario.getEmail().equals(usuarioExistente.get().getEmail())) {
            throw new IllegalArgumentException("Email já registrado");
        }

        Usuario usuarioParaSalvar = usuarioExistente.get();

        if (usuario.getSenha() != null && !usuario.getSenha().equals(usuarioParaSalvar.getSenha())) {
            usuarioParaSalvar.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        usuarioParaSalvar.setEmail(usuario.getEmail());
        usuarioParaSalvar.setNome(usuario.getNome());

        usuarioRepository.save(usuarioParaSalvar);
        return usuarioParaSalvar;
    }

    public List<Usuario> excluirUsuario(ObjectId id) {
        usuarioRepository.deleteById(id);
        return listarUsuario();
    }
}
