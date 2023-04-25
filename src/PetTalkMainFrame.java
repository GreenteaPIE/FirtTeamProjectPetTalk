
import javax.swing.JFrame;

public class PetTalkMainFrame {
	Login loginView;
	MainFrame loginTest;

	public static void main(String[] args) { // main Frame
		// TODO Auto-generated method stub

		// 메인클래스 실행
		PetTalkMainFrame main = new PetTalkMainFrame();
		main.loginView = new Login(); // 로그인창 보이기
		main.loginView.setMain(main);
		Music introMusic = new Music("introMusic.mp3", true);
		introMusic.start();
	}

	public void showFrameTest() {
		loginView.dispose();
		this.loginTest = new MainFrame();

	}

}
