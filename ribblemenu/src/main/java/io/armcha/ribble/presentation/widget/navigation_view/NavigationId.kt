package io.armcha.ribble.presentation.widget.navigation_view

import io.armcha.ribble.presentation.utils.extensions.emptyString

/**
 * Created by Chatikyan on 21.08.2017.
 */
sealed class NavigationId(val name: String = emptyString, val fullName: String = emptyString) {

    //For English
    object HOME : NavigationId("HOME")
    object LOCATION : NavigationId("LOCATION")
    object NOTIFICATION : NavigationId("NOTIFICATION")
    object PAYMENT_CARD : NavigationId("PAYMENT CARD")
    object ADD_RESTAURANT : NavigationId("ADD RESTAURANT")
    object MENU : NavigationId("MENU")
    object PROFILE : NavigationId("PROFILE")
    object LOG_OUT : NavigationId("LOG OUT")
}