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
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        usuarioDAO = new UsuarioDAOHibernateJPA();
        em = EMF.getEMF().createEntityManager();
    }
    @AfterEach
    public void tearDown() {
        if (em.isOpen()) {
            em.clear();
            em.close();
        }
    }

    @Test
    public void testAltaUsuario() {
        Usuario u = new Usuario();
        u.setNombre("Ianela");
        u.setEmail("iane@test.com");
        u.setPassword("1234");

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        usuarioDAO.persist(u);
        tx.commit();

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

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        usuarioDAO.persist(u);
        tx.commit();

        u.setNombre("Juan Modificado");
        tx.begin();
        usuarioDAO.update(u);
        tx.commit();

        Usuario actualizado = usuarioDAO.get(u.getId());
        assertEquals("Juan Modificado", actualizado.getNombre());
    }

    @Test
    public void testBajaUsuario() {
        Usuario u = new Usuario();
        u.setNombre("Laura");
        u.setEmail("laura@test.com");
        u.setPassword("pass");

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        usuarioDAO.persist(u);
        tx.commit();

        Long id = u.getId();

        tx.begin();
        usuarioDAO.delete(u);
        tx.commit();

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

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        usuarioDAO.persist(u);
        usuarioDAO.persist(u2);
        usuarioDAO.persist(u3);
        tx.commit();
        List<Usuario> usuarios = usuarioDAO.getAll("id");
        assertFalse(usuarios.isEmpty());
    }

    @Test
    public void testUsuarioPublicaciones(){
        DesaparicionDAOHibernateJPA desaparicionDAO = new DesaparicionDAOHibernateJPA();
        AvistamientoDAOHibernateJPA avistamientoDAO = new AvistamientoDAOHibernateJPA();

        Usuario u = new Usuario();
        u.setNombre("Carlos");
        u.setEmail("carlos@test.com");
        u.setPassword("pass");

        Desaparicion d = new Desaparicion();
        d.setComentario("Perdido perro");
        d.setCoordenada("-34.7,-58.6");
        d.setFecha(new Date());
        u.addDesaparicion(d);

        Avistamiento avistamiento = new Avistamiento();
        avistamiento.setComentario("Vi un perro");
        avistamiento.setCoordenada("-34.8,-58.7");
        avistamiento.setFecha(new Date());
        u.addAvistamiento(avistamiento);

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        desaparicionDAO.persist(d);
        avistamientoDAO.persist(avistamiento);
        usuarioDAO.persist(u);
        tx.commit();

        EntityTransaction tx2 = em.getTransaction();
        tx2.begin();
        Usuario recuperado = em.find(Usuario.class, u.getId());
        tx2.commit();

        assertFalse(recuperado.getDesapariciones().isEmpty());
        assertFalse(recuperado.getAvistamientos().isEmpty());
    }
}