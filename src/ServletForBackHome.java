/*******************************************************************
*** File Name : ServletForBackHome.java
*** Version : V1.0
*** Designer : 井上 泰輝
*** Date : 2024/07/18
*** Purpose : ホーム画面に遷移するためのサーブレット
***
*******************************************************************/

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dbtest.ProjectInfo;

public class ServletForBackHome extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
		//sessionからProjectIDをget
    	HttpSession session = request.getSession(true);
    	int projectID = (int)session.getAttribute("projectID");
    	
    	String destination = request.getParameter("destination");
    	
    	ProjectInfo pi = new ProjectInfo(projectID);
    	//Home.htmlに遷移
    	String url="/Nine2/Home.html";
    	
		response.sendRedirect(url);    }
}