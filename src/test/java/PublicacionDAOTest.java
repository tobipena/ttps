import static org.junit.jupiter.api.Assertions.*;

import org.example.ttps.models.Publicacion;
import org.example.ttps.persistence.EMF;
import org.example.ttps.persistence.PublicacionDAOHibernateJPA;
import org.junit.jupiter.api.*;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

public class PublicacionDAOTest {

    private PublicacionDAOHibernateJPA publicacionDAO;
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        publicacionDAO = new PublicacionDAOHibernateJPA();
        em = EMF.getEMF().createEntityManager();
    }
    @AfterEach
    public void tearDown() {
        if (em.isOpen()) {
            em.close();
        }
    }

    @Test
    public void testAltaPublicacion() {
        Publicacion p = new Publicacion();
        p.setComentario("Se perdió un perro");
        p.setCoordenada("-34.5,-58.4");
        p.setFecha(new Date());

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        publicacionDAO.persist(p);
        tx.commit();

        Publicacion recuperado = publicacionDAO.get(p.getId());
        assertNotNull(recuperado);
        assertEquals("Se perdió un perro", recuperado.getComentario());
    }

    @Test
    public void testModificacionPublicacion() {
        Publicacion p = new Publicacion();
        p.setComentario("Encontrado gato");
        p.setCoordenada("-34.6,-58.5");
        p.setFecha(new Date());

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        publicacionDAO.persist(p);
        tx.commit();

        p.setComentario("Encontrado gato modificado");
        tx.begin();
        publicacionDAO.update(p);
        tx.commit();

        Publicacion actualizado = publicacionDAO.get(p.getId());
        assertEquals("Encontrado gato modificado", actualizado.getComentario());
    }

    @Test
    public void testBajaPublicacion() {
        Publicacion p = new Publicacion();
        p.setComentario("Perdido cachorro");
        p.setCoordenada("-34.7,-58.6");
        p.setFecha(new Date());

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        publicacionDAO.persist(p);
        tx.commit();

        Long id = p.getId();

        tx.begin();
        publicacionDAO.delete(p);
        tx.commit();

        Publicacion eliminado = publicacionDAO.get(id);
        assertNull(eliminado);
    }

    @Test
    public void testRecuperacionPublicaciones() {
        List<Publicacion> publicaciones = publicacionDAO.getAll("id");
        assertNotNull(publicaciones);
    }
}

