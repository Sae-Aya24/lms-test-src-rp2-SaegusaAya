package jp.co.sss.lms.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.sss.lms.dto.EvReportDto;
import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.entity.MEvReport;
import jp.co.sss.lms.entity.MEvReportDetail;
import jp.co.sss.lms.form.EvReportForm;
import jp.co.sss.lms.mapper.MEvReportDetailMapper;
import jp.co.sss.lms.mapper.MEvReportMapper;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.MessageUtil;

/**
 * 評価レポート情報サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class EvReportService {

	@Autowired
	private MEvReportMapper mEvReportMapper;
	@Autowired
	private MEvReportDetailMapper mEvReportDetailMapper;
	@Autowired
	private LoginUserDto loginUserDto;
	@Autowired
	private MessageUtil messageUtil;

	/**
	 * Task.103 評価レポートDTOリストの取得
	 * 
	 * @param evReportForm
	 * @return 評価レポートDTOリスト
	 */
	public List<EvReportDto> getEvReportDtoList(String reportName) {
		return mEvReportMapper.getEvReportDtoList(reportName, Constants.DB_FLG_FALSE);
	}

	/**
	 * Task.103 評価レポートのコピー
	 * 
	 * @param evReportForm
	 * @return 完了メッセージ
	 */
	public String copy(EvReportForm evReportForm) {
		Date now = new Date();
		MEvReport mEvReport = mEvReportMapper.findByEvReportId(evReportForm.getEvReportId(),
				Constants.DB_FLG_FALSE);
		mEvReport.setFirstCreateUser(loginUserDto.getLmsUserId());
		mEvReport.setFirstCreateDate(now);
		mEvReport.setLastModifiedUser(loginUserDto.getLmsUserId());
		mEvReport.setLastModifiedDate(now);
		mEvReportMapper.insert(mEvReport);

		List<MEvReportDetail> mEvReportDetailList = mEvReportDetailMapper
				.findByEvReportId(evReportForm.getEvReportId(), Constants.DB_FLG_FALSE);
		for (MEvReportDetail mEvReportDetail : mEvReportDetailList) {
			mEvReportDetail.setEvReportId(mEvReport.getEvReportId());
			mEvReportDetail.setFirstCreateUser(loginUserDto.getLmsUserId());
			mEvReportDetail.setFirstCreateDate(now);
			mEvReportDetail.setLastModifiedUser(loginUserDto.getLmsUserId());
			mEvReportDetail.setLastModifiedDate(now);
			mEvReportDetailMapper.insert(mEvReportDetail);
		}
		return messageUtil.getMessage(Constants.PROP_KEY_ANYITEMS_COMPLETE,
				new String[] { "評価レポート", "コピー" });
	}

	/**
	 * Task.103 評価レポートの削除
	 * 
	 * @param evReportForm
	 * @return 完了メッセージ
	 */
	public String delete(EvReportForm evReportForm) {
		String evReport = messageUtil.getMessage("evReport");
		Date now = new Date();

		MEvReport mEvReport = new MEvReport();
		mEvReport.setEvReportId(evReportForm.getEvReportId());
		mEvReport.setDeleteFlg(Constants.DB_FLG_TRUE);
		mEvReport.setLastModifiedUser(loginUserDto.getLmsUserId());
		mEvReport.setLastModifiedDate(now);
		mEvReportMapper.updateDeleteFlg(mEvReport);

		MEvReportDetail mEvReportDetail = new MEvReportDetail();
		mEvReportDetail.setEvReportId(evReportForm.getEvReportId());
		mEvReportDetail.setDeleteFlg(Constants.DB_FLG_TRUE);
		mEvReportDetail.setLastModifiedUser(loginUserDto.getLmsUserId());
		mEvReportDetail.setLastModifiedDate(now);
		mEvReportDetailMapper.updateDeleteFlg(mEvReportDetail);

		return messageUtil.getMessage(Constants.PROP_KEY_DELETE_COMPLETE,
				new String[] { evReport });
	}

}
