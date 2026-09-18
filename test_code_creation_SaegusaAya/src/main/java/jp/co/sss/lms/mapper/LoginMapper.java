package jp.co.sss.lms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.entity.LoginUser;

/**
 * ログインマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface LoginMapper {

	/**
	 * ログイン詳細取得（ログインID&パスワード）
	 * 
	 * @param loginId
	 * @param password
	 * @param deleteFlg
	 * @return ログイン情報エンティティ
	 */
	LoginUser getLoginDetailByLoginIdAndPassword(@Param("loginId") String loginId,
			@Param("password") String password, @Param("deleteFlg") Short deleteFlg);

	/**
	 * ログイン詳細取得（LMSユーザID）
	 * 
	 * @param lmsUserId
	 * @param deleteFlg
	 * @return ログイン情報エンティティ
	 */
	LoginUser getLoginDetailByLmsUserId(@Param("lmsUserId") Integer lmsUserId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * ログイン取得（ユーザID）
	 * 
	 * @param userId
	 * @param deleteFlg
	 * @return ログイン情報エンティティ
	 */
	LoginUser getLoginDetailByUserId(@Param("userId") Integer userId,
			@Param("deleteFlg") Short deleteFlg);

}
