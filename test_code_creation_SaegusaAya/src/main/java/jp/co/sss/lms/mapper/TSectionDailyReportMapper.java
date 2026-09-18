package jp.co.sss.lms.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.DailyReportDto;
import jp.co.sss.lms.dto.SectionServiceDailyReportDto;
import jp.co.sss.lms.entity.TSectionDailyReport;

/**
 * セクション・日報紐付けテーブルマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface TSectionDailyReportMapper {

	/**
	 * セクションサービスレポートDTO取得
	 * 
	 * @param sectionId
	 * @param lmsUserId
	 * @param deleteFlg
	 * @return セクションサービスレポートDTO
	 */
	List<SectionServiceDailyReportDto> getSectionServiceDailyReportDto(
			@Param("sectionId") Integer sectionId, @Param("lmsUserId") Integer lmsUserId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * レポートDTO取得
	 * 
	 * @param dailyReportId
	 * @param sectionId
	 * @param accountId
	 * @param lmsUserId
	 * @param deleteFlg
	 * @param date
	 * @return レポートDTO
	 */
	DailyReportDto getDailyReportDto(@Param("dailyReportId") Integer dailyReportId,
			@Param("sectionId") Integer sectionId, @Param("accountId") Integer accountId,
			@Param("lmsUserId") Integer lmsUserId, @Param("deleteFlg") Short deleteFlg,
			@Param("date") Date date);

	/**
	 * Task.116 セクション・日報紐付け件数取得
	 * 
	 * @param sectionId
	 * @param dailyReportId
	 * @param deleteFlg
	 * @return 件数
	 */
	Integer getSectionReportCount(@Param("sectionId") Integer sectionId,
			@Param("dailyReportId") Integer dailyReportId, @Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.116 セクション・日報紐付け登録
	 * 
	 * @param tSectionDailyReport
	 * @return 登録結果
	 */
	Boolean insert(TSectionDailyReport tSectionDailyReport);

	/**
	 * Task.116 セクション・日報紐付け削除
	 * 
	 * @param tSectionDailyReport
	 * @return 削除結果
	 */
	Boolean delete(TSectionDailyReport tSectionDailyReport);

}
