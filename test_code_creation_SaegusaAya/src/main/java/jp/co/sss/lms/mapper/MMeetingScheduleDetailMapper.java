package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.TakeOverScheduleDetailDto;
import jp.co.sss.lms.entity.MMeetingScheduleDetail;

/**
 * 面談スケジュール詳細マスタマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface MMeetingScheduleDetailMapper {

	/**
	 * Task.73 引継面談／会場見学スケジュール詳細DTOリスト取得
	 * 
	 * @param meetingScheduleId
	 * @param deleteFlg
	 * @return 引継面談／会場見学スケジュール詳細DTOリスト
	 */
	List<TakeOverScheduleDetailDto> getTakeOverScheduleDetailDtoList(
			@Param("meetingScheduleId") Integer meetingScheduleId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.108 面談スケジュール詳細削除
	 * 
	 * @param meetingScheduleId
	 * @return 削除結果
	 */
	Boolean delete(@Param("meetingScheduleId") Integer meetingScheduleId);

	/**
	 * Task.108 面談スケジュール詳細件数取得
	 * 
	 * @param meetingScheduleId
	 * @param deleteFlg
	 * @return 件数
	 */
	Integer getMeetingScheduleDetailCount(@Param("meetingScheduleId") Integer meetingScheduleId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.111 面談スケジュール詳細登録
	 * 
	 * @param mMeetingScheduleDetail
	 * @return 登録結果
	 */
	Boolean insert(MMeetingScheduleDetail mMeetingScheduleDetail);

}
