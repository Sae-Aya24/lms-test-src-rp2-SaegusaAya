package jp.co.sss.lms.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.PresentationDto;

/**
 * 成果報告会スケジュールマスタマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface MPresentationScheduleMapper {

	/**
	 * Task.60 成果報告会スケジュール取得
	 * 
	 * @param deleteFlg
	 * @param publishedFlg
	 * @param placeId
	 * @param companyId
	 * @return 成果報告会DTOリスト
	 */
	List<PresentationDto> getPresentationDtoList(@Param("deleteFlg") Short deleteFlg,
			@Param("publishedFlg") Short publishedFlg, @Param("placeId") Integer placeId,
			@Param("companyId") Integer companyId);

	/**
	 * Task.61 成果報告会予約状況取得
	 * 
	 * @param deleteFlg
	 * @param presentationScheduleId
	 * @param companyId
	 * @return 成果報告会DTO
	 */
	PresentationDto getReserveDetail(@Param("deleteFlg") Short deleteFlg,
			@Param("presentationScheduleId") Integer presentationScheduleId,
			@Param("companyId") Integer companyId);


	/**
	 * Task.63 成果報告会チーム詳細DTOリスト取得
	 * 
	 * @param presentationScheduleId
	 * @param presentationTeamId
	 * @param companyId
	 * @param deleteFlg
	 * @return 成果報告会チーム詳細DTOリスト
	 */

	PresentationDto getPresentationTeamDetailDtoList(
			@Param("presentationScheduleId") Integer presentationScheduleId,
			@Param("presentationTeamId") Integer presentationTeamId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.75 成果報告会未予約件数取得
	 * 
	 * @param deleteFlg
	 * @param publishedFlg
	 * @param placeId
	 * @param companyId
	 * @param today
	 * @param nextDay
	 * @param joinAbleFlg
	 * @return 件数
	 */
	Integer notReserveCheck(@Param("deleteFlg") Short deleteFlg,
			@Param("publishedFlg") Short publishedFlg, @Param("placeId") Integer placeId,
			@Param("companyId") Integer companyId, @Param("today") Date today,
			@Param("nextDay") Date nextDay, @Param("joinAbleFlg") Short joinAbleFlg);

}
