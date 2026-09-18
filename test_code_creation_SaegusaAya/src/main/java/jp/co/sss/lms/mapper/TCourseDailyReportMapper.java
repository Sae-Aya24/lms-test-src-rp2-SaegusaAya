package jp.co.sss.lms.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.DailyReportDto;
import jp.co.sss.lms.entity.TCourseDailyReport;

/**
 * コース・日報紐付けテーブルマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface TCourseDailyReportMapper {

	/**
	 * レポートDTO取得
	 * 
	 * @param dailyReportId
	 * @param courseId
	 * @param accountId
	 * @param lmsUserId
	 * @param deleteFlg
	 * @param date
	 * @return レポートDTO
	 */
	DailyReportDto getDailyReportDto(@Param("dailyReportId") Integer dailyReportId,
			@Param("courseId") Integer courseId, @Param("accountId") Integer accountId,
			@Param("lmsUserId") Integer lmsUserId, @Param("deleteFlg") Short deleteFlg,
			@Param("date") Date date);

	/**
	 * Task.114 コース・日報紐付け登録
	 * 
	 * @param tCourseDailyReport
	 * @return 登録結果
	 */
	Boolean insert(TCourseDailyReport tCourseDailyReport);

	/**
	 * Task.114 コース・日報紐付け削除
	 * 
	 * @param courseId
	 * @return 削除結果
	 */
	Boolean delete(Integer courseId);

	/**
	 * Task.116 コース・日報紐付け件数取得
	 * 
	 * @param courseId
	 * @param dailyReportId
	 * @param deleteFlg
	 * @return 件数
	 */
	Integer getCourseReportCount(@Param("courseId") Integer sectionId,
			@Param("dailyReportId") Integer dailyReportId, @Param("deleteFlg") Short deleteFlg);

}
