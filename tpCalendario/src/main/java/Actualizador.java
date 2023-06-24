import calendario.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Actualizador {

    private final Calendario calendario;
    private final String ruta;

    public Actualizador(Calendario calendario, String ruta) {
        this.calendario = calendario;
        this.ruta = ruta;

        javafx.util.Duration minutos = javafx.util.Duration.minutes(1);
        Timeline temporizadorRepeticiones = new Timeline(new KeyFrame(minutos, this::chequearRepeticiones));
        temporizadorRepeticiones.setCycleCount(Timeline.INDEFINITE);
        temporizadorRepeticiones.play();
    }

    /* COMPLETAR/DESCOMPLETAR TAREA */
    public void cambiarTareaCompletada(Tarea tarea) {
        calendario.marcarTarea(tarea);
        Estado.guardar(calendario, ruta);
    }

    /* CREAR EVENTOS Y TAREAS */

    public void crearEvento(TextField eventTitle, TextArea eventDescription, CheckBox completeDayCheckbox, TextField fieldFechaInicio, TextField startTimeEvent, TextField fieldFechaFin,  TextField endTimeEvent, TextArea areaAlarms, CheckBox dailyRepeatCheck, TextField frequencyRepeatDaily, TextField quantityReps) {
        // Validar input del usuario.
        if (!esFechaValida(fieldFechaInicio.getText())) {
            showErrorAlert("Formato de fecha de inicio invalido");
            return;
        }

        if (!esFechaValida(fieldFechaFin.getText())) {
            showErrorAlert("Formato de fecha de finalizacion invalido");
            return;
        }

        if (!esTiempoValido(startTimeEvent.getText())) {
            showErrorAlert("Formato de horario de inicio invalido");
            return;
        }

        if (!esTiempoValido(endTimeEvent.getText())) {
            showErrorAlert("Formato de horario de finalizacion invalido");
            return;
        }

        int quantityRepsParsed = 0;

        if (dailyRepeatCheck.isSelected()) {
            // Validar y procesar quantityReps y frequencyRepeatDaily

            String quantityRepsText = quantityReps.getText();
            if (quantityRepsText.isEmpty()) {
                quantityRepsParsed = -1;
            } else if (quantityRepsText.matches("\\d+") && Integer.parseInt(quantityRepsText) > 0) {
                quantityRepsParsed = Integer.parseInt(quantityRepsText);
            } else {
                showErrorAlert("Formato de cantidad de repeticiones inválido");
                return;
            }

            String frequencyRepeatText = frequencyRepeatDaily.getText();
            if (!frequencyRepeatText.matches("\\d+") || Integer.parseInt(frequencyRepeatText) <= 0) {
                showErrorAlert("Formato de frecuencia de repetición diaria inválido");
                return;
            }
        }

        String[] alarmAreaInput = areaAlarms.getText().split("\n");
        List<String> alarmAreaList = new ArrayList<>();

        for (String input : alarmAreaInput) {
            if (input.isEmpty()) {
                continue;
            } else if (input.matches("\\d+[dhm]")) {
                alarmAreaList.add(input);
            } else {
                showErrorAlert("Formato de alarma/s inválido");
                return;
            }
        }

        String[] alarmDates = alarmAreaList.toArray(new String[0]);

        String fechaInicio = fieldFechaInicio.getText();
        String fechaFin = fieldFechaFin.getText();

        String tituloEvento = eventTitle.getText();
        String descripcionEvento = eventDescription.getText();

        String inicioTiempoEvento = startTimeEvent.getText();
        String finTiempoEvento = endTimeEvent.getText();

        boolean diaCompleto = completeDayCheckbox.isSelected();
        boolean seRepite = dailyRepeatCheck.isSelected();

        // Una vez validados los inputs, realizar creacion de evento.

        LocalDate fechaInicioFormatProcess = LocalDate.parse(fechaInicio, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalTime tiempoInicioFormatProcess = LocalTime.parse(inicioTiempoEvento, DateTimeFormatter.ofPattern("HH:mm"));
        LocalDateTime inicioEventoFormateado = fechaInicioFormatProcess.atTime(tiempoInicioFormatProcess);

        LocalDate fechaFinalFormatProcess = LocalDate.parse(fechaFin, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalTime tiempoFinalFormatProcess = LocalTime.parse(finTiempoEvento, DateTimeFormatter.ofPattern("HH:mm"));
        LocalDateTime finEventoFormateado = fechaFinalFormatProcess.atTime(tiempoFinalFormatProcess);

        LocalDateTime[] alarmasFormateadas = new LocalDateTime[alarmDates.length];

        for (int i = 0; i < alarmDates.length; i++) {
            String alarmDate = alarmDates[i];
            Duration duration = calcularDuration(alarmDate);
            alarmasFormateadas[i] = inicioEventoFormateado.minus(duration);
        }

        Efecto[] efectos = new Efecto[alarmasFormateadas.length];
        Arrays.fill(efectos, Efecto.NOTIFICACION);

        if (seRepite && quantityRepsParsed > 0) {
            Integer frecuenciaRepeticion = Integer.valueOf(frequencyRepeatDaily.getText());
            Repeticion repeticion = new RepeticionDiariaIntervalo(inicioEventoFormateado, quantityRepsParsed, frecuenciaRepeticion);
            calendario.crearEvento(tituloEvento, descripcionEvento, diaCompleto, inicioEventoFormateado, finEventoFormateado, alarmasFormateadas, efectos, repeticion);
            Estado.guardar(calendario, ruta);
        } else if (seRepite) {
            Integer frecuenciaRepeticion = Integer.valueOf(frequencyRepeatDaily.getText());
            Repeticion repeticion = new RepeticionDiariaIntervalo(inicioEventoFormateado, frecuenciaRepeticion);
            calendario.crearEvento(tituloEvento, descripcionEvento, diaCompleto, inicioEventoFormateado, finEventoFormateado, alarmasFormateadas, efectos, repeticion);
            Estado.guardar(calendario, ruta);
        } else {
            calendario.crearEvento(tituloEvento, descripcionEvento, diaCompleto, inicioEventoFormateado, finEventoFormateado, alarmasFormateadas, efectos, null);
            Estado.guardar(calendario, ruta);
        }
    }

    public void crearTarea(TextField taskTitle, TextArea taskDescription, CheckBox completeDayCheckbox, TextField endTimeTask, TextField fieldFecha, TextArea areaAlarms) {
        // Validar input del usuario
        if (!esFechaValida(fieldFecha.getText())) {
            showErrorAlert("Formato de fecha invalido");
            return;
        }

        if (!esTiempoValido(endTimeTask.getText())) {
            showErrorAlert("Formato de horario limite invalido");
            return;
        }

        String[] alarmAreaInput = areaAlarms.getText().split("\n");
        List<String> alarmAreaList = new ArrayList<>();

        for (String input : alarmAreaInput) {
            if (input.isEmpty()) {
                continue;
            } else if (input.matches("\\d+[dhm]")) {
                alarmAreaList.add(input);
            } else {
                showErrorAlert("Formato de alarma/s inválido");
                return;
            }
        }

        String[] alarmDates = alarmAreaList.toArray(new String[0]);

        String fechaInicio = fieldFecha.getText();

        String tituloTarea = taskTitle.getText();
        String descripcionTarea = taskDescription.getText();

        String limiteTarea = endTimeTask.getText();

        boolean diaCompleto = completeDayCheckbox.isSelected();

        // Una vez validados los inputs, realizar creacion de tarea.

        LocalDate fechaInicioFormatProcess = LocalDate.parse(fechaInicio, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalTime tiempoInicioFormatProcess = LocalTime.parse(limiteTarea, DateTimeFormatter.ofPattern("HH:mm"));
        LocalDateTime dateTimeTareaFormateado = fechaInicioFormatProcess.atTime(tiempoInicioFormatProcess);

        LocalDateTime[] alarmasFormateadas = new LocalDateTime[alarmDates.length];

        for (int i = 0; i < alarmDates.length; i++) {
            String alarmDate = alarmDates[i];
            Duration duration = calcularDuration(alarmDate);
            alarmasFormateadas[i] = dateTimeTareaFormateado.minus(duration);
        }

        Efecto[] efectos = new Efecto[alarmasFormateadas.length];
        Arrays.fill(efectos, Efecto.NOTIFICACION);

        calendario.crearTarea(tituloTarea, descripcionTarea, diaCompleto, dateTimeTareaFormateado, alarmasFormateadas, efectos);
        Estado.guardar(calendario, ruta);
    }

    /* MODIFICAR EVENTOS Y TAREAS */

    public void modificarEvento(Evento evento, TextField newTitleModifyEvent, TextArea newDescriptionModifyEvent, CheckBox allDayCheckboxModifyEvent, TextField newTimeStartModifyEvent, TextField newDateStartModifyEvent, TextField newTimeEndModifyEvent, TextField newDateEndModifyEvent) {
        String nuevoTitulo = null;
        String nuevaDescripcion = null;
        LocalDateTime nuevoInicio = null;
        LocalDateTime nuevoFin = null;

        String auxiliar_date_inicio = newDateStartModifyEvent.getText();
        String auxiliar_time_inicio = newTimeStartModifyEvent.getText();
        String auxiliar_date_fin = newDateEndModifyEvent.getText();
        String auxiliar_time_fin = newTimeEndModifyEvent.getText();

        if (!auxiliar_date_inicio.isEmpty()) {
            if (!esFechaValida(auxiliar_date_inicio)) {
                showErrorAlert("Formato de fecha invalido");
                return;
            }
        }

        if (!auxiliar_time_inicio.isEmpty()) {
            if (!esTiempoValido(auxiliar_time_inicio)) {
                showErrorAlert("Formato de horario limite invalido");
                return;
            }
        }

        if (!auxiliar_date_fin.isEmpty()) {
            if (!esFechaValida(auxiliar_date_fin)) {
                showErrorAlert("Formato de fecha invalido");
                return;
            }
        }

        if (!auxiliar_time_fin.isEmpty()) {
            if (!esTiempoValido(auxiliar_time_fin)) {
                showErrorAlert("Formato de horario limite invalido");
                return;
            }
        }

        if ((!auxiliar_date_inicio.isEmpty() && auxiliar_time_inicio.isEmpty()) || (auxiliar_date_inicio.isEmpty() && (!auxiliar_time_inicio.isEmpty()))) {
            showErrorAlert("Ingrese tanto la fecha como el horario de inicio (Complete ambos campos)");
            return;
        }

        if ((!auxiliar_date_fin.isEmpty() && auxiliar_time_fin.isEmpty()) || (auxiliar_date_fin.isEmpty() && (!auxiliar_time_fin.isEmpty()))) {
            showErrorAlert("Ingrese tanto la fecha como el horario de fin (Complete ambos campos)");
            return;
        }

        if ((!newTitleModifyEvent.getText().isEmpty())) {
            nuevoTitulo = newTitleModifyEvent.getText();
        }

        if ((!newDescriptionModifyEvent.getText().isEmpty())) {
            nuevaDescripcion = newDescriptionModifyEvent.getText();
        }

        if ((!auxiliar_date_inicio.isEmpty()) && (!auxiliar_time_inicio.isEmpty())) {
            LocalDate nuevaFechaInicioFormatProcess = LocalDate.parse(auxiliar_date_inicio, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalTime nuevoTiempoLimiteFormatProcess = LocalTime.parse(auxiliar_time_inicio, DateTimeFormatter.ofPattern("HH:mm"));
            nuevoInicio = nuevaFechaInicioFormatProcess.atTime(nuevoTiempoLimiteFormatProcess);
        }

        if ((!auxiliar_date_fin.isEmpty()) && (!auxiliar_time_fin.isEmpty())) {
            LocalDate nuevaFechaInicioFormatProcess = LocalDate.parse(auxiliar_date_fin, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalTime nuevoTiempoLimiteFormatProcess = LocalTime.parse(auxiliar_time_fin, DateTimeFormatter.ofPattern("HH:mm"));
            nuevoFin = nuevaFechaInicioFormatProcess.atTime(nuevoTiempoLimiteFormatProcess);
        }

        calendario.modificar(evento, nuevoTitulo, nuevaDescripcion, nuevoInicio, nuevoFin);
        calendario.modificar(evento, allDayCheckboxModifyEvent.isSelected());
        Estado.guardar(calendario, ruta);
    }

    public void modificarTarea(Tarea tarea, TextField newTitleModifyTask, TextArea newDescriptionModifyTask, CheckBox allDayCheckboxModifyTask, TextField newTimeLimitModifyTask, TextField newDateModifyTask) {
        String nuevoTitulo = null;
        String nuevaDescripcion = null;
        LocalDateTime nuevoLimite = null;

        String auxiliar_date = newDateModifyTask.getText();
        String auxiliar_time = newTimeLimitModifyTask.getText();

        if (!auxiliar_date.isEmpty()) {
            if (!esFechaValida(auxiliar_date)) {
                showErrorAlert("Formato de fecha invalido");
                return;
            }
        }

        if (!auxiliar_time.isEmpty()) {
            if (!esTiempoValido(auxiliar_time)) {
                showErrorAlert("Formato de horario limite invalido");
                return;
            }
        }

        if ((!auxiliar_date.isEmpty() && auxiliar_time.isEmpty()) || (auxiliar_date.isEmpty() && (!auxiliar_time.isEmpty()))) {
            showErrorAlert("Ingrese tanto la fecha como el horario (Complete ambos campos)");
            return;
        }

        if ((!newTitleModifyTask.getText().isEmpty())) {
            nuevoTitulo = newTitleModifyTask.getText();
        }

        if ((!newDescriptionModifyTask.getText().isEmpty())) {
            nuevaDescripcion = newDescriptionModifyTask.getText();
        }

        if ((!auxiliar_date.isEmpty()) && (!auxiliar_time.isEmpty())) {
            LocalDate nuevaFechaInicioFormatProcess = LocalDate.parse(auxiliar_date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalTime nuevoTiempoLimiteFormatProcess = LocalTime.parse(auxiliar_time, DateTimeFormatter.ofPattern("HH:mm"));
            nuevoLimite = nuevaFechaInicioFormatProcess.atTime(nuevoTiempoLimiteFormatProcess);
        }

        calendario.modificar(tarea, nuevoTitulo, nuevaDescripcion, nuevoLimite);
        calendario.modificar(tarea, allDayCheckboxModifyTask.isSelected());
        Estado.guardar(calendario, ruta);
    }

    /* METODOS AUXILIARES */
    public Duration calcularDuration(String alarmDate) {
        // Borrar basura de input
        alarmDate = alarmDate.trim();

        // Numero
        int value = Integer.parseInt(alarmDate.substring(0, alarmDate.length() - 1));

        // Letra
        char unitChar = Character.toLowerCase(alarmDate.charAt(alarmDate.length() - 1));

        // Calcular duracion basado en tipo
        return switch (unitChar) {
            case 'd' -> Duration.ofDays(value);
            case 'h' -> Duration.ofHours(value);
            case 'm' -> Duration.ofMinutes(value);
            default -> throw new IllegalArgumentException("Formato de duracion invalido: " + alarmDate);
        };
    }

    public void showErrorAlert(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacion");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public boolean esFechaValida(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public boolean esTiempoValido(String time) {
        return time.matches("(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]");
    }

    /* ELIMINAR EVENTOS Y TAREAS */
    public void eliminarEvento(Evento evento) {
        calendario.eliminarEvento(evento);
        Estado.guardar(calendario, ruta);
    }

    public void eliminarTarea(Tarea tarea) {
        calendario.eliminarTarea(tarea);
        Estado.guardar(calendario, ruta);
    }

    /* CONTROLAR LAS REPETICIONES */
    public void chequearRepeticiones(ActionEvent event) {
        LocalDateTime horaActual = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        calendario.actualizarEventosRepetidos(horaActual);
        Estado.guardar(calendario, ruta);
    }
}
