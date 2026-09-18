package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.PlaceAssignDto;

/**
 * Task.67 会場アサイン情報テーブルマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface TPlaceAssignMapper {

	/**
	 * Task.67 会場アサイン情報DTOリスト取得
	 * 
	 * @param companyCourseId
	 * @param deleteFlg
	 * @return 会場アサイン情報DTOリスト
	 */
	List<PlaceAssignDto> getPlaceAssignDtoList(@Param("companyCourseId") Integer companyCourseId,
			@Param("deleteFlg") Short deleteFlg);

}
