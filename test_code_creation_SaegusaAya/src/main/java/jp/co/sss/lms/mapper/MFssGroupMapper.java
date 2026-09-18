package jp.co.sss.lms.mapper;

import org.apache.ibatis.annotations.Mapper;

import jp.co.sss.lms.entity.MFssGroup;

/**
 * 共有グループマスタマッパー
 * 
 * @author 東京ITスクール
 */

@Mapper
public interface MFssGroupMapper {

	/**
	 * Task.105 共有グループ登録
	 * 
	 * @param mFssGroup
	 * @return 登録結果
	 */
	Boolean insert(MFssGroup mFssGroup);

	/**
	 * Task.105 共有グループ更新
	 * 
	 * @param mFssGroup
	 * @return 更新結果
	 */
	Boolean updateGroupName(MFssGroup mFssGroup);

}
