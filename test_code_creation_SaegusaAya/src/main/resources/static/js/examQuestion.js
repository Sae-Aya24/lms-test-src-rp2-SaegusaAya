/**
 * 試験実施機能
 * 
 * @author 東京ITスクール
 */
$(function(){
	setInterval("timeCount()", 1000);
	setTimeout("timeUp()", limitTimeMin * 60 * 1000 - timeMSec + 1000);
	// Task.35 ②戻るボタンの追加
	$('#backButton').click(function(){
		if (confirm("試験を中止します。よろしいですか？")) {
			$('#backForm').submit();
		}
	});
	// Task.38 回答送信ダイアログの追加
	$('#sendButton').click(function(){
		if (confirm("回答を送信します。よろしいですか？")) {
			$('#examQuestionForm').submit();
		}
	});
});

function timeCount() {
	timeMSec += 1000;
	$('.sendTime').val(timeMSec);
	// Task.35 ①残り時間の表示
	// Task.36 残り時間の表示
	const remainMSec = limitTimeMin * 60 * 1000 - timeMSec;
	const remainMin = Math.floor(remainMSec / 60 / 1000);
	const remainSec = Math.floor((remainMSec - (remainMin * 60 * 1000)) / 1000);
	$('#remainTime').text("残り時間：" + remainMin + "分" + remainSec + "秒");
}

function timeUp() {
	alert('制限時間となりました。回答を送信します。');
	$('.sendTime').val(limitTimeMin * 60 * 1000);
	$('#examQuestionForm').submit();
}
