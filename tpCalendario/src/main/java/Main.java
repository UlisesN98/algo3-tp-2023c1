import calendario.*;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main extends Application {

    @FXML
    private Label fecha;

    @FXML
    private VBox listaActividades;

    @Override
    public void start(Stage stage) throws Exception {

        Calendario calendario;
        try {
            var archivo = new BufferedInputStream(new FileInputStream("calendario"));
            calendario = Calendario.deserializar(archivo);
        } catch (IOException error) {
            calendario = new Calendario();
        }

        calendario.crearTarea("Compras", "Comprar aceite, huevos y pan", false, LocalDateTime.parse("2023-06-01T19:00"), new LocalDateTime[]{LocalDateTime.parse("2023-06-01T18:00")}, new Efecto[]{Efecto.SONIDO});
        calendario.crearEvento("Cumpleaños", null, true, LocalDateTime.parse("2023-06-03T10:00"), LocalDateTime.parse("2023-06-03T22:00"), new Duration[]{Duration.ofHours(1)}, new Efecto[]{Efecto.NOTIFICACION}, null);
        calendario.crearEvento("EyO", "Clase de Estructuras y Organizaciones", false, LocalDateTime.parse("2023-03-17T18:00"), LocalDateTime.parse("2023-03-17T22:00"), new LocalDateTime[]{LocalDateTime.parse("2023-03-17T14:00"), LocalDateTime.parse("2023-03-17T16:00")}, new Efecto[]{Efecto.NOTIFICACION, Efecto.SONIDO}, new RepeticionComun(LocalDateTime.parse("2023-03-17T18:00"), LocalDateTime.parse("2023-06-30T23:59"), Frecuencia.SEMANAL));
        calendario.crearTarea("TP Algo 3", null, true, LocalDateTime.parse("2023-06-15T22:00"), new LocalDateTime[]{}, null);

        FXMLLoader loader = new FXMLLoader(getClass().
                getResource("scena1.fxml"));
        loader.setController(this);
        AnchorPane vista = loader.load();

        fecha.setText(LocalDate.now().toString());
        mostrarActividades(FXCollections.observableList(calendario.buscarPorIntervalo(LocalDate.now().atStartOfDay(), LocalDate.now().atTime(23, 59))));

        Scene scene = new Scene(vista);
        stage.setScene(scene);
        stage.show();
    }

    private void mostrarActividades(ObservableList<Actividad> actividades) {
        for (Actividad a : actividades) {
            if (a instanceof Evento) {
                mostrarEvento((Evento) a);
            }
            if (a instanceof Tarea) {
                mostrarTarea((Tarea) a);
            }
        }
    }

    private void mostrarEvento(Evento evento) {
        Label label = new Label();
        label.setText(evento.getTitulo());

        if (evento.isDiaCompleto()) {
            label.setText(String.format("%s < Inicio: %s | Fin: %s >",  label.getText(), formatoFecha(evento.getInicio()), formatoFecha(evento.getFin().minusDays(1))));
            label.setStyle("-fx-background-color: blue; " + "-fx-text-fill: white;");
        } else {
            label.setText(String.format("%s < Inicio: %s | Fin: %s >",  label.getText(), formatoTiempo(evento.getInicio()), formatoTiempo(evento.getFin())));
            label.setStyle("-fx-text-fill: blue;");
        }

        listaActividades.getChildren().add(label);
    }

    private void mostrarTarea(Tarea tarea) {
        Label label = new Label();
        label.setText(tarea.getTitulo());


        if (tarea.isDiaCompleto()) {
            label.setText(String.format("%s < Limite: %s", label.getText(), formatoFecha(tarea.getInicio().minusDays(1))));
            label.setStyle("-fx-background-color: red; " + "-fx-text-fill: white;");
        } else {
            label.setText(String.format("%s < Limite: %s", label.getText(), formatoTiempo(tarea.getInicio())));
            label.setStyle("-fx-text-fill: red;");
        }

        if (tarea.isCompletada()) {
            label.setText(String.format("%s | Completada: ✅ >", label.getText()));
        } else {
            label.setText(String.format("%s | Completada: ✗ >", label.getText()));
        }

        listaActividades.getChildren().add(label);
    }

    public String formatoTiempo(LocalDateTime tiempo) {
        return String.format("%s - %s/%s/%s", tiempo.toLocalTime(), tiempo.getDayOfMonth(), tiempo.getMonthValue(), tiempo.getYear());
    }

    public String formatoFecha(LocalDateTime tiempo) {
        return String.format("%s/%s/%s", tiempo.getDayOfMonth(), tiempo.getMonthValue(), tiempo.getYear());
    }

}