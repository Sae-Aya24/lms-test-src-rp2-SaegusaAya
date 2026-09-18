package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.PresentationTeamDetailDto;
import jp.co.sss.lms.dto.PresentationTeamDto;

/**
 * 成果報告会予定マッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface MPresentationTeamMapper {

	/**
	 * Task.88 成果報告会予定
	 * 
	 * @param placeId
	 * @param deleteFlg
	 * @return 成果報告会予定DTOリスト
	 */
	List<PresentationTeamDto> getPresentationTeam(@Param("placeId") Integer placeId,
			@Param("deleteFlg") short deleteFlg);

	/**
	 * Task.110 成果報告会チーム詳細取得
	 * 
	 * @param presentation_place_id
	 * @param deleteFlg
	 * @return 成果報告チーム編成詳細DTOリスト
	 */
	List<PresentationTeamDetailDto> getPresentationTeamDetail(
			@Param("presentation_place_id") Integer presentation_place_id,
			@Param("deleteFlg") short deleteFlg);

}
