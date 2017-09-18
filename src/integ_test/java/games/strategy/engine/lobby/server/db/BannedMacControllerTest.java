package games.strategy.engine.lobby.server.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.Instant;

import org.junit.Test;

import games.strategy.util.MD5Crypt;
import games.strategy.util.Tuple;
import games.strategy.util.Util;

public class BannedMacControllerTest {

  private final BannedMacController controller = spy(new BannedMacController());

  @Test
  public void testBanMacForever() {
    final String hashedMac = banMac(null);
    final Tuple<Boolean, Timestamp> macDetails = controller.isMacBanned(hashedMac);
    assertTrue(macDetails.getFirst());
    assertNull(macDetails.getSecond());
  }

  @Test
  public void testBanMac() {
    final Instant banUntil = Instant.now();
    when(controller.now()).thenReturn(Instant.now().minusSeconds(1L));
    final String hashedMac = banMac(banUntil);
    final Tuple<Boolean, Timestamp> macDetails = controller.isMacBanned(hashedMac);
    assertTrue(macDetails.getFirst());
    assertEquals(banUntil, macDetails.getSecond().toInstant());
    when(controller.now()).thenCallRealMethod();
    final Tuple<Boolean, Timestamp> macDetails2 = controller.isMacBanned(hashedMac);
    assertFalse(macDetails2.getFirst());
    assertEquals(banUntil, macDetails2.getSecond().toInstant());
  }

  @Test
  public void testBanMacInThePast() {
    final Instant banUntil = Instant.now().minusSeconds(10L);
    final String hashedMac = banMac(banUntil);
    final Tuple<Boolean, Timestamp> macDetails = controller.isMacBanned(hashedMac);
    assertFalse(macDetails.getFirst());
    assertEquals(banUntil, macDetails.getSecond().toInstant());
  }

  @Test
  public void testBanMacUpdate() {
    final String hashedMac = banMac(null);
    final Tuple<Boolean, Timestamp> macDetails = controller.isMacBanned(hashedMac);
    assertTrue(macDetails.getFirst());
    assertNull(macDetails.getSecond());
    final Instant banTill = Instant.now().plusSeconds(100L);
    controller.addBannedMac(hashedMac, banTill);
    final Tuple<Boolean, Timestamp> macDetails2 = controller.isMacBanned(hashedMac);
    assertTrue(macDetails2.getFirst());
    assertEquals(banTill, macDetails2.getSecond().toInstant());
  }

  private String banMac(final Instant length) {
    final String hashedMac = MD5Crypt.crypt(Util.createUniqueTimeStamp(), "MH");
    controller.addBannedMac(hashedMac, length);
    return hashedMac;
  }
}