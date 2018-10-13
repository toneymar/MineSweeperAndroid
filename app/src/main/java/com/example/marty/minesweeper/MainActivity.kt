package com.example.marty.minesweeper

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.marty.minesweeper.model.MineSweeperModel
import com.example.marty.minesweeper.ui.MineSweeperView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import android.support.design.widget.Snackbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnReset.setOnClickListener {
            btnToggle.isChecked = false
            mineSweeperView.restart()
        }
        mineSweeperView.setMines()
        mineSweeperView.setNums()
    }

    private fun MineSweeperView.restart(){
        MineSweeperModel.resetModel()
        mineSweeperView.setMines()
        mineSweeperView.setNums()
        mineSweeperView.gameEnd = false
        invalidate()
    }

    fun showMessage(msg : String){
        Snackbar.make(mineSweeperView, msg, Snackbar.LENGTH_INDEFINITE).setAction("Restart"){
            btnToggle.isChecked = false
            mineSweeperView.restart()
        }.show()
    }

}
