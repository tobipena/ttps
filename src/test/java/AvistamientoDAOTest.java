import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.ttps.models.Avistamiento;
import org.example.ttps.models.Desaparicion;
import org.example.ttps.persistence.AvistamientoDAOHibernateJPA;
import org.example.ttps.persistence.EMF;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AvistamientoDAOTest {

    private AvistamientoDAOHibernateJPA avistamientoDAO;
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        avistamientoDAO = new AvistamientoDAOHibernateJPA();
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
    public void testAltaAvistamiento() {
        Avistamiento a = new Avistamiento();
        a.setComentario("aviste un perro");
        a.setCoordenada("-34.5,-58.4");
        a.setFecha(new Date());

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        avistamientoDAO.persist(a);
        tx.commit();

        Avistamiento recuperado = avistamientoDAO.get(a.getId());
        assertNotNull(recuperado);
        assertEquals("aviste un perro", recuperado.getComentario());
    }

    @Test
    public void testModificacionAvistamiento() {
        Avistamiento a = new Avistamiento();
        a.setComentario("vi un gato");
        a.setCoordenada("-34.6,-58.5");
        a.setFecha(new Date());

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        avistamientoDAO.persist(a);
        tx.commit();

        a.setComentario("vi un gato modificado");
        tx.begin();
        avistamientoDAO.update(a);
        tx.commit();

        Avistamiento actualizado = avistamientoDAO.get(a.getId());
        assertEquals("vi un gato modificado", actualizado.getComentario());
    }

    @Test
    public void testBajaAvistamiento() {
        Avistamiento a = new Avistamiento();
        a.setComentario("Perdido cachorro");
        a.setCoordenada("-34.7,-58.6");
        a.setFecha(new Date());

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        avistamientoDAO.persist(a);
        tx.commit();

        Long id = a.getId();

        tx.begin();
        avistamientoDAO.delete(a);
        tx.commit();

        Avistamiento eliminado = avistamientoDAO.get(id);
        assertNull(eliminado);
    }

    @Test
    public void testRecuperacionAvistamientos() {
        Avistamiento a = new Avistamiento();
        a.setComentario("vi cachorro");
        a.setCoordenada("-34.7,-58.6");
        a.setFecha(new Date());
        Avistamiento a2 = new Avistamiento();
        a2.setComentario("aviste gato");
        a2.setCoordenada("-34.7,-58.6");
        a2.setFecha(new Date());
        Avistamiento a3 = new Avistamiento();
        a3.setComentario("aviste un perro");
        a3.setCoordenada("-34.7,-58.6");
        a3.setFecha(new Date());
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        avistamientoDAO.persist(a);
        avistamientoDAO.persist(a2);
        avistamientoDAO.persist(a3);
        tx.commit();
        List<Avistamiento> avistamientos = avistamientoDAO.getAll("id");
        assertFalse(avistamientos.isEmpty());
    }

}
