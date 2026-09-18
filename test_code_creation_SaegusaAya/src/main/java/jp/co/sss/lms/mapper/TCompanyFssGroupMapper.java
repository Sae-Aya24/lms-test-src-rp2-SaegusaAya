package jp.co.sss.lms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.entity.TCompanyFssGroup;

/**
 * 企業・共有グループ紐付けテーブルマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface TCompanyFssGroupMapper {

	/**
	 * Task.105 企業・共有グループ紐付け登録
	 * 
	 * @param tCompanyFssGroup
	 * @return 登録結果
	 */
	Boolean insert(TCompanyFssGroup tCompanyFssGroup);

	/**
	 * Task.105 企業・共有グループ紐付け取得（企業ID）
	 * 
	 * @param companyId
	 * @param deleteFlg
	 * @return 企業・共有グループ紐付けエンティティ
	 */
	TCompanyFssGroup findByCompanyId(@Param("companyId") Integer companyId,
			@Param("deleteFlg") Short deleteFlg);

}
