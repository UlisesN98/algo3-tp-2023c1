import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.TreeSet;

public class RepeticionSemanalDias extends Repeticion {

    private DayOfWeek[] dias;
    public RepeticionSemanalDias(DayOfWeek[] dias, Finalizacion finalizacion, String fin) {
        super(finalizacion, fin);
        this.dias = dias;
    }

    @Override
    public LocalDateTime calcularSiguienteRepeticion(LocalDateTime fecha) {
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
        return repeticiones.first();
    }

    @Override
    public TreeSet<LocalDateTime> calcularRepeticionesPorIntervalo(LocalDateTime fecha, LocalDateTime inicioIntervalo, LocalDateTime finIntervalo) {
        return null;
    }

    @Override
    public boolean superoLimite(LocalDateTime fecha) {
        return false;
    }

}
