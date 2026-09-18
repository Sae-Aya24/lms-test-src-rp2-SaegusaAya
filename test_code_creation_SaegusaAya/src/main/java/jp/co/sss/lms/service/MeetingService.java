package jp.co.sss.lms.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.CellReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.dto.MeetingDetailDto;
import jp.co.sss.lms.dto.MeetingDownloadDto;
import jp.co.sss.lms.dto.MeetingFileDto;
import jp.co.sss.lms.dto.UserDetailDto;
import jp.co.sss.lms.dto.WorkbookDto;
import jp.co.sss.lms.entity.MMeetingFile;
import jp.co.sss.lms.entity.TCourseMeeting;
import jp.co.sss.lms.entity.TMeeting;
import jp.co.sss.lms.entity.TMeetingDetail;
import jp.co.sss.lms.form.MeetingFileListForm;
import jp.co.sss.lms.form.MeetingFileRegistForm;
import jp.co.sss.lms.form.MeetingRegistForm;
import jp.co.sss.lms.mapper.MLmsUserMapper;
import jp.co.sss.lms.mapper.MMeetingFileMapper;
import jp.co.sss.lms.mapper.TCourseMeetingMapper;
import jp.co.sss.lms.mapper.TMeetingDetailMapper;
import jp.co.sss.lms.mapper.TMeetingMapper;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.DateUtil;
import jp.co.sss.lms.util.ExcelUtil;
import jp.co.sss.lms.util.MessageUtil;

/**
 * 面談情報サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class MeetingService {

	@Autowired
	private TMeetingMapper tMeetingMapper;
	@Autowired
	private TMeetingDetailMapper tMeetingDetailMapper;
	@Autowired
	private MMeetingFileMapper mMeetingFileMapper;
	@Autowired
	private TCourseMeetingMapper tCourseMeetingMapper;
	@Autowired
	private MLmsUserMapper mLmsUserMapper;
	@Autowired
	private LoginUserDto loginUserDto;
	@Autowired
	private MessageUtil messageUtil;
	@Autowired
	private DateUtil dateUtil;

	/**
	 * 面談記録ファイルのダウンロード
	 * 
	 * @param meetingId
	 * @param response
	 * @throws IOException
	 */
	public void downloadMeetingFile(Integer meetingId, HttpServletResponse response)
			throws IOException {

		// 面談ダウンロードDTOの取得
		MeetingDownloadDto meetingDownloadDto = tMeetingMapper.getMeetingDownloadDto(meetingId,
				Constants.DB_FLG_FALSE);

		// テンプレートファイルのパスを設定
		String commonFileDir = messageUtil.getMessage("setting.file.common.dir");
		String excelFilePath = commonFileDir + "/" + meetingDownloadDto.getFileName();
		ExcelUtil excelUtil = new ExcelUtil(excelFilePath);

		// 企業名
		excelUtil.setVal(meetingDownloadDto.getSheetName(), meetingDownloadDto.getRowCompany(),
				meetingDownloadDto.getClmCompany(), meetingDownloadDto.getCompanyName());
		// ユーザー名
		excelUtil.setVal(meetingDownloadDto.getSheetName(), meetingDownloadDto.getRowUser(),
				meetingDownloadDto.getClmUser(), meetingDownloadDto.getInterviewee());
		// 面談実施日
		excelUtil.setVal(meetingDownloadDto.getSheetName(), meetingDownloadDto.getRowDate(),
				meetingDownloadDto.getClmDate(), meetingDownloadDto.getInterviewDate());
		// 面談実施者
		excelUtil.setVal(meetingDownloadDto.getSheetName(), meetingDownloadDto.getRowDate(),
				meetingDownloadDto.getClmUser(), meetingDownloadDto.getInterviewer());
		// 面談内容
		int rowTechnical = meetingDownloadDto.getRowMeeting() - 1;
		int rowHuman = rowTechnical + 5;
		for (MeetingDetailDto meetingDetailDto : meetingDownloadDto.getMeetingDetailDtoList()) {
			int row = meetingDetailDto.getQuestionType() == 0 ? rowTechnical : rowHuman;
			int col = meetingDownloadDto.getClmMeeting();
			// 質問
			excelUtil.setVal(meetingDownloadDto.getSheetName(), ++row, col,
					meetingDetailDto.getQuestion());
			// 回答
			excelUtil.setVal(meetingDownloadDto.getSheetName(), row, ++col,
					meetingDetailDto.getAnswer());
			// フォロー
			excelUtil.setVal(meetingDownloadDto.getSheetName(), row, ++col,
					meetingDetailDto.getFollow());
			if (meetingDetailDto.getQuestionType() == 0) {
				rowTechnical = row;
			} else {
				rowHuman = row;
			}
		}

		WorkbookDto workbookDto = new WorkbookDto();
		workbookDto.setWb(excelUtil.getWb());
		workbookDto.getWb().setForceFormulaRecalculation(true);

		String[] fileName = meetingDownloadDto.getFileName().split("\\.");
		String bookName = "";
		for (int i = 0; i < fileName.length - 1; i++) {
			if (i > 0) {
				bookName += ".";
			}
			bookName += fileName[i];
		}

		// 受講生名に全角スペースが使用されている場合は半角スペースに置き換える
		String userName = meetingDownloadDto.getInterviewee().replaceAll("　", " ");
		String companyName = meetingDownloadDto.getCompanyName().replaceAll("　", " ");

		workbookDto.setWbName(bookName + "_"
				+ dateUtil.toString(meetingDownloadDto.getInterviewDate(), "YYYYMMdd") + "_"
				+ companyName + "_" + userName + "." + fileName[fileName.length - 1]);

		ExcelUtil.downloadBook(workbookDto, response);
	}

	/**
	 * Task.46 面談記録の削除
	 * 
	 * @param meetingId
	 */
	public void deleteMeeting(Integer meetingId) {
		Date today = new Date();
		TMeeting tMeeting = new TMeeting();
		tMeeting.setMeetingId(meetingId);
		tMeeting.setLastModifiedDate(today);
		tMeeting.setLastModifiedUser(loginUserDto.getLmsUserId());
		tMeetingMapper.updateDeleteFlg(tMeeting);

		TMeetingDetail tMeetingDetail = new TMeetingDetail();
		tMeetingDetail.setMeetingId(meetingId);
		tMeetingDetail.setLastModifiedDate(today);
		tMeetingDetail.setLastModifiedUser(loginUserDto.getLmsUserId());
		tMeetingDetailMapper.updateDeleteFlg(tMeetingDetail);
	}

	/**
	 * Task.47 面談記録登録フォームの設定
	 * 
	 * @param meetingRegistForm
	 */
	public void setMeetingRegistForm(MeetingRegistForm meetingRegistForm) {
		UserDetailDto userDetailDto = mLmsUserMapper.getUserDetail(meetingRegistForm.getLmsUserId(),
				Constants.DB_FLG_FALSE);
		meetingRegistForm.setUserDetailDto(userDetailDto);

		List<MeetingDetailDto> meetingDetailDtoList = new ArrayList<>();
		if (meetingRegistForm.getMeetingId() == null) {
			for (int i = 0; i < 10; i++) {
				meetingDetailDtoList.add(new MeetingDetailDto());
			}
		} else {
			meetingDetailDtoList = tMeetingDetailMapper.getMeetingDetailDtoList(
					meetingRegistForm.getMeetingId(), Constants.DB_FLG_FALSE);
		}
		meetingRegistForm.setMeetingDetailDtoList(meetingDetailDtoList);
	}

	/**
	 * Task.47 面談記録登録フォームの入力チェック
	 * 
	 * @param meetingRegistForm
	 * @param result
	 */
	public void meetingRegistInputCheck(MeetingRegistForm meetingRegistForm, BindingResult result) {
		List<MeetingDetailDto> meetingDetailDtoList = meetingRegistForm.getMeetingDetailDtoList();
		for (int i = 0; i < meetingDetailDtoList.size(); i++) {
			MeetingDetailDto meetingDetailDto = meetingDetailDtoList.get(i);
			if (!StringUtils.isBlank(meetingDetailDto.getQuestion())
					&& meetingDetailDto.getQuestion().length() > 10000) {
				result.addError(new FieldError(result.getObjectName(),
						"meetingDetailDtoList[" + i + "].question", messageUtil.getMessage(
								Constants.VALID_KEY_MAXLENGTH, new String[] { "質問内容", "10000" })));
			}
			if (!StringUtils.isBlank(meetingDetailDto.getQuestion())
					&& meetingDetailDto.getAnswer().length() > 10000) {
				result.addError(new FieldError(result.getObjectName(),
						"meetingDetailDtoList[" + i + "].answer", messageUtil.getMessage(
								Constants.VALID_KEY_MAXLENGTH, new String[] { "回答内容", "10000" })));
			}
			if (!StringUtils.isBlank(meetingDetailDto.getQuestion())
					&& meetingDetailDto.getFollow().length() > 10000) {
				result.addError(new FieldError(result.getObjectName(),
						"meetingDetailDtoList[" + i + "].follow",
						messageUtil.getMessage(Constants.VALID_KEY_MAXLENGTH,
								new String[] { "フォロー内容", "10000" })));
			}
		}
	}

	/**
	 * Task.47 面談記録の登録
	 * 
	 * @param meetingRegistForm
	 */
	public void registMeeting(MeetingRegistForm meetingRegistForm) {

		Date now = new Date();
		TMeeting tMeeting = new TMeeting();

		if (meetingRegistForm.getMeetingId() == null) {
			tMeeting.setLmsUserId(meetingRegistForm.getLmsUserId());
			tMeeting.setAccountId(meetingRegistForm.getAccountId());
			tMeeting.setDeleteFlg(Constants.DB_FLG_FALSE);
			tMeeting.setFirstCreateUser(loginUserDto.getLmsUserId());
			tMeeting.setFirstCreateDate(now);
			tMeeting.setLastModifiedUser(loginUserDto.getLmsUserId());
			tMeeting.setLastModifiedDate(now);
			tMeetingMapper.insert(tMeeting);
		} else {
			tMeetingDetailMapper.delete(meetingRegistForm.getMeetingId());
		}

		Integer meetingId = meetingRegistForm.getMeetingId() == null ? tMeeting.getMeetingId()
				: meetingRegistForm.getMeetingId();
		for (MeetingDetailDto meetingDetailDto : meetingRegistForm.getMeetingDetailDtoList()) {
			TMeetingDetail tMeetingDetail = new TMeetingDetail();
			tMeetingDetail.setMeetingId(meetingId);
			tMeetingDetail.setQuestion(meetingDetailDto.getQuestion());
			tMeetingDetail.setAnswer(meetingDetailDto.getAnswer());
			tMeetingDetail.setFollow(meetingDetailDto.getFollow());
			tMeetingDetail.setQuestionType(meetingDetailDto.getQuestionType());
			tMeetingDetail.setAccountId(meetingRegistForm.getAccountId());
			tMeetingDetail.setDeleteFlg(Constants.DB_FLG_FALSE);
			tMeetingDetail.setFirstCreateUser(loginUserDto.getLmsUserId());
			tMeetingDetail.setFirstCreateDate(now);
			tMeetingDetail.setLastModifiedUser(loginUserDto.getLmsUserId());
			tMeetingDetail.setLastModifiedDate(now);
			tMeetingDetailMapper.insert(tMeetingDetail);
		}
	}

	/**
	 * Task.106 面談テンプレートファイルDTOリストの取得
	 * 
	 * @param paramMeetingFileId
	 * @param fileName
	 * @return 面談テンプレートファイルDTOリスト
	 */
	public List<MeetingFileDto> getMeetingFileDtoList(Integer meetingFileId, String fileName) {
		return mMeetingFileMapper.getMeetingFilleDtoList(meetingFileId, fileName,
				Constants.DB_FLG_FALSE);
	}

	/**
	 * Task.106 面談ファイル削除前の入力チェック
	 * 
	 * @param meetingFileListForm
	 * @param result
	 */
	public void fileDeleteInputCheck(MeetingFileListForm meetingFileListForm,
			BindingResult result) {
		// 削除対象の存在チェック
		List<MeetingFileDto> meetingFileList = mMeetingFileMapper.getMeetingFilleDtoList(
				meetingFileListForm.getMeetingFileId(), null, Constants.DB_FLG_FALSE);
		if (meetingFileList.size() == 0) {
			result.addError(new FieldError(result.getObjectName(), "meetingFileId",
					"* " + messageUtil.getMessage(Constants.VALID_KEY_ALREADYDELETE,
							new String[] { "面談ファイル" })));
			return;
		}
		// 削除対象とコースの紐付きチェック
		List<TCourseMeeting> tCourseMeetingList = tCourseMeetingMapper.findByCourseMeetingId(
				meetingFileListForm.getMeetingFileId(), Constants.DB_FLG_FALSE);
		if (tCourseMeetingList.size() >= 1) {
			result.addError(new FieldError(result.getObjectName(),
					"meetingFileDtoList[" + meetingFileListForm.getSelectedIndex()
							+ "].meetingFileId",
					"* " + messageUtil.getMessage(Constants.VALID_KEY_DELETE_RELATION,
							new String[] { "コース" })));
			return;
		}
	}

	/**
	 * Task.106 面談テンプレートファイルの削除
	 * 
	 * @param meetingFileId
	 * @return 完了メッセージ
	 */
	public String delete(Integer meetingFileId) {
		Date today = new Date();
		MMeetingFile mMeetinfFile = new MMeetingFile();
		mMeetinfFile.setMeetingFileId(meetingFileId);
		mMeetinfFile.setDeleteFlg(Constants.DB_FLG_TRUE);
		mMeetinfFile.setLastModifiedUser(loginUserDto.getLmsUserId());
		mMeetinfFile.setLastModifiedDate(today);
		mMeetingFileMapper.updateDeleteFlg(mMeetinfFile);
		return messageUtil.getMessage(Constants.PROP_KEY_DELETE_COMPLETE,
				new String[] { "面談テンプレートファイル" });
	}

	/**
	 * Task.107 面談テンプレートファイル登録フォームの設定
	 * 
	 * @param meetingFileRegistForm
	 */
	public void setMeetingFileRegistForm(MeetingFileRegistForm meetingFileRegistForm) {
		if (meetingFileRegistForm.getMeetingFileId() != null) {
			MMeetingFile mMeetingFile = mMeetingFileMapper.findByMeetingFileId(
					meetingFileRegistForm.getMeetingFileId(), Constants.DB_FLG_FALSE);
			BeanUtils.copyProperties(mMeetingFile, meetingFileRegistForm);
			meetingFileRegistForm.setClmCompany(
					CellReference.convertNumToColString(mMeetingFile.getClmCompany() - 1));
			meetingFileRegistForm
					.setClmUser(CellReference.convertNumToColString(mMeetingFile.getClmUser() - 1));
			meetingFileRegistForm
					.setClmDate(CellReference.convertNumToColString(mMeetingFile.getClmDate() - 1));
			meetingFileRegistForm.setClmMeeting(
					CellReference.convertNumToColString(mMeetingFile.getClmMeeting() - 1));
		}
	}

	/**
	 * Task.107 面談テンプレートファイル登録フォームの入力チェック
	 * 
	 * @param meetingFileForm
	 * @param result
	 */
	public void fileRegistInputCheck(MeetingFileRegistForm meetingFileForm, BindingResult result) {

		// FORM入力チェックでエラーの場合はチェックを行わない
		if (result.hasErrors()) {
			return;
		}

		// 列番号の最大値チェック
		Integer clmCompanyIndex = CellReference
				.convertColStringToIndex(meetingFileForm.getClmCompany());
		if (clmCompanyIndex < 0 || clmCompanyIndex > ExcelUtil.MAX_COL_NUM) {
			result.addError(new FieldError(result.getObjectName(), "clmCompany",
					"* " + messageUtil.getMessage(Constants.VALID_KEY_MAXVAL,
							new String[] { "企業名出力列番号", ExcelUtil.MAX_COL_STR })));
		}
		Integer clmUserIndex = CellReference.convertColStringToIndex(meetingFileForm.getClmUser());
		if (clmUserIndex < 0 || clmUserIndex > ExcelUtil.MAX_COL_NUM) {
			result.addError(new FieldError(result.getObjectName(), "clmUser",
					"* " + messageUtil.getMessage(Constants.VALID_KEY_MAXVAL,
							new String[] { "ユーザー名出力列番号", ExcelUtil.MAX_COL_STR })));
		}
		Integer clmDateIndex = CellReference.convertColStringToIndex(meetingFileForm.getClmDate());
		if (clmDateIndex < 0 || clmDateIndex > ExcelUtil.MAX_COL_NUM) {
			result.addError(new FieldError(result.getObjectName(), "clmDate",
					"* " + messageUtil.getMessage(Constants.VALID_KEY_MAXVAL,
							new String[] { "日付出力列番号", ExcelUtil.MAX_COL_STR })));
		}
		Integer clmMeetingIndex = CellReference
				.convertColStringToIndex(meetingFileForm.getClmMeeting());
		if (clmMeetingIndex < 0 || clmMeetingIndex > ExcelUtil.MAX_COL_NUM) {
			result.addError(new FieldError(result.getObjectName(), "clmMeeting",
					"* " + messageUtil.getMessage(Constants.VALID_KEY_MAXVAL,
							new String[] { "面談内容出力開始列番号", ExcelUtil.MAX_COL_STR })));
		}
	}

	/**
	 * Task.107 面談テンプレートファイルの登録または変更
	 * 
	 * @param meetingFileForm
	 * @return 登録完了メッセージ
	 */
	public String fileRegistComplete(MeetingFileRegistForm meetingFileForm) {
		MMeetingFile mMeetingFile = new MMeetingFile();
		mMeetingFile.setFileName(meetingFileForm.getFileName());
		mMeetingFile.setSheetName(meetingFileForm.getSheetName());
		mMeetingFile.setClmCompany(
				CellReference.convertColStringToIndex(meetingFileForm.getClmCompany()) + 1);
		mMeetingFile.setRowCompany(meetingFileForm.getRowCompany());
		mMeetingFile.setClmUser(
				CellReference.convertColStringToIndex(meetingFileForm.getClmUser()) + 1);
		mMeetingFile.setRowUser(meetingFileForm.getRowUser());
		mMeetingFile.setClmDate(
				CellReference.convertColStringToIndex(meetingFileForm.getClmDate()) + 1);
		mMeetingFile.setRowDate(meetingFileForm.getRowDate());
		mMeetingFile.setClmMeeting(
				CellReference.convertColStringToIndex(meetingFileForm.getClmMeeting()) + 1);
		mMeetingFile.setRowMeeting(meetingFileForm.getRowMeeting());
		Date now = new Date();
		mMeetingFile.setLastModifiedUser(loginUserDto.getLmsUserId());
		mMeetingFile.setLastModifiedDate(now);

		if (meetingFileForm.getMeetingFileId() == null) {
			// 登録
			mMeetingFile.setAccountId(loginUserDto.getAccountId());
			mMeetingFile.setDeleteFlg(Constants.DB_FLG_FALSE);
			mMeetingFile.setFirstCreateUser(loginUserDto.getLmsUserId());
			mMeetingFile.setFirstCreateDate(now);
			mMeetingFileMapper.insert(mMeetingFile);
			return messageUtil.getMessage(Constants.PROP_KEY_REGIST_COMPLETE,
					new String[] { "面談テンプレートファイル" });
		} else {
			// 更新
			mMeetingFile.setMeetingFileId(meetingFileForm.getMeetingFileId());
			mMeetingFileMapper.update(mMeetingFile);
			return messageUtil.getMessage(Constants.PROP_KEY_UPDATE_COMPLETE,
					new String[] { "面談テンプレートファイル" });
		}

	}

}
