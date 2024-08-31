import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import dbtest.ProjectInfo;
import dbtest.UserAndProjectInfo;
/**
 * File Name: JoinRegister.java
 * Version: V1.0
 * Designer: 新保陽己
 * Date: 07/223
 * Purpose: UIサーバよりデータを受け取り、C8 企画情報管理部へ送信する。
 *          通信失敗時には、通信エラーが起きたことを報告し、W2ホーム画面に戻る。
 */
public class JoinRegister {
	public String sendData(int projectID, String displayName, int userID) {
		String string = "";
		System.out.println(projectID);

		for (JSONObject data : MyServlet.jsonDataList) {
			Number existingprojectIDNumber = (Number) data.get("projectID");
			int existingProjectID = existingprojectIDNumber.intValue();
			if (existingProjectID == projectID) {
				// Add participants array if it does not exist
				if (!data.containsKey("participants")) {
					data.put("participants", new JSONArray());
				}

				// Get the participants array
				JSONArray participantsArray = (JSONArray) data.get("participants");

				// Check if the participant already exists
				boolean participantExists = false;
				for (Object obj : participantsArray) {
					JSONObject participant = (JSONObject) obj;
					if ((Integer) participant.get("userID") == userID) {
						participantExists = true;
						break;
					}
				}

				// Add the participant if they do not already exist
				if (!participantExists) {
					ProjectInfo cons = new ProjectInfo();
					ProjectInfo project = cons.getProjectInfo(projectID);
					string = project.progressStatus;
					if("Registration".equals(string)){
						JSONObject participant = new JSONObject();
						participant.put("displayName", displayName);
						participant.put("userID", userID);
						participantsArray.add(participant);

						UserAndProjectInfo userproject = new UserAndProjectInfo();
						userproject.userID = userID;
						userproject.projectID = projectID;
						userproject.setUserAndProjectInfo();
					}

				}
				break;
			}
		}
		return string;
	}
	public void disposeData(JSONObject requestBody) {
		int projectID = Integer.parseInt((String) requestBody.get("projectID"));
		int userID = Integer.parseInt((String) requestBody.get("userID"));
		
		//userIDとprojectIDを持つデータをDBから削除する。
		UserAndProjectInfo userAndProjectInfo = new UserAndProjectInfo();
		userAndProjectInfo.deleteUserAndProjectInfo(userID, projectID);
		
		 // jsonDataListからprojectIDを持つJSONObjectを検索
        for (JSONObject data : (Iterable<JSONObject>) MyServlet.jsonDataList) {
            Number existingProjectIDNumber = (Number) data.get("projectID");
            int existingProjectID = existingProjectIDNumber.intValue();
            if (existingProjectID == projectID) {
                // participants配列からuserIDを持つ要素を検索
                JSONArray participantsArray = (JSONArray) data.get("participants");
                if (participantsArray != null) {
                    Iterator<JSONObject> iterator = participantsArray.iterator();
                    while (iterator.hasNext()) {
                        JSONObject participant = (JSONObject) iterator.next();
                        if (((Number) participant.get("userID")).intValue() == userID) {
                            // userIDを持つ要素を削除
                            iterator.remove();
                            break;
                        }
                    }
                }
                break;
            }
        }
        
	}
}
