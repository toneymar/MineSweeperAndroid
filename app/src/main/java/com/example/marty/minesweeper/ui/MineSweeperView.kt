package com.example.marty.minesweeper.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.marty.minesweeper.MainActivity
import com.example.marty.minesweeper.R
import com.example.marty.minesweeper.model.MineSweeperModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MineSweeperView(context: Context?, attrs: AttributeSet?) : View(context, attrs){

    private val paintBackground = Paint()
    private val paintTile = Paint()
    private val paintLine = Paint()
    private val paintText = Paint()

    private var bitmapFlag = BitmapFactory.decodeResource(resources, R.drawable.flag)
    private var bitmapMine = BitmapFactory.decodeResource(resources, R.drawable.mine)

    init{

        paintBackground.color = Color.WHITE
        paintBackground.strokeWidth = 10F
        paintBackground.style = Paint.Style.FILL

        paintTile.color = Color.GRAY
        paintTile.strokeWidth = 10F
        paintTile.style = Paint.Style.FILL

        paintLine.color = Color.BLACK
        paintLine.strokeWidth = 10F
        paintLine.style = Paint.Style.STROKE

        paintText.color = Color.BLUE
        paintText.textSize = 200F
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        bitmapFlag = Bitmap.createScaledBitmap(bitmapFlag, width/5 - 15, height/5 - 15, false)
        bitmapMine = Bitmap.createScaledBitmap(bitmapMine, width/5 - 15, height/5 - 15, false)
    }

    private fun IntRange.random() = Random().nextInt((endInclusive + 1) - start) +  start

    fun setMines() {

        val rands = ArrayList<Pair<Int, Int>>()

        do{
            for (i in 0..2) {
                rands.add(i, Pair((0..4).random(),(0..4).random()))
            }
        } while (rands[0] == rands[1] || rands[1] == rands[2] || rands[2] == rands[0])

        for (i in 0..2) {
            MineSweeperModel.setMine(rands[i].first, rands[i].second)
        }
    }

    fun setNums() {
        for (i in 0..4){
            for (j in 0..4){
                if (MineSweeperModel.checkMine(i, j)){
                    MineSweeperModel.addNum(i - 1, j - 1)
                    MineSweeperModel.addNum(i, j - 1)
                    MineSweeperModel.addNum(i + 1, j - 1)
                    MineSweeperModel.addNum(i + 1, j)
                    MineSweeperModel.addNum(i + 1, j + 1)
                    MineSweeperModel.addNum(i, j + 1)
                    MineSweeperModel.addNum(i - 1, j + 1)
                    MineSweeperModel.addNum(i - 1, j)
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //Paint the background
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), paintBackground)
        drawGameArea(canvas)
        drawTiles(canvas)
    }

    private fun drawGameArea(canvas: Canvas){

        //Border
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), paintLine)

        //Grid
        //Horizontal
        canvas.drawLine(0F, (height / 5).toFloat(), width.toFloat(), (height / 5).toFloat(), paintLine)
        canvas.drawLine(0F, ((2 * height) / 5).toFloat(), width.toFloat(), ((2 * height) / 5).toFloat(), paintLine)
        canvas.drawLine(0F, ((3 * height) / 5).toFloat(), width.toFloat(), ((3 * height) / 5).toFloat(), paintLine)
        canvas.drawLine(0F, ((4 * height) / 5).toFloat(), width.toFloat(), ((4 * height) / 5).toFloat(), paintLine)
        //Vertical
        canvas.drawLine((width / 5).toFloat(), 0F, (width / 5).toFloat(), height.toFloat(), paintLine)
        canvas.drawLine((2 * width / 5).toFloat(), 0F, (2 * width / 5).toFloat(), height.toFloat(), paintLine)
        canvas.drawLine((3 * width / 5).toFloat(), 0F, (3 * width / 5).toFloat(), height.toFloat(), paintLine)
        canvas.drawLine((4 * width / 5).toFloat(), 0F, (4 * width / 5).toFloat(), height.toFloat(), paintLine)
    }

    private fun drawTiles (canvas: Canvas) {

        for (i in 0..4) {
            for (j in 0..4) {
                val centerX = (i * width / 5 + width/ 10)
                val centerY = (j * height / 5 + height / 10)

                if (!MineSweeperModel.checkMine(i, j))
                    canvas.drawText(MineSweeperModel.numAround(i, j), (centerX - 55).toFloat(), (centerY + 60).toFloat(), paintText)

                if (MineSweeperModel.checkMine(i, j))
                    canvas.drawBitmap(bitmapMine, (centerX - 100).toFloat(), (centerY - 110).toFloat(), null)

                if (!MineSweeperModel.checkUncovered(i, j))
                    canvas.drawRect((centerX - 100).toFloat(), (centerY - 110).toFloat(), (centerX + 100).toFloat(), (centerY + 110).toFloat(), paintTile)

                if (MineSweeperModel.isFlagged(i, j))
                    canvas.drawBitmap(bitmapFlag, (centerX - 100).toFloat(), (centerY - 110).toFloat(), null)
            }
        }

    }

    private fun flagChecked() : Boolean {
        return (context as MainActivity).btnToggle.isChecked
    }

    private fun onWin() {
        (context as MainActivity).showMessage("YOU HAVE WON! :)")
        gameEnd = true
    }

    private fun onLose() {
        for (i in 0..4) {
            for (j in 0..4) {
                if (MineSweeperModel.checkMine(i, j)) {
                    MineSweeperModel.uncover(i, j)
                    MineSweeperModel.removeFlag(i,j)
                }
            }
        }
        (context as MainActivity).showMessage("YOU HAVE LOST")
        gameEnd = true
    }

    var gameEnd = false

    override fun onTouchEvent(event: MotionEvent): Boolean {

        val tX = event.x.toInt() / (width / 5)
        val tY = event.y.toInt() / (height / 5)

        if (tX < 5 && tY < 5 && !MineSweeperModel.checkUncovered(tX, tY) && !gameEnd){
            if (!flagChecked()) {
                if (!MineSweeperModel.checkMine(tX, tY))
                    MineSweeperModel.uncover(tX, tY)
                else
                    onLose()
            }
            if (flagChecked()){
                if(!MineSweeperModel.checkMine(tX,tY))
                    onLose()
                else if (MineSweeperModel.flags == 0){
                    MineSweeperModel.placeFlag(tX, tY)
                    onWin()
                }
                else
                    MineSweeperModel.placeFlag(tX, tY)
            }
            invalidate()
        }
        return super.onTouchEvent(event)
    }
}