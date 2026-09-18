package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.MeetingDownloadDto;
import jp.co.sss.lms.dto.MeetingDto;
import jp.co.sss.lms.entity.TMeeting;

/**
 * 面談テーブルマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface TMeetingMapper {

	/**
	 * 面談ダウンロードDTO取得
	 * 
	 * @param meetingId
	 * @param deleteFlg
	 * @return 面談ダウンロードDTO
	 */
	MeetingDownloadDto getMeetingDownloadDto(@Param("meetingId") Integer meetingId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.46 面談DTOリスト取得
	 * 
	 * @param lmsUserId
	 * @param deleteFlg
	 * @return 面談DTOリスト
	 */
	List<MeetingDto> getMeetingDtoList(@Param("lmsUserId") Integer lmsUserId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.46 面談削除フラグ更新
	 * 
	 * @param tMeeting
	 * @return 削除結果
	 */
	Boolean updateDeleteFlg(TMeeting tMeeting);

	/**
	 * Task.47 面談登録
	 * 
	 * @param tMeeting
	 * @return 登録結果
	 */
	Boolean insert(TMeeting tMeeting);

}
