package ProjetoTCC.TCC2.service;

import ProjetoTCC.TCC2.entity.Tarefa;
import ProjetoTCC.TCC2.entity.Usuario;
import ProjetoTCC.TCC2.repository.TarefaRepository;
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
    private TarefaRepository tarefaRepository;
    private PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, TarefaRepository tarefaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tarefaRepository = tarefaRepository;
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
     * Edita um usuário existente, atualizando seu nome e email.
     * Também atualiza o email associado a todas as tarefas do usuário.
     *
     * @param email   do usuário a ser editado.
     * @param usuario O objeto Usuario contendo as novas informações a serem atualizadas.
     * @return O usuário atualizado.
     * @throws IllegalArgumentException Se o usuário não for encontrado ou se o novo email já estiver registrado.
     */
    public Usuario editarUsuario(String email, Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(email);

        if (usuarioExistente.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()
                && !usuario.getEmail().equals(usuarioExistente.get().getEmail())) {
            throw new IllegalArgumentException("Email já registrado");
        }

        Usuario usuarioParaSalvar = usuarioExistente.get();

        if (usuario.getSenha() != null && !usuario.getSenha().equals(usuarioParaSalvar.getSenha())) {
            usuarioParaSalvar.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        usuarioParaSalvar.setNome(usuario.getNome());

        String emailAntigo = usuarioParaSalvar.getEmail();
        usuarioParaSalvar.setEmail(usuario.getEmail());

        List<Tarefa> tarefas = tarefaRepository.findByEmailUsuario(emailAntigo);
        for (Tarefa tarefa : tarefas) {
            tarefa.setEmailUsuario(usuario.getEmail());
            tarefaRepository.save(tarefa);
        }

        usuarioRepository.save(usuarioParaSalvar);
        return usuarioParaSalvar;
    }

    /**
     * Exclui o usuário pelo seu id e suas tarefas.
     *
     * @param id do usuário.
     * @return Lista dos usuários atualizada.
     */
    public List<Usuario> excluirUsuarioPorId(ObjectId id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        usuarioRepository.deleteById(id);

        tarefaRepository.deleteByEmailUsuario(usuario.getEmail());

        return listarUsuario();
    }

    /**
     * Exclui o usuário pelo seu email e suas tarefas.
     *
     * @param email do usuário.
     * @return Lista de usuário atualizada.
     */
    public List<Usuario> excluirUsuarioPorEmail(String email) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if (usuario.isPresent()) {
            usuarioRepository.deleteByEmail(email);

            tarefaRepository.deleteByEmailUsuario(email);
        } else {
            throw new RuntimeException("Usuário não encontrado com o email: " + email);
        }

        return listarUsuario();
    }

    /**
     * Busca um usuário pelo seu email.
     *
     * @param email do usuário.
     * @return O usuário encontrado.
     * @throws IllegalArgumentException Se o usuário não for encontrado.
     */
    public Usuario buscarUsuarioPorEmail(String email) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if (usuario.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        return usuario.get();
    }
}
