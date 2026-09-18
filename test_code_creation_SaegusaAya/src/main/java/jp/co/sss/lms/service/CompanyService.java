package jp.co.sss.lms.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import jp.co.sss.lms.dto.CompanyDto;
import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.entity.MCompany;
import jp.co.sss.lms.entity.MFssGroup;
import jp.co.sss.lms.entity.TCompanyFssGroup;
import jp.co.sss.lms.entity.TUserCompany;
import jp.co.sss.lms.form.CompanyListForm;
import jp.co.sss.lms.form.CompanyRegistForm;
import jp.co.sss.lms.mapper.MCompanyMapper;
import jp.co.sss.lms.mapper.MFssGroupMapper;
import jp.co.sss.lms.mapper.TCompanyFssGroupMapper;
import jp.co.sss.lms.mapper.TUserCompanyMapper;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.MessageUtil;
import jp.co.sss.lms.util.StringUtil;

/**
 * 企業情報サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class CompanyService {

	@Autowired
	private MCompanyMapper mCompanyMapper;
	@Autowired
	private MessageUtil messageUtil;
	@Autowired
	private TUserCompanyMapper tUserCompanyMapper;
	@Autowired
	private LoginUserDto loginUserDto;
	@Autowired
	private MFssGroupMapper mFssGroupMapper;
	@Autowired
	private TCompanyFssGroupMapper tCompanyFssGroupMapper;

	/**
	 * Task.43 企業DTOリストの取得
	 * 
	 * @return 企業DTOリスト
	 */
	// 講師・管理者画面で汎用的に使われる
	public List<CompanyDto> getCompanyDtoList() {
		return mCompanyMapper.getCompanyDto(Constants.DB_FLG_FALSE);
	}

	/**
	 * Task.104 企業一覧フォームの設定
	 * 
	 * @param companyListForm
	 */
	public void setCompanyListForm(CompanyListForm companyListForm) {
		// データリスト用企業DTOリスト
		List<CompanyDto> companyDtoList1 = getCompanyDtoList();
		companyListForm.setCompanyDtoList1(companyDtoList1);
		// 検索結果用企業DTOリスト
		String paramCompanyName = companyListForm.getCompanyName();
		String companyName = null;
		String address = null;
		if (paramCompanyName != null
				&& paramCompanyName.contains(Constants.SEPARATE_ADDRESS_JUDGE_CHARACTER)) {
			String[] placeDescription = paramCompanyName
					.split(Constants.SEPARATE_ADDRESS_REGEX_JUDGE_CHARACTER, -1);
			companyName = placeDescription[0];
			address = placeDescription[1];
		} else {
			companyName = paramCompanyName;
		}
		List<CompanyDto> companyDtoList2 = mCompanyMapper.getCompanyDtoForSearch(companyName,
				address, Constants.DB_FLG_FALSE);
		companyListForm.setCompanyDtoList2(companyDtoList2);
	}

	/**
	 * Task.104 企業検索入力チェック
	 * 
	 * @param companyListForm
	 * @param result
	 */
	public void searchCheck(CompanyListForm companyListForm, BindingResult result) {
		if (1 < StringUtil.separatorCount(Constants.SEPARATE_ADDRESS_JUDGE_CHARACTER.charAt(0),
				companyListForm.getCompanyName())) {
			result.addError(new FieldError(result.getObjectName(), "companyName",
					"* " + messageUtil.getMessage(Constants.VALID_KEY_SEARCHTOCOMPANYNAMEERROR)));
		}
	}

	/**
	 * Task.104 企業削除前のチェック
	 * 
	 * @param companyListForm
	 */
	public void checkCompanyDelete(CompanyListForm companyListForm, BindingResult result) {
		// 削除対象の存在チェック
		MCompany mCompany = mCompanyMapper.findByCompanyId(companyListForm.getCompanyId(),
				Constants.DB_FLG_FALSE);
		if (mCompany == null) {
			String company = messageUtil.getMessage("company");
			result.addError(new FieldError(result.getObjectName(), "companyId", "* " + messageUtil
					.getMessage(Constants.VALID_KEY_ALREADYDELETE, new String[] { company })));
			return;
		}
		// 削除対象企業とのユーザー紐づきチェック
		List<TUserCompany> tUserCompanyList = tUserCompanyMapper
				.findByCompanyId(companyListForm.getCompanyId(), Constants.DB_FLG_FALSE);
		if (tUserCompanyList.size() > 0) {
			String user = messageUtil.getMessage("user");
			result.addError(new FieldError(result.getObjectName(),
					"companyDtoList2[" + companyListForm.getSelectedIndex() + "].companyId",
					"* " + messageUtil.getMessage(Constants.VALID_KEY_DELETE_RELATION,
							new String[] { user })));
			return;
		}
	}

	/**
	 * Task.104 企業情報の削除
	 * 
	 * @param companyListForm
	 * @return 完了メッセージ
	 */
	public String delete(CompanyListForm companyListForm) {
		String company = messageUtil.getMessage("company");
		Date today = new Date();
		MCompany mCompany = new MCompany();
		mCompany.setCompanyId(companyListForm.getCompanyId());
		mCompany.setDeleteFlg(Constants.DB_FLG_TRUE);
		mCompany.setLastModifiedUser(loginUserDto.getLmsUserId());
		mCompany.setLastModifiedDate(today);
		mCompanyMapper.updateDeleteFlg(mCompany);
		return messageUtil.getMessage(Constants.PROP_KEY_DELETE_COMPLETE, new String[] { company });
	}

	/**
	 * Task.105 企業登録フォームの設定
	 * 
	 * @param companyRegistForm
	 */
	public void setCompanyRegistForm(CompanyRegistForm companyRegistForm) {
		if (companyRegistForm.getCompanyId() == null) {
			companyRegistForm.setFileShareFlg(Constants.FILESHARE_FLG_TRUE);
		} else {
			MCompany mCompany = mCompanyMapper.findByCompanyId(companyRegistForm.getCompanyId(),
					Constants.DB_FLG_FALSE);
			companyRegistForm.setCompanyName(mCompany.getCompanyName());
			companyRegistForm.setFileShareFlg(mCompany.getFileShareFlg());
		}
	}

	/**
	 * Task.105 企業情報の登録または更新
	 * 
	 * @param companyRegistForm
	 * @return 結果メッセージ
	 */
	public String registComplete(CompanyRegistForm companyRegistForm) {

		String company = messageUtil.getMessage("company");
		Date now = new Date();

		if (companyRegistForm.getCompanyId() == null) {
			// 登録
			MCompany mCompany = new MCompany();
			mCompany.setCompanyName(companyRegistForm.getCompanyName());
			if (companyRegistForm.getFileShareFlg() == null
					|| companyRegistForm.getFileShareFlg() != 1) {
				mCompany.setFileShareFlg(Constants.DB_FLG_FALSE);
			} else {
				mCompany.setFileShareFlg(companyRegistForm.getFileShareFlg());
			}
			mCompany.setDeleteFlg(Constants.DB_FLG_FALSE);
			mCompany.setFirstCreateUser(loginUserDto.getLmsUserId());
			mCompany.setFirstCreateDate(now);
			mCompany.setLastModifiedUser(loginUserDto.getLmsUserId());
			mCompany.setLastModifiedDate(now);
			mCompanyMapper.insert(mCompany);

			MFssGroup mFssGroup = new MFssGroup();
			mFssGroup.setGroupName(companyRegistForm.getCompanyName());
			mFssGroup.setDescription(company);
			mFssGroup.setDeleteFlg(Constants.DB_FLG_FALSE);
			mFssGroup.setFirstCreateUser(loginUserDto.getLmsUserId());
			mFssGroup.setFirstCreateDate(now);
			mFssGroup.setLastModifiedUser(loginUserDto.getLmsUserId());
			mFssGroup.setLastModifiedDate(now);
			mFssGroupMapper.insert(mFssGroup);

			TCompanyFssGroup tCompanyFssGroup = new TCompanyFssGroup();
			tCompanyFssGroup.setCompanyId(mCompany.getCompanyId());
			tCompanyFssGroup.setFssGroupId(mFssGroup.getFssGroupId());
			tCompanyFssGroup.setDeleteFlg(Constants.DB_FLG_FALSE);
			tCompanyFssGroup.setFirstCreateUser(loginUserDto.getLmsUserId());
			tCompanyFssGroup.setFirstCreateDate(now);
			tCompanyFssGroup.setLastModifiedUser(loginUserDto.getLmsUserId());
			tCompanyFssGroup.setLastModifiedDate(now);
			tCompanyFssGroupMapper.insert(tCompanyFssGroup);

			return messageUtil.getMessage(Constants.PROP_KEY_REGIST_COMPLETE,
					new String[] { company });
		} else {
			// 更新
			MCompany mCompany = mCompanyMapper.findByCompanyId(companyRegistForm.getCompanyId(),
					Constants.DB_FLG_FALSE);
			mCompany.setCompanyName(companyRegistForm.getCompanyName());
			if (companyRegistForm.getFileShareFlg() == null
					|| companyRegistForm.getFileShareFlg() != 1) {
				mCompany.setFileShareFlg(Constants.DB_FLG_FALSE);
			} else {
				mCompany.setFileShareFlg(companyRegistForm.getFileShareFlg());
			}
			mCompany.setLastModifiedUser(loginUserDto.getLmsUserId());
			mCompany.setLastModifiedDate(now);
			mCompanyMapper.updateCompanyName(mCompany);

			TCompanyFssGroup tCompanyFssGroup = tCompanyFssGroupMapper
					.findByCompanyId(companyRegistForm.getCompanyId(), Constants.DB_FLG_FALSE);
			MFssGroup mFssGroup = new MFssGroup();
			mFssGroup.setFssGroupId(tCompanyFssGroup.getFssGroupId());
			mFssGroup.setGroupName(companyRegistForm.getCompanyName());
			mFssGroup.setDescription(company);
			mFssGroup.setLastModifiedUser(loginUserDto.getLmsUserId());
			mFssGroup.setLastModifiedDate(now);
			mFssGroupMapper.updateGroupName(mFssGroup);

			return messageUtil.getMessage(Constants.PROP_KEY_UPDATE_COMPLETE,
					new String[] { company });
		}

	}

}
