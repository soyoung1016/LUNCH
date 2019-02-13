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
		
		//도시락 목록을 설정한다.
		setLunch();
		orderNum="";
	}//LunchMainController
	
	/**
	 * JTable에 DB에서 조회한 도시락 정보를 보여준다.
	 */
	public void setLunch() {
		DefaultTableModel dtmLunch = lmv.getDtmLunch();
		dtmLunch.setRowCount(0);;
		
		try {
			//DB에서 도시락 정보를 조회
			List<LunchVO> listLunch = la_dao.selectLunch();
			
			//JTable에 조회한 정보를 출력
			LunchVO lv = null;
			String imgPath = "C:/dev/workspace/lunch_prj/src/kr/co/sist/lunch/admin/img/s_";
			
			Object[] rowData = null;
			for(int i=0; i<listLunch.size(); i++) {
				lv = listLunch.get(i);
				
				//DTM에 데이터를 추가하기 위한 일차원배열(Vector)을 생성
				rowData = new Object[5];
				rowData[0] = new Integer(i+1);	//기본형이 object에 들어가는게 원래는 안되지만 java가 알아서 autoboxing해준다
				rowData[1] = lv.getLunchCode();
				rowData[2] = new ImageIcon(imgPath+lv.getImg());
				rowData[3] = lv.getLunchName();
				rowData[4] = new Integer(lv.getPrice());
				
				//DTM에 추가
				dtmLunch.addRow(rowData);
			}//end for
			
			if(listLunch.isEmpty()) { //입력된 도시락이 없을 때
				JOptionPane.showMessageDialog(lmv, "입력된 제품이 없습니다.");
			}//end if
			
		} catch (SQLException se) {
			JOptionPane.showMessageDialog(lmv, "DB에서 데이터를 받아오는 중 문제 발생...");
			se.printStackTrace();
		}//end catch
		
	}//setLunch
	
	@Override
	public void windowClosing(WindowEvent we) {
		lmv.dispose();
	}//windowClosing
	
	@Override
	public void windowClosed(WindowEvent e) {
		System.exit(0); //JVM의 모든 인스턴스 종료
	}//windowClosed
	
	private void searchOrder() {
		try {
			List<OrderVO> list = la_dao.selectOrderList();
			DefaultTableModel dtm = lmv.getDtmOrder();
			dtm.setRowCount(0);//초기화
			
			//이번엔 Object말고 Vector로~!
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
				
				//추가
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
			if(lmv.getJtb().getSelectedIndex()==1) {//두번째 탭(주문)에서 이벤트 발생
				//실시간으로 DB를 조회하여 주문현황을 계속 갱신
				if(threadOrdering == null) {
					threadOrdering = new Thread(this);
					threadOrdering.start();
				}//end if
				
//				//현재까지의 주문 사항을 조회
//				searchOrder();
			}//end if
		}//end if
		
		if(me.getSource() == lmv.getJtOrder() && me.getButton()==MouseEvent.BUTTON3) {
			JTable jt = lmv.getJtOrder();
			
			//마우스 포인터가 클릭 되면 테이블에서 클릭한 행을 가져오는 일
			int r = jt.rowAtPoint(me.getPoint());
			
		    if (r >= 0 && r < jt.getRowCount()) {
		    jt.setRowSelectionInterval(r, r); //시작행과 끝행 사이의 행을 선택하는 일 //같은걸 넣음으로서 그 행을 선택하게함
		    } else {
		    	jt.clearSelection();
		    }//end else
		    //선택한 행을 넣는다.
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
			if(me.getSource() == lmv.getJtLunch()) {//도시락 테이블에서 더블클릭이 되면
				//도시락 코드로 DB Table을 검색하여 상세정보를 전달한다.
				JTable jt = lmv.getJtLunch();
				try {
					LunchDetailVO ldvo = la_dao.selectDetailLunch((String)jt.getValueAt(jt.getSelectedRow(), 1));
					new LunchDetailView(lmv,ldvo,this);
				} catch (SQLException e) {
					e.printStackTrace();
				}//end catch
//			} else if(me.getSource() == lmv.getJtOrder()) {
//				if(la_dao.selectOrderList()) {
//					System.out.println("널");
//				} else {
//					System.out.println("not null");
//				}
//			}
			}
		}//end switch
			
	}//mouseClicked


	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == lmv.getJbtAddLunch()) { //도시락 추가 버튼
			new LunchAddView(lmv,this);
		}//end if
		if(ae.getSource() == lmv.getJcbMonth()) {//월별 마지막 일자 변경
			setDay();
		}//end if
		
		if(ae.getSource() == lmv.getJbtCalcOrder()) {//정산 버튼 클릭
			searchCalc();
		}//end if
		
		if(ae.getSource() == lmv.getJmOrderRemove()) {
			//제작상태가 'N'인 상태에서만 동작
			JTable jt = lmv.getJtOrder();
			if(((String)jt.getValueAt(selectedRow, 10)).equals("N")) { //제작상태가 'N'인 상태에서만 동작
				switch(JOptionPane.showConfirmDialog(lmv, "[ "+orderNum+lunchName+" ] 주문정보를 삭제하시겠습니까?")) {
				case JOptionPane.OK_OPTION :
					try {
						if(la_dao.deleteOrder(orderNum)) { //DB Table의 해당 레코드 삭제
							lmv.getJpOrderMenu().setVisible(false);
							msgCenter(lmv, "주문이 삭제되었습니다.");
							//테이블 갱신
							searchOrder();
						} else {
							JOptionPane.showMessageDialog(lmv, "도시락 주문 삭제에 실패하였습니다.");
						}//end else
					} catch (SQLException se) {
						lmv.getJpOrderMenu().setVisible(false);
						JOptionPane.showMessageDialog(lmv, "DB에서 문제가 발생하였습니다.");
						se.printStackTrace();
					}//end catch
				}//end switch
			} else {
				lmv.getJpOrderMenu().setVisible(false);
				JOptionPane.showMessageDialog(lmv, "제작된 도시락은 삭제할 수 없습니다.");
			}//end else
		}//end if
		
		if(ae.getSource() == lmv.getJmOrderStatus()) {
			JTable jt = lmv.getJtOrder();
			JPopupMenu jp = lmv.getJpOrderMenu();
			if(((String)jt.getValueAt(selectedRow, 10)).equals("N")) { //제작상태가 'N'인 상태에서만 동작
				switch(JOptionPane.showConfirmDialog(lmv, "[ "+orderNum+lunchName+" ] 주문이 완료 되었습니까?")) {
				case JOptionPane.OK_OPTION :
					//DB Table의 해당 레코드 변경
					try {
						if(la_dao.updateStatus(orderNum)) {//상태변환 성공
							jt.setValueAt("Y", selectedRow, 10); //테이블의 값만 변경
							JOptionPane.showMessageDialog(lmv, "도시락 제작이 완료되었습니다.");
						} else {//상태변환 실패
							JOptionPane.showMessageDialog(lmv, "도시락 제작상태 변환이 실패하였습니다.");
						}//end else
					} catch (SQLException se) {
						JOptionPane.showMessageDialog(lmv, "DB에서 문제가 발생하였습니다.");
						se.printStackTrace();
					}//end catch
				}//end switch
			} else {
				JOptionPane.showMessageDialog(lmv, "이미 제작이 완료된 도시락입니다.");
			}//end else
			jp.setVisible(false); //popup메뉴 숨김
		}//end if
		
	}//actionPerformed
	
	/**
	 * 년, 월, 일 정보를 가져와서 정산
	 */
	private void searchCalc() {
		int selYear = ((Integer)lmv.getJcbYear().getSelectedItem()).intValue();
		int selMonth = ((Integer)lmv.getJcbMonth().getSelectedItem()).intValue();
		int selDay = ((Integer)lmv.getJcbDay().getSelectedItem()).intValue();
		
		StringBuilder searchDate = new StringBuilder();
		searchDate.append(selYear).append("-").append(selMonth).append("-").append(selDay);

		try {
			//선택한 일자의 조회 결과를 받아서 JTable에 출력
			List<CalcVO> list = la_dao.selectCalc(searchDate.toString());
			
			//JTable에 데이터를 추가하는 코드를 작성해보세요.
			//데이터가 없는 날에는 메세지 출력
			DefaultTableModel dtm = lmv.getDtmCalc();
			dtm.setRowCount(0);//데이터를 출력하기 전에 초기화
			
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
				JOptionPane.showMessageDialog(lmv, "데이터가 없습니다.");
			}//end if
			
		} catch (SQLException e) {
			e.printStackTrace();
		}//end catch
		
	}//searchCalc
	
	/**
	 * 월이 선택되면 해당 연의 해당 월의 마지막 날을 설정
	 */
	private void setDay() {
		int selYear = ((Integer)lmv.getJcbYear().getSelectedItem()).intValue();
		int selMonth = ((Integer)lmv.getJcbMonth().getSelectedItem()).intValue();
		
		//마지막날 얻기
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, selYear);
		cal.set(Calendar.MONTH, selMonth-1);
		
		int lastDay = cal.getActualMaximum(Calendar.DATE);
		int nowDay = cal.get(Calendar.DAY_OF_MONTH);
		
		lmv.getCbmDay().removeAllElements(); //모델을 초기화하고
		
		for(int day=1; day<lastDay+1; day++) {
			lmv.getCbmDay().addElement(day); //마지막날을 설정한다. 그래야 1월 누르고 2월 눌러도 일에 1,2월 둘 다 뜨지 않게 된다.
		}
//		lmv.getCbmDay().setSelectedItem(new Integer(nowDay)); //오늘을 선택한다
		
	}//setDay
	
	@Override
	public void run() {
		//30초마다 한번씩 조회 수행
		try {
			while(true) {
				searchOrder();
				Thread.sleep(1000*30);
			}//end while
		} catch (InterruptedException ie) {
			msgCenter(lmv, "주문 조회 중 문제가 발생하였습니다.");
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
