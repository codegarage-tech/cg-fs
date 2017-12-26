package io.armcha.ribble.presentation.widget.navigation_view

import io.armcha.ribble.presentation.utils.extensions.emptyString

/**
 * Created by Chatikyan on 21.08.2017.
 */
sealed class NavigationId(val name: String = emptyString, val fullName: String = emptyString) {

    //For English
    object HOME : NavigationId("HOME")

    object ACCOUNT : NavigationId("ACCOUNT")
    object FAQ : NavigationId("FAQ")
    object PRIVACY_POLICY : NavigationId("PRIVACY POLICY")
}