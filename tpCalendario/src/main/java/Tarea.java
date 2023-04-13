import java.time.LocalDateTime;

public class Tarea extends Actividad {

    private boolean completada;

    public Tarea(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        super(titulo, descripcion, inicio, fin);
        this.completada = false;
    }

    public boolean getCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }
}
