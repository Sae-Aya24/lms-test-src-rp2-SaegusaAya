package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.CourseDto;
import jp.co.sss.lms.dto.CourseReportDto;
import jp.co.sss.lms.dto.CourseServiceCourseDto;
import jp.co.sss.lms.dto.CourseWithTeachingMaterialCountDto;
import jp.co.sss.lms.entity.MCourse;

/**
 * コースマスタマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface MCourseMapper {

	/**
	 * コース詳細取得
	 * 
	 * @param courseId
	 * @param lmsUserId
	 * @param deleteFlg
	 * @return コース情報サービス コースDTO
	 */
	// Task.15 引数にLmsUserIdを追加
	CourseServiceCourseDto getCourseDetail(@Param("courseId") Integer courseId,
			@Param("lmsUserId") Integer lmsUserId, @Param("deleteFlg") Short deleteFlg);

	/**
	 * コース数取得
	 * 
	 * @param courseId
	 * @return コース数
	 */
	Integer getCourseCount(Integer courseId);

	/**
	 * Task.42 コース・紐付き教材件数DTO取得
	 * 
	 * @param accountId
	 * @param deleteFlg
	 * @param hiddenFlg
	 * @return コース・紐付き教材件数DTOリスト
	 */
	List<CourseWithTeachingMaterialCountDto> getCourseWithTeachingMaterialCountDtoList(
			@Param("accountId") Integer accountId, @Param("deleteFlg") Short deleteFlg,
			@Param("hiddenFlg") Short hiddenFlg);

	/**
	 * Task.43 コースDTOリスト取得
	 * 
	 * @param hiddenFlg
	 * @param deleteFlg
	 * @return コースDTOリスト
	 */
	List<CourseDto> getCourseDtoList(@Param("hiddenFlg") Short hiddenFlg,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.114 コースレポートDTOリスト取得
	 * 
	 * @param courseId
	 * @param hiddenFlg
	 * @param deleteFlg
	 * @return コースレポートDTOリスト
	 */
	List<CourseReportDto> getCourseReportDtoList(@Param("courseId") Integer courseId,
			@Param("hiddenFlg") Short hiddenFlg, @Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.114 コース登録
	 * 
	 * @param mCourse
	 * @return 登録結果
	 */
	Boolean insert(MCourse mCourse);

	/**
	 * Task.114 コース変更
	 * 
	 * @param courseId
	 * @return 変更結果
	 */
	Boolean update(MCourse mCourse);

}
