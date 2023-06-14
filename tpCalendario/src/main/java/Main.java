import calendario.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.Duration;

public class Main extends Application {

    @FXML
    private Label fechaTitulo;
    @FXML
    private VBox listaActividades;
    @FXML
    private Button siguiente;
    @FXML
    private Button anterior;
    @FXML
    private Button crearEventScene;
    @FXML
    private Button crearTareaScene;
    @FXML
    private ChoiceBox<String> opcionesIntervalo;

    @FXML
    private Button cancelarActividad;
    @FXML
    private Button eliminar;
    @FXML
    private VBox detalleActividad;
    @FXML
    private Button marcarTareaButton;

    // Cosas de crearEvento
    @FXML
    private Button crearEvento;
    @FXML
    private Button cancelarEvento;
    @FXML
    private CheckBox completeDayCheckbox;
    @FXML
    private TextField eventTitle;
    @FXML
    private TextArea eventDescription;
    @FXML
    private TextField fieldFechaInicio;
    @FXML
    private TextField fieldFechaFin;
    @FXML
    private TextField startTimeEvent;
    @FXML
    private TextField endTimeEvent;
    @FXML
    private CheckBox dailyRepeatCheck;
    @FXML
    private TextField frequencyRepeatDaily;
    @FXML
    private TextArea areaAlarms;
    @FXML
    private TextField quantityReps;

    // Cosas de crearTarea
    @FXML
    private Button cancelarTarea;
    @FXML
    private TextField taskTitle;
    @FXML
    private TextArea taskDescription;
    @FXML
    private TextField fieldFecha;
    @FXML
    private TextField endTimeTask;
    @FXML
    private Button crearTarea;

    // Cosas de modificarEvento
    @FXML
    private Button modifyEventButton;
    @FXML
    private Button cancelarModifyEvent;
    @FXML
    private Button modifyEvent_Effect;
    @FXML
    private TextField newTitleModifyEvent;
    @FXML
    private TextArea newDescriptionModifyEvent;
    @FXML
    private CheckBox allDayCheckboxModifyEvent;
    @FXML
    private TextField newDateStartModifyEvent;
    @FXML
    private TextField newTimeStartModifyEvent;
    @FXML
    private TextField newDateEndModifyEvent;
    @FXML
    private TextField newTimeEndModifyEvent;

    // Cosas de modificarTarea
    @FXML
    private Button modifyTaskButton;
    @FXML
    private Button cancelarModifyTask;
    @FXML
    private Button modifyTask_Effect;
    @FXML
    private TextField newTitleModifyTask;
    @FXML
    private TextArea newDescriptionModifyTask;
    @FXML
    private CheckBox allDayCheckboxModifyTask;
    @FXML
    private TextField newDateModifyTask;
    @FXML
    private TextField newTimeLimitModifyTask;

    private Calendario calendario;
    private LocalDate fechaActual;
    private Frecuencia intervalo;
    private Stage escenario;
    private final String ruta;
    private Timeline temporizadorAlarma;
    private Timeline temporizadorRepeticiones;

    public Main() {
        fechaActual = LocalDate.now();
        intervalo = Frecuencia.DIARIA;
        ruta = "calendario";

        try {
            recuperarEstado();
        } catch (Exception e) {
            calendario = new Calendario();
        }
    }

    @Override
    public void start(Stage stage) {
        this.escenario = stage;

        javafx.util.Duration segundos = javafx.util.Duration.seconds(1);
        this.temporizadorAlarma = new Timeline(new KeyFrame(segundos, this::chequearAlarma));
        temporizadorAlarma.setCycleCount(Timeline.INDEFINITE);
        temporizadorAlarma.play();

        javafx.util.Duration minutos = javafx.util.Duration.minutes(1);
        this.temporizadorRepeticiones = new Timeline(new KeyFrame(minutos, this::chequearRepeticiones));
        temporizadorRepeticiones.setCycleCount(Timeline.INDEFINITE);
        temporizadorRepeticiones.play();

        mostrarVistaActividades();
    }

    // METODOS RELATIVOS A VISTA DE ACTIVIDADES

    private void mostrarVistaActividades() {
        FXMLLoader loader = new FXMLLoader(getClass().
                getResource("scena1.fxml"));
        loader.setController(this);
        AnchorPane vista;
        try {
            vista = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        switch (intervalo) {
            case DIARIA -> mostrarDia();
            case SEMANAL -> mostrarSemana();
            case MENSUAL -> mostrarMes();
        }

        anterior.setOnAction(event -> retroceder());
        siguiente.setOnAction(event -> avanzar());

        crearEventScene.setOnAction(event -> mostrarVistaCrearEvento());

        crearTareaScene.setOnAction(event -> mostrarVistaCrearTarea());

        opcionesIntervalo.getItems().addAll(Frecuencia.DIARIA.toString(), Frecuencia.SEMANAL.toString(), Frecuencia.MENSUAL.toString());
        opcionesIntervalo.setValue(intervalo.toString());
        opcionesIntervalo.setOnAction(event -> {
            String opcionSeleccionada = opcionesIntervalo.getValue();
            if (opcionSeleccionada.equals(Frecuencia.DIARIA.toString())) {
                intervalo = Frecuencia.DIARIA;
                mostrarDia();
            } else if (opcionSeleccionada.equals(Frecuencia.SEMANAL.toString())) {
                intervalo = Frecuencia.SEMANAL;
                mostrarSemana();
            } else if (opcionSeleccionada.equals(Frecuencia.MENSUAL.toString())) {
                intervalo = Frecuencia.MENSUAL;
                mostrarMes();
            }
        });

        Scene scene = new Scene(vista);
        escenario.setScene(scene);
        escenario.show();
    }

    // MOSTRAR LOS DISTINTOS INTERVALOS
    public void mostrarDia() {
        listaActividades.getChildren().clear();
        fechaTitulo.setText(formatoFecha(fechaActual));
        mostrarActividades(FXCollections.observableList(calendario.buscarPorIntervalo(fechaActual.atStartOfDay(), fechaActual.atStartOfDay().plusDays(1))));
    }

    public void mostrarSemana() {
        listaActividades.getChildren().clear();
        fechaTitulo.setText(formatoFecha(obtenerPrincipioSemana(fechaActual)) + " - " + formatoFecha(obtenerFinSemana(fechaActual)));
        mostrarActividades(FXCollections.observableList(calendario.buscarPorIntervalo(obtenerPrincipioSemana(fechaActual).atStartOfDay(), obtenerFinSemana(fechaActual).atStartOfDay().plusDays(1))));
    }

    public void mostrarMes() {
        listaActividades.getChildren().clear();
        fechaTitulo.setText(fechaActual.getMonth().toString() + " " + fechaActual.getYear());
        mostrarActividades(FXCollections.observableList(calendario.buscarPorIntervalo(obtenerPrincipioMes(fechaActual).atStartOfDay(), obtenerFinMes(fechaActual).atStartOfDay().plusDays(1))));
    }

    // MOSTRAR LAS DISTINTAS ACTIVIDADES
    public void mostrarActividades(ObservableList<Actividad> actividades) {
        for (Actividad a : actividades) {
            a.aceptar(new ActividadVisitante() {
                @Override
                public void visitarEvento(Evento evento) {
                    mostrarEvento(a);
                }

                @Override
                public void visitarTarea(Tarea tarea) {
                    mostrarTarea(a);
                }
            });
        }
    }

    public void mostrarEvento(Actividad actividad) {
        Evento evento = (Evento) actividad;

        Label label = new Label();
        label.setText(evento.getTitulo());

        if (evento.isDiaCompleto()) {
            label.setText(String.format("%s [ Inicio: %s | Fin: %s ]",  label.getText(), formatoFecha(evento.getInicio()), formatoFecha(evento.getFin())));
            label.setStyle("-fx-background-color: blue; " + "-fx-text-fill: white;");
        } else {
            label.setText(String.format("%s [ Inicio: %s | Fin: %s ]",  label.getText(), formatoTiempo(evento.getInicio()), formatoTiempo(evento.getFin())));
            label.setStyle("-fx-text-fill: blue;");
        }

        listaActividades.getChildren().add(label);

        label.setOnMouseClicked(event -> mostrarVistaEvento(evento));
    }

    public void mostrarTarea(Actividad actividad) {
        Tarea tarea = (Tarea) actividad;

        Label label = new Label();
        label.setText(tarea.getTitulo());

        if (tarea.isDiaCompleto()) {
            label.setText(String.format("%s [ Limite: %s", label.getText(), formatoFecha(tarea.getInicio())));
            label.setStyle("-fx-background-color: red; " + "-fx-text-fill: white;");
        } else {
            label.setText(String.format("%s [ Limite: %s", label.getText(), formatoTiempo(tarea.getInicio())));
            label.setStyle("-fx-text-fill: red;");
        }

        if (tarea.isCompletada()) {
            label.setText(String.format("%s | Completada: ✅ ]", label.getText()));
        } else {
            label.setText(String.format("%s | Completada: ✗ ]", label.getText()));
        }

        listaActividades.getChildren().add(label);

        label.setOnMouseClicked(event -> mostrarVistaTarea(tarea));
    }

    // OBTENER PRINCIPIOS Y FINES DE INTERVALOS
    public static LocalDate obtenerPrincipioSemana(LocalDate fecha) {
        return fecha.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
    }
    public static LocalDate obtenerFinSemana(LocalDate fecha) {
        return fecha.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
    }
    public static LocalDate obtenerPrincipioMes(LocalDate fecha) {
        return fecha.withDayOfMonth(1);
    }
    public static LocalDate obtenerFinMes(LocalDate fecha) {
        int ultimoDiaMes = fecha.lengthOfMonth();
        return fecha.withDayOfMonth(ultimoDiaMes);
    }

    // DARLE FORMATO A LOCALDATE
    public String formatoTiempo(LocalDateTime tiempo) {
        return String.format("%s - %s/%s/%s", tiempo.toLocalTime(), tiempo.getDayOfMonth(), tiempo.getMonthValue(), tiempo.getYear());
    }
    public String formatoFecha(LocalDateTime tiempo) {
        return String.format("%s/%s/%s", tiempo.getDayOfMonth(), tiempo.getMonthValue(), tiempo.getYear());
    }
    public String formatoFecha(LocalDate fecha) {
        return String.format("%s/%s/%s", fecha.getDayOfMonth(), fecha.getMonthValue(), fecha.getYear());
    }

    // AVANZAR Y RETROCEDER EN EL TIEMPO
    public void retroceder() {
        if (intervalo.equals(Frecuencia.DIARIA)) {
            fechaActual = fechaActual.minusDays(1);
            mostrarDia();
        } else if (intervalo.equals(Frecuencia.SEMANAL)) {
            fechaActual = fechaActual.minusWeeks(1);
            mostrarSemana();
        } else if (intervalo.equals(Frecuencia.MENSUAL)) {
            fechaActual = fechaActual.minusMonths(1);
            mostrarMes();
        }
    }

    public void avanzar() {
        if (intervalo.equals(Frecuencia.DIARIA)) {
            fechaActual = fechaActual.plusDays(1);
            mostrarDia();
        } else if (intervalo.equals(Frecuencia.SEMANAL)) {
            fechaActual = fechaActual.plusWeeks(1);
            mostrarSemana();
        } else if (intervalo.equals(Frecuencia.MENSUAL)) {
            fechaActual = fechaActual.plusMonths(1);
            mostrarMes();
        }
    }

    // METODOS RELATIVOS A LA VISTA DE EVENTO Y TAREAS

    public void mostrarVistaEvento(Evento evento) {
        FXMLLoader loader = new FXMLLoader(getClass().
                getResource("scena2.fxml"));
        loader.setController(this);
        AnchorPane vista;
        try {
            vista = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cancelarActividad.setOnAction(event -> mostrarVistaActividades());

        eliminar.setOnAction(event-> eliminarEvento(evento));

        modifyEventButton.setOnAction(event -> mostrarVistaModificarEvento(evento));

        mostrarDetalleEvento(evento);

        Scene scene = new Scene(vista);
        escenario.setScene(scene);
        escenario.show();
    }

    public void mostrarVistaModificarEvento(Evento evento) {
        FXMLLoader loader = new FXMLLoader(getClass().
                getResource("scenaModificarEvento.fxml"));
        loader.setController(this);
        AnchorPane vista;
        try {
            vista = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cancelarModifyEvent.setOnAction(event -> mostrarVistaEvento(evento));

        modifyEvent_Effect.setOnAction(event -> modificarEvento(evento));

        Scene scene = new Scene(vista);
        escenario.setScene(scene);
        escenario.show();
    }

    public void mostrarDetalleEvento(Evento evento) {

        Label titulo = new Label();
        titulo.setText(evento.getTitulo());

        Label descripcion = new Label();
        descripcion.setText(evento.getDescripcion());

        Label diaCompleto = new Label();
        Label inicio = new Label();
        Label fin = new Label();

        if (evento.isDiaCompleto()) {
            diaCompleto.setText("Dia completo ✅");
            inicio.setText(formatoFecha(evento.getInicio()));
            fin.setText(formatoFecha(evento.getFin()));
        } else {
            diaCompleto.setText("Dia completo ✗");
            inicio.setText(formatoTiempo(evento.getInicio()));
            fin.setText(formatoTiempo(evento.getFin()));
        }

        Label repeticion = new Label();
        if (evento.esRepetido()) {
            repeticion.setText("Repeticion ✅");
        } else {
            repeticion.setText("Repeticion ✗");
        }

        detalleActividad.getChildren().add(titulo);
        detalleActividad.getChildren().add(descripcion);
        detalleActividad.getChildren().add(diaCompleto);
        detalleActividad.getChildren().add(inicio);
        detalleActividad.getChildren().add(fin);
        detalleActividad.getChildren().add(repeticion);

        detalleActividad.getChildren().add(new Label("Alarmas"));
        for (Alarma a: evento.getListaAlarmas()) {
            Label alarma = new Label();
            alarma.setText(String.format("Inicio: %s - Efecto: %s", formatoTiempo(a.getInicio()), a.getEfecto()));
            detalleActividad.getChildren().add(alarma);
        }
    }

    public void mostrarVistaTarea(Tarea tarea)  {
        FXMLLoader loader = new FXMLLoader(getClass().
                getResource("scena2Tarea.fxml"));
        loader.setController(this);
        AnchorPane vista;
        try {
            vista = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cancelarModifyTask.setOnAction(event -> mostrarVistaActividades());

        eliminar.setOnAction(event-> eliminarTarea(tarea));

        marcarTareaButton.setOnAction(event -> {
            cambiarTareaCompletada(tarea);
            mostrarVistaTarea(tarea);
        });

        modifyTaskButton.setOnAction(event -> mostrarVistaModificarTarea(tarea));

        mostrarDetalleTarea(tarea);

        Scene scene = new Scene(vista);
        escenario.setScene(scene);
        escenario.show();
    }

    public void mostrarVistaModificarTarea(Tarea tarea) {
        FXMLLoader loader = new FXMLLoader(getClass().
                getResource("scenaModificarTarea.fxml"));
        loader.setController(this);
        AnchorPane vista;
        try {
            vista = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cancelarModifyTask.setOnAction(event -> mostrarVistaTarea(tarea));

        modifyTask_Effect.setOnAction(event -> modificarTarea(tarea));

        Scene scene = new Scene(vista);
        escenario.setScene(scene);
        escenario.show();
    }

    public void cambiarTareaCompletada(Tarea tarea) {
        calendario.marcarTarea(tarea);
        guardarEstado();
    }

    public void mostrarDetalleTarea(Tarea tarea) {

        Label titulo = new Label();
        titulo.setText(tarea.getTitulo());

        Label descripcion = new Label();
        descripcion.setText(tarea.getDescripcion());

        Label diaCompleto = new Label();
        Label inicio = new Label();
        Label fin = new Label();

        if (tarea.isDiaCompleto()) {
            diaCompleto.setText("Dia completo ✅");
            inicio.setText(formatoFecha(tarea.getInicio()));
        } else {
            diaCompleto.setText("Dia completo ✗");
            inicio.setText(formatoTiempo(tarea.getInicio()));
        }

        Label completada = new Label();
        if (tarea.isCompletada()) {
            completada.setText("Completada: ✅");
        } else {
            completada.setText("Completada: ✗");
        }

        detalleActividad.getChildren().add(titulo);
        detalleActividad.getChildren().add(descripcion);
        detalleActividad.getChildren().add(diaCompleto);
        detalleActividad.getChildren().add(inicio);
        detalleActividad.getChildren().add(fin);
        detalleActividad.getChildren().add(completada);

        detalleActividad.getChildren().add(new Label("Alarmas"));
        for (Alarma a: tarea.getListaAlarmas()) {
            Label alarma = new Label();
            alarma.setText(String.format("Inicio: %s - Efecto: %s", formatoTiempo(a.getInicio()), a.getEfecto()));
            detalleActividad.getChildren().add(alarma);
        }
    }

    public void mostrarVistaCrearEvento() {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scenaCrearEvento.fxml"));
        loader.setController(this);
        AnchorPane vista;
        try {
            vista = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        crearEvento.setOnAction(event -> crearEvento());

        cancelarEvento.setOnAction(event -> mostrarVistaActividades());

        // Create the scene
        Scene scene = new Scene(vista);
        escenario.setScene(scene);
        escenario.show();
    }

    public void mostrarVistaCrearTarea() {
        // Load FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ScenaCrearTarea.fxml"));
        loader.setController(this);
        AnchorPane vista;
        try {
            vista = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        crearTarea.setOnAction(event -> crearTarea());

        cancelarTarea.setOnAction(event -> mostrarVistaActividades());

        // Create the scene
        Scene scene = new Scene(vista);
        escenario.setScene(scene);
        escenario.show();
    }

    // METODOS RELATIVOS A LA CREACION Y MODIFICACION DE EVENTOS Y TAREAS

    public void crearEvento() {
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

        if (seRepite && quantityRepsParsed > 0){
            Integer frecuenciaRepeticion = Integer.valueOf(frequencyRepeatDaily.getText());
            Repeticion repeticion = new RepeticionDiariaIntervalo(inicioEventoFormateado, quantityRepsParsed,frecuenciaRepeticion);
            Efecto[] efectos = new Efecto[quantityRepsParsed];
            Arrays.fill(efectos, Efecto.NOTIFICACION);
            calendario.crearEvento(tituloEvento, descripcionEvento, diaCompleto, inicioEventoFormateado, finEventoFormateado, alarmasFormateadas, efectos, repeticion);
            guardarEstado();
        }

        else if (seRepite){
            Integer frecuenciaRepeticion = Integer.valueOf(frequencyRepeatDaily.getText());
            Repeticion repeticion = new RepeticionDiariaIntervalo(inicioEventoFormateado, frecuenciaRepeticion);
            calendario.crearEvento(tituloEvento, descripcionEvento, diaCompleto, inicioEventoFormateado, finEventoFormateado, alarmasFormateadas, new Efecto[]{Efecto.NOTIFICACION}, repeticion);
            guardarEstado();
        }

        else {
            calendario.crearEvento(tituloEvento, descripcionEvento, diaCompleto, inicioEventoFormateado, finEventoFormateado, alarmasFormateadas, new Efecto[]{Efecto.NOTIFICACION}, null);
            guardarEstado();
        }
        mostrarVistaActividades();
    }

    public void crearTarea() {
        // Validar input del usuario
        if (!esFechaValida(fieldFecha.getText())){
            showErrorAlert("Formato de fecha invalido");
            return;
        }

        if (!esTiempoValido(endTimeTask.getText())){
            showErrorAlert("Formato de horario limite invalido");
            return;
        }

        String[] alarmAreaInput = areaAlarms.getText().split("\n");
        List<String> alarmAreaList = new ArrayList<>();

        for (String input : alarmAreaInput) {
            if (input.isEmpty()) {
                continue;  // Skip empty lines
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

        // Una vez validados los inputs, realizar creacion de evento.

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
        guardarEstado();
        mostrarVistaActividades();
    }

    public void modificarTarea(Tarea tarea) {
        String nuevoTitulo = null;
        String nuevaDescripcion = null;
        LocalDateTime nuevoLimite = null;

        String auxiliar_date = newDateModifyTask.getText();
        String auxiliar_time = newTimeLimitModifyTask.getText();

        if (!auxiliar_date.isEmpty()){
            if (!esFechaValida(auxiliar_date)){
                showErrorAlert("Formato de fecha invalido");
                return;
            }
        }

        if (!auxiliar_time.isEmpty()){
            if (!esTiempoValido(auxiliar_time)){
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

        if ((!auxiliar_date.isEmpty()) && (!auxiliar_time.isEmpty())){
            LocalDate nuevaFechaInicioFormatProcess = LocalDate.parse(auxiliar_date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalTime nuevoTiempoLimiteFormatProcess = LocalTime.parse(auxiliar_time, DateTimeFormatter.ofPattern("HH:mm"));
            nuevoLimite = nuevaFechaInicioFormatProcess.atTime(nuevoTiempoLimiteFormatProcess);
        }

        calendario.modificar(tarea, nuevoTitulo, nuevaDescripcion, nuevoLimite);
        calendario.modificar(tarea, allDayCheckboxModifyTask.isSelected());
        guardarEstado();
        mostrarVistaActividades();
    }

    public void modificarEvento(Evento evento) {
        String nuevoTitulo = null;
        String nuevaDescripcion = null;
        LocalDateTime nuevoInicio = null;
        LocalDateTime nuevoFin = null;

        String auxiliar_date_inicio = newDateStartModifyEvent.getText();
        String auxiliar_time_inicio = newTimeStartModifyEvent.getText();
        String auxiliar_date_fin = newDateEndModifyEvent.getText();
        String auxiliar_time_fin = newTimeEndModifyEvent.getText();

        if (!auxiliar_date_inicio.isEmpty()){
            if (!esFechaValida(auxiliar_date_inicio)){
                showErrorAlert("Formato de fecha invalido");
                return;
            }
        }

        if (!auxiliar_time_inicio.isEmpty()){
            if (!esTiempoValido(auxiliar_time_inicio)){
                showErrorAlert("Formato de horario limite invalido");
                return;
            }
        }

        if (!auxiliar_date_fin.isEmpty()){
            if (!esFechaValida(auxiliar_date_fin)){
                showErrorAlert("Formato de fecha invalido");
                return;
            }
        }

        if (!auxiliar_time_fin.isEmpty()){
            if (!esTiempoValido(auxiliar_time_fin)){
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

        if ((!auxiliar_date_inicio.isEmpty()) && (!auxiliar_time_inicio.isEmpty())){
            LocalDate nuevaFechaInicioFormatProcess = LocalDate.parse(auxiliar_date_inicio, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalTime nuevoTiempoLimiteFormatProcess = LocalTime.parse(auxiliar_time_inicio, DateTimeFormatter.ofPattern("HH:mm"));
            nuevoInicio = nuevaFechaInicioFormatProcess.atTime(nuevoTiempoLimiteFormatProcess);
        }

        if ((!auxiliar_date_fin.isEmpty()) && (!auxiliar_time_fin.isEmpty())){
            LocalDate nuevaFechaInicioFormatProcess = LocalDate.parse(auxiliar_date_fin, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalTime nuevoTiempoLimiteFormatProcess = LocalTime.parse(auxiliar_time_fin, DateTimeFormatter.ofPattern("HH:mm"));
            nuevoFin = nuevaFechaInicioFormatProcess.atTime(nuevoTiempoLimiteFormatProcess);
        }

        calendario.modificar(evento, nuevoTitulo, nuevaDescripcion, nuevoInicio, nuevoFin);
        calendario.modificar(evento, allDayCheckboxModifyEvent.isSelected());
        guardarEstado();
        mostrarVistaActividades();
    }

    private Duration calcularDuration(String alarmDate) {
        // Borrar basura de input
        alarmDate = alarmDate.trim();

        // Numero
        int value = Integer.parseInt(alarmDate.substring(0, alarmDate.length() - 1));

        // Letra
        char unitChar = Character.toLowerCase(alarmDate.charAt(alarmDate.length() - 1));

        // Calcular duracion basado en tipo
        switch (unitChar) {
            case 'd':
                return Duration.ofDays(value);
            case 'h':
                return Duration.ofHours(value);
            case 'm':
                return Duration.ofMinutes(value);
            default:
                throw new IllegalArgumentException("Formato de duracion invalido: " + alarmDate);
        }
    }

    public void showErrorAlert(String mensaje){
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

    // METODOS PARA ELIMINAR
    public void eliminarEvento(Evento evento) {
        calendario.eliminarEvento(evento);
        guardarEstado();
        mostrarVistaActividades();
    }

    public void eliminarTarea(Tarea tarea) {
        calendario.eliminarTarea(tarea);
        guardarEstado();
        mostrarVistaActividades();
    }

    // METODO RELATIVO A LAS REPETICIONES
    private void chequearRepeticiones(ActionEvent event) {
        LocalDateTime horaActual = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        calendario.actualizarEventosRepetidos(horaActual);
    }

    // METODOS RELATIVOS A LA ALARMA
    private void chequearAlarma(ActionEvent event) {
        LocalDateTime horaActual = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        if (calendario.iniciaProximaAlarma(horaActual)) {
            mostrarAlarma(calendario.obtenerProximaAlarma(horaActual));
        }
    }

    private void mostrarAlarma(Alarma alarma) {
        Actividad actividad = alarma.disparar();
        guardarEstado();

        var vista = new VBox();
        var titulo = new Label(actividad.getTitulo());
        var descripcion = new Label(actividad.getDescripcion());

        vista.getChildren().add(titulo);
        vista.getChildren().add(descripcion);

        var escena = new Scene(vista, 200, 100);

        var ventana = new Stage();
        ventana.setMinWidth(200);
        ventana.setMaxWidth(200);
        ventana.setMinHeight(100);
        ventana.setMaxHeight(100);
        ventana.setScene(escena);
        ventana.show();
    }

    // METODOS PARA GUARDAR Y RECUPERAR ESTADO
    public void guardarEstado() {
        BufferedOutputStream estado;
        try {
            estado = new BufferedOutputStream(new FileOutputStream(ruta));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            calendario.serializar(estado);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void recuperarEstado() throws IOException, ClassNotFoundException {
        var estado = new BufferedInputStream(new FileInputStream(ruta));
        calendario = Calendario.deserializar(estado);

    }
}