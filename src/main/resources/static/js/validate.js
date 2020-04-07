
/**
 * 输入值验证
 * @param value 获取到的值
 * @param attr 提示名
 * @param min   最小长度
 * @param max   最大长度
 * @returns {boolean}
 */
function validate(value, attr, min, max){
    if(value == null || value == ''){
        layer.msg(attr+"不能为空！");
        return false;
    }
    if(min != null){
        if(value.length < min){
            layer.msg(attr+"最小长度为"+min);
            return false;
        }
    }
    if(max != null){
        if(value.length > max){
            layer.msg(attr+"最大长度为"+max);
            return false;
        }
    }
    return true;
}

/*密码是否相等比较*/
function comparePassWord(p1, p2){
    if(p1 != p2){
        layer.msg("确认密码不正确！");
        return false;
    }
    return true;
}


function validateEmail(email) {
    //验证邮箱正则
    var re = /^(([^()[\]\\.,;:\s@\"]+(\.[^()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    if(!re.test(email)){
        layer.msg("邮箱格式不正确！");
        return false;
    }
    return true;
}

function validatePhone(phone) {
    var reg=/^[1][3,4,5,6,7,8,9][0-9]{9}$/;
    if (!reg.test(phone)) {
        layer.msg("手机号码不正确！请输入11位中国手机号码！");
        return false;
    }
    return true;
}


function LayerMsg(data){
    if(data.code == 0){
        layer.msg(data.data);
        return true;
    }else{
        layer.msg(data.msg);
        return false;
    }

}
