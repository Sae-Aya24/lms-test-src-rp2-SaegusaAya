package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.CategoryDto;

/**
 * Task.115 カテゴリマスタマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface MCategoryMapper {

	/**
	 * カテゴリDTOリスト取得
	 * 
	 * @param deleteFlg
	 * @return カテゴリDTOリスト
	 */
	List<CategoryDto> getCategoryDtoList(@Param("deleteFlg") Short deleteFlg);

}
