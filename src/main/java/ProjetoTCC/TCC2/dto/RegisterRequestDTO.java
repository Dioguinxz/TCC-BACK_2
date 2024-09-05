package ProjetoTCC.TCC2.dto;

import org.bson.types.ObjectId;


public record RegisterRequestDTO(ObjectId id, String nome, String email, String senha) {
}
