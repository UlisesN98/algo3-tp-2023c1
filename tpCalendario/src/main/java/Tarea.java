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
            fin = fin.getHour() == 0 && fin.getMinute() == 0? fin : LocalDateTime.of(fin.getYear(), fin.getMonth(), fin.getDayOfMonth(), 0, 0).plusDays(1);
        }
        this.fin = fin;
    }

    @Override
    public void setDiaCompleto(boolean esDiaCompleto) {
        if (esDiaCompleto) {
            this.fin = LocalDateTime.of(fin.getYear(), fin.getMonth(), fin.getDayOfMonth(), 0, 0).plusDays(1);
        }
        this.diaCompleto = esDiaCompleto;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void cambiarEstadoTarea() {
        this.completada = !completada;
    }

    @Override
    public String toString() {
        return titulo + ": " + descripcion + " - " + fin;
    }
}
