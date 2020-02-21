/*
 * Server와 1대1로 통신하며 로그인, 회원가입, 채팅 기능을 수행하는 Client클래스
 */

package 채팅클라이언트;

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

import 채팅서버.DBSave;

public class Client extends JFrame implements ActionListener,KeyListener{

	// Login Client GUI 변수(로그인 하기 전 로그인하기 위한 창)
	private JFrame Login_GUI = new JFrame();
	private JPanel Login_Pane;				
	private JTextField id_tf;					// id 받는 텍스트 필드
	private JTextField passwd_tf;				// passwd 받는 텍스트 필드
	private JButton login_btn = new JButton("로그인");	// 로그인 버튼
	private JButton join_btn = new JButton("회원 가입");	// 회원가입 버튼
	private JButton Find_idpw = new JButton("ID/PW 찾기"); //아이디 비번찾기 버튼
	
	
	// Main Client GUI 변수(로그인 한 후 채팅할 수 있는 창)
	private JPanel contentPane;
	private JTextField message_tf;								// 메세지 입력 창
	private final	JTextPane Chat_Pane = new JTextPane();				// 채팅 상태 창
	private JButton notesend_btn = new JButton("쪽지보내기");	// 쪽지보내기 버튼
	private JButton joinroom_btn = new JButton("채팅방참여");	// 채팅방참여 버튼
	private JButton createroom_btn = new JButton("방만들기");	// 방만들기 버튼
	private JButton send_btn = new JButton("전 송");			// 채팅 메세지 전송 버튼
	private JList User_list = new JList();						// 전체 접속자 리스트
	private JList Room_list = new JList();						// 전체 방목록 리스트
	private InputStream image_is=null;								// 사용자 이미지 stream 저장
	private JLabel userimage_lbl;								// 사용자 사진 라벨(db에서불러옴)
	private JLabel lbl_image;									// 사용자 사진 라벨(첨부)
	private final JButton outroom_btn = new JButton("채팅방나가기");	// 채팅방 나가기 버튼
	
	// 회원가입을 위한 GUI 변수(회원가입 창)
	public JFrame rgtn_GUI = new JFrame();
	private JPanel rgtn_Pane;
	private JTextField tf_id;			// 아이디 필드
	private JTextField tf_password;		// 비밀번호 필드
	private JTextField tf_age;			// 나이 필드
	private JTextField tf_name;			// 이름 필드 
	private JTextField tf_phone;		// 핸드폰 필드
	public static JTextField tf_postcode;		// 우편번호 필드
	public JTextField tf_addr;			// 주소 필드
	        //상세주소 필드
	
	private JTextField tf_email;		// 이메일 필드
	private JScrollPane image_pane;		// 이미지 테두리
	private JButton attach_btn = new JButton("첨부하기");	// 첨부하기 버튼
	private JComboBox job_cbx = new JComboBox();			// 직업 콤보박스
	private JButton postchk_btn = new JButton("조회");		// 우편번호 조회 버튼
	private JButton idchk_btn = new JButton("중복확인");	// 아이디 중복확인 버튼
	private JRadioButton man_btn = new JRadioButton("남성");	// 남성 라디오 버튼
	private JRadioButton woman_btn = new JRadioButton("여성");	// 여성 라디오 버튼
	private JButton ok_join = new JButton("회원등록");		// 회원가입 등록 버튼
	private JButton cancel_join = new JButton("취소"); 		// 회원가입 취소 버튼
	private String user_id="";				// 사용자 아이디
	private String user_name="";			// 사용자 이름
	private String user_password="";		// 사용자 비밀번호
	private int user_age=0;					// 사용자 나이
	private String user_sex="";	            // 사용자 성별
	private String user_phoneNum="";		// 사용자 전화번호
	private String user_postNum="";			// 사용자 우편번호
	private String user_postAddr="";		// 사용자 주소
	private String user_email="";			// 사용자 이메일주소
	private String user_job="";				// 사용자 직업
	private String image_path="";			// 이미지 경로
	private JFrame ZipSearch = new ZipSearch();//우편번호GUI
	

	
	static Client client;					// client 객체
	
	// 네트워크를 위한 자원 변수
	private Socket socket=null;				// 소켓 변수
	private String ip="127.0.0.1";			// ip 정보
	private String id;						// id 정보
	private String passwd;					// passwd 정보
	private int port=1234;					// port번호
	

	private InputStream is;					// OutputStream(출력 스트림)
	private OutputStream os;				// InputStream(입력 스트림)
	private DataInputStream dis;			// Data를 보낼 수 있는 Stream
	private DataOutputStream dos;			// Data를 받을 수 있는 Stream
	
	// 그외 변수들
	Vector user_list = new Vector();	// 사용자 리스트 
	Vector room_list = new Vector();	// 채팅방 리스트
	StringTokenizer st;					// 프로토콜 구분을 위한 변수
	
	private String My_Room;				// 현재 들어와 있는 방 정보
	
	private final JScrollPane scrollPane = new JScrollPane(Chat_Pane);	// 채팅내용 scrollpane
	private final JScrollPane scrollPane_1 = new JScrollPane();			// 사용자리스트 scrollpane
	private final JScrollPane scrollPane_2 = new JScrollPane();			// 방 리스트 scrollpane
	
	private String roomname;
	private RoomDB roomdb;
	
	
	private String profile_image;
	private JTextField userid_tf = new JTextField();
	private JTextField username_tf = new JTextField();
	private JTextField userage_tf = new JTextField();
	private JTextField usersex_tf = new JTextField();
	//private PostInfor post_infor;
	//private Post_Search pSH;
	
	//////폰트색 바꾸기
	 SimpleAttributeSet styleSet;
	 SimpleAttributeSet styleSet1;


	
	
	
	Client(){
		setTitle("Chatting Program");
		Login_init();	// 로그인 창 화면 구성 메소드
		Main_init();	// 메인 창 화면 구성 메소드
		start();		// 리스너 실행
	}
	
	private void start(){
		idchk_btn.addActionListener(this);		// 중복확인 버튼 리스너(추가구현)
		attach_btn.addActionListener(this);		// 파일첨부 버튼 리스너(추가구현)
		postchk_btn.addActionListener(this);	// 우편번호 찾기 버튼 리스너(추가구현)
		outroom_btn.addActionListener(this);	// 채팅방 나가기 버튼 리스너(추가구현)
		join_btn.addActionListener(this);		// 회원가입 버튼 리스너(추가구현)
		Find_idpw.addActionListener(this);		//아이디찾기(추가구현)
		login_btn.addActionListener(this);		// 로그인 버튼 리스너
		ok_join.addActionListener(this);		// 회원등록 버튼 리스너
		cancel_join.addActionListener(this);	// 취소 버튼 리스너
		notesend_btn.addActionListener(this);	// 쪽지보내기 리스너
		joinroom_btn.addActionListener(this);	// 채팅방 참여 리스너
		createroom_btn.addActionListener(this);	// 방만들기 리스너
		send_btn.addActionListener(this);		// 전송 리스너
		message_tf.addKeyListener(this);		// 채팅 메시지 리스너
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
		lblNewLabel.setFont(new Font("한컴 윤고딕 250", Font.PLAIN, 16));
		lblNewLabel.setBounds(34, 42, 80, 23);
		contentPane.add(lblNewLabel);
		
		JLabel label = new JLabel("채팅방목록");
		label.setFont(new Font("한컴 윤고딕 250", Font.PLAIN, 16));
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
		notesend_btn.setFont(new Font("한컴 윤고딕 250", Font.PLAIN, 12));
		contentPane.add(notesend_btn);
		scrollPane_2.setBounds(12, 285, 114, 138);
		
		contentPane.add(scrollPane_2);
		scrollPane_2.setViewportView(Room_list);
		Room_list.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		Room_list.setListData(room_list);
		
		joinroom_btn.setBounds(12, 465, 114, 23);
		joinroom_btn.setFont(new Font("한컴 윤고딕 250", Font.PLAIN, 12));
		contentPane.add(joinroom_btn);
		
		createroom_btn.setBounds(12, 433, 114, 23);
		createroom_btn.setFont(new Font("한컴 윤고딕 250", Font.PLAIN, 12));

		contentPane.add(createroom_btn);
		scrollPane.setBounds(140, 68, 511, 388);
	
		contentPane.add(scrollPane);
		scrollPane.setViewportView(Chat_Pane);
		Chat_Pane.setFont(new Font("한컴 윤고딕 250", Font.PLAIN, 20));
		Chat_Pane.setDisabledTextColor(Color.black);
		Chat_Pane.setEnabled(false);
		
		message_tf = new JTextField();
		message_tf.setFont(new Font("한컴 윤고딕 250", Font.PLAIN, 15));
		message_tf.setBounds(138, 467, 398, 30);
		contentPane.add(message_tf);
		message_tf.setColumns(10);
		message_tf.setEnabled(false);
		
		JLabel label_1 = new JLabel("채팅내용");
		send_btn.setBounds(554, 466, 97, 31);
		send_btn.setFont(new Font("한컴 윤고딕 250", Font.PLAIN, 16));
		contentPane.add(send_btn);
		send_btn.setEnabled(false);
		label_1.setFont(new Font("한컴 윤고딕 250", Font.PLAIN, 16));
		label_1.setForeground(Color.BLACK);
		label_1.setBounds(363, 42, 57, 22);
		contentPane.add(label_1);
		
		JLabel lblCacaoTalk = new JLabel("TALKING ABOUT");
		lblCacaoTalk.setFont(new Font("HY강B", Font.BOLD, 24));
		lblCacaoTalk.setBounds(27, 10, 200, 30);
		contentPane.add(lblCacaoTalk);
		
		outroom_btn.setFont(new Font("한컴 윤고딕 240", Font.PLAIN, 12));
		outroom_btn.setBounds(12, 496, 114, 23);
		
		contentPane.add(outroom_btn);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 235, 205));
		JLabel lblNewLabel_8 = new JLabel("사용자 사진");
		lblNewLabel_8.setForeground(Color.BLACK);
		lblNewLabel_8.setFont(new Font("한컴 윤고딕 250", Font.PLAIN, 16));
		lblNewLabel_8.setBounds(687, 42, 106, 22);
		contentPane.add(lblNewLabel_8);
		panel.setBorder(new TitledBorder(null, "사진", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(678, 72, 132, 144);
		
		
		contentPane.add(panel);
		panel.setLayout(null);

		userimage_lbl = new JLabel();
		userimage_lbl.setBounds(6, 17, 120, 120);
		panel.add(userimage_lbl);
		
		JLabel lblNewLabel_10 = new JLabel("ID :");
		lblNewLabel_10.setFont(new Font("한컴 윤고딕 250", Font.PLAIN, 18));
		lblNewLabel_10.setBounds(678, 268, 57, 15);
		contentPane.add(lblNewLabel_10);
		
		JLabel lblNewLabel_11 = new JLabel("\uC774\uB984 :");
		lblNewLabel_11.setFont(new Font("한컴 윤고딕 250", Font.PLAIN, 18));
		lblNewLabel_11.setBounds(668, 317, 57, 15);
		contentPane.add(lblNewLabel_11);
		
		JLabel lblNewLabel_12 = new JLabel("\uB098\uC774 :");
		lblNewLabel_12.setFont(new Font("한컴 윤고딕 250", Font.PLAIN, 18));
		lblNewLabel_12.setBounds(668, 373, 57, 15);
		contentPane.add(lblNewLabel_12);
		
		JLabel lblNewLabel_13 = new JLabel("\uC131\uBCC4 :");
		lblNewLabel_13.setFont(new Font("한컴 윤고딕 250", Font.PLAIN, 18));
		lblNewLabel_13.setBounds(668, 421, 57, 15);
		contentPane.add(lblNewLabel_13);
		
		
	
		JButton logout_btn = new JButton("로그아웃");
		logout_btn.addActionListener(new ActionListener() {	// 로그아웃 버튼을 누르면
			public void actionPerformed(ActionEvent arg0) {
				try {
					out_time(id);					// db에 로그아웃 시간을 저장 하고
												// 스트림과 socket을 종료 한다.
					os.close();					
					is.close();					
					dos.close();				
					dis.close();
					socket.close();
					setVisible(false);			// 현재 채팅화면 을 종료하고
					passwd_tf.setText("");
					id_tf.setText("");
					Login_GUI.setVisible(true);	// 로그인 화면을 띄워준다.
					user_list.remove(id);		// user_list에서 해당 id를 종료시킨다.
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		logout_btn.setFont(new Font("한컴 윤고딕 250", Font.PLAIN, 16));
		logout_btn.setBounds(678, 465, 132, 30);
		contentPane.add(logout_btn);
		
		JButton kk_image;
		JButton sad_image;
		JButton angry_image;
		JButton smile_image;		
		
		/*
		kk_image = new JButton("");
		kk_image.setIcon(new ImageIcon("C:\\Users\\image\\ㅋㅋ.PNG"));	// 버튼을 이모티콘이미지로 출력
		kk_image.setBounds(140, 503, 106, 93);
		kk_image.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				send_message("Chatting/"+My_Room+"/"+"C:\\Users\\image\\ㅋㅋ.PNG" + "/" + profile_image);
				// 서버에 프로토콜 전송(Chatting/방이름/이모티콘이미지경로/프로필이미지경로)
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
				send_message("Chatting/"+My_Room+"/"+"C:\\Users\\image\\ㅡㅡ.PNG" + "/" + profile_image);
			}
		});
		angry_image.setIcon(new ImageIcon("C:\\Users\\image\\ㅡㅡ.PNG"));
		angry_image.setBounds(410, 503, 106, 93);
		contentPane.add(angry_image);
		
		
		sad_image = new JButton("");
		sad_image.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send_message("Chatting/"+My_Room+"/"+"C:\\Users\\image\\ㅠㅠ.PNG" + "/" + profile_image);
			}
		});
		sad_image.setIcon(new ImageIcon("C:\\Users\\image\\ㅠㅠ.PNG"));
		sad_image.setBounds(545, 503, 106, 93);
		contentPane.add(sad_image);
		*/
	
		
		JButton imageChange_btn = new JButton("아이콘변경");
		imageChange_btn.setFont(new Font("한컴 윤고딕 250", Font.PLAIN, 16));
		imageChange_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FileDialog fdlg = new FileDialog(new JFrame(),"file open",FileDialog.LOAD);
				fdlg.setVisible(true);			//파일 다이얼로그 생성

				if(fdlg.getFile()==null)		//파일을 선택하지 않았을 때
				{
					return;
				}
				else{		//파일이 선택되었을 때
					ImageIcon originalIcon;
					originalIcon=new ImageIcon(fdlg.getDirectory()+fdlg.getFile());
					Image originalImage = originalIcon.getImage();
					Image resizeImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
					ImageIcon resizeIcon = new ImageIcon(resizeImage);
					profile_image=fdlg.getDirectory()+fdlg.getFile();
					userimage_lbl.setIcon(resizeIcon);		//선택한 이미지를 사이즈에 맞게 출력
					
					
					Connection conn = null;
					PreparedStatement psmt = null;	
					FileInputStream fis=null;		// 이미지 파일 binary로 저장하기 위한 변수 

					// preparedStatement 에서는 ?로 사용해야 한다.
					String sql = "update image_info where id = ? set image_path = ?, fis= ? ";	

					try{
						// db연결
						Class.forName("com.mysql.jdbc.Driver");
						conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/bomi?useSSL=false","root", "0720");
						
						fis=new FileInputStream(profile_image);	// 이미지 경로를 받아옴 
						
						psmt=conn.prepareStatement(sql);		// setBinaryStream을 사용하기 위해 prepareStatement 사용
						psmt.setString(1, id);		
						psmt.setString(2, image_path);
						psmt.setBinaryStream(3, fis);// prepareStatement는 setString으로 하나하나 다 저장해줘야 한다
					
						psmt.executeUpdate();				// 다 저장해준 후 executeUpdate한다(createStatement는 매개변수에 쿼리문 넣는다)
					}
					catch(ClassNotFoundException cnfe) {
						System.out.println("해당클래스를 찾을수 없습니다."+cnfe.getMessage());
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
		lblId.setFont(new Font("한컴 윤고딕 250", Font.PLAIN, 24));
		lblId.setBounds(62, 211, 70, 28);
		Login_Pane.add(lblId);
		
		JLabel lblPasswd = new JLabel("PW : ");
		lblPasswd.setFont(new Font("한컴 윤고딕 250", Font.PLAIN, 24));
		lblPasswd.setBounds(62, 264, 129, 28);
		Login_Pane.add(lblPasswd);
	
		
		id_tf = new JTextField();
		id_tf.setFont(new Font("굴림", Font.PLAIN, 24));
		id_tf.setBounds(181, 209, 181, 31);
		Login_Pane.add(id_tf);
		id_tf.setColumns(10);
		
		
		passwd_tf = new JPasswordField(10);
		passwd_tf.setFont(new Font("굴림", Font.PLAIN, 24));
		passwd_tf.setBounds(181, 262, 181, 31);
		
		Login_Pane.add(passwd_tf);
		passwd_tf.setColumns(10);
		login_btn.setForeground(Color.BLACK);
		login_btn.setBackground(Color.WHITE);
		
		
		login_btn.setBounds(62, 332, 294, 44);
		login_btn.setFont(new Font("한컴 윤고딕 250", Font.PLAIN, 24));
		Login_Pane.add(login_btn);
		
		JLabel lblChattingProgram = new JLabel("Chatting Program");
		lblChattingProgram.setFont(new Font("Segoe UI Black", Font.BOLD, 30));
		lblChattingProgram.setBounds(48, 90, 314, 47);
		Login_Pane.add(lblChattingProgram);
		join_btn.setBackground(Color.WHITE);
		
		join_btn.setBounds(62, 391, 294, 44);
	    join_btn.setFont(new Font("한컴 윤고딕 250", Font.PLAIN, 24));
	    Login_Pane.add(join_btn);
	      
		Login_GUI.setVisible(true); // true일 경우 화면에 보인다.
	}
	
	
	public void Rgtn_init(){
		rgtn_GUI.setTitle("회원가입");
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
		rgtn_Pane.add(lbl_image);//회원가입 사람이미지
		
		JLabel lblNewLabel = new JLabel("회 원 가 입");
		lblNewLabel.setBounds(219, 0, 164, 69);
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setFont(new Font("한컴 윤고딕 250", Font.BOLD, 22));
		rgtn_Pane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("ID : ");
		lblNewLabel_1.setBounds(237, 269, 57, 15);
		rgtn_Pane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Password :");
		lblNewLabel_2.setBounds(237, 312, 86, 15);
		rgtn_Pane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("성별 : ");
		lblNewLabel_3.setBounds(237, 390, 57, 15);
		rgtn_Pane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("나이 :");
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
		
		JLabel label = new JLabel("이름 : ");
		label.setBounds(12, 269, 57, 15);
		rgtn_Pane.add(label);
		
		tf_name = new JTextField();
		tf_name.setBounds(77, 269, 110, 21);
		rgtn_Pane.add(tf_name);
		tf_name.setColumns(10);
		
		JLabel label_1 = new JLabel("사진추가");
		label_1.setBounds(75, 135, 125, 15);   //사진추가 라벨
		rgtn_Pane.add(label_1);
		
		image_pane = new JScrollPane();
		image_pane.setBounds(215, 84, 120, 120);    //이미지
		rgtn_Pane.add(image_pane);
		
		attach_btn.setBounds(380, 131, 97, 23); 	// 사진추가 버튼
		rgtn_Pane.add(attach_btn);
		
		JLabel lblNewLabel_5 = new JLabel("직업 :");
		lblNewLabel_5.setBounds(13, 351, 57, 15);
		rgtn_Pane.add(lblNewLabel_5);
		
		job_cbx.setBounds(77, 348, 82, 21);
		job_cbx.setModel(new DefaultComboBoxModel(new String[] {"초등학생", "중학생", "고등학생", "대학생", "회사원", "공무원", "사업가", "무직"}));
		rgtn_Pane.add(job_cbx);
		
		JLabel lblNewLabel_6 = new JLabel("e-mail :");
		lblNewLabel_6.setBounds(12, 390, 57, 15);
		rgtn_Pane.add(lblNewLabel_6);
		
		JLabel label_2 = new JLabel("전화번호 :");
		label_2.setBounds(237, 354, 67, 15);
		rgtn_Pane.add(label_2);
		
		tf_phone = new JTextField();
		tf_phone.setBounds(316, 348, 103, 21);
		rgtn_Pane.add(tf_phone);
		tf_phone.setColumns(10);
		JLabel lblNewLabel_7 = new JLabel("우편번호 :");
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
		
		JLabel lblNewLabel_9 = new JLabel("상세주소");
		lblNewLabel_9.setBounds(12, 473, 78, 21);
		rgtn_Pane.add(lblNewLabel_9);
		
		
		
		rgtn_GUI.setVisible(true);
	}
	private void Network(){
		try {
			socket = new Socket(ip,port);	// 127.0.0.1 ip와 1234 port번호를 정보로 소켓 생성

			if(socket!=null){	// 정상적으로 소켓이 연결 되었을 경우
				Connection();	// 연결한다.
			}
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "연결 실패","알림",JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "연결 실패","알림",JOptionPane.ERROR_MESSAGE);
		}
	}
	private void send_message(String str){	
		try {
			dos.writeUTF(str);			// 서버에게 프로토콜 전송
		} 
		catch (IOException e) {
		}
	}
	
	private void Connection(){	// server-client 연결
		try{
			// Stream 연결 구성
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			
			os= socket.getOutputStream();
			dos = new DataOutputStream(os);
		}
		catch(IOException e){
			JOptionPane.showMessageDialog(null, "연결 실패","알림",JOptionPane.ERROR_MESSAGE);
		}
		// 연결 후 Main GUI를 키고 Login GUI를 끈다.
		this.setVisible(true);
		this.Login_GUI.setVisible(false);
		
		// 처음 접속시에 user ID를 서버에 전송
		send_message(id);
		
		// User_list에 user ID 추가
		user_list.add(id);		
		
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try {
						String msg = dis.readUTF();		// Server로부터 메세지를 받는다.
						System.out.println("서버로부터 수신된 메시지 : " + msg);
						
						inmessage(msg);					// Server로부터 받아온 메세지 처리
					} 
					catch (IOException e) {
						try{
							// 입출력 스트림 종료
							out_time(id);			// 로그아웃 시간을 DB에 저장
							os.close();				// 스트림 종료
							is.close();					
							dos.close();				
							dis.close();
							socket.close();			// 클라이언트 소켓 종료
							JOptionPane.showMessageDialog(null, "종료되었습니다.","알림",JOptionPane.INFORMATION_MESSAGE);
						}
						catch(IOException e2){
						}
						break;
					}
				}
			}
		});
		th.start();			// 스레드 시작
	}
	
	private void inmessage(String str){			// 서버로부터 들어오는 모든 메세지
		
		
		st = new StringTokenizer(str,"/");		// StrigTokenizer를 이용해 '/'로 프로토콜, 메세지 구분
		
		String protocol = st.nextToken();	// 어떤 프로토콜인지 저장
		String Message = st.nextToken();	// 프로토콜 다음에 해당하는 정보(사용자, 방이름)
		
		if(protocol.equals("NewUser")){			// 새로운 사용자 프로토콜 일 때
			user_list.add(Message);				// 해당 사용자를 user_list에 더한다.
		}
		else if(protocol.equals("OldUser")){	// 기존의 사용자 프로토콜일 때
			user_list.add(Message);				// 해당 사용자를 user_list에 더한다. (늦게 들어온 user의 리스트 창에 표시)
		}
		else if(protocol.equals("Note")){		// 프로토콜이 쪽지보내기(Note) 일 때
			String note = st.nextToken();		// 쪽지 메세지 저장
			
			JOptionPane.showMessageDialog(null, note,Message+"님으로 부터 온 쪽지",JOptionPane.CLOSED_OPTION);	// 쪽지 알림
		}
		else if(protocol.equals("CreateRoom")){	// 방을 만들었을 때
			My_Room=Message;					// 방 이름 저장
			message_tf.setEnabled(true);		// 채팅창 잠금 해제
			send_btn.setEnabled(true);			// 전송 버튼 잠금 해제
			joinroom_btn.setEnabled(false);		// 채팅방 참여 잠금
			createroom_btn.setEnabled(false);	// 방만들기 잠금
		}
		else if(protocol.equals("CreateRoomFail")){	// 방만들기 실패했을 때 실패 알림
			JOptionPane.showMessageDialog(null, "방 만들기 실패","알림",JOptionPane.ERROR_MESSAGE);
		}
		else if(protocol.equals("New_Room")){	// 프로토콜이 New_room일 때(사용자들에게 새로운 방이 생겼단 걸 알림)
			room_list.add(Message);				// 채팅방 리스트에 정보 저장
			Room_list.setListData(room_list);	// 채팅방 리스트 GUI에 업데이트
			
		}
		else if(protocol.equals("Chatting")){	// 프로토콜이 채팅 메세지(Chatting)전송일 때
			
			// 이모티콘을 저장하기위해 Textarea가 아닌 TextPane을 사용했다.
			// TextPane은 Textarea와는 달리 append라는 메소드가 없어서 계속해서 TextPane 창에
			// 채팅창을 입력할 수가 없다. 이를 위해 StyleDocument를 사용한다.
			// 이미지 경로 받아오기
			StyledDocument doc = Chat_Pane.getStyledDocument();
			String msg = st.nextToken();		// 채팅 메세지 저장
			String image = st.nextToken();	
			//----------------------------------------------
						//프로필사진 사이즈 조정부분
						ImageIcon ori=new ImageIcon(image_path);
						Image oriimage = ori.getImage();
						Image reimage = oriimage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
						ImageIcon resizeIcon = new ImageIcon(reimage);								
						
						//이모티콘 사이즈 조정부분.
						ImageIcon ori_emoti=new ImageIcon(msg);
						Image ori_emo_image = ori_emoti.getImage();
						Image re_emo_image = ori_emo_image.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
						ImageIcon re_emoti = new ImageIcon(re_emo_image);	
						
						//StyledDocument doc = Chat_Pane.getStyledDocument();		
						SimpleAttributeSet smi=new SimpleAttributeSet();			// 이미지를 출력하기 위한 변수
						StyleConstants.setIcon(smi,resizeIcon);						// smi변수에 프로필 이미지를 설정
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
				else if(Message.equals("알림")) {
					
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
				else if(Message.equals("알림")) {
					
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
				/*else{							// 그냥 일반 메시지일때
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
			
			
			/*// 이미지 경로 받아오기
			
			//프로필사진 사이즈 조정부분
			ImageIcon ori=new ImageIcon(image);
			Image oriimage = ori.getImage();
			Image reimage = oriimage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			ImageIcon resizeIcon = new ImageIcon(reimage);								
			
			//이모티콘 사이즈 조정부분.
			ImageIcon ori_emoti=new ImageIcon(msg);
			Image ori_emo_image = ori_emoti.getImage();
			Image re_emo_image = ori_emo_image.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
			ImageIcon re_emoti = new ImageIcon(re_emo_image);	
			
			//StyledDocument doc = Chat_Pane.getStyledDocument();		
			SimpleAttributeSet smi=new SimpleAttributeSet();			// 이미지를 출력하기 위한 변수
			StyleConstants.setIcon(smi,resizeIcon);						// smi변수에 프로필 이미지를 설정*/
			
			
			//-------------------------------------------------------------------이모티콘
			if(msg.contains("C:\\Users")){
				
				
                try {
                	
                	doc.insertString(doc.getLength(),"ignored",smi);					// 프로필사진 입력
                	doc.insertString(doc.getLength(), Message + " : " ,null);			// 사용자 : 입력
                	StyleConstants.setIcon(smi,re_emoti);										// smi변수에 이모티콘 이미지 설정
					doc.insertString(doc.getLength(),"ignored",smi);					// 이모티콘 입력
					doc.insertString(doc.getLength(), "\n" ,null);					// 엔터키 입력
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
                Chat_Pane.setCaretPosition(doc.getLength());				// 입력 후 스크롤바를 맨 아래로 맞춰준다.
			}
			else{
				if(msg.equals("ㅋㅋ")){				// ㅋㅋ입력시 이모티콘
					
					StyleConstants.setIcon(smi,new ImageIcon("C:/Users/image/ㅋㅋ.PNG"));		// smi변수에 이모티콘을 저장한다.
					
	                Chat_Pane.setCaretPosition(doc.getLength());
					if(Message.equals(id)) {
						
						try {
							int length = doc.getLength();
							SimpleAttributeSet right = new SimpleAttributeSet();
							StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
							doc.insertString(doc.getLength(),"ignored",smi);					// 이미지가 출력된다.
							doc.setParagraphAttributes(length+1, 1, right, false);
							doc.insertString(doc.getLength(), "\n" ,null);					// 그 후 엔터가 입력된다.
							
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
				else if(msg.equals("ㅠㅠ")){				// ㅋㅋ입력시 이모티콘
					
					StyleConstants.setIcon(smi,new ImageIcon("C:/Users/image/ㅠㅠ.PNG"));		// smi변수에 이모티콘을 저장한다.
					
	                Chat_Pane.setCaretPosition(doc.getLength());
					if(Message.equals(id)) {
						
						try {
							int length = doc.getLength();
							SimpleAttributeSet right = new SimpleAttributeSet();
							StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
							doc.insertString(doc.getLength(),"ignored",smi);					// 이미지가 출력된다.
							doc.setParagraphAttributes(length+1, 1, right, false);
							doc.insertString(doc.getLength(), "\n" ,null);					// 그 후 엔터가 입력된다.
							
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
				else if(msg.equals("^^")){				// ㅋㅋ입력시 이모티콘
					
					StyleConstants.setIcon(smi,new ImageIcon("C:/Users/image/^^.PNG"));		// smi변수에 이모티콘을 저장한다.
					
	                Chat_Pane.setCaretPosition(doc.getLength());
					if(Message.equals(id)) {
						
						try {
							int length = doc.getLength();
							SimpleAttributeSet right = new SimpleAttributeSet();
							StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
							doc.insertString(doc.getLength(),"ignored",smi);					// 이미지가 출력된다.
							doc.setParagraphAttributes(length+1, 1, right, false);
							doc.insertString(doc.getLength(), "\n" ,null);					// 그 후 엔터가 입력된다.
							
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
				else if(msg.equals("ㅡㅡ")){				// ㅋㅋ입력시 이모티콘
					
					StyleConstants.setIcon(smi,new ImageIcon("C:/Users/image/ㅡㅡ.PNG"));		// smi변수에 이모티콘을 저장한다.
					
	                Chat_Pane.setCaretPosition(doc.getLength());
					if(Message.equals(id)) {
						
						try {
							int length = doc.getLength();
							SimpleAttributeSet right = new SimpleAttributeSet();
							StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
							doc.insertString(doc.getLength(),"ignored",smi);					// 이미지가 출력된다.
							doc.setParagraphAttributes(length+1, 1, right, false);
							doc.insertString(doc.getLength(), "\n" ,null);					// 그 후 엔터가 입력된다.
							
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
				//-------------------------------------------------------------------이모티콘끝

			
				/*else{							// 그냥 일반 메시지일때
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
		else if(protocol.equals("OldRoom")){	// 프로토콜이 OldRoom일 때(새로 들어온 사용자에게 기존의 방 리스트 알림)
			room_list.add(Message);				// 채팅방 리스트 정보 저장
		}
		else if(protocol.equals("user_list_update")){	// 프로토콜이 GUI 사용자 리스트 업데이트 일 때
			User_list.setListData(user_list);			// GUI 사용자 리스트 세팅
		}
		else if(protocol.equals("room_list_update")){	// 프로토콜이 GUI 채팅방 리스트 업데이트일 때
			Room_list.setListData(room_list);			// GUI 채팅방 리스트  세팅
		}
		else if(protocol.equals("JoinRoom")){	// 프로토콜이 채팅방 참여일 때
			message_tf.setEnabled(true);		// 채팅 메세지 창 잠금 해제
			send_btn.setEnabled(true);			// 전송 버튼 잠금 해제
			joinroom_btn.setEnabled(false);		// 채팅방 참여 버튼 잠금
			createroom_btn.setEnabled(false);	// 방만들기 버튼 잠금
			My_Room=Message;					// 방이름 정보 저장
			
			JOptionPane.showMessageDialog(null,My_Room+"채팅방에 입장했습니다","알림",JOptionPane.INFORMATION_MESSAGE);
		}
		else if(protocol.equals("User_out")){	// 사용자가 종료했을 때
			user_list.remove(Message);			// 사용자 정보 삭제
		}
		
		
		else if(protocol.equals("OutRoom")){	// 방나가기(추가구현)
			message_tf.setEnabled(false);		// 채팅 메세지 창 잠금 해제
			send_btn.setEnabled(false);			// 전송 버튼 잠금 해제
			joinroom_btn.setEnabled(true);		// 채팅방 참여 버튼 잠금
			createroom_btn.setEnabled(true);	// 방만들기 버튼 잠금
			Chat_Pane.setText("");				// 채팅 창을 공백으로
			JOptionPane.showMessageDialog(null, My_Room + "채팅방에서 퇴장했습니다","알림",JOptionPane.INFORMATION_MESSAGE);
			// 채팅방에서 퇴장했다고 알림
			My_Room="";							// 현재 채팅 방 정보를 초기화
		}
	}
	// 메인 메소드

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==login_btn){	// 로그인 GUI 창에서 접속 버튼 클릭 시
			if(id_tf.getText().length()==0){	// ID 필드에 아무것도 적혀져 있지 않으면
				id_tf.setText("ID를 입력해주세요");
				id_tf.requestFocus();
			}
			else if(passwd_tf.getText().length()==0){	// ID 필드에 아무것도 적혀져 있지 않으면
				passwd_tf.setText("Password를 입력해주세요");
				passwd_tf.requestFocus();
			}
			else{									// 모두 잘 적혀있다
				id=id_tf.getText().trim();	// 사용자가 입력한 id를 받아온다.
				passwd=passwd_tf.getText().trim();	// 사용자가 입력한 password를 받아온다.
				
				if(DB_Check(id,passwd)){	// db에 존재하는 id와 password이면
					in_time(id);				// db에 로그인 시간을 기록한다.
					Network();				// 정상연결(서버와 통신하기 위한 Network 메소드 호출)
					DB_Open(id);			// 해당 id 대한 db정보 저장(채팅창에서 사용자 이미지 띄워 줌)
				}							
				else{						// db에 존재하지 않는 id이면
					JOptionPane.showMessageDialog(null, "아이디와 비밀번호가 일치하지 않습니다!","알림",JOptionPane.ERROR_MESSAGE);
					id_tf.setText("");
					passwd_tf.setText("");
				}
			}
		}
		else if(e.getSource()==notesend_btn){							// 쪽지보내기 버튼 클릭 시
			String user = (String)User_list.getSelectedValue();			// 사용자리스트에서 어떤 유저가 선택됬는지 알 수 있다.
			String note = JOptionPane.showInputDialog("보낼메세지");	// 보낼 메세지를 입력받음
			if(note!=null){	
				send_message("Note/"+user+"/"+note);					// 쪽지보내기 프로토콜로 서버에게 메세지 보냄
				// ex) Note/User2/나는 User1입니다.
			}
		}
		else if(e.getSource()==joinroom_btn){							// 채팅방 참여 버튼 클릭 시
			System.out.println("채팅방 참여 : " + profile_image);
			String JoinRoom=(String)Room_list.getSelectedValue();		// 채팅방리스트에서 어떤 채팅방이 선택됬는지 알 수 있다.
			send_message("JoinRoom/"+JoinRoom + "/" + profile_image);							// 채팅방 참여 프로토콜로 서버에게 메세지 보냄
			
		}
		else if(e.getSource()==createroom_btn){					// 방만들기 버튼 클릭 시
			roomname=JOptionPane.showInputDialog("방 이름");	// 방이름을 입력받음.
			if(roomname!=null){
				send_message("CreateRoom/"+roomname);			// 방만들기 프로토콜로 서버에게 메세지 보냄
				roomdb = new RoomDB(roomname);					// 방이름을 DB 테이블로 저장한다.
			
			}
		}
		else if(e.getSource()==send_btn){										// 채팅 메세지 전송 버튼 클릭 시
			send_message("Chatting/"+My_Room+"/"+message_tf.getText().trim()+ "/" + profile_image);	
			message_tf.setText("");												// 채팅 메세지 필드를 공백처리
			message_tf.requestFocus();											// 채팅 메세지 필드 포커스 맞춤
			// Chatting + 방이름 + 내용
		}
		
		
		else if(e.getSource()==join_btn){			// 회원가입 버튼 클릭시
			Rgtn_init();							// 회원가입 GUI 호출
		}
		else if(e.getSource()==ok_join){			// 회원가입 GUI에서 회원등록 버튼 클릭 시
			if(tf_age.getText().length()==0){		// 나이 필드를 입력하지 않았으면
				JOptionPane.showMessageDialog(null, "나이를 입력해주세요.","알림",JOptionPane.ERROR_MESSAGE);
				return ;
			}
			if(tf_password.getText().length()>4) {
				JOptionPane.showMessageDialog(null, "정상처리되었습니다.");
			}
			else {
				JOptionPane.showMessageDialog(null, "비밀번호를 다시 확인하세요.");
				tf_password.setText("");
				tf_password.requestFocus();
				
				
			}
			// 사용자가 입력한 회원정보 데이터를 각각 저장함
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
				user_sex="남";
			if(woman_btn.isSelected()==true)
				user_sex="여";
			
			// 어느하나라도 입력하지 않았다면 경고메시지 출력
			if(user_id.length()==0 || user_password.length()==0 || user_name.length()==0 || user_sex.length()==0 || user_job.length()==0 || user_phoneNum.length()==0 ||
					user_email.length()==0 || user_postNum.length()==0 || user_postAddr.length()==0 || image_path.length()==0){
				JOptionPane.showMessageDialog(null, "입력하지 않은 정보가 있습니다.","알림",JOptionPane.ERROR_MESSAGE);
				return ;
			}
			// 다입력했으면
			else{
				if(DB_Check(user_id)){			// 중복아이디 체크 후 중복 없으면
					// 회원정보 데이터를 db에 저장
					insert_DB(user_id,user_name,user_password,user_age,user_sex, user_phoneNum, user_job, user_postNum,
							user_postAddr, user_email);
					// 완료메세지 출력
					JOptionPane.showMessageDialog(null, "회원가입이 완료되었습니다.","알림",JOptionPane.INFORMATION_MESSAGE);
					// 회원등록 GUI 종료
					rgtn_GUI.setVisible(false);
				}
				else{	// 중복아이디 있으면
						// 중복아이디 있다고 메세지 출력
					JOptionPane.showMessageDialog(null, "아이디가 중복되었습니다.","알림",JOptionPane.ERROR_MESSAGE);
					tf_id.setText("");
				}
			}
		}
		// 회원가입GUI 에서 취소버튼 클릭 시 GUI종료
		else if(e.getSource()==cancel_join){
			rgtn_GUI.setVisible(false);
		}
		else if(e.getSource()==outroom_btn){	// 채팅방 나가기 버튼 클릭 시
			System.out.println("ㅇㅇㅇㅇㅇ " + profile_image);
			send_message("OutRoom/"+My_Room+ "/" + profile_image);	// OutRoom 프로토콜과 현재 방이름을 Server로 보낸다
		}

		else if(e.getSource()==postchk_btn){	// 우편번호 체크 버튼 클릭 시
			System.out.println("우편번호 조회 버튼 클릭");
			//int row = tf_addr.getSelectedText();
			//Object post = tf_postcode.getValueAt(row,1);
			this.ZipSearch.setVisible(true);

			//this.setVisible(false);
			//Rgtn_init().tf_addr.setText((String) post);
			//Mypage.U_PostField.setText((String) post);
			//post_infor = new PostInfor();		// 우편번호 클래스를 새로 생성해서
			//pSH= new Post_Search(post_infor,client);	// 우편번호 조회 GUI를 호출 후 데이터 저장
		}
		
		
		else if(e.getSource()==attach_btn){		// 첨부하기 버튼 클릭 시
			FileDialog fdlg = new FileDialog(this,"file open",FileDialog.LOAD);	// 파일 다이얼로그 출력
	        fdlg.setVisible(true);
	        if(fdlg.getFile()==null)			// 취소버튼을 눌렀을 경우 리턴
	        	return;
	        ImageIcon originalIcon = new ImageIcon(fdlg.getDirectory()+fdlg.getFile());	// 해당 이미지 저장
	        Image originalImage = originalIcon.getImage();								// 사이즈를 조절하기 위한 Image 변수
	        Image resizeImage= originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);	// 사이즈 조절한 Image 변수
	        ImageIcon resizeIcon = new ImageIcon(resizeImage);		// 사이즈 조절이 완성 된 이미지아이콘 객체
	        image_path=fdlg.getDirectory()+fdlg.getFile();			// 이미지 경로 저장
	        lbl_image.setIcon(resizeIcon);							// 라벨을 사이즈 조절이 완성된 이미지로 출력
		}
		else if(e.getSource()==idchk_btn){	// 중복확인 버튼 클릭 시
			user_id=tf_id.getText().trim();
			if(DB_Check(user_id)){			// 중복아이디가 없으면
				 if(user_id.length()==0){	// 아무것도 입력 안했으면
						JOptionPane.showMessageDialog(null, "아이디를 입력해주세요","알림",JOptionPane.ERROR_MESSAGE);
				}
				 else{						// 아이디를 입력하고 중복도 안됬으면
					 	JOptionPane.showMessageDialog(null, "사용할 수 있는 아이디입니다.","알림",JOptionPane.INFORMATION_MESSAGE);
				}
			}
			else{	// 중복아이디 있으면
				JOptionPane.showMessageDialog(null, "아이디가 중복되었습니다.","알림",JOptionPane.ERROR_MESSAGE);
				tf_id.setText("");
			}
		}
	}
	
	/*public void actionPerformed1(ActionEvent e){
		if(e.getSource() == postchk_btn){
			System.out.println("우편번호 조회 버튼 클릭");
			int row = table.getSelectedRow();
			Object post = table.getValueAt(row,1);

			this.setVisible(false);
			Rgtn_init().tf_addr.setText((String) post);
			Mypage.U_PostField.setText((String) post);

			}
		}

	}*/ 
	
	// db를 저장하는 메소드
	public void insert_DB(String id,String name, String password, int age,String sex, String phoneNum, String job,
			String postNum, String postAddr,String email){
		Connection conn = null;
		
		
		// db에 이미지 파일을 저장하기 위해서는 db에 blob 자료형에 맞게 binary값을 자바에서 넘겨줘야 하는데
		// Statement 클래스에는 setBinaryStream 함수가 없기 때문에 preparedStatement 클래스를 사용한다.
		PreparedStatement psmt = null;	
		PreparedStatement psmt2 = null;
		FileInputStream fis= null;		// 이미지 파일 binary로 저장하기 위한 변수 
		//InputStream is = file.getBinaryStream();
       
		//fis = new ByteArrayOutputStream();
		

		String sql = "insert into user_info values(?,?,?,?,?,?,?,?,?,?,?,?);";	// preparedStatement 에서는 ?로 사용해야 한다.
		String sql2 = "insert into image_info values(?,?,?)";
		

		try{
			// db연결
			Class.forName("com.mysql.jdbc.Driver");
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/bomi?useSSL=false","root", "0720");			
			fis=new FileInputStream(image_path);	// 이미지 경로를 받아옴 
			
			
			psmt=conn.prepareStatement(sql);		// setBinaryStream을 사용하기 위해 prepareStatement 사용
			psmt.setString(1, name);					// prepareStatement는 setString으로 하나하나 다 저장해줘야 한다
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
			psmt.executeUpdate();				// 다 저장해준 후 executeUpdate한다(createStatement는 매개변수에 쿼리문 넣는다)
			
			psmt2=conn.prepareStatement(sql2);
			psmt2.setString(1, id);
			psmt2.setString(2, image_path);
			psmt2.setBinaryStream(3, fis);           // db에 이미지 파일 binary로 저장
			psmt2.executeUpdate();                           //image_info에 insert
		}
		catch(ClassNotFoundException cnfe) {
			System.out.println("해당클래스를 찾을수 없습니다."+cnfe.getMessage());
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
	public boolean DB_Check(String id,String password){	// 로그인하기 위해 일치하는 id,password 찾는 메소드
		Connection conn = null;
		Statement stmt = null;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");	// JDBC 드라이버를 로드
			
			// DB에 연결(localhost/3307포트/java_chatting DataBase/root계정/비밀번호 1234
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/bomi?useSSL=false","root", "0720");
			
			// 데이터베이스의 데이터를 읽어온다.
			stmt=conn.createStatement();
			
			// id와 password가 정확히 일치하는 데이터를 받아온다.
			ResultSet rs=stmt.executeQuery("select * from user_info where id = '" + id + "' and password = '" + password + "'");
			
			if(rs.next()){	// rs의 데이터가 존재하면 true 그렇지 않으면 false
				// 매개변수로 입력받은 id, password 값과 DB에 저장되어있는 ID,Password 필드 값이 동일하면 true 반환   
				if(id.equals(rs.getString("ID")) && password.equals(rs.getString("Password"))){	
					return true;
				}
			}
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
		return false;	//rs.next에서 데이터가 존재하지 않다면 false반환
	}
	
	public boolean DB_Check(String id){	// 회원가입시 id중복확인 하는 메소드
		Connection conn = null;
		Statement stmt = null;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/bomi?useSSL=false","root", "0720");
			stmt=conn.createStatement();
			// 입력받은 id와 일치하는 데이터를 받아온다.
			ResultSet rs=stmt.executeQuery("select * from user_info where id = '" + id + "'");
			
			if(rs.next()){	// 데이터가 존재하고
				if(id.equals(rs.getString("ID"))){	// 입력받은 id와 db의 데이터가 같다면
					return false;				//false 반환
				}
			}
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
		return true;	// 아이디가 중복되지 않으면 true 반환
	}
	
	// 매개변수로 입력받은 id의 db에 있는 정보를 저장 시켜주는 메소드(로그인 시에 호출)
	public void DB_Open(String id){		 
		Connection conn =null;
		Statement stmt = null;
		Statement stmt2 = null;
		try{
			// preparedStatement 쓰려다가 자꾸 쿼리문이 안먹어서
			// createStatmenet문으로 썼음. 어차피 이미지 binary를 가져올 수 있는 클래스는 ResultSet이기 때문에
			// preparedStatement 쓸 필요가 없음. preparedStatement는 setBinary 메소드를 위해 썼었음
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bomi?useSSL=false","root", "0720");
			stmt=conn.createStatement();
			stmt2=conn.createStatement();
			ResultSet rs =stmt.executeQuery("select * from user_info where id = '" + id + "'");
			ResultSet rs2 = stmt2.executeQuery("select * from image_info where id = '"+ id+"'");
			
			if(rs2.next()){ // rs에 관한 정보를 가져오기 위해서는 꼭 if문이나 while문에 rs.next를 해줘야 함
				image_is = rs2.getBinaryStream("fis");			// 이미지 binaryStream을 InputStream 변수에 저장
				String image_path = rs2.getString("image_path");
				try {
					System.out.println("이미지 경로 : " + image_path);
					ImageIcon originalIcon=new ImageIcon(ImageIO.read(image_is));	// db에 저장되어있던 이미지 아이콘 읽어온다.
			        Image originalImage = originalIcon.getImage();					
			        Image resizeImage= originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
			        ImageIcon resizeIcon = new ImageIcon(resizeImage);
					userimage_lbl.setIcon(resizeIcon);		// resize된 이미지를 채팅창 사용자 사진에 출력한다.
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 각 db에 저장된 내용을 저장한다
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
				
				System.out.println("로그인 시 : " + profile_image);
				// 저장 후 main_gui 채팅창 사용자 정보에 저장된 데이터를 출력한다
				
				usersex_tf.setText(user_sex);
				userage_tf.setText(Integer.toString(user_age));
				username_tf.setText(user_name);
				userid_tf.setText(user_id);
				}
			}
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
	// 회원의 로그인 시간을 db에 저장해주는 메소드
	public void in_time(String id){
		Connection conn = null;
		Statement stmt = null;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");	// JDBC 드라이버를 로드
			// DB에 연결(localhost/3307포트/java_chatting DataBase/root계정/비밀번호 1234
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/bomi?useSSL=false","root", "0720");
			// 데이터베이스의 데이터를 읽어온다.
			stmt=conn.createStatement();
			
			// update문을 써서 현재 로그인 한 시간을 저장한다. 그 전에는 null값을 넣어주었음.
			stmt.executeUpdate("update user_info set in_time = now() where id = '" + id + "'");
			
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
	// 회원의 로그아웃 시간을 db에 저장해주는 메소드
	public void out_time(String id){
		Connection conn = null;
		Statement stmt = null;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");	// JDBC 드라이버를 로드
			// DB에 연결(localhost/3307포트/java_chatting DataBase/root계정/비밀번호 1234
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/bomi?useSSL=false","root", "0720");
			// 데이터베이스의 데이터를 읽어온다.
			stmt=conn.createStatement();
			
			// update문을 써서 현재 로그아웃 한 시간을 저장한다. 그 전에는 null값을 넣어주었음.
			stmt.executeUpdate("update user_info set out_time = now() where id = '" + id + "'");
			
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
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub	
	}
	@Override
	// 엔터키를 누르면 채팅 메세지가 전송되고, 채팅 메세지 필드를 공백 후 포커스 맞추기
	public void keyPressed(KeyEvent e) {	
		if(e.getKeyCode()==10){				// 엔터키가 눌렸으면
			// 서버에 프로토콜 전송(Chatting/방이름/채팅내용/프로필이미지경로)
			send_message("Chatting/"+My_Room+"/"+message_tf.getText().trim()+ "/" + profile_image);	
			message_tf.setText("");			// 채팅 메세지 필드를 공백처리
			message_tf.requestFocus();		// 채팅 메세지 필드 포커스 맞춤
		}
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
