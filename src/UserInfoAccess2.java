/*******************************************************************
*** File Name : UserInfoAccess2.java
*** Version : V1.0
*** Designer : 村田 悠真
*** Date : 2024/07/16
*** Purpose : ログイン時，userID，email，calendarInfo，nickNameのDBアクセスメソッドを呼び出す
***
*******************************************************************/

import dbtest.UserInfo;

/* ログイン時，userID，emailのDB登録を行うクラス */
public class UserInfoAccess2 {
	/* userID，emailのDB登録を行うメソッド */
	public int emailReg(String email) throws Exception {
		int userID = -1;
		//emailでuserID DB問い合わせ
		UserInfo userInfo = new UserInfo();
		String userIdStr = userInfo.getUserInfo("UserID", "Email", email);

		//未登録:最大のuserID DB問い合わせ
		//userID割り当て
		//userID，email，DB挿入
		if (userIdStr != null) {
			userID = Integer.parseInt(userIdStr);
		}
		else {
			userID = userInfo.getMaxID() + 1;
			userInfo.userID = userID;
			userInfo.email = email;
			userInfo.setUserInfo();
		}
		return userID;
	}

  /* userIDでNickNameを取得するメソッド */
	public String getNickName(int userID) {
		// DBに登録済のニックネーム　未登録の場合は""
		UserInfo userInfo = new UserInfo();
		String nickName = userInfo.getUserInfo("DisplayName", "UserId", String.valueOf(userID));
		if (nickName == null) {
			nickName = "";
		}
		return nickName;
	}

  /* userIDでNickNameを更新するメソッド */
	public void nickNameReg(int userID, String nickName) throws Exception {
		//userIDでNickName更新
		UserInfo userInfo = new UserInfo();
		userInfo.updateUserInfo(userID, "DisplayName", nickName);
	}

  /* userIDでcalendarInfoを更新するメソッド */
	public void calendarInfoReg(int userID, String calendarInfo) throws Exception {
		//userIDでcalendarInfo更新
		UserInfo userInfo = new UserInfo();
		userInfo.updateUserInfo(userID, "CalendarInfo", calendarInfo);
	}
}
