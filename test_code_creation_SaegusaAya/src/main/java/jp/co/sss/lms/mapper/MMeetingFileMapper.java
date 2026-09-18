package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.MeetingFileDto;
import jp.co.sss.lms.entity.MMeetingFile;

/**
 * 面談ファイルマスタマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface MMeetingFileMapper {

	/**
	 * Task.106 面談ファイルDTOリスト取得
	 * 
	 * @param meetingFileId
	 * @param fileName
	 * @param deleteFlg
	 * @return 面談ファイルDTOリスト
	 */
	List<MeetingFileDto> getMeetingFilleDtoList(@Param("meetingFileId") Integer meetingFileId,
			@Param("fileName") String fileName, @Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.106 面談ファイル更新削除
	 * 
	 * @param mMeetingFile
	 * @return 更新結果
	 */
	Boolean updateDeleteFlg(MMeetingFile mMeetingFile);

	/**
	 * Task.107 面談ファイルDTO取得
	 * 
	 * @param meetingFileId
	 * @param deleteFlg
	 * @return 面談ファイルエンティティ
	 */
	MMeetingFile findByMeetingFileId(@Param("meetingFileId") Integer meetingFileId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.107 面談ファイル登録
	 * 
	 * @param mMeetingFile
	 * @return 登録結果
	 */
	Boolean insert(MMeetingFile mMeetingFile);

	/**
	 * Task.107 面談ファイル更新
	 * 
	 * @param mMeetingFile
	 * @return 更新結果
	 */
	Boolean update(MMeetingFile mMeetingFile);

}
