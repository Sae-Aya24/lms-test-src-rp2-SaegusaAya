package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.entity.TUserCompany;

/**
 * ユーザー・企業紐付けテーブルマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface TUserCompanyMapper {

	/**
	 * Task.67 ユーザー・企業紐付け登録
	 * 
	 * @param tUserCompany
	 * @return 登録結果
	 */
	Boolean insert(TUserCompany tUserCompany);

	/**
	 * Task.70 削除フラグ更新
	 * 
	 * @param tUserCompany
	 * @return 更新結果
	 */
	Boolean updateDeleteFlg(TUserCompany tUserCompany);

	/**
	 * Task.104 ユーザー・企業紐付け取得（企業ID）
	 * 
	 * @param companyId
	 * @param deleteFlg
	 * @return ユーザー・企業紐付けエンティティリスト
	 */
	List<TUserCompany> findByCompanyId(@Param("companyId") Integer companyId,
			@Param("deleteFlg") Short deleteFlg);

}
