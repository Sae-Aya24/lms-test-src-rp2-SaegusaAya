package jp.co.sss.lms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.AgreementDto;

/**
 * 契約内容マッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface MAgreementMapper {

	/**
	 * Task.64 契約内容DTO取得
	 * 
	 * @param agreementConsentId
	 * @param deleteFlg
	 * @return 契約内容DTO
	 */
	AgreementDto getAgreementDto(@Param("agreementConsentId") Integer agreementConsentId,
			@Param("deleteFlg") Short deleteFlg);

}
