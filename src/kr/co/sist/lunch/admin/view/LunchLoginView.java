package kr.co.sist.lunch.admin.view;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import kr.co.sist.lunch.admin.controller.LunchLoginController;

@SuppressWarnings("serial")
public class LunchLoginView extends JFrame{
	private JTextField jtfId;
	private JPasswordField jpfPass;
	private JButton jbtLogin;
	
	public LunchLoginView() {
		super("도시락 관리자 로그인");
		
		jtfId = new JTextField();
		jpfPass = new JPasswordField();
		jbtLogin = new JButton("로그인");
		
		JLabel jlLoginTitle = new JLabel("관리자 로그인");
		jlLoginTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
		
		JLabel jlIdTitle = new JLabel("아이디");
		JLabel jlPassTitle = new JLabel("비밀번호");
		
		setLayout(null);
		
		jlLoginTitle.setBounds(95,10,200,40);
		jlIdTitle.setBounds(30,60,80,20);
		jtfId.setBounds(90,60,100,20);
		jlPassTitle.setBounds(30,90,80,20);
		jpfPass.setBounds(90,90,100,20);
		jbtLogin.setBounds(200,60,80,50);
		
		add(jlLoginTitle);
		add(jlIdTitle);
		add(jtfId);
		add(jlPassTitle);
		add(jpfPass);
		add(jbtLogin);
		
		//이벤트 처리 등록
		LunchLoginController llc = new LunchLoginController(this);
		addWindowListener(llc);
		jtfId.addActionListener(llc);
		jpfPass.addActionListener(llc);
		jbtLogin.addActionListener(llc);
		
		setBounds(100,100,325,180);
		setVisible(true);
		setResizable(false);
		
		jtfId.requestFocus();
		
	}//LunchLoginView

	public JTextField getJtfId() {
		return jtfId;
	}

	public JPasswordField getJpfPass() {
		return jpfPass;
	}

	public JButton getJbtLogin() {
		return jbtLogin;
	}
	
}//class
