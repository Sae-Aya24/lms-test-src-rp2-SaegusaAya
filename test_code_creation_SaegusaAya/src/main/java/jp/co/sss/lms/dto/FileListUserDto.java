package jp.co.sss.lms.dto;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import lombok.Data;

/**
 * ファイルリストユーザーDTO
 * 
 * @author 東京ITスクール
 */
@Component
@SessionScope
@Data
public class FileListUserDto {

	/** 共有ユーザーID */
	private Integer fssUserId;
	/** ログインユーザーチェック */
	private boolean isLoginUser;
	/** ユーザー名 */
	private String userName;

}
