package ProjetoTCC.TCC2.service;

import ProjetoTCC.TCC2.entity.Usuario;
import ProjetoTCC.TCC2.repository.UsuarioRepository;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelas operações de CRUD do Usuario.
 */
@Service
public class UsuarioService {
    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cria um novo usuário, antes verificando se o email já está registrado.
     * Se a senha for alterada, ela será codificada antes de salvar.
     *
     * @param usuario
     * @return O usuário criado.
     * @throws IllegalArgumentException Se o email já estiver registrado.
     */
    public Usuario criarUsuario(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já registrado");
        }
        return usuarioRepository.save(usuario);
    }

    /**
     * Lista os usuários, ordenados por nome em ordem crescente.
     *
     * @return Lista dos usuários.
     */
    public List<Usuario> listarUsuario() {
        Sort sort = Sort.by("nome").ascending();
        return usuarioRepository.findAll();
    }

    /**
     * Edita um usuário existente, antes verifica se o usuário existe e se o email já está registrado.
     *
     * @param usuario
     * @return O usuário editado.
     * @throws IllegalArgumentException Se o usuário não for encontrado ou o email já estiver registrado.
     */
    public Usuario editarUsuario(Usuario usuario) {
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

    /**
     * Exclui o usuário pelo seu id.
     *
     * @param id do usuário.
     * @return Lista dos usuários atualizada.
     */
    public List<Usuario> excluirUsuario(ObjectId id) {
        usuarioRepository.deleteById(id);
        return listarUsuario();
    }
}
