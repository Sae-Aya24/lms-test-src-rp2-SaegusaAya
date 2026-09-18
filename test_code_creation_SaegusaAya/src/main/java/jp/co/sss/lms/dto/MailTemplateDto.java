package jp.co.sss.lms.dto;

import java.util.List;

import lombok.Data;

/**
 * Task.92 メールテンプレートDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class MailTemplateDto {

	/** メールテンプレートID */
	private int mailTemplateId;
	/** タイトル */
	private String subject;
	/** 本文 */
	private String body;
	/** ファイルDTOリスト */
	private List<FileDto> fileDtoList;
	/** 備考 */
	private String mailTempleteNote;

}
