package org.sdi.injector

import java.lang.reflect.Field

/**
 *@author Luis Miguel Barcos
 */
internal data class PendingInjection(val instance: Any, val field: Field)
