package jp.co.sss.lms.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.sss.lms.dto.AgreementConsentDto;
import jp.co.sss.lms.dto.AgreementDto;
import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.entity.TAgreementConsent;
import jp.co.sss.lms.mapper.MAgreementMapper;
import jp.co.sss.lms.mapper.TAgreementConsentMapper;
import jp.co.sss.lms.util.Constants;

/**
 * 契約情報サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class ContractService {

	@Autowired
	private TAgreementConsentMapper tAgreementConsentMapper;
	@Autowired
	private MAgreementMapper mAgreementMapper;
	@Autowired
	private LoginUserDto loginUserDto;

	/**
	 * 契約同意DTOリストの取得
	 * 
	 * @return 契約同意DTOリスト
	 */
	public List<AgreementConsentDto> getAgreementConsentDtoList() {
		// Task.65 引数today、consentFlgを追加
		return tAgreementConsentMapper.getAgreementConsentDtoList(loginUserDto.getCompanyId(), null,
				Constants.CODE_VAL_CONTRACT_AGREE, Constants.DB_FLG_FALSE);
	}

	/**
	 * 未同意の契約同意DTOリストの取得
	 * 
	 * @return 契約同意DTOリスト
	 */
	public List<AgreementConsentDto> getDisagreementConsentDtoList() {
		Date today = new Date();
		return tAgreementConsentMapper.getAgreementConsentDtoList(loginUserDto.getCompanyId(),
				today, Constants.CODE_VAL_CONTRACT_DISAGREE, Constants.DB_FLG_FALSE);
	}

	/**
	 * Task.64 契約内容DTOの取得
	 * 
	 * @param agreementConsentId
	 * @return 契約内容DTO
	 */
	public AgreementDto getAgreementDto(Integer agreementConsentId) {
		AgreementDto agreementDto = mAgreementMapper.getAgreementDto(agreementConsentId,
				Constants.DB_FLG_FALSE);
		String agreementContent = agreementDto.getAgreementContent();
		agreementContent = agreementContent.replaceAll(":company_name:",
				agreementDto.getCompanyName());
		agreementContent = agreementContent.replaceAll(":company_address:",
				agreementDto.getCompanyAddress());
		agreementContent = agreementContent.replaceAll(":representative_post:",
				agreementDto.getRepresentativePost());
		agreementContent = agreementContent.replaceAll(":representative_name:",
				agreementDto.getRepresentativeName());
		agreementContent = agreementContent.replaceAll(":course_name:",
				agreementDto.getCourseName());
		agreementContent = agreementContent.replaceAll(":contract_start_date:",
				agreementDto.getContractStartDate());
		agreementContent = agreementContent.replaceAll(":contract_end_date:",
				agreementDto.getContractEndDate());
		agreementContent = agreementContent.replaceAll(":contract_date:",
				agreementDto.getContractDate());
		agreementDto.setAgreementContent(agreementContent);

		return agreementDto;
	}

	/**
	 * Task.65 未同意の契約同意DTOの取得
	 * 
	 * @return 契約内容DTO
	 */
	public AgreementDto getDisagreementDto() {
		List<AgreementConsentDto> agreementConsentDtoList = getDisagreementConsentDtoList();
		Integer agreementConsentId = agreementConsentDtoList.get(0).getAgreementConsentId();
		AgreementDto agreementDto = getAgreementDto(agreementConsentId);
		agreementDto.setAgreementConsentId(agreementConsentId);
		return agreementDto;
	}

	/**
	 * Task.65 契約への同意
	 * 
	 * @param agreementConsentId
	 */
	public void updateConsentFlg(Integer agreementConsentId) {
		Date today = new Date();
		TAgreementConsent tAgreementConsent = new TAgreementConsent();
		tAgreementConsent.setAgreementConsentId(agreementConsentId);
		tAgreementConsent.setConsentFlg(Constants.CODE_VAL_CONTRACT_AGREE);
		tAgreementConsent.setLastModifiedUser(loginUserDto.getLmsUserId());
		tAgreementConsent.setLastModifiedDate(today);
		tAgreementConsentMapper.updateConsentFlg(tAgreementConsent);
	}

}
