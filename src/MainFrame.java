import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Member;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class MainFrame extends JFrame implements ActionListener { // 로그인 성공하면 띄울 페이지

	UserDTO userDTO;
	UserDAO userDAO;
	JLabel jlTitle, mTitle, pluserID;
	JLabel mName, mAge, mNum, mAddress; // 불러올 유저정보
	RoundJTextField mtName;
	RoundJTextField mtAge, mtNum, mtAddress; // db에서 유저정보 불러오기
	JLabel jlpetName, jlpetAge, jlpetSex, jlpetBreed; // 반려동물 항목
	RoundJTextField jtpetName, jtpetAge, jtpetSex, jtpetBreed, ptuserID; // 항목입력칸
	JLabel dbpetName, dbpetAge, dbpetSex, dbpetBreed; // db에서 반려동물 불러오기
	RoundJTextField dpetName, dpetAge, dpetSex, dpetBreed; // 불러올 반려동물정보

	JRadioButton jrbtn1;

	JButton jbSubmit, jbReset, jbPet; // 버튼
	JButton my_info; // 내 정보
	JButton btn2; // 채팅
	JButton btn3; // 동물병원
	JButton btn4; // 캐치마인드
	JButton jb;
	// JTextArea jtChatt; // 채팅

	JPanel friendsPanel = new JPanel(); // 친구목록 만들 친구메인패널생성
	RoundJTextField addffriend;
	JButton abfriend;

	private JList<Member> userList; // 친구목록 리스트
	private DefaultListModel<Member> listModel;
	private JScrollPane jspane;

	JPanel mainPen = new JPanel(); // 버튼클릭시 바뀌는 메인패널
	JPanel subPen = new JPanel(); // 내정보안에 반려동물정보추가 패널

	JScrollPane scrollPane;
	ImageIcon icon;

	boolean visible1 = false;
	boolean visible2 = true;

	private String loggedInUserId; // 저장할 로그인id

	public void setLoggedInUserId(String userId) {
		this.loggedInUserId = userId;
	}

	public String getLoggedInUserId() {
		return loggedInUserId;
	}

	public MainFrame() {

		userDAO = new UserDAO();
		icon = new ImageIcon("bg3.gif"); // 배경이미지
		JLabel gifLabel = new JLabel(icon);
		setContentPane(gifLabel);
		setLayout(null);

		// 친구목록,내정보수정페이지 모두 담을 큰 패널
		JPanel firstPanel = new JPanel();
		firstPanel.setBounds(0, 85, 400, 480); // 패널 위치
		firstPanel.setBackground(new Color(185, 218, 255)); // 배경색 설정
		firstPanel.setLayout(null);
		firstPanel.setVisible(true);

		// 친구목록이 보이는 패널
		JPanel friendsPanel = new JPanel(); // 친구목록 만들 패널생성
		friendsPanel.setBounds(0, 0, 400, 535); //
		friendsPanel.setBackground(new Color(185, 218, 255)); // 배경색 설정
		friendsPanel.setVisible(true);
		friendsPanel.setLayout(null);

		RoundJTextField addffriend = new RoundJTextField(); // 친구검색 필드
		addffriend.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		addffriend.setBounds(100, 0, 150, 20);
		friendsPanel.add(addffriend);
		
		listModel = new DefaultListModel<>();  //친구목록리스트

		// DefaultListModel 생성
		DefaultListModel<String> listModel = new DefaultListModel<>();
		// JList 생성 및 DefaultListModel 설정
		JList<String> userList = new JList<>(listModel);
		// JScrollPane 생성 및 JList 추가
		JScrollPane jspane = new JScrollPane(userList);
		// JList의 높이를 50으로 설정
		userList.setFixedCellHeight(50);
		jspane.setBounds(0, 40, 400, 600);
		// friendsPanel에 JScrollPane 추가
		friendsPanel.add(jspane);

		friendsPanel.add(abfriend = new JButton()); // 친구추가 버튼
		abfriend.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		abfriend.setIcon(new ImageIcon("plus.png"));
		abfriend.setBorderPainted(false); // 버튼의 외곽선을 지워준다.
		abfriend.setContentAreaFilled(false); // 버튼의 내용영역 채우기 하지 않음.
		abfriend.setFocusPainted(false); // 버튼이 선택되었을 때 생기는 테두리 사용안함 .
		abfriend.setBounds(260, 0, 30, 25);
		abfriend.setBounds(253, -3, 30, 25);
		abfriend.addActionListener(this);
		abfriend.addActionListener(this);
		abfriend.addActionListener(new ActionListener() {

			private ResultSet friendListRs;

			@Override
			public void actionPerformed(ActionEvent e) {
				String friendId = addffriend.getText();

				if (friendId.isEmpty()) {
					JOptionPane.showMessageDialog(null, "친구 ID를 입력해주세요.");
					return;
				}

				if (friendId.equals(loggedInUserId)) {
					JOptionPane.showMessageDialog(null, "자신은 추가할 수 없습니다.");
					return;
				}

				String furl = "jdbc:mysql://localhost:3306/Mini";
				String fuser = "root";
				String fpassword = "1234";

				try (Connection conn = DriverManager.getConnection(furl, fuser, fpassword)) {
					String selectSql = "SELECT * FROM Breeder WHERE userId = ?";
					PreparedStatement selectStmt = conn.prepareStatement(selectSql);
					selectStmt.setString(1, friendId);
					ResultSet rs = selectStmt.executeQuery();

					if (rs.next()) { // 친구 ID가 Breeder 테이블에 존재하는 경우
						String fId = rs.getString("userId");

						// Friendlist 테이블에서 이미 추가되어 있는지 검색
						String checkSql = "SELECT * FROM Friendlist WHERE fId = ? AND userId = ?";
						PreparedStatement checkStmt = conn.prepareStatement(checkSql);
						checkStmt.setString(1, fId);
						checkStmt.setString(2, loggedInUserId);
						ResultSet checkRs = checkStmt.executeQuery();

						if (checkRs.next()) { // 이미 추가된 경우
							JOptionPane.showMessageDialog(null, "이미 추가 된 친구입니다.");
						} else { // 추가되지 않은 경우
							String insertSql = "INSERT INTO Friendlist(fId, userId) VALUES (?, ?)";
							PreparedStatement insertStmt = conn.prepareStatement(insertSql);
							insertStmt.setString(1, fId);
							insertStmt.setString(2, loggedInUserId);
							insertStmt.executeUpdate();

							String selectAllSql = "SELECT * FROM Friendlist WHERE userId = ?";
							PreparedStatement selectAllStmt = conn.prepareStatement(selectAllSql);
							selectAllStmt.setString(1, loggedInUserId);
							ResultSet friendListRs = selectAllStmt.executeQuery();

							DefaultListModel<String> listModel = new DefaultListModel<>();
							while (friendListRs.next()) {
								String friendId1 = friendListRs.getString("fId");
								listModel.addElement(friendId1);
							}
							userList.setModel(listModel);
							friendListRs.close();
							selectAllStmt.close();

							if (listModel.isEmpty()) {
								JOptionPane.showMessageDialog(null, "친구를 찾을 수 없습니다.");
							} else {
								JOptionPane.showMessageDialog(null, "친구 추가가 완료되었습니다.");
							}
						}

						checkRs.close();
						checkStmt.close();

						abfriend.getModel().setPressed(false);
						abfriend.getModel().setArmed(false);
					} else { // 친구 ID가 Breeder 테이블에 존재하지 않는 경우
						JOptionPane.showMessageDialog(null, "친구를 찾을 수 없습니다.");
					}

					rs.close();
					selectStmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		// 친구list 버튼
		JButton jFriendsbutton1 = new JButton();
		jFriendsbutton1.setIcon(new ImageIcon("user.png"));
		jFriendsbutton1.setBounds(0, 550, 100, 100);
		jFriendsbutton1.setBorderPainted(false); // 버튼의 외곽선을 지워준다.
		jFriendsbutton1.setContentAreaFilled(false); // 버튼의 내용영역 채우기 하지 않음.
		jFriendsbutton1.setFocusPainted(false); // 버튼이 선택되었을 때 생기는 테두리 사용안함 .

		add(jFriendsbutton1);
		add(firstPanel);
		firstPanel.add(friendsPanel);

		jFriendsbutton1.addActionListener(this); // 친구list 눌렀을때
		jFriendsbutton1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				friendsPanel.setVisible(true); // 친구창 활성호
				mainPen.setVisible(false);

			}
		});

		// 타이틀
		add(jlTitle = new JLabel(""), JLabel.CENTER);
		jlTitle.setIcon(new ImageIcon("jltitle3.png")); // JButton에 이미지 달면 뒤에 배경이 투명해지지 않음
		jlTitle.setBounds(400 / 2 - 75, 5, 150, 80);
		jlTitle.setFont(new Font("맑은 고딕", Font.BOLD, 30));

		// 내 정보
		friendsPanel.add(my_info = new RoundedButton("내 정보 수정"));
		my_info.setBounds(290, 0, 85, 20);
		my_info.addActionListener(this);
		my_info.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// TODO Auto-generated method stub
				friendsPanel.setVisible(false);
				mainPen.setVisible(true);
				subPen.setVisible(false);
				// visible1 = !visible1;
			}
		});

		add(btn2 = new JButton()); // 채팅
		btn2.setIcon(new ImageIcon("chat.png"));
		btn2.setBorderPainted(false); // 버튼의 외곽선을 지워준다.
		btn2.setContentAreaFilled(false); // 버튼의 내용영역 채우기 하지 않음.
		btn2.setFocusPainted(false); // 버튼이 선택되었을 때 생기는 테두리 사용안함 .
		btn2.setBounds(100, 550, 100, 100);
		btn2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PetChatClient mcc = new PetChatClient("127.0.0.1");

			}
		});

		add(btn3 = new JButton("")); // 동물병원
		btn3.setIcon(new ImageIcon("hospital.png"));
		btn3.setBorderPainted(false); // 버튼의 외곽선을 지워준다.
		btn3.setContentAreaFilled(false); // 버튼의 내용영역 채우기 하지 않음.
		btn3.setFocusPainted(false); // 버튼이 선택되었을 때 생기는 테두리 사용안함 .
		btn3.setBounds(200, 550, 100, 100);
		btn3.addActionListener(this);

		add(btn4 = new JButton("")); // 강아지 그리기
		btn4.setIcon(new ImageIcon("draw.png"));
		btn4.setBorderPainted(false); // 버튼의 외곽선을 지워준다.
		btn4.setContentAreaFilled(false); // 버튼의 내용영역 채우기 하지 않음.
		btn4.setFocusPainted(false); // 버튼이 선택되었을 때 생기는 테두리 사용안함 .
		btn4.setBounds(300, 550, 100, 100);
		btn4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				new PaintlGame().main(null);

			}
		});

		// 채팅
		/*
		 * add(jtChatt = new JTextArea(50, 100)); jtChatt.setBounds(400 / 2 - 200, 480,
		 * 400, 50);
		 */

		// 내 정보 수정
		// db 에서 유저정보 불러오기
		// 타이틀
		mainPen.add(mTitle = new JLabel("나의 정보", JLabel.CENTER));
		mTitle.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		mTitle.setBounds(135, 10, 120, 25);

		// 이름
		mainPen.add(mName = new JLabel("이름", JLabel.CENTER));
		mName.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 18));
		mName.setBounds((350 / 3) - 30, 50, 60, 20);

		mainPen.add(mtName = new RoundJTextField());
		mtName.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		mtName.setBounds((350 / 3) + 60, 50, 120, 20);

		// 나이
		mainPen.add(mAge = new JLabel("나이", JLabel.CENTER));
		mAge.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 18));
		mAge.setBounds((350 / 3) - 30, 50 + (30 * 1), 60, 20);

		mainPen.add(mtAge = new RoundJTextField());
		mtAge.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		mtAge.setBounds((350 / 3) + 60, 50 + (30 * 1), 120, 20);

		// 연락처
		mainPen.add(mNum = new JLabel("연락처", JLabel.CENTER));
		mNum.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 18));
		mNum.setBounds((350 / 3) - 30, 50 + (30 * 2), 60, 20);

		mainPen.add(mtNum = new RoundJTextField());
		mtNum.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		mtNum.setBounds((350 / 3) + 60, 50 + (30 * 2), 120, 20);

		// 주소
		mainPen.add(mAddress = new JLabel("주소", JLabel.CENTER));
		mAddress.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 18));
		mAddress.setBounds((350 / 3) - 30, 50 + (30 * 3), 60, 20);

		mainPen.add(mtAddress = new RoundJTextField());
		mtAddress.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		mtAddress.setBounds((350 / 3) + 60, 50 + (30 * 3), 120, 20);

		// 버튼
		mainPen.add(jbPet = new JButton()); // 반려동물 정보입력
		jbPet.setIcon(new ImageIcon("petinfo.png")); // JButton에 이미지 달면 뒤에 배경이 투명해지지 않음
		jbPet.setBounds(400 / 2 - 60, 390, 120, 50);
		jbPet.setBorderPainted(false); // 버튼의 외곽선을 지워준다.
		jbPet.setContentAreaFilled(false); // 버튼의 내용영역 채우기 하지 않음.
		jbPet.setFocusPainted(false); // 버튼이 선택되었을 때 생기는 테두리 사용안함 .
		jbPet.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				visible1 = !visible1;
				visible2 = !visible2;
				mainPen.setVisible(visible1);
				subPen.setVisible(visible2);
			}
		});

		// db 반려동물 정보 불러오기
		// 타이틀
		mainPen.add(jlTitle = new JLabel("반려동물 정보", JLabel.CENTER));
		jlTitle.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		jlTitle.setBounds(95, 200, 200, 25);

		// 이름
		mainPen.add(dbpetName = new JLabel("이름", JLabel.CENTER));
		dbpetName.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 18));
		dbpetName.setBounds((350 / 3) - 30, 240, 60, 20);

		mainPen.add(dpetName = new RoundJTextField());
		dpetName.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		dpetName.setBounds((350 / 3) + 60, 240, 120, 20);

		// 나이
		mainPen.add(dbpetAge = new JLabel("나이", JLabel.CENTER));
		dbpetAge.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 18));
		dbpetAge.setBounds((350 / 3) - 30, 240 + (30 * 1), 60, 20);

		mainPen.add(dpetAge = new RoundJTextField());
		dpetAge.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		dpetAge.setBounds((350 / 3) + 60, 240 + (30 * 1), 120, 20);

		// 성별
		mainPen.add(dbpetSex = new JLabel("성별", JLabel.CENTER));
		dbpetSex.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 18));
		dbpetSex.setBounds((350 / 3) - 30, 240 + (30 * 2), 60, 20);

		mainPen.add(dpetSex = new RoundJTextField());
		dpetSex.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		dpetSex.setBounds((350 / 3) + 60, 240 + (30 * 2), 120, 20);

		// 종
		mainPen.add(dbpetBreed = new JLabel("종", JLabel.CENTER));
		dbpetBreed.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 18));
		dbpetBreed.setBounds((350 / 3) - 30, 240 + (30 * 3), 60, 20);

		mainPen.add(dpetBreed = new RoundJTextField());
		dpetBreed.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		dpetBreed.setBounds((350 / 3) + 60, 240 + (30 * 3), 120, 20);

		// 버튼클릭시 바뀌는 메인창
		firstPanel.add(mainPen);
		mainPen.setBounds(0, 0, 400, 600);
		mainPen.setBackground(new Color(185, 218, 255)); // 배경색 설정
		mainPen.setLayout(null);
		mainPen.setVisible(visible1);

		// 반려동물 정보 입력
		// 타이틀
		subPen.add(jlTitle = new JLabel("반려동물", JLabel.CENTER));
		jlTitle.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		jlTitle.setBounds((400 / 2) - 60, 100, 120, 25);

		// 사용자 아이디
		subPen.add(pluserID = new JLabel("사용자ID", JLabel.CENTER));
		pluserID.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 18));
		pluserID.setBounds((350 / 3) - 30, 140, 100, 20);

		subPen.add(ptuserID = new RoundJTextField());
		ptuserID.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		ptuserID.setHorizontalAlignment(JTextField.CENTER);
		ptuserID.setBounds((350 / 3) + 60, 140, 120, 20);

		// 이름
		subPen.add(jlpetName = new JLabel("이름", JLabel.CENTER));
		jlpetName.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 18));
		jlpetName.setBounds((350 / 3) - 30, 170, 60, 20);

		subPen.add(jtpetName = new RoundJTextField());
		jtpetName.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		jtpetName.setHorizontalAlignment(JTextField.CENTER);
		jtpetName.setBounds((350 / 3) + 60, 170, 120, 20);

		// 나이
		subPen.add(jlpetAge = new JLabel("나이", JLabel.CENTER));
		jlpetAge.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 18));
		jlpetAge.setBounds((350 / 3) - 30, 200, 60, 20);

		subPen.add(jtpetAge = new RoundJTextField());
		jtpetAge.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		jtpetAge.setHorizontalAlignment(JTextField.CENTER);
		jtpetAge.setBounds((350 / 3) + 60, 200, 120, 20);

		// 성별
		subPen.add(jlpetSex = new JLabel("성별", JLabel.CENTER));
		jlpetSex.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 18));
		jlpetSex.setBounds((350 / 3) - 30, 230, 60, 20);

		subPen.add(jtpetSex = new RoundJTextField()); // 라디오버튼으로 바꾸기
		jtpetSex.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		jtpetSex.setHorizontalAlignment(JTextField.CENTER);
		jtpetSex.setBounds((350 / 3) + 60, 230, 120, 20);

		// 종
		subPen.add(jlpetBreed = new JLabel("종", JLabel.CENTER));
		jlpetBreed.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 18));
		jlpetBreed.setBounds((350 / 3) - 30, 260, 60, 20);

		subPen.add(jtpetBreed = new RoundJTextField());
		jtpetBreed.setFont(new Font("맑은 고딕", Font.CENTER_BASELINE, 15));
		jtpetBreed.setHorizontalAlignment(JTextField.CENTER);
		jtpetBreed.setBounds((350 / 3) + 60, 260, 120, 20);

		// 버튼
		subPen.add(jbSubmit = new JButton()); // 저장
		jbSubmit.setIcon(new ImageIcon("submit.png")); // JButton에 이미지 달면 뒤에 배경이 투명해지지 않음
		jbSubmit.setBounds((400 / 3) - 60, 360, 120, 50);
		jbSubmit.setBorderPainted(false); // 버튼의 외곽선을 지워준다.
		jbSubmit.setContentAreaFilled(false); // 버튼의 내용영역 채우기 하지 않음.
		jbSubmit.setFocusPainted(false); // 버튼이 선택되었을 때 생기는 테두리 사용안함 .
		jbSubmit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Object obj = e.getSource();
				if ((JButton) obj == jbSubmit) {
					try {
						contentSet();
						int result = userDAO.insertPet(userDTO);
						if (result == 1) {
							JOptionPane.showMessageDialog(null, "정보가 입력되었습니다.");
							dispose();
							new MainFrame();

						} else {
							JOptionPane.showMessageDialog(null, "실패하였습니다. ");
						}

					} catch (Exception ex) {
						// TODO: handle exception
						System.out.println(ex);
						JOptionPane.showMessageDialog(null, "값을 입력하세요.");
					} finally {

					}
				}
			}
		});

		subPen.add(jbReset = new JButton());
		jbReset.setIcon(new ImageIcon("cancel.png")); // JButton에 이미지 달면 뒤에 배경이 투명해지지 않음
		jbReset.setBounds((400 / 3) + 60, 360, 120, 50);
		jbReset.setBorderPainted(false); // 버튼의 외곽선을 지워준다.
		jbReset.setContentAreaFilled(false); // 버튼의 내용영역 채우기 하지 않음.
		jbReset.setFocusPainted(false); // 버튼이 선택되었을 때 생기는 테두리 사용안함 .
		jbReset.addActionListener(this);

		firstPanel.add(subPen);
		subPen.setBounds(0, 0, 400, 500);
		subPen.setBackground(new Color(185, 218, 255)); // 배경색 설정
		subPen.setLayout(null);
		subPen.setVisible(false);

		// 창설정
		setSize(400, 700);
		setTitle("PET TALK ver.1.0");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
//		getContentPane().setBackground(new Color(185, 218, 255)); // 배경색 설정
		setVisible(true);
		setLocationRelativeTo(null);

	}

	public static void main(String[] args) {
		MainFrame mf = new MainFrame();
		mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mf.setSize(400, 700);
		mf.setResizable(false);
		mf.setVisible(true);
		mf.setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String ButtonFlag = e.getActionCommand();
		Object obj = e.getSource();

		if ((JButton) obj == jbReset) {
			contentClear();
			subPen.setVisible(false);
			mainPen.setVisible(true);
		}
		if ((JButton) obj == my_info) {
			System.out.println("현재 로그인된 userId: " + loggedInUserId);

			try {
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Mini", "root", "1234");

				// Breeder와 Pet 테이블에서 데이터 가져오기
				String query = "SELECT b.userName, b.userAge, b.userNum, b.userAddress, p.petName, p.petAge, p.petSex, p.petBreed "
						+ "FROM Breeder b " + "LEFT JOIN Pet p ON b.userId = p.userId "
						+ "WHERE b.userId = ? AND (p.petName IS NOT NULL OR p.petAge IS NOT NULL OR p.petSex IS NOT NULL OR p.petBreed IS NOT NULL)";
				PreparedStatement pstmt = conn.prepareStatement(query);
				pstmt.setString(1, loggedInUserId);
				ResultSet rs = pstmt.executeQuery();

				// JTextArea에 데이터 출력
				if (rs.next()) {
					String userName = rs.getString("userName");
					int userAge = rs.getInt("userAge");
					String userNum = rs.getString("userNum");
					String userAddress = rs.getString("userAddress");
					String petName = rs.getString("petName");
					int petAge = rs.getInt("petAge");
					String petSex = rs.getString("petSex");
					String petBreed = rs.getString("petBreed");

					mtName.setText(userName);
					mtAge.setText(Integer.toString(userAge));
					mtNum.setText(userNum);
					mtAddress.setText(userAddress);
					dpetName.setText(petName);
					dpetAge.setText(Integer.toString(petAge));
					dpetSex.setText(petSex);
					dpetBreed.setText(petBreed);
				} else {
					// Pet 테이블의 값이 없는 경우 Breeder 테이블에서만 데이터 가져오기
					query = "SELECT userName, userAge, userNum, userAddress " + "FROM Breeder " + "WHERE userId = ?";
					pstmt = conn.prepareStatement(query);
					pstmt.setString(1, loggedInUserId);
					rs = pstmt.executeQuery();

					// JTextArea에 데이터 출력
					if (rs.next()) {
						String userName = rs.getString("userName");
						int userAge = rs.getInt("userAge");
						String userNum = rs.getString("userNum");
						String userAddress = rs.getString("userAddress");

						mtName.setText(userName);
						mtAge.setText(Integer.toString(userAge));
						mtNum.setText(userNum);
						mtAddress.setText(userAddress);
						dpetName.setText("");
						dpetAge.setText("");
						dpetSex.setText("");
						dpetBreed.setText("");
					}
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

		Object obj2 = e.getSource();
		if ((JButton) obj2 == btn3) {
			try {
				Desktop.getDesktop()
						.browse(new URI("https://www.animal.go.kr/front/awtis/shop/hospitalList.do?menuNo=6000000002"));
			} catch (IOException | URISyntaxException ex) {
				JOptionPane.showMessageDialog(this, "웹사이트를 열 수 없습니다.", "에러", JOptionPane.ERROR_MESSAGE);
			}
		}

	}
	// else if (ButtonFlag.equals("애견그리기")) {

	// dispose();

	// }

	// }

	public void contentClear() {
		ptuserID.setText("");
		jtpetName.setText("");
		jtpetAge.setText("");
		jtpetSex.setText("");
		jtpetBreed.setText("");
	}

	public void contentSet() {
		userDTO = new UserDTO();
		String petName = jtpetName.getText();
		int petAge = Integer.parseInt(jtpetAge.getText());
		String petSex = jtpetSex.getText();
		String petBreed = jtpetBreed.getText();
		String petUserid = ptuserID.getText();

		userDTO.setPetName(petName);
		userDTO.setPetAge(petAge);
		userDTO.setPetSex(petSex);
		userDTO.setPetBreed(petBreed);
		userDTO.setPetUserid(petUserid);

	}

}