package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.entity.TCourseMeeting;

/**
 * コース・面談ファイル付けテーブルマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface TCourseMeetingMapper {

	/**
	 * Task.106 コース・面談ファイル紐付け取得（面談ファイルID）
	 * 
	 * @param meetingFileId
	 * @param deleteFlg
	 * @return コース・面談ファイル付けエンティティリスト
	 */
	List<TCourseMeeting> findByCourseMeetingId(@Param("meetingFileId") Integer meetingFileId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.114 コース・面談ファイル紐付け登録
	 * 
	 * @param tCourseMeeting
	 * @return 登録結果
	 */
	Boolean insert(TCourseMeeting tCourseMeeting);

	/**
	 * Task.114 コース・面談ファイル紐付け削除
	 * 
	 * @param courseId
	 * @return 削除結果
	 */
	Boolean delete(Integer courseId);

}
