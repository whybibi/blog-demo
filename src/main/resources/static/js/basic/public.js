
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


/*把回车转换成html字符*/
function getFormatCode(strValue) {
    return strValue.replace(/\r\n/g, '<br/>').replace(/\n/g, '<br/>').replace(/\s/g, ' ');
}
/*把html字符转换成系统换行*/
function decryptCode(strValue) {
    return strValue.replace(/<br\s*\/?>/ig, '\r\n').replace(/<br\s*\/?>/ig, '\n').replace(/\ \;/g, ' ');
}


/*打开一个layer消息弹窗*/
function LayerOpen(msg){
    layer.open({
        type: 1,
        area: ['70%', '25%'],
        shadeClose: true, //点击遮罩关闭
        content: '\<\div style="padding:20px;">'+msg+'\<\/div>'
    });
}
