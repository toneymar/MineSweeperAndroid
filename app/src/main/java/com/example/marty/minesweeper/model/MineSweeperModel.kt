package com.example.marty.minesweeper.model

object MineSweeperModel {

    data class Field (var ismine: Boolean, var mAround: Int, var uncvd: Boolean, var flagged: Boolean)

    private var model = Array(5) {
        Array(5) {
            Field(false, 0, false, false)
        }
    }

    var flags = 2

    private fun setFieldContent(x: Int, y: Int, ismine: Boolean, mAround: Int, uncvd: Boolean, flagged: Boolean){
        model[x][y].ismine = ismine
        model[x][y].mAround = mAround
        model[x][y].uncvd = uncvd
        model[x][y].flagged = flagged
    }

    fun uncover(x: Int, y: Int){
        model[x][y].uncvd = true
    }

    fun setMine(x: Int, y: Int) {
        model[x][y].ismine = true
    }

    fun placeFlag(x: Int, y: Int) {
        model[x][y].flagged = true
        flags -= 1
    }

    fun removeFlag(x: Int, y: Int) {
        model[x][y].flagged = false
    }

    fun checkMine (x: Int, y: Int) : Boolean {
        return model[x][y].ismine
    }

    fun numAround (x: Int, y: Int) : String {
        return model[x][y].mAround.toString()
    }

    fun addNum (x: Int, y: Int) {
        if (x in 0..4 && y in 0..4 && !checkMine(x, y)) {
            model[x][y].mAround++
        }
    }

    fun checkUncovered (x: Int, y: Int) : Boolean {
        return model[x][y].uncvd
    }

    fun isFlagged (x: Int, y: Int) : Boolean {
        return model[x][y].flagged
    }

    fun resetModel() {
        for (i in 0..4) {
            for (j in 0..4){
                setFieldContent(i, j, false, 0, false, false)
            }
        }
        flags = 2
    }
}