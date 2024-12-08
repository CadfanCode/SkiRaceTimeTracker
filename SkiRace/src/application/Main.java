package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;

public class Main extends Application {

	// Fields
	TextField nameField;
	VBox raceDistanceVBox;
	TilePane startTypeTilePane;
	CheckBox startIntervalCheckbox;
	ObservableList<Skier> skierList;
	int dist;
	Button startBtn;
	Button stopBtn;
	boolean raceInProgress;
	TableView<Skier> resultsTable = new TableView<>();

	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("SkiRaceTracker");

			// Initialize the list of athletes
			skierList = FXCollections.observableArrayList();

			// --- Top Region ---
			VBox nameFieldVBox = new VBox();
			Label nameFieldLabel = new Label("Enter Athlete's Name");
			nameField = new TextField();
			nameField.setMinWidth(200);
			nameFieldVBox.getChildren().addAll(nameFieldLabel, nameField);
			nameFieldVBox.setAlignment(Pos.CENTER);
			nameFieldVBox.setPadding(new Insets(20));
			nameFieldVBox.setMinSize(200, 75);
			nameFieldVBox.setSpacing(20);

			VBox raceDistanceVBox = new VBox();
			Label raceDistanceLabel = new Label("Choose distance:");
			CheckBox k5 = new CheckBox("5 km");
			CheckBox k10 = new CheckBox("10 km");
			CheckBox k20 = new CheckBox("20 km");
			CheckBox k40 = new CheckBox("40 km");
			raceDistanceVBox.getChildren().addAll(raceDistanceLabel, k5, k10, k20, k40);
			raceDistanceVBox.setPadding(new Insets(20));
			raceDistanceVBox.setMinSize(200, 100);
			raceDistanceVBox.setAlignment(Pos.CENTER);
			raceDistanceVBox.setSpacing(1);

			TilePane startTypeTilePane = new TilePane();
			CheckBox massStart = new CheckBox("Mass Start");
			CheckBox staggeredStart = new CheckBox("Staggered Start");
			CheckBox jaktStart = new CheckBox("Jaktstart");
			startTypeTilePane.getChildren().addAll(massStart, staggeredStart, jaktStart);
			startTypeTilePane.setAlignment(Pos.CENTER);
			startTypeTilePane.setMinSize(300, 75);

			Button addButton = new Button("Add");
			addButton.setOnMouseClicked(event -> {
				if (!nameField.getText().isEmpty() && k5.isSelected() && massStart.isSelected()) {
					skierList.add(new Skier(nameField.getText(), "massStart", 5, skierIDGenerator()));
				} else if (!nameField.getText().isEmpty() && k10.isSelected() && massStart.isSelected()) {
					skierList.add(new Skier(nameField.getText(), "massStart", 10, skierIDGenerator()));
				} else if (!nameField.getText().isEmpty() && k20.isSelected() && massStart.isSelected()) {
					skierList.add(new Skier(nameField.getText(), "massStart", 20, skierIDGenerator()));
				} else if (!nameField.getText().isEmpty() && k40.isSelected() && massStart.isSelected()) {
					skierList.add(new Skier(nameField.getText(), "massStart", 40, skierIDGenerator()));
				} else if (!nameField.getText().isEmpty() && k5.isSelected() && staggeredStart.isSelected()) {
					skierList.add(new Skier(nameField.getText(), "staggeredStart", 5, skierIDGenerator()));
				} else if (!nameField.getText().isEmpty() && k10.isSelected() && staggeredStart.isSelected()) {
					skierList.add(new Skier(nameField.getText(), "staggeredStart", 10, skierIDGenerator()));
				} else if (!nameField.getText().isEmpty() && k20.isSelected() && staggeredStart.isSelected()) {
					skierList.add(new Skier(nameField.getText(), "staggeredStart", 20, skierIDGenerator()));
				} else if (!nameField.getText().isEmpty() && k40.isSelected() && staggeredStart.isSelected()) {
					skierList.add(new Skier(nameField.getText(), "staggeredStart", 40, skierIDGenerator()));
				} else if (!nameField.getText().isEmpty() && k5.isSelected() && jaktStart.isSelected()) {
					skierList.add(new Skier(nameField.getText(), "jaktStart", 5, skierIDGenerator()));
				} else if (!nameField.getText().isEmpty() && k10.isSelected() && jaktStart.isSelected()) {
					skierList.add(new Skier(nameField.getText(), "jaktStart", 10, skierIDGenerator()));
				} else if (!nameField.getText().isEmpty() && k20.isSelected() && jaktStart.isSelected()) {
					skierList.add(new Skier(nameField.getText(), "jaktStart", 20, skierIDGenerator()));
				} else if (!nameField.getText().isEmpty() && k40.isSelected() && jaktStart.isSelected()) {
					skierList.add(new Skier(nameField.getText(), "jaktStart", 40, skierIDGenerator()));
				}
			});

			HBox innerTopBox = new HBox();
			innerTopBox.getChildren().addAll(nameFieldVBox, raceDistanceVBox, startTypeTilePane);

			HBox outerTopBox = new HBox(addButton);
			outerTopBox.setAlignment(Pos.BOTTOM_RIGHT);
			outerTopBox.setPadding(new Insets(10));

			VBox topRegion = new VBox(innerTopBox, outerTopBox);
			topRegion.setStyle("-fx-background-color: #fafaa7");

			// --- Center Region ---
			ArrayList<Double> photoCells = new ArrayList<>();
			photoCells.add(250.00);
			photoCells.add(500.00);
			photoCells.add(800.00);

			Track track = new Track(1000, photoCells);
			Race race = new Race(track, skierList); // Pass skierList to Race
			Pane pane = new Pane();

			ObservableList<Circle> skierNodes = createSkiersNodes(race.getSkiers());
			for (Circle skierNode : skierNodes) {
				pane.getChildren().add(skierNode);
			}

			// Table Columns for Skier
			TableColumn<Skier, String> nameCol = new TableColumn<>("Name");
			nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

			TableColumn<Skier, String> currentTimeCol = new TableColumn<>("Current time");
			currentTimeCol.setCellValueFactory(new PropertyValueFactory<>("currentTime"));  // Refers to getCurrentTime()

			TableColumn<Skier, Integer> distanceTravelledCol = new TableColumn<>("Distance travelled");
			distanceTravelledCol.setCellValueFactory(new PropertyValueFactory<>("distance"));

			TableColumn<Skier, LocalTime> middleTimeCol = new TableColumn<>("Middle time");
			middleTimeCol.setCellValueFactory(new PropertyValueFactory<>("lastMiddleTime"));  // Refers to getLastMiddleTime()

			resultsTable.getColumns().addAll(nameCol, distanceTravelledCol, currentTimeCol, middleTimeCol);
			resultsTable.setItems(skierList);
			
			VBox centerRegion = new VBox();
			centerRegion.getChildren().addAll(pane, resultsTable);

			// --- Bottom Region ---
			HBox bottomRegion = new HBox();
			startBtn = new Button("Start");
			stopBtn = new Button("Stop");

			startBtn.setMinSize(50, 50);
			stopBtn.setMinSize(50, 50);
			startBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
			stopBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
			startBtn.setStyle("-fx-background-color: #d9ceb0");
			stopBtn.setStyle("-fx-background-color: #d9ceb0");

			startBtn.setOnMouseClicked(event -> {
				if (!raceInProgress) {
					raceInProgress = true;
					race.startRace();
					updateSkiers(skierNodes, race);
				}
			});

			stopBtn.setOnMouseClicked(event -> {
				raceInProgress = false; // Logic to stop the race thread can be implemented in Race
			});

			bottomRegion.getChildren().addAll(startBtn, stopBtn);
			bottomRegion.setAlignment(Pos.CENTER);
			bottomRegion.setSpacing(50);
			bottomRegion.setPadding(new Insets(20));
			bottomRegion.setStyle("-fx-background-color: #fafaa7");

			// --- Main Layout ---
			BorderPane borderPane = new BorderPane();
			borderPane.setTop(topRegion);
			borderPane.setCenter(centerRegion); // Display skier visualization
			borderPane.setBottom(bottomRegion);

			Scene scene = new Scene(borderPane, 800, 600);
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	// Helper method to find lead athlete.
	public Skier findLeader(ObservableList<Skier> skierList) {
		if (skierList.isEmpty()) {
			return null;
		}
		Skier leader = skierList.get(0);

		for (int i = 0; i < skierList.size(); i++) {
			Skier currentAthlete = skierList.get(i);
			if (currentAthlete.getDistance() > leader.getDistance()) {
				leader = currentAthlete;
			}
		}
		return leader;
	}

	// Helper method create skier id number.
	public int skierIDGenerator() {
		if (skierList.isEmpty()) {
			return 1; // Start IDs at 1 if the list is empty
		} else {
			return skierList.size() + 1; // Generate the next ID based on the current size
		}
	}


	// Method for skier nodes.
	public ObservableList<Circle> createSkiersNodes(ObservableList<Skier> skiers) {
		ObservableList<Circle> skierNodes = FXCollections.observableArrayList();

		for (Skier skier : skiers) {
			Circle skierCircle = skier.skierMarker();
			skierNodes.add(skierCircle);
		}

		return skierNodes;
	}

	// Method updateSkiers
	public void updateSkiers(ObservableList<Circle> skiers, Race race) {
		Thread thread = new Thread(() -> {
			while(!race.skiersFinished()) {
				try {
					Thread.sleep(10);
					for (Circle circle : skiers) {
						for (Skier skier : race.getSkiers()) {
							if (circle.getId().equals(String.valueOf(skier.getStartNumber()))) {
								circle.setTranslateX(skier.getDistance());
							}
						}
					}
				} catch (InterruptedException ex) {
					System.out.println(ex);
				}
			}
		});

		thread.start();		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
