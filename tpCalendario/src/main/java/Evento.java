import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.TreeSet;

public class Evento extends Actividad {

    private Repeticion repeticion;

    public Evento(String titulo, String descripcion, boolean diaCompleto, LocalDateTime inicio, LocalDateTime fin, Repeticion repeticion) {
        super(titulo, descripcion, diaCompleto, inicio, fin);

        this.repeticion = repeticion;
    }

    public long getDiferencia() { return ChronoUnit.SECONDS.between(inicio, fin); }

    public void setInicio(LocalDateTime inicio) {
        if (diaCompleto) {
            inicio = LocalDateTime.of(inicio.getYear(), inicio.getMonth(), inicio.getDayOfMonth(), 0, 0);
        }
        this.inicio = inicio;
    }

    public void setRepeticion(Repeticion repeticion) { this.repeticion = repeticion; }
    public boolean esRepetido() {return (repeticion != null);}

    public TreeSet<LocalDateTime> repeticionesPorIntervalo(LocalDateTime inicioIntervalo, LocalDateTime finIntervalo) {
        return repeticion.calcularRepeticionesPorIntervalo(inicioIntervalo, finIntervalo);
    }

    @Override
    public String toString() {
        return titulo + ": " + descripcion + " - " + inicio + "/" + fin;
    }
}
