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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Score {
    private String playerName;
    private int scoreValue;

    public Score(String playerName, int scoreValue) {
        this.playerName = playerName;
        this.scoreValue = scoreValue;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScoreValue() {
        return scoreValue;
    }

    @Override
    public String toString() {
        return playerName + ": " + scoreValue;
    }
}

public class ClickGame extends Application {

    private ImageView imageViewObj;
    private Text gainedNum;
    private Text leftNum;
    private int intGainedNum = 0;
    private int intLeftNum = 10;
    private Text scoreText; // declare scoreText as a class member variable
    private String[] objects = {"obj_1.png","obj_2.png","obj_3.png"};
    private List<Score> topScores = new ArrayList<>();

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

        gainedNum = new Text(217, 35, String.valueOf(intGainedNum));
        gainedNum.setFont(new Font(26));

        leftNum = new Text(765, 35, String.valueOf(intLeftNum));
        leftNum.setFont(new Font(26));

        startBtnImageView.setOnMouseClicked(e -> {
            pane.getChildren().removeAll(startBtnImageView, startImageView);
            pane.getChildren().addAll(imageView, gainedNum, leftNum);
            selectRandomObject();
            pane.getChildren().add(imageViewObj);
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
            double y = 56;
            double dy = 2;
            double x = Math.random() * 720;


            @Override
            public void handle(long now) {
                y += dy;
                imageViewObj.setTranslateY(y);
                imageViewObj.setTranslateX(x);

                /* this condition  run when the object fall out of the pane */
                if (imageViewObj.getTranslateY() >= 600.) {
                    intLeftNum--;
                    gainedNum.setText(String.valueOf(intGainedNum));
                    leftNum.setText(String.valueOf(intLeftNum));
                    x = Math.random() * 720;
                    y = 56;
                    dy = dy * 1.1;
                    imageViewObj.setTranslateY(y);
                    imageViewObj.setTranslateX(x);

                }
                /* this lambda method run when the player click on the object falling */
                imageViewObj.setOnMouseClicked(e -> {
                    intGainedNum++;
                    intLeftNum--;
                    gainedNum.setText(String.valueOf(intGainedNum));
                    leftNum.setText(String.valueOf(intLeftNum));
                    y = 56;
                    dy = dy * 1.1;
                    x = Math.random() * 720;
                    Pane parentPane = (Pane) imageViewObj.getParent();
                    parentPane.getChildren().remove(imageViewObj);
                    selectRandomObject();
                    parentPane.getChildren().add(imageViewObj);
                });

                if (intLeftNum==0){
                    stop();
                    finalWallpaper();

                }}

        };
        animationTimer.start();
    }
    public void finalWallpaper() {
        Pane pane = (Pane) imageViewObj.getParent();
        pane.getChildren().removeAll(imageViewObj, gainedNum, leftNum);

        // Add the final wallpaper
        Image finalWallpaperImage = new Image("FinalWallpapere.png");
        ImageView finalImageView = new ImageView(finalWallpaperImage);
        pane.getChildren().add(finalImageView);

        // Add a "Play Again" button
        Image playAgainBtnImage = new Image("TryAgainBtn.png"); // set an image
        ImageView playAgainBtnImageView = new ImageView(playAgainBtnImage);
        playAgainBtnImageView.setTranslateX(1.0);
        playAgainBtnImageView.setTranslateY(1.1);
        playAgainBtnImageView.setOnMouseClicked(e -> {
            // Reset the game
            intGainedNum = 0;
            intLeftNum = 10;
            gainedNum.setText(String.valueOf(intGainedNum));
            leftNum.setText(String.valueOf(intLeftNum));
            selectRandomObject();
            pane.getChildren().addAll(imageViewObj, gainedNum, leftNum);
            startAnimationLoop();

            // Remove the "Play Again" button and final wallpaper
            pane.getChildren().removeAll(playAgainBtnImageView, finalImageView, scoreText);

        });
        pane.getChildren().add(playAgainBtnImageView);

        if (intGainedNum > 0) {
            Score newScore = new Score("Player", intGainedNum);
            topScores.add(newScore);
            Collections.sort(topScores, (s1, s2) -> Integer.compare(s2.getScoreValue(), s1.getScoreValue()));
            if (topScores.size() > 5) {
                topScores.remove(5);
            }
        }

        int y = 180;
        int x = 320;
        for (Score score : topScores) {
            scoreText = new Text(4, y, score.toString());
            scoreText.setFont(new Font(25));
            if (score.getPlayerName().equals("Player")) {
                scoreText.setTranslateY(y);
                scoreText.setTranslateX(x);
            }
            pane.getChildren().add(scoreText);
            y += 25;
        }
    }

    public void selectRandomObject() {
        String objImg = objects[(int) (objects.length * Math.random())];
        Image image = new Image(objImg);
        imageViewObj = new ImageView(image);
    }

    public static void main(String[] args) {
        launch();
    }
}