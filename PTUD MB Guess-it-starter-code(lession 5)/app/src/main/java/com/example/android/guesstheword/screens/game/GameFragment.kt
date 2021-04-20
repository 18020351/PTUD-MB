package com.example.android.guesstheword.screens.game

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    private lateinit var viewModel : GameViewModel
    // The current word


    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )

        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        binding.gameViewModel = viewModel
        binding.setLifecycleOwner(this)
        //viewModel.word.observe(this, Observer{ newWord->
          //  binding.wordText.text = newWord })

        //viewModel.score.observe(this, Observer { newScore ->
         //   binding.scoreText.text = newScore.toString() })

       // viewModel.currentTime.observe(this, Observer { newTime ->
         //   binding.timerText.text = DateUtils.formatElapsedTime(newTime)

        //})

        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer { isFinished ->
            if(isFinished){
                val currentScore = viewModel.score.value?:0
                val action = GameFragmentDirections.actionGameToScore(currentScore)
                findNavController(this).navigate(action)
                viewModel.onGameFinishComplete()
            }
        })

        return binding.root

    }

    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService<Vibrator>()

        buzzer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
            }
        }
    }
}

