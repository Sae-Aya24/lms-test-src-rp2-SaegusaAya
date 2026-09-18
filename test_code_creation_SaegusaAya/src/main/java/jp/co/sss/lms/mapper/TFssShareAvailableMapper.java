package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.ShareUserDto;

/**
 * 共有可能ユーザテーブルマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface TFssShareAvailableMapper {

	/**
	 * ファイル共有ユーザーDTOリスト取得
	 * 
	 * @param fssUserId
	 * @param deleteFlg
	 * @return ファイル共有ユーザーDTOリスト
	 */
	List<ShareUserDto> getShareUserDtoList(@Param("fssUserId") Integer fssUserId,
			@Param("deleteFlg") short deleteFlg);

}
