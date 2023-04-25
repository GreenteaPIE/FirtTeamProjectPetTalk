import java.lang.reflect.Member;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO { // Data Access Object
	// singleton pattern : 단 한개의 객체만을 가지고 구현(설계) 한다 .

	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/Mini"; // 벤치 데이터베이스 이름!!
	private static final String USER = "root";
	private static final String PASS = "1234";

	UserDTO userDTO;

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

	// 여기서부터 로그인 함수까지 출처 : ttps://chaengstory.tistory.com/48
	private final String LOGIN = "select * from Breeder where userId=? and userPass=?";

	UserDAO() {
	}

	private static UserDAO dao = new UserDAO();

	public static UserDAO getDao() {
		return dao;
	}

	public int loginUser(String userId, String userPass) {
		Connection con = null;
		con = getConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(LOGIN);
			pstmt.setString(1, userId);
			pstmt.setString(2, userPass);

			rs = pstmt.executeQuery();

			if (rs.next())
				return 1;

		} catch (Exception e) {
			System.out.println(e);
		} finally {

		}
		return -1;
	}

	// 아이디 중복검사
	public int idCheck(String userId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int value = 0;
		try {
			con = getConn();
			String sql = "select * from Breeder where userid=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				System.out.println("사용할 수 없는 아이디입니다. ");
			} else {
				System.out.println("사용할 수 있는 아이디 입니다. ");
				value = 1;
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return value;
	}

//	public void loginUser(UserDTO dto) { // 로그인
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		try {
//			con = getConn();
//			String sql = "select * from Breeder where userId=? and userPass=?";
//			pstmt = con.prepareStatement(sql);
//			pstmt.setString(1, dto.getUserId());
//			pstmt.setString(2, dto.getUserPass());
//			rs = pstmt.executeQuery();
//
//			if (rs.next()) {
//				System.out.println("로그인이 되었습니다.");
//			} else
//				System.out.println("아이디 또는 비밀번호를 확인하세요.");
//
//		} catch (Exception e) {
//			System.out.println("[함수명 Exception] " + e.getMessage());
//			System.out.println(e);
//		} finally {
//			try {
//				if (pstmt != null)
//					pstmt.close();
//				if (con != null)
//					con.close();
//				if (rs != null) {
//					rs.close();
//				}
//			} catch (Exception e) {
//			}
//		}
//	}

	public int insertUser(UserDTO dto) { // 가입
		Connection con = null;
		PreparedStatement pstmt = null;
		int result = 0;
		try {
			con = getConn();
			String sql = "insert into Breeder values(?,?,?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getUserPass());
			pstmt.setString(3, dto.getUserName());
			pstmt.setInt(4, dto.getUserAge());
			pstmt.setString(5, dto.getUserNum());
			pstmt.setString(6, dto.getUserAddress());
			result = pstmt.executeUpdate();

			String userId = dto.getUserId();
			String insertSql = "INSERT INTO Friendlist (fId) VALUES ('" + userId + "')";
			pstmt.executeUpdate(insertSql);

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

	public int insertPet(UserDTO dto) { // 반려동물정보입력
		// System.out.println("userdao 저장된 userId: " + logInUserId);
		Connection con = null;
		PreparedStatement pstmt = null;
		int result = 0;
		try {
			// Connection conn =
			// DriverManager.getConnection("jdbc:mysql://localhost:3306/Mini", "root",
			// "1234");
			// String userId = logInUserId;
			// System.out.println("userdao에 저장된 userId: " + userId);
			con = getConn();
			String sql = "insert into Pet values(?,?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, dto.getPetName());
			pstmt.setInt(2, dto.getPetAge());
			pstmt.setString(3, dto.getPetSex());
			pstmt.setString(4, dto.getPetBreed());
			pstmt.setString(5, dto.getPetUserid());
			result = pstmt.executeUpdate();
			System.out.println("반려동물의 정보가 입력됐습니다.");

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

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public Member getMemberById(String friendId1) {
		// TODO Auto-generated method stub
		return null;
	}

}
