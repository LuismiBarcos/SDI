package org.sdi.helper

import org.sdi.annotations.Inject

/**
 * @author Luis Miguel Barcos
 */
class Foo {
    private lateinit var name: String

    @Inject
    private lateinit var injectedString: String

    @Inject
    private lateinit var injectedList: List<String>


    fun getName() = name
}