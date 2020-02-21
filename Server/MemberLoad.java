/*
 * db�� ����Ǿ� �ִ� ȸ������ ����(user_info ���̺�)�� JTable���·� ������ִ� Ŭ����
 * (�����ڸ�忡�� ȸ������ ��ư Ŭ�� �� �۵�) 
 */

package ä�ü���;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;



public class MemberLoad extends JFrame{
	private JPanel contentPane;
    
	JScrollPane scrollPane = new JScrollPane();
	JTable table;
	
    public MemberLoad() {
    	setTitle("ȸ������ ");
        setVisible(true); 
       
        setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);		
	
        setBounds(200, 100, 1100, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(Color.DARK_GRAY);
		setContentPane(contentPane);
			
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/bomi?useSSL=false";
			conn = DriverManager.getConnection(url, "root", "0720");
			stmt = conn.createStatement();
			
			// user_info�� ����Ǿ��ִ� ���̵�, �̸�, ����, ����, ��ȭ��ȣ, ����, �����ȣ, �̸��� �����͸� ResultSet ���� rs�� ����
			String sql = "select name,id,age,sex,phoneNum,job,postNum,email,in_time,out_time from user_info;"; 
			rs = stmt.executeQuery(sql);	
			
			// JTable�� �÷����� ��Ÿ���� ���� String �迭
			String col[] = { "�̸�","�����ID", "����","����","��ȭ��ȣ","����","�����ȣ","�̸���","�α��νð�","�α׾ƿ��ð�"};	
			table = new JTable();		// JTable ����
			
			// JTable�� �����͸� �����ϱ� ���� DefaultTableModel Ŭ���� ���� ����
			DefaultTableModel model = (DefaultTableModel) table.getModel();	
			// �����ͺ��̽��� �÷� ������ ��ȯ�ϱ� ���� ResultSetMetaData ���� ����
			ResultSetMetaData meta = rs.getMetaData();

			model.setColumnIdentifiers(col);	// JTable�� Į������ �����Ѵ�
			int colNo = meta.getColumnCount();	// ������ �����ͺ��̽��� Į�� ������ �����Ѵ�
			while(rs.next()) {					// rs�� ���� �����Ͱ� �����ϸ� true �׷��� ������ false
				String [] rowData = new String[colNo];	// �����ͺ��̽��� Į�� ���� ��ŭ�� String ��ü �迭 ���� 
				for(int i=0; i<rowData.length; i++) {	// �����ͺ��̽��� Į�� ���� ��ŭ �ݺ��Ͽ�
					// rowData[0] = name, rowData[1] = id, rowData[2] = ����, rowData[3] = ����
					// rowData[4] = ��ȭ��ȣ, rowData[5] = ����, rowData[6] = �����ȣ, rowData[7]= �̸��� ������ ����
					rowData[i] = rs.getString(i+1);		  
				}
				model.addRow(rowData);	// �����͸� DefaultTableModel model ������ �� �྿ ����
			}
			contentPane.setLayout(null);
			table.setModel(model);		// ����� ���� model�� JTable ��ü�� �ű��. 
			
			JScrollPane scrollPane2 = new JScrollPane();
			scrollPane2.setBounds(2, 4, 1000, 200);
			contentPane.add(scrollPane2);
			scrollPane2.setViewportView(table);
			table.setEnabled(true);
			// ������� ȸ���� ���� ������ ���̺�� ����Ѵ�.
			
			JButton delete_btn = new JButton("ȸ������");
			delete_btn.setFont(new Font("���� ��ü M", Font.PLAIN, 18));
			delete_btn.addActionListener(new ActionListener() {	// ������ư�� ������ ��
				public void actionPerformed(ActionEvent e) {
					int row = table.getSelectedRow();			// � ���̺��� �����ߴ� �� �����´�.
					if(row>-1){				// ����ڰ� ��� ���� �������� ���
						try{
							Connection conn = null;
							Statement stmt = null;
							
							Class.forName("com.mysql.jdbc.Driver");
							String url = "jdbc:mysql://localhost:3306/bomi?useSSL=false";
							conn = DriverManager.getConnection(url, "root", "0720");
							
							String id = (String) table.getValueAt(row, 1);	// ����ڰ� ������ ���� id������ �����ͼ�
							
							// ����ڰ� ������ id�� ��ġ�ϴ� db �����͸� �����Ѵ�.
							String sql = "delete from user_info where id = '" + id + "'";	
							stmt=conn.createStatement();
							stmt.executeUpdate(sql);
							
							JOptionPane.showMessageDialog(null, "������ �Ϸ� �Ǿ����ϴ�.","�˸�",JOptionPane.INFORMATION_MESSAGE);
							setVisible(false);
						}
						catch(ClassNotFoundException e1){
							e1.printStackTrace();
						}
						catch (SQLException se) {
							System.out.println(se.getMessage());
						}
					}
				}
			});
			
			delete_btn.setBounds(470, 250, 100, 40);
			contentPane.add(delete_btn);
			
			JButton cancel_btn = new JButton("���");
			cancel_btn.setFont(new Font("���� ��ü M", Font.PLAIN, 18));
			cancel_btn.addActionListener(new ActionListener() {		//��ҹ�ư Ŭ�� ��
				public void actionPerformed(ActionEvent arg0) {
					setVisible(false);
				}
			});
			cancel_btn.setBounds(590, 250, 100, 40);
			contentPane.add(cancel_btn);
			
			JButton btnNewButton = new JButton("��������");
			btnNewButton.setFont(new Font("���� ��ü M", Font.PLAIN, 18));
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int row = table.getSelectedRow();			// � ���̺��� �����ߴ� �� �����´�.
					if(row>-1){				// ����ڰ� ��� ���� �������� ���
						try{
							Connection conn = null;
							Statement stmt = null;
							Statement stmt2 = null;
							ResultSet rs = null;
							ResultSet rs2 = null;
							Class.forName("com.mysql.jdbc.Driver");
							String url = "jdbc:mysql://localhost:3306/bomi?useSSL=false";
							conn = DriverManager.getConnection(url, "root", "0720");
							
							String id = (String) table.getValueAt(row, 1);	// ����ڰ� ������ ���� id������ �����ͼ�
							
							// ����ڰ� ������ id�� ��ġ�ϴ� db �����͸� �����Ѵ�.
							String sql = "select * from user_info where id = '" + id + "'";	
							String sql2 = "select * from image_info where id = '" + id + "'";
							stmt=conn.createStatement();
							stmt2=conn.createStatement();
							rs = stmt.executeQuery(sql);
							rs2 = stmt2.executeQuery(sql2);
							if(rs.next()){
								String name = rs.getString("name");
								id = rs.getString("id");
								int age = rs.getInt("age");
								String sex = rs.getString("sex");
								String job = rs.getString("job");
								String phone = rs.getString("phoneNum");
								String intime = rs.getString("in_time");
								String outtime = rs.getString("out_time");
								//InputStream is = rs2.getBinaryStream("image_path");
								
								new MemberInfo(id,name,age,sex,job,phone,intime,outtime/*,is*/);
							}
							
							
						}
						catch(ClassNotFoundException e1){
							e1.printStackTrace();
						}
						catch (SQLException se) {
							System.out.println(se.getMessage());
						}
					}
				}
					
				});
			btnNewButton.setBounds(350, 250, 97, 40);
			contentPane.add(btnNewButton);
			
			table.getColumnModel().getColumn(0).setPreferredWidth(100);		// ���̺� �� �� ���� ����
			table.getColumnModel().getColumn(1).setPreferredWidth(75);
			table.getColumnModel().getColumn(2).setPreferredWidth(75);
			table.getColumnModel().getColumn(3).setPreferredWidth(75);
			table.getColumnModel().getColumn(4).setPreferredWidth(100);
			table.getColumnModel().getColumn(5).setPreferredWidth(100);
			table.getColumnModel().getColumn(6).setPreferredWidth(75);
			table.getColumnModel().getColumn(7).setPreferredWidth(150);
			table.getColumnModel().getColumn(8).setPreferredWidth(170);
			table.getColumnModel().getColumn(9).setPreferredWidth(170);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException se) {
			System.out.println(se.getMessage());
		} finally {
			try {
				stmt.close();
			} catch (Exception ignored) {
			}
			try {
				conn.close();
			} catch (Exception ignored) {
			}
		}
    } 
}
