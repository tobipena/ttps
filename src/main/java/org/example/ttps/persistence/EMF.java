package org.example.ttps.persistence;

import jakarta.persistence.*;

public class EMF {
    private static EntityManagerFactory emf = null;
    static {
        try {
            emf = Persistence.createEntityManagerFactory("unlp");
        } catch (PersistenceException e) {
            System.err.println("Error al crear EntityManagerFactory: "+e.getMessage());
            e.printStackTrace();
        }
    }
    public static EntityManagerFactory getEMF(){
        return emf;
    }
   private EMF() {}
}
