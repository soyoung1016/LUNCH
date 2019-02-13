package kr.co.sist.lunch.user.vo;

public class LunchListVO {
	private String lunchCode, lunchName, img, lunchSpec;

	public LunchListVO(String img, String lunchName, String lunchSpec, String lunchCode) {
		this.img = img;
		this.lunchName = lunchName;
		this.lunchSpec = lunchSpec;
		this.lunchCode = lunchCode;
	}

	public String getImg() {
		return img;
	}

	public String getLunchName() {
		return lunchName;
	}

	public String getLunchSpec() {
		return lunchSpec;
	}

	public String getLunchCode() {
		return lunchCode;
	}

	@Override
	public String toString() {
		return "LunchListVO [img=" + img + ", lunchName=" + lunchName + ", lunchSpec=" + lunchSpec + ", lunchCode="
				+ lunchCode + "]";
	}

}//class
