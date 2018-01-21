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

public class Game extends Application {
    public static final String TITLE = "Breakout";
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
    public static final int MOVER_SPEED = 8;
    public static final Paint GROWER_COLOR = Color.BISQUE;
    public static final double GROWER_RATE = 1.1;
    public static final int GROWER_HEIGHT = 18;
    public static final int GROWER_WIDTH = 48;
    public static final int maxnumB = 3;
    public static final double bouncerSpeed = 150;
    public static int level;
    public static int life;
    public static Group root;
    	

    // some things we need to remember during our game
    private Scene myScene;
    private ArrayList<Bouncer> myBouncers = new ArrayList<Bouncer>();
    private Rectangle myMover;
    private ArrayList<Block> blocks = new ArrayList<Block>();
    
    //Initialize what will be displayed and how it will be updated.
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
        root = new Group();
        Scene scene = new Scene(root, width, height, background);
    	life = 3;
        level = 1;
        setupBouncer();
        myMover = new Rectangle(width / 2 - 35, height - 5, MOVER_WIDTH, MOVER_HEIGHT);
        myMover.setFill(MOVER_COLOR);
        setupBlocks1();
        drawBlocks();
        root.getChildren().add(myMover);
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }
    
    private void setupBouncer() {
        Image image = new Image(getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
    	myBouncers.add(new Bouncer(0, 0, new ImageView(image)));
        myBouncers.get(0).setX(SIZE / 2);
        myBouncers.get(0).setY(SIZE - 20);
        root.getChildren().add(myBouncers.get(0).getImage());
    }
    
    private void setupBlocks1() {
    	for(int i = 0; i < 32; i++) {
        	blocks.add(new Block(1, new Rectangle((i % 8) * 50, (i / 8) * 20, GROWER_WIDTH, GROWER_HEIGHT)));
        }
    }
    
    private void setupBlocks2() {
        for(int i = 0; i < 48; i++) {
        	blocks.add(new Block(i/16 + 1, new Rectangle((i % 8) * 50, (i / 8) * 20, GROWER_WIDTH, GROWER_HEIGHT)));
        }
    }
    
    private void setupBlocks3() {
        for(int i = 0; i < 40; i++) {
        	blocks.add(new Block((i + 8)/24 + 2, new Rectangle((i % 8) * 50, (i / 8) * 20, GROWER_WIDTH, GROWER_HEIGHT)));
        }
        for(int i = 0; i < 4; i++) {
        	if(i < 2)
        		blocks.add(new Block(Integer.MAX_VALUE, new Rectangle(i * 50, 100, GROWER_WIDTH, GROWER_HEIGHT)));
        	else
        		blocks.add(new Block(Integer.MAX_VALUE, new Rectangle((i + 4) * 50, 100, GROWER_WIDTH, GROWER_HEIGHT)));
        }
    }
    
    private void drawBlocks() {
    	for(int i = 0; i < blocks.size(); i++) {
    		setBlockColor(blocks.get(i));
       		root.getChildren().add(blocks.get(i).getRec());
    	}
    }
    
    private void setBlockColor(Block block) {
    	if(block.getHit() == 1)
    		block.setFill(Color.rgb(255, 0, 0));
		else if(block.getHit() == 2)
			block.setFill(Color.rgb(0, 0, 255));
		else if(block.getHit() == 3)
			block.setFill(Color.rgb(0, 255, 0));
		else
			block.setFill(Color.rgb(0, 0, 0));
    }
    
    private void updateBlock(Block block) {
    	if(block.getHit() == 1) {
			root.getChildren().remove(block.getRec());
			blocks.remove(block);
    	}
    	else
    		block.setHit(block.getHit() - 1);
    	setBlockColor(block);
    }
    // Change properties of shapes to animate them 
    // Note, there are more sophisticated ways to animate shapes, but these simple ways work fine to start.
    private void step (double elapsedTime) {
    	if(blocks.size() == 0)
    		levelup();
    	updateBouncers();
    	updatePosition(elapsedTime);
    }


	private void levelup() {
    	level++;
    	while(myBouncers.size() != 0) {
    		root.getChildren().remove(myBouncers.get(0).getImage());
    		myBouncers.remove(0);
    	}
    	setupBouncer();
    	if(level == 2) setupBlocks2();
    	if(level == 3) setupBlocks3();
    	drawBlocks();
    }
    
    private void updateBouncers() {
    	for(int i = 0; i < myBouncers.size(); i++) {
    		if(myBouncers.get(i).getY() > 450) {
    			root.getChildren().remove(myBouncers.get(i).getImage());
    			myBouncers.remove(myBouncers.get(i));
    			life--;
    		}
    	}
    	if(life > 0 && myBouncers.size() == 0)
    		setupBouncer();
	}
    
    private void updatePosition(double elapsedTime) {
    	for(int i = 0; i < myBouncers.size(); i++) {
        	if(myBouncers.get(i).getX() > SIZE - 15 || myBouncers.get(i).getX() < -2) myBouncers.get(i).setSpeedX(-myBouncers.get(i).getSpeedX());
        	if(myBouncers.get(i).getY() < 0) myBouncers.get(i).setSpeedY(-myBouncers.get(i).getSpeedY());
        	if(myMover.getBoundsInParent().intersects(myBouncers.get(i).getImage().getBoundsInParent())) myBouncers.get(i).setSpeedY(-Math.abs(myBouncers.get(i).getSpeedY()));
        	for(int j = 0; j < blocks.size(); j ++) {
        		if(blocks.get(j).getBoundsInParent().intersects(myBouncers.get(i).getImage().getBoundsInParent())) {
        			if(Math.abs(blocks.get(j).getBoundsInParent().getMaxX() - myBouncers.get(i).getImage().getBoundsInParent().getMinX()) <= bouncerSpeed/80 ||
        					Math.abs(blocks.get(j).getBoundsInParent().getMinX() - myBouncers.get(i).getImage().getBoundsInParent().getMaxX()) <= bouncerSpeed/80)
        				myBouncers.get(i).setSpeedX(-myBouncers.get(i).getSpeedX());
        			else myBouncers.get(i).setSpeedY(-myBouncers.get(i).getSpeedY());
        			updateBlock(blocks.get(j));
        		}
        	}
        	myBouncers.get(i).setX(myBouncers.get(i).getX() + myBouncers.get(i).getSpeedX() * elapsedTime);
        	myBouncers.get(i).setY(myBouncers.get(i).getY() + myBouncers.get(i).getSpeedY() * elapsedTime);    		
    	}
    }
    
    // What to do each time a key is pressed
    private void handleKeyInput (KeyCode code) {
        if (code == KeyCode.RIGHT && myMover.getX() < SIZE - MOVER_WIDTH) {
            myMover.setX(myMover.getX() + MOVER_SPEED);
        }
        else if (code == KeyCode.LEFT && myMover.getX() > 0) {
            myMover.setX(myMover.getX() - MOVER_SPEED);
        }
        else if (code == KeyCode.UP && myBouncers.get(0).getSpeedX() == 0) {
        	myBouncers.get(0).setSpeedX(bouncerSpeed + Math.random() * 100);
        	myBouncers.get(0).setSpeedY(-bouncerSpeed - Math.random() * 50);
        }
        else if (code == KeyCode.DOWN) {
        	for(int i = blocks.size() - 1; i >= 0; i--) {
        		root.getChildren().remove(blocks.get(i).getRec());
    			blocks.remove(blocks.get(i));
        	}
        }
    }

    /**
     * Start the program.
     */
    public static void main (String[] args) {
        launch(args);
    }
}
