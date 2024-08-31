/******************************************************************* 
***  File Name  : VoteServlet.java 
***  Version  : v1.0
***  Designer  : 相内優真 
***  Date   : 2024.07.23
***  Purpose  : 投票内容を処理するサーブレット
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
import dbtest.UserAndProjectInfo;
import usedb.VoteBean;

public class VoteServlet extends HttpServlet {


    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
		//投票画面からの入力を受け取る
	    HttpSession session = request.getSession(true);
	    String ISEVENT = request.getParameter("ISEVENT");
	    int userID = (int)session.getAttribute("userID");
	    int projectID = (int)session.getAttribute("projectID");
	    
	    session.setAttribute("userID", userID);
	    session.setAttribute("projectID", projectID);
	    
	    UserAndProjectInfo upinfo = new UserAndProjectInfo();
	    ProjectInfo pinfo = new ProjectInfo();

	    pinfo.updateProjectInfo(projectID, "ProgressStatus", "Voting");
	    
	    upinfo.genre = request.getParameter("GENRE");
	    if(ISEVENT.equals("1")) {
	    	upinfo.updateVoteInfo(userID, projectID);
	    	session.setAttribute("ISEVENT", "1");
	    }else {
	    	if(Integer.parseInt(request.getParameter("BUDGET2")) > Integer.parseInt(request.getParameter("BUDGET1"))){
			upinfo.budget1 = request.getParameter("BUDGET1");
		    	upinfo.budget2 = request.getParameter("BUDGET2");		
			}else{
				upinfo.budget1 = request.getParameter("BUDGET2");
		    		upinfo.budget2 = request.getParameter("BUDGET1");	
			}   		    
		    upinfo.review = request.getParameter("REVIEW");
			upinfo.updateVoteInfo(userID, projectID);
			session.setAttribute("ISEVENT", "0");
	    }
	    
		//Beanにset
	    VoteBean vb = new VoteBean();
	    vb.setGenre(userID, projectID);
	    vb.setBudget1(userID, projectID);
	    vb.setBudget2(userID, projectID);
	    vb.setReview(userID, projectID);
	    vb.setDateTime(projectID);
	    vb.setVoterList(projectID);
	    request.setAttribute("vb", vb);        
	    	  
		//JSPにフォワード
	    if(pinfo.getProjectInfo(projectID).managerID != userID) {	        
	        String url="/checkVoteMember.jsp";
	    	RequestDispatcher dispatcher
	                       =getServletContext().getRequestDispatcher(url);
	    	dispatcher.forward(request, response);
	    } else {
	        String url="/checkVoteManager.jsp";
	    	RequestDispatcher dispatcher
	                       =getServletContext().getRequestDispatcher(url);
	    	dispatcher.forward(request, response);
	    }
    }
}
