import calendario.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public class Mostrar {

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
    private Button vistaModificarEvento;
    @FXML
    private Button vistaModificarTarea;
    @FXML
    private Button marcarTarea;

    // ATRIBUTOS RELATIVOS A VISTA CREAR EVENTO
    @FXML
    private Button crearEvento;
    @FXML
    private Button cancelarEvento;
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
    private TextField quantityReps;
    @FXML
    private CheckBox completeDayCheckbox;
    @FXML
    private TextArea areaAlarms;

    // ATRIBUTOS RELATIVOS A VISTA CREAR TAREA
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

    // ATRIBUTOS RELATIVOS A VISTA MODIFICAR EVENTO
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


    // ATRIBUTOS RELATIVOS A VISTA MODIFICAR TAREA
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

    // ATRIBUTOS GENERALES
    private final Calendario calendario;
    private final Actualizar actualizar;
    private LocalDate fechaActual;
    private Frecuencia intervalo;
    private final Stage escenario;

    public Mostrar(Calendario calendario, Actualizar actualizar, Stage escenario) {
        this.calendario = calendario;
        this.actualizar = actualizar;
        fechaActual = LocalDate.now();
        intervalo = Frecuencia.DIARIA;
        this.escenario = escenario;
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
    public static String formatoTiempo(LocalDateTime tiempo) {
        return String.format("%s - %s/%s/%s", tiempo.toLocalTime(), tiempo.getDayOfMonth(), tiempo.getMonthValue(), tiempo.getYear());
    }

    public static String formatoFecha(LocalDateTime tiempo) {
        return String.format("%s/%s/%s", tiempo.getDayOfMonth(), tiempo.getMonthValue(), tiempo.getYear());
    }

    public static String formatoFecha(LocalDate fecha) {
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

        eliminar.setOnAction(event -> {
            actualizar.eliminarEvento(evento);
            mostrarVistaActividades();
        });

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

        eliminar.setOnAction(event -> {
            actualizar.eliminarTarea(tarea);
            mostrarVistaActividades();
        });

        marcarTarea.setOnAction(event -> {
            actualizar.cambiarTareaCompletada(tarea);
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

    /* METODOS RELATIVOS A VISTA CREAR */
    public void mostrarVistaCrearEvento() {
        AnchorPane vista = cargarVista("vistaCrearEvento.fxml");

        crearEvento.setOnAction(event -> {
            actualizar.crearEvento(eventTitle, eventDescription, completeDayCheckbox, fieldFechaInicio, startTimeEvent, fieldFechaFin,  endTimeEvent, areaAlarms, dailyRepeatCheck, frequencyRepeatDaily, quantityReps);
            mostrarVistaActividades();
        });

        cancelarEvento.setOnAction(event -> mostrarVistaActividades());

        mostrarEscenario(vista);
    }

    public void mostrarVistaCrearTarea() {
        AnchorPane vista = cargarVista("vistaCrearTarea.fxml");

        crearTarea.setOnAction(event -> {
            actualizar.crearTarea(taskTitle, taskDescription, completeDayCheckbox, endTimeTask, fieldFecha, areaAlarms);
            mostrarVistaActividades();
        });

        cancelarTarea.setOnAction(event -> mostrarVistaActividades());

        mostrarEscenario(vista);
    }

    /* METODOS RELATIVOS A VISTA MODIFICAR */
    public void mostrarVistaModificarTarea(Tarea tarea) {
        AnchorPane vista = cargarVista("vistaModificarTarea.fxml");

        cancelarModificarTarea.setOnAction(event -> mostrarVistaTarea(tarea));

        modificarTarea.setOnAction(event -> {
            actualizar.modificarTarea(tarea, newTitleModifyTask, newDescriptionModifyTask, allDayCheckboxModifyTask, newTimeLimitModifyTask, newDateModifyTask);
            mostrarVistaActividades();
        });

        mostrarEscenario(vista);
    }

    public void mostrarVistaModificarEvento(Evento evento) {
        AnchorPane vista = cargarVista("vistaModificarEvento.fxml");

        cancelarModificarEvento.setOnAction(event -> mostrarVistaEvento(evento));

        modificarEvento.setOnAction(event -> {
            actualizar.modificarEvento(evento, newTitleModifyEvent, newDescriptionModifyEvent, allDayCheckboxModifyEvent, newTimeStartModifyEvent, newDateStartModifyEvent, newTimeEndModifyEvent, newDateEndModifyEvent);
            mostrarVistaActividades();
        });

        mostrarEscenario(vista);
    }
}
