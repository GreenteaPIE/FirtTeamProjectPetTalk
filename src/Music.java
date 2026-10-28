import java.io.BufferedInputStream;
import java.io.InputStream;

import javazoom.jl.player.Player;

public class Music extends Thread {

	private Player player;
	private boolean isLoop;
	private final String name;

	public Music(String name, boolean isLoop) {
		this.name = name;
		this.isLoop = isLoop;
		try {
			player = new Player(open());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// jar 로 묶여도 읽히도록 파일 경로 대신 클래스패스 스트림으로 연다
	private InputStream open() {
		return new BufferedInputStream(MainFrame.class.getResourceAsStream("/music/" + name));
	}

	public int getTime() {
		if (player == null)
			return 0;
		return player.getPosition();
	}

	public void close() {
		isLoop = false;
		player.close();
		this.interrupt();
	}

	@Override
	public void run() {
		try {
			do {
				player.play();
				player = new Player(open());
			} while (isLoop);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
