package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.InfoDto;
import jp.co.sss.lms.entity.TInfo;

/**
 * お知らせテーブルマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface TInfoMapper {

	/**
	 * お知らせ取得（最新1件）
	 * 
	 * @param deleteFlg
	 * @return お知らせエンティティ
	 */
	TInfo findBySingleResult(@Param("deleteFlg") Short deleteFlg); // Task.85 でdeleteFlg追加

	/**
	 * Task.83 お知らせ登録
	 * 
	 * @param tInfo
	 * @return 登録結果
	 */
	Boolean insert(TInfo tInfo);

	/**
	 * Task.84 お知らせ取得（全件）
	 * 
	 * @param deleteFlg
	 * @return お知らせエンティティリスト
	 */
	List<InfoDto> findAll(@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.85 お知らせ更新
	 * 
	 * @param tInfo
	 * @return 更新結果
	 */
	Boolean update(TInfo tInfo);

	/**
	 * Task.85 お知らせ論理削除
	 * 
	 * @param tInfo
	 * @return 更新結果
	 */
	Boolean updateDeleteFlg(TInfo tInfo);

}
