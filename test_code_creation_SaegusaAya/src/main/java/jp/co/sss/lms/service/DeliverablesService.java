package jp.co.sss.lms.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jp.co.sss.lms.dto.DeliverablesWithSubmissionFlgDto;
import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.entity.TDeliverablesResult;
import jp.co.sss.lms.form.DeliverablesForm;
import jp.co.sss.lms.mapper.TDeliverablesResultMapper;
import jp.co.sss.lms.mapper.TDeliverablesSectionMapper;
import jp.co.sss.lms.util.Constants;

/**
 * 成果物情報サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class DeliverablesService {

	@Autowired
	private LoginUserDto loginUserDto;
	@Autowired
	private TDeliverablesSectionMapper tDeliverablesSectionMapper;
//	@Autowired
//	private AWSS3Util awsS3Util;
	@Autowired
	private TDeliverablesResultMapper tDeliverablesResultMapper;

	/**
	 * 成果物サービスDTOリスト取得
	 * 
	 * @param sectionId
	 * @param lmsUserId
	 * @return 成果物サービスDTOリスト
	 */
	public List<DeliverablesWithSubmissionFlgDto> getDeliverablesWithSubmissionFlgDtoList(
			Integer sectionId, Integer lmsUserId) {
		return tDeliverablesSectionMapper.getDeliverablesWithSubmissionFlgDtoList(sectionId,
				lmsUserId, Constants.DB_FLG_FALSE);
	}

	/**
	 * 成果物アップロード
	 * 
	 * @param deliverablesForm
	 */
	public void upload(DeliverablesForm deliverablesForm) {

		MultipartFile file = deliverablesForm.getUploadFile();
		Integer lmsUserId = loginUserDto.getLmsUserId();
		Date now = new Date();

		TDeliverablesResult tDeliverablesResult = new TDeliverablesResult();
		tDeliverablesResult.setDeliverablesSectionId(deliverablesForm.getDeliverablesSectionId());
		tDeliverablesResult
				.setFilePath("deliverablesFiles/" + deliverablesForm.getDeliverablesSectionId()
						+ "/" + lmsUserId + "/" + file.getOriginalFilename());
		tDeliverablesResult.setFileSize(file.getSize());
		tDeliverablesResult.setLmsUserId(lmsUserId);
		tDeliverablesResult.setSubmissionTime(now);
		tDeliverablesResult.setAccountId(loginUserDto.getAccountId());
		tDeliverablesResult.setDeleteFlg(Constants.DB_FLG_FALSE);
		tDeliverablesResult.setFirstCreateUser(lmsUserId);
		tDeliverablesResult.setFirstCreateDate(now);
		tDeliverablesResult.setLastModifiedUser(lmsUserId);
		tDeliverablesResult.setLastModifiedDate(now);

		try {
			//awsS3Util.upload(file, tDeliverablesResult.getFilePath());
		} catch (Exception e) {
			throw e;
		}

		tDeliverablesResultMapper.insert(tDeliverablesResult);
	}

}
