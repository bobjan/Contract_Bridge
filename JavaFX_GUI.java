/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.*;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class JavaFXApplication1 extends Application {

    HBox bottomCards, centerField;
    Group parent;

    //	Score Board
    Score team1 = new Score();
    Score team2 = new Score();
    Text team1Score, team2Score;
    String[] myCards;
    int myCardsLength;

    Player p1, p2, p3, p4;

    Stage stage;
    
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        ///////////// Game Mechanics /////////////
        p1 = new Player();
        p2 = new Player();
        p3 = new Player();
        p4 = new Player();
        p1.drawCards();
        p2.drawCards();
        p3.drawCards();
        p4.drawCards();
        myCards = new String[13];
        myCards = p1.hand();
        myCardsLength = 13;

        //  Center Play Ground
        centerField = new HBox(20);
        centerField.setAlignment(Pos.CENTER);

        ////////////////////////////////////////////
        //BorderPane borderPane = new BorderPane();
        final BorderPane controlLayout = new BorderPane();
        ////////////////////////////////////////////////
        //  Team Labels (Top)

        final Label team1Label = new Label("Team 1:");
        final Label team2Label = new Label("Team 2:");
        team1Score = new Text(10, 10, "0");
        team2Score = new Text(50, 10, "0");
        team1Score.setFont(new Font(20));
        team2Score.setFont(new Font(20));
        team1Label.setFont(new Font(20));
        team2Label.setFont(new Font(20));
        
        /////// New Game Button ///////////
        Button restartBtn = new Button("New Game");
        restartBtn.setAlignment(Pos.TOP_RIGHT);
        restartBtn.setOnMousePressed(e -> {
            System.out.println("New Game !!!");
            start(primaryStage);
            //  Reset Score.
            team1 = new Score();  
            team2 = new Score();
            //  Reset game mechanics.
            player = 1;
            userClicked = true;
            //  
        });

        HBox labels = new HBox(team1Label, team1Score, team2Label, team2Score, restartBtn);
        labels.setSpacing(50);
        labels.setPadding(new Insets(20));
        labels.setAlignment(Pos.CENTER);

        //  Left Side Bar
        Pane leftSide = new Pane();
        leftSide.setPadding(new Insets(11, 12, 13, 14));
        //leftSide.setStyle("-fx-border-color: red");
        for (int i = 0; i < 13; i++) {
            Rectangle r1 = new Rectangle(40, 80 + i * 10, 90, 140);
            r1.setFill(Color.AQUA);
            r1.setStroke(Color.BLACK);
            leftSide.getChildren().add(r1);
        }

        //  Right Side Bar
        Pane rightSide = new Pane();
        rightSide.setPadding(new Insets(11, 12, 13, 75));
        //rightSide.setStyle("-fx-border-color: red");
        for (int i = 0; i < 13; i++) {
            Rectangle r2 = new Rectangle(0, 80 + i * 10, 90, 140);
            r2.setFill(Color.AQUA);
            r2.setStroke(Color.BLACK);
            rightSide.getChildren().add(r2);
        }

        ////////////////////////////////////////////////
        //  Display Player Cards
        bottomCards = new HBox(-30);
        bottomCards.setPadding(new Insets(20));
        bottomCards.setAlignment(Pos.CENTER);

        for (int i = 0; i < 13; i++) {
            String fileName = "file:playing_cards_images/" + myCards[i] + ".jpg";
            Image cardImage = new Image(fileName);
            ImageView imageView = new ImageView(cardImage);
            imageView.setFitHeight(140);
            imageView.setFitWidth(90);
            imageView.setId(myCards[i]);
            bottomCards.getChildren().add(imageView);
            imageView.setOnMousePressed(e -> {
                try {
                    handleClickAction(e);
                    
                } catch (InterruptedException ex) {
                    //e.notify();
                    Logger.getLogger(JavaFXApplication1.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

        }

        /////////// Border Layout //////////////
        controlLayout.setTop(labels);
        controlLayout.setBottom(bottomCards);
        controlLayout.setLeft(leftSide);
        controlLayout.setRight(rightSide);
        controlLayout.setCenter(centerField);

        ////////////////  BackGround Color ///////////////
        BackgroundFill myBF = new BackgroundFill(Color.GREEN, new CornerRadii(1),
                null);// or null for the padding
        //then you set to your node or container or layout
        controlLayout.setBackground(new Background(myBF));

        Scene scene = new Scene(controlLayout, 1200, 800);
        primaryStage.setTitle("Contract Bridge");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    int temp;
    int player = 1;
    String cardPicked, max, max1, max2;
    boolean userClicked = true;
    CurrentPlay card;
    boolean match = true;
    
    ImageView img, img1, img2, img3;

    FadeTransition ft1 = new FadeTransition(Duration.millis(1000));
    FadeTransition ft2 = new FadeTransition(Duration.millis(1000));
    FadeTransition ft3 = new FadeTransition(Duration.millis(1000));
    FadeTransition ft4 = new FadeTransition(Duration.millis(1000));

    final Duration period = Duration.millis(2000);
    SequentialTransition seqT = new SequentialTransition(centerField);

    private void handleClickAction(MouseEvent e) throws InterruptedException {
        //  Get card from user.
        img = (ImageView) e.getSource();
        cardPicked = img.getId();
        
        //  Check for matching suit.
        if (player!=1) {
            if (indexOfCard(cardPicked)[0]==card.getSuit())
                match = true;
            else{
                for (int i=0;i<p1.getNumOfCards();i++) {
                    if (indexOfCard(myCards[i])[0]==card.getSuit()) {
                        match = false;
                        System.out.println("Must play matching Suit!");
                        break;
                    }
                    match = true;
                }
            }
        }
        
        while (match) {
            if (player == 1) {
                //  Player 1
                card = new CurrentPlay(cardPicked);
                temp = 1;
                //  Player 2
                max = p2.play(cardPicked);
                card.add(max);
                if (card.isHigher()) {
                    temp = 2;
                }
                //  Player 3
                max1 = p3.play(card.getHighCard());
                card.add(max1);
                if (card.isHigher()) {
                    temp = 3;
                }
                //  Player 4
                max2 = p4.play(card.getHighCard());
                card.add(max2);
                if (card.isHigher()) {
                    temp = 4;
                }
                //  Remove Cards
                p1.popCard(cardPicked);
                p2.popCard(max);
                p3.popCard(max1);
                p4.popCard(max2);
                // Add Cards to Center Board
                

                img1 = new ImageView(new Image("file:playing_cards_images/" + max + ".jpg"));
                img2 = new ImageView(new Image("file:playing_cards_images/" + max1 + ".jpg"));
                img3 = new ImageView(new Image("file:playing_cards_images/" + max2 + ".jpg"));
                
                ft2.setNode(img1);
                ft3.setNode(img2);
                ft4.setNode(img3);
                 
                seqT.getChildren().clear();
                seqT.getChildren().addAll(ft2, ft3, ft4);
                
                centerField.getChildren().clear();
                centerField.getChildren().addAll(img, img1, img2, img3);
                
                player = temp;
                System.out.println(cardPicked + "\n" + max + "\n" + max1 + "\n" + max2);
                System.out.println("Player " + player + " Won!");
                
            }
            //seqT.play();
            
            if (player == 2) {
                if (userClicked) {
                    userClicked = !userClicked;
                    max = p2.play();
                    card = new CurrentPlay(max);
                    if (card.isHigher()) {
                        temp = 2;
                    }
                    //  Player 3
                    max1 = p3.play(card.getHighCard());
                    card.add(max1);
                    if (card.isHigher()) {
                        temp = 3;
                    }
                    //  Player 4
                    max2 = p4.play(card.getHighCard());
                    card.add(max2);
                    if (card.isHigher()) {
                        temp = 4;
                    }
                    //  Remove Cards
                    p2.popCard(max);
                    p3.popCard(max1);
                    p4.popCard(max2);
                // Add Cards to Center Board

                    //centerField.getChildren().clear();
                    img1 = new ImageView(new Image("file:playing_cards_images/" + max + ".jpg"));
                    img2 = new ImageView(new Image("file:playing_cards_images/" + max1 + ".jpg"));
                    img3 = new ImageView(new Image("file:playing_cards_images/" + max2 + ".jpg"));

                    System.out.println(max + "\n" + max1 + "\n" + max2);
                    centerField.getChildren().clear();
                    centerField.getChildren().addAll(img1, img2, img3);
                    
                    break;
                } else {
                    userClicked = !userClicked;
                    card.add(cardPicked);
                    if (card.isHigher())
                        temp = 1;

                    p1.popCard(cardPicked);
                    
                    seqT.getChildren().add(ft1);
                    centerField.getChildren().add(img);
                    
                    player = temp;
                    System.out.println(cardPicked);
                    System.out.println("Player " + player + " Won!");
                    
                }
            }
            if (player == 3) {
                if (userClicked) {
                    userClicked = !userClicked;
                    max = p3.play();
                    card = new CurrentPlay(max);
                    if (card.isHigher()) {
                        temp = 3;
                    }
                    //  Player 4
                    max1 = p4.play(card.getHighCard());
                    card.add(max1);
                    if (card.isHigher()) {
                        temp = 4;
                    }
                    //  Remove Cards
                    p3.popCard(max);
                    p4.popCard(max1);
                    // Add Cards to Center Board
                    
                    img2 = new ImageView(new Image("file:playing_cards_images/" + max + ".jpg"));
                    img3 = new ImageView(new Image("file:playing_cards_images/" + max1 + ".jpg"));

                    System.out.println(max + "\n" + max1);
                    centerField.getChildren().clear();
                    centerField.getChildren().addAll(img2, img3);

                    break;
                } else {
                    userClicked = !userClicked;
                    card.add(cardPicked);
                    if (card.isHigher()) {
                        temp = 1;
                    }
                    
                    max2 = p2.play(card.getHighCard());
                    card.add(max2);
                    if (card.isHigher()) {
                        temp = 2;
                    }
                    img1 = new ImageView(new Image("file:playing_cards_images/" + max2 + ".jpg"));
                    
                    centerField.getChildren().addAll(img, img1);
                    //Thread.sleep(5000);

                    //  Remove Cards.
                    p1.popCard(cardPicked);
                    p2.popCard(max2);
                    //  Print Winner.
                    player = temp;
                    System.out.println(cardPicked + "\n" + max2);
                    System.out.println("Player " + player + " Won!");
                }
            }

            if (player == 4) {
                if (userClicked) {
                    userClicked = !userClicked;
                    max = p4.play();
                    card = new CurrentPlay(max);
                    if (card.isHigher()) {
                        temp = 4;
                    }

                    // Add Cards to Center Board
                    img3 = new ImageView(new Image("file:playing_cards_images/" + max + ".jpg"));
                    
                    System.out.println(max);
                    centerField.getChildren().clear();
                    centerField.getChildren().add(img3);

                    //Remove Cards
                    p4.popCard(max);
                    break;
                } else {
                    userClicked = !userClicked;
                    card.add(cardPicked);
                    if (card.isHigher()) {
                        temp = 1;
                    }
                    
                    //  Player 2
                    max2 = p2.play(card.getHighCard());
                    card.add(max2);
                    if (card.isHigher()) {
                        temp = 2;
                    }
                    img1 = new ImageView(new Image("file:playing_cards_images/" + max2 + ".jpg"));
                    //  Player 3
                    max1 = p3.play(card.getHighCard());
                    card.add(max1);
                    if (card.isHigher()) {
                        temp = 3;
                    }
                    img2 = new ImageView(new Image("file:playing_cards_images/" + max1 + ".jpg"));
                    centerField.getChildren().addAll(img, img1, img2);

                    p1.popCard(cardPicked);
                    p2.popCard(max2);
                    p3.popCard(max1);
                    //  Print Winner
                    player = temp;
                    System.out.println(cardPicked + "\n" + max2 + "\n" + max1);
                    System.out.println("Player " + player + " Won!");    
                }
            }
            if (player == 1) {
                userClicked = true;
                break;
            }  
        }

        //////  Score ////////
        if ((!userClicked || player == 1) && match == true) {
            if (player == 1 || player == 3) {
                team1.addOne();
            } else {
                team2.addOne();
            }
        }
        team1Score.setText("" + team1.getScore());
        team2Score.setText("" + team2.getScore());

    }

    public static void main(String[] args) {
        launch(args);

    }
    
    public static int[] indexOfCard(String card) {
        int[] result = new int[2];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                if (Deck.stringDeck[i][j].equals(card)) {
                    result[0] = i;
                    result[1] = j;
                }
            }
        }
        return result;
    }

}
