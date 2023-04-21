import java.time.LocalDateTime;
import java.util.TreeSet;

public class RepeticionComun extends Repeticion {

    private Frecuencia frecuencia;

    public RepeticionComun(Frecuencia frecuencia, Finalizacion finalizacion, String fin) {
        super(finalizacion, fin);
        this.frecuencia = frecuencia;
    }

    @Override
    public LocalDateTime calcularSiguienteRepeticion(LocalDateTime fecha) {
        LocalDateTime repeticion = sumarTiempo(fecha, 1);
        return superoLimite(repeticion)? null : repeticion;
    }

    @Override
    public TreeSet<LocalDateTime> calcularRepeticionesPorIntervalo(LocalDateTime fecha, LocalDateTime inicioIntervalo, LocalDateTime finIntervalo) {
        return null;
    }

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

    @Override
    public boolean superoLimite(LocalDateTime fecha) {
        if (finalizacion.equals(Finalizacion.FECHA)) {
            return fecha.isAfter(LocalDateTime.parse(fin));
        } else if (finalizacion.equals(Finalizacion.CANTIDAD)) {
            return fecha.isAfter(sumarTiempo(fecha, Integer.parseInt(fin)));
        } else {
            return false;
        }
    }



}
