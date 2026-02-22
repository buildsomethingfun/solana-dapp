package dapp.buildsomething.common.util

fun Float.format(digits: Int) = "%.${digits}f".format(this)