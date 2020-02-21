/*
 * Main Ŭ������ ��������� ��
 * 1234��Ʈ��ȣ�� ���� Client�� ����ϴ� Server Ŭ����
 */
package ä�ü���;

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
	// ctrl + shift + o �ڵ����� import��
	
	private int port=1234;										// ���� ��Ʈ��ȣ
	
	// ��Ʈ��ũ�� ���� �ڿ� ����
	private ServerSocket server_socket;						// ���� ����
	private Socket socket;									// ����
	private Vector user_vc = new Vector();					// ����� ����
	private Vector room_vc= new Vector();					// ä�ù� ����
	
	private StringTokenizer st;								// �������� ������ ���� ����
	
	public Server(){
		Server_start();	// ���� ����
	}
	
	private void Server_start(){
		try {
			server_socket = new ServerSocket(port);	// 1234�� ��Ʈ�� ����.
		} 
		catch (IOException e) {		// �̹� 1234�� ��Ʈ�� ������ ���	
			JOptionPane.showMessageDialog(null, "�̹� ������� ��Ʈ","�˸�",JOptionPane.ERROR_MESSAGE);
		}
		if(server_socket!=null){	// ���������� ��Ʈ�� ������ ���
			Connection();
		}
	}
	
	private void Connection(){
		
		// 1������ �����忡���� 1������ �ϸ� ó���� �� �ִ�.
		Thread th = new Thread(new Runnable() {	// ������ ����
			
			@Override
				public void run() {	// �����忡�� ó���� ���� �����Ѵ�.
					while(true){
						try {
							socket=server_socket.accept();		// ����� ���� ���� ���!!
							// ����ڰ� �����ϸ� ����� ����!! ���
							UserInfo user = new UserInfo(socket);	// ���ο� user�����Ͽ�
																	// socket�� �̿��� Client�� ���
							
							user.start(); // ��ü�� ������ ����
						}
						catch (IOException e) {
							break;
						}
					} // while�� ��
				} // run �޼ҵ� ��
			});
			
		th.start();	// ������ ����
	}
	
	public static void main(String[] args) {
		new Server();

	}
	
	class UserInfo extends Thread{		// �׽�Ƽ�� Ŭ����
		private OutputStream os;		// OutputStream(��� ��Ʈ��)
		private InputStream is;			// InputStream(�Է� ��Ʈ��)
		private DataOutputStream dos;	// Data�� ���� �� �ִ� Stream
		private DataInputStream dis;	// Data�� ���� �� �ִ� Stream
		
		private Socket user_socket;		// ����� ����
		private String Nickname = "";	// user��
		
		private boolean RoomCh=true;	// ä�ù� üũ ����
		
		UserInfo(Socket soc){
			this.user_socket=soc;		// ������ �Ű������� user_socket�� ���� ���� ����
			
			UserNetwork();
		}
		
		private void UserNetwork(){
			try{
				// Stream ���� ����
				is = user_socket.getInputStream();
				dis = new DataInputStream(is);
				
				os = user_socket.getOutputStream();
				dos = new DataOutputStream(os);
				
				Nickname=dis.readUTF();	// ������� �г����� �޴´�. 
										// Client�� Stream�� ���� �ְ����.
				
				BroadCast("NewUser/" + Nickname);	// ��������ڿ��� �ڽ��� �˸�
				
				for(int i=0;i<user_vc.size();i++){	// �ڽſ��� ���� ����ڸ� �˸�
					UserInfo u = (UserInfo)user_vc.elementAt(i);
					
					send_Message("OldUser/" + u.Nickname);	// OldUser ��������
				}
				
				// �ڽſ��� ������ �� ����� �޾ƿ�
				for(int i=0;i<room_vc.size();i++){
					RoomInfo r = (RoomInfo)room_vc.elementAt(i);
					send_Message("OldRoom/"+r.Room_name);	// OldRoom �������� 
				}
				
				send_Message("room_list_update/ ");	// room_list_update ��������(ä�ù� ������Ʈ)
				user_vc.add(this);					// ����ڿ��� �˸� �� ���� �߰�
				
				BroadCast("user_list_update/ ");	// user_list_update ��������(��ü����� ������Ʈ)
			}
			catch(IOException e){
				JOptionPane.showMessageDialog(null, "Stream ���� �߻�","�˸�",JOptionPane.ERROR_MESSAGE);
			}
		}
		public void insert_DB(String id,String room_name, String chat_txt){
			Connection conn = null;
			Statement stmt = null;
			try{
				Class.forName("com.mysql.jdbc.Driver");	// JDBC ����̹��� �ε�
				
				// DB�� ����(localhost/3307��Ʈ/java_chatting DataBase/root����/��й�ȣ 1234
				conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/bomi?useSSL=false","root", "0720");
				
				// �����ͺ��̽��� �����͸� �о�´�.
				stmt=conn.createStatement();
				// insert into �������� �����Ͽ� java�� �����͸� MySQL�� ����
				stmt.executeUpdate("insert into " + room_name + "(id,chatText,time) values('" + id + "','" + chat_txt + "', now())");
			}
			catch(ClassNotFoundException cnfe) {
				System.out.println("�ش�Ŭ������ ã���� �����ϴ�."+cnfe.getMessage());
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
		
		public void run(){	// Thread���� ó���� ����
			while(true){
				try {
					String msg = dis.readUTF();		// Client�κ��� �޼����� �޴´�.
					
					InMessage(msg);	// Ŭ���̾�Ʈ�κ��� �޾ƿ� �޼��� ó��
					
				} 
				catch (IOException e) {

					try{
						out_time(Nickname);					// DB�� �α׾ƿ� �ð� ����

						dos.close();						// ��� ��Ʈ�� ����
						dis.close();						// �Է� ��Ʈ�� ����
						user_socket.close();				// user ���� ����
						user_vc.remove(this);				// �ش� ����� ���� ����
						BroadCast("User_out/"+Nickname);	// ����ڰ� �����ٰ� ��ü �˸�
						BroadCast("user_list_update/ ");	// ����� ����Ʈ ������Ʈ
	
						user_vc.removeAllElements();	// ������ user���͵鿡 ���� ���� ����
						room_vc.removeAllElements();	// ������ room���͵鿡 ���� ���� ����
						
					}
					catch(IOException e2){}
					break;
					
				}	// �޽��� ����
			}
		}// run �޼ҵ� ��
		
		private void InMessage(String str){		// Ŭ���̾�Ʈ�κ��� ������ �޼��� ó��
			st=new StringTokenizer(str, "/");	// StrigTokenizer�� �̿��� '/'�� ��������, �޼��� ����
			String protocol = st.nextToken();
			String message = st.nextToken();	// �������� ������ �ش��ϴ� ����(�����, ���̸�)
			
			if(protocol.equals("Note")){	// protocol�� ����(Note)�� ��
				String note = st.nextToken();	// ���� ���� ����
				
				// ���Ϳ��� �ش� ����ڸ� ã�Ƽ� �޼��� ����
				for(int i=0;i<user_vc.size();i++){
					UserInfo u = (UserInfo)user_vc.elementAt(i);
					if(u.Nickname.equals(message)){					// ������ ������ �ش� Client�� ã����
						u.send_Message("Note/"+Nickname+"/"+note);	// �ش� Client���� ������ ������.
						// Note/User1/~~~
					}
				}
			}// if�� ��
			else if(protocol.equals("CreateRoom")){	// ���������� �游���(CreateRoom) �� ��
				// ���� ���� ���� �����ϴ��� Ȯ��
				for(int i=0;i<room_vc.size();i++){
					RoomInfo r = (RoomInfo)room_vc.elementAt(i);
					if(r.Room_name.equals(message)){				// ������� �ϴ� ���̸��� �̹� ������ ��
						send_Message("CreateRoomFail/ok");			// �ش� Client���� �游��� ���� �˸�
						RoomCh=false;								// ���� �� ����� �ϱ� ���� false
						break;
					}
				}// for�� ��
				
				if(RoomCh){		// ���� ���� �� ������
					RoomInfo new_room= new RoomInfo(message,this);
					room_vc.add(new_room);	//  ��ü �� ���Ϳ� ���� �߰�
					send_Message("CreateRoom/"+message);			// �ش� Client���� �游��� ���� �˸�
					
					BroadCast("New_Room/"+message);					// ��� ����ڿ��� ���ο� ���� ������ �˸�
				}
				RoomCh=true;	// ���� ������ ������(�游��� ���� �� �ٽ� ����� ����)
			} // elseif ��
			
			else if(protocol.equals("Chatting")){		// ���������� ä������(Chatting)�� ��
				String msg = st.nextToken();			// �̸�Ƽ�� �̹��� ���
				String image_path = st.nextToken();		// ������ �̹��� ���
				if(msg.contains("C:\\Users")){			// �̸�Ƽ�� �̹��� ����� ���
					String emo = msg.substring(msg.lastIndexOf('.')-2);		// �̸�Ƽ�� ���ϸ��� ������ ��
					insert_DB(Nickname,message,emo);						// DB�� ���̵�, �̸�Ƽ�����ϸ��� ����
					
					for(int i=0;i<room_vc.size();i++){
						RoomInfo r = (RoomInfo)room_vc.elementAt(i);		
						if(r.Room_name.equals(message)){					// ���̸��� ���� ����ڿ���
							r.BroadCast_Room("Chatting/"+Nickname+"/"+msg + "/" + image_path);	// �ش� ���� ����ڿ��� �������� ����
						}
					}
				}
				else{
					insert_DB(Nickname,message,msg);	// DB�� ���̵�, ä�ó����� ����
					for(int i=0;i<room_vc.size();i++){
						RoomInfo r = (RoomInfo)room_vc.elementAt(i);		// ��� �� ������ �˻��ؼ�
						if(r.Room_name.equals(message)){					// ���̸��� ���� ����ڿ���
							r.BroadCast_Room("Chatting/"+Nickname+"/"+msg + "/" + image_path);	// �ش� ���� ����ڿ��� �������� ����
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
			else if(protocol.equals("JoinRoom")){	// ���������� ä�ù� ����(JoinRoom)�� ��
				String image_path = st.nextToken();
				for(int i=0;i<room_vc.size();i++){
					RoomInfo r = (RoomInfo)room_vc.elementAt(i);
					if(r.Room_name.equals(message)){	// �� �̸��� ������
						// ������߰�
						r.Add_User(this);	// �濡 �ش� ����� �߰�
						send_Message("JoinRoom/"+message);	// �ش� Client���� ä�ù� ���� �˸�
						r.BroadCast_Room("Chatting/"+Nickname+"/"+ message + "!!! �濡 ���� !!!!" + "/" + image_path);
					}
				}
			}
			else if(protocol.equals("OutRoom")){		// ���������� ä�ù� ������(OutRoom) �� ��
				String image_path = st.nextToken();
				for(int i=0;i<room_vc.size();i++){		// ��� ä�ù��� �� ��ŭ �ݺ�
					RoomInfo r = (RoomInfo)room_vc.elementAt(i);	// ä�ù� ���� ����
					if(r.Room_name.equals(message)){	// ä�ù� �̸��� ���� ä�� �濡��
						r.Out_User(this);				// �濡 �ش� ����ڸ� �����ϰ�
						send_Message("OutRoom/"+message);	// �ش� Client���� ä�ù� out �˸�
						r.BroadCast_Room("Chatting/"+Nickname+"/"+"���� " + message + "!!! �濡�� ���� !!!" + "/" + image_path);
						// ä�ù� �����ڿ��� ~���� �����ߴٰ� �˸�
					}
				}
			}
		}
		private void BroadCast(String str){	// ��ü����ڿ��� �޼��� ������ �κ�
			
			for(int i=0;i<user_vc.size();i++){	// ���� ���ӵ� ����ڿ��� ���ο� ����� �˸�
				UserInfo u = (UserInfo)user_vc.elementAt(i);	// ���Ϳ� �ִ� ��ü �ϳ��� ����
				u.send_Message(str);			// ��� Client���� �޼����� ����.
			}
		}
		private void send_Message(String str){
			try {
				dos.writeUTF(str);	// Client���� �޼��� ����
			} 
			catch (IOException e) {
				
			}
		}
		public void out_time(String id){
			Connection conn = null;
			Statement stmt = null;
			
			try{
				Class.forName("com.mysql.jdbc.Driver");	// JDBC ����̹��� �ε�
				// DB�� ����(localhost/3307��Ʈ/java_chatting DataBase/root����/��й�ȣ 1234
				conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/bomi?useSSL=false","root", "0720");
				// �����ͺ��̽��� �����͸� �о�´�.
				stmt=conn.createStatement();
				
				// update���� �Ἥ ���� �α׾ƿ� �� �ð��� �����Ѵ�. �� ������ null���� �־��־���.
				stmt.executeUpdate("update user_info set out_time = now() where id = '" + id + "'");
				
			}
			catch(ClassNotFoundException cnfe) {
				System.out.println("�ش�Ŭ������ ã���� �����ϴ�."+cnfe.getMessage());
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
	}	// UserInfo class ��
	class RoomInfo{										// �׽�Ƽ�� Ŭ����
		private String Room_name;						// ���̸�
		private Vector Room_user_vc = new Vector();		// �濡 ����� ����
		
		RoomInfo(String str,UserInfo u){				// ������
			this.Room_name = str;						// ���̸� ����
			this.Room_user_vc.add(u);					// ����� �߰�
			
		}
		 public void BroadCast_Room(String str){		// ���� ���� ��� ������� �˸���
			 for(int i=0;i<Room_user_vc.size();i++){
				 UserInfo u = (UserInfo)Room_user_vc.elementAt(i);
				 u.send_Message(str);					// Client���� �޼��� ����
			 }
		 }
		 private void Add_User(UserInfo u){				// ����� �߰��ϴ� �޼ҵ�
			 this.Room_user_vc.add(u);
		 }
		 private void Out_User(UserInfo u){				// ����ڰ� �����ϴ� �޼ҵ�
			 this.Room_user_vc.remove(u);
		 }
	}
}
