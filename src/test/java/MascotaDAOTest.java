import static org.junit.jupiter.api.Assertions.*;

import org.example.ttps.models.Mascota;
import org.example.ttps.persistence.EMF;
import org.example.ttps.persistence.MascotaDAOHibernateJPA;
import org.junit.jupiter.api.*;
import jakarta.persistence.*;
import java.util.List;

public class MascotaDAOTest {

    private MascotaDAOHibernateJPA mascotaDAO;
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        mascotaDAO = new MascotaDAOHibernateJPA();
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
    public void testAltaMascota() {
        Mascota m = new Mascota();
        m.setNombre("Firulais");

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        mascotaDAO.persist(m);
        tx.commit();

        Mascota recuperado = mascotaDAO.get(m.getId());
        assertNotNull(recuperado);
        assertEquals("Firulais", recuperado.getNombre());
    }

    @Test
    public void testModificacionMascota() {
        Mascota m = new Mascota();
        m.setNombre("Bobby");

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        mascotaDAO.persist(m);
        tx.commit();

        m.setNombre("Bobby Modificado");
        tx.begin();
        mascotaDAO.update(m);
        tx.commit();

        Mascota actualizado = mascotaDAO.get(m.getId());
        assertEquals("Bobby Modificado", actualizado.getNombre());
    }

    @Test
    public void testBajaMascota() {
        Mascota m = new Mascota();
        m.setNombre("Luna");

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        mascotaDAO.persist(m);
        tx.commit();

        Long id = m.getId();

        tx.begin();
        mascotaDAO.delete(m);
        tx.commit();

        Mascota eliminado = mascotaDAO.get(id);
        assertNull(eliminado);
    }

    @Test
    public void testRecuperacionMascotas() {
        Mascota m = new Mascota();
        m.setNombre("Luna");
        Mascota m2 = new Mascota();
        m2.setNombre("tobi");
        Mascota m3 = new Mascota();
        m3.setNombre("juampe");
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        mascotaDAO.persist(m);
        mascotaDAO.persist(m2);
        mascotaDAO.persist(m3);
        tx.commit();
        List<Mascota> mascotas = mascotaDAO.getAll("id");
        assertFalse(mascotas.isEmpty());
    }
}

