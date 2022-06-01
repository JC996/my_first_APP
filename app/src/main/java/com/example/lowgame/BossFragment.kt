package com.example.lowgame

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.util.*
import kotlin.concurrent.schedule
import kotlin.random.Random

class BossFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    private lateinit var dialog: ImageView
    private lateinit var textView: TextView

    private lateinit var heart1: ImageView
    private lateinit var heart2: ImageView
    private lateinit var heart3: ImageView

    private lateinit var boss: AnimationView
    private lateinit var fireBall: AnimationView
    private lateinit var character: AnimationView

    private lateinit var dice1: ImageButton
    private lateinit var dice2: ImageButton
    private lateinit var dice3: ImageButton
    private lateinit var dice4: ImageButton
    private lateinit var dice5: ImageButton
    private lateinit var dice6: ImageButton

    private lateinit var communication: Array<String>
    private var answer = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_boss, container, false)

        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        dialog = root.findViewById(R.id.dialog)
        textView = root.findViewById(R.id.textView)

        boss = root.findViewById(R.id.boss)
        fireBall = root.findViewById(R.id.fireBall)
        character = root.findViewById(R.id.character)

        heart1 = root.findViewById(R.id.heart1)
        heart2 = root.findViewById(R.id.heart2)
        heart3 = root.findViewById(R.id.heart3)

        dice1 = root.findViewById(R.id.dice1)
        dice2 = root.findViewById(R.id.dice2)
        dice3 = root.findViewById(R.id.dice3)
        dice4 = root.findViewById(R.id.dice4)
        dice5 = root.findViewById(R.id.dice5)
        dice6 = root.findViewById(R.id.dice6)

        (boss.drawable as AnimationDrawable).start()
        (character.drawable as AnimationDrawable).start()

        communication = requireContext().resources.getStringArray(R.array.communication)

        viewModel.textNumber.observe(viewLifecycleOwner) {
            if(it >= 0) textView.text = communication[it]
            else {
                if (it == -1)textView.text = "可恶，竟然被你猜到了！"
                else textView.text = "哈哈，你猜错了，刚才的是$answer"
            }
        }

        viewModel.dialogDirection.observe(viewLifecycleOwner) {
            if (it) dialog.setImageResource(R.drawable.dialog)
            else dialog.setImageResource(R.drawable.dialog_inverse)
        }

        viewModel.visibility.observe(viewLifecycleOwner) {
            dialog.visibility = when (it){
                true -> View.VISIBLE
                false -> View.INVISIBLE
            }
            textView.visibility = when (it){
                true -> View.VISIBLE
                false -> View.INVISIBLE
            }
        }

        viewModel.bossHealth.observe(viewLifecycleOwner) { it ->
            when(it){
                6 -> {
                    root.setBackgroundResource(R.drawable.battle_ground2_bmp)
                    root.startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.battle_change))
                }
                4 -> {
                    root.setBackgroundResource(R.drawable.battle_ground3_bmp)
                    root.startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.battle_change))
                }
                2 -> {
                    root.setBackgroundResource(R.drawable.battle_ground4_bmp)
                    root.startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.battle_change))
                }
                0 ->{
                    Timer().schedule(500) {
                        var bossFlag = false
                        var characterFlag = false

                        boss.setImageResource(R.drawable.dragon_death)
                        character.setImageResource(R.drawable.knight_jump)

                        sound(requireContext(),R.raw.victory_sound)

                        boss.setOnAnimationEndListener {
                            bossFlag = true
                            ((it as AnimationView).drawable as AnimationDrawable).stop()
                        }

                        character.setOnAnimationEndListener {
                            characterFlag = true
                            ((it as AnimationView).drawable as AnimationDrawable).stop()
                        }

                        (boss.drawable as AnimationDrawable).start()

                        Timer().schedule(500) {
                            (character.drawable as AnimationDrawable).start()
                        }

                        Timer().schedule(2000, 100) {
                            if (characterFlag && bossFlag) {
                                viewModel.textChange(0)
                                viewModel.dialogDirectionChange()
                                viewModel.visibilityChange()
                                Timer().schedule(3000){
                                    viewModel.endChange(1)
                                }
                                cancel()
                            }
                        }
                    }
                }
                else -> {}
            }
        }

        viewModel.characterHealth.observe(viewLifecycleOwner) {
            when(it){
                2 -> {
                    Timer().schedule(200) {
                        heart3.setImageResource(R.drawable.heart_void)
                    }
                }
                1 -> {
                    Timer().schedule(200) {
                        heart2.setImageResource(R.drawable.heart_void)
                    }
                }
                0 -> {
                    Timer().schedule(200) {

                        heart1.setImageResource(R.drawable.heart_void)

                        boss.setImageResource(R.drawable.dragon_idle)
                        character.setImageResource(R.drawable.knight_die)

                        sound(requireContext(),R.raw.failure_sound)

                        boss.setOnAnimationEndListener { }

                        character.setOnAnimationEndListener { he ->
                            ((he as AnimationView).drawable as AnimationDrawable).stop()
                            Timer().schedule(3000) {
                                viewModel.endChange(2)
                            }
                        }

                        (character.drawable as AnimationDrawable).start()
                        (boss.drawable as AnimationDrawable).start()
                    }
                }
                else -> {}
            }
        }

        dice1.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.scale_anim))
            sound(requireContext(), R.raw.click)
            if (answer == 1) bossHurt()
            else characterHurt()
        }

        dice2.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.scale_anim))
            sound(requireContext(), R.raw.click)
            if (answer == 2) bossHurt()
            else characterHurt()
        }

        dice3.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.scale_anim))
            sound(requireContext(), R.raw.click)
            if (answer == 3) bossHurt()
            else characterHurt()
        }

        dice4.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.scale_anim))
            sound(requireContext(), R.raw.click)
            if (answer == 4) bossHurt()
            else characterHurt()
        }

        dice5.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.scale_anim))
            sound(requireContext(), R.raw.click)
            if (answer == 5) bossHurt()
            else characterHurt()
        }

        dice6.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.scale_anim))
            sound(requireContext(), R.raw.click)
            if (answer == 6) bossHurt()
            else characterHurt()
        }

        diceClickable()

        return root
    }

    private fun diceClickable() {
        dice1.isClickable = dice1.isClickable.xor(true)
        dice2.isClickable = dice2.isClickable.xor(true)
        dice3.isClickable = dice3.isClickable.xor(true)
        dice4.isClickable = dice4.isClickable.xor(true)
        dice5.isClickable = dice5.isClickable.xor(true)
        dice6.isClickable = dice6.isClickable.xor(true)
    }

    private fun bossHurt() {
        diceClickable()

        var bossFlag = false
        var characterFlag = false

        (boss.drawable as AnimationDrawable).stop()
        boss.setImageResource(R.drawable.dragon_hurt)

        (character.drawable as AnimationDrawable).stop()
        character.setImageResource(R.drawable.knight_attack)

        boss.setOnAnimationEndListener {
            bossFlag = true
            ((it as AnimationView).drawable as AnimationDrawable).stop()
        }

        character.setOnAnimationEndListener {
            characterFlag = true
            ((it as AnimationView).drawable as AnimationDrawable).stop()
        }

        (character.drawable as AnimationDrawable).start()
        sound(requireContext(),R.raw.attack)

        Timer().schedule(1000){
            (boss.drawable as AnimationDrawable).start()
            sound(requireContext(),R.raw.dragon_hurt)
        }

        Timer().schedule(3000, 100) {
            if (characterFlag && bossFlag) {

                val  curHealth = viewModel.bossHealth.value!!.minus(1)

                viewModel.bossInjured()

                if(curHealth > 0) {

                    boss.setOnAnimationEndListener { }
                    boss.setImageResource(R.drawable.dragon_idle)

                    character.setOnAnimationEndListener { }
                    character.setImageResource(R.drawable.knight_idle)

                    (boss.drawable as AnimationDrawable).start()
                    (character.drawable as AnimationDrawable).start()

                    viewModel.visibilityChange()
                    viewModel.textChange(-1)

                    dialog.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in))
                    textView.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in))

                    Timer().schedule(2000){
                        viewModel.visibilityChange()
                        rollDice()
                    }
                }
                cancel()
            }
        }

    }

    private fun characterHurt() {
        diceClickable()

        fireBall.visibility = View.VISIBLE

        var bossFlag = false
        var characterFlag = false
        var fireBallFlag = false

        (boss.drawable as AnimationDrawable).stop()
        boss.setImageResource(R.drawable.dragon_attack)

        (character.drawable as AnimationDrawable).stop()
        character.setImageResource(R.drawable.knight_hurt)

        boss.setOnAnimationEndListener {
            bossFlag = true
            ((it as AnimationView).drawable as AnimationDrawable).stop()
        }

        character.setOnAnimationEndListener {
            characterFlag = true
            ((it as AnimationView).drawable as AnimationDrawable).stop()
        }

        fireBall.setOnAnimationEndListener {
            fireBallFlag = true
            ((it as AnimationView).drawable as AnimationDrawable).stop()
            it.visibility = View.INVISIBLE
        }

        (boss.drawable as AnimationDrawable).start()

        Timer().schedule(500){
            (fireBall.drawable as AnimationDrawable).start()
            sound(requireContext(),R.raw.fireball)
        }

        Timer().schedule(1500){
            (character.drawable as AnimationDrawable).start()
            sound(requireContext(),R.raw.knight_hurt)
        }

        Timer().schedule(3000, 100) {
            if (characterFlag && bossFlag && fireBallFlag) {

                val curHealth = viewModel.characterHealth.value!!.minus(1)

                viewModel.characterInjured()

                if(curHealth > 0) {

                    boss.setOnAnimationEndListener { }
                    boss.setImageResource(R.drawable.dragon_idle)

                    character.setOnAnimationEndListener { }
                    character.setImageResource(R.drawable.knight_idle)

                    fireBall.setOnAnimationEndListener { }

                    (boss.drawable as AnimationDrawable).start()
                    (character.drawable as AnimationDrawable).start()

                    viewModel.visibilityChange()
                    viewModel.textChange(-2)

                    dialog.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in))
                    textView.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in))

                    Timer().schedule(2000){
                        viewModel.visibilityChange()
                        rollDice()
                    }
                }
                cancel()
            }
        }
    }

    private var doneStartFlag = false

    override fun onStart() {
        super.onStart()
        if(!doneStartFlag){
            val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationEnd(animation: Animation?) {
                    Timer().schedule(3000) {
                        viewModel.visibilityChange()
                        rollDice()
                    }
                }

                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationStart(animation: Animation?) {}
            })
            dialog.startAnimation(anim)
            textView.startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.fade_in))
            doneStartFlag = true
        }
    }

    private fun rollDice() {
        answer = Random(System.currentTimeMillis()).nextInt(5).plus(1)
        val rawId = when (answer) {
            1 -> R.raw.dice1
            2 -> R.raw.dice2
            3 -> R.raw.dice3
            4 -> R.raw.dice4
            5 -> R.raw.dice5
            6 -> R.raw.dice6
            else -> error("Invalidate Number")
        }
        sound(requireContext(), rawId)
        diceClickable()
        Log.d("Fine","answer = $answer")
    }

    private var playerList = Vector<AutoReleasePlayer>()

    private fun sound(context : Context, id : Int){
        if(viewModel.soundswitch.value!!){
            playerList.addElement(AutoReleasePlayer(context,id))
        }
    }

    override fun onPause() {
        super.onPause()
        playerList.forEach {
            it.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        playerList.forEach {
            it.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playerList.forEach {
            it.release()
        }
    }

}
