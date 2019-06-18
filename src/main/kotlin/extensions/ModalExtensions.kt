package com.necroworld.extensions

import org.hexworks.zircon.api.component.modal.ModalResult
import org.hexworks.zircon.api.data.impl.Position3D

class TargetPickerModalResult(var hasSelectedTarget: Boolean, val targets: List<Position3D>) : ModalResult
