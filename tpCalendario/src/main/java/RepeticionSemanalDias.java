import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.TreeSet;

public class RepeticionSemanalDias extends Repeticion {

    private DayOfWeek[] dias; // Arreglo con los dias de la semana donde ocurre una repeticion
    private LocalDateTime fin; // Fecha limite de la repeticion

    // Constructor para repeticiones sin limite
    public RepeticionSemanalDias(LocalDateTime inicio, DayOfWeek[] dias) {
        super(inicio);
        this.dias = dias;
        this.fin = null;
    }

    // Constructor para repeticiones con fecha limite
    public RepeticionSemanalDias(LocalDateTime inicio, LocalDateTime fin, DayOfWeek[] dias) {
        super(inicio);
        this.dias = dias;
        this.fin = fin;
    }

    // Constructor para repeticiones con cantidad limite
    public RepeticionSemanalDias(LocalDateTime inicio, Integer fin,  DayOfWeek[] dias) {
        super(inicio);
        this.dias = dias;
        this.fin = calcularFechaFin(fin - 1);
    }

    // Metodo que calcula la fecha limite en base a la cantidad especificada
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
        var repeticiones = new TreeSet<LocalDateTime>();
        repeticiones = obtenerRepeticionesSemana(fecha);
        return superoLimite(repeticiones.first())? null : repeticiones.first();
    }

    @Override
    public TreeSet<LocalDateTime> calcularRepeticionesPorIntervalo(LocalDateTime inicioIntervalo, LocalDateTime finIntervalo) {
        LocalDateTime repeticion = inicio;
        var repeticiones = new TreeSet<LocalDateTime>();

        if (repeticion.isAfter(finIntervalo)) {return repeticiones;}

        if (superoLimite(inicioIntervalo)) {return repeticiones;}

        while (repeticion.isBefore(finIntervalo) || repeticion.equals(finIntervalo)) {
            TreeSet<LocalDateTime> repSemanales = obtenerRepeticionesSemana(repeticion);
            for (LocalDateTime rep : repSemanales) {
                if (superoLimite(rep)) {break;}
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

    // Devuelve un treeset con las fechas de los dias de la semana donde ocurre una repeticion,
    // toma como punto de partida de la semana el dia de la fecha especificada por parametro
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
