package ProjetoTCC.TCC2.repository;

import ProjetoTCC.TCC2.entity.Tarefa;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface  TarefaRepository  extends MongoRepository<Tarefa, Long> {
}
