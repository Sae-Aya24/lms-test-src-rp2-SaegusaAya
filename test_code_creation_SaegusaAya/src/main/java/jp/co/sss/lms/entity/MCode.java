package jp.co.sss.lms.entity;

import lombok.Data;

/**
 * コースマスタエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class MCode {

	/** ID */
	private Integer id;
	/** キー */
	private String key;
	/** コード */
	private String code;
	/** 値 */
	private String value;
	/** 並び順 */
	private Integer order;

}
