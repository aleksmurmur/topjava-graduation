package ru.javaops.bootjava.web;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.javaops.bootjava.util.JsonUtil;

import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Factory for creating test matchers.
 * <p>
 * Comparing actual and expected objects via AssertJ
 * Support converting json MvcResult to objects for comparation.
 */
public class MatcherFactory {

    public static <T, S> Matcher<T, S> usingAssertions(Class<T> clazz, Class<S> dto, BiConsumer<S, T> assertion, BiConsumer<Iterable<S>, Iterable<T>> iterableAssertion) {
        return new Matcher<>(clazz, dto, assertion, iterableAssertion);
    }

    public static <T, S> Matcher<T, S> usingEqualsComparator(Class<T> clazz, Class<S> dto) {
        return usingAssertions(clazz, dto,
                (a, e) -> assertThat(a).isEqualTo(e),
                (a, e) -> assertThat(a).isEqualTo(e));
    }

    public static <T, S> Matcher<T, S> usingIgnoringFieldsComparator(Class<T> clazz, Class<S> dto, String... fieldsToIgnore) {
        return usingAssertions(clazz, dto,
                (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields(fieldsToIgnore).isEqualTo(e),
                (a, e) -> assertThat(a).usingRecursiveFieldByFieldElementComparatorIgnoringFields(fieldsToIgnore).isEqualTo(e));
    }

    public static class Matcher<T, S> {
        private final Class<T> clazz;
        private final Class<S> dto;
        private final BiConsumer<S, T> assertion;
        private final BiConsumer<Iterable<S>, Iterable<T>> iterableAssertion;

        private Matcher(Class<T> clazz, Class<S> dto, BiConsumer<S, T> assertion, BiConsumer<Iterable<S>, Iterable<T>> iterableAssertion) {
            this.clazz = clazz;
            this.dto = dto;
            this.assertion = assertion;
            this.iterableAssertion = iterableAssertion;
        }

        public void assertMatch(S actual, T expected) {
            assertion.accept(actual, expected);
        }

        @SafeVarargs
        public final void assertMatch(Iterable<S> actual, T... expected) {
            assertMatch(actual, List.of(expected));
        }

        public void assertMatch(Iterable<S> actual, Iterable<T> expected) {
            iterableAssertion.accept(actual, expected);
        }

        public ResultMatcher contentJson(T expected) {
            return result -> assertMatch(JsonUtil.readValue(getContent(result), dto), expected);
        }

        @SafeVarargs
        public final ResultMatcher contentJson(T... expected) {
            return contentJson(List.of(expected));
        }

        public ResultMatcher contentJson(Iterable<T> expected) {
            return result -> assertMatch(JsonUtil.readValues(getContent(result), dto), expected);
        }

        public S readFromJson(ResultActions action) throws UnsupportedEncodingException {
            return JsonUtil.readValue(getContent(action.andReturn()), dto);
        }

        private static String getContent(MvcResult result) throws UnsupportedEncodingException {
            return result.getResponse().getContentAsString();
        }
    }
}
