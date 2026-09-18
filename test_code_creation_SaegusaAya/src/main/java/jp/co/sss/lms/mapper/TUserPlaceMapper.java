package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.entity.TUserPlace;

/**
 * ユーザー・会場紐付けテーブルマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface TUserPlaceMapper {

	/**
	 * Task.43 会場ID取得
	 * 
	 * @param lmsUserId
	 * @param deleteFlg
	 * @return 会場IDリスト
	 */
	List<Integer> getPlaceId(@Param("lmsUserId") Integer lmsUserId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.57 ユーザー・会場紐付け登録
	 * 
	 * @param tsUserPlace
	 * @return 登録結果
	 */
	Boolean insert(TUserPlace tsUserPlace);

	/**
	 * Task.79 削除フラグ更新
	 * 
	 * @param tsUserPlace
	 * @return 更新結果
	 */
	Boolean updateDeleteFlg(TUserPlace tsUserPlace);

	/**
	 * Task.95 件数取得（会場ID）
	 * 
	 * @param placeId
	 * @param deleteFlg
	 * @return 件数
	 */
	Integer getCountByPlaceId(@Param("placeId") Integer placeId,
			@Param("deleteFlg") Short deleteFlg);

}
