<!--*******************************************************************
*** File Name : checkVoteMember.jsp
*** Version : V1.0
*** Designer : 相内 優真
*** Date : 2024.6.30
*** Purpose : W7 投票内容画面(メンバー)を表示する
***
*******************************************************************-->

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<jsp:useBean id="vb" class="usedb.VoteBean" scope="request" />
<% List<List<String>> voterList = vb.getVoterList(); 
   String dateTime = vb.getDateTime();
   String genre = vb.getGenre();
   String budget1 = vb.getBudget1();
   String budget2 = vb.getBudget2();
   String review = vb.getReview();
   String month = dateTime.substring(5, 7);
   String date = dateTime.substring(8, 10); 
%>

<html>
    <head>
        <!--外部のCSSファイルを読み込み-->
        <link rel="stylesheet" href="/Nine2/pageStyle.css">
    </head>
    <body>
        <div class="header">
		<h1>投票内容確認画面</h1>
	    </div>
        <div class="container">
            <div class="flex-container">
                <div class="flex-item">
                    <div id="check">
                            <p>投票内容確認</p>
                            <p>日程： <%= month %>月<%= date %>日</p>

                            <!--投票した内容を表示する-->
                            ジャンル:
                            <%= genre %>
                            <br><br>

                            <% if(budget1 != null){
                                out.println("予算:" + budget1 + "～" + budget2 + "<br><br>" + "レビュー評価:" + review + "以上");
                            } %>
                            

                            <br>

                            <!--投票内容修正ボタンの追加-->
                            <% if(budget1 != null){
                                out.println("<button id=\"vote\" onclick=\"location.href=\'/Nine2/voteDest.html\'\">投票内容修正</button>");
                            }else{
                                out.println("<button id=\"vote\" onclick=\"location.href=\'/Nine2/voteEvent.html\'\">投票内容修正</button>");
                            } %>
                            <button id="backButton">ホームに戻る</button>
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
