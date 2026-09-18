package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.FileShareDto;

/**
 * 共有ファイルテーブルマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface TFssFileMapper {

	/**
	 * ファイル共有DTOリスト取得
	 * 
	 * @param fssUserId
	 * @param deleteFlg
	 * @return ファイル共有DTOリスト
	 */
	List<FileShareDto> getFileShareDtoList(@Param("fssUserId") Integer fssUserId,
			@Param("deleteFlg") short deleteFlg);

}
