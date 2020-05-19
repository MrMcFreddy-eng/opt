import java.util.ArrayList;

public class BG {

	static final int rows = 11;
	static final int cols = 15;
	int score;
	private ArrayList<Brick> bricks;
	Ball ball;
	double startX;
	double startY;
	double startAngle;
	double width;
	double height;
	public int Wallthiccness = 10;
	private final boolean groundDeath;
	boolean finished;
	boolean end;
	private int scoreToWin;

	BG(double x, double y, double width, double height, double angle, boolean groundDeath) {
		this.startX = x;
		this.startY = y;
		this.startAngle = angle;
		this.width = width;
		this.height = height;
		score = 0;
		this.groundDeath = groundDeath;
		finished = false;
		end = false;
	}

	void createConfig() {
		createBricks1();
		scoreToWin = bricks.size();
		ball = new Ball(startX, startY, startAngle, 2, 5);
	}

	void update() {
		if (score == scoreToWin) {
			finished = true;
			System.out.println("WON");
		}
		if (finished) {
			System.out.println("Your score: " +  score);
			end = true;
		}
		ball.update();
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
