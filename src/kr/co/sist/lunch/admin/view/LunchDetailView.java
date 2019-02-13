package kr.co.sist.lunch.admin.view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import kr.co.sist.lunch.admin.controller.LunchDetailController;
import kr.co.sist.lunch.admin.controller.LunchMainController;
import kr.co.sist.lunch.admin.vo.LunchDetailVO;

/**
 * ���ö� ���̺��� ���õ� ���ö��� �� ������ ����ϰ� ����, ������ �� �� �ִ� â
 * @author owner
 */
@SuppressWarnings("serial")
public class LunchDetailView extends JDialog {
	private JLabel jlLunchImg;
	private JTextField jtfLunchCode, jtfLunchName, jtfLunchDate, jtfLunchPrice;
	private JTextArea jtaLunchSpec;
	private JButton jbImg, jbUpdate, jbDelete, jbEnd;
	
	public LunchDetailView(LunchMainView lmv, LunchDetailVO ldvo, LunchMainController lmc) {
		super(lmv,"���ö� �� ����", true); //���
		
		//DB���� ��ȸ�� ����� Component���� ä���.
		ImageIcon iiLunch = new ImageIcon("C:/dev/workspace/lunch_prj/src/kr/co/sist/lunch/admin/img/"+ldvo.getImg());
		jlLunchImg = new JLabel(iiLunch);
		
		jtfLunchCode = new JTextField();
		jtfLunchName = new JTextField();
		jtfLunchDate = new JTextField();
		jtfLunchPrice = new JTextField();
		
		jtaLunchSpec = new JTextArea();
		
		//DB���� ��ȸ�� �� ����
		jtfLunchCode.setText(ldvo.getLunch_code());
		jtfLunchName.setText(ldvo.getLunch_name());
		jtfLunchDate.setText(ldvo.getInput_date());
		jtfLunchPrice.setText(String.valueOf(ldvo.getPrice()));
		jtaLunchSpec.setText(ldvo.getSpec());
		
		jbImg = new JButton("�̹��� ����");
		jbUpdate = new JButton("����");
		jbDelete = new JButton("����");
		jbEnd = new JButton("�ݱ�");
		
		JScrollPane jspTaSpec = new JScrollPane(jtaLunchSpec);
		
		setLayout(null);
		
		JLabel jlDetailTitle = new JLabel("���ö� ������");
		jlDetailTitle.setFont(new Font("Dialog", Font.BOLD, 25));
		
		JLabel jlLunchCode = new JLabel("�ڵ�");
		JLabel jlLunchName = new JLabel("���ö���");
		JLabel jlLunchPrice = new JLabel("����");
		JLabel jlLunchDate = new JLabel("�Է���");
		JLabel jlLunchSpec = new JLabel("Ư����");
		
		//��ġ
		jlDetailTitle.setBounds(10, 25, 250, 30);
		jlLunchImg.setBounds(10, 65, 244, 220);
		jbImg.setBounds(80,290,105,25);
		
		jlLunchCode.setBounds(270,65,80,25);
		jlLunchName.setBounds(270,95,80,25);
		jlLunchPrice.setBounds(270,125,80,25);
		jlLunchDate.setBounds(270,155,80,25);
		jlLunchSpec.setBounds(270,185,80,25);
		
		jtfLunchCode.setBounds(340,65,185,25);
		jtfLunchCode.setEditable(false);
		jtfLunchCode.setBackground(Color.WHITE);
		
		jtfLunchName.setBounds(340,95,185,25);
		jtfLunchPrice.setBounds(340,125,185,25);
		
		jtfLunchDate.setBounds(340,155,185,25);
		jtfLunchDate.setEditable(false);
		jtfLunchDate.setBackground(Color.WHITE);
		
		jspTaSpec.setBounds(340,185,185,100);
		
		jbUpdate.setBounds(230,360,80,30);
		jbDelete.setBounds(320,360,80,30);
		jbEnd.setBounds(410,360,80,30);
		
		add(jlDetailTitle);
		add(jlLunchImg);
		add(jbImg);
		add(jlLunchCode);
		add(jlLunchName);
		add(jlLunchPrice);
		add(jlLunchDate);
		add(jlLunchSpec);
		add(jtfLunchCode);
		add(jtfLunchName);
		add(jtfLunchDate);
		add(jtfLunchPrice);
		add(jspTaSpec);
		add(jbUpdate);
		add(jbDelete);
		add(jbEnd);
		
		LunchDetailController ldc = new LunchDetailController(this,lmc, ldvo.getImg());
		addWindowListener(ldc);
		
		jbImg.addActionListener(ldc);
		jbUpdate.addActionListener(ldc);
		jbDelete.addActionListener(ldc);
		jbEnd.addActionListener(ldc);
		
		setBounds(lmv.getX()+100,lmv.getY()+50,550,500);
		setVisible(true);
		//���̾�α׸� ������ ������d JFrame�� �ٸ��� DISPOSE_ON_CLOSE�� �ݾƾ��Ѵ�.
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}//LunchDetailView

	public JLabel getJlLunchImg() {
		return jlLunchImg;
	}

	public JTextField getJtfLunchCode() {
		return jtfLunchCode;
	}

	public JTextField getJtfLunchName() {
		return jtfLunchName;
	}

	public JTextField getJtfLunchDate() {
		return jtfLunchDate;
	}

	public JTextField getJtfLunchPrice() {
		return jtfLunchPrice;
	}

	public JTextArea getJtaLunchSpec() {
		return jtaLunchSpec;
	}

	public JButton getJbImg() {
		return jbImg;
	}

	public JButton getJbUpdate() {
		return jbUpdate;
	}

	public JButton getJbDelete() {
		return jbDelete;
	}

	public JButton getJbEnd() {
		return jbEnd;
	}
	
}//class
