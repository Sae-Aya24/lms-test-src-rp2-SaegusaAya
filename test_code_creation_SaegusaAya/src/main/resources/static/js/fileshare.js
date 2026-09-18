$(function(){

  $('#del-link').click(function() {
    var targetDiv = $($(this).attr('data-target') + ' .modal-dialog .modal-content');
    var chkRec=$('input[name=fileCheck]:checked').parents('tr');
    var html='';
    if (chkRec.length <= 0) {
      html='<span class="help-inline error">ファイルを選択してください</span>';
      targetDiv.children('.modal-footer').hide();
    } else {
      var fileIdArr = '';
      var fileNameArr = '';
      var sharedFileArr = '';
      var shareFileArr = '';

      for(i=0;i<chkRec.length;i++) {
        var fileId=$(chkRec[i]).children('td').children(':hidden[name=fileId]').val();
        var fileName=$(chkRec[i]).children('td').children(':hidden[name=fileName]').val();
        var sharedPerson=$(chkRec[i]).children('td').children(':hidden[name=sharedPerson]').val();
        var sharePerson=$(chkRec[i]).children('td').children(':hidden[name=sharePerson]').val();


        if (sharedPerson != '') {
          sharedFileArr = '<li>' + fileName + '[' + sharedPerson + ']' + '</li>' + sharedFileArr;
        } else {
          if (sharePerson != '') {
            shareFileArr = '<li>' + fileName + '[' + sharePerson + ']' + '</li>' + shareFileArr;
          }
          fileNameArr = '<li>' + fileName + '</li>' +  fileNameArr ;
          fileIdArr = '<input type=hidden name=fileId[' + i + '] value=' + fileId + ' />' + fileIdArr;
        }
      }

      if (fileNameArr != '') {
        html=html + '以下のファイル削除します。';
        html=html + '<ul style="color:#bb9944;">';
        html=html + fileNameArr;
        html=html + '</ul>';
        html=html + fileIdArr;
        if (shareFileArr != '') {
          html=html + '以下のファイルは共有が解除されます。';
          html=html + '<ul style="color:#bb9944;">';
          html=html + shareFileArr;
          html=html + '</ul>';

        }
        targetDiv.children('.modal-footer').show();
      } else {
        targetDiv.children('.modal-footer').hide();
      }

      if (sharedFileArr != '') {
        html=html + '<span class="help-inline error">以下のファイルは共有されたファイルなので削除できません。</span>';
        html=html + '<ul style="color:#bb9944;">';
        html=html + sharedFileArr;
        html=html + '</ul>';
      }

    }
    targetDiv.children('.modal-body').html(html);
  });

  $('#shr-link').click(function() {

    var otherShareListId  = 'share-file-list-shr';
    var otherSharedListId = 'shared-file-list-shr'

    var targetId = $(this).attr('data-target');
    var targetDiv = $(targetId + ' .modal-dialog .modal-content');

    $(targetId).off('click', '#shared-file-list-shr-toggle');
    $(targetId).off('click', '#share-file-list-shr-toggle');

    var chkRec=$('input[name=fileCheck]:checked').parents('tr');
    var html='';
    if (chkRec.length <= 0) {
      html='<span class="help-inline error">ファイルを選択してください</span>';
      targetDiv.children('.modal-body').children('.table').hide();
      targetDiv.children('.modal-footer').hide();
    } else {
      var fileIdArr = '';
      var fileNameArr = '';
      var sharefileName = '';
      var sharedFile = '';
      var sharedFileArr = '';
      var shareFileAmnt = 0;
      var sharedFileAmnt = 0;

      for(i=0;i<chkRec.length;i++) {
        var fileId=$(chkRec[i]).children('td').children(':hidden[name=fileId]').val();
        var fileName=$(chkRec[i]).children('td').children(':hidden[name=fileName]').val();
        var sharedPerson=$(chkRec[i]).children('td').children(':hidden[name=sharedPerson]').val();

        if (sharedPerson == '') {
          shareFileAmnt ++;
          if (shareFileAmnt > 1) {
            fileNameArr = '<li>' + fileName + '</li>' +  fileNameArr ;
          } else {
            sharefileName = '<li>' + fileName + '</li>';
          }
          fileIdArr = '<input type=hidden name=fileId[' + i + '] value=' + fileId + ' />' + fileIdArr;
        }

        else {
          sharedFileAmnt++;
          if (sharedFileAmnt > 1) {
            sharedFileArr = '<li>' + fileName + '[' + sharedPerson + ']' + '</li>' + sharedFileArr;
          } else {
            sharedFile = '<li>' + fileName + '[' + sharedPerson + ']' + '</li>';
          }

        }
      }

      if (shareFileAmnt > 0) {
        html=html + '以下のファイルを共有します。';
        html=html + '<ul style="color:#bb9944;">';
        html=html + sharefileName;
        if (shareFileAmnt > 1) {
          html=html + '（他' + (shareFileAmnt - 1) + '件　<a href="#" style="color:#bb9944;text-decoration: underline;" id="' + otherShareListId + '-toggle">表示する</a>）';
          html=html + '</ul>';
          html=html + '<div id="' + otherShareListId + '">';
          html=html + '<ul style="color:#bb9944;">';
          html=html + fileNameArr;
          html=html + '</ul>';
          html=html + '</div>';
          $(targetId).on('click', '#' + otherShareListId + '-toggle', function(){
            var targetDivObj = $('#' + otherShareListId);
            if (targetDivObj.is(':visible')) {
              targetDivObj.hide();
              $(this).text('表示する');
            } else {
              targetDivObj.show();
              $(this).text('隠す');
            }
          });
        } else {
          html=html + '</ul>';
        }
        html=html + fileIdArr;
        targetDiv.children('.modal-body').children('.table').show();
        targetDiv.children('.modal-footer').show();
      } else {
        targetDiv.children('.modal-body').children('.table').hide();
        targetDiv.children('.modal-footer').hide();
      }

      if (sharedFileAmnt > 0) {
        html=html + '<span class="help-inline error">以下のファイルは共有されたファイルなので共有できません。</span>';
        html=html + '<ul style="color:#bb9944;">';
        html=html + sharedFile;
        if (sharedFileAmnt > 1) {
          html=html + '（他' + (sharedFileAmnt -1 ) + '件　<a href="#" style="color:#bb9944;text-decoration: underline;" id="' + otherSharedListId + '-toggle">表示する</a>）';
          html=html + '</ul>';
          html=html + '<div id="' + otherSharedListId + '">';
          html=html + '<ul style="color:#bb9944;">';
          html=html + sharedFileArr;
          html=html + '</ul>';
          html=html + '</div>';
          $(targetId).on('click', '#' + otherSharedListId + '-toggle', function(){
            var targetDivObj = $('#' + otherSharedListId);
            if (targetDivObj.is(':visible')) {
              targetDivObj.hide();
              $(this).text('表示する');
            } else {
              targetDivObj.show();
              $(this).text('隠す');
            }
          });
        } else {
          html=html + '</ul>';
        }
      }
    }

    targetDiv.children('.modal-body').children('.messages').html(html);
    $('#' + otherShareListId).hide();
    $('#' + otherSharedListId).hide();
  });


  $('#shr-release-link').click(function() {
    var targetDiv = $($(this).attr('data-target') + ' .modal-dialog .modal-content');
    var chkRec=$('input[name=fileCheck]:checked').parents('tr');
    var html='';

    var fileIdArr = '';
    var shareFileArr = '';

    for(i=0;i<chkRec.length;i++) {
      var fileId=$(chkRec[i]).children('td').children(':hidden[name=fileId]').val();
      var fileName=$(chkRec[i]).children('td').children(':hidden[name=fileName]').val();
      var sharePerson=$(chkRec[i]).children('td').children(':hidden[name=sharePerson]').val();
      if (sharePerson != '') {
        shareFileArr = '<li>' + fileName + '[' + sharePerson + ']' + '</li>' + shareFileArr;
        fileIdArr = '<input type=hidden name=fileId[' + i + '] value=' + fileId + ' />' + fileIdArr;
      }
    }

    if (shareFileArr != '') {
      html=html + '以下のファイルは共有が解除されます。';
      html=html + '<ul style="color:#bb9944;">';
      html=html + shareFileArr;
      html=html + '</ul>';
      html=html + fileIdArr;
      targetDiv.children('.modal-footer').show();
    } else {
      html=html + '<span class="help-inline error">共有しているファイルを選んでください。</span>';
      targetDiv.children('.modal-footer').hide();
    }
    targetDiv.children('.modal-body').html(html);
  });

  // 全チェック
  $('#checkbox-shr-all').click(function(){
    allCheck('#shr-table', this);
  });

  // 全チェック
  $('#checkbox-all').click(function(){
    allCheck('#file-table', this);
  });

  $('#shr-table').DataTable({
      pageLength: 4,
      columnDefs: [
        { orderable: false, targets: 0 }
      ],
      autoWidth: false,
      lengthChange: false,
      language: l
    });


    $('#file-table').DataTable({
      order: [3, 'desc'],
      columnDefs: [
        { orderable: false, targets: 0 }
      ],
      lengthMenu: [ [10, 20, 30, 40, 50, -1], [10, 20, 30, 40, 50, 'ALL'] ],
      autoWidth: false,
      language: l
    });

  $('[data-toggle="popover"]').popover();

  var errorTxt = $('#modal-errors .modal-dialog .modal-content .modal-body .errors').text();
  if (errorTxt != '') {
    $('#modal-errors [data-target="#modal-errors"]').click();
  }

});