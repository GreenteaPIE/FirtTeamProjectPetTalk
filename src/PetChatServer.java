import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Iterator;

import java.text.*;

public class PetChatServer {
	private static final int LOGIN = 100;
	private static final int EXIT = 200;
	private static final int NOMAL = 300;
	private static final int USERLIST = 400;
	private static final int WISPER = 500;
	private static final int CHECK = 600;

	private ServerSocket serverSocket = null;
	private Socket socket = null;

	ArrayList<ChatThread> chatlist = new ArrayList<ChatThread>();
	HashMap<String, ChatThread> hash = new HashMap<String, ChatThread>();
	Date now = new Date(System.currentTimeMillis());
	SimpleDateFormat time = new SimpleDateFormat("(a hh:mm)");

	public void start() {
		try {
			serverSocket = new ServerSocket(9888);
			System.out.println("server start");
			while (true) {
				socket = serverSocket.accept();

				ChatThread chat = new ChatThread(socket);

				chatlist.add(chat);
				chat.start();

			}

		} catch (IOException e) {
			System.out.println("통신소켓 생성불가");
			if (!serverSocket.isClosed()) {
				stopServer();
			}
		} catch (Exception e) {
			System.out.println("서버소켓 생성불가");
			if (!serverSocket.isClosed()) {
				stopServer();
			}
		}
	}

	public void stopServer() {
		try {

			Iterator<ChatThread> iterator = chatlist.iterator();
			while (iterator.hasNext()) {
				ChatThread chat = iterator.next();
				chat.soket.close();
				iterator.remove();

			}
			if (serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
			System.out.println("서버종료");

		} catch (Exception e) {
		}
	}

	void broadCast(String msg) {
		for (ChatThread ct : chatlist) {
			ct.outMsg.println(msg + time.format(now));
		}
	}

	void updatinglist() {
		Set<String> list = hash.keySet();
		for (ChatThread ct : chatlist) {
			ct.outMsg.println(USERLIST + "/" + list);
		}
	}

	void disconnect(ChatThread thread, String id) {
		try {
			thread.soket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		hash.remove(id);
		chatlist.remove(thread);

	}

	void wisper(ChatThread from, ChatThread to, String msg) { // 송신그레드,수신스레드,대화내용 매개변수)
		from.outMsg.println(msg + time.format(now)); // 송신스레드 채팅창에 출력
		to.outMsg.println(msg + time.format(now)); // 수신스레드 채팅창에 출력
	}

	class ChatThread extends Thread {

		Socket soket;
		String msg;
		String[] rmsg;

		public ChatThread(Socket socket) {
			this.soket = socket;

		}

		private BufferedReader inMsg = null;
		private PrintWriter outMsg = null;

		public void run() {
			boolean status = true;

			System.out.println("##ChatThread start...");
			try {
				inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				outMsg = new PrintWriter(socket.getOutputStream(), true);

				while (status) {
					msg = inMsg.readLine();
					rmsg = msg.split("/");

					int commend = Integer.parseInt(rmsg[0]);

					switch (commend) {
					case LOGIN: {
						System.out.println(commend);
						if (hash.containsKey(rmsg[1])) {
							this.outMsg.println(CHECK + "/" + "[PetTalk]" + "/" + "로그인불가>ID 중복");
							socket.close();
							chatlist.remove(this);
							status = false;
							break;
						} else {
							hash.put(rmsg[1], this);
							broadCast(NOMAL + "/" + "[PetTalk]" + "/" + rmsg[1] + "님이 로그인했습니다.");
							updatinglist();
							break;
						}

					}
					case EXIT: {
						disconnect(this, rmsg[1]);
						broadCast(NOMAL + "/" + "[PetTalk]" + "/" + rmsg[1] + "님과 연결이 끊어졌습니다.");
						updatinglist();
						status = false;
						break;
					}
					case NOMAL: {
						broadCast(msg);
						break;
					}
					case WISPER: {
						ChatThread from = hash.get(rmsg[1]);
						ChatThread to = hash.get(rmsg[2]);
						wisper(from, to, msg);
						break;
					}

					}
				}

			} catch (IOException e) {
				chatlist.remove(this);
				e.printStackTrace();
				System.out.println("[ChatThread]run() IOException!!");
			}
		}

	}

	public static void main(String[] args) {
		PetChatServer server = new PetChatServer();
		server.start();

	}

}