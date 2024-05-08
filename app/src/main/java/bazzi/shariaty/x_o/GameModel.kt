package bazzi.shariaty.x_o

import kotlin.random.Random

data class GameModel (
    var gameId : String = "-1",
    var fildPos : MutableList<String> = mutableListOf("","","","","","","","",""),
    var winner : String = "",
    var gameStatus : GameStatus = GameStatus.CREATED,
    var currentPlayer : String = (arrayOf("X","O"))[Random.nextInt(2)]
)
enum class GameStatus {
    CREATED,
    JOINED,
    INPROGRESS,
    FINISHED
}