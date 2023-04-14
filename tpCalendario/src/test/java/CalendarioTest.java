import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class CalendarioTest {

    @Test
    public void crearEvento() {
        var nuevoCalendario = new Calendario();
        String titulo = "Evento 1";
        String descripcion = "Primer evento a probar";
        LocalDateTime inicio = LocalDateTime.parse("2023-04-03T14:00");
        LocalDateTime fin = LocalDateTime.parse("2023-04-24T17:00");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2023-04-24T14:00")};
        Integer[] efectoAlarmas = {0};

        nuevoCalendario.crearEvento(titulo, descripcion, false, inicio, fin, inicioAlarmas, efectoAlarmas);
        Evento nuevoEvento = nuevoCalendario.buscarEvento(titulo, descripcion, inicio, fin);
        Alarma alarmaEvento = nuevoEvento.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);

        assertEquals(titulo, nuevoEvento.getTitulo());
        assertEquals(descripcion, nuevoEvento.getDescripcion());
        assertEquals(inicio, nuevoEvento.getInicio());
        assertEquals(fin, nuevoEvento.getFin());
        assertEquals(inicioAlarmas[0], alarmaEvento.getInicio());
        assertEquals(efectoAlarmas[0], alarmaEvento.getEfecto());
    }

    @Test
    public void modificarEvento() {
        var nuevoCalendario = new Calendario();

        String titulo1 = "Evento 1";
        String descripcion1 = "Primer evento a probar";
        LocalDateTime inicio1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime fin1 = LocalDateTime.parse("2018-10-10T14:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Integer[] efectoAlarmas = {0};

        String titulo2 = "Evento 2";
        String descripcion2 = "Segundo evento a probar";
        LocalDateTime inicio2 = LocalDateTime.parse("2018-10-10T12:00");
        LocalDateTime fin2 = LocalDateTime.parse("2018-12-14T15:00");


        nuevoCalendario.crearEvento(titulo1, descripcion1, false, inicio1, fin1, inicioAlarmas, efectoAlarmas);
        Evento nuevoEvento = nuevoCalendario.buscarEvento(titulo1, descripcion1, inicio1, fin1);
        nuevoCalendario.modificarEvento(nuevoEvento, titulo2, descripcion2, inicio2, fin2);


        assertEquals(titulo2, nuevoEvento.getTitulo());
        assertEquals(descripcion2, nuevoEvento.getDescripcion());
        assertEquals(inicio2, nuevoEvento.getInicio());
        assertEquals(fin2, nuevoEvento.getFin());
    }

    @Test
    public void eliminarEvento() {
        var nuevoCalendario = new Calendario();
        String titulo = "Evento 3";
        String descripcion = "Tercer evento a probar";
        LocalDateTime inicio = LocalDateTime.parse("2018-04-10T11:25");
        LocalDateTime fin = LocalDateTime.parse("2018-07-10T14:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-07-10T14:00")};
        Integer[] efectoAlarmas = {0};

        nuevoCalendario.crearEvento(titulo, descripcion, false, inicio, fin, inicioAlarmas, efectoAlarmas);
        Evento nuevoEvento1 = nuevoCalendario.buscarEvento(titulo, descripcion, inicio, fin);
        nuevoCalendario.eliminarEvento(nuevoEvento1);
        Evento nuevoEvento2 = nuevoCalendario.buscarEvento(titulo, descripcion, inicio, fin);

        assertNull(nuevoEvento2);
    }

    @Test
    public void crearTarea() {
        var nuevoCalendario = new Calendario();
        String titulo = "Tarea 1";
        String descripcion = "Primer tarea a probar";
        LocalDateTime limite = LocalDateTime.parse("2023-04-24T17:00");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2023-04-24T14:00")};
        Integer[] efectoAlarmas = {0};

        nuevoCalendario.crearTarea(titulo, descripcion, false, limite, inicioAlarmas, efectoAlarmas);
        Tarea nuevaTarea = nuevoCalendario.buscarTarea(titulo, descripcion, limite);
        Alarma alarmaTarea = nuevaTarea.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);

        assertEquals(titulo, nuevaTarea.getTitulo());
        assertEquals(descripcion, nuevaTarea.getDescripcion());
        assertEquals(limite, nuevaTarea.getLimite());
        assertEquals(inicioAlarmas[0], alarmaTarea.getInicio());
        assertEquals(efectoAlarmas[0], alarmaTarea.getEfecto());
    }

    @Test
    public void modificarTarea() {
        var nuevoCalendario = new Calendario();
        String titulo = "Tarea 1";
        String descripcion = "Primer tarea a probar";
        LocalDateTime limite = LocalDateTime.parse("2023-04-24T17:00");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2023-04-24T14:00")};
        Integer[] efectoAlarmas = {0};

        String titulo2 = "Tarea 2";
        String descripcion2 = "Segundo tarea a probar";
        LocalDateTime limite2 = LocalDateTime.parse("2023-04-24T18:00");

        nuevoCalendario.crearTarea(titulo, descripcion, false, limite, inicioAlarmas, efectoAlarmas);
        Tarea nuevaTarea = nuevoCalendario.buscarTarea(titulo, descripcion, limite);
        nuevoCalendario.modificarTarea(nuevaTarea, titulo2, descripcion2, limite);

        assertEquals(titulo2, nuevaTarea.getTitulo());
        assertEquals(descripcion2, nuevaTarea.getDescripcion());
        assertEquals(limite2, nuevaTarea.getLimite());
    }

    @Test
    public void eliminarTarea() {
        var nuevoCalendario = new Calendario();
        String titulo = "Tarea 3";
        String descripcion = "Tercer tarea a probar";
        LocalDateTime limite = LocalDateTime.parse("2023-04-24T17:00");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-07-10T14:00")};
        Integer[] efectoAlarmas = {0};

        nuevoCalendario.crearTarea(titulo, descripcion, false, limite, inicioAlarmas, efectoAlarmas);
        Tarea nuevaTarea1 = nuevoCalendario.buscarTarea(titulo, descripcion, limite);
        nuevoCalendario.eliminarTarea(nuevaTarea1);
        Tarea nuevaTarea2 = nuevoCalendario.buscarTarea(titulo, descripcion, limite);

        assertNull(nuevaTarea2);
    }

    @Test
    public void crearEventosIdenticos() {
        var nuevoCalendario = new Calendario();
        String titulo1 = "Evento A";
        String descripcion1 = "Desc. evento A";
        LocalDateTime inicio1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime fin1 = LocalDateTime.parse("2018-10-10T14:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Integer[] efectoAlarmas = {0};

        String titulo2 = "Evento A";
        String descripcion2 = "Desc. evento A";
        LocalDateTime inicio2 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime fin2 = LocalDateTime.parse("2018-10-10T14:25");

        nuevoCalendario.crearEvento(titulo1, descripcion1, false, inicio1, fin1, inicioAlarmas, efectoAlarmas);
        nuevoCalendario.crearEvento(titulo2, descripcion2, false, inicio2, fin2, inicioAlarmas, efectoAlarmas);
        Evento nuevoEvento1 = nuevoCalendario.buscarEvento(titulo1, descripcion1, inicio1, fin1);
        Evento nuevoEvento2 = nuevoCalendario.buscarEvento(titulo2, descripcion2, inicio2, fin2);
        Alarma alarmaEvento1 = nuevoEvento1.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);
        Alarma alarmaEvento2 = nuevoEvento2.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);

        assertEquals(titulo1, nuevoEvento1.getTitulo());
        assertEquals(descripcion1, nuevoEvento1.getDescripcion());
        assertEquals(inicio1, nuevoEvento1.getInicio());
        assertEquals(fin1, nuevoEvento1.getFin());
        assertEquals(inicioAlarmas[0], alarmaEvento1.getInicio());
        assertEquals(efectoAlarmas[0], alarmaEvento1.getEfecto());

        assertEquals(titulo2, nuevoEvento2.getTitulo());
        assertEquals(descripcion2, nuevoEvento2.getDescripcion());
        assertEquals(inicio2, nuevoEvento2.getInicio());
        assertEquals(fin2, nuevoEvento2.getFin());
        assertEquals(inicioAlarmas[0], alarmaEvento2.getInicio());
        assertEquals(efectoAlarmas[0], alarmaEvento2.getEfecto());
    }

    @Test
    public void crearTareasIdenticas() {
        var nuevoCalendario = new Calendario();
        String titulo1 = "Tarea A";
        String descripcion1 = "Desc. tarea A";
        LocalDateTime limite1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Integer[] efectoAlarmas = {0};

        String titulo2 = "Tarea A";
        String descripcion2 = "Desc. tarea A";
        LocalDateTime limite2 = LocalDateTime.parse("2018-10-10T11:25");

        nuevoCalendario.crearTarea(titulo1, descripcion1, false, limite1, inicioAlarmas, efectoAlarmas);
        nuevoCalendario.crearTarea(titulo2, descripcion2, false, limite2, inicioAlarmas, efectoAlarmas);
        Tarea nuevaTarea1 = nuevoCalendario.buscarTarea(titulo1, descripcion1, limite1);
        Tarea nuevaTarea2 = nuevoCalendario.buscarTarea(titulo2, descripcion2, limite2);
        Alarma alarmaTarea1 = nuevaTarea1.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);
        Alarma alarmaTarea2 = nuevaTarea2.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);

        assertEquals(titulo1, nuevaTarea1.getTitulo());
        assertEquals(descripcion1, nuevaTarea1.getDescripcion());
        assertEquals(limite1, nuevaTarea1.getLimite());
        assertEquals(inicioAlarmas[0], alarmaTarea1.getInicio());
        assertEquals(efectoAlarmas[0], alarmaTarea1.getEfecto());

        assertEquals(titulo2, nuevaTarea1.getTitulo());
        assertEquals(descripcion2, nuevaTarea1.getDescripcion());
        assertEquals(limite1, nuevaTarea1.getLimite());
        assertEquals(inicioAlarmas[0], alarmaTarea2.getInicio());
        assertEquals(efectoAlarmas[0], alarmaTarea2.getEfecto());
    }

    @Test
    public void modificarAlarma() {
        var nuevoCalendario = new Calendario();
        String titulo1 = "Tarea A";
        String descripcion1 = "Desc. tarea A";
        LocalDateTime limite1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Integer[] efectoAlarmas = {0};
        LocalDateTime inicioAlarmasNuevo = LocalDateTime.parse("2018-10-10T20:00");
        Integer efectoAlarmasNuevo = 2;

        nuevoCalendario.crearTarea(titulo1, descripcion1, false, limite1, inicioAlarmas, efectoAlarmas);
        Tarea nuevaTarea1 = nuevoCalendario.buscarTarea(titulo1, descripcion1, limite1);
        Alarma alarmaTarea1 = nuevaTarea1.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);
        nuevaTarea1.modificarAlarma(alarmaTarea1, inicioAlarmasNuevo, efectoAlarmasNuevo);

        assertEquals(inicioAlarmasNuevo, alarmaTarea1.getInicio());
        assertEquals(efectoAlarmasNuevo, alarmaTarea1.getEfecto());
    }

    @Test
    public void eliminarAlarma() {
        var nuevoCalendario = new Calendario();
        String titulo1 = "Tarea A";
        String descripcion1 = "Desc. tarea A";
        LocalDateTime limite1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Integer[] efectoAlarmas = {0};

        nuevoCalendario.crearTarea(titulo1, descripcion1, false, limite1, inicioAlarmas, efectoAlarmas);
        Tarea nuevaTarea1 = nuevoCalendario.buscarTarea(titulo1, descripcion1, limite1);
        Alarma alarmaTarea1 = nuevaTarea1.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);
        nuevaTarea1.eliminarAlarma(alarmaTarea1);

        assertNull(alarmaTarea1);
    }
}