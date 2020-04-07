$(function(){

    $("#img").on("click",function(){
        $("#imgDiv").empty();
        $(".file span").html("选择图片");
        formData.delete("img");
    })

    $("#img").on("change",function(){
        if(this.files.length <= 0){
            $("#imgDiv").empty();
            formData.delete("img");
            return false;
        }
        for(var i = 0; i < this.files.length; i++){
            if(this.files[i] != null){
                if(!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(this.files[0].name)){
                    this.value = "";
                    layer.msg("上传文件必须为图片类型！");
                    break;
                }
                $(".file span").html("共"+this.files.length+"个图片");
                var url = getImgUrl(this.files[i]);
                if(url){
                    formData.append("img",this.files[i]);
                    $("#imgDiv").append("<img class='dynamics_img' src='"+url+"'/>");
                }
            }
        }
    });
    var formData = new FormData();
    $("#submitDynamics").click(function(){
        if(!validate($("#message").val(),"动态内容",4,500))
            return false;
        var text = getFormatCode($("#message").val());
        var img = $("#img").files;
        formData.append("text",text);
        $.ajax({
            url:"/dynamics/submit",
            type:"POST",
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            dataType:"JSON",
            success:function(data){
                if(LayerMsg(data)){
                    $(".close-popup").click();
                }
            }
        });
    });


})