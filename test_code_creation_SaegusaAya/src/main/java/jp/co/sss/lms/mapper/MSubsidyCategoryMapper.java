package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.SubsidyCategoryDto;

/**
 * Task.67 助成金カテゴリマスタマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface MSubsidyCategoryMapper {

	/**
	 * Task.67 助成金カテゴリDTOリスト取得
	 * 
	 * @param deleteFlg
	 * @return 助成金カテゴリDTOリスト
	 */
	List<SubsidyCategoryDto> getSubsidyCategoryDtoList(@Param("deleteFlg") Short deleteFlg);

}
