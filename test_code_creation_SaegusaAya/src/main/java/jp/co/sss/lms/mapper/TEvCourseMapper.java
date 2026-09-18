package jp.co.sss.lms.mapper;

import org.apache.ibatis.annotations.Mapper;

import jp.co.sss.lms.entity.TEvCourse;

/**
 * 評価レポート・コース紐付けテーブルマッパー
 * 
 * @author 東京ITスクール
 */
@Mapper
public interface TEvCourseMapper {

	/**
	 * Task.114 評価レポート・コース紐付け登録
	 * 
	 * @param tEvCourse
	 * @return 登録結果
	 */
	Boolean insert(TEvCourse tEvCourse);

	/**
	 * Task.114 評価レポート・コース紐付け削除
	 * 
	 * @param courseId
	 * @return 削除結果
	 */
	Boolean delete(Integer courseId);

}
