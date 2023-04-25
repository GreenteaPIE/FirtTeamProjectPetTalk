import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

class Join extends JFrame implements ActionListener, MouseListener {

	UserDAO userDAO;
	UserDTO userDTO;

	String ImageIcon = null;
	JLabel jlTitle;
	JLabel jluserID, jluserPass, jluserName, jluserAge, jluserNum, jluserAddress; // 회원가입 항목
	JTextField jtuserID, jtuserName, jtuserAge, jtuserNum, jtuserAddress; // 항목입력칸
	JPasswordField jpuserPass; // 비밀번호 입력칸!!
	JPasswordField jpuserPass2; // 비밀번호 중복확인 입력칸!!
	JLabel jluserPass2; // 비밀번호 중복확인 라벨

	JLabel jlchk1, jlchk2; // 선택약관
	JRadioButton jrbtn1;

	JButton jbIdChk, jbJoin, jbReset; // 버튼

	JLabel jlPwChk, jlxIcon; // 라벨에 이미지 붙이기

	JScrollPane scrollPane;
	ImageIcon icon;

	public Join() {
		userDAO = new UserDAO();
		icon = new ImageIcon("bg2.png"); // 배경이미지

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
		background.add(jlTitle = new JLabel("회원가입"), JLabel.CENTER);
		jlTitle.setIcon(new ImageIcon("jljoin.png")); // JButton에 이미지 달면 뒤에 배경이 투명해지지 않음
		jlTitle.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		jlTitle.setBounds((350 / 2) - 100, 0, 200, 100);
//		jlTitle.setBorder(BorderFactory.createBevelBorder(0));

		// 사용자 정보
		// 아이디
		background.add(jluserID = new JLabel("아이디", JLabel.CENTER));
		jluserID.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		jluserID.setBounds(((350 / 3) - 60), 70, 80, 50);

		background.add(jtuserID = new RoundJTextField());
		jtuserID.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		jtuserID.setHorizontalAlignment(JTextField.CENTER);
		jtuserID.setBounds(((((350 / 3) - 60) / 2) + 145), 80, 120, 30);
		jtuserID.addKeyListener(new KeyAdapter() { // 엔터 이벤트
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				int key = e.getKeyCode();
				String id = jtuserID.getText().trim();

				if (id.length() == 0 && key == KeyEvent.VK_ENTER) { // 출처 : https://zzarungna.com/1608
					JOptionPane.showMessageDialog(null, "아이디를 입력하십시오.");
					return;
				}

				if (key == KeyEvent.VK_ENTER) {
					String userId = jtuserID.getText();

					UserDAO uDAO = UserDAO.getDao();
					int result = uDAO.idCheck(userId);
					if (result == 1) {
						JOptionPane.showMessageDialog(null, "입력한 아이디를 사용하실 수 있습니다. ");
						System.out.println("중복 아이디 없음");

					} else {
						System.out.println(e);
						System.out.println("중복 아이디 있음");
						JOptionPane.showMessageDialog(null, "이미 사용중인 아이디입니다.\n 다시 입력하세요.");
						jtuserID.setText("");
					}

				}

			}
		});

		// 아이디 중복확인
		background.add(jbIdChk = new JButton(""));
		jbIdChk.setIcon(new ImageIcon("jbidchk.png")); // JButton에 이미지 달면 뒤에 배경이 투명해지지 않음
//		jbIdChk.setPressedIcon(new ImageIcon("jbjoinclick.png")); // 클릭하면 이미지 변경
		jbIdChk.setBorderPainted(false); // 버튼의 외곽선을 지워준다.
		jbIdChk.setContentAreaFilled(false); // 버튼의 내용영역 채우기 하지 않음.
		jbIdChk.setFocusPainted(false); // 버튼이 선택되었을 때 생기는 테두리 사용안함 .
		jbIdChk.setBounds(((((350 / 3) - 60) / 2) + 150 + 120), 85, 20, 20);
		jbIdChk.addActionListener(new ActionListener() { // 입력 값이 없을 때
			@Override
			public void actionPerformed(ActionEvent ex) {
				// TODO Auto-generated method stub
				String id = jtuserID.getText().trim();

				if (id.length() == 0) { // 출처 : https://zzarungna.com/1608
					JOptionPane.showMessageDialog(null, "사용할 아이디를 입력하십시오.");
					return;
				}
			}
		});
		jbIdChk.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent ex) {
				// TODO Auto-generated method stub
				Object obj = ex.getSource();
				if ((JButton) obj == jbIdChk) {
					String userId = jtuserID.getText();

					UserDAO uDAO = UserDAO.getDao();
					int result = uDAO.idCheck(userId);
					if (result == 1) {
						JOptionPane.showMessageDialog(null, "입력한 아이디를 사용하실 수 있습니다. ");
						System.out.println("중복 아이디 없음");
					} else {
						System.out.println(ex);
						System.out.println("중복 아이디 있음");
						JOptionPane.showMessageDialog(null, "이미 사용중인 아이디입니다.\n 다시 입력하세요.");
						jtuserID.setText("");
					}

				}

			}
		});

		// 비밀번호
		background.add(jluserPass = new JLabel("비밀번호", JLabel.CENTER));
		jluserPass.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		jluserPass.setBounds(((350 / 3) - 60), 70 + 40, 80, 50);

		background.add(jpuserPass = new RoundJPasswordField());
		jpuserPass.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		jpuserPass.setHorizontalAlignment(JTextField.CENTER);
		jpuserPass.setBounds(((((350 / 3) - 60) / 2) + 145), 120, 120, 30);

		// 비밀번호 재입력

		background.add(jluserPass2 = new JLabel("비밀번호 확인", JLabel.CENTER));
		jluserPass2.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		jluserPass2.setBounds(((350 / 3) - 60), 70 + 80, 90, 50);

		background.add(jlPwChk = new JLabel()); // 비밀번호가 일치하면 옆에 뜨는 아이콘
		jlPwChk.setIcon(new ImageIcon("jbidchk.png")); // JButton에 이미지 달면 뒤에 배경이 투명해지지 않음
		jlPwChk.setBounds(((((350 / 3) - 60) / 2) + 150 + 120), 165, 20, 20);
		jlPwChk.setVisible(false);

		background.add(jlxIcon = new JLabel()); // 비밀번호가 틀리면 옆에 뜨는 아이콘
		jlxIcon.setIcon(new ImageIcon("xicon.png")); // JButton에 이미지 달면 뒤에 배경이 투명해지지 않음
		jlxIcon.setBounds(((((350 / 3) - 60) / 2) + 150 + 120), 165, 20, 20);
		jlxIcon.setVisible(false);

		background.add(jpuserPass2 = new RoundJPasswordField());
		jpuserPass2.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		jpuserPass2.setHorizontalAlignment(JTextField.CENTER);
		jpuserPass2.setBounds(((((350 / 3) - 60) / 2) + 145), 160, 120, 30);
		jpuserPass2.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { // 문자키를 눌렀을 때 (문자키에만 반응)
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) { // 키보드를 뗐을 때 (모든 키에 반응)
				// TODO Auto-generated method stub

				int key = e.getKeyCode();
				String userPass = jpuserPass.getText().trim();

				String userPass1 = jpuserPass.getText();
				String userPass2 = jpuserPass2.getText();

				if (userPass1.equals(userPass2)) {
					System.out.println("비밀번호 일치함");
					jlPwChk.setVisible(true);
					jlxIcon.setVisible(false);

				} else {
					System.out.println(e);
					System.out.println("비밀번호 일치하지 않음");
					jlxIcon.setVisible(true);
					jlPwChk.setVisible(false);

//					JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.\n다시 입력해주세요.");
//					jpuserPass.setText("");
//					jpuserPass2.setText("");

				}

			}

			@Override
			public void keyPressed(KeyEvent e) { // 키를 눌렀을 때 (모든 키에 반응)
				// TODO Auto-generated method stub

				int key = e.getKeyCode();
				String userPass = jpuserPass.getText().trim();

				if (userPass.length() == 0 || userPass.length() == 0 && key == KeyEvent.VK_ENTER) { // 출처 :
																									// https://zzarungna.com/1608
					JOptionPane.showMessageDialog(null, "비밀번호를 입력하십시오.");
					return;
				}

				if (key == KeyEvent.VK_ENTER) {
					String userPass1 = jpuserPass.getText();
					String userPass2 = jpuserPass2.getText();

					if (userPass1.equals(userPass2)) {
						System.out.println("비밀번호 일치함");
						jlxIcon.setVisible(false);
						jlPwChk.setVisible(true);

					} else {
						System.out.println(e);
						System.out.println("비밀번호 일치하지 않음");
						jlPwChk.setVisible(false);
						jlxIcon.setVisible(true);
						JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.\n다시 입력해주세요.");
						jpuserPass.setText("");
						jpuserPass2.setText("");
					}

				}

			}

		});

		// 이름
		background.add(jluserName = new JLabel("이름", JLabel.CENTER));
		jluserName.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		jluserName.setBounds(((350 / 3) - 60), 70 + (40 * 3), 80, 50);

		background.add(jtuserName = new RoundJTextField());
		jtuserName.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		jtuserName.setHorizontalAlignment(JTextField.CENTER);
		jtuserName.setBounds(((((350 / 3) - 60) / 2) + 145), 200, 120, 30);

		// 나이
		background.add(jluserAge = new JLabel("나이", JLabel.CENTER));
		jluserAge.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		jluserAge.setBounds(((350 / 3) - 60), 70 + (40 * 4), 80, 50);

		background.add(jtuserAge = new RoundJTextField());
		jtuserAge.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		jtuserAge.setHorizontalAlignment(JTextField.CENTER);
		jtuserAge.setBounds(((((350 / 3) - 60) / 2) + 145), 240, 120, 30);

		// 연락처
		background.add(jluserNum = new JLabel("연락처", JLabel.CENTER));
		jluserNum.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		jluserNum.setBounds(((350 / 3) - 60), 70 + (40 * 5), 80, 50);

		background.add(jtuserNum = new RoundJTextField());
		jtuserNum.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		jtuserNum.setHorizontalAlignment(JTextField.CENTER);
		jtuserNum.setBounds(((((350 / 3) - 60) / 2) + 145), 280, 120, 30);

		// 주소
		background.add(jluserAddress = new JLabel("주소", JLabel.CENTER));
		jluserAddress.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		jluserAddress.setBounds(((350 / 3) - 60), 70 + (40 * 6), 80, 50);

		background.add(jtuserAddress = new RoundJTextField());
		jtuserAddress.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		jtuserAddress.setHorizontalAlignment(JTextField.CENTER);
		jtuserAddress.setBounds(((((350 / 3) - 60) / 2) + 145), 320, 120, 30);

		// 선택약관 1
		background.add(jlchk2 = new JLabel("마케팅 활용 동의 및 광고 수신 동의"));
		jlchk2.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 13));
		jlchk2.setBounds((350 / 2) - 90, 345, 200, 40);

		background.add(jrbtn1 = new JRadioButton());
		jrbtn1.setBounds(350 / 3 - 10, 370, 25, 25);

		background.add(jlchk1 = new JLabel("SMS 수신동의(선택)"));
		jlchk1.setBounds((350 / 3) + 30, 367, 200, 30);

		// 회원가입 버튼 or 취소 버튼
		background.add(jbJoin = new JButton(""));
		jbJoin.setIcon(new ImageIcon("jbjoin.png")); // JButton에 이미지 달면 뒤에 배경이 투명해지지
														// 않음
		jbJoin.setPressedIcon(new ImageIcon("jbjoinclick.png")); // 클릭하면 이미지 변경
		jbJoin.setBorderPainted(false); // 버튼의 외곽선을 지워준다.
		jbJoin.setContentAreaFilled(false); // 버튼의 내용영역 채우기 하지 않음.
		jbJoin.setFocusPainted(false); // 버튼이 선택되었을 때 생기는 테두리 사용안함 .
		jbJoin.addMouseListener(this); // 이미지 버튼 눌렸을 때 이벤트 발생
		jbJoin.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		jbJoin.setBounds(350 / 3 - 60, 400, 120, 50);
		jbJoin.addActionListener(this);

		background.add(jbReset = new JButton(""));
		jbReset.setIcon(new ImageIcon("cancel.png")); // JButton에 이미지 달면 뒤에 배경이
														// 투명해지지 않음
		jbReset.setPressedIcon(new ImageIcon("cancel.png")); // 클릭하면 이미지 변경
		jbReset.setBorderPainted(false); // 버튼의 외곽선을 지워준다.
		jbReset.setContentAreaFilled(false); // 버튼의 내용영역 채우기 하지 않음.
		jbReset.setFocusPainted(false); // 버튼이 선택되었을 때 생기는 테두리 사용안함 .
		jbReset.addMouseListener(this); // 이미지 버튼 눌렸을 때 이벤트 발생
		jbReset.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		jbReset.setBounds(350 / 3 + 60, 400, 120, 50);
		jbReset.addActionListener(this);

		scrollPane = new JScrollPane(background);

		setContentPane(scrollPane);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("REGISTER");
		setSize(350, 500);
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

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
		if ((JButton) obj == jbJoin) {
			jbJoin.setIcon(new ImageIcon("jbjoinclick.png")); // 클릭하면 이미지 변경
		}
		if ((JButton) obj == jbReset) {
			jbReset.setIcon(new ImageIcon("cancel.png")); // 클릭하면 이미지 변경
		}
	}

	@Override
	public void mouseExited(MouseEvent e) { // 버튼 밖으로 나가면
		// TODO Auto-generated method stub
		Object obj = e.getSource();
		if ((JButton) obj == jbJoin) {
			jbJoin.setIcon(new ImageIcon("jbJoin.png")); // 클릭하면 이미지 변경
		}
		if ((JButton) obj == jbReset) {
			jbReset.setIcon(new ImageIcon("cancel.png")); // 클릭하면 이미지 변경
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		Object obj = e.getSource();
		if ((JButton) obj == jbJoin) {
			System.out.println("회원가입 버튼이 눌렸습니다. ");

			try {
				contentSet();
				int result = userDAO.insertUser(userDTO);
				if (result == 1) {
					JOptionPane.showMessageDialog(this, "회원가입이 완료되었습니다.");
					dispose();
					new Login();

				} else {
					JOptionPane.showMessageDialog(this, "실패하였습니다. ");
				}

			} catch (Exception ex) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(this, "값을 입력하세요.");
			} finally {

			}
		}

		if ((JButton) obj == jbReset) {
			System.out.println("취소 버튼이 눌렸습니다. ");
			dispose();
			new Login();
		}
	}

	public void contentClear() {
		jtuserID.setText("");
		jpuserPass.setText("");
		jtuserName.setText("");
		jtuserAge.setText("");
		jtuserNum.setText("");
		jtuserAddress.setText("");
	}

	public void contentSet() {
		userDTO = new UserDTO();
		String userId = jtuserID.getText();
		String userPass = jpuserPass.getText();
		String userName = jtuserName.getText();
		int userAge = Integer.parseInt(jtuserAge.getText());
		String userNum = jtuserNum.getText();
		String userAddress = jtuserAddress.getText();

		userDTO.setUserId(userId);
		userDTO.setUserPass(userPass);
		userDTO.setUserName(userName);
		userDTO.setUserAge(userAge);
		userDTO.setUserNum(userNum);
		userDTO.setUserAddress(userAddress);
	}

	public static void main(String[] args) {

//		userDAO dao = new userDAO();
//		dao.getConn(); //mysql 접속성공 

		Join joinframe = new Join();
		joinframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		joinframe.setSize(350, 500);
		joinframe.setResizable(false);
		joinframe.setVisible(true);
		joinframe.setLocationRelativeTo(null);
	}
}