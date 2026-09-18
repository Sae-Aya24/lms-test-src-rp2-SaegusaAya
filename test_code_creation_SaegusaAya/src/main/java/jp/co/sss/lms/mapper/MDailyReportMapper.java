package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.DailyReportDto;

/**
 * 日報マスタマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface MDailyReportMapper {

	/**
	 * Task.114 レポートDTOリスト取得
	 * 
	 * @param hiddenFlg
	 * @param deleteFlg
	 * @return レポートDTOリスト
	 */
	List<DailyReportDto> getDailyReportDtoList(@Param("hiddenFlg") Short hiddenFlg,
			@Param("deleteFlg") Short deleteFlg);

}
