/*******************************************************************
*** File Name : CalendatServlet.java
*** Version : V1.0
*** Designer : 村田 悠真
*** Date : 2024/07/16
*** Purpose : calendarInfoをPostリクエストから受信，ニックネーム入力画面に遷移する
***
*******************************************************************/

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import dbtest.ProjectInfo;

//import javax.servlet.annotation.WebServlet;

/* calendarInfoを取得，DB登録，ニックネーム入力画面への遷移を行うクラス */
//@WebServlet("/servlet/CalendarServlet/")
public class CalendarServlet extends HttpServlet {

	/* emailを含んだPostリクエストを受信するメソッド */
	@Override
	public void doPost(HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {

        request.setCharacterEncoding("UTF-8");
    	response.setContentType("text/html; charset=UTF-8");

    	
    	String calendarInfo = request.getParameter("calendarInfo");
		String nickName = "";
		
		/* カレンダー情報をDBに登録する */
		UserInfoAccess2 userInfoAccess = new UserInfoAccess2();
		HttpSession session = request.getSession();
		int userID = 0;
		//int userID = (int)session.getAttribute("userID");
		if (session != null) {
			userID = (int) session.getAttribute("userID");
			System.out.println("Get userID: " + userID);
		} else {
			System.out.println("Session is null.");
			userID = 100;
		} 
		
		try {
		if(calendarInfo != null){
			userInfoAccess.calendarInfoReg(userID, calendarInfo);
		}
		nickName = userInfoAccess.getNickName(userID);
		}
		/* DBアクセスが失敗した時の例外処理 */
		catch (Exception e) {
			
			// エラー画面のURL
			String errorUrl = "/Nine2/loginError2.html";
			response.sendRedirect(errorUrl);
		}
		
		// nickName登録画面のURL
		String nickNameUrl = "/Nine2/nickName.html";
		/* 登録済の場合URLにnickNameパラメータを加えて送信 */
		if (nickName != "") {
			nickNameUrl = nickNameUrl + "?nickName=" + URLEncoder.encode(nickName, "UTF-8");
		}

		if(request.getParameter("selectedDate") != null){
			int projectID = (int)session.getAttribute("projectID");
			ProjectInfo cons = new ProjectInfo();
			ProjectInfo project = cons.getProjectInfo(projectID);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String str = request.getParameter("selectedDate");
			try{
				Date date = sdf.parse(str);
				Timestamp ts = new Timestamp(date.getTime());
                        	project.updateProjectInfo(projectID, "dateTime", ts);
                        	project.updateProjectInfo(projectID, "progressStatus", "Voting");
			}catch(Exception e){
				e.printStackTrace();
			}
			System.out.println("selectedDate = not null");
			response.sendRedirect("/Nine2/Home.html");
		}else{
			System.out.println("selectedDate = null");
			response.sendRedirect(nickNameUrl);
		}
		
	}
}
