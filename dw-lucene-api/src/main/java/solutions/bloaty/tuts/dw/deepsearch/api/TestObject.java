package solutions.bloaty.tuts.dw.deepsearch.api;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public interface TestObject {
    String getFoo();
    List<String> getList();
    ImmutableTestObject ooo();
}
