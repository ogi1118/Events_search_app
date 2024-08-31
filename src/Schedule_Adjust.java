/**
 * File Name: Schedule_Adjust.java
 * Version: V1.0
 * Designer: 菅原幹太
 * Date: 07/12
 * Purpose: ホーム画面よりprojectidを受け取りそのprojectidをもとにuserandprojectsdetailstableninthテーブルからuseridを取り出し、useridをもとに
 * 			userstableninthからCalendarInfoを取り出しここに入っている予定のある日程以外の予定のない日を合わせて日程選択画面に送る
 * 			日程選択画面から得られた結果をprojectstableninthに挿入する。
 *          
 */

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dbtest.ProjectInfo;

@WebServlet("/servlet/Schedule_Adjust/")
public class Schedule_Adjust extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	HttpSession session = request.getSession();
    	int projectID = (int)session.getAttribute("projectID");
    	
    	// JDBC接続設定
        String url = "jdbc:postgresql://172.27.0.15:5432/g9_db"; // データベースのURL
        String username = "group09";
        String password = "grp9";

        Set<Date> availableDates = new HashSet<>();
        Map<String, List<LocalDate>> memberCalendarInfo = new HashMap<>();
        LocalDate daymin = LocalDate.MIN;
        LocalDate daymax = LocalDate.MAX;

        try {
            // JDBCドライバのロード
            Class.forName("org.postgresql.Driver");
            // データベース接続
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                // プロジェクトIDからユーザーIDを取得するSQLクエリの準備
                List<Integer> userIds = getUserIdsForProject(connection, projectID); // プロジェクトIDを指定
                System.out.println("projectID:" + Integer.toString(projectID));
                for (int userId : userIds) {
                    System.out.println("userID:" + Integer.toString(userId));
                    // ユーザーIDからカレンダー情報を取得するSQLクエリの準備
                    String query = "SELECT DisplayName, CalendarInfo FROM UsersTableNinth WHERE UserID = ?";
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setInt(1, userId);
                        try (ResultSet resultSet = statement.executeQuery()) {
                            while (resultSet.next()) {
                                String displayName = resultSet.getString("displayname");
                                String calendarInfo = resultSet.getString("calendarinfo");

                                List<LocalDate> busyDays = new ArrayList<>();
                                String[] calendarInfoSplit = calendarInfo.split(",");
                                String[] calendarRange = calendarInfoSplit[0].split(" ");
                                if (calendarInfoSplit.length != 1) {
	                                String[] calendarBusyDay = calendarInfoSplit[1].split(" ");
	                                for (String day : calendarBusyDay) {
	                                    busyDays.add(LocalDate.parse(day));
	                                }
                                }
                                memberCalendarInfo.put(displayName, busyDays);

                                if (daymin.isBefore(LocalDate.parse(calendarRange[0]))) {
                                    daymin = LocalDate.parse(calendarRange[0]);
                                }
                                if (daymax.isAfter(LocalDate.parse(calendarRange[1]))) {
                                    daymax = LocalDate.parse(calendarRange[1]);
                                }
                            }
                        }
                    }
                }

                // メンバーの忙しい日付を考慮して空いている日付を計算
                List<LocalDate> freeDays = calculateFreeDays(daymin, daymax, memberCalendarInfo);

                
                if (freeDays.size() == 0) {
	                // データをリクエスト属性に設定
	                request.setAttribute("memberCalendarInfo", memberCalendarInfo);
	                request.setAttribute("daymin", daymin);
	                request.setAttribute("daymax", daymax);
	                // JSPにフォワード
	                request.getRequestDispatcher("/DecideDateif.jsp").forward(request, response);
                }
                else {
	                // データをリクエスト属性に設定
	                request.setAttribute("freedays", freeDays);
	                // JSPにフォワード
	                request.getRequestDispatcher("/DecideDate.jsp").forward(request, response);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // エラーハンドリング：エラーページにリダイレクトするなど
            String error = URLEncoder.encode(e.getMessage(), "UTF-8");
            response.sendRedirect("http://localhost:8080/?error=" + error);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 選択された日程の取得
        String selectedDateStr = request.getParameter("selectedDate");
        System.out.println("selectedDateStr: " + selectedDateStr); // デバッグ出力

        if (selectedDateStr == null || selectedDateStr.isEmpty()) {
            // エラーハンドリング: 選択された日程がない場合
            request.setAttribute("error", "選択された日程が無効です。");
            request.getRequestDispatcher("/DecideDate.jsp").forward(request, response);
            return;
        }

        // 日付の解析
        LocalDate selectedDate;
        try {
            selectedDate = LocalDate.parse(selectedDateStr);
        } catch (DateTimeParseException e) {
            // エラーハンドリング: 日付の解析に失敗した場合
            String error = URLEncoder.encode("日程の解析に失敗しました。", "UTF-8");
            response.sendRedirect("http://localhost:8080/?error=" + error);
            return;
        }

        // JDBC接続設定
        String url = "jdbc:postgresql://172.27.0.15:5432/g9_db";
        String username = "group09";
        String password = "grp9";

    	HttpSession session = request.getSession();
    	int projectID = (int)session.getAttribute("projectID");
        
    	ProjectInfo projectInfo = new ProjectInfo();
    	projectInfo = projectInfo.getProjectInfo(projectID);

        try {
            // JDBCドライバのロード
            Class.forName("org.postgresql.Driver");
            // データベース接続
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                // データベースに日程を更新するSQLクエリの準備
                String updateQuery = "UPDATE ProjectsTableNinth SET DateTime = ? WHERE ProjectID = ?";
                try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
                    // ProjectID を手動で指定
                    statement.setInt(2, projectID);
                    // 日程をSQLクエリにセット
                    statement.setDate(1, Date.valueOf(selectedDate));

                    // SQLクエリの実行
                    int rowsUpdated = statement.executeUpdate();

                    if (rowsUpdated > 0) {
                        // 更新成功した場合の処理
                    	if (projectInfo.category.equals("event")) {
                    		projectInfo.updateProjectInfo(projectID, "progressstatus", "Voting");
                    		response.sendRedirect("/Nine2/voteEvent.html");
                    	}
                    	else if (projectInfo.category.equals("facility")) {
                    		projectInfo.updateProjectInfo(projectID, "progressstatus", "Voting");
                    		response.sendRedirect("/Nine2/voteDest.html");
                    	}
                    	else {
                        String errorMessage = URLEncoder.encode("遷移失敗", "UTF-8");
                        response.sendRedirect("http://localhost:8080/?error=" + errorMessage);
                    	}
                    } else {
                        // 更新された行がない場合の処理
                        String errorMessage = URLEncoder.encode("日程を更新できませんでした。", "UTF-8");
                        response.sendRedirect("http://localhost:8080/?error=" + errorMessage);
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // エラーハンドリング：エラーページにリダイレクトするなど
            String error = URLEncoder.encode(e.getMessage(), "UTF-8");
            response.sendRedirect("http://localhost:8080/?error=" + error);
        }
    }

    // プロジェクトIDから関連するユーザーIDのリストを取得するメソッド
    private List<Integer> getUserIdsForProject(Connection connection, int projectId) throws SQLException {
        List<Integer> userIds = new ArrayList<>();
        String selectQuery = "SELECT userid FROM userandprojectsdetailstableninth WHERE projectid = ?";
        try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
            statement.setInt(1, projectId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int userId = resultSet.getInt("userid");
                    userIds.add(userId);
                }
            }
        }
        return userIds;
    }

    // メンバーの忙しい日付を考慮して空いている日付を計算するメソッド
    private List<LocalDate> calculateFreeDays(LocalDate daymin, LocalDate daymax, Map<String, List<LocalDate>> memberCalendarInfo) {
        List<LocalDate> freeDays = new ArrayList<>();
        LocalDate day = daymin;
        LocalDate dayAfterMonth = day.plusMonths(1);

        while (day.isBefore(dayAfterMonth) && day.isBefore(daymax)) {
            freeDays.add(day);
            day = day.plusDays(1);
        }

        for (List<LocalDate> busyDays : memberCalendarInfo.values()) {
            freeDays.removeAll(busyDays);
        }

        return freeDays;
    }
}