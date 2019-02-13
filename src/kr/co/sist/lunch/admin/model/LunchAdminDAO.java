package kr.co.sist.lunch.admin.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.sist.lunch.admin.view.LunchMainView;
import kr.co.sist.lunch.admin.vo.AdminLoginVO;
import kr.co.sist.lunch.admin.vo.CalcVO;
import kr.co.sist.lunch.admin.vo.LunchAddVO;
import kr.co.sist.lunch.admin.vo.LunchDetailVO;
import kr.co.sist.lunch.admin.vo.LunchUpdateVO;
import kr.co.sist.lunch.admin.vo.LunchVO;
import kr.co.sist.lunch.admin.vo.OrderVO;

public class LunchAdminDAO {
	private static LunchAdminDAO la_dao;
	
	private LunchAdminDAO() {
		//1. 드라이버 로딩
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}//end catch
	}//LunchAdminDAO
	
	public static LunchAdminDAO getInstance() {
		if(la_dao == null) {
			la_dao = new LunchAdminDAO();
		}//end if
		return la_dao;
	}//getInstance
	
	private Connection getConn() throws SQLException {
		//2.
		String url = "jdbc:oracle:thin:@211.63.89.141:1521:orcl";
		String id = "scott";
		String pass = "tiger";
		Connection con = DriverManager.getConnection(url, id, pass);
		
		return con;
	}//getConn
	
	/**
	 * ID와 Password를 입력받아 lunch_admin에서 이름을 조회하는 일
	 * @param alvo
	 * @return
	 * @throws SQLException
	 */
	public String login(AdminLoginVO alvo) throws SQLException{
		String adminName = "";
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
		//1.
		//2.
			con = getConn();
		//3.
			String selectName = "SELECT NAME FROM LUNCH_ADMIN WHERE ID=? AND PASS=?";
			pstmt = con.prepareStatement(selectName);
		//4.
			pstmt.setString(1, alvo.getId());
			pstmt.setString(2, alvo.getPass());
		//5.
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				adminName = rs.getString("NAME");
			}//end if
			
		} finally {
		//6.
			if(rs != null) {
				rs.close();
			}//end if
			if(pstmt != null) {
				pstmt.close();
			}//end if
			if(con != null) {
				con.close();
			}//end if
		}//end finally
		
		return adminName;
	}//login
	
	/**
	 * 입력된 도시락의 코드, 이미지, 도시락명, 단가 조회
	 * @return
	 * @throws SQLException
	 */
	public List<LunchVO> selectLunch() throws SQLException {
		List<LunchVO> list = new ArrayList<LunchVO>();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
		//1.
		//2.
			con = getConn();
		//3.
			String selectAllLunch = "SELECT LUNCH_CODE,LUNCH_NAME,IMG,PRICE FROM LUNCH ORDER BY LUNCH_CODE DESC";
			pstmt = con.prepareStatement(selectAllLunch);
		//4.
		//5.
			rs = pstmt.executeQuery();
			
			LunchVO lv = null;
			while(rs.next()) {
				lv = new LunchVO(rs.getString("LUNCH_CODE"), rs.getString("LUNCH_NAME"), rs.getString("IMG"), rs.getInt("PRICE"));
				list.add(lv);
			}//end while
			
		} finally {
		//6.
			if(rs != null) {
				rs.close();
			}//end if
			if(pstmt != null) {
				pstmt.close();
			}//end if
			if(con != null) {
				con.close();
			}//end if
		}//end finally
		
		return list;
	}//selectLunch
	
	/**
	 * 입력되는 코드에 의한 도시락 상세정보 조회
	 * @param code
	 * @return
	 * @throws SQLException
	 */
	public LunchDetailVO selectDetailLunch(String code) throws SQLException {
		LunchDetailVO ldvo = null;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
		//1.
		//2.
			con = getConn();
		//3.
			String selectLunch = "SELECT LUNCH_NAME,IMG,PRICE,SPEC,TO_CHAR(INPUT_DATE,'YYYY-MM-DD') INPUT_DATE FROM LUNCH WHERE LUNCH_CODE=?";
			pstmt = con.prepareStatement(selectLunch);
		//4.
			pstmt.setString(1, code);
		//5.
			rs = pstmt.executeQuery();
			//입력된 코드로 조회된 레코드가 존재할때 VO를 생성하고 값 추가
			if(rs.next()) {
				ldvo = new LunchDetailVO(code, rs.getString("lunch_name"), rs.getString("img"), rs.getString("spec"),
										rs.getString("input_date"), rs.getInt("price"));
			}//end if
			
		} finally {
		//6.
			if(rs != null) {
				rs.close();
			}//end if
			if(pstmt != null) {
				pstmt.close();
			}//end if
			if(con != null) {
				con.close();
			}//end if
		}//end finally
		
		return ldvo;
	}//selectDetailLunch
	
	/**
	 * 도시락 정보를 추가하는 일
	 * @param lav
	 * @throws SQLException
	 */
	public void insertLunch(LunchAddVO lav) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
		//1.
		//2.
			con = getConn();
		//3.
			StringBuilder insertLunch = new StringBuilder();
			insertLunch.append("insert into lunch(lunch_code,lunch_name,img,price,spec,id) ")
						.append("values(lunch_code,?,?,?,?,?)");
			
			pstmt = con.prepareStatement(insertLunch.toString());
		//4.
			pstmt.setString(1, lav.getLunch_name());
			pstmt.setString(2, lav.getImg());
			pstmt.setInt(3, lav.getPrice());
			pstmt.setString(4, lav.getSpec());
			pstmt.setString(5, LunchMainView.adminId);
		//5.
			pstmt.executeUpdate();
		} finally {
		//6.
			if(pstmt != null) {
				pstmt.close();
			}//end if
			if(con != null) {
				con.close();
			}//end if
		}//end finally
		
	}//insertLunch
	
	public boolean deleteLunch(String code) throws SQLException {
		boolean flag = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
		//1.
		//2.
			con = getConn();
		//3.
			String deleteQuery = "DELETE FROM LUNCH WHERE LUNCH_CODE=?";
			pstmt = con.prepareStatement(deleteQuery);
		//4.
			pstmt.setString(1, code);
		//5.
			int cnt = pstmt.executeUpdate();
			if(cnt == 1) {
				flag = true;
			}//end if
		} finally {
		//6.
			if(pstmt != null) {
				pstmt.close();
			}//end if
			if(con != null) {
				con.close();
			}//end if
		}//end finally
		
		return flag;
	}//deleteLunch
	
	/**
	 * 도시락 코드, 도시락명, 이미지, 가격, 특장점을 입력받아 도시락 코드에 해당하는 도시락을 변경
	 * 이미지가 empty("")라면 이미지는 변경하지 않는다.
	 * @param luvo
	 * @return
	 * @throws SQLException
	 */
	public boolean updateLunch(LunchUpdateVO luvo) throws SQLException {
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
		//1. 
		//2.
			con = getConn();
		//3.
			StringBuilder updateLunch = new StringBuilder();
			updateLunch.append("update lunch ")
						.append("set lunch_name=?, ")
						.append("price =?, spec=?");
			if(!luvo.getImg().equals("")) {
				updateLunch.append(", img=? ");
			}//end if
			updateLunch.append("where lunch_code=?");
			
			pstmt = con.prepareStatement(updateLunch.toString());
		//4.
			pstmt.setString(1, luvo.getLunch_name());
			pstmt.setInt(2, luvo.getPrice());
			pstmt.setString(3, luvo.getSpec());
			
			if(!luvo.getImg().equals("")) {
				pstmt.setString(4, luvo.getImg());
				pstmt.setString(5, luvo.getLunch_code());
			} else {
				pstmt.setString(4, luvo.getLunch_code());
			}//end else
			
			/*
			 * 변수, 연산자 이용해서 위의 코드를 줄인것
			int index=4;
			if(!luvo.getImg().equals("")) {
				pstmt.setString(index++, luvo.getImg());
			}
				pstmt.setString(index, luvo.getLunch_code());
			*/
			
		//5.
			int cnt = pstmt.executeUpdate();
			if(cnt == 1) {
				flag = true;
			}//end if
			
		} finally {
		//6.
			if(pstmt != null) {
				pstmt.close();
			}//end if
			if(con != null) {
				con.close();
			}//end if
		}//end finally
		
		return flag;
	}//updateLunch
	
	public List<CalcVO> selectCalc(String date) throws SQLException {
		List<CalcVO> list = new ArrayList<CalcVO>();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
		//1.
		//2.
			con = getConn();
		//3.
			StringBuilder selectCalc = new StringBuilder();
			selectCalc
			.append("	select l.lunch_code, l.lunch_name, sum(o.quan) as total, sum(o.quan)*l.price as price	")
			.append("	from lunch l, ordering o	")
			.append("	where l.lunch_code = o.lunch_code	")
			.append("	and to_char(o.order_date,'yyyy-mm-dd')=to_char(to_date(?,'yyyy-mm-dd'),'yyyy-mm-dd')	")
			.append("	and o.status='Y'	")
			.append("	group by l.lunch_code, l.lunch_name, l.price	")
			.append("	order by l.lunch_code	");
			
			pstmt = con.prepareStatement(selectCalc.toString());
		//4.
			pstmt.setString(1, date);
		//5.
			rs = pstmt.executeQuery();
			CalcVO cvo = null;
			
			while(rs.next()) {
				cvo = new CalcVO(rs.getString("lunch_code"), rs.getString("lunch_name"), rs.getInt("price"),
						rs.getInt("total"));
				list.add(cvo);
			}//end while
			
		} finally {
		//6.
			if(rs != null) {
				rs.close();
			}//end if
			if(pstmt != null) {
				pstmt.close();
			}//end if
			if(con != null) {
				con.close();
			}//end if
		}//end finally 
		
		return list;
	}//selectCalc
	
	/**
	 * 오늘의 13시 이전 주문 현황
	 * @return
	 * @throws SQLException
	 */
	public List<OrderVO> selectOrderList() throws SQLException{
		List<OrderVO> list = new ArrayList<OrderVO>();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
		//1.
		//2.
			con = getConn();
		//3.
			StringBuilder selectOrder = new StringBuilder();
			selectOrder
			.append("	select o.order_num, l.lunch_code, l.lunch_name, o.order_name, o.quan, l.price*o.quan price,	")
			.append("	to_char(o.order_date,'yyyy-mm-dd hh:mi:ss') order_date, o.phone, o.ip_address, o.status	")
			.append("	from lunch l, ordering o	")
			.append("	where o.lunch_code = l.lunch_code	")
			.append("	and to_char(order_date, 'yyyy-mm-dd')=to_char(sysdate,'yyyy-mm-dd')	")
//			.append("	and to_char(order_date, 'hh24') <=13	")
			.append("	order by o.order_num	");
			
			pstmt = con.prepareStatement(selectOrder.toString());
		//4.
		//5.
			rs = pstmt.executeQuery();
			OrderVO ovo = null;
			
			while(rs.next()) {
				ovo = new OrderVO(rs.getString("order_num"), rs.getString("lunch_code"), rs.getString("lunch_name"),
						rs.getString("order_name"), rs.getString("order_date"), rs.getString("phone"), 
						rs.getString("ip_address"), rs.getString("status"), rs.getInt("quan"), rs.getInt("price"));
				list.add(ovo);
			}//end while
			
		} finally {
		//6.
			if(rs != null) {
				rs.close();
			}//end if
			if(pstmt != null) {
				pstmt.close();
			}//end if
			if(con != null) {
				con.close();
			}//end if
		}//end finally
		
		return list;
	}//selectOrderList
	
	public boolean checkRequest(String orderNum) throws SQLException {
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = getConn();
			
			String checkRequest = "select request from ordering where order_num=?";
			pstmt = con.prepareStatement(checkRequest);
			pstmt.setString(1, orderNum);
			
			
			
			
		} finally {
			if(rs !=null) {
				rs.close();
			}//end if
			if(pstmt !=null) {
				pstmt.close();
			}//end if
			if(con !=null) {
				con.close();
			}//end if
		}//end finally
		
		return flag;
	}//checkRequest
	
	/**
	 * 도시락 제작 완료 시점에 호출되어 해당 주문 도시락의 완성 상태를 변경하는 일을 한다
	 * @param orderNum
	 * @return
	 */
	public boolean updateStatus(String orderNum) throws SQLException{
		boolean flag = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
		//1.
		//2.
			con = getConn();
		//3.
			String updateStatus = "update ordering set status='Y' where order_num=?";
			pstmt = con.prepareStatement(updateStatus);
		//4.
			pstmt.setString(1, orderNum);
		//5.
			int cnt = pstmt.executeUpdate();
			if(cnt == 1) {
				flag = true;
			}//end if
		} finally {
		//6.
			if(pstmt != null) {
				pstmt.close();
			}//end if
			if(con != null) {
				con.close();
			}//end if
		}//end finally
		
		return flag;
	}//updateStatus
	
	/**
	 * 주문된 도시락을 삭제하는 일
	 * @param orderNum
	 * @return
	 */
	public boolean deleteOrder(String orderNum) throws SQLException {
		boolean flag = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
		//1.
		//2.
			con = getConn();
		//3.
			String deleteOrder = "delete from ordering where order_num=?";
			pstmt = con.prepareStatement(deleteOrder);
		//4.
			pstmt.setString(1, orderNum);
		//5.
			int cnt = pstmt.executeUpdate();
			if(cnt == 1) {
				flag = true;
			}//end if
		} finally {
		//6.
			if(pstmt != null) {
				pstmt.close();
			}//end if
			if(con != null) {
				con.close();
			}//end if
		}//end finally
		
		return flag;
	}//deleteOrder
	
	
	
	public static void main(String[] args) {
		try {
			
			System.out.println(getInstance().selectOrderList());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}//main
	
}//class
