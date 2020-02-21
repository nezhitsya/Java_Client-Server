package 채팅클라이언트;

/* 
 * 서버를 실행시키면서 클라이언트도 실행시켜서 서버와 클라이언트가 바로 통신할 수 있는 Main클래스
 * 서버가 꺼져있으면 서버를 실행시키고 클라이언트를 실행시키고,
 * 서버가 켜져있으면 IOException이 발생해 catch문으로 들어가 클라이언트를 하나 더 실행시켜준다.
 * 
 * try문 : 서버가 꺼져있을 때 client를 실행시킨다. (서버가 켜져있으면 catch문으로 넘어간다)
 * catch문 : 서버가 켜져있을 때 client를 실행시킨다.
 */
import java.io.IOException;
import java.net.ServerSocket;

import 채팅서버.Server;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try{
			ServerSocket socket = new ServerSocket(1234);			// 1234포트로 소켓을 생성해본다.
																	// 이미 1234포트를 사용중일경우 catch문으로 넘어간다.
			
			socket.close();											// 사용중이 아니면, 소켓을 닫아주고 Server클래스를 생성하여
			Server server=new Server();								// 1234포트로 서버를 실행해준다.
			
			Client.client = new Client();							// 클라이언트를 실행시켜준다.
			
		} catch (IOException e) {
			// 서버가 켜져있으면 그 서버와 연결할 수 있는 클라이언트를 실행시켜준다.
			Client.client = new Client();
		}
	}
}