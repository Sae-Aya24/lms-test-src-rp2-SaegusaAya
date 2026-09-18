package jp.co.sss.lms.service;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.dto.SubsidyCategoryDto;
import jp.co.sss.lms.entity.MCompany;
import jp.co.sss.lms.form.CompanySubsidyForm;
import jp.co.sss.lms.mapper.MCompanyMapper;
import jp.co.sss.lms.mapper.MSubsidyCategoryMapper;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.MessageUtil;

/**
 * Task.66 助成金情報サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class SubsidyService {

	@Autowired
	private MCompanyMapper mCompanyMapper;
	@Autowired
	private MSubsidyCategoryMapper mSubsidyCategoryMapper;
	@Autowired
	private LoginUserDto loginUserDto;
	@Autowired
	private MessageUtil messageUtil;

	/**
	 * Task.66 企業(助成金)情報フォームの設定
	 * 
	 * @param companySubsidyForm
	 */
	public void setCompanySubsidyForm(CompanySubsidyForm companySubsidyForm) {
		MCompany mCompany = mCompanyMapper.findByCompanyId(loginUserDto.getCompanyId(),
				Constants.DB_FLG_FALSE);
		BeanUtils.copyProperties(mCompany, companySubsidyForm);
	}

	/**
	 * Task.66 企業(助成金)情報フォームの入力チェック
	 * 
	 * @param companySubsidyForm
	 */
	public void companyUpdateInputCheck(CompanySubsidyForm companySubsidyForm,
			BindingResult result) {
		if (StringUtils.isBlank(companySubsidyForm.getCompanyName())) {
			result.addError(new FieldError(result.getObjectName(), "companyName", "* " + messageUtil
					.getMessage(Constants.VALID_KEY_REQUIRED, new String[] { "企業名" })));
		}
		if (StringUtils.isBlank(companySubsidyForm.getCompanyNameKana())) {
			result.addError(
					new FieldError(result.getObjectName(), "companyNameKana", "* " + messageUtil
							.getMessage(Constants.VALID_KEY_REQUIRED, new String[] { "企業名（カナ）" })));
		}

		Pattern postNumber1Pattern = Pattern.compile("^[0-9]{3}$");
		Pattern postNumber2Pattern = Pattern.compile("^[0-9]{4}$");
		if (StringUtils.isBlank(companySubsidyForm.getPostNumber1())
				|| StringUtils.isBlank(companySubsidyForm.getPostNumber2())) {
			result.addError(new FieldError(result.getObjectName(), "postNumber1", "* " + messageUtil
					.getMessage(Constants.VALID_KEY_REQUIRED, new String[] { "郵便番号" })));
			result.addError(new FieldError(result.getObjectName(), "postNumber2", null));
		} else if (!postNumber1Pattern.matcher(companySubsidyForm.getPostNumber1()).find()
				|| !postNumber2Pattern.matcher(companySubsidyForm.getPostNumber2()).find()) {
			result.addError(new FieldError(result.getObjectName(), "postNumber1", "* " + messageUtil
					.getMessage(Constants.VALID_KEY_INVALID, new String[] { "郵便番号" })));
			result.addError(new FieldError(result.getObjectName(), "postNumber2", null));
		}

		if (StringUtils.isBlank(companySubsidyForm.getAddress())) {
			result.addError(new FieldError(result.getObjectName(), "address", "* " + messageUtil
					.getMessage(Constants.VALID_KEY_REQUIRED, new String[] { "企業所在地" })));
		}

		Pattern phoneNumber1Pattern = Pattern.compile("^[0-9]{2,3}$");
		Pattern phoneNumber2Pattern = Pattern.compile("^[0-9]{4}$");
		if (StringUtils.isBlank(companySubsidyForm.getPhoneNumber1())
				|| StringUtils.isBlank(companySubsidyForm.getPhoneNumber2())
				|| StringUtils.isBlank(companySubsidyForm.getPhoneNumber3())) {
			result.addError(
					new FieldError(result.getObjectName(), "phoneNumber1", "* " + messageUtil
							.getMessage(Constants.VALID_KEY_REQUIRED, new String[] { "電話番号" })));
			result.addError(new FieldError(result.getObjectName(), "phoneNumber2", null));
			result.addError(new FieldError(result.getObjectName(), "phoneNumber3", null));
		} else if (!phoneNumber1Pattern.matcher(companySubsidyForm.getPhoneNumber1()).find()
				|| !phoneNumber2Pattern.matcher(companySubsidyForm.getPhoneNumber2()).find()
				|| !phoneNumber2Pattern.matcher(companySubsidyForm.getPhoneNumber3()).find()) {
			result.addError(
					new FieldError(result.getObjectName(), "phoneNumber1", "* " + messageUtil
							.getMessage(Constants.VALID_KEY_INVALID, new String[] { "電話番号" })));
			result.addError(new FieldError(result.getObjectName(), "phoneNumber2", null));
			result.addError(new FieldError(result.getObjectName(), "phoneNumber3", null));
		}

		if (StringUtils.isBlank(companySubsidyForm.getRepresentativePost())) {
			result.addError(
					new FieldError(result.getObjectName(), "representativePost", "* " + messageUtil
							.getMessage(Constants.VALID_KEY_REQUIRED, new String[] { "代表者役職名" })));
		}
		if (StringUtils.isBlank(companySubsidyForm.getRepresentativeName())) {
			result.addError(
					new FieldError(result.getObjectName(), "representativeName", "* " + messageUtil
							.getMessage(Constants.VALID_KEY_REQUIRED, new String[] { "代表者氏名" })));
		}

		if (companySubsidyForm.getSubsidyUse() == 0) {
			return;
		}

		if (companySubsidyForm.getCapital() == null) {
			result.addError(new FieldError(result.getObjectName(), "capital", "* " + messageUtil
					.getMessage(Constants.VALID_KEY_REQUIRED, new String[] { "資本額" })));
		}
		if (companySubsidyForm.getWorkerAmount() == null) {
			result.addError(
					new FieldError(result.getObjectName(), "workerAmount", "* " + messageUtil
							.getMessage(Constants.VALID_KEY_REQUIRED, new String[] { "社員数" })));
		}

		Pattern trainingTimePattern = Pattern.compile("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");
		if (StringUtils.isBlank(companySubsidyForm.getWorkStartTime())) {
			result.addError(
					new FieldError(result.getObjectName(), "workStartTime", "* " + messageUtil
							.getMessage(Constants.VALID_KEY_REQUIRED, new String[] { "始業時間" })));
		} else if (!trainingTimePattern.matcher(companySubsidyForm.getWorkStartTime()).find()) {
			result.addError(
					new FieldError(result.getObjectName(), "workStartTime", "* " + messageUtil
							.getMessage(Constants.VALID_KEY_WORK24TIME, new String[] { "始業時間" })));
		}
		if (StringUtils.isBlank(companySubsidyForm.getWorkEndTime())) {
			result.addError(new FieldError(result.getObjectName(), "workEndTime", "* " + messageUtil
					.getMessage(Constants.VALID_KEY_REQUIRED, new String[] { "終業時間" })));
		} else if (!trainingTimePattern.matcher(companySubsidyForm.getWorkEndTime()).find()) {
			result.addError(new FieldError(result.getObjectName(), "workEndTime", "* " + messageUtil
					.getMessage(Constants.VALID_KEY_WORK24TIME, new String[] { "終業時間" })));
		}
		if (StringUtils.isBlank(companySubsidyForm.getRestStartTime())) {
			result.addError(
					new FieldError(result.getObjectName(), "restStartTime", "* " + messageUtil
							.getMessage(Constants.VALID_KEY_REQUIRED, new String[] { "休憩開始時間" })));
		} else if (!trainingTimePattern.matcher(companySubsidyForm.getRestStartTime()).find()) {
			result.addError(new FieldError(result.getObjectName(), "restStartTime",
					"* " + messageUtil.getMessage(Constants.VALID_KEY_WORK24TIME,
							new String[] { "休憩開始時間" })));
		}
		if (StringUtils.isBlank(companySubsidyForm.getRestEndTime())) {
			result.addError(new FieldError(result.getObjectName(), "restEndTime", "* " + messageUtil
					.getMessage(Constants.VALID_KEY_REQUIRED, new String[] { "休憩終了時間" })));
		} else if (!trainingTimePattern.matcher(companySubsidyForm.getRestEndTime()).find()) {
			result.addError(new FieldError(result.getObjectName(), "restEndTime", "* " + messageUtil
					.getMessage(Constants.VALID_KEY_WORK24TIME, new String[] { "休憩終了時間" })));
		}

		if (StringUtils.isBlank(companySubsidyForm.getHoliday())) {
			result.addError(new FieldError(result.getObjectName(), "holiday", "* "
					+ messageUtil.getMessage(Constants.VALID_KEY_REQUIRED, new String[] { "休日" })));
		}

		if (StringUtils.isBlank(companySubsidyForm.getSubsidyPhoneNumber1())
				|| StringUtils.isBlank(companySubsidyForm.getSubsidyPhoneNumber2())
				|| StringUtils.isBlank(companySubsidyForm.getSubsidyPhoneNumber3())) {
			result.addError(
					new FieldError(result.getObjectName(), "subsidyPhoneNumber1", "* " + messageUtil
							.getMessage(Constants.VALID_KEY_REQUIRED, new String[] { "担当電話番号" })));
			result.addError(new FieldError(result.getObjectName(), "subsidyPhoneNumber2", null));
			result.addError(new FieldError(result.getObjectName(), "subsidyPhoneNumber3", null));
		} else if (!phoneNumber1Pattern.matcher(companySubsidyForm.getSubsidyPhoneNumber1()).find()
				|| !phoneNumber2Pattern.matcher(companySubsidyForm.getSubsidyPhoneNumber2()).find()
				|| !phoneNumber2Pattern.matcher(companySubsidyForm.getSubsidyPhoneNumber3())
						.find()) {
			result.addError(
					new FieldError(result.getObjectName(), "subsidyPhoneNumber1", "* " + messageUtil
							.getMessage(Constants.VALID_KEY_INVALID, new String[] { "担当電話番号" })));
			result.addError(new FieldError(result.getObjectName(), "subsidyPhoneNumber2", null));
			result.addError(new FieldError(result.getObjectName(), "subsidyPhoneNumber3", null));
		}

		if (StringUtils.isBlank(companySubsidyForm.getPrefecture())) {
			result.addError(new FieldError(result.getObjectName(), "prefecture", "* " + messageUtil
					.getMessage(Constants.VALID_KEY_REQUIRED_SELECT, new String[] { "労働局都道府県" })));
		}
	}

	/**
	 * Task.66 企業(助成金)情報の登録
	 * 
	 * @param companySubsidyForm
	 */
	public void registCompanySubsidyInfo(CompanySubsidyForm companySubsidyForm) {
		Date today = new Date();
		MCompany mCompany = new MCompany();
		BeanUtils.copyProperties(companySubsidyForm, mCompany);
		mCompany.setLastModifiedDate(today);
		mCompany.setLastModifiedUser(loginUserDto.getLmsUserId());
		mCompanyMapper.updateCompanySubsidyInfo(mCompany);
	}

	/**
	 * Task.67 助成金情報一覧取得
	 * 
	 * @return 助成金カテゴリDTOリスト
	 */
	public List<SubsidyCategoryDto> getSubsidyCategoryList() {
		List<SubsidyCategoryDto> subsidyCategoryDtoList = mSubsidyCategoryMapper
				.getSubsidyCategoryDtoList(Constants.DB_FLG_FALSE);
		return subsidyCategoryDtoList;
	}

}
