package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.DailyReportDownloadDto;
import jp.co.sss.lms.dto.DailyReportDto;
import jp.co.sss.lms.entity.TDailyReportSubmit;

/**
 * 日報提出テーブルマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface TDailyReportSubmitMapper {

	/**
	 * 日報提出登録
	 * 
	 * @param tDailyReportSubmit
	 * @return 登録結果
	 */
	Boolean insert(TDailyReportSubmit tDailyReportSubmit);

	/**
	 * 日報提出更新
	 * 
	 * @param tDailyReportSubmit
	 * @return 更新結果
	 */
	Boolean update(TDailyReportSubmit tDailyReportSubmit);

	/**
	 * レポート提出リスト取得
	 * 
	 * @param lmsUserId
	 * @param deleteFlg
	 * @return レポートDTOリスト
	 */
	List<DailyReportDto> getDailyReportSubmitList(@Param("lmsUserId") Integer lmsUserId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * レポートダウンロードDTO取得
	 * 
	 * @param dailyReportSubmitId
	 * @param dailyReportId
	 * @param deleteFlg
	 * @return レポートダウンロードDTO
	 */
	DailyReportDownloadDto getDailyReportDownloadDto(
			@Param("dailyReportSubmitId") Integer dailyReportSubmitId,
			@Param("dailyReportId") Integer dailyReportId, @Param("deleteFlg") Short deleteFlg);

	/**
	 * レポートDTO取得
	 * 
	 * @param dailyReportSubmitId
	 * @param deleteFlg
	 * @return レポートDTO
	 */
	DailyReportDto getDailyReportDto(@Param("dailyReportSubmitId") Integer dailyReportSubmitId,
			@Param("deleteFlg") Short deleteFlg);

}
