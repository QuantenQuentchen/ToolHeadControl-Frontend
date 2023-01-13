package com.example.toolheadadjust

import android.media.Image
import java.sql.Timestamp

data class ToolCfg(
    var name: String,
    var description: String,
    var xCord: Int = 0,
    var yCord: Int = 0,
    var lastAccess: Timestamp = Timestamp(System.currentTimeMillis()),
    var id: Int = 0,
    var imageSrc: Image? = null,
)