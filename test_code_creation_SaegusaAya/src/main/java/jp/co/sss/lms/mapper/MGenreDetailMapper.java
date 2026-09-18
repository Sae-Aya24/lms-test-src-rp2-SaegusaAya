package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.GenreDetailDto;
import jp.co.sss.lms.entity.MGenreDetail;

/**
 * ジャンル詳細マスタマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface MGenreDetailMapper {

	/**
	 * Task.56 ジャンル詳細取得（ジャンル詳細ID）
	 * 
	 * @param genreDetailId
	 * @param deleteFlg
	 * @return ジャンル詳細エンティティ
	 */
	MGenreDetail findByGenreDetailId(@Param("genreDetailId") Integer genreDetailId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.97 削除フラグ更新（ジャンルID）
	 * 
	 * @param mGenreDetail
	 * @return 更新結果
	 */
	Boolean updateDeleteFlgByGenreId(MGenreDetail mGenreDetail);

	/**
	 * Task.97 削除フラグ更新（ジャンル詳細ID）
	 * 
	 * @param mGenreDetail
	 * @return 更新結果
	 */
	Boolean updateDeleteFlgByGenreDetailId(MGenreDetail mGenreDetail);

	/**
	 * Task.99 ジャンル詳細登録
	 * 
	 * @param mGenreDetail
	 * @return 登録結果
	 */
	Boolean insert(MGenreDetail mGenreDetail);

	/**
	 * Task.99 ジャンル詳細更新
	 * 
	 * @param mGenreDetail
	 * @return 更新結果
	 */
	Boolean update(MGenreDetail mGenreDetail);

	/**
	 * Task.113 ジャンル詳細DTOリスト取得
	 * 
	 * @param genreId
	 * @param deleteFlg
	 * @return ジャンル詳細DTOリスト
	 */
	List<GenreDetailDto> getGenreDetailDtoList(@Param("genreId") Integer genreId,
			@Param("deleteFlg") Short deleteFlg);

}
