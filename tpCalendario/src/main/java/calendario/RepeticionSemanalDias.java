package calendario;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

public class RepeticionSemanalDias extends Repeticion {

    private final Set<DayOfWeek> dias; // Arreglo con los días de la semana donde ocurre una repeticion
    private final LocalDateTime fin; // Fecha límite de la repeticion

    // Constructor para repeticiones sin limite
    public RepeticionSemanalDias(LocalDateTime inicio, Set<DayOfWeek> dias) {
        super(inicio);
        this.dias = dias;
        this.fin = null;
    }

    // Constructor para repeticiones con fecha limite
    public RepeticionSemanalDias(LocalDateTime inicio, LocalDateTime fin, Set<DayOfWeek> dias) {
        super(inicio);
        this.dias = dias;
        this.fin = fin;
    }

    // Constructor para repeticiones con cantidad límite
    public RepeticionSemanalDias(LocalDateTime inicio, Integer fin,  Set<DayOfWeek> dias) {
        super(inicio);
        this.dias = dias;
        this.fin = calcularFechaFin(fin - 1);
    }

    // Metodo que calcula la fecha límite basándose en la cantidad especificada
    private LocalDateTime calcularFechaFin(Integer fin) {
        LocalDateTime fecha = inicio;
        var repeticiones = new TreeSet<LocalDateTime>();

        while (repeticiones.size() <= fin) {
            repeticiones.addAll(obtenerRepeticionesSemana(fecha));
            fecha = fecha.plusWeeks(1);
        }
        for (int i = 0; i < fin; i++) {
            repeticiones.remove(repeticiones.first());
        }
        return repeticiones.first();
    }

    @Override
    public LocalDateTime calcularSiguienteRepeticion(LocalDateTime fecha) {
        if (superoLimite(fecha)) {
            return null;
        }
        LocalDateTime inicioSemana = inicio;
        var repeticiones = new TreeSet<LocalDateTime>();
        while (true) {
            repeticiones = obtenerRepeticionesSemana(inicioSemana);
            for (LocalDateTime r : repeticiones) {
                if (superoLimite(r)) {
                    return null;
                }
                if (r.isBefore(fecha)) {
                    continue;
                }
                return r;
            }
            inicioSemana = inicioSemana.plusWeeks(1);
        }
    }

    @Override
    public TreeSet<LocalDateTime> calcularRepeticionesPorIntervalo(LocalDateTime inicioIntervalo, LocalDateTime finIntervalo) {
        LocalDateTime repeticion = inicio;
        var repeticiones = new TreeSet<LocalDateTime>();

        if (repeticion.isAfter(finIntervalo)) {return repeticiones;}

        if (superoLimite(inicioIntervalo)) {return repeticiones;}

        while (repeticion.isBefore(finIntervalo)) {
            TreeSet<LocalDateTime> repSemanales = obtenerRepeticionesSemana(repeticion);
            for (LocalDateTime rep : repSemanales) {
                if (superoLimite(rep) || (rep.isAfter(finIntervalo))) {break;}
                if (rep.isAfter(inicioIntervalo) || rep.equals(inicioIntervalo)) {repeticiones.add(rep);}
            }
            repeticion = repeticion.plusWeeks(1);

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

    // Devuelve un treeset con las fechas de los días de la semana donde ocurre una repeticion,
    // toma como punto de partida de la semana el día de la fecha especificada por parametro
    private TreeSet<LocalDateTime> obtenerRepeticionesSemana(LocalDateTime fecha) {
        var repeticiones = new TreeSet<LocalDateTime>();
        for (DayOfWeek diaRep : dias) {
            DayOfWeek dia = fecha.getDayOfWeek();
            int resta = diaRep.getValue() - dia.getValue();
            if (resta >= 0) {
                repeticiones.add(fecha.plusDays(resta));
            } else {
                repeticiones.add(fecha.plusDays(resta).plusWeeks(1));
            }
        }
        return repeticiones;
    }
}
