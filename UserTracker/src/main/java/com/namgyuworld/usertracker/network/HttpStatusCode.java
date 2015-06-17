package com.namgyuworld.usertracker.network;

/**
 * The message of Http status code <br>
 * 200 : OK <br>
 * 400 : Bad request <br>
 * ... etc
 * <br><br>
 * Created by danielpark on 6/17/15.
 */
public class HttpStatusCode {

    private static final int SC_ACCEPTED = 202;
    private static final int SC_BAD_GATEWAY = 502;
    private static final int SC_BAD_REQUEST = 400;
    private static final int SC_CONFLICT = 409;
    private static final int SC_CONTINUE = 100;
    private static final int SC_CREATED = 201;
    private static final int SC_EXPECTATION_FAILED = 417;
    private static final int SC_FAILED_DEPENDENCY = 424;
    private static final int SC_FORBIDDEN = 403;
    private static final int SC_GATEWAY_TIMEOUT = 504;
    private static final int SC_GONE = 410;
    private static final int SC_HTTP_VERSION_NOT_SUPPORTED = 505;
    private static final int SC_INSUFFICIENT_SPACE_ON_RESOURCE = 419;
    private static final int SC_INSUFFICIENT_STORAGE = 507;
    private static final int SC_INTERNAL_SERVER_ERROR = 500;
    private static final int SC_LENGTH_REQUIRED = 411;
    private static final int SC_LOCKED = 423;
    private static final int SC_METHOD_FAILURE = 420;
    private static final int SC_METHOD_NOT_ALLOWED = 405;
    private static final int SC_MOVED_PERMANENTLY = 301;
    private static final int SC_MOVED_TEMPORARILY = 302;
    private static final int SC_MULTIPLE_CHOICES = 300;
    private static final int SC_MULTI_STATUS = 207;
    private static final int SC_NON_AUTHORITATIVE_INFORMATION = 203;
    private static final int SC_NOT_ACCEPTABLE = 406;
    private static final int SC_NOT_FOUND = 404;
    private static final int SC_NOT_IMPLEMENTED = 501;
    private static final int SC_NOT_MODIFIED = 304;
    private static final int SC_NO_CONTENT = 204;
    private static final int SC_OK = 200;
    private static final int SC_PARTIAL_CONTENT = 206;
    private static final int SC_PAYMENT_REQUIRED = 402;
    private static final int SC_PRECONDITION_FAILED = 412;
    private static final int SC_PROCESSING = 102;
    private static final int SC_PROXY_AUTHENTICATION_REQUIRED = 407;
    private static final int SC_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    private static final int SC_REQUEST_TIMEOUT = 408;
    private static final int SC_REQUEST_TOO_LONG = 413;
    private static final int SC_REQUEST_URI_TOO_LONG = 414;
    private static final int SC_RESET_CONTENT = 205;
    private static final int SC_SEE_OTHER = 303;
    private static final int SC_SERVICE_UNAVAILABLE = 503;
    private static final int SC_SWITCHING_PROTOCOLS = 101;
    private static final int SC_TEMPORARY_REDIRECT = 307;
    private static final int SC_UNAUTHORIZED = 401;
    private static final int SC_UNPROCESSABLE_ENTITY = 422;
    private static final int SC_UNSUPPORTED_MEDIA_TYPE = 415;
    private static final int SC_USE_PROXY = 305;

    /**
     * Get message of HTTP Status code
     *
     * @param code HTTP status code
     * @return message of HTTP status code
     */
    public static final String getMessage(int code) {
        switch (code) {
            // 400
            case HttpStatusCode.SC_BAD_REQUEST:
                return "서버가 요청의 구문을 이해하지 못했습니다.";
            // 401
            case HttpStatusCode.SC_UNAUTHORIZED:
                return "인증이 필요한 요청입니다.";
            // 403
            case HttpStatusCode.SC_FORBIDDEN:
                return "서버가 요청을 거부하였습니다.";
            // 404
            case HttpStatusCode.SC_NOT_FOUND:
                return "서버로 요청한 명령이 없습니다.";
            case HttpStatusCode.SC_METHOD_NOT_ALLOWED:
                return "The method specified in the Request-Line is not allowed for the resource identified by the Request-URI. The response MUST include an Allow header containing a list of valid methods for the requested resource.";
            // 408
            case HttpStatusCode.SC_REQUEST_TIMEOUT:
                return "요청 대기시간이 초과되었습니다.";
            // 413
            case HttpStatusCode.SC_REQUEST_TOO_LONG:
                return "요청이 너무 커서 서버가 처리할 수 없었습니다.";
            // 414
            case HttpStatusCode.SC_REQUEST_URI_TOO_LONG:
                return "요청이 너무 길어 서버가 처리할 수 없었습니다.";
            // 500
            case HttpStatusCode.SC_INTERNAL_SERVER_ERROR:
                return "서버에 오류가 발생하여 요청을 수행할 수 없었습니다.";
            // 501
            case HttpStatusCode.SC_NOT_IMPLEMENTED:
                return "서버에 요청을 수행할 수 있는 기능이 없습니다.";
            // 502
            case HttpStatusCode.SC_BAD_GATEWAY:
                return "서버가 게이트웨이나 프록시 역할을 하고 있거나 또는 업스트림 서버에서 잘못된 응답을 받았습니다.";
            // 503
            case HttpStatusCode.SC_SERVICE_UNAVAILABLE:
                return "서버가 오버로드되었거나 유지관리를 위해 다운되었기 때문에 현재 일시적으로 서버를 사용할 수 없습니다.";
            // 504
            case HttpStatusCode.SC_GATEWAY_TIMEOUT:
                return "서버가 게이트웨이나 프록시 역할을 하고 있거나 또는 업스트림 서버에서 제때 요청을 받지 못했습니다.";
            // 505
            case HttpStatusCode.SC_HTTP_VERSION_NOT_SUPPORTED:
                return "서버가 요청에 사용된 HTTP 프로토콜 버전을 지원하지 않습니다.";
            // 507
            case HttpStatusCode.SC_INSUFFICIENT_STORAGE:
                return "서버 용량이 부족합니다. 관리자에게 문의하세요.";
            // etc
            default:
                return "기타 알 수 없는 이유로 인해 요청을 처리하지 못했습니다.";
        }
    }

}
