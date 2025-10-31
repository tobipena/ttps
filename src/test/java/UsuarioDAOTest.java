import static org.junit.jupiter.api.Assertions.*;

import org.example.ttps.models.Avistamiento;
import org.example.ttps.models.Desaparicion;
import org.example.ttps.models.Usuario;
import org.example.ttps.persistence.AvistamientoDAOHibernateJPA;
import org.example.ttps.persistence.DesaparicionDAOHibernateJPA;
import org.example.ttps.persistence.EMF;
import org.example.ttps.persistence.UsuarioDAOHibernateJPA;
import org.junit.jupiter.api.*;
import jakarta.persistence.*;
import java.util.List;
import java.util.Date;

public class UsuarioDAOTest {

    private UsuarioDAOHibernateJPA usuarioDAO;
    private DesaparicionDAOHibernateJPA desaparicionDAO;
    private AvistamientoDAOHibernateJPA avistamientoDAO;

    @BeforeEach
    public void setUp() {
        usuarioDAO = new UsuarioDAOHibernateJPA();
        desaparicionDAO = new DesaparicionDAOHibernateJPA();
        avistamientoDAO = new AvistamientoDAOHibernateJPA();
    }
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testAltaUsuario() {
        Usuario u = new Usuario();
        u.setNombre("Ianela");
        u.setEmail("iane@test.com");
        u.setPassword("1234");

        usuarioDAO.persist(u);

        Usuario recuperado = usuarioDAO.get(u.getId());
        assertNotNull(recuperado);
        assertEquals("Ianela", recuperado.getNombre());
    }

    @Test
    public void testModificacionUsuario() {
        Usuario u = new Usuario();
        u.setNombre("Juan");
        u.setEmail("juan@test.com");
        u.setPassword("abc");

        usuarioDAO.persist(u);

        u.setNombre("Juan Modificado");
        usuarioDAO.update(u);

        Usuario actualizado = usuarioDAO.get(u.getId());
        assertEquals("Juan Modificado", actualizado.getNombre());
    }

    @Test
    public void testBajaUsuario() {
        Usuario u = new Usuario();
        u.setNombre("Laura");
        u.setEmail("laura@test.com");
        u.setPassword("pass");


        usuarioDAO.persist(u);

        Long id = u.getId();

        usuarioDAO.delete(u);


        Usuario eliminado = usuarioDAO.get(id);
        assertNull(eliminado);
    }

    @Test
    public void testRecuperacionUsuarios() {
        Usuario u = new Usuario();
        u.setNombre("Laura");
        u.setEmail("laura@test.com");
        u.setPassword("pass");
        Usuario u2 = new Usuario();
        u2.setNombre("Iane");
        u2.setEmail("iane@test.com");
        u2.setPassword("pass");
        Usuario u3 = new Usuario();
        u3.setNombre("tobi");
        u3.setEmail("tobi@test.com");
        u3.setPassword("pass");


        usuarioDAO.persist(u);
        usuarioDAO.persist(u2);
        usuarioDAO.persist(u3);

        List<Usuario> usuarios = usuarioDAO.getAll("id");
        assertFalse(usuarios.isEmpty());
    }

    @Test
    public void testUsuarioPublicaciones(){


        Usuario u = new Usuario();
        u.setNombre("Carlos");
        u.setEmail("carlos@test.com");
        u.setPassword("pass");

        usuarioDAO.persist(u);

        Desaparicion d = new Desaparicion();
        d.setComentario("Perdido perro");
        d.setCoordenada("-34.7,-58.6");
        d.setFecha(new Date());
        d.setUsuario(u);
        u.addDesaparicion(d);

        Avistamiento avistamiento = new Avistamiento();
        avistamiento.setComentario("Vi un perro");
        avistamiento.setCoordenada("-34.8,-58.7");
        avistamiento.setFecha(new Date());
        avistamiento.setUsuario(u);
        u.addAvistamiento(avistamiento);

        desaparicionDAO.persist(d);
        avistamientoDAO.persist(avistamiento);

        List<Desaparicion> desaparicionesRecuperadas = usuarioDAO.getDesapariciones(u);
        List<Avistamiento> avistamientosRecuperados = usuarioDAO.getAvistamientos(u);

        assertFalse(desaparicionesRecuperadas.isEmpty());
        assertFalse(avistamientosRecuperados.isEmpty());
    }
}