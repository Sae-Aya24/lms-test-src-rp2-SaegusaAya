package jp.co.sss.lms.form;

import java.util.List;

import jp.co.sss.lms.dto.CompanyDto;
import lombok.Data;

/**
 * Task.104 企業一覧フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class CompanyListForm {

	/** 企業DTOリスト */
	private List<CompanyDto> companyDtoList1;
	/** 検索結果リスト */
	private List<CompanyDto> companyDtoList2;
	/** 企業ID */
	private Integer companyId;
	/** 企業名 */
	private String companyName;
	/** 選択インデックス */
	private Integer selectedIndex;

}
