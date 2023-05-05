package com.example.demo;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

class FallingObject {
    ImageView imageView;
    double x;
    double y;
    double dy;

    public FallingObject(ImageView imageView, double x, double y, double dy) {
        this.imageView = imageView;
        this.x = x;
        this.y = y;
        this.dy = dy;
    }
}

public class p2 extends Application {
    private List<FallingObject> fallingObjects = new ArrayList<>();
    private ImageView imageViewObj;
    private Text Score;
    File scores = new File("src/scores.txt");
    ArrayList<Integer> ar = new ArrayList<>();
    //File unSorted = new File("unsortedStudents.txt");
    private int objectValue =0 ;
    private int objectValues =0 ;
    private int intLeftNum = 30;
    private String[] objects = {"obj_1.png","obj_2.png","obj_3.png"};

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();

        Image wallpaperImage = new Image("Wallpaper.PNG");
        ImageView imageView = new ImageView(wallpaperImage);

        Image startWallpaperImage = new Image("StartWallpaper.png");
        ImageView startImageView = new ImageView(startWallpaperImage);

        Image startBtnImage = new Image("StartBtn.png");
        ImageView startBtnImageView = new ImageView(startBtnImage);

        startBtnImageView.setOnMouseEntered(e -> {
            startBtnImageView.setOpacity(0.8);
            startBtnImageView.setScaleX(1.1);
            startBtnImageView.setScaleY(1.1);
        });

        startBtnImageView.setOnMouseExited(e -> {
            startBtnImageView.setOpacity(1.0);
            startBtnImageView.setScaleX(1.0);
            startBtnImageView.setScaleY(1.0);

        });

        Score = new Text(706, 44, String.valueOf(objectValues));
        Score.setFont(new Font(29));

        startBtnImageView.setOnMouseClicked(e -> {
            resetGame(pane);
            pane.getChildren().removeAll(startBtnImageView, startImageView);
            pane.getChildren().addAll(imageView, Score);

            for (int i = 0; i < 3; i++) {
                addNewFallingObject(pane);
            }

            startAnimationLoop();
        });

        pane.getChildren().addAll(startImageView, startBtnImageView);
        Scene scene = new Scene(pane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ClickGame");
        primaryStage.show();
    }

    public void startAnimationLoop() {
        AnimationTimer animationTimer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                for (FallingObject fallingObject : fallingObjects) {
                    fallingObject.y += fallingObject.dy;
                    fallingObject.imageView.setTranslateY(fallingObject.y);
                    fallingObject.imageView.setTranslateX(fallingObject.x);

                    if (fallingObject.imageView.getTranslateY() >= 600.) {
                        intLeftNum--;
                        resetFallingObject(fallingObject);
                    }

                    fallingObject.imageView.setOnMouseClicked(e -> {
                        ImageView clickedImageView = (ImageView) e.getSource();
                        String imageName = clickedImageView.getImage().getUrl();
                        String objectName = null;
                        for (String object : objects) {
                            if (imageName.contains(object)) {
                                objectName = object;
                                break;
                            }
                        }
                        if (objectName== objects[0]){
                            objectValue = 3 ;
                        } else if (objectName== objects[1]) {
                            objectValue = -2 ;
                        } else if (objectName== objects[2]) {
                            objectValue = 1 ;
                        }
                        intLeftNum--;
                        objectValues += objectValue ;
                        Score.setText(String.valueOf(objectValues));
                        Pane parentPane = (Pane) fallingObject.imageView.getParent();
                        parentPane.getChildren().remove(fallingObject.imageView);
                        fallingObjects.remove(fallingObject);
                        addNewFallingObject(parentPane);
                    });

                    if (intLeftNum == 0) {

                        stop();
                        finalWallpaper();
                        try {
                            addScore(objectValues);
                        } catch (FileNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        };
        animationTimer.start();
    }

    public void addNewFallingObject(Pane parentPane) {
        Image image = selectRandomObject();
        imageViewObj = new ImageView(image);
        double x = Math.random() * 720;
        double y = 56;
        double dy = 2;
        FallingObject fallingObject = new FallingObject(imageViewObj, x, y, dy);
        fallingObjects.add(fallingObject);
        parentPane.getChildren().add(imageViewObj);
        // set tag for the ImageView
        String imageName = image.getUrl().substring(image.getUrl().lastIndexOf('/') + 1);
        imageViewObj.setUserData(imageName);
    }


    public void resetFallingObject(FallingObject fallingObject) {
        fallingObject.y = 56;
        fallingObject.x = Math.random() * 720;
        fallingObject.dy = fallingObject.dy * 1.1;
        fallingObject.imageView.setImage(selectRandomObject());
    }

    public void finalWallpaper() {
        Pane pane = (Pane) imageViewObj.getParent();
        if (pane != null) {
            pane.getChildren().removeAll(imageViewObj, Score);

            // Add the final wallpaper
            Image finalWallpaperImage = new Image("FinalWallpapere.png");
            ImageView finalImageView = new ImageView(finalWallpaperImage);
            pane.getChildren().add(finalImageView);

            // Add a "Play Again" button
            Image playAgainBtnImage = new Image("TryAgainBtn.png");
            ImageView playAgainBtnImageView = new ImageView(playAgainBtnImage);


            playAgainBtnImageView.setOnMouseClicked(e -> {
                resetGame(pane);
                pane.getChildren().removeAll(playAgainBtnImageView, finalImageView);
                pane.getChildren().add(Score);

                for (int i = 0; i < 3; i++) {
                    addNewFallingObject(pane);
                }
                startAnimationLoop();
            });
            pane.getChildren().add(playAgainBtnImageView);


        }
    }
    public void resetGame(Pane pane) {
        intLeftNum = 30;
        objectValues = 0 ;
        Score.setText(String.valueOf(objectValues));
        for (FallingObject fallingObject : fallingObjects) {
            pane.getChildren().remove(fallingObject.imageView);
        }
        fallingObjects.clear();
    }
    public Image selectRandomObject() {
        int randomIndex = (int) (Math.random() * objects.length);
        Image image = new Image(objects[randomIndex]);
        return image;
    }

    public void addScore(int score) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new File("src/scores.txt"));
        if (scores.exists()) {
            Scanner scanner = new Scanner(scores);
            while (scanner.hasNextInt()) {
                ar.add(scanner.nextInt());
            }
            scanner.close();
        }
        ar.add(score);
        Collections.sort(ar);
        Collections.reverse(ar);
        for (int i = 0; i < ar.size(); i++) {
            writer.println(ar.get(i));
        }
        writer.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}