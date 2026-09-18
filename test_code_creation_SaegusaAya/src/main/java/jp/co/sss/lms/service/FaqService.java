package jp.co.sss.lms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.sss.lms.dto.FaqCategoryDto;
import jp.co.sss.lms.dto.FaqDto;
import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.entity.MFrequentlyAskedQuestion;
import jp.co.sss.lms.entity.MFrequentlyAskedQuestionCategory;
import jp.co.sss.lms.form.FaqCategoryRegistForm;
import jp.co.sss.lms.form.FaqCategorySearchForm;
import jp.co.sss.lms.form.FaqRegistForm;
import jp.co.sss.lms.form.FaqSearchForm;
import jp.co.sss.lms.mapper.MFrequentlyAskedQuestionCategoryMapper;
import jp.co.sss.lms.mapper.MFrequentlyAskedQuestionMapper;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.MessageUtil;

/**
 * Task.11 よくある質問情報サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class FaqService {

	@Autowired
	private MFrequentlyAskedQuestionCategoryMapper mFrequentlyAskedQuestionCategoryMapper;
	@Autowired
	private MFrequentlyAskedQuestionMapper mFrequentlyAskedQuestionMapper;
	@Autowired
	private MessageUtil messageUtil;
	@Autowired
	private LoginUserDto loginUserDto;

	/**
	 * Task.11 よくある質問カテゴリを全件取得
	 * 
	 * @return よくある質問カテゴリDTOリスト
	 */
	public List<FaqCategoryDto> getFaqCategoryList() {
		List<FaqCategoryDto> faqCategoryDtoList = mFrequentlyAskedQuestionCategoryMapper
				.getFaqCategoryDtoList(Constants.DB_FLG_FALSE);
		return faqCategoryDtoList;
	}

	/**
	 * Task.11 よくある質問を取得
	 * 
	 * @param faqSearchForm
	 * @return よくある質問DTOリスト
	 */
	public List<FaqDto> getFaqDtoList(FaqSearchForm faqSearchForm) {
		List<FaqDto> faqDtoList = new ArrayList<>();
		if (faqSearchForm.getFrequentlyAskedQuestionCategoryId() == null
				&& StringUtils.isBlank(faqSearchForm.getKeyword())) {
			// 全件検索
			faqDtoList = mFrequentlyAskedQuestionMapper.getFaqDtoList(Constants.DB_FLG_FALSE, null,
					null);
		} else if (!StringUtils.isBlank(faqSearchForm.getKeyword())) {
			// キーワード検索
			faqDtoList = mFrequentlyAskedQuestionMapper.getFaqDtoList(Constants.DB_FLG_FALSE,
					faqSearchForm.getKeyword(), null);
		} else if (faqSearchForm.getFrequentlyAskedQuestionCategoryId() != null) {
			// カテゴリID検索
			faqDtoList = mFrequentlyAskedQuestionMapper.getFaqDtoList(Constants.DB_FLG_FALSE, null,
					faqSearchForm.getFrequentlyAskedQuestionCategoryId());
		}
		return faqDtoList;
	}

	/**
	 * Task.89 よくある質問カテゴリを取得（検索）
	 * 
	 * @param faqCategoryName
	 * @return よくある質問カテゴリDTOリスト
	 */
	public List<FaqCategoryDto> getFaqCategoryDtoList(FaqCategorySearchForm faqCategorySearchForm) {

		List<FaqCategoryDto> faqCategoryDtoList = mFrequentlyAskedQuestionCategoryMapper
				.getFaqCategoryDtoListByCategoryName(faqCategorySearchForm.getFaqCategoryName(),
						Constants.DB_FLG_FALSE);

		return faqCategoryDtoList;
	}

	/**
	 * Task.89 よくある質問カテゴリ削除前の入力チェック
	 * 
	 * @param faqCategorySearchForm
	 * @return エラーメッセージ
	 */
	public String deleteCategoryInputCheck(FaqCategorySearchForm faqCategorySearchForm) {
		Integer count = mFrequentlyAskedQuestionMapper
				.getFaqCount(faqCategorySearchForm.getFaqCategoryId(), Constants.DB_FLG_FALSE);
		if (count > 0) {
			return "* " + messageUtil.getMessage(Constants.VALID_KEY_DELETE_RELATION,
					new String[] { "質問" });
		}
		return null;
	}

	/**
	 * Task.89 よくある質問カテゴリを削除
	 * 
	 * @param faqCategorySearchForm
	 * @return 完了メッセージ
	 */
	public String categoryDelete(FaqCategorySearchForm faqCategorySearchForm) {
		Date today = new Date();
		MFrequentlyAskedQuestionCategory mFrequentlyAskedQuestionCategory = new MFrequentlyAskedQuestionCategory();
		mFrequentlyAskedQuestionCategory
				.setFrequentlyAskedQuestionCategoryId(faqCategorySearchForm.getFaqCategoryId());
		mFrequentlyAskedQuestionCategory.setDeleteFlg(Constants.DB_FLG_TRUE);
		mFrequentlyAskedQuestionCategory.setLastModifiedUser(loginUserDto.getLmsUserId());
		mFrequentlyAskedQuestionCategory.setLastModifiedDate(today);
		mFrequentlyAskedQuestionCategoryMapper.updateDeleteFlg(mFrequentlyAskedQuestionCategory);
		return messageUtil.getMessage(Constants.PROP_KEY_DELETE_COMPLETE,
				new String[] { "質問カテゴリー" });
	}

	/**
	 * Task.89 よくある質問を削除
	 * 
	 * @param frequentlyAskedQuestionId
	 */
	public String delete(Integer frequentlyAskedQuestionId) {
		Date today = new Date();
		MFrequentlyAskedQuestion mFrequentlyAskedQuestion = new MFrequentlyAskedQuestion();
		mFrequentlyAskedQuestion.setFrequentlyAskedQuestionId(frequentlyAskedQuestionId);
		mFrequentlyAskedQuestion.setDeleteFlg(Constants.DB_FLG_TRUE);
		mFrequentlyAskedQuestion.setLastModifiedUser(loginUserDto.getLmsUserId());
		mFrequentlyAskedQuestion.setLastModifiedDate(today);
		mFrequentlyAskedQuestionMapper.updateDeleteFlg(mFrequentlyAskedQuestion);
		return messageUtil.getMessage(Constants.PROP_KEY_DELETE_COMPLETE,
				new String[] { "よくある質問" });
	}

	/**
	 * Task.90 よくある質問カテゴリを取得
	 * 
	 * @param faqCategoryRegistForm
	 */
	public void setFaqCategoryRegistForm(FaqCategoryRegistForm faqCategoryRegistForm) {
		if (faqCategoryRegistForm.getActionType() == 0) {
			MFrequentlyAskedQuestionCategory mFaqCategory = mFrequentlyAskedQuestionCategoryMapper
					.findByFrequentlyAskedQuestionCategoryId(
							faqCategoryRegistForm.getFaqCategoryId(), Constants.DB_FLG_FALSE);
			faqCategoryRegistForm
					.setFaqCategoryName(mFaqCategory.getFrequentlyAskedQuestionCategoryName());
		}
	}

	/**
	 * Task.90 よくある質問カテゴリを登録
	 * 
	 * @param faqCategoryRegistForm
	 */
	public String categoryComplete(FaqCategoryRegistForm faqCategoryRegistForm) {
		Date now = new Date();
		MFrequentlyAskedQuestionCategory mFrequentlyAskedQuestionCategory = new MFrequentlyAskedQuestionCategory();
		mFrequentlyAskedQuestionCategory
				.setFrequentlyAskedQuestionCategoryName(faqCategoryRegistForm.getFaqCategoryName());
		mFrequentlyAskedQuestionCategory.setLastModifiedUser(loginUserDto.getLmsUserId());
		mFrequentlyAskedQuestionCategory.setLastModifiedDate(now);
		if (faqCategoryRegistForm.getActionType() == 1) {
			// 登録
			mFrequentlyAskedQuestionCategory.setDeleteFlg(Constants.DB_FLG_FALSE);
			mFrequentlyAskedQuestionCategory.setFirstCreateUser(loginUserDto.getLmsUserId());
			mFrequentlyAskedQuestionCategory.setFirstCreateDate(now);
			mFrequentlyAskedQuestionCategoryMapper.insert(mFrequentlyAskedQuestionCategory);
			return messageUtil.getMessage(Constants.PROP_KEY_REGIST_COMPLETE,
					new String[] { "質問カテゴリー" });
		} else {
			// 更新
			mFrequentlyAskedQuestionCategory
					.setFrequentlyAskedQuestionCategoryId(faqCategoryRegistForm.getFaqCategoryId());
			mFrequentlyAskedQuestionCategoryMapper.update(mFrequentlyAskedQuestionCategory);
			return messageUtil.getMessage(Constants.PROP_KEY_UPDATE_COMPLETE,
					new String[] { "質問カテゴリー" });
		}
	}

	/**
	 * Task.91 よくある質問を取得
	 * 
	 * @param faqRegistForm
	 */
	public void setFaqRegistForm(FaqRegistForm faqRegistForm) {
		if (faqRegistForm.getActionType() == 0) {
			MFrequentlyAskedQuestion mFaq = mFrequentlyAskedQuestionMapper
					.findByFrequentlyAskedQuestionId(faqRegistForm.getFaqId(),
							Constants.DB_FLG_FALSE);
			faqRegistForm.setFaqCategoryId(mFaq.getFrequentlyAskedQuestionCategoryId());
			faqRegistForm.setQuestion(mFaq.getQuestion());
			faqRegistForm.setAnswer(mFaq.getAnswer());
		}
	}

	/**
	 * Task.91 よくある質問を登録
	 * 
	 * @param faqRegistForm
	 */
	public String registComlete(FaqRegistForm faqRegistForm) {
		Date now = new Date();
		MFrequentlyAskedQuestion mFrequentlyAskedQuestion = new MFrequentlyAskedQuestion();
		mFrequentlyAskedQuestion
				.setFrequentlyAskedQuestionCategoryId(faqRegistForm.getFaqCategoryId());
		mFrequentlyAskedQuestion.setQuestion(faqRegistForm.getQuestion());
		mFrequentlyAskedQuestion.setAnswer(faqRegistForm.getAnswer());
		mFrequentlyAskedQuestion.setLastModifiedUser(loginUserDto.getLmsUserId());
		mFrequentlyAskedQuestion.setLastModifiedDate(now);
		if (faqRegistForm.getActionType() == 1) {
			// 登録
			mFrequentlyAskedQuestion.setDeleteFlg(Constants.DB_FLG_FALSE);
			mFrequentlyAskedQuestion.setFirstCreateUser(loginUserDto.getLmsUserId());
			mFrequentlyAskedQuestion.setFirstCreateDate(now);
			mFrequentlyAskedQuestionMapper.insert(mFrequentlyAskedQuestion);
			return messageUtil.getMessage(Constants.PROP_KEY_REGIST_COMPLETE,
					new String[] { "よくある質問" });
		} else {
			// 更新
			mFrequentlyAskedQuestion.setFrequentlyAskedQuestionId(faqRegistForm.getFaqId());
			mFrequentlyAskedQuestionMapper.update(mFrequentlyAskedQuestion);
			return messageUtil.getMessage(Constants.PROP_KEY_UPDATE_COMPLETE,
					new String[] { "よくある質問" });
		}
	}

}