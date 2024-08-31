/*******************************************************************
*** File Name : sendNickName.js
*** Version : V1.1
*** Designer : 村田 悠真
*** Date : 2024/07/02
*** Purpose : テキストフィールドに入力されたニックネームをサーブレットに送信する
***
*******************************************************************/
/*
*** Revision :
*** V1.0 : 村田悠真, 2024.06.27
*** V1.1 : 村田悠真, 2024.07.02 sendNickName
*/

/* POSTリクエストを送信する関数 */
function sendPostRequest(url, data) {
  /* POSTリクエストの送信 */
  fetch(url, {
      method: 'POST',
      headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: data
  })
  .then(response => {
      if (!response.ok) {
          throw new Error('Network response was not ok');
      }
      // サーブレットから受け取ったリダイレクト先に遷移
      window.location.replace(response.url);
  })
  .catch(error => {
      // エラーページに移動，ログイン処理再試行
      window.location.replace('/Nine2/loginError2.html');
  });
}

/* サーブレットにニックネームを送信する関数 */
function sendNickName(){
  // テキストフィールドから取得したニックネーム
  var nickName = document.getElementById('nickName').value;

  if (nickName.length > 0 && nickName.length < 17){
    // サーバーのURL
    const serverUrl = '/Nine2/servlet/NickNameServlet/';

  	// POSTリクエストに含めるデータ
  	const postData = `nickName=${nickName}`;

    /* POSTリクエストを送信 */
    sendPostRequest(serverUrl, postData);
  }
  /* 入力が適切でない場合 */
  else {
    var errorMessage= document.getElementById("errorMessage");  
    errorMessage.innerHTML= "ニックネームが適切ではありません";
  }
}

/* ページ読み込み時，すでに登録済みのニックネームをテキストフィールドにセットする関数 */
window.onload = function() {
  // 表示中のページのURL
  const url = window.location.search;

  // URLから取得したパラメータ
  const params = new URLSearchParams(url);

  // パラメータのうち，nickName
  let nickName = params.get('nickName');

  // テキストフィールドにセット
  document.getElementById('nickName').value = nickName;
}

