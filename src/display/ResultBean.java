/******************************************************************* 
***  File Name  : ResultBean.java 
***  Version  : v1.0
***  Designer  : 井上 泰輝 
***  Date   : 2024.07.09
***  Purpose        : 選択結果表示時のデータ保持用Bean
*** 
*******************************************************************/

package display;

import java.io.*;

public class ResultBean implements Serializable {
	String eventName;	//イベント名
	String rating;		//評価値
	String name;		//店名
	String address;		//住所
	String date;		//日付
	
	public String getRating() {
		return rating;
	}
	
	public void setRating(String rating) {
		this.rating = rating;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getEvent() {
		return eventName;
	}
	
	public void setEvent(String eventName) {
		this.eventName = eventName;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
		
		
}
