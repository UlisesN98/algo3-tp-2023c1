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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public class Main extends Application {

    @FXML
    private Label fecha;
    @FXML
    private VBox listaActividades;
    @FXML
    private Button siguiente;
    @FXML
    private Button anterior;
    @FXML
    private ChoiceBox<String> opciones;

    private Calendario calendario;
    private LocalDate fechaActual;
    private Frecuencia intervalo;

    public Main() throws Exception {
        fechaActual = LocalDate.now();
        intervalo = Frecuencia.SEMANAL;

        try {
            var archivo = new BufferedInputStream(new FileInputStream("calendario"));
            calendario = Calendario.deserializar(archivo);
        } catch (IOException error) {
            calendario = new Calendario();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {

        calendario.crearTarea("Compras", "Comprar aceite, huevos y pan", false, LocalDateTime.parse("2023-06-01T19:00"), new LocalDateTime[]{LocalDateTime.parse("2023-06-01T18:00")}, new Efecto[]{Efecto.SONIDO});
        calendario.crearEvento("Cumpleaños", null, true, LocalDateTime.parse("2023-06-03T10:00"), LocalDateTime.parse("2023-06-03T22:00"), new Duration[]{Duration.ofHours(1)}, new Efecto[]{Efecto.NOTIFICACION}, null);
        calendario.crearEvento(
                "EyO",
                "Clase de Estructuras y Organizaciones",
                false,
                LocalDateTime.parse("2023-03-17T18:00"),
                LocalDateTime.parse("2023-03-17T22:00"),
                new LocalDateTime[]{LocalDateTime.parse("2023-03-17T14:00"), LocalDateTime.parse("2023-03-17T16:00")},
                new Efecto[]{Efecto.NOTIFICACION, Efecto.SONIDO},
                new RepeticionComun(LocalDateTime.parse("2023-03-17T18:00"), LocalDateTime.parse("2023-06-30T00:00"), Frecuencia.SEMANAL));
        calendario.crearTarea("TP Algo 3", null, true, LocalDateTime.parse("2023-06-15T22:00"), new LocalDateTime[]{}, null);

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

        opciones.getItems().addAll(Frecuencia.DIARIA.toString(), Frecuencia.SEMANAL.toString(), Frecuencia.MENSUAL.toString());
        opciones.setValue(intervalo.toString());
        opciones.setOnAction(event -> {
            String opcionSeleccionada = opciones.getValue();
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
        stage.setScene(scene);
        stage.show();
    }

    private void mostrarActividades(ObservableList<Actividad> actividades) {
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

    private void mostrarEvento(Actividad actividad) {
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
    }

    private void mostrarTarea(Actividad actividad) {
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
    }

    public String formatoTiempo(LocalDateTime tiempo) {
        return String.format("%s - %s/%s/%s", tiempo.toLocalTime(), tiempo.getDayOfMonth(), tiempo.getMonthValue(), tiempo.getYear());
    }

    public String formatoFecha(LocalDateTime tiempo) {
        return String.format("%s/%s/%s", tiempo.getDayOfMonth(), tiempo.getMonthValue(), tiempo.getYear());
    }

    public String formatoFecha(LocalDate fecha) {
        return String.format("%s/%s/%s", fecha.getDayOfMonth(), fecha.getMonthValue(), fecha.getYear());
    }

    private void retroceder() {
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

    private void avanzar() {
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

    private void mostrarDia() {
        listaActividades.getChildren().clear();
        fecha.setText(formatoFecha(fechaActual));
        mostrarActividades(FXCollections.observableList(calendario.buscarPorIntervalo(fechaActual.atStartOfDay(), fechaActual.atStartOfDay().plusDays(1))));
    }

    private void mostrarSemana() {
        listaActividades.getChildren().clear();
        fecha.setText(formatoFecha(obtenerPrincipioSemana(fechaActual)) + " - " + formatoFecha(obtenerFinSemana(fechaActual)));
        mostrarActividades(FXCollections.observableList(calendario.buscarPorIntervalo(obtenerPrincipioSemana(fechaActual).atStartOfDay(), obtenerFinSemana(fechaActual).atStartOfDay().plusDays(1))));
    }

    private void mostrarMes() {
        listaActividades.getChildren().clear();
        fecha.setText(fechaActual.getMonth().toString() + " " + fechaActual.getYear());
        mostrarActividades(FXCollections.observableList(calendario.buscarPorIntervalo(obtenerPrincipioMes(fechaActual).atStartOfDay(), obtenerFinMes(fechaActual).atStartOfDay().plusDays(1))));
    }

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

}