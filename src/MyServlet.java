import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dbtest.ProjectInfo;
import dbtest.UserAndProjectInfo;

import java.sql.Timestamp;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
/**
 * File Name: MyServlet.java
 * Version: V1.0
 * Designer: 新保陽己
 * Date: 07/223
 * Purpose: ホーム画面、メンバー登録画面から送られてきた情報を処理する
 */

@WebServlet("/api/data/")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static List<JSONObject> jsonDataList = new ArrayList<>();

	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setStatus(HttpServletResponse.SC_OK);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		StringBuilder requestBody = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				requestBody.append(line);
			}
		}

		System.out.println("Request Body: " + requestBody.toString());

		try {
			JSONParser parser = new JSONParser();
			JSONObject requestData = (JSONObject) parser.parse(requestBody.toString());
			String action = (String) requestData.get("action");

			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("message", "POST received");
			jsonResponse.put("data", requestData);
			String redirectUrl = "";

			if ("event".equals(action)) {
				EventRegister eventRegister = new EventRegister();
				int eventResponse = eventRegister.sendData(requestData);
				jsonResponse.put("projectID", eventResponse);
			} else if ("join".equals(action)) {
				JoinRegister joinRegister = new JoinRegister();
				String status = joinRegister.sendData(Integer.parseInt((String) requestData.get("projectID")),
						(String) requestData.get("displayName"), ((Number) requestData.get("userID")).intValue());
				jsonResponse.put("progressStatus", status);
			} else if ("dispose".equals(action)) {
				EventRegister eventRegister = new EventRegister();
				eventRegister.disposeData(requestData);
			} else if ("decline".equals(action)) {
				JoinRegister joinRegister = new JoinRegister();
				joinRegister.disposeData(requestData);
			}else if ("move".equals(action)) {
				int projectID = ((Number) requestData.get("projectID")).intValue();
				int userID = Integer.parseInt((String) requestData.get("userID"));
				HttpSession session = request.getSession();
				session.setAttribute("projectID", projectID);
				ProjectInfo cons = new ProjectInfo();
				ProjectInfo project = cons.getProjectInfo(projectID);
				String progressStatus = project.progressStatus;
				UserAndProjectInfo userandprojectinfo = new UserAndProjectInfo();
				UserAndProjectInfo user = userandprojectinfo.getVoteInfo(projectID, userID);
				String genre = user.genre;
				if ("Registration".equals(progressStatus)) {
					//(締め切り画面へ遷移)
					redirectUrl = "/Nine2/JoinDisplay.html";
					//response.sendRedirect(closingUrl);
				} else if ("Matching".equals(progressStatus)) {
					//(日程画面へ遷移)
					if (project.managerID == userID) {
						redirectUrl = "/Nine2/servlet/Schedule_Adjust/";
						//response.sendRedirect(schedule_AdjustUrl);
					} else {
						redirectUrl = "/Nine2/JoinDisplay.html";
						//response.sendRedirect(closingUrl);
					}
				} else if ("Searching".equals(progressStatus)) {
					//(検索画面へ遷移)
					if (project.managerID == userID) {
					if(project.category.equals("event")){
						redirectUrl = "/Nine2/servlet/ServletForDisplay/?ISEVENT=1";
					}else{
						redirectUrl = "/Nine2/servlet/ServletForDisplay/?ISEVENT=0";
					}
					}else{
						redirectUrl = "/Nine2/Home.html";
					}
				} else if ("Voting".equals(progressStatus)) {
					//(投票画面へ遷移)
					if(user.genre == null){
						if (project.category.equals("event")) {
							session.setAttribute("ISEVENT", "1");
							redirectUrl = "/Nine2/voteEvent.html";
							//response.sendRedirect(closingUrl);
						} else {
							session.setAttribute("ISEVENT", "0");
							redirectUrl = "/Nine2/voteDest.html";
							//response.sendRedirect(closingUrl);
						}
					}else{
						if (project.category.equals("event")) {
							session.setAttribute("ISEVENT", "1");
							redirectUrl = "/Nine2/servlet/vote/?ISEVENT=1&GENRE=" + user.genre;
							//response.sendRedirect(closingUrl);
						} else {
							session.setAttribute("ISEVENT", "0");
							redirectUrl = "/Nine2/servlet/vote/?ISEVENT=0&GENRE=" + user.genre + "&BUDGET1=" + user.budget1 + "&BUDGET2=" + user.budget2 + "&REVIEW=" + user.review;
							//response.sendRedirect(closingUrl);
						}
					}
				} else if ("Notification".equals(progressStatus)) {
					//(決定画面へ遷移)
					String destination = project.destination;
					Timestamp date = project.dateTime; // 'Timestamp' のクラス名を修正

					try {
    					String encodedDestination = URLEncoder.encode(destination, "UTF-8");
    					String encodedDate = URLEncoder.encode(date.toString(), "UTF-8");
    					redirectUrl = "/Nine2/servlet/ServletForSave/?destination=" + encodedDestination + "&date=" + encodedDate;
					} catch (UnsupportedEncodingException e) {
    					// エンコードエラー処理
    					e.printStackTrace();
					}
					//response.sendRedirect(homeUrl);
				}
				//締め切り操作による遷移
			} else if ("close".equals(action)) {
				int projectID = Integer.parseInt((String) requestData.get("projectID"));
				HttpSession session = request.getSession();
				session.setAttribute("projectID", projectID);
				ProjectInfo cons = new ProjectInfo();
				ProjectInfo project = cons.getProjectInfo(projectID);
				project.updateProjectInfo(projectID, "progressStatus", "Matching");
				//(日程画面へ遷移)
				//String schedule_AdjustUrl = "/Nine2/servlet/Schedule_Adjust/";
				//response.sendRedirect(schedule_AdjustUrl);
				redirectUrl = "/Nine2/servlet/Schedule_Adjust/";
			}

			if (!redirectUrl.equals("")) { 
				jsonResponse.put("redirectUrl", redirectUrl); 
			}

			String jsonResponseString = jsonResponse.toJSONString();
			response.setContentType("application/json");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setStatus(HttpServletResponse.SC_OK);
			try (OutputStream os = response.getOutputStream()) {
				os.write(jsonResponseString.getBytes(StandardCharsets.UTF_8));
			}

		} catch (ParseException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON");
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONArray jsonArray = new JSONArray();

		try {
			String projectIDParam = request.getParameter("projectID");
			String userIDParam = request.getParameter("userID");

			if (projectIDParam != null && userIDParam == null) {
				int requestedProjectID = Integer.parseInt(projectIDParam);

				// Search for matching projectID in jsonDataList
				for (JSONObject data : jsonDataList) {
					int projectID = ((Integer) data.get("projectID")).intValue(); // Cast to int from Long
					if (projectID == requestedProjectID) {
						ProjectInfo cons = new ProjectInfo();
						ProjectInfo project = cons.getProjectInfo(projectID);
						String status = project.progressStatus;
						data.put("status",status);
						jsonArray.add(data);
					}
				}
			} else {
				// If no projectID parameter provided, return all data
				for (JSONObject data : jsonDataList) {
					int projectID = ((Integer) data.get("projectID")).intValue();
					ProjectInfo cons = new ProjectInfo();
					ProjectInfo project = cons.getProjectInfo(projectID);
					String status = project.progressStatus;
					data.put("status",status);
					jsonArray.add(data);
				}
			}
			if (projectIDParam != null && userIDParam != null) {
				int projectID = Integer.parseInt(projectIDParam);
				int userID = Integer.parseInt(userIDParam);
				JSONObject data = new JSONObject();
				ProjectInfo cons = new ProjectInfo();
				ProjectInfo project = cons.getProjectInfo(projectID);
				UserAndProjectInfo userandprojectinfo = new UserAndProjectInfo();
				UserAndProjectInfo user = userandprojectinfo.getVoteInfo(projectID, userID);
				String progressStatus = project.progressStatus;
				String genre = user.genre;
				data.put("progressStatus", progressStatus);
				data.put("genre", genre);
				jsonArray.add(genre);
			}

			// Prepare response
			String jsonDataString = jsonArray.toJSONString();
			System.out.println("response" + jsonDataString);
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_OK);
			try (OutputStream os = response.getOutputStream()) {
				os.write(jsonDataString.getBytes(StandardCharsets.UTF_8));
			}
		} catch (NumberFormatException e) {
			// Handle invalid projectID parameter
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("Invalid projectID parameter");
			e.printStackTrace(); // Optionally log the exception
		} catch (Exception e) {
			// Handle other exceptions
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Internal server error");
			e.printStackTrace(); // Optionally log the exception
		}
	}

}
