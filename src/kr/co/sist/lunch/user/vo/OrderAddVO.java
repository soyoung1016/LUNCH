package kr.co.sist.lunch.user.vo;

public class OrderAddVO {
	private String orderName, phone, ipAddress, lunchCode, request;
	private int quan;
	
	public OrderAddVO(String orderName, String phone, String ipAddress, String lunchCode, String request, int quan) {
		this.orderName = orderName;
		this.phone = phone;
		this.ipAddress = ipAddress;
		this.lunchCode = lunchCode;
		this.request = request;
		this.quan = quan;
	}
	
	public String getOrderName() {
		return orderName;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}
	
	public String getLunchCode() {
		return lunchCode;
	}
	
	public String getRequest() {
		return request;
	}
	
	public int getQuan() {
		return quan;
	}

	@Override
	public String toString() {
		return "OrderAddVO [orderName=" + orderName + ", phone=" + phone + ", ipAddress=" + ipAddress + ", lunchCode="
				+ lunchCode + ", request=" + request + ", quan=" + quan + "]";
	}
	
}//class
