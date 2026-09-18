package jp.co.sss.lms.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.dto.PlaceDto;
import jp.co.sss.lms.entity.MPlace;
import jp.co.sss.lms.form.PlaceForm;
import jp.co.sss.lms.mapper.MPlaceMapper;
import jp.co.sss.lms.mapper.TUserPlaceMapper;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.MessageUtil;
import jp.co.sss.lms.util.PlaceUtil;

/**
 * 会場情報サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class PlaceService {

	@Autowired
	private MPlaceMapper mPlaceMapper;
	@Autowired
	private LoginUserDto loginUserDto;
	@Autowired
	private TUserPlaceMapper tUserPlaceMapper;
	@Autowired
	private MessageUtil messageUtil;

	/**
	 * Task.43 会場DTO取得
	 * 
	 * @return 会場DTO
	 */
	// 講師用画面で汎用的に利用
	public PlaceDto getPlaceDto() {
		MPlace mPlace = mPlaceMapper.findByPlaceId(loginUserDto.getPlaceId(),
				Constants.DB_HIDDEN_FLG_FALSE, Constants.DB_FLG_FALSE);
		PlaceDto placeDto = new PlaceDto();
		if (mPlace != null) {
			BeanUtils.copyProperties(mPlace, placeDto);
		}
		return placeDto;
	}

	/**
	 * Task.79 会場DTOリストの取得
	 * 
	 * @param placeName
	 * @return 会場DTOリスト
	 */
	// 管理者画面で汎用的に利用
	public List<PlaceDto> getPlaceDtoList(String placeName) {
		List<PlaceDto> placeDtoList = mPlaceMapper.getPlaceDtoList(placeName,
				Constants.DB_HIDDEN_FLG_FALSE, Constants.DB_FLG_FALSE);
		return placeDtoList;
	}

	/**
	 * Task.95 会場DTOリストの設定
	 * 
	 * @param placeName
	 * @param model
	 */
	public void getPlaceDtoList(PlaceForm placeForm, Model model) {
		// データリスト用
		List<PlaceDto> placeDtoList1 = getPlaceDtoList(null);
		model.addAttribute("placeDtoList1", placeDtoList1);
		// 検索結果
		List<PlaceDto> placeDtoList2 = getPlaceDtoList(placeForm.getPlaceName());
		model.addAttribute("placeDtoList2", placeDtoList2);
	}

	/**
	 * Task.95 会場削除入力チェック
	 * 
	 * @param placeName
	 * @param model
	 * @return チェック結果
	 */
	public Boolean checkDeletePlace(PlaceForm placeForm, Model model) {
		// 存在確認チェック
		MPlace mPlace = mPlaceMapper.findByPlaceId(placeForm.getPlaceId(),
				Constants.DB_HIDDEN_FLG_FALSE, Constants.DB_FLG_FALSE);
		if (mPlace == null) {
			model.addAttribute("error", "* " + messageUtil
					.getMessage(Constants.VALID_KEY_ALREADYDELETE, new String[] { "会場情報" }));
			return true;
		}
		// ユーザーの紐づき確認チェック
		Integer count = tUserPlaceMapper.getCountByPlaceId(placeForm.getPlaceId(),
				Constants.DB_FLG_FALSE);
		if (count > 0) {
			model.addAttribute("placeId_" + placeForm.getPlaceId(), "* " + messageUtil
					.getMessage(Constants.VALID_KEY_DELETE_RELATION, new String[] { "ユーザー" }));
			return true;
		}
		return false;
	}

	/**
	 * Task.95 会場削除
	 * 
	 * @param placeForm
	 * @return 完了メッセージ
	 */
	public String delete(PlaceForm placeForm) {
		Date today = new Date();
		MPlace mPlace = new MPlace();
		String place = messageUtil.getMessage("placeId");
		mPlace.setPlaceId(placeForm.getPlaceId());
		mPlace.setDeleteFlg(Constants.DB_FLG_TRUE);
		mPlace.setLastModifiedUser(loginUserDto.getLmsUserId());
		mPlace.setLastModifiedDate(today);
		mPlaceMapper.updateDeleteFlg(mPlace);
		return messageUtil.getMessage(Constants.PROP_KEY_DELETE_COMPLETE, new String[] { place });
	}

	/**
	 * Task.96 会場登録フォームの設定
	 * 
	 * @param placeForm
	 */
	public void setPlaceForm(PlaceForm placeForm) {
		if (placeForm.getPlaceId() != null) {
			MPlace mPlace = mPlaceMapper.findByPlaceId(placeForm.getPlaceId(),
					Constants.DB_HIDDEN_FLG_FALSE, Constants.DB_FLG_FALSE);
			placeForm.setPlaceName(mPlace.getPlaceName());
			placeForm.setPlaceDescription(mPlace.getPlaceDescription());
			placeForm.setSeatingCapacity(mPlace.getSeatingCapacity());
			placeForm.setPlaceNote(mPlace.getPlaceNote());
			placeForm.setSupportAvailable(mPlace.getSupportAvailable());
		}
	}

	/**
	 * Task.96 会場登録入力チェック
	 * 
	 * @param placeForm
	 * @param result
	 */
	public void checkRegistPlace(PlaceForm placeForm, BindingResult result) {
		// フォーム入力チェックでエラーがある場合は実行しない
		if (result.hasErrors()) {
			return;
		}
		// 備考チェック
		String[] placeNoteArray = placeForm.getPlaceNote().split(PlaceUtil.REGEX_CHAR, -1);
		if (placeNoteArray.length != 3) {
			String placeNote = messageUtil.getMessage("placeNote");
			result.addError(new FieldError(result.getObjectName(), "placeNote", "* " + messageUtil
					.getMessage(Constants.VALID_KEY_FORMATERROR, new String[] { placeNote })));
		}
	}

	/**
	 * Task.96 会場登録
	 * 
	 * @param placeForm
	 */
	public String regist(PlaceForm placeForm) {
		Date now = new Date();
		MPlace mPlace = new MPlace();
		String place = messageUtil.getMessage("placeId");
		mPlace.setPlaceName(placeForm.getPlaceName());
		mPlace.setPlaceDescription(placeForm.getPlaceDescription());
		mPlace.setPlaceNote(placeForm.getPlaceNote());
		mPlace.setSupportAvailable(placeForm.getSupportAvailable());
		mPlace.setAccountId(loginUserDto.getAccountId());
		mPlace.setDeleteFlg(Constants.DB_FLG_FALSE);
		mPlace.setLastModifiedUser(loginUserDto.getLmsUserId());
		mPlace.setLastModifiedDate(now);
		mPlace.setSeatingCapacity(placeForm.getSeatingCapacity());
		mPlace.setHiddenFlg(Constants.DB_HIDDEN_FLG_FALSE);
		if (placeForm.getPlaceId() == null) {
			// 登録
			mPlace.setFirstCreateUser(loginUserDto.getLmsUserId());
			mPlace.setFirstCreateDate(now);
			mPlaceMapper.insert(mPlace);
			return messageUtil.getMessage(Constants.PROP_KEY_REGIST_COMPLETE,
					new String[] { place });
		} else {
			// 更新
			mPlace.setPlaceId(placeForm.getPlaceId());
			mPlaceMapper.update(mPlace);
			return messageUtil.getMessage(Constants.PROP_KEY_UPDATE_COMPLETE,
					new String[] { place });
		}
	}

}
