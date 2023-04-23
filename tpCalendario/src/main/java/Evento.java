import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.TreeSet;

public class Evento extends Actividad {

    private LocalDateTime inicio;
    private LocalDateTime fin;
    private Repeticion repeticion;

    public Evento(String titulo, String descripcion, boolean diaCompleto, LocalDateTime inicio, LocalDateTime fin, Repeticion repeticion) {
        super(titulo, descripcion, diaCompleto);

        if (diaCompleto) {
            inicio = LocalDateTime.of(inicio.getYear(), inicio.getMonth(), inicio.getDayOfMonth(), 0, 0);
            fin = LocalDateTime.of(fin.getYear(), fin.getMonth(), fin.getDayOfMonth(), 0, 0);
        }

        this.inicio = inicio;
        this.fin = fin;

        this.repeticion = repeticion;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }
    public LocalDateTime getFin() {return fin;}

    public long getDiferencia() {
        return ChronoUnit.SECONDS.between(inicio, fin);
    }

    public void setInicio(LocalDateTime inicio) {
        if (diaCompleto) {
            inicio = LocalDateTime.of(inicio.getYear(), inicio.getMonth(), inicio.getDayOfMonth(), 0, 0);
        }
        this.inicio = inicio;
    }
    public void setFin(LocalDateTime fin) {
        if (diaCompleto) {
            fin = LocalDateTime.of(fin.getYear(), fin.getMonth(), fin.getDayOfMonth(), 0, 0);
        }
        this.fin = fin;
    }

    @Override
    public void setDiaCompleto(boolean esDiaCompleto) {
        if (esDiaCompleto) {
            inicio = LocalDateTime.of(inicio.getYear(), inicio.getMonth(), inicio.getDayOfMonth(), 0, 0);
            fin = LocalDateTime.of(fin.getYear(), fin.getMonth(), fin.getDayOfMonth(), 0, 0);
        }
        this.diaCompleto = esDiaCompleto;
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
