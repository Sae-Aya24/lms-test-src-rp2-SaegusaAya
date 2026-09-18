package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.FaqCategoryDto;
import jp.co.sss.lms.entity.MFrequentlyAskedQuestionCategory;

/**
 * Task.11 よくある質問カテゴリマスタマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface MFrequentlyAskedQuestionCategoryMapper {

	/**
	 * Task.11 よくある質問カテゴリDTOリスト取得
	 * 
	 * @param deleteFlg
	 * @return よくある質問カテゴリDTOリスト
	 */
	List<FaqCategoryDto> getFaqCategoryDtoList(@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.89 よくある質問カテゴリDTOリスト取得（カテゴリ）
	 * 
	 * @param categoryName
	 * @param deleteFlg
	 * @return よくある質問カテゴリDTOリスト
	 */
	List<FaqCategoryDto> getFaqCategoryDtoListByCategoryName(
			@Param("categoryName") String categoryName, @Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.89 削除フラグ更新
	 * 
	 * @param mFrequentlyAskedQuestionCategory
	 * @return 更新結果
	 */
	Boolean updateDeleteFlg(MFrequentlyAskedQuestionCategory mFrequentlyAskedQuestionCategory);

	/**
	 * Task.90 質問カテゴリ取得（質問カテゴリID）
	 * 
	 * @param faqCategoryId
	 * @param deleteFlg
	 * @return 質問カテゴリエンティティ
	 */
	MFrequentlyAskedQuestionCategory findByFrequentlyAskedQuestionCategoryId(
			@Param("faqCategoryId") Integer faqCategoryId, @Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.90 質問カテゴリ登録
	 * 
	 * @param mFrequentlyAskedQuestionCategory
	 * @return 登録結果
	 */
	Boolean insert(MFrequentlyAskedQuestionCategory mFrequentlyAskedQuestionCategory);

	/**
	 * Task.90 質問カテゴリ更新
	 * 
	 * @param mFrequentlyAskedQuestionCategory
	 * @return 更新結果
	 */
	Boolean update(MFrequentlyAskedQuestionCategory mFrequentlyAskedQuestionCategory);

}
