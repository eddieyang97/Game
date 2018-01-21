package game_zy53;

import javafx.geometry.Bounds;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Block {
	private int hit2destroy;
	private Rectangle rec;
	
	public Block(int hit2destroy, Rectangle rec) {
		this.hit2destroy = hit2destroy;
		this.rec = rec;
	}

	public int getHit() {
		return hit2destroy;
	}

	public void setHit(int hit2destroy) {
		this.hit2destroy = hit2destroy;
	}

	public Rectangle getRec() {
		return rec;
	}
	
	public void setFill(Paint value) {
		rec.setFill(value);
	}
	
	public Bounds getBoundsInParent() {
		return rec.getBoundsInParent();
	}
}
