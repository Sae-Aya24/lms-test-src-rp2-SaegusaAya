package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * セクションマスタエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
// Task.115 Comparableを実装
public class MSection implements Comparable<MSection> {

	/** セクションID */
	private Integer sectionId;
	/** セクション名 */
	private String sectionName;
	/** 概要 */
	private String sectionDescription;
	/** コースID */
	private Integer courseId;
	/** カテゴリID */
	private Integer categoryId;
	/** 日付 */
	private Date date;
	/** 企業アカウントID */
	private Integer accountId;
	/** 削除フラグ */
	private Short deleteFlg;
	/** 初回作成者 */
	private Integer firstCreateUser;
	/** 初回作成日時 */
	private Date firstCreateDate;
	/** 最終更新者 */
	private Integer lastModifiedUser;
	/** 最終更新日時 */
	private Date lastModifiedDate;

	/*
	 * Task.115 日付順で並べ替え用比較メソッド (非 Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(MSection mSection) {
		return this.date.compareTo(mSection.getDate());
	}

}