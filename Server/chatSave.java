package ä�ü���;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

import javax.swing.JFrame;
/*
 * DB�� ����Ǿ� �ִ� room���̺��� �����͸� txt���Ϸ� �������ִ� Ŭ����
 * (txt ���� ��ư Ŭ���� �۵�)
 */
public class chatSave extends JFrame {
	chatSave(){
		Connection conn = null;
		Statement stmt = null;
		PrintWriter writer = null;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bomi?useSSL=false","root", "0720");
			stmt = conn.createStatement(); 
			ResultSet rs = stmt.executeQuery("select * from room;");
			writer = new PrintWriter("chatroom.txt");

			while(rs.next()){
				String id = rs.getString("id");
				String chatText = rs.getString("chatText");
				String time = rs.getString("time");
				

				writer.printf("< %s >  %s  -  %s  %n%n",
						id, chatText, time);
			}
			
		}
		catch(ClassNotFoundException cnfe){
			System.out.println("�ش�Ŭ������ ã�� �� �����ϴ�.111");
		}
		catch(SQLException se){
			System.out.println(se.getMessage());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(IOException ioe){
			System.out.println("��� �Ұ�");
		}
		finally{
			try{
				writer.close();
			}
			catch(Exception e){	}
			try{
				stmt.close();
			}
			catch(Exception ignored){
			}
			try{
				conn.close();
			}
			catch(Exception ignored){
			}
		}
	}
}
