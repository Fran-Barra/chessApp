package movement.movementStrategy

import board.Board
import movement.MovementStrategy
import piece.Piece
import pieceEatingRuler.PieceEatingRuler
import player.Player
import vector.Vector
import kotlin.math.pow
import kotlin.math.sqrt

class DistanceLimitMovement(val maxDistance: Int): MovementStrategy {
    override fun checkMovement(pieceEatingRuler: PieceEatingRuler, player: Player, actual: Vector,
                               destination: Vector, board: Board): Boolean {
        if (!board.positionExists(destination) || !board.positionExists(actual)) return false

        if (sqrt((actual.x - destination.x).toFloat().pow(2) +
                    (actual.y - destination.y).toFloat().pow(2))
            > maxDistance) return false

        //TODO("move eating logic to gameManager")
        val pieceInDestination: Result<Piece> = board.getPieceInPosition(destination)
        if (!pieceInDestination.isSuccess) return true
        return pieceEatingRuler.canPieceEatPiece(board.getPieceInPosition(actual).getOrThrow(),
            pieceInDestination.getOrThrow())
    }
}