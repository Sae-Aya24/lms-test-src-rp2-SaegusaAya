package jp.co.sss.lms.mapper;

import org.apache.ibatis.annotations.Mapper;

import jp.co.sss.lms.entity.TMailQueFile;

/**
 * メール送信キュー・ファイル紐づけテーブルマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface TMailQueFileMapper {

	/**
	 * メール送信キュー・ファイル紐付け登録
	 * 
	 * @param tMailQueFile
	 * @return 登録結果
	 */
	Boolean insert(TMailQueFile tMailQueFile);

	/**
	 * Task.94 メール送信キュー・ファイル紐付け更新削除
	 * 
	 * @param tMailQueFile
	 * @return 更新結果
	 */
	Boolean updateDeleteFlg(TMailQueFile tMailQueFile);

}
