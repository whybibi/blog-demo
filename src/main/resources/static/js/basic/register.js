
/*切换验证码*/
function changeVerify(img){
    img.src = "/verify.jpg?" + Math.floor(Math.random()*100);
}

$(function(){
    $("#submit").click(function () {
        if(!validate($("#userName").val(),"用户名",1,25))
            return false;
        if(!validate($("#passWord").val(),"密码",6,15))
            return false;
        if(!comparePassWord($("#passWord").val(),$("#rPassWord").val()))
            return false;
        if(!validatePhone($("#phone").val()))
            return false;
        if(!validateEmail($("#email").val()))
            return false;
        if(!validate($("#verify").val(),"验证码",null,null))
            return false;
        var obj = {};
        obj.userName = $("#userName").val();
        obj.passWord = $("#passWord").val();
        obj.realName = $("#realName").val();
        obj.phone = $("#phone").val();
        obj.email = $("#email").val();
        obj.sex = $("#sex").val();
        obj.birthday = $("#birthday").val();
        obj.description = $("#description").val();
        var headImg = $("#headImg")[0].files[0];
        var verifyCode = $("#verify").val();
        var formData = new FormData();
        formData.append("headImg",headImg);
        formData.append("verifyCode",verifyCode);
        formData.append("userStr",JSON.stringify(obj));
        $.ajax({
            url:"/basic_user/register",
            type:"POST",
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            dataType:"JSON",
            success:function(data){
                if(LayerMsg(data)){
                    window.location.href = "/login.html";
                }
            }
        });
    });

    $("#cancel").click(function(){
        layer.confirm('不想注册了吗？', {
            closeBtn: 0,
            time: 20000, //20s后自动关闭
            btn: [ '再等等','退出'],
        },function(){
            layer.close(layer.index);
        },function(){
            window.history.go(-1);
        });
    });


    $("#headImg").on('change',function(){
        if(!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(this.files[0].name)){
            this.value = "";
            $("#headImgShow").hide();
            layer.msg("上传文件必须为图片类型！");
            return false;
        }
        var url = getImgUrl(this.files[0]);
        if(url){
            $("#headImgShow").attr("src",url);
            $("#headImgShow").show();
        }else{
            $("#headImgShow").attr("src","");
            $("#headImgShow").hide();
        }
    });


    
});