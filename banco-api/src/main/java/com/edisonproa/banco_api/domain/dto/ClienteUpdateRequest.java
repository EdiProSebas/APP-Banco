package com.edisonproa.banco_api.domain.dto;

import com.edisonproa.banco_api.domain.entity.Genero;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteUpdateRequest {

    @NotNull
    private Boolean estado;

    @NotBlank @Size(max = 120)
    private String nombre;

    @NotNull
    private Genero genero;

    @NotNull @Min(0) @Max(120)
    private Integer edad;

    @NotBlank @Size(max = 200)
    private String direccion;

    @NotBlank @Size(min = 7, max = 20)
    private String telefono;
}
