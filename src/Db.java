import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DB 접속 정보를 한곳에서 관리한다. 비밀번호를 코드에 넣지 않도록 환경 변수로 받는다.
 *
 * PETTALK_DB_URL      (기본 jdbc:mysql://localhost:3306/Mini)
 * PETTALK_DB_USER     (기본 root)
 * PETTALK_DB_PASSWORD (기본 빈 값)
 */
public class Db {
	private static final String URL = env("PETTALK_DB_URL", "jdbc:mysql://localhost:3306/Mini");
	private static final String USER = env("PETTALK_DB_USER", "root");
	private static final String PASSWORD = env("PETTALK_DB_PASSWORD", "");

	private static String env(String name, String fallback) {
		String value = System.getenv(name);
		return value == null || value.isEmpty() ? fallback : value;
	}

	public static Connection connect() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
}
