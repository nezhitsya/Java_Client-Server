package 채팅서버;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

public class ManagerLogin extends JFrame implements ActionListener  {

   private JPanel contentPane;
   private JPasswordField tf_pw;      //관리자 비밀번호를 입력할 텍스트 필드
   JButton btn_OK;               //확인버튼
   JButton btn_CANCEL;         //취소버튼
   
   public ManagerLogin() {
      init();      //화면구성
      start();      //버튼리스너 시작
      
   }
   public void init(){
      
	   setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	      setBounds(100, 100, 387, 208);
	      contentPane = new JPanel();
	      contentPane.setBackground(new Color(147, 112, 219));
	      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	      setContentPane(contentPane);
	      contentPane.setLayout(null);
	      
	      JLabel label = new JLabel("관리자 비밀번호를 입력해주세요");
	      label.setFont(new Font("휴먼둥근헤드라인", Font.PLAIN, 20));
	      label.setBounds(33, 15, 330, 36);
	      contentPane.add(label);
	      
	      tf_pw = new JPasswordField();
	      tf_pw.setBounds(122, 66, 116, 21);
	      contentPane.add(tf_pw);
	      tf_pw.setColumns(10);
	      
	      btn_OK = new JButton("확    인");
	      btn_OK.setFont(new Font("한컴 윤체 M", Font.PLAIN, 18));
	      btn_OK.setBounds(52, 114, 97, 23);
	      contentPane.add(btn_OK);
	      
	      btn_CANCEL = new JButton("취    소");
	      btn_CANCEL.setFont(new Font("한컴 윤체 M", Font.PLAIN, 18));
	      btn_CANCEL.setBounds(219, 114, 97, 23);
	      contentPane.add(btn_CANCEL);     
	      
	      this.setVisible(true);
      
      this.setVisible(true);
   }
   public void start()
   {
      btn_OK.addActionListener(this);      //확인버튼 리스너 시작
      btn_CANCEL.addActionListener(this);   //취소버튼 리스너 시작
   }
   
   @Override
   public void actionPerformed(ActionEvent e) {

      if (e.getSource() == btn_OK)       //확인버튼이 눌렀을 경우
      {
         if(tf_pw.getText().trim().equals("0720"))      //입력한 값이 7552이면
         {
            JOptionPane.showMessageDialog(null, "관리자 접속 성공","알림",JOptionPane.INFORMATION_MESSAGE);
            Manager manager = new Manager("0720");      //접속성공 Manager객체생성
            setVisible(false);                           //현재화면 가리기
            
         }
         else      //비밀번호를 틀림
         {
            JOptionPane.showMessageDialog(null, "비밀번호 틀림!!관리자만 접속가능","알림",JOptionPane.WARNING_MESSAGE);
            tf_pw.requestFocus();
         }
      }
      else if(e.getSource() == btn_CANCEL)      //취소버튼을 눌렀을 때
      {
         setVisible(false);               //현재화면 가리기
      }
   }
   
}
