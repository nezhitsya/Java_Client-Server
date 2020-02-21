package 채팅서버;

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
 * DB에 저장되어 있는 chat_info테이블의 데이터를 txt파일로 저장해주는 클래스
 * (txt 저장 버튼 클릭시 작동)
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

				writer.printf("아이디 : %s %n이름 : %s %n성별 : %s %n나이 : %d  %n"
						+ "전화번호 : %s%n직업 : %s %n우편번호 : %s %n주소지 : %s %n"
						+ "이메일 : %s %n로그인시간 : %s %n로그아웃시간 : %s %n"
						+ "---------------------------------------------------------------%n%n",
						id,name,sex,age,phoneNum,job,postNum,postAddr,email,in_time,out_time);
			}
			
		}
		catch(ClassNotFoundException cnfe){
			System.out.println("해당클래스를 찾을 수 없습니다.111");
		}
		catch(SQLException se){
			System.out.println(se.getMessage());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(IOException ioe){
			System.out.println("출력 불가");
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
