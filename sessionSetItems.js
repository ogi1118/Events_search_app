/*******************************************************************
*** File Name : sessionSetItems.js
*** Version : V1.0
*** Designer : 村田 悠真
*** Date : 2024/07/16
*** Purpose : ホーム画面に遷移する際に必要なデータをJavascriptのセッションに登録する
***
*******************************************************************/

/* ページ読み込み時，必要なデータをJavascriptのセッションに登録する処理 */
window.onload = function() {
	
	// 表示中のページのURLからクエリパラメータを取得
	const urlParams = new URLSearchParams(window.location.search);

	// URLから取得したパラメータ
	const userID = urlParams.get('userID');
	const displayName = urlParams.get('displayName');

  //セッションに登録する
	sessionStorage.setItem('userID', userID);
	sessionStorage.setItem('displayName', displayName);

	window.location.replace("/Nine2/Home.html");
	
}
