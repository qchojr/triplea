package games.strategy.internal.persistence.serializable;

import static com.google.common.base.Preconditions.checkNotNull;
import static games.strategy.engine.data.Matchers.equalToGameData;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;

import games.strategy.engine.data.GameData;
import games.strategy.engine.data.TestGameDataFactory;
import games.strategy.persistence.serializable.AbstractProxyTestCase;
import games.strategy.persistence.serializable.ProxyFactory;

public final class GameDataProxyAsProxyTest extends AbstractProxyTestCase<GameData> {
  public GameDataProxyAsProxyTest() {
    super(GameData.class);
  }

  @Override
  protected void assertPrincipalEquals(final GameData expected, final GameData actual) {
    checkNotNull(expected);
    checkNotNull(actual);

    assertThat(actual, is(equalToGameData(expected)));
  }

  @Override
  protected Collection<GameData> createPrincipals() {
    return Arrays.asList(TestGameDataFactory.newValidGameData());
  }

  @Override
  protected Collection<ProxyFactory> getProxyFactories() {
    return TestProxyFactoryCollectionBuilder.forGameData().build();
  }
}
