import java.util.*;

public class BricksGame {

	static final int rows = 11;
	static final int cols = 15;
	private final int speed = 5500;
	int score;
	private ArrayList<Brick> bricks;
	Ball ball;
	Window window;
	double startX;
	double startY;
	double startAngle;
	double width;
	double height;
	public int Wallthiccness = 10;
	private final boolean groundDeath;
	boolean finished;
	private int scoreToWin;

	BricksGame(Window window, double x, double y, double angle, boolean groundDeath) {
		this.window = window;
		this.startX = x;
		this.startY = y;
		this.startAngle = angle;
		width = window.getWidth();
		height = window.getHeight();
		score = 0;
		this.groundDeath = groundDeath;
		finished = false;
	}

	void createConfig() {
		drawBackground();
		createBricks1();
		scoreToWin = bricks.size();
		drawBricks();
		ball = new Ball(startX, startY, startAngle, 2, 5);
		ball.draw(window);
	}

	void update() {
		if (score == scoreToWin) {
			endGame();
		}
		if (finished) {
			endGameWithScore();
		}

		drawBackground();
		drawBricks();
		ball.update();
		ball.draw(window);
		window.refreshAndClear(1000 / speed);
	}

	void endGameWithScore() {
		drawBackground();
		window.setColor(255, 255, 255);
		window.setFontSize(50);
		window.drawString("SCORE: " + score, width / 3, height / 2);
		window.refresh();
		window.waitUntilClosed();

	}

	void endGame() {
		drawBackground();
		window.setColor(255, 255, 255);
		window.setFontSize(50);
		window.drawString("WON", width / 3, height / 2);
		window.refresh();
		window.waitUntilClosed();
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
	
	void createBricks1() {
		bricks = new ArrayList<Brick>();
		double firstX = width * 0.1;
		double firstY = height * 0.1;
		double w = width * 0.8/ cols;
		double h = height * 0.4 / rows;
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				if((i + j) % 2 == 0) {
					bricks.add(new Brick(firstX + j * w, firstY + i * h, h, w));
				}
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
		drawScore();
	}

	void drawScore() {
		window.setFontSize(15);
		window.setColor(255, 255, 255);
		window.drawString("Score: " + score, 11, 25);
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
			this.size = size;
		}

		void draw(Window window) {
			window.setColor(255, 255, 0);
			window.fillCircle(x, y, size);
		}

		void update() {
			collisiondetection();
			this.x += Math.cos(toRad(angle)) * stepsize;
			this.y -= Math.sin(toRad(angle)) * stepsize;
		}

		double toRad(double angle) {
			return angle / 180 * Math.PI;
		}

		void collisiondetection() {
			double nextX = this.x + Math.cos(toRad(angle)) * stepsize;
			double nextY = this.y - Math.sin(toRad(angle)) * stepsize;
			wall(nextX, nextY);
			if (bricks(nextX, nextY))
				score++;
		}

		boolean bricks(double nextX, double nextY) {
			boolean change = false;
			Brick toDelete = new Brick(0, 0, 0, 0);
			for (Brick b : bricks) {
				if (!change) {
					switch (b.collides(nextX, nextY, size, angle)) {
					case 1:
						angle = toLeft();
						toDelete = b;
						change = true;
						break;
					case 2:
						angle = toUp();
						toDelete = b;
						change = true;
						break;
					case 3:
						angle = toRight();
						toDelete = b;
						change = true;
						break;
					case 4:
						angle = toDown();
						toDelete = b;
						change = true;
						break;
					default:
					}
				}
			}
			if (change) {
				bricks.remove(toDelete);
			}
			return change;
		}

		boolean wall(double nextX, double nextY) {
			boolean change = false;
			if (nextX + size >= width - Wallthiccness) {
				angle = toLeft();
				change = true;
			} else if (nextX - size <= Wallthiccness) {
				angle = toRight();
				change = true;
			}
			if (nextY + size >= height - Wallthiccness) {
				angle = toUp();
				change = true;
				if (groundDeath) {
					finished = true;
				}
			} else if (nextY - size <= Wallthiccness) {
				angle = toDown();
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

		double toDown() {
			if (angle >= 0 && angle <= 90) {
				return 360 - angle;
			} else if (angle >= 90 && angle <= 180) {
				return Math.abs(angle - 180) + 180;
			}
			return angle;
		}

		double toUp() {
			if (angle >= 180 && angle <= 270) {
				return 360 - angle;
			} else if (angle >= 270 && angle <= 360) {
				return 360 - angle;
			}
			return angle;
		}

		double toLeft() {
			if (angle <= 360 && angle >= 270) {
				return 360 - angle + 180;
			} else if (angle <= 90 && angle >= 0) {
				return 180 - angle;
			}
			return angle;
		}

		double toRight() {
			if (angle >= 90 && angle <= 180) {
				return 180 - angle;
			} else if (angle <= 270 && angle >= 180) {
				return 360 - angle + 180;
			}
			return angle;
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

		/*
		 * 0 -> no collision
		 * 1 -> collision west 
		 * 2 -> collision north 
		 * 3 -> collision east
		 * 4 -> collision south
		 */
		int collides(double nextX, double nextY, int size, double angle) {
			if (!(nextX + size < x || nextX - size > x + width)) {
				if (!(nextY + size < y || nextY - size > y + height)) {
					// if(nextX < x && nextY > y && nextY < y + height)
					if (angle >= 0 && angle <= 90) {
						if (nextX < x)
							return 1;
						return 4;
					} else if (angle > 90 && angle <= 180) {
						if (nextX > x + width)
							return 3;
						return 4;
					} else if (angle > 180 && angle <= 270) {
						if (nextX > x + width)
							return 3;
						return 2;
					} else if (angle > 270) {
						if (nextX < x)
							return 1;
						return 2;
					}
				}
				
			}
			return 0;
		}
	}
}
