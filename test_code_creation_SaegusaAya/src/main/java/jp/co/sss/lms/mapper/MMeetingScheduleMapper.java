package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.TakeOverScheduleDto;
import jp.co.sss.lms.entity.MMeetingSchedule;

/**
 * 面談スケジュールマスタマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface MMeetingScheduleMapper {

	/**
	 * Task.59 引継面談／会場見学 スケジュールDTOリスト取得
	 * 
	 * @param placeId
	 * @param publishedFlg
	 * @param deleteFlg
	 * @return 引継面談／会場見学スケジュールDTOリスト
	 */
	// Task.108 引数にpublishedFlgを追加
	List<TakeOverScheduleDto> getTakeOverScheduleDtoList(@Param("placeId") Integer placeId,
			@Param("publishedFlg") Short publishedFlg, @Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.108 削除フラグ更新
	 * 
	 * @param mMeetingSchedule
	 * @return 削除結果
	 */
	Boolean updateDeleteFlg(MMeetingSchedule mMeetingSchedule);

	/**
	 * Task.110 面談スケジュールDTO取得
	 * 
	 * @param meetingScheduleId
	 * @param deleteFlg
	 * @return 面談スケジュールDTO
	 */
	TakeOverScheduleDto getTakeOverScheduleDto(
			@Param("meetingScheduleId") Integer meetingScheduleId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.110 面談スケジュール登録
	 * 
	 * @param mMeetingSchedule
	 * @return 登録結果
	 */
	Boolean insert(MMeetingSchedule mMeetingSchedule);

}
