<!--
  File Name: Result.jsp
  Version: V1.2
  Designer: 井上泰輝
  Date: 07/14
  Purpose:  ServletForResult.javaから確認内容を
  		    表示し2つのボタンで保存か前の画面に遷移するかを行う
-->
<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="result" class="display.ResultBean" scope="session"/>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>確認ページ</title>
    <link rel="stylesheet" href="/Nine2/pageStyle.css">
</head>
<body>
    <div class="header">
		<h1>行き先決定画面</h1>
	</div>
    <div class="container">
        <div class="flex-container">
            <div class="flex-item">
            <%
                String name = result.getName();
                String date = result.getDate();
                String event = result.getEvent() + "(" + result.getAddress() + ")";
                String str = result.getAddress() + "***" + result.getEvent();
            %>
                <p>以下の内容で決定します.よろしいですか?</p>
                <p><%= name %></p>
                <p><%= date %></p>
                <p><%= event %></p>
                <FORM method="GET" action="/Nine2/servlet/ServletForSave/">
                    <div>
                        <label>
                            <button type="submit" class="button button-yes" value="はい">はい</button>
                            <input type="hidden" name="destination" value=<%= str %>>
                            <input type="hidden" name="name" value=<%= name %>>
                            <input type="hidden" name="event" value=<%= event %>>
                            <input type="hidden" name="date" value=<%= date %>>
                        </label>
                    </div>
                </FORM>
                <button class="button button-no" onclick="history.back()">いいえ</button>
                <button id="backButton">ホームに戻る</button>
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
