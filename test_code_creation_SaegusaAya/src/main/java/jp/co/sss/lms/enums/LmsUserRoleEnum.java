package jp.co.sss.lms.enums;

import jp.co.sss.lms.util.Constants;

/**
 * 権限名称変換
 * 
 * @author 東京ITスクール
 */
public enum LmsUserRoleEnum {
	STUDENT(Constants.CODE_VAL_ROLL_STUDENT, "受講生"), TEACHER(Constants.CODE_VAL_ROLL_TEACHER, "講師"),
	COMPANY_SUBSIDY(Constants.CODE_VAL_ROLL_COMPANY, "企業担当者"), ADMIN((Constants.CODE_VAL_ROLL_ADMIN), "管理者"),
	COMPANY_TRAINING(Constants.CODE_VAL_ROLL_TRAINING, "育成担当者"),;

	public String code;
	public String value;

	private LmsUserRoleEnum(String code, String value) {
		this.code = code;
		this.value = value;
	}

	public static LmsUserRoleEnum getEnum(String code) {
		for (LmsUserRoleEnum e : LmsUserRoleEnum.values()) {
			if (e.code.equals(code)) {
				return e;
			}
		}
		return null;
	}

}
