package jp.co.sss.lms.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.sss.lms.dto.GenreDetailDto;
import jp.co.sss.lms.dto.GenreDto;
import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.entity.MGenre;
import jp.co.sss.lms.entity.MGenreDetail;
import jp.co.sss.lms.form.GenreDetailForm;
import jp.co.sss.lms.form.GenreForm;
import jp.co.sss.lms.mapper.MGenreDetailMapper;
import jp.co.sss.lms.mapper.MGenreMapper;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.MessageUtil;

/**
 * ジャンル情報サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class GenreService {

	@Autowired
	private MGenreMapper mGenreMapper;
	@Autowired
	private MGenreDetailMapper mGenreDetailMapper;
	@Autowired
	private LoginUserDto loginUserDto;
	@Autowired
	private MessageUtil messageUtil;

	/**
	 * Task.97 試験カテゴリーリスト取得
	 * 
	 * @param genreForm
	 * @return ジャンルDTOリスト
	 */
	public List<GenreDto> getGenreDtoList(GenreForm genreForm) {
		return mGenreMapper.getGenreDtoList(genreForm.getGenreName(), Constants.DB_FLG_FALSE);
	}

	/**
	 * Task.97 ジャンル削除
	 * 
	 * @param genreForm
	 * @return 完了メッセージ
	 */
	public String delete(GenreForm genreForm) {

		Date now = new Date();
		MGenre mGenre = new MGenre();
		String genreId = messageUtil.getMessage("genreId");

		mGenre.setGenreId(genreForm.getGenreId());
		mGenre.setDeleteFlg(Constants.DB_FLG_TRUE);
		mGenre.setLastModifiedUser(loginUserDto.getLmsUserId());
		mGenre.setLastModifiedDate(now);
		mGenreMapper.updateDeleteFlg(mGenre);

		MGenreDetail mGenreDetail = new MGenreDetail();
		mGenreDetail.setGenreId(genreForm.getGenreId());
		mGenreDetail.setDeleteFlg(Constants.DB_FLG_TRUE);
		mGenreDetail.setLastModifiedUser(loginUserDto.getLmsUserId());
		mGenreDetail.setLastModifiedDate(now);
		mGenreDetailMapper.updateDeleteFlgByGenreId(mGenreDetail);

		return messageUtil.getMessage(Constants.PROP_KEY_DELETE_COMPLETE, new String[] { genreId });
	}

	/**
	 * Task.97 ジャンル詳細削除
	 * 
	 * @param genreDetailForm
	 * @return 完了メッセージ
	 */
	public String detailDelete(GenreDetailForm genreDetailForm) {

		MGenreDetail mGenreDetail = new MGenreDetail();
		Date today = new Date();
		String genreDetailId = messageUtil.getMessage("genreDetailId");

		mGenreDetail.setGenreDetailId(genreDetailForm.getGenreDetailId());
		mGenreDetail.setDeleteFlg(Constants.DB_FLG_TRUE);
		mGenreDetail.setLastModifiedUser(loginUserDto.getLmsUserId());
		mGenreDetail.setLastModifiedDate(today);
		mGenreDetailMapper.updateDeleteFlgByGenreDetailId(mGenreDetail);

		return messageUtil.getMessage(Constants.PROP_KEY_DELETE_COMPLETE,
				new String[] { genreDetailId });
	}

	/**
	 * Task.98 ジャンル情報取得
	 * 
	 * @param genreForm
	 */
	public void getGenreForm(GenreForm genreForm) {
		if (genreForm.getGenreId() != null) {
			MGenre mGenre = mGenreMapper.findByGenreId(genreForm.getGenreId(),
					Constants.DB_FLG_FALSE);
			genreForm.setGenreName(mGenre.getGenreName());
		}
	}

	/**
	 * Task.98 ジャンル登録
	 * 
	 * @param genreForm
	 * @return 完了メッセージ
	 */
	public String regist(GenreForm genreForm) {

		Date now = new Date();
		MGenre mGenre = new MGenre();
		String genreId = messageUtil.getMessage("genreId");

		mGenre.setGenreName(genreForm.getGenreName());
		mGenre.setDeleteFlg(Constants.DB_FLG_FALSE);
		mGenre.setLastModifiedUser(loginUserDto.getLmsUserId());
		mGenre.setLastModifiedDate(now);
		if (genreForm.getGenreId() == null) {
			// 登録
			mGenre.setAccountId(loginUserDto.getAccountId());
			mGenre.setFirstCreateUser(loginUserDto.getLmsUserId());
			mGenre.setFirstCreateDate(now);
			mGenreMapper.insert(mGenre);
			return messageUtil.getMessage(Constants.PROP_KEY_REGIST_COMPLETE,
					new String[] { genreId });
		} else {
			// 更新
			mGenre.setGenreId(genreForm.getGenreId());
			mGenreMapper.update(mGenre);
			return messageUtil.getMessage(Constants.PROP_KEY_UPDATE_COMPLETE,
					new String[] { genreId });
		}

	}

	/**
	 * Task.99 ジャンル詳細取得
	 * 
	 * @param genreDetailForm
	 */
	public void getGenreDetailForm(GenreDetailForm genreDetailForm) {
		if (genreDetailForm.getGenreDetailId() != null) {
			MGenreDetail mGenreDetail = mGenreDetailMapper.findByGenreDetailId(
					genreDetailForm.getGenreDetailId(), Constants.DB_FLG_FALSE);
			genreDetailForm.setGenreDetailName(mGenreDetail.getGenreDetailName());
		}
	}

	/**
	 * Task.99 ジャンル詳細登録
	 * 
	 * @param genreDetailForm
	 * @return 完了メッセージ
	 */
	public String detailRegist(GenreDetailForm genreDetailForm) {

		MGenreDetail mGenreDetail = new MGenreDetail();
		Date now = new Date();
		String genreDetailId = messageUtil.getMessage("genreDetailId");

		mGenreDetail.setGenreDetailName(genreDetailForm.getGenreDetailName());
		mGenreDetail.setDeleteFlg(Constants.DB_FLG_FALSE);
		mGenreDetail.setLastModifiedUser(loginUserDto.getLmsUserId());
		mGenreDetail.setLastModifiedDate(now);
		if (genreDetailForm.getGenreDetailId() == null) {
			// 登録
			mGenreDetail.setGenreId(Integer.parseInt(genreDetailForm.getGenreId()));
			mGenreDetail.setAccountId(loginUserDto.getAccountId());
			mGenreDetail.setFirstCreateUser(loginUserDto.getLmsUserId());
			mGenreDetail.setFirstCreateDate(now);
			mGenreDetailMapper.insert(mGenreDetail);
			return messageUtil.getMessage(Constants.PROP_KEY_REGIST_COMPLETE,
					new String[] { genreDetailId });
		} else {
			// 更新
			mGenreDetail.setGenreDetailId(genreDetailForm.getGenreDetailId());
			mGenreDetailMapper.update(mGenreDetail);
			return messageUtil.getMessage(Constants.PROP_KEY_UPDATE_COMPLETE,
					new String[] { genreDetailId });
		}
	}

	/**
	 * Task.113 ジャンル詳細DTOリスト取得
	 * 
	 * @param genreId
	 * @return ジャンル詳細DTOリスト
	 */
	public List<GenreDetailDto> getGenreDetailDtoList(Integer genreId) {
		return mGenreDetailMapper.getGenreDetailDtoList(genreId, Constants.DB_FLG_FALSE);
	}

}
