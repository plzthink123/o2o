/**
 1.将前台的信息获取到
 2.提交表单
 */
$(function () {
    var initUrl="/o2o/shop/getshopinitinfo";
    var registerShopUrl="/o2o/shop/registershop";
    getshopinitinfo();
    alert(initUrl);
    function getshopinitinfo(){
        $getJSON(initUrl,function (data) {
            if(data.success){
                var tempHtml="";
                var tempAreaHtml="";
                data.shopCategoryList.map(function(item,index){
                    tempHtml+='<option data-id="'+item.shopCategoryId+'">'
                        +item.shopCategoryName+'</option>';
                });
                data.areaList.map(function (item,index) {
                    tempAreaHtml+='<option data-id="'+item.areaId+'">'
                        +item.areaName+'</option>';
                });
                $('#shop-category').html(tempHtml);
                $('#area').html(tempAreaHtml);
            }
        })
    $('#submit') .click(function(){
        var shop={};
        shop.shopName=$('#shop-name').val();
        shop.shopAddr=$('#shop-addr').val();
        shop.phone=$('#shop-phone').val();
        shop.shopDesc=$('#shop-desc').val();
        shop.shopCategory={ 
            shopCategoryId:$('#shop-category').find('option').not(function () {
                return !this.selected;
            }).data('id')
        };
        shop.area={
            areaId:$('#area').find('option').not(function () {
                return !this.selected;
            }).data('id')
        }
        var shopImg=$('#shop-img')[0].files[0];
        var formData=new formData();
        formData.append('shopImg',shopImg);
        formData.append('shopStr',JSON.stringify(shop));
        $.ajax({
            url:registerShopUrl,
            type:post,
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data){
                if(data.success){
                    $.toast('提交成功');
                }else{
                    $.toast('提交成功'+data.errMsg);

                }
            }
        })
    });
    }
    
})