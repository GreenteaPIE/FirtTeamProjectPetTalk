
public class UserDTO { // 출처 : https://chaengstory.tistory.com/48
	


	// 사용자 정보입력
	private String userId;
	private String userPass;
	private String userName;
	private int userAge;
	private String userNum;
	private String userAddress;

	// 반려동물 정보입력
	private String petName;
	private int petAge;
	private String petSex;
	private String petBreed;
	private String petUserid;
	
	//친구목록 추가
	private String fId;
	
	public String getfId() {
		return fId;
	}

	public void setfId(String fId) {
		this.fId = fId;
	}

	public String getPetUserid() {
		return petUserid;
	}

	public void setPetUserid(String petUserid) {
		this.petUserid = petUserid;
	}

	//채팅창 닉네임
	private String chatN;
	
	private String adminId;
	private String adminPw;
	
	
	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getAdminPw() {
		return adminPw;
	}

	public void setAdminPw(String adminPw) {
		this.adminPw = adminPw;
	}

	public String getChatN() {
		return chatN;
	}

	public void setChatN(String chatN) {
		this.chatN = chatN;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserAge() {
		return userAge;
	}

	public void setUserAge(int userAge) {
		this.userAge = userAge;
	}

	public String getUserNum() {
		return userNum;
	}

	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public int getPetAge() {
		return petAge;
	}

	public void setPetAge(int petAge) {
		this.petAge = petAge;
	}

	public String getPetSex() {
		return petSex;
	}

	public void setPetSex(String petSex) {
		this.petSex = petSex;
	}

	public String getPetBreed() {
		return petBreed;
	}

	public void setPetBreed(String petBreed) {
		this.petBreed = petBreed;
	}

	@Override
	public String toString() {
		return "UserDTO [userId=" + userId + ", userPass=" + userPass + ", userName=" + userName + ", userAge="
				+ userAge + ", userNum=" + userNum + ", userAddress=" + userAddress + ", petName=" + petName
				+ ", petAge=" + petAge + ", petSex=" + petSex+ ", fId=" + fId + ",petUserid=" + petUserid + ",petBreed=" + petBreed + ", chatN=" + chatN + "]";
	}

}
