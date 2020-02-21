/*
 * ä�ù��� �Ű������� �޾Ƽ�
 * ä�ù渶�� ���̺��� �����ϴ� Ŭ����
 */
package ä��Ŭ���̾�Ʈ;

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
	public RoomDB(String room_name){		// �� �̸��� �Ű������� �Է¹޾�
			Connection conn =null;
			Statement stmt = null;
			try{
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bomi?useSSL=false","root", "0720");
				stmt=conn.createStatement();
				// �� �̸����� ���̺��� ������ش�.
				stmt.executeUpdate("create table " + room_name + "(id varchar(20), chatText text, time time)");
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
}
