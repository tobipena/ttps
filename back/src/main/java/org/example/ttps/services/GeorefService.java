package org.example.ttps.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeorefService {
    private static final String GEOREF_API_BASE = "https://apis.datos.gob.ar/georef/api";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GeorefService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public UbicacionInfo obtenerUbicacion(Double latitud, Double longitud) {
        try {
            // Llamar a la API de georef-ar - usar String.valueOf para evitar problemas de locale
            String url = GEOREF_API_BASE + "/ubicacion?lat=" + latitud + "&lon=" + longitud;
            
            System.out.println("Llamando a georef-ar: " + url);
            
            String response = restTemplate.getForObject(url, String.class);
            System.out.println("Respuesta georef-ar: " + response);
            
            JsonNode root = objectMapper.readTree(response);
            
            UbicacionInfo ubicacion = new UbicacionInfo();
            
            // Extraer información de la respuesta
            if (root.has("ubicacion")) {
                JsonNode ubicacionNode = root.get("ubicacion");
                
                // ciudad = municipio.nombre (ciudad/localidad específica)
                if (ubicacionNode.has("municipio") && ubicacionNode.get("municipio").has("nombre")) {
                    ubicacion.setCiudad(ubicacionNode.get("municipio").get("nombre").asText());
                } else if (ubicacionNode.has("departamento") && ubicacionNode.get("departamento").has("nombre")) {
                    // Si no hay municipio, usar departamento como fallback
                    ubicacion.setCiudad(ubicacionNode.get("departamento").get("nombre").asText());
                }
                
                // provincia = provincia.nombre
                if (ubicacionNode.has("provincia") && ubicacionNode.get("provincia").has("nombre")) {
                    ubicacion.setProvincia(ubicacionNode.get("provincia").get("nombre").asText());
                }
            }
            
            System.out.println("Ubicación obtenida - Ciudad: " + ubicacion.getCiudad() + ", Provincia: " + ubicacion.getProvincia());
            return ubicacion;
        } catch (Exception e) {
            // Si falla la API, lanzar excepción con más detalle
            System.err.println("Error al obtener ubicación: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al obtener la ubicación desde el servicio de geolocalización. Por favor, intente nuevamente más tarde.", e);
        }
    }

    public static class UbicacionInfo {
        private String ciudad;
        private String provincia;

        public String getCiudad() {
            return ciudad;
        }

        public void setCiudad(String ciudad) {
            this.ciudad = ciudad;
        }

        public String getProvincia() {
            return provincia;
        }

        public void setProvincia(String provincia) {
            this.provincia = provincia;
        }
    }
}
