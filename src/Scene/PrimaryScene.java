package Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import be.tarsos.dsp.AudioDispatcher;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.animation.Timeline;
import javafx.animation.AnimationTimer;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Model.ADPModel;
import Model.Note;




public class PrimaryScene extends Application{
	


	
	
    // declare a pane class that holds the graph and the text label
    public class ChartAnnotationOverlay extends Pane {
        private Label mylabel;
        private XYChart<Number, Number> chart;

        // adding the chart and the label nodes as children of the root node(self)
        public ChartAnnotationOverlay (XYChart<Number, Number> chart, Label mylabel, Label mylabel2) {
            this.getChildren().add(chart);
            // align the text labels
            this.getChildren().add(mylabel);
            this.getChildren().add(mylabel2);
            VBox labelGroup = new VBox(mylabel, mylabel2);
            labelGroup.setAlignment(Pos.CENTER_RIGHT);
            this.getChildren().add(labelGroup);
        }
    }

    private static final int MAX_DATA_POINTS = 50;
    private Series series;
    private int xSeriesData = 0;
    private ConcurrentLinkedQueue<Number> dataQ = new ConcurrentLinkedQueue<Number>();
    private ExecutorService executor;
    private ADPModel mymodel;
    private Timeline timeline2;
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private AudioDispatcher adp;
    private ChartAnnotationOverlay myPane;
    private Label mylabel;
    private Label standardNoteLabel;
    private StartScene startScene;
    
	
    private void init(Stage primaryStage) {
        mylabel = new Label("Current Note");
        standardNoteLabel = new Label( "Standard Note");
        xAxis = new NumberAxis(0,MAX_DATA_POINTS,MAX_DATA_POINTS/10);
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);
        yAxis = new NumberAxis();
        yAxis.setAutoRanging(false);
        mymodel = new ADPModel();

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
        myPane  = new ChartAnnotationOverlay(sc, mylabel, standardNoteLabel);
        
        Scene primaryScene = new Scene(myPane);
        
        startScene = new StartScene(primaryStage, primaryScene,executor, mymodel);
        
        primaryStage.setScene(new Scene(startScene,500,500));
        EventHandler closeWindow =  new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				// TODO Auto-generated method stub
				Platform.exit();
                System.exit(0);
			}
        };
        primaryStage.setOnCloseRequest(closeWindow);
    }

    @Override public void start(Stage primaryStage) throws Exception {
    	
    	//-- Prepare Executor Services
        executor = Executors.newCachedThreadPool();
        
        init(primaryStage);
        primaryStage.show();
        
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
        Note standard_n = mymodel.getStandardNote();
        //System.out.println("the current Note Key is "+mymodel.getNote().getKeyNumber());
        if(n != null){
        	Data data = new LineChart.Data(xSeriesData++, n.getKeyNumber());
        	series.getData().add(data);
        	mylabel.setText("Real-Time Pitch: " + n.getNote() + n.getOctave());
        	standardNoteLabel.setText("Standard Note is: " + standard_n.getNote() + standard_n.getOctave());
            // remove points to keep us at no more than MAX_DATA_POINTS
            if (series.getData().size() > MAX_DATA_POINTS) {
                series.getData().remove(0, series.getData().size() - MAX_DATA_POINTS);
            }
            // update
            xAxis.setLowerBound(xSeriesData-MAX_DATA_POINTS);
            xAxis.setUpperBound(xSeriesData-1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
