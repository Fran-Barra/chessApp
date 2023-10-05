package movement

import board.Board
import piece.Piece
import pieceEatingRuler.PieceEatingRuler
import player.Player
import vector.Vector

interface SpecialMovementController {
    fun getEventNotification(event: Event, piece: Piece) : SpecialMovementsPieces
    fun removePiece(piece: Piece) : SpecialMovementsPieces
    fun checkMovement(eatingRuler: PieceEatingRuler, player: Player, actual: Vector,
                      destination: Vector, board: Board
    ) : SpecialMovement

}