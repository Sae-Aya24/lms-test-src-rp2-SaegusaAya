package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.entity.MEvReportDetail;

/**
 * 評価レポート詳細マッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface MEvReportDetailMapper {

	/**
	 * Task.103 評価レポート詳細取得（評価レポートID）
	 * 
	 * @param evReportId
	 * @param deleteFlg
	 * @return 評価レポート詳細エンティティリスト
	 */
	List<MEvReportDetail> findByEvReportId(@Param("evReportId") Integer evReportId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.103 評価レポート詳細登録
	 * 
	 * @param mEvReportDetail
	 * @return 登録結果
	 */
	Boolean insert(MEvReportDetail mEvReportDetail);

	/**
	 * Task.103 評価レポート詳細更新削除
	 * 
	 * @param mEvReportDetail
	 * @return 更新結果
	 */
	Boolean updateDeleteFlg(MEvReportDetail mEvReportDetail);

}
