package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.RepairDto;
import jp.co.sss.lms.entity.TRepairHistory;

/**
 * 改修履歴テーブルマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface TRepairHistoryMapper {

	/**
	 * Task.87 改修DTO取得
	 * 
	 * @param deleteFlg
	 * @return 改修DTOリスト
	 */
	List<RepairDto> getRepairDtoList(@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.87 改修履歴更新削除
	 * 
	 * @param tRepairHistory
	 * @return 更新結果
	 */
	Boolean updateDeleteFlg(TRepairHistory tRepairHistory);

	/**
	 * Task.88 改修履歴取得（改修履歴ID）
	 * 
	 * @param historyId
	 * @param deleteFlg
	 * @return 改修履歴エンティティ
	 */
	TRepairHistory findByHistoryId(@Param("historyId") Integer historyId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.88 改修履歴登録
	 * 
	 * @param tRepairHistory
	 * @return 登録結果
	 */
	Boolean insert(TRepairHistory tRepairHistory);

	/**
	 * Task.88 改修履歴更新
	 * 
	 * @param tRepairHistory
	 * @return 更新結果
	 */
	Boolean update(TRepairHistory tRepairHistory);

}
