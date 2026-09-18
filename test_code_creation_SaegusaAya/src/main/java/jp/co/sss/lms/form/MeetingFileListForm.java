package jp.co.sss.lms.form;

import java.util.List;

import jp.co.sss.lms.dto.MeetingFileDto;
import lombok.Data;

/**
 * Task.106 面談テンプレートファイル一覧フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class MeetingFileListForm {

	/** 面談ファイルDTOリスト */
	private List<MeetingFileDto> meetingFileDtoList;
	/** ファイル名 */
	private String fileName;
	/** 面談ファイルID */
	private Integer meetingFileId;
	/** 選択インデックス */
	private Integer selectedIndex;

}
