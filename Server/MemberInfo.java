/*
 * ������ ����� ȸ������ â���� �����ڰ� ���ϴ� ����ڸ� Ŭ���� �Ŀ�
 * �������� ��ư�� ������ �� ����� ������ ������ִ� Ŭ����
 */

package ä�ü���;

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
		
		setTitle(name + "���� ȸ������");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 423, 318);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "����", TitledBorder.LEADING, TitledBorder.TOP, null, null));
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
		}	// db�� ����Ǿ��ִ� �̹��� ������ �о�´�.
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
		
		
		
		JLabel lblNewLabel = new JLabel("���̵� : ");
		lblNewLabel.setBounds(16, 161, 57, 15);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("�̸� :");
		lblNewLabel_1.setBounds(16, 195, 57, 15);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("���� : ");
		lblNewLabel_2.setBounds(16, 231, 57, 15);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("��ȭ��ȣ : ");
		lblNewLabel_3.setBounds(171, 231, 72, 15);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("���� : ");
		lblNewLabel_4.setBounds(171, 161, 57, 15);
		contentPane.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("���� : ");
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
		
		JLabel lblNewLabel_6 = new JLabel("�α��� �ð� : ");
		lblNewLabel_6.setBounds(140, 32, 87, 15);
		contentPane.add(lblNewLabel_6);
		
		JLabel lblNewLabel_7 = new JLabel("�α׾ƿ� �ð� : ");
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
