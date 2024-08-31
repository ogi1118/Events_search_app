<!--
  File Name: DecideDateif.jsp
  Version: V1.0
  Designer: 菅原幹太
  Date: 07/12
  Purpose: 	空いている日程がないときは
            Schedule_Adjust.javaより送られてきた日程候補群を提示し選択させる
  			選択ができていなければエラー表示を出し選択ユーザーの選択を待つ
  			選ばれた結果をSchedule_Adjust.javaに渡す
-->
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%@ page contentType="text/html;　charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>参加者のスケジュール</title>
    <link rel="stylesheet" href="./pageStyle.css">
    <script>
        // 選択された日付を保存する変数
        let selectedDate = '';

        // 日付を選択する関数
        function selectDate(date) {
            selectedDate = date;
            document.getElementById('selectDateMessage').innerText = "選択された日付: " + date;
        }

        // 選択された日付をフォームに追加する関数
        function submitDates() {
            if (!selectedDate) {
                document.getElementById('error-message').classList.remove('hidden');
                return false;
            }
            document.getElementById('selectedDateInput').value = selectedDate;
            return true;
        }
    </script>
</head>
<body>
    <div class="container">
        <div class="flex-container">
            <div class="flex-item">
                <div class="box">
                    <form action="<%= request.getContextPath() %>/servlet/Schedule_Adjust/" method="post" id="scheduleForm" onsubmit="return submitDates()">
                        <div class="date-box">
                            <p id="selectDateMessage">日程を選択してください。</p>
                            <!-- 選択された日付を送信する隠しフィールド -->
                            <input type="hidden" name="selectedDate" id="selectedDateInput">
                        </div>
                        <button type="submit">決定ボタン</button>
                    </form>
                    <div id="error-message" class="hidden" style="color: red;">日程を選択してください。</div>
                </div>

                <h2>参加者のスケジュール</h2>
                <table>
                    <thead>
                        <tr>
                            <th>日付</th>
                            <%
                                Map<String, List<LocalDate>> memberCalendarInfo = (Map<String, List<LocalDate>>) request.getAttribute("memberCalendarInfo");
                                if (memberCalendarInfo != null && !memberCalendarInfo.isEmpty()) {
                                    for (String displayName : memberCalendarInfo.keySet()) {
                            %>
                                        <th><%= displayName %></th>
                            <%
                                    }
                                }
                            %>
                            <th>選択</th> <!-- 選択ボタンの列を追加 -->
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            // 日付の範囲を取得
                            LocalDate daymin = (LocalDate) request.getAttribute("daymin");
                            LocalDate daymax = (LocalDate) request.getAttribute("daymax");

                            // dayminからdaymaxまでの日付をループしてテーブルを構築
                            if (daymin != null && daymax != null) {
                                LocalDate date = daymin;
                                while (!date.isAfter(daymax)) {
                        %>
                                    <tr>
                                        <td><%= date %></td>
                                        <%
                                            // 各参加者ごとに予定の有無をチェックしてセルに○または×を表示
                                            if (memberCalendarInfo != null && !memberCalendarInfo.isEmpty()) {
                                                for (String displayName : memberCalendarInfo.keySet()) {
                                                    List<LocalDate> busyDays = memberCalendarInfo.get(displayName);
                                                    boolean isBusy = busyDays != null && busyDays.contains(date);
                                        %>
                                                    <td><%= isBusy ? "×" : "○" %></td>
                                        <%
                                                }
                                            }
                                        %>
                                        <td>
                                            <button type="button" onclick="selectDate('<%= date %>')">選択</button>
                                        </td>
                                    </tr>
                        <%
                                    date = date.plusDays(1); // 日付を1日進める
                                }
                            }
                        %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>    
</body>
</html>
