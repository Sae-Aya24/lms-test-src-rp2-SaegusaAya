package jp.co.sss.lms.mapper;

import org.apache.ibatis.annotations.Mapper;

import jp.co.sss.lms.entity.TExamResultDetail;

/**
 * 試験結果詳細テーブルマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface TExamResultDetailMapper {

	/**
	 * 試験結果詳細登録
	 * 
	 * @param tExamResultDetail
	 * @return 登録結果
	 */
	Boolean insert(TExamResultDetail tExamResultDetail);

}
