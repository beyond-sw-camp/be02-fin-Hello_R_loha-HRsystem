package com.HelloRolha.HR.error;



public class UserAccountException extends BusinessException {
    public UserAccountException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public static UserAccountException forTokenNotExists(String token) {
        return new UserAccountException(ErrorCode.TOKEN_NOT_EXISTS, String.format("Token [ %s ] is not exists.", token));
    }

    public static UserAccountException forInvalidToken(String token) {
        return new UserAccountException(ErrorCode.INVALID_VERIFICATION_TOKEN, String.format("Token [ %s ] is invalid.", token));
    }

    public static UserAccountException forInvalidPassword(String password) {
        return new UserAccountException(ErrorCode.DIFFERENT_USER_PASSWORD, String.format("User Password [ %s ] is different.", password));
    }

    public static UserAccountException forInvalidStatus(Boolean status) {
        return new UserAccountException(ErrorCode.USER_UNCERTIFIED, String.format("User CERTIFIED Status  is false", status));
    }
    public static UserAccountException forExpiredToken(String token) {
        return new UserAccountException(ErrorCode.EXPIRED_VERIFICATION_TOKEN, String.format("Verification Token [ %s ] is expired.", token));
    }
}
