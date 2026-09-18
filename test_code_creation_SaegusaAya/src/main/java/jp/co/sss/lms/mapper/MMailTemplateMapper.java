package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.MailTemplateDto;

/**
 * メールテンプレートマスタマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface MMailTemplateMapper {

	/**
	 * Task.92 メールテンプレートDTOリスト取得
	 * 
	 * @param deleteFlg
	 * @return メールテンプレートDTOリスト
	 */
	List<MailTemplateDto> getMailTemplateDtoList(@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.93 メールテンプレートDTO取得
	 * 
	 * @param mailTemplateId
	 * @param deleteFlg
	 * @return メールテンプレートDTO
	 */
	MailTemplateDto getMailTemplateDto(@Param("mailTemplateId") Integer mailTemplateId,
			@Param("deleteFlg") short deleteFlg);

}
