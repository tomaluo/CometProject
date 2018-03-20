package com.mazing.com.cavasdemo.overscroll;

/**
 * Created by user on 2017/12/11.
 */

public interface IOverScrollStateListener {

    /**
     * The invoked callback.
     *
     * @param decor The associated over-scroll 'decorator'.
     * @param oldState The old over-scroll state; ID's specified by {@link IOverScrollState}, e.g.
     *                 {@link IOverScrollState#STATE_IDLE}.
     * @param newState The <b>new</b> over-scroll state; ID's specified by {@link IOverScrollState},
     *                 e.g. {@link IOverScrollState#STATE_IDLE}.
     */
    void onOverScrollStateChange(IOverScrollDecor decor, int oldState, int newState);
}
