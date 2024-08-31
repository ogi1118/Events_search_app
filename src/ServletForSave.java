/******************************************************************* 
***  File Name  : ServletForSave.java 
***  Version  : v1.1
***  Designer  : 井上 泰輝 
***  Date   : 2024.07.12
***  Purpose        : 保存画面表示用サーブレット
*** 
*******************************************************************/

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dbtest.ProjectInfo;
import display.SaveBean;

public class ServletForSave extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
    	// Get引数から「名前」をとってくる
    	String name = request.getParameter("name");
    	String destination = request.getParameter("destination");
    	String date = request.getParameter("date");
    	String event = request.getParameter("event");

    	// Beanを作る（「名前」から「メッセージ」を作る）
    	SaveBean save = new SaveBean();

    	// セッションの属性として永続化されているBeanを登録する．
		// データベースから企画情報を取得
    	HttpSession session = request.getSession(true);
    	int projectID = (int)session.getAttribute("projectID");
    	ProjectInfo projectinfo = new ProjectInfo();
		ProjectInfo pi = projectinfo.getProjectInfo(projectID);
		pi.destination = destination;
		pi.projectID = projectID;
		pi.progressStatus = "Notification";
		pi.updateProjectInfo();
		
    	session.setAttribute("save", save);
    	save.setName(name);
    	save.setDestination(destination);
    	save.setEvent(event);
    	save.setDate(date);
    	
		// 通知
    	Notification noti = new Notification(projectID);
    	noti.notificationFirst();

    	// このサーブレットの入出力を渡して，JSPを呼び出す．
    	// Beanに設定だれたデータをJSPの中で使う．
    	String url="/Save.jsp";
    	RequestDispatcher dispatcher
                   = getServletContext().getRequestDispatcher(url);
    	dispatcher.forward(request, response);
    }
}