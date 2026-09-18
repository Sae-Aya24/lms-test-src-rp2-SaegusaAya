package jp.co.sss.lms.form;

import java.util.LinkedHashMap;
import java.util.List;

import lombok.Data;

/**
 * 勤怠フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class AttendanceForm {

	/** LMSユーザーID */
	private Integer lmsUserId;
	/** グループID */
	private Integer groupId;
	/** 年間計画No */
	private String nenkanKeikakuNo;
	/** ユーザー名 */
	private String userName;
	/** 退校フラグ */
	private Integer leaveFlg;
	/** 退校日 */
	private String leaveDate;
	/** 退校日（表示用） */
	private String dispLeaveDate;
	/** 中抜け時間マップ */
	private LinkedHashMap<Integer, String> blankTimes;
	/** 時間マップ */
	private LinkedHashMap<Integer, String> hourMap; // Task.26
	/** 分マップ */
	private LinkedHashMap<Integer, String> minuteMap; // Task.26
	/** 日次の勤怠フォームリスト */
	private List<DailyAttendanceForm> attendanceList;

}
