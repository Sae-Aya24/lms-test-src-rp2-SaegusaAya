  $(function(){

    $('input[name^=amountArr]').attr('type', 'number');

    var pattern = /^([1-9]\d*|0)$/;
    var pricePattern = /^[+,-]?([1-9]\d*|0)$/;

    function check(price, amount, tax) {
      if (!pricePattern.test(price)) { return false;}
      if (!pattern.test(amount)) { return false;}
      if (!pattern.test(tax)) { return false;}
      return true;
    }


    function calc () {

      var tbodyElementArr = $('table[id^=estimate]').children('tbody');
      $(tbodyElementArr).each(function(i, tBodyElement){
        var priceSubtotal   = 0;
        var priceSummary = 0 ;
        var tax      = $(tBodyElement).parent().find('input[name^=tax]').val();

        var priceArr  = $(tBodyElement).find('input[name^=priceArr]');
        var amountArr = $(tBodyElement).find('input[name^=amountArr]');
        var taxincludedArr = $(tBodyElement).find('span[id^=taxincluded]');
        $(priceArr).each(function(j, priceElement){
            var price  = $(priceElement).val();
            var amount = $(amountArr.get(j)).val();

            var taxincluded = 0;
            if (check(price, amount, tax)) {
              price  = parseInt(price);
              amount = parseInt(amount);
              tax    = parseInt(tax);

              priceSubtotal += price * amount;
              taxincluded = Math.round(price + (price * tax * 0.01)) * amount;
              priceSummary  += taxincluded;
            }
            $(taxincludedArr.get(j)).text(taxincluded.toString().replace(/(\d)(?=(\d{3})+$)/g , '$1,'));

            var numPattern = /^[-]([1-9]\d*|0)$/;
            if (numPattern.test(amount)) {
              $(amountArr.get(j)).val('0');
            }
        });

        $(tBodyElement).parent().find('span[id^=subtotal]').text(priceSubtotal.toString().replace(/(\d)(?=(\d{3})+$)/g , '$1,'));
        $(tBodyElement).parent().find('span[id^=subsummary]').text(priceSummary.toString().replace(/(\d)(?=(\d{3})+$)/g , '$1,'));

      });
    }

    calc();
    $(document).on('keyup', 'input.calc',  function() {calc();});
    $(document).on('blur', 'input.calc',  function() {calc();});
    $(document).on('click', 'input.calc',  function() {calc();});

    $(document).on('click', 'input.calc',  function() {calc();});

    function getPrice(e) {
      var estimateProductId = $(e).val();
      $(e).parent().parent().find('input[name^=priceArr]').val(0);

      if (estimateProductId === '') {
        calc();
        return;
      }
      $.ajax({
          type: "POST",
          url: "/lms/estimate/product/detail/json",
          data: "estimateProductId=" + estimateProductId,
          success: function(estimateProductDto){

            $(e).parent().parent().find('input[name^=priceArr]').val(estimateProductDto.price);
            calc();

          },
          fail:function() {
              alert('通信に失敗しました。');
          }

      });
    }
    $(document).on('change', 'select.product',  function() {getPrice($(this));});


  });