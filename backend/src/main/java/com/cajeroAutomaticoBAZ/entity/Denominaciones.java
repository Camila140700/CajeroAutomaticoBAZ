package com.cajeroAutomaticoBAZ.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table (name="DENOMINACIONES_CAJA")
@NoArgsConstructor //Constructor vacío 
@AllArgsConstructor //Constructor con parametros; cada uno de los atributos 
@ToString //Metodo toString 
@Getter //Genera getters
@Setter //Genera setters 

public class Denominaciones  {
	
	@Id
	private Long id;
	private String tipo;
	private Long cantidad;
	private Double denominacion;
	
	
	
	

	
	
}
