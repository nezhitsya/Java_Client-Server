/*
 * Main 클래스를 실행시켰을 때
 * 1234포트번호를 갖고 Client와 통신하는 Server 클래스
 */
package 채팅서버;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Server{
	// ctrl + shift + o 자동으로 import됨
	
	private int port=1234;										// 서버 포트번호
	
	// 네트워크를 위한 자원 변수
	private ServerSocket server_socket;						// 서버 소켓
	private Socket socket;									// 소켓
	private Vector user_vc = new Vector();					// 사용자 벡터
	private Vector room_vc= new Vector();					// 채팅방 벡터
	
	private StringTokenizer st;								// 프로토콜 구분을 위한 변수
	
	public Server(){
		Server_start();	// 소켓 생성
	}
	
	private void Server_start(){
		try {
			server_socket = new ServerSocket(port);	// 1234번 포트를 연다.
		} 
		catch (IOException e) {		// 이미 1234번 포트를 열었을 경우	
			JOptionPane.showMessageDialog(null, "이미 사용중인 포트","알림",JOptionPane.ERROR_MESSAGE);
		}
		if(server_socket!=null){	// 정상적으로 포트가 열렸을 경우
			Connection();
		}
	}
	
	private void Connection(){
		
		// 1가지의 스레드에서는 1가지의 일만 처리할 수 있다.
		Thread th = new Thread(new Runnable() {	// 스레드 생성
			
			@Override
				public void run() {	// 스레드에서 처리할 일을 기재한다.
					while(true){
						try {
							socket=server_socket.accept();		// 사용자 접속 무한 대기!!
							// 사용자가 접속하면 사용자 접속!! 출력
							UserInfo user = new UserInfo(socket);	// 새로운 user생성하여
																	// socket을 이용해 Client와 통신
							
							user.start(); // 객체의 스레드 실행
						}
						catch (IOException e) {
							break;
						}
					} // while문 끝
				} // run 메소드 끝
			});
			
		th.start();	// 스레드 동작
	}
	
	public static void main(String[] args) {
		new Server();

	}
	
	class UserInfo extends Thread{		// 네스티드 클래스
		private OutputStream os;		// OutputStream(출력 스트림)
		private InputStream is;			// InputStream(입력 스트림)
		private DataOutputStream dos;	// Data를 보낼 수 있는 Stream
		private DataInputStream dis;	// Data를 받을 수 있는 Stream
		
		private Socket user_socket;		// 사용자 소켓
		private String Nickname = "";	// user명
		
		private boolean RoomCh=true;	// 채팅방 체크 변수
		
		UserInfo(Socket soc){
			this.user_socket=soc;		// 생성자 매개변수로 user_socket에 대한 정보 저장
			
			UserNetwork();
		}
		
		private void UserNetwork(){
			try{
				// Stream 연결 구성
				is = user_socket.getInputStream();
				dis = new DataInputStream(is);
				
				os = user_socket.getOutputStream();
				dos = new DataOutputStream(os);
				
				Nickname=dis.readUTF();	// 사용자의 닉네임을 받는다. 
										// Client와 Stream을 통해 주고받음.
				
				BroadCast("NewUser/" + Nickname);	// 기존사용자에게 자신을 알림
				
				for(int i=0;i<user_vc.size();i++){	// 자신에게 기존 사용자를 알림
					UserInfo u = (UserInfo)user_vc.elementAt(i);
					
					send_Message("OldUser/" + u.Nickname);	// OldUser 프로토콜
				}
				
				// 자신에게 기존의 방 목록을 받아옴
				for(int i=0;i<room_vc.size();i++){
					RoomInfo r = (RoomInfo)room_vc.elementAt(i);
					send_Message("OldRoom/"+r.Room_name);	// OldRoom 프로토콜 
				}
				
				send_Message("room_list_update/ ");	// room_list_update 프로토콜(채팅방 업데이트)
				user_vc.add(this);					// 사용자에게 알린 후 벡터 추가
				
				BroadCast("user_list_update/ ");	// user_list_update 프로토콜(전체사용자 업데이트)
			}
			catch(IOException e){
				JOptionPane.showMessageDialog(null, "Stream 설정 발생","알림",JOptionPane.ERROR_MESSAGE);
			}
		}
		public void insert_DB(String id,String room_name, String chat_txt){
			Connection conn = null;
			Statement stmt = null;
			try{
				Class.forName("com.mysql.jdbc.Driver");	// JDBC 드라이버를 로드
				
				// DB에 연결(localhost/3307포트/java_chatting DataBase/root계정/비밀번호 1234
				conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/bomi?useSSL=false","root", "0720");
				
				// 데이터베이스의 데이터를 읽어온다.
				stmt=conn.createStatement();
				// insert into 쿼리문을 실행하여 java의 데이터를 MySQL에 저장
				stmt.executeUpdate("insert into " + room_name + "(id,chatText,time) values('" + id + "','" + chat_txt + "', now())");
			}
			catch(ClassNotFoundException cnfe) {
				System.out.println("해당클래스를 찾을수 없습니다."+cnfe.getMessage());
	        }
	        catch(SQLException se) {
	            System.out.println(se.getMessage());
	        }
	        finally {
	            try {
	            	stmt.close();
	            }
	            catch(Exception ignored) {
	            }
	            try {
	            	conn.close();
	            }
	            catch (Exception ignored ) {
	            }
	         }
		}
		
		public void run(){	// Thread에서 처리할 내용
			while(true){
				try {
					String msg = dis.readUTF();		// Client로부터 메세지를 받는다.
					
					InMessage(msg);	// 클라이언트로부터 받아온 메세지 처리
					
				} 
				catch (IOException e) {

					try{
						out_time(Nickname);					// DB에 로그아웃 시간 저장

						dos.close();						// 출력 스트림 종료
						dis.close();						// 입력 스트림 종료
						user_socket.close();				// user 소켓 종료
						user_vc.remove(this);				// 해당 사용자 벡터 삭제
						BroadCast("User_out/"+Nickname);	// 사용자가 나갔다고 전체 알림
						BroadCast("user_list_update/ ");	// 사용자 리스트 업데이트
	
						user_vc.removeAllElements();	// 기존의 user벡터들에 대한 정보 삭제
						room_vc.removeAllElements();	// 기존의 room벡터들에 대한 정보 삭제
						
					}
					catch(IOException e2){}
					break;
					
				}	// 메시지 수신
			}
		}// run 메소드 끝
		
		private void InMessage(String str){		// 클라이언트로부터 들어오는 메세지 처리
			st=new StringTokenizer(str, "/");	// StrigTokenizer를 이용해 '/'로 프로토콜, 메세지 구분
			String protocol = st.nextToken();
			String message = st.nextToken();	// 프로토콜 다음에 해당하는 정보(사용자, 방이름)
			
			if(protocol.equals("Note")){	// protocol이 쪽지(Note)일 때
				String note = st.nextToken();	// 쪽지 내용 저장
				
				// 벡터에서 해당 사용자를 찾아서 메세지 전송
				for(int i=0;i<user_vc.size();i++){
					UserInfo u = (UserInfo)user_vc.elementAt(i);
					if(u.Nickname.equals(message)){					// 쪽지를 보내는 해당 Client를 찾으면
						u.send_Message("Note/"+Nickname+"/"+note);	// 해당 Client에게 쪽지를 보낸다.
						// Note/User1/~~~
					}
				}
			}// if문 끝
			else if(protocol.equals("CreateRoom")){	// 프로토콜이 방만들기(CreateRoom) 일 때
				// 현재 같은 방이 존재하는지 확인
				for(int i=0;i<room_vc.size();i++){
					RoomInfo r = (RoomInfo)room_vc.elementAt(i);
					if(r.Room_name.equals(message)){				// 만들고자 하는 방이름이 이미 존재할 때
						send_Message("CreateRoomFail/ok");			// 해당 Client에게 방만들기 실패 알림
						RoomCh=false;								// 방을 못 만들게 하기 위해 false
						break;
					}
				}// for문 끝
				
				if(RoomCh){		// 방을 만들 수 있으면
					RoomInfo new_room= new RoomInfo(message,this);
					room_vc.add(new_room);	//  전체 방 벡터에 방을 추가
					send_Message("CreateRoom/"+message);			// 해당 Client에게 방만들기 성공 알림
					
					BroadCast("New_Room/"+message);					// 모든 사용자에게 새로운 방이 생김을 알림
				}
				RoomCh=true;	// 원래 값으로 돌려줌(방만들기 실패 후 다시 만들기 위해)
			} // elseif 끝
			
			else if(protocol.equals("Chatting")){		// 프로토콜이 채팅전송(Chatting)일 때
				String msg = st.nextToken();			// 이모티콘 이미지 경로
				String image_path = st.nextToken();		// 프로필 이미지 경로
				if(msg.contains("C:\\Users")){			// 이모티콘 이미지 경로일 경우
					String emo = msg.substring(msg.lastIndexOf('.')-2);		// 이모티콘 파일명을 저장한 뒤
					insert_DB(Nickname,message,emo);						// DB에 아이디, 이모티콘파일명을 저장
					
					for(int i=0;i<room_vc.size();i++){
						RoomInfo r = (RoomInfo)room_vc.elementAt(i);		
						if(r.Room_name.equals(message)){					// 방이름이 같은 사용자에게
							r.BroadCast_Room("Chatting/"+Nickname+"/"+msg + "/" + image_path);	// 해당 방의 사용자에게 프로토콜 전송
						}
					}
				}
				else{
					insert_DB(Nickname,message,msg);	// DB에 아이디, 채팅내용을 저장
					for(int i=0;i<room_vc.size();i++){
						RoomInfo r = (RoomInfo)room_vc.elementAt(i);		// 모든 방 정보를 검색해서
						if(r.Room_name.equals(message)){					// 방이름이 같은 사용자에게
							r.BroadCast_Room("Chatting/"+Nickname+"/"+msg + "/" + image_path);	// 해당 방의 사용자에게 프로토콜 전송
						}
					}
				}
			}
			else if(protocol.equals("Chatting_emo")) {
				String msg = st.nextToken();
				for(int i=0; i<room_vc.size();i++)
				{
					RoomInfo r = (RoomInfo)room_vc.elementAt(i);
					if(r.Room_name.equals(message))
					{
						r.BroadCast_Room("Chatting_emo/" +Nickname +"/"+msg);
					}
				}
			}
			else if(protocol.equals("JoinRoom")){	// 프로토콜이 채팅방 참여(JoinRoom)일 때
				String image_path = st.nextToken();
				for(int i=0;i<room_vc.size();i++){
					RoomInfo r = (RoomInfo)room_vc.elementAt(i);
					if(r.Room_name.equals(message)){	// 방 이름이 같으면
						// 사용자추가
						r.Add_User(this);	// 방에 해당 사용자 추가
						send_Message("JoinRoom/"+message);	// 해당 Client에게 채팅방 참여 알림
						r.BroadCast_Room("Chatting/"+Nickname+"/"+ message + "!!! 방에 입장 !!!!" + "/" + image_path);
					}
				}
			}
			else if(protocol.equals("OutRoom")){		// 프로토콜이 채팅방 나가기(OutRoom) 일 때
				String image_path = st.nextToken();
				for(int i=0;i<room_vc.size();i++){		// 모든 채팅방의 수 만큼 반복
					RoomInfo r = (RoomInfo)room_vc.elementAt(i);	// 채팅방 정보 저장
					if(r.Room_name.equals(message)){	// 채팅방 이름이 같은 채팅 방에서
						r.Out_User(this);				// 방에 해당 사용자를 삭제하고
						send_Message("OutRoom/"+message);	// 해당 Client에게 채팅방 out 알림
						r.BroadCast_Room("Chatting/"+Nickname+"/"+"님이 " + message + "!!! 방에서 퇴장 !!!" + "/" + image_path);
						// 채팅방 참가자에게 ~님이 퇴장했다고 알림
					}
				}
			}
		}
		private void BroadCast(String str){	// 전체사용자에게 메세지 보내는 부분
			
			for(int i=0;i<user_vc.size();i++){	// 현재 접속된 사용자에게 새로운 사용자 알림
				UserInfo u = (UserInfo)user_vc.elementAt(i);	// 벡터에 있는 객체 하나를 꺼냄
				u.send_Message(str);			// 모든 Client에게 메세지를 보냄.
			}
		}
		private void send_Message(String str){
			try {
				dos.writeUTF(str);	// Client에게 메세지 전송
			} 
			catch (IOException e) {
				
			}
		}
		public void out_time(String id){
			Connection conn = null;
			Statement stmt = null;
			
			try{
				Class.forName("com.mysql.jdbc.Driver");	// JDBC 드라이버를 로드
				// DB에 연결(localhost/3307포트/java_chatting DataBase/root계정/비밀번호 1234
				conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/bomi?useSSL=false","root", "0720");
				// 데이터베이스의 데이터를 읽어온다.
				stmt=conn.createStatement();
				
				// update문을 써서 현재 로그아웃 한 시간을 저장한다. 그 전에는 null값을 넣어주었음.
				stmt.executeUpdate("update user_info set out_time = now() where id = '" + id + "'");
				
			}
			catch(ClassNotFoundException cnfe) {
				System.out.println("해당클래스를 찾을수 없습니다."+cnfe.getMessage());
	        }
	        catch(SQLException se) {
	            System.out.println(se.getMessage());
	        }
	        finally {
	            try {
	            	stmt.close();
	            }
	            catch(Exception ignored) {
	            }
	            try {
	            	conn.close();
	            }
	            catch (Exception ignored ) {
	            }
	         }
		}
	}	// UserInfo class 끝
	class RoomInfo{										// 네스티드 클래스
		private String Room_name;						// 방이름
		private Vector Room_user_vc = new Vector();		// 방에 사용자 정보
		
		RoomInfo(String str,UserInfo u){				// 생성자
			this.Room_name = str;						// 방이름 저장
			this.Room_user_vc.add(u);					// 사용자 추가
			
		}
		 public void BroadCast_Room(String str){		// 현재 방의 모든 사람에게 알린다
			 for(int i=0;i<Room_user_vc.size();i++){
				 UserInfo u = (UserInfo)Room_user_vc.elementAt(i);
				 u.send_Message(str);					// Client에게 메세지 전송
			 }
		 }
		 private void Add_User(UserInfo u){				// 사용자 추가하는 메소드
			 this.Room_user_vc.add(u);
		 }
		 private void Out_User(UserInfo u){				// 사용자가 제거하는 메소드
			 this.Room_user_vc.remove(u);
		 }
	}
}
