import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class CalendarioTest {

    @Test
    public void crearEvento() { // Crea un evento, se le pide al calendario dicho evento y su alarma y se corrobora que sus parametros sean correctos.
        var nuevoCalendario = new Calendario();
        String titulo = "Evento 1";
        String descripcion = "Primer evento a probar";
        LocalDateTime inicio = LocalDateTime.parse("2023-04-03T14:00");
        LocalDateTime fin = LocalDateTime.parse("2023-04-24T17:00");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2023-04-24T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        nuevoCalendario.crearEvento(titulo, descripcion, false, inicio, fin, inicioAlarmas, efectoAlarmas, null);
        ArrayList<Evento> nuevaListaEventos = nuevoCalendario.buscarEvento(titulo, descripcion, inicio, fin);
        Evento eventoBuscado = nuevaListaEventos.get(0);
        Alarma alarmaEvento = eventoBuscado.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);

        assertEquals(titulo, eventoBuscado.getTitulo());
        assertEquals(descripcion, eventoBuscado.getDescripcion());
        assertEquals(inicio, eventoBuscado.getInicio());
        assertEquals(fin, eventoBuscado.getFin());
        assertEquals(inicioAlarmas[0], alarmaEvento.getInicio());
        assertEquals(efectoAlarmas[0], alarmaEvento.getEfecto());
    }

    @Test
    public void modificarEvento() { // Crea 1 evento en el calendario, lo busca y luego lo modifica con nuevos parametros.
        var nuevoCalendario = new Calendario();

        String titulo1 = "Evento 1";
        String descripcion1 = "Primer evento a probar";
        LocalDateTime inicio1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime fin1 = LocalDateTime.parse("2018-10-10T14:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        String titulo2 = "Evento 2";
        String descripcion2 = "Segundo evento a probar";
        LocalDateTime inicio2 = LocalDateTime.parse("2018-10-10T12:00");
        LocalDateTime fin2 = LocalDateTime.parse("2018-12-14T15:00");


        nuevoCalendario.crearEvento(titulo1, descripcion1, false, inicio1, fin1, inicioAlarmas, efectoAlarmas, null);
        ArrayList<Evento> nuevaListaEventos = nuevoCalendario.buscarEvento(titulo1, descripcion1, inicio1, fin1);
        Evento eventoBuscado = nuevaListaEventos.get(0);
        nuevoCalendario.modificar(eventoBuscado, titulo2, descripcion2, inicio2, fin2);


        assertEquals(titulo2, eventoBuscado.getTitulo());
        assertEquals(descripcion2, eventoBuscado.getDescripcion());
        assertEquals(inicio2, eventoBuscado.getInicio());
        assertEquals(fin2, eventoBuscado.getFin());
    }

    @Test
    public void eliminarEvento() { // Crea un evento, lo busca, elimina y luego chequea que no persista en el calendario.
        var nuevoCalendario = new Calendario();
        String titulo = "Evento 3";
        String descripcion = "Tercer evento a probar";
        LocalDateTime inicio = LocalDateTime.parse("2018-04-10T11:25");
        LocalDateTime fin = LocalDateTime.parse("2018-07-10T14:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-07-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        nuevoCalendario.crearEvento(titulo, descripcion, false, inicio, fin, inicioAlarmas, efectoAlarmas, null);
        ArrayList<Evento> nuevaListaEventos1 = nuevoCalendario.buscarEvento(titulo, descripcion, inicio, fin);
        Evento eventoBuscado1 = nuevaListaEventos1.get(0);
        nuevoCalendario.eliminarEvento(eventoBuscado1);
        ArrayList<Evento> listaEventosPostBorrado = nuevoCalendario.buscarEvento(titulo, descripcion, inicio, fin);

        assertEquals(listaEventosPostBorrado.size(), 0);
    }

    @Test
    public void crearTarea() { // Crea una tarea, se le pide al calendario dicho evento y su alarma y se corrobora que sus parametros sean correctos.
        var nuevoCalendario = new Calendario();
        String titulo = "Tarea 1";
        String descripcion = "Primer tarea a probar";
        LocalDateTime limite = LocalDateTime.parse("2023-04-24T17:00");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2023-04-24T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        nuevoCalendario.crearTarea(titulo, descripcion, false, limite, inicioAlarmas, efectoAlarmas);
        ArrayList<Tarea> nuevaListaTareas = nuevoCalendario.buscarTarea(titulo, descripcion, limite);
        Tarea tareaBuscada = nuevaListaTareas.get(0);
        Alarma alarmaTarea = tareaBuscada.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);

        assertEquals(titulo, tareaBuscada.getTitulo());
        assertEquals(descripcion, tareaBuscada.getDescripcion());
        assertEquals(limite, tareaBuscada.getFin());
        assertEquals(inicioAlarmas[0], alarmaTarea.getInicio());
        assertEquals(efectoAlarmas[0], alarmaTarea.getEfecto());
    }

    @Test
    public void modificarTarea() { // Crea una tarea, la busca y modifica en base a nuevos parametros y chequea que sean correctos al finalizar.
        var nuevoCalendario = new Calendario();
        String titulo = "Tarea 1";
        String descripcion = "Primer tarea a probar";
        LocalDateTime limite = LocalDateTime.parse("2023-04-24T17:00");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2023-04-24T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        String titulo2 = "Tarea 2";
        String descripcion2 = "Segundo tarea a probar";
        LocalDateTime limite2 = LocalDateTime.parse("2023-04-24T18:00");

        nuevoCalendario.crearTarea(titulo, descripcion, false, limite, inicioAlarmas, efectoAlarmas);
        ArrayList<Tarea> nuevaListaTareas = nuevoCalendario.buscarTarea(titulo, descripcion, limite);
        Tarea tareaBuscada = nuevaListaTareas.get(0);
        nuevoCalendario.modificar(tareaBuscada, titulo2, descripcion2, limite2);

        assertEquals(titulo2, tareaBuscada.getTitulo());
        assertEquals(descripcion2, tareaBuscada.getDescripcion());
        assertEquals(limite2, tareaBuscada.getFin());
    }

    @Test
    public void eliminarTarea() { // Crea una tarea, la busca, elimina y luego chequea que no persista en el calendario.
        var nuevoCalendario = new Calendario();
        String titulo = "Tarea 3";
        String descripcion = "Tercer tarea a probar";
        LocalDateTime limite = LocalDateTime.parse("2023-04-24T17:00");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-07-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        nuevoCalendario.crearTarea(titulo, descripcion, false, limite, inicioAlarmas, efectoAlarmas);
        ArrayList<Tarea> nuevaListaTareas1 = nuevoCalendario.buscarTarea(titulo, descripcion, limite);
        Tarea tareaBuscada1 = nuevaListaTareas1.get(0);
        nuevoCalendario.eliminarTarea(tareaBuscada1);
        ArrayList<Tarea> listaTareasPostBorrado = nuevoCalendario.buscarTarea(titulo, descripcion, limite);

        assertEquals(listaTareasPostBorrado.size(), 0);
    }

    @Test
    public void crearEventosIdenticos() { // Crea 2 eventos con los mismos parametros, luego los busca en base a dichos parametros y chequea que ambos tengan los mismos parametros.
        var nuevoCalendario = new Calendario();
        String titulo1 = "Evento A";
        String descripcion1 = "Desc. evento A";
        LocalDateTime inicio1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime fin1 = LocalDateTime.parse("2018-10-10T14:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        String titulo2 = "Evento A";
        String descripcion2 = "Desc. evento A";
        LocalDateTime inicio2 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime fin2 = LocalDateTime.parse("2018-10-10T14:25");

        nuevoCalendario.crearEvento(titulo1, descripcion1, false, inicio1, fin1, inicioAlarmas, efectoAlarmas, null);
        nuevoCalendario.crearEvento(titulo2, descripcion2, false, inicio2, fin2, inicioAlarmas, efectoAlarmas, null);
        ArrayList<Evento> nuevaListaEventos1 = nuevoCalendario.buscarEvento(titulo1, descripcion1, inicio1, fin1);
        ArrayList<Evento> nuevaListaEventos2 = nuevoCalendario.buscarEvento(titulo2, descripcion2, inicio2, fin2);
        Evento eventoBuscado1 = nuevaListaEventos1.get(0);
        Evento eventoBuscado2 = nuevaListaEventos2.get(0);
        Alarma alarmaEvento1 = eventoBuscado1.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);
        Alarma alarmaEvento2 = eventoBuscado2.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);

        assertEquals(titulo1, eventoBuscado1.getTitulo());
        assertEquals(descripcion1, eventoBuscado1.getDescripcion());
        assertEquals(inicio1, eventoBuscado1.getInicio());
        assertEquals(fin1, eventoBuscado1.getFin());
        assertEquals(inicioAlarmas[0], alarmaEvento1.getInicio());
        assertEquals(efectoAlarmas[0], alarmaEvento1.getEfecto());

        assertEquals(titulo2, eventoBuscado1.getTitulo());
        assertEquals(descripcion2, eventoBuscado1.getDescripcion());
        assertEquals(inicio2, eventoBuscado1.getInicio());
        assertEquals(fin2, eventoBuscado1.getFin());
        assertEquals(inicioAlarmas[0], alarmaEvento2.getInicio());
        assertEquals(efectoAlarmas[0], alarmaEvento2.getEfecto());
    }

    @Test
    public void crearTareasIdenticas() { // Crea 2 eventos con los mismos parametros, luego los busca en base a dichos parametros y chequea que ambos tengan los mismos parametros.
        var nuevoCalendario = new Calendario();
        String titulo1 = "Tarea A";
        String descripcion1 = "Desc. tarea A";
        LocalDateTime limite1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        String titulo2 = "Tarea A";
        String descripcion2 = "Desc. tarea A";
        LocalDateTime limite2 = LocalDateTime.parse("2018-10-10T11:25");

        nuevoCalendario.crearTarea(titulo1, descripcion1, false, limite1, inicioAlarmas, efectoAlarmas);
        nuevoCalendario.crearTarea(titulo2, descripcion2, false, limite2, inicioAlarmas, efectoAlarmas);
        ArrayList<Tarea> nuevaListaTareas1 = nuevoCalendario.buscarTarea(titulo1, descripcion1, limite1);
        ArrayList<Tarea> nuevaListaTareas2 = nuevoCalendario.buscarTarea(titulo2, descripcion2, limite2);
        Tarea tareaBuscada1 = nuevaListaTareas1.get(0);
        Tarea tareaBuscada2 = nuevaListaTareas2.get(0);
        Alarma alarmaTarea1 = tareaBuscada1.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);
        Alarma alarmaTarea2 = tareaBuscada2.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);

        assertEquals(titulo1, tareaBuscada1.getTitulo());
        assertEquals(descripcion1, tareaBuscada1.getDescripcion());
        assertEquals(limite1, tareaBuscada1.getFin());
        assertEquals(inicioAlarmas[0], alarmaTarea1.getInicio());
        assertEquals(efectoAlarmas[0], alarmaTarea1.getEfecto());

        assertEquals(titulo2, tareaBuscada1.getTitulo());
        assertEquals(descripcion2, tareaBuscada1.getDescripcion());
        assertEquals(limite2, tareaBuscada1.getFin());
        assertEquals(inicioAlarmas[0], alarmaTarea2.getInicio());
        assertEquals(efectoAlarmas[0], alarmaTarea2.getEfecto());
    }

    @Test
    public void crearAlarma(){ // Crea un evento y tarea en el calendario con una alarma dada, luego busca esas actividades y les agrega una alarma nueva a cada uno
        var nuevoCalendario = new Calendario();
        String titulo1 = "Tarea/Evento A";
        String descripcion1 = "Desc tarea/evento A";
        LocalDateTime limite1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime inicio1 = LocalDateTime.parse("2018-10-10T15:25");
        LocalDateTime fin1 = LocalDateTime.parse("2018-10-10T20:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};
        LocalDateTime inicioAlarmasNuevo = LocalDateTime.parse("2018-10-10T20:00");
        Efecto efectoAlarmaNuevo = Efecto.SONIDO;

        nuevoCalendario.crearTarea(titulo1, descripcion1, false, limite1, inicioAlarmas, efectoAlarmas);
        nuevoCalendario.crearEvento(titulo1, descripcion1, false, inicio1, fin1, inicioAlarmas, efectoAlarmas, null);
        ArrayList<Tarea> nuevaListaTarea1 = nuevoCalendario.buscarTarea(titulo1, descripcion1, limite1);
        ArrayList<Evento> nuevaListaEvento1 = nuevoCalendario.buscarEvento(titulo1, descripcion1, inicio1, fin1);

        Tarea tareaBuscada = nuevaListaTarea1.get(0);
        Evento eventoBuscado = nuevaListaEvento1.get(0);

        nuevoCalendario.agregarAlarma(tareaBuscada, inicioAlarmasNuevo, efectoAlarmaNuevo);
        nuevoCalendario.agregarAlarma(eventoBuscado, inicioAlarmasNuevo, efectoAlarmaNuevo);

        Alarma alarmaTareaOriginal = tareaBuscada.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);
        Alarma alarmaTareaNueva = tareaBuscada.buscarAlarma(inicioAlarmasNuevo, efectoAlarmaNuevo);
        Alarma alarmaEventoOriginal = eventoBuscado.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);
        Alarma alarmaEventoNueva = eventoBuscado.buscarAlarma(inicioAlarmasNuevo, efectoAlarmaNuevo);

        assertEquals(inicioAlarmas[0], alarmaTareaOriginal.getInicio());
        assertEquals(efectoAlarmas[0], alarmaTareaOriginal.getEfecto());

        assertEquals(inicioAlarmas[0], alarmaEventoOriginal.getInicio());
        assertEquals(efectoAlarmas[0], alarmaEventoOriginal.getEfecto());

        assertEquals(inicioAlarmasNuevo, alarmaTareaNueva.getInicio());
        assertEquals(efectoAlarmaNuevo, alarmaTareaNueva.getEfecto());

        assertEquals(inicioAlarmasNuevo, alarmaEventoNueva.getInicio());
        assertEquals(efectoAlarmaNuevo, alarmaEventoNueva.getEfecto());
    }

    @Test
    public void modificarAlarma() { // Crea una tarea y la busca en el calendario para luego cambiarle la alarma con nuevos parametros.
        var nuevoCalendario = new Calendario();
        String titulo1 = "Tarea A";
        String descripcion1 = "Desc. tarea A";
        LocalDateTime limite1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};
        LocalDateTime inicioAlarmasNuevo = LocalDateTime.parse("2018-10-10T20:00");
        Efecto efectoAlarmaNuevo = Efecto.SONIDO;

        nuevoCalendario.crearTarea(titulo1, descripcion1, false, limite1, inicioAlarmas, efectoAlarmas);
        ArrayList<Tarea> nuevaListaTarea1 = nuevoCalendario.buscarTarea(titulo1, descripcion1, limite1);
        Tarea tareaBuscada = nuevaListaTarea1.get(0);

        nuevoCalendario.modificarAlarma(tareaBuscada, inicioAlarmas[0], efectoAlarmas[0], inicioAlarmasNuevo);
        nuevoCalendario.modificarAlarma(tareaBuscada, inicioAlarmasNuevo, efectoAlarmas[0], efectoAlarmaNuevo);

        Alarma alarmaTarea1 = tareaBuscada.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);
        Alarma alarmaTarea2 = tareaBuscada.buscarAlarma(inicioAlarmasNuevo, efectoAlarmaNuevo);
        // COMO CAMBIE LA IMPLEMENTACION ESTA PRUEBA SE TUVO QUE MODIFICAR
        assertNull(alarmaTarea1);
        assertEquals(inicioAlarmasNuevo, alarmaTarea2.getInicio());
        assertEquals(efectoAlarmaNuevo, alarmaTarea2.getEfecto());
    }

    @Test
    public void eliminarAlarma() { // Crea una tarea y la busca en el calendario para eliminar su alarma y luego chequear que dicha alarma efectivamente dejo de existir.
        var nuevoCalendario = new Calendario();
        String titulo1 = "Tarea A";
        String descripcion1 = "Desc. tarea A";
        LocalDateTime limite1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        nuevoCalendario.crearTarea(titulo1, descripcion1, false, limite1, inicioAlarmas, efectoAlarmas);
        ArrayList<Tarea> nuevaListaTarea1 = nuevoCalendario.buscarTarea(titulo1, descripcion1, limite1);
        Tarea tareaBuscada = nuevaListaTarea1.get(0);

        nuevoCalendario.eliminarAlarma(tareaBuscada, inicioAlarmas[0], efectoAlarmas[0]);

        Alarma alarmaTareaPostBorrado = tareaBuscada.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);

        assertNull(alarmaTareaPostBorrado);
    }
    @Test
    public void tareaCompletaIncompleta(){ // Crea una tarea y corrobora si su comportamiento de TareaCompleta es el esperado (Inicia en false, es true luego de ser marcada).
        var nuevoCalendario = new Calendario();
        String titulo1 = "Tarea A";
        String descripcion1 = "Desc. tarea A";
        LocalDateTime limite1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        nuevoCalendario.crearTarea(titulo1, descripcion1, false, limite1, inicioAlarmas, efectoAlarmas);
        ArrayList<Tarea> nuevaListaTarea1 = nuevoCalendario.buscarTarea(titulo1, descripcion1, limite1);
        Tarea tareaBuscada = nuevaListaTarea1.get(0);

        assertFalse(nuevoCalendario.tareaEstaCompletada(tareaBuscada));

        nuevoCalendario.marcarTarea(tareaBuscada);

        assertTrue(nuevoCalendario.tareaEstaCompletada(tareaBuscada));
    }

    @Test
    public void crearTareasMasa(){ // Crea 1000 tareas con limites distintos y corrobora que esten en el calendario como corresponde.
        var nuevoCalendario = new Calendario();
        String titulo1 = "Tarea A";
        String descripcion1 = "Desc. tarea A";
        LocalDateTime limite1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        for (int i = 0; i < 1000; i++){
            nuevoCalendario.crearTarea(titulo1, descripcion1, false, limite1.plusMinutes(i), inicioAlarmas, efectoAlarmas);
        }

        ArrayList<Tarea> nuevaListaTareas = nuevoCalendario.buscarTareaPorIntervalo(limite1, limite1.plusMinutes(1000));

        for (int i = 0; i < 1000; i++){
            Tarea tareaActual = nuevaListaTareas.get(i);
            Alarma alarmaActual = tareaActual.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);
            assertEquals(titulo1, tareaActual.getTitulo());
            assertEquals(descripcion1, tareaActual.getDescripcion());
            assertEquals(limite1.plusMinutes(i), tareaActual.getFin());
            assertEquals(inicioAlarmas[0], alarmaActual.getInicio());
            assertEquals(efectoAlarmas[0], alarmaActual.getEfecto());
        }
    }

    @Test
    public void crearEventosMasa(){ // Crea 1000 eventos con inicio y fin diferentes, corroborando que esten agregados de manera correcta al calendario.
        var nuevoCalendario = new Calendario();
        String titulo1 = "Evento A";
        String descripcion1 = "Desc. evento A";
        LocalDateTime inicio1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime fin1 = LocalDateTime.parse("2018-10-10T14:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        for (int i = 0; i < 1000; i++){
            nuevoCalendario.crearEvento(titulo1, descripcion1, false, inicio1.plusMinutes(i), fin1.plusMinutes(i), inicioAlarmas, efectoAlarmas, null);
        }

        ArrayList<Evento> nuevaListaEventos = nuevoCalendario.buscarEventoPorIntervalo(inicio1, fin1.plusMinutes(1000));

        for (int i = 0; i < 1000; i++){
            Evento eventoActual = nuevaListaEventos.get(i);
            Alarma alarmaActual = eventoActual.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);
            assertEquals(titulo1, eventoActual.getTitulo());
            assertEquals(descripcion1, eventoActual.getDescripcion());
            assertEquals(inicio1.plusMinutes(i), eventoActual.getInicio());
            assertEquals(fin1.plusMinutes(i), eventoActual.getFin());
            assertEquals(inicioAlarmas[0], alarmaActual.getInicio());
            assertEquals(efectoAlarmas[0], alarmaActual.getEfecto());
        }
    }

    @Test
    public void modificarTareasMasa(){ // Crea 1000 tareas, las modifica y corrobora que sus parametros sean correctos.
        var nuevoCalendario = new Calendario();
        String titulo1 = "Tarea A";
        String descripcion1 = "Desc. tarea A";
        LocalDateTime limite1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        String titulo2 = "Tarea B";
        String descripcion2 = "Desc. tarea B";
        LocalDateTime limite2 = LocalDateTime.parse("2018-10-10T21:25");

        for (int i = 0; i < 1000; i++){
            nuevoCalendario.crearTarea(titulo1, descripcion1, false, limite1.plusMinutes(i), inicioAlarmas, efectoAlarmas);
        }

        ArrayList<Tarea> nuevaListaTareas = nuevoCalendario.buscarTareaPorIntervalo(limite1, limite1.plusMinutes(1000));

        for (int i = 0; i < 1000; i++){
            Tarea tareaActual = nuevaListaTareas.get(i);
            nuevoCalendario.modificar(tareaActual, titulo2, descripcion2, limite2.plusMinutes(i));
            Alarma alarmaActual = tareaActual.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);
            assertEquals(titulo2, tareaActual.getTitulo());
            assertEquals(descripcion2, tareaActual.getDescripcion());
            assertEquals(limite2.plusMinutes(i), tareaActual.getFin());
            assertEquals(inicioAlarmas[0], alarmaActual.getInicio());
            assertEquals(efectoAlarmas[0], alarmaActual.getEfecto());
        }
    }

    @Test
    public void modificarEventosMasa(){ // Crea 1000 eventos, los modifica y corrobora que sus parametros sean correctos.
        var nuevoCalendario = new Calendario();
        String titulo1 = "Evento A";
        String descripcion1 = "Desc. evento A";
        LocalDateTime inicio1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime fin1 = LocalDateTime.parse("2018-10-10T14:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        String titulo2 = "Evento B";
        String descripcion2 = "Desc. evento B";
        LocalDateTime inicio2 = LocalDateTime.parse("2018-10-10T21:25");
        LocalDateTime fin2 = LocalDateTime.parse("2018-10-10T23:25");

        for (int i = 0; i < 1000; i++){
            nuevoCalendario.crearEvento(titulo1, descripcion1, false, inicio1.plusMinutes(i), fin1.plusMinutes(i), inicioAlarmas, efectoAlarmas, null);
        }

        ArrayList<Evento> nuevaListaEventos = nuevoCalendario.buscarEventoPorIntervalo(inicio1, fin1.plusMinutes(1000));

        for (int i = 0; i < 1000; i++){
            Evento eventoActual = nuevaListaEventos.get(i);
            nuevoCalendario.modificar(eventoActual, titulo2, descripcion2, inicio2.plusMinutes(i), fin2.plusMinutes(i));
            Alarma alarmaActual = eventoActual.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);
            assertEquals(titulo2, eventoActual.getTitulo());
            assertEquals(descripcion2, eventoActual.getDescripcion());
            assertEquals(inicio2.plusMinutes(i), eventoActual.getInicio());
            assertEquals(fin2.plusMinutes(i), eventoActual.getFin());
            assertEquals(inicioAlarmas[0], alarmaActual.getInicio());
            assertEquals(efectoAlarmas[0], alarmaActual.getEfecto());
        }
    }

    @Test
    public void eliminarTareasMasa(){ // Crea 1000 tareas, luego las elimina y luego chequea que efectivamente el calendario no tiene ninguna de esas 1000 tareas al final.
        var nuevoCalendario = new Calendario();
        String titulo1 = "Tarea A";
        String descripcion1 = "Desc. tarea A";
        LocalDateTime limite1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        for (int i = 0; i < 1000; i++){
            nuevoCalendario.crearTarea(titulo1, descripcion1, false, limite1.plusMinutes(i), inicioAlarmas, efectoAlarmas);
        }

        ArrayList<Tarea> nuevaListaTareas = nuevoCalendario.buscarTareaPorIntervalo(limite1, limite1.plusMinutes(1000));

        for (int i = 0; i < 1000; i++){
            Tarea tareaActual = nuevaListaTareas.get(i);
            nuevoCalendario.eliminarAlarma(tareaActual, inicioAlarmas[0], efectoAlarmas[0]);
            nuevoCalendario.eliminarTarea(tareaActual);
        }

        ArrayList<Tarea> listaTareasPostBorrado = nuevoCalendario.buscarTareaPorIntervalo(limite1, limite1.plusMinutes(1000));

        for (int i = 0; i < 1000; i++){
            assertEquals(listaTareasPostBorrado.size(), 0);
        }
    }

    @Test
    public void eliminarEventosMasa(){ // Crea 1000 eventos, los elimina y luego chequea que el calendario no tiene ninguno de esos 1000 eventos al final.
        var nuevoCalendario = new Calendario();
        String titulo1 = "Evento A";
        String descripcion1 = "Desc. evento A";
        LocalDateTime inicio1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime fin1 = LocalDateTime.parse("2018-10-10T14:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        for (int i = 0; i < 1000; i++){
            nuevoCalendario.crearEvento(titulo1, descripcion1, false, inicio1.plusMinutes(i), fin1.plusMinutes(i), inicioAlarmas, efectoAlarmas, null);
        }

        ArrayList<Evento> nuevaListaEventos = nuevoCalendario.buscarEventoPorIntervalo(inicio1, fin1.plusMinutes(1000));

        for (int i = 0; i < 1000; i++){
            Evento eventoActual = nuevaListaEventos.get(i);
            nuevoCalendario.eliminarAlarma(eventoActual, inicioAlarmas[0], efectoAlarmas[0]);
            nuevoCalendario.eliminarEvento(eventoActual);
        }

        ArrayList<Evento> listaEventosPostBorrado = nuevoCalendario.buscarEventoPorIntervalo(inicio1, fin1.plusMinutes(1000));

        for (int i = 0; i < 1000; i++){
            assertEquals(listaEventosPostBorrado.size(), 0);
        }
    }

    @Test
    public void testProximaDisparoAlarma(){ // Chequea manualmente con un evento y una tarea que las alarmas se dispararian en el orden esperado.
        var nuevoCalendario = new Calendario();
        String titulo1 = "Evento A";
        String descripcion1 = "Desc. evento A";
        LocalDateTime inicio1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime fin1 = LocalDateTime.parse("2018-10-10T14:25");
        LocalDateTime[] inicioAlarmasEvento = {LocalDateTime.parse("2018-10-10T14:00")};

        String titulo2 = "Tarea A";
        String descripcion2 = "Desc. Tarea A";
        LocalDateTime limite1 = LocalDateTime.parse("2018-10-10T18:25");
        LocalDateTime[] inicioAlarmasTarea = {LocalDateTime.parse("2018-10-10T14:30")};

        LocalDateTime inicioAlarmaTarea2 = LocalDateTime.parse("2018-10-10T15:30");

        LocalDateTime inicioAlarmaEvento2 = LocalDateTime.parse("2018-10-10T15:00");
        LocalDateTime inicioAlarmaEvento3 = LocalDateTime.parse("2018-10-10T16:00");

        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        nuevoCalendario.setTiempoActual(LocalDateTime.parse("2018-10-10T12:00")); // Tiempo inicial del calendario.

        nuevoCalendario.crearEvento(titulo1, descripcion1, false, inicio1, fin1, inicioAlarmasEvento, efectoAlarmas, null); // Evento creado con su primera alarma.
        ArrayList<Evento> nuevaListaEventos = nuevoCalendario.buscarEvento(titulo1, descripcion1, inicio1, fin1);
        Evento eventoBuscado = nuevaListaEventos.get(0);

        nuevoCalendario.agregarAlarma(eventoBuscado, inicioAlarmaEvento2, efectoAlarmas[0]); // Se agrega alarma a evento con tiempo = inicioAlarmaEvento2
        nuevoCalendario.agregarAlarma(eventoBuscado, inicioAlarmaEvento3, efectoAlarmas[0]); // Se agrega alarma a evento con tiempo = inicioAlarmaEvento3

        nuevoCalendario.crearTarea(titulo2, descripcion2, false, limite1, inicioAlarmasTarea, efectoAlarmas); // Tarea creada con su alarma original
        ArrayList<Tarea> nuevaListaTareas = nuevoCalendario.buscarTarea(titulo2, descripcion2, limite1);
        Tarea tareaBuscada = nuevaListaTareas.get(0);

        nuevoCalendario.agregarAlarma(tareaBuscada, inicioAlarmaTarea2, efectoAlarmas[0]); // Se agrega alarma a tarea con tiempo = inicioAlarmaTarea2

        Alarma alarmaEvento1 = eventoBuscado.buscarAlarma(inicioAlarmasEvento[0], efectoAlarmas[0]);
        Alarma alarmaEvento2 = eventoBuscado.buscarAlarma(inicioAlarmaEvento2, efectoAlarmas[0]);
        Alarma alarmaEvento3 = eventoBuscado.buscarAlarma(inicioAlarmaEvento3, efectoAlarmas[0]);

        Alarma alarmaTarea1 = tareaBuscada.buscarAlarma(inicioAlarmasTarea[0], efectoAlarmas[0]);
        Alarma alarmaTarea2 = tareaBuscada.buscarAlarma(inicioAlarmaTarea2, efectoAlarmas[0]); // Alarmas buscadas y traidas a variables para comprobar

        assertEquals(alarmaEvento1, nuevoCalendario.obtenerProximaAlarma());
        assertEquals(alarmaEvento1, nuevoCalendario.obtenerProximaAlarma()); // A proposito, chequea que la proxima alarma siga siendo la misma a pesar de que no cambio nada.

        assertFalse(nuevoCalendario.iniciaProximaAlarma());

        assertEquals(nuevoCalendario.dispararProximaAlarma(), eventoBuscado); // Primera alarma es removida al ejecutar .dispararProximaAlarma()

        assertEquals(alarmaTarea1, nuevoCalendario.obtenerProximaAlarma());
        assertEquals(alarmaTarea1, nuevoCalendario.obtenerProximaAlarma());

        assertFalse(nuevoCalendario.iniciaProximaAlarma());

        assertEquals(nuevoCalendario.dispararProximaAlarma(), tareaBuscada); // Segunda alarma removida.

        assertEquals(alarmaEvento2, nuevoCalendario.obtenerProximaAlarma());
        assertEquals(alarmaEvento2, nuevoCalendario.obtenerProximaAlarma());

        assertFalse(nuevoCalendario.iniciaProximaAlarma());

        assertEquals(nuevoCalendario.dispararProximaAlarma(), eventoBuscado); // Tercera removida.

        assertEquals(alarmaTarea2, nuevoCalendario.obtenerProximaAlarma());
        assertEquals(alarmaTarea2, nuevoCalendario.obtenerProximaAlarma());

        assertFalse(nuevoCalendario.iniciaProximaAlarma());

        assertEquals(nuevoCalendario.dispararProximaAlarma(), tareaBuscada); // Cuarta removida.

        nuevoCalendario.setTiempoActual(LocalDateTime.parse("2018-10-10T16:00")); // Fijamos el tiempo del calendario a la quinta alarma para chequear que de true

        assertEquals(alarmaEvento3, nuevoCalendario.obtenerProximaAlarma());
        assertEquals(alarmaEvento3, nuevoCalendario.obtenerProximaAlarma());

        assertTrue(nuevoCalendario.iniciaProximaAlarma()); // Chequeo de que el tiempo del calendario y la alarma que viene coinciden

        assertEquals(nuevoCalendario.dispararProximaAlarma(), eventoBuscado); // Quinta y ultima alarma removida.

        assertNull(eventoBuscado.buscarAlarma(inicioAlarmaEvento2, efectoAlarmas[0]));
        assertNull(tareaBuscada.buscarAlarma(inicioAlarmaTarea2, efectoAlarmas[0]));
    }

    @Test
    public void testBusquedaActividadesMixtasConReps(){ // Crea un evento que se repite y una tarea. Se usa el metodo de buscarActividadesPorIntervalo
        //y se corrobora que dicha busqueda de intervalo devuelve la lista de actividades ordenada de manera ascendente segun las fechas.

        // Arrange
        var nuevoCalendario = new Calendario();
        String titulo1 = "Algoritmos 3";
        String descripcion1 = "Clase de Algo3";
        LocalDateTime inicio1 = LocalDateTime.parse("2023-03-13T14:00");
        LocalDateTime fin1 = LocalDateTime.parse("2023-03-13T17:00");
        LocalDateTime[] inicioAlarmasEvento = {LocalDateTime.parse("2023-03-13T13:00")};

        Repeticion repeticionEvento = new RepeticionSemanalDias(inicio1, LocalDateTime.parse("2023-06-29T17:00"), new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.THURSDAY}); // Se repite todos los Lunes y Jueves

        String titulo2 = "Entrega TP";
        String descripcion2 = "Fecha limite entrega TP Etapa 1";
        LocalDateTime limite1 = LocalDateTime.parse("2023-04-24T23:59");
        LocalDateTime[] inicioAlarmasTarea = {LocalDateTime.parse("2023-04-24T22:00")};

        LocalDateTime inicioIntervaloBuscar = LocalDateTime.parse("2023-04-24T00:00"); // Inicio intervalo a buscar
        LocalDateTime finIntervaloBuscar = LocalDateTime.parse("2023-05-07T00:00"); // Fin intervalo a buscar

        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        // Act
        nuevoCalendario.crearTarea(titulo2, descripcion2, false, limite1, inicioAlarmasTarea, efectoAlarmas);

        nuevoCalendario.crearEvento(titulo1, descripcion1, false, inicio1, fin1, inicioAlarmasEvento, efectoAlarmas, repeticionEvento);

        ArrayList<Actividad> nuevaListaActividades = nuevoCalendario.buscarPorIntervalo(inicioIntervaloBuscar, finIntervaloBuscar); // Intervalo buscado

        assertEquals(nuevaListaActividades.size(), 5); // Deberia tener 5 actividades dentro de este intervalo

        Evento eventoPrimera = (Evento) nuevaListaActividades.get(0);
        Tarea tareaPrimera = (Tarea) nuevaListaActividades.get(1);
        Evento eventoSegunda = (Evento) nuevaListaActividades.get(2);
        Evento eventoTercera = (Evento) nuevaListaActividades.get(3);
        Evento eventoCuarta = (Evento) nuevaListaActividades.get(4);

        // Assert

        assertEquals(eventoPrimera.getTitulo(), titulo1);
        assertEquals(eventoPrimera.getDescripcion(), descripcion1);
        assertEquals(eventoPrimera.getInicio(), LocalDateTime.parse("2023-04-24T14:00"));
        assertEquals(eventoPrimera.getFin(), LocalDateTime.parse("2023-04-24T17:00"));

        assertEquals(tareaPrimera.getTitulo(), titulo2);
        assertEquals(tareaPrimera.getDescripcion(), descripcion2);
        assertEquals(tareaPrimera.getFin(), limite1);

        assertEquals(eventoSegunda.getTitulo(), titulo1);
        assertEquals(eventoSegunda.getDescripcion(), descripcion1);
        assertEquals(eventoSegunda.getInicio(), LocalDateTime.parse("2023-04-27T14:00"));
        assertEquals(eventoSegunda.getFin(), LocalDateTime.parse("2023-04-27T17:00"));

        assertEquals(eventoTercera.getTitulo(), titulo1);
        assertEquals(eventoTercera.getDescripcion(), descripcion1);
        assertEquals(eventoTercera.getInicio(), LocalDateTime.parse("2023-05-01T14:00"));
        assertEquals(eventoTercera.getFin(), LocalDateTime.parse("2023-05-01T17:00"));

        assertEquals(eventoCuarta.getTitulo(), titulo1);
        assertEquals(eventoCuarta.getDescripcion(), descripcion1);
        assertEquals(eventoCuarta.getInicio(), LocalDateTime.parse("2023-05-04T14:00"));
        assertEquals(eventoCuarta.getFin(), LocalDateTime.parse("2023-05-04T17:00"));
    }

    @Test
    public void testBusquedaMezclada(){ // Crea 2 tareas y 2 eventos en el calendario (1 evento se repite, el otro no) y los busca con los metodos
        // buscarTareaPorIntervalo y buscarEventoPorIntervalo respectivamente, chequeando que devuelven las listas esperadas.
        var nuevoCalendario = new Calendario();
        String tituloEvento1 = "Algoritmos 3";
        String descripcionEvento1 = "Clase de Algo3";
        LocalDateTime inicio1 = LocalDateTime.parse("2023-03-13T14:00");
        LocalDateTime fin1 = LocalDateTime.parse("2023-03-13T17:00");
        LocalDateTime[] inicioAlarmasEvento = {LocalDateTime.parse("2023-03-13T13:00")};

        Repeticion repeticionEvento = new RepeticionSemanalDias(inicio1, LocalDateTime.parse("2023-06-29T17:00"), new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.THURSDAY});

        String tituloEvento2 = "Torneo Local";
        String descripcionEvento2 = "Cosas";
        LocalDateTime inicio2 = LocalDateTime.parse("2023-04-29T12:00");
        LocalDateTime fin2 = LocalDateTime.parse("2023-04-29T18:00");

        String tituloTarea1 = "Entrega TP";
        String descripcionTarea1 = "Fecha limite entrega TP Etapa 1";
        LocalDateTime limite1 = LocalDateTime.parse("2023-04-24T23:59");
        LocalDateTime[] inicioAlarmasTarea = {LocalDateTime.parse("2023-04-24T22:00")};

        String tituloTarea2 = "Entrega Informe";
        String descripcionTarea2 = "Informe de trabajo";
        LocalDateTime limite2 = LocalDateTime.parse("2023-04-26T23:59");

        LocalDateTime inicioIntervaloBuscar = LocalDateTime.parse("2023-04-24T00:00");
        LocalDateTime finIntervaloBuscar = LocalDateTime.parse("2023-05-02T00:00");

        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        nuevoCalendario.crearTarea(tituloTarea2, descripcionTarea2, false, limite2, inicioAlarmasTarea, efectoAlarmas);
        nuevoCalendario.crearEvento(tituloEvento2, descripcionEvento2, false, inicio2, fin2, inicioAlarmasEvento, efectoAlarmas, null);
        nuevoCalendario.crearTarea(tituloTarea1, descripcionTarea1, false, limite1, inicioAlarmasTarea, efectoAlarmas);
        nuevoCalendario.crearEvento(tituloEvento1, descripcionEvento1, false, inicio1, fin1, inicioAlarmasEvento, efectoAlarmas, repeticionEvento);

        ArrayList<Evento> listaEventos = nuevoCalendario.buscarEventoPorIntervalo(inicioIntervaloBuscar, finIntervaloBuscar);
        ArrayList<Tarea> listaTareas = nuevoCalendario.buscarTareaPorIntervalo(inicioIntervaloBuscar, finIntervaloBuscar);
        // Ayuda memoria, eventos: evento1, evento1, evento2, evento1
        // tareas: tarea1, tarea2

        assertEquals(listaEventos.size(), 4); // Se esperan 4 eventos dentro del intervalo dado, debido al evento repetido
        assertEquals(listaTareas.size(), 2); // Se esperan 2 tareas dentro del intervalo dado

        Evento primerEventoLista = listaEventos.get(0);
        Evento segundoEventoLista = listaEventos.get(1);
        Evento tercerEventoLista = listaEventos.get(2);
        Evento cuartoEventoLista = listaEventos.get(3);

        Tarea primerTareaLista = listaTareas.get(0);
        Tarea segundaTareaLista = listaTareas.get(1);

        assertEquals(primerEventoLista.getTitulo(), tituloEvento1);
        assertEquals(primerEventoLista.getDescripcion(), descripcionEvento1);
        assertEquals(primerEventoLista.getInicio(), LocalDateTime.parse("2023-04-24T14:00"));
        assertEquals(primerEventoLista.getFin(), LocalDateTime.parse("2023-04-24T17:00"));

        assertEquals(segundoEventoLista.getTitulo(), tituloEvento1);
        assertEquals(segundoEventoLista.getDescripcion(), descripcionEvento1);
        assertEquals(segundoEventoLista.getInicio(), LocalDateTime.parse("2023-04-27T14:00"));
        assertEquals(segundoEventoLista.getFin(), LocalDateTime.parse("2023-04-27T17:00"));

        assertEquals(tercerEventoLista.getTitulo(), tituloEvento2);
        assertEquals(tercerEventoLista.getDescripcion(), descripcionEvento2);
        assertEquals(tercerEventoLista.getInicio(), LocalDateTime.parse("2023-04-29T12:00"));
        assertEquals(tercerEventoLista.getFin(), LocalDateTime.parse("2023-04-29T18:00"));

        assertEquals(cuartoEventoLista.getTitulo(), tituloEvento1);
        assertEquals(cuartoEventoLista.getDescripcion(), descripcionEvento1);
        assertEquals(cuartoEventoLista.getInicio(), LocalDateTime.parse("2023-05-01T14:00"));
        assertEquals(cuartoEventoLista.getFin(), LocalDateTime.parse("2023-05-01T17:00"));

        assertEquals(primerTareaLista.getTitulo(), tituloTarea1);
        assertEquals(primerTareaLista.getDescripcion(), descripcionTarea1);
        assertEquals(primerTareaLista.getFin(), limite1);

        assertEquals(segundaTareaLista.getTitulo(), tituloTarea2);
        assertEquals(segundaTareaLista.getDescripcion(), descripcionTarea2);
        assertEquals(segundaTareaLista.getFin(), limite2);
    }

    @Test
    public void testEventoDiaCompleto(){ // Crea un evento con un dado inicio y fin pero indicando que es de dia completo y chequea que sus valores de inicio y fin son los
        // que corresponden al ser marcado como dia completo
        var nuevoCalendario = new Calendario();
        String tituloEvento1 = "Algoritmos 3";
        String descripcionEvento1 = "Clase de Algo3";
        LocalDateTime inicio1 = LocalDateTime.parse("2023-03-13T14:00");
        LocalDateTime fin1 = LocalDateTime.parse("2023-03-16T17:00");
        LocalDateTime[] inicioAlarmasEvento = {LocalDateTime.parse("2023-03-13T13:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        nuevoCalendario.crearEvento(tituloEvento1, descripcionEvento1, true, inicio1, fin1, inicioAlarmasEvento, efectoAlarmas, null);
        ArrayList<Evento> listaEventos = nuevoCalendario.buscarEventoPorIntervalo(LocalDateTime.parse("2023-03-11T12:00"), LocalDateTime.parse("2023-03-20T12:00"));
        Evento eventoBuscado = listaEventos.get(0);

        assertEquals(eventoBuscado.getInicio(), LocalDateTime.parse("2023-03-13T00:00"));
        assertEquals(eventoBuscado.getFin(), LocalDateTime.parse("2023-03-17T00:00"));
    }

    @Test
    public void testEventoConvertidoDiaCompleto(){ // Crea un evento con un dado inicio y fin, marcada como NO de dia completo, y luego se la modifica para ser de dia
        // completo, chequeando que su inicio y fin son los que corresponden luego de ser marcada como de dia completo
        var nuevoCalendario = new Calendario();
        String tituloEvento1 = "Algoritmos 3";
        String descripcionEvento1 = "Clase de Algo3";
        LocalDateTime inicio1 = LocalDateTime.parse("2023-03-13T14:00");
        LocalDateTime fin1 = LocalDateTime.parse("2023-03-16T17:00");
        LocalDateTime[] inicioAlarmasEvento = {LocalDateTime.parse("2023-03-13T13:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        nuevoCalendario.crearEvento(tituloEvento1, descripcionEvento1, false, inicio1, fin1, inicioAlarmasEvento, efectoAlarmas, null);
        ArrayList<Evento> listaEventos = nuevoCalendario.buscarEventoPorIntervalo(LocalDateTime.parse("2023-03-11T12:00"), LocalDateTime.parse("2023-03-20T12:00"));
        Evento eventoBuscado = listaEventos.get(0);
        nuevoCalendario.modificar(eventoBuscado, true);

        assertEquals(eventoBuscado.getInicio(), LocalDateTime.parse("2023-03-13T00:00"));
        assertEquals(eventoBuscado.getFin(), LocalDateTime.parse("2023-03-17T00:00"));
    }

    @Test
    public void testTareaDiaCompleto(){ // Crea una tarea con un limite dado y marcada como de dia completo, chequeando que su limite es el que corresponde si es de dia
        // completo
        var nuevoCalendario = new Calendario();
        String tituloTarea1 = "Algoritmos 3";
        String descripcionTarea1 = "Clase de Algo3";
        LocalDateTime limite1 = LocalDateTime.parse("2023-03-13T14:00");
        LocalDateTime[] inicioAlarmasTarea = {LocalDateTime.parse("2023-03-13T13:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        nuevoCalendario.crearTarea(tituloTarea1, descripcionTarea1, true, limite1, inicioAlarmasTarea, efectoAlarmas);
        ArrayList<Tarea> listaTareas = nuevoCalendario.buscarTareaPorIntervalo(LocalDateTime.parse("2023-03-11T12:00"), LocalDateTime.parse("2023-03-20T12:00"));
        Tarea tareaBuscada = listaTareas.get(0);

        assertEquals(tareaBuscada.getFin(), LocalDateTime.parse("2023-03-14T00:00"));
    }

    @Test
    public void testTareaConvertidaDiaCompleto(){ // Crea una tarea con un limite dado y marcada como NO de dia completo y luego se la modifica para que sea de dia completo
        // chequeando que su limite es el que corresponde para una tarea de dia completo
        var nuevoCalendario = new Calendario();
        String tituloTarea1 = "Algoritmos 3";
        String descripcionTarea1 = "Clase de Algo3";
        LocalDateTime limite1 = LocalDateTime.parse("2023-03-13T14:00");
        LocalDateTime[] inicioAlarmasTarea = {LocalDateTime.parse("2023-03-13T13:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        nuevoCalendario.crearTarea(tituloTarea1, descripcionTarea1, false, limite1, inicioAlarmasTarea, efectoAlarmas);
        ArrayList<Tarea> listaTareas = nuevoCalendario.buscarTareaPorIntervalo(LocalDateTime.parse("2023-03-11T12:00"), LocalDateTime.parse("2023-03-20T12:00"));
        Tarea tareaBuscada = listaTareas.get(0);
        nuevoCalendario.modificar(tareaBuscada, true);

        assertEquals(tareaBuscada.getFin(), LocalDateTime.parse("2023-03-14T00:00"));
    }

    /*
    @Test
    public void testAlarmasEventosRepetidos() {
        // Arrange

        var nuevoCalendario = new Calendario();
        nuevoCalendario.setTiempoActual(LocalDateTime.parse("2023-04-21T00:00"));

        String tituloEvento1 = "Estructuras y Organizaciones";
        String descripcionEvento1 = "Clase de EyO";
        LocalDateTime inicio1 = LocalDateTime.parse("2023-04-21T18:00");
        LocalDateTime fin1 = LocalDateTime.parse("2023-04-21T22:00");
        LocalDateTime[] inicioAlarmasEvento = {LocalDateTime.parse("2023-04-21T16:00"), LocalDateTime.parse("2023-04-21T17:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION, Efecto.SONIDO};
        var repeticion1 = new RepeticionComun(inicio1, 3, Frecuencia.SEMANAL);

        var inicioEsperado1 = LocalDateTime.parse("2023-04-21T16:00");
        var inicioEsperado2 = LocalDateTime.parse("2023-04-21T17:00");
        var inicioEsperado3 = LocalDateTime.parse("2023-04-28T16:00");
        var inicioEsperado4 = LocalDateTime.parse("2023-04-28T17:00");
        var inicioEsperado5 = LocalDateTime.parse("2023-05-05T16:00");
        var inicioEsperado6 = LocalDateTime.parse("2023-05-05T17:00");

        var inicioEventoEsp1 = LocalDateTime.parse("2023-04-21T18:00");
        var inicioEventoEsp2 = LocalDateTime.parse("2023-04-28T18:00");
        var inicioEventoEsp3 = LocalDateTime.parse("2023-05-05T18:00");

        var finEventoEsp1 = LocalDateTime.parse("2023-04-21T22:00");
        var finEventoEsp2 = LocalDateTime.parse("2023-04-28T22:00");
        var finEventoEsp3 = LocalDateTime.parse("2023-05-05T22:00");

        // Act

        nuevoCalendario.crearEvento(tituloEvento1, descripcionEvento1, false, inicio1, fin1, inicioAlarmasEvento, efectoAlarmas, repeticion1);

        // Primera ocurrencia
        Alarma alarma1 = nuevoCalendario.obtenerProximaAlarma();
        Actividad evento1 = nuevoCalendario.dispararProximaAlarma();

        assertEquals(inicioEventoEsp1, evento1.getInicio());
        assertEquals(finEventoEsp1, evento1.getFin());

        Alarma alarma2 = nuevoCalendario.obtenerProximaAlarma();
        Actividad evento2 = nuevoCalendario.dispararProximaAlarma();
        assertEquals(inicioEventoEsp1, evento2.getInicio());
        assertEquals(finEventoEsp1, evento2.getFin());

        nuevoCalendario.setTiempoActual(LocalDateTime.parse("2023-04-21T22:01"));

        nuevoCalendario.actualizarEventosRepetidos();

        // Segunda ocurrencia
        Alarma alarma3 = nuevoCalendario.obtenerProximaAlarma();
        Actividad evento3 = nuevoCalendario.dispararProximaAlarma();

        assertEquals(inicioEventoEsp2, evento3.getInicio());
        assertEquals(finEventoEsp2, evento3.getFin());

        Alarma alarma4 = nuevoCalendario.obtenerProximaAlarma();
        Actividad evento4 = nuevoCalendario.dispararProximaAlarma();

        assertEquals(inicioEventoEsp2, evento4.getInicio());
        assertEquals(finEventoEsp2, evento4.getFin());

        nuevoCalendario.setTiempoActual(LocalDateTime.parse("2023-04-28T22:01"));

        nuevoCalendario.actualizarEventosRepetidos();

        // Tercera ocurrencia
        Alarma alarma5 = nuevoCalendario.obtenerProximaAlarma();
        Actividad evento5 = nuevoCalendario.dispararProximaAlarma();

        assertEquals(inicioEventoEsp3, evento5.getInicio());
        assertEquals(finEventoEsp3, evento5.getFin());

        Alarma alarma6 = nuevoCalendario.obtenerProximaAlarma();
        Actividad evento6 = nuevoCalendario.dispararProximaAlarma();

        assertEquals(inicioEventoEsp3, evento6.getInicio());
        assertEquals(finEventoEsp3, evento6.getFin());

        nuevoCalendario.setTiempoActual(LocalDateTime.parse("2023-05-05T22:01"));

        nuevoCalendario.actualizarEventosRepetidos();

        // Chequeo que ya no haya alarmas
        Alarma alarma7 = nuevoCalendario.obtenerProximaAlarma();

        // Assert

        assertEquals(inicioEsperado1, alarma1.getInicio());
        assertEquals(Efecto.NOTIFICACION, alarma1.getEfecto());

        assertEquals(inicioEsperado2, alarma2.getInicio());
        assertEquals(Efecto.SONIDO, alarma2.getEfecto());

        assertEquals(inicioEsperado3, alarma3.getInicio());
        assertEquals(Efecto.NOTIFICACION, alarma3.getEfecto());

        assertEquals(inicioEsperado4, alarma4.getInicio());
        assertEquals(Efecto.SONIDO, alarma4.getEfecto());

        assertEquals(inicioEsperado5, alarma5.getInicio());
        assertEquals(Efecto.NOTIFICACION, alarma5.getEfecto());

        assertEquals(inicioEsperado6, alarma6.getInicio());
        assertEquals(Efecto.SONIDO, alarma6.getEfecto());

        assertNull(alarma7);
    }

    @Test
    public void name() {
        var Calendario = new Calendario();

        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2020-04-24T11:00"), LocalDateTime.parse("2020-04-24T09:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION, Efecto.MAIL};

        Calendario.crearTarea("HOLA", "SOY RUSELL", false, LocalDateTime.of(2020, 4,24, 12,0), inicioAlarmas, efectoAlarmas);

        System.out.println(Calendario.obtenerProximaAlarma());

        ArrayList<Tarea> t = Calendario.buscarTarea("HOLA", "SOY RUSELL", LocalDateTime.of(2020, 4,24, 12,0));
        Tarea t1 = t.get(0);
        Alarma a = t1.getListaAlarmas().get(0);
        //a.setInicio(LocalDateTime.parse("2020-04-24T07:00"));

        //t1.modificarAlarma(a, LocalDateTime.parse("2020-04-24T07:00"));
        System.out.println(t1.getListaAlarmas());
        Calendario.modificarAlarma(t1, a.getInicio(), a.getEfecto(), LocalDateTime.parse("2020-04-24T07:00"));
        System.out.println(t1.getListaAlarmas());
        System.out.println(Calendario.obtenerProximaAlarma());
    }*/
}