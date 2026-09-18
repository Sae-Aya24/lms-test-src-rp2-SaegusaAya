package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.MovieDto;
import jp.co.sss.lms.entity.MMovie;

/**
 * 動画マスタマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface MMovieMapper {

	/**
	 * Task.100 削除フラグ更新（動画カテゴリID）
	 * 
	 * @param mMovie
	 * @return 更新結果
	 */
	Boolean updateDeleteFlgByMovieCategoryId(MMovie mMovie);

	/**
	 * Task.100 動画取得（動画ID）
	 * 
	 * @param movieId
	 * @param deleteFlg
	 * @return 動画エンティティ
	 */
	MMovie findByMovieId(@Param("movieId") Integer movieId, @Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.100 削除フラグ更新（動画ID）
	 * 
	 * @param mMovie
	 * @return 更新結果
	 */
	Boolean updateDeleteFlgByMovieId(MMovie mMovie);

	/**
	 * Task.100 動画取得（動画カテゴリID）
	 * 
	 * @param movieCategoryId
	 * @param deleteFlg
	 * @return 動画エンティティリスト
	 */
	List<MMovie> findByMovieCategoryId(@Param("movieCategoryId") Integer movieCategoryId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.100 ソート順更新
	 * 
	 * @param mMovie
	 * @return 更新結果
	 */
	Boolean updateSortNumber(MMovie mMovie);

	/**
	 * Task.102 動画情報DTO取得
	 * 
	 * @param movieId
	 * @param deleteFlg
	 * @return 動画情報DTO
	 */
	MovieDto getMovieDto(@Param("movieId") Integer movieId, @Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.102 ソート順最大値取得
	 * 
	 * @param movieCategoryId
	 * @param deleteFlg
	 * @return ソート順最大値
	 */
	Integer getMaxSortNumber(@Param("movieCategoryId") Integer movieCategoryId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.102 動画登録
	 * 
	 * @param mMovie
	 * @return 登録結果
	 */
	Boolean insert(MMovie mMovie);

	/**
	 * Task.102 動画更新
	 * 
	 * @param mMovie
	 * @return 更新結果
	 */
	Boolean update(MMovie mMovie);

}
