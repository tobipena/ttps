import static org.junit.jupiter.api.Assertions.*;

import org.example.ttps.models.Desaparicion;
import org.example.ttps.persistence.EMF;
import org.example.ttps.persistence.DesaparicionDAOHibernateJPA;
import org.junit.jupiter.api.*;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

public class DesaparicionDAOTest {

    private DesaparicionDAOHibernateJPA desaparicionDAO;
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        desaparicionDAO = new DesaparicionDAOHibernateJPA();
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
    public void testAltaDesaparicion() {
        Desaparicion p = new Desaparicion();
        p.setComentario("Se perdió un perro");
        p.setCoordenada("-34.5,-58.4");
        p.setFecha(new Date());

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        desaparicionDAO.persist(p);
        tx.commit();

        Desaparicion recuperado = desaparicionDAO.get(p.getId());
        assertNotNull(recuperado);
        assertEquals("Se perdió un perro", recuperado.getComentario());
    }

    @Test
    public void testModificacionDesaparicion() {
        Desaparicion p = new Desaparicion();
        p.setComentario("Encontrado gato");
        p.setCoordenada("-34.6,-58.5");
        p.setFecha(new Date());

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        desaparicionDAO.persist(p);
        tx.commit();

        p.setComentario("Encontrado gato modificado");
        tx.begin();
        desaparicionDAO.update(p);
        tx.commit();

        Desaparicion actualizado = desaparicionDAO.get(p.getId());
        assertEquals("Encontrado gato modificado", actualizado.getComentario());
    }

    @Test
    public void testBajaDesaparicion() {
        Desaparicion p = new Desaparicion();
        p.setComentario("Perdido cachorro");
        p.setCoordenada("-34.7,-58.6");
        p.setFecha(new Date());

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        desaparicionDAO.persist(p);
        tx.commit();

        Long id = p.getId();

        tx.begin();
        desaparicionDAO.delete(p);
        tx.commit();

        Desaparicion eliminado = desaparicionDAO.get(id);
        assertNull(eliminado);
    }

    @Test
    public void testRecuperacionDesapariciones() {
        Desaparicion p = new Desaparicion();
        p.setComentario("Perdido cachorro");
        p.setCoordenada("-34.7,-58.6");
        p.setFecha(new Date());
        Desaparicion p2 = new Desaparicion();
        p2.setComentario("Perdido");
        p2.setCoordenada("-34.7,-58.6");
        p2.setFecha(new Date());
        Desaparicion p3 = new Desaparicion();
        p3.setComentario("Perdido amigo");
        p3.setCoordenada("-34.7,-58.6");
        p3.setFecha(new Date());
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        desaparicionDAO.persist(p);
        desaparicionDAO.persist(p2);
        desaparicionDAO.persist(p3);
        tx.commit();
        List<Desaparicion> desapariciones = desaparicionDAO.getAll("id");
        assertFalse(desapariciones.isEmpty());
    }
}

