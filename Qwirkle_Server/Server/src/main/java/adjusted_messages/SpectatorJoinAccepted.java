package adjusted_messages;

import java.util.Objects;

import de.upb.swtpra1819interface.messages.Message;
import game_elements.Game;



public class SpectatorJoinAccepted
/*    */   extends Message
/*    */ {
/*    */   public static final int uniqueID = 305;
/*    */   private Game game;
/*    */   
/*    */   public SpectatorJoinAccepted(Game game)
/*    */   {
/* 20 */     super(305);
/* 21 */     this.game = game;
/*    */   }
/*    */   
/*    */   public Game getGame() {
/* 25 */     return this.game;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
    if (this == o) {
      return true;
    }
      if (!(o instanceof SpectatorJoinAccepted)) {
      return false;
     }
    SpectatorJoinAccepted that = (SpectatorJoinAccepted)o;
    return Objects.equals(this.game, that.game);
  }
}
