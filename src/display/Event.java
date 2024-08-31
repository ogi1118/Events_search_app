/*
 * File Nmae			:Event.java
 * Verision				:Ver1.0
 * Designer				:荻野新
 * 
 * Purpose				:イベント情報を保持するクラス
 * 
 */
package display;

public class Event {
	String name = "";
	String startDate = "";
	String endDate = "";
	String location = "";
	String description = "";
	String eventUrl = "";
	
	public String getName() {
        return name;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    // セッターメソッド
    public void setName(String name) {
        this.name = name;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }
}