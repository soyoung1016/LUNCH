package kr.co.sist.lunch.user.vo;

public class OrderListVO {
	private String lunchName, orderData;
	private int quan;
	
	public OrderListVO(String lunchName, String orderData, int quan) {
		this.lunchName = lunchName;
		this.orderData = orderData;
		this.quan = quan;
	}
	
	public String getLunchName() {
		return lunchName;
	}
	
	public String getorderData() {
		return orderData;
	}
	
	public int getQuan() {
		return quan;
	}
	
	@Override
	public String toString() {
		return "OrderListVO [lunchName=" + lunchName + ", orderData=" + orderData + ", quan=" + quan + "]";
	}
	
}//class
