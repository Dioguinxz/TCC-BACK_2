package ProjetoTCC.TCC2.dto;

import org.bson.types.ObjectId;

/**
 * Requisiçõa de registro de usuário
 *
 * @param id do usuário.
 * @param nome do usuário.
 * @param email do usuário.
 * @param senha do usuário.
 */
public record RegisterRequestDTO(ObjectId id, String nome, String email, String senha) {
}
