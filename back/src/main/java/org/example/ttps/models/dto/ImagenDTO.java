package org.example.ttps.models.dto;

import lombok.Data;

@Data
public class ImagenDTO {
    private String datos; // Base64 string
    private String tipo;  // "image/jpeg", "image/png", etc.
}

