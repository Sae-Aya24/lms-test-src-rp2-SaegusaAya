package jp.co.sss.lms.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import jp.co.sss.lms.dto.CourseTeachingMaterialFileDto;
import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.entity.TCourseTeachingMaterial;
import jp.co.sss.lms.mapper.TCourseTeachingMaterialMapper;
import jp.co.sss.lms.util.AWSS3Util;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.MessageUtil;

/**
 * 教材情報サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class TeachingMaterialService {

	@Autowired
	private TCourseTeachingMaterialMapper tCourseTeachingMaterialMapper;
	@Autowired
	private LoginUserDto loginUserDto;
	@Autowired
	private MessageUtil messageUtil;
	@Autowired
	private AWSS3Util awsS3Util;

	/**
	 * 教材情報を取得
	 * 
	 * @return コース教材紐づけDTOリスト
	 */
	public List<CourseTeachingMaterialFileDto> getCourseTeachingMaterialDownloadUrlDtoList() {

		Integer courseId = loginUserDto.getCourseId();

		List<TCourseTeachingMaterial> tCourseTeachingMaterialList = tCourseTeachingMaterialMapper
				.findByCourseId(courseId, Constants.DB_FLG_FALSE);

		List<CourseTeachingMaterialFileDto> courseTeachingMaterialFileDtoList = new ArrayList<CourseTeachingMaterialFileDto>();
		String bucketName = messageUtil.getMessage("setting.contentsManagement.bucketName");
		for (TCourseTeachingMaterial tCourseTeachingMaterial : tCourseTeachingMaterialList) {
			CourseTeachingMaterialFileDto courseTeachingMaterialFileDto = new CourseTeachingMaterialFileDto();
			courseTeachingMaterialFileDto.setCourseId(tCourseTeachingMaterial.getCourseId());
			courseTeachingMaterialFileDto.setCurrentFile(tCourseTeachingMaterial.getFilePath());
			String directoryName = tCourseTeachingMaterial.getFilePath()
					.substring(tCourseTeachingMaterial.getFilePath().lastIndexOf("/") + 1);
			courseTeachingMaterialFileDto.setDirectoryName(directoryName);
			MultiValueMap<String, String> fileMap = awsS3Util.makeTeachingMaterialDownloadPath(
					bucketName, tCourseTeachingMaterial.getFilePath() + "/");
			if (fileMap.isEmpty()) {
				courseTeachingMaterialFileDto.setFileMap(null);
			} else {
				courseTeachingMaterialFileDto.setFileMap(fileMap);
			}

			courseTeachingMaterialFileDtoList.add(courseTeachingMaterialFileDto);

		}

		return courseTeachingMaterialFileDtoList;
	}

	/**
	 * ファイル存在チェック
	 * 
	 * @param filePath
	 * @return 判定結果
	 */
	public boolean isFileExist(String filePath) {
		String bucketName = messageUtil.getMessage("setting.contentsManagement.bucketName");
		return awsS3Util.isExistFile(bucketName, filePath);
	}

	/**
	 * ファイルダウンロード
	 * 
	 * @param fileName
	 * @param filePath
	 * @return ダウンロードしたファイル情報
	 * @throws IOException
	 */
	public InputStream download(String fileName, String filePath) throws IOException {
		String bucketName = messageUtil.getMessage("setting.contentsManagement.bucketName");
		return awsS3Util.makeDowloadFileInputStream(bucketName, filePath);
	}

}
