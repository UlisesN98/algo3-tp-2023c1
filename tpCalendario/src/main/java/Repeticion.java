import java.time.LocalDateTime;
import java.util.TreeSet;

public abstract class Repeticion {

    protected Finalizacion finalizacion;
    protected String fin;

    public Repeticion(Finalizacion finalizacion, String fin) {
        this.finalizacion = finalizacion;
        this.fin = fin;
    }

    public abstract LocalDateTime calcularSiguienteRepeticion(LocalDateTime fecha);

    public abstract TreeSet<LocalDateTime> calcularRepeticionesPorIntervalo(LocalDateTime fecha, LocalDateTime inicioIntervalo, LocalDateTime finIntervalo);

    public abstract boolean superoLimite(LocalDateTime fecha);
}

