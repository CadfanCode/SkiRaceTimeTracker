package application;
	
import java.util.ArrayList;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Tidtabell");
			
			Skier skier = new Skier("Eymi", 1);
			Skier skier2 = new Skier("Ludvig", 2);									
			Skier skier3 = new Skier("Calle", 3);									
			Skier skier4 = new Skier("Johan", 4);									
			Skier skier5 = new Skier("Erika", 5);									
			Skier skier6 = new Skier("Johannes", 6);									
			
			ArrayList<Double> photoCells = new ArrayList<>();
			photoCells.add(250.00);
			photoCells.add(500.00);
			photoCells.add(800.00);
			
			Track track = new Track(1000, photoCells);
			
			ArrayList<Skier> skiers = new ArrayList<>();
			skiers.add(skier);
			skiers.add(skier2);
			skiers.add(skier3);
			skiers.add(skier4);
			skiers.add(skier5);
			skiers.add(skier6);
			
			Race race = new Race(track, skiers);			
			Pane pane = new Pane();

			ArrayList<Circle> skierNodes = createSkiersNodes(race.getSkiers());
			for (Circle skierNode : skierNodes) {
				pane.getChildren().add(skierNode);
			}
						
			updateSkiers(skierNodes, race);			
			Scene scene = new Scene(pane, 1000, 1000);
			primaryStage.setScene(scene);
			primaryStage.show();						
			race.startRace();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
		
	public ArrayList<Circle> createSkiersNodes(ArrayList<Skier> skiers) {
		ArrayList<Circle> skierNodes = new ArrayList<>();
		
		for (Skier skier : skiers) {
			Circle skierCircle = skier.skierMarker();
			skierNodes.add(skierCircle);
		}
		
		return skierNodes;
	}
	
	public void updateSkiers(ArrayList<Circle> skiers, Race race) {
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
}
