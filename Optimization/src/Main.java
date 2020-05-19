public class Main {

	public static void main(String[] args) {
		
		double angle = 20;
		Window window = new Window("Bricks", 900, 700);
		BricksGame game = new BricksGame(window, window.getWidth()/2, window.getHeight()*3/4, angle, true);
		BG g = new BG(window.getMouseX()/2, window.getHeight()*3/4, window.getWidth(), window.getHeight(), angle, true);
		g.createConfig();
		while(!g.end) {
			g.update();
		}
		game.createConfig();
		window.open();
		while(window.isOpen()) {
			game.update();
		}
	}

}
