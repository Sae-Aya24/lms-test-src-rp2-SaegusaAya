package jp.co.sss.lms.mapper;

import org.apache.ibatis.annotations.Mapper;

import jp.co.sss.lms.entity.TCourseUser;

/**
 * コース・ユーザー紐付けテーブルマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface TCourseUserMapper {

	/**
	 * Task.67 コース・ユーザー紐付け登録
	 * 
	 * @param tCourseUser
	 * @return 登録結果
	 */
	Boolean insert(TCourseUser tCourseUser);

	/**
	 * Task.70 削除フラグ更新
	 * 
	 * @param tCourseUser
	 * @return 更新結果
	 */
	Boolean updateDeleteFlg(TCourseUser tCourseUser);

}
