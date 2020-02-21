/*
 * 채팅방을 매개변수로 받아서
 * 채팅방마다 테이블을 저장하는 클래스
 */
package 채팅클라이언트;

import java.awt.Image;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class RoomDB {
	public RoomDB(String room_name){		// 방 이름을 매개변수로 입력받아
			Connection conn =null;
			Statement stmt = null;
			try{
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bomi?useSSL=false","root", "0720");
				stmt=conn.createStatement();
				// 방 이름으로 테이블을 만들어준다.
				stmt.executeUpdate("create table " + room_name + "(id varchar(20), chatText text, time time)");
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
}
