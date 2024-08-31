/*******************************************************************
*** File Name : sendCalendarInfo.js
*** Version : V1.0
*** Designer : 村田 悠真
*** Date : 2024/07/16
*** Purpose : 選択された予定情報をサーブレットに送信する
***
*******************************************************************/

var dayMinStr;
var dayMaxStr;

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

/* チェックボックスがチェックされた日付を取得する */
function getCalendarInfo() {
	var busyDays = dayMinStr + " " + dayMaxStr + ",";
	var checkboxes = document.querySelectorAll('input[type="checkbox"]:checked');
	checkboxes.forEach(checkbox => {
		busyDays += checkbox.value + " ";
	});

	// サーバーのURL
	const serverUrl = '/Nine2/servlet/CalendarServlet/';

	// POSTリクエストに含めるデータ
	const postData = `calendarInfo=${encodeURIComponent(busyDays.trim())}`;

	sendPostRequest(serverUrl, postData);
}

/* 30日分のチェックボックスを作成する関数 */
function generateCalender() {
	const container = document.getElementById('checkbox-container');
	container.innerHTML = '';

	// 現在の日付を取得
	const dayMin = new Date();
	dayMinStr = getFormattedDate(dayMin);
	const dayMax = new Date(dayMin);
	dayMax.setDate(dayMin.getDate() + 30);
	dayMaxStr = getFormattedDate(dayMax);

	// 30日分のチェックボックスを生成する
	let html = '';
	for (let i = 0; i <= 30; i++) {
		// 現在の日付をコピーして日付を進める
		const currentDate = new Date(dayMin);
		currentDate.setDate(dayMin.getDate() + i);

		const formattedDate = getFormattedDate(currentDate);

		html += `
            <div>
                <input type="checkbox" value="${formattedDate}">
                <label for="day-${i}">${formattedDate}</label>
            </div>
        `;
	}
	container.innerHTML = html;
}

/* Date型の日付をYYYY-MM-DD形式に成型する関数 */
function getFormattedDate(date) {

	const year = date.getFullYear();
	const month = String(date.getMonth() + 1).padStart(2, '0');
	const day = String(date.getDate()).padStart(2, '0');


	return `${year}-${month}-${day}`;
}

/* ページがロードされたときにチェックボックスを生成する */
document.addEventListener('DOMContentLoaded', generateCalender);
