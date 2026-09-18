package jp.co.sss.lms.mapper;

import org.apache.ibatis.annotations.Mapper;

import jp.co.sss.lms.entity.TPresentationCompany;

/**
 * 成果報告会対象企業テーブルマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface TPresentationCompanyMapper {

	/**
	 * Task.76 成果報告会対象企業更新
	 * 
	 * @param tPresentationCompany
	 * @return 更新結果
	 */
	Boolean update(TPresentationCompany tPresentationCompany);

}
