package dbtest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/*
 * File Name	:UserAndProjectInfo.java
 * Version		:Ver1.0
 * Designer		:相内 優真
 * Date			:2024.06.18
 * Purpose		:企画とユーザーの情報を扱うためのクラス
 * 
 *
 * 
 */

public class UserAndProjectInfo {
	public int projectID;
	public int userID;
	public String genre = null;
	public String budget1 = null;
	public String budget2 = null;
	public String review = null;
	public List<String> genreList = new ArrayList<String>();
	public List<String> budget1List = new ArrayList<String>();
	public List<String> budget2List = new ArrayList<String>();
	public List<String> reviewList = new ArrayList<String>();

	// DB接続のためのアドレスなど
    String server = "//172.27.0.15:5432/"; 
	String dataBase = "g9_db";
	String user = "group09";
	String passWord = "grp9";
	String url = "jdbc:postgresql:" + server + dataBase;

	/*String server = "//172.21.40.30:5432/"; // seserverのIPアドレス
	String dataBase = "firstdb";
	String user = "shibaura";
	String passWord = "toyosu";
	String url = "jdbc:postgresql:" + server + dataBase;*/

	// 企画IDとユーザIDから投票情報を取得、UserAndProjectInfoクラスのオブジェクトに保存するメソッド
	public UserAndProjectInfo getVoteInfo(int projectID) {

		// DB接続
		try {
			UserAndProjectInfo ret = new UserAndProjectInfo();

			Class.forName("org.postgresql.Driver");

			Connection con = DriverManager.getConnection(url, user, passWord);
			Statement stmt = con.createStatement();
			// 検索の実施と結果の格納
			String sql = "SELECT * FROM UserAndProjectsDetailsTableNinth WHERE ProjectID=" + projectID;
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				if (rs.getString("Genre") != null) {
					ret.genreList.add(rs.getString("Genre"));
				}
				if (rs.getString("Budget1") != null) {
					ret.budget1List.add(rs.getString("Budget1"));
				}
				if (rs.getString("Budget2") != null) {
					ret.budget2List.add(rs.getString("Budget2"));
				}
				if (rs.getString("Review") != null) {
					ret.reviewList.add(rs.getString("Review"));
				}
			}

			stmt.close();
			con.close();

			return ret;
		} catch (Exception e) {
			UserAndProjectInfo ret = new UserAndProjectInfo();
			e.printStackTrace();
			return ret;
		}
	}

	// 企画IDから投票情報を取得、UserAndProjectInfoクラスのオブジェクトに保存するメソッド
	public UserAndProjectInfo getVoteInfo(int projectID, int userID) {

		// DB接続
		try {
			UserAndProjectInfo ret = new UserAndProjectInfo();

			Class.forName("org.postgresql.Driver");

			Connection con = DriverManager.getConnection(url, user, passWord);
			Statement stmt = con.createStatement();
			// 検索の実施と結果の格納
			String sql = "SELECT * FROM UserAndProjectsDetailsTableNinth WHERE ProjectID=" + projectID + " AND UserID="
					+ userID;
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			ret.genre = rs.getString("Genre");
			ret.budget1 = rs.getString("Budget1");
			ret.budget2 = rs.getString("Budget2");
			ret.review = rs.getString("Review");

			stmt.close();
			con.close();

			return ret;
		} catch (Exception e) {
			UserAndProjectInfo ret = new UserAndProjectInfo();
			e.printStackTrace();
			return ret;
		}
	}

	// useIDとprojectIDを紐づけてデータベースに保存するメソッド
	public void setUserAndProjectInfo() {
		try {
			Class.forName("org.postgresql.Driver");
			Connection con = DriverManager.getConnection(url, user, passWord);

			String sql = "INSERT INTO UserAndProjectsDetailsTableNinth VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement prestmt = con.prepareStatement(sql);

			prestmt.setInt(1, userID);
			prestmt.setInt(2, projectID);
			prestmt.setString(3, null);
			prestmt.setString(4, null);
			prestmt.setString(5, null);
			prestmt.setString(6, null);

			prestmt.executeUpdate();
			prestmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//userIDとprojectIDから投票データを更新するメソッド
	public void updateVoteInfo(int userID, int projectID) {
		try {
			Class.forName("org.postgresql.Driver");
			Connection con = DriverManager.getConnection(url, user, passWord);

			String sql = "UPDATE UserAndProjectsDetailsTableNinth SET genre=?, budget1=?, budget2=?, review=? WHERE UserID=? AND projectID=?";
			PreparedStatement prestmt = con.prepareStatement(sql);

			prestmt.setString(1, genre);
			prestmt.setString(2, budget1);
			prestmt.setString(3, budget2);
			prestmt.setString(4, review);
			prestmt.setInt(5, userID);
			prestmt.setInt(6, projectID);

			prestmt.executeUpdate();
			prestmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//userIDとprojectIDからテーブルを削除するメソッド
	public void deleteUserAndProjectInfo(int userID, int projectID) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(url, user, passWord);
            
            String sql = "DELETE FROM UserAndProjectsDetailsTableNinth WHERE ProjectID=" + projectID + " AND UserID=" + userID;
            Statement stmt = con.createStatement();
            stmt.execute(sql);
            
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
