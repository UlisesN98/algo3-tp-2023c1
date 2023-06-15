import calendario.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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

    // ATRIBUTOS RELATIVOS A VISTA DE ACTIVIDADES
    @FXML
    private Label fechaTitulo;
    @FXML
    private VBox listaActividades;
    @FXML
    private Button siguiente;
    @FXML
    private Button anterior;
    @FXML
    private Button vistaCrearEvento;
    @FXML
    private Button vistaCrearTarea;
    @FXML
    private ChoiceBox<String> opcionesIntervalo;

    // ATRIBUTOS RELATIVOS A VISTA DE EVENTO Y TAREA
    @FXML
    private Button cancelarActividad;
    @FXML
    private Button eliminar;
    @FXML
    private VBox detalleActividad;
    @FXML
    private Button marcarTarea;
    @FXML
    private Button vistaModificarEvento;
    @FXML
    private Button vistaModificarTarea;

    // ATRIBUTOS RELATIVOS A CREAR EVENTO
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

    // ATRIBUTOS RELATIVOS A CREAR TAREA
    @FXML
    private Button crearTarea;
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

    // ATRIBUTOS RELATIVOS A MODIFICAR EVENTO
    @FXML
    private Button modificarEvento;
    @FXML
    private Button cancelarModificarEvento;
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

    // ATRIBUTOS RELATIVOS A MODIFICAR TAREA
    @FXML
    private Button modificarTarea;
    @FXML
    private Button cancelarModificarTarea;
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

    // ATRIBUTO RELATIVO A ALARMA
    @FXML
    private VBox detalleAlarma;

    // ATRIBUTOS DEL CONTROLADOR
    private final Calendario calendario;
    private LocalDate fechaActual;
    private Frecuencia intervalo;
    private Stage escenario;
    private final String ruta;

    public Main() {
        fechaActual = LocalDate.now();
        intervalo = Frecuencia.DIARIA;
        ruta = "calendario";
        calendario = recuperarEstado();
        guardarEstado();
    }

    @Override
    public void start(Stage stage) {
        this.escenario = stage;

        javafx.util.Duration segundos = javafx.util.Duration.seconds(1);
        Timeline temporizadorAlarma = new Timeline(new KeyFrame(segundos, this::chequearAlarma));
        temporizadorAlarma.setCycleCount(Timeline.INDEFINITE);
        temporizadorAlarma.play();

        javafx.util.Duration minutos = javafx.util.Duration.minutes(1);
        Timeline temporizadorRepeticiones = new Timeline(new KeyFrame(minutos, this::chequearRepeticiones));
        temporizadorRepeticiones.setCycleCount(Timeline.INDEFINITE);
        temporizadorRepeticiones.play();

        mostrarVistaActividades();
    }

    /* METODOS RELATIVOS AL STAGE */
    public AnchorPane cargarVista(String nombreArchivo) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(nombreArchivo));
        loader.setController(this);
        AnchorPane vista;
        try {
            vista = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return vista;
    }

    public void mostrarEscenario(AnchorPane vista) {
        Scene escena = new Scene(vista);
        escenario.setScene(escena);
        escenario.show();
    }

    /* METODOS RELATIVOS A VISTA DE ACTIVIDADES */
    public void mostrarVistaActividades() {
        AnchorPane vista = cargarVista("vistaActividades.fxml");

        switch (intervalo) {
            case DIARIA -> mostrarDia();
            case SEMANAL -> mostrarSemana();
            case MENSUAL -> mostrarMes();
        }

        anterior.setOnAction(event -> retroceder());
        siguiente.setOnAction(event -> avanzar());

        vistaCrearEvento.setOnAction(event -> mostrarVistaCrearEvento());

        vistaCrearTarea.setOnAction(event -> mostrarVistaCrearTarea());

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

        mostrarEscenario(vista);
    }

    // Mostrar los distintos intervalos
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

    // Mostrar las distintas actividades
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
            label.setText(String.format("%s [ Inicio: %s | Fin: %s ]", label.getText(), formatoFecha(evento.getInicio()), formatoFecha(evento.getFin())));
            label.setStyle("-fx-background-color: blue; " + "-fx-text-fill: white;");
        } else {
            label.setText(String.format("%s [ Inicio: %s | Fin: %s ]", label.getText(), formatoTiempo(evento.getInicio()), formatoTiempo(evento.getFin())));
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

    // Obtener principios y fines de intervalos
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

    // Darle formato a LocalDate
    public String formatoTiempo(LocalDateTime tiempo) {
        return String.format("%s - %s/%s/%s", tiempo.toLocalTime(), tiempo.getDayOfMonth(), tiempo.getMonthValue(), tiempo.getYear());
    }

    public String formatoFecha(LocalDateTime tiempo) {
        return String.format("%s/%s/%s", tiempo.getDayOfMonth(), tiempo.getMonthValue(), tiempo.getYear());
    }

    public String formatoFecha(LocalDate fecha) {
        return String.format("%s/%s/%s", fecha.getDayOfMonth(), fecha.getMonthValue(), fecha.getYear());
    }

    // Avanzar y retroceder en el tiempo
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

    /* METODOS RELATIVOS A LA VISTA DETALLADA DE EVENTO Y TAREA */
    public void mostrarVistaEvento(Evento evento) {
        AnchorPane vista = cargarVista("vistaEvento.fxml");

        cancelarActividad.setOnAction(event -> mostrarVistaActividades());

        eliminar.setOnAction(event -> eliminarEvento(evento));

        vistaModificarEvento.setOnAction(event -> mostrarVistaModificarEvento(evento));

        mostrarDetalleEvento(evento);

        mostrarEscenario(vista);
    }

    public void mostrarDetalleEvento(Evento evento) {
        Label titulo = new Label();
        Font f1 = new Font(18);
        titulo.setText(evento.getTitulo());
        titulo.setFont(f1);

        Label descripcion = new Label();
        Font f2 = new Font(14);
        descripcion.setText(evento.getDescripcion());
        descripcion.setFont(f2);

        Label diaCompleto = new Label();
        Label inicio = new Label();
        Label fin = new Label();

        if (evento.isDiaCompleto()) {
            diaCompleto.setText("Dia completo ✅");
            inicio.setText("Inicia: " + formatoFecha(evento.getInicio()));
            fin.setText("Finaliza: "  + formatoFecha(evento.getFin()));
        } else {
            diaCompleto.setText("Dia completo ✗");
            inicio.setText("Inicia: " + formatoTiempo(evento.getInicio()));
            fin.setText("Finaliza: " + formatoTiempo(evento.getFin()));
        }

        Label repeticion = new Label();
        if (evento.esRepetido()) {
            repeticion.setText("Se repite ✅");
        } else {
            repeticion.setText("Se repite ✗");
        }

        detalleActividad.getChildren().add(titulo);
        detalleActividad.getChildren().add(descripcion);
        detalleActividad.getChildren().add(new Label());
        detalleActividad.getChildren().add(diaCompleto);
        detalleActividad.getChildren().add(inicio);
        detalleActividad.getChildren().add(fin);
        detalleActividad.getChildren().add(new Label());
        detalleActividad.getChildren().add(repeticion);
        detalleActividad.getChildren().add(new Label());

        mostrarAlarmas(evento);
    }

    public void mostrarVistaTarea(Tarea tarea) {
        AnchorPane vista = cargarVista("vistaTarea.fxml");

        cancelarActividad.setOnAction(event -> mostrarVistaActividades());

        eliminar.setOnAction(event -> eliminarTarea(tarea));

        marcarTarea.setOnAction(event -> {
            cambiarTareaCompletada(tarea);
            mostrarVistaTarea(tarea);
        });

        vistaModificarTarea.setOnAction(event -> mostrarVistaModificarTarea(tarea));

        mostrarDetalleTarea(tarea);

        mostrarEscenario(vista);
    }

    public void mostrarDetalleTarea(Tarea tarea) {
        Label titulo = new Label();
        Font f1 = new Font(18);
        titulo.setText(tarea.getTitulo());
        titulo.setFont(f1);

        Label descripcion = new Label();
        Font f2 = new Font(14);
        descripcion.setText(tarea.getDescripcion());
        descripcion.setFont(f2);

        Label diaCompleto = new Label();
        Label inicio = new Label();

        if (tarea.isDiaCompleto()) {
            diaCompleto.setText("Dia completo ✅");
            inicio.setText("Vence: " + formatoFecha(tarea.getInicio()));
        } else {
            diaCompleto.setText("Dia completo ✗");
            inicio.setText("Vence: " + formatoTiempo(tarea.getInicio()));
        }

        Label completada = new Label();
        if (tarea.isCompletada()) {
            completada.setText("Completada: ✅");
        } else {
            completada.setText("Completada: ✗");
        }

        detalleActividad.getChildren().add(titulo);
        detalleActividad.getChildren().add(descripcion);
        detalleActividad.getChildren().add(new Label());
        detalleActividad.getChildren().add(diaCompleto);
        detalleActividad.getChildren().add(inicio);
        detalleActividad.getChildren().add(new Label());
        detalleActividad.getChildren().add(completada);
        detalleActividad.getChildren().add(new Label());

        mostrarAlarmas(tarea);
    }

    public void mostrarAlarmas(Actividad actividad) {
        if (actividad.getListaAlarmas().size() == 0) {
            detalleActividad.getChildren().add(new Label("Sin alarmas"));
        } else {
            for (Alarma a : actividad.getListaAlarmas()) {
                Label alarma = new Label();
                alarma.setText(String.format("Alarma [ Inicio: %s ]", formatoTiempo(a.getInicio())));
                detalleActividad.getChildren().add(alarma);
            }
        }
    }
    public void cambiarTareaCompletada(Tarea tarea) {
        calendario.marcarTarea(tarea);
        guardarEstado();
    }

    /* METODOS RELATIVOS A CREAR */
    public void mostrarVistaCrearEvento() {
        AnchorPane vista = cargarVista("vistaCrearEvento.fxml");

        crearEvento.setOnAction(event -> crearEvento());

        cancelarEvento.setOnAction(event -> mostrarVistaActividades());

        mostrarEscenario(vista);
    }

    public void mostrarVistaCrearTarea() {
        AnchorPane vista = cargarVista("vistaCrearTarea.fxml");

        crearTarea.setOnAction(event -> crearTarea());

        cancelarTarea.setOnAction(event -> mostrarVistaActividades());

        mostrarEscenario(vista);
    }

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

        Efecto[] efectos = new Efecto[alarmasFormateadas.length];
        Arrays.fill(efectos, Efecto.NOTIFICACION);

        if (seRepite && quantityRepsParsed > 0) {
            Integer frecuenciaRepeticion = Integer.valueOf(frequencyRepeatDaily.getText());
            Repeticion repeticion = new RepeticionDiariaIntervalo(inicioEventoFormateado, quantityRepsParsed, frecuenciaRepeticion);
            calendario.crearEvento(tituloEvento, descripcionEvento, diaCompleto, inicioEventoFormateado, finEventoFormateado, alarmasFormateadas, efectos, repeticion);
            guardarEstado();
        } else if (seRepite) {
            Integer frecuenciaRepeticion = Integer.valueOf(frequencyRepeatDaily.getText());
            Repeticion repeticion = new RepeticionDiariaIntervalo(inicioEventoFormateado, frecuenciaRepeticion);
            calendario.crearEvento(tituloEvento, descripcionEvento, diaCompleto, inicioEventoFormateado, finEventoFormateado, alarmasFormateadas, efectos, repeticion);
            guardarEstado();
        } else {
            calendario.crearEvento(tituloEvento, descripcionEvento, diaCompleto, inicioEventoFormateado, finEventoFormateado, alarmasFormateadas, efectos, null);
            guardarEstado();
        }
        mostrarVistaActividades();
    }

    public void crearTarea() {
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
        guardarEstado();
        mostrarVistaActividades();
    }

    /* METODOS RELATIVOS A MODIFICAR */
    public void mostrarVistaModificarTarea(Tarea tarea) {
        AnchorPane vista = cargarVista("vistaModificarTarea.fxml");

        cancelarModificarTarea.setOnAction(event -> mostrarVistaTarea(tarea));

        modificarTarea.setOnAction(event -> modificarTarea(tarea));

        mostrarEscenario(vista);
    }

    public void mostrarVistaModificarEvento(Evento evento) {
        AnchorPane vista = cargarVista("vistaModificarEvento.fxml");

        cancelarModificarEvento.setOnAction(event -> mostrarVistaEvento(evento));

        modificarEvento.setOnAction(event -> modificarEvento(evento));

        mostrarEscenario(vista);
    }

    public void modificarTarea(Tarea tarea) {
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

    /* METODOS RELATIVOS A ELIMINAR */
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

    /* METODO RELATIVO A CONTROLAR LAS REPETICIONES */
    public void chequearRepeticiones(ActionEvent event) {
        LocalDateTime horaActual = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        calendario.actualizarEventosRepetidos(horaActual);
        guardarEstado();
    }

    /* METODOS RELATIVOS A CONTROLAR LAS ALARMAS */
    public void chequearAlarma(ActionEvent event) {
        LocalDateTime horaActual = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        if (calendario.iniciaProximaAlarma(horaActual)) {
            mostrarAlarma(calendario.obtenerProximaAlarma(horaActual));
        }
    }
    public void mostrarAlarma(Alarma alarma) {
        Actividad actividad = alarma.disparar();
        guardarEstado();

        AnchorPane vista = cargarVista("vistaAlarma.fxml");

        var titulo = new Label(actividad.getTitulo());
        var inicio = new Label(formatoTiempo(actividad.getInicio()));

        detalleAlarma.getChildren().add(titulo);
        detalleAlarma.getChildren().add(inicio);

        var escena = new Scene(vista);
        var ventana = new Stage();
        ventana.setScene(escena);
        ventana.show();
    }

    /* METODOS RELATIVOS A GUARDAR Y RECUPERAR EL ESTADO */
    public void guardarEstado() {
        try {
            var estado = new BufferedOutputStream(new FileOutputStream(ruta));
            calendario.serializar(estado);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Calendario recuperarEstado() {
        Calendario c;
        try {
            var estado = new BufferedInputStream(new FileInputStream(ruta));
            c = Calendario.deserializar(estado);
        } catch (FileNotFoundException e) {
            c = new Calendario();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return c;
    }
}