/*
 * 관리자 모드의 회원관리 창에서 관리자가 원하는 사용자를 클릭한 후에
 * 정보보기 버튼을 눌렀을 때 사용자 정보를 출력해주는 클래스
 */

package 채팅서버;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;

public class MemberInfo extends JFrame {

	private JPanel contentPane;
	private JTextField tf_id;
	private JTextField tf_name;
	private JTextField tf_age;
	private JTextField tf_sex;
	private JTextField tf_job;
	private JTextField tf_phone;
	private JTextField tf_intime;
	private JTextField tf_outtime;
	private JLabel  image_lbl;	


	public MemberInfo( String name,String id, int age, String sex, String job, String phone,
						String intime, String outtime /*,InputStream is*/) {
		
		setTitle(name + "님의 회원정보");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 423, 318);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "사진", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(16, 10, 112, 124);
		contentPane.add(panel);
		panel.setLayout(null);
		/*
		ImageIcon resizeIcon=null;
		try {
			ImageIcon originalIcon = new ImageIcon(ImageIO.read(is));
			Image originalImage = originalIcon.getImage();					
	        Image resizeImage= originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
	        resizeIcon = new ImageIcon(resizeImage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	// db에 저장되어있던 이미지 아이콘 읽어온다.
        //-----------------------------------------------
         * */
         try {
			 image_lbl = new JLabel(new ImageIcon(ImageIO.read(new File("C:/Users/image/bomi.jpg"))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		image_lbl.setBounds(6, 17, 100, 100);
		panel.add(image_lbl);
		
		
		
		JLabel lblNewLabel = new JLabel("아이디 : ");
		lblNewLabel.setBounds(16, 161, 57, 15);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("이름 :");
		lblNewLabel_1.setBounds(16, 195, 57, 15);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("나이 : ");
		lblNewLabel_2.setBounds(16, 231, 57, 15);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("전화번호 : ");
		lblNewLabel_3.setBounds(171, 231, 72, 15);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("성별 : ");
		lblNewLabel_4.setBounds(171, 161, 57, 15);
		contentPane.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("직업 : ");
		lblNewLabel_5.setBounds(171, 195, 57, 15);
		contentPane.add(lblNewLabel_5);
		
		tf_id = new JTextField();
		tf_id.setEditable(false);
		tf_id.setBounds(72, 158, 77, 21);
		tf_id.setText(id);
		contentPane.add(tf_id);
		tf_id.setColumns(10);
		
		tf_name = new JTextField();
		tf_name.setEditable(false);
		tf_name.setBounds(72, 192, 77, 21);
		tf_name.setText(name);
		contentPane.add(tf_name);
		tf_name.setColumns(10);
		
		tf_age = new JTextField();
		tf_age.setEditable(false);
		tf_age.setBounds(72, 228, 77, 21);
		tf_age.setText(Integer.toString(age));
		contentPane.add(tf_age);
		tf_age.setColumns(10);
		
		tf_sex = new JTextField();
		tf_sex.setEditable(false);
		tf_sex.setBounds(245, 158, 83, 21);
		tf_sex.setText(sex);
		contentPane.add(tf_sex);
		tf_sex.setColumns(10);
		
		tf_job = new JTextField();
		tf_job.setEditable(false);
		tf_job.setBounds(245, 192, 83, 21);
		tf_job.setText(job);
		contentPane.add(tf_job);
		tf_job.setColumns(10);
		
		tf_phone = new JTextField();
		tf_phone.setEditable(false);
		tf_phone.setBounds(241, 228, 88, 21);
		tf_phone.setText(phone);
		contentPane.add(tf_phone);
		tf_phone.setColumns(10);
		
		JLabel lblNewLabel_6 = new JLabel("로그인 시간 : ");
		lblNewLabel_6.setBounds(140, 32, 87, 15);
		contentPane.add(lblNewLabel_6);
		
		JLabel lblNewLabel_7 = new JLabel("로그아웃 시간 : ");
		lblNewLabel_7.setBounds(140, 77, 100, 15);
		contentPane.add(lblNewLabel_7);
		
		tf_intime = new JTextField();
		tf_intime.setEditable(false);
		tf_intime.setBounds(241, 29, 146, 21);
		tf_intime.setText(intime);
		contentPane.add(tf_intime);
		tf_intime.setColumns(10);
		
		tf_outtime = new JTextField();
		tf_outtime.setEditable(false);
		tf_outtime.setBounds(241, 74, 146, 21);
		tf_outtime.setText(outtime);
		contentPane.add(tf_outtime);
		tf_outtime.setColumns(10);
		
		setVisible(true);
	}
}
