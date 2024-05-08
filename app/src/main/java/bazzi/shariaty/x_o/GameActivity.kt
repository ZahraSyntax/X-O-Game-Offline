package bazzi.shariaty.x_o

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import bazzi.shariaty.x_o.databinding.ActivityGameBinding
import bazzi.shariaty.x_o.databinding.ActivityMainBinding

class GameActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding : ActivityGameBinding

    private var gameModel :GameModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.btn0.setOnClickListener(this)
        binding.btn1.setOnClickListener(this)
        binding.btn2.setOnClickListener(this)
        binding.btn3.setOnClickListener(this)
        binding.btn4.setOnClickListener(this)
        binding.btn5.setOnClickListener(this)
        binding.btn6.setOnClickListener(this)
        binding.btn7.setOnClickListener(this)
        binding.btn8.setOnClickListener(this)
        binding.startGameBtn.setOnClickListener{
            startGame()
        }

        GameData.gameModel.observe(this){
            gameModel = it
            setUi()
        }

    }

    fun setUi() {
        gameModel?.apply {
            binding.btn0.text = fildPos[0]
            binding.btn1.text = fildPos[1]
            binding.btn2.text = fildPos[2]
            binding.btn3.text = fildPos[3]
            binding.btn4.text = fildPos[4]
            binding.btn5.text = fildPos[5]
            binding.btn6.text = fildPos[6]
            binding.btn7.text = fildPos[7]
            binding.btn8.text = fildPos[8]
            binding.startGameBtn.visibility = View.VISIBLE
            binding.gameStatusText.text =
                when(gameStatus){
                    GameStatus.CREATED -> {
                        binding.startGameBtn.visibility = View.INVISIBLE
                        "Game ID: " + gameId
                    }
                    GameStatus.JOINED -> {
                        "Click on Start Game"
                    }
                    GameStatus.INPROGRESS -> {
                        binding.startGameBtn.visibility = View.INVISIBLE
                        currentPlayer + " Turn"
                    }
                    GameStatus.FINISHED -> {
                        binding.startGameBtn.setText("Play again")
                        if(winner.isNotEmpty()) winner + " Won"
                        else "Draw"
                    }
                }

        }
    }

    fun startGame() {
        gameModel?.apply{
            updateGameData(
                GameModel(
                    gameId = gameId,
                    gameStatus = GameStatus.INPROGRESS
                )
            )
        }
    }

    fun updateGameData(model: GameModel) {
        GameData.saveGameModel(model)
    }

    fun checkForWinner(){
        val winningPos = arrayOf(
            intArrayOf(0,1,2),
            intArrayOf(3,4,5),
            intArrayOf(6,7,8),
            intArrayOf(0,3,6),
            intArrayOf(1,4,7),
            intArrayOf(2,5,8),
            intArrayOf(0,4,8),
            intArrayOf(2,4,6)
            )
        gameModel?.apply {
            for (i in winningPos){
                //012
                if (
                    fildPos[i[0]] == fildPos[i[1]] && fildPos[i[1]] == fildPos[i[2]] && fildPos[i[0]].isNotEmpty()
                ){
                    gameStatus = GameStatus.FINISHED
                    winner = fildPos[i[0]]
                }
            }
            if (fildPos.none(){
                it.isEmpty()
                }){
                gameStatus = GameStatus.FINISHED
            }
            updateGameData(this)
        }
    }

    override fun onClick(v: View?) {
        gameModel?.apply {
            if (gameStatus != GameStatus.INPROGRESS){
                Toast.makeText(applicationContext,"Game isn't started!",Toast.LENGTH_SHORT).show()
                return
            }

            //Game is in progress
            val clickedPos = (v?.tag as String).toInt()
            if (fildPos[clickedPos].isEmpty()){
                fildPos[clickedPos] = currentPlayer
                currentPlayer = if (currentPlayer == "X") "O" else "X"
                checkForWinner()
                updateGameData(this)
            }
        }
    }
}