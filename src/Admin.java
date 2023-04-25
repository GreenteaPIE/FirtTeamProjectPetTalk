import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

class AdminFrame extends JFrame implements ActionListener, MouseListener {
	AdminDAO adminDAO;
	UserDTO userDTO;
	JLabel jlName;
	JButton jbDel, jbChange;
	JTable table;

	public AdminFrame() {
		add(jbDel = new JButton("제거"));
		jbDel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		jbDel.setBounds(0, 400, 120, 50);
		jbDel.addActionListener(this);

		table = new JTable();
		table.addMouseListener(this);
		JScrollPane scroll = new JScrollPane(table);
		add(scroll);
		scroll.setBounds(415, 10, 770, 250);

		// AdminDAO 초기화
		adminDAO = new AdminDAO();
		// DB 정보를 JTable에 출력
		Vector data = adminDAO.getScore();
		Vector columns = new Vector();
		columns.add("아이디");
		columns.add("비밀번호");
		columns.add("이름");
		columns.add("나이");
		columns.add("전화번호");
		columns.add("주소");
		DefaultTableModel model = new DefaultTableModel(data, columns);
		table.setModel(model);

		setSize(400, 500);
		setTitle("관리자 메뉴");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
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
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String buttonFlag = e.getActionCommand();
		if (buttonFlag.equals("제거")) {
			try {
				contentSet(); // 테이블에서 선택된 행의 데이터를 userDTO 객체에 저장
				int result = adminDAO.deleteUser(userDTO); // userDTO 객체를 deleteUser 메소드의 매개변수로 넘겨줌
				if (result == 1) {
					JOptionPane.showMessageDialog(this, "삭제되었습니다.");
					// DB 정보를 JTable에 다시 출력
					Vector data = adminDAO.getScore();
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					refreshTable();
				} else {
					JOptionPane.showMessageDialog(this, "실패하였습니다.");
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "값을 입력하세요.");
			}
		}
	}

	public void contentSet() {
		userDTO = new UserDTO();
		int selectedRow = table.getSelectedRow();
		String userId = (String) table.getValueAt(selectedRow, 0);
		String userPass = (String) table.getValueAt(selectedRow, 1);
		String userName = (String) table.getValueAt(selectedRow, 2);
		int userAge = (int) table.getValueAt(selectedRow, 3);
		String userNum = (String) table.getValueAt(selectedRow, 4);
		String userAddress = (String) table.getValueAt(selectedRow, 5);
		userDTO.setUserId(userId);
		userDTO.setUserPass(userPass);
		userDTO.setUserName(userName);
		userDTO.setUserAge(userAge);
		userDTO.setUserNum(userNum);
		userDTO.setUserAddress(userAddress);
	}

	public void refreshTable() {
		// DB 정보를 JTable에 다시 출력
		Vector data = adminDAO.getScore();
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setDataVector(data, new Vector<>(Arrays.asList("아이디", "비밀번호", "이름", "나이", "전화번호", "주소")));
	}

}

public class Admin {

	public static void main(String[] args) {
		new AdminFrame();
	}

}
