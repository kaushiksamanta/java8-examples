package no.njm.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

/**
 * Each lambda corresponds to a given type, specified by an interface.
 * A functional interface must contain exactly one abstract method declaration.
 */
public class Lambdas {

    private static final Logger log = LoggerFactory.getLogger(Lambdas.class);

    public static void main(String[] args) {
        Lambdas lambdas = new Lambdas();
        lambdas.basicLambda();
        lambdas.functionalInterface();
        lambdas.methodReference();
        lambdas.constructorReference();
    }

    private void basicLambda() {
        List<String> list = new ArrayList<>();

        // Implementing interface Comparator
        list.sort((String a, String b) -> {
            return a.compareTo(b);
        });

        // Skipping {} and "return" for one-line methods
        list.sort((String a, String b) -> a.compareTo(b));

        // Skipping parameter types
        list.sort((a, b) -> a.compareTo(b));
    }

    private void functionalInterface() {
        // Omitting () around single input parameter
        Converter<String, Integer> converter = from -> Integer.valueOf(from);
        log.debug("Converted to {}", converter.convert("100"));
    }

    /**
     * Method references enables referencing an existing method by name instead
     * of using a lambda to call that method.
     */
    private void methodReference() {
        // Passing references to methods or constructors using the :: keyword
        Converter<String, Integer> intConverter = Integer::valueOf;
        log.debug("Converted to {}", intConverter.convert("100"));

        // Referencing instance method
        Word word = new Word();

        Converter<String, String> firstLetter = word::firstLetter;
        log.debug("First letter is {}", firstLetter.convert("string"));
    }

    /**
     * ArrayList::new equals () -> new ArrayList<>().
     * <p>
     * Methods and constuctors can be overloaded so ArrayList::new could refer to any of its three
     * constructors.  The method it resolves to depends on which functional interface it's being used for.
     */
    private void constructorReference() {
        //The compiler chooses the right constructor by matching the function interface signature
        PersonFactory<Person> personFactory = Person::new;
        Person person = personFactory.create("Firstname", "Lastname");
        log.debug("Person has name {} {}", person.firstName, person.lastName);

        // The functional interface Supplier the method get() that returns an object
        HashSet<String> filledCollection = initCollection(HashSet::new, "First", "Second");
        for (String element : filledCollection) {
            log.debug("Collection element is {}", element);
        }
    }

    private <T, P extends Collection<T>> P initCollection(Supplier<P> collectionFactory, T... elements) {
        P collection = collectionFactory.get();
        for (T element : elements)
            collection.add(element);
        return collection;
    }
}
