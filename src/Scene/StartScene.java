package Scene;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Model.ADPModel;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class StartScene extends VBox {
		
		public StartScene(Stage primaryStage, Scene primaryScene, ExecutorService executor,ADPModel mymodel) {
//			this.setPadding(new Insets(0, 0, 400, 400)); 
			
			Label title = new Label("Pitch Perfect");
			title.setTextFill(Color.web("#ffffff"));
			title.setFont(Font.font("Verdana", 45));
			title.setTextOverrun(OverrunStyle.CLIP);
			title.setPrefWidth(300);
			
			BackgroundImage myBackground = new BackgroundImage(new Image("Assets/background.jpg",500,500,false,true),
			        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
			          BackgroundSize.DEFAULT);
			this.setBackground(new Background(myBackground));
			
			ImageView icon = new ImageView("Assets/piano.png");
			icon.setFitHeight(100);
			icon.setFitWidth(100);
			
			Button button = new Button();
			button.setAlignment(Pos.CENTER);
			button.setShape(new Circle(1));
			button.setStyle(
	                "-fx-background-radius: 5em; " +
	                        "-fx-min-width: 100px; " +
	                        "-fx-min-height: 100px; " +
	                        "-fx-max-width: 100px; " +
	                        "-fx-max-height: 100px;"
	                );
			button.setTranslateX(100);
			button.setTranslateY(50);
			button.setGraphic(icon);
			
            
			button.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() { 
		         @Override 
		         public void handle(MouseEvent e) { 
		        	 primaryStage.setScene(primaryScene);
		        	 executor.execute(mymodel);
		         } 
		      });  
			
			this.getChildren().add(title);
			this.getChildren().add(button);
			
			this.setPadding(new Insets(100));
		}
	}

