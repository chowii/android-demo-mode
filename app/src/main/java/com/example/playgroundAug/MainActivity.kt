package com.example.playgroundAug

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playgroundAug.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding.inflate(layoutInflater)
    }
}

