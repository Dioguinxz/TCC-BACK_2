package ProjetoTCC.TCC2.dto;

/**
 * Requisição de login.
 *
 * @param email do usuário.
 * @param senha do usuário.
 */
public record LoginRequestDTO(String email, String senha) {
}
