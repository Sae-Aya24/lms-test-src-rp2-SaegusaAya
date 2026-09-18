package jp.co.sss.lms.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import jp.co.sss.lms.dto.ExamDetailDto;
import jp.co.sss.lms.dto.ExamDto;
import jp.co.sss.lms.dto.ExamQuestionDto;
import jp.co.sss.lms.dto.ExamResultDetailDto;
import jp.co.sss.lms.dto.ExamResultDto;
import jp.co.sss.lms.dto.LmsUserDto;
import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.dto.QuestionDto;
import jp.co.sss.lms.entity.MExam;
import jp.co.sss.lms.entity.MGenreDetail;
import jp.co.sss.lms.entity.MQuestion;
import jp.co.sss.lms.entity.TExamResult;
import jp.co.sss.lms.entity.TExamResultDetail;
import jp.co.sss.lms.form.ExamQuestionForm;
import jp.co.sss.lms.form.ExamRegistForm;
import jp.co.sss.lms.form.ExamResultListForm;
import jp.co.sss.lms.mapper.MExamMapper;
import jp.co.sss.lms.mapper.MGenreDetailMapper;
import jp.co.sss.lms.mapper.MLmsUserMapper;
import jp.co.sss.lms.mapper.MQuestionMapper;
import jp.co.sss.lms.mapper.TExamResultDetailMapper;
import jp.co.sss.lms.mapper.TExamResultMapper;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.DateUtil;
import jp.co.sss.lms.util.LoginUserUtil;
import jp.co.sss.lms.util.MessageUtil;

/**
 * 試験情報サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class ExamService {

	@Autowired
	private TExamResultMapper tExamResultMapper;
	@Autowired
	private MGenreDetailMapper mGenreDetailMapper;
	@Autowired
	private MExamMapper mExamMapper;
	@Autowired
	private TExamResultDetailMapper tExamResultDetailMapper;
	@Autowired
	private MQuestionMapper mQuestionMapper;
	@Autowired
	private MLmsUserMapper mLmsUserMapper;
	@Autowired
	private LoginUserDto loginUserDto;
	@Autowired
	private LoginUserUtil loginUserUtil;
	@Autowired
	private MessageUtil messageUtil;
	@Autowired
	private DateUtil dateUtil;

	/**
	 * 試験詳細情報を取得
	 * 
	 * @param examSectionId
	 * @param lmsUserId
	 * @return 試験詳細DTO
	 */
	public ExamDetailDto getExamDetail(Integer examSectionId, Integer lmsUserId) {
		// 試験詳細DTOを取得
		ExamDetailDto examDetailDto = tExamResultMapper.getExamDetailDto(examSectionId, lmsUserId,
				Constants.DB_FLG_FALSE);
		// Task.34 試験の解答時間の表示
		for (ExamResultDto examResultDto : examDetailDto.getExamResultDtoList()) {
			examResultDto.setTimeDisp(dateUtil.getTimeFormat(examResultDto.getTime()));
		}
		return examDetailDto;
	}

	/**
	 * 試験結果詳細情報取得
	 * 
	 * @param examResultId
	 * @return 試験結果詳細情報
	 */
	public ExamResultDetailDto getExamResultDetail(Integer examResultId) {

		// 試験結果詳細を取得
		ExamResultDetailDto examResultDetailDto = tExamResultMapper
				.getExamResultDetail(examResultId, Constants.DB_FLG_FALSE);

		// 試験結果詳細を基に試験結果詳細DTO．問題DTOリスト．回答リストを設定
		for (QuestionDto questionDto : examResultDetailDto.getQuestionDtoList()) {
			List<String> answerList = new LinkedList<>();
			answerList.add(questionDto.getChoice1());
			answerList.add(questionDto.getChoice2());
			answerList.add(questionDto.getChoice3());
			answerList.add(questionDto.getChoice4());
			questionDto.setAnswerList(answerList);
		}

		return examResultDetailDto;

	}

	/**
	 * 試験問題画面フォームの設定
	 * 
	 * @param examQuestionForm
	 */
	public void setExamQuestionForm(ExamQuestionForm examQuestionForm) {
		// 試験問題情報を取得
		ExamQuestionDto examQuestionDto = mExamMapper.getExamQuestion(examQuestionForm.getExamId(),
				Constants.DB_FLG_FALSE);
		BeanUtils.copyProperties(examQuestionDto, examQuestionForm);
		// 試験問題情報を基に試験問題DTO．問題DTOリスト．回答リストを設定
		for (QuestionDto questionDto : examQuestionForm.getQuestionDtoList()) {
			List<String> answerList = new LinkedList<>();
			answerList.add(questionDto.getChoice1());
			answerList.add(questionDto.getChoice2());
			answerList.add(questionDto.getChoice3());
			answerList.add(questionDto.getChoice4());
			questionDto.setAnswerList(answerList);
		}
		// 経過時間の初期化
		if (examQuestionForm.getTime() == null) {
			examQuestionForm.setTime(0);
		}
		// 回答の再設定
		int questionCount = examQuestionDto.getQuestionDtoList().size();
		Short[] answerArray = new Short[questionCount];
		Short[] userAnswer = examQuestionForm.getAnswer();
		if (userAnswer != null) {
			for (int i = 0; i < userAnswer.length; i++) {
				answerArray[i] = userAnswer[i];
			}
		}
		examQuestionForm.setAnswer(answerArray);
	}

	/**
	 * Task.37 回答数の表示
	 * 
	 * @param examQuestionForm
	 * @return 回答数・問題数の配列
	 */
	public int[] getAnswerQuestionCount(ExamQuestionForm examQuestionForm) {
		int answerCount = 0;
		for (Short answer : examQuestionForm.getAnswer()) {
			if (answer != null) {
				answerCount++;
			}
		}
		int questionCount = examQuestionForm.getQuestionDtoList().size();
		return new int[] { answerCount, questionCount };
	}

	/**
	 * 試験結果登録
	 * 
	 * @param examQuestionForm
	 * @return examResultId
	 */
	public Integer insert(ExamQuestionForm examQuestionForm) {

		// 試験IDに紐づく試験問題リストを取得
		List<MQuestion> mQuestionList = mQuestionMapper.findByExamId(examQuestionForm.getExamId(),
				Constants.DB_FLG_FALSE);

		// 現在日時情報
		Date now = new Date();

		// 試験結果エンティティを生成
		TExamResult tExamResult = new TExamResult();

		// 得点設定
		Short score = 0;
		Short[] answerArray = examQuestionForm.getAnswer();
		for (int i = 0; i < answerArray.length; i++) {
			if (answerArray[i] != null && answerArray[i] == mQuestionList.get(i).getAnswerNum()) {
				score++;
			}
		}

		// 試験結果件数取得
		Integer examCount = tExamResultMapper.getExamCount(examQuestionForm.getExamSectionId(),
				loginUserDto.getLmsUserId(), Constants.DB_FLG_FALSE);

		// 試験結果の設定
		tExamResult.setExamSectionId(examQuestionForm.getExamSectionId());
		tExamResult.setLmsUserId(loginUserDto.getLmsUserId());
		tExamResult.setScore(score);
		tExamResult.setTime(examQuestionForm.getTime());
		if (examCount == 0) {
			tExamResult.setMarkFlg(Constants.DB_FLG_TRUE);
		} else {
			tExamResult.setMarkFlg(Constants.DB_FLG_FALSE);
		}
		tExamResult.setAccountId(loginUserDto.getAccountId());
		tExamResult.setDeleteFlg(Constants.DB_FLG_FALSE);
		tExamResult.setFirstCreateUser(loginUserDto.getLmsUserId());
		tExamResult.setFirstCreateDate(now);
		tExamResult.setLastModifiedUser(loginUserDto.getLmsUserId());
		tExamResult.setLastModifiedDate(now);

		// 試験結果へ登録
		tExamResultMapper.insert(tExamResult);

		// 試験結果詳細登録
		for (int j = 0; j < mQuestionList.size(); j++) {
			// 試験結果詳細を生成
			TExamResultDetail tExamResultDetail = new TExamResultDetail();
			tExamResultDetail.setExamResultId(tExamResult.getExamResultId());
			tExamResultDetail.setLmsUserId(loginUserDto.getLmsUserId());
			tExamResultDetail.setQuestionId(mQuestionList.get(j).getQuestionId());
			Short reply = 0;
			try {
				reply = examQuestionForm.getAnswer()[j];
			} catch (Exception e) {
				reply = 0;
			}
			tExamResultDetail.setReply(reply);
			tExamResultDetail.setAccountId(loginUserDto.getAccountId());
			tExamResultDetail.setDeleteFlg(Constants.DB_FLG_FALSE);
			tExamResultDetail.setFirstCreateUser(loginUserDto.getLmsUserId());
			tExamResultDetail.setFirstCreateDate(now);
			tExamResultDetail.setLastModifiedUser(loginUserDto.getLmsUserId());
			tExamResultDetail.setLastModifiedDate(now);
			// 試験結果詳細へ登録
			tExamResultDetailMapper.insert(tExamResultDetail);
		}

		return tExamResult.getExamResultId();
	}

	/**
	 * Task.51 試験DTOリスト取得
	 * 
	 * @param examName
	 * @return 試験DTOリスト
	 */
	public List<ExamDto> getExamDtoList(String examName) {
		// Task.81 ②検索条件の追加
		Integer placeId = loginUserUtil.isAdmin() ? null : loginUserDto.getPlaceId();
		return mExamMapper.getExamDtoList(placeId, examName, Constants.DB_FLG_FALSE);
	}

	/**
	 * Task.52 試験詳細DTO取得（試験ID）
	 * 
	 * @param examId
	 * @return 試験詳細DTOリスト
	 */
	public ExamDetailDto getExamDetailDto(Integer examId) {
		return mExamMapper.getExamDetailDto(examId, Constants.DB_FLG_FALSE);
	}

	/**
	 * Task.52 ユーザー詳細DTO（試験結果）リストの取得
	 * 
	 * @param examResultListForm
	 * @param numOfQuestion
	 * @return ユーザー詳細DTO（試験結果）リスト
	 */
	public List<LmsUserDto> getLmsUserDtoList(ExamResultListForm examResultListForm,
			Integer numOfQuestion) {

		List<LmsUserDto> lmsUserDtoList = mLmsUserMapper.getLmsUserDto(
				examResultListForm.getExamId(), examResultListForm.getCourseId(),
				examResultListForm.getCompanyId(), loginUserDto.getPlaceId(),
				Constants.CODE_VAL_ROLL_STUDENT, Constants.DB_FLG_FALSE);

		for (LmsUserDto lmsUserDto : lmsUserDtoList) {
			for (ExamResultDto examResultDto : lmsUserDto.getExamResultDtoList()) {
				if (examResultDto.getScore() != null) {
					BigDecimal bd = new BigDecimal(
							(double) examResultDto.getScore() / numOfQuestion * 100);
					examResultDto.setPoint(bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
				}
			}
		}

		return lmsUserDtoList;

	}

	/**
	 * Task.53 試験結果の論理削除
	 * 
	 * @param lmsUserId
	 * @return 完了メッセージ
	 */
	public String deleteResultList(Integer examSectionId, Integer lmsUserId) {
		Date date = new Date();
		TExamResult tExamResult = new TExamResult();
		tExamResult.setExamSectionId(examSectionId);
		tExamResult.setLmsUserId(lmsUserId);
		tExamResult.setDeleteFlg(Constants.DB_FLG_TRUE);
		tExamResult.setLastModifiedUser(loginUserDto.getLmsUserId());
		tExamResult.setLastModifiedDate(date);
		tExamResultMapper.updateDeleteFlg(tExamResult);
		return messageUtil.getMessage(Constants.PROP_KEY_DELETE_COMPLETE, new String[] { "試験結果" });
	}

	/**
	 * Task.56 試験DTO取得
	 * 
	 * @param examId
	 * @return 試験DTO
	 */
	public ExamDto getExamDto(Integer examId) {
		return mExamMapper.getExamDto(examId, Constants.DB_FLG_FALSE);
	}

	/**
	 * Task.56 試験問題解答情報取得
	 * 
	 * @param examResultId
	 * @return 試験結果詳細情報
	 */
	public List<QuestionDto> getExamPreview(Integer examId) {
		// 試験IDに紐づく試験問題リストを取得
		List<MQuestion> mQuestionList = mQuestionMapper.findByExamId(examId,
				Constants.DB_FLG_FALSE);

		List<QuestionDto> questionDtoList = new ArrayList<QuestionDto>();

		for (MQuestion mQuestion : mQuestionList) {
			QuestionDto questionDto = new QuestionDto();
			// 試験問題に紐づくジャンルを取得
			MGenreDetail mGenreDetail = mGenreDetailMapper
					.findByGenreDetailId(mQuestion.getGenreDetailId(), Constants.DB_FLG_FALSE);
			questionDto.setQuestionId(mQuestion.getQuestionId());
			questionDto.setQuestion(mQuestion.getQuestion());
			questionDto.setGenreDetailName(mGenreDetail.getGenreDetailName());
			questionDto.setChoice1(mQuestion.getChoice1());
			questionDto.setChoice2(mQuestion.getChoice2());
			questionDto.setChoice3(mQuestion.getChoice3());
			questionDto.setChoice4(mQuestion.getChoice4());
			questionDto.setAnswerNum(mQuestion.getAnswerNum());
			questionDto.setExplain(mQuestion.getExplain());
			questionDtoList.add(questionDto);
		}
		return questionDtoList;

	}

	/**
	 * Task.81 試験情報の削除
	 * 
	 * @param examId
	 * @return 完了メッセージ
	 */
	public String delete(Integer examId) {
		Date date = new Date();
		MExam mExam = new MExam();
		mExam.setExamId(examId);
		mExam.setDeleteFlg(Constants.DB_FLG_TRUE);
		mExam.setLastModifiedUser(loginUserDto.getLmsUserId());
		mExam.setLastModifiedDate(date);
		mExamMapper.updateDeleteFlg(mExam);
		return messageUtil.getMessage(Constants.PROP_KEY_DELETE_COMPLETE, new String[] { "試験" });
	}

	/**
	 * Task.112 試験情報の取得
	 * 
	 * @param examRegistForm
	 */
	public void setExamRegistForm(ExamRegistForm examRegistForm) {
		ExamDto examDto = getExamDto(examRegistForm.getExamId());
		examRegistForm.setExamName(examDto.getExamName());
		examRegistForm.setLimitTime(examDto.getLimitTime());
		examRegistForm.setGenreId(examDto.getGenreId());
	}

	/**
	 * Task.113 試験問題情報の取得
	 * 
	 * @param examRegistForm
	 */
	public void setExamRegistFormQuestion(ExamRegistForm examRegistForm) {

		List<MQuestion> mQuestionList = mQuestionMapper.findByExamId(examRegistForm.getExamId(),
				Constants.DB_FLG_FALSE);
		int size = mQuestionList.size();
		examRegistForm.setNumOfQuestion(size);
		examRegistForm.setQuestionId(new Integer[size]);
		examRegistForm.setQuestion(new String[size]);
		examRegistForm.setGrade(new Short[size]);
		examRegistForm.setAnswerNum(new Short[size]);
		examRegistForm.setChoice1(new String[size]);
		examRegistForm.setChoice2(new String[size]);
		examRegistForm.setChoice3(new String[size]);
		examRegistForm.setChoice4(new String[size]);
		examRegistForm.setExplain(new String[size]);
		examRegistForm.setGenreDetailId(new Integer[size]);
		for (int i = 0; i < size; i++) {
			MQuestion mQuestion = mQuestionList.get(i);
			examRegistForm.getQuestionId()[i] = mQuestion.getQuestionId();
			examRegistForm.getQuestion()[i] = mQuestion.getQuestion();
			examRegistForm.getGrade()[i] = mQuestion.getGrade();
			examRegistForm.getAnswerNum()[i] = mQuestion.getAnswerNum();
			examRegistForm.getChoice1()[i] = mQuestion.getChoice1();
			examRegistForm.getChoice2()[i] = mQuestion.getChoice2();
			examRegistForm.getChoice3()[i] = mQuestion.getChoice3();
			examRegistForm.getChoice4()[i] = mQuestion.getChoice4();
			examRegistForm.getExplain()[i] = mQuestion.getExplain();
			examRegistForm.getGenreDetailId()[i] = mQuestion.getGenreDetailId();
		}

	}

	/**
	 * Task.113 試験問題入力欄の追加
	 * 
	 * @param examRegistForm
	 */
	public void addDetail(ExamRegistForm examRegistForm) {
		examRegistForm.setNumOfQuestion(examRegistForm.getNumOfQuestion() + 1);
		examRegistForm.setQuestionId(ArrayUtils.add(examRegistForm.getQuestionId(), 0));
		examRegistForm.setQuestion(ArrayUtils.add(examRegistForm.getQuestion(), new String()));
		examRegistForm.setAnswerNum(ArrayUtils.add(examRegistForm.getAnswerNum(), (short) 0));
		examRegistForm.setChoice1(ArrayUtils.add(examRegistForm.getChoice1(), new String()));
		examRegistForm.setChoice2(ArrayUtils.add(examRegistForm.getChoice2(), new String()));
		examRegistForm.setChoice3(ArrayUtils.add(examRegistForm.getChoice3(), new String()));
		examRegistForm.setChoice4(ArrayUtils.add(examRegistForm.getChoice4(), new String()));
		examRegistForm.setExplain(ArrayUtils.add(examRegistForm.getExplain(), new String()));
		examRegistForm.setGenreDetailId(ArrayUtils.add(examRegistForm.getGenreDetailId(), 0));
	}

	/**
	 * Task.113 試験問題入力欄の削除
	 * 
	 * @param examRegistForm
	 */
	public void remDetail(ExamRegistForm examRegistForm) {
		int num = examRegistForm.getNumOfQuestion() - 1;
		if (num > 0) {
			examRegistForm.setNumOfQuestion(num);
			examRegistForm.setQuestionId(ArrayUtils.remove(examRegistForm.getQuestionId(), num));
			examRegistForm.setQuestion(ArrayUtils.remove(examRegistForm.getQuestion(), num));
			examRegistForm.setAnswerNum(ArrayUtils.remove(examRegistForm.getAnswerNum(), num));
			examRegistForm.setChoice1(ArrayUtils.remove(examRegistForm.getChoice1(), num));
			examRegistForm.setChoice2(ArrayUtils.remove(examRegistForm.getChoice2(), num));
			examRegistForm.setChoice3(ArrayUtils.remove(examRegistForm.getChoice3(), num));
			examRegistForm.setChoice4(ArrayUtils.remove(examRegistForm.getChoice4(), num));
			examRegistForm.setExplain(ArrayUtils.remove(examRegistForm.getExplain(), num));
			examRegistForm
					.setGenreDetailId(ArrayUtils.remove(examRegistForm.getGenreDetailId(), num));
		}
	}

	/**
	 * Task.113 試験問題登録 入力チェック
	 * 
	 * @param examRegistForm
	 * @param result
	 */
	public void questionRegistInputCheck(ExamRegistForm examRegistForm, BindingResult result) {

		for (int i = 0; i < examRegistForm.getNumOfQuestion(); i++) {
			// 必須チェック
			if (StringUtils.isBlank(examRegistForm.getQuestion()[i])) {
				result.addError(
						new FieldError(result.getObjectName(), "question[" + i + "]", messageUtil
								.getMessage(Constants.VALID_KEY_REQUIRED, new String[] { "問題" })));
			}
			if (examRegistForm.getAnswerNum()[i] == null) {
				result.addError(
						new FieldError(result.getObjectName(), "answerNum[" + i + "]", messageUtil
								.getMessage(Constants.VALID_KEY_REQUIRED, new String[] { "正答" })));
			}
			if (StringUtils.isBlank(examRegistForm.getChoice1()[i])) {
				result.addError(new FieldError(result.getObjectName(), "choice1[" + i + "]",
						messageUtil.getMessage(Constants.VALID_KEY_REQUIRED,
								new String[] { "選択肢１" })));
			}
			if (StringUtils.isBlank(examRegistForm.getChoice2()[i])) {
				result.addError(new FieldError(result.getObjectName(), "choice2[" + i + "]",
						messageUtil.getMessage(Constants.VALID_KEY_REQUIRED,
								new String[] { "選択肢２" })));
			}
			if (StringUtils.isBlank(examRegistForm.getChoice3()[i])) {
				result.addError(new FieldError(result.getObjectName(), "choice3[" + i + "]",
						messageUtil.getMessage(Constants.VALID_KEY_REQUIRED,
								new String[] { "選択肢３" })));
			}
			if (StringUtils.isBlank(examRegistForm.getChoice4()[i])) {
				result.addError(new FieldError(result.getObjectName(), "choice4[" + i + "]",
						messageUtil.getMessage(Constants.VALID_KEY_REQUIRED,
								new String[] { "選択肢４" })));
			}
			if (StringUtils.isBlank(examRegistForm.getExplain()[i])) {
				result.addError(
						new FieldError(result.getObjectName(), "explain[" + i + "]", messageUtil
								.getMessage(Constants.VALID_KEY_REQUIRED, new String[] { "解説" })));
			}
			if (examRegistForm.getGenreDetailId()[i] == null) {
				result.addError(new FieldError(result.getObjectName(), "genreDetailId[" + i + "]",
						messageUtil.getMessage(Constants.VALID_KEY_REQUIRED,
								new String[] { "詳細試験カテゴリー" })));
			}

			// 桁数チェック
			if (!result.hasErrors()) {
				if (examRegistForm.getQuestion()[i].length() > 10000) {
					result.addError(new FieldError(result.getObjectName(), "question[" + i + "]",
							messageUtil.getMessage(Constants.VALID_KEY_MAXLENGTH,
									new String[] { "問題", "10000" })));
				}
				if (examRegistForm.getChoice1()[i].length() > 10000) {
					result.addError(new FieldError(result.getObjectName(), "choice1[" + i + "]",
							messageUtil.getMessage(Constants.VALID_KEY_MAXLENGTH,
									new String[] { "選択肢１", "10000" })));
				}
				if (examRegistForm.getChoice2()[i].length() > 10000) {
					result.addError(new FieldError(result.getObjectName(), "choice2[" + i + "]",
							messageUtil.getMessage(Constants.VALID_KEY_MAXLENGTH,
									new String[] { "選択肢２", "10000" })));
				}
				if (examRegistForm.getChoice3()[i].length() > 10000) {
					result.addError(new FieldError(result.getObjectName(), "choice3[" + i + "]",
							messageUtil.getMessage(Constants.VALID_KEY_MAXLENGTH,
									new String[] { "選択肢３", "10000" })));
				}
				if (examRegistForm.getChoice4()[i].length() > 10000) {
					result.addError(new FieldError(result.getObjectName(), "choice4[" + i + "]",
							messageUtil.getMessage(Constants.VALID_KEY_MAXLENGTH,
									new String[] { "選択肢４", "10000" })));
				}
				if (examRegistForm.getExplain()[i].length() > 1000) {
					result.addError(new FieldError(result.getObjectName(), "explain[" + i + "]",
							messageUtil.getMessage(Constants.VALID_KEY_MAXLENGTH,
									new String[] { "解説", "1000" })));
				}
			}
		}

	}

	/**
	 * Task.113 試験問題の登録
	 * 
	 * @param examRegistForm
	 * @return 完了メッセージ
	 */
	public String questionDetailRegist(ExamRegistForm examRegistForm) {

		Date now = new Date();
		MExam mExam = new MExam();
		mExam.setExamName(examRegistForm.getExamName());
		mExam.setLimitTime(examRegistForm.getLimitTime());
		mExam.setGenreId(examRegistForm.getGenreId());
		mExam.setAccountId(loginUserDto.getAccountId());
		mExam.setDeleteFlg(Constants.DB_FLG_FALSE);
		mExam.setFirstCreateUser(loginUserDto.getLmsUserId());
		mExam.setFirstCreateDate(now);
		mExam.setLastModifiedUser(loginUserDto.getLmsUserId());
		mExam.setLastModifiedDate(now);
		mExam.setHiddenFlg(Constants.DB_HIDDEN_FLG_FALSE);
		mExamMapper.insert(mExam);

		for (int i = 0; i < examRegistForm.getNumOfQuestion(); i++) {
			MQuestion mQuestion = new MQuestion();
			mQuestion.setQuestion(examRegistForm.getQuestion()[i]);
			mQuestion.setGrade(Short.parseShort("1"));
			mQuestion.setAnswerNum(examRegistForm.getAnswerNum()[i]);
			mQuestion.setChoice1(examRegistForm.getChoice1()[i]);
			mQuestion.setChoice2(examRegistForm.getChoice2()[i]);
			mQuestion.setChoice3(examRegistForm.getChoice3()[i]);
			mQuestion.setChoice4(examRegistForm.getChoice4()[i]);
			mQuestion.setExplain(examRegistForm.getExplain()[i]);
			mQuestion.setExamId(mExam.getExamId());
			mQuestion.setGenreDetailId(examRegistForm.getGenreDetailId()[i]);
			mQuestion.setAccountId(loginUserDto.getAccountId());
			mQuestion.setDeleteFlg(Constants.DB_FLG_FALSE);
			mQuestion.setFirstCreateUser(loginUserDto.getLmsUserId());
			mQuestion.setFirstCreateDate(now);
			mQuestion.setLastModifiedUser(loginUserDto.getLmsUserId());
			mQuestion.setLastModifiedDate(now);
			mQuestionMapper.insert(mQuestion);
		}

		return messageUtil.getMessage(Constants.PROP_KEY_REGIST_COMPLETE, new String[] { "試験問題" });
	}

	/**
	 * Task.113 試験問題の更新
	 * 
	 * @param examRegistForm
	 * @return 完了メッセージ
	 */
	public String questionDetailUpdate(ExamRegistForm examRegistForm) {

		Date now = new Date();
		MExam mExam = new MExam();
		mExam.setExamId(examRegistForm.getExamId());
		mExam.setExamName(examRegistForm.getExamName());
		mExam.setLimitTime(examRegistForm.getLimitTime());
		mExam.setGenreId(examRegistForm.getGenreId());
		mExam.setLastModifiedUser(loginUserDto.getLmsUserId());
		mExam.setLastModifiedDate(now);
		mExam.setHiddenFlg(Constants.DB_HIDDEN_FLG_FALSE);
		mExamMapper.update(mExam);

		for (int i = 0; i < examRegistForm.getNumOfQuestion(); i++) {
			MQuestion mQuestion = new MQuestion();
			mQuestion.setQuestionId(examRegistForm.getQuestionId()[i]);
			mQuestion.setQuestion(examRegistForm.getQuestion()[i]);
			mQuestion.setGrade(Short.parseShort("1"));
			mQuestion.setAnswerNum(examRegistForm.getAnswerNum()[i]);
			mQuestion.setChoice1(examRegistForm.getChoice1()[i]);
			mQuestion.setChoice2(examRegistForm.getChoice2()[i]);
			mQuestion.setChoice3(examRegistForm.getChoice3()[i]);
			mQuestion.setChoice4(examRegistForm.getChoice4()[i]);
			mQuestion.setExplain(examRegistForm.getExplain()[i]);
			mQuestion.setExamId(examRegistForm.getExamId());
			mQuestion.setGenreDetailId(examRegistForm.getGenreDetailId()[i]);
			mQuestion.setLastModifiedUser(loginUserDto.getLmsUserId());
			mQuestion.setLastModifiedDate(now);
			mQuestionMapper.update(mQuestion);
		}

		return messageUtil.getMessage(Constants.PROP_KEY_UPDATE_COMPLETE, new String[] { "試験問題" });
	}

}