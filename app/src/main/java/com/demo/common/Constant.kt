package com.demo.common

object Constant {
    const val KEY_START_FROM_LANGUAGE = "KEY_START_FROM_LANGUAGE"

    const val CODENAME_LANGUAGE_ENGLISH = "en"

    const val TIME_OUT_API = 60L


    // language code
    const val LANGUAGE_ES = "es"
    const val LANGUAGE_KO = "ko"
    const val LANGUAGE_PT = "pt"
    const val LANGUAGE_EN = "en"
    const val LANGUAGE_INDO = "id"
    const val LANGUAGE_HINDI = "hi"

    val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100
    val PERMISSIONS_REQUEST_CAMERA = 101

    // Photo
    val MAXIMUM_OUTPUT_IMAGE_AI_SIZE = 512
    val AI_TEMPLATE_PHOTO_FOLDER_NAME = "ai_template_photo"
    val AI_GENERATED_PHOTO_FOLDER_NAME = "ai_generated_photo"
    val AI_GENERATED_PHOTO_FOLDER = "AI Generate"

    enum class StatusGenerate {
        LOADING, NONE, FAIL, SUCCESS, SHOWN
    }
}