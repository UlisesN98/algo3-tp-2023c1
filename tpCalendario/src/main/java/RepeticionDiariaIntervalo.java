import java.time.LocalDateTime;
import java.util.TreeSet;

public class RepeticionDiariaIntervalo extends Repeticion {

    private Integer intervalo;
    public RepeticionDiariaIntervalo(Integer intervalo, Finalizacion finalizacion, String fin) {
        super(finalizacion, fin);
        this.intervalo = intervalo;
    }

    @Override
    public LocalDateTime calcularSiguienteRepeticion(LocalDateTime fecha) {
        LocalDateTime repeticion = fecha.plusDays(intervalo);
        return superoLimite(repeticion)? null : repeticion;
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
            return fecha.isAfter(fecha.plusDays((long) intervalo * Integer.parseInt(fin)));
        } else {
            return false;
        }
    }

}
