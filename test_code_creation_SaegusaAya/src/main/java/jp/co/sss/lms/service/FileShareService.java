package jp.co.sss.lms.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jp.co.sss.lms.dto.FileShareDto;
import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.dto.ShareUserDto;
import jp.co.sss.lms.mapper.TFssFileMapper;
import jp.co.sss.lms.mapper.TFssShareAvailableMapper;
import jp.co.sss.lms.mapper.TUserFssUserMapper;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.FileUtil;

/**
 * ファイル共有サービス
 * 
 * @author 東京ITスクール
 */
@Service
@SuppressWarnings("deprecation")
public class FileShareService {

	@Autowired
	private LoginUserDto loginUserDto;
	@Autowired
	private TUserFssUserMapper tUserFssUserMapper;
	@Autowired
	private TFssFileMapper tFssFileMapper;
	@Autowired
	private TFssShareAvailableMapper tFssShareAvailableMapper;

	/**
	 * ユーザー・共有ユーザー紐付け確認
	 * 
	 * @return 確認結果
	 */
	public boolean isLoginUserAdmin() {
		Integer count = tUserFssUserMapper.getCount(loginUserDto.getUserId(),
				Constants.CODE_VAL_GROUP_AUTH_ADMIN, Constants.DB_FLG_FALSE);
		if (count > 0) {
			return true;
		}
		return false;
	}

	/**
	 * ユーザーIDから共有ユーザーIDを取得し、セッションに設定する
	 * 
	 * @param userId
	 * @return fssUserId
	 */
	public Integer getFssUserId(Integer userId) {
		Integer fssUserId = tUserFssUserMapper.getFssUserId(userId, Constants.DB_FLG_FALSE);
		return fssUserId;
	}

	/**
	 * 共有ファイルのリストを取得する
	 * 
	 * @param fssUserId
	 * @return 共有ファイルリスト
	 */
	public List<FileShareDto> getFileShareDtoList(Integer fssUserId) {

		List<FileShareDto> fileShareDtoList = tFssFileMapper.getFileShareDtoList(fssUserId,
				Constants.DB_FLG_FALSE);

		for (FileShareDto fileShareDto : fileShareDtoList) {

			File file = new File(fileShareDto.getFilePath());
			fileShareDto.setFileName(file.getName());
			fileShareDto.setFileType(FileUtil.getSuffix(file.getName()) + "ファイル");
			fileShareDto.setFileSize(FileUtil.parseFileSize(fileShareDto.getFileSize()));
			// 所有者
			fileShareDto.setOwner(!StringUtils.isEmpty(fileShareDto.getOwnerNickname())
					? fileShareDto.getOwnerNickname()
					: fileShareDto.getOwnerUserName());
			// 共有者
			if (!fssUserId.equals(fileShareDto.getOwnerFssUserId())) {
				fileShareDto.setSharedPerson(fileShareDto.getOwner());
			}
			// 共有者（共有されたユーザー）
			if (fileShareDto.getSharedFssUserId() == null
					|| fssUserId.equals(fileShareDto.getSharedFssUserId())) {
				fileShareDto.setSharePerson("");
			} else {
				fileShareDto.setSharePerson(!StringUtils.isEmpty(fileShareDto.getSharedNickname())
						? fileShareDto.getSharedNickname()
						: fileShareDto.getSharedUserName());
			}
		}

		return fileShareDtoList;

	}

	/**
	 * 共有ユーザリスト取得
	 * 
	 * @param fssUserId
	 * @return 共有ユーザリスト
	 */
	public List<ShareUserDto> getShareUserDtoList(Integer fssUserId) {

		List<ShareUserDto> shareUserDtoList = tFssShareAvailableMapper
				.getShareUserDtoList(fssUserId, Constants.DB_FLG_FALSE);

		for (ShareUserDto shareUserDto : shareUserDtoList) {

			// 共有グループの設定
			String fssGroupName = "";
			for (String groupName : shareUserDto.getGroupNameList()) {
				if (StringUtils.isEmpty(groupName)) {
					continue;
				}
				if (!StringUtils.isEmpty(fssGroupName)) {
					fssGroupName += " | ";
				}
				fssGroupName += groupName;
			}
			shareUserDto.setFssGroupName(fssGroupName);

			// 共有ユーザの設定
			String userName = shareUserDto.getNickname();
			if (StringUtils.isEmpty(userName)) {
				userName = shareUserDto.getShareUserName();
			}
			shareUserDto.setUserName(userName);
		}

		return shareUserDtoList;

	}

}
