package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.FaqDto;
import jp.co.sss.lms.entity.MFrequentlyAskedQuestion;

/**
 * Task.11 よくある質問マスタマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface MFrequentlyAskedQuestionMapper {

	/**
	 * Task.11 よくある質問DTOリスト取得
	 * 
	 * @param deleteFlg
	 * @param keyword
	 * @param frequentlyAskedQuestionCategoryId
	 * @return よくある質問DTOリスト
	 */
	List<FaqDto> getFaqDtoList(@Param("deleteFlg") Short deleteFlg,
			@Param("keyword") String keyword,
			@Param("frequentlyAskedQuestionCategoryId") Integer frequentlyAskedQuestionCategoryId);

	/**
	 * Task.89 質問件数取得
	 * 
	 * @param categoryId
	 * @param deleteFlg
	 * @return 件数
	 */
	Integer getFaqCount(@Param("categoryId") Integer categoryId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.89 削除フラグ更新
	 * 
	 * @param mFrequentlyAskedQuestion
	 * @return 更新結果
	 */
	Boolean updateDeleteFlg(MFrequentlyAskedQuestion mFrequentlyAskedQuestion);

	/**
	 * Task.91 質問取得（質問ID）
	 * 
	 * @param faqId
	 * @param deleteFlg
	 * @return 質問エンティティ
	 */
	MFrequentlyAskedQuestion findByFrequentlyAskedQuestionId(@Param("faqId") Integer faqId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.91 質問登録
	 * 
	 * @param mFrequentlyAskedQuestion
	 * @return 登録結果
	 */
	Boolean insert(MFrequentlyAskedQuestion mFrequentlyAskedQuestion);

	/**
	 * Task.91 質問更新
	 * 
	 * @param mFrequentlyAskedQuestion
	 * @return 更新結果
	 */
	Boolean update(MFrequentlyAskedQuestion mFrequentlyAskedQuestion);

}
