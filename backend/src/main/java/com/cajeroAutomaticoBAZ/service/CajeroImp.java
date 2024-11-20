package com.cajeroAutomaticoBAZ.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cajeroAutomaticoBAZ.entity.Denominaciones;
import com.cajeroAutomaticoBAZ.repository.DenominacionesRepository;

@Service
public class CajeroImp {

	@Autowired
	private DenominacionesRepository denomRep;

	public List<Denominaciones> realizarRetiro(double monto) {
		// Validar que el monto sea positivo
		validarMonto(monto);

		List<Denominaciones> resultado = new ArrayList<>();
		double montoRestante = monto;

		// Obtener todas las denominaciones desde la base de datos
		List<Denominaciones> denominaciones = denomRep.findAll();

		// Validar que haya suficiente dinero en el cajero
		validarSuficienteDinero(denominaciones, monto);

		// Realizar el retiro con las denominaciones disponibles
		for (Denominaciones denominacion : denominaciones) {
			if (montoRestante >= denominacion.getDenominacion() && denominacion.getCantidad() > 0) {
				// Calcular cuántos billetes o monedas entregar
				long cantidadAEntregar = (long) (montoRestante / denominacion.getDenominacion());

				// Limitar la cantidad a lo disponible en el cajero
				if (cantidadAEntregar > denominacion.getCantidad()) {
					cantidadAEntregar = denominacion.getCantidad();
				}

				// Reducir la cantidad del cajero en la base de datos
				denominacion.setCantidad(denominacion.getCantidad() - cantidadAEntregar);
				denomRep.save(denominacion);

				// Añadir la denominación al resultado
				if (cantidadAEntregar > 0) {
					resultado.add(new Denominaciones(denominacion.getId(), denominacion.getTipo(), cantidadAEntregar,
							denominacion.getDenominacion()));
				}

				// Reducir el monto restante
				montoRestante -= cantidadAEntregar * denominacion.getDenominacion();
			}
		}

		// Verificar si se logró entregar el monto completo
		if (montoRestante > 0) {
			throw new RuntimeException("No hay suficiente dinero en el cajero para realizar el retiro.");
		}

		return resultado;
	}

	// Método para validar que el monto es mayor que cero
	private void validarMonto(double monto) {
		if (monto <= 0) {
			throw new RuntimeException("El monto a retirar debe ser mayor que 0.");
		}

		// Validar que el monto no tenga centavos que no se puedan cubrir
		if (monto % 1 != 0 && (monto * 10) % 5 != 0) {
			throw new RuntimeException("No tenemos denominaciones suficientes para cubrir esos centavos. Intente otra cantidad.");
		}
	}

	// Método para validar que haya suficiente dinero en el cajero
	private void validarSuficienteDinero(List<Denominaciones> denominaciones, double monto) {
		double totalDisponible = 0;
		for (Denominaciones denominacion : denominaciones) {
			totalDisponible += denominacion.getCantidad() * denominacion.getDenominacion();
		}

		// Si no hay suficiente dinero en el cajero, lanzar el mensaje
		if (totalDisponible < monto) {
			throw new RuntimeException("Lo sentimos. No hay suficiente dinero en el cajero para realizar el retiro.");
		}
	}
}
