package kr.co.sist.lunch.admin.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import kr.co.sist.lunch.admin.model.LunchAdminDAO;
import kr.co.sist.lunch.admin.view.LunchAddView;
import kr.co.sist.lunch.admin.view.LunchDetailView;
import kr.co.sist.lunch.admin.view.LunchMainView;
import kr.co.sist.lunch.admin.vo.CalcVO;
import kr.co.sist.lunch.admin.vo.LunchDetailVO;
import kr.co.sist.lunch.admin.vo.LunchVO;
import kr.co.sist.lunch.admin.vo.OrderVO;
import kr.co.sist.lunch.user.vo.OrderAddVO;

public class LunchMainController extends WindowAdapter implements ActionListener, MouseListener, Runnable{
	
	private LunchMainView lmv;
	private LunchAdminDAO la_dao;
	public static final int DBL_CLICK = 2;
	private String orderNum;
	private String lunchName;
	private int selectedRow;
	private Thread threadOrdering;
	
	public LunchMainController(LunchMainView lmv) {
		this.lmv = lmv;
		la_dao = LunchAdminDAO.getInstance();
		
		//���ö� ����� �����Ѵ�.
		setLunch();
		orderNum="";
	}//LunchMainController
	
	/**
	 * JTable�� DB���� ��ȸ�� ���ö� ������ �����ش�.
	 */
	public void setLunch() {
		DefaultTableModel dtmLunch = lmv.getDtmLunch();
		dtmLunch.setRowCount(0);;
		
		try {
			//DB���� ���ö� ������ ��ȸ
			List<LunchVO> listLunch = la_dao.selectLunch();
			
			//JTable�� ��ȸ�� ������ ���
			LunchVO lv = null;
			String imgPath = "C:/dev/workspace/lunch_prj/src/kr/co/sist/lunch/admin/img/s_";
			
			Object[] rowData = null;
			for(int i=0; i<listLunch.size(); i++) {
				lv = listLunch.get(i);
				
				//DTM�� �����͸� �߰��ϱ� ���� �������迭(Vector)�� ����
				rowData = new Object[5];
				rowData[0] = new Integer(i+1);	//�⺻���� object�� ���°� ������ �ȵ����� java�� �˾Ƽ� autoboxing���ش�
				rowData[1] = lv.getLunchCode();
				rowData[2] = new ImageIcon(imgPath+lv.getImg());
				rowData[3] = lv.getLunchName();
				rowData[4] = new Integer(lv.getPrice());
				
				//DTM�� �߰�
				dtmLunch.addRow(rowData);
			}//end for
			
			if(listLunch.isEmpty()) { //�Էµ� ���ö��� ���� ��
				JOptionPane.showMessageDialog(lmv, "�Էµ� ��ǰ�� �����ϴ�.");
			}//end if
			
		} catch (SQLException se) {
			JOptionPane.showMessageDialog(lmv, "DB���� �����͸� �޾ƿ��� �� ���� �߻�...");
			se.printStackTrace();
		}//end catch
		
	}//setLunch
	
	@Override
	public void windowClosing(WindowEvent we) {
		lmv.dispose();
	}//windowClosing
	
	@Override
	public void windowClosed(WindowEvent e) {
		System.exit(0); //JVM�� ��� �ν��Ͻ� ����
	}//windowClosed
	
	private void searchOrder() {
		try {
			List<OrderVO> list = la_dao.selectOrderList();
			DefaultTableModel dtm = lmv.getDtmOrder();
			dtm.setRowCount(0);//�ʱ�ȭ
			
			//�̹��� Object���� Vector��~!
			Vector<Object> vec = null;
			OrderVO ovo = null;
			
			for(int i=0; i<list.size();i++) {
				ovo = list.get(i);
				vec = new Vector<Object>();
				vec.add(new Integer(i+1));
				vec.add(ovo.getOrderNum());
				vec.add(ovo.getLunchCode());
				vec.add(ovo.getLunchName());
				vec.add(ovo.getOrderName());
				vec.add(ovo.getQuan());
				vec.add(ovo.getPrice());
				vec.add(ovo.getOrderDate());
				vec.add(ovo.getPhone());
				vec.add(ovo.getIpAddress());
				vec.add(ovo.getStatus());
				
				//�߰�
				dtm.addRow(vec);
//				vec.clear();
				
			}//end for
			
		} catch (SQLException e) {
			e.printStackTrace();
		}//end catch
	}//searchOrder
	
	public void msgCenter(Component parentComponet, String message) {
		JOptionPane.showMessageDialog(parentComponet, message);
	}//msgCenter

	@Override
	public void mouseClicked(MouseEvent me) {
		if(me.getSource()==lmv.getJtb()) {
			if(lmv.getJtb().getSelectedIndex()==1) {//�ι�° ��(�ֹ�)���� �̺�Ʈ �߻�
				//�ǽð����� DB�� ��ȸ�Ͽ� �ֹ���Ȳ�� ��� ����
				if(threadOrdering == null) {
					threadOrdering = new Thread(this);
					threadOrdering.start();
				}//end if
				
//				//��������� �ֹ� ������ ��ȸ
//				searchOrder();
			}//end if
		}//end if
		
		if(me.getSource() == lmv.getJtOrder() && me.getButton()==MouseEvent.BUTTON3) {
			JTable jt = lmv.getJtOrder();
			
			//���콺 �����Ͱ� Ŭ�� �Ǹ� ���̺��� Ŭ���� ���� �������� ��
			int r = jt.rowAtPoint(me.getPoint());
			
		    if (r >= 0 && r < jt.getRowCount()) {
		    jt.setRowSelectionInterval(r, r); //������� ���� ������ ���� �����ϴ� �� //������ �������μ� �� ���� �����ϰ���
		    } else {
		    	jt.clearSelection();
		    }//end else
		    //������ ���� �ִ´�.
		    selectedRow=r;

			JPopupMenu jp = lmv.getJpOrderMenu();
			jp.setLocation(me.getXOnScreen(), me.getYOnScreen());
			jp.setVisible(true);
			
			orderNum = (String)jt.getValueAt(jt.getSelectedRow(), 1);
			lunchName = (String)jt.getValueAt(jt.getSelectedRow(), 3)+" "+
						(String)jt.getValueAt(jt.getSelectedRow(), 4);
			
		} else {
			JPopupMenu jp = lmv.getJpOrderMenu();
			jp.setVisible(false);
		}//end else
		
		switch(me.getClickCount()) {
		case DBL_CLICK :
			if(me.getSource() == lmv.getJtLunch()) {//���ö� ���̺��� ����Ŭ���� �Ǹ�
				//���ö� �ڵ�� DB Table�� �˻��Ͽ� �������� �����Ѵ�.
				JTable jt = lmv.getJtLunch();
				try {
					LunchDetailVO ldvo = la_dao.selectDetailLunch((String)jt.getValueAt(jt.getSelectedRow(), 1));
					new LunchDetailView(lmv,ldvo,this);
				} catch (SQLException e) {
					e.printStackTrace();
				}//end catch
//			} else if(me.getSource() == lmv.getJtOrder()) {
//				if(la_dao.selectOrderList()) {
//					System.out.println("��");
//				} else {
//					System.out.println("not null");
//				}
//			}
			}
		}//end switch
			
	}//mouseClicked


	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == lmv.getJbtAddLunch()) { //���ö� �߰� ��ư
			new LunchAddView(lmv,this);
		}//end if
		if(ae.getSource() == lmv.getJcbMonth()) {//���� ������ ���� ����
			setDay();
		}//end if
		
		if(ae.getSource() == lmv.getJbtCalcOrder()) {//���� ��ư Ŭ��
			searchCalc();
		}//end if
		
		if(ae.getSource() == lmv.getJmOrderRemove()) {
			//���ۻ��°� 'N'�� ���¿����� ����
			JTable jt = lmv.getJtOrder();
			if(((String)jt.getValueAt(selectedRow, 10)).equals("N")) { //���ۻ��°� 'N'�� ���¿����� ����
				switch(JOptionPane.showConfirmDialog(lmv, "[ "+orderNum+lunchName+" ] �ֹ������� �����Ͻðڽ��ϱ�?")) {
				case JOptionPane.OK_OPTION :
					try {
						if(la_dao.deleteOrder(orderNum)) { //DB Table�� �ش� ���ڵ� ����
							lmv.getJpOrderMenu().setVisible(false);
							msgCenter(lmv, "�ֹ��� �����Ǿ����ϴ�.");
							//���̺� ����
							searchOrder();
						} else {
							JOptionPane.showMessageDialog(lmv, "���ö� �ֹ� ������ �����Ͽ����ϴ�.");
						}//end else
					} catch (SQLException se) {
						lmv.getJpOrderMenu().setVisible(false);
						JOptionPane.showMessageDialog(lmv, "DB���� ������ �߻��Ͽ����ϴ�.");
						se.printStackTrace();
					}//end catch
				}//end switch
			} else {
				lmv.getJpOrderMenu().setVisible(false);
				JOptionPane.showMessageDialog(lmv, "���۵� ���ö��� ������ �� �����ϴ�.");
			}//end else
		}//end if
		
		if(ae.getSource() == lmv.getJmOrderStatus()) {
			JTable jt = lmv.getJtOrder();
			JPopupMenu jp = lmv.getJpOrderMenu();
			if(((String)jt.getValueAt(selectedRow, 10)).equals("N")) { //���ۻ��°� 'N'�� ���¿����� ����
				switch(JOptionPane.showConfirmDialog(lmv, "[ "+orderNum+lunchName+" ] �ֹ��� �Ϸ� �Ǿ����ϱ�?")) {
				case JOptionPane.OK_OPTION :
					//DB Table�� �ش� ���ڵ� ����
					try {
						if(la_dao.updateStatus(orderNum)) {//���º�ȯ ����
							jt.setValueAt("Y", selectedRow, 10); //���̺��� ���� ����
							JOptionPane.showMessageDialog(lmv, "���ö� ������ �Ϸ�Ǿ����ϴ�.");
						} else {//���º�ȯ ����
							JOptionPane.showMessageDialog(lmv, "���ö� ���ۻ��� ��ȯ�� �����Ͽ����ϴ�.");
						}//end else
					} catch (SQLException se) {
						JOptionPane.showMessageDialog(lmv, "DB���� ������ �߻��Ͽ����ϴ�.");
						se.printStackTrace();
					}//end catch
				}//end switch
			} else {
				JOptionPane.showMessageDialog(lmv, "�̹� ������ �Ϸ�� ���ö��Դϴ�.");
			}//end else
			jp.setVisible(false); //popup�޴� ����
		}//end if
		
	}//actionPerformed
	
	/**
	 * ��, ��, �� ������ �����ͼ� ����
	 */
	private void searchCalc() {
		int selYear = ((Integer)lmv.getJcbYear().getSelectedItem()).intValue();
		int selMonth = ((Integer)lmv.getJcbMonth().getSelectedItem()).intValue();
		int selDay = ((Integer)lmv.getJcbDay().getSelectedItem()).intValue();
		
		StringBuilder searchDate = new StringBuilder();
		searchDate.append(selYear).append("-").append(selMonth).append("-").append(selDay);

		try {
			//������ ������ ��ȸ ����� �޾Ƽ� JTable�� ���
			List<CalcVO> list = la_dao.selectCalc(searchDate.toString());
			
			//JTable�� �����͸� �߰��ϴ� �ڵ带 �ۼ��غ�����.
			//�����Ͱ� ���� ������ �޼��� ���
			DefaultTableModel dtm = lmv.getDtmCalc();
			dtm.setRowCount(0);//�����͸� ����ϱ� ���� �ʱ�ȭ
			
			Object[] rowData = null;
			rowData = new Object[4];
			for(int i=0; i<list.size(); i++) {
				rowData[0] = String.valueOf(i+1);
				rowData[1] = list.get(i).getLunchName();
				rowData[2] = list.get(i).getTotal();
				rowData[3] = list.get(i).getPrice();
				
				dtm.addRow(rowData);
			}//end for
			
			if(list.isEmpty()) {
				JOptionPane.showMessageDialog(lmv, "�����Ͱ� �����ϴ�.");
			}//end if
			
		} catch (SQLException e) {
			e.printStackTrace();
		}//end catch
		
	}//searchCalc
	
	/**
	 * ���� ���õǸ� �ش� ���� �ش� ���� ������ ���� ����
	 */
	private void setDay() {
		int selYear = ((Integer)lmv.getJcbYear().getSelectedItem()).intValue();
		int selMonth = ((Integer)lmv.getJcbMonth().getSelectedItem()).intValue();
		
		//�������� ���
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, selYear);
		cal.set(Calendar.MONTH, selMonth-1);
		
		int lastDay = cal.getActualMaximum(Calendar.DATE);
		int nowDay = cal.get(Calendar.DAY_OF_MONTH);
		
		lmv.getCbmDay().removeAllElements(); //���� �ʱ�ȭ�ϰ�
		
		for(int day=1; day<lastDay+1; day++) {
			lmv.getCbmDay().addElement(day); //���������� �����Ѵ�. �׷��� 1�� ������ 2�� ������ �Ͽ� 1,2�� �� �� ���� �ʰ� �ȴ�.
		}
//		lmv.getCbmDay().setSelectedItem(new Integer(nowDay)); //������ �����Ѵ�
		
	}//setDay
	
	@Override
	public void run() {
		//30�ʸ��� �ѹ��� ��ȸ ����
		try {
			while(true) {
				searchOrder();
				Thread.sleep(1000*30);
			}//end while
		} catch (InterruptedException ie) {
			msgCenter(lmv, "�ֹ� ��ȸ �� ������ �߻��Ͽ����ϴ�.");
			ie.printStackTrace();
		}//end while
	}//run

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void mousePressed(MouseEvent e) {}
	
	@Override
	public void mouseReleased(MouseEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent e) {}

	
}//class
