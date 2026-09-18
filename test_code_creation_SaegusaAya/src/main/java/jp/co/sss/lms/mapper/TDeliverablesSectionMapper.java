package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.DeliverablesWithSubmissionFlgDto;

/**
 * 成果物・セクション紐付けテーブルマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface TDeliverablesSectionMapper {

	/**
	 * 成果物サービスDTOリスト取得
	 * 
	 * @param sectionId
	 * @param lmsUserId
	 * @param deleteFlg
	 * @return 成果物サービスDTOリスト
	 */
	List<DeliverablesWithSubmissionFlgDto> getDeliverablesWithSubmissionFlgDtoList(
			@Param("sectionId") Integer sectionId, @Param("lmsUserId") Integer lmsUserId,
			@Param("deleteFlg") short deleteFlg);

}
