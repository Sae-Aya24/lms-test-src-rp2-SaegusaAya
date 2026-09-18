package jp.co.sss.lms.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.dto.RepairDto;
import jp.co.sss.lms.entity.TRepairHistory;
import jp.co.sss.lms.form.RepairForm;
import jp.co.sss.lms.mapper.TRepairHistoryMapper;
import jp.co.sss.lms.util.Constants;

/**
 * 改修履歴情報サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class RepairService {

	@Autowired
	private TRepairHistoryMapper tRepairHistoryMapper;
	@Autowired
	private LoginUserDto loginUserDto;

	/**
	 * Task.87 改修履歴DTOリスト取得
	 * 
	 * @return 改修履歴DTOリスト
	 */
	public List<RepairDto> getRepaiirDtoList() {
		return tRepairHistoryMapper.getRepairDtoList(Constants.DB_FLG_FALSE);
	}

	/**
	 * Task.87 改修履歴の削除
	 * 
	 * @param historyId
	 */
	public void delete(RepairForm repairForm) {
		Date today = new Date();
		TRepairHistory tRepairHistory = new TRepairHistory();
		tRepairHistory.setHistoryId(repairForm.getHistoryId());
		tRepairHistory.setDeleteFlg(Constants.DB_FLG_TRUE);
		tRepairHistory.setLastModifiedUser(loginUserDto.getLmsUserId());
		tRepairHistory.setLastModifiedDate(today);
		tRepairHistoryMapper.updateDeleteFlg(tRepairHistory);
	}

	/**
	 * Task.88 改修履歴の取得
	 * 
	 * @param repairForm
	 */
	public void setRepairForm(RepairForm repairForm) {
		if (repairForm.getHistoryId() != null) {
			// 更新
			TRepairHistory tRepairHistory = tRepairHistoryMapper
					.findByHistoryId(repairForm.getHistoryId(), Constants.DB_FLG_FALSE);
			repairForm.setHistoryId(tRepairHistory.getHistoryId());
			repairForm.setContent(tRepairHistory.getContent());
		}
	}

	/**
	 * Task.88 改修履歴の登録または更新
	 * 
	 * @param repairForm
	 */
	public void regist(RepairForm repairForm) {
		Date now = new Date();
		TRepairHistory tRepairHistory = new TRepairHistory();
		tRepairHistory.setContent(repairForm.getContent());
		tRepairHistory.setLastModifiedUser(loginUserDto.getLmsUserId());
		tRepairHistory.setLastModifiedDate(now);
		if (repairForm.getHistoryId() == null) {
			// 登録
			tRepairHistory.setDeleteFlg(Constants.DB_FLG_FALSE);
			tRepairHistory.setFirstCreateUser(loginUserDto.getLmsUserId());
			tRepairHistory.setFirstCreateDate(now);
			tRepairHistoryMapper.insert(tRepairHistory);
		} else {
			// 更新
			tRepairHistory.setHistoryId(repairForm.getHistoryId());
			tRepairHistoryMapper.update(tRepairHistory);
		}
	}

}
