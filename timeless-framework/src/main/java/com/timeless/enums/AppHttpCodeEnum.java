package com.timeless.enums;

/**
 * @author timeless
 * @create 2022-12-05 14:56
 */
public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200, "操作成功"),
    // 登录
    NEED_LOGIN(401, "需要登录后操作"),
    NO_OPERATOR_AUTH(403, "无权限操作"),
    SYSTEM_ERROR(500, "出现错误"),
    USERNAME_EXIST(501, "用户名已存在"),

    PHONENUMBER_EXIST(502, "手机号已存在"),
    EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505, "用户名或密码错误"),
    CONTENT_CANNOT_BE_NULL(506, "评论内容不能为空"),
    FILE_TYPE_ERROR(507, "文件类型错误，请上传png图片"),
    USERNAME_NOT_NULL(508, "用户名不能为空"),
    PASSWORD_NOT_NULL(509, "密码不能为空"),
    EMAIL_NOT_NULL(510, "邮箱不能为空"),
    NICKNAME_NOT_NULL(511, "昵称不能为空"),
    NICKNAME_EXIST(512, "昵称已存在"),
    NOT_COMMENT(513, "该文章不允许评论"),
    SON_CANNOT_BE_FATHER(514, "修改菜单失败,上级菜单不能选择自己"),
    HAVE_SON_MENU(515, "存在子菜单，不允许删除"),
    REQUIRE_EMAIL(516, "必需填写QQ邮箱"),
    EMAIL_IS_NOT_EXIST(517, "该邮箱不存在"),
    CODE_IS_EXPIRED(518, "验证码已失效，请重新发送"),
    CODE_ERROR(519, "验证码错误"),
    CODE_SEND_ERROR(520, "验证码发送错误"),
    UPLOAD_FAIL(521, "上传图片失败");
    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage) {
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
