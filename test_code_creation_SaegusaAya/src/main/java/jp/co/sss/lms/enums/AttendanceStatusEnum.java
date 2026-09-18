package jp.co.sss.lms.enums;

/**
 * 勤怠管理画面用ステータスクラス
 * 
 * @author 東京ITスクール
 *
 */
public enum AttendanceStatusEnum {
	NONE((short) 0, ""), ABSENT((short) 1, "欠席"), TARDY((short) 2, "遅刻"), LEAVING_EARLY((short) 3, "早退"),
	TARDY_AND_LEAVING_EARLY((short) 4, "遅刻＆早退"),;
	public Short code;
	public String name;

	private AttendanceStatusEnum(Short code, String name) {
		this.code = code;
		this.name = name;
	}

	public static AttendanceStatusEnum getEnum(Short code) {
		for (AttendanceStatusEnum e : AttendanceStatusEnum.values()) {
			if (e.code.equals(code)) {
				return e;
			}
		}
		return null;
	}
}