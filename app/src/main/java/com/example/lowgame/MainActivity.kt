package com.example.lowgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerView,StartFragment(),"start")
            .commit()

        viewModel.startSign.observe(this){
            if (it){
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView,BossFragment(),"boss")
                    .commit()
            }
        }

        viewModel.endSign.observe(this){
            if(it > 0){
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView,FinishFragment(),"finish")
                    .commit()
            }
        }

        viewModel.restartFlag.observe(this){
            if(it){
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView,StartFragment(),"start")
                    .commit()
                viewModel.restartFlag.postValue(false)
            }
        }
    }
}
