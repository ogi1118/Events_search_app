/******************************************************************* 
***  File Name  : DisplayEventsBean.java 
***  Version  : v1.1
***  Designer  : 井上 泰輝 
***  Date   : 2024.07.09
***  Purpose        : イベント検索結果表示時のデータ保持用Bean
*** 
*******************************************************************/

package display;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dbtest.ProjectInfo;
import dbtest.UserAndProjectInfo;

public class DisplayEventsBean implements Serializable {
	public List<Event> event;	//検索結果
	public String name;			//企画名
	public String date;			//日付
	public String url;
	public String category;		//検索するイベントのカテゴリ
	
	public List<Event> getList() {
		return event;
	}
	
	public void setList(List<Event> event) {
		this.event = event;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	//検索してその結果をリストで返すメソッド
	public void makeList(int KEY, String rpath, String gpath) {
		SearchEvents sea = new SearchEvents(rpath, gpath);
		//データベースから投票結果を取得
		ProjectInfo pi = new ProjectInfo(KEY);
		this.name = pi.projectName;
		this.date = "" + pi.dateTime;
		
		UserAndProjectInfo uapi = new UserAndProjectInfo();
		UserAndProjectInfo UAPI = uapi.getVoteInfo(KEY);
		
		String category = DisplayBean.findMode(UAPI.genreList);
		setCategory(category);
		pi.genre = category;
		pi.updateProjectInfo();
		//検索
		ArrayList<Event> events = sea.search(pi.region, pi.genre, pi.dateTime.toString());
		this.event = events;
	}
	
}














