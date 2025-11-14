package org.example.ttps;

import static org.junit.jupiter.api.Assertions.*;

import org.example.ttps.models.Desaparicion;
import org.example.ttps.models.Mascota;
import org.example.ttps.persistence.DesaparicionDAOHibernateJPA;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

public class DesaparicionDAOTest {

    private DesaparicionDAOHibernateJPA desaparicionDAO;

    @BeforeEach
    public void setUp() {
        desaparicionDAO = new DesaparicionDAOHibernateJPA();
    }
    @AfterEach
    public void tearDown() {

    }

    @Test
    public void testAltaDesaparicion() {
        Desaparicion p = new Desaparicion();
        p.setComentario("Se perdió un perro");
        p.setCoordenada("-34.5,-58.4");
        p.setFecha(new Date());
        Mascota m = new Mascota();
        m.setNombre("Firulais");
        p.setMascota(m);


        desaparicionDAO.persist(p);

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
        Mascota m = new Mascota();
        m.setNombre("Sol");
        p.setMascota(m);

        desaparicionDAO.persist(p);

        p.setComentario("Encontrado gato modificado");

        desaparicionDAO.update(p);

        Desaparicion actualizado = desaparicionDAO.get(p.getId());
        assertEquals("Encontrado gato modificado", actualizado.getComentario());
    }

    @Test
    public void testBajaDesaparicion() {
        Desaparicion p = new Desaparicion();
        p.setComentario("Perdido cachorro");
        p.setCoordenada("-34.7,-58.6");
        p.setFecha(new Date());
        Mascota m = new Mascota();
        m.setNombre("iane");
        p.setMascota(m);

        desaparicionDAO.persist(p);

        Long id = p.getId();

        desaparicionDAO.delete(p);

        Desaparicion eliminado = desaparicionDAO.get(id);
        assertNull(eliminado);
    }

    @Test
    public void testRecuperacionDesapariciones() {
        Desaparicion p = new Desaparicion();
        p.setComentario("Perdido cachorro");
        p.setCoordenada("-34.7,-58.6");
        p.setFecha(new Date());
        Mascota m = new Mascota();
        m.setNombre("luna");
        p.setMascota(m);
        Desaparicion p2 = new Desaparicion();
        p2.setComentario("Perdido");
        p2.setCoordenada("-34.7,-58.6");
        p2.setFecha(new Date());
        Mascota m2 = new Mascota();
        m2.setNombre("nina");
        p2.setMascota(m2);
        Desaparicion p3 = new Desaparicion();
        p3.setComentario("Perdido amigo");
        p3.setCoordenada("-34.7,-58.6");
        p3.setFecha(new Date());
        Mascota m3 = new Mascota();
        m3.setNombre("betun");
        p3.setMascota(m3);

        desaparicionDAO.persist(p);
        desaparicionDAO.persist(p2);
        desaparicionDAO.persist(p3);

        List<Desaparicion> desapariciones = desaparicionDAO.getAll("id");
        assertFalse(desapariciones.isEmpty());
    }
}

