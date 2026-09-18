package jp.co.sss.lms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.entity.TDailyReportSubmitDetail;

/**
 * 日報提出詳細テーブルマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface TDailyReportSubmitDetailMapper {

	/**
	 * 日報提出詳細件数取得（日報提出ＩＤ）
	 * 
	 * @param dailyReportSubmitId
	 * @return 件数
	 */
	Integer getCountByDailyReportSubmitId(
			@Param("dailyReportSubmitId") Integer dailyReportSubmitId);

	/**
	 * 日報提出詳細削除
	 * 
	 * @param dailyReportSubmitId
	 * @return 削除結果
	 */
	Boolean delete(@Param("dailyReportSubmitId") Integer dailyReportSubmitId);

	/**
	 * 日報提出詳細登録
	 * 
	 * @param tDailyReportSubmitDetail
	 * @return 登録結果
	 */
	Boolean insert(TDailyReportSubmitDetail tDailyReportSubmitDetail);

}
