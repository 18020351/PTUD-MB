package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlin.concurrent.timer
import androidx.lifecycle.MutableLiveData as MutableLiveData

private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

class GameViewModel : ViewModel(){
    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    companion object{
        private const val DONE = 0L
        private const val COUNTDOWN_PANIC_SECONDS = 10L
        private const val ONE_SECOND = 1000L
        private const val COUNTDOWN_TIME = 60000L
    }
    private val timer: CountDownTimer
    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime
    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }


    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
    get() = _word
    // The current score
    private val _score = MutableLiveData<Int>()
    val score : LiveData<Int>
    get() = _score
    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    private val _eventBuzz = MutableLiveData<BuzzType>()
    val eventBuzz: LiveData<BuzzType>
        get() = _eventBuzz

    init{
        //Log.i("GameViewModel","GameViewModel created!")
        //_eventGameFinish.value = false
        resetList()
        nextWord()
        _score.value = 0

         timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND){
            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished / ONE_SECOND)
            }

            override fun onFinish() {
                _currentTime.value = DONE
                _eventGameFinish.value = true
            }
            //DateUtils.formatElapsedTime()
        }
        timer.start()

    }

    override fun onCleared() {
        super.onCleared()
        //Log.i("GameViewModel", "GameViewModel destroyed!")
        timer.cancel()
    }
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }
    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
           // gameFinished()
           // _eventGameFinish.value = true
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }
    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        nextWord()
    }
    fun onGameFinishComplete(){
        _eventGameFinish.value = false
    }

}
