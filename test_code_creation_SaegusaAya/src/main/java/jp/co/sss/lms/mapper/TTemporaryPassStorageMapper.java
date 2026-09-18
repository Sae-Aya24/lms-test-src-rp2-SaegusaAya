package jp.co.sss.lms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.entity.TTemporaryPassStorage;

/**
 * パスワード変更テーブルマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface TTemporaryPassStorageMapper {

	/**
	 * パスワード変更取得（ユーザーID）
	 * 
	 * @param userId
	 * @param deleteFlg
	 * @return パスワード変更エンティティ
	 */
	TTemporaryPassStorage findByUserId(@Param("userId") Integer userId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * パスワード変更登録
	 * 
	 * @param tTemporaryPassStorage
	 * @return 登録結果
	 */
	Boolean insert(TTemporaryPassStorage tTemporaryPassStorage);

	/**
	 * パスワード変更更新
	 * 
	 * @param tTemporaryPassStorage
	 * @return 更新結果
	 */
	Boolean update(TTemporaryPassStorage tTemporaryPassStorage);

	/**
	 * パスワード変更削除
	 * 
	 * @param tTemporaryPassStorage
	 * @return 更新結果
	 */
	Boolean deleteUpdate(TTemporaryPassStorage tTemporaryPassStorage);

	/**
	 * パスワード変更取得（変更キー）
	 * 
	 * @param key
	 * @param deleteFlg
	 * @return パスワード変更エンティティ
	 */
	TTemporaryPassStorage findByChangeKey(@Param("key") String key,
			@Param("deleteFlg") Short deleteFlg);

}
