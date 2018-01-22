package game_zy53;

import javafx.scene.image.ImageView;

public class Bouncer {
	private double SpeedX;
	private double SpeedY;
	private ImageView image;
	
	public Bouncer(double speedX, double speedY, ImageView image) {
		SpeedX = speedX;
		SpeedY = speedY;
		this.image = image;
	}

	public double getSpeedX() {
		return SpeedX;
	}

	public void setSpeedX(double speedX) {
		SpeedX = speedX;
	}

	public double getSpeedY() {
		return SpeedY;
	}

	public void setSpeedY(double speedY) {
		SpeedY = speedY;
	}

	public ImageView getImage() {
		return image;
	}
	
	public double getX() {
		return image.getX();
	}
	
	public double getY() {
		return image.getY();
	}
	
	public void setX(double inX) {
		image.setX(inX);
	}
	
	public void setY(double inY) {
		image.setY(inY);
	}
}
