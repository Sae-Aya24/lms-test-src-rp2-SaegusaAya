package jp.co.sss.lms.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.sss.lms.dto.InfoDto;
import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.entity.TInfo;
import jp.co.sss.lms.form.InfoForm;
import jp.co.sss.lms.mapper.TInfoMapper;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.MessageUtil;

/**
 * お知らせ情報サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class InfoService {

	@Autowired
	private TInfoMapper tInfoMapper;
	@Autowired
	private MessageUtil messageUtil;
	@Autowired
	private LoginUserDto loginUserDto;

	/**
	 * お知らせ情報取得
	 * 
	 * @return お知らせ情報
	 */
	public InfoDto getInfo() {

		// Task.85 引数にdeleteFlg追加
		TInfo tInfo = tInfoMapper.findBySingleResult(Constants.DB_FLG_FALSE);

		InfoDto infoDto = new InfoDto();
		if (tInfo != null) {
			// Task.02 ②新規お知らせの強調
			infoDto = setInfoDto(tInfo);
		}

		return infoDto;

	}

	/**
	 * Task.83 お知らせ登録
	 * 
	 * @param infoForm
	 * @return 完了メッセージ
	 */
	public String insert(InfoForm infoForm) {

		Date date = new Date();
		TInfo tInfo = new TInfo();
		String info = messageUtil.getMessage("info");

		tInfo.setContent(infoForm.getContent());
		tInfo.setDeleteFlg(Constants.DB_FLG_FALSE);
		tInfo.setFirstCreateUser(loginUserDto.getLmsUserId());
		tInfo.setFirstCreateDate(date);
		tInfo.setLastModifiedUser(loginUserDto.getLmsUserId());
		tInfo.setLastModifiedDate(date);
		tInfoMapper.insert(tInfo);

		return messageUtil.getMessage(Constants.PROP_KEY_REGIST_COMPLETE, new String[] { info });

	}

	/**
	 * Task.84 お知らせDTOリスト取得
	 * 
	 * @return お知らせDTOリスト
	 */
	public List<InfoDto> getInfoDtoList() {

		List<InfoDto> infoDtoList = tInfoMapper.findAll(Constants.DB_FLG_FALSE);
		return infoDtoList;

	}

	/**
	 * Task.85 お知らせ変更
	 * 
	 * @param infoDto
	 * @return 完了メッセージ
	 */
	public String update(InfoForm infoForm) {

		Date date = new Date();
		TInfo tInfo = new TInfo();
		String info = messageUtil.getMessage("info");

		tInfo.setInfoId(infoForm.getInfoId());
		tInfo.setContent(infoForm.getContent());
		tInfo.setLastModifiedUser(loginUserDto.getLmsUserId());
		tInfo.setLastModifiedDate(date);
		tInfoMapper.update(tInfo);

		return messageUtil.getMessage(Constants.PROP_KEY_UPDATE_COMPLETE, new String[] { info });

	}

	/**
	 * Task.85 お知らせ削除
	 * 
	 * @param infoForm
	 * @return 完了メッセージ
	 */
	public String delete(InfoForm infoForm) {

		Date date = new Date();
		TInfo tInfo = new TInfo();
		String info = messageUtil.getMessage("info");

		tInfo.setInfoId(infoForm.getInfoId());
		tInfo.setDeleteFlg(Constants.DB_FLG_TRUE);
		tInfo.setLastModifiedUser(loginUserDto.getLmsUserId());
		tInfo.setLastModifiedDate(date);
		tInfoMapper.updateDeleteFlg(tInfo);

		return messageUtil.getMessage(Constants.PROP_KEY_DELETE_COMPLETE, new String[] { info });

	}

	/**
	 * Task.02 お知らせ情報DTO設定
	 * 
	 * @return お知らせ情報DTO
	 */
	private InfoDto setInfoDto(TInfo tInfo) {
		InfoDto infoDto = new InfoDto();
		infoDto.setInfoId(tInfo.getInfoId());
		infoDto.setContent(tInfo.getContent());
		infoDto.setLastModifiedDate(tInfo.getLastModifiedDate());
		// Newフラグ判定
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(infoDto.getLastModifiedDate());
		calendar.add(Calendar.DAY_OF_MONTH, 7);
		Calendar now = Calendar.getInstance();
		if (calendar.compareTo(now) >= 0) {
			infoDto.setNewFlg(true);
		} else {
			infoDto.setNewFlg(false);
		}
		return infoDto;
	}

}
