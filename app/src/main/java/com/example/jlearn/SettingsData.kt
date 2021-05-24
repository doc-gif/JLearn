package com.example.jlearn

import io.realm.RealmObject

open class SettingsData: RealmObject() {
    var wordCJ = false
    var wordJC = false
    var exCJ = false
    var exJC = false
    var numF = 0
    var numE = 0
    var searchCorrect = 0
    var searchCheck = 0
    var howManyQue = 0
    var howQue = 0
    var oneMoreFlag = false
    var howManyCorrect = 0
    var howManyIncorrect = 0
    var totalQue = 0
}