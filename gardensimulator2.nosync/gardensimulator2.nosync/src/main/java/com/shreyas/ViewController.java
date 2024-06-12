package com.shreyas;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static com.shreyas.Pest.pests;
import static com.shreyas.Plant.plantImageViewMap;
import static com.shreyas.Plant.plantsList;

public class ViewController {
    private static final Logger log = LogManager.getLogger(GardenController.class);
    public static final Image sunflowerImage = new Image(Objects.requireNonNull(ViewController.class.getResourceAsStream("/images/sunflower.jpg")));
    public static final Image lilyImage = new Image(Objects.requireNonNull(ViewController.class.getResourceAsStream("/images/lily.jpg")));
    public static final Map<Pest, ImageView> pestImageViewMap = new HashMap<>();
    private static final Image roseImage = new Image(Objects.requireNonNull(ViewController.class.getResourceAsStream("/images/rose.jpg")));
    private static final Image soilImage = new Image("file:OIP.jfif");
    private static final Image pestImage = new Image(Objects.requireNonNull(ViewController.class.getResourceAsStream("/images/bug.jpg")));
    public static Set<String> occupiedCells = new HashSet<>();
    public static ArrayList<String> occupiedPestCells = new ArrayList<>();
    private HeatingController heatingController;
    private SprinklerController sprinklerController;
    private PesticideController pesticideController;
    private RainController rainController;


    public static int day = 1;
    public Button pressToPlayButton;
    public Button iterateDayButton;
    @FXML
    private GridPane gardenGrid;
    @FXML
    private GridPane weatherGrid;
    @FXML
    private Pane imagePane;
    @FXML
    private RadioButton roseButton;
    @FXML
    private RadioButton sunflowerButton;
    @FXML
    private RadioButton lilyButton;
    @FXML
    private HBox imageBox = new HBox();
    @FXML
    private HBox weatherBox = new HBox();
    private Timeline timeline;
    @FXML
    private Label userInfoLabel;
    @FXML
    private Label label2;
    @FXML
    private Label systemLabel;
    @FXML
    private Button heatingButton;
    @FXML
    private Button sprinklersButton;
    @FXML
    private Button pesticideButton;
    @FXML
    private Label statusLabel;
    @FXML
    private Button rainButton;

    public ViewController() {
        heatingController = new HeatingController();
        sprinklerController = new SprinklerController();
        pesticideController = new PesticideController();
        rainController = new RainController();
    }

    //here we initialize our garden: fill all the cells with soil, set the first weather, and first day
    @FXML
    public void initializeGarden() throws FileNotFoundException {
        int rows = gardenGrid.getRowCount() + 1;
        int cols = gardenGrid.getColumnCount() + 1;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                HBox imageBox = new HBox();
                ImageView soilView = new ImageView();
                soilView.setFitHeight(25);
                soilView.setFitWidth(25);
                soilView.setImage(soilImage);
                imageBox.getChildren().add(soilView);
                gardenGrid.add(imageBox, row, col);
                userInfoLabel.setText("   Today is Day 1");
            }
        }
        pressToPlayButton.setDisable(true);
    }

    //this is our method to call each plant method depending on which button is selected
    @FXML
    public void plantPlants() {
        log.info("Planting plants");
        EventHandler<MouseEvent> plantHandler = event -> {
            Node node = (Node) event.getSource();
            int row = GridPane.getRowIndex(node);
            int col = GridPane.getColumnIndex(node);
            try {
                if (roseButton.isSelected()) {
                    plantRose(row, col);
                }
                if (sunflowerButton.isSelected()) {
                    plantSunflower(row, col);
                }
                if (lilyButton.isSelected()) {
                    plantLily(row, col);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        };
        for (Node node : gardenGrid.getChildren()) {
            node.setOnMouseClicked(plantHandler);
        }
    }

    //planting a Rose ; creating rose object and adding it to the gird
    public void plantRose(int row, int col) throws FileNotFoundException {
        String roseCell = row + "," + col;
        if (occupiedCells.contains(roseCell)) {
            return;
        }
        HBox imageBox = (HBox) gardenGrid.getChildren().get(col * gardenGrid.getRowCount() + (row + 1));
        ImageView plantView = new ImageView();
        plantView.setFitHeight(25);
        plantView.setFitWidth(25);
        plantView.setImage(roseImage);
        imageBox.getChildren().add(plantView);
        occupiedCells.add(roseCell);
        Rose rose = new Rose(gardenGrid);
        plantImageViewMap.put(rose, plantView);
        plantsList.add(rose);
    }


    //planting a Sunflower; create Sunflower object and add it to the grid
    public void plantSunflower(int row, int col) throws FileNotFoundException {
        String cell = row + "," + col;
        if (occupiedCells.contains(cell)) {
            return;
        }
        HBox imageBox = (HBox) gardenGrid.getChildren().get(col * gardenGrid.getRowCount() + (row + 1));
        ImageView plantView = new ImageView();
        plantView.setFitHeight(25);
        plantView.setFitWidth(25);
        plantView.setImage(sunflowerImage);
        imageBox.getChildren().add(plantView);
        occupiedCells.add(cell);
        Sunflower sunflower = new Sunflower(gardenGrid);
        plantImageViewMap.put(sunflower, plantView);
        plantsList.add(sunflower);
    }

    //planting a Lily; create lily object and add it to the grid
    public void plantLily(int row, int col) throws FileNotFoundException {
        String cell = row + "," + col;
        if (occupiedCells.contains(cell)) {
            return;
        }
        HBox imageBox = (HBox) gardenGrid.getChildren().get(col * gardenGrid.getRowCount() + (row + 1));
        ImageView plantView = new ImageView();
        plantView.setFitHeight(25);
        plantView.setFitWidth(25);
        plantView.setImage(lilyImage);
        imageBox.getChildren().add(plantView);
        occupiedCells.add(cell);
        Lily lily = new Lily(gardenGrid);
        plantImageViewMap.put(lily, plantView);
        plantsList.add(lily);
    }
    public void activateHeating() {
        int temperature = heatingController.activateHeating();
        // Update UI with temperature
    }

    public void activateSprinklers() {
        sprinklerController.activateSprinklers(plantsList);
        // Update UI to indicate sprinklers are active
    }

    public void applyPesticide() {
        pesticideController.applyPesticide(plantsList);
        // Update UI to indicate pesticide is applied
    }
    @FXML
    private void activateRain(ActionEvent event) {
        int rainfallAmount = 10; // Set the desired rainfall amount
        rainController.simulateRain(rainfallAmount, plantsList);
    }
    //remove plant object from grid
    public void die() {
        ArrayList<Plant> plants = new ArrayList<>();

        for (Plant plant : plantsList) {
            int col = plant.getCol();
            int row = plant.getRow();
            String cell = row + "," + col;
            ImageView plantView = plantImageViewMap.get(plant);

            if (plantView != null) {
                // remove ImageView from grid
                HBox imageBox = (HBox) plantView.getParent();
                imageBox.getChildren().remove(plantView);

                // remove association between Flower object and ImageView
                plantImageViewMap.remove(plant);
                //occupiedCells.remove(cell);
                occupiedCells.remove(cell);
                plants.add(plant);
            }
        }

        plantsList.removeAll(plants);
    }


    //incrementing each day and calling appropriate methods to run each day
    public void iterateDay() throws IOException {
        day++;
        userInfoLabel.setText("Today is Day " + day);
//        waterHeatPlant();
//        die();
        //removePestsFromCells();
        pestControl();
        pestKillPlant();
        addPestsToCells();
        if (!occupiedCells.isEmpty()) {
            //pests();
        }
    }

    public void iterateDayWithTimer() throws InterruptedException, IOException {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<>() {
            int i = 0;

            @Override
            public void handle(ActionEvent event) {
                try {
                    iterateDay();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                i++;
            }
        }));
        timeline.setCycleCount(100);
        timeline.play();
        iterateDayButton.setDisable(true);
    }

    public void finish() {
        System.exit(0);
    }

    public void addPestsToCells() {
        for (String cell : occupiedCells) {
            Random random = new Random();
            int num = random.nextInt(3);
            if (num == 1) {
                String[] parts = cell.split(",");
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);
                HBox imageBox = (HBox) gardenGrid.getChildren().get(col * gardenGrid.getRowCount() + (row + 1));
                ImageView pestView = new ImageView();
                pestView.setFitHeight(25);
                pestView.setFitWidth(25);
                pestView.setImage(pestImage);
                imageBox.getChildren().add(pestView);
                Pest pest = new Pest(row, col, 0);
                pestImageViewMap.put(pest, pestView);
                occupiedPestCells.add(cell);
                pests.add(pest);
                for (Plant plant : Plant.plantsList) {
                    if (plant.getRow() == row && plant.getCol() == col) {
                        plant.numPests++;
                    }
                }
            }
        }
    }

    public void pestKillPlant() {
        List<Plant> plantsToRemove = new ArrayList<>();
        List<Pest> pestsToRemove = new ArrayList<>();
        for (Plant plant : plantsList) {
            if (plant.numPests >= 2) {
                int col = plant.getCol();
                int row = plant.getRow();
                String cell = row + "," + col;
                ImageView plantView = plantImageViewMap.get(plant);

                if (plantView != null) {
                    // remove ImageView from grid
                    HBox imageBox = (HBox) plantView.getParent();
                    imageBox.getChildren().remove(plantView);

                    // remove association between Flower object and ImageView
                    plantImageViewMap.remove(plant, plantView);
                    occupiedCells.remove(cell);
                    plantsToRemove.add(plant);

                    for (Pest pest : pests) {
                        if (pest.getRow() == row && pest.getCol() == col) {
                            ImageView pestView = pestImageViewMap.get(pest);
                            imageBox.getChildren().remove(pestView);
                            pestImageViewMap.remove(pest, pestView);
                            pestsToRemove.add(pest);
                        }
                    }
                }
            }
        }
        Plant.plantsList.removeAll(plantsToRemove);
        pests.removeAll(pestsToRemove);
    }

    public void pestControl() {
        List<Pest> pestsToRemove = new ArrayList<>();

        for (Pest pest : pests) {
            Random ran = new Random();
            int rando = ran.nextInt(8);

            String cell = pest.getRow() + "," + pest.getCol();
            if (rando != 1) {
                ImageView spiderView = pestImageViewMap.get(pest);

                if (spiderView != null) {
                    // remove ImageView from grid
                    HBox imageBox = (HBox) spiderView.getParent();
                    imageBox.getChildren().remove(spiderView);
                    pestsToRemove.add(pest);

                    pestImageViewMap.remove(pest);
                    occupiedPestCells.remove(cell);
                }
            }
        }
        pests.removeAll(pestsToRemove);
    }

    //sprinkler and heating system for garden; automatically waters every 2 days and heats every 3 days
//    public void waterHeatPlant() {
//        int rows = weatherGrid.getRowCount();
//        int cols = weatherGrid.getColumnCount();
//        clearTable();
//        for (int row = 0; row < rows; row++) {
//            for (int col = 0; col < cols; col++) {
//                HBox weatherBox = new HBox();
//                ImageView rainView = new ImageView();
//                rainView.setFitHeight(40);
//
//                if (day % 2 == 0 && day % 3 == 0) { //both heat and sprinkler
//                    rainView.setFitWidth(55);
//                    rainView.setImage(sunWaterImage);
//                    weatherBox.getChildren().add(rainView);
//                    weatherGrid.add(weatherBox, row, col);
//                    systemLabel.setText("Sprinklers ON \n Heater ON");
//
//                } else if (day % 2 == 0) { //sprinkler system
//                    rainView.setFitWidth(40);
//                    rainView.setImage(waterImage);
//                    weatherBox.getChildren().add(rainView);
//                    weatherGrid.add(weatherBox, row, col);
//                    systemLabel.setText("Sprinklers ON");
//                } else if (day % 3 == 0) { //heat system
//                    rainView.setFitWidth(40);
//                    rainView.setImage(sunImage);
//                    weatherBox.getChildren().add(rainView);
//                    weatherGrid.add(weatherBox, row, col);
//                    systemLabel.setText("Heater ON");
//                } else {
//                    systemLabel.setText(" ");
//                    clearTable();
//                }
//            }
//        }
//        if (day % 2 == 0 && day % 3 == 0) {
//            log.info("Sprinkler and heating system ON today");
//        } else if (day % 2 == 0) {
//            log.info("Sprinkler system ON today");
//        } else if (day % 3 == 0) {
//            log.info("Heating system ON today");
//        }
//    }

//    //removing heating and watering images
//    public void clearTable() {
//        weatherGrid.getChildren().clear();
//    }

//    //randomly choosing and displaying a weather in the garden each day
//    public void chooseWeather(){
//        int randomChoice = (int)(Math.random()*3+1);
//        ImageView weatherView = new ImageView();
//        weatherView.setFitHeight(330);
//        weatherView.setFitWidth(450);
//        if(randomChoice==1) {
//            weatherView.setImage(sunnyGardenImage);
//            imagePane.getChildren().add(weatherView);
//            label2.setText("Today it is sunny!");
//            log.info("It was sunny in the garden today!");
//        }
//        else if(randomChoice==2){
//            weatherView.setImage(rainyGardenImage);
//            imagePane.getChildren().add(weatherView);
//            label2.setText("Today it is rainy");
//            log.info("It was rainy in the garden today");
//        }
//        else{
//            weatherView.setImage(cloudyGardenImage);
//            imagePane.getChildren().add(weatherView);
//            label2.setText("Today it is cloudy");
//            log.info("It was cloudy in the garden today");
//        }
//
//    }
//
//    public void clearGarden() {
//        gardenGrid.getChildren().clear();
//    }
///*
//    public void burn() throws FileNotFoundException, InterruptedException {
//        ImageView burnView = new ImageView();
//        burnView.setFitHeight(330);
//        burnView.setFitWidth(450);
//        burnView.setImage(fireImage);
//        imagePane.getChildren().add(burnView);
//        label2.setText("YOU BURNED THE GARDEN!");
//        log.info("You burned down the garden today");
//
//    }

// */

}
