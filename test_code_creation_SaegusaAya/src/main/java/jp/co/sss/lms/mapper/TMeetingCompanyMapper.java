package jp.co.sss.lms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.entity.TMeetingCompany;

/**
 * 面談対象企業テーブルマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface TMeetingCompanyMapper {

	/**
	 * Task.74 面談対象企業変更
	 * 
	 * @param tMeetingCompany
	 * @return 変更結果
	 */
	Boolean update(TMeetingCompany tMeetingCompany);

	/**
	 * Task.108 面談対象企業件数取得
	 * 
	 * @param meetingPlaceId
	 * @param deleteFlg
	 * @return 件数
	 */
	Integer getMeetingCompanyCount(@Param("meetingPlaceId") Integer meetingPlaceId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.108 面談対象企業削除
	 * 
	 * @param meetingPlaceId
	 * @return 変更結果
	 */
	Boolean delete(@Param("meetingPlaceId") Integer meetingPlaceId);

	/**
	 * Task.110 面談対象企業登録
	 * 
	 * @param tMeetingCompany
	 * @return 登録結果
	 */
	Boolean insert(TMeetingCompany tMeetingCompany);

}
