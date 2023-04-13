import java.time.LocalDateTime;
import java.util.ArrayList;

public class Calendario {

    private final ArrayList<Evento> listaEventos;
    private final ArrayList<Tarea> listaTareas;

    public Calendario() {
        this.listaEventos = new ArrayList<>();
        this.listaTareas = new ArrayList<>();
    }

    public Evento buscarEvento(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        for (Evento evento : listaEventos) {
            if (evento.getTitulo().equals(titulo) && evento.getDescripcion().equals(descripcion) && evento.getInicio().equals(inicio) && evento.getFin().equals(fin)) {
                return evento;
            }
        }
        return null;
    }

    public void crearEvento(String titulo, String descripcion, boolean diaCompleto, LocalDateTime inicio, LocalDateTime fin, LocalDateTime[] inicioAlarmas, Integer[] efectoAlarmas) {
        var nuevoEvento = new Evento(titulo, descripcion, diaCompleto, inicio, fin);

        if (inicioAlarmas.length != 0) {
            for (int i = 0; i < inicioAlarmas.length; i++) {
                nuevoEvento.agregarAlarma(inicioAlarmas[i], efectoAlarmas[i]);
            }
        }

        listaEventos.add(nuevoEvento);
    }

    public void modificarEvento(Evento evento, String nuevoTitulo, String nuevaDescripcion, LocalDateTime nuevoInicio, LocalDateTime nuevoFin) {
        evento.setTitulo(nuevoTitulo);
        evento.setDescripcion(nuevaDescripcion);
        evento.setInicio(nuevoInicio);
        evento.setFin(nuevoFin);
    }

    public void eliminarEvento(Evento evento) {
        listaEventos.remove(evento);
    }

    public Tarea buscarTarea(String titulo, String descripcion, LocalDateTime limite) {
        for (Tarea tarea : listaTareas) {
            if (tarea.getTitulo().equals(titulo) && tarea.getDescripcion().equals(descripcion) && tarea.getLimite().equals(limite)) {
                return tarea;
            }
        }
        return null;
    }

    public void crearTarea(String titulo, String descripcion, boolean diaCompleto, LocalDateTime limite, LocalDateTime[] inicioAlarmas, Integer[] efectoAlarmas) {
        var nuevaTarea = new Tarea(titulo, descripcion, diaCompleto, limite);

        if (inicioAlarmas.length != 0) {
            for (int i = 0; i < inicioAlarmas.length; i++) {
                nuevaTarea.agregarAlarma(inicioAlarmas[i], efectoAlarmas[i]);
            }
        }

        listaTareas.add(nuevaTarea);
    }

    public void modificarTarea(Tarea tarea, String nuevoTitulo, String nuevaDescripcion, LocalDateTime nuevolimite) {
        tarea.setTitulo(nuevoTitulo);
        tarea.setDescripcion(nuevaDescripcion);
        tarea.setLimite(nuevolimite);
    }

    public void eliminarTarea(Tarea tarea) {
        listaTareas.remove(tarea);
    }

    public void completarTarea(Tarea tarea) {
        tarea.setCompletada(true);
    }
}
