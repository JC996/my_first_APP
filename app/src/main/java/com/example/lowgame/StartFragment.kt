package com.example.lowgame

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton

class StartFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var soundSwitch : MaterialButton
    private lateinit var startButton: MaterialButton
    private lateinit var titleView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_start, container, false)

        mediaPlayer = MediaPlayer.create(context, R.raw.bgm)
        mediaPlayer.isLooping = true

        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        soundSwitch = root.findViewById(R.id.soundSwitch)
        startButton = root.findViewById(R.id.startButton)
        titleView = root.findViewById(R.id.titleView)

        soundSwitch.setOnClickListener {
            if (viewModel.soundswitch.value!!){
                (it as MaterialButton).setIconResource(R.drawable.ic_volume_off)
                mediaPlayer.stop()
                mediaPlayer.prepare()
                AutoReleasePlayer(requireContext(),R.raw.click)
            }
            else{
                (it as MaterialButton).setIconResource(R.drawable.ic_volume_on)
                mediaPlayer.start()
            }
            viewModel.soundSwitch()
        }

        startButton.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.scale_anim))
            viewModel.startButtonClicked()
            if(viewModel.soundswitch.value!!)
                AutoReleasePlayer(requireContext(),R.raw.click)
        }

        return root
    }

    override fun onStart() {
        super.onStart()
        titleView.startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.fade_in))
    }

    override fun onPause() {
        super.onPause()
        if(viewModel.soundswitch.value !!)
            mediaPlayer.pause()
    }

    override fun onResume() {
        super.onResume()
        if(viewModel.soundswitch.value !!)
            mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

}