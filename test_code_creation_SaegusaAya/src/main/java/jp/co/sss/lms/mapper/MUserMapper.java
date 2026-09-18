package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.entity.MUser;

/**
 * ユーザーマスタマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface MUserMapper {

	/**
	 * ユーザー取得（メールアドレス）
	 * 
	 * @param mailAddress
	 * @param role
	 * @param deleteFlg
	 * @return ユーザーエンティティリスト
	 */
	// Task.05 ②対象者特定の条件に権限を追加
	// Task.06 対象者が複数の場合の対応
	List<MUser> findByMailAddress(@Param("mailAddress") String mailAddress,
			@Param("role") String role, @Param("deleteFlg") Short deleteFlg);

	/**
	 * ユーザー取得（ユーザID）
	 * 
	 * @param userId
	 * @param deleteFlg
	 * @return ユーザーエンティティ
	 */
	MUser findByUserId(@Param("userId") Integer userId, @Param("deleteFlg") Short deleteFlg);

	/**
	 * ログインID取得
	 * 
	 * @param userId
	 * @param deleteFlg
	 * @return ログインID
	 */
	String getLoginId(@Param("userId") Integer userId, @Param("deleteFlg") Short deleteFlg);

	/**
	 * パスワード更新
	 * 
	 * @param mUser
	 * @return 更新結果
	 */
	Boolean updatePassword(MUser mUser);

	/**
	 * パスワード変更日付更新
	 * 
	 * @param mUser
	 * @return 更新結果
	 */
	Boolean updatePasswordChangeDate(MUser mUser);

	/**
	 * セキュリティ同意フラグ更新
	 * 
	 * @param mUser
	 * @return 更新結果
	 */
	Boolean updateSecrityFlg(MUser mUser);

	/**
	 * Task.67 ユーザー登録
	 * 
	 * @param mUser
	 * @return 登録結果
	 */
	Boolean insert(MUser mUser);

	/**
	 * Task.69 ユーザー更新
	 * 
	 * @param mUser
	 * @return 登録結果
	 */
	Boolean update(MUser mUser);

	/**
	 * Task.70 削除フラグ更新
	 * 
	 * @param mUser
	 * @return 更新結果
	 */
	Boolean updateDeleteFlg(MUser mUser);

}
