package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.dto.CompanyCourseDto;
import jp.co.sss.lms.dto.CompanyDto;
import jp.co.sss.lms.entity.MCompany;

/**
 * 企業マスタマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface MCompanyMapper {

	/**
	 * Task.43 企業DTO取得（全件）
	 * 
	 * @param deleteFlg
	 * @return 企業DTOリスト
	 */
	List<CompanyDto> getCompanyDto(@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.66 企業取得（企業ID）
	 * 
	 * @param companyId
	 * @param deleteFlg
	 * @return 企業エンティティ
	 */
	MCompany findByCompanyId(@Param("companyId") Integer companyId,
			@Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.66 企業助成金情報更新
	 * 
	 * @param mCompany
	 * @return 更新結果
	 */
	Boolean updateCompanySubsidyInfo(MCompany mCompany);

	/**
	 * Task.104 企業DTO取得（検索）
	 * 
	 * @param companyName
	 * @param address
	 * @param deleteFlg
	 * @return 企業DTOリスト
	 */
	List<CompanyDto> getCompanyDtoForSearch(@Param("companyName") String companyName,
			@Param("address") String address, @Param("deleteFlg") Short deleteFlg);

	/**
	 * Task.104 企業更新削除
	 * 
	 * @param mCompany
	 * @return 更新結果
	 */
	Boolean updateDeleteFlg(MCompany mCompany);

	/**
	 * Task.105 企業登録
	 * 
	 * @param mCompany
	 * @return 登録結果
	 */
	Boolean insert(MCompany mCompany);

	/**
	 * Task.105 企業名更新
	 * 
	 * @param mCompany
	 * @return 更新結果
	 */
	Boolean updateCompanyName(MCompany mCompany);

	/**
	 * Task.110 企業コースDTOリスト取得
	 * 
	 * @param deleteFlg
	 * @return 企業コースDTOリスト
	 */
	List<CompanyCourseDto> getCompanyCourseDtoList(@Param("deleteFlg") Short deleteFlg);

}
