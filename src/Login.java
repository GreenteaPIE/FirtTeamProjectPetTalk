import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class Login extends JFrame implements MouseListener {
	UserDAO userDAO;
	UserDTO userDTO;

	PetTalkMainFrame main;
	String ImageIcon = null;
	JLabel jlTitle; // 타이틀
	JLabel jlId, jlPass; // 아이디, 비밀번호
	JTextField jtId; // 아이디 입력창
	JPasswordField jpPass; // 비밀번호
	JButton jbLogin, jbJoin, jbadmin; // 로그인, 회원가입

	JScrollPane scrollPane;
	ImageIcon icon;
	MainFrame mainframe;

	public Login() {

		icon = new ImageIcon("bg.png"); // 배경이미지

		// 배경 Panel 생성후 컨텐츠페인으로 지정
		JPanel background = new JPanel() {
			public void paintComponent(Graphics g) {
				// Approach 1: Dispaly image at at full size
				g.drawImage(icon.getImage(), 0, 0, null);
				// Approach 2: Scale image to size of component
				// Dimension d = getSize();
				// g.drawImage(icon.getImage(), 0, 0, d.width, d.height, null);
				// Approach 3: Fix the image position in the scroll pane
				// Point p = scrollPane.getViewport().getViewPosition();
				// g.drawImage(icon.getImage(), p.x, p.y, null);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};

		background.setLayout(null);

		// 타이틀
		background.add(jlTitle = new JLabel(""), JLabel.CENTER);
		jlTitle.setIcon(new ImageIcon("jltitle2.png")); // JButton에 이미지 달면 뒤에 배경이 투명해지지 않음
		jlTitle.setBounds(350 / 2 - 50, 15, 100, 70);
		jlTitle.setFont(new Font("맑은 고딕", Font.BOLD, 30));

		// 타이틀 이미지

		JLabel imgLabel = new JLabel();
		ImageIcon icon = new ImageIcon("pets.gif");
		imgLabel.setIcon(icon);
		imgLabel.setBounds(350 / 2 - 100, 85, 200, 200);
		imgLabel.setHorizontalAlignment(JLabel.CENTER);
		background.add(imgLabel);

		// 아이디, 비밀번호
		background.add(jlId = new JLabel("아이디", JLabel.LEFT));
		jlId.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		jlId.setBounds((350 / 3) - 50, 280, 100, 50);

		background.add(jtId = new RoundJTextField());
		jtId.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		jtId.setHorizontalAlignment(JTextField.CENTER);
		jtId.setBounds((350 / 3) + 60, 290, 120, 30);

		background.add(jlPass = new JLabel("비밀번호", JLabel.LEFT));
		jlPass.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		jlPass.setBounds((350 / 3) - 50, 310, 100, 50);

		background.add(jpPass = new RoundJPasswordField());
		jpPass.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		jpPass.setHorizontalAlignment(JPasswordField.CENTER);
		jpPass.setBounds((350 / 3) + 60, 320, 120, 30);
		jpPass.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {

				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					String userId = jtId.getText();
					String userPass = jpPass.getText();

					UserDAO uDAO = UserDAO.getDao();
					int result = uDAO.loginUser(userId, userPass);
					if (result == 1) {

						JOptionPane.showMessageDialog(null, "로그인 되었습니다. ");

						dispose();
						new MainFrame();
					} else {
						System.out.println(e);
						System.out.println("아이디 혹은 비밀번호 불일치");
						JOptionPane.showMessageDialog(null, "아이디 또는 비밀번호를 확인하세요.");
					}

				}

			}
		});

		// 버튼달기
		background.add(jbLogin = new JButton("")); // 로그인 버튼
		jbLogin.setIcon(new ImageIcon("jblogin.png")); // JButton에 이미지 달면 뒤에 배경이 투명해지지 않음
		jbLogin.setPressedIcon(new ImageIcon("jbloginclick.png")); // 클릭하면 이미지 변경
		jbLogin.setBounds(350 / 3 - 60, 360, 120, 50);
		jbLogin.setBorderPainted(false); // 버튼의 외곽선을 지워준다.
		jbLogin.setContentAreaFilled(false); // 버튼의 내용영역 채우기 하지 않음.
		jbLogin.setFocusPainted(false); // 버튼이 선택되었을 때 생기는 테두리 사용안함 .
		jbLogin.addMouseListener(this); // 이미지 위에 올렸을 때 이벤트 발생
		jbLogin.addActionListener(new ActionListener() { // 익명클래스(+)

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String id = jtId.getText().trim();
				String pw = jpPass.getText().trim();
				String login = "";

				if (id.length() == 0 || pw.length() == 0) { // 출처 : https://zzarungna.com/1608
					JOptionPane.showMessageDialog(null, "아이디 또는 비밀번호를 입력하십시오.");
					return;
				}
			}
		});

		background.add(jbJoin = new JButton("")); // 회원가입 버튼
		jbJoin.setIcon(new ImageIcon("jbjoin.png")); // JButton에 이미지 달면 뒤에 배경이 투명해지지 않음
		jbJoin.setPressedIcon(new ImageIcon("jbjoinclick.png")); // 클릭하면 이미지 변경
		jbJoin.setBounds(350 / 3 + 60, 360, 120, 50);
		jbJoin.setBorderPainted(false); // 버튼의 외곽선을 지워준다.
		jbJoin.setContentAreaFilled(false); // 버튼의 내용영역 채우기 하지 않음.
		jbJoin.setFocusPainted(false); // 버튼이 선택되었을 때 생기는 테두리 사용안함 .
		jbJoin.addMouseListener(this); // 이미지 위에 올렸을 때 이벤트 발생

		background.add(jbadmin = new JButton("관리자")); // 관리자 버튼
		jbadmin.setIcon(new ImageIcon("admin.png")); // JButton에 이미지 달면 뒤에 배경이 투명해지지 않음
		jbadmin.setBounds(350 / 2 - 60, 415, 120, 40);
		jbadmin.setBorderPainted(false); // 버튼의 외곽선을 지워준다.
		jbadmin.setContentAreaFilled(false); // 버튼의 내용영역 채우기 하지 않음.
		jbadmin.setFocusPainted(false); // 버튼이 선택되었을 때 생기는 테두리 사용안함 .
		jbadmin.addMouseListener(this);

		scrollPane = new JScrollPane(background);
		setContentPane(scrollPane);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("PET TLAK ver.1.0");
		setSize(350, 500);
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
	}

	// PetTalkMainFrame 과 연동
	public void setMain(PetTalkMainFrame main) {
		this.main = main;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

		Object obj = e.getSource();

		if ((JButton) obj == jbJoin) {
			dispose();
			System.out.println("회원가입 버튼이 눌렸습니다. ");
			new Join();
		}
		if ((JButton) obj == jbLogin) {
			System.out.println("로그인 버튼이 눌렸습니다. ");
			String userId = jtId.getText();
			String userPass = jpPass.getText();

			UserDAO uDAO = UserDAO.getDao();
			int result = uDAO.loginUser(userId, userPass);
			if (result == 1) {
			
				JOptionPane.showMessageDialog(this, "로그인 되었습니다. ");
				System.out.println("로그인 성공");
				System.out.println("로그인한 아이디: " + userId); // 이 부분을 추가

				dispose();

				MainFrame mainFrame = new MainFrame();
				mainFrame.setLoggedInUserId(userId); // 메인프레임에 로그인한 사용자 아이디 저장

				String loggedInUserId = mainFrame.getLoggedInUserId();

				mainFrame.setVisible(true);

				// 저장된 id 값 불러오기 테스트후에 주석처리

				System.out.println("메인프레임에 저장한 userId: " + loggedInUserId);

			} else {
				System.out.println(e);
				System.out.println("아이디 혹은 비밀번호 불일치");
				JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호를 확인하세요.");
			}
		}

		if ((JButton) obj == jbadmin) {
			String adminId = jtId.getText();
			String adminPw = jpPass.getText();

			// 관리자 로그인
			AdminDAO aDAO = AdminDAO.getDao();
			int result = aDAO.loginAdmin(adminId, adminPw);
			if (result == 1) {
				MainFrame loginInstance = new MainFrame();
				JOptionPane.showMessageDialog(this, "관리자 로그인 되었습니다. ");
				System.out.println("로그인 성공");

				dispose();
				new AdminFrame();

			} else {
				System.out.println(e);
				// System.out.println("아이디 혹은 비밀번호 불일치");
				JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호를 확인하세요.");
			}

		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) { // 버튼 안으로 들어오면
		// TODO Auto-generated method stub
		Object obj = e.getSource();
		if ((JButton) obj == jbLogin) {
			jbLogin.setIcon(new ImageIcon("jbloginclick.png")); // 클릭하면 이미지 변경
		}
		if ((JButton) obj == jbJoin) {
			jbJoin.setIcon(new ImageIcon("jbjoinclick.png")); // 클릭하면 이미지 변경
		}
	}

	@Override
	public void mouseExited(MouseEvent e) { // 버튼 밖으로 나가면
		// TODO Auto-generated method stub
		Object obj = e.getSource();
		if ((JButton) obj == jbLogin) {
			jbLogin.setIcon(new ImageIcon("jblogin.png")); // 클릭하면 이미지 변경
		}
		if ((JButton) obj == jbJoin) {
			jbJoin.setIcon(new ImageIcon("jbjoin.png")); // 클릭하면 이미지 변경
		}

	}

	public void contentSet() {
		userDTO = new UserDTO();
		String userId = jtId.getText();
		String userPass = jpPass.getText();

		userDTO.setUserId(userId);
		userDTO.setUserPass(userPass);
	}

	public static void main(String[] args) {

		Login frame = new Login();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("PET TLAK ver.1.0");
		frame.setSize(350, 500);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null); // 출처 :
											// https://stackoverflow.com/questions/3480102/java-jframe-setlocationrelativetonull-not-centering-the-window-on-ubuntu-10-0
	}

}