package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.MovieCategoryDto;
import jp.co.sss.lms.entity.MMovieCategory;

/**
 * 動画カテゴリマスタマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface MMovieCategoryMapper {

	/**
	 * Task.30 動画カテゴリ取得
	 * 
	 * @param movieCategoryName
	 * @param deleteFlg
	 * @return 動画カテゴリDTOリスト
	 */
	List<MovieCategoryDto> getMovieCategory(@Param("movieCategoryName") String movieCategoryName,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.100 動画カテゴリ取得（動画カテゴリID）
	 * 
	 * @param movieCategoryId
	 * @param deleteFlg
	 * @return 動画カテゴリエンティティ
	 */
	MMovieCategory findByMovieCategoryId(@Param("movieCategoryId") Integer movieCategoryId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.100 削除フラグ更新
	 * 
	 * @param mMovieCategory
	 * @return 更新結果
	 */
	Boolean updateDeleteFlg(MMovieCategory mMovieCategory);

	/**
	 * Task.101 動画カテゴリ登録
	 * 
	 * @param mMovieCategory
	 * @return 登録結果
	 */
	Boolean insert(MMovieCategory mMovieCategory);

	/**
	 * Task.101 動画カテゴリ更新
	 * 
	 * @param mMovieCategory
	 * @return 更新結果
	 */
	Boolean update(MMovieCategory mMovieCategory);

}
