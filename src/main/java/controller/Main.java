package controller;

import datastorage.ConnectionBuilder;
import datastorage.DAOFactory;
import datastorage.TreatmentDAO;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        mainWindow();
    }

    public void mainWindow() {
        try {
            // Pruefen, ob es Behandlungen in der Datenbank gibt, die geloescht soll (Echtes Loeschen erst nach 10 Jahren).
            // https://www.tutorialspoint.com/java/util/timer_scheduleatfixedrate_delay.htm
            // java-scheduledexecutorservice ( Siehe : https://stackoverflow.com/questions/20387881/how-to-run-certain-task-every-day-at-a-particular-time-using-scheduledexecutorse)
            // Bereinigung von Tabelle Behandlung der Datenbank soll jeden Tag am 02:00 anfangen.
            // Wenn die Tageszeit vor dem Datum von Cron liegt, dann wird Cron morgen durchgefuehrt
            String timeToStart = "02:00:00";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
            SimpleDateFormat formatOnlyDay = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            Date dateToStart = format.parse(formatOnlyDay.format(now) + " at " + timeToStart);
            long diff = dateToStart.getTime() - now.getTime();
            if (diff < 0) {
                // Das Loeschen wird am naeschten Tag durchgefuehrt.
                Date tomorrow = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(tomorrow);
                c.add(Calendar.DATE, 1);
                tomorrow = c.getTime();
                dateToStart = format.parse(formatOnlyDay.format(tomorrow) + " at " + timeToStart);
                diff = dateToStart.getTime() - now.getTime();
            }
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                        try {
                            TreatmentDAO tdao = DAOFactory.getDAOFactory().createTreatmentDAO();
                            tdao.deleteInvalidTreatments();

                        } catch(Exception ex) {
                            ex.printStackTrace();
                        }
                    }, TimeUnit.MILLISECONDS.toSeconds(diff) ,
                    24*60*60, TimeUnit.SECONDS
            );

            Parent root = FXMLLoader.load(getClass().getResource("/LoginView.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

            this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    ConnectionBuilder.closeConnection();
                    Platform.exit();
                    System.exit(0);
                }
            });
        } catch (IOException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}