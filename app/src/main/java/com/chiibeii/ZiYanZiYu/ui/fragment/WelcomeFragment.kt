package com.chiibeii.ZiYanZiYu.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.RenderProcessGoneDetail
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chiibeii.ZiYanZiYu.R

class WelcomeFragment:Fragment() {

    private lateinit var welcome_to_main: Button
    private lateinit var welcome_to_edit: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 这是这个容器里面默认显示的那个 fragment，跳转逻辑都写在这里面
        val view = inflater.inflate(R.layout.welcome,container,false)

        var isFirstUse = true

        welcome_to_edit = view.findViewById(R.id.welcome_to_edit)
        welcome_to_main = view.findViewById(R.id.welcome_to_main)

        // 利用navigation跳转
        welcome_to_edit.setOnClickListener{
            findNavController().navigate(R.id.action_welcome_to_login)
            isFirstUse = false
        }

        welcome_to_main.setOnClickListener{
            findNavController().navigate(R.id.action_welcome_to_main)
        }

        return view
    }

}