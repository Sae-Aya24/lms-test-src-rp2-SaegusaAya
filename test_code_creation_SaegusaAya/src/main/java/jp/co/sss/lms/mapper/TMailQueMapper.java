package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.MailQueDto;
import jp.co.sss.lms.entity.TMailQue;

/**
 * メール送信キューテーブルマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface TMailQueMapper {

	/**
	 * メール送信キュー登録
	 * 
	 * @param tMailQue
	 * @return 登録結果
	 */
	Boolean insert(TMailQue tMailQue);

	/**
	 * Task.94 メール送信キューDTOリスト取得
	 * 
	 * @param deleteFlg
	 * @return メール送信キューDTOリスト
	 */
	List<MailQueDto> getMailQueDtoList(@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.94 メール送信キュー取得（メール送信キューID）
	 * 
	 * @param mailQueId
	 * @param deleteFlg
	 * @return メール送信キューエンティティ
	 */
	TMailQue findByMailQueId(@Param("mailQueId") Integer mailQueId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.94 メール送信キュー更新削除
	 * 
	 * @param tMailQue
	 * @return 更新結果
	 */
	Boolean updateDeleteFlg(TMailQue tMailQue);

}
