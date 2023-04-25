import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import javax.swing.*;

public class PetChatClient implements ActionListener, Runnable {

	private static final int LOGIN = 100;
	private static final int EXIT = 200;
	private static final int NOMAL = 300;
	private static final int USERLIST = 400;
	private static final int WISPER = 500;

	private UserDTO userDTO;

	private String ip;
	private String id;
	private String contents;
	private Socket socket;
	private BufferedReader inMsg = null;
	private PrintWriter outMsg = null;

	private JPanel loginPanel;
	private JButton loginButton;
	private JLabel label1;
	private JTextField idInput;

	private JPanel logoutPanel;
	private JLabel label2;
	private JButton logoutButton;

	private JPanel msgPanel;
	private JTextField msgInput;
	private JButton exitButton;

	private JFrame jframe;
	private JTextArea msgOut;

	private JPanel chatpListPanel;
	private JLabel label3;
	private JTextArea listOut;

	private Container tab;
	private CardLayout clayout;
	private Thread thread;

	boolean status;

	public PetChatClient(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

	public void setTextFieldValue() {
		System.out.println("하이");
//      String id3 = userDTO.getChatN();      
//      idInput.setText(id3);
	}

	public PetChatClient(String ip) {

		this.ip = ip;
		loginPanel = new JPanel();
		loginPanel.setLayout(new BorderLayout());

		idInput = new JTextField(15);
		loginButton = new JButton("대화참가");
		loginButton.addActionListener(this);

		label1 = new JLabel("대화명");

		loginPanel.add(label1, BorderLayout.WEST);
		loginPanel.add(idInput, BorderLayout.CENTER);
		loginPanel.add(loginButton, BorderLayout.EAST);

		logoutPanel = new JPanel();

		logoutPanel.setLayout(new BorderLayout());
		label2 = new JLabel();
		logoutButton = new JButton("로그아웃");

		logoutButton.addActionListener(this);

		logoutPanel.add(label2, BorderLayout.CENTER);
		logoutPanel.add(logoutButton, BorderLayout.EAST);

		msgPanel = new JPanel();

		msgPanel.setLayout(new BorderLayout());
		msgInput = new JTextField(30);

		msgInput.addActionListener(this);
		msgInput.setEditable(false); // 로그인 하기 전에는 채팅입력 불가
		exitButton = new JButton("종료");
		exitButton.addActionListener(this);

		msgPanel.add(msgInput, BorderLayout.CENTER);
		msgPanel.add(exitButton, BorderLayout.EAST);

		tab = new JPanel();
		clayout = new CardLayout();
		tab.setLayout(clayout);
		tab.add(loginPanel, "login");
		tab.add(logoutPanel, "logout");

		jframe = new JFrame("PetChat");
		Image img = new ImageIcon("bg3.png").getImage();
		msgOut = new JTextArea("", 10, 25) {
			{
				setOpaque(false);
			}

			public void paintComponent(Graphics g) {
				g.drawImage(img, 0, 0, null); // 이미지 그리기
				super.paintComponent(g);
			}
		};
		msgOut.setLineWrap(true); // JTextArea의 자동 줄바꿈 기능 활성화
		msgOut.setWrapStyleWord(true); // 단어 단위로 줄바꿈 수행

		msgOut.setEditable(false);

		JScrollPane jsp = new JScrollPane(msgOut, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		chatpListPanel = new JPanel(); // 채팅 참가자
		chatpListPanel.setLayout(new BorderLayout());

		label3 = new JLabel(" @채팅참가자");
		listOut = new JTextArea("", 10, 7) {
			{
				setOpaque(false);
			}

			public void paintComponent(Graphics g) {
				g.drawImage(img, 0, 0, null); // 이미지 그리기
				super.paintComponent(g);
			}
		}; // 채팅참가자를 나타낼 영역
		listOut.setEditable(false); // 편집불가
		JScrollPane jsp2 = new JScrollPane(listOut, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		chatpListPanel.add(label3, BorderLayout.NORTH); // 패널에 라벨과 스크롤을 갖다 붙임
		chatpListPanel.add(jsp2, BorderLayout.CENTER);
		jframe.add(tab, BorderLayout.NORTH);
		jframe.add(jsp, BorderLayout.WEST);
		jframe.add(chatpListPanel, BorderLayout.EAST);
		jframe.add(msgPanel, BorderLayout.SOUTH);

		clayout.show(tab, "login");

		jframe.setBounds(10, 0, 380, 500);
		int x = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - jframe.getWidth()) / 2;
		int y = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - jframe.getHeight()) / 2 - 40;
		jframe.setLocation(x, y);
		// jframe.setUndecorated(true);
		jframe.setVisible(true);
		jframe.setResizable(false);

	}

	public void connectServer() {
		try {

			socket = new Socket(ip, 9888);
			System.out.println("[PetTalk]Server 연결 성공!!");

			inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outMsg = new PrintWriter(socket.getOutputStream(), true);

			outMsg.println(LOGIN + "/" + id); // LOGIN 명령어로 해당 ID 출력

			thread = new Thread(this);
			thread.start();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("서버연결불가");
			if (!socket.isClosed()) {
				stopClient();
			}
			return;
		}
	}

	public void stopClient() {
		System.out.println("연결끊음");

		msgOut.setText(""); // 채팅창 비우기
		listOut.setText(" "); // 참가자 창 비우기
		msgInput.setEditable(false); // 채팅입력불가
		clayout.show(tab, "login");
		status = false;

		if (socket != null && !socket.isClosed()) {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}

	}

	public void actionPerformed(ActionEvent arg0) {
		Object obj = arg0.getSource();

		if (obj == exitButton) {
			try {
				jframe.setVisible(false);
				outMsg.println(EXIT + "/" + id);
				stopClient();
				jframe.setVisible(false);

			} catch (Exception e) {
				System.out.println("채팅 닫음");
			}
		} else if (obj == loginButton) {

			id = idInput.getText().trim();
			label2.setText("대화명 : " + id);
			clayout.show(tab, "나가기");
			msgInput.setEditable(true); // 채팅입력 창 활성화
			connectServer();

		} else if (obj == msgInput) {
			Thread thread = new Thread() {

				@Override
				public void run() {
					contents = msgInput.getText();
					// 입력창의 내용 contents에 대입

					if (contents.indexOf("속삭임") == 0) {
						int sprit = contents.indexOf(" ") + 1;
						int end = contents.indexOf(" ", sprit);
						String toid = contents.substring(sprit, end);

						String wisper = contents.substring(end + 1);
						outMsg.println(WISPER + "/" + id + "/" + toid + "/" + wisper);
					} else {

						outMsg.println(NOMAL + "/" + id + "/" + contents);
						msgInput.setText("");
					}
				}
			};
			thread.start();

		}
	}

	public void run() {
		UserDTO udto = new UserDTO();

		String uid = udto.getChatN();

		String msg;
		String[] rmsg;

		status = true;

		while (status) { // 수신부
			try {

				msg = inMsg.readLine();
				rmsg = msg.split("/");
				int commend = Integer.parseInt(rmsg[0]);

				switch (commend) {

				case USERLIST: { // 채팅참가자 리스트가 온 경우
					String[] userlist = rmsg[1].split(",");
					// 1번 인덱스에 있는 참가자 ID SET을 ,를 구분자로 하여 userlist배열에 담기
					int size = userlist.length;
					listOut.setText(" "); // 참가자 리스트창 비우기

					for (int i = 0; i < size; i++) { // 요소 하나씩 읽어들여서 참가자 리스트에 추가
						listOut.append(userlist[i]);
						listOut.append("\n");
					}

					break;
				}

				case WISPER: {
					msgOut.append(rmsg[1] + "님의 속삭임이  " + rmsg[2] + "님에게" + "\n" + rmsg[3] + "\n");
					break;
				}

				default:
					msgOut.append(rmsg[1] + ">" + rmsg[2] + "\n");
					break;
				}

				msgOut.setCaretPosition(msgOut.getDocument().getLength());
			} catch (Exception e) {
				System.out.println("채팅닫음" + "로그인아이디" + uid);
				status = false;
			}
		}

		System.out.println("[MultiChatClient]" + thread.getName() + "종료됨");
	}

	public static void main(String[] args) {
		PetChatClient PetChat = new PetChatClient("127.0.0.1");

	}
}