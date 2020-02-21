package 채팅서버;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;
/*
 * db에 저장되어있는 user_info 테이블과 chat_info테이블의 데이터를 모두 삭제 해주는 클래스
 * (관리자모드에서 DB 삭제 버튼 클릭시 작동)
 */
public class DBDelete {
	public DBDelete(){
		Connection conn = null;
		Statement stmt = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/bomi?useSSL=false","root", "0720");
			stmt=conn.createStatement();
			stmt.executeUpdate("delete from room");		// chat_info db 데이터 삭제
			stmt.executeUpdate("delete from user_info");		// user_info db 데이터 삭제
			
			JOptionPane.showMessageDialog(null, "DB가 모두 삭제되었습니다.","알림",JOptionPane.INFORMATION_MESSAGE);
			
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
