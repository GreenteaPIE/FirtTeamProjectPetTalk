import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import com.mysql.cj.xdevapi.Statement;

public class AdminDAO { // Data Access Object
	// singleton pattern : 단 한개의 객체만을 가지고 구현(설계) 한다 .

	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/Mini"; // 벤치 데이터베이스 이름!!
	private static final String USER = "root";
	private static final String PASS = "1234";

	public Connection getConn() {
		Connection con = null;
		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASS);
			System.out.println("접속 성공");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			System.out.println("접속 실패");
		} finally {

		}
		return con;
	}
	public Vector getScore() {
		Vector data = new Vector();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConn();
			String sql = "select * from Breeder order by UserId asc";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String userId = rs.getString("userId");
				String userPass = rs.getString("userPass");
				String userName = rs.getString("userName");
				int userAge = rs.getInt("userAge");
				String userNum = rs.getString("userNum");
				String userAddress = rs.getString("userAddress");
				Vector row = new Vector();
				row.add(userId);
				row.add(userPass);
				row.add(userName);
				row.add(userAge);
				row.add(userNum);
				row.add(userAddress);
				data.add(row);
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {

			}
		}
		return data;
	}

	// 여기서부터 로그인 함수까지 출처 : https://chaengstory.tistory.com/48
	private final String LOGIN = "select * from adminn where adminId=? and adminPw=?";

	AdminDAO() {
	}

	private static AdminDAO dao = new AdminDAO();

	public static AdminDAO getDao() {
		return dao;
	}

	public int loginAdmin(String adminId, String adminPw) {
		Connection con = null;
		con = getConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(LOGIN);
			pstmt.setString(1, adminId);
			pstmt.setString(2, adminPw);
			rs = pstmt.executeQuery();

			if (rs.next())
				return 1;

		} catch (Exception e) {
			System.out.println(e);
		}
		return -1;
	}
	
	public int insertAdmin(UserDTO dto) { // 가입
		Connection con = null;
		PreparedStatement pstmt = null;
		int result = 0;
		try {
			con = getConn();
			String sql = "insert into admin values(?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, dto.getAdminId());
			pstmt.setString(2, dto.getAdminPw());
			result = pstmt.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			System.out.println("삽입오류");
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (Exception e2) {
				// TODO: handle exception
			} finally {

			}

		}
		return result;

	}
	
	public int deleteUser(UserDTO dto) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "delete from Breeder where userId=?";
		try {
			con = getConn();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, dto.getUserId());
			result = pstmt.executeUpdate();

		} catch (Exception e) {
			System.out.println("삭제 실패");
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (Exception e) {

			}
		}
		return result;
	}
}


	

