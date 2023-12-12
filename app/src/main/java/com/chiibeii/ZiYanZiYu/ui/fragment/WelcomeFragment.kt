package com.chiibeii.ZiYanZiYu.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chiibeii.ZiYanZiYu.R

class WelcomeFragment : Fragment() {

    private lateinit var welcomeToMain: Button
    private lateinit var welcomeToEdit: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // 这是这个容器里面默认显示的那个 fragment，跳转逻辑都写在这里面
        val view = inflater.inflate(R.layout.welcome, container, false)

        var isFirstUse = true

        welcomeToEdit = view.findViewById(R.id.welcome_to_edit)
        welcomeToMain = view.findViewById(R.id.welcome_to_main)

        // 利用navigation跳转
        welcomeToEdit.setOnClickListener {
            findNavController().navigate(R.id.action_welcome_to_login)
            isFirstUse = false
        }

        welcomeToMain.setOnClickListener {
            findNavController().navigate(R.id.action_welcome_to_main)
        }

        return view
    }

}