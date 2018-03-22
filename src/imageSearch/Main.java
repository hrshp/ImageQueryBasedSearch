package imageSearch;

import javafx.scene.image.Image;

import java.io.File;

import javafx.application.Application;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Main extends Application {

	private FileChooser fileChooser;
	private File file = null;
	private HBox top;
	private VBox mainLayout, linksLayout, controlLayout;
	private ScrollPane mainScroll, linkScroll;
	private Scene scene;
	private Button uploadButton;
	private Button detectButton;
	private String fileName = "";
	private String filePath = "";
	private ImageView imageView;
	private ProgressIndicator pi;
	private String[] tags;
	private Button[] tagButton;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub		
	
		// imageView
		imageView = new ImageView();
		imageView.setX(10);
		imageView.setY(10);
		imageView.setFitHeight(500);
		imageView.setFitWidth(500);
		imageView.setPreserveRatio(true);
		
		// fileChooser
		fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.jpeg")
				);
		
		// upload button
		uploadButton = new Button("browse");
		uploadButton.setOnAction(e -> {
			file = fileChooser.showOpenDialog(stage);
			if (file != null) {
				fileName = file.getName();
				filePath = file.getAbsolutePath();
				System.out.println(fileName + " " + filePath);
				imageView.setImage(new Image(new File(filePath).toURI().toString()));
			}
		});
		
		// progress indicator
		pi = new ProgressIndicator();
		pi.setProgress(0);
		
		// detect button
		detectButton = new Button("detect");
		detectButton.setOnAction(e -> {
			if (file == null) {
				System.out.println("upload a file");
			}
			else {
				controlLayout.getChildren().add(pi);				
				MyService myService = new MyService(filePath);
				myService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
		            @Override
		            public void handle(WorkerStateEvent t) {
		            	pi.progressProperty().unbind();
		            	controlLayout.getChildren().remove(pi);
		            	imageView.setImage(new Image(new File("/home/aditi/DarkNet/darknet/predictions.png").toURI().toString()));
		            	tags = myService.tags;
		            	updateUI(tags);
		            }
		        });
				
				myService.setOnReady(new EventHandler<WorkerStateEvent>() {

					@Override
					public void handle(WorkerStateEvent arg0) {
						// TODO Auto-generated method stub
						controlLayout.getChildren().add(pi);
					}
				});
								
				pi.progressProperty().bind(myService.progressProperty());
				
				
				myService.start();	
			}
		});
		
		mainLayout = new VBox();
		
		top = new HBox();
		linksLayout = new VBox();
		
		controlLayout = new VBox();
		controlLayout.getChildren().addAll(uploadButton, detectButton);
		
		mainScroll = new ScrollPane();
		mainScroll.setContent(mainLayout);
		mainScroll.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		mainScroll.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		
		linkScroll = new ScrollPane();
		linkScroll.setContent(linksLayout);
		linkScroll.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		linkScroll.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		
		top.getChildren().addAll(imageView, controlLayout);
		
		mainLayout.getChildren().addAll(top, linkScroll);
		
		
		
		
		
//		mainLayout = new BorderPane();
//
//		mainLayout.setCenter(imageView);
//
//		leftLayout = new VBox();		
//		leftLayout.getChildren().addAll(uploadButton, detectButton);		
//		mainLayout.setLeft(leftLayout);
//		
//		bottomLayout = new VBox();
////		scrollPane = new ScrollPane();
////		scrollPane.setContent(bottomLayout);
////		
////		mainLayout.setBottom(scrollPane);
		
		scene = new Scene(mainScroll, 1200, 700);
		
		stage.setScene(scene);
		stage.show();
	}
	
	public void updateUI(String[] tags) {
		tagButton = new Button[tags.length];
		for (int i=0 ; i<tags.length ; ++i) {
			tagButton[i] = new Button(tags[i]);
			final String tag = tags[i];
			tagButton[i].setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	            	WebParserService webParserService = new WebParserService(tag, linksLayout);
	            	webParserService.setOnCompleterListener(new OnCompleteListener() {
						
						@Override
						public void updateUI(Node node) {
							// TODO Auto-generated method stub
							linksLayout.getChildren().add(node);
						}
					});
	            	webParserService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

						@Override
						public void handle(WorkerStateEvent arg0) {
							// TODO Auto-generated method stub
							System.out.println("completed web search!");
							
						}
					});
	            	webParserService.start();
	            }
	        });
			controlLayout.getChildren().add(tagButton[i]);
		}
	}
	
	
}


