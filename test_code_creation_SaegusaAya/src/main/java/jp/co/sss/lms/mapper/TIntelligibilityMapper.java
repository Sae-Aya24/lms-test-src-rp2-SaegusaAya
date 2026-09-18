package jp.co.sss.lms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.entity.TIntelligibility;

/**
 * 理解度テーブルマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface TIntelligibilityMapper {

	/**
	 * 理解度件数取得（日報提出ID）
	 * 
	 * @param dailyReportSubmitId
	 * @return 件数
	 */
	Integer getCountByDailyReportSubmitId(
			@Param("dailyReportSubmitId") Integer dailyReportSubmitId);

	/**
	 * 理解度削除
	 * 
	 * @param dailyReportSubmitId
	 * @return 削除結果
	 */
	Boolean delete(@Param("dailyReportSubmitId") Integer dailyReportSubmitId);

	/**
	 * 理解度登録
	 * 
	 * @param tIntelligibility
	 * @return 登録結果
	 */
	Boolean insert(TIntelligibility tIntelligibility);

}
