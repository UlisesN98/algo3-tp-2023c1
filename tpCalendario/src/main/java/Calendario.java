import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.TreeSet;

public class Calendario {

    private final ArrayList<Evento> listaEventos; // ArrayList que contiene Eventos
    private final ArrayList<Tarea> listaTareas; // ArrayList que contiene Tareas
    private final TreeSet<Alarma> listaAlarmas; // Treeset que contiene las Alarmas existentes ordenadas por orden aparicion

    public Calendario() {
        this.listaEventos = new ArrayList<>();
        this.listaTareas = new ArrayList<>();
        this.listaAlarmas = new TreeSet<>(new Alarma.ComparadorAlarma());
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
    // y devuelve un ArrayList con aquellos que las tienen
    public ArrayList<Evento> buscarEvento(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        var eventos = new ArrayList<Evento>();
        for (Evento evento : listaEventos) {
            if (evento.getTitulo().equals(titulo) && evento.getDescripcion().equals(descripcion) && evento.getInicio().equals(inicio) && evento.getFin().equals(fin)) {
                eventos.add(evento);
            }
        }
        return eventos;
    }

    // Busca Tareas a partir sus caracteristicas principales (titulo, descripcion y fecha limite)
    // y devuelve un ArrayList con aquellas que las tienen
    public ArrayList<Tarea> buscarTarea(String titulo, String descripcion, LocalDateTime limite) {
        var tareas = new ArrayList<Tarea>();
        for (Tarea tarea : listaTareas) {
            if (tarea.getTitulo().equals(titulo) && tarea.getDescripcion().equals(descripcion) && tarea.getFin().equals(limite)) {
                tareas.add(tarea);
            }
        }
        return tareas;
    }

    // Dado un intervalo de tiempo devuelve un ArrayList con los Eventos que ocurren en este.
    // El ArrayList esta ordenado.
    public ArrayList<Evento> buscarEventoPorIntervalo(LocalDateTime inicioIntervalo, LocalDateTime finIntervalo) {
        var eventosIntervalo = new ArrayList<Evento>();

        for (Evento evento : listaEventos) {
            if ((evento.getInicio().isAfter(inicioIntervalo) || evento.getInicio().isEqual(inicioIntervalo)) && evento.getInicio().isBefore(finIntervalo)) {
                eventosIntervalo.add(evento);
            } else if (evento.getFin().isAfter(inicioIntervalo) && evento.getFin().isBefore(finIntervalo)) {
                eventosIntervalo.add(evento);
            } else if (evento.getInicio().isBefore(inicioIntervalo) && evento.getFin().isAfter(finIntervalo)) {
                eventosIntervalo.add(evento);
            }

            if (evento.esRepetido()){
                TreeSet<LocalDateTime> instanciasRepetidas = evento.repeticionesPorIntervalo(inicioIntervalo, finIntervalo);
                for (LocalDateTime fecha : instanciasRepetidas){
                    Evento eventoCreado = new Evento(evento.getTitulo(), evento.getDescripcion(), evento.isDiaCompleto(), fecha, fecha.plusSeconds(ChronoUnit.SECONDS.between(evento.getInicio(), evento.getFin())), null);
                    eventosIntervalo.add(eventoCreado);
                }
            }
        }
        eventosIntervalo.sort(new Actividad.ComparadorActividad());
        return eventosIntervalo;
    }

    // Dado un intervalo de tiempo devuelve un ArrayList con las Tareas que ocurren en este.
    // El ArrayList esta ordenado.
    public ArrayList<Tarea> buscarTareaPorIntervalo(LocalDateTime inicioIntervalo, LocalDateTime finIntervalo) {
        var tareasIntervalo = new ArrayList<Tarea>();
        for (Tarea tarea : listaTareas) {
            if ((tarea.getFin().isAfter(inicioIntervalo) || tarea.getFin().isEqual(inicioIntervalo)) && (tarea.getFin().isBefore(finIntervalo))) {
                tareasIntervalo.add(tarea);
            }
        }
        tareasIntervalo.sort(new Actividad.ComparadorActividad());
        return tareasIntervalo;
    }

    // Dado un intervalo de tiempo devuelve un ArrayList con los Eventos y Tareas que ocurren en este.
    // El ArrayList esta ordenado.
    public ArrayList<Actividad> buscarPorIntervalo(LocalDateTime inicioIntervalo, LocalDateTime finIntervalo) {
        var listaActividades = new ArrayList<Actividad>();

        ArrayList<Evento> eventosIntervalo = buscarEventoPorIntervalo(inicioIntervalo, finIntervalo);
        ArrayList<Tarea> tareasIntervalo = buscarTareaPorIntervalo(inicioIntervalo, finIntervalo);

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
    // instancia de Evento que se guardara en la lista de Eventos. A su vez, guarda las alarmas del Evento en el treeset de Alarmas.
    public void crearEvento(String titulo, String descripcion, boolean diaCompleto, LocalDateTime inicio, LocalDateTime fin, LocalDateTime[] inicioAlarmas, Efecto[] efectoAlarmas, Repeticion repeticion) {
        var nuevoEvento = new Evento(titulo, descripcion, diaCompleto, inicio, fin, repeticion);
        agregarAlarmas(nuevoEvento, inicioAlarmas, efectoAlarmas);
        listaEventos.add(nuevoEvento);
    }

    // Recibe un String que indique su titulo, un String que indique una descripcion, un boolean que indique si es de dia completo
    // un LocalDateTime que indique su fecha y hora de inicio, un LocalDateTime que indique su fecha y hora de fin, un array que
    // indique el intervalo de tiempo previo de sus alarmas , un array de la misma longitud que el anterior que indique el efecto de estas
    // alarmas y una instancia de Repeticion que indique de que modo se repite. A partir de estas caracterisiticas se crea una
    // instancia de Evento que se guardara en la lista de Eventos. A su vez, guarda las alarmas del Evento en el treeset de Alarmas.
    public void crearEvento(String titulo, String descripcion, boolean diaCompleto, LocalDateTime inicio, LocalDateTime fin, Duration[] inicioAlarmas, Efecto[] efectoAlarmas, Repeticion repeticion) {
        var nuevoEvento = new Evento(titulo, descripcion, diaCompleto, inicio, fin, repeticion);
        agregarAlarmas(nuevoEvento, inicioAlarmas, efectoAlarmas);
        listaEventos.add(nuevoEvento);
    }

    // Recibe un String que indique su titulo, un String que indique una descripcion, un boolean que indique si es de dia completo,
    // un LocalDateTime que indique su fecha y hora limite, un array que indique la fecha y hora absoluta de sus alarmas, un array
    // de la misma longitud que el anterior que indique el efecto de estas alarmas. A partir de estas caracterisiticas se crea una
    // instancia de Tarea que se guardara en la lista de Tareas. A su vez, guarda las alarmas de la Tarea en el treeset de Alarmas.
    public void crearTarea(String titulo, String descripcion, boolean diaCompleto, LocalDateTime limite, LocalDateTime[] inicioAlarmas, Efecto[] efectoAlarmas) {
        var nuevaTarea = new Tarea(titulo, descripcion, diaCompleto, limite, limite);
        agregarAlarmas(nuevaTarea, inicioAlarmas, efectoAlarmas);
        listaTareas.add(nuevaTarea);
    }

    // Recibe un String que indique su titulo, un String que indique una descripcion, un boolean que indique si es de dia completo,
    // un LocalDateTime que indique su fecha y hora limite, un array que indique el intervalo de tiempo previo de sus alarmas, un array
    // de la misma longitud que el anterior que indique el efecto de estas alarmas. A partir de estas caracterisiticas se crea una
    // instancia de Tarea que se guardara en la lista de Tareas. A su vez, guarda las alarmas de la Tarea en el treeset de Alarmas.
    public void crearTarea(String titulo, String descripcion, boolean diaCompleto, LocalDateTime limite, Duration[] inicioAlarmas, Efecto[] efectoAlarmas) {
        var nuevaTarea = new Tarea(titulo, descripcion, diaCompleto, limite, limite);
        agregarAlarmas(nuevaTarea, inicioAlarmas, efectoAlarmas);
        listaTareas.add(nuevaTarea);
    }

    // Crea y agrega las alarmas indicadas a un evento o tarea. El inicio de las alarmas lo determina una fecha
    // y hora definida
    private void agregarAlarmas(Actividad nuevaActividad, LocalDateTime[] inicioAlarmas, Efecto[] efectoAlarmas) {
        if (inicioAlarmas.length != 0) {
            for (int i = 0; i < inicioAlarmas.length; i++) {
                var nuevaAlarma = new Alarma(nuevaActividad, inicioAlarmas[i], efectoAlarmas[i]);
                nuevaActividad.agregarAlarma(nuevaAlarma);
                listaAlarmas.add(nuevaAlarma);
            }
        }
    }

    // Crea y agrega las alarmas indicadas a un evento o tarea. El inicio de las alarmas lo determina una intervalo previo
    private void agregarAlarmas(Actividad nuevaActividad, Duration[] inicioAlarmas, Efecto[] efectoAlarmas) {
        if (inicioAlarmas.length != 0) {
            for (int i = 0; i < inicioAlarmas.length; i++) {
                var nuevaAlarma = new Alarma(nuevaActividad, inicioAlarmas[i], efectoAlarmas[i]);
                nuevaActividad.agregarAlarma(nuevaAlarma);
                listaAlarmas.add(nuevaAlarma);
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
        tarea.setFin(nuevoLimite);
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

    // Recibe una instancia de Evento y la quita de la lista de Eventos. A su vez, quita las alarmas del Evento del treeset de Alarmas.
    public void eliminarEvento(Evento evento) {
        for (Alarma alarma : evento.getListaAlarmas()) {
            listaAlarmas.remove(alarma);
        }
        listaEventos.remove(evento);
    }

    // Recibe una instancia de Tarea y la quita de la lista de Tareas. A su vez, quita las alarmas de la Tarea del treeset de Alarmas.
    public void eliminarTarea(Tarea tarea) {
        for (Alarma alarma : tarea.getListaAlarmas()) {
            listaAlarmas.remove(alarma);
        }
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
    // caracteristicas pasadas por parametro. Tambien la agrega al treeset de Alarmas.
    public void agregarAlarma(Actividad actividad, LocalDateTime inicio, Efecto efecto) {
        var nuevaAlarma = new Alarma(actividad, inicio, efecto);
        actividad.agregarAlarma(nuevaAlarma);
        listaAlarmas.add(nuevaAlarma);
    }

    // Recibe una instancia de Evento o Tarea y agrega le agrega una alarma con las
    // caracteristicas pasadas por parametro. Tambien la agrega al treeset de Alarmas.
    public void agregarAlarma(Actividad actividad, Duration inicio, Efecto efecto) {
        var nuevaAlarma = new Alarma(actividad, inicio, efecto);
        actividad.agregarAlarma(nuevaAlarma);
        listaAlarmas.add(nuevaAlarma);
    }

    // Recibe una instancia de Evento o Tarea y modifica la alarma, que tiene las caracteristicas indicadas, con el parametro pasado.
    public void modificarAlarma(Actividad actividad, LocalDateTime inicio, Efecto efecto, LocalDateTime nuevoInicio) {
        Alarma alarma = actividad.buscarAlarma(inicio, efecto);
        actividad.eliminarAlarma(alarma);
        listaAlarmas.remove(alarma);

        this.agregarAlarma(actividad, nuevoInicio, efecto);
    }

    // Recibe una instancia de Evento o Tarea y modifica la alarma, que tiene las caracteristicas indicadas, con el parametro pasado.
    public void modificarAlarma(Actividad actividad, LocalDateTime inicio, Efecto efecto, Duration nuevoInicio) {
        Alarma alarma = actividad.buscarAlarma(inicio, efecto);
        actividad.eliminarAlarma(alarma);
        listaAlarmas.remove(alarma);

        this.agregarAlarma(actividad, nuevoInicio, efecto);
    }

    // Recibe una instancia de Evento o Tarea y modifica la alarma, que tiene las caracteristicas indicadas, con el parametro pasado.
    public void modificarAlarma(Actividad actividad, LocalDateTime inicio, Efecto efecto, Efecto nuevoEfecto) {
        Alarma alarma = actividad.buscarAlarma(inicio, efecto);
        actividad.eliminarAlarma(alarma);
        listaAlarmas.remove(alarma);

        this.agregarAlarma(actividad, inicio, nuevoEfecto);
    }

    // Recibe una instancia de Evento o Tarea y le elimina la alarma que tiene las caracteristicas indicadas.
    // Tambien la elimina del treeset de Alarmas.
    public void eliminarAlarma(Actividad actividad, LocalDateTime inicio, Efecto efecto) {
        Alarma alarma = actividad.buscarAlarma(inicio, efecto);
        actividad.eliminarAlarma(alarma);
        listaAlarmas.remove(alarma);
    }

    // Devuelve la siguiente alarma que deberia sonar. Devuelve null si no hay ninguna alarma.
    public Alarma obtenerProximaAlarma() {
        return listaAlarmas.isEmpty()? null : listaAlarmas.first();
    }

    // Metodo que muestra, en base a una fecha y hora indicada,
    // si debe sonar la proxima alarma.
    public boolean iniciaProximaAlarma (LocalDateTime tiempoActual) {
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
        listaAlarmas.remove(alarmaActual);
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

        var alarmaRepeticion = new Alarma(eventoAlarma, fechaSiguienteAlarma, alarma.getEfecto());

        eventoAlarma.agregarAlarma(alarmaRepeticion);
        listaAlarmas.add(alarmaRepeticion);
    }
}
