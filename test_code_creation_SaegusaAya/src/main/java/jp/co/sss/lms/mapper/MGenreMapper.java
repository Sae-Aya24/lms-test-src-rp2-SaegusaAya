package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.GenreDto;
import jp.co.sss.lms.entity.MGenre;

/**
 * ジャンルマスタマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface MGenreMapper {

	/**
	 * Task.97 ジャンルDTOリスト取得
	 * 
	 * @param genreName
	 * @param deleteFlg
	 * @return ジャンルDTOリスト
	 */
	List<GenreDto> getGenreDtoList(@Param("genreName") String genreName,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.97 削除フラグ更新
	 * 
	 * @param mGenre
	 * @return 更新結果
	 */
	Boolean updateDeleteFlg(MGenre mGenre);

	/**
	 * Task.98 ジャンル取得（ジャンルID）
	 * 
	 * @param genreId
	 * @param deleteFlg
	 * @return ジャンルエンティティ
	 */
	MGenre findByGenreId(@Param("genreId") Integer genreId, @Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.98 ジャンル登録
	 * 
	 * @param mGenre
	 * @return 登録結果
	 */
	Boolean insert(MGenre mGenre);

	/**
	 * Task.98 ジャンル更新
	 * 
	 * @param mGenre
	 * @return 更新結果
	 */
	Boolean update(MGenre mGenre);

}
