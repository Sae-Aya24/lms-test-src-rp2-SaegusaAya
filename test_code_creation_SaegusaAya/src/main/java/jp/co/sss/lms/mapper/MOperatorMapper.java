package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.OperatorDto;

/**
 * Task.41 オペレーターマスタマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface MOperatorMapper {

	/**
	 * Task.41 オペレータ取得（オペレータタイプ）
	 * 
	 * @param operatorType
	 * @param deleteFlg
	 * @return オペレータDTOリスト
	 */
	List<OperatorDto> findByOperatorType(@Param("operatorType") Short operatorType,
			@Param("deleteFlg") Short deleteFlg);

}
