package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.ExamDetailDto;
import jp.co.sss.lms.dto.ExamDto;
import jp.co.sss.lms.dto.ExamQuestionDto;
import jp.co.sss.lms.entity.MExam;

/**
 * 試験マスタマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface MExamMapper {

	/**
	 * 試験問題DTO取得
	 * 
	 * @param examId
	 * @param deleteFlg
	 * @return 試験問題DTO
	 */
	ExamQuestionDto getExamQuestion(@Param("examId") Integer examId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.51 試験DTOリスト取得
	 * 
	 * @param placeId
	 * @param examName
	 * @param deleteFlg
	 * @return 試験DTOリスト
	 */
	// Task.81 ②検索条件の追加
	List<ExamDto> getExamDtoList(@Param("placeId") Integer placeId,
			@Param("examName") String examName, @Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.52 試験詳細DTO取得
	 * 
	 * @param examId
	 * @param deleteFlg
	 * @return 試験詳細DTO
	 */
	ExamDetailDto getExamDetailDto(@Param("examId") Integer examId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.56 試験DTO取得
	 * 
	 * @param examId
	 * @param deleteFlg
	 * @return 試験DTO
	 */
	ExamDto getExamDto(@Param("examId") Integer examId, @Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.81 削除フラグ更新
	 * 
	 * @param mExam
	 * @return 更新結果
	 */
	Boolean updateDeleteFlg(MExam mExam);

	/**
	 * Task.113 試験登録
	 * 
	 * @param mExam
	 * @return 登録結果
	 */
	Boolean insert(MExam mExam);

	/**
	 * Task.113 試験更新
	 * 
	 * @param mExam
	 * @return 更新結果
	 */
	Boolean update(MExam mExam);

	/**
	 * Task.116 試験リスト取得
	 * 
	 * @param deleteFlg
	 * @return 試験DTOリスト
	 */
	List<ExamDto> getExamList(@Param("deleteFlg") Short deleteFlg);

}
