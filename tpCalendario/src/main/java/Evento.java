import java.time.LocalDateTime;

public class Evento extends Actividad {

    private LocalDateTime inicio;
    private LocalDateTime fin;
    private Repeticion repeticion;

    public Evento(String titulo, String descripcion, boolean diaCompleto, LocalDateTime inicio, LocalDateTime fin, Repeticion repeticion) { // (boolean repetido, Repeticion repeticion)
        super(titulo, descripcion, diaCompleto);

        if (diaCompleto) {
            inicio = LocalDateTime.of(inicio.getYear(), inicio.getMonth(), inicio.getDayOfMonth(), 0, 0);
            fin = LocalDateTime.of(fin.getYear(), fin.getMonth(), fin.getDayOfMonth(), 0, 0).plusDays(1);
        }

        this.inicio = inicio;
        this.fin = fin;

        this.repeticion = repeticion;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public Repeticion getRepeticion(){ return repeticion; }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    public void setRepeticion(Repeticion repeticion) { this.repeticion = repeticion; }

}
