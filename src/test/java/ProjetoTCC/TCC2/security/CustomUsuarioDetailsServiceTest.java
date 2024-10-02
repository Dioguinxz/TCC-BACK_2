package ProjetoTCC.TCC2.security;

import ProjetoTCC.TCC2.entity.Usuario;
import ProjetoTCC.TCC2.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CustomUsuarioDetailsServiceTest {
    @InjectMocks
    private CustomUsuarioDetailsService customUsuarioDetailsService;

    @Mock
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        usuario = new Usuario();
        usuario.setEmail("usuario@teste.com");
        usuario.setSenha("senhaSegura");
    }

    @Test
    @DisplayName("Deve carregar o usuário com sucesso pelo email")
    public void testLoadUserByUsernameSuccess() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        UserDetails userDetails = customUsuarioDetailsService.loadUserByUsername(usuario.getEmail());

        assertNotNull(userDetails);
        assertEquals(usuario.getEmail(), userDetails.getUsername());
        assertEquals(usuario.getSenha(), userDetails.getPassword());
    }

    @Test
    @DisplayName("Deve lançar UsernameNotFoundException quando o usuário não for encontrado pelo email")
    public void testLoadUserByUsernameUserNotFound() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            customUsuarioDetailsService.loadUserByUsername(usuario.getEmail());
        });
    }
}