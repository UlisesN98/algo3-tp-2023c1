import java.time.LocalDateTime;

public class Tarea extends Actividad {

    private boolean completada;

    public Tarea(String titulo, String descripcion, boolean diaCompleto, LocalDateTime inicio, LocalDateTime fin) {
        super(titulo, descripcion, diaCompleto, inicio, fin);

        this.completada = false;
    }

    @Override
    public void setFin(LocalDateTime fin) {
        if (diaCompleto) {
            inicio = LocalDateTime.of(fin.getYear(), fin.getMonth(), fin.getDayOfMonth(), 0, 0);
            this.fin = inicio.plusDays(1);
        } else {
            this.inicio = fin;
            this.fin = fin;
        }
    }

    @Override
    public void setDiaCompleto(boolean esDiaCompleto) {
        if (!diaCompleto && esDiaCompleto) {
            inicio = LocalDateTime.of(fin.getYear(), fin.getMonth(), fin.getDayOfMonth(), 0, 0);
            fin = inicio.plusDays(1);
        } else if (diaCompleto && !esDiaCompleto) {
            inicio = fin;
        }
        this.diaCompleto = esDiaCompleto;
    }

    public boolean isCompletada() { return completada; }

    public void cambiarEstadoTarea() { this.completada = !completada; }

    @Override
    public String toString() {
        return titulo + ": " + descripcion + " - " + fin;
    }
}
