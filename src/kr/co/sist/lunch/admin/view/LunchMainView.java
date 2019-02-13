package kr.co.sist.lunch.admin.view;

import java.awt.BorderLayout;
import java.util.Calendar;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import kr.co.sist.lunch.admin.controller.LunchMainController;

@SuppressWarnings("serial")
public class LunchMainView extends JFrame{
	private JTabbedPane jtb;
	private DefaultTableModel dtmLunch, dtmOrder, dtmCalc;
	private JButton jbtAddLunch, jbtCalcOrder;
	private JComboBox<Integer> jcbYear, jcbMonth, jcbDay;
	private JTable jtLunch, jtOrder;
	private DefaultComboBoxModel<Integer> cbmYear, cbmMonth, cbmDay;
	private Calendar cal;
	
	public static String adminId;
	
	private JPopupMenu jpOrderMenu;
	private JMenuItem jmOrderRemove, jmOrderStatus;
	
	public LunchMainView(String adminName) {
		super("���ö� ���� [�α��� ���� : "+adminName+"]");
		
		cal = Calendar.getInstance();
		
		jtb = new JTabbedPane();
		//���ö�
		String[] lunchColumns = {"��ȣ", "���ö��ڵ�", "�̹���", "���ö���", "����"};
		
		//cell ���� ������ �������� ���ϵ���
		dtmLunch = new DefaultTableModel(lunchColumns, 4) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}//isCellEditable
		};
		
		//�̹����� ���̰� �ϱ� ���� Ŭ����
		jtLunch = new JTable(dtmLunch) {
			@Override
			public Class getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}//getColumnClass
		};
		
		
		//���ö� ���̺� ũ�� ���� : ��ü80, �̹��� w(122) x h(110)
		jtLunch.getColumnModel().getColumn(0).setPreferredWidth(80);
		jtLunch.getColumnModel().getColumn(1).setPreferredWidth(120);
		jtLunch.getColumnModel().getColumn(2).setPreferredWidth(125);
		jtLunch.getColumnModel().getColumn(3).setPreferredWidth(265);
		jtLunch.getColumnModel().getColumn(4).setPreferredWidth(220);
		
		//���̺��� ����
		jtLunch.setRowHeight(110);
		
		//����
		String[] calcColumns = {"��ȣ", "���ö���(���ö��ڵ�)", "����", "����"};
		dtmCalc = new DefaultTableModel(calcColumns, 4) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}//isCellEditable
		};
		JTable jtCalc = new JTable(dtmCalc); //�̺�Ʈ �߻� ���ؼ� ���� �Ȼ����ȴ�.
		
		//���� ���̺� �÷� ���� ���� : ��ü 800,
		jtCalc.getColumnModel().getColumn(0).setPreferredWidth(100);
		jtCalc.getColumnModel().getColumn(1).setPreferredWidth(400);
		jtCalc.getColumnModel().getColumn(2).setPreferredWidth(150);
		jtCalc.getColumnModel().getColumn(3).setPreferredWidth(150);
		
		//�������̺� ���� ����
		jtCalc.setRowHeight(25);
		
		//�ֹ�
		String[] orderColumns = {"��ȣ", "�ֹ���ȣ", "���ö��ڵ�", "���ö���", "�ֹ��ڸ�", "����", "����", "�ֹ���", "����ó", "�ֹ���ip", "���ۻ���"};
		dtmOrder = new DefaultTableModel(orderColumns, 4) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}//isCellEditable
		};
		jtOrder = new JTable(dtmOrder);
		
		
		//�ֹ� ���̺� �÷� ���� ���� : ��ü 800
		jtOrder.getColumnModel().getColumn(0).setPreferredWidth(30);
		jtOrder.getColumnModel().getColumn(1).setPreferredWidth(105);
		jtOrder.getColumnModel().getColumn(2).setPreferredWidth(70);
		jtOrder.getColumnModel().getColumn(3).setPreferredWidth(70);
		jtOrder.getColumnModel().getColumn(4).setPreferredWidth(55);
		jtOrder.getColumnModel().getColumn(5).setPreferredWidth(30);
		jtOrder.getColumnModel().getColumn(6).setPreferredWidth(50);
		jtOrder.getColumnModel().getColumn(7).setPreferredWidth(130);
		jtOrder.getColumnModel().getColumn(8).setPreferredWidth(100);
		jtOrder.getColumnModel().getColumn(9).setPreferredWidth(100);
		jtOrder.getColumnModel().getColumn(10).setPreferredWidth(60);
		//���̺��� ����
		jtOrder.setRowHeight(23);
		
		jbtAddLunch = new JButton("���ö� �߰�");
		jbtCalcOrder = new JButton("����");
		
		cbmYear = new DefaultComboBoxModel<Integer>();
		jcbYear = new JComboBox<Integer>(cbmYear);
		
		cbmMonth = new DefaultComboBoxModel<Integer>();
		jcbMonth = new JComboBox<Integer>(cbmMonth);
		
		cbmDay = new DefaultComboBoxModel<Integer>();
		jcbDay = new JComboBox<Integer>(cbmDay);
		
		JScrollPane jspLunch = new JScrollPane(jtLunch);
		jspLunch.setBorder(new TitledBorder("���ö����"));
		JScrollPane jspOrder = new JScrollPane(jtOrder);
		jspOrder.setBorder(new TitledBorder("�ֹ����"));
		JScrollPane jspCalc = new JScrollPane(jtCalc);
		jspCalc.setBorder(new TitledBorder("����"));
		
		//ó�� �ǿ� �� ������Ʈ ��ġ
		JPanel jpLunch = new JPanel();
		jpLunch.setLayout(new BorderLayout());

		JPanel jpLunchNorth = new JPanel();
		jpLunchNorth.add(jbtAddLunch);
		jpLunch.add("Center", jspLunch);
		jpLunch.add("North", jpLunchNorth);
		
		jtb.add("���ö�", jpLunch);
		
		//�ι�° �ǿ� �� ������Ʈ ��ġ
		JPanel jpOrder = new JPanel();
		jpOrder.setLayout(new BorderLayout());
		jpOrder.add(jspOrder);
		
		jtb.add("�ֹ�", jpOrder);

		//����° �ǿ� �� ������Ʈ ��ġ
		JPanel jpCalc = new JPanel();
		jpCalc.setLayout(new BorderLayout());
		
		JPanel jpCalcNorth = new JPanel();
		jpCalcNorth.setBorder(new TitledBorder("�������� ����"));
		jpCalcNorth.add(jcbYear);
		jpCalcNorth.add(new JLabel("��"));
		jpCalcNorth.add(jcbMonth);
		jpCalcNorth.add(new JLabel("��"));
		jpCalcNorth.add(jcbDay);
		jpCalcNorth.add(new JLabel("��"));
		jpCalcNorth.add(jbtCalcOrder);
		
		jpCalc.add("North", jpCalcNorth);
		jpCalc.add("Center", jspCalc);
		
		jtb.addTab("����", jpCalc);
		
		jpOrderMenu = new JPopupMenu();
		jmOrderRemove = new JMenuItem("�ֹ� ����");
		jmOrderStatus = new JMenuItem("���� �Ϸ�");
		
		jpOrderMenu.add(jmOrderStatus);
		jpOrderMenu.addSeparator();
		jpOrderMenu.add(jmOrderRemove);
		
		//���� �����ӿ� ��ġ
		add("Center", jtb);
		
		setYear(); //JCB Year ����
		setMonth(); //JCB Month ����
		setDay(); //JCB Day ����
		
		LunchMainController lmc = new LunchMainController(this);
		addWindowListener(lmc);
		jtb.addMouseListener(lmc); //�ǿ��� �̺�Ʈ�� �߻�������
		jtLunch.addMouseListener(lmc);
		jtOrder.addMouseListener(lmc);
		
		jbtAddLunch.addActionListener(lmc);
		jbtCalcOrder.addActionListener(lmc);
		
		jcbMonth.addActionListener(lmc);
		
		jmOrderRemove.addActionListener(lmc);
		jmOrderStatus.addActionListener(lmc);
		
		setBounds(100,100, 900,600);
		setVisible(true);
		
	}//LunchMainView
	
	private void setYear() {//����⵵�� 4�� ������ ��������
		int year = cal.get(Calendar.YEAR);
		for(int temp =0; temp<4; temp++) {
			cbmYear.addElement(year-temp);
		}//end for
		jcbYear.setSelectedItem(new Integer(year));
	}//setYear
	
	private void setMonth() {//���� 1~12��
		int now_month = cal.get(Calendar.MONTH)+1;
		for(int month=1; month<13; month++) {
			cbmMonth.addElement(month);
		}//end for
		jcbMonth.setSelectedItem(new Integer(now_month));
	}//setYear
	
	private void setDay() {//���� ��������
		int lastDay = cal.getActualMaximum(Calendar.DATE);
		int nowDay = cal.get(Calendar.DAY_OF_MONTH);
		for(int day=1; day<lastDay+1; day++) {
			cbmDay.addElement(day);
		}
		jcbDay.setSelectedItem(new Integer(nowDay));
	}//setYear

	public JTabbedPane getJtb() {
		return jtb;
	}

	public DefaultTableModel getDtmLunch() {
		return dtmLunch;
	}

	public DefaultTableModel getDtmOrder() {
		return dtmOrder;
	}

	public DefaultTableModel getDtmCalc() {
		return dtmCalc;
	}

	public JButton getJbtAddLunch() {
		return jbtAddLunch;
	}

	public JButton getJbtCalcOrder() {
		return jbtCalcOrder;
	}

	public JComboBox<Integer> getJcbYear() {
		return jcbYear;
	}

	public JComboBox<Integer> getJcbMonth() {
		return jcbMonth;
	}

	public JComboBox<Integer> getJcbDay() {
		return jcbDay;
	}

	public JTable getJtLunch() {
		return jtLunch;
	}

	public JTable getJtOrder() {
		return jtOrder;
	}

	public DefaultComboBoxModel<Integer> getCbmYear() {
		return cbmYear;
	}

	public DefaultComboBoxModel<Integer> getCbmMonth() {
		return cbmMonth;
	}

	public DefaultComboBoxModel<Integer> getCbmDay() {
		return cbmDay;
	}

	public JPopupMenu getJpOrderMenu() {
		return jpOrderMenu;
	}

	public void setJpOrderMenu(JPopupMenu jpOrderMenu) {
		this.jpOrderMenu = jpOrderMenu;
	}

	public JMenuItem getJmOrderRemove() {
		return jmOrderRemove;
	}

	public void setJmOrderRemove(JMenuItem jmOrderRemove) {
		this.jmOrderRemove = jmOrderRemove;
	}

	public JMenuItem getJmOrderStatus() {
		return jmOrderStatus;
	}

	public void setJmOrderStatus(JMenuItem jmOrderStatus) {
		this.jmOrderStatus = jmOrderStatus;
	}
	
}//class