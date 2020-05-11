public class Main {

	public static void main(String[] args) {
		
		Window window = new Window("Bricks", 900, 700);
		BricksGame game = new BricksGame(window, window.getWidth()/2, window.getHeight()*3/4, 0);
		game.createConfig();
		window.open();
		while(window.isOpen()) {
			//game.update();
			window.refresh();
		}
	}

}
