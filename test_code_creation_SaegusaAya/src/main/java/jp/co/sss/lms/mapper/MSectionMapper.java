package jp.co.sss.lms.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.SectionServiceSectionDto;
import jp.co.sss.lms.entity.MSection;

/**
 * セクションマスタマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface MSectionMapper {

	/**
	 * セクションサービスDTO取得
	 * 
	 * @param sectionId
	 * @param accountId
	 * @param lmsUserId
	 * @param deleteFlg
	 * @return セクションサービスDTO
	 */
	SectionServiceSectionDto getSectionDetail(@Param("sectionId") Integer sectionId,
			@Param("accountId") Integer accountId, @Param("lmsUserId") Integer lmsUserId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * セクション件数取得
	 * 
	 * @param sectionId
	 * @return セクション件数
	 */
	Integer getSectionCount(@Param("sectionId") Integer sectionId);

	/**
	 * セクション件数取得（コースID）
	 * 
	 * @param courseId
	 * @param date
	 * @return セクション件数
	 */
	Integer getSectionCountByCourseId(@Param("courseId") Integer courseId,
			@Param("date") Date date);

	/**
	 * Task.115 セクション登録
	 * 
	 * @param mSection
	 * @return 登録結果
	 */
	Boolean insert(MSection mSection);

	/**
	 * Task.115 セクション削除
	 * 
	 * @param courseId
	 * @return 更新結果
	 */
	Boolean delete(Integer courseId);

	/**
	 * Task.116 セクション更新
	 * 
	 * @param mSection
	 * @return 更新結果
	 */
	Boolean update(MSection mSection);

}
