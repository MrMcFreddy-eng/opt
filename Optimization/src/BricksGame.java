import java.util.*;

public class BricksGame {

	static final int rows = 10;
	static final int cols = 15;
	private ArrayList<Brick> bricks;
	Ball ball;
	Window window;
	double startX;
	double startY;
	double startAngle;
	double width;
	double height;
	public int Wallthiccness = 10;

	BricksGame(Window window, double x, double y, double angle) {
		this.window = window;
		this.startX = x;
		this.startY = y;
		this.startAngle = angle;
		width = window.getWidth();
		height = window.getHeight();
	}

	void createConfig() {
		drawBackground();
		createBricks();
		drawBricks();
		ball = new Ball(startX, startY, 1.0, 0.5, 50);
		ball.draw(window);
	}

	void update() {
		ball.update();
		ball.draw(window);
	}

	void createBricks() {
		bricks = new ArrayList<Brick>();
		double firstX = width * 0.1;
		double firstY = height * 0.1;
		double w = width * 0.8 / cols;
		double h = (height * 0.5 - height * 0.1) / rows;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				bricks.add(new Brick(firstX + j * w, firstY + i * h, h, w));
			}
		}
	}

	void drawBackground() {
		window.setColor(51, 51, 255);
		window.fillRect(0, 0, width, height);
		window.setColor(0, 0, 0);
		window.fillRect(0, 0, width, Wallthiccness);
		window.fillRect(0, 0, Wallthiccness, height);
		window.fillRect(width - Wallthiccness, 0, Wallthiccness, height);
		window.fillRect(0, height - Wallthiccness, width, Wallthiccness);
	}

	void drawBricks() {
		for (Brick b : bricks) {
			b.draw(window);
		}
	}

	class Ball {
		double x;
		double y;
		double angle;
		double stepsize;
		int size;

		Ball(double x, double y, double angle, double stepsize, int size) {
			this.x = x;
			this.y = y;
			this.angle = angle;
			this.stepsize = stepsize;
		}

		void draw(Window window) {
			window.setColor(255, 255, 0);
			window.fillCircle(x, y, size);
		}

		void update() {
			collisiondetection();
			this.x += Math.cos(toRad(angle)) * stepsize;
			this.y += Math.sin(toRad(angle)) * stepsize;
		}

		double toRad(double angle) {
			return angle / 180 * Math.PI;
		}

		void collisiondetection() {
			double nextX = this.x + Math.cos(angle) * stepsize;
			double nextY = this.y + Math.sin(angle) * stepsize;
			if (!wall(nextX, nextY)) {
			}
		}

		boolean wall(double nextX, double nextY) {
			boolean change = false;
			if (nextX + size >= width - Wallthiccness || nextX - size <= width + Wallthiccness) {
				angle = invX();
				change = true;
			}
			if (nextY + size >= height - Wallthiccness || nextY - size <= height + Wallthiccness) {
				angle = invY();
				change = true;
			}
			return change;
		}

		double invY() {
			return 360 - angle;
		}

		double invX() {
			if (angle >= 0 && angle <= 180)
				return 180 - angle;
			return 360 - angle + 180;
		}
	}

	class Brick {
		double x;
		double y;
		double width;
		double height;

		Brick(double x, double y, double height, double width) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		void draw(Window window) {
			window.setColor(255, 0, 0);
			window.fillRect(x, y, width, height);
			window.setColor(0, 0, 0);
			window.drawRect(x, y, width, height);
		}
	}
}
