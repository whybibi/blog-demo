
/*移动端主页内容流加载，每次10条*/
layui.use(['flow','layer'], function(){
    var $ = layui.jquery;
    var flow = layui.flow;
    var layer = layui.layer;

    flow.load({
        elem: '#facebook_content' //指定列表容器
        ,isAuto: true //下滑到底自动加载
        ,end: '啊偶，没有更多内容了w(゜Д゜)w'
        ,done: function(page, next){ //到达临界点（默认滚动触发），触发下一页
            var lis = [];
            //以jQuery的Ajax请求为例，请求下一页数据（注意：page是从2开始返回）
            $.get('/dynamics/flow?page='+page, function(res){
                //假设你的列表返回在data集合中
                layui.each(res.data, function(index, item){
                    lis.push(
                        '<div class="card facebook-card">'+
                        '<div class="card-header no-border">'+
                        '<div class="facebook-avatar"><img src="'+item.user.headImg+'" width="34" height="34"></div>'+
                        '<div class="facebook-name">'+item.user.userName+'</div>'+
                        '<div class="facebook-date">'+Format(item.dynamics.createDate,"yyyy-MM-dd hh:mm")+'</div>'+
                        '</div>'+
                        '<div class="card-content">'+
                        '<div class="card-content-inner">'+item.dynamics.dynamicsMsg+'</div>'
                    );
                    var imgs = '';
                    layui.each(item.dynamicsImgList, function(i,img){
                        imgs+='<a href="javascript:void(0);"><img src="'+img.dynamicsImgUrl+'" width="100%"></a>'
                    });
                    lis.push(imgs);
                    lis.push(
                        '</div>'+
                        '<div class="card-footer no-border" dynamic-id="'+item.dynamics.id+'">'+
                        '<a class="link layui-icon layui-icon-praise" onclick="good(this);">赞</a>'+
                        '<a href="javascript:;" class="open-popup layui-icon layui-icon-reply-fill" data-popup=".popup-services" onclick="comment(this);">评论</a>'+
                        '<a class="link layui-icon layui-icon-share">分享</a>'+
                        '</div>'+
                        '</div>');
                });

                //执行下一页渲染，第二参数为：满足“加载更多”的条件，即后面仍有分页
                //pages为Ajax返回的总页数，只有当前页小于总页数的情况下，才会继续出现加载更多
                next(lis.join(''), page < res.pages);
            });
        }
    });


    $("#submit_comment").click(function () {
        var dynamicsId = $("#comment_dynamic_id").val();
        var commentMsg = $("#comment_msg").val();
        if(commentMsg.trim() == ''){
            layer.msg("请输入评论内容");
            return false;
        }
        if(dynamicsId){
            $.ajax({
                url:'/comment/submit',
                type:'post',
                data:{dynamicsId:dynamicsId,commentMsg:commentMsg},
                dataType:'json',
                success:function (data) {
                    if(layerTip(data)){}
                },
                error:function () {
                    layer.msg('评论失败');
                }
            });
        }else{
            layer.msg("获取动态ID失败，无法提交评论");
        }
    });


});

/*老版本VUE加载（已弃用但保留）*/
function loadIndexDynamics(){
    $.post("/dynamics/load",function(data){
        if(data.success){
            console.log(data);
            var vue = new Vue({
                el:"#facebook_content",
                data:{dynamicsList : data.dynamicsList}
            });
        }
    });
}
$(function(){
    function loadCurrentUser(){
        $.post("/basic_user/current_user",function(data){
            if(data.success){
                    var vue = new Vue({
                            el:"#panel-left",
                            data:{user:data.user}
                        }
                    );
                    var vue2 = new Vue({
                            el:"#panel-top",
                            data:{user:data.user}
                        }
                    );
            }
        });
    }

});


    /*点赞方法*/
    function good(obj) {
        var dynamic_id = $(obj).parent().attr("dynamic-id");
        if(dynamic_id){
            $.ajax({
               url:'/',
               type:'post',
               dataType:'json',
               success:function (data) {
                    obj.style.color = "red";
               },
               error:function () {
                    console.log("点赞失败，连接异常");
               }
            });
            return;
        }
        console.log($(obj).parent().attr("dynamic-id"));
        console.log("无法获取动态ID");
    }

    /*弹出评论下拉框*/
    function comment(obj) {
        var dynamic_id = $(obj).parent().attr("dynamic-id");
        $("#comment_msg").val('');
        $("#comment_dynamic_id").val('');
        if(dynamic_id){
            $("#comment_dynamic_id").val(dynamic_id);
            $.ajax({
                url:"/comment/get_comment_by_dynamic_id",
                type:'post',
                data:{dynamicId:dynamic_id},
                dataType:'json',
                success:function (data) {
                    if(data == '' || data.length <= 0){
                        $("#comment_popup").empty();
                        $("#comment_popup").append("<p class='comment_login_p'>暂无评论</p>");
                    }else{
                        $("#comment_popup").empty();
                        $.each(data,function (index,item) {
                            $("#comment_popup").append("<p><img style='width: 2rem;height: 2rem;display: inline-block' src='"+item.user.headImg+"'></img> "+item.user.userName+"</p>");
                            $("#comment_popup").append("<p>评论: "+item.commentMsg+"</p>");
                        });
                    }
                },
                error:function () {
                    $("#comment_popup").empty();
                    $("#comment_popup").append("<p class='comment_login_p'>请<a href='/login' style='color: deepskyblue'>登录</a>后查看或发表评论</p>");
                }
            });
        }
    }




