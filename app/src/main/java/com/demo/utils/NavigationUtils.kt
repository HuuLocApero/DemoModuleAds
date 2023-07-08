package com.demo.utils

import androidx.fragment.app.Fragment
import androidx.navigation.NavDestination
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController

object NavigationUtils {

    fun Fragment.safeNavigateAction(action: NavDirections) {
        val navDirections: NavDestination? = findNavController().currentDestination
        val className = (navDirections as? FragmentNavigator.Destination)?.className
        if (this::class.java.name.equals(className, true)) {
            findNavController().navigate(action)
        }
    }

    fun Fragment.safeNavigationUp() {
        val navDirections: NavDestination? = findNavController().currentDestination
        val className = (navDirections as? FragmentNavigator.Destination)?.className
        if (this::class.java.name.equals(className, true)) {
            findNavController().navigateUp()
        }
    }

    fun Fragment.safePopBackStack(id: Int, inclusive: Boolean, saveState: Boolean) {
        val navDirections: NavDestination? = findNavController().currentDestination
        val className = (navDirections as? FragmentNavigator.Destination)?.className
        if (this::class.java.name.equals(className, true)) {
            findNavController().popBackStack(id, inclusive = inclusive, saveState = saveState)
        }
    }
}