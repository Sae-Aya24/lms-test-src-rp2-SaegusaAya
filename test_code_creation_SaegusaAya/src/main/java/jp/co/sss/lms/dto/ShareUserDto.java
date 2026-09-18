package jp.co.sss.lms.dto;

import java.util.List;

import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * ファイル共有ユーザーDTO
 * 
 * @author 東京ITスクール
 */
@Getter
@Setter
public class ShareUserDto {

	/** ファイル共有ユーザーID */
	private String fssUserId;
	/** 共有グループ名リスト */
	private List<String> groupNameList;
	/** ファイル共有グループ名 */
	private String fssGroupName;
	/** 共有者ニックネーム */
	private String nickname;
	/** 共有者名 */
	private String shareUserName;
	/** ユーザー名 */
	private String userName;

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ShareUserDto)) {
			return false;
		}

		ShareUserDto dto = (ShareUserDto) obj;
		boolean isSameFssUserId = StringUtils.pathEquals(this.fssUserId, dto.fssUserId);
		boolean isSameFssGroupName = StringUtils.pathEquals(this.fssGroupName, dto.fssGroupName);
		boolean isSameUserName = StringUtils.pathEquals(this.userName, dto.userName);

		return isSameFssUserId && isSameFssGroupName && isSameUserName;
	}

}
