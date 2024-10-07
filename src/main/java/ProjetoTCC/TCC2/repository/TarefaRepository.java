package ProjetoTCC.TCC2.repository;

import ProjetoTCC.TCC2.entity.Tarefa;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TarefaRepository extends MongoRepository<Tarefa, ObjectId> {
    List<Tarefa> findByEmailUsuario(String emailUsuario);
    void deleteByEmailUsuario(String emailUsuario);
    Optional<Tarefa> findById(ObjectId id);
}
