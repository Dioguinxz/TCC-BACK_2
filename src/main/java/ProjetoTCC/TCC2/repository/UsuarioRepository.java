package ProjetoTCC.TCC2.repository;

import ProjetoTCC.TCC2.entity.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UsuarioRepository extends MongoRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

}
