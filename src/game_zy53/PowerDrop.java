package game_zy53;

import javafx.scene.image.ImageView;

public class PowerDrop {
	private int type;
	private ImageView image;
	
	public PowerDrop(int type, ImageView image) {
		this.type = type;
		this.image = image;
	}
	
	public int getType() {
		return type;
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
