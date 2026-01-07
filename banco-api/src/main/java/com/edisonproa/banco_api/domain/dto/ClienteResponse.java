package com.edisonproa.banco_api.domain.dto;

import com.edisonproa.banco_api.domain.entity.Genero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteResponse {
    private Long id; // PK técnica (Persona.id)
    private String clienteId; // business key
    private Boolean estado;

    private String nombre;
    private Genero genero;
    private Integer edad;
    private String identificacion;
    private String direccion;
    private String telefono;
}
