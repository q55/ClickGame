package com.example.demo;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Random;

public class ClickGame extends Application {

    private ImageView imageViewObj;
    private Text gainedNum;
    private Text leftNum;
    private int intGainedNum = 0;
    private int intLeftNum = 10;
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
        Image finalWallpaperImage = new Image("FinalWallpapere.png");
        ImageView finalImageView = new ImageView(finalWallpaperImage);
        pane.getChildren().add(finalImageView);
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