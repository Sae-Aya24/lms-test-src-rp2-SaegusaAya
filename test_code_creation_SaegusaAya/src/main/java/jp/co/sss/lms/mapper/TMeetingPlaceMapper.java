package jp.co.sss.lms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.entity.TMeetingPlace;

/**
 * 面談対象会場テーブルマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface TMeetingPlaceMapper {

	/**
	 * Task.108 面談対象会場件数取得
	 * 
	 * @param meetingScheduleId
	 * @param deleteFlg
	 * @return 件数
	 */
	Integer getMeetingPlaceCount(@Param("meetingScheduleId") Integer meetingScheduleId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.108 公開フラグ更新
	 * 
	 * @param tMeetingPlace
	 * @return 更新結果
	 */
	Boolean updatePublishedFlg(TMeetingPlace tMeetingPlace);

	/**
	 * Task.108 面談対象会場削除
	 * 
	 * @param tMeetingPlace
	 * @return 変更結果
	 */
	Boolean delete(@Param("meetingScheduleId") Integer meetingScheduleId);

	/**
	 * Task.110 面談対象会場ID取得
	 * 
	 * @param meetingScheduleId
	 * @param deleteFlg
	 * @return 面談対象会場ID
	 */
	Integer getMeetingPlaceId(@Param("meetingScheduleId") Integer meetingScheduleId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.110 面談対象会場登録
	 * 
	 * @param tMeetingPlace
	 * @return 登録結果
	 */
	Boolean insert(TMeetingPlace tMeetingPlace);

}
