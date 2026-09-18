package jp.co.sss.lms.mapper;

import org.apache.ibatis.annotations.Mapper;

import jp.co.sss.lms.entity.TDeliverablesResult;

/**
 * 成果物結果テーブルマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface TDeliverablesResultMapper {

	/**
	 * 成果物結果登録
	 * 
	 * @param tDeliverablesResult
	 * @return 登録結果
	 */
	boolean insert(TDeliverablesResult tDeliverablesResult);

}
