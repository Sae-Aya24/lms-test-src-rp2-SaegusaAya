package jp.co.sss.lms.mapper;

import org.apache.ibatis.annotations.Mapper;

import jp.co.sss.lms.entity.TDailyReportFb;

/**
 * 日報フィードバックコメントテーブルマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface TDailyReportFbMapper {

	/**
	 * Task.22 日報フィードバックコメント登録
	 * 
	 * @param tDailyReportFb
	 * @return 登録結果
	 */
	Boolean insert(TDailyReportFb tDailyReportFb);

	/**
	 * Task.22 日報フィードバックコメント削除
	 * 
	 * @param tDailyReportFb
	 * @return 更新結果
	 */
	Boolean deleteUpdate(TDailyReportFb tDailyReportFb);

	/**
	 * Task.23 日報フィードバックコメント更新
	 * 
	 * @param tDailyReportFb
	 * @return 更新結果
	 */
	Boolean update(TDailyReportFb tDailyReportFb);

}
