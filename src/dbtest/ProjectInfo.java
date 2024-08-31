package dbtest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

/*
 * File Name	:ProjectInfo.java
 * Version		:Ver1.0
 * Designer		:荻野新
 * Date			:2024.06.16
 * Purpose		:企画の情報を扱うためのクラス
 * 
 * set
 * ProjectInfo project = new ProjectInfo();
 * project.projectID = ....
 * .
 * .
 * .
 * .
 * .
 * project.setProjectInfo();
 * 
 * 
 * get
 * ProjectInfo cons = new ProjectInfo();
 * ProjectInfo project = cons.getProjectInfo(projectID);
 * 
 */

public class ProjectInfo {
    public int projectID;
    public String projectName = "";
    public Timestamp dateTime;
    public String category = "";
    public String destination = "";
    public int managerID;
    public String region = "";
    public String progressStatus = "";
    public String genre = "";

    // DB接続のためのアドレスなど
    String server = "//172.27.0.15:5432/";
    String dataBase = "g9_db";
    String user = "group09";
    String passWord = "grp9";
    String url = "jdbc:postgresql:" + server + dataBase;

    // String server = "//172.21.40.30:5432/"; // seserverのIPアドレス
    // String dataBase = "firstdb";
    // String user = "shibaura";
    // String passWord = "toyosu";
    // String url = "jdbc:postgresql:" + server + dataBase;

    public ProjectInfo() {

    }

    public ProjectInfo(int projectID) {
        // DB接続
        try {

            Class.forName("org.postgresql.Driver");

            Connection con = DriverManager.getConnection(url, user, passWord);
            Statement stmt = con.createStatement();
            // 検索の実施と結果の格納
            String sql = "SELECT * FROM ProjectsTableNinth WHERE ProjectID = " + projectID;
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            this.projectID = rs.getInt("projectID");
            this.projectName = rs.getString("Name");
            this.dateTime = rs.getTimestamp("DateTime");
            this.category = rs.getString("Category");
            this.genre = rs.getString("Genre");
            this.destination = rs.getString("Destination");
            this.managerID = rs.getInt("ManagerID");
            this.region = rs.getString("Region");
            this.progressStatus = rs.getString("progressStatus");
            System.out.println("constractor");
            System.out.println(this.projectID);
            System.out.println(this.projectName);
            System.out.println(this.dateTime);
            System.out.println(this.region);

            stmt.close();
            con.close();
            System.out.println("return data.");
        } catch (Exception e) {
            System.out.println("Failed to fetch data.");
            e.printStackTrace();
        }
    }

    // 企画IDから企画情報を取得、projectinfoクラスのフィールドに保存するメソッド
    public ProjectInfo getProjectInfo(int projectID) {

        // DB接続
        try {
            ProjectInfo ret = new ProjectInfo();
            Class.forName("org.postgresql.Driver");
            try (Connection con = DriverManager.getConnection(url, user, passWord);
                    PreparedStatement pstmt = con
                            .prepareStatement("SELECT * FROM ProjectsTableNinth WHERE ProjectID = ?")) {

                pstmt.setInt(1, projectID);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        ret.projectID = rs.getInt("ProjectID");
                        ret.projectName = rs.getString("Name");
                        ret.dateTime = rs.getTimestamp("DateTime");
                        ret.category = rs.getString("Category");
                        ret.genre = rs.getString("Genre");
                        ret.destination = rs.getString("Destination");
                        ret.managerID = rs.getInt("ManagerID");
                        ret.region = rs.getString("Region");
                        ret.progressStatus = rs.getString("progressStatus");
                    }
                }
            }
            return ret;
        } catch (Exception e) {
            ProjectInfo ret = new ProjectInfo();
            e.printStackTrace();
            return ret;
        }
    }

    // ProjectInfoクラスのフィールドに格納されたデータをデータベースに登録するメソッド
    public void setProjectInfo() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(url, user, passWord);

            String sql = "INSERT INTO ProjectsTableNinth VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement prestmt = con.prepareStatement(sql);

            prestmt.setInt(1, projectID);
            prestmt.setString(2, projectName);
            prestmt.setTimestamp(3, dateTime);
            prestmt.setString(4, category);
            prestmt.setString(5, destination);
            prestmt.setInt(6, managerID);
            prestmt.setString(7, region);
            prestmt.setString(8, progressStatus);
            prestmt.setString(9, genre);

            prestmt.executeUpdate();
            prestmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // projectIDを引数に、そのIDに紐づくデータを全部消すメソッド
    public void deleteProjectInfo(int projectID) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(url, user, passWord);

            String sql2 = "DELETE FROM userandProjectsdetailsTableNinth WHERE projectID=" + projectID;
            Statement stmt2 = con.createStatement();
            stmt2.execute(sql2);

            stmt2.close();

            String sql1 = "DELETE FROM ProjectsTableNinth WHERE projectID=" + projectID + " ";
            Statement stmt1 = con.createStatement();
            stmt1.execute(sql1);

            stmt1.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // テーブルを更新するメソッド
    public void updateProjectInfo() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(url, user, passWord);

            String sql = "UPDATE ProjectsTableNinth SET Name = ?, dateTime = ?, Genre = ?, destination = ?, managerID = ?, region = ?, progressStatus = ? WHERE projectID = ?";
            PreparedStatement prestmt = con.prepareStatement(sql);

            prestmt.setString(1, projectName);
            prestmt.setTimestamp(2, dateTime);
            prestmt.setString(3, genre);
            prestmt.setString(4, destination);
            prestmt.setInt(5, managerID);
            prestmt.setString(6, region);
            prestmt.setString(7, progressStatus);
            prestmt.setInt(8, projectID);

            prestmt.executeUpdate();
            prestmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ProjectID、更新するフィールド、更新する値を渡すとテーブルを更新するメソッド
    public void updateProjectInfo(int projectID, String updateField, String value) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(url, user, passWord);

            String sql = "UPDATE ProjectsTableNinth SET " + updateField + "=? WHERE ProjectID=" + projectID;
            PreparedStatement prestmt = con.prepareStatement(sql);

            prestmt.setString(1, value);

            prestmt.executeUpdate();
            prestmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateProjectInfo(int projectID, String updateField, Timestamp value) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(url, user, passWord);

            String sql = "UPDATE ProjectsTableNinth SET " + updateField + "=? WHERE ProjectID=" + projectID;
            PreparedStatement prestmt = con.prepareStatement(sql);

            prestmt.setTimestamp(1, value);

            prestmt.executeUpdate();
            prestmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}