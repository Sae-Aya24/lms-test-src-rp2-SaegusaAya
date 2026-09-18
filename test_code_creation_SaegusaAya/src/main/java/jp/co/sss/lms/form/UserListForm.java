package jp.co.sss.lms.form;

import java.util.List;

import jp.co.sss.lms.dto.CompanyDto;
import jp.co.sss.lms.dto.CourseDto;
import jp.co.sss.lms.dto.PlaceDto;
import jp.co.sss.lms.dto.UserDetailDto;
import jp.co.sss.lms.enums.LmsUserRoleEnum;
import lombok.Data;

/**
 * Task.43 ユーザー一覧フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class UserListForm {

	/** コースDTOリスト */
	private List<CourseDto> courseDtoList;
	/** コース名 */
	private String courseName;
	/** 会場DTOリスト */
	private List<PlaceDto> placeDtoList;
	/** 会場ID */
	private Integer placeId;
	/** 会場名 */
	private String placeName;
	/** 備考 */
	private String placeNote;
	/** 企業DTOリスト */
	private List<CompanyDto> companyDtoList;
	/** 企業名 */
	private String companyName;
	/** ユーザー基本情報DTOリスト */
	List<UserDetailDto> userDetailDtoList;
	/** ユーザー名 */
	private String userName;
	/** LMSユーザーID */
	private Integer lmsUserId; // Task.70
	/** ユーザーID */
	private Integer userId; // Task.70
	/** 権限の列挙 */
	private LmsUserRoleEnum[] roleEnum; // Task.79
	/** 権限 */
	private String role; // Task.79
	/** 退校フラグ */
	private Short leaveFlg; // Task.79
	/** 経過フラグ */
	private Short pastFlg; // Task.79
	/** 経過期間ラベル */
	private String pastTimeLabel; // Task.79
	/** ユーザーID配列 */
	private String[] lmsUserIdArr; // Task.79

}
