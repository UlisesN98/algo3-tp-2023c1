import calendario.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

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
    private ChoiceBox<String> opcionesIntervalo;

    @FXML
    private Button cancelarEvento;
    @FXML
    private Button eliminar;
    @FXML
    private VBox detalleEvento;


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

        cancelarEvento.setOnAction(event -> {
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

        detalleEvento.getChildren().add(titulo);
        detalleEvento.getChildren().add(descripcion);
        detalleEvento.getChildren().add(diaCompleto);
        detalleEvento.getChildren().add(inicio);
        detalleEvento.getChildren().add(fin);
        detalleEvento.getChildren().add(repeticion);

        detalleEvento.getChildren().add(new Label("Alarmas"));
        for (Alarma a: evento.getListaAlarmas()) {
            Label alarma = new Label();
            alarma.setText(String.format("Inicio: %s - Efecto: %s", formatoTiempo(a.getInicio()), a.getEfecto()));
            detalleEvento.getChildren().add(alarma);
        }
    }

    public void mostrarVistaTarea(Tarea tarea) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().
                getResource("scena2.fxml"));
        loader.setController(this);
        AnchorPane vista = loader.load();

        cancelarEvento.setOnAction(event -> {
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

        detalleEvento.getChildren().add(titulo);
        detalleEvento.getChildren().add(descripcion);
        detalleEvento.getChildren().add(diaCompleto);
        detalleEvento.getChildren().add(inicio);
        detalleEvento.getChildren().add(fin);
        detalleEvento.getChildren().add(completada);

        detalleEvento.getChildren().add(new Label("Alarmas"));
        for (Alarma a: tarea.getListaAlarmas()) {
            Label alarma = new Label();
            alarma.setText(String.format("Inicio: %s - Efecto: %s", formatoTiempo(a.getInicio()), a.getEfecto()));
            detalleEvento.getChildren().add(alarma);
        }
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