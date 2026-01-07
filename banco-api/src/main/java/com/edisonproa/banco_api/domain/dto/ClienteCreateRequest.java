package com.edisonproa.banco_api.domain.dto;

import com.edisonproa.banco_api.domain.entity.Genero;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteCreateRequest {

    @NotBlank @Size(min = 5, max = 30)
    private String clienteId;

    @NotBlank @Size(min = 8, max = 100)
    private String contrasena;

    @NotNull
    private Boolean estado;

    @NotBlank @Size(max = 120)
    private String nombre;

    @NotNull
    private Genero genero;

    @NotNull @Min(0) @Max(120)
    private Integer edad;

    @NotBlank @Size(min = 5, max = 30)
    private String identificacion;

    @NotBlank @Size(max = 200)
    private String direccion;

    @NotBlank @Size(min = 7, max = 20)
    private String telefono;
}
