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
import java.io.*;
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
    private Text scoreText;
    ArrayList<Integer> topScore = new ArrayList<>();
    private int objectValue =0 ;
    private int objectValues =0 ;
    private int intLeftNum = 30;
    private int yCordinateTopScore = 180;
    private int xCordinateTopScore = 300;
    double dy = 2;

    List<Text> scoreTextList = new ArrayList<>();
    private String[] objects = {"obj_1.png","obj_2.png","obj_3.png"};

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
                        dy = dy*1.05;
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
                        try {
                            finalWallpaper(objectValues);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
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

    public void finalWallpaper(int score) throws FileNotFoundException {

        Pane pane = (Pane) imageViewObj.getParent();
        if (pane != null) {
            pane.getChildren().removeAll(imageViewObj, Score);

            // Add the final wallpaper
            Image finalWallpaperImage = new Image("FinalWallpapere2.png");
            ImageView finalImageView = new ImageView(finalWallpaperImage);
            pane.getChildren().add(finalImageView);

            try {
                PrintWriter writer = new PrintWriter(new FileWriter("src/scores.txt",true));
                writer.println(score);
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            File fileContrnt = new File("src/scores.txt");

            Scanner input = new Scanner(fileContrnt);
            while (input.hasNextInt()) {
                topScore.add(input.nextInt());
            }
            Collections.sort(topScore);
            Collections.reverse(topScore);
            if (topScore.size()<5){
                for (Integer num : topScore) {
                    scoreText = new Text(4, yCordinateTopScore,"TOP"+ (1+ topScore.indexOf(num)) + " : " + String.valueOf(num));
                    scoreText.setFont(new Font(25));
                    scoreText.setTranslateY(yCordinateTopScore);
                    scoreText.setTranslateX(xCordinateTopScore);
                    pane.getChildren().add(scoreText);
                    scoreTextList.add(scoreText);
                    yCordinateTopScore += 22;
                }
            }else {
                for (int i = 0 ; i<5 ; i++) {
                    scoreText = new Text(4, yCordinateTopScore,"TOP"+ (1+i) + " : " +String.valueOf(topScore.get(i)));
                    scoreText.setFont(new Font(25));
                    scoreText.setTranslateY(yCordinateTopScore);
                    scoreText.setTranslateX(xCordinateTopScore);
                    pane.getChildren().add(scoreText);
                    scoreTextList.add(scoreText);
                    yCordinateTopScore += 22;
                }
            }

            // Add a "Play Again" button
            Image playAgainBtnImage = new Image("TryAgainBtn.png");
            ImageView playAgainBtnImageView = new ImageView(playAgainBtnImage);

            playAgainBtnImageView.setOnMouseClicked(e -> {
                dy = 2;
                topScore.clear();
                yCordinateTopScore = 175;
                resetGame(pane);
                pane.getChildren().removeAll(playAgainBtnImageView, finalImageView);
                for (Text scoreText : scoreTextList) {
                    pane.getChildren().remove(scoreText);
                }
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
    public static void main(String[] args) {
        launch(args);
    }
}