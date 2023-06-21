import calendario.Actividad;
import calendario.Alarma;
import calendario.Calendario;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Alarmar {

    @FXML
    private VBox detalleAlarma;
    private final Calendario calendario;
    private final String ruta;

    public Alarmar(Calendario calendario, String ruta) {
        this.calendario = calendario;
        this.ruta = ruta;

        javafx.util.Duration segundos = javafx.util.Duration.seconds(1);
        Timeline temporizadorAlarma = new Timeline(new KeyFrame(segundos, this::chequearAlarma));
        temporizadorAlarma.setCycleCount(Timeline.INDEFINITE);
        temporizadorAlarma.play();
    }

    public void chequearAlarma(ActionEvent event) {
        LocalDateTime horaActual = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        if (calendario.iniciaProximaAlarma(horaActual)) {
            Alarma alarma = calendario.obtenerProximaAlarma(horaActual);
            Actividad actividad = alarma.disparar();
            Persistir.guardarEstado(calendario, ruta);
            mostrarAlarma(actividad);
        }
    }

    public void mostrarAlarma(Actividad actividad) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("vistaAlarma.fxml"));
        loader.setController(this);
        AnchorPane vista;
        try {
            vista = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var titulo = new Label(actividad.getTitulo());
        var inicio = new Label(Mostrar.formatoTiempo(actividad.getInicio()));

        detalleAlarma.getChildren().add(titulo);
        detalleAlarma.getChildren().add(inicio);

        var escena = new Scene(vista);
        var ventana = new Stage();
        ventana.setScene(escena);
        ventana.show();
    }
}
