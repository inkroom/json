package cn.inkroom.json;

public enum Token {
    DOCUMENT_START,
    DOCUMENT_END,
    OBJECT_START,
    OBJECT_END,
    ARRAY_START,
    ARRAY_END,
    BOOLEAN,
    /**
     * 冒号
     */
    SEP_COLON,
    /**
     * 逗号
     */
    SEP_COMMA,
    /**
     * 引号，接下来是个字符串
     */
    TEXT,
    NUMBER,
    NULL,
    /**
     * 非法
     */
    ILL,

}
