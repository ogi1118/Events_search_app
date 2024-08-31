/******************************************************************* 
***  File Name  : ServletForResult.java 
***  Version  : v1.0
***  Designer  : 井上 泰輝 
***  Date   : 2024.07.12
***  Purpose        : 選択確認表示用サーブレット
*** 
*******************************************************************/

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import dbtest.*;
import display.*;

public class ServletForResult extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
    	//Get引数から「名前」をとってくる
    	String rating = request.getParameter("rating");
    	String name = request.getParameter("name");
    	String address = request.getParameter("address");
    	String date = request.getParameter("date");

    	//Beanを作る（「名前」から「メッセージ」を作る）
    	ResultBean result = new ResultBean();
    	


    	//セッションの属性として永続化されているBeanを登録する．
    	HttpSession session = request.getSession(true);
    	int projectID = (int)session.getAttribute("projectID");
		ProjectInfo pi = new ProjectInfo(projectID);
		String eventName = name;
		name = pi.projectName;
		date = "" + pi.dateTime;
		
    	session.setAttribute("result", result);
    	result.setRating(rating);
    	result.setName(name);
    	result.setAddress(address);
    	result.setEvent(eventName);
    	result.setDate(date);

    	//このサーブレットの入出力を渡して，JSPを呼び出す．
    	//Beanに設定だれたデータをJSPの中で使う．
    	String url="/Result.jsp";
    	RequestDispatcher dispatcher
                   = getServletContext().getRequestDispatcher(url);
    	dispatcher.forward(request, response);
    }
}