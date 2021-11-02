package com.spring.oechsle.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ClienteDTO {

	private String id;
	private String nombre;
    private String apellido;
    private String edad;
    private String fechaNacimiento;
    private Date fechaProbableMuerte;
	
}
