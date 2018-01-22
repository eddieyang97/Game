package game_zy53;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
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
    public static final String BALL_POWER = "extraballpower.gif";
    public static final String PADDLE_POWER = "sizepower.gif";
    public static final String LIFE_POWER = "lifepower.gif";
    public static final String WELCOME = "welcome.png";
    public static final Paint MOVER_COLOR = Color.PLUM;
    public static final int MOVER_HEIGHT = 5;
    public static final int MOVER_WIDTH = 80;
    public static final int MOVER_SPEED = 10;
    private boolean MOVER_BOT = true;
    public static final Paint GROWER_COLOR = Color.BISQUE;
    public static final double GROWER_RATE = 1.1;
    public static final int GROWER_HEIGHT = 18;
    public static final int GROWER_WIDTH = 48;
    public static final int GROWER_SIDE = 20;
    public static final int MAXNUMB = 3;
    public static final double POWERDROPSPEED = 60;
    public static final double BOUNCERSPEED = 100;
    private boolean start = false;
    private int level;
    private int life;
    private int maxLife = 3;
    private int powerUpDuration = 0;
    private Group root;
    private Label showLevel = new Label("Level: " + level);
    private Label showLife = new Label("Life: " + life);
    private Label showPow = new Label("PowerUpDuration: " + powerUpDuration/60);
    	
    // some things we need to remember during our game
    private Scene myScene;
    private ArrayList<Bouncer> myBouncers = new ArrayList<Bouncer>();
    private Rectangle myMover;
    private ArrayList<Block> blocks = new ArrayList<Block>();
    private ArrayList<PowerDrop> powerDrops = new ArrayList<PowerDrop>();
    
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
        setupWelcome();
        setupPaddle();
        setupBouncer();
        setupLabels();
        setupBlocks1();
        drawBlocks();
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }
    
    private void setupWelcome() {
        Image image = new Image(getClass().getClassLoader().getResourceAsStream(WELCOME));
        ImageView view = new ImageView(image);
        view.setY(100);
        view.setFitHeight(220);
        view.setFitWidth(400);
        root.getChildren().add(view);
    }
    
    private void setupPaddle() {
        myMover = new Rectangle((SIZE - MOVER_WIDTH) / 2, SIZE - MOVER_HEIGHT, MOVER_WIDTH, MOVER_HEIGHT);
        myMover.setFill(MOVER_COLOR);
        root.getChildren().add(myMover);
    }
    
    private void setupLabels() {
    	showLevel.setLayoutX(10);
    	showLevel.setLayoutY(360);
        root.getChildren().add(showLevel);
        showLife.setLayoutX(10);
        showLife.setLayoutY(380);
        root.getChildren().add(showLife);
        showPow.setLayoutX(280);
        showPow.setLayoutY(380);
        root.getChildren().add(showPow);
    }
    
	private void setupBouncer() {
        Image image = new Image(getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
    	myBouncers.add(new Bouncer(0, 0, new ImageView(image)));
        myBouncers.get(0).setX(SIZE / 2);
        myBouncers.get(0).setY(SIZE - 20);
        root.getChildren().add(myBouncers.get(0).getImage());
    }
    
	private void startBouncer()	{
		if(level == 4) {
			myBouncers.get(0).setSpeedX((-Math.random() * 50 + BOUNCERSPEED * 1.5) * (-1 + (int)(2*Math.random())*2));
			myBouncers.get(0).setSpeedY((-Math.random() * 20 + BOUNCERSPEED) * (-1 + (int)(2*Math.random())*2));
		}
		else {
	    	myBouncers.get(0).setSpeedX((Math.random() * 100 + BOUNCERSPEED) * (-1 + (int)(2*Math.random())*2));
	    	myBouncers.get(0).setSpeedY(-BOUNCERSPEED + Math.random() * 50);
		}
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
    
    private void setupBlocks4() {
        for(int i = 0; i < 36; i++) {
        	int distance = (int)Math.sqrt(((i % 6) * 25 - 62.5) * ((i % 6) * 25 - 62.5) + ((i / 6) * 25 - 62.5) * ((i / 6) * 25 - 62.5));
        	blocks.add(new Block((distance + 20)/40 + 1, new Rectangle(125 + (i % 6) * 25, 125 + (i / 6) * 25, GROWER_SIDE, GROWER_SIDE)));
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
    
    
    // Change properties of shapes to animate them 
    private void step (double elapsedTime) {
    	if(life > 0 && myBouncers.size() == 0) {
    		reset();
    	}
    	else if(level == 5)
    		win();
    	else if(blocks.size() == 0 || level == 3 && blocks.size() <= 4)
    		levelup();
    	else if(life == 0)
    		loss();
    	else {
    		updatePaddle();
    		updatePowerDrops(elapsedTime);
        	updateLabel();
        	updateBouncers();
        	updatePosition(elapsedTime);
    	}
    }

    private void reset() {
		clearPowerDrops();
		powerUpDuration = 0;
		setupBouncer();
		MOVER_BOT = true;
		myMover.setY(SIZE - MOVER_HEIGHT);
		myMover.setX((SIZE - MOVER_WIDTH) / 2);
    }

    private void win() {
    	root.getChildren().clear();
    	Label endLabel = new Label("You Won!");
    	endLabel.setFont(new Font("Arial", 30));
        endLabel.setLayoutX(125);
        endLabel.setLayoutY(150);
        root.getChildren().add(endLabel);
    }
    
	private void levelup() {
		if(level == 3)
			clearAll();
		if(level < 5)
			level++;
		while(myBouncers.size() != 0) {
    		root.getChildren().remove(myBouncers.get(0).getImage());
    		myBouncers.remove(0);
    	}
		reset();
    	if(level == 2) 
    		setupBlocks2();
    	else if(level == 3) 
    		setupBlocks3();
    	else if(level == 4)
    		setupBlocks4();
    	drawBlocks();
    }

    private void loss() {
    	root.getChildren().clear();
    	Label endLabel = new Label("Game Over!");
    	endLabel.setFont(new Font("Arial", 30));
        endLabel.setLayoutX(120);
        endLabel.setLayoutY(150);
        root.getChildren().add(endLabel);
    }
    
	private void clearPowerDrops() {
		for(int i = powerDrops.size() - 1; i >= 0; i--) {
			root.getChildren().remove(powerDrops.get(i).getImage());
			powerDrops.remove(i);
		}
	}
    
	private void dropPowerUp(Block block) {
    	int temp = (int)(Math.random()*4);
    	if(temp == 0) {
	    	int type = (int)(Math.random()*3);
	    	PowerDrop pd;
			if(type == 0)
	    		pd = new PowerDrop(0, new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(BALL_POWER))));
	    	else if(type == 1)
	    		pd = new PowerDrop(1, new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(PADDLE_POWER))));
	    	else
	    		pd = new PowerDrop(2, new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(LIFE_POWER))));
	    	powerDrops.add(pd);
	        pd.setX(block.getRec().getX());
	        pd.setY(block.getRec().getY());
	        root.getChildren().add(pd.getImage());
    	}
    }
	
	private void updatePowerDrops(double elapsedTime) {
		for(int i = powerDrops.size() - 1; i >= 0; i--) {
			if(powerDrops.get(i).getImage().getBoundsInParent().intersects(myMover.getBoundsInParent())) {
				if(powerDrops.get(i).getType() == 0)	extraBall();
				else if (powerDrops.get(i).getType() == 1) {
					if(powerUpDuration == 0) myMover.setWidth(MOVER_WIDTH * 1.5);
					powerUpDuration += 900;
				}
				else	extraLife();
				root.getChildren().remove(powerDrops.get(i).getImage());
				powerDrops.remove(i);
			}
			else if(powerDrops.get(i).getY() > 450) {
				root.getChildren().remove(powerDrops.get(i).getImage());
				powerDrops.remove(i);
			}
			else
				powerDrops.get(i).setY(powerDrops.get(i).getY() + POWERDROPSPEED * elapsedTime);
		}
	}
	
	private void extraBall() {
		int temp = myBouncers.size();
		for(int i = temp; i < temp + 2; i++) {
			Image image = new Image(getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
			double speedX = (Math.random() * 100 + BOUNCERSPEED) * (-1 + (int)(2*Math.random())*2);
			double speedY = (Math.random() * 50 + BOUNCERSPEED) * (-1 + (int)(2*Math.random())*2);
	    	myBouncers.add(new Bouncer(speedX, speedY, new ImageView(image)));
	        myBouncers.get(i).setX(myBouncers.get(0).getX());
	        myBouncers.get(i).setY(myBouncers.get(0).getY());
	        root.getChildren().add(myBouncers.get(i).getImage());
		}
	}
	
	private void extraLife() {
		if(life < maxLife)
			life++;
	}
	
	private void updatePaddle() {
		if(powerUpDuration == 0)
			myMover.setWidth(MOVER_WIDTH);
		if(powerUpDuration > 0)
			powerUpDuration--;
	}
	
	private void updateLabel() {
		showLevel.setText("Level: " + level);
		showLife.setText("Life: " + life);
		showPow.setText("PowerUpDuration: " + powerUpDuration/60);
	}
	
    private void updateBlock(Block block) {
    	if(block.getHit() == 1) {
    		dropPowerUp(block);
			root.getChildren().remove(block.getRec());
			blocks.remove(block);
    	}
    	else
    		block.setHit(block.getHit() - 1);
    	setBlockColor(block);
    }
	
    private void updateBouncers() {
    	if(myBouncers.get(0).getSpeedX() == 0)
    		myBouncers.get(0).setX(myMover.getX() + myMover.getWidth()/2);
    	for(int i = 0; i < myBouncers.size(); i++) {
    		if(myBouncers.get(i).getY() > SIZE + 20 || myBouncers.get(i).getY() < -20 && level == 4) {
    			root.getChildren().remove(myBouncers.get(i).getImage());
    			myBouncers.remove(myBouncers.get(i));
    			if(myBouncers.size() == 0)
    				life--;
    		}
    	}
	}
    
    private void updatePosition(double elapsedTime) {
    	for(int i = 0; i < myBouncers.size(); i++) {
    		updateSpeedY(myBouncers.get(i));
    		updateSpeedX(myBouncers.get(i));
        	for(int j = 0; j < blocks.size(); j ++) {
        		if(blocks.get(j).getBoundsInParent().intersects(myBouncers.get(i).getImage().getBoundsInParent())) {
        			if(Math.abs(blocks.get(j).getBoundsInParent().getMaxX() - myBouncers.get(i).getImage().getBoundsInParent().getMinX()) <= BOUNCERSPEED/80 ||
        					Math.abs(blocks.get(j).getBoundsInParent().getMinX() - myBouncers.get(i).getImage().getBoundsInParent().getMaxX()) <= BOUNCERSPEED/80)
        				myBouncers.get(i).setSpeedX(-myBouncers.get(i).getSpeedX());
        			else 
        				myBouncers.get(i).setSpeedY(-myBouncers.get(i).getSpeedY());
        			updateBlock(blocks.get(j));
        		}
        	}
        	myBouncers.get(i).setX(myBouncers.get(i).getX() + myBouncers.get(i).getSpeedX() * elapsedTime);
        	myBouncers.get(i).setY(myBouncers.get(i).getY() + myBouncers.get(i).getSpeedY() * elapsedTime);    		
    	}
    }
    
    private void updateSpeedY(Bouncer bouncer) {
    	if(level < 4) {
	    	if(bouncer.getY() < 0)
	    		bouncer.setSpeedY(-bouncer.getSpeedY());
	    	if(myMover.getBoundsInParent().intersects(bouncer.getImage().getBoundsInParent()))
	    		bouncer.setSpeedY(-Math.abs(bouncer.getSpeedY()));
    	}
    	else {
	    	if(myMover.getBoundsInParent().intersects(bouncer.getImage().getBoundsInParent()) && MOVER_BOT)
	    		bouncer.setSpeedY(-Math.abs(bouncer.getSpeedY()));
	    	else if (myMover.getBoundsInParent().intersects(bouncer.getImage().getBoundsInParent()) && !MOVER_BOT)
	    		bouncer.setSpeedY(Math.abs(bouncer.getSpeedY()));
    	}
    }
    
    private void updateSpeedX(Bouncer bouncer) {
    	if(bouncer.getX() > SIZE - 15) 
    		bouncer.setSpeedX(-Math.abs(bouncer.getSpeedX()));
    	if(bouncer.getX() < -2)
    		bouncer.setSpeedX(Math.abs(bouncer.getSpeedX()));
    }

    //cheats
    private void clearAll() {
    	for(int i = blocks.size() - 1; i >= 0; i--) {
    		root.getChildren().remove(blocks.get(i).getRec());
			blocks.remove(blocks.get(i));
    	}
    }

    private void lifeCheat() {
    	maxLife = Integer.MAX_VALUE;
    	life += 5;    	
    }
    
    private void destroy5()	{
    	int count = Math.min(5, blocks.size());
    	for(int i = count - 1; i >= 0; i--) {
    		int temp = (int)(Math.random() * blocks.size());
    		root.getChildren().remove(blocks.get(temp).getRec());
			blocks.remove(blocks.get(temp));
    	}
    }
    
    private void speedUp() {
    	for(int i = 0; i < myBouncers.size(); i++) {
    		myBouncers.get(i).setSpeedY(1.5 * myBouncers.get(i).getSpeedY());
    		myBouncers.get(i).setSpeedX(1.5 * myBouncers.get(i).getSpeedX());
    	}
    }
    
    private void handleKeyInput (KeyCode code) {
    	if(!start) {
    		if(code == KeyCode.ENTER) {
    			start = true;
    			root.getChildren().remove(0);
    		}
    	}
    	else {
		    if (code == KeyCode.UP && myBouncers.get(0).getSpeedX() == 0)	startBouncer();
		    else if (code == KeyCode.RIGHT && myMover.getX() < SIZE - myMover.getWidth())	myMover.setX(myMover.getX() + MOVER_SPEED);
		    else if (code == KeyCode.DOWN && level == 4 && !MOVER_BOT) {
		    	MOVER_BOT = true;
		    	myMover.setY(SIZE - myMover.getHeight());
		    }
		    else if (code == KeyCode.UP && level == 4 && MOVER_BOT) {
		    	MOVER_BOT = false;
		    	myMover.setY(0);
		    }
		    else if (code == KeyCode.LEFT && myMover.getX() > 0)	myMover.setX(myMover.getX() - MOVER_SPEED);
	        else if (code == KeyCode.DIGIT1)	clearAll();
	        else if (code == KeyCode.DIGIT2)	lifeCheat();
	        else if	(code == KeyCode.DIGIT3)	destroy5();
	        else if (code == KeyCode.DIGIT4)	speedUp();
    	}
    }

    public static void main (String[] args) {
        launch(args);
    }
}
