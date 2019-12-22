package com.llipter.sigdict;

public class ErrorMessage {

    public static final String SIGH_IN_FIRST = "PLEASE SIGN IN FIRST";

    public static final String LOGOUT_FIRST = "PLEASE LOG OUT FIRST";

    public static final String ALREADY_SIGNED_IN = "YOU HAVE ALREADY SIGNED IN";

    public static final String NOT_SIGN_IN_YET = "YOU HAVE NOT SIGNED IN YET";

    public static final String USER_NOT_EXISTED = "USER NOT EXISTED";

    public static final String USERNAME_INVALID = "USERNAME INVALID";

    public static final String USERNAME_DUPLICATE = "USERNAME DUPLICATE";

    public static final String PASSWORD_INCORRECT = "PASSWORD INCORRECT";

    public static final String PASSWORD_INVALID = "PASSWORD INVALID";

    public static final String PASSWORD_MISMATCHED = "PASSWORD MISMATCHED";

    public static final String EMAIL_INVALID = "EMAIL INVALID";

    public static final String MAX_FILE_SIZE_EXCEEDED = "FILE MUST BE LESS THAN 10 MB";

    public static final String FILE_NOT_SELECT = "PLEASE SELECT FILE FIRST";

    public static final String FILENAME_INVALID = "FILENAME INVALID";

    public static final String FILE_EXTENSION_INVALID = "FILE EXTENSION IS INVALID";

    public static final String SIGNATURE_ALGORITHM_INVALID = "SIGNATURE ALGORITHM INVALID";

    public static final String FILE_IDENTIFIER_INVALID = "FILE IDENTIFIER IS NOT VALID";

    public static final String FILE_NOT_FOUND = "FILE NOT FOUND";

    public static final String CANNOT_SAVE_FILE = "CANNOT SAVE FILE";

    public static final String CANNOT_DELETE_FILE = "CANNOT DELETE FILE";

    public static final String CANNOT_ENCODE = "CANNOT ENCODE";

    public static final String CANNOT_SEND_EMAIL = "CANNOT SEND EMAIL";

    public static final String EMAIL_ALREADY_VERIFIED = "EMAIL ALREADY VERIFIED";

    public static final String EMAIL_SENT = "A LINK HAS BEEN SENT TO YOUR EMAIL ADDRESS, " +
            "PLEASE CLICK THE LINK TO VERIFY YOUR EMAIL ADDRESS.";

    public static final String TOKEN_INVALID = "TOKEN INVALID";

    public static final String LINK_EXPIRED = "LINK EXPIRED";

    public static final String LINK_EXPIRED_MESSAGE = "Sorry, your link is expired. " +
            "Please regenerate the link through our website";

    public static final String BAD_REQUEST = "Sorry, your request is invalid. " +
            "Missing or incorrect mandatory parameter is one possible reason. " +
            "Please do not use any third-party tools when visiting our website.";

    public static final String NOT_FOUND = "Sorry, we cannot find the resource you asked. " +
            "Please double check your URL.";

    public static final String METHOD_NOT_ALLOWED = "Sorry, the method in your request is not allowed. " +
            "Please do not use any third-party tools when visiting our website.";

    public static final String INTERNAL_SERVER_ERROR = "Sorry, there's something wrong in our side. " +
            "Please be patient, we're working on it.";

    public static final String UNKNOWN_ERROR = "Sorry there's an unexpected error. " +
            "Please retry or report this issue to us.";

    public static final String RESET_LINK_SENT = "A link has benn sent to your email address. " +
            "Please click the link to reset your password";

    public static final String EMAIL_CHANGED = "YOUR EMAIL ADDRESS IS CHANGED";

}
