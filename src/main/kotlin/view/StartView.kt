package com.necroworld.view

import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.extensions.onComponentEvent
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.mvc.base.BaseView
import org.hexworks.zircon.api.uievent.ComponentEventType
import org.hexworks.zircon.api.uievent.Processed

class StartView : BaseView() {

    override val theme = ColorThemes.arc()

    override fun onDock() {
        val msg = "Welcome to Necroworld."
        val header = Components.textBox() // a text box can hold headers, paragraphs and list items
            .withContentWidth(msg.length) // the width of the content of this text box
            .addHeader(msg) // we add a header
            .addNewLine() // and a new line
            .withAlignmentWithin(screen, ComponentAlignment.CENTER) // and align it to center
            .build() // finally we build the component
        val startButton = Components.button()
            // we align the button to the bottom center of our header
            .withAlignmentAround(header, ComponentAlignment.BOTTOM_CENTER)
            .withText("Start!") // its text is "Start!"
            .wrapSides(false) // we don't want to wrap this button with [ and ]
            .withBoxType(BoxType.SINGLE) // but we want a box around it
            .wrapWithShadow() // and some shadow
            .wrapWithBox()
            .build()

        startButton.onComponentEvent(ComponentEventType.ACTIVATED) {
            replaceWith(PlayView())
            close()
            Processed
        }

        screen.addComponent(header)
        screen.addComponent(startButton)
    }
}
