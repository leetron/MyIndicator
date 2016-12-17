package com.luclx.lxindicator;

/**
 * Created by LucLX on 12/17/16.
 */

public class PageLessException extends Exception {
    @Override
    public String getMessage() {
        return "Count page must equal or larger then 2";
    }
}
