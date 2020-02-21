/*
 * �����ڸ�� GUI Ŭ����
 * ������ ��й�ȣ�� �Է¹޾� ������ �����ڸ�带 �����Ų��.
 */
package ä�ü���;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;



public class Manager extends JFrame{
	// ctrl + shift + o �ڵ����� import��
	
	private JPanel contentPane;	
	
	public Manager(String pw){
		
		if(pw.equals("0720")){														// 0720�� �Է¹����� ������ ��带 �����Ų��.
			setTitle("�����ڸ��");
			init();			// ȭ�� ���� �޼ҵ�			
		}
		
		
		
	}
	
	private void init(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 366, 455);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		

		JLabel lblChatting = new JLabel("ä�����α׷�");
		lblChatting.setFont(new Font("���� ��ü M", Font.BOLD, 32));
		lblChatting.setBounds(88, 30, 200, 50);
		contentPane.add(lblChatting);
		
		JLabel lblProgram = new JLabel("�����ڸ��");
		lblProgram.setFont(new Font("���� ��ü M", Font.BOLD, 32));
		lblProgram.setBounds(98, 69, 172, 65);
		contentPane.add(lblProgram);
		
		
		contentPane.setBackground(Color.PINK);
	
		
		JButton btnNewButton = new JButton("DB ����");			// DB���� ��ư
		btnNewButton.addActionListener(new ActionListener() {	// DB ���� ��ư Ŭ�� ��
			public void actionPerformed(ActionEvent arg0) {
				new DBDelete();
			}
		});
		btnNewButton.setFont(new Font("���� ����� 240", Font.PLAIN, 14));
		btnNewButton.setBounds(68, 172, 220, 38);
		contentPane.add(btnNewButton);
		//---------------------------------------------------------------------
		JButton infoSave_btn = new JButton("ȸ����������");		// txt���� ��ư Ŭ�� ��
		infoSave_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new DBSave();
				JOptionPane.showMessageDialog(null, "���������� DB�����Ͱ� txt�� ����Ǿ����ϴ�.","�˸�",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		infoSave_btn.setFont(new Font("���� ����� 240", Font.PLAIN, 14));
		infoSave_btn.setBounds(68, 278, 220, 38);
		contentPane.add(infoSave_btn);
		//-------------------------------------------------------------------
		JButton member_btn = new JButton("ȸ������");			// ȸ������ ��ư Ŭ�� ��
		member_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new MemberLoad(); 
			}
		});
		member_btn.setFont(new Font("���� ����� 240", Font.PLAIN, 14));
		member_btn.setBounds(68, 225, 220, 38);
		contentPane.add(member_btn);
		//-----------------------------------------------------------------
		JButton chatSave_btn = new JButton("ä�ó�������");
		chatSave_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new chatSave(); 
				JOptionPane.showMessageDialog(null, "���������� DB�����Ͱ� txt�� ����Ǿ����ϴ�.","�˸�",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		chatSave_btn.setFont(new Font("���� ����� 240", Font.PLAIN, 14));
		chatSave_btn.setBounds(68, 331, 220, 38);
		contentPane.add(chatSave_btn);
		
		this.setVisible(true);	// true = ȭ�鿡 ���̰� false = ȭ�鿡 ������ �ʰ�
	}
	public static void main(String args[]){
		new ManagerLogin();
	}
}