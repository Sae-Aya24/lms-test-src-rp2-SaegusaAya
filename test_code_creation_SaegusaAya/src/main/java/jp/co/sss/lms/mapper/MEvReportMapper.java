package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.EvReportDto;
import jp.co.sss.lms.entity.MEvReport;

/**
 * 評価レポートマスタマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface MEvReportMapper {

	/**
	 * Task.103 評価レポートDTO取得（検索）
	 * 
	 * @param reportName
	 * @param deleteFlg
	 * @return 評価レポートDTOリスト
	 */
	List<EvReportDto> getEvReportDtoList(@Param("reportName") String reportName,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.103 評価レポート取得（評価レポートID）
	 * 
	 * @param evReportId
	 * @param deleteFlg
	 * @return 評価レポートエンティティ
	 */
	MEvReport findByEvReportId(@Param("evReportId") Integer evReportId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.103 評価レポート登録
	 * 
	 * @param mEvReport
	 * @return 登録結果
	 */
	Boolean insert(MEvReport mEvReport);

	/**
	 * Task.103 評価レポート更新削除
	 * 
	 * @param mEvReport
	 * @return 更新結果
	 */
	Boolean updateDeleteFlg(MEvReport mEvReport);

}
