package com.necroworld.attributes

import org.hexworks.amethyst.api.Attribute

data class DecisionMapUser(val weightedTags: HashMap<String, Int>) : Attribute