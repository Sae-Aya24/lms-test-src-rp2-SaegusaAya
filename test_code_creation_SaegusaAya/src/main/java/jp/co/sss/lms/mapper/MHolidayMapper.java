package jp.co.sss.lms.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.entity.MHoliday;

/**
 * Task.86 休日マスタマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface MHolidayMapper {

	/**
	 * Task.86 休日取得（年月）
	 * 
	 * @param startDate
	 * @param deleteFlg
	 * @return 休日エンティティ
	 */
	List<MHoliday> findByHolidayDate(@Param("startDate") String startDate,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.86 休日削除
	 * 
	 * @param holidayId
	 * @return 削除結果
	 */
	Boolean delete(@Param("holidayId") Integer holidayId);

	/**
	 * Task.86 休日登録
	 * 
	 * @param mHoliday
	 * @return 登録結果
	 */
	Boolean insert(MHoliday mHoliday);

	/**
	 * Task.115 休日取得（期間）
	 * 
	 * @param startDate
	 * @param endDate
	 * @param deleteFlg
	 * @return 休日エンティティ
	 */
	List<MHoliday> findByHolidayStartAndEndDate(@Param("startDate") Date startDate,
			@Param("endDate") Date endDate, @Param("deleteFlg") Short deleteFlg);

}
