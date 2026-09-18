package jp.co.sss.lms.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * Amazon S3のユーティリティ
 * 
 * @author 東京ITスクール
 */
@Component
public class AWSS3Util {

	@Autowired
	private MessageUtil messageUtil;

	/** マルチパートアップロード最大サイズ(1GB) */
//	private final long PART_SIZE = 10L * 1024L * 1024L * 1024L;

	@Value("${cloud.aws.credentials.accessKey:}")
	private String accessKey;
	@Value("${cloud.aws.credentials.secretKey:}")
	private String secretKey;
	@Value("${cloud.aws.region.static:}")
	private final Regions region = Regions.AP_NORTHEAST_1;

	/**
	 * Amazon S3にファイルをアップロードする。
	 * 
	 * @param uploadFile
	 * @param filePath
	 */
//	public void upload(MultipartFile uploadFile, String filePath) {
//
//		// メッセージリソースからバケット名を取得する。
//		// （バケット名が間違っていると403が発生する）
//		String bucketName = messageUtil.getMessage("setting.fileshare.bucketName");
//
//		try (InputStream is = uploadFile.getInputStream()) {
//			AmazonS3 s3 = connectS3();
//
//			TransferManager manager = TransferManagerBuilder.standard().withMinimumUploadPartSize(PART_SIZE)
//					.withS3Client(s3).build();
//			ObjectMetadata putMetadata = new ObjectMetadata();
//			putMetadata.setContentLength(uploadFile.getSize());
//
//			// ファイルアップロード
//			Upload upload = manager.upload(bucketName, filePath, is, putMetadata);
//
//			// アップロードが完了するまで、状態を取得し続ける
//			while (!upload.isDone()) {
//				upload.getProgress();
//			}
//			upload.waitForCompletion();
//
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}

//	/**
//	 * Amazon S3からファイルをダウンロードするURLを生成する。
//	 * 
//	 * @param filePath
//	 * @return
//	 */
//	public String makeDowloadUrl(String filePath) {
//		URL url = getFileUrl(filePath);
//		return url.toString();
//	}

//	/**
//	 * Amazon S3のファイルをダウンロードするURLを生成する。
//	 * 
//	 * @param filePath
//	 * @return
//	 */
//	public InputStream getFileStream(String filePath) throws IOException {
//		URL url = getFileUrl(filePath);
//		return url.openStream();
//	}

//	/**
//	 *
//	 * @param filePath
//	 * @return
//	 */
//	private URL getFileUrl(String filePath) {
//		String bucketName = messageUtil.getMessage("setting.fileshare.bucketName");
//		String timeLimit = messageUtil.getMessage("setting.fileshare.urltimelimit");
//
//		if (!StringUtils.isNumeric(timeLimit)) {
//			return null;
//		}
//
//		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region)
//				.withCredentials(new DefaultAWSCredentialsProviderChain()).build();
//
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.MILLISECOND, Integer.parseInt(timeLimit));
//		Date limit = cal.getTime();
//
//		URL url = s3.generatePresignedUrl(new GeneratePresignedUrlRequest(bucketName, filePath).withExpiration(limit));
//
//		return url;
//	}

//	/**
//	 * Amazon S3からローカルにファイルを保存する
//	 */
//	public static void dlDeliverablesFiles(List<TDeliverablesResult> tDeliverablesResultList,
//			HttpServletResponse response) throws IOException {
//		try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream(), Charset.forName("MS932"))) {
//			
//			for (TDeliverablesResult tDeliverablesResult : tDeliverablesResultList) {
//
//				// Zip内ディレクトリパス([成果物名]/[ユーザ名(ID)]/[アップロードファイル名])
//				String[] splitFilePath = tDeliverablesResult.getFilePath().split("/");
//				String uploadFileName = splitFilePath[splitFilePath.length - 1];
//				String path = tDeliverablesResult.tDeliverablesSection.mDeliverables.deliverablesName + "/"
//						+ tDeliverablesResult.mLmsUser.mUser.userName + "(" + tDeliverablesResult.mLmsUser.lmsUserId
//						+ ")" + "/" + uploadFileName;
//
//				InputStream is = AWSS3Util.getFileStream(tDeliverablesResult.filePath);
//
//				String zipName = tDeliverablesResult.tDeliverablesSection.mDeliverables.deliverablesName;
//				String fileName = new String((zipName + "_" + new Date().getTime()).getBytes("Windows-31J"),
//						"ISO-8859-1");
//				fileName += ".zip";
//				response.setContentType("application/octet-stream");
//				response.setHeader("Content-Disposition", "filename=\"" + fileName + "\"");
//
//				byte[] binary = IOUtils.toByteArray(is);
//
//				zos.putNextEntry(new ZipEntry(path));
//				zos.write(binary, 0, binary.length);
//				zos.closeEntry();
//			}
//		}
//	}

//	/**
//	 * 複数ファイルを削除する。
//	 * 
//	 * @param fileIdList
//	 */
//	public void deleteMultipleFile(List<String> fileIdList) {
//		String bucketName = messageUtil.getMessage("setting.fileshare.bucketName");
//		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region)
//				.withCredentials(new DefaultAWSCredentialsProviderChain()).build();
//		DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(bucketName);
//
//		List<KeyVersion> keys = new ArrayList<KeyVersion>();
//		for (String fileId : fileIdList) {
//			keys.add(new KeyVersion(fileId));
//		}
//
//		multiObjectDeleteRequest.setKeys(keys);
//
//		// DeleteObjectsResult delObjRes = s3.deleteObjects(multiObjectDeleteRequest);
//		s3.deleteObjects(multiObjectDeleteRequest);
//	}

//	/**
//	 * Amazon S3から指定したbucketNameの指定ディレクトリのフォルダを取得する
//	 * 
//	 * @param bucketName
//	 * @param prefix
//	 * @param delimiter
//	 * @return List<String>
//	 */
//	public List<String> getDirectoryNames(String bucketName, String prefix, String delimiter) {
//		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region)
//				.withCredentials(new DefaultAWSCredentialsProviderChain()).build();
//
//		ListObjectsRequest request = new ListObjectsRequest().withBucketName(bucketName).withPrefix(prefix)
//				.withDelimiter(delimiter);
//
//		ObjectListing list = s3.listObjects(request);
//		List<String> folders = list.getCommonPrefixes();
//		return folders;
//	}

//	/**
//	 * Amazon S3から指定したbucketNameの指定ディレクトリのファルダ名を変更する
//	 * 
//	 * @param bucketName
//	 * @param oldPrefix
//	 * @param newPrefix
//	 */
//	public void changeDirectoryName(String bucketName, String oldPrefix, String newPrefix) {
//		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region)
//				.withCredentials(new DefaultAWSCredentialsProviderChain()).build();
//
//		ObjectListing listing = s3.listObjects(bucketName, oldPrefix);
//
//		for (S3ObjectSummary summary : listing.getObjectSummaries()) {
//			String oldKey = summary.getKey();
//			String newKey = new StringBuilder().append(newPrefix).append(oldKey.substring(oldPrefix.length()))
//					.toString();
//			s3.copyObject(bucketName, oldKey, bucketName, newKey);
//			s3.deleteObject(bucketName, oldKey);
//		}
//	}

//	/**
//	 * Amazon S3の指定したbucketNameに指定名のフォルダが存在するかチェック
//	 * 
//	 * @param bucketName
//	 * @param prefix
//	 */
//	public boolean isExistDirectory(String bucketName, String prefix) {
//		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region)
//				.withCredentials(new DefaultAWSCredentialsProviderChain()).build();
//
//		ObjectListing listing = s3.listObjects(bucketName, prefix);
//		List<S3ObjectSummary> list = listing.getObjectSummaries();
//		boolean result = list.size() == 0 ? false : true;
//		return result;
//	}

//	/**
//	 * Amazon S3の指定したbucketNameの指定名のフォルダ直下にファイルがいくつ存在するかカウント
//	 * 
//	 * @param bucketName
//	 * @param prefix
//	 */
//	public int countFileAndDirectory(String bucketName, String prefix) {
//		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region)
//				.withCredentials(new DefaultAWSCredentialsProviderChain()).build();
//
//		ObjectListing listing = s3.listObjects(bucketName, prefix);
//		List<S3ObjectSummary> list = listing.getObjectSummaries();
//
//		return list.size();
//	}

//	/**
//	 * Amazon S3の指定したbucketNameの指定名のフォルダを削除する
//	 * 
//	 * @param bucketName
//	 * @param prefix
//	 */
//	public void deleteDirectory(String bucketName, String prefix) {
//		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region)
//				.withCredentials(new DefaultAWSCredentialsProviderChain()).build();
//
//		ObjectListing listing = s3.listObjects(bucketName, prefix);
//
//		for (S3ObjectSummary summary : listing.getObjectSummaries()) {
//			String key = summary.getKey();
//			s3.deleteObject(bucketName, key);
//		}
//	}

	public MultiValueMap<String, String> makeTeachingMaterialDownloadPath(String bucketName, String prefix) {

		AmazonS3 s3 = connectS3();

		ObjectListing listing = s3.listObjects(bucketName, prefix);
		MultiValueMap<String, String> multiMap = new LinkedMultiValueMap<String, String>();

		for (S3ObjectSummary summary : listing.getObjectSummaries()) {
			String key = summary.getKey();
			
			if (!key.substring(key.length() - 1).equals("/")) {
				
				String[] keyArray = key.split("/");
				String currentKey = "";
				for (int i = 0; i < keyArray.length - 1; i++) {
					
					currentKey = currentKey + keyArray[i] + "/";
					
				}
				currentKey = currentKey.substring(0, currentKey.length() - 1);
				multiMap.add(currentKey, keyArray[keyArray.length - 1]);
			}
		}

		return multiMap;
	}

	/**
	 * Amazon S3からフ対象ファイルのInputStreamを作成
	 * 
	 * @param filePath
	 * @return InputStream
	 * @throws IOException
	 */
	public InputStream makeDowloadFileInputStream(String bucketName, String key) throws IOException {
		String timeLimit = messageUtil.getMessage("setting.fileshare.urltimelimit");
		if (!StringUtils.isNumeric(timeLimit)) {
			return null;
		}
		AmazonS3 s3 = connectS3();

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MILLISECOND, Integer.parseInt(timeLimit));
		Date limit = cal.getTime();
		URL url = s3.generatePresignedUrl(new GeneratePresignedUrlRequest(bucketName, key).withExpiration(limit));

		try {
			return url.openStream();
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	/**
	 * Amazon S3の対象ファイルの存在チェック
	 * 
	 * @param bucketName
	 * @param key
	 * @return boolean
	 */
	public boolean isExistFile(String bucketName, String key) {

		AmazonS3 s3 = connectS3();
		
		try {
			s3.getObjectMetadata(bucketName, key);
			return true;
		} catch (AmazonServiceException e) {
			if (e.getStatusCode() != 404) {
				throw e;
			}
			return false;
		}
	}
	
	/**
	 * S3の接続
	 * 
	 * @return AmazonS3
	 */
	private AmazonS3 connectS3() {
		return AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(getCredentials())).withRegion(region).build();
	}

	/**
	 * アクセスキーとシークレットキーの設定
	 * 
	 * @return AWSCredentials
	 */
	private AWSCredentials getCredentials() {
		
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		return credentials;
		
	}
}
