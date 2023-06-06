package calendario;

import java.time.LocalDateTime;
import java.util.TreeSet;

public class RepeticionDiariaIntervalo extends Repeticion {

    private final Integer intervalo; // Intervalo de dias en los que ocurre una repeticion
    private final LocalDateTime fin; // Fecha limite de la repeticion

    // Constructor para repeticiones sin limite
    public RepeticionDiariaIntervalo(LocalDateTime inicio, Integer intervalo) {
        super(inicio);
        this.intervalo = intervalo;
        this.fin = null;
    }

    // Constructor para repeticiones con fecha limite
    public RepeticionDiariaIntervalo(LocalDateTime inicio, LocalDateTime fin, Integer intervalo) {
        super(inicio);
        this.intervalo = intervalo;
        this.fin = fin;
    }

    // Constructor para repeticiones con cantidad limite
    public RepeticionDiariaIntervalo(LocalDateTime inicio, Integer fin, Integer intervalo) {
        super(inicio);
        this.intervalo = intervalo;
        this.fin = calcularFechaFin(fin - 1);

    }

    // Metodo que calcula la fecha limite en base a la cantidad especificada
    private LocalDateTime calcularFechaFin(Integer fin) { return inicio.plusDays((long) intervalo * fin); }

    @Override
    public LocalDateTime calcularSiguienteRepeticion(LocalDateTime fecha) {
        LocalDateTime repeticion = fecha.plusDays(intervalo);
        return superoLimite(repeticion)? null : repeticion;
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
            repeticion = repeticion.plusDays(intervalo);

            if (superoLimite(repeticion)) {break;}
        }
        return repeticiones;
    }

    // Indica si la fecha pasada por parametro supero la fecha limite
    private boolean superoLimite(LocalDateTime fecha) {
        if (fin == null) {
            return false;
        }
        return fecha.isAfter(fin);
    }
}
