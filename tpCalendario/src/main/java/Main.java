import calendario.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

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

    private Calendario calendario;
    private LocalDate fechaActual;
    private Frecuencia intervalo;
    private Stage escenario;
    private final String ruta;

    public Main() throws Exception {
        fechaActual = LocalDate.now();
        intervalo = Frecuencia.DIARIA;
        ruta = "calendario";

        try {
            recuperarEstado();
        } catch (IOException error) {
            calendario = new Calendario();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.escenario = stage;
        mostrarVistaActividades();
    }

    // METODOS RELATIVOS A VISTA DE ACTIVIDADES

    private void mostrarVistaActividades() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().
                getResource("scena1.fxml"));
        loader.setController(this);
        AnchorPane vista = loader.load();

        switch (intervalo) {
            case DIARIA -> mostrarDia();
            case SEMANAL -> mostrarSemana();
            case MENSUAL -> mostrarMes();
        }

        anterior.setOnAction(event -> retroceder());
        siguiente.setOnAction(event -> avanzar());

        crearEventScene.setOnAction(event -> {
            try {
                mostrarVistaCrearEvento();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        crearTareaScene.setOnAction(event -> {
            try {
                mostrarVistaCrearTarea();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

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

        label.setOnMouseClicked(event -> {
            try {
                mostrarVistaEvento(evento);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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

        label.setOnMouseClicked(event -> {
            try {
                mostrarVistaTarea(tarea);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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

    public void mostrarVistaEvento(Evento evento) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().
                getResource("scena2.fxml"));
        loader.setController(this);
        AnchorPane vista = loader.load();

        cancelarActividad.setOnAction(event -> {
            try {
                mostrarVistaActividades();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        eliminar.setOnAction(event-> {
            try {
                eliminarEvento(evento);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        mostrarDetalleEvento(evento);

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

    public void mostrarVistaTarea(Tarea tarea) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().
                getResource("scena2.fxml"));
        loader.setController(this);
        AnchorPane vista = loader.load();

        cancelarActividad.setOnAction(event -> {
            try {
                mostrarVistaActividades();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        eliminar.setOnAction(event-> {
            try {
                eliminarTarea(tarea);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        mostrarDetalleTarea(tarea);

        Scene scene = new Scene(vista);
        escenario.setScene(scene);
        escenario.show();
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

    public void mostrarVistaCrearEvento() throws IOException{
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scenaCrearEvento.fxml"));
        loader.setController(this);
        AnchorPane vista = loader.load();

        crearEvento.setOnAction(event -> {
            try {
                crearEvento();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        cancelarEvento.setOnAction(event -> {
            try {
                mostrarVistaActividades();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Create the scene
        Scene scene = new Scene(vista);
        escenario.setScene(scene);
        escenario.show();
    }

    public void mostrarVistaCrearTarea() throws IOException{
        // Load FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ScenaCrearTarea.fxml"));
        loader.setController(this);
        AnchorPane vista = loader.load();

        crearTarea.setOnAction(event -> {
            try {
                crearTarea();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        cancelarTarea.setOnAction(event -> {
            try {
                mostrarVistaActividades();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Create the scene
        Scene scene = new Scene(vista);
        escenario.setScene(scene);
        escenario.show();
    }

    // METODOS RELATIVOS A LA CREACION DE EVENTOS Y TAREAS

    public void crearEvento() throws IOException {
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
                continue;  // Skip empty lines
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
            calendario.crearEvento(tituloEvento, descripcionEvento, diaCompleto, inicioEventoFormateado, finEventoFormateado, alarmasFormateadas, new Efecto[]{Efecto.NOTIFICACION}, repeticion);
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

    public void crearTarea() throws IOException{
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

        calendario.crearTarea(tituloTarea, descripcionTarea, diaCompleto, dateTimeTareaFormateado, alarmasFormateadas, new Efecto[]{Efecto.NOTIFICACION});
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
    public void eliminarEvento(Evento evento) throws IOException {
        calendario.eliminarEvento(evento);
        guardarEstado();
        mostrarVistaActividades();
    }

    public void eliminarTarea(Tarea tarea) throws IOException {
        calendario.eliminarTarea(tarea);
        guardarEstado();
        mostrarVistaActividades();
    }

    // METODOS PARA GUARDAR Y RECUPERAR ESTADO
    public void guardarEstado() throws IOException {
        var estado = new BufferedOutputStream(new FileOutputStream(ruta));
        calendario.serializar(estado);
    }

    public void recuperarEstado() throws IOException, ClassNotFoundException {
        var estado = new BufferedInputStream(new FileInputStream(ruta));
        calendario = Calendario.deserializar(estado);
    }
}