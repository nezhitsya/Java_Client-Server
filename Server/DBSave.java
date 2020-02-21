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
 * DB�� ����Ǿ� �ִ� chat_info���̺��� �����͸� txt���Ϸ� �������ִ� Ŭ����
 * (txt ���� ��ư Ŭ���� �۵�)
 */
public class DBSave extends JFrame {
	DBSave(){
		Connection conn = null;
		Statement stmt = null;
		PrintWriter writer = null;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bomi?useSSL=false","root", "0720");
			stmt = conn.createStatement(); 
			ResultSet rs = stmt.executeQuery("select * from user_info;");
			writer = new PrintWriter("chatting.txt");

			while(rs.next()){
				String name = rs.getString("name");
				String id = rs.getString("id");
				String sex = rs.getString("sex");
				int age = rs.getInt("age");
				String phoneNum = rs.getString("phoneNum");
				String job = rs.getString("job");
				String postNum = rs.getString("postNum");
				String postAddr = rs.getString("postAddr");
				String email = rs.getString("email");
				String in_time = rs.getString("in_time");
				String out_time = rs.getString("out_time");

				writer.printf("���̵� : %s %n�̸� : %s %n���� : %s %n���� : %d  %n"
						+ "��ȭ��ȣ : %s%n���� : %s %n�����ȣ : %s %n�ּ��� : %s %n"
						+ "�̸��� : %s %n�α��νð� : %s %n�α׾ƿ��ð� : %s %n"
						+ "---------------------------------------------------------------%n%n",
						id,name,sex,age,phoneNum,job,postNum,postAddr,email,in_time,out_time);
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
