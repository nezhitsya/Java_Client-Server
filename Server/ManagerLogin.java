package ä�ü���;

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
   private JPasswordField tf_pw;      //������ ��й�ȣ�� �Է��� �ؽ�Ʈ �ʵ�
   JButton btn_OK;               //Ȯ�ι�ư
   JButton btn_CANCEL;         //��ҹ�ư
   
   public ManagerLogin() {
      init();      //ȭ�鱸��
      start();      //��ư������ ����
      
   }
   public void init(){
      
	   setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	      setBounds(100, 100, 387, 208);
	      contentPane = new JPanel();
	      contentPane.setBackground(new Color(147, 112, 219));
	      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	      setContentPane(contentPane);
	      contentPane.setLayout(null);
	      
	      JLabel label = new JLabel("������ ��й�ȣ�� �Է����ּ���");
	      label.setFont(new Font("�޸յձ�������", Font.PLAIN, 20));
	      label.setBounds(33, 15, 330, 36);
	      contentPane.add(label);
	      
	      tf_pw = new JPasswordField();
	      tf_pw.setBounds(122, 66, 116, 21);
	      contentPane.add(tf_pw);
	      tf_pw.setColumns(10);
	      
	      btn_OK = new JButton("Ȯ    ��");
	      btn_OK.setFont(new Font("���� ��ü M", Font.PLAIN, 18));
	      btn_OK.setBounds(52, 114, 97, 23);
	      contentPane.add(btn_OK);
	      
	      btn_CANCEL = new JButton("��    ��");
	      btn_CANCEL.setFont(new Font("���� ��ü M", Font.PLAIN, 18));
	      btn_CANCEL.setBounds(219, 114, 97, 23);
	      contentPane.add(btn_CANCEL);     
	      
	      this.setVisible(true);
      
      this.setVisible(true);
   }
   public void start()
   {
      btn_OK.addActionListener(this);      //Ȯ�ι�ư ������ ����
      btn_CANCEL.addActionListener(this);   //��ҹ�ư ������ ����
   }
   
   @Override
   public void actionPerformed(ActionEvent e) {

      if (e.getSource() == btn_OK)       //Ȯ�ι�ư�� ������ ���
      {
         if(tf_pw.getText().trim().equals("0720"))      //�Է��� ���� 7552�̸�
         {
            JOptionPane.showMessageDialog(null, "������ ���� ����","�˸�",JOptionPane.INFORMATION_MESSAGE);
            Manager manager = new Manager("0720");      //���Ӽ��� Manager��ü����
            setVisible(false);                           //����ȭ�� ������
            
         }
         else      //��й�ȣ�� Ʋ��
         {
            JOptionPane.showMessageDialog(null, "��й�ȣ Ʋ��!!�����ڸ� ���Ӱ���","�˸�",JOptionPane.WARNING_MESSAGE);
            tf_pw.requestFocus();
         }
      }
      else if(e.getSource() == btn_CANCEL)      //��ҹ�ư�� ������ ��
      {
         setVisible(false);               //����ȭ�� ������
      }
   }
   
}
