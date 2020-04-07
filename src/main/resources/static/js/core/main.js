
/*$.notify({
	icon: 'la la-bell',
	title: '页面加载提示',
	message: '暂无新消息',
},{
	type: 'success',
	placement: {
		from: "top",
		align: "right"
	},
	time: 1000,
});*/


/*格式化时间*/
function Format(datetime,fmt) {
    if (parseInt(datetime)==datetime) {
        if (datetime.length==10) {
            datetime=parseInt(datetime)*1000;
        } else if(datetime.length==13) {
            datetime=parseInt(datetime);
        }
    }
    datetime=new Date(datetime);
    var o = {
        "M+" : datetime.getMonth()+1,                 //月份
        "d+" : datetime.getDate(),                    //日
        "h+" : datetime.getHours(),                   //小时
        "m+" : datetime.getMinutes(),                 //分
        "s+" : datetime.getSeconds(),                 //秒
        "q+" : Math.floor((datetime.getMonth()+3)/3), //季度
        "S"  : datetime.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (datetime.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}

/*点击头像弹出大图*/
function showImg(obj){
    layer.open({
        type: 1,
        title: false,
        closeBtn: 0,
        area: 'auto',
        skin: 'layui-layer-nobg', //没有背景色
        shadeClose: true,
        content: '<img src="'+ $(obj).attr("src") +'"/>'
    });
}

/*返回数据弹窗并返回boolean值*/
function layerTip(data){
    if(data.code==0){
        if(data.data != null && data.data != ''){
            layer.msg(data.data, {icon: 1});
        }else{
            layer.msg(data.msg, {icon: 1});
        }
        return true;
    }else{
        if(data.data != null && data.data != ''){
            layer.msg(data.data);
        }else{
            layer.msg(data.msg);
        }
        return false;
    }
}

/*显示Tip*/
function showTip(text,id){
    layer.tips(text, '#'+id, {
        tips: 3
    });
}

/*获取上传图片的路径*/
function getImgUrl(file){
    var url = null;
    try{
        if(window.createObjectURL != undefined){// 通用
            url = window.createObjectURL(file);
        }else if(window.URL != undefined){ //火狐 谷歌
            url = window.URL.createObjectURL(file);
        }else if(window.webkitURL != undefined){ // webkit
            url = window.webkitURL.createObjectURL(file);
        }
    }catch(e){
        return url;
    }
    return url;
}