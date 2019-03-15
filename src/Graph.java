import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;
import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
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


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;






public class Graph extends Application{

    // declare a pane class that holds the graph and the text label
    public class ChartAnnotationOverlay extends Pane {
        private Label _mylabel;
        private XYChart<Number, Number> _chart;

        // adding the chart and the label nodes as children of the root node(self)
        public ChartAnnotationOverlay (XYChart<Number, Number> chart, Label mylabel) {
            this.getChildren().add(chart);
            this.getChildren().add(mylabel);
        }
    }

    private static final int MAX_DATA_POINTS = 50;
    private Series series;
    private int xSeriesData = 0;
    private ConcurrentLinkedQueue<Number> dataQ = new ConcurrentLinkedQueue<Number>();
    private ExecutorService executor;
    private Model mymodel;
    private Timeline timeline2;
    private NumberAxis xAxis;
    private AudioDispatcher adp;
    private ChartAnnotationOverlay myPane;
    private Label mylabel;
    private void init(Stage primaryStage, AudioDispatcher myadp) {
        mylabel = new Label("Current Note ");
        xAxis = new NumberAxis(0,MAX_DATA_POINTS,MAX_DATA_POINTS/10);
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(false);
        mymodel = new Model();

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
        myPane  = new ChartAnnotationOverlay(sc, mylabel);
        primaryStage.setScene(new Scene(myPane));
    }

    @Override public void start(Stage primaryStage) throws Exception {
        init(primaryStage,adp);
        primaryStage.show();

        //-- Prepare Executor Services
        executor = Executors.newCachedThreadPool();
        executor.execute(mymodel);
        //-- Prepare Timeline
        prepareTimeline();
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
        Note n = mymodel.getNote();
        //System.out.println("the current Note Key is "+mymodel.getNote().getKeyNumber());
        series.getData().add(new LineChart.Data(xSeriesData++, n.getKeyNumber()));
        mylabel.setText(n.getNote() + n.getOctave());
        // remove points to keep us at no more than MAX_DATA_POINTS
        if (series.getData().size() > MAX_DATA_POINTS) {
            series.getData().remove(0, series.getData().size() - MAX_DATA_POINTS);
        }
        // update
        xAxis.setLowerBound(xSeriesData-MAX_DATA_POINTS);
        xAxis.setUpperBound(xSeriesData-1);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
