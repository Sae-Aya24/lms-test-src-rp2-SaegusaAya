package jp.co.sss.lms.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.PlaceDto;
import jp.co.sss.lms.dto.UserAttendanceDto;
import jp.co.sss.lms.entity.MPlace;

/**
 * 会場マスタマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface MPlaceMapper {

	/**
	 * Task.43 会場取得（会場ID）
	 * 
	 * @param placeId
	 * @param hiddenFlg
	 * @param deleteFlg
	 * @return 会場エンティティ
	 */
	MPlace findByPlaceId(@Param("placeId") Integer placeId, @Param("hiddenFlg") Short hiddenFlg,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.58 ユーザー勤怠情報DTO取得
	 * 
	 * @param placeId
	 * @param form
	 * @param to
	 * @param deleteFlg
	 * @return ユーザー勤怠情報DTOリスト
	 */
	List<UserAttendanceDto> getUserAttendanceDto(@Param("placeId") Integer placeId,
			@Param("from") Date form, @Param("to") Date to, @Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.79 会場DTOリスト取得
	 * 
	 * @param placeName
	 * @param hiddenFlg
	 * @param deleteFlg
	 * @return 会場DTOリスト
	 */
	List<PlaceDto> getPlaceDtoList(@Param("placeName") String placeName,
			@Param("hiddenFlg") Short hiddenFlg, @Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.95 削除フラグ更新
	 * 
	 * @param mPlace
	 * @return 更新結果
	 */
	Boolean updateDeleteFlg(MPlace mPlace);

	/**
	 * Task.96 会場登録
	 * 
	 * @param mPlace
	 * @return 登録結果
	 */
	Boolean insert(MPlace mPlace);

	/**
	 * Task.96 会場更新
	 * 
	 * @param mPlace
	 * @return 更新結果
	 */
	Boolean update(MPlace mPlace);

}
