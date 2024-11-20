package com.cajeroAutomaticoBAZ.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cajeroAutomaticoBAZ.entity.Denominaciones;
import com.cajeroAutomaticoBAZ.service.CajeroImp;

@RestController // Marca la clase como un controlador REST
@RequestMapping("CajeroWS") // Definimos la URL base para las rutas del controlador
@CrossOrigin // Habilita el acceso
public class CajeroWS {

	@Autowired
	private CajeroImp cajeroImp;

	// http://localhost:9002/CajeroWs/retirar ---- URL
	@PostMapping("retirar")
	public ResponseEntity<?> retirar(@RequestBody Map<String, Double> request) {
		double montoSolicitado = request.get("monto");
		try {
			// Llamamos al servicio para procesar el retiro
			List<Denominaciones> denominacionesEntregadas = cajeroImp.realizarRetiro(montoSolicitado);

			// Retornamos las denominaciones entregadas
			return new ResponseEntity<>(denominacionesEntregadas, HttpStatus.OK);

		} catch (Exception e) {
			// Si ocurre una excepción (por ejemplo, no hay suficiente dinero en el cajero)
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}