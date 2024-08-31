<!--
  File Name: DisplayEvents.jsp
  Version: V1.0
  Designer: 井上泰輝
  Date: 07/09
  Purpose:  ServletForDisplay.javaから渡された投票結果のListを
  			表形式で表示し選択された内容をServletForResultにGETで渡す
-->
<%@ page contentType="text/html; charset=UTF-8" import="java.util.*" import="display.*" %>
<jsp:useBean id="deb" class="display.DisplayEventsBean" scope="session"/>
<html lang="ja">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    
    <title>検索結果表示</title>
    <style>
        table {
            width: auto;
            border-collapse: collapse;
            margin: 20px;
            font-size: 18px;
            text-align: left;
        }
        th, td {
            padding: 12px;
            border: 1px solid #ddd;
        }
	</style>
	<link rel="stylesheet" href="/Nine2/pageStyle.css">
</head>
<body>
	<div class="header">
		<h1>検索結果表示画面</h1>
	</div>
	<div class="container">
		<div class="flex-container">
			<div class="flex-item">
				<h1><jsp:getProperty name="deb" property="category"/>
				の投票結果に基づく検索結果表示</h1>
				<%
				List<Event> list = deb.getList();
				String event;
				String[] s_address = new String[5];
				String[] s_name = new String[5];
				String[] s_date = new String[5];
				String[] s_description = new String[5];
				String[] s_url = new String[5];
				%>
				<table>
					<thead>
						<tr>
							<th>No.</th>
							<th>イベント名</th>
							<th>説明</th>
							<th>住所</th>
							<th>期間(開始-終了日時)</th>
							<th>投票</th>
						</tr>
					</thead>
					<tbody>
				<%
				if(list.size() == 0){
					%>
					<p class="caution">条件 <jsp:getProperty name="deb" property="category"/> のイベントは存在しませんでした。ボタンより前のページに戻って投票内容を修正してください。</p>
					<button id="vote" onclick="location.href='/Nine2/voteEvent.html'">戻る</button>
					<%
				}else{
					for (int i = 0; i < list.size(); i++){
						Event e = list.get(i);
						s_name[i] = e.getName();
						s_address[i] = e.getLocation();
						s_date[i] = e.getStartDate() + " ～ " + e.getEndDate();
						s_description[i] = e.getDescription();
						s_url[i] = e.getEventUrl();
						String mes = (i+1) + "に投票する";
					%>
						<tr>
							<td><%= i+1 %></td>
							<td><%= s_name[i] %></td>
							<td>
								<%= s_description[i] %>
								<br>
								<a href="<%= s_url[i] %>"><%= s_url[i] %></a>
							</td>
							<td><%= s_address[i] %></td>
							<td><%= s_date[i] %></td>
							<td>
								<FORM method="GET" action="../ServletForResult/">
									<div align="center">
										<label>
											<input type="submit" value=<%= mes %>>
											<input type="hidden" name="name" value=<%= s_name[i] %> >
											<input type="hidden" name="address" value=<%= s_address[i] %> >
											<input type="hidden" name="event" value=<%= list.get(i) %> >
											<input type="hidden" name="date" value=<%= s_date[i] %> >
										</label>
									</div>
								</FORM>
							</td>
						</tr>
					<%
					}
				}
					%>
					</tbody>
				</table>
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