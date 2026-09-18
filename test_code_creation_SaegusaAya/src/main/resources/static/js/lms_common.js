function delcheck(){
  return window.confirm('削除します。よろしいですか？');
}
function submitcheck() {
  return window.confirm('送信します。よろしいですか？');
}
function changecheck(){
  return window.confirm('変更します。よろしいですか？　※既に試験が受験されている場合、試験の問題を追加または削除すると試験結果が参照できなくなります。');
}
function allCheck(parentName, object) {
  if(object.checked){
    $(parentName + ' input').prop('checked', true);
  }else{
    $(parentName + ' input').prop('checked', false);
  }
}
var l = {
      emptyTable : 'データが登録されていません。',
      infoFiltered : '(全_MAX_ 件からの絞り込み表示)',
      infoEmpty : '',
      emptyTable : 'データが登録されていません。',
      info : '_TOTAL_ 件中 _START_ 件から _END_ 件までを表示',
      lengthMenu : '1ページあたりの表示件数: _MENU_',
      search : '検索：',
      zeroRecords : '該当するデータが見つかりませんでした。',
      paginate : {
        first : '先頭',
        previous : '前へ',
        next : '次へ',
        last : '末尾'
      }
};
function escape(str) {
  return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/\'/g, '&#39;').replace(/\"/g, '&quot;');
}
$(function(){
    $('[data-toggle="popover"]').popover();

    $('#file-input').change(function() {
        $('#file-input-dummy').val($(this).val());
    });

    $('input[type=date]').blur(function(){
        var dateVal = $(this).val();
        dateVal = dateVal.replace(/\u002f/g, '-');
        if (dateVal.replace(/-/g, '').length == 8) {
            dateVal = dateVal.replace(/-/g, '');
            var yyyy = dateVal.substring(0, 4);
            var mm   = dateVal.substring(4, 6);
            var dd   = dateVal.substring(6, 8);
            dateVal = yyyy + '-' + mm + '-' + dd;
        } else {
            var dateYmd = dateVal.split('-');
            if (dateYmd.length == 3) {
                var yyyy = dateYmd[0];
                if (yyyy.length != 4) {
                    yyyy = '2' + ('000' + yyyy).slice( -3 );
                }

                var mm   = dateYmd[1];
                if (mm.length != 2) {
                    mm = ('00' + mm).slice( -2 );
                }
                if (mm == '00') {
                    mm = '01';
                }

                var dd   = dateYmd[2];
                if (dd.length != 2) {
                    dd = ('00' + dd).slice( -2 );
                }
                if (dd == '00') {
                    dd = '01';
                }
                dateVal = yyyy + '-' + mm + '-' + dd;
            }
        }
        $(this).val(dateVal);
    });


    $('.sortabletable').DataTable({
        searching: false,
        stateSave: true,
        lengthMenu: [ [10, 20, 30, 40, 50, -1], [10, 20, 30, 40, 50, 'ALL'] ],
        autoWidth: false,
        language: l
      });

    $('.currencysortabletable').DataTable({
        searching: false,
        stateSave: true,
        columnDefs: [ { type: 'currency', targets: [4] } ],
        lengthMenu: [ [10, 20, 30, 40, 50, -1], [10, 20, 30, 40, 50, 'ALL'] ],
        autoWidth: false,
        language: l
      });

    $('.sortabletablecheckbox').DataTable({
        searching: false,
        stateSave: true,
        lengthMenu: [ [10, 20, 30, 40, 50, -1], [10, 20, 30, 40, 50, 'ALL'] ],
        autoWidth: false,
        language: l,
        order: [1, 'asc'],
        columnDefs: [ {
          targets: [ 0 ],
          orderable: false
        }]
      });

    $('.sortandsearchabletable').DataTable({
        searching: true,
        stateSave: true,
        lengthMenu: [ [10, 20, 30, 40, 50, -1], [10, 20, 30, 40, 50, 'ALL'] ],
        autoWidth: false,
        language: l
      });

    $('.sortandsearchableandeckboxtable').DataTable({
        searching: true,
        stateSave: true,
        lengthMenu: [ [10, 20, 30, 40, 50, -1], [10, 20, 30, 40, 50, 'ALL'] ],
        autoWidth: false,
        language: l,
        order: [1, 'asc'],
        columnDefs: [ {
          targets: [ 0 ],
          orderable: false
        }]
      });

    $('.searchabletable').DataTable({
        ordering: false,
        stateSave: true,
        responsive: true,
        lengthMenu: [ [3, 5, 10, 15, 20, -1], [3, 5, 10, 15, 20, 'ALL'] ],
        autoWidth: false,
        language: l
      });


    var isInfoOver = false;
    $('#lms-infomation').click(function(){
      if (isInfoOver == false) {
        $.ajax({
          url:'/lms/info',
          async : true,
          contentType : "application/json",
          dataType : "json",
          cache : false,
          success: function (infoDto) {
                isInfoOver = true;
                const message = infoDto.content == '' ? '<strong>* 現在お知らせはありません</strong>' : infoDto.content.replaceAll('\n', '<br />');
                $('#lms-infomation-hide').attr('data-content', message);
                $('#lms-infomation-hide').click();
          }
        });
      } else {
          isInfoOver = false;
          $('#lms-infomation-hide').click();
      }
    });

    $('.datepicker').datepicker({
        format: 'yyyy-mm-dd',
        language: 'ja',
        autoclose: true
    });

    $('.datepickermonth').datepicker({
        format: 'yyyy-mm',
        language: 'ja',
        autoclose: true,
        minViewMode: 'months'
    });

    $('.datepickerFrom').datepicker({
        format: 'yyyy-mm-dd',
        endDate: 'today',
        language: 'ja',
        autoclose: true
    });

    $('.datepickerTo').datepicker({
        format: 'yyyy-mm-dd',
        endDate: 'today',
        language: 'ja',
        autoclose: true
    });

    function reIndex(tbodyElement, rowIndex) {
        var trElement = $(tbodyElement).children();
        var trElementAmount = trElement.length;

        for (var i = 0; i < trElementAmount; i ++) {
          var tdElement = $(trElement.get(i)).children();
          var tdElementAmount = tdElement.length;
          for (var j = 0; j < tdElementAmount; j++) {
            var inputElement        = $(tdElement.get(j)).children();
            var inputElementTagName = $(inputElement).prop('tagName');
            var inputElementType    = $(inputElement).attr('type');
            var inputElementName    = $(inputElement).attr('name');
            var inputElementId      = $(inputElement).attr('id');

            var b11 = inputElementName == null;

            var b21 = inputElementTagName !== 'TEXTAREA';
            var b22 = inputElementTagName !== 'INPUT';
            var b23 = inputElementTagName !== 'SELECT';

            var b31 = inputElementTagName === 'INPUT';
            var b32 = inputElementType    === 'button';
            var b33 = inputElementType    === 'submit';
            var b34 = inputElementType    === 'reset';
            var b35 = inputElementType    === 'image';
            var b36 = inputElementType    === 'hidden';

            if (b11 || (b21 && b22 && b23) || (b31 && (b32 || b33 || b34 || b35 || b36) ) ) {
              continue;
            }

            if (rowIndex == i) {
              $(inputElement).val('');
            }
            
            if (inputElementName.indexOf('[') != -1) {
              var charLength = inputElementName.length;
              var replaceName = inputElementName.substring(0, inputElementName.lastIndexOf('[')) + '[' + i + ']';
              $(inputElement).attr('name', replaceName);
            }

            if (inputElementId.indexOf('[') != -1) {
              var charLength = inputElementId.length;
              var replaceId = inputElementId.substring(0, inputElementId.lastIndexOf('[')) + '[' + i + ']';
              $(inputElement).attr('id', replaceId);
            }
          }
        }
      }

});