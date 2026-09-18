/**
 * Task.115 コース詳細登録
 * 
 * @author 東京ITスクール
 */
let courseDto; // フォームで渡されたコース情報
let numOfCategory = 0; // カテゴリ番号シーケンス
let numOfSection = 0; // セクション番号シーケンス
let amntSection = new Map(); // カテゴリ毎のセクション数マップ

/* 初期化 */
function init(courseDetailRegistForm) {
	courseDto = courseDetailRegistForm;
	if (courseDto.updateFlg) {
		update();
	} else {
		regist();
	}
}

/* コース詳細登録 初期表示 */
function regist() {
	let html = getCategoryHeaderHtml(numOfCategory, '');
	html += getSectionHtml(numOfCategory, numOfSection, '', '', '');
	html += getCategoryFooterHtml(numOfCategory);
	$("#courseDetailArea").html(html);
	amntSection.set(numOfCategory, 1);
	numOfSection++;
	numOfCategory++;
	setWorkingDay();
}

/* コース詳細変更 初期表示 */
function update() {
	let html = '';
	for (let i = 0; i < courseDto.courseServiceCategoryDtoList.length; i++) {
		const categoryDto = courseDto.courseServiceCategoryDtoList[i];
//		console.log(`カテゴリ名：${categoryDto.categoryName}`);
		html += getCategoryHeaderHtml(numOfCategory, categoryDto.categoryId);
		for (let j = 0; j < courseDto.courseServiceCategoryDtoList[i].courseServiceSectionDtoList.length; j++) {
			const sectionDto = courseDto.courseServiceCategoryDtoList[i].courseServiceSectionDtoList[j];
//			console.log(`　　セクション名:${sectionDto.sectionName}`);
			html += getSectionHtml(numOfCategory, numOfSection, formatDate(sectionDto.date), sectionDto.sectionId, sectionDto.sectionName);
			let amnt = amntSection.get(numOfCategory);
			amnt = amnt == null ? 0 : amnt;
			amntSection.set(numOfCategory, ++amnt);
			numOfSection++;
		}
		html += getCategoryFooterHtml(numOfCategory);
		numOfCategory++;
	}
	$("#courseDetailArea").html(html);
	setWorkingDay();
}

/* 『上に(下に)カテゴリーを追加』ボタン(カテゴリ番号, 上下{0,1}) */
function addCategory(categoryNum, direction) {
	let html = getCategoryHeaderHtml(numOfCategory, '');
	html += getSectionHtml(numOfCategory, numOfSection, '', '', '');
	html += getCategoryFooterHtml(numOfCategory);
	if (direction == 0) {
		$("#categoryArea" + categoryNum).before(html);
	} else {
		$("#categoryArea" + categoryNum).after(html);
	}
	amntSection.set(numOfCategory, 1);
	numOfSection++;
	numOfCategory++;
	setWorkingDay();
}

/* 『このカテゴリーを削除』ボタン(カテゴリ番号) */
function delCategory(categoryNum) {
	if (amntSection.size == 1) {
		alert('カテゴリーは最低一つ必要です。');
		return;
	}
	amntSection.delete(categoryNum);
	$("#categoryDelFlg" + categoryNum).val(1);
	$("#categoryArea" + categoryNum).each(function(i) {
		$(this).find(".sectionDelFlg").val(1);
		$(this).find(".date").removeClass("date");
	});
	$("#categoryArea" + categoryNum).hide();
	setWorkingDay();
}

/* 『上に(下に)追加』ボタン(カテゴリ番号, セクション番号, 上下{0,1}) */
function addSection(categoryNum, sectionNum, direction) {
	let html = getSectionHtml(categoryNum, numOfSection, '', '', '');
	if (direction == 0) {
		$("#sectionRow" + sectionNum).before(html);
	} else {
		$("#sectionRow" + sectionNum).after(html);
	}
	let amnt = amntSection.get(categoryNum);
	amntSection.set(categoryNum, ++amnt);
	numOfSection++;
	setWorkingDay();
}

/* 『削除』ボタン(カテゴリ番号, セクション番号) */
function delSection(categoryNum, sectionNum) {
	let amnt = amntSection.get(categoryNum);
	if (amnt == 1) {
		alert('カテゴリーには最低一つのセクションが必要です。');
		return;
	} else {
		amnt--;
	}
	amntSection.set(categoryNum, amnt);
	$("#sectionDelFlg" + sectionNum).val(1);
	$("#sectionRow" + sectionNum).find(".date").removeClass("date");
	$("#sectionRow" + sectionNum).hide();
	setWorkingDay();
}

/* 『決定』ボタン */
function submitForm() {
	let requiredError = 0;
	for (let i = 0; i < numOfSection; i++) {
		if ($("#categoryDelFlg" + i).val() == 1) {
			continue;
		}
		$("#categoryArea" + i).each(function(i) {
			if ($(this).find(".categoryNameInput").val() == "") {
				requiredError++;
			}
		});
	}
	for (var i = 0; i < numOfSection; i++) {
		if ($("#sectionDelFlg" + i).val() == 1) {
			$("#date" + i).remove();
			continue;
		}
		$("#sectionRow" + i).each(function(i) {
			if ($(this).find(".sectionNameInput").val() == "") {
				requiredError++;
			}
		});
	}
	if (requiredError > 0) {
		alert(requiredError + "個の未入力項目があります。");
		return false;
	}
	if ($(".date").size() != courseDto.workingDaysList.length) {
		alert("研修日数が不適切です。");
		return false;
	}
	$("#courseDetailForm").submit();
}

/* 日付ラベルの振りなおし */
function setWorkingDay() {
	$(".date").each(function(i) {
		if (i < courseDto.workingDaysList.length) {
			$(this).find(".dateString").html(courseDto.workingDaysList[i]);
			$(this).find(".param").val(courseDto.workingDaysList[i]);
		} else {
			$(this).find(".dateString").html("コース期間超過");
		}
	});
//	console.log(`全研修日: ${courseDto.workingDaysList}`);
//	console.log(`カテゴリ番号シーケンス:${numOfCategory} セクション番号シーケンス:${numOfSection} 総カテゴリ数:${amntSection.size}`);
//	amntSection.forEach((value, key) => {
//		console.log(`　カテゴリ番号=${key} セクション数=${value}`);
//	});
}

/* 日付オブジェクトのフォーマット変更 */
function formatDate(time) {
	let date = new Date(time);
	let y = date.getFullYear();
	let m = date.getMonth() + 1;
	let d = date.getDate();
	return y + "/" + m + "/" + d;
}

/* カテゴリヘッダーHTML生成（カテゴリ番号, カテゴリID） */
function getCategoryHeaderHtml(categoryNum, categoryId) {
	let html =`
	<div id="categoryArea${categoryNum}" class="categoryArea well bs-component">
		<div class="mb20">
			<button type="button" class="btn btn-primary" onclick="addCategory(${categoryNum}, 0);">上へカテゴリーを追加</button>
			&nbsp;<button type="button" class="btn btn-danger" onclick="delCategory(${categoryNum});">このカテゴリーを削除</button>
		</div>
		<fieldset>
			<div class="form-group">
				<label for="categoryId${categoryNum}" class="col-lg-2 control-label">カテゴリー名</label>
				<div class="col-lg-10">
					<select id="categoryId${categoryNum}" name="courseServiceCategoryDtoList[${categoryNum}].categoryId" class="categoryNameInput form-control">
						<option value=""></option>
	`;
	for (let categoryDto of courseDto.categoryDtoList) {
		html += `						<option value="${categoryDto.categoryId}"`;
		html += categoryId != '' && categoryDto.categoryId == categoryId ? '	selected' : '';
		html += `>${categoryDto.categoryName}</option>`;		
	}
	html += `					</select>
				</div>
				<input type="hidden" id="categoryDelFlg${categoryNum}" name="courseServiceCategoryDtoList[${categoryNum}].categoryDelFlg" value="0" />
			</div>
			<table id="categoryTable${categoryNum}" class="table">
				<tr>
					<th>日付</th>
					<th>セクション名</th>
					<th></th>
				</tr>
	`;
	return html;
}

/* セクションエリアHTML生成（カテゴリ番号, セクション番号, 日付, セクションID, セクション名） */
function getSectionHtml(categoryNum, sectionNum, date, sectionId, sectionName) {
	return `
			<tr id="sectionRow${sectionNum}">
				<th class="date">
					<span class="dateString">${date}</span>
					<input type="hidden" id="date${sectionNum}" name="courseServiceCategoryDtoList[${categoryNum}].courseServiceSectionDtoList[${sectionNum}].date" value="${date}" class="param form-control" />
					<input type="hidden" id="sectionDelFlg${sectionNum}" name="courseServiceCategoryDtoList[${categoryNum}].courseServiceSectionDtoList[${sectionNum}].sectionDelFlg" class="sectionDelFlg" value="0" />
				</th>
				<td>
					<input type="hidden" id="sectionId${sectionNum}" name="courseServiceCategoryDtoList[${categoryNum}].courseServiceSectionDtoList[${sectionNum}].sectionId" value="${sectionId}" class="form-control" />
					<input type="text" id="sectionName${sectionNum}" name="courseServiceCategoryDtoList[${categoryNum}].courseServiceSectionDtoList[${sectionNum}].sectionName" value="${sectionName}" class="sectionNameInput form-control" maxlength="30" />
				</td>
				<td>
					<button type="button" class="btn btn-primary" onclick="addSection(${categoryNum},${sectionNum},0);">上に追加</button>
					&nbsp;<button type="button" class="btn btn-primary" onclick="addSection(${categoryNum},${sectionNum},1);">下に追加</button>
					&nbsp;<button type="button" class="btn btn-danger" onclick="delSection(${categoryNum},${sectionNum});">削除</button>
				</td>
			</tr>
	`;
}

/* カテゴリフッターHTML生成（カテゴリ番号） */
function getCategoryFooterHtml(categoryNum) {
	return `
		</table>
		<button type="button" class="btn btn-primary" onclick="addCategory(${categoryNum}, 1);">下へカテゴリーを追加</button>
	</fieldset>
	</div>
	`;
}
