package com.example.lowgame

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

class FinishFragment : Fragment() {

    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var viewModel: MainViewModel
    private lateinit var finishView : TextView
    private lateinit var quitButton: Button
    private lateinit var restartButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_finish, container, false)

        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        finishView = root.findViewById(R.id.finishView)
        quitButton = root.findViewById(R.id.quitButton)
        restartButton = root.findViewById(R.id.restart)

        if(viewModel.endSign.value == 1){
            finishView.text = getString(R.string.victory)
            finishView.setTextColor(Color.parseColor("#F20000"))
        }
        else {
            finishView.text = getString(R.string.lose)
            finishView.setTextColor(Color.parseColor("#000000"))
        }

        quitButton.setOnClickListener {
            this.requireActivity().finish()
        }

        restartButton.setOnClickListener {
            viewModel.restart()
        }

        return root
    }

    override fun onStart() {
        super.onStart()
        finishView.startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.fade_in))

        if (viewModel.soundswitch.value!!){
            if(viewModel.endSign.value == 1){
                mediaPlayer = MediaPlayer.create(context, R.raw.victory_music)
                mediaPlayer.isLooping = true
            }
            else mediaPlayer = MediaPlayer.create(context, R.raw.failure_music)
            mediaPlayer.start()
        }

    }

    override fun onResume() {
        super.onResume()
        if(viewModel.soundswitch.value!!)
            mediaPlayer.start()
    }

    override fun onPause() {
        super.onPause()
        if(viewModel.soundswitch.value!!)
            mediaPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(viewModel.soundswitch.value!!)
            mediaPlayer.release()
    }

}