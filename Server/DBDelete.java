package ä�ü���;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;
/*
 * db�� ����Ǿ��ִ� user_info ���̺�� chat_info���̺��� �����͸� ��� ���� ���ִ� Ŭ����
 * (�����ڸ�忡�� DB ���� ��ư Ŭ���� �۵�)
 */
public class DBDelete {
	public DBDelete(){
		Connection conn = null;
		Statement stmt = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/bomi?useSSL=false","root", "0720");
			stmt=conn.createStatement();
			stmt.executeUpdate("delete from room");		// chat_info db ������ ����
			stmt.executeUpdate("delete from user_info");		// user_info db ������ ����
			
			JOptionPane.showMessageDialog(null, "DB�� ��� �����Ǿ����ϴ�.","�˸�",JOptionPane.INFORMATION_MESSAGE);
			
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
