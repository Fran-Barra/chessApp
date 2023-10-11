import board.Board
import movement.MovementStrategy
import movement.SpecialMovementController
import piece.Piece
import pieceEatingRuler.PieceEatingRuler
import player.Player
import turnsController.TurnsController
import vector.Vector
import winningConditionStrategy.WinningConditionStrategy

class Game (private val board: Board,
            private val actualPlayer: Player,
            private val turnsController: TurnsController,
            private val pieceEatingRuler: PieceEatingRuler,
            private val pieceMovementStrategy: Map<Int, MovementStrategy>,
            private val specialMovementsController: SpecialMovementController,
            private val winningCondition: WinningConditionStrategy
    ){

    fun makeMovement(player: Player, origin: Vector, destination: Vector): Result<Game> {
        if (player != actualPlayer) return Result.failure(Exception("Is not the player turn"))

        val pieceR: Result<Piece> = board.getPieceInPosition(origin)
        if (pieceR.isFailure) return exceptionHandler(pieceR, "board", "get piece in position")
        val piece: Piece = pieceR.getOrNull()!!

        if (!player.playerControlColor(piece.getPieceColor()))
            Result.failure<Pair<Boolean, Game>>(Exception("This color is not controlled by the actual player"))

        //TODO("Integrate special movements")
        //specialMovementsController.checkMovement(pieceEatingRuler, player, origin, destination, board)

        val movementStrategy: MovementStrategy = pieceMovementStrategy[piece.getPieceType()]
            ?: return Result.failure(Exception("Piece type don't match with the rules of the game"))

        if (!movementStrategy.checkMovement(pieceEatingRuler, player, origin, destination, board))
            return Result.failure(Exception("Movement is not valid for this piece"))

        val newBoard: Board = board.movePiece(piece, destination)

        //TODO: check winning conditions

        val getNextPlayer: Result<Pair<Player, TurnsController>> = turnsController.getNextPlayerTurn()

        if (getNextPlayer.isFailure)
            return exceptionHandler(getNextPlayer, "turn controller", "get next player")
        val nextPlayer: Player = getNextPlayer.getOrNull()?.first!!
        val newTurnsControllerStatus: TurnsController = getNextPlayer.getOrNull()?.second!!
        //TODO: special movement strategy new is needed
        return Result.success(Game(newBoard, nextPlayer, newTurnsControllerStatus, pieceEatingRuler,
            pieceMovementStrategy, specialMovementsController, winningCondition))
    }

    /**
     * Used when result fails, if it fails it will return the exception thrown by the given result.
     * In case that the given result does not have an error (should not happen) this method will create
     * a generic exception, describing the object and the action.
     */
    private fun <T>exceptionHandler(result: Result<T>, obj: String, action: String): Result<Game> {
        val exception: Throwable? = result.exceptionOrNull()
        return  Result.failure(exception ?:
        Exception("A not specified error happened within the $obj performing the action: $action")
        )
    }

}