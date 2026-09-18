package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.ExamDetailDto;
import jp.co.sss.lms.dto.ExamResultDetailDto;
import jp.co.sss.lms.dto.ExamResultDto;
import jp.co.sss.lms.entity.TExamResult;

/**
 * 試験結果テーブルマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface TExamResultMapper {

	/**
	 * 試験結果DTO取得
	 * 
	 * @param lmsUserId
	 * @param accountId
	 * @param deleteFlg
	 * @return 試験結果DTOリスト
	 */
	List<ExamResultDto> getExamResultDto(@Param("lmsUserId") Integer lmsUserId,
			@Param("accountId") Integer accountId, @Param("deleteFlg") Short deleteFlg);

	/**
	 * 試験詳細DTO取得
	 * 
	 * @param examSectionId
	 * @param lmsUserId
	 * @param deleteFlg
	 * @return 試験詳細DTO
	 */
	ExamDetailDto getExamDetailDto(@Param("examSectionId") Integer examSectionId,
			@Param("lmsUserId") Integer lmsUserId, @Param("deleteFlg") Short deleteFlg);

	/**
	 * 試験結果詳細DTO取得
	 * 
	 * @param examResultId
	 * @param deleteFlg
	 * @return 試験結果詳細DTO
	 */
	ExamResultDetailDto getExamResultDetail(@Param("examResultId") Integer examResultId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * 試験結果登録
	 * 
	 * @param tExamResult
	 * @return 登録結果
	 */
	Boolean insert(TExamResult tExamResult);

	/**
	 * 試験件数取得
	 * 
	 * @param examSectionId
	 * @param lmsUserId
	 * @param deleteFlg
	 * @return 件数
	 */
	Integer getExamCount(@Param("examSectionId") Integer examSectionId,
			@Param("lmsUserId") Integer lmsUserId, @Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.53 削除フラグ更新
	 * 
	 * @param tExamResult
	 * @return 更新結果
	 */
	Boolean updateDeleteFlg(TExamResult tExamResult);

}
