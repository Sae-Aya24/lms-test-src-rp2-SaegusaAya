package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.CodeDto;

/**
 * コードマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface MCodeMapper {

	/**
	 * Task.51 コード取得（キー）
	 * 
	 * @param key
	 * @return 権限DTOリスト
	 */
	List<CodeDto> findByKey(@Param("key") String key);

}
