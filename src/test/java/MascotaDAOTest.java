import static org.junit.jupiter.api.Assertions.*;

import org.example.ttps.models.Mascota;
import org.example.ttps.persistence.EMF;
import org.example.ttps.persistence.MascotaDAOHibernateJPA;
import org.junit.jupiter.api.*;
import jakarta.persistence.*;
import java.util.List;

public class MascotaDAOTest {

    private MascotaDAOHibernateJPA mascotaDAO;

    @BeforeEach
    public void setUp() {
        mascotaDAO = new MascotaDAOHibernateJPA();
    }
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testAltaMascota() {
        Mascota m = new Mascota();
        m.setNombre("Firulais");

        mascotaDAO.persist(m);

        Mascota recuperado = mascotaDAO.get(m.getId());
        assertNotNull(recuperado);
        assertEquals("Firulais", recuperado.getNombre());
    }

    @Test
    public void testModificacionMascota() {
        Mascota m = new Mascota();
        m.setNombre("Bobby");

        mascotaDAO.persist(m);

        m.setNombre("Bobby Modificado");
        mascotaDAO.update(m);

        Mascota actualizado = mascotaDAO.get(m.getId());
        assertEquals("Bobby Modificado", actualizado.getNombre());
    }

    @Test
    public void testBajaMascota() {
        Mascota m = new Mascota();
        m.setNombre("Luna");

        mascotaDAO.persist(m);

        Long id = m.getId();

        mascotaDAO.delete(m);

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

        mascotaDAO.persist(m);
        mascotaDAO.persist(m2);
        mascotaDAO.persist(m3);

        List<Mascota> mascotas = mascotaDAO.getAll("id");
        assertFalse(mascotas.isEmpty());
    }
}

