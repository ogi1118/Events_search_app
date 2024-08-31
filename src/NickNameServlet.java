/*******************************************************************
*** File Name : NickNameServlet.java
*** Version : V1.0
*** Designer : 村田 悠真
*** Date : 2024/06/27
*** Purpose : nickNameをPostリクエストから受信，ホーム画面に遷移する
***
*******************************************************************/

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/* nickNameを取得，DB登録，ホーム画面への遷移を行うクラス */
public class NickNameServlet extends HttpServlet {

	/* nickNameを含んだPostリクエストを受信するメソッド */
    @Override
	public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        request.setCharacterEncoding("UTF-8");
    	response.setContentType("text/html; charset=UTF-8");

    	// Postリクエストから取得したnickName
    	String nickName = request.getParameter("nickName");
    	
    	//セッションからuserID取得
        HttpSession session = request.getSession();
        session.setAttribute("displayName", nickName);
        int userID = (int)session.getAttribute("userID");
        
        try {
        	//userIDでNickName更新
        	UserInfoAccess2 userInfoAccess = new UserInfoAccess2();
        	userInfoAccess.nickNameReg(userID, nickName);
        }
		/* DBアクセスが失敗した時の例外処理 */
		catch (Exception e) {
			
			// エラー画面のURL
			String errorUrl = "/Nine2/loginError2.html";
			response.sendRedirect(errorUrl);
		}
        
        
		String homeUrl = "/Nine2/homeRedirect.html" + "?userID=" + userID + "&displayName=" + URLEncoder.encode(nickName, "UTF-8");
        response.sendRedirect(homeUrl);
    }
}