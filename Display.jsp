<!--
  File Name: Display.jsp
  Version: V1.2
  Designer: 井上泰輝
  Date: 07/12
  Purpose:  ServletForDisplay.javaから渡された投票結果のListを
  			表形式で表示し選択された内容をServletForResultにGETで渡す
-->


<%@ page contentType="text/html; charset=UTF-8" import="java.util.*" %>
	<jsp:useBean id="display" class="display.DisplayBean" scope="session" />
	<html lang="ja">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

		<title>検索結果表示</title>
		<style>
			table {
				width: 50%;
				border-collapse: collapse;
				margin: 20px;
				font-size: 18px;
				text-align: left;
			}

			th,
			td {
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
					<h1>
						<jsp:getProperty name="display" property="name" />
						の投票結果に基づく検索結果表示
					</h1>
					<% List<String[]> list = display.getList();
						String[] temp;
						String[] s_rating = new String[5];
						String[] s_address = new String[5];
						String[] s_name = new String[5];
						String event = display.getName();
						String date = display.getDate();
						%>
						<table>
							<thead>
								<tr>
									<th>評価順位</th>
									<th>評価値</th>
									<th>店・施設名</th>
									<th>住所</th>
									<th>投票</th>
								</tr>
							</thead>
							<tbody>
								<% 
								if(list.size()==0){ %>
									<p class="caution">条件
										<jsp:getProperty name="display" property="name" />
										の店舗は存在しませんでした。ボタンより前のページに戻って投票内容を修正してください。<br>参加者にも投票修正を呼びかけてください。
									</p>
									<button id="vote" onclick="location.href='/Nine2/voteDest.html'">戻る</button>
									<%
								}else{ 
									for (int i=0; i < list.size(); i++){
										temp=list.get(i);
										s_rating[i]=temp[2];
										s_name[i]=temp[0];
										s_address[i]=temp[1];
										String tier = (i + 1) + "位";
										String mes=(i+1)+ "に決定する" ; %>
										<tr>
											<td>
												<%= tier %>
											</td>
											<td>
												<%= s_rating[i] %>
											</td>
											<td>
												<%= s_name[i] %>
											</td>
											<td>
												<%= s_address[i] %>
											</td>
											<td>
												<FORM method="GET" action="/Nine2/servlet/ServletForResult/">
													<div align="center">
														<label>
															<input type="submit" value=<%=mes %>>
															<input type="hidden" name="rating" value=<%= s_rating[i] %>/>
															<input type="hidden" name="name" value=<%= s_name[i] %> />
															<input type="hidden" name="address" value=<%= s_address[i] %> />
															<input type="hidden" name="event" value=<%= event %> />
															<input type="hidden" name="date" value=<%= date %> />
															<% System.out.println("s_name:" + s_name[i]); %>
														</label>
													</div>
												</FORM>
											</td>
										</tr>
										<% }
									} %>
					
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
