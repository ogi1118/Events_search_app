/*******************************************************************
*** File Name : LoginServlet.java
*** Version : V1.0
*** Designer : 村田 悠真
*** Date : 2024/07/16
*** Purpose : emailをPostリクエストから受信，カレンダー情報入力画面に遷移する
***
*******************************************************************/

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/* emailを取得，DB登録，カレンダー情報入力画面への遷移を行うクラス */
public class LoginServlet extends HttpServlet {
	/* emailを含んだPostリクエストを受信するメソッド */
	@Override
	public void doPost(HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		// Postリクエストから取得したemail
		String email = request.getParameter("email");
		HttpSession session = request.getSession();
    /* emailをもとにDBアクセスを行い，userIDを取得する */
		try {
			UserInfoAccess2 userInfoAccess = new UserInfoAccess2();
			int userID = userInfoAccess.emailReg(email);
			session.setAttribute("userID", userID);
			System.out.println("Set userId: " + userID);

			String calendarUrl = "/Nine2/busyDaySelect.html";			
			response.sendRedirect(calendarUrl);
		}
		/* DBアクセスが失敗した時の例外処理 */
		catch (Exception e) {
			// エラー画面のURL
			String errorUrl = "/Nine2/loginError2.html";
			response.sendRedirect(errorUrl);
		}
	}
}