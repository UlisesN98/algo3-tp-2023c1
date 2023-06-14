package calendario;

import java.time.LocalDateTime;
import java.util.TreeSet;

public class RepeticionComun extends Repeticion {

    private final Frecuencia frecuencia; // Frecuencia que puede tomar una repeticion
    private final LocalDateTime fin; // Fecha límite de la repeticion

    // Constructor para repeticiones sin limite
    public RepeticionComun(LocalDateTime inicio, Frecuencia frecuencia) {
        super(inicio);
        this.frecuencia = frecuencia;
        this.fin = null;
    }

    // Constructor para repeticiones con fecha limite
    public RepeticionComun(LocalDateTime inicio, LocalDateTime fin, Frecuencia frecuencia) {
        super(inicio);
        this.frecuencia = frecuencia;
        this.fin = fin;
    }

    // Constructor para repeticiones con cantidad límite
    public RepeticionComun(LocalDateTime inicio, Integer fin, Frecuencia frecuencia) {
        super(inicio);
        this.frecuencia = frecuencia;
        this.fin = calcularFechaFin(fin - 1);
    }

    // Metodo que calcula la fecha límite basándose en la cantidad especificada
    private LocalDateTime calcularFechaFin(Integer fin) {
        return sumarTiempo(inicio, fin);
    }

    @Override
    public LocalDateTime calcularSiguienteRepeticion(LocalDateTime fecha) {
        if (superoLimite(fecha)) {
            return null;
        }
        if (fecha.isBefore(inicio)) {
            return inicio;
        }
        LocalDateTime repeticion = inicio;
        while (repeticion.isBefore(fecha)) {
            repeticion = sumarTiempo(repeticion, 1);
            if (superoLimite(repeticion)) {
                return null;
            }
        }
        return repeticion;
    }

    @Override
    public TreeSet<LocalDateTime> calcularRepeticionesPorIntervalo(LocalDateTime inicioIntervalo, LocalDateTime finIntervalo) {
        LocalDateTime repeticion = inicio;
        var repeticiones = new TreeSet<LocalDateTime>();

        if (repeticion.isAfter(finIntervalo)) {
            return repeticiones;
        }

        if (superoLimite(inicioIntervalo)) {
            return repeticiones;
        }

        while (repeticion.isBefore(finIntervalo)) {
            if (repeticion.isAfter(inicioIntervalo) || repeticion.equals(inicioIntervalo)) {
                repeticiones.add(repeticion);
            }
            repeticion = sumarTiempo(repeticion, 1);

            if (superoLimite(repeticion)) {break;}
        }
        return repeticiones;
    }

    // Calcula basándose en una frecuencia y una cantidad la siguiente fecha
    private LocalDateTime sumarTiempo(LocalDateTime fecha, int cantidad) {
        if (frecuencia.equals(Frecuencia.DIARIA)) {
            return fecha.plusDays(cantidad);
        } else if (frecuencia.equals(Frecuencia.SEMANAL)) {
            return fecha.plusWeeks(cantidad);
        } else if (frecuencia.equals(Frecuencia.MENSUAL)) {
            return fecha.plusMonths(cantidad);
        } else {
            return fecha.plusYears(cantidad);
        }
    }

    // Indica si la fecha pasada por parametro supero la fecha limite
    private boolean superoLimite(LocalDateTime fecha) {
        if (fin == null) {
            return false;
        }
        return fecha.isAfter(fin);
    }
}
