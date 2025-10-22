import static org.junit.jupiter.api.Assertions.*;

import org.example.ttps.models.Usuario;
import org.example.ttps.persistence.EMF;
import org.example.ttps.persistence.UsuarioDAOHibernateJPA;
import org.junit.jupiter.api.*;
import jakarta.persistence.*;
import java.util.List;

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
        List<Usuario> usuarios = usuarioDAO.getAll("id");
        assertNotNull(usuarios);
    }
}