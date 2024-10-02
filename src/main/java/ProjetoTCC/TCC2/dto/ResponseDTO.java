package ProjetoTCC.TCC2.dto;

/**
 * Resposta da autenticaçãodo usuário.
 *
 * @param nome  do usuário.
 * @param token JWT gerado para autenticação do usuário.
 */
public record ResponseDTO(String nome, String token) {
}
