package com.demo.di.module

class LanguageModule {
    var code: String = ""
    var name: String = ""
    var isChoose = false

    constructor() {}
    constructor(code: String, name: String, isChoose: Boolean) {
        this.name = name
        this.code = code
        this.isChoose = isChoose
    }

    override fun toString(): String {
        return "LanguageModule(code=$code, name=$name, isChoose=$isChoose)"
    }
}