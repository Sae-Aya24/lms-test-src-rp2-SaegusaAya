package jp.co.sss.lms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.sss.lms.entity.TCourseTeachingMaterial;

/**
 * コース・教材紐付けテーブル
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface TCourseTeachingMaterialMapper {

	/**
	 * コース・教材紐付けリスト取得（コースID）
	 * 
	 * @param courseId
	 * @param deleteFlg
	 * @return コース・教材紐付けエンティティリスト
	 */
	List<TCourseTeachingMaterial> findByCourseId(@Param("courseId") Integer courseId,
			@Param("deleteFlg") Short deleteFlg);

}
