package kr.co.sist.lunch.user.view;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import kr.co.sist.lunch.user.controller.LunchClientController;

@SuppressWarnings("serial")
public class LunchClientVIew extends JFrame {
	private JTabbedPane jtp;
	private DefaultTableModel dtmLunchList, dtmOrderList;
	private JTextField jtfName, jtfTel;
	private JButton jbtSearch;
	private JTable jtLunch, jtOrder;
	
	public LunchClientVIew(){
		super("���� ���ö� �ֹ�");
		jtp = new JTabbedPane();
		
		//���ö� ����Ʈ
		String[] lunchColumns = {"��ȣ", "�̹���","���ö��ڵ�", "���ö���","����"};
		dtmLunchList = new DefaultTableModel(lunchColumns, 4) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}//isCellEditable
		};
		
		jtLunch = new JTable(dtmLunchList) {
			@Override
			public Class<?> getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}//getColumnClass
		};
		
		jtLunch.getColumnModel().getColumn(0).setPreferredWidth(80);
		jtLunch.getColumnModel().getColumn(1).setPreferredWidth(130);
		jtLunch.getColumnModel().getColumn(2).setPreferredWidth(100);
		jtLunch.getColumnModel().getColumn(3).setPreferredWidth(120);
		jtLunch.getColumnModel().getColumn(4).setPreferredWidth(370);
		
		//���̺��� �� ���� ����
		jtLunch.setRowHeight(110);
		
		//�ֹ���Ȳ
		String[] orderColumns = {"��ȣ", "���ö���", "�ֹ�����","����"};
		dtmOrderList = new DefaultTableModel(orderColumns, 4) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}//isCellEditable
		};
		
		jtOrder = new JTable(dtmOrderList);
		jtOrder.getColumnModel().getColumn(0).setPreferredWidth(100);
		jtOrder.getColumnModel().getColumn(1).setPreferredWidth(250);
		jtOrder.getColumnModel().getColumn(2).setPreferredWidth(350);
		jtOrder.getColumnModel().getColumn(3).setPreferredWidth(100);
		
		jtOrder.setRowHeight(25);
		
		jtfName = new JTextField(10);
		jtfTel = new JTextField(10);
		
		jbtSearch = new JButton("��ȸ");
		
		JScrollPane jspLunch = new JScrollPane(jtLunch);
		jspLunch.setBorder(new TitledBorder("���ö� ���"));
		
		jtp.add("���ö� ���", jspLunch);
		
		JPanel panelOrder = new JPanel();
		panelOrder.setLayout(new BorderLayout());
		
		JPanel panelOrderNorth = new JPanel();
		panelOrderNorth.setBorder(new TitledBorder("�ֹ��� ����"));
		panelOrderNorth.add(new JLabel("�ֹ��ڸ�"));
		panelOrderNorth.add(jtfName);
		panelOrderNorth.add(new JLabel("��ȭ��ȣ"));
		panelOrderNorth.add(jtfTel);
		panelOrderNorth.add(jbtSearch);
		
		JScrollPane jspOrder = new JScrollPane(jtOrder);
		jspOrder.setBorder(new TitledBorder("�ֹ���Ȳ"));
		
		panelOrder.add("North", panelOrderNorth);
		panelOrder.add(jspOrder);
		
		jtp.add("�ֹ������ȸ", panelOrder);
		
		LunchClientController lcc = new LunchClientController(this);
		addWindowListener(lcc);
		
		jtLunch.addMouseListener(lcc);
		jbtSearch.addActionListener(lcc);
		
		jtfName.addActionListener(lcc);
		jtfTel.addActionListener(lcc);
		
		jtp.addMouseListener(lcc);
		
		add("Center", jtp);
		setBounds(100,100,800,600);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}//LunchClientVIew

	public JTabbedPane getJtp() {
		return jtp;
	}

	public DefaultTableModel getDtmLunchList() {
		return dtmLunchList;
	}

	public DefaultTableModel getDtmOrderList() {
		return dtmOrderList;
	}

	public JTextField getJtfName() {
		return jtfName;
	}

	public JTextField getJtfTel() {
		return jtfTel;
	}

	public JButton getJbtSearch() {
		return jbtSearch;
	}

	public JTable getJtLunch() {
		return jtLunch;
	}

	public JTable getJtOrder() {
		return jtOrder;
	}
	
}//class
