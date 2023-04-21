import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.TreeSet;

public class RepeticionSemanalDias extends Repeticion {

    private DayOfWeek[] dias;

    public RepeticionSemanalDias(LocalDateTime inicio, DayOfWeek[] dias, Finalizacion finalizacion, String fin) {
        super(inicio, finalizacion, fin);
        this.dias = dias;
    }

    @Override
    public LocalDateTime calcularSiguienteRepeticion(LocalDateTime fecha) {
        var repeticiones = new TreeSet<LocalDateTime>();
        repeticiones = obtenerRepeticionesSemana(fecha);
        return superoLimite(repeticiones.first())? null : repeticiones.first();
    }

    @Override
    public TreeSet<LocalDateTime> calcularRepeticionesPorIntervalo(LocalDateTime fecha, LocalDateTime inicioIntervalo, LocalDateTime finIntervalo) {
        return null;
    }

    @Override
    public boolean superoLimite(LocalDateTime fecha) {
        if (finalizacion.equals(Finalizacion.FECHA)) {
            return fecha.isAfter(LocalDateTime.parse(fin));
        } else if (finalizacion.equals(Finalizacion.CANTIDAD)) {
            return fecha.isAfter(obtenerUltimaRepeticionCantidad(inicio));
        } else {
            return false;
        }
    }

    private LocalDateTime obtenerUltimaRepeticionCantidad(LocalDateTime fecha) {
        var repeticiones = new TreeSet<LocalDateTime>();

        do {
            repeticiones.addAll(obtenerRepeticionesSemana(fecha));
            fecha = fecha.plusWeeks(1);
        } while (repeticiones.size() <= Integer.parseInt(fin));
        for (int i = 1; i < Integer.parseInt(fin); i++) {
            repeticiones.remove(repeticiones.first());
        }
        return repeticiones.first();
    }

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
