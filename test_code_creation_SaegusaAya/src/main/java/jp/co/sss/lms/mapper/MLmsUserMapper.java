package jp.co.sss.lms.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.LmsUserDto;
import jp.co.sss.lms.dto.UserDailyReportDto;
import jp.co.sss.lms.dto.UserDetailDto;
import jp.co.sss.lms.entity.MLmsUser;

/**
 * LMSユーザーマスタマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface MLmsUserMapper {

	/**
	 * ユーザー基本情報取得
	 * 
	 * @param lmsUserId
	 * @param deleteFlg
	 * @return ユーザー基本情報DTO
	 */
	UserDetailDto getUserDetail(@Param("lmsUserId") Integer lmsUserId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.43 ユーザー基本情報取得（検索用）
	 * 
	 * @param courseName
	 * @param companyName
	 * @param userName
	 * @param placeIdList
	 * @param role
	 * @param leaveFlg
	 * @param pastDate
	 * @param deleteFlg
	 * @return ユーザー基本情報DTOリスト
	 */
	List<UserDetailDto> getUserDetailForSearch(@Param("courseName") String courseName,
			@Param("companyName") String companyName, @Param("userName") String userName,
			@Param("placeIdList") List<Integer> placeIdList, @Param("role") String role,
			@Param("leaveFlg") Short leaveFlg, @Param("pastDate") Date pastDate,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.50 ユーザーレポート情報DTO取得
	 * 
	 * @param dateFrom
	 * @param dateTo
	 * @param courseName
	 * @param placeId
	 * @param companyName
	 * @param role
	 * @param pastDate
	 * @param deleteFlg
	 * @return ユーザーレポート情報DTOリスト
	 */
	List<UserDailyReportDto> getUserDailyReportDto(@Param("dateFrom") Date dateFrom,
			@Param("dateTo") Date dateTo, @Param("courseName") String courseName,
			@Param("placeId") Integer placeId, @Param("companyName") String companyName,
			@Param("role") String role, @Param("pastDate") Date pastDate,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.52 ユーザー詳細取得（試験結果）
	 * 
	 * @param examId
	 * @param courseId
	 * @param companyId
	 * @param placeId
	 * @param role
	 * @param deleteFlg
	 * @return ユーザー詳細DTOリスト
	 */
	List<LmsUserDto> getLmsUserDto(@Param("examId") Integer examId,
			@Param("courseId") Integer courseId, @Param("companyId") Integer companyId,
			@Param("placeId") Integer placeId, @Param("role") String role,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.67 LMSユーザー登録
	 * 
	 * @param mLmsUser
	 * @return 登録結果
	 */
	Boolean insert(MLmsUser mLmsUser);

	/**
	 * Task.68 受講生ユーザー基本情報取得
	 * 
	 * @param companyName
	 * @param userName
	 * @param role
	 * @param pastDate
	 * @param deleteFlg
	 * @return ユーザー基本情報DTOリスト
	 */
	List<UserDetailDto> getStudentUserDetailForSearch(@Param("companyName") String companyName,
			@Param("userName") String userName, @Param("role") String role,
			@Param("pastDate") Date pastDate, @Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.69 LMSユーザー更新
	 * 
	 * @param mLmsUser
	 * @return 登録結果
	 */
	Boolean update(MLmsUser mLmsUser);

	/**
	 * Task.70 企業ユーザー基本情報取得
	 * 
	 * @param companyName
	 * @param role
	 * @param deleteFlg
	 * @return ユーザー基本情報DTOリスト
	 */
	List<UserDetailDto> getCompanyUserDetailForSearch(@Param("companyName") String companyName,
			@Param("role") String role, @Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.70 LMSユーザー論理削除
	 * 
	 * @param mLmsUser
	 * @return 更新結果
	 */
	Boolean updateDeleteFlg(MLmsUser mLmsUser);

	/**
	 * Task.79 LMSユーザー取得（LMSユーザID)
	 * 
	 * @param lmsUserId
	 * @param deleteFlg
	 * @return LMSユーザーエンティティ
	 */
	MLmsUser findByLmsUserId(@Param("lmsUserId") Integer lmsUserId,
			@Param("deleteFlg") Short deleteFlg);

}
