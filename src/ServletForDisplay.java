/******************************************************************* 
***  File Name  : ServletForDisplay.java 
***  Version  : v1.1
***  Designer  : 井上 泰輝 
***  Date   : 2024.07.14
***  Purpose        : 検索結果表示用サーブレット
*** 
*******************************************************************/

import java.io.*;
import java.net.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import dbtest.*;
import display.*;

public class ServletForDisplay extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
    	//Get引数から「名前」をとってくる
    	HttpSession session = request.getSession(true);
    	String ISEVENT = request.getParameter("ISEVENT");
    	int projectID = (int)session.getAttribute("projectID");
    	
		//progressStatusを更新
    	ProjectInfo pi = new ProjectInfo(projectID);
    	pi.progressStatus = "Searching";
    	pi.updateProjectInfo();
    	
		//店検索に分岐
    	if (ISEVENT.equals("0")) {
    		// Beanを作る（「名前」から「メッセージ」を作る）
        	DisplayBean display = new DisplayBean();
        	
			//データベースから投票内容を取得
        	UserAndProjectInfo userAndProjectInfo = new UserAndProjectInfo();
        	UserAndProjectInfo UAPI = userAndProjectInfo.getVoteInfo(projectID);
        	display.genre = UAPI.genreList;
        	display.budget1 = UAPI.budget1List;
        	display.budget2 = UAPI.budget2List;
        	display.review = UAPI.reviewList;

        	display.makeList(projectID);
        	
        	List<String[]> list = display.getList();
        	
        	
        	display.setList(list);
        	
        	
        	// セッションの属性として永続化されているBeanを登録する．
        	session.setAttribute("display", display);

        	// このサーブレットの入出力を渡して，JSPを呼び出す．
        	// Beanに設定だれたデータをJSPの中で使う．
        	String url="/Display.jsp";
        	RequestDispatcher dispatcher
                       = getServletContext().getRequestDispatcher(url);
        	dispatcher.forward(request, response);
    	}
    	else {
    		//イベント検索に分岐
    		DisplayEventsBean deb = new DisplayEventsBean();
			ServletContext context = this.getServletContext();
			String rpath = this.getServletContext().getRealPath("/regionIDMap.txt");
			String gpath = this.getServletContext().getRealPath("/genreIDMap.txt");
    		deb.makeList(projectID, rpath, gpath);
    		
    		for (int i = 0; i < deb.event.size(); i++) {
    			System.out.println(i + ":" + deb.event.get(i));
    		}
    		
    		session.setAttribute("deb", deb);
			// このサーブレットの入出力を渡して，JSPを呼び出す．
        	// Beanに設定だれたデータをJSPの中で使う．
    		String url="/DisplayEvents.jsp";
        	RequestDispatcher dispatcher
                       = getServletContext().getRequestDispatcher(url);
        	dispatcher.forward(request, response);
    	}

    	
    }
}
