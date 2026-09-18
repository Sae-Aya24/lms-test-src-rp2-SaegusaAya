package jp.co.sss.lms.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.AgreementConsentDto;
import jp.co.sss.lms.entity.TAgreementConsent;

/**
 * 契約同意マッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface TAgreementConsentMapper {

	/**
	 * 契約同意DTOリスト取得
	 * 
	 * @param companyId
	 * @param today
	 * @param consentFlg
	 * @param deleteFlg
	 * @return 契約同意DTOリスト
	 */
	List<AgreementConsentDto> getAgreementConsentDtoList(@Param("companyId") Integer companyId,
			@Param("today") Date today, @Param("consentFlg") Short consentFlg,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.65 契約同意フラグ更新
	 * 
	 * @param agreementConsentId
	 * @return 更新結果
	 */
	Boolean updateConsentFlg(TAgreementConsent tAgreementConsent);

}
