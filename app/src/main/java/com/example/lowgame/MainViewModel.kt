package com.example.lowgame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _soundswitch = MutableLiveData(true)
    val soundswitch: LiveData<Boolean> get() = _soundswitch

    fun soundSwitch() {
        _soundswitch.value = _soundswitch.value!!.xor(true)
    }

    private val _startSign = MutableLiveData(false)
    val startSign: LiveData<Boolean> get() = _startSign

    fun startButtonClicked() {
        _startSign.value = true
    }

    val endSign = MutableLiveData(0)

    fun endChange(num : Int) {
        endSign.postValue(num)
    }

    val characterHealth = MutableLiveData(3)

    fun characterInjured() {
        characterHealth.postValue(characterHealth.value!!.minus(1))
    }

    val bossHealth = MutableLiveData(8)

    fun bossInjured() {
        bossHealth.postValue(bossHealth.value!!.minus(1))
    }

    val textNumber = MutableLiveData(0)
    private var lastValue = 0

    fun textChange(flag:Int) {
        if (flag == 0){
            lastValue = lastValue.plus(1)
            textNumber.postValue(lastValue)
        }
        else {
            textNumber.postValue(flag)
        }
    }

    val dialogDirection = MutableLiveData(false)

    fun dialogDirectionChange() {
        dialogDirection.postValue(dialogDirection.value!!.xor(true))
    }

    val visibility = MutableLiveData(true)

    fun visibilityChange() {
        visibility.postValue(visibility.value!!.xor(true))
    }

    val restartFlag = MutableLiveData(false)

    fun restart(){
        _startSign.postValue(false)
        endSign.postValue(0)
        characterHealth.postValue(3)
        bossHealth.postValue(8)
        textNumber.postValue(0)
        dialogDirection.postValue(false)
        visibility.postValue(true)
        restartFlag.postValue(true)
        lastValue = 0
    }

}