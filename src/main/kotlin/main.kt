package com.necroworld

import com.necroworld.view.StartView
import org.hexworks.zircon.api.SwingApplications

fun main(args: Array<String>) {

    SwingApplications.startApplication(GameConfig.buildAppConfig()).dock(StartView())

}

