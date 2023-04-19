import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

public class Calendario {

    private final ArrayList<Evento> listaEventos;
    private final ArrayList<Tarea> listaTareas;

    private final TreeSet<Alarma> listaAlarmasCalendario;

    public Calendario() {
        this.listaEventos = new ArrayList<>();
        this.listaTareas = new ArrayList<>();
        this.listaAlarmasCalendario = new TreeSet<>(new Comparator<Alarma>() {
            @Override
            public int compare(Alarma o1, Alarma o2) {
                if (o1.getInicio().isBefore(o2.getInicio())){
                    return -1;
                }
                if (o1.getInicio().isAfter(o2.getInicio())){
                    return 1;
                }
                return 0;
            }
        });
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
        modificar(evento, nuevoTitulo, nuevaDescripcion);
        modificarInicioFin(evento, nuevoInicio, nuevoFin);
    }

    public void modificarInicioFin(Evento evento, LocalDateTime nuevoInicio, LocalDateTime nuevoFin){
        if (nuevoInicio != null){
            evento.setInicio(nuevoInicio);
        }
        if (nuevoFin != null){
            evento.setFin(nuevoFin);
        }
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

    public void modificarTarea(Tarea tarea, String nuevoTitulo, String nuevaDescripcion, LocalDateTime nuevoLimite) {
        modificar(tarea, nuevoTitulo, nuevaDescripcion);
        modificarLimite(tarea, nuevoLimite);
    }

    public void eliminarTarea(Tarea tarea) {
        listaTareas.remove(tarea);
    }

    public void modificar(Actividad actividad, String nuevoTitulo, String nuevaDescripcion){
        if (nuevoTitulo != null){
            actividad.setTitulo(nuevoTitulo);
        }
        if (nuevaDescripcion != null){
            actividad.setDescripcion(nuevaDescripcion);
        }
    }

    public void modificarLimite(Tarea tarea, LocalDateTime nuevoLimite){
        tarea.setLimite(nuevoLimite);
    }

    public void completarTarea(Tarea tarea) {
        tarea.setCompletada(true);
    }
}
