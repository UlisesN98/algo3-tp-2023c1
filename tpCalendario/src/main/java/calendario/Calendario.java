package calendario;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Calendario implements Serializable {

    private final List<Evento> listaEventos;
    private final List<Tarea> listaTareas;

    public Calendario() {
        this.listaEventos = new ArrayList<>();
        this.listaTareas = new ArrayList<>();
    }

    // Recibe una fecha y hora y actualiza el inicio y fin
    // de un evento repetido en caso de que este ya haya ocurrido.
    public void actualizarEventosRepetidos(LocalDateTime tiempoActual) {
        for (Evento evento : listaEventos) {
            if (!evento.esRepetido()) {continue;}
            if (evento.getFin().isBefore(tiempoActual)) {
                evento.actualizarSiguienteRepeticion();
            }
        }
    }

    // METODOS DE BUSQUEDA

    // Busca Eventos a partir sus caracteristicas principales (titulo, descripcion, fecha de inicio y fecha de fin)
    // y devuelve un Lista con aquellos que las tienen
    public List<Evento> buscarEventos(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        var eventos = new ArrayList<Evento>();
        for (Evento evento : listaEventos) {
            if (evento.getTitulo().equals(titulo) && evento.getDescripcion().equals(descripcion) && evento.getInicio().equals(inicio) && evento.getFin().equals(fin)) {
                eventos.add(evento);
            }
        }
        return eventos;
    }

    // Busca Tareas a partir sus caracteristicas principales (titulo, descripcion y fecha limite)
    // y devuelve un Lista con aquellas que las tienen
    public List<Tarea> buscarTareas(String titulo, String descripcion, LocalDateTime limite) {
        var tareas = new ArrayList<Tarea>();
        for (Tarea tarea : listaTareas) {
            if (tarea.getTitulo().equals(titulo) && tarea.getDescripcion().equals(descripcion) && tarea.getInicio().equals(limite)) {
                tareas.add(tarea);
            }
        }
        return tareas;
    }

    // Dado un intervalo de tiempo devuelve un Lista con los Eventos que ocurren en este.
    // El Lista esta ordenado.
    public List<Evento> buscarEventoPorIntervalo(LocalDateTime inicioIntervalo, LocalDateTime finIntervalo) {
        var eventosIntervalo = new ArrayList<Evento>();

        for (Evento evento : listaEventos) {
            if (evento.esRepetido()){
                TreeSet<LocalDateTime> instanciasRepetidas = evento.repeticionesPorIntervalo(inicioIntervalo, finIntervalo);
                for (LocalDateTime fecha : instanciasRepetidas){
                    Evento eventoCreado = crearEventoRepetido(evento, fecha);
                    eventosIntervalo.add(eventoCreado);
                }
            } else {
                if ((evento.getInicio().isAfter(inicioIntervalo) || evento.getInicio().isEqual(inicioIntervalo)) && evento.getInicio().isBefore(finIntervalo)) {
                    eventosIntervalo.add(evento);
                } else if (evento.getFin().isAfter(inicioIntervalo) && evento.getFin().isBefore(finIntervalo)) {
                    eventosIntervalo.add(evento);
                } else if (evento.getInicio().isBefore(inicioIntervalo) && evento.getFin().isAfter(finIntervalo)) {
                    eventosIntervalo.add(evento);
                }
            }
        }
        eventosIntervalo.sort(new Actividad.ComparadorActividad());
        return eventosIntervalo;
    }

    // Dado un intervalo de tiempo devuelve un Lista con las Tareas que ocurren en este.
    // El Lista esta ordenado.
    public List<Tarea> buscarTareaPorIntervalo(LocalDateTime inicioIntervalo, LocalDateTime finIntervalo) {
        var tareasIntervalo = new ArrayList<Tarea>();
        for (Tarea tarea : listaTareas) {
            if ((tarea.getInicio().isAfter(inicioIntervalo) || tarea.getInicio().isEqual(inicioIntervalo)) && (tarea.getInicio().isBefore(finIntervalo))) {
                tareasIntervalo.add(tarea);
            }
        }
        tareasIntervalo.sort(new Actividad.ComparadorActividad());
        return tareasIntervalo;
    }

    // Dado un intervalo de tiempo devuelve un Lista con los Eventos y Tareas que ocurren en este.
    // El Lista esta ordenado.
    public List<Actividad> buscarPorIntervalo(LocalDateTime inicioIntervalo, LocalDateTime finIntervalo) {
        var listaActividades = new ArrayList<Actividad>();

        List<Evento> eventosIntervalo = buscarEventoPorIntervalo(inicioIntervalo, finIntervalo);
        List<Tarea> tareasIntervalo = buscarTareaPorIntervalo(inicioIntervalo, finIntervalo);

        listaActividades.addAll(eventosIntervalo);
        listaActividades.addAll(tareasIntervalo);

        listaActividades.sort(new Actividad.ComparadorActividad());
        return listaActividades;
    }


    // METODOS DE CREACION

    // Recibe un String que indique su titulo, un String que indique una descripcion, un boolean que indique si es de dia completo
    // un LocalDateTime que indique su fecha y hora de inicio, un LocalDateTime que indique su fecha y hora de fin, un array que
    // indique la fecha y hora absoluta de sus alarmas, un array de la misma longitud que el anterior que indique el efecto de estas
    // alarmas y una instancia de Repeticion que indique de que modo se repite. A partir de estas caracterisiticas se crea una
    // instancia de Evento que se guardara en la lista de Eventos.
    public void crearEvento(String titulo, String descripcion, boolean diaCompleto, LocalDateTime inicio, LocalDateTime fin, LocalDateTime[] inicioAlarmas, Efecto[] efectoAlarmas, Repeticion repeticion) {
        var nuevoEvento = new Evento(titulo, descripcion, diaCompleto, inicio, fin, repeticion);
        agregarAlarmas(nuevoEvento, inicioAlarmas, efectoAlarmas);
        listaEventos.add(nuevoEvento);
    }

    // Recibe un String que indique su titulo, un String que indique una descripcion, un boolean que indique si es de dia completo
    // un LocalDateTime que indique su fecha y hora de inicio, un LocalDateTime que indique su fecha y hora de fin, un array que
    // indique el intervalo de tiempo previo de sus alarmas , un array de la misma longitud que el anterior que indique el efecto de estas
    // alarmas y una instancia de Repeticion que indique de que modo se repite. A partir de estas caracterisiticas se crea una
    // instancia de Evento que se guardara en la lista de Eventos.
    public void crearEvento(String titulo, String descripcion, boolean diaCompleto, LocalDateTime inicio, LocalDateTime fin, Duration[] inicioAlarmas, Efecto[] efectoAlarmas, Repeticion repeticion) {
        var nuevoEvento = new Evento(titulo, descripcion, diaCompleto, inicio, fin, repeticion);
        agregarAlarmas(nuevoEvento, inicioAlarmas, efectoAlarmas);
        listaEventos.add(nuevoEvento);
    }

    private Evento crearEventoRepetido(Evento evento, LocalDateTime fecha) {
        var nuevoEvento = new EventoRepetido(evento.getTitulo(), evento.getDescripcion(), evento.isDiaCompleto(), fecha, fecha.plusSeconds(ChronoUnit.SECONDS.between(evento.getInicio(), evento.getFin())), evento.getRepeticion(), evento);

        List<LocalDateTime> inicios = new ArrayList<>();
        List<Efecto> efectos = new ArrayList<>();
        var alarmas = evento.getListaAlarmas();
        for (Alarma alarma : alarmas) {
            inicios.add(fecha.minusSeconds(ChronoUnit.SECONDS.between(alarma.getInicio(), evento.getInicio())));
            efectos.add(alarma.getEfecto());
        }
        agregarAlarmas(nuevoEvento, inicios.toArray(new LocalDateTime[0]), efectos.toArray(new Efecto[0]));
        return nuevoEvento;
    }

    // Recibe un String que indique su titulo, un String que indique una descripcion, un boolean que indique si es de dia completo,
    // un LocalDateTime que indique su fecha y hora limite, un array que indique la fecha y hora absoluta de sus alarmas, un array
    // de la misma longitud que el anterior que indique el efecto de estas alarmas. A partir de estas caracterisiticas se crea una
    // instancia de Tarea que se guardara en la lista de Tareas.
    public void crearTarea(String titulo, String descripcion, boolean diaCompleto, LocalDateTime limite, LocalDateTime[] inicioAlarmas, Efecto[] efectoAlarmas) {
        var nuevaTarea = new Tarea(titulo, descripcion, diaCompleto, limite);
        agregarAlarmas(nuevaTarea, inicioAlarmas, efectoAlarmas);
        listaTareas.add(nuevaTarea);
    }

    // Recibe un String que indique su titulo, un String que indique una descripcion, un boolean que indique si es de dia completo,
    // un LocalDateTime que indique su fecha y hora limite, un array que indique el intervalo de tiempo previo de sus alarmas, un array
    // de la misma longitud que el anterior que indique el efecto de estas alarmas. A partir de estas caracterisiticas se crea una
    // instancia de Tarea que se guardara en la lista de Tareas.
    public void crearTarea(String titulo, String descripcion, boolean diaCompleto, LocalDateTime limite, Duration[] inicioAlarmas, Efecto[] efectoAlarmas) {
        var nuevaTarea = new Tarea(titulo, descripcion, diaCompleto, limite);
        agregarAlarmas(nuevaTarea, inicioAlarmas, efectoAlarmas);
        listaTareas.add(nuevaTarea);
    }

    // Crea y agrega las alarmas indicadas a un evento o tarea. El inicio de las alarmas lo determina una fecha
    // y hora definida
    private void agregarAlarmas(Actividad nuevaActividad, LocalDateTime[] inicioAlarmas, Efecto[] efectoAlarmas) {
        if (inicioAlarmas.length != 0) {
            for (int i = 0; i < inicioAlarmas.length; i++) {
                nuevaActividad.agregarAlarma(inicioAlarmas[i], efectoAlarmas[i]);
            }
        }
    }

    // Crea y agrega las alarmas indicadas a un evento o tarea. El inicio de las alarmas lo determina una intervalo previo
    private void agregarAlarmas(Actividad nuevaActividad, Duration[] inicioAlarmas, Efecto[] efectoAlarmas) {
        if (inicioAlarmas.length != 0) {
            for (int i = 0; i < inicioAlarmas.length; i++) {
                nuevaActividad.agregarAlarma(inicioAlarmas[i], efectoAlarmas[i]);
            }
        }
    }

    // METODOS DE MODIFICACION

    // Recibe una instancia de Evento o Tarea y modifica su titulo y su descripcion, o unicamente uno de los dos en
    // caso de que uno de los parametros sea null.
    public void modificar(Actividad actividad, String nuevoTitulo, String nuevaDescripcion) {
        if (nuevoTitulo != null){
            actividad.setTitulo(nuevoTitulo);
        }
        if (nuevaDescripcion != null){
            actividad.setDescripcion(nuevaDescripcion);
        }
    }

    // Recibe una instancia de Evento y modifica su inicio y su fin, o unicamente uno de los dos en
    // caso de que uno de los parametros sea null.
    public void modificar(Evento evento, LocalDateTime nuevoInicio, LocalDateTime nuevoFin) {
        if (nuevoInicio != null){
            evento.setInicio(nuevoInicio);
        }
        if (nuevoFin != null){
            evento.setFin(nuevoFin);
        }
    }

    // Recibe una instancia de Tarea y modifica su limite.
    public void modificar(Tarea tarea, LocalDateTime nuevoLimite){
        if (nuevoLimite != null){
            tarea.setInicio(nuevoLimite);
        }
    }

    // Recibe una instancia de Evento y modifica su titulo, descripcion, inicio y fin, o unicamente los parametros pasados distintos de null.
    public void modificar(Evento evento, String nuevoTitulo, String nuevaDescripcion, LocalDateTime nuevoInicio, LocalDateTime nuevoFin) {
        modificar(evento, nuevoTitulo, nuevaDescripcion);
        modificar(evento, nuevoInicio, nuevoFin);
    }

    // Recibe una instancia de Tarea y modifica su titulo, descripcion y limite, o unicamente los parametros pasados distintos de null.
    public void modificar(Tarea tarea, String nuevoTitulo, String nuevaDescripcion, LocalDateTime nuevoLimite) {
        modificar(tarea, nuevoTitulo, nuevaDescripcion);
        modificar(tarea, nuevoLimite);
    }

    // Recibe una instancia de Evento y segun el parametro pasado cambia su estado de dia completo.
    public void modificar(Evento evento, boolean diaCompleto) {
        evento.setDiaCompleto(diaCompleto);
    }

    // Recibe una instancia de Tarea y segun el parametro pasado cambia su estado de dia completo.
    public void modificar(Tarea tarea, boolean diaCompleto) {
        tarea.setDiaCompleto(diaCompleto);
    }

    // Modifica la Repeticion de un Evento tras pasarle una nueva instancia de esta con las nuevas caracteristicas requeridas.
    public void modificar(Evento evento, Repeticion repeticion) {
        evento.setRepeticion(repeticion);
    }


    // METODOS DE ELIMINACION

    // Recibe una instancia de Evento y la quita de la lista de Eventos.
    public void eliminarEvento(Evento evento) {
        if (evento.esOriginal()) {
            listaEventos.remove(evento);
        } else {
            var eventoRep = (EventoRepetido) evento;
            listaEventos.remove(eventoRep.getEventoOriginal());
        }
    }

    // Recibe una instancia de Tarea y la quita de la lista de Tareas.
    public void eliminarTarea(Tarea tarea) {
        listaTareas.remove(tarea);
    }


    // METODOS DE COMPLETAR TAREA

    // Recibe una instancia de Tarea y devuelve un boolean que indica si esta completa o no.
    public boolean tareaEstaCompletada(Tarea tarea) {
        return tarea.isCompletada();
    }

    // Recibe una instancia de Tarea y dependiendo de su estado la cambia a completa o incompleta.
    public void marcarTarea(Tarea tarea) {
        tarea.cambiarEstadoTarea();
    }


    // METODOS RELATIVOS A ALARMAS

    // Recibe una instancia de Evento o Tarea y agrega le agrega una alarma con las
    // caracteristicas pasadas por parametro.
    public void agregarAlarma(Actividad actividad, LocalDateTime inicio, Efecto efecto) {
        actividad.agregarAlarma(inicio, efecto);
    }

    // Recibe una instancia de Evento o Tarea y agrega le agrega una alarma con las
    // caracteristicas pasadas por parametro.
    public void agregarAlarma(Actividad actividad, Duration inicio, Efecto efecto) {
        actividad.agregarAlarma(inicio, efecto);
    }

    // Recibe una instancia de Evento o Tarea y modifica la alarma, que tiene las caracteristicas indicadas, con el parametro pasado.
    public void modificarAlarma(Actividad actividad, LocalDateTime inicio, Efecto efecto, LocalDateTime nuevoInicio) {
        Alarma alarma = actividad.buscarAlarma(inicio, efecto);
        actividad.eliminarAlarma(alarma);
        actividad.agregarAlarma(nuevoInicio, efecto);
    }

    // Recibe una instancia de Evento o Tarea y modifica la alarma, que tiene las caracteristicas indicadas, con el parametro pasado.
    public void modificarAlarma(Actividad actividad, LocalDateTime inicio, Efecto efecto, Duration nuevoInicio) {
        Alarma alarma = actividad.buscarAlarma(inicio, efecto);
        actividad.eliminarAlarma(alarma);
        actividad.agregarAlarma(nuevoInicio, efecto);
    }

    // Recibe una instancia de Evento o Tarea y modifica la alarma, que tiene las caracteristicas indicadas, con el parametro pasado.
    public void modificarAlarma(Actividad actividad, LocalDateTime inicio, Efecto efecto, Efecto nuevoEfecto) {
        Alarma alarma = actividad.buscarAlarma(inicio, efecto);
        actividad.eliminarAlarma(alarma);
        actividad.agregarAlarma(inicio, nuevoEfecto);
    }

    // Recibe una instancia de Evento o Tarea y le elimina la alarma que tiene las caracteristicas indicadas.
    public void eliminarAlarma(Actividad actividad, LocalDateTime inicio, Efecto efecto) {
        Alarma alarma = actividad.buscarAlarma(inicio, efecto);
        actividad.eliminarAlarma(alarma);
    }

    // Devuelve la siguiente alarma que deberia sonar. Devuelve null si no hay ninguna alarma.
    public Alarma obtenerProximaAlarma() {
        Alarma proximaAlarma = null;
        for (Evento evento : listaEventos) {
            proximaAlarma = actualizarProximaAlarma(evento, proximaAlarma);
        }
        for (Tarea tarea : listaTareas) {
            proximaAlarma = actualizarProximaAlarma(tarea, proximaAlarma);
        }
        return proximaAlarma;
    }

    private Alarma actualizarProximaAlarma(Actividad actividad, Alarma proximaAlarma) {
        Alarma alarma = actividad.obtenerProximaAlarma();
        if (alarma == null) {
            return proximaAlarma;
        }
        if (proximaAlarma == null) {
            return alarma;
        }
        if (alarma.esAnterior(proximaAlarma)) {
            return alarma;
        }
        return proximaAlarma;
    }

    // Metodo que muestra, en base a una fecha y hora indicada,
    // si debe sonar la proxima alarma.
    public boolean iniciaProximaAlarma (LocalDateTime tiempoActual) {
        if (obtenerProximaAlarma() == null) {
            return false;
        }
        return tiempoActual.equals(obtenerProximaAlarma().getInicio());
    }

    // Metodo que obtiene a partir de la alarma que debio activarse el Evento o Tarea correspondiente.
    // Si se trata de un Evento con repeticion, actualiza la alarma disparada a la de su repeticion.
    // Finalmente elimina la alarma disparada.
    public Actividad dispararProximaAlarma() {
        Alarma alarmaActual = obtenerProximaAlarma();
        Actividad actividad = alarmaActual.getActividad();

        actualizarAlarmaRepeticion(actividad, alarmaActual);

        actividad.eliminarAlarma(alarmaActual);
        return actividad;
    }

    // Metodo que se encarga de actualizar la alarma de un Evento dado el caso de que este deba repetirse.
    public void actualizarAlarmaRepeticion(Actividad actividad, Alarma alarma) {
        if (!listaEventos.contains(actividad)) { return;}

        int i = listaEventos.indexOf(actividad);
        Evento eventoAlarma = listaEventos.get(i);

        if (!eventoAlarma.esRepetido()) {return;}

        LocalDateTime fechaRepeticion = eventoAlarma.getSiguienteRepeticion();

        if (fechaRepeticion == null) {return;}

        LocalDateTime fechaSiguienteAlarma = eventoAlarma.getRepeticion().calcularSiguienteRepeticion(alarma.getInicio());

        eventoAlarma.agregarAlarma(fechaSiguienteAlarma, alarma.getEfecto());
    }

    // METODOS DE SERIALIZACION

    public void serializar(OutputStream os) throws IOException {
        ObjectOutputStream objectOutStream = new ObjectOutputStream(os);
        objectOutStream.writeObject(this);
        objectOutStream.flush();
    }

    public static Calendario deserializar(InputStream is) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInStream = new ObjectInputStream(is);
        return (Calendario) objectInStream.readObject();
    }

}
