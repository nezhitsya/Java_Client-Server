/*
 * Server�� 1��1�� ����ϸ� �α���, ȸ������, ä�� ����� �����ϴ� ClientŬ����
 */

package ä��Ŭ���̾�Ʈ;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import ä�ü���.DBSave;

public class Client extends JFrame implements ActionListener,KeyListener{

	// Login Client GUI ����(�α��� �ϱ� �� �α����ϱ� ���� â)
	private JFrame Login_GUI = new JFrame();
	private JPanel Login_Pane;				
	private JTextField id_tf;					// id �޴� �ؽ�Ʈ �ʵ�
	private JTextField passwd_tf;				// passwd �޴� �ؽ�Ʈ �ʵ�
	private JButton login_btn = new JButton("�α���");	// �α��� ��ư
	private JButton join_btn = new JButton("ȸ�� ����");	// ȸ������ ��ư
	private JButton Find_idpw = new JButton("ID/PW ã��"); //���̵� ���ã�� ��ư
	
	
	// Main Client GUI ����(�α��� �� �� ä���� �� �ִ� â)
	private JPanel contentPane;
	private JTextField message_tf;								// �޼��� �Է� â
	private final	JTextPane Chat_Pane = new JTextPane();				// ä�� ���� â
	private JButton notesend_btn = new JButton("����������");	// ���������� ��ư
	private JButton joinroom_btn = new JButton("ä�ù�����");	// ä�ù����� ��ư
	private JButton createroom_btn = new JButton("�游���");	// �游��� ��ư
	private JButton send_btn = new JButton("�� ��");			// ä�� �޼��� ���� ��ư
	private JList User_list = new JList();						// ��ü ������ ����Ʈ
	private JList Room_list = new JList();						// ��ü ���� ����Ʈ
	private InputStream image_is=null;								// ����� �̹��� stream ����
	private JLabel userimage_lbl;								// ����� ���� ��(db�����ҷ���)
	private JLabel lbl_image;									// ����� ���� ��(÷��)
	private final JButton outroom_btn = new JButton("ä�ù泪����");	// ä�ù� ������ ��ư
	
	// ȸ�������� ���� GUI ����(ȸ������ â)
	public JFrame rgtn_GUI = new JFrame();
	private JPanel rgtn_Pane;
	private JTextField tf_id;			// ���̵� �ʵ�
	private JTextField tf_password;		// ��й�ȣ �ʵ�
	private JTextField tf_age;			// ���� �ʵ�
	private JTextField tf_name;			// �̸� �ʵ� 
	private JTextField tf_phone;		// �ڵ��� �ʵ�
	public static JTextField tf_postcode;		// �����ȣ �ʵ�
	public JTextField tf_addr;			// �ּ� �ʵ�
	        //���ּ� �ʵ�
	
	private JTextField tf_email;		// �̸��� �ʵ�
	private JScrollPane image_pane;		// �̹��� �׵θ�
	private JButton attach_btn = new JButton("÷���ϱ�");	// ÷���ϱ� ��ư
	private JComboBox job_cbx = new JComboBox();			// ���� �޺��ڽ�
	private JButton postchk_btn = new JButton("��ȸ");		// �����ȣ ��ȸ ��ư
	private JButton idchk_btn = new JButton("�ߺ�Ȯ��");	// ���̵� �ߺ�Ȯ�� ��ư
	private JRadioButton man_btn = new JRadioButton("����");	// ���� ���� ��ư
	private JRadioButton woman_btn = new JRadioButton("����");	// ���� ���� ��ư
	private JButton ok_join = new JButton("ȸ�����");		// ȸ������ ��� ��ư
	private JButton cancel_join = new JButton("���"); 		// ȸ������ ��� ��ư
	private String user_id="";				// ����� ���̵�
	private String user_name="";			// ����� �̸�
	private String user_password="";		// ����� ��й�ȣ
	private int user_age=0;					// ����� ����
	private String user_sex="";	            // ����� ����
	private String user_phoneNum="";		// ����� ��ȭ��ȣ
	private String user_postNum="";			// ����� �����ȣ
	private String user_postAddr="";		// ����� �ּ�
	private String user_email="";			// ����� �̸����ּ�
	private String user_job="";				// ����� ����
	private String image_path="";			// �̹��� ���
	private JFrame ZipSearch = new ZipSearch();//�����ȣGUI
	

	
	static Client client;					// client ��ü
	
	// ��Ʈ��ũ�� ���� �ڿ� ����
	private Socket socket=null;				// ���� ����
	private String ip="127.0.0.1";			// ip ����
	private String id;						// id ����
	private String passwd;					// passwd ����
	private int port=1234;					// port��ȣ
	

	private InputStream is;					// OutputStream(��� ��Ʈ��)
	private OutputStream os;				// InputStream(�Է� ��Ʈ��)
	private DataInputStream dis;			// Data�� ���� �� �ִ� Stream
	private DataOutputStream dos;			// Data�� ���� �� �ִ� Stream
	
	// �׿� ������
	Vector user_list = new Vector();	// ����� ����Ʈ 
	Vector room_list = new Vector();	// ä�ù� ����Ʈ
	StringTokenizer st;					// �������� ������ ���� ����
	
	private String My_Room;				// ���� ���� �ִ� �� ����
	
	private final JScrollPane scrollPane = new JScrollPane(Chat_Pane);	// ä�ó��� scrollpane
	private final JScrollPane scrollPane_1 = new JScrollPane();			// ����ڸ���Ʈ scrollpane
	private final JScrollPane scrollPane_2 = new JScrollPane();			// �� ����Ʈ scrollpane
	
	private String roomname;
	private RoomDB roomdb;
	
	
	private String profile_image;
	private JTextField userid_tf = new JTextField();
	private JTextField username_tf = new JTextField();
	private JTextField userage_tf = new JTextField();
	private JTextField usersex_tf = new JTextField();
	//private PostInfor post_infor;
	//private Post_Search pSH;
	
	//////��Ʈ�� �ٲٱ�
	 SimpleAttributeSet styleSet;
	 SimpleAttributeSet styleSet1;


	
	
	
	Client(){
		setTitle("Chatting Program");
		Login_init();	// �α��� â ȭ�� ���� �޼ҵ�
		Main_init();	// ���� â ȭ�� ���� �޼ҵ�
		start();		// ������ ����
	}
	
	private void start(){
		idchk_btn.addActionListener(this);		// �ߺ�Ȯ�� ��ư ������(�߰�����)
		attach_btn.addActionListener(this);		// ����÷�� ��ư ������(�߰�����)
		postchk_btn.addActionListener(this);	// �����ȣ ã�� ��ư ������(�߰�����)
		outroom_btn.addActionListener(this);	// ä�ù� ������ ��ư ������(�߰�����)
		join_btn.addActionListener(this);		// ȸ������ ��ư ������(�߰�����)
		Find_idpw.addActionListener(this);		//���̵�ã��(�߰�����)
		login_btn.addActionListener(this);		// �α��� ��ư ������
		ok_join.addActionListener(this);		// ȸ����� ��ư ������
		cancel_join.addActionListener(this);	// ��� ��ư ������
		notesend_btn.addActionListener(this);	// ���������� ������
		joinroom_btn.addActionListener(this);	// ä�ù� ���� ������
		createroom_btn.addActionListener(this);	// �游��� ������
		send_btn.addActionListener(this);		// ���� ������
		message_tf.addKeyListener(this);		// ä�� �޽��� ������
	}
	
	private void Main_init(){
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(720, 100, 851, 594);
		contentPane = new JPanel();
		contentPane.setForeground(Color.BLACK);
		contentPane.setBackground(Color.PINK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("\uC804\uCCB4\uC811\uC18D\uC790");
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setFont(new Font("���� ����� 250", Font.PLAIN, 16));
		lblNewLabel.setBounds(34, 42, 80, 23);
		contentPane.add(lblNewLabel);
		
		JLabel label = new JLabel("ä�ù���");
		label.setFont(new Font("���� ����� 250", Font.PLAIN, 16));
		label.setForeground(Color.BLACK);
		label.setBounds(34, 265, 74, 15);
		contentPane.add(label);
		scrollPane_1.setBounds(12, 68, 114, 154);
		
		contentPane.add(scrollPane_1);
		scrollPane_1.setViewportView(User_list);
		User_list.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		User_list.setListData(user_list);



		notesend_btn.setBounds(12, 232, 114, 23);
		notesend_btn.setForeground(Color.BLACK);
		notesend_btn.setFont(new Font("���� ����� 250", Font.PLAIN, 12));
		contentPane.add(notesend_btn);
		scrollPane_2.setBounds(12, 285, 114, 138);
		
		contentPane.add(scrollPane_2);
		scrollPane_2.setViewportView(Room_list);
		Room_list.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		Room_list.setListData(room_list);
		
		joinroom_btn.setBounds(12, 465, 114, 23);
		joinroom_btn.setFont(new Font("���� ����� 250", Font.PLAIN, 12));
		contentPane.add(joinroom_btn);
		
		createroom_btn.setBounds(12, 433, 114, 23);
		createroom_btn.setFont(new Font("���� ����� 250", Font.PLAIN, 12));

		contentPane.add(createroom_btn);
		scrollPane.setBounds(140, 68, 511, 388);
	
		contentPane.add(scrollPane);
		scrollPane.setViewportView(Chat_Pane);
		Chat_Pane.setFont(new Font("���� ����� 250", Font.PLAIN, 20));
		Chat_Pane.setDisabledTextColor(Color.black);
		Chat_Pane.setEnabled(false);
		
		message_tf = new JTextField();
		message_tf.setFont(new Font("���� ����� 250", Font.PLAIN, 15));
		message_tf.setBounds(138, 467, 398, 30);
		contentPane.add(message_tf);
		message_tf.setColumns(10);
		message_tf.setEnabled(false);
		
		JLabel label_1 = new JLabel("ä�ó���");
		send_btn.setBounds(554, 466, 97, 31);
		send_btn.setFont(new Font("���� ����� 250", Font.PLAIN, 16));
		contentPane.add(send_btn);
		send_btn.setEnabled(false);
		label_1.setFont(new Font("���� ����� 250", Font.PLAIN, 16));
		label_1.setForeground(Color.BLACK);
		label_1.setBounds(363, 42, 57, 22);
		contentPane.add(label_1);
		
		JLabel lblCacaoTalk = new JLabel("TALKING ABOUT");
		lblCacaoTalk.setFont(new Font("HY��B", Font.BOLD, 24));
		lblCacaoTalk.setBounds(27, 10, 200, 30);
		contentPane.add(lblCacaoTalk);
		
		outroom_btn.setFont(new Font("���� ����� 240", Font.PLAIN, 12));
		outroom_btn.setBounds(12, 496, 114, 23);
		
		contentPane.add(outroom_btn);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 235, 205));
		JLabel lblNewLabel_8 = new JLabel("����� ����");
		lblNewLabel_8.setForeground(Color.BLACK);
		lblNewLabel_8.setFont(new Font("���� ����� 250", Font.PLAIN, 16));
		lblNewLabel_8.setBounds(687, 42, 106, 22);
		contentPane.add(lblNewLabel_8);
		panel.setBorder(new TitledBorder(null, "����", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(678, 72, 132, 144);
		
		
		contentPane.add(panel);
		panel.setLayout(null);

		userimage_lbl = new JLabel();
		userimage_lbl.setBounds(6, 17, 120, 120);
		panel.add(userimage_lbl);
		
		JLabel lblNewLabel_10 = new JLabel("ID :");
		lblNewLabel_10.setFont(new Font("���� ����� 250", Font.PLAIN, 18));
		lblNewLabel_10.setBounds(678, 268, 57, 15);
		contentPane.add(lblNewLabel_10);
		
		JLabel lblNewLabel_11 = new JLabel("\uC774\uB984 :");
		lblNewLabel_11.setFont(new Font("���� ����� 250", Font.PLAIN, 18));
		lblNewLabel_11.setBounds(668, 317, 57, 15);
		contentPane.add(lblNewLabel_11);
		
		JLabel lblNewLabel_12 = new JLabel("\uB098\uC774 :");
		lblNewLabel_12.setFont(new Font("���� ����� 250", Font.PLAIN, 18));
		lblNewLabel_12.setBounds(668, 373, 57, 15);
		contentPane.add(lblNewLabel_12);
		
		JLabel lblNewLabel_13 = new JLabel("\uC131\uBCC4 :");
		lblNewLabel_13.setFont(new Font("���� ����� 250", Font.PLAIN, 18));
		lblNewLabel_13.setBounds(668, 421, 57, 15);
		contentPane.add(lblNewLabel_13);
		
		
	
		JButton logout_btn = new JButton("�α׾ƿ�");
		logout_btn.addActionListener(new ActionListener() {	// �α׾ƿ� ��ư�� ������
			public void actionPerformed(ActionEvent arg0) {
				try {
					out_time(id);					// db�� �α׾ƿ� �ð��� ���� �ϰ�
												// ��Ʈ���� socket�� ���� �Ѵ�.
					os.close();					
					is.close();					
					dos.close();				
					dis.close();
					socket.close();
					setVisible(false);			// ���� ä��ȭ�� �� �����ϰ�
					passwd_tf.setText("");
					id_tf.setText("");
					Login_GUI.setVisible(true);	// �α��� ȭ���� ����ش�.
					user_list.remove(id);		// user_list���� �ش� id�� �����Ų��.
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		logout_btn.setFont(new Font("���� ����� 250", Font.PLAIN, 16));
		logout_btn.setBounds(678, 465, 132, 30);
		contentPane.add(logout_btn);
		
		JButton kk_image;
		JButton sad_image;
		JButton angry_image;
		JButton smile_image;		
		
		/*
		kk_image = new JButton("");
		kk_image.setIcon(new ImageIcon("C:\\Users\\image\\����.PNG"));	// ��ư�� �̸�Ƽ���̹����� ���
		kk_image.setBounds(140, 503, 106, 93);
		kk_image.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				send_message("Chatting/"+My_Room+"/"+"C:\\Users\\image\\����.PNG" + "/" + profile_image);
				// ������ �������� ����(Chatting/���̸�/�̸�Ƽ���̹������/�������̹������)
			}
		});
		contentPane.add(kk_image);
		
		
		smile_image = new JButton("");
		smile_image.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send_message("Chatting/"+My_Room+"/"+"C:\\Users\\image\\^^.PNG" + "/" + profile_image);
			}
		});
		smile_image.setIcon(new ImageIcon("C:\\Users\\image\\^^.PNG"));
		smile_image.setBounds(277, 503, 106, 93);
		contentPane.add(smile_image);
		
		
		
		angry_image = new JButton("");
		angry_image.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send_message("Chatting/"+My_Room+"/"+"C:\\Users\\image\\�Ѥ�.PNG" + "/" + profile_image);
			}
		});
		angry_image.setIcon(new ImageIcon("C:\\Users\\image\\�Ѥ�.PNG"));
		angry_image.setBounds(410, 503, 106, 93);
		contentPane.add(angry_image);
		
		
		sad_image = new JButton("");
		sad_image.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send_message("Chatting/"+My_Room+"/"+"C:\\Users\\image\\�Ф�.PNG" + "/" + profile_image);
			}
		});
		sad_image.setIcon(new ImageIcon("C:\\Users\\image\\�Ф�.PNG"));
		sad_image.setBounds(545, 503, 106, 93);
		contentPane.add(sad_image);
		*/
	
		
		JButton imageChange_btn = new JButton("�����ܺ���");
		imageChange_btn.setFont(new Font("���� ����� 250", Font.PLAIN, 16));
		imageChange_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FileDialog fdlg = new FileDialog(new JFrame(),"file open",FileDialog.LOAD);
				fdlg.setVisible(true);			//���� ���̾�α� ����

				if(fdlg.getFile()==null)		//������ �������� �ʾ��� ��
				{
					return;
				}
				else{		//������ ���õǾ��� ��
					ImageIcon originalIcon;
					originalIcon=new ImageIcon(fdlg.getDirectory()+fdlg.getFile());
					Image originalImage = originalIcon.getImage();
					Image resizeImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
					ImageIcon resizeIcon = new ImageIcon(resizeImage);
					profile_image=fdlg.getDirectory()+fdlg.getFile();
					userimage_lbl.setIcon(resizeIcon);		//������ �̹����� ����� �°� ���
					
					
					Connection conn = null;
					PreparedStatement psmt = null;	
					FileInputStream fis=null;		// �̹��� ���� binary�� �����ϱ� ���� ���� 

					// preparedStatement ������ ?�� ����ؾ� �Ѵ�.
					String sql = "update image_info where id = ? set image_path = ?, fis= ? ";	

					try{
						// db����
						Class.forName("com.mysql.jdbc.Driver");
						conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/bomi?useSSL=false","root", "0720");
						
						fis=new FileInputStream(profile_image);	// �̹��� ��θ� �޾ƿ� 
						
						psmt=conn.prepareStatement(sql);		// setBinaryStream�� ����ϱ� ���� prepareStatement ���
						psmt.setString(1, id);		
						psmt.setString(2, image_path);
						psmt.setBinaryStream(3, fis);// prepareStatement�� setString���� �ϳ��ϳ� �� ��������� �Ѵ�
					
						psmt.executeUpdate();				// �� �������� �� executeUpdate�Ѵ�(createStatement�� �Ű������� ������ �ִ´�)
					}
					catch(ClassNotFoundException cnfe) {
						System.out.println("�ش�Ŭ������ ã���� �����ϴ�."+cnfe.getMessage());
			        }
			        catch(SQLException se) {
			            System.out.println(se.getMessage());
			        } catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        finally {
			            try {
			            	psmt.close();
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
		});
		imageChange_btn.setBounds(678, 225, 134, 30);
		
		usersex_tf.setEditable(false);
		
		usersex_tf.setBounds(725, 428, 80, 21);
		usersex_tf.setColumns(10);
		userage_tf.setEditable(false);
		userage_tf.setBounds(725, 370, 80, 21);
		userage_tf.setColumns(10);
		username_tf.setEditable(false);
		username_tf.setBounds(725, 311, 80, 21);
		username_tf.setColumns(10);
		userid_tf.setEditable(false);
		userid_tf.setBounds(725, 265, 80, 21);
		userid_tf.setColumns(10);
		
		//contentPane.add(sad_image);
		
		contentPane.add(userid_tf);
		
		contentPane.add(username_tf);
		
		contentPane.add(userage_tf);
		
		contentPane.add(usersex_tf);
		
		
		contentPane.add(imageChange_btn);
		
		this.setVisible(false);
		
	}
	
	private void Login_init(){
		Login_GUI.setTitle("chatting program");

		Login_GUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Login_GUI.setBounds(720, 100, 418, 524);
		Login_Pane = new JPanel();
		Login_Pane.setBorder(new EmptyBorder(5, 5, 5, 5));
		Login_GUI.setContentPane(Login_Pane);
		Login_Pane.setBackground(Color.PINK);

		Login_Pane.setLayout(null);
		
		JLabel lblId = new JLabel("ID : ");
		lblId.setFont(new Font("���� ����� 250", Font.PLAIN, 24));
		lblId.setBounds(62, 211, 70, 28);
		Login_Pane.add(lblId);
		
		JLabel lblPasswd = new JLabel("PW : ");
		lblPasswd.setFont(new Font("���� ����� 250", Font.PLAIN, 24));
		lblPasswd.setBounds(62, 264, 129, 28);
		Login_Pane.add(lblPasswd);
	
		
		id_tf = new JTextField();
		id_tf.setFont(new Font("����", Font.PLAIN, 24));
		id_tf.setBounds(181, 209, 181, 31);
		Login_Pane.add(id_tf);
		id_tf.setColumns(10);
		
		
		passwd_tf = new JPasswordField(10);
		passwd_tf.setFont(new Font("����", Font.PLAIN, 24));
		passwd_tf.setBounds(181, 262, 181, 31);
		
		Login_Pane.add(passwd_tf);
		passwd_tf.setColumns(10);
		login_btn.setForeground(Color.BLACK);
		login_btn.setBackground(Color.WHITE);
		
		
		login_btn.setBounds(62, 332, 294, 44);
		login_btn.setFont(new Font("���� ����� 250", Font.PLAIN, 24));
		Login_Pane.add(login_btn);
		
		JLabel lblChattingProgram = new JLabel("Chatting Program");
		lblChattingProgram.setFont(new Font("Segoe UI Black", Font.BOLD, 30));
		lblChattingProgram.setBounds(48, 90, 314, 47);
		Login_Pane.add(lblChattingProgram);
		join_btn.setBackground(Color.WHITE);
		
		join_btn.setBounds(62, 391, 294, 44);
	    join_btn.setFont(new Font("���� ����� 250", Font.PLAIN, 24));
	    Login_Pane.add(join_btn);
	      
		Login_GUI.setVisible(true); // true�� ��� ȭ�鿡 ���δ�.
	}
	
	
	public void Rgtn_init(){
		rgtn_GUI.setTitle("ȸ������");
		rgtn_GUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		rgtn_GUI.setBounds(100, 100, 560, 618);
		rgtn_GUI.setBackground(Color.WHITE);
		rgtn_Pane = new JPanel();
		rgtn_Pane.setBackground(Color.PINK);
		rgtn_Pane.setBorder(new EmptyBorder(5, 5, 5, 5));
		rgtn_GUI.setContentPane(rgtn_Pane);
		rgtn_Pane.setLayout(null);
		
		try {
			lbl_image = new JLabel(new ImageIcon(ImageIO.read(new File("C:/Users/image/bm.PNG"))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lbl_image.setBounds(215, 84, 120, 120);
		rgtn_Pane.add(lbl_image);//ȸ������ ����̹���
		
		JLabel lblNewLabel = new JLabel("ȸ �� �� ��");
		lblNewLabel.setBounds(219, 0, 164, 69);
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setFont(new Font("���� ����� 250", Font.BOLD, 22));
		rgtn_Pane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("ID : ");
		lblNewLabel_1.setBounds(237, 269, 57, 15);
		rgtn_Pane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Password :");
		lblNewLabel_2.setBounds(237, 312, 86, 15);
		rgtn_Pane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("���� : ");
		lblNewLabel_3.setBounds(237, 390, 57, 15);
		rgtn_Pane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("���� :");
		lblNewLabel_4.setBounds(12, 315, 57, 15);
		rgtn_Pane.add(lblNewLabel_4);
		
		tf_id = new JTextField();
		tf_id.setBounds(316, 266, 103, 21);
		rgtn_Pane.add(tf_id);
		tf_id.setColumns(10);
		
		tf_password = new JPasswordField();
		tf_password.setBounds(316, 309, 103, 21);
		rgtn_Pane.add(tf_password);
		tf_password.setColumns(10);
		
		tf_age = new JTextField();
		tf_age.setBounds(77, 313, 110, 21);
		rgtn_Pane.add(tf_age);
		tf_age.setColumns(10);
		
		
		man_btn.setBounds(307, 386, 58, 23);
		man_btn.setBackground(Color.PINK);
		rgtn_Pane.add(man_btn);
		
		woman_btn.setBounds(406, 386, 71, 23);
		woman_btn.setBackground(Color.PINK);
		rgtn_Pane.add(woman_btn);
		
		ButtonGroup g = new ButtonGroup();
		g.add(man_btn);
		g.add(woman_btn);
		
		ok_join.setBounds(145, 507, 97, 40);
		rgtn_Pane.add(ok_join);
		
		cancel_join.setBounds(301, 507, 97, 40);
		rgtn_Pane.add(cancel_join);
		
		JLabel label = new JLabel("�̸� : ");
		label.setBounds(12, 269, 57, 15);
		rgtn_Pane.add(label);
		
		tf_name = new JTextField();
		tf_name.setBounds(77, 269, 110, 21);
		rgtn_Pane.add(tf_name);
		tf_name.setColumns(10);
		
		JLabel label_1 = new JLabel("�����߰�");
		label_1.setBounds(75, 135, 125, 15);   //�����߰� ��
		rgtn_Pane.add(label_1);
		
		image_pane = new JScrollPane();
		image_pane.setBounds(215, 84, 120, 120);    //�̹���
		rgtn_Pane.add(image_pane);
		
		attach_btn.setBounds(380, 131, 97, 23); 	// �����߰� ��ư
		rgtn_Pane.add(attach_btn);
		
		JLabel lblNewLabel_5 = new JLabel("���� :");
		lblNewLabel_5.setBounds(13, 351, 57, 15);
		rgtn_Pane.add(lblNewLabel_5);
		
		job_cbx.setBounds(77, 348, 82, 21);
		job_cbx.setModel(new DefaultComboBoxModel(new String[] {"�ʵ��л�", "���л�", "����л�", "���л�", "ȸ���", "������", "�����", "����"}));
		rgtn_Pane.add(job_cbx);
		
		JLabel lblNewLabel_6 = new JLabel("e-mail :");
		lblNewLabel_6.setBounds(12, 390, 57, 15);
		rgtn_Pane.add(lblNewLabel_6);
		
		JLabel label_2 = new JLabel("��ȭ��ȣ :");
		label_2.setBounds(237, 354, 67, 15);
		rgtn_Pane.add(label_2);
		
		tf_phone = new JTextField();
		tf_phone.setBounds(316, 348, 103, 21);
		rgtn_Pane.add(tf_phone);
		tf_phone.setColumns(10);
		JLabel lblNewLabel_7 = new JLabel("�����ȣ :");
		lblNewLabel_7.setBounds(12, 443, 67, 15);
		rgtn_Pane.add(lblNewLabel_7);
		
		tf_postcode = new JTextField();
		tf_postcode.setBounds(96, 440, 258, 21);
		rgtn_Pane.add(tf_postcode);
		//tf_postcode.setColumns(10);
		
		postchk_btn.setBounds(385, 439, 71, 23);
		rgtn_Pane.add(postchk_btn);
		
		tf_addr = new JTextField();
		tf_addr.setBounds(96, 471, 258, 21);
		rgtn_Pane.add(tf_addr);
		tf_addr.setColumns(10);
		
		idchk_btn.setBounds(429, 265, 90, 23);
		rgtn_Pane.add(idchk_btn);
		
		tf_email = new JTextField();
		tf_email.setBounds(77, 387, 110, 21);
		rgtn_Pane.add(tf_email);
		tf_email.setColumns(10);
		
		JLabel lblNewLabel_9 = new JLabel("���ּ�");
		lblNewLabel_9.setBounds(12, 473, 78, 21);
		rgtn_Pane.add(lblNewLabel_9);
		
		
		
		rgtn_GUI.setVisible(true);
	}
	private void Network(){
		try {
			socket = new Socket(ip,port);	// 127.0.0.1 ip�� 1234 port��ȣ�� ������ ���� ����

			if(socket!=null){	// ���������� ������ ���� �Ǿ��� ���
				Connection();	// �����Ѵ�.
			}
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "���� ����","�˸�",JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "���� ����","�˸�",JOptionPane.ERROR_MESSAGE);
		}
	}
	private void send_message(String str){	
		try {
			dos.writeUTF(str);			// �������� �������� ����
		} 
		catch (IOException e) {
		}
	}
	
	private void Connection(){	// server-client ����
		try{
			// Stream ���� ����
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			
			os= socket.getOutputStream();
			dos = new DataOutputStream(os);
		}
		catch(IOException e){
			JOptionPane.showMessageDialog(null, "���� ����","�˸�",JOptionPane.ERROR_MESSAGE);
		}
		// ���� �� Main GUI�� Ű�� Login GUI�� ����.
		this.setVisible(true);
		this.Login_GUI.setVisible(false);
		
		// ó�� ���ӽÿ� user ID�� ������ ����
		send_message(id);
		
		// User_list�� user ID �߰�
		user_list.add(id);		
		
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try {
						String msg = dis.readUTF();		// Server�κ��� �޼����� �޴´�.
						System.out.println("�����κ��� ���ŵ� �޽��� : " + msg);
						
						inmessage(msg);					// Server�κ��� �޾ƿ� �޼��� ó��
					} 
					catch (IOException e) {
						try{
							// ����� ��Ʈ�� ����
							out_time(id);			// �α׾ƿ� �ð��� DB�� ����
							os.close();				// ��Ʈ�� ����
							is.close();					
							dos.close();				
							dis.close();
							socket.close();			// Ŭ���̾�Ʈ ���� ����
							JOptionPane.showMessageDialog(null, "����Ǿ����ϴ�.","�˸�",JOptionPane.INFORMATION_MESSAGE);
						}
						catch(IOException e2){
						}
						break;
					}
				}
			}
		});
		th.start();			// ������ ����
	}
	
	private void inmessage(String str){			// �����κ��� ������ ��� �޼���
		
		
		st = new StringTokenizer(str,"/");		// StrigTokenizer�� �̿��� '/'�� ��������, �޼��� ����
		
		String protocol = st.nextToken();	// � ������������ ����
		String Message = st.nextToken();	// �������� ������ �ش��ϴ� ����(�����, ���̸�)
		
		if(protocol.equals("NewUser")){			// ���ο� ����� �������� �� ��
			user_list.add(Message);				// �ش� ����ڸ� user_list�� ���Ѵ�.
		}
		else if(protocol.equals("OldUser")){	// ������ ����� ���������� ��
			user_list.add(Message);				// �ش� ����ڸ� user_list�� ���Ѵ�. (�ʰ� ���� user�� ����Ʈ â�� ǥ��)
		}
		else if(protocol.equals("Note")){		// ���������� ����������(Note) �� ��
			String note = st.nextToken();		// ���� �޼��� ����
			
			JOptionPane.showMessageDialog(null, note,Message+"������ ���� �� ����",JOptionPane.CLOSED_OPTION);	// ���� �˸�
		}
		else if(protocol.equals("CreateRoom")){	// ���� ������� ��
			My_Room=Message;					// �� �̸� ����
			message_tf.setEnabled(true);		// ä��â ��� ����
			send_btn.setEnabled(true);			// ���� ��ư ��� ����
			joinroom_btn.setEnabled(false);		// ä�ù� ���� ���
			createroom_btn.setEnabled(false);	// �游��� ���
		}
		else if(protocol.equals("CreateRoomFail")){	// �游��� �������� �� ���� �˸�
			JOptionPane.showMessageDialog(null, "�� ����� ����","�˸�",JOptionPane.ERROR_MESSAGE);
		}
		else if(protocol.equals("New_Room")){	// ���������� New_room�� ��(����ڵ鿡�� ���ο� ���� ����� �� �˸�)
			room_list.add(Message);				// ä�ù� ����Ʈ�� ���� ����
			Room_list.setListData(room_list);	// ä�ù� ����Ʈ GUI�� ������Ʈ
			
		}
		else if(protocol.equals("Chatting")){	// ���������� ä�� �޼���(Chatting)������ ��
			
			// �̸�Ƽ���� �����ϱ����� Textarea�� �ƴ� TextPane�� ����ߴ�.
			// TextPane�� Textarea�ʹ� �޸� append��� �޼ҵ尡 ��� ����ؼ� TextPane â��
			// ä��â�� �Է��� ���� ����. �̸� ���� StyleDocument�� ����Ѵ�.
			// �̹��� ��� �޾ƿ���
			StyledDocument doc = Chat_Pane.getStyledDocument();
			String msg = st.nextToken();		// ä�� �޼��� ����
			String image = st.nextToken();	
			//----------------------------------------------
						//�����ʻ��� ������ �����κ�
						ImageIcon ori=new ImageIcon(image_path);
						Image oriimage = ori.getImage();
						Image reimage = oriimage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
						ImageIcon resizeIcon = new ImageIcon(reimage);								
						
						//�̸�Ƽ�� ������ �����κ�.
						ImageIcon ori_emoti=new ImageIcon(msg);
						Image ori_emo_image = ori_emoti.getImage();
						Image re_emo_image = ori_emo_image.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
						ImageIcon re_emoti = new ImageIcon(re_emo_image);	
						
						//StyledDocument doc = Chat_Pane.getStyledDocument();		
						SimpleAttributeSet smi=new SimpleAttributeSet();			// �̹����� ����ϱ� ���� ����
						StyleConstants.setIcon(smi,resizeIcon);						// smi������ ������ �̹����� ����
			//-----------------------------------------------------------
			
			
				/*if(Message.equals(id)) {
					
					SimpleAttributeSet right = new SimpleAttributeSet();
					StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
					try {
						int length = doc.getLength();
						doc.insertString(doc.getLength(),msg+"\n", doc.getStyle("blue"));
						doc.setParagraphAttributes(length+1, 1, right, false);
					}catch(BadLocationException e){
						e.printStackTrace();
					}
					
				}
				else if(Message.equals("�˸�")) {
					
					try{
					int length = doc.getLength();
					doc.insertString(doc.getLength(), Message+":"+msg+"\n",null);}
					catch(BadLocationException e){
						e.printStackTrace();
					}
				}
				else {
					try {
						doc.insertString(doc.getLength(),"ignored",smi);
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						int length = doc.getLength();
						doc.insertString(doc.getLength(), Message+":"+msg+"\n",null);
					}catch(BadLocationException e){
						e.printStackTrace();
					}
				}*/
				if(Message.equals(id)) {
					
					try {
						int length = doc.getLength();
						SimpleAttributeSet right = new SimpleAttributeSet();
						StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
						doc.insertString(doc.getLength(),msg+"\n", doc.getStyle("blue"));
						doc.setParagraphAttributes(length+1, 1, right, false);
					}catch(BadLocationException e){
						e.printStackTrace();
					}
					
				}
				else if(Message.equals("�˸�")) {
					
					try{
					int length = doc.getLength();
					doc.insertString(doc.getLength(), Message+":"+msg+"\n",null);}
					catch(BadLocationException e){
						e.printStackTrace();
					}
				}
				else {
					
					try {
						SimpleAttributeSet left = new SimpleAttributeSet();
						StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
						int length = doc.getLength();
						doc.insertString(doc.getLength(),"ignored",smi);
						doc.insertString(doc.getLength(), Message+":"+msg+"\n",null);
						doc.setParagraphAttributes(length+1, 1, left, false);
					}catch(BadLocationException e){
						e.printStackTrace();
					}
				}	
			//-------------------------------------------------------------
				/*else{							// �׳� �Ϲ� �޽����϶�
					try {
						document.insertString(document.getLength(),"ignored",smi);
						document.insertString(document.getLength(), Message + " : " + msg + "\n" , null);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
						
					}
				}
				 */
	          
	          
			//---------------------------------------------
			
			
			/*// �̹��� ��� �޾ƿ���
			
			//�����ʻ��� ������ �����κ�
			ImageIcon ori=new ImageIcon(image);
			Image oriimage = ori.getImage();
			Image reimage = oriimage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			ImageIcon resizeIcon = new ImageIcon(reimage);								
			
			//�̸�Ƽ�� ������ �����κ�.
			ImageIcon ori_emoti=new ImageIcon(msg);
			Image ori_emo_image = ori_emoti.getImage();
			Image re_emo_image = ori_emo_image.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
			ImageIcon re_emoti = new ImageIcon(re_emo_image);	
			
			//StyledDocument doc = Chat_Pane.getStyledDocument();		
			SimpleAttributeSet smi=new SimpleAttributeSet();			// �̹����� ����ϱ� ���� ����
			StyleConstants.setIcon(smi,resizeIcon);						// smi������ ������ �̹����� ����*/
			
			
			//-------------------------------------------------------------------�̸�Ƽ��
			if(msg.contains("C:\\Users")){
				
				
                try {
                	
                	doc.insertString(doc.getLength(),"ignored",smi);					// �����ʻ��� �Է�
                	doc.insertString(doc.getLength(), Message + " : " ,null);			// ����� : �Է�
                	StyleConstants.setIcon(smi,re_emoti);										// smi������ �̸�Ƽ�� �̹��� ����
					doc.insertString(doc.getLength(),"ignored",smi);					// �̸�Ƽ�� �Է�
					doc.insertString(doc.getLength(), "\n" ,null);					// ����Ű �Է�
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
                Chat_Pane.setCaretPosition(doc.getLength());				// �Է� �� ��ũ�ѹٸ� �� �Ʒ��� �����ش�.
			}
			else{
				if(msg.equals("����")){				// �����Է½� �̸�Ƽ��
					
					StyleConstants.setIcon(smi,new ImageIcon("C:/Users/image/����.PNG"));		// smi������ �̸�Ƽ���� �����Ѵ�.
					
	                Chat_Pane.setCaretPosition(doc.getLength());
					if(Message.equals(id)) {
						
						try {
							int length = doc.getLength();
							SimpleAttributeSet right = new SimpleAttributeSet();
							StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
							doc.insertString(doc.getLength(),"ignored",smi);					// �̹����� ��µȴ�.
							doc.setParagraphAttributes(length+1, 1, right, false);
							doc.insertString(doc.getLength(), "\n" ,null);					// �� �� ���Ͱ� �Էµȴ�.
							
						}catch(BadLocationException e){
							e.printStackTrace();
						}
						
					}
					
					else {
						try {
							SimpleAttributeSet left = new SimpleAttributeSet();
							StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
							int length = doc.getLength();
							doc.insertString(doc.getLength(),"ignored",smi);
							doc.setParagraphAttributes(length+1, 1, left, false);
							doc.insertString(doc.getLength(), "\n" ,null);		
						}catch(BadLocationException e){
							e.printStackTrace();
						}
					}
				}
				else if(msg.equals("�Ф�")){				// �����Է½� �̸�Ƽ��
					
					StyleConstants.setIcon(smi,new ImageIcon("C:/Users/image/�Ф�.PNG"));		// smi������ �̸�Ƽ���� �����Ѵ�.
					
	                Chat_Pane.setCaretPosition(doc.getLength());
					if(Message.equals(id)) {
						
						try {
							int length = doc.getLength();
							SimpleAttributeSet right = new SimpleAttributeSet();
							StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
							doc.insertString(doc.getLength(),"ignored",smi);					// �̹����� ��µȴ�.
							doc.setParagraphAttributes(length+1, 1, right, false);
							doc.insertString(doc.getLength(), "\n" ,null);					// �� �� ���Ͱ� �Էµȴ�.
							
						}catch(BadLocationException e){
							e.printStackTrace();
						}
						
					}
					
					else {
						try {
							SimpleAttributeSet left = new SimpleAttributeSet();
							StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
							int length = doc.getLength();
							doc.insertString(doc.getLength(),"ignored",smi);
							doc.setParagraphAttributes(length+1, 1, left, false);
							doc.insertString(doc.getLength(), "\n" ,null);		
						}catch(BadLocationException e){
							e.printStackTrace();
						}
					}
				}
				else if(msg.equals("^^")){				// �����Է½� �̸�Ƽ��
					
					StyleConstants.setIcon(smi,new ImageIcon("C:/Users/image/^^.PNG"));		// smi������ �̸�Ƽ���� �����Ѵ�.
					
	                Chat_Pane.setCaretPosition(doc.getLength());
					if(Message.equals(id)) {
						
						try {
							int length = doc.getLength();
							SimpleAttributeSet right = new SimpleAttributeSet();
							StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
							doc.insertString(doc.getLength(),"ignored",smi);					// �̹����� ��µȴ�.
							doc.setParagraphAttributes(length+1, 1, right, false);
							doc.insertString(doc.getLength(), "\n" ,null);					// �� �� ���Ͱ� �Էµȴ�.
							
						}catch(BadLocationException e){
							e.printStackTrace();
						}
						
					}
					
					else {
						try {
							SimpleAttributeSet left = new SimpleAttributeSet();
							StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
							int length = doc.getLength();
							doc.insertString(doc.getLength(),"ignored",smi);
							doc.setParagraphAttributes(length+1, 1, left, false);
							doc.insertString(doc.getLength(), "\n" ,null);		
						}catch(BadLocationException e){
							e.printStackTrace();
						}
					}
				}
				else if(msg.equals("�Ѥ�")){				// �����Է½� �̸�Ƽ��
					
					StyleConstants.setIcon(smi,new ImageIcon("C:/Users/image/�Ѥ�.PNG"));		// smi������ �̸�Ƽ���� �����Ѵ�.
					
	                Chat_Pane.setCaretPosition(doc.getLength());
					if(Message.equals(id)) {
						
						try {
							int length = doc.getLength();
							SimpleAttributeSet right = new SimpleAttributeSet();
							StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
							doc.insertString(doc.getLength(),"ignored",smi);					// �̹����� ��µȴ�.
							doc.setParagraphAttributes(length+1, 1, right, false);
							doc.insertString(doc.getLength(), "\n" ,null);					// �� �� ���Ͱ� �Էµȴ�.
							
						}catch(BadLocationException e){
							e.printStackTrace();
						}
						
					}
					
					else {
						try {
							SimpleAttributeSet left = new SimpleAttributeSet();
							StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
							int length = doc.getLength();
							doc.insertString(doc.getLength(),"ignored",smi);
							doc.setParagraphAttributes(length+1, 1, left, false);
							doc.insertString(doc.getLength(), "\n" ,null);		
						}catch(BadLocationException e){
							e.printStackTrace();
						}
					}
				}
				//-------------------------------------------------------------------�̸�Ƽ�ܳ�

			
				/*else{							// �׳� �Ϲ� �޽����϶�
					try {
						document.insertString(document.getLength(),"ignored",smi);
						document.insertString(document.getLength(), Message + " : " + msg + "\n" , null);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
						
					}
				}
				if (msg.equals(this.client)) {
		               
		               try {
						document.insertString(document.getLength(), Message + " : " + msg + "\n", styleSet);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            } else if (!(msg.equals(this.client))) {
		               
		               try {
						document.insertString(document.getLength(), Message + " : "+ msg +"\n", styleSet1);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            }*/
				
				Chat_Pane.setCaretPosition(doc.getLength());
			}
			
		}
		else if(protocol.equals("OldRoom")){	// ���������� OldRoom�� ��(���� ���� ����ڿ��� ������ �� ����Ʈ �˸�)
			room_list.add(Message);				// ä�ù� ����Ʈ ���� ����
		}
		else if(protocol.equals("user_list_update")){	// ���������� GUI ����� ����Ʈ ������Ʈ �� ��
			User_list.setListData(user_list);			// GUI ����� ����Ʈ ����
		}
		else if(protocol.equals("room_list_update")){	// ���������� GUI ä�ù� ����Ʈ ������Ʈ�� ��
			Room_list.setListData(room_list);			// GUI ä�ù� ����Ʈ  ����
		}
		else if(protocol.equals("JoinRoom")){	// ���������� ä�ù� ������ ��
			message_tf.setEnabled(true);		// ä�� �޼��� â ��� ����
			send_btn.setEnabled(true);			// ���� ��ư ��� ����
			joinroom_btn.setEnabled(false);		// ä�ù� ���� ��ư ���
			createroom_btn.setEnabled(false);	// �游��� ��ư ���
			My_Room=Message;					// ���̸� ���� ����
			
			JOptionPane.showMessageDialog(null,My_Room+"ä�ù濡 �����߽��ϴ�","�˸�",JOptionPane.INFORMATION_MESSAGE);
		}
		else if(protocol.equals("User_out")){	// ����ڰ� �������� ��
			user_list.remove(Message);			// ����� ���� ����
		}
		
		
		else if(protocol.equals("OutRoom")){	// �泪����(�߰�����)
			message_tf.setEnabled(false);		// ä�� �޼��� â ��� ����
			send_btn.setEnabled(false);			// ���� ��ư ��� ����
			joinroom_btn.setEnabled(true);		// ä�ù� ���� ��ư ���
			createroom_btn.setEnabled(true);	// �游��� ��ư ���
			Chat_Pane.setText("");				// ä�� â�� ��������
			JOptionPane.showMessageDialog(null, My_Room + "ä�ù濡�� �����߽��ϴ�","�˸�",JOptionPane.INFORMATION_MESSAGE);
			// ä�ù濡�� �����ߴٰ� �˸�
			My_Room="";							// ���� ä�� �� ������ �ʱ�ȭ
		}
	}
	// ���� �޼ҵ�

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==login_btn){	// �α��� GUI â���� ���� ��ư Ŭ�� ��
			if(id_tf.getText().length()==0){	// ID �ʵ忡 �ƹ��͵� ������ ���� ������
				id_tf.setText("ID�� �Է����ּ���");
				id_tf.requestFocus();
			}
			else if(passwd_tf.getText().length()==0){	// ID �ʵ忡 �ƹ��͵� ������ ���� ������
				passwd_tf.setText("Password�� �Է����ּ���");
				passwd_tf.requestFocus();
			}
			else{									// ��� �� �����ִ�
				id=id_tf.getText().trim();	// ����ڰ� �Է��� id�� �޾ƿ´�.
				passwd=passwd_tf.getText().trim();	// ����ڰ� �Է��� password�� �޾ƿ´�.
				
				if(DB_Check(id,passwd)){	// db�� �����ϴ� id�� password�̸�
					in_time(id);				// db�� �α��� �ð��� ����Ѵ�.
					Network();				// ���󿬰�(������ ����ϱ� ���� Network �޼ҵ� ȣ��)
					DB_Open(id);			// �ش� id ���� db���� ����(ä��â���� ����� �̹��� ��� ��)
				}							
				else{						// db�� �������� �ʴ� id�̸�
					JOptionPane.showMessageDialog(null, "���̵�� ��й�ȣ�� ��ġ���� �ʽ��ϴ�!","�˸�",JOptionPane.ERROR_MESSAGE);
					id_tf.setText("");
					passwd_tf.setText("");
				}
			}
		}
		else if(e.getSource()==notesend_btn){							// ���������� ��ư Ŭ�� ��
			String user = (String)User_list.getSelectedValue();			// ����ڸ���Ʈ���� � ������ ���É���� �� �� �ִ�.
			String note = JOptionPane.showInputDialog("�����޼���");	// ���� �޼����� �Է¹���
			if(note!=null){	
				send_message("Note/"+user+"/"+note);					// ���������� �������ݷ� �������� �޼��� ����
				// ex) Note/User2/���� User1�Դϴ�.
			}
		}
		else if(e.getSource()==joinroom_btn){							// ä�ù� ���� ��ư Ŭ�� ��
			System.out.println("ä�ù� ���� : " + profile_image);
			String JoinRoom=(String)Room_list.getSelectedValue();		// ä�ù渮��Ʈ���� � ä�ù��� ���É���� �� �� �ִ�.
			send_message("JoinRoom/"+JoinRoom + "/" + profile_image);							// ä�ù� ���� �������ݷ� �������� �޼��� ����
			
		}
		else if(e.getSource()==createroom_btn){					// �游��� ��ư Ŭ�� ��
			roomname=JOptionPane.showInputDialog("�� �̸�");	// ���̸��� �Է¹���.
			if(roomname!=null){
				send_message("CreateRoom/"+roomname);			// �游��� �������ݷ� �������� �޼��� ����
				roomdb = new RoomDB(roomname);					// ���̸��� DB ���̺�� �����Ѵ�.
			
			}
		}
		else if(e.getSource()==send_btn){										// ä�� �޼��� ���� ��ư Ŭ�� ��
			send_message("Chatting/"+My_Room+"/"+message_tf.getText().trim()+ "/" + profile_image);	
			message_tf.setText("");												// ä�� �޼��� �ʵ带 ����ó��
			message_tf.requestFocus();											// ä�� �޼��� �ʵ� ��Ŀ�� ����
			// Chatting + ���̸� + ����
		}
		
		
		else if(e.getSource()==join_btn){			// ȸ������ ��ư Ŭ����
			Rgtn_init();							// ȸ������ GUI ȣ��
		}
		else if(e.getSource()==ok_join){			// ȸ������ GUI���� ȸ����� ��ư Ŭ�� ��
			if(tf_age.getText().length()==0){		// ���� �ʵ带 �Է����� �ʾ�����
				JOptionPane.showMessageDialog(null, "���̸� �Է����ּ���.","�˸�",JOptionPane.ERROR_MESSAGE);
				return ;
			}
			if(tf_password.getText().length()>4) {
				JOptionPane.showMessageDialog(null, "����ó���Ǿ����ϴ�.");
			}
			else {
				JOptionPane.showMessageDialog(null, "��й�ȣ�� �ٽ� Ȯ���ϼ���.");
				tf_password.setText("");
				tf_password.requestFocus();
				
				
			}
			// ����ڰ� �Է��� ȸ������ �����͸� ���� ������
			user_id=tf_id.getText().trim();						
			user_password=tf_password.getText().trim();
			user_age=Integer.parseInt(tf_age.getText().trim());
			user_name= tf_name.getText().trim();
			user_job = job_cbx.getSelectedItem().toString();
			user_phoneNum = tf_phone.getText().trim();
			user_email=tf_email.getText().trim();
			user_postNum=tf_postcode.getText().trim();
			user_postAddr=tf_addr.getText().trim();
			
			
			if(man_btn.isSelected()==true)
				user_sex="��";
			if(woman_btn.isSelected()==true)
				user_sex="��";
			
			// ����ϳ��� �Է����� �ʾҴٸ� ���޽��� ���
			if(user_id.length()==0 || user_password.length()==0 || user_name.length()==0 || user_sex.length()==0 || user_job.length()==0 || user_phoneNum.length()==0 ||
					user_email.length()==0 || user_postNum.length()==0 || user_postAddr.length()==0 || image_path.length()==0){
				JOptionPane.showMessageDialog(null, "�Է����� ���� ������ �ֽ��ϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
				return ;
			}
			// ���Է�������
			else{
				if(DB_Check(user_id)){			// �ߺ����̵� üũ �� �ߺ� ������
					// ȸ������ �����͸� db�� ����
					insert_DB(user_id,user_name,user_password,user_age,user_sex, user_phoneNum, user_job, user_postNum,
							user_postAddr, user_email);
					// �Ϸ�޼��� ���
					JOptionPane.showMessageDialog(null, "ȸ�������� �Ϸ�Ǿ����ϴ�.","�˸�",JOptionPane.INFORMATION_MESSAGE);
					// ȸ����� GUI ����
					rgtn_GUI.setVisible(false);
				}
				else{	// �ߺ����̵� ������
						// �ߺ����̵� �ִٰ� �޼��� ���
					JOptionPane.showMessageDialog(null, "���̵� �ߺ��Ǿ����ϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
					tf_id.setText("");
				}
			}
		}
		// ȸ������GUI ���� ��ҹ�ư Ŭ�� �� GUI����
		else if(e.getSource()==cancel_join){
			rgtn_GUI.setVisible(false);
		}
		else if(e.getSource()==outroom_btn){	// ä�ù� ������ ��ư Ŭ�� ��
			System.out.println("���������� " + profile_image);
			send_message("OutRoom/"+My_Room+ "/" + profile_image);	// OutRoom �������ݰ� ���� ���̸��� Server�� ������
		}

		else if(e.getSource()==postchk_btn){	// �����ȣ üũ ��ư Ŭ�� ��
			System.out.println("�����ȣ ��ȸ ��ư Ŭ��");
			//int row = tf_addr.getSelectedText();
			//Object post = tf_postcode.getValueAt(row,1);
			this.ZipSearch.setVisible(true);

			//this.setVisible(false);
			//Rgtn_init().tf_addr.setText((String) post);
			//Mypage.U_PostField.setText((String) post);
			//post_infor = new PostInfor();		// �����ȣ Ŭ������ ���� �����ؼ�
			//pSH= new Post_Search(post_infor,client);	// �����ȣ ��ȸ GUI�� ȣ�� �� ������ ����
		}
		
		
		else if(e.getSource()==attach_btn){		// ÷���ϱ� ��ư Ŭ�� ��
			FileDialog fdlg = new FileDialog(this,"file open",FileDialog.LOAD);	// ���� ���̾�α� ���
	        fdlg.setVisible(true);
	        if(fdlg.getFile()==null)			// ��ҹ�ư�� ������ ��� ����
	        	return;
	        ImageIcon originalIcon = new ImageIcon(fdlg.getDirectory()+fdlg.getFile());	// �ش� �̹��� ����
	        Image originalImage = originalIcon.getImage();								// ����� �����ϱ� ���� Image ����
	        Image resizeImage= originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);	// ������ ������ Image ����
	        ImageIcon resizeIcon = new ImageIcon(resizeImage);		// ������ ������ �ϼ� �� �̹��������� ��ü
	        image_path=fdlg.getDirectory()+fdlg.getFile();			// �̹��� ��� ����
	        lbl_image.setIcon(resizeIcon);							// ���� ������ ������ �ϼ��� �̹����� ���
		}
		else if(e.getSource()==idchk_btn){	// �ߺ�Ȯ�� ��ư Ŭ�� ��
			user_id=tf_id.getText().trim();
			if(DB_Check(user_id)){			// �ߺ����̵� ������
				 if(user_id.length()==0){	// �ƹ��͵� �Է� ��������
						JOptionPane.showMessageDialog(null, "���̵� �Է����ּ���","�˸�",JOptionPane.ERROR_MESSAGE);
				}
				 else{						// ���̵� �Է��ϰ� �ߺ��� �ȉ�����
					 	JOptionPane.showMessageDialog(null, "����� �� �ִ� ���̵��Դϴ�.","�˸�",JOptionPane.INFORMATION_MESSAGE);
				}
			}
			else{	// �ߺ����̵� ������
				JOptionPane.showMessageDialog(null, "���̵� �ߺ��Ǿ����ϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
				tf_id.setText("");
			}
		}
	}
	
	/*public void actionPerformed1(ActionEvent e){
		if(e.getSource() == postchk_btn){
			System.out.println("�����ȣ ��ȸ ��ư Ŭ��");
			int row = table.getSelectedRow();
			Object post = table.getValueAt(row,1);

			this.setVisible(false);
			Rgtn_init().tf_addr.setText((String) post);
			Mypage.U_PostField.setText((String) post);

			}
		}

	}*/ 
	
	// db�� �����ϴ� �޼ҵ�
	public void insert_DB(String id,String name, String password, int age,String sex, String phoneNum, String job,
			String postNum, String postAddr,String email){
		Connection conn = null;
		
		
		// db�� �̹��� ������ �����ϱ� ���ؼ��� db�� blob �ڷ����� �°� binary���� �ڹٿ��� �Ѱ���� �ϴµ�
		// Statement Ŭ�������� setBinaryStream �Լ��� ���� ������ preparedStatement Ŭ������ ����Ѵ�.
		PreparedStatement psmt = null;	
		PreparedStatement psmt2 = null;
		FileInputStream fis= null;		// �̹��� ���� binary�� �����ϱ� ���� ���� 
		//InputStream is = file.getBinaryStream();
       
		//fis = new ByteArrayOutputStream();
		

		String sql = "insert into user_info values(?,?,?,?,?,?,?,?,?,?,?,?);";	// preparedStatement ������ ?�� ����ؾ� �Ѵ�.
		String sql2 = "insert into image_info values(?,?,?)";
		

		try{
			// db����
			Class.forName("com.mysql.jdbc.Driver");
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/bomi?useSSL=false","root", "0720");			
			fis=new FileInputStream(image_path);	// �̹��� ��θ� �޾ƿ� 
			
			
			psmt=conn.prepareStatement(sql);		// setBinaryStream�� ����ϱ� ���� prepareStatement ���
			psmt.setString(1, name);					// prepareStatement�� setString���� �ϳ��ϳ� �� ��������� �Ѵ�
			psmt.setString(2, id);
			psmt.setString(3, password);
			psmt.setInt(4, age);
			psmt.setString(5, sex);
			psmt.setString(6, phoneNum);
			psmt.setString(7, job);
			psmt.setString(8, postNum);
			psmt.setString(9, postAddr);
			psmt.setString(10, email);
			psmt.setTimestamp(11, null);
			psmt.setTimestamp(12, null);
			psmt.executeUpdate();				// �� �������� �� executeUpdate�Ѵ�(createStatement�� �Ű������� ������ �ִ´�)
			
			psmt2=conn.prepareStatement(sql2);
			psmt2.setString(1, id);
			psmt2.setString(2, image_path);
			psmt2.setBinaryStream(3, fis);           // db�� �̹��� ���� binary�� ����
			psmt2.executeUpdate();                           //image_info�� insert
		}
		catch(ClassNotFoundException cnfe) {
			System.out.println("�ش�Ŭ������ ã���� �����ϴ�."+cnfe.getMessage());
        }
        catch(SQLException se) {
            System.out.println(se.getMessage());
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        finally {
            try {
            	psmt.close();
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
	public boolean DB_Check(String id,String password){	// �α����ϱ� ���� ��ġ�ϴ� id,password ã�� �޼ҵ�
		Connection conn = null;
		Statement stmt = null;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");	// JDBC ����̹��� �ε�
			
			// DB�� ����(localhost/3307��Ʈ/java_chatting DataBase/root����/��й�ȣ 1234
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/bomi?useSSL=false","root", "0720");
			
			// �����ͺ��̽��� �����͸� �о�´�.
			stmt=conn.createStatement();
			
			// id�� password�� ��Ȯ�� ��ġ�ϴ� �����͸� �޾ƿ´�.
			ResultSet rs=stmt.executeQuery("select * from user_info where id = '" + id + "' and password = '" + password + "'");
			
			if(rs.next()){	// rs�� �����Ͱ� �����ϸ� true �׷��� ������ false
				// �Ű������� �Է¹��� id, password ���� DB�� ����Ǿ��ִ� ID,Password �ʵ� ���� �����ϸ� true ��ȯ   
				if(id.equals(rs.getString("ID")) && password.equals(rs.getString("Password"))){	
					return true;
				}
			}
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
		return false;	//rs.next���� �����Ͱ� �������� �ʴٸ� false��ȯ
	}
	
	public boolean DB_Check(String id){	// ȸ�����Խ� id�ߺ�Ȯ�� �ϴ� �޼ҵ�
		Connection conn = null;
		Statement stmt = null;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/bomi?useSSL=false","root", "0720");
			stmt=conn.createStatement();
			// �Է¹��� id�� ��ġ�ϴ� �����͸� �޾ƿ´�.
			ResultSet rs=stmt.executeQuery("select * from user_info where id = '" + id + "'");
			
			if(rs.next()){	// �����Ͱ� �����ϰ�
				if(id.equals(rs.getString("ID"))){	// �Է¹��� id�� db�� �����Ͱ� ���ٸ�
					return false;				//false ��ȯ
				}
			}
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
		return true;	// ���̵� �ߺ����� ������ true ��ȯ
	}
	
	// �Ű������� �Է¹��� id�� db�� �ִ� ������ ���� �����ִ� �޼ҵ�(�α��� �ÿ� ȣ��)
	public void DB_Open(String id){		 
		Connection conn =null;
		Statement stmt = null;
		Statement stmt2 = null;
		try{
			// preparedStatement �����ٰ� �ڲ� �������� �ȸԾ
			// createStatmenet������ ����. ������ �̹��� binary�� ������ �� �ִ� Ŭ������ ResultSet�̱� ������
			// preparedStatement �� �ʿ䰡 ����. preparedStatement�� setBinary �޼ҵ带 ���� �����
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bomi?useSSL=false","root", "0720");
			stmt=conn.createStatement();
			stmt2=conn.createStatement();
			ResultSet rs =stmt.executeQuery("select * from user_info where id = '" + id + "'");
			ResultSet rs2 = stmt2.executeQuery("select * from image_info where id = '"+ id+"'");
			
			if(rs2.next()){ // rs�� ���� ������ �������� ���ؼ��� �� if���̳� while���� rs.next�� ����� ��
				image_is = rs2.getBinaryStream("fis");			// �̹��� binaryStream�� InputStream ������ ����
				String image_path = rs2.getString("image_path");
				try {
					System.out.println("�̹��� ��� : " + image_path);
					ImageIcon originalIcon=new ImageIcon(ImageIO.read(image_is));	// db�� ����Ǿ��ִ� �̹��� ������ �о�´�.
			        Image originalImage = originalIcon.getImage();					
			        Image resizeImage= originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
			        ImageIcon resizeIcon = new ImageIcon(resizeImage);
					userimage_lbl.setIcon(resizeIcon);		// resize�� �̹����� ä��â ����� ������ ����Ѵ�.
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// �� db�� ����� ������ �����Ѵ�
				if(rs.next()) {
				user_name=rs.getString(1);
				user_id=rs.getString(2);
				user_password = rs.getString(3);
				user_age=rs.getInt(4);
				user_sex=rs.getString(5);
				user_phoneNum=rs.getString(6);
				user_job=rs.getString(7);
				user_postNum = rs.getString(8);
				user_postAddr = rs.getString(9);
				user_email=rs.getString(10);
				
				System.out.println("�α��� �� : " + profile_image);
				// ���� �� main_gui ä��â ����� ������ ����� �����͸� ����Ѵ�
				
				usersex_tf.setText(user_sex);
				userage_tf.setText(Integer.toString(user_age));
				username_tf.setText(user_name);
				userid_tf.setText(user_id);
				}
			}
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
	// ȸ���� �α��� �ð��� db�� �������ִ� �޼ҵ�
	public void in_time(String id){
		Connection conn = null;
		Statement stmt = null;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");	// JDBC ����̹��� �ε�
			// DB�� ����(localhost/3307��Ʈ/java_chatting DataBase/root����/��й�ȣ 1234
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/bomi?useSSL=false","root", "0720");
			// �����ͺ��̽��� �����͸� �о�´�.
			stmt=conn.createStatement();
			
			// update���� �Ἥ ���� �α��� �� �ð��� �����Ѵ�. �� ������ null���� �־��־���.
			stmt.executeUpdate("update user_info set in_time = now() where id = '" + id + "'");
			
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
	// ȸ���� �α׾ƿ� �ð��� db�� �������ִ� �޼ҵ�
	public void out_time(String id){
		Connection conn = null;
		Statement stmt = null;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");	// JDBC ����̹��� �ε�
			// DB�� ����(localhost/3307��Ʈ/java_chatting DataBase/root����/��й�ȣ 1234
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/bomi?useSSL=false","root", "0720");
			// �����ͺ��̽��� �����͸� �о�´�.
			stmt=conn.createStatement();
			
			// update���� �Ἥ ���� �α׾ƿ� �� �ð��� �����Ѵ�. �� ������ null���� �־��־���.
			stmt.executeUpdate("update user_info set out_time = now() where id = '" + id + "'");
			
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
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub	
	}
	@Override
	// ����Ű�� ������ ä�� �޼����� ���۵ǰ�, ä�� �޼��� �ʵ带 ���� �� ��Ŀ�� ���߱�
	public void keyPressed(KeyEvent e) {	
		if(e.getKeyCode()==10){				// ����Ű�� ��������
			// ������ �������� ����(Chatting/���̸�/ä�ó���/�������̹������)
			send_message("Chatting/"+My_Room+"/"+message_tf.getText().trim()+ "/" + profile_image);	
			message_tf.setText("");			// ä�� �޼��� �ʵ带 ����ó��
			message_tf.requestFocus();		// ä�� �޼��� �ʵ� ��Ŀ�� ����
		}
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
