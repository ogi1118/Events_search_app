<!--
  File Name: Save.jsp
  Version: V1.0
  Designer: 井上泰輝
  Date: 07/10
  Purpose:  ServletForDisplay.javaから渡された内容を
  			保存したことを表示する
-->
<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="save" class="display.SaveBean" scope="session"/>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>保存完了</title>
	<link rel="stylesheet" href="/Nine2/pageStyle.css">
</head>
<body>
	<div class="header">
		<h1>決定内容保存画面</h1>
	</div>
  	<div class="container">
    	<div class="flex-container">
      		<div class="flex-item">
<%
	String name = save.getName();
	String date = save.getDate();
	String event = save.getEvent();
%>
				<p>以下の内容を保存しました!</p>
				<p><%= event %></p>
				<p><%= date %></p>
				<p><%= name %></p>
				<FORM method="GET" action="/Nine2/servlet/ServletForBackHome/">
					<div>
						<label>
							<button type="submit" value="ホームへ戻る">ホームへ戻る</button>
						</label>
					</div>
				</FORM>
      		</div>
    	</div>
  	</div>
</body>
</html>
