/*******************************************************************
*** File Name : sendEmail.js
*** Version : V1.1
*** Designer : 村田 悠真
*** Date : 2024/07/23
*** Purpose : テキストフィールドに入力されたメールアドレスをサーブレットに送信する
***
*******************************************************************/
/*
*** Revision :
*** V1.0 : 村田悠真, 2024.07.16
*** V1.1 : 村田悠真, 2024.07.23 sendEmail
*/

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
			window.location.replace(response.url);
		})
		.catch(error => {
			// エラーページに移動，ログイン処理再試行
			window.location.replace('/Nine2/loginError2.html');
		});
}

/* サーブレットにニックネームを送信する関数 */
function sendEmail() {
	// テキストフィールドから取得したニックネーム
	var email = document.getElementById('email').value;
  if (email.indexOf('@') != -1){
  	const serverUrl = '/Nine2/servlet/LoginServlet/';
    // POSTリクエストに含めるデータ
    const postData = `email=${email}`;

    /* POSTリクエストを送信 */
    sendPostRequest(serverUrl, postData);
      
  }
  /* 入力が適切でない場合 */
  else {
    var errorMessage= document.getElementById("errorMessage");  
    errorMessage.innerHTML= "「@」を含んだメールアドレスを入力してください";
  }
}
