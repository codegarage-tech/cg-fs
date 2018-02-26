package com.reversecoder.gcm.listener;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public interface GcmResultListener<T> {
    public void onGcmResult(T result);
}