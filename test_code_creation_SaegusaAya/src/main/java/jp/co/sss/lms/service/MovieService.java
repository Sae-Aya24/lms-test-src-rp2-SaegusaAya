package jp.co.sss.lms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.dto.MovieCategoryDto;
import jp.co.sss.lms.dto.MovieDto;
import jp.co.sss.lms.entity.MMovie;
import jp.co.sss.lms.entity.MMovieCategory;
import jp.co.sss.lms.form.MovieCategoryForm;
import jp.co.sss.lms.form.MovieForm;
import jp.co.sss.lms.mapper.MMovieCategoryMapper;
import jp.co.sss.lms.mapper.MMovieMapper;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.MessageUtil;

/**
 * Task.30 動画情報サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class MovieService {

	@Autowired
	private MMovieCategoryMapper mMovieCategoryMapper;
	@Autowired
	private MMovieMapper mMovieMapper;
	@Autowired
	private MessageUtil messageUtil;
	@Autowired
	private LoginUserDto loginUserDto;

	/**
	 * Task.30 動画カテゴリDTOリスト取得
	 * 
	 * @param movieCategoryName
	 * @return 動画カテゴリDTOリスト
	 */
	public List<MovieCategoryDto> getMovieCategoryList() {
		List<MovieCategoryDto> movieCategoryDtoList = mMovieCategoryMapper.getMovieCategory(null,
				Constants.DB_FLG_FALSE);
		return movieCategoryDtoList;
	}

	/**
	 * Task.100 動画カテゴリ検索
	 * 
	 * @param movieCategoryName
	 * @return 動画カテゴリDTOリスト
	 */
	public List<MovieCategoryDto> searchMovieCategoryList(String movieCategoryName) {
		List<MovieCategoryDto> movieCategoryDtoList = mMovieCategoryMapper
				.getMovieCategory(movieCategoryName, Constants.DB_FLG_FALSE);
		return movieCategoryDtoList;
	}

	/**
	 * Task.100 動画カテゴリ削除
	 * 
	 * @param movieCategoryForm
	 * @param result
	 * @return 完了メッセージ
	 */
	public String categoryDelete(MovieCategoryForm movieCategoryForm, BindingResult result) {

		String movieCategory = messageUtil.getMessage("movie.category");

		MMovieCategory mMovieCategory = mMovieCategoryMapper.findByMovieCategoryId(
				movieCategoryForm.getMovieCategoryId(), Constants.DB_FLG_FALSE);
		if (mMovieCategory == null) {
			result.addError(new FieldError(result.getObjectName(), "movieCategoryId",
					messageUtil.getMessage(Constants.VALID_KEY_ALREADYDELETE,
							new String[] { movieCategory })));
			return null;
		}

		Date now = new Date();
		mMovieCategory.setDeleteFlg(Constants.DB_FLG_TRUE);
		mMovieCategory.setLastModifiedUser(loginUserDto.getLmsUserId());
		mMovieCategory.setLastModifiedDate(now);
		mMovieCategoryMapper.updateDeleteFlg(mMovieCategory);

		MMovie mMovie = new MMovie();
		mMovie.setMovieCategoryId(movieCategoryForm.getMovieCategoryId());
		mMovie.setDeleteFlg(Constants.DB_FLG_TRUE);
		mMovie.setLastModifiedUser(loginUserDto.getLmsUserId());
		mMovie.setLastModifiedDate(now);
		mMovieMapper.updateDeleteFlgByMovieCategoryId(mMovie);

		return messageUtil.getMessage(Constants.PROP_KEY_DELETE_COMPLETE,
				new String[] { movieCategory });
	}

	/**
	 * Task.100 動画削除
	 * 
	 * @param movieForm
	 * @param result
	 * @return 完了メッセージ
	 */
	public String delete(MovieForm movieForm, BindingResult result) {
		String movie = messageUtil.getMessage("movie");

		MMovie mMovie = mMovieMapper.findByMovieId(movieForm.getMovieId(), Constants.DB_FLG_FALSE);
		if (mMovie == null) {
			result.addError(new FieldError(result.getObjectName(), "movieId", messageUtil
					.getMessage(Constants.VALID_KEY_ALREADYDELETE, new String[] { movie })));
			return null;
		}

		mMovie.setDeleteFlg(Constants.DB_FLG_TRUE);
		mMovie.setLastModifiedUser(loginUserDto.getLmsUserId());
		mMovie.setLastModifiedDate(new Date());
		mMovieMapper.updateDeleteFlgByMovieId(mMovie);

		return messageUtil.getMessage(Constants.PROP_KEY_DELETE_COMPLETE, new String[] { movie });
	}

	/**
	 * Task.100 動画の表示順を上に変更
	 * 
	 * @param movieForm
	 * @param result
	 * @return 完了メッセージ
	 */
	public String updateMoveup(MovieForm movieForm, BindingResult result) {
		String movie = messageUtil.getMessage("movie");

		MMovie mMovie = mMovieMapper.findByMovieId(movieForm.getMovieId(), Constants.DB_FLG_FALSE);
		if (mMovie == null) {
			result.addError(new FieldError(result.getObjectName(), "movieId", messageUtil
					.getMessage(Constants.VALID_KEY_ALREADYDELETE, new String[] { movie })));
			return null;
		}

		List<MMovie> mMovieList = mMovieMapper.findByMovieCategoryId(mMovie.getMovieCategoryId(),
				Constants.DB_FLG_FALSE);
		MMovie targetMMovie = null;
		List<MMovie> updateMMovieList = new ArrayList<>();
		for (int i = mMovieList.size() - 1; i >= 0; i--) {
			MMovie tempMMovie = mMovieList.get(i);
			if (tempMMovie.getMovieId().equals(movieForm.getMovieId())) {
				targetMMovie = tempMMovie;
			} else {
				updateMMovieList.add(tempMMovie);
				if (targetMMovie != null) {
					updateMMovieList.add(targetMMovie);
					targetMMovie = null;
				}
			}
		}
		if (targetMMovie != null) {
			updateMMovieList.add(targetMMovie);
		}
		int j = 1;
		for (int i = updateMMovieList.size() - 1; i >= 0; i--) {
			MMovie updateMMovie = updateMMovieList.get(i);
			updateMMovie.setSortNumber(j);
			mMovieMapper.updateSortNumber(updateMMovie);
			j++;
		}

		return messageUtil.getMessage(Constants.PROP_KEY_UPDATE_COMPLETE, new String[] { movie });
	}

	/**
	 * Task.100 動画の表示順を下に変更
	 * 
	 * @param movieForm
	 * @param result
	 * @return 完了メッセージ
	 */
	public String updateMovedown(MovieForm movieForm, BindingResult result) {
		String movie = messageUtil.getMessage("movie");
		MMovie mMovie = mMovieMapper.findByMovieId(movieForm.getMovieId(), Constants.DB_FLG_FALSE);
		if (mMovie == null) {
			result.addError(new FieldError(result.getObjectName(), "movieId", messageUtil
					.getMessage(Constants.VALID_KEY_ALREADYDELETE, new String[] { movie })));
			return null;
		}

		List<MMovie> mMovieList = mMovieMapper.findByMovieCategoryId(mMovie.getMovieCategoryId(),
				Constants.DB_FLG_FALSE);

		MMovie targetMMovie = null;
		List<MMovie> updateMMovieList = new ArrayList<>();
		for (int i = 0; i < mMovieList.size(); i++) {
			MMovie tempMMovie = mMovieList.get(i);

			if (tempMMovie.getMovieId().equals(movieForm.getMovieId())) {
				targetMMovie = tempMMovie;
			} else {
				updateMMovieList.add(tempMMovie);
				if (targetMMovie != null) {
					updateMMovieList.add(targetMMovie);
					targetMMovie = null;
				}
			}
		}
		if (targetMMovie != null) {
			updateMMovieList.add(targetMMovie);
		}

		for (int i = 0; i < updateMMovieList.size(); i++) {
			MMovie updateMMovie = updateMMovieList.get(i);
			updateMMovie.setSortNumber(i + 1);
			mMovieMapper.updateSortNumber(updateMMovie);
		}

		return messageUtil.getMessage(Constants.PROP_KEY_UPDATE_COMPLETE, new String[] { movie });
	}

	/**
	 * Task.101 動画カテゴリフォームを設定
	 * 
	 * @param movieCategoryForm
	 */
	public void setMovieCategoryForm(MovieCategoryForm movieCategoryForm) {
		if (movieCategoryForm.getMovieCategoryId() != null) {
			MMovieCategory mMovieCategory = mMovieCategoryMapper.findByMovieCategoryId(
					movieCategoryForm.getMovieCategoryId(), Constants.DB_FLG_FALSE);
			movieCategoryForm.setMovieCategoryName(mMovieCategory.getMovieCategoryName());
		}
	}

	/**
	 * Task.101 動画カテゴリを登録
	 * 
	 * @param movieCategoryForm
	 * @param result
	 * @return 完了メッセージ
	 */
	public String categoryRegistComplete(MovieCategoryForm movieCategoryForm,
			BindingResult result) {
		Date now = new Date();
		String movieCategory = messageUtil.getMessage("movie.category");

		if (movieCategoryForm.getMovieCategoryId() == null) {
			// 登録
			MMovieCategory mMovieCategory = new MMovieCategory();
			mMovieCategory.setMovieCategoryName(movieCategoryForm.getMovieCategoryName());
			mMovieCategory.setAccountId(loginUserDto.getAccountId());
			mMovieCategory.setDeleteFlg(Constants.DB_FLG_FALSE);
			mMovieCategory.setFirstCreateUser(loginUserDto.getLmsUserId());
			mMovieCategory.setFirstCreateDate(now);
			mMovieCategory.setLastModifiedUser(loginUserDto.getLmsUserId());
			mMovieCategory.setLastModifiedDate(now);
			mMovieCategoryMapper.insert(mMovieCategory);
			return messageUtil.getMessage(Constants.PROP_KEY_REGIST_COMPLETE,
					new String[] { movieCategory });
		} else {
			// 更新
			MMovieCategory mMovieCategory = mMovieCategoryMapper.findByMovieCategoryId(
					movieCategoryForm.getMovieCategoryId(), Constants.DB_FLG_FALSE);

			if (mMovieCategory == null) {
				result.addError(new FieldError(result.getObjectName(), "movieCategoryId",
						messageUtil.getMessage(Constants.VALID_KEY_ALREADYDELETE,
								new String[] { movieCategory })));
				return null;
			}

			mMovieCategory.setMovieCategoryName(movieCategoryForm.getMovieCategoryName());
			mMovieCategory.setLastModifiedUser(loginUserDto.getLmsUserId());
			mMovieCategory.setLastModifiedDate(now);
			mMovieCategoryMapper.update(mMovieCategory);
			return messageUtil.getMessage(Constants.PROP_KEY_UPDATE_COMPLETE,
					new String[] { movieCategory });
		}

	}

	/**
	 * Task.102 動画フォームを設定
	 * 
	 * @param movieForm
	 */
	public void setMovieForm(MovieForm movieForm) {
		if (movieForm.getMovieId() == null) {
			MMovieCategory mMovieCategory = mMovieCategoryMapper
					.findByMovieCategoryId(movieForm.getMovieCategoryId(), Constants.DB_FLG_FALSE);
			movieForm.setMovieCategoryName(mMovieCategory.getMovieCategoryName());
		} else {
			MovieDto movieDto = mMovieMapper.getMovieDto(movieForm.getMovieId(),
					Constants.DB_FLG_FALSE);
			movieForm.setMovieCategoryId(movieDto.getMovieCategoryId());
			movieForm.setMovieCategoryName(movieDto.getMovieCategoryName());
			movieForm.setMovieName(movieDto.getMovieName());
			movieForm.setUrl(movieDto.getUrl());
		}
	}

	/**
	 * Task.102 動画を登録
	 * 
	 * @param movieForm
	 * @param result
	 * @return 完了メッセージ
	 */
	public String movieRegistComplete(MovieForm movieForm, BindingResult result) {
		String movieCategory = messageUtil.getMessage("movie.category");
		MMovieCategory mMovieCategory = mMovieCategoryMapper
				.findByMovieCategoryId(movieForm.getMovieCategoryId(), Constants.DB_FLG_FALSE);
		if (mMovieCategory == null) {
			result.addError(
					new FieldError(result.getObjectName(), "movieId", messageUtil.getMessage(
							Constants.VALID_KEY_ALREADYDELETE, new String[] { movieCategory })));
			return null;
		}

		Date now = new Date();
		String movie = messageUtil.getMessage("movie");
		if (movieForm.getMovieId() == null) {
			// 登録
			MMovie mMovie = new MMovie();
			mMovie.setMovieCategoryId(movieForm.getMovieCategoryId());
			mMovie.setMovieName(movieForm.getMovieName());
			mMovie.setUrl(movieForm.getUrl());
			mMovie.setAccountId(loginUserDto.getAccountId());
			mMovie.setDeleteFlg(Constants.DB_FLG_FALSE);
			Integer sortNumber = mMovieMapper.getMaxSortNumber(movieForm.getMovieCategoryId(),
					Constants.DB_FLG_FALSE);
			if (sortNumber == null) {
				sortNumber = 1;
			} else {
				sortNumber++;
			}
			mMovie.setSortNumber(sortNumber);
			mMovie.setFirstCreateUser(loginUserDto.getLmsUserId());
			mMovie.setFirstCreateDate(now);
			mMovie.setLastModifiedUser(loginUserDto.getLmsUserId());
			mMovie.setLastModifiedDate(now);
			mMovieMapper.insert(mMovie);
			return messageUtil.getMessage(Constants.PROP_KEY_REGIST_COMPLETE,
					new String[] { movie });
		} else {
			// 更新
			MMovie mMovie = mMovieMapper.findByMovieId(movieForm.getMovieId(),
					Constants.DB_FLG_FALSE);
			if (mMovie == null) {
				result.addError(new FieldError(result.getObjectName(), "movieId", messageUtil
						.getMessage(Constants.VALID_KEY_ALREADYDELETE, new String[] { movie })));
				return null;
			}
			mMovie.setMovieName(movieForm.getMovieName());
			mMovie.setUrl(movieForm.getUrl());
			mMovie.setLastModifiedUser(loginUserDto.getLmsUserId());
			mMovie.setLastModifiedDate(now);
			mMovieMapper.update(mMovie);
			return messageUtil.getMessage(Constants.PROP_KEY_UPDATE_COMPLETE,
					new String[] { movie });
		}
	}

}
