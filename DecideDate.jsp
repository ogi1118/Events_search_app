<!--
  File Name: DecideDate.jsp
  Version: V1.0
  Designer: 菅原幹太
  Date: 07/12
  Purpose:  Schedule_Adjust.javaより送られてきた日程候補群を提示し選択させる
            選択ができていなければエラー表示を出し選択ユーザーの選択を待つ
            選ばれた結果をSchedule_Adjust.javaに渡す
-->
<%@ page import="java.util.List"%>
<%@ page import="java.time.*"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Schedule Confirmation</title>
<link rel="stylesheet" href="/Nine2/pageStyle.css">
</head>
<body>
    <div class="header">
        <h1>日程決定画面</h1>
    </div>
    <div class="container">
        <div class="flex-container">
            <div class="flex-item">
                <div class="container">
                    <div class="box">
                        <h2>予定候補</h2>
                        <form action="${pageContext.request.contextPath}/servlet/CalendarServlet/" method="post" id="scheduleForm" onsubmit="return validateForm()">
                            <div class="date-box">
                                <%
                                List<LocalDate> freedays = (List<LocalDate>) request.getAttribute("freedays");
                                if (freedays != null && !freedays.isEmpty()) {
                                    for (LocalDate freeDay : freedays) {
                                        String fDay = freeDay.toString();
                                %>
                                <label>
                                    <input type="radio" name="selectedDate" value="<%= fDay %>" id="date_<%= freeDay %>" onchange="updateSelectedDate('<%= freeDay %>')" required> <%= freeDay %>
                                </label><br>
                                <%
                                    }
                                }
                                %>
                            </div>
                            <button type="submit">決定ボタン</button>
                            <%
                            String errorMessage = (String) request.getAttribute("errorMessage");
                            if (errorMessage != null) {
                            %>
                            <div class="error-message" style="color: red;"><%= errorMessage %></div>
                            <%
                            }
                            %>
                        </form>
                        <button id="backButton">ホームに戻る</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script>
        document.getElementById('backButton').addEventListener('click', function () {
            window.location.href = '/Nine2/Home.html';
        });
    </script>
</body>
</html>
