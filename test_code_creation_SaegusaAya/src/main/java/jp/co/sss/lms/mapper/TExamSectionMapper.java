package jp.co.sss.lms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.entity.TExamSection;

/**
 * 試験・セクション紐付けテーブルマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface TExamSectionMapper {

	/**
	 * Task.116 試験・セクション紐付け件数取得
	 * 
	 * @param sectionId
	 * @param examId
	 * @param deleteFlg
	 * @return 件数
	 */
	Integer getSectionExamCount(@Param("sectionId") Integer sectionId,
			@Param("examId") Integer examId, @Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.116 試験・セクション紐付け登録
	 * 
	 * @param TExamSection
	 * @return 登録結果
	 */
	Boolean insert(TExamSection tExamSection);

	/**
	 * Task.116 試験・セクション紐付け更新
	 * 
	 * @param TExamSection
	 * @return 更新結果
	 */
	Boolean update(TExamSection tExamSection);

	/**
	 * Task.116 試験・セクション紐付け削除
	 * 
	 * @param tExamSection
	 * @return 削除結果
	 */
	Boolean delete(TExamSection tExamSection);

}
