package cn.why.blog.basic.entity.enums;

import com.baomidou.mybatisplus.extension.api.IErrorCode;

public enum SimpleError implements IErrorCode {

    /**
     * 通用Code
     */
    ERROR(1L, "失败"),
    NULL_POINT_ERROR(2L, "传入数据为空！"),
    EXIST_NAME_ERROR(3L, "数据库中存在相同名称！"),
    ADMIN_ERROR(4L, "不能操作超级管理员"),
    /**
     * 增删改查
     */
    OPERATION_ERROR(1000L, "操作失败！"),
    SAVE_ERROR(1001L, "保存失败！"),
    DELETE_ERROR(1002L, "删除失败！"),
    DELETE_BACK(10021L, "不能删除管理员！"),
    QUERY_ERROR(1003L, "查询失败！"),
    UPLOAD_ERROR(1004L, "上传失败！"),
    /**
     * 登陆异常
     */
    INVALID_KAPTCHA(551L,"验证码不正确"),
    USERNAME_PASSWORD_NOT_EQUALS(552L,"用户名或密码错误"),
    USER_IS_ENABLE(553L,"账号已锁定"),
    NULL_USER(554L,"用户不存在"),
    USER_IS_EXIST(556L,"用户名已存在"),
    PASSWORD_NOT_EQUALS(555L,"密码错误"),
    PASSWORD_RECOVER_FAIL(5551L,"重置密码失败！"),


    NO_PERMISSION(556L,"没有权限！"),
    ROLE_NO_FIT(5561L,"角色与当前登陆用户不相符！"),
    VERIFY_ERR(557L,"验证码错误！"),
    JSON_TO_BEAN_ERR(558L,"实体转换异常！"),
    SAVE_HEAD_IMG_ERR(558L,"头像保存失败!"),
    SAVE_DYNAMICS_IMG_ERR(559L,"动态图片保存失败!"),
    DYNAMICS_INFO_EMPTY(560L,"动态文字不能为空!"),
    COMMENT_ERROR(561L,"评论失败!");

    private long  code;
    private String msg;

    SimpleError(final long code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
