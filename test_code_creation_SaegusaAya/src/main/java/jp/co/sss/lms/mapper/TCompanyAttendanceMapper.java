package jp.co.sss.lms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.entity.TCompanyAttendance;

/**
 * Task.58 勤怠情報（企業入力）テーブルマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface TCompanyAttendanceMapper {

	/**
	 * Task.58 勤怠情報（企業入力）取得（企業入力勤怠情報ID）
	 * 
	 * @param companyAttendanceId
	 * @param deleteFlg
	 * @return 勤怠情報（企業入力）エンティティ
	 */
	TCompanyAttendance findByCompanyAttendanceId(
			@Param("companyAttendanceId") Integer companyAttendanceId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.58 勤怠情報（企業入力）登録
	 * 
	 * @param tCompanyAttendance
	 * @return 登録結果
	 */
	Boolean insert(TCompanyAttendance tCompanyAttendance);

	/**
	 * Task.58 勤怠情報（企業入力）更新
	 * 
	 * @param tCompanyAttendance
	 * @return 更新結果
	 */
	Boolean update(TCompanyAttendance tCompanyAttendance);

}
