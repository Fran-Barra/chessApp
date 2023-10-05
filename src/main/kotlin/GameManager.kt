import board.Board
import movement.MovementStrategy
import movement.SpecialMovementController
import piece.Piece
import pieceEatingRuler.PieceEatingRuler
import player.Player
import turnsController.TurnsController
import vector.Vector
import winningConditionStrategy.WinningConditionStrategy

class GameManager (private val board: Board,
                   private val actualPlayer: Player,
                   private val turnsController: TurnsController,
                   private val pieceEatingRuler: PieceEatingRuler,
                   private val pieceMovementStrategy: Map<Int, MovementStrategy>,
                   private val specialMovementsController: SpecialMovementController,
                   private val winningCondition: WinningConditionStrategy
    ){

    fun makeMovement(player: Player, piece: Piece, position: Vector): Pair<Boolean, GameManager> {
        throw NotImplementedError()
    }
}