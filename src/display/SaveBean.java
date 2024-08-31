/******************************************************************* 
***  File Name  : SaveBean.java 
***  Version  : v1.0
***  Designer  : 井上 泰輝 
***  Date   : 2024.07.10
***  Purpose        : 保存完了画面表示時のデータ保持用Bean
*** 
*******************************************************************/

package display;

import java.io.*;

public class SaveBean implements Serializable {
	String event;	//イベント名
	String name;	//店名
	String destination;	//行き先
	String date;		//日付
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDestination() {
		return destination;
	}
	
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	public String getEvent() {
		return event;
	}
	
	public void setEvent(String event) {
		this.event = event;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
		
		
}
