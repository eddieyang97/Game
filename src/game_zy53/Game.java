package game_zy53;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * A basic example JavaFX program for the first lab.
 * 
 * @author Robert C. Duvall
 */
public class Game extends Application {
    public static final String TITLE = "Example JavaFX";
    public static final int SIZE = 400;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.AZURE;
    public static final Paint HIGHLIGHT = Color.OLIVEDRAB;    
    public static final String BOUNCER_IMAGE = "ball.gif";
    public static final Paint MOVER_COLOR = Color.PLUM;
    public static final int MOVER_HEIGHT = 5;
    public static final int MOVER_WIDTH = 80;
    public static final int MOVER_SPEED = 5;
    public static final Paint GROWER_COLOR = Color.BISQUE;
    public static final double GROWER_RATE = 1.1;
    public static final int GROWER_HEIGHT = 18;
    public static final int GROWER_WIDTH = 48;
    public static final int maxnumB = 3;
    public static final int maxSpeed = 100;
    public static int numBouncers = maxnumB;//(int)(Math.random()*maxnumB + 1);
	public static double[] BOUNCER_SPEEDX = new double[numBouncers];
    public static double[] BOUNCER_SPEEDY = new double[numBouncers];
    public static Group root;
    	

    // some things we need to remember during our game
    private Scene myScene;
    private ArrayList<ImageView> myBouncers = new ArrayList<ImageView>();
    private Rectangle myMover;
    private ArrayList<Rectangle> myGrowers = new ArrayList<Rectangle>();
    
    /**
     * Initialize what will be displayed and how it will be updated.
     */
    @Override
    public void start (Stage stage) {
        // attach scene to the stage and display it
        myScene = setupGame(SIZE, SIZE, BACKGROUND);
        stage.setScene(myScene);
        stage.setTitle(TITLE);
        stage.show();
        // attach "game loop" to timeline to play it
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
                                      e -> step(SECOND_DELAY));
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    // Create the game's "scene": what shapes will be in the game and their starting properties
    private Scene setupGame (int width, int height, Paint background) {
        // create one top level collection to organize the things in the scene
        root = new Group();
        // create a place to see the shapes
        Scene scene = new Scene(root, width, height, background);
        // make some shapes and set their properties
        Image image = new Image(getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
        for(int i = 0; i < numBouncers; i++) {
        	myBouncers.add(new ImageView(image));
        	BOUNCER_SPEEDX[i] = 0;
        	BOUNCER_SPEEDY[i] = 0;
        	myBouncers.get(i).setX(width / 2);
        	myBouncers.get(i).setY(height - 20);
        	//BOUNCER_SPEEDX[i] = Math.random()*maxSpeed;
            //BOUNCER_SPEEDY[i] = Math.random()*maxSpeed;
            //myBouncers.get(i).setX(Math.random()*(SIZE - 10));
            //myBouncers.get(i).setY(Math.random()*SIZE - 10);
            root.getChildren().add(myBouncers.get(i));
        }
        myMover = new Rectangle(width / 2 - 35, height - 5, MOVER_WIDTH, MOVER_HEIGHT);
        myMover.setFill(MOVER_COLOR);
        for(int i = 0; i < 32; i++) {
        	myGrowers.add(new Rectangle((i % 8) * 50, (i / 8) * 20, GROWER_WIDTH, GROWER_HEIGHT));
        	myGrowers.get(i).setFill(Color.rgb(100,100,100));//(int)(Math.random()*256)
       		root.getChildren().add(myGrowers.get(i));
        }
        //myGrower = new Rectangle(width / 2 - 35, 0, GROWER_SIZE * 10, GROWER_SIZE);
        //myGrower.setFill(GROWER_COLOR);
        // order added to the group is the order in which they are drawn
        root.getChildren().add(myMover);
        // respond to input
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        //scene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
        return scene;
    }

    // Change properties of shapes to animate them 
    // Note, there are more sophisticated ways to animate shapes, but these simple ways work fine to start.
    private void step (double elapsedTime) {
        // update attributes
    	for(int i = 0; i < myBouncers.size(); i++) {
        	if(myBouncers.get(i).getX() > SIZE - 10 || myBouncers.get(i).getX() < -3) {
        		BOUNCER_SPEEDX[i] *= -1;
        	}
        	if(myBouncers.get(i).getY() < 0 || myBouncers.get(i).getY() < SIZE - 15 
        			&& myMover.getBoundsInParent().intersects(myBouncers.get(i).getBoundsInParent())) {
        		BOUNCER_SPEEDY[i] *= -1;
        	}
        	for(int j = 0; j < myGrowers.size(); j ++) {
        		if(myGrowers.get(j).getBoundsInParent().intersects(myBouncers.get(i).getBoundsInParent())) {
        			BOUNCER_SPEEDY[i] *= -1;
        			root.getChildren().remove(myGrowers.get(j));
        			myGrowers.remove(myGrowers.get(j));
        		}
        	}
        	//if(!myMover.getBoundsInParent().intersects(myBouncers.get(i).getBoundsInParent())) myMover.setFill(MOVER_COLOR);
        	myBouncers.get(i).setX(myBouncers.get(i).getX() + BOUNCER_SPEEDX[i] * elapsedTime);
        	myBouncers.get(i).setY(myBouncers.get(i).getY() + BOUNCER_SPEEDY[i] * elapsedTime);    		
    	}
    	/*int temp = myBouncers.size();
    	for(int i = temp - 1; i >= 0; i--) {
    		if(myBouncers.get(i).getY() > SIZE + 50) {
    			myBouncers.remove(i);
    			BOUNCER_SPEEDX[i] = 0;
    			BOUNCER_SPEEDY[i] = 0;
    		}
    	}*/
        //myMover.setRotate(myMover.getRotate() - 1);
        //myGrower.setRotate(myGrower.getRotate() + 1);

        // check for collisions
        // with shapes, can check precisely
        /*Shape intersect = Shape.intersect(myMover, myGrower);
        if (intersect.getBoundsInLocal().getWidth() != -1) {
            myMover.setFill(HIGHLIGHT);
        }
        else {
            myMover.setFill(MOVER_COLOR);
        }
        // with images can only check bounding box
        for(int i = 0; i < numBouncers; i++) {
	        if (myGrower.getBoundsInParent().intersects(myBouncers.get(i).getBoundsInParent())) {
	            myGrower.setFill(HIGHLIGHT);
	        }
	        else {
	            myGrower.setFill(GROWER_COLOR);
	        }
        }*/
    }

    // What to do each time a key is pressed
    private void handleKeyInput (KeyCode code) {
        if (code == KeyCode.RIGHT && myMover.getX() < SIZE - MOVER_WIDTH) {
            myMover.setX(myMover.getX() + MOVER_SPEED);
        }
        else if (code == KeyCode.LEFT && myMover.getX() > 0) {
            myMover.setX(myMover.getX() - MOVER_SPEED);
        }
        else if (code == KeyCode.UP && BOUNCER_SPEEDX[0] == 0) {
        	for(int i = 0; i < myBouncers.size(); i++) {
        		BOUNCER_SPEEDX[i] = Math.random()*maxSpeed + 50;
            	BOUNCER_SPEEDY[i] = Math.random()*maxSpeed + 50;
        	}
            //myMover.setY(myMover.getY() - MOVER_SPEED);
        }/*
        else if (code == KeyCode.DOWN && myBouncers.size() == 0) {
        	Image image = new Image(getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
        	myBouncers.add(new ImageView(image));
        	BOUNCER_SPEEDX[0] = 0;
        	BOUNCER_SPEEDY[0] = 0;
        	myBouncers.get(0).setX(SIZE / 2);
        	myBouncers.get(0).setY(SIZE - 20);
        	Group root = new Group();
        	Scene scene = new Scene(root, SIZE, SIZE, BACKGROUND);
        	root.getChildren().add(myBouncers.get(0));
            //myMover.setY(myMover.getY() + MOVER_SPEED);
        }*/
    }

    // What to do each time a key is pressed
    /*private void handleMouseInput (double x, double y) {
        if (myGrower.contains(x, y)) {
            myGrower.setScaleX(myGrower.getScaleX() * GROWER_RATE);
            myGrower.setScaleY(myGrower.getScaleY() * GROWER_RATE);
        }
    }*/

    /**
     * Start the program.
     */
    public static void main (String[] args) {
        launch(args);
    }
}
