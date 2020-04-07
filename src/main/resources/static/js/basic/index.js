
function comment(){
        layer.open({
            type: 2,
            title: '查看评论',
            maxmin: true,
            /*shadeClose: true, //点击遮罩关闭层*/
            area : ['90%' , '90%'],
            content: '/iFrame/comment'
        });
}

function showImg(obj){
    layer.open({
        type: 1,
        title: false,
        closeBtn: 0,
        area: '100%',
        skin: 'layui-layer-nobg', //没有背景色
        shadeClose: true,
        content: '<img width="100%" height="100%" src="'+$(obj).attr('src')+'">'
    });
}

