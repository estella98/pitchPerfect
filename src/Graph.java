import be.tarsos.dsp.AudioDispatcher;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.LineChart;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Graph extends Application{
    private static final int MAX_DATA_POINTS = 50;
    private Series series;
    private int xSeriesData = 0;
    private ConcurrentLinkedQueue<Number> dataQ = new ConcurrentLinkedQueue<Number>();
    private ExecutorService executor;
    private AddToQueue addToQueue;
    private Timeline timeline2;
    private NumberAxis xAxis;
    private AudioDispatcher adp;
    private void init(Stage primaryStage, AudioDispatcher myadp) {
        xAxis = new NumberAxis(0,MAX_DATA_POINTS,MAX_DATA_POINTS/10);
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(true);
        adp = myadp;

        //-- Line
        final LineChart<Number, Number> sc = new LineChart<Number, Number>(xAxis, yAxis) {
            // Override to remove symbols on each data point
            @Override protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) {}
        };
        sc.setAnimated(false);
        sc.setId("liveLineChart");
        sc.setTitle("Pitch Real Time");

        //-- Chart Series
        series = new LineChart.Series<Number, Number>();
        series.setName("Pitch Line Series");
        sc.getData().add(series);

        primaryStage.setScene(new Scene(sc));
    }

    @Override public void start(Stage primaryStage) throws Exception {
        init(primaryStage,adp);
        primaryStage.show();

        //-- Prepare Executor Services
        executor = Executors.newCachedThreadPool();
        addToQueue = new AddToQueue();
        executor.execute(addToQueue);
        //-- Prepare Timeline
        prepareTimeline();
    }


    private class AddToQueue implements Runnable {
        public void run() {
            try {
                // add a item of random data to queue
                adp.run();
                Thread.sleep(50);
                executor.execute(this);
            } catch (InterruptedException ex) {
                //Logger.getLogger(LineChartSample.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //-- Timeline gets called in the JavaFX Main thread
    private void prepareTimeline() {
        // Every frame to take any data from queue and add to chart
        new AnimationTimer() {
            @Override public void handle(long now) {
                addDataToSeries();
            }
        }.start();
    }

    private void addDataToSeries() {
        for (int i = 0; i < 20; i++) { //-- add 20 numbers to the plot+
            if (dataQ.isEmpty()) break;
            series.getData().add(new LineChart.Data(xSeriesData++, dataQ.remove()));
        }
        // remove points to keep us at no more than MAX_DATA_POINTS
        if (series.getData().size() > MAX_DATA_POINTS) {
            series.getData().remove(0, series.getData().size() - MAX_DATA_POINTS);
        }
        // update
        xAxis.setLowerBound(xSeriesData-MAX_DATA_POINTS);
        xAxis.setUpperBound(xSeriesData-1);
    }
}
