/*
 * db에 저장되어 있는 회원들의 정보(user_info 테이블)를 JTable형태로 출력해주는 클래스
 * (관리자모드에서 회원관리 버튼 클릭 시 작동) 
 */

package 채팅서버;

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
    	setTitle("회원관리 ");
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
			
			// user_info에 저장되어있는 아이디, 이름, 나이, 성별, 전화번호, 직업, 우편번호, 이메일 데이터를 ResultSet 변수 rs에 저장
			String sql = "select name,id,age,sex,phoneNum,job,postNum,email,in_time,out_time from user_info;"; 
			rs = stmt.executeQuery(sql);	
			
			// JTable에 컬럼명을 나타내기 위한 String 배열
			String col[] = { "이름","사용자ID", "나이","성별","전화번호","직업","우편번호","이메일","로그인시간","로그아웃시간"};	
			table = new JTable();		// JTable 생성
			
			// JTable에 데이터를 삽입하기 위해 DefaultTableModel 클래스 변수 선언
			DefaultTableModel model = (DefaultTableModel) table.getModel();	
			// 데이터베이스의 컬럼 개수를 반환하기 위해 ResultSetMetaData 변수 선언
			ResultSetMetaData meta = rs.getMetaData();

			model.setColumnIdentifiers(col);	// JTable의 칼럼명을 저장한다
			int colNo = meta.getColumnCount();	// 가져온 데이터베이스의 칼럼 개수를 저장한다
			while(rs.next()) {					// rs의 다음 데이터가 존재하면 true 그렇지 않으면 false
				String [] rowData = new String[colNo];	// 데이터베이스의 칼럼 개수 만큼의 String 객체 배열 저장 
				for(int i=0; i<rowData.length; i++) {	// 데이터베이스의 칼럼 개수 만큼 반복하여
					// rowData[0] = name, rowData[1] = id, rowData[2] = 나이, rowData[3] = 성별
					// rowData[4] = 전화번호, rowData[5] = 직업, rowData[6] = 우편번호, rowData[7]= 이메일 순으로 저장
					rowData[i] = rs.getString(i+1);		  
				}
				model.addRow(rowData);	// 데이터를 DefaultTableModel model 변수에 한 행씩 저장
			}
			contentPane.setLayout(null);
			table.setModel(model);		// 저장된 변수 model을 JTable 객체로 옮긴다. 
			
			JScrollPane scrollPane2 = new JScrollPane();
			scrollPane2.setBounds(2, 4, 1000, 200);
			contentPane.add(scrollPane2);
			scrollPane2.setViewportView(table);
			table.setEnabled(true);
			// 여기까지 회원에 대한 정보를 테이블로 출력한다.
			
			JButton delete_btn = new JButton("회원삭제");
			delete_btn.setFont(new Font("한컴 윤체 M", Font.PLAIN, 18));
			delete_btn.addActionListener(new ActionListener() {	// 삭제버튼을 눌렀을 때
				public void actionPerformed(ActionEvent e) {
					int row = table.getSelectedRow();			// 어떤 테이블을 선택했는 지 가져온다.
					if(row>-1){				// 사용자가 어떠한 행을 선택했을 경우
						try{
							Connection conn = null;
							Statement stmt = null;
							
							Class.forName("com.mysql.jdbc.Driver");
							String url = "jdbc:mysql://localhost:3306/bomi?useSSL=false";
							conn = DriverManager.getConnection(url, "root", "0720");
							
							String id = (String) table.getValueAt(row, 1);	// 사용자가 선택한 행의 id정보를 가져와서
							
							// 사용자가 선택한 id와 일치하는 db 데이터를 삭제한다.
							String sql = "delete from user_info where id = '" + id + "'";	
							stmt=conn.createStatement();
							stmt.executeUpdate(sql);
							
							JOptionPane.showMessageDialog(null, "삭제가 완료 되었습니다.","알림",JOptionPane.INFORMATION_MESSAGE);
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
			
			JButton cancel_btn = new JButton("취소");
			cancel_btn.setFont(new Font("한컴 윤체 M", Font.PLAIN, 18));
			cancel_btn.addActionListener(new ActionListener() {		//취소버튼 클릭 시
				public void actionPerformed(ActionEvent arg0) {
					setVisible(false);
				}
			});
			cancel_btn.setBounds(590, 250, 100, 40);
			contentPane.add(cancel_btn);
			
			JButton btnNewButton = new JButton("정보보기");
			btnNewButton.setFont(new Font("한컴 윤체 M", Font.PLAIN, 18));
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int row = table.getSelectedRow();			// 어떤 테이블을 선택했는 지 가져온다.
					if(row>-1){				// 사용자가 어떠한 행을 선택했을 경우
						try{
							Connection conn = null;
							Statement stmt = null;
							Statement stmt2 = null;
							ResultSet rs = null;
							ResultSet rs2 = null;
							Class.forName("com.mysql.jdbc.Driver");
							String url = "jdbc:mysql://localhost:3306/bomi?useSSL=false";
							conn = DriverManager.getConnection(url, "root", "0720");
							
							String id = (String) table.getValueAt(row, 1);	// 사용자가 선택한 행의 id정보를 가져와서
							
							// 사용자가 선택한 id와 일치하는 db 데이터를 삭제한다.
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
			
			table.getColumnModel().getColumn(0).setPreferredWidth(100);		// 테이블 간 열 간격 조절
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
