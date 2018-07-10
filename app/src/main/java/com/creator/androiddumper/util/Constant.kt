package com.creator.androiddumper.util

class Constant private constructor() {

    companion object {
        //intent extra
        const val EXTRA_DETAIL_PACKAGE_NAME = "extra_detail_package_name"

        //notification
        const val CHANNEL_ID = "meminfo_quick_start"
        const val NOTIFICATION_ID = 8080

        //shared preferences
        const val PREFERENCES_NAME = "com_creator_androiddumper_preferences"
        const val KEY_CURRENT_SHELL_TEXT_SIZE = "key_current_text_size"
        const val KEY_CURRENT_AUTO_NEWLINE = "key_auto_newline"

        //configuration
        const val DEFAULT_SHELL_TEXT_SIZE = 15F
        const val MIN_SHELL_TEXT_SIZE = 5
        const val MAX_SHELL_TEXT_SIZE = 40

        //Log tag
        const val TAG_DEBUG = "Dumper_debug"
    }
}