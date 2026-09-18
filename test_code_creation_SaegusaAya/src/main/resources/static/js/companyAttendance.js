/**
 * Task.58 勤怠一括登録
 * 
 * @author 東京ITスクール
 */
$(function(){
 	  /**
	   * 『受講生が入力した時刻をコピーする』ボタン押下
	   */
	  $('.copyAttendanceDaily').click(function(){
			const index = $(this).val();
			let startTimeCompanyAry = $('.startTimeCompany' + index);
			let endTimeCompanyAry = $('.endTimeCompany'  + index);
			let startTimeCompanyRoundedAry = $('.startTimeCompanyRounded' + index);
			let endTimeCompanyRoundedAry = $('.endTimeCompanyRounded' + index);
			let idxI, $startTimeCompany, $endTimeCompany, $startTimeCompanyRounded, $endTimeCompanyRounded;
			for (idxI = 0; idxI < startTimeCompanyAry.length; idxI++) {
				  $startTimeCompany = $(startTimeCompanyAry[idxI]);
				  $endTimeCompany = $(endTimeCompanyAry[idxI]);
				  $startTimeCompanyRounded = $(startTimeCompanyRoundedAry[idxI]);
				  $endTimeCompanyRounded = $(endTimeCompanyRoundedAry[idxI]);
				if ($startTimeCompany.val() === '' && !($startTimeCompanyRounded.val() === '')) {
					$startTimeCompany.val($startTimeCompanyRounded.val());
					$startTimeCompany.addClass('warnInput');
				}
				if ($endTimeCompany.val() === '' && !($endTimeCompanyRounded.val() === '')) {
					$endTimeCompany.val($endTimeCompanyRounded.val());
					$endTimeCompany.addClass('warnInput');
				}
			}
	  });
});
 /**
 * 『確定する』ボタン押下
 */
function setTargetIndex(index) {
	// エレメントを作成
	var element = document.createElement('input');
	// データを設定
	element.setAttribute('type', 'hidden');
	element.setAttribute('name', 'targetIndex');
	element.setAttribute('value', index);
	// 要素を追加
	document.bulkRegistForm.appendChild(element);
	return true;
}
