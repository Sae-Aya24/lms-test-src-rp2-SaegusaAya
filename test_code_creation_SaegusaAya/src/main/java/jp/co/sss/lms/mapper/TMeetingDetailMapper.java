package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.MeetingDetailDto;
import jp.co.sss.lms.entity.TMeetingDetail;

/**
 * Task.46 面談詳細テーブルマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface TMeetingDetailMapper {

	/**
	 * Task.46 面談詳細削除フラグ更新
	 * 
	 * @param tMeetingDetail
	 * @return 削除結果
	 */
	Boolean updateDeleteFlg(TMeetingDetail tMeetingDetail);

	/**
	 * Task.47 面談詳細DTOリスト取得
	 * 
	 * @param meetingId
	 * @param deleteFlg
	 * @return 面談詳細DTO
	 */
	List<MeetingDetailDto> getMeetingDetailDtoList(@Param("meetingId") Integer meetingId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.47 面談詳細削除
	 * 
	 * @param meetingId
	 * @return 削除結果
	 */
	Boolean delete(@Param("meetingId") Integer meetingId);

	/**
	 * Task.47 面談詳細登録
	 * 
	 * @param tMeetingDetail
	 * @return 登録結果
	 */
	Boolean insert(TMeetingDetail tMeetingDetail);
}
